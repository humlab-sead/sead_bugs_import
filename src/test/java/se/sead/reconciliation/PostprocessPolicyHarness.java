package se.sead.reconciliation;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class PostprocessPolicyHarness {

	private static final Path POLICY_DIRECTORY = Paths.get("doc", "reconciliation_policies");

	public PostprocessResult execute(String policyName, String postprocessName, List<Map<String, Object>> sourceRows) throws IOException {
		Map<String, Object> policy = loadPolicy(policyName);
		List<Object> postprocessItems = listValue(policy.get("postprocess"), policyName + ".postprocess");
		for (Object postprocessValue : postprocessItems) {
			Map<String, Object> postprocess = mapValue(postprocessValue, postprocessName);
			if (postprocessName.equals(stringValue(postprocess.get("name"), postprocessName + ".name"))) {
				return executePostprocess(postprocess, sourceRows);
			}
		}
		throw new IllegalArgumentException("No postprocess named '" + postprocessName + "' in policy '" + policyName + "'");
	}

	private PostprocessResult executePostprocess(Map<String, Object> postprocess, List<Map<String, Object>> sourceRows) {
		String mode = stringValue(postprocess.get("mode"), "postprocess.mode");
		if (!"pair_merge".equals(mode)) {
			throw new IllegalArgumentException("Unsupported postprocess mode: " + mode);
		}

		PostprocessResult result = new PostprocessResult();
		result.retainedRow = stringValue(postprocess.get("retain_row"), "postprocess.retain_row");

		List<List<Integer>> partitions = partitionRows(postprocess, sourceRows);
		Map<String, Object> pairRules = mapValue(postprocess.get("pair_rules"), "postprocess.pair_rules");
		String leftExpression = stringValue(pairRules.get("left"), "postprocess.pair_rules.left");
		String rightExpression = stringValue(pairRules.get("right"), "postprocess.pair_rules.right");
		boolean includeSingletons = Boolean.TRUE.equals(pairRules.get("include_singletons"));

		for (List<Integer> rowIndexes : partitions) {
			List<Integer> leftIndexes = matchingIndexes(sourceRows, rowIndexes, leftExpression);
			List<Integer> rightIndexes = matchingIndexes(sourceRows, rowIndexes, rightExpression);
			if (leftIndexes.size() > 1 || rightIndexes.size() > 1) {
				Map<String, Object> onConflict = mapValue(postprocess.get("on_conflict"), "postprocess.on_conflict");
				result.emitCodes.add(stringValue(onConflict.get("code"), "postprocess.on_conflict.code"));
				continue;
			}
			if (leftIndexes.size() == 1 && rightIndexes.size() == 1) {
				applyRangeResult(postprocess, sourceRows, result, leftIndexes.get(0), rightIndexes.get(0));
			} else if (includeSingletons && leftIndexes.size() == 1 && rightIndexes.isEmpty()) {
				applyRangeResult(postprocess, sourceRows, result, leftIndexes.get(0), null);
			} else if (includeSingletons && leftIndexes.isEmpty() && rightIndexes.size() == 1) {
				applyRangeResult(postprocess, sourceRows, result, rightIndexes.get(0), null);
			}
		}
		return result;
	}

	private void applyRangeResult(Map<String, Object> postprocess, List<Map<String, Object>> sourceRows, PostprocessResult result, Integer retainedIndex, Integer pairedIndex) {
		result.retainedRowIndexes.add(retainedIndex);
		Map<String, Object> retainedRow = sourceRows.get(retainedIndex.intValue());
		Map<String, Object> pairedRow = pairedIndex == null ? null : sourceRows.get(pairedIndex.intValue());
		Map<String, Object> rangeResult = new LinkedHashMap<String, Object>();
		applyActions(listValue(postprocess.get("actions"), "postprocess.actions"), retainedRow, pairedRow, result.updatedFields, rangeResult);
		rangeResult.put("retained_calendar_code", retainedRow.get("CalendarCODE"));
		rangeResult.put("paired_calendar_code", pairedRow == null ? null : pairedRow.get("CalendarCODE"));
		result.postprocessResults.add(rangeResult);
		if (result.postprocessResults.size() == 1) {
			result.postprocessResult.clear();
			result.postprocessResult.putAll(rangeResult);
		}
	}

	private List<List<Integer>> partitionRows(Map<String, Object> postprocess, List<Map<String, Object>> sourceRows) {
		List<String> groupByExpressions = new ArrayList<String>();
		collectExpressions(groupByExpressions, postprocess.get("group_by"), "postprocess.group_by");
		List<String> partitionExpressions = new ArrayList<String>();
		collectExpressions(partitionExpressions, postprocess.get("partition_by"), "postprocess.partition_by");

		if (partitionExpressions.contains("source.DatingMethod")
				&& partitionExpressions.contains("normalized.note_group")
				&& partitionExpressions.contains("normalized.uncertainty_family")) {
			return partitionDatesCalendarRows(sourceRows, groupByExpressions);
		}

		Map<String, List<Integer>> partitions = new LinkedHashMap<String, List<Integer>>();
		List<String> expressions = new ArrayList<String>();
		expressions.addAll(groupByExpressions);
		expressions.addAll(partitionExpressions);
		for (int i = 0; i < sourceRows.size(); i++) {
			String key = buildPartitionKey(sourceRows.get(i), expressions);
			if (!partitions.containsKey(key)) {
				partitions.put(key, new ArrayList<Integer>());
			}
			partitions.get(key).add(Integer.valueOf(i));
		}
		return new ArrayList<List<Integer>>(partitions.values());
	}

	private List<List<Integer>> partitionDatesCalendarRows(List<Map<String, Object>> sourceRows, List<String> groupByExpressions) {
		Map<String, List<Integer>> groupedRows = new LinkedHashMap<String, List<Integer>>();
		for (int i = 0; i < sourceRows.size(); i++) {
			String key = buildPartitionKey(sourceRows.get(i), groupByExpressions);
			if (!groupedRows.containsKey(key)) {
				groupedRows.put(key, new ArrayList<Integer>());
			}
			groupedRows.get(key).add(Integer.valueOf(i));
		}

		List<List<Integer>> partitions = new ArrayList<List<Integer>>();
		for (List<Integer> groupIndexes : groupedRows.values()) {
			LinkedHashSet<String> repeatedNotes = repeatedNonEmptyNotes(sourceRows, groupIndexes);
			Map<String, List<Integer>> datingMethodGroups = new HashMap<String, List<Integer>>();
			datingMethodGroups.put("", new ArrayList<Integer>());
			for (Integer rowIndex : groupIndexes) {
				String datingMethod = stringOrEmpty(sourceRows.get(rowIndex.intValue()).get("DatingMethod"));
				if (!datingMethodGroups.containsKey(datingMethod)) {
					datingMethodGroups.put(datingMethod, new ArrayList<Integer>());
				}
				datingMethodGroups.get(datingMethod).add(rowIndex);
			}

			for (Map.Entry<String, List<Integer>> datingMethodEntry : datingMethodGroups.entrySet()) {
				Map<String, List<Integer>> noteGroups = new HashMap<String, List<Integer>>();
				for (Integer rowIndex : datingMethodEntry.getValue()) {
					String note = noteGroup(sourceRows.get(rowIndex.intValue()), repeatedNotes);
					if (!noteGroups.containsKey(note)) {
						noteGroups.put(note, new ArrayList<Integer>());
					}
					noteGroups.get(note).add(rowIndex);
				}

				for (Map.Entry<String, List<Integer>> noteEntry : noteGroups.entrySet()) {
					List<Integer> caIndexes = new ArrayList<Integer>();
					List<Integer> standardIndexes = new ArrayList<Integer>();
					for (Integer rowIndex : noteEntry.getValue()) {
						if ("ca".equals(deriveUncertaintyFamily(sourceRows.get(rowIndex.intValue())))) {
							caIndexes.add(rowIndex);
						} else {
							standardIndexes.add(rowIndex);
						}
					}
					if (!caIndexes.isEmpty()) {
						partitions.add(caIndexes);
					}
					if (!standardIndexes.isEmpty()) {
						partitions.add(standardIndexes);
					}
				}
			}
		}
		return partitions;
	}

	private LinkedHashSet<String> repeatedNonEmptyNotes(List<Map<String, Object>> sourceRows, List<Integer> rowIndexes) {
		Map<String, Integer> counts = new LinkedHashMap<String, Integer>();
		for (Integer rowIndex : rowIndexes) {
			String note = stringOrEmpty(sourceRows.get(rowIndex.intValue()).get("Notes"));
			if (!note.isEmpty()) {
				Integer count = counts.get(note);
				counts.put(note, Integer.valueOf(count == null ? 1 : count.intValue() + 1));
			}
		}
		LinkedHashSet<String> repeated = new LinkedHashSet<String>();
		for (Map.Entry<String, Integer> entry : counts.entrySet()) {
			if (entry.getValue().intValue() > 1) {
				repeated.add(entry.getKey());
			}
		}
		return repeated;
	}

	private String noteGroup(Map<String, Object> sourceRow, LinkedHashSet<String> repeatedNotes) {
		String note = stringOrEmpty(sourceRow.get("Notes"));
		return repeatedNotes.contains(note) ? note : "";
	}

	private void collectExpressions(List<String> expressions, Object value, String context) {
		List<Object> items = listValue(value, context);
		for (int i = 0; i < items.size(); i++) {
			expressions.add(stringValue(items.get(i), context + "[" + i + "]"));
		}
	}

	private String buildPartitionKey(Map<String, Object> sourceRow, List<String> expressions) {
		List<String> values = new ArrayList<String>();
		for (String expression : expressions) {
			values.add(resolveExpression(expression, sourceRow));
		}
		return String.join("|", values);
	}

	private List<Integer> matchingIndexes(List<Map<String, Object>> sourceRows, List<Integer> rowIndexes, String expression) {
		List<Integer> matches = new ArrayList<Integer>();
		for (Integer rowIndex : rowIndexes) {
			if (matchesPairExpression(expression, sourceRows.get(rowIndex))) {
				matches.add(rowIndex);
			}
		}
		return matches;
	}

	private void applyActions(List<Object> actions, Map<String, Object> retainedRow, Map<String, Object> pairedRow, LinkedHashSet<String> updatedFields, Map<String, Object> postprocessResult) {
		for (int i = 0; i < actions.size(); i++) {
			Map<String, Object> action = mapValue(actions.get(i), "postprocess.actions[" + i + "]");
			String actionType = stringValue(action.get("type"), "postprocess.actions[" + i + "].type");
			String target = stringValue(action.get("target"), "postprocess.actions[" + i + "].target");
			if ("use_pair_range_relative_age".equals(actionType)) {
				updatedFields.add(target);
				Map<String, Object> relativeAgeRange = relativeAgeRange(retainedRow, pairedRow);
				postprocessResult.put("relative_age_range", relativeAgeRange);
			} else if ("normalize_pair_uncertainty".equals(actionType)) {
				updatedFields.add(target);
				String uncertaintyState = deriveUncertaintyState(action, retainedRow, pairedRow);
				postprocessResult.put("dating_uncertainty_state", uncertaintyState);
				postprocessResult.put("dating_uncertainty_name", deriveUncertaintyName(uncertaintyState, retainedRow));
			} else if ("copy_if_empty".equals(actionType)) {
				String retainedNotes = stringOrEmpty(retainedRow.get("Notes"));
				String pairedNotes = pairedRow == null ? "" : stringOrEmpty(pairedRow.get("Notes"));
				if (retainedNotes.trim().isEmpty() && !pairedNotes.trim().isEmpty()) {
					updatedFields.add(target);
					postprocessResult.put("notes", pairedNotes);
				} else {
					postprocessResult.put("notes", retainedNotes);
				}
			} else {
				updatedFields.add(target);
			}
		}
	}

	private Map<String, Object> relativeAgeRange(Map<String, Object> retainedRow, Map<String, Object> pairedRow) {
		Map<String, Object> relativeAgeRange = new LinkedHashMap<String, Object>();
		String retainedSide = deriveUncertaintySide(retainedRow);
		if (pairedRow == null) {
			if ("from".equals(retainedSide)) {
				relativeAgeRange.put("older_date", retainedRow.get("Date"));
				relativeAgeRange.put("older_era", retainedRow.get("BCADBP"));
				relativeAgeRange.put("younger_date", null);
				relativeAgeRange.put("younger_era", null);
			} else {
				relativeAgeRange.put("older_date", null);
				relativeAgeRange.put("older_era", null);
				relativeAgeRange.put("younger_date", retainedRow.get("Date"));
				relativeAgeRange.put("younger_era", retainedRow.get("BCADBP"));
			}
			return relativeAgeRange;
		}
		relativeAgeRange.put("older_date", retainedRow.get("Date"));
		relativeAgeRange.put("older_era", retainedRow.get("BCADBP"));
		relativeAgeRange.put("younger_date", pairedRow.get("Date"));
		relativeAgeRange.put("younger_era", pairedRow.get("BCADBP"));
		return relativeAgeRange;
	}

	private String deriveUncertaintyState(Map<String, Object> action, Map<String, Object> retainedRow, Map<String, Object> pairedRow) {
		String retainedFamily = deriveUncertaintyFamily(retainedRow);
		String pairedFamily = pairedRow == null ? null : deriveUncertaintyFamily(pairedRow);
		boolean clearWhenClosedRange = Boolean.TRUE.equals(action.get("clear_when_closed_range"));
		boolean convertCaPairToStandard = Boolean.TRUE.equals(action.get("convert_ca_pair_to_standard"));
		if (pairedRow == null) {
			if (convertCaPairToStandard && "ca".equals(retainedFamily)) {
				return "normalized_to_standard";
			}
			return "retained";
		}
		if (clearWhenClosedRange && "standard".equals(retainedFamily) && retainedFamily.equals(pairedFamily)) {
			return "cleared";
		}
		if (convertCaPairToStandard && "ca".equals(retainedFamily) && retainedFamily.equals(pairedFamily)) {
			return "normalized_to_standard";
		}
		return "retained";
	}

	private Object deriveUncertaintyName(String uncertaintyState, Map<String, Object> retainedRow) {
		if ("cleared".equals(uncertaintyState)) {
			return null;
		}
		if ("normalized_to_standard".equals(uncertaintyState)) {
			return "Ca";
		}
		return retainedRow.get("Uncertainty");
	}

	private String resolveExpression(String expression, Map<String, Object> sourceRow) {
		if (expression.startsWith("source.")) {
			return stringOrEmpty(sourceRow.get(expression.substring("source.".length())));
		}
		if ("normalized.uncertainty_family".equals(expression)) {
			return deriveUncertaintyFamily(sourceRow);
		}
		if ("normalized.note_group".equals(expression)) {
			return stringOrEmpty(sourceRow.get("Notes"));
		}
		throw new IllegalArgumentException("Unsupported partition expression: " + expression);
	}

	private boolean matchesPairExpression(String expression, Map<String, Object> sourceRow) {
		String uncertaintySide = deriveUncertaintySide(sourceRow);
		if ("normalized.uncertainty_side == 'from'".equals(expression)) {
			return "from".equals(uncertaintySide);
		}
		if ("normalized.uncertainty_side == 'to'".equals(expression)) {
			return "to".equals(uncertaintySide);
		}
		throw new IllegalArgumentException("Unsupported pair expression: " + expression);
	}

	private String deriveUncertaintySide(Map<String, Object> sourceRow) {
		String uncertainty = stringOrEmpty(sourceRow.get("Uncertainty"));
		if (uncertainty.startsWith("From")) {
			return "from";
		}
		if (uncertainty.startsWith("To")) {
			return "to";
		}
		return "other";
	}

	private String deriveUncertaintyFamily(Map<String, Object> sourceRow) {
		String uncertainty = stringOrEmpty(sourceRow.get("Uncertainty"));
		return uncertainty.contains("Ca") ? "ca" : "standard";
	}

	private String stringOrEmpty(Object value) {
		return value == null ? "" : String.valueOf(value);
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> loadPolicy(String policyName) throws IOException {
		Yaml yaml = new Yaml();
		Path policyFile = POLICY_DIRECTORY.resolve(policyName + ".policy.yml");
		try (InputStream inputStream = Files.newInputStream(policyFile)) {
			Object loaded = yaml.load(inputStream);
			return mapValue(loaded, policyFile.toString());
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> mapValue(Object value, String context) {
		if (!(value instanceof Map)) {
			throw new IllegalArgumentException(context + " must be a mapping");
		}
		return (Map<String, Object>) value;
	}

	@SuppressWarnings("unchecked")
	private List<Object> listValue(Object value, String context) {
		if (!(value instanceof List)) {
			throw new IllegalArgumentException(context + " must be a list");
		}
		return (List<Object>) value;
	}

	private String stringValue(Object value, String context) {
		if (!(value instanceof String)) {
			throw new IllegalArgumentException(context + " must be a string");
		}
		return (String) value;
	}

	public static class PostprocessResult {
		private final List<String> emitCodes = new ArrayList<String>();
		private final LinkedHashSet<String> updatedFields = new LinkedHashSet<String>();
		private final List<Integer> retainedRowIndexes = new ArrayList<Integer>();
		private final Map<String, Object> postprocessResult = new LinkedHashMap<String, Object>();
		private final List<Map<String, Object>> postprocessResults = new ArrayList<Map<String, Object>>();
		private String retainedRow;

		public List<String> getEmitCodes() {
			return emitCodes;
		}

		public List<String> getUpdatedFields() {
			return new ArrayList<String>(updatedFields);
		}

		public List<Integer> getRetainedRowIndexes() {
			return retainedRowIndexes;
		}

		public String getRetainedRow() {
			return retainedRow;
		}

		public Map<String, Object> getPostprocessResult() {
			return postprocessResult;
		}

		public List<Map<String, Object>> getPostprocessResults() {
			return postprocessResults;
		}
	}
}