package se.sead.reconciliation;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ReconciliationPolicyHarnessTest {

	private PolicyFixtureLoader fixtureLoader;
	private ReconciliationPolicyHarness reconciliationPolicyHarness;

	@Before
	public void setup() {
		fixtureLoader = new PolicyFixtureLoader();
		reconciliationPolicyHarness = new ReconciliationPolicyHarness();
	}

	@Test
	public void mcrNamesTraceLookupScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("mcrnames.fixture.yml", "mcrnames", "trace_lookup_updates_existing_mcr_name");
	}

	@Test
	public void mcrNamesSpeciesLookupScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("mcrnames.fixture.yml", "mcrnames", "species_value_lookup_updates_existing_mcr_name");
	}

	@Test
	public void mcrNamesCreateScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("mcrnames.fixture.yml", "mcrnames", "create_new_mcr_name_when_search_chain_misses");
	}

	@Test
	public void taxaSeasonalityTraceLookupScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("taxaseasonality.fixture.yml", "taxaseasonality", "trace_lookup_updates_existing_taxa_seasonality");
	}

	@Test
	public void taxaSeasonalityHistoryGuardScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("taxaseasonality.fixture.yml", "taxaseasonality", "history_guard_returns_error_carrier");
	}

	@Test
	public void taxaSeasonalityRepositoryLookupScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("taxaseasonality.fixture.yml", "taxaseasonality", "repository_lookup_updates_existing_taxa_seasonality");
	}

	@Test
	public void taxaSeasonalityCreateScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("taxaseasonality.fixture.yml", "taxaseasonality", "create_new_taxa_seasonality_when_search_chain_misses");
	}

	@Test
	public void mcrSummaryLookupScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("mcrsummary.fixture.yml", "mcrsummary", "species_lookup_returns_existing_summary_as_is");
	}

	@Test
	public void mcrSummaryCreateScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("mcrsummary.fixture.yml", "mcrsummary", "create_new_summary_when_species_lookup_misses");
	}

	@Test
	public void birmBeetleCompositeLookupScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("birmbeetledata.fixture.yml", "birmbeetledata", "composite_lookup_returns_existing_birm_data");
	}

	@Test
	public void birmBeetleCreateScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("birmbeetledata.fixture.yml", "birmbeetledata", "create_new_birm_data_when_composite_lookup_misses");
	}

	@Test
	public void speciesCodeLookupScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("species.fixture.yml", "species", "code_lookup_returns_existing_taxonomic_order");
	}

	@Test
	public void speciesCreateScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("species.fixture.yml", "species", "create_new_taxonomic_order_when_code_lookup_misses");
	}

	@Test
	public void traceHitScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("lab.fixture.yml", "lab", "trace_hit_updates_existing_lab");
	}

	@Test
	public void traceHitErrorScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("lab.fixture.yml", "lab", "trace_hit_returns_existing_error_row");
	}

	@Test
	public void labIdLookupScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("lab.fixture.yml", "lab", "lab_id_lookup_updates_existing_lab");
	}

	@Test
	public void createNewScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("lab.fixture.yml", "lab", "create_new_lab_when_no_match_exists");
	}

	@Test
	public void bibliographyTraceHitScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("bibliography.fixture.yml", "bibliography", "trace_hit_updates_existing_biblio");
	}

	@Test
	public void bibliographyTraceErrorScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("bibliography.fixture.yml", "bibliography", "trace_hit_returns_existing_error_row");
	}

	@Test
	public void bibliographyDatabaseLookupScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("bibliography.fixture.yml", "bibliography", "database_lookup_updates_existing_biblio");
	}

	@Test
	public void bibliographyCreateScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("bibliography.fixture.yml", "bibliography", "create_new_biblio_when_no_match_exists");
	}

	@Test
	public void rdbCodeTraceHitScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("rdbcode.fixture.yml", "rdbcode", "trace_hit_updates_existing_rdb_code");
	}

	@Test
	public void rdbCodeTraceErrorScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("rdbcode.fixture.yml", "rdbcode", "trace_hit_returns_existing_error_row");
	}

	@Test
	public void rdbCodeDuplicateGuardScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("rdbcode.fixture.yml", "rdbcode", "duplicate_value_guard_returns_error_carrier");
	}

	@Test
	public void rdbCodeCreateScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("rdbcode.fixture.yml", "rdbcode", "create_new_rdb_code_when_no_match_exists");
	}

	@Test
	public void rdbTraceHitScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("rdb.fixture.yml", "rdb", "trace_hit_updates_existing_rdb");
	}

	@Test
	public void rdbHistoryGuardScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("rdb.fixture.yml", "rdb", "history_conflict_guard_returns_error_carrier");
	}

	@Test
	public void rdbRepositoryLookupScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("rdb.fixture.yml", "rdb", "repository_lookup_updates_existing_rdb");
	}

	@Test
	public void rdbCreateScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("rdb.fixture.yml", "rdb", "create_new_rdb_when_no_match_exists");
	}

	@Test
	public void speciesDistributionLookupScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("speciesdistribution.fixture.yml", "speciesdistribution", "tuple_lookup_updates_existing_distribution");
	}

	@Test
	public void speciesDistributionCreateScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("speciesdistribution.fixture.yml", "speciesdistribution", "create_new_distribution_when_tuple_missing");
	}

	@Test
	public void taxonomicNotesLookupScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("taxanotes.fixture.yml", "taxanotes", "taxonomy_note_lookup_updates_existing_note");
	}

	@Test
	public void taxonomicNotesCreateScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("taxanotes.fixture.yml", "taxanotes", "create_new_taxonomy_note_when_tuple_missing");
	}

	@Test
	public void countryTraceHitScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("country.fixture.yml", "country", "trace_hit_updates_existing_country");
	}

	@Test
	public void countryExistingLookupScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("country.fixture.yml", "country", "existing_country_lookup_updates_existing_country");
	}

	@Test
	public void countryCreateScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("country.fixture.yml", "country", "create_new_country_when_no_match_exists");
	}

	@Test
	public void periodIgnoreScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("period.fixture.yml", "period", "ignore_unknown_period_code_before_trace_lookup");
	}

	@Test
	public void periodTraceHitScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("period.fixture.yml", "period", "trace_hit_updates_existing_period");
	}

	@Test
	public void periodTraceErrorScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("period.fixture.yml", "period", "trace_hit_returns_existing_period_error");
	}

	@Test
	public void periodCreateScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("period.fixture.yml", "period", "create_new_period_when_no_trace_exists");
	}

	@Test
	public void rdbSystemTraceHitScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("rdbsystem.fixture.yml", "rdbsystem", "trace_hit_updates_existing_rdb_system");
	}

	@Test
	public void rdbSystemTraceErrorScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("rdbsystem.fixture.yml", "rdbsystem", "trace_hit_returns_existing_rdb_system_error");
	}

	@Test
	public void rdbSystemCreateScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("rdbsystem.fixture.yml", "rdbsystem", "create_new_rdb_system_when_no_trace_exists");
	}

	@Test
	public void bugsDefinitionTraceHitScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("ecocodedefinition_bugs.fixture.yml", "ecocodedefinition_bugs", "trace_hit_updates_existing_bugs_definition");
	}

	@Test
	public void bugsDefinitionTraceErrorScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("ecocodedefinition_bugs.fixture.yml", "ecocodedefinition_bugs", "trace_hit_returns_existing_bugs_definition_error");
	}

	@Test
	public void bugsDefinitionCreateScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("ecocodedefinition_bugs.fixture.yml", "ecocodedefinition_bugs", "create_new_bugs_definition_when_no_trace_exists");
	}

	@Test
	public void kochDefinitionTraceHitScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("ecocodedefinition_koch.fixture.yml", "ecocodedefinition_koch", "trace_hit_updates_existing_koch_definition");
	}

	@Test
	public void kochDefinitionTraceErrorScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("ecocodedefinition_koch.fixture.yml", "ecocodedefinition_koch", "trace_hit_returns_existing_koch_definition_error");
	}

	@Test
	public void kochDefinitionCreateScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("ecocodedefinition_koch.fixture.yml", "ecocodedefinition_koch", "create_new_koch_definition_when_no_trace_exists");
	}

	@Test
	public void synonymTraceHitScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("speciessynonyms.fixture.yml", "speciessynonyms", "trace_hit_returns_existing_synonym_association");
	}

	@Test
	public void synonymCreateScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("speciessynonyms.fixture.yml", "speciessynonyms", "create_new_synonym_association_when_no_trace_exists");
	}

	@Test
	public void speciesAssociationTraceHitScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("speciesassociation.fixture.yml", "speciesassociation", "trace_hit_updates_existing_species_association");
	}

	@Test
	public void speciesAssociationTraceErrorScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("speciesassociation.fixture.yml", "speciesassociation", "trace_hit_returns_existing_error_row");
	}

	@Test
	public void speciesAssociationCreateScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("speciesassociation.fixture.yml", "speciesassociation", "create_new_species_association_when_no_trace_exists");
	}

	@Test
	public void speciesBiologyLookupScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("speciesbiology.fixture.yml", "speciesbiology", "biology_lookup_returns_existing_text_biology");
	}

	@Test
	public void speciesBiologyCreateScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("speciesbiology.fixture.yml", "speciesbiology", "create_new_text_biology_when_tuple_missing");
	}

	@Test
	public void speciesKeysLookupScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("specieskeys.fixture.yml", "specieskeys", "identification_key_lookup_returns_existing_text_key");
	}

	@Test
	public void speciesKeysCreateScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("specieskeys.fixture.yml", "specieskeys", "create_new_text_key_when_tuple_missing");
	}

	@Test
	public void bugsEcocodeTraceHitScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("ecocode_bugs.fixture.yml", "ecocode_bugs", "trace_hit_returns_existing_bugs_ecocode");
	}

	@Test
	public void bugsEcocodeCreateScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("ecocode_bugs.fixture.yml", "ecocode_bugs", "create_new_bugs_ecocode_when_no_trace_exists");
	}

	@Test
	public void kochEcocodeTraceHitScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("ecocode_koch.fixture.yml", "ecocode_koch", "trace_hit_returns_existing_koch_ecocode");
	}

	@Test
	public void kochEcocodeCreateScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("ecocode_koch.fixture.yml", "ecocode_koch", "create_new_koch_ecocode_when_no_trace_exists");
	}

	@Test
	public void ecocodeGroupTraceHitScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("ecocodegroup.fixture.yml", "ecocodegroup", "trace_hit_updates_existing_ecocode_group");
	}

	@Test
	public void ecocodeGroupCreateScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("ecocodegroup.fixture.yml", "ecocodegroup", "create_new_ecocode_group_when_no_trace_exists");
	}

	@Test
	public void siteReferenceMissingSiteScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("sitereferences.fixture.yml", "sitereferences", "missing_site_prerequisite_returns_guard_error");
	}

	@Test
	public void siteReferenceMissingReferenceScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("sitereferences.fixture.yml", "sitereferences", "missing_reference_prerequisite_returns_guard_error");
	}

	@Test
	public void siteReferenceMissingBibliographyScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("sitereferences.fixture.yml", "sitereferences", "missing_bibliography_prerequisite_returns_guard_error");
	}

	@Test
	public void siteReferenceTupleLookupScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("sitereferences.fixture.yml", "sitereferences", "tuple_lookup_returns_existing_site_reference");
	}

	@Test
	public void siteReferenceCreateScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("sitereferences.fixture.yml", "sitereferences", "create_new_site_reference_when_tuple_missing");
	}

	@Test
	public void siteMissingCountryScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("site.fixture.yml", "site", "missing_country_prerequisite_returns_guard_error");
	}

	@Test
	public void siteUpdateDisallowedScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("site.fixture.yml", "site", "trace_hit_updates_disallowed_returns_guard_error");
	}

	@Test
	public void siteTraceHitScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("site.fixture.yml", "site", "trace_hit_updates_existing_site");
	}

	@Test
	public void siteTraceErrorScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("site.fixture.yml", "site", "trace_hit_returns_existing_error_row");
	}

	@Test
	public void siteNameLocationSingleResultScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("site.fixture.yml", "site", "name_location_lookup_returns_site_exists_error");
	}

	@Test
	public void siteNameLocationMultipleResultScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("site.fixture.yml", "site", "name_location_lookup_returns_multiple_sites_error");
	}

	@Test
	public void siteCreateScenarioMatchesPolicyReconciliationPath() throws IOException {
		assertScenarioMatchesPolicy("site.fixture.yml", "site", "create_new_site_when_no_match_exists");
	}

	private void assertScenarioMatchesPolicy(String fixtureFileName, String policyName, String scenarioName) throws IOException {
		Map<String, Object> fixture = fixtureLoader.loadFixture(fixtureFileName);
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> stepHitsRaw = fixtureLoader.mapValue(policyContext.get("step_hits"), scenarioName + ".policy_context.step_hits");
		Map<String, Boolean> stepHits = new LinkedHashMap<String, Boolean>();
		for (Map.Entry<String, Object> entry : stepHitsRaw.entrySet()) {
			stepHits.put(entry.getKey(), fixtureLoader.booleanValue(entry.getValue(), scenarioName + ".policy_context.step_hits." + entry.getKey()));
		}
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		ReconciliationPolicyHarness.ReconciliationResult result = reconciliationPolicyHarness.execute(policyName, stepHits, state, sourceRow);

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("reconciliation_path"), scenarioName + ".expects.reconciliation_path"), result.getPath());
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("reconciliation_result"), scenarioName + ".expects.reconciliation_result"), result.getReconciliationResult());
	}
}