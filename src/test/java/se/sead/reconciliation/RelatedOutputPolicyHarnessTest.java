package se.sead.reconciliation;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RelatedOutputPolicyHarnessTest {

	private PolicyFixtureLoader fixtureLoader;
	private RelatedOutputPolicyHarness relatedOutputPolicyHarness;

	@Before
	public void setup() {
		fixtureLoader = new PolicyFixtureLoader();
		relatedOutputPolicyHarness = new RelatedOutputPolicyHarness();
	}

	@Test
	public void cloneDatasetScenarioMatchesPolicyRelatedOutputResult() throws IOException {
		assertScenarioMatchesPolicy("clone_dataset_when_updates_disabled");
	}

	@Test
	public void reuseDatasetScenarioMatchesPolicyRelatedOutputResult() throws IOException {
		assertScenarioMatchesPolicy("reuse_dataset_when_updates_enabled");
	}

	@Test
	public void reuseExistingAnalysisEntityScenarioMatchesPolicyRelatedOutputResult() throws IOException {
		assertScenarioMatchesPolicy("reuse_existing_analysis_entity_when_present");
	}

	@Test
	public void duplicateAnalysisEntitiesScenarioMatchesPolicyGraphIssue() throws IOException {
		assertScenarioMatchesPolicy("duplicate_analysis_entities_emit_graph_issue");
	}

	@Test
	public void missingSampleCodeScenarioMatchesPolicyGraphIssue() throws IOException {
		assertScenarioMatchesPolicy("missing_sample_code_emits_graph_issue");
	}

	@Test
	public void datesPeriodCreateGraphScenarioMatchesPolicyRelatedOutputResult() throws IOException {
		assertScenarioMatchesPolicy("datesperiod.fixture.yml", "datesperiod", "create_related_output_graph_when_support_rows_missing");
	}

	@Test
	public void datesPeriodUpdateGraphScenarioMatchesPolicyRelatedOutputResult() throws IOException {
		assertScenarioMatchesPolicy("datesperiod.fixture.yml", "datesperiod", "update_related_output_graph_when_values_change");
	}

	@Test
	public void datesPeriodKeepGraphScenarioMatchesPolicyRelatedOutputResult() throws IOException {
		assertScenarioMatchesPolicy("datesperiod.fixture.yml", "datesperiod", "keep_related_output_graph_when_values_match");
	}

	@Test
	public void datesPeriodBlankSampleScenarioMatchesPolicyGraphIssue() throws IOException {
		assertScenarioMatchesPolicy("datesperiod.fixture.yml", "datesperiod", "blank_sample_code_emits_datesperiod_graph_issue");
	}

	@Test
	public void datesPeriodMissingSampleTraceScenarioMatchesPolicyGraphIssue() throws IOException {
		assertScenarioMatchesPolicy("datesperiod.fixture.yml", "datesperiod", "missing_sample_trace_emits_datesperiod_graph_issue");
	}

	@Test
	public void datesPeriodUnknownUncertaintyScenarioMatchesPolicyGraphIssue() throws IOException {
		assertScenarioMatchesPolicy("datesperiod.fixture.yml", "datesperiod", "unknown_uncertainty_emits_datesperiod_graph_issue");
	}

	@Test
	public void datesRadioCreateGraphScenarioMatchesPolicyRelatedOutputResult() throws IOException {
		assertScenarioMatchesPolicy("datesradio.fixture.yml", "datesradio", "create_related_output_graph_for_new_geochronology_row");
	}

	@Test
	public void datesRadioMissingSampleScenarioMatchesPolicyGraphIssue() throws IOException {
		assertScenarioMatchesPolicy("datesradio.fixture.yml", "datesradio", "missing_sample_emits_geochronology_graph_issue");
	}

	@Test
	public void datesRadioMissingMethodScenarioMatchesPolicyGraphIssue() throws IOException {
		assertScenarioMatchesPolicy("datesradio.fixture.yml", "datesradio", "missing_method_emits_geochronology_graph_issue");
	}

	@Test
	public void datesRadioMissingDateScenarioMatchesPolicyGraphIssue() throws IOException {
		assertScenarioMatchesPolicy("datesradio.fixture.yml", "datesradio", "missing_date_emits_geochronology_graph_issue");
	}

	@Test
	public void datesRadioUnknownUncertaintyScenarioMatchesPolicyGraphIssue() throws IOException {
		assertScenarioMatchesPolicy("datesradio.fixture.yml", "datesradio", "unknown_uncertainty_emits_geochronology_graph_issue");
	}

	@Test
	public void datesCalendarCreateGraphScenarioMatchesPolicyRelatedOutputResult() throws IOException {
		assertScenarioMatchesPolicy("datescalendar.fixture.yml", "datescalendar", "create_related_output_graph_for_new_calendar_row");
	}

	@Test
	public void speciesCreateGraphScenarioMatchesPolicyRelatedOutputResult() throws IOException {
		assertScenarioMatchesPolicy("species.fixture.yml", "species", "create_species_related_output_graph_when_tree_missing");
	}

	@Test
	public void speciesReuseGraphScenarioMatchesPolicyRelatedOutputResult() throws IOException {
		assertScenarioMatchesPolicy("species.fixture.yml", "species", "reuse_species_related_output_graph_when_tree_exists");
	}

	private void assertScenarioMatchesPolicy(String scenarioName) throws IOException {
		assertScenarioMatchesPolicy("fossil.fixture.yml", "fossil", scenarioName);
	}

	private void assertScenarioMatchesPolicy(String fixtureName, String policyName, String scenarioName) throws IOException {
		Map<String, Object> fixture = fixtureLoader.loadFixture(fixtureName);
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> config = fixtureLoader.mapValue(policyContext.get("config"), scenarioName + ".policy_context.config");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		RelatedOutputPolicyHarness.RelatedOutputResult result = relatedOutputPolicyHarness.execute(policyName, config, state, sourceRow);

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("related_outputs"), scenarioName + ".expects.related_outputs"), result.getRelatedOutputs());
		if (expects.containsKey("row_changed")) {
			assertEquals(fixtureLoader.booleanValue(expects.get("row_changed"), scenarioName + ".expects.row_changed"), result.isRowChanged());
		}
		if (expects.containsKey("updates_target_fields")) {
			assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("updates_target_fields"), scenarioName + ".expects.updates_target_fields"), result.getUpdatedTargetFields());
		}
		if (expects.containsKey("dataset_link_mode")) {
			assertEquals(fixtureLoader.stringValue(expects.get("dataset_link_mode"), scenarioName + ".expects.dataset_link_mode"), result.getDatasetLinkMode());
		}
		if (expects.containsKey("graph_result")) {
			assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("graph_result"), scenarioName + ".expects.graph_result"), result.getGraphResult());
		}
		if (expects.containsKey("graph_issue")) {
			assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("graph_issue"), scenarioName + ".expects.graph_issue"), result.getGraphIssue());
		}
	}
}