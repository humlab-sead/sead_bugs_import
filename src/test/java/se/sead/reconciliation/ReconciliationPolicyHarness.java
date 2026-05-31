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

public class ReconciliationPolicyHarness {

	private static final Path POLICY_DIRECTORY = Paths.get("doc", "reconciliation_policies");

	public ReconciliationResult execute(String policyName, Map<String, Boolean> stepHits, Map<String, Object> state, Map<String, Object> sourceRow) throws IOException {
		Map<String, Object> policy = loadPolicy(policyName);
		Map<String, Object> reconciliation = mapValue(policy.get("reconciliation"), policyName + ".reconciliation");
		if (prerequisiteFailed(state) && reconciliation.containsKey("prerequisite")) {
			Map<String, Object> prerequisite = mapValue(reconciliation.get("prerequisite"), policyName + ".reconciliation.prerequisite");
			String prerequisiteName = prerequisite.containsKey("name")
					? stringValue(prerequisite.get("name"), policyName + ".reconciliation.prerequisite.name")
					: "prerequisite";
			ReconciliationResult prerequisiteResult = new ReconciliationResult();
			prerequisiteResult.path.add(prerequisiteName);
			applyGuardResult(prerequisiteName, state, sourceRow, prerequisiteResult.reconciliationResult, policyName);
			return prerequisiteResult;
		}
		List<Object> rules = listValue(reconciliation.get("rules"), policyName + ".reconciliation.rules");
		ReconciliationResult result = new ReconciliationResult();
		for (int i = 0; i < rules.size(); i++) {
			Map<String, Object> rule = mapValue(rules.get(i), policyName + ".reconciliation.rules[" + i + "]");
			String ruleName = stringValue(rule.get("name"), policyName + ".reconciliation.rules[" + i + "].name");
			String ruleType = stringValue(rule.get("type"), policyName + ".reconciliation.rules[" + i + "].type");
			if ("search_chain".equals(ruleType)) {
				if (executeSearchChain(rule, stepHits, state, sourceRow, result, policyName + ".reconciliation.rules[" + i + "]")) {
					return result;
				}
				continue;
			}
			result.path.add(ruleName);
			if ("create_new".equals(ruleType)) {
				result.reconciliationResult.put("result_kind", "insert_new");
				applyPersistedAction(result.reconciliationResult, policyName, "insert_new");
				result.reconciliationResult.put("source", ruleName);
				result.reconciliationResult.put("row_id", null);
				applySourceIdentity(result.reconciliationResult, sourceRow, policyName);
				return result;
			}
			if (Boolean.TRUE.equals(stepHits.get(ruleName))) {
				applyMatchedRuleResult(rule, ruleName, state, sourceRow, result.reconciliationResult, policyName);
				return result;
			}
		}
		return result;
	}

	private boolean executeSearchChain(Map<String, Object> rule, Map<String, Boolean> stepHits, Map<String, Object> state, Map<String, Object> sourceRow, ReconciliationResult result, String context) {
		List<Object> steps = listValue(rule.get("steps"), context + ".steps");
		for (int i = 0; i < steps.size(); i++) {
			Map<String, Object> step = mapValue(steps.get(i), context + ".steps[" + i + "]");
			String stepName = stringValue(step.get("name"), context + ".steps[" + i + "].name");
			result.path.add(stepName);
			if (Boolean.TRUE.equals(stepHits.get(stepName))) {
				applyMatchedStepResult(step, stepName, state, sourceRow, result.reconciliationResult, policyName(context));
				return true;
			}
		}
		return false;
	}

	private void applyMatchedStepResult(Map<String, Object> step, String stepName, Map<String, Object> state, Map<String, Object> sourceRow, Map<String, Object> reconciliationResult, String policyName) {
		applyMatchedRuleResult(step, stepName, state, sourceRow, reconciliationResult, policyName);
	}

	private void applyMatchedRuleResult(Map<String, Object> rule, String ruleName, Map<String, Object> state, Map<String, Object> sourceRow, Map<String, Object> reconciliationResult, String policyName) {
		String guardErrorMessage = guardErrorMessage(state);
		String existingErrorMessage = existingErrorMessage(state);
		if ("mcrsummary".equals(policyName) && "species_lookup".equals(ruleName)) {
			reconciliationResult.put("result_kind", "return_as_is");
			reconciliationResult.put("source", ruleName);
			reconciliationResult.put("row_id", state == null ? null : state.get("existing_row_id"));
			if (guardErrorMessage != null || existingErrorMessage != null) {
				Map<String, Object> issue = new LinkedHashMap<String, Object>();
				issue.put("severity", "error");
				issue.put("message", guardErrorMessage != null ? guardErrorMessage : existingErrorMessage);
				reconciliationResult.put("issue", issue);
			}
			applySourceIdentity(reconciliationResult, sourceRow, policyName);
		} else if (guardErrorMessage != null) {
			applyGuardResult(ruleName, state, sourceRow, reconciliationResult, policyName);
		} else if (existingErrorMessage != null) {
			reconciliationResult.put("result_kind", "return_existing_error");
			Map<String, Object> issue = new LinkedHashMap<String, Object>();
			issue.put("severity", "error");
			issue.put("message", existingErrorMessage);
			reconciliationResult.put("issue", issue);
			reconciliationResult.put("source", ruleName);
			reconciliationResult.put("row_id", state == null ? null : state.get("existing_row_id"));
			applySourceIdentity(reconciliationResult, sourceRow, policyName);
		} else {
			reconciliationResult.put("result_kind", "update_existing");
			applyPersistedAction(reconciliationResult, policyName, "update_existing");
			reconciliationResult.put("source", ruleName);
			reconciliationResult.put("row_id", state == null ? null : state.get("existing_row_id"));
			applySourceIdentity(reconciliationResult, sourceRow, policyName);
		}
	}

	private void applyPersistedAction(Map<String, Object> reconciliationResult, String policyName, String resultKind) {
		if (!("speciesassociation".equals(policyName)
				|| "speciesbiology".equals(policyName)
				|| "specieskeys".equals(policyName)
				|| "speciessynonyms".equals(policyName)
				|| "speciesdistribution".equals(policyName))) {
			return;
		}
		if ("insert_new".equals(resultKind)) {
			reconciliationResult.put("persisted_action", "create");
			return;
		}
		if ("update_existing".equals(resultKind)) {
			reconciliationResult.put("persisted_action", "update");
		}
	}

	private void applyGuardResult(String sourceName, Map<String, Object> state, Map<String, Object> sourceRow, Map<String, Object> reconciliationResult, String policyName) {
		String guardErrorMessage = guardErrorMessage(state);
		reconciliationResult.put("result_kind", "return_guard_error");
		Map<String, Object> issue = new LinkedHashMap<String, Object>();
		issue.put("severity", "error");
		issue.put("message", guardErrorMessage);
		reconciliationResult.put("issue", issue);
		reconciliationResult.put("source", sourceName);
		reconciliationResult.put("row_id", null);
		applySourceIdentity(reconciliationResult, sourceRow, policyName);
	}

	private String policyName(String context) {
		int separatorIndex = context.indexOf(".reconciliation");
		return separatorIndex >= 0 ? context.substring(0, separatorIndex) : context;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> loadPolicy(String policyName) throws IOException {
		Yaml yaml = new Yaml();
		Path policyFile = POLICY_DIRECTORY.resolve(policyName + ".policy.yml");
		try (InputStream inputStream = Files.newInputStream(policyFile)) {
			return mapValue(yaml.load(inputStream), policyFile.toString());
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

	private String existingErrorMessage(Map<String, Object> state) {
		if (state == null || !state.containsKey("existing_error_message")) {
			return null;
		}
		Object value = state.get("existing_error_message");
		return value == null ? null : stringValue(value, "state.existing_error_message");
	}

	private String guardErrorMessage(Map<String, Object> state) {
		if (state == null || !state.containsKey("guard_error_message")) {
			return null;
		}
		Object value = state.get("guard_error_message");
		return value == null ? null : stringValue(value, "state.guard_error_message");
	}

	private boolean prerequisiteFailed(Map<String, Object> state) {
		if (state == null || !state.containsKey("prerequisite_failed")) {
			return false;
		}
		Object value = state.get("prerequisite_failed");
		if (!(value instanceof Boolean)) {
			throw new IllegalArgumentException("state.prerequisite_failed must be boolean");
		}
		return (Boolean) value;
	}

	private void applySourceIdentity(Map<String, Object> reconciliationResult, Map<String, Object> sourceRow, String policyName) {
		if (sourceRow.containsKey("LabID")) {
			reconciliationResult.put("lab_id", sourceRow.get("LabID"));
		}
		if (sourceRow.containsKey("PeriodCODE")) {
			reconciliationResult.put("period_code", sourceRow.get("PeriodCODE"));
		}
		if ("rdbsystem".equals(policyName) && sourceRow.containsKey("RDBSystemCode")) {
			Object rdbSystemCode = sourceRow.get("RDBSystemCode");
			reconciliationResult.put("rdb_system_code", rdbSystemCode instanceof Number ? ((Number) rdbSystemCode).intValue() : rdbSystemCode);
		}
		if (sourceRow.containsKey("SpeciesAssociationID")) {
			reconciliationResult.put("species_association_id", sourceRow.get("SpeciesAssociationID"));
		}
		if (("speciessynonyms".equals(policyName) || "speciesassociation".equals(policyName)) && sourceRow.containsKey("Ref")) {
			reconciliationResult.put("bugs_reference", sourceRow.get("Ref"));
		}
		if (sourceRow.containsKey("EcoGroupCode")) {
			reconciliationResult.put("ecocode_group_code", sourceRow.get("EcoGroupCode"));
		}
		if (sourceRow.containsKey("BugsEcoCODE")) {
			reconciliationResult.put("bugs_ecocode_code", sourceRow.get("BugsEcoCODE"));
		}
		if (sourceRow.containsKey("BugsKochCode")) {
			reconciliationResult.put("bugs_koch_code", sourceRow.get("BugsKochCode"));
		}
		if (sourceRow.containsKey("REFERENCE")) {
			reconciliationResult.put("bugs_reference", sourceRow.get("REFERENCE"));
		}
		if (sourceRow.containsKey("Ref") && sourceRow.containsKey("SiteCODE")) {
			reconciliationResult.put("bugs_reference", sourceRow.get("Ref"));
		}
		if (("speciesbiology".equals(policyName) || "specieskeys".equals(policyName)) && sourceRow.containsKey("Ref") && sourceRow.containsKey("Data")) {
			reconciliationResult.put("bugs_reference", sourceRow.get("Ref"));
		}
		if (sourceRow.containsKey("SiteCODE")) {
			reconciliationResult.put("site_code", sourceRow.get("SiteCODE"));
		}
		if (sourceRow.containsKey("AssociatedSpeciesCODE")) {
			Object associatedSpeciesCode = sourceRow.get("AssociatedSpeciesCODE");
			reconciliationResult.put("associated_species_code", associatedSpeciesCode instanceof Number ? ((Number) associatedSpeciesCode).intValue() : associatedSpeciesCode);
		}
		if (sourceRow.containsKey("AssociationType")) {
			reconciliationResult.put("association_type", sourceRow.get("AssociationType"));
		}
		if ("speciessynonyms".equals(policyName) && sourceRow.containsKey("SynGenus")) {
			reconciliationResult.put("synonym_genus", sourceRow.get("SynGenus"));
		}
		if ("speciessynonyms".equals(policyName) && sourceRow.containsKey("SynSpecies")) {
			reconciliationResult.put("synonym_species", sourceRow.get("SynSpecies"));
		}
		if ("speciessynonyms".equals(policyName) && sourceRow.containsKey("SynAuthority")) {
			reconciliationResult.put("synonym_authority", sourceRow.get("SynAuthority"));
		}
		if (sourceRow.containsKey("RDBCode")) {
			reconciliationResult.put("rdb_code", sourceRow.get("RDBCode"));
		} else if (sourceRow.containsKey("CODE")) {
			Object code = sourceRow.get("CODE");
			reconciliationResult.put("species_code", code instanceof Number ? ((Number) code).intValue() : code);
		}
		if ("country".equals(policyName) && !sourceRow.containsKey("RDBCode") && !sourceRow.containsKey("CODE") && sourceRow.containsKey("CountryCode")) {
			reconciliationResult.put("country_code", sourceRow.get("CountryCode"));
		}
		if ("speciesbiology".equals(policyName) || "specieskeys".equals(policyName)) {
			reconciliationResult.put("text_value", sourceRow.get("Data"));
		}
	}

	public static class ReconciliationResult {
		private final List<String> path = new ArrayList<String>();
		private final Map<String, Object> reconciliationResult = new LinkedHashMap<String, Object>();

		public List<String> getPath() {
			return path;
		}

		public Map<String, Object> getReconciliationResult() {
			return reconciliationResult;
		}
	}
}