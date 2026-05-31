package se.sead.reconciliation;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PostprocessPolicyHarnessTest {

	private PolicyFixtureLoader fixtureLoader;
	private PostprocessPolicyHarness postprocessPolicyHarness;

	@Before
	public void setup() {
		fixtureLoader = new PolicyFixtureLoader();
		postprocessPolicyHarness = new PostprocessPolicyHarness();
	}

	@Test
	public void mergeScenarioMatchesPolicyPostprocessPath() throws IOException {
		assertMergeScenarioMatchesPolicy("merge_from_to_range");
	}

	@Test
	public void mergeCaScenarioMatchesPolicyPostprocessPath() throws IOException {
		assertMergeScenarioMatchesPolicy("merge_fromca_toca_range");
	}

	@Test
	public void openEndedFromScenarioMatchesPolicyPostprocessPath() throws IOException {
		assertMergeScenarioMatchesPolicy("retain_open_ended_from_range");
	}

	@Test
	public void openEndedToScenarioMatchesPolicyPostprocessPath() throws IOException {
		assertMergeScenarioMatchesPolicy("retain_open_ended_to_range");
	}

	@Test
	public void openEndedFromCaScenarioMatchesPolicyPostprocessPath() throws IOException {
		assertMergeScenarioMatchesPolicy("retain_open_ended_fromca_range");
	}

	@Test
	public void openEndedToCaScenarioMatchesPolicyPostprocessPath() throws IOException {
		assertMergeScenarioMatchesPolicy("retain_open_ended_toca_range");
	}

	@Test
	public void caAndStandardPairsScenarioMatchesPolicyPostprocessPath() throws IOException {
		assertMergeScenarioMatchesPolicy("retain_ca_and_standard_pairs_separately");
	}

	@Test
	public void openFromCaAndOpenToScenarioMatchesPolicyPostprocessPath() throws IOException {
		assertMergeScenarioMatchesPolicy("retain_open_fromca_and_open_to_separately");
	}

	@Test
	public void notePartitionClosedAndOpenScenarioMatchesPolicyPostprocessPath() throws IOException {
		assertMergeScenarioMatchesPolicy("partition_by_note_group_for_closed_and_open_rows");
	}

	@Test
	public void notePartitionTwoClosedPairsScenarioMatchesPolicyPostprocessPath() throws IOException {
		assertMergeScenarioMatchesPolicy("partition_by_note_group_for_two_closed_pairs");
	}

	@Test
	public void datingMethodPartitionScenarioMatchesPolicyPostprocessPath() throws IOException {
		assertMergeScenarioMatchesPolicy("partition_by_dating_method_for_closed_and_open_rows");
	}

	@Test
	public void conflictScenarioMatchesPolicyPostprocessPath() throws IOException {
		Map<String, Object> fixture = fixtureLoader.loadFixture("datescalendar.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, "conflict_too_many_same_kind_uncertainties");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), "conflict_too_many_same_kind_uncertainties.expects");

		PostprocessPolicyHarness.PostprocessResult result = postprocessPolicyHarness.execute(
				"datescalendar",
				fixtureLoader.stringValue(expects.get("postprocess"), "conflict_too_many_same_kind_uncertainties.expects.postprocess"),
				toSourceRows(scenario)
		);

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("emit_codes"), "conflict_too_many_same_kind_uncertainties.expects.emit_codes"), result.getEmitCodes());
		assertEquals(new ArrayList<String>(), result.getUpdatedFields());
	}

	private void assertMergeScenarioMatchesPolicy(String scenarioName) throws IOException {
		Map<String, Object> fixture = fixtureLoader.loadFixture("datescalendar.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");

		PostprocessPolicyHarness.PostprocessResult result = postprocessPolicyHarness.execute(
				"datescalendar",
				fixtureLoader.stringValue(expects.get("postprocess"), scenarioName + ".expects.postprocess"),
				toSourceRows(scenario)
		);

		assertEquals(fixtureLoader.stringValue(expects.get("retained_row"), scenarioName + ".expects.retained_row"), result.getRetainedRow());
		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("updates_target_fields"), scenarioName + ".expects.updates_target_fields"), result.getUpdatedFields());
		if (expects.containsKey("postprocess_results")) {
			assertEquals(FixtureExpectationHelper.toNestedMapList(fixtureLoader, expects.get("postprocess_results"), scenarioName + ".expects.postprocess_results"), result.getPostprocessResults());
			assertEquals(FixtureExpectationHelper.toIntegerList(fixtureLoader, expects.get("retained_row_indexes"), scenarioName + ".expects.retained_row_indexes"), result.getRetainedRowIndexes());
		} else {
			assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("postprocess_result"), scenarioName + ".expects.postprocess_result"), result.getPostprocessResult());
			assertEquals(Integer.valueOf(0), result.getRetainedRowIndexes().get(0));
		}
		assertEquals(new ArrayList<String>(), result.getEmitCodes());
	}

	private List<Map<String, Object>> toSourceRows(Map<String, Object> scenario) {
		List<Object> rawSourceRows = fixtureLoader.listValue(scenario.get("source_rows"), "source_rows");
		List<Map<String, Object>> sourceRows = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < rawSourceRows.size(); i++) {
			sourceRows.add(fixtureLoader.mapValue(rawSourceRows.get(i), "source_rows[" + i + "]"));
		}
		return sourceRows;
	}

}