package se.sead.reconciliation;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RelatedOutputPolicyHarness {

	private static final Path POLICY_DIRECTORY = Paths.get("doc", "reconciliation_policies");
	private static final Integer DEFAULT_EXISTING_DATASET_ID = 20;
	private static final Integer DEFAULT_PHYSICAL_SAMPLE_ID = 10;

	public RelatedOutputResult execute(String policyName, Map<String, Object> configOverrides) throws IOException {
		return execute(policyName, configOverrides, null, null);
	}

	public RelatedOutputResult execute(String policyName, Map<String, Object> configOverrides, Map<String, Object> stateOverrides) throws IOException {
		return execute(policyName, configOverrides, stateOverrides, null);
	}

	public RelatedOutputResult execute(String policyName, Map<String, Object> configOverrides, Map<String, Object> stateOverrides, Map<String, Object> sourceRow) throws IOException {
		Map<String, Object> policy = loadPolicy(policyName);
		List<Object> relatedOutputs = listValue(policy.get("related_outputs"), policyName + ".related_outputs");
		if ("species".equals(policyName)) {
			return executeSpeciesStyleGraph(relatedOutputs, configOverrides, stateOverrides, sourceRow);
		}
		Map<String, Object> config = defaultConfig(policy);
		if (configOverrides != null) {
			config.putAll(configOverrides);
		}
		Map<String, Map<String, Object>> relatedOutputIndex = indexRelatedOutputs(relatedOutputs);
		if (!relatedOutputIndex.containsKey("dataset") || !relatedOutputIndex.containsKey("analysis_entity")) {
			throw new IllegalArgumentException("Policy '" + policyName + "' must define dataset and analysis_entity related outputs for this harness");
		}
		if ("datescalendar".equals(policyName)) {
			return executeDatesCalendarStyleGraph(relatedOutputs, stateOverrides, sourceRow);
		}
		if ("datesradio".equals(policyName)) {
			return executeDatesRadioStyleGraph(relatedOutputs, stateOverrides, sourceRow);
		}
		if (!config.containsKey("allow_dataset_updates")) {
			return executeDatesPeriodStyleGraph(relatedOutputs, stateOverrides, sourceRow);
		}

		boolean allowDatasetUpdates = booleanValue(config.get("allow_dataset_updates"), "allow_dataset_updates");
		RelatedOutputResult result = new RelatedOutputResult();
		for (Object relatedOutputValue : relatedOutputs) {
			Map<String, Object> relatedOutput = mapValue(relatedOutputValue, policyName + ".related_outputs.item");
			result.relatedOutputs.add(stringValue(relatedOutput.get("name"), policyName + ".related_outputs.item.name"));
		}
		if (sourceRow != null && isBlank(sourceRow.get("SampleCODE"))) {
			result.rowChanged = true;
			result.graphIssue.put("severity", "error");
			result.graphIssue.put("message", "No sample specified");
			return result;
		}
		if (stateOverrides != null && Boolean.FALSE.equals(stateOverrides.get("sample_found"))) {
			result.rowChanged = false;
			result.graphIssue.put("severity", "error");
			result.graphIssue.put("message", "No sample found for code");
			return result;
		}
		result.datasetLinkMode = allowDatasetUpdates ? "reuse_existing_dataset" : "clone_updated_dataset";
		if (stateOverrides != null && stateOverrides.containsKey("matching_analysis_entity_count")
				&& integerValue(stateOverrides.get("matching_analysis_entity_count"), "matching_analysis_entity_count") > 1) {
			result.rowChanged = false;
			result.graphIssue.put("severity", "error");
			result.graphIssue.put("message", "More than one analysis entity found for same dataset and sample");
			return result;
		}
		result.rowChanged = true;
		result.updatedTargetFields.add("analysis_entity_id");

		Map<String, Object> datasetResult = new LinkedHashMap<String, Object>();
		if (allowDatasetUpdates) {
			datasetResult.put("dataset_id", DEFAULT_EXISTING_DATASET_ID);
			datasetResult.put("updated_dataset_id", null);
			datasetResult.put("supporting_action", "reuse");
		} else {
			datasetResult.put("dataset_id", null);
			datasetResult.put("updated_dataset_id", DEFAULT_EXISTING_DATASET_ID);
			datasetResult.put("supporting_action", "create");
		}

		Map<String, Object> analysisEntityResult = new LinkedHashMap<String, Object>();
		if (stateOverrides != null && stateOverrides.containsKey("existing_analysis_entity_id")) {
			analysisEntityResult.put("analysis_entity_id", integerValue(stateOverrides.get("existing_analysis_entity_id"), "existing_analysis_entity_id"));
		}
		analysisEntityResult.put("supporting_action", stateOverrides != null && stateOverrides.containsKey("existing_analysis_entity_id") ? "reuse" : "create");
		analysisEntityResult.put("physical_sample_id", DEFAULT_PHYSICAL_SAMPLE_ID);
		analysisEntityResult.put("dataset_id", datasetResult.get("dataset_id"));

		result.graphResult.put("dataset", datasetResult);
		result.graphResult.put("analysis_entity", analysisEntityResult);
		return result;
	}

	private RelatedOutputResult executeDatesRadioStyleGraph(List<Object> relatedOutputs, Map<String, Object> stateOverrides, Map<String, Object> sourceRow) {
		RelatedOutputResult result = new RelatedOutputResult();
		for (Object relatedOutputValue : relatedOutputs) {
			Map<String, Object> relatedOutput = mapValue(relatedOutputValue, "related_outputs.item");
			result.relatedOutputs.add(stringValue(relatedOutput.get("name"), "related_outputs.item.name"));
		}
		if (stateOverrides != null && Boolean.FALSE.equals(stateOverrides.get("sample_found"))) {
			result.rowChanged = false;
			result.graphIssue.put("severity", "error");
			result.graphIssue.put("message", "No sample found");
			return result;
		}
		if (stateOverrides != null && Boolean.FALSE.equals(stateOverrides.get("method_found"))) {
			result.rowChanged = true;
			result.graphIssue.put("severity", "error");
			result.graphIssue.put("message", "No method found");
			return result;
		}
		if (sourceRow != null && sourceRow.containsKey("Date") && sourceRow.get("Date") == null) {
			result.rowChanged = true;
			result.graphIssue.put("severity", "error");
			result.graphIssue.put("message", "No date found");
			return result;
		}
		if (stateOverrides != null && Boolean.FALSE.equals(stateOverrides.get("uncertainty_found"))) {
			result.rowChanged = true;
			result.graphIssue.put("severity", "error");
			result.graphIssue.put("message", "Unknown uncertainty symbol");
			return result;
		}
		result.updatedTargetFields.add("analysis_entity_id");
		result.rowChanged = true;

		Map<String, Object> datasetResult = new LinkedHashMap<String, Object>();
		datasetResult.put("dataset_id", null);
		datasetResult.put("supporting_action", "create");
		datasetResult.put("dataset_name", stringValue(sourceRow.get("DateCODE"), "DateCODE"));
		datasetResult.put("data_type_id", integerValue(stateOverrides.get("data_type_id"), "data_type_id"));
		datasetResult.put("method_abbreviation", stringValue(stateOverrides.get("method_abbreviation"), "method_abbreviation"));
		datasetResult.put("master_set_id", integerValue(stateOverrides.get("master_set_id"), "master_set_id"));
		datasetResult.put("updated", false);

		Map<String, Object> analysisEntityResult = new LinkedHashMap<String, Object>();
		analysisEntityResult.put("analysis_entity_id", null);
		analysisEntityResult.put("supporting_action", "create");
		analysisEntityResult.put("physical_sample_id", integerValue(stateOverrides.get("physical_sample_id"), "physical_sample_id"));
		analysisEntityResult.put("dataset_id", null);

		result.graphResult.put("dataset", datasetResult);
		result.graphResult.put("analysis_entity", analysisEntityResult);
		return result;
	}

	private RelatedOutputResult executeDatesPeriodStyleGraph(List<Object> relatedOutputs, Map<String, Object> stateOverrides, Map<String, Object> sourceRow) {
		RelatedOutputResult result = new RelatedOutputResult();
		for (Object relatedOutputValue : relatedOutputs) {
			Map<String, Object> relatedOutput = mapValue(relatedOutputValue, "related_outputs.item");
			result.relatedOutputs.add(stringValue(relatedOutput.get("name"), "related_outputs.item.name"));
		}
		if (sourceRow != null && isBlank(sourceRow.get("SampleCODE"))) {
			result.rowChanged = false;
			result.graphIssue.put("severity", "error");
			result.graphIssue.put("message", "No sample specified");
			return result;
		}
		if (stateOverrides != null && Boolean.FALSE.equals(stateOverrides.get("sample_found"))) {
			result.rowChanged = false;
			result.graphIssue.put("severity", "error");
			result.graphIssue.put("message", "No sample found for code");
			return result;
		}
		if (stateOverrides != null && Boolean.FALSE.equals(stateOverrides.get("uncertainty_found"))) {
			result.rowChanged = false;
			result.graphIssue.put("severity", "error");
			result.graphIssue.put("message", "No uncertainty found for definition");
			return result;
		}
		result.updatedTargetFields.add("analysis_entity_id");
		Map<String, Object> datasetResult = new LinkedHashMap<String, Object>();
		Integer datasetId = integerOrNull(stateOverrides, "existing_dataset_id");
		datasetResult.put("dataset_id", datasetId);
		datasetResult.put("dataset_name", stringValue(sourceRow.get("PeriodDateCODE"), "PeriodDateCODE"));
		datasetResult.put("data_type_id", integerValue(stateOverrides.get("data_type_id"), "data_type_id"));
		datasetResult.put("method_abbreviation", stringValue(stateOverrides.get("method_abbreviation"), "method_abbreviation"));
		datasetResult.put("master_set_id", integerValue(stateOverrides.get("master_set_id"), "master_set_id"));
		boolean datasetUpdated = datasetUpdated(stateOverrides, datasetResult);
		datasetResult.put("updated", datasetUpdated);
		datasetResult.put("supporting_action", datasetAction(datasetId, datasetUpdated));

		Map<String, Object> analysisEntityResult = new LinkedHashMap<String, Object>();
		Integer analysisEntityId = integerOrNull(stateOverrides, "existing_analysis_entity_id");
		Integer physicalSampleId = integerValue(stateOverrides.get("physical_sample_id"), "physical_sample_id");
		analysisEntityResult.put("analysis_entity_id", analysisEntityId);
		analysisEntityResult.put("physical_sample_id", physicalSampleId);
		analysisEntityResult.put("dataset_id", datasetId);
		analysisEntityResult.put("supporting_action", analysisEntityAction(stateOverrides, analysisEntityId, physicalSampleId, datasetId));

		result.rowChanged = booleanValue(datasetResult.get("updated"), "dataset.updated");
		result.graphResult.put("dataset", datasetResult);
		result.graphResult.put("analysis_entity", analysisEntityResult);
		return result;
	}

	private RelatedOutputResult executeDatesCalendarStyleGraph(List<Object> relatedOutputs, Map<String, Object> stateOverrides, Map<String, Object> sourceRow) {
		RelatedOutputResult result = new RelatedOutputResult();
		for (Object relatedOutputValue : relatedOutputs) {
			Map<String, Object> relatedOutput = mapValue(relatedOutputValue, "related_outputs.item");
			result.relatedOutputs.add(stringValue(relatedOutput.get("name"), "related_outputs.item.name"));
		}
		if (sourceRow != null && isBlank(sourceRow.get("SampleCODE"))) {
			result.rowChanged = false;
			result.graphIssue.put("severity", "error");
			result.graphIssue.put("message", "No sample specified");
			return result;
		}
		if (sourceRow != null && isBlank(sourceRow.get("BCADBP"))) {
			result.rowChanged = false;
			result.graphIssue.put("severity", "error");
			result.graphIssue.put("message", "No BCADBP specified");
			return result;
		}
		if (stateOverrides != null && Boolean.FALSE.equals(stateOverrides.get("sample_found"))) {
			result.rowChanged = false;
			result.graphIssue.put("severity", "error");
			result.graphIssue.put("message", "No sample found for code");
			return result;
		}
		result.updatedTargetFields.add("analysis_entity_id");

		Map<String, Object> relativeAgeResult = new LinkedHashMap<String, Object>();
		Integer relativeAgeId = integerOrNull(stateOverrides, "existing_relative_age_id");
		relativeAgeResult.put("relative_age_id", relativeAgeId);
		relativeAgeResult.put("supporting_action", relativeAgeId == null ? "create" : "reuse");
		String abbreviation = "CAL_" + integerValue(sourceRow.get("Date"), "Date") + "_" + stringValue(sourceRow.get("BCADBP"), "BCADBP");
		relativeAgeResult.put("abbreviation", abbreviation);
		relativeAgeResult.put("name", abbreviation);
		relativeAgeResult.put("type_name", "Calendar date");

		Map<String, Object> datasetResult = new LinkedHashMap<String, Object>();
		Integer datasetId = integerOrNull(stateOverrides, "existing_dataset_id");
		datasetResult.put("dataset_id", datasetId);
		datasetResult.put("dataset_name", stringValue(sourceRow.get("CalendarCODE"), "CalendarCODE"));
		datasetResult.put("data_type_id", integerValue(stateOverrides.get("data_type_id"), "data_type_id"));
		datasetResult.put("method_abbreviation", stringValue(stateOverrides.get("method_abbreviation"), "method_abbreviation"));
		datasetResult.put("master_set_id", integerValue(stateOverrides.get("master_set_id"), "master_set_id"));
		boolean datasetUpdated = datasetUpdated(stateOverrides, datasetResult);
		datasetResult.put("updated", datasetUpdated);
		datasetResult.put("supporting_action", datasetAction(datasetId, datasetUpdated));

		Map<String, Object> analysisEntityResult = new LinkedHashMap<String, Object>();
		Integer analysisEntityId = integerOrNull(stateOverrides, "existing_analysis_entity_id");
		Integer physicalSampleId = integerValue(stateOverrides.get("physical_sample_id"), "physical_sample_id");
		analysisEntityResult.put("analysis_entity_id", analysisEntityId);
		analysisEntityResult.put("physical_sample_id", physicalSampleId);
		analysisEntityResult.put("dataset_id", datasetId);
		analysisEntityResult.put("supporting_action", analysisEntityAction(stateOverrides, analysisEntityId, physicalSampleId, datasetId));

		result.rowChanged = booleanValue(datasetResult.get("updated"), "dataset.updated")
				|| !abbreviation.equals(stringOrNull(stateOverrides, "existing_relative_age_abbreviation"));
		result.graphResult.put("relative_age", relativeAgeResult);
		result.graphResult.put("dataset", datasetResult);
		result.graphResult.put("analysis_entity", analysisEntityResult);
		return result;
	}

	private String datasetAction(Integer datasetId, boolean updated) {
		if (datasetId == null) {
			return "create";
		}
		return updated ? "update" : "keep";
	}

	private String analysisEntityAction(Map<String, Object> state, Integer analysisEntityId, Integer physicalSampleId, Integer datasetId) {
		if (analysisEntityId == null) {
			return "create";
		}
		Integer existingPhysicalSampleId = integerOrNull(state, "existing_physical_sample_id");
		Integer existingDatasetId = integerOrNull(state, "existing_dataset_id");
		if (!sameInteger(existingPhysicalSampleId, physicalSampleId) || !sameInteger(existingDatasetId, datasetId)) {
			return "update";
		}
		return "keep";
	}

	private boolean sameInteger(Integer left, Integer right) {
		if (left == null) {
			return right == null;
		}
		return left.equals(right);
	}

	private RelatedOutputResult executeSpeciesStyleGraph(List<Object> relatedOutputs, Map<String, Object> configOverrides, Map<String, Object> stateOverrides, Map<String, Object> sourceRow) {
		RelatedOutputResult result = new RelatedOutputResult();
		for (Object relatedOutputValue : relatedOutputs) {
			Map<String, Object> relatedOutput = mapValue(relatedOutputValue, "related_outputs.item");
			result.relatedOutputs.add(stringValue(relatedOutput.get("name"), "related_outputs.item.name"));
		}
		result.updatedTargetFields.add("taxon_id");
		result.rowChanged = true;
		Integer importOrderId = configOverrides != null && configOverrides.containsKey("import_order_id")
				? integerValue(configOverrides.get("import_order_id"), "import_order_id")
				: integerOrDefault(stateOverrides, "import_order_id", Integer.valueOf(701));

		Map<String, Object> familyResult = new LinkedHashMap<String, Object>();
		Integer familyId = integerOrNull(stateOverrides, "existing_family_id");
		familyResult.put("result_kind", familyId == null ? "insert_new" : "return_existing");
		familyResult.put("supporting_action", familyId == null ? "create" : "reuse");
		familyResult.put("family_id", familyId);
		familyResult.put("family_name", stringValue(sourceRow.get("FAMILY"), "FAMILY"));
		familyResult.put("order_id", importOrderId);

		Map<String, Object> genusResult = new LinkedHashMap<String, Object>();
		Integer genusId = integerOrNull(stateOverrides, "existing_genus_id");
		genusResult.put("result_kind", genusId == null ? "insert_new" : "return_existing");
		genusResult.put("supporting_action", genusId == null ? "create" : "reuse");
		genusResult.put("genus_id", genusId);
		genusResult.put("genus_name", stringValue(sourceRow.get("GENUS"), "GENUS"));
		genusResult.put("family_id", familyId);

		Map<String, Object> authorResult = new LinkedHashMap<String, Object>();
		Integer authorId = integerOrNull(stateOverrides, "existing_author_id");
		authorResult.put("result_kind", authorId == null ? "insert_new" : "return_existing");
		authorResult.put("supporting_action", authorId == null ? "create" : "reuse");
		authorResult.put("author_id", authorId);
		authorResult.put("author_name", stringOrNull(stateOverrides, "existing_author_name") == null ? stringOrNull(sourceRow, "AUTHORITY") : stringOrNull(stateOverrides, "existing_author_name"));

		Map<String, Object> speciesResult = new LinkedHashMap<String, Object>();
		Integer speciesId = integerOrNull(stateOverrides, "existing_species_id");
		speciesResult.put("result_kind", speciesId == null ? "insert_new" : "return_existing");
		speciesResult.put("supporting_action", speciesId == null ? "create" : "reuse");
		speciesResult.put("species_id", speciesId);
		speciesResult.put("species", stringValue(sourceRow.get("SPECIES"), "SPECIES"));
		speciesResult.put("genus_id", genusId);
		speciesResult.put("author_id", authorId);

		result.graphResult.put("taxa_family", familyResult);
		result.graphResult.put("taxa_genus", genusResult);
		result.graphResult.put("taxa_author", authorResult);
		result.graphResult.put("taxa_species", speciesResult);
		return result;
	}

	private boolean datasetUpdated(Map<String, Object> stateOverrides, Map<String, Object> datasetResult) {
		return integerOrNull(stateOverrides, "existing_dataset_id") == null
				|| !datasetResult.get("dataset_name").equals(stringOrNull(stateOverrides, "existing_dataset_name"))
				|| integerValue(datasetResult.get("data_type_id"), "data_type_id").intValue() != integerOrDefault(stateOverrides, "existing_data_type_id", integerValue(datasetResult.get("data_type_id"), "data_type_id")).intValue()
				|| !datasetResult.get("method_abbreviation").equals(stringOrNull(stateOverrides, "existing_method_abbreviation"))
				|| integerValue(datasetResult.get("master_set_id"), "master_set_id").intValue() != integerOrDefault(stateOverrides, "existing_master_set_id", integerValue(datasetResult.get("master_set_id"), "master_set_id")).intValue();
	}

	private Map<String, Map<String, Object>> indexRelatedOutputs(List<Object> relatedOutputs) {
		Map<String, Map<String, Object>> index = new LinkedHashMap<String, Map<String, Object>>();
		for (int i = 0; i < relatedOutputs.size(); i++) {
			Map<String, Object> relatedOutput = mapValue(relatedOutputs.get(i), "related_outputs[" + i + "]");
			index.put(stringValue(relatedOutput.get("name"), "related_outputs[" + i + "].name"), relatedOutput);
		}
		return index;
	}

	private Map<String, Object> defaultConfig(Map<String, Object> policy) {
		Map<String, Object> config = new LinkedHashMap<String, Object>();
		Map<String, Object> reconciliation = mapValue(policy.get("reconciliation"), "policy.reconciliation");
		if (reconciliation.containsKey("prerequisite")) {
			Map<String, Object> prerequisite = mapValue(reconciliation.get("prerequisite"), "policy.reconciliation.prerequisite");
			if (prerequisite.containsKey("config")) {
				List<Object> configItems = listValue(prerequisite.get("config"), "policy.reconciliation.prerequisite.config");
				for (int i = 0; i < configItems.size(); i++) {
					Map<String, Object> configItem = mapValue(configItems.get(i), "policy.reconciliation.prerequisite.config[" + i + "]");
					config.put(stringValue(configItem.get("name"), "policy.reconciliation.prerequisite.config[" + i + "].name"), configItem.get("default"));
				}
			}
		}
		return config;
	}

	private boolean booleanValue(Object value, String context) {
		if (!(value instanceof Boolean)) {
			throw new IllegalArgumentException(context + " must be boolean");
		}
		return (Boolean) value;
	}

	private Integer integerValue(Object value, String context) {
		if (value instanceof Number) {
			return ((Number) value).intValue();
		}
		if (value instanceof String) {
			return Integer.valueOf((String) value);
		}
		throw new IllegalArgumentException(context + " must be numeric");
	}

	private Integer integerOrDefault(Map<String, Object> state, String key, Integer fallback) {
		Integer value = integerOrNull(state, key);
		return value == null ? fallback : value;
	}

	private Integer integerOrNull(Map<String, Object> state, String key) {
		if (state == null || !state.containsKey(key) || state.get(key) == null) {
			return null;
		}
		return integerValue(state.get(key), key);
	}

	private String stringOrNull(Map<String, Object> state, String key) {
		if (state == null || !state.containsKey(key) || state.get(key) == null) {
			return null;
		}
		return stringValue(state.get(key), key);
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

	private boolean isBlank(Object value) {
		return value == null || (value instanceof String && ((String) value).trim().isEmpty());
	}

	public static class RelatedOutputResult {
		private final List<String> relatedOutputs = new ArrayList<String>();
		private final List<String> updatedTargetFields = new ArrayList<String>();
		private String datasetLinkMode;
		private boolean rowChanged;
		private final Map<String, Object> graphResult = new LinkedHashMap<String, Object>();
		private final Map<String, Object> graphIssue = new LinkedHashMap<String, Object>();

		public List<String> getRelatedOutputs() {
			return relatedOutputs;
		}

		public List<String> getUpdatedTargetFields() {
			return updatedTargetFields;
		}

		public String getDatasetLinkMode() {
			return datasetLinkMode;
		}

		public boolean isRowChanged() {
			return rowChanged;
		}

		public Map<String, Object> getGraphResult() {
			return graphResult;
		}

		public Map<String, Object> getGraphIssue() {
			return graphIssue;
		}
	}
}