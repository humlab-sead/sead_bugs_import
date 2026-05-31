package se.sead.bugsimport.taxaseasonality;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.taxaseasonality.bugsmodel.SeasonActiveAdult;
import se.sead.bugsimport.taxaseasonality.search.SeasonalitySearchRule;
import se.sead.bugsimport.taxaseasonality.seadmodel.TaxaSeasonality;
import se.sead.model.TestTaxaSeasonality;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TaxaSeasonalityBugsTableRowConverterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;
	private RecordingSeasonalitySearchRule traceLookup;
	private RecordingSeasonalitySearchRule historyGuard;
	private RecordingSeasonalitySearchRule repositoryLookup;
	private RecordingSeasonalitySearchRule createNew;
	private TaxaSeasonalityBugsTableRowConverter rowConverter;

	@Before
	public void setup() throws Exception {
		fixtureLoader = new PolicyFixtureLoader();
		traceLookup = new RecordingSeasonalitySearchRule("trace_lookup");
		historyGuard = new RecordingSeasonalitySearchRule("updated_since_last_import_guard");
		repositoryLookup = new RecordingSeasonalitySearchRule("repository_lookup");
		createNew = new RecordingSeasonalitySearchRule("create_new");
		rowConverter = new TaxaSeasonalityBugsTableRowConverter();
		injectField(rowConverter, "searchRules", Arrays.<SeasonalitySearchRule>asList(traceLookup, historyGuard, repositoryLookup, createNew));
	}

	@Test
	public void traceLookupFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("trace_lookup_updates_existing_taxa_seasonality");
	}

	@Test
	public void historyGuardFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("history_guard_returns_error_carrier");
	}

	@Test
	public void repositoryLookupFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("repository_lookup_updates_existing_taxa_seasonality");
	}

	@Test
	public void createNewFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_new_taxa_seasonality_when_search_chain_misses");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("taxaseasonality.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		configureScenario(policyContext, state, scenarioName);
		TaxaSeasonality result = rowConverter.convertForDataRow(createBugsRow(sourceRow, scenarioName));

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("reconciliation_path"), scenarioName + ".expects.reconciliation_path"), actualPath(result));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("reconciliation_result"), scenarioName + ".expects.reconciliation_result"), actualReconciliationResult(result, sourceRow));
	}

	private void configureScenario(Map<String, Object> policyContext, Map<String, Object> state, String scenarioName) {
		Map<String, Object> stepHits = fixtureLoader.mapValue(policyContext.get("step_hits"), scenarioName + ".policy_context.step_hits");
		traceLookup.reset();
		historyGuard.reset();
		repositoryLookup.reset();
		createNew.reset();
		if (Boolean.TRUE.equals(stepHits.get("trace_lookup"))) {
			traceLookup.setMatch(TestTaxaSeasonality.create(fixtureLoader.integerValue(state.get("existing_row_id"), scenarioName + ".policy_context.state.existing_row_id"), null, null, null, null));
		}
		if (Boolean.TRUE.equals(stepHits.get("updated_since_last_import_guard"))) {
			TaxaSeasonality errorCarrier = TestTaxaSeasonality.create(null, null, null, null, null);
			errorCarrier.addError(fixtureLoader.stringValue(state.get("guard_error_message"), scenarioName + ".policy_context.state.guard_error_message"));
			historyGuard.setMatch(errorCarrier);
		}
		if (Boolean.TRUE.equals(stepHits.get("repository_lookup"))) {
			repositoryLookup.setMatch(TestTaxaSeasonality.create(fixtureLoader.integerValue(state.get("existing_row_id"), scenarioName + ".policy_context.state.existing_row_id"), null, null, null, null));
		}
		if (!Boolean.TRUE.equals(stepHits.get("trace_lookup")) && !Boolean.TRUE.equals(stepHits.get("updated_since_last_import_guard")) && !Boolean.TRUE.equals(stepHits.get("repository_lookup"))) {
			createNew.setMatch(TestTaxaSeasonality.create(null, null, null, null, null));
		}
	}

	private SeasonActiveAdult createBugsRow(Map<String, Object> sourceRow, String scenarioName) {
		SeasonActiveAdult bugs = new SeasonActiveAdult();
		bugs.setCode(doubleValue(sourceRow.get("CODE"), scenarioName + ".source_rows[0].CODE"));
		bugs.setSeason(fixtureLoader.stringValue(sourceRow.get("HSeason"), scenarioName + ".source_rows[0].HSeason"));
		bugs.setCountryCode(fixtureLoader.stringValue(sourceRow.get("CountryCode"), scenarioName + ".source_rows[0].CountryCode"));
		return bugs;
	}

	private List<String> actualPath(TaxaSeasonality result) {
		List<String> path = new ArrayList<String>();
		path.addAll(traceLookup.getRecordedPath());
		path.addAll(historyGuard.getRecordedPath());
		path.addAll(repositoryLookup.getRecordedPath());
		path.addAll(createNew.getRecordedPath());
		return path;
	}

	private Map<String, Object> actualReconciliationResult(TaxaSeasonality result, Map<String, Object> sourceRow) {
		Map<String, Object> reconciliationResult = new LinkedHashMap<String, Object>();
		String source = traceLookup.didMatch()
				? "trace_lookup"
				: (historyGuard.didMatch() ? "updated_since_last_import_guard" : (repositoryLookup.didMatch() ? "repository_lookup" : "create_new"));
		if (!result.isErrorFree()) {
			reconciliationResult.put("result_kind", "return_guard_error");
			Map<String, Object> issue = new LinkedHashMap<String, Object>();
			issue.put("severity", "error");
			issue.put("message", result.getErrorMessages().get(0));
			reconciliationResult.put("issue", issue);
		} else if (result.getId() == null) {
			reconciliationResult.put("result_kind", "insert_new");
		} else {
			reconciliationResult.put("result_kind", "update_existing");
		}
		reconciliationResult.put("source", source);
		reconciliationResult.put("row_id", result.getId());
		reconciliationResult.put("species_code", doubleValue(sourceRow.get("CODE"), "sourceRow.CODE").intValue());
		return reconciliationResult;
	}

	private Double doubleValue(Object value, String context) {
		if (!(value instanceof Number)) {
			throw new IllegalArgumentException(context + " must be numeric");
		}
		return ((Number) value).doubleValue();
	}

	private void injectField(Object target, String fieldName, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

	private static class RecordingSeasonalitySearchRule implements SeasonalitySearchRule {
		private final String stepName;
		private final List<String> recordedPath = new ArrayList<String>();
		private TaxaSeasonality match;

		RecordingSeasonalitySearchRule(String stepName) {
			this.stepName = stepName;
		}

		void setMatch(TaxaSeasonality match) {
			this.match = match;
		}

		void reset() {
			recordedPath.clear();
			match = null;
		}

		boolean didMatch() {
			return match != null;
		}

		List<String> getRecordedPath() {
			return recordedPath;
		}

		@Override
		public void init() {
		}

		@Override
		public boolean findFor(SeasonActiveAdult bugsData) {
			recordedPath.add(stepName);
			return match != null;
		}

		@Override
		public TaxaSeasonality getFound() {
			return match;
		}
	}
}