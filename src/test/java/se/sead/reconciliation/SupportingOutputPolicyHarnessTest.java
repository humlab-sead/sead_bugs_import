package se.sead.reconciliation;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SupportingOutputPolicyHarnessTest {

	private PolicyFixtureLoader fixtureLoader;
	private SupportingOutputPolicyHarness supportingOutputPolicyHarness;

	@Before
	public void setup() {
		fixtureLoader = new PolicyFixtureLoader();
		supportingOutputPolicyHarness = new SupportingOutputPolicyHarness();
	}

	@Test
	public void createDatasetScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("create_dataset_when_missing");
	}

	@Test
	public void updateDatasetScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("update_existing_dataset_when_values_change");
	}

	@Test
	public void keepDatasetScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("keep_existing_dataset_when_values_match");
	}

	@Test
	public void createAnalysisEntityScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("create_analysis_entity_when_missing");
	}

	@Test
	public void updateAnalysisEntityScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("update_existing_analysis_entity_when_values_change");
	}

	@Test
	public void keepAnalysisEntityScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("keep_existing_analysis_entity_when_values_match");
	}

	@Test
	public void createGeochronologyDatasetScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("datesradio.fixture.yml", "datesradio", "create_dataset_for_new_geochronology_row");
	}

	@Test
	public void createGeochronologyAnalysisEntityScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("datesradio.fixture.yml", "datesradio", "create_analysis_entity_for_new_geochronology_row");
	}

	@Test
	public void createCalendarRelativeAgeScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("datescalendar.fixture.yml", "datescalendar", "create_single_relative_age_when_missing");
	}

	@Test
	public void reuseCalendarRelativeAgeScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("datescalendar.fixture.yml", "datescalendar", "reuse_single_relative_age_when_present");
	}

	@Test
	public void createCalendarDatasetScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("datescalendar.fixture.yml", "datescalendar", "create_calendar_dataset_when_missing");
	}

	@Test
	public void updateCalendarDatasetScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("datescalendar.fixture.yml", "datescalendar", "update_existing_calendar_dataset_when_values_change");
	}

	@Test
	public void keepCalendarDatasetScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("datescalendar.fixture.yml", "datescalendar", "keep_existing_calendar_dataset_when_values_match");
	}

	@Test
	public void createCalendarAnalysisEntityScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("datescalendar.fixture.yml", "datescalendar", "create_calendar_analysis_entity_when_missing");
	}

	@Test
	public void updateCalendarAnalysisEntityScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("datescalendar.fixture.yml", "datescalendar", "update_existing_calendar_analysis_entity_when_values_change");
	}

	@Test
	public void keepCalendarAnalysisEntityScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("datescalendar.fixture.yml", "datescalendar", "keep_existing_calendar_analysis_entity_when_values_match");
	}

	@Test
	public void createSampleDimensionsScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("sample.fixture.yml", "sample", "create_sample_dimensions_when_missing");
	}

	@Test
	public void updateSampleDimensionsScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("sample.fixture.yml", "sample", "update_existing_sample_dimensions_when_values_change");
	}

	@Test
	public void keepSampleDimensionsScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("sample.fixture.yml", "sample", "keep_existing_sample_dimensions_when_values_match");
	}

	@Test
	public void deleteSampleDimensionScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("sample.fixture.yml", "sample", "delete_lower_sample_dimension_when_value_missing");
	}

	@Test
	public void createDatasetContactSupportingContactScenarioMatchesPolicy() throws IOException {
		assertScenarioMatchesPolicy("datasetcontacts.fixture.yml", "datasetcontacts", "create_identified_by_contact_when_unmatched");
	}

	@Test
	public void repositoryDatasetContactSupportingContactScenarioMatchesPolicy() throws IOException {
		assertScenarioMatchesPolicy("datasetcontacts.fixture.yml", "datasetcontacts", "reuse_repository_contact_for_identified_by_name");
	}

	@Test
	public void cachedDatasetContactSupportingContactScenarioMatchesPolicy() throws IOException {
		assertScenarioMatchesPolicy("datasetcontacts.fixture.yml", "datasetcontacts", "reuse_cached_contact_before_repository_lookup");
	}

	@Test
	public void parseOrderDatasetContactSupportingContactScenarioMatchesPolicy() throws IOException {
		assertScenarioMatchesPolicy("datasetcontacts.fixture.yml", "datasetcontacts", "create_identified_by_and_specimen_contacts_in_parse_order");
	}

	@Test
	public void createSpecimenRepositoryDatasetContactSupportingContactScenarioMatchesPolicy() throws IOException {
		assertScenarioMatchesPolicy("datasetcontacts.fixture.yml", "datasetcontacts", "create_specimen_repository_contact_when_unmatched");
	}

	@Test
	public void repositorySpecimenRepositoryDatasetContactSupportingContactScenarioMatchesPolicy() throws IOException {
		assertScenarioMatchesPolicy("datasetcontacts.fixture.yml", "datasetcontacts", "reuse_repository_specimen_repository_contact");
	}

	@Test
	public void createTaxaFamilyScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("species.fixture.yml", "species", "create_taxa_family_when_missing");
	}

	@Test
	public void reuseTaxaFamilyScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("species.fixture.yml", "species", "reuse_existing_taxa_family");
	}

	@Test
	public void createTaxaGenusScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("species.fixture.yml", "species", "create_taxa_genus_when_missing");
	}

	@Test
	public void reuseTaxaGenusScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("species.fixture.yml", "species", "reuse_existing_taxa_genus");
	}

	@Test
	public void createTaxaAuthorScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("species.fixture.yml", "species", "create_taxa_author_when_missing");
	}

	@Test
	public void reuseTaxaAuthorScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("species.fixture.yml", "species", "reuse_existing_taxa_author");
	}

	@Test
	public void nullTaxaAuthorScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("species.fixture.yml", "species", "return_null_taxa_author_when_authority_missing");
	}

	@Test
	public void createTaxaSpeciesScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("species.fixture.yml", "species", "create_taxa_species_when_missing");
	}

	@Test
	public void reuseTaxaSpeciesScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("species.fixture.yml", "species", "reuse_existing_taxa_species");
	}

	@Test
	public void createTaxaSpeciesWithoutAuthorScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("species.fixture.yml", "species", "create_taxa_species_without_author_when_authority_missing");
	}

	@Test
	public void reuseTaxaSpeciesWithoutAuthorScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("species.fixture.yml", "species", "reuse_existing_taxa_species_without_author");
	}

	@Test
	public void noDataTaxaSpeciesScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("species.fixture.yml", "species", "reuse_no_data_taxa_species_shortcut");
	}

	@Test
	public void cloneFossilDatasetScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("fossil.fixture.yml", "fossil", "clone_fossil_dataset_when_updates_disabled");
	}

	@Test
	public void reuseFossilDatasetScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("fossil.fixture.yml", "fossil", "reuse_existing_fossil_dataset_when_updates_enabled");
	}

	@Test
	public void createFossilAnalysisEntityScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("fossil.fixture.yml", "fossil", "create_fossil_analysis_entity_when_missing");
	}

	@Test
	public void reuseFossilAnalysisEntityScenarioMatchesPolicySupportingOutputResult() throws IOException {
		assertScenarioMatchesPolicy("fossil.fixture.yml", "fossil", "reuse_existing_fossil_analysis_entity_when_present");
	}

	private void assertScenarioMatchesPolicy(String scenarioName) throws IOException {
		assertScenarioMatchesPolicy("datesperiod.fixture.yml", "datesperiod", scenarioName);
	}

	private void assertScenarioMatchesPolicy(String fixtureName, String policyName, String scenarioName) throws IOException {
		Map<String, Object> fixture = fixtureLoader.loadFixture(fixtureName);
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> args = fixtureLoader.mapValue(policyContext.get("args"), scenarioName + ".policy_context.args");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		SupportingOutputPolicyHarness.SupportingOutputResult result = supportingOutputPolicyHarness.execute(policyName, args, state, sourceRow);

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("related_outputs"), scenarioName + ".expects.related_outputs"), result.getRelatedOutputs());
		if (expects.containsKey("row_changed")) {
			assertEquals(fixtureLoader.booleanValue(expects.get("row_changed"), scenarioName + ".expects.row_changed"), result.isRowChanged());
		}
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("graph_result"), scenarioName + ".expects.graph_result"), result.getGraphResult());
	}
}