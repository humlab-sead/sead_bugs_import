package se.sead.reconciliation;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class PolicyFormatValidationTest {

	private static final Path POLICY_DIRECTORY = Paths.get("doc", "reconciliation_policies");
	private static final Path FIXTURE_DIRECTORY = POLICY_DIRECTORY.resolve("fixtures");
	private static final Set<String> TOP_LEVEL_KEYS = setOf(
			"meta", "policy", "source", "target", "mappings", "output", "related_outputs", "reconciliation", "postprocess", "update_detection", "dependencies", "tracing", "known_divergences", "resolvers", "helpers"
	);
	private static final Set<String> FIXTURE_KEYS = setOf("fixture_version", "policy", "scenarios");
	private static final Set<String> FIXTURE_SCENARIO_KEYS = setOf("name", "intent", "source_rows", "policy_context", "expects", "comment");
	private static final Set<String> FIXTURE_POLICY_CONTEXT_KEYS = setOf("args", "step_hits", "config", "state");
	private static final Set<String> FIXTURE_EXPECT_KEYS = setOf("postprocess", "retained_row", "retained_row_indexes", "updates_target_fields", "related_outputs", "emit_codes", "resolver", "resolver_path", "reconciliation_path", "target_field", "known_divergence_areas", "dataset_link_mode", "row_changed", "graph_result", "graph_issue", "postprocess_result", "postprocess_results", "resolver_result", "reconciliation_result", "output_result", "comment");
	private static final Set<String> META_KEYS = setOf("format_version", "generated_from", "reviewed", "notes");
	private static final Set<String> POLICY_KEYS = setOf("domain", "description");
	private static final Set<String> SOURCE_KEYS = setOf("system", "table", "sql", "fields", "identity_key", "trace_key");
	private static final Set<String> SOURCE_FIELD_KEYS = setOf("name", "type", "nullable", "role", "comment");
	private static final Set<String> TRACE_KEY_KEYS = setOf("template", "comment");
	private static final Set<String> TARGET_KEYS = setOf("system", "table", "identity");
	private static final Set<String> TARGET_IDENTITY_KEYS = setOf("column", "type", "strategy", "sequence", "identity_service_endpoint", "comment");
	private static final Set<String> MAPPING_KEYS = setOf("target_field", "type", "nullable", "source_field", "transform", "comment");
	private static final Set<String> OUTPUT_KEYS = setOf("mode", "item_helper", "compare_fields", "on_missing_generated", "on_missing_existing", "on_match", "comment");
	private static final Set<String> RELATED_OUTPUT_KEYS = setOf("name", "table", "mode", "relationship", "phase", "item_helper", "key_fields", "persistence", "identity", "mappings", "reconciliation", "update_detection", "tracing", "comment");
	private static final Set<String> RELATED_OUTPUT_RECONCILIATION_KEYS = setOf("existing_rows", "compare_fields", "on_missing_generated", "on_missing_existing", "on_match", "comment");
	private static final Set<String> RELATED_OUTPUT_EXISTING_ROWS_KEYS = setOf("table", "where", "query", "bind");
	private static final Set<String> TRANSFORM_KEYS = setOf(
			"type", "condition", "if_true", "if_false", "parts", "separator", "helper", "args", "input", "output_type", "value", "field", "expr"
	);
	private static final Set<String> RECONCILIATION_KEYS = setOf("strategy", "prerequisite", "rules");
	private static final Set<String> PREREQUISITE_KEYS = setOf("name", "description", "config");
	private static final Set<String> CONFIG_KEYS = setOf("name", "property", "default", "description");
	private static final Set<String> TRACE_LOOKUP_RULE_KEYS = setOf(
			"name", "order", "type", "description", "bugs_table", "identifier_expr", "allow_deleted", "external_edit", "on_match", "on_error", "comment"
	);
	private static final Set<String> DATABASE_QUERY_RULE_KEYS = setOf(
			"name", "order", "type", "description", "table", "where", "query", "bind", "variants", "result_handling", "on_match", "on_error", "comment"
	);
	private static final Set<String> SEARCH_CHAIN_RULE_KEYS = setOf("name", "order", "type", "description", "steps", "on_no_match", "comment");
	private static final Set<String> CREATE_NEW_RULE_KEYS = setOf("name", "order", "type", "description", "on_match", "comment");
	private static final Set<String> HISTORY_CHECK_STEP_KEYS = setOf("name", "order", "type", "description", "match_condition", "on_match", "comment");
	private static final Set<String> EXTERNAL_EDIT_KEYS = setOf("on_conflict", "message");
	private static final Set<String> VARIANT_KEYS = setOf("name", "condition", "table", "where", "query", "bind");
	private static final Set<String> RESULT_HANDLING_KEYS = setOf("count_zero", "count_one", "count_many");
	private static final Set<String> UPDATE_DETECTION_KEYS = setOf("strategy", "fields", "on_change", "comment", "config");
	private static final Set<String> TRACING_KEYS = setOf("bugs_table", "sead_table", "write_on", "comment");
	private static final Set<String> POSTPROCESS_KEYS = setOf("name", "phase", "mode", "description", "group_by", "partition_by", "pair_rules", "retain_row", "actions", "on_conflict", "comment");
	private static final Set<String> POSTPROCESS_PAIR_RULE_KEYS = setOf("left", "right", "include_singletons");
	private static final Set<String> POSTPROCESS_ACTION_KEYS = setOf("type", "target", "from_paired_field", "clear_when_closed_range", "convert_ca_pair_to_standard", "comment");
	private static final Set<String> KNOWN_DIVERGENCE_KEYS = setOf("area", "status", "description", "policy_choice");
	private static final Set<String> EMIT_KEYS = setOf("severity", "code", "message", "set_flagged");
	private static final Set<String> RESOLVER_KEYS = setOf("name", "description", "returns", "args", "config", "steps", "used_by", "comment");
	private static final Set<String> RESOLVER_STEP_KEYS = setOf("name", "when", "action", "bugs_table", "identifier_expr", "table", "where", "query", "bind", "emit", "return", "comment");
	private static final Set<String> HELPER_KEYS = setOf("name", "description", "signature", "expression", "used_by");

	@Test
	public void allPolicyFilesMatchDocumentedFormat() throws IOException {
		List<Path> policyFiles = listPolicyFiles();
		assertFalse("Expected at least one policy file", policyFiles.isEmpty());

		for (Path policyFile : policyFiles) {
			validatePolicy(policyFile);
		}
	}

	@Test
	public void policyFixturesStayAlignedWithReferencedPolicies() throws IOException {
		List<Path> fixtureFiles = listFixtureFiles();
		assertFalse("Expected at least one policy fixture file", fixtureFiles.isEmpty());

		for (Path fixtureFile : fixtureFiles) {
			validateFixture(fixtureFile);
		}
	}

	private List<Path> listPolicyFiles() throws IOException {
		try (Stream<Path> stream = Files.list(POLICY_DIRECTORY)) {
			return stream
					.filter(path -> path.getFileName().toString().endsWith(".policy.yml"))
					.sorted()
					.collect(Collectors.toList());
		}
	}

	private List<Path> listFixtureFiles() throws IOException {
		try (Stream<Path> stream = Files.list(FIXTURE_DIRECTORY)) {
			return stream
					.filter(path -> path.getFileName().toString().endsWith(".fixture.yml"))
					.sorted()
					.collect(Collectors.toList());
		}
	}

	private void validatePolicy(Path policyFile) throws IOException {
		Map<String, Object> policy = readYamlMap(policyFile);
		assertAllowedKeys(policy, context(policyFile, "top-level"), TOP_LEVEL_KEYS);
		requireKeys(policy, context(policyFile, "top-level"), "meta", "policy", "source", "target", "mappings", "reconciliation", "update_detection", "dependencies", "tracing", "helpers");

		Map<String, Object> meta = mapValue(policy.get("meta"), context(policyFile, "meta"));
		assertAllowedKeys(meta, context(policyFile, "meta"), META_KEYS);
		requireKeys(meta, context(policyFile, "meta"), "format_version", "generated_from", "reviewed");
		assertEnum(stringValue(meta.get("generated_from"), context(policyFile, "meta.generated_from")), context(policyFile, "meta.generated_from"), "java_source", "manual", "llm_extracted");
		assertTrue(context(policyFile, "meta.reviewed") + " must be boolean", meta.get("reviewed") instanceof Boolean);

		Map<String, Object> policySection = mapValue(policy.get("policy"), context(policyFile, "policy"));
		assertAllowedKeys(policySection, context(policyFile, "policy"), POLICY_KEYS);
		requireKeys(policySection, context(policyFile, "policy"), "domain", "description");

		Map<String, Object> source = mapValue(policy.get("source"), context(policyFile, "source"));
		assertAllowedKeys(source, context(policyFile, "source"), SOURCE_KEYS);
		requireKeys(source, context(policyFile, "source"), "system", "table", "sql", "fields", "identity_key", "trace_key");
		assertEquals(context(policyFile, "source.system"), "bugscep", stringValue(source.get("system"), context(policyFile, "source.system")));
		List<Object> sourceFields = listValue(source.get("fields"), context(policyFile, "source.fields"));
		Set<String> sourceFieldNames = new HashSet<String>();
		for (int i = 0; i < sourceFields.size(); i++) {
			Map<String, Object> field = mapValue(sourceFields.get(i), context(policyFile, "source.fields[" + i + "]"));
			assertAllowedKeys(field, context(policyFile, "source.fields[" + i + "]"), SOURCE_FIELD_KEYS);
			requireKeys(field, context(policyFile, "source.fields[" + i + "]"), "name", "type", "nullable", "role");
			String fieldName = stringValue(field.get("name"), context(policyFile, "source.fields[" + i + "].name"));
			sourceFieldNames.add(fieldName);
			assertEnum(stringValue(field.get("type"), context(policyFile, "source.fields[" + i + "].type")), context(policyFile, "source.fields[" + i + "].type"), "string", "integer", "decimal", "float", "boolean", "date");
			assertTrue(context(policyFile, "source.fields[" + i + "].nullable") + " must be boolean", field.get("nullable") instanceof Boolean);
			assertEnum(stringValue(field.get("role"), context(policyFile, "source.fields[" + i + "].role")), context(policyFile, "source.fields[" + i + "].role"), "natural_key", "data", "ignored");
		}
		assertTrue(context(policyFile, "source.identity_key") + " must match a source field", sourceFieldNames.contains(stringValue(source.get("identity_key"), context(policyFile, "source.identity_key"))));

		Map<String, Object> traceKey = mapValue(source.get("trace_key"), context(policyFile, "source.trace_key"));
		assertAllowedKeys(traceKey, context(policyFile, "source.trace_key"), TRACE_KEY_KEYS);
		requireKeys(traceKey, context(policyFile, "source.trace_key"), "template");

		Map<String, Object> target = mapValue(policy.get("target"), context(policyFile, "target"));
		assertAllowedKeys(target, context(policyFile, "target"), TARGET_KEYS);
		requireKeys(target, context(policyFile, "target"), "system", "table", "identity");
		assertEquals(context(policyFile, "target.system"), "sead", stringValue(target.get("system"), context(policyFile, "target.system")));
		Map<String, Object> identity = mapValue(target.get("identity"), context(policyFile, "target.identity"));
		validateIdentity(identity, context(policyFile, "target.identity"));

		Set<String> mappingTargets = validateMappings(listValue(policy.get("mappings"), context(policyFile, "mappings")), context(policyFile, "mappings"), sourceFieldNames);

		if (policy.containsKey("output")) {
			validateOutput(mapValue(policy.get("output"), context(policyFile, "output")), context(policyFile, "output"), mappingTargets);
		}

		if (policy.containsKey("related_outputs")) {
			validateRelatedOutputs(listValue(policy.get("related_outputs"), context(policyFile, "related_outputs")), context(policyFile, "related_outputs"), sourceFieldNames);
		}

		Map<String, Object> reconciliation = mapValue(policy.get("reconciliation"), context(policyFile, "reconciliation"));
		assertAllowedKeys(reconciliation, context(policyFile, "reconciliation"), RECONCILIATION_KEYS);
		requireKeys(reconciliation, context(policyFile, "reconciliation"), "strategy", "rules");
		assertEquals(context(policyFile, "reconciliation.strategy"), "ordered_rules", stringValue(reconciliation.get("strategy"), context(policyFile, "reconciliation.strategy")));
		if (reconciliation.containsKey("prerequisite")) {
			validatePrerequisite(mapValue(reconciliation.get("prerequisite"), context(policyFile, "reconciliation.prerequisite")), context(policyFile, "reconciliation.prerequisite"));
		}
		List<Object> rules = listValue(reconciliation.get("rules"), context(policyFile, "reconciliation.rules"));
		assertFalse(context(policyFile, "reconciliation.rules") + " must not be empty", rules.isEmpty());
		List<Integer> ruleOrders = new ArrayList<Integer>();
		for (int i = 0; i < rules.size(); i++) {
			ruleOrders.add(validateRule(mapValue(rules.get(i), context(policyFile, "reconciliation.rules[" + i + "]")), context(policyFile, "reconciliation.rules[" + i + "]"), stringValue(source.get("table"), context(policyFile, "source.table"))));
		}
		List<Integer> sortedOrders = new ArrayList<Integer>(ruleOrders);
		Collections.sort(sortedOrders);
		assertEquals(context(policyFile, "reconciliation.rules") + " must be ordered by ascending order", sortedOrders, ruleOrders);

		if (policy.containsKey("postprocess")) {
			validatePostprocess(listValue(policy.get("postprocess"), context(policyFile, "postprocess")), context(policyFile, "postprocess"), mappingTargets);
		}

		validateUpdateDetection(mapValue(policy.get("update_detection"), context(policyFile, "update_detection")), context(policyFile, "update_detection"), mappingTargets);

		Object dependencies = policy.get("dependencies");
		List<Object> dependencyList = listValue(dependencies, context(policyFile, "dependencies"));
		for (int i = 0; i < dependencyList.size(); i++) {
			stringValue(dependencyList.get(i), context(policyFile, "dependencies[" + i + "]"));
		}

		validateTracing(mapValue(policy.get("tracing"), context(policyFile, "tracing")), context(policyFile, "tracing"), stringValue(source.get("table"), context(policyFile, "source.table")), stringValue(target.get("table"), context(policyFile, "target.table")), true);

		if (policy.containsKey("known_divergences")) {
			validateKnownDivergences(listValue(policy.get("known_divergences"), context(policyFile, "known_divergences")), context(policyFile, "known_divergences"));
		}

		if (policy.containsKey("resolvers")) {
			validateResolvers(listValue(policy.get("resolvers"), context(policyFile, "resolvers")), context(policyFile, "resolvers"), mappingTargets);
		}

		List<Object> helpers = listValue(policy.get("helpers"), context(policyFile, "helpers"));
		for (int i = 0; i < helpers.size(); i++) {
			Map<String, Object> helper = mapValue(helpers.get(i), context(policyFile, "helpers[" + i + "]"));
			assertAllowedKeys(helper, context(policyFile, "helpers[" + i + "]"), HELPER_KEYS);
			requireKeys(helper, context(policyFile, "helpers[" + i + "]"), "name", "description", "signature");
			if (helper.containsKey("used_by")) {
				List<Object> usedBy = listValue(helper.get("used_by"), context(policyFile, "helpers[" + i + "].used_by"));
				for (int j = 0; j < usedBy.size(); j++) {
					String targetField = stringValue(usedBy.get(j), context(policyFile, "helpers[" + i + "].used_by[" + j + "]"));
					assertTrue(context(policyFile, "helpers[" + i + "].used_by[" + j + "]") + " must match a mapping target", mappingTargets.contains(targetField));
				}
			}
		}
	}

	private int validateRule(Map<String, Object> rule, String context, String sourceTable) {
		requireKeys(rule, context, "name", "order", "type", "description");
		String type = stringValue(rule.get("type"), context + ".type");
		assertEnum(type, context + ".type", "trace_lookup", "database_query", "create_new", "search_chain");
		assertTrue(context + ".order must be numeric", rule.get("order") instanceof Number);
		if (rule.containsKey("on_match")) {
			assertEnum(stringValue(rule.get("on_match"), context + ".on_match"), context + ".on_match", "update", "return_as_is", "skip", "insert");
		}
		if (rule.containsKey("on_error")) {
			assertEnum(stringValue(rule.get("on_error"), context + ".on_error"), context + ".on_error", "skip", "raise", "return_as_is");
		}
		if (rule.containsKey("on_no_match")) {
			assertEnum(stringValue(rule.get("on_no_match"), context + ".on_no_match"), context + ".on_no_match", "fall_through", "error", "skip", "return_as_is");
		}

		if ("trace_lookup".equals(type)) {
			assertAllowedKeys(rule, context, TRACE_LOOKUP_RULE_KEYS);
			requireKeys(rule, context, "bugs_table", "identifier_expr", "on_match");
			assertEquals(context + ".bugs_table must match source.table", sourceTable, stringValue(rule.get("bugs_table"), context + ".bugs_table"));
			if (rule.containsKey("allow_deleted")) {
				assertTrue(context + ".allow_deleted must be boolean", rule.get("allow_deleted") instanceof Boolean);
			}
			if (rule.containsKey("external_edit")) {
				Map<String, Object> externalEdit = mapValue(rule.get("external_edit"), context + ".external_edit");
				assertAllowedKeys(externalEdit, context + ".external_edit", EXTERNAL_EDIT_KEYS);
				requireKeys(externalEdit, context + ".external_edit", "on_conflict", "message");
				assertEnum(stringValue(externalEdit.get("on_conflict"), context + ".external_edit.on_conflict"), context + ".external_edit.on_conflict", "error", "skip", "return_as_is");
			}
		} else if ("database_query".equals(type)) {
			assertAllowedKeys(rule, context, DATABASE_QUERY_RULE_KEYS);
			boolean hasWhere = rule.containsKey("where");
			boolean hasQuery = rule.containsKey("query");
			boolean hasVariants = rule.containsKey("variants");
			assertTrue(context + " must define where, query, or variants", hasWhere || hasQuery || hasVariants);
			assertTrue(context + " must define on_match or result_handling", rule.containsKey("on_match") || rule.containsKey("result_handling"));
			if (hasWhere) {
				assertNotNull(context + ".table is required when using where", rule.get("table"));
			}
			if (rule.containsKey("bind")) {
				mapValue(rule.get("bind"), context + ".bind");
			}
			if (hasVariants) {
				validateVariants(listValue(rule.get("variants"), context + ".variants"), context + ".variants");
			}
			if (rule.containsKey("result_handling")) {
				validateResultHandling(mapValue(rule.get("result_handling"), context + ".result_handling"), context + ".result_handling");
			}
		} else if ("search_chain".equals(type)) {
			assertAllowedKeys(rule, context, SEARCH_CHAIN_RULE_KEYS);
			requireKeys(rule, context, "steps", "on_no_match");
			List<Object> steps = listValue(rule.get("steps"), context + ".steps");
			assertFalse(context + ".steps must not be empty", steps.isEmpty());
			List<Integer> stepOrders = new ArrayList<Integer>();
			for (int i = 0; i < steps.size(); i++) {
				stepOrders.add(validateSearchChainStep(mapValue(steps.get(i), context + ".steps[" + i + "]"), context + ".steps[" + i + "]", sourceTable));
			}
			List<Integer> sortedStepOrders = new ArrayList<Integer>(stepOrders);
			Collections.sort(sortedStepOrders);
			assertEquals(context + ".steps must be ordered by ascending order", sortedStepOrders, stepOrders);
		} else {
			assertAllowedKeys(rule, context, CREATE_NEW_RULE_KEYS);
			requireKeys(rule, context, "on_match");
		}

		return ((Number) rule.get("order")).intValue();
	}

	private void validateFixture(Path fixtureFile) throws IOException {
		Map<String, Object> fixture = readYamlMap(fixtureFile);
		assertAllowedKeys(fixture, context(fixtureFile, "top-level"), FIXTURE_KEYS);
		requireKeys(fixture, context(fixtureFile, "top-level"), "fixture_version", "policy", "scenarios");
		assertEquals(context(fixtureFile, "fixture_version"), "1.0", stringValue(fixture.get("fixture_version"), context(fixtureFile, "fixture_version")));

		String policyDomain = stringValue(fixture.get("policy"), context(fixtureFile, "policy"));
		Path policyFile = POLICY_DIRECTORY.resolve(policyDomain + ".policy.yml");
		assertTrue(context(fixtureFile, "policy") + " must reference an existing policy file", Files.exists(policyFile));

		PolicyReferenceData policyReferenceData = extractPolicyReferenceData(readYamlMap(policyFile), policyFile);
		List<Object> scenarios = listValue(fixture.get("scenarios"), context(fixtureFile, "scenarios"));
		assertFalse(context(fixtureFile, "scenarios") + " must not be empty", scenarios.isEmpty());

		Set<String> scenarioNames = new HashSet<String>();
		for (int i = 0; i < scenarios.size(); i++) {
			Map<String, Object> scenario = mapValue(scenarios.get(i), context(fixtureFile, "scenarios[" + i + "]"));
			validateFixtureScenario(scenario, context(fixtureFile, "scenarios[" + i + "]"), policyReferenceData);
			String scenarioName = stringValue(scenario.get("name"), context(fixtureFile, "scenarios[" + i + "].name"));
			assertTrue(context(fixtureFile, "scenarios[" + i + "].name") + " must be unique within the fixture file", scenarioNames.add(scenarioName));
		}
	}

	private void validateFixtureScenario(Map<String, Object> scenario, String context, PolicyReferenceData policyReferenceData) {
		assertAllowedKeys(scenario, context, FIXTURE_SCENARIO_KEYS);
		requireKeys(scenario, context, "name", "intent", "source_rows", "expects");
		String intent = stringValue(scenario.get("intent"), context + ".intent");
		assertEnum(intent, context + ".intent", "postprocess_merge", "postprocess_conflict", "resolver_path", "reconciliation_path", "related_output_graph", "supporting_output_result", "output_result");

		List<Object> sourceRows = listValue(scenario.get("source_rows"), context + ".source_rows");
		assertFalse(context + ".source_rows must not be empty", sourceRows.isEmpty());
		Map<String, Object> expects = mapValue(scenario.get("expects"), context + ".expects");
		assertAllowedKeys(expects, context + ".expects", FIXTURE_EXPECT_KEYS);
		if ("postprocess_merge".equals(intent)) {
			if (sourceRows.size() < 2) {
				String postprocessName = stringValue(expects.get("postprocess"), context + ".expects.postprocess");
				assertTrue(
						context + ".source_rows must include at least two provisional rows for postprocess scenarios unless the referenced postprocess allows singletons",
						policyReferenceData.singletonEnabledPostprocessNames.contains(postprocessName)
				);
			}
		} else if ("postprocess_conflict".equals(intent)) {
			assertTrue(context + ".source_rows must include at least two provisional rows for postprocess scenarios", sourceRows.size() >= 2);
		}
		for (int i = 0; i < sourceRows.size(); i++) {
			Map<String, Object> sourceRow = mapValue(sourceRows.get(i), context + ".source_rows[" + i + "]");
			for (String fieldName : sourceRow.keySet()) {
				assertTrue(context + ".source_rows[" + i + "] contains unknown source field '" + fieldName + "'", policyReferenceData.sourceFields.contains(fieldName));
			}
		}
		if (scenario.containsKey("policy_context")) {
			validateFixturePolicyContext(mapValue(scenario.get("policy_context"), context + ".policy_context"), context + ".policy_context");
		}

		if ("postprocess_merge".equals(intent)) {
			requireKeys(expects, context + ".expects", "postprocess", "retained_row", "updates_target_fields");
			assertTrue(context + ".expects must include either postprocess_result or postprocess_results", expects.containsKey("postprocess_result") || expects.containsKey("postprocess_results"));
		} else if ("postprocess_conflict".equals(intent)) {
			requireKeys(expects, context + ".expects", "postprocess", "emit_codes");
		} else if ("resolver_path".equals(intent)) {
			requireKeys(expects, context + ".expects", "resolver", "resolver_path", "target_field", "resolver_result");
		} else if ("reconciliation_path".equals(intent)) {
			requireKeys(expects, context + ".expects", "reconciliation_path", "reconciliation_result");
		} else if ("supporting_output_result".equals(intent)) {
			requireKeys(expects, context + ".expects", "related_outputs", "graph_result");
		} else if ("output_result".equals(intent)) {
			requireKeys(expects, context + ".expects", "output_result");
		} else {
			requireKeys(expects, context + ".expects", "related_outputs");
			assertTrue(context + ".expects must define graph_result or graph_issue for related_output_graph scenarios", expects.containsKey("graph_result") || expects.containsKey("graph_issue"));
			if (expects.containsKey("graph_result")) {
				requireKeys(expects, context + ".expects", "updates_target_fields");
			}
		}

		if (expects.containsKey("postprocess")) {
			String postprocess = stringValue(expects.get("postprocess"), context + ".expects.postprocess");
			assertTrue(context + ".expects.postprocess must match a policy postprocess entry", policyReferenceData.postprocessNames.contains(postprocess));
		}
		if (expects.containsKey("retained_row")) {
			assertEnum(stringValue(expects.get("retained_row"), context + ".expects.retained_row"), context + ".expects.retained_row", "left_when_present_else_right");
		}
		if (expects.containsKey("retained_row_indexes")) {
			List<Object> retainedRowIndexes = listValue(expects.get("retained_row_indexes"), context + ".expects.retained_row_indexes");
			assertFalse(context + ".expects.retained_row_indexes must not be empty", retainedRowIndexes.isEmpty());
			for (int i = 0; i < retainedRowIndexes.size(); i++) {
				assertTrue(context + ".expects.retained_row_indexes[" + i + "] must be numeric", retainedRowIndexes.get(i) instanceof Number);
			}
		}
		if (expects.containsKey("updates_target_fields")) {
			List<Object> updatesTargetFields = listValue(expects.get("updates_target_fields"), context + ".expects.updates_target_fields");
			assertFalse(context + ".expects.updates_target_fields must not be empty", updatesTargetFields.isEmpty());
			for (int i = 0; i < updatesTargetFields.size(); i++) {
				String targetField = stringValue(updatesTargetFields.get(i), context + ".expects.updates_target_fields[" + i + "]");
				assertTrue(context + ".expects.updates_target_fields[" + i + "] must match a policy mapping target", policyReferenceData.mappingTargets.contains(targetField));
			}
		}
		if (expects.containsKey("related_outputs")) {
			List<Object> relatedOutputs = listValue(expects.get("related_outputs"), context + ".expects.related_outputs");
			for (int i = 0; i < relatedOutputs.size(); i++) {
				String relatedOutput = stringValue(relatedOutputs.get(i), context + ".expects.related_outputs[" + i + "]");
				assertTrue(context + ".expects.related_outputs[" + i + "] must match a policy related output", policyReferenceData.relatedOutputNames.contains(relatedOutput));
			}
		}
		if (expects.containsKey("emit_codes")) {
			List<Object> emitCodes = listValue(expects.get("emit_codes"), context + ".expects.emit_codes");
			assertFalse(context + ".expects.emit_codes must not be empty", emitCodes.isEmpty());
			for (int i = 0; i < emitCodes.size(); i++) {
				String emitCode = stringValue(emitCodes.get(i), context + ".expects.emit_codes[" + i + "]");
				assertTrue(context + ".expects.emit_codes[" + i + "] must match a policy emit code", policyReferenceData.emitCodes.contains(emitCode));
			}
		}
		if (expects.containsKey("resolver")) {
			String resolver = stringValue(expects.get("resolver"), context + ".expects.resolver");
			assertTrue(context + ".expects.resolver must match a policy resolver", policyReferenceData.resolverSteps.containsKey(resolver));
			if (expects.containsKey("target_field")) {
				String targetField = stringValue(expects.get("target_field"), context + ".expects.target_field");
				assertTrue(context + ".expects.target_field must match a policy mapping target", policyReferenceData.mappingTargets.contains(targetField));
				assertTrue(context + ".expects.target_field must be listed in the resolver used_by fields", policyReferenceData.resolverUsedBy.containsKey(resolver) && policyReferenceData.resolverUsedBy.get(resolver).contains(targetField));
			}
			if (expects.containsKey("resolver_path")) {
				List<Object> resolverPath = listValue(expects.get("resolver_path"), context + ".expects.resolver_path");
				assertFalse(context + ".expects.resolver_path must not be empty", resolverPath.isEmpty());
				List<String> actualSteps = policyReferenceData.resolverSteps.get(resolver);
				int lastIndex = -1;
				for (int i = 0; i < resolverPath.size(); i++) {
					String stepName = stringValue(resolverPath.get(i), context + ".expects.resolver_path[" + i + "]");
					int stepIndex = actualSteps.indexOf(stepName);
					assertTrue(context + ".expects.resolver_path[" + i + "] must match a resolver step", stepIndex >= 0);
					assertTrue(context + ".expects.resolver_path must preserve resolver step order", stepIndex > lastIndex);
					lastIndex = stepIndex;
				}
			}
		}
		if (expects.containsKey("reconciliation_path")) {
			List<Object> reconciliationPath = listValue(expects.get("reconciliation_path"), context + ".expects.reconciliation_path");
			assertFalse(context + ".expects.reconciliation_path must not be empty", reconciliationPath.isEmpty());
			int lastIndex = -1;
			for (int i = 0; i < reconciliationPath.size(); i++) {
				String ruleName = stringValue(reconciliationPath.get(i), context + ".expects.reconciliation_path[" + i + "]");
				int ruleIndex = policyReferenceData.reconciliationRules.indexOf(ruleName);
				assertTrue(context + ".expects.reconciliation_path[" + i + "] must match a reconciliation rule", ruleIndex >= 0);
				assertTrue(context + ".expects.reconciliation_path must preserve rule order", ruleIndex > lastIndex);
				lastIndex = ruleIndex;
			}
		}
		if (expects.containsKey("known_divergence_areas")) {
			List<Object> knownDivergenceAreas = listValue(expects.get("known_divergence_areas"), context + ".expects.known_divergence_areas");
			for (int i = 0; i < knownDivergenceAreas.size(); i++) {
				String area = stringValue(knownDivergenceAreas.get(i), context + ".expects.known_divergence_areas[" + i + "]");
				assertTrue(context + ".expects.known_divergence_areas[" + i + "] must match a policy known divergence area", policyReferenceData.knownDivergenceAreas.contains(area));
			}
		}
		if (expects.containsKey("dataset_link_mode")) {
			assertEnum(stringValue(expects.get("dataset_link_mode"), context + ".expects.dataset_link_mode"), context + ".expects.dataset_link_mode", "reuse_existing_dataset", "clone_updated_dataset");
		}
		if (expects.containsKey("row_changed")) {
			assertTrue(context + ".expects.row_changed must be boolean", expects.get("row_changed") instanceof Boolean);
		}
		if (expects.containsKey("postprocess_result")) {
			validateFixtureScalarMap(mapValue(expects.get("postprocess_result"), context + ".expects.postprocess_result"), context + ".expects.postprocess_result");
		}
		if (expects.containsKey("postprocess_results")) {
			List<Object> postprocessResults = listValue(expects.get("postprocess_results"), context + ".expects.postprocess_results");
			assertFalse(context + ".expects.postprocess_results must not be empty", postprocessResults.isEmpty());
			for (int i = 0; i < postprocessResults.size(); i++) {
				validateFixtureScalarMap(mapValue(postprocessResults.get(i), context + ".expects.postprocess_results[" + i + "]"), context + ".expects.postprocess_results[" + i + "]");
			}
		}
		if (expects.containsKey("resolver_result")) {
			validateFixtureScalarMap(mapValue(expects.get("resolver_result"), context + ".expects.resolver_result"), context + ".expects.resolver_result");
		}
		if (expects.containsKey("reconciliation_result")) {
			validateFixtureScalarMap(mapValue(expects.get("reconciliation_result"), context + ".expects.reconciliation_result"), context + ".expects.reconciliation_result");
		}
		if (expects.containsKey("graph_result")) {
			Map<String, Object> graphResult = mapValue(expects.get("graph_result"), context + ".expects.graph_result");
			assertFalse(context + ".expects.graph_result must not be empty", graphResult.isEmpty());
			Set<String> expectedRelatedOutputs = new HashSet<String>();
			if (expects.containsKey("related_outputs")) {
				List<Object> relatedOutputs = listValue(expects.get("related_outputs"), context + ".expects.related_outputs");
				for (int i = 0; i < relatedOutputs.size(); i++) {
					expectedRelatedOutputs.add(stringValue(relatedOutputs.get(i), context + ".expects.related_outputs[" + i + "]"));
				}
			}
			for (Map.Entry<String, Object> entry : graphResult.entrySet()) {
				assertTrue(context + ".expects.graph_result contains unknown related output '" + entry.getKey() + "'", policyReferenceData.relatedOutputNames.contains(entry.getKey()));
				if (!expectedRelatedOutputs.isEmpty()) {
					assertTrue(context + ".expects.graph_result." + entry.getKey() + " must also be listed in expects.related_outputs", expectedRelatedOutputs.contains(entry.getKey()));
				}
				validateFixtureScalarMap(mapValue(entry.getValue(), context + ".expects.graph_result." + entry.getKey()), context + ".expects.graph_result." + entry.getKey());
			}
		}
		if (expects.containsKey("graph_issue")) {
			validateFixtureScalarMap(mapValue(expects.get("graph_issue"), context + ".expects.graph_issue"), context + ".expects.graph_issue");
		}
	}

	private void validateFixtureScalarMap(Map<String, Object> values, String context) {
		assertFalse(context + " must not be empty", values.isEmpty());
		for (Map.Entry<String, Object> entry : values.entrySet()) {
			Object value = entry.getValue();
			if (value instanceof Map) {
				validateFixtureScalarMap(mapValue(value, context + "." + entry.getKey()), context + "." + entry.getKey());
			} else if (!(value instanceof String) && !(value instanceof Number) && !(value instanceof Boolean) && value != null) {
				fail(context + "." + entry.getKey() + " must be a string, number, boolean, mapping, or null");
			}
		}
	}

	private void validateFixturePolicyContext(Map<String, Object> policyContext, String context) {
		assertAllowedKeys(policyContext, context, FIXTURE_POLICY_CONTEXT_KEYS);
		if (policyContext.containsKey("args")) {
			Map<String, Object> args = mapValue(policyContext.get("args"), context + ".args");
			for (Map.Entry<String, Object> entry : args.entrySet()) {
				if (!(entry.getValue() instanceof String) && entry.getValue() != null) {
					fail(context + ".args." + entry.getKey() + " must be a string or null");
				}
			}
		}
		if (policyContext.containsKey("step_hits")) {
			Map<String, Object> stepHits = mapValue(policyContext.get("step_hits"), context + ".step_hits");
			for (Map.Entry<String, Object> entry : stepHits.entrySet()) {
				assertTrue(context + ".step_hits." + entry.getKey() + " must be boolean", entry.getValue() instanceof Boolean);
			}
		}
		if (policyContext.containsKey("config")) {
			Map<String, Object> config = mapValue(policyContext.get("config"), context + ".config");
			for (Map.Entry<String, Object> entry : config.entrySet()) {
				Object value = entry.getValue();
				if (!(value instanceof String) && !(value instanceof Boolean) && !(value instanceof Number) && value != null) {
					fail(context + ".config." + entry.getKey() + " must be a string, boolean, number, or null");
				}
			}
		}
		if (policyContext.containsKey("state")) {
			validateFixtureScalarMap(mapValue(policyContext.get("state"), context + ".state"), context + ".state");
		}
	}

	private PolicyReferenceData extractPolicyReferenceData(Map<String, Object> policy, Path policyFile) {
		PolicyReferenceData referenceData = new PolicyReferenceData();

		Map<String, Object> source = mapValue(policy.get("source"), context(policyFile, "source"));
		List<Object> sourceFields = listValue(source.get("fields"), context(policyFile, "source.fields"));
		for (int i = 0; i < sourceFields.size(); i++) {
			Map<String, Object> field = mapValue(sourceFields.get(i), context(policyFile, "source.fields[" + i + "]"));
			referenceData.sourceFields.add(stringValue(field.get("name"), context(policyFile, "source.fields[" + i + "].name")));
		}

		List<Object> mappings = listValue(policy.get("mappings"), context(policyFile, "mappings"));
		for (int i = 0; i < mappings.size(); i++) {
			Map<String, Object> mapping = mapValue(mappings.get(i), context(policyFile, "mappings[" + i + "]"));
			referenceData.mappingTargets.add(stringValue(mapping.get("target_field"), context(policyFile, "mappings[" + i + "].target_field")));
		}

		Map<String, Object> reconciliation = mapValue(policy.get("reconciliation"), context(policyFile, "reconciliation"));
		if (reconciliation.containsKey("prerequisite")) {
			Map<String, Object> prerequisite = mapValue(reconciliation.get("prerequisite"), context(policyFile, "reconciliation.prerequisite"));
			if (prerequisite.containsKey("name")) {
				referenceData.reconciliationRules.add(stringValue(prerequisite.get("name"), context(policyFile, "reconciliation.prerequisite.name")));
			}
		}
		List<Object> reconciliationRules = listValue(reconciliation.get("rules"), context(policyFile, "reconciliation.rules"));
		for (int i = 0; i < reconciliationRules.size(); i++) {
			Map<String, Object> rule = mapValue(reconciliationRules.get(i), context(policyFile, "reconciliation.rules[" + i + "]"));
			String ruleName = stringValue(rule.get("name"), context(policyFile, "reconciliation.rules[" + i + "].name"));
			String ruleType = stringValue(rule.get("type"), context(policyFile, "reconciliation.rules[" + i + "].type"));
			if ("search_chain".equals(ruleType)) {
				List<Object> steps = listValue(rule.get("steps"), context(policyFile, "reconciliation.rules[" + i + "].steps"));
				for (int j = 0; j < steps.size(); j++) {
					Map<String, Object> step = mapValue(steps.get(j), context(policyFile, "reconciliation.rules[" + i + "].steps[" + j + "]"));
					referenceData.reconciliationRules.add(stringValue(step.get("name"), context(policyFile, "reconciliation.rules[" + i + "].steps[" + j + "].name")));
				}
			} else {
				referenceData.reconciliationRules.add(ruleName);
			}
		}

		if (policy.containsKey("related_outputs")) {
			List<Object> relatedOutputs = listValue(policy.get("related_outputs"), context(policyFile, "related_outputs"));
			for (int i = 0; i < relatedOutputs.size(); i++) {
				Map<String, Object> relatedOutput = mapValue(relatedOutputs.get(i), context(policyFile, "related_outputs[" + i + "]"));
				referenceData.relatedOutputNames.add(stringValue(relatedOutput.get("name"), context(policyFile, "related_outputs[" + i + "].name")));
			}
		}

		if (policy.containsKey("postprocess")) {
			List<Object> postprocessItems = listValue(policy.get("postprocess"), context(policyFile, "postprocess"));
			for (int i = 0; i < postprocessItems.size(); i++) {
				Map<String, Object> postprocess = mapValue(postprocessItems.get(i), context(policyFile, "postprocess[" + i + "]"));
				String postprocessName = stringValue(postprocess.get("name"), context(policyFile, "postprocess[" + i + "].name"));
				referenceData.postprocessNames.add(postprocessName);
				Map<String, Object> pairRules = mapValue(postprocess.get("pair_rules"), context(policyFile, "postprocess[" + i + "].pair_rules"));
				if (Boolean.TRUE.equals(pairRules.get("include_singletons"))) {
					referenceData.singletonEnabledPostprocessNames.add(postprocessName);
				}
				Map<String, Object> onConflict = mapValue(postprocess.get("on_conflict"), context(policyFile, "postprocess[" + i + "].on_conflict"));
				referenceData.emitCodes.add(stringValue(onConflict.get("code"), context(policyFile, "postprocess[" + i + "].on_conflict.code")));
			}
		}

		if (policy.containsKey("resolvers")) {
			List<Object> resolvers = listValue(policy.get("resolvers"), context(policyFile, "resolvers"));
			for (int i = 0; i < resolvers.size(); i++) {
				Map<String, Object> resolver = mapValue(resolvers.get(i), context(policyFile, "resolvers[" + i + "]"));
				String resolverName = stringValue(resolver.get("name"), context(policyFile, "resolvers[" + i + "].name"));
				List<String> stepNames = new ArrayList<String>();
				List<Object> steps = listValue(resolver.get("steps"), context(policyFile, "resolvers[" + i + "].steps"));
				for (int j = 0; j < steps.size(); j++) {
					Map<String, Object> step = mapValue(steps.get(j), context(policyFile, "resolvers[" + i + "].steps[" + j + "]"));
					stepNames.add(stringValue(step.get("name"), context(policyFile, "resolvers[" + i + "].steps[" + j + "].name")));
					if (step.containsKey("emit")) {
						Map<String, Object> emit = mapValue(step.get("emit"), context(policyFile, "resolvers[" + i + "].steps[" + j + "].emit"));
						referenceData.emitCodes.add(stringValue(emit.get("code"), context(policyFile, "resolvers[" + i + "].steps[" + j + "].emit.code")));
					}
				}
				referenceData.resolverSteps.put(resolverName, stepNames);
				Set<String> usedByTargets = new HashSet<String>();
				if (resolver.containsKey("used_by")) {
					List<Object> usedBy = listValue(resolver.get("used_by"), context(policyFile, "resolvers[" + i + "].used_by"));
					for (int j = 0; j < usedBy.size(); j++) {
						usedByTargets.add(stringValue(usedBy.get(j), context(policyFile, "resolvers[" + i + "].used_by[" + j + "]")));
					}
				}
				referenceData.resolverUsedBy.put(resolverName, usedByTargets);
			}
		}

		if (policy.containsKey("known_divergences")) {
			List<Object> knownDivergences = listValue(policy.get("known_divergences"), context(policyFile, "known_divergences"));
			for (int i = 0; i < knownDivergences.size(); i++) {
				Map<String, Object> divergence = mapValue(knownDivergences.get(i), context(policyFile, "known_divergences[" + i + "]"));
				referenceData.knownDivergenceAreas.add(stringValue(divergence.get("area"), context(policyFile, "known_divergences[" + i + "].area")));
			}
		}

		return referenceData;
	}

	private int validateSearchChainStep(Map<String, Object> step, String context, String sourceTable) {
		requireKeys(step, context, "name", "order", "type", "description");
		String type = stringValue(step.get("type"), context + ".type");
		assertEnum(type, context + ".type", "trace_lookup", "database_query", "history_check");
		assertTrue(context + ".order must be numeric", step.get("order") instanceof Number);
		if (step.containsKey("on_match")) {
			assertEnum(stringValue(step.get("on_match"), context + ".on_match"), context + ".on_match", "update", "return_as_is", "skip");
		}
		if (step.containsKey("on_error")) {
			assertEnum(stringValue(step.get("on_error"), context + ".on_error"), context + ".on_error", "skip", "raise", "return_as_is");
		}

		if ("trace_lookup".equals(type)) {
			assertAllowedKeys(step, context, TRACE_LOOKUP_RULE_KEYS);
			requireKeys(step, context, "bugs_table", "identifier_expr", "on_match");
			assertEquals(context + ".bugs_table must match source.table", sourceTable, stringValue(step.get("bugs_table"), context + ".bugs_table"));
			if (step.containsKey("allow_deleted")) {
				assertTrue(context + ".allow_deleted must be boolean", step.get("allow_deleted") instanceof Boolean);
			}
			if (step.containsKey("external_edit")) {
				Map<String, Object> externalEdit = mapValue(step.get("external_edit"), context + ".external_edit");
				assertAllowedKeys(externalEdit, context + ".external_edit", EXTERNAL_EDIT_KEYS);
				requireKeys(externalEdit, context + ".external_edit", "on_conflict", "message");
				assertEnum(stringValue(externalEdit.get("on_conflict"), context + ".external_edit.on_conflict"), context + ".external_edit.on_conflict", "error", "skip", "return_as_is");
			}
		} else if ("database_query".equals(type)) {
			assertAllowedKeys(step, context, DATABASE_QUERY_RULE_KEYS);
			boolean hasWhere = step.containsKey("where");
			boolean hasQuery = step.containsKey("query");
			boolean hasVariants = step.containsKey("variants");
			assertTrue(context + " must define where, query, or variants", hasWhere || hasQuery || hasVariants);
			assertTrue(context + " must define on_match or result_handling", step.containsKey("on_match") || step.containsKey("result_handling"));
			if (hasWhere) {
				assertNotNull(context + ".table is required when using where", step.get("table"));
			}
			if (step.containsKey("bind")) {
				mapValue(step.get("bind"), context + ".bind");
			}
			if (hasVariants) {
				validateVariants(listValue(step.get("variants"), context + ".variants"), context + ".variants");
			}
			if (step.containsKey("result_handling")) {
				validateResultHandling(mapValue(step.get("result_handling"), context + ".result_handling"), context + ".result_handling");
			}
		} else {
			assertAllowedKeys(step, context, HISTORY_CHECK_STEP_KEYS);
			requireKeys(step, context, "match_condition", "on_match");
		}

		return ((Number) step.get("order")).intValue();
	}

	private void validateTransform(Map<String, Object> transform, String context) {
		assertAllowedKeys(transform, context, TRANSFORM_KEYS);
		requireKeys(transform, context, "type");
		String type = stringValue(transform.get("type"), context + ".type");
		assertEnum(type, context + ".type", "conditional", "concat", "call", "coerce", "constant", "generated", "expr");
		if ("conditional".equals(type)) {
			requireKeys(transform, context, "condition", "if_true", "if_false");
		} else if ("concat".equals(type)) {
			requireKeys(transform, context, "parts");
			listValue(transform.get("parts"), context + ".parts");
		} else if ("call".equals(type)) {
			requireKeys(transform, context, "helper", "args");
			listValue(transform.get("args"), context + ".args");
		} else if ("coerce".equals(type)) {
			requireKeys(transform, context, "input", "output_type");
		} else if ("constant".equals(type)) {
			requireKeys(transform, context, "value");
		} else if ("generated".equals(type)) {
			requireKeys(transform, context, "field");
		} else if ("expr".equals(type)) {
			requireKeys(transform, context, "expr");
		}
	}

	private void validateOutput(Map<String, Object> output, String context, Set<String> mappingTargets) {
		assertAllowedKeys(output, context, OUTPUT_KEYS);
		requireKeys(output, context, "mode");
		String mode = stringValue(output.get("mode"), context + ".mode");
		assertEnum(mode, context + ".mode", "one_to_one", "one_to_many");
		if ("one_to_many".equals(mode)) {
			requireKeys(output, context, "item_helper", "compare_fields", "on_missing_generated", "on_missing_existing", "on_match");
			assertEnum(stringValue(output.get("on_missing_generated"), context + ".on_missing_generated"), context + ".on_missing_generated", "insert", "ignore");
			assertEnum(stringValue(output.get("on_missing_existing"), context + ".on_missing_existing"), context + ".on_missing_existing", "mark_for_deletion", "keep");
			assertEnum(stringValue(output.get("on_match"), context + ".on_match"), context + ".on_match", "return_as_is", "update");
			List<Object> compareFields = listValue(output.get("compare_fields"), context + ".compare_fields");
			assertFalse(context + ".compare_fields must not be empty", compareFields.isEmpty());
			for (int i = 0; i < compareFields.size(); i++) {
				String targetField = stringValue(compareFields.get(i), context + ".compare_fields[" + i + "]");
				assertTrue(context + ".compare_fields[" + i + "] must match a mapping target", mappingTargets.contains(targetField));
			}
		}
	}

	private void validateRelatedOutputs(List<Object> relatedOutputs, String context, Set<String> sourceFieldNames) {
		for (int i = 0; i < relatedOutputs.size(); i++) {
			Map<String, Object> relatedOutput = mapValue(relatedOutputs.get(i), context + "[" + i + "]");
			assertAllowedKeys(relatedOutput, context + "[" + i + "]", RELATED_OUTPUT_KEYS);
			requireKeys(relatedOutput, context + "[" + i + "]", "name", "table", "mode", "relationship", "persistence");
			String relatedTable = stringValue(relatedOutput.get("table"), context + "[" + i + "].table");
			assertEnum(stringValue(relatedOutput.get("mode"), context + "[" + i + "].mode"), context + "[" + i + "].mode", "one_to_one", "zero_or_one", "one_to_many");
			assertEnum(stringValue(relatedOutput.get("relationship"), context + "[" + i + "].relationship"), context + "[" + i + "].relationship", "child", "supporting");
			if (relatedOutput.containsKey("phase")) {
				assertEnum(stringValue(relatedOutput.get("phase"), context + "[" + i + "].phase"), context + "[" + i + "].phase", "before_parent", "after_parent");
			}
			assertEnum(stringValue(relatedOutput.get("persistence"), context + "[" + i + "].persistence"), context + "[" + i + "].persistence", "save_or_update", "cascade", "cache_then_save", "cache_then_link");
			if (relatedOutput.containsKey("key_fields")) {
				List<Object> keyFields = listValue(relatedOutput.get("key_fields"), context + "[" + i + "].key_fields");
				assertFalse(context + "[" + i + "].key_fields must not be empty", keyFields.isEmpty());
				for (int j = 0; j < keyFields.size(); j++) {
					stringValue(keyFields.get(j), context + "[" + i + "].key_fields[" + j + "]");
				}
			}
			if ("one_to_many".equals(stringValue(relatedOutput.get("mode"), context + "[" + i + "].mode"))) {
				assertNotNull(context + "[" + i + "].item_helper is required for one_to_many mode", relatedOutput.get("item_helper"));
			}
			boolean hasRunnableSections = relatedOutput.containsKey("identity") || relatedOutput.containsKey("mappings") || relatedOutput.containsKey("reconciliation") || relatedOutput.containsKey("update_detection") || relatedOutput.containsKey("tracing");
			if (hasRunnableSections) {
				requireKeys(relatedOutput, context + "[" + i + "]", "identity", "mappings", "reconciliation", "update_detection");
				validateIdentity(mapValue(relatedOutput.get("identity"), context + "[" + i + "].identity"), context + "[" + i + "].identity");
				Set<String> childMappingTargets = validateMappings(listValue(relatedOutput.get("mappings"), context + "[" + i + "].mappings"), context + "[" + i + "].mappings", sourceFieldNames);
				validateRelatedOutputReconciliation(mapValue(relatedOutput.get("reconciliation"), context + "[" + i + "].reconciliation"), context + "[" + i + "].reconciliation", childMappingTargets, relatedTable);
				validateUpdateDetection(mapValue(relatedOutput.get("update_detection"), context + "[" + i + "].update_detection"), context + "[" + i + "].update_detection", childMappingTargets);
				if (relatedOutput.containsKey("tracing")) {
					validateTracing(mapValue(relatedOutput.get("tracing"), context + "[" + i + "].tracing"), context + "[" + i + "].tracing", null, relatedTable, false);
				}
			}
		}
	}

	private void validateIdentity(Map<String, Object> identity, String context) {
		assertAllowedKeys(identity, context, TARGET_IDENTITY_KEYS);
		requireKeys(identity, context, "column", "type", "strategy");
		assertEnum(stringValue(identity.get("type"), context + ".type"), context + ".type", "integer", "bigint", "uuid");
		String identityStrategy = stringValue(identity.get("strategy"), context + ".strategy");
		assertEnum(identityStrategy, context + ".strategy", "sequence", "identity_service", "provided");
		if ("sequence".equals(identityStrategy)) {
			assertNotNull(context + ".sequence is required for sequence strategy", identity.get("sequence"));
		}
		if ("identity_service".equals(identityStrategy)) {
			assertNotNull(context + ".identity_service_endpoint is required for identity_service strategy", identity.get("identity_service_endpoint"));
		}
	}

	private Set<String> validateMappings(List<Object> mappings, String context, Set<String> sourceFieldNames) {
		Set<String> mappingTargets = new HashSet<String>();
		for (int i = 0; i < mappings.size(); i++) {
			Map<String, Object> mapping = mapValue(mappings.get(i), context + "[" + i + "]");
			assertAllowedKeys(mapping, context + "[" + i + "]", MAPPING_KEYS);
			requireKeys(mapping, context + "[" + i + "]", "target_field", "type", "nullable");
			boolean hasSourceField = mapping.containsKey("source_field");
			boolean hasTransform = mapping.containsKey("transform");
			assertTrue(context + "[" + i + "] must define exactly one of source_field or transform", hasSourceField ^ hasTransform);
			mappingTargets.add(stringValue(mapping.get("target_field"), context + "[" + i + "].target_field"));
			assertEnum(stringValue(mapping.get("type"), context + "[" + i + "].type"), context + "[" + i + "].type", "string", "integer", "decimal", "boolean", "date", "timestamp");
			assertTrue(context + "[" + i + "].nullable must be boolean", mapping.get("nullable") instanceof Boolean);
			if (hasSourceField) {
				String sourceField = stringValue(mapping.get("source_field"), context + "[" + i + "].source_field");
				assertTrue(context + "[" + i + "].source_field must match a source field", sourceFieldNames.contains(sourceField));
			}
			if (hasTransform) {
				validateTransform(mapValue(mapping.get("transform"), context + "[" + i + "].transform"), context + "[" + i + "].transform");
			}
		}
		return mappingTargets;
	}

	private void validateUpdateDetection(Map<String, Object> updateDetection, String context, Set<String> mappingTargets) {
		assertAllowedKeys(updateDetection, context, UPDATE_DETECTION_KEYS);
		requireKeys(updateDetection, context, "strategy", "fields", "on_change");
		assertEnum(stringValue(updateDetection.get("strategy"), context + ".strategy"), context + ".strategy", "field_comparison", "always", "never");
		assertEnum(stringValue(updateDetection.get("on_change"), context + ".on_change"), context + ".on_change", "mark_updated", "ignore");
		List<Object> updateFields = listValue(updateDetection.get("fields"), context + ".fields");
		for (int i = 0; i < updateFields.size(); i++) {
			String updateField = stringValue(updateFields.get(i), context + ".fields[" + i + "]");
			assertTrue(context + ".fields[" + i + "] must match a mapping target", mappingTargets.contains(updateField));
		}
		if (updateDetection.containsKey("config")) {
			validateConfigList(listValue(updateDetection.get("config"), context + ".config"), context + ".config");
		}
	}

	private void validateTracing(Map<String, Object> tracing, String context, String expectedBugsTable, String expectedSeadTable, boolean requireBugsTableMatch) {
		assertAllowedKeys(tracing, context, TRACING_KEYS);
		requireKeys(tracing, context, "bugs_table", "sead_table", "write_on");
		if (requireBugsTableMatch) {
			assertEquals(context + ".bugs_table must match source.table", expectedBugsTable, stringValue(tracing.get("bugs_table"), context + ".bugs_table"));
		} else {
			stringValue(tracing.get("bugs_table"), context + ".bugs_table");
		}
		assertEquals(context + ".sead_table must match target.table", expectedSeadTable, stringValue(tracing.get("sead_table"), context + ".sead_table"));
		List<Object> writeOn = listValue(tracing.get("write_on"), context + ".write_on");
		for (int i = 0; i < writeOn.size(); i++) {
			assertEnum(stringValue(writeOn.get(i), context + ".write_on[" + i + "]"), context + ".write_on[" + i + "]", "insert", "update");
		}
	}

	private void validatePostprocess(List<Object> postprocessItems, String context, Set<String> mappingTargets) {
		for (int i = 0; i < postprocessItems.size(); i++) {
			Map<String, Object> postprocess = mapValue(postprocessItems.get(i), context + "[" + i + "]");
			assertAllowedKeys(postprocess, context + "[" + i + "]", POSTPROCESS_KEYS);
			requireKeys(postprocess, context + "[" + i + "]", "name", "phase", "mode", "description", "group_by", "pair_rules", "actions", "on_conflict");
			assertEnum(stringValue(postprocess.get("phase"), context + "[" + i + "].phase"), context + "[" + i + "].phase", "after_row_mapping_before_persist");
			assertEnum(stringValue(postprocess.get("mode"), context + "[" + i + "].mode"), context + "[" + i + "].mode", "pair_merge");
			List<Object> groupBy = listValue(postprocess.get("group_by"), context + "[" + i + "].group_by");
			assertFalse(context + "[" + i + "].group_by must not be empty", groupBy.isEmpty());
			for (int j = 0; j < groupBy.size(); j++) {
				stringValue(groupBy.get(j), context + "[" + i + "].group_by[" + j + "]");
			}
			if (postprocess.containsKey("partition_by")) {
				List<Object> partitionBy = listValue(postprocess.get("partition_by"), context + "[" + i + "].partition_by");
				for (int j = 0; j < partitionBy.size(); j++) {
					stringValue(partitionBy.get(j), context + "[" + i + "].partition_by[" + j + "]");
				}
			}
			Map<String, Object> pairRules = mapValue(postprocess.get("pair_rules"), context + "[" + i + "].pair_rules");
			assertAllowedKeys(pairRules, context + "[" + i + "].pair_rules", POSTPROCESS_PAIR_RULE_KEYS);
			requireKeys(pairRules, context + "[" + i + "].pair_rules", "left", "right");
			stringValue(pairRules.get("left"), context + "[" + i + "].pair_rules.left");
			stringValue(pairRules.get("right"), context + "[" + i + "].pair_rules.right");
			if (pairRules.containsKey("include_singletons")) {
				assertTrue(context + "[" + i + "].pair_rules.include_singletons must be boolean", pairRules.get("include_singletons") instanceof Boolean);
			}
			if (postprocess.containsKey("retain_row")) {
				assertEnum(stringValue(postprocess.get("retain_row"), context + "[" + i + "].retain_row"), context + "[" + i + "].retain_row", "left_when_present_else_right");
			}
			List<Object> actions = listValue(postprocess.get("actions"), context + "[" + i + "].actions");
			assertFalse(context + "[" + i + "].actions must not be empty", actions.isEmpty());
			for (int j = 0; j < actions.size(); j++) {
				validatePostprocessAction(mapValue(actions.get(j), context + "[" + i + "].actions[" + j + "]"), context + "[" + i + "].actions[" + j + "]", mappingTargets);
			}
			validateEmit(mapValue(postprocess.get("on_conflict"), context + "[" + i + "].on_conflict"), context + "[" + i + "].on_conflict");
		}
	}

	private void validatePostprocessAction(Map<String, Object> action, String context, Set<String> mappingTargets) {
		assertAllowedKeys(action, context, POSTPROCESS_ACTION_KEYS);
		requireKeys(action, context, "type", "target");
		String type = stringValue(action.get("type"), context + ".type");
		assertEnum(type, context + ".type", "use_pair_range_relative_age", "normalize_pair_uncertainty", "copy_if_empty");
		String targetField = stringValue(action.get("target"), context + ".target");
		assertTrue(context + ".target must match a mapping target", mappingTargets.contains(targetField));
		if ("normalize_pair_uncertainty".equals(type)) {
			if (action.containsKey("clear_when_closed_range")) {
				assertTrue(context + ".clear_when_closed_range must be boolean", action.get("clear_when_closed_range") instanceof Boolean);
			}
			if (action.containsKey("convert_ca_pair_to_standard")) {
				assertTrue(context + ".convert_ca_pair_to_standard must be boolean", action.get("convert_ca_pair_to_standard") instanceof Boolean);
			}
		}
		if ("copy_if_empty".equals(type)) {
			assertNotNull(context + ".from_paired_field is required for copy_if_empty", action.get("from_paired_field"));
			stringValue(action.get("from_paired_field"), context + ".from_paired_field");
		}
	}

	private void validateRelatedOutputReconciliation(Map<String, Object> reconciliation, String context, Set<String> mappingTargets, String relatedTable) {
		assertAllowedKeys(reconciliation, context, RELATED_OUTPUT_RECONCILIATION_KEYS);
		requireKeys(reconciliation, context, "compare_fields", "on_missing_generated", "on_missing_existing", "on_match");
		if (reconciliation.containsKey("existing_rows")) {
			Map<String, Object> existingRows = mapValue(reconciliation.get("existing_rows"), context + ".existing_rows");
			assertAllowedKeys(existingRows, context + ".existing_rows", RELATED_OUTPUT_EXISTING_ROWS_KEYS);
			boolean hasWhere = existingRows.containsKey("where");
			boolean hasQuery = existingRows.containsKey("query");
			assertTrue(context + ".existing_rows must define where or query", hasWhere || hasQuery);
			if (hasWhere && existingRows.containsKey("table")) {
				assertEquals(context + ".existing_rows.table must match related output table", relatedTable, stringValue(existingRows.get("table"), context + ".existing_rows.table"));
			}
			if (existingRows.containsKey("bind")) {
				mapValue(existingRows.get("bind"), context + ".existing_rows.bind");
			}
		}
		List<Object> compareFields = listValue(reconciliation.get("compare_fields"), context + ".compare_fields");
		assertFalse(context + ".compare_fields must not be empty", compareFields.isEmpty());
		for (int i = 0; i < compareFields.size(); i++) {
			String targetField = stringValue(compareFields.get(i), context + ".compare_fields[" + i + "]");
			assertTrue(context + ".compare_fields[" + i + "] must match a child mapping target", mappingTargets.contains(targetField));
		}
		assertEnum(stringValue(reconciliation.get("on_missing_generated"), context + ".on_missing_generated"), context + ".on_missing_generated", "insert", "ignore");
		assertEnum(stringValue(reconciliation.get("on_missing_existing"), context + ".on_missing_existing"), context + ".on_missing_existing", "mark_for_deletion", "keep");
		assertEnum(stringValue(reconciliation.get("on_match"), context + ".on_match"), context + ".on_match", "return_as_is", "update");
	}

	private void validatePrerequisite(Map<String, Object> prerequisite, String context) {
		assertAllowedKeys(prerequisite, context, PREREQUISITE_KEYS);
		requireKeys(prerequisite, context, "description");
		if (prerequisite.containsKey("name")) {
			stringValue(prerequisite.get("name"), context + ".name");
		}
		if (prerequisite.containsKey("config")) {
			validateConfigList(listValue(prerequisite.get("config"), context + ".config"), context + ".config");
		}
	}

	private void validateResolvers(List<Object> resolvers, String context, Set<String> mappingTargets) {
		for (int i = 0; i < resolvers.size(); i++) {
			Map<String, Object> resolver = mapValue(resolvers.get(i), context + "[" + i + "]");
			assertAllowedKeys(resolver, context + "[" + i + "]", RESOLVER_KEYS);
			requireKeys(resolver, context + "[" + i + "]", "name", "description", "returns", "steps");
			assertEnum(stringValue(resolver.get("returns"), context + "[" + i + "].returns"), context + "[" + i + "].returns", "scalar", "entity_ref", "list");
			if (resolver.containsKey("args")) {
				List<Object> args = listValue(resolver.get("args"), context + "[" + i + "].args");
				for (int j = 0; j < args.size(); j++) {
					stringValue(args.get(j), context + "[" + i + "].args[" + j + "]");
				}
			}
			if (resolver.containsKey("config")) {
				validateConfigList(listValue(resolver.get("config"), context + "[" + i + "].config"), context + "[" + i + "].config");
			}
			List<Object> steps = listValue(resolver.get("steps"), context + "[" + i + "].steps");
			assertFalse(context + "[" + i + "].steps must not be empty", steps.isEmpty());
			for (int j = 0; j < steps.size(); j++) {
				validateResolverStep(mapValue(steps.get(j), context + "[" + i + "].steps[" + j + "]"), context + "[" + i + "].steps[" + j + "]");
			}
			if (resolver.containsKey("used_by")) {
				List<Object> usedBy = listValue(resolver.get("used_by"), context + "[" + i + "].used_by");
				for (int j = 0; j < usedBy.size(); j++) {
					String targetField = stringValue(usedBy.get(j), context + "[" + i + "].used_by[" + j + "]");
					assertTrue(context + "[" + i + "].used_by[" + j + "] must match a mapping target", mappingTargets.contains(targetField));
				}
			}
		}
	}

	private void validateResolverStep(Map<String, Object> step, String context) {
		assertAllowedKeys(step, context, RESOLVER_STEP_KEYS);
		requireKeys(step, context, "name", "action", "return");
		String action = stringValue(step.get("action"), context + ".action");
		assertEnum(action, context + ".action", "trace_lookup", "database_query", "emit_issue");
		assertEnum(stringValue(step.get("return"), context + ".return"), context + ".return", "continue", "return_entity", "return_value", "empty_entity", "none");
		if ("trace_lookup".equals(action)) {
			requireKeys(step, context, "bugs_table", "identifier_expr");
		} else if ("database_query".equals(action)) {
			boolean hasWhere = step.containsKey("where");
			boolean hasQuery = step.containsKey("query");
			assertTrue(context + " must define where or query", hasWhere || hasQuery);
			if (hasWhere) {
				assertNotNull(context + ".table is required when using where", step.get("table"));
			}
			if (step.containsKey("bind")) {
				mapValue(step.get("bind"), context + ".bind");
			}
		} else {
			validateEmit(mapValue(step.get("emit"), context + ".emit"), context + ".emit");
		}
	}

	private void validateEmit(Map<String, Object> emit, String context) {
		assertAllowedKeys(emit, context, EMIT_KEYS);
		requireKeys(emit, context, "severity", "code", "message");
		assertEnum(stringValue(emit.get("severity"), context + ".severity"), context + ".severity", "error", "warning", "ignored", "flag");
		if (emit.containsKey("set_flagged")) {
			assertTrue(context + ".set_flagged must be boolean", emit.get("set_flagged") instanceof Boolean);
		}
	}

	private void validateKnownDivergences(List<Object> knownDivergences, String context) {
		for (int i = 0; i < knownDivergences.size(); i++) {
			Map<String, Object> divergence = mapValue(knownDivergences.get(i), context + "[" + i + "]");
			assertAllowedKeys(divergence, context + "[" + i + "]", KNOWN_DIVERGENCE_KEYS);
			requireKeys(divergence, context + "[" + i + "]", "area", "status", "description", "policy_choice");
			assertEnum(stringValue(divergence.get("status"), context + "[" + i + "].status"), context + "[" + i + "].status", "java_bug_suspected", "behavior_ambiguous", "schema_limit");
			assertEnum(stringValue(divergence.get("policy_choice"), context + "[" + i + "].policy_choice"), context + "[" + i + "].policy_choice", "match_java_runtime", "match_intended_behavior", "defer_exact_parity");
		}
	}

	private void validateConfigList(List<Object> configItems, String context) {
		for (int i = 0; i < configItems.size(); i++) {
			Map<String, Object> config = mapValue(configItems.get(i), context + "[" + i + "]");
			assertAllowedKeys(config, context + "[" + i + "]", CONFIG_KEYS);
			requireKeys(config, context + "[" + i + "]", "name", "property", "default", "description");
		}
	}

	private void validateVariants(List<Object> variants, String context) {
		assertFalse(context + " must not be empty", variants.isEmpty());
		for (int i = 0; i < variants.size(); i++) {
			Map<String, Object> variant = mapValue(variants.get(i), context + "[" + i + "]");
			assertAllowedKeys(variant, context + "[" + i + "]", VARIANT_KEYS);
			requireKeys(variant, context + "[" + i + "]", "name", "condition");
			boolean hasWhere = variant.containsKey("where");
			boolean hasQuery = variant.containsKey("query");
			assertTrue(context + "[" + i + "] must define where or query", hasWhere || hasQuery);
			if (hasWhere) {
				assertNotNull(context + "[" + i + "].table is required when using where", variant.get("table"));
			}
			if (variant.containsKey("bind")) {
				mapValue(variant.get("bind"), context + "[" + i + "].bind");
			}
		}
	}

	private void validateResultHandling(Map<String, Object> resultHandling, String context) {
		assertAllowedKeys(resultHandling, context, RESULT_HANDLING_KEYS);
		for (Map.Entry<String, Object> entry : resultHandling.entrySet()) {
			assertEnum(stringValue(entry.getValue(), context + "." + entry.getKey()), context + "." + entry.getKey(), "fall_through", "error", "skip", "return_as_is", "on_match");
		}
	}

	private Map<String, Object> readYamlMap(Path file) throws IOException {
		Yaml yaml = new Yaml();
		try (InputStream inputStream = Files.newInputStream(file)) {
			Object loaded = yaml.load(inputStream);
			return mapValue(loaded, context(file, "document"));
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> mapValue(Object value, String context) {
		assertTrue(context + " must be a mapping", value instanceof Map);
		return (Map<String, Object>) value;
	}

	@SuppressWarnings("unchecked")
	private List<Object> listValue(Object value, String context) {
		assertTrue(context + " must be a list", value instanceof List);
		return (List<Object>) value;
	}

	private String stringValue(Object value, String context) {
		assertTrue(context + " must be a string", value instanceof String);
		return (String) value;
	}

	private void requireKeys(Map<String, Object> map, String context, String... keys) {
		for (String key : keys) {
			if (!map.containsKey(key)) {
				fail(context + " is missing required key '" + key + "'");
			}
		}
	}

	private void assertAllowedKeys(Map<String, Object> map, String context, Set<String> allowedKeys) {
		for (String key : map.keySet()) {
			assertTrue(context + " contains unexpected key '" + key + "'", allowedKeys.contains(key));
		}
	}

	private void assertEnum(String actual, String context, String... allowedValues) {
		assertTrue(context + " must be one of " + Arrays.toString(allowedValues) + " but was '" + actual + "'", setOf(allowedValues).contains(actual));
	}

	private String context(Path policyFile, String section) {
		return policyFile.getFileName().toString() + " " + section;
	}

	private static Set<String> setOf(String... values) {
		return new HashSet<String>(Arrays.asList(values));
	}

	private static final class PolicyReferenceData {
		private final Set<String> sourceFields = new HashSet<String>();
		private final Set<String> mappingTargets = new HashSet<String>();
		private final List<String> reconciliationRules = new ArrayList<String>();
		private final Set<String> relatedOutputNames = new HashSet<String>();
		private final Set<String> postprocessNames = new HashSet<String>();
		private final Set<String> singletonEnabledPostprocessNames = new HashSet<String>();
		private final Set<String> emitCodes = new HashSet<String>();
		private final Set<String> knownDivergenceAreas = new HashSet<String>();
		private final Map<String, List<String>> resolverSteps = new HashMap<String, List<String>>();
		private final Map<String, Set<String>> resolverUsedBy = new HashMap<String, Set<String>>();
	}
}