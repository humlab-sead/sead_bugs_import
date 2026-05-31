package se.sead.reconciliation;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class OutputPolicyHarnessTest {

	private PolicyFixtureLoader fixtureLoader;
	private OutputPolicyHarness outputPolicyHarness;

	@Before
	public void setup() {
		fixtureLoader = new PolicyFixtureLoader();
		outputPolicyHarness = new OutputPolicyHarness();
	}

	@Test
	public void createDatasetContactsScenarioMatchesPolicyOutputResult() throws IOException {
		assertScenarioMatchesPolicy("create_dataset_contacts_when_no_existing_rows");
	}

	@Test
	public void appendOnlyUnmatchedDatasetContactsScenarioMatchesPolicyOutputResult() throws IOException {
		assertScenarioMatchesPolicy("keep_existing_and_append_only_unmatched_generated_contacts");
	}

	@Test
	public void keepExistingDatasetContactsScenarioMatchesPolicyOutputResult() throws IOException {
		assertScenarioMatchesPolicy("keep_only_existing_contacts_when_all_generated_match");
	}

	@Test
	public void keepExistingWhenNoGeneratedDatasetContactsScenarioMatchesPolicyOutputResult() throws IOException {
		assertScenarioMatchesPolicy("keep_existing_contacts_when_no_generated_contacts");
	}

	@Test
	public void createSiteOtherRecordsScenarioMatchesPolicyOutputResult() throws IOException {
		assertScenarioMatchesPolicy("siteotherproxies.fixture.yml", "siteotherproxies", "create_site_other_records_when_none_exist");
	}

	@Test
	public void updateSiteOtherRecordsScenarioMatchesPolicyOutputResult() throws IOException {
		assertScenarioMatchesPolicy("siteotherproxies.fixture.yml", "siteotherproxies", "keep_existing_mark_missing_and_append_new_site_other_records");
	}

	@Test
	public void keepSiteOtherRecordsScenarioMatchesPolicyOutputResult() throws IOException {
		assertScenarioMatchesPolicy("siteotherproxies.fixture.yml", "siteotherproxies", "keep_existing_site_other_records_when_enabled_proxies_match");
	}

	@Test
	public void deleteSiteOtherRecordsScenarioMatchesPolicyOutputResult() throws IOException {
		assertScenarioMatchesPolicy("siteotherproxies.fixture.yml", "siteotherproxies", "mark_existing_site_other_records_for_deletion_when_no_proxies_enabled");
	}

	@Test
	public void createSiteLocationsScenarioMatchesPolicyOutputResult() throws IOException {
		assertScenarioMatchesPolicy("sitelocations.fixture.yml", "sitelocations", "create_site_locations_when_none_exist");
	}

	@Test
	public void updateSiteLocationsScenarioMatchesPolicyOutputResult() throws IOException {
		assertScenarioMatchesPolicy("sitelocations.fixture.yml", "sitelocations", "mark_missing_and_append_new_site_locations_when_lists_differ");
	}

	@Test
	public void keepSiteLocationsScenarioMatchesPolicyOutputResult() throws IOException {
		assertScenarioMatchesPolicy("sitelocations.fixture.yml", "sitelocations", "keep_existing_site_locations_when_lists_match");
	}

	@Test
	public void deleteAllSiteLocationsScenarioMatchesPolicyOutputResult() throws IOException {
		assertScenarioMatchesPolicy("sitelocations.fixture.yml", "sitelocations", "mark_all_existing_site_locations_for_deletion_when_generated_empty");
	}

	@Test
	public void missingImportedSiteScenarioMatchesPolicyOutputResult() throws IOException {
		assertScenarioMatchesPolicy("sitelocations.fixture.yml", "sitelocations", "return_single_error_row_when_imported_site_missing");
	}

	@Test
	public void locationManagerErrorScenarioMatchesPolicyOutputResult() throws IOException {
		assertScenarioMatchesPolicy("sitelocations.fixture.yml", "sitelocations", "return_generated_rows_when_location_manager_reports_error");
	}

	private void assertScenarioMatchesPolicy(String scenarioName) throws IOException {
		Map<String, Object> fixture = fixtureLoader.loadFixture("datasetcontacts.fixture.yml");
		assertScenarioMatchesPolicy("datasetcontacts.fixture.yml", "datasetcontacts", scenarioName);
	}

	private void assertScenarioMatchesPolicy(String fixtureName, String policyName, String scenarioName) throws IOException {
		Map<String, Object> fixture = fixtureLoader.loadFixture(fixtureName);
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> args = fixtureLoader.mapValue(policyContext.get("args"), scenarioName + ".policy_context.args");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		OutputPolicyHarness.OutputResult result = outputPolicyHarness.execute(policyName, args, state, sourceRow);

		if (expects.containsKey("row_changed")) {
			assertEquals(fixtureLoader.booleanValue(expects.get("row_changed"), scenarioName + ".expects.row_changed"), result.isRowChanged());
		}
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("output_result"), scenarioName + ".expects.output_result"), result.getOutputResult());
	}
}