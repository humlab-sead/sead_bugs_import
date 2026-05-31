package se.sead.bugsimport.mcrnames;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.mcrnames.bugsmodel.BugsMCRNames;
import se.sead.bugsimport.mcrnames.converters.McrNameUpdater;
import se.sead.bugsimport.mcrnames.search.MCRSearch;
import se.sead.bugsimport.mcrnames.seadmodel.MCRName;
import se.sead.model.TestMCRName;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class McrNameTableRowConverterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;
	private RecordingMcrSearch traceLookup;
	private RecordingMcrSearch speciesValueLookup;
	private McrNameTableRowConverter rowConverter;

	@Before
	public void setup() throws Exception {
		fixtureLoader = new PolicyFixtureLoader();
		traceLookup = new RecordingMcrSearch("trace_lookup");
		speciesValueLookup = new RecordingMcrSearch("species_value_lookup");
		rowConverter = new McrNameTableRowConverter();
		injectField(rowConverter, "searchStrategies", Arrays.<MCRSearch>asList(traceLookup, speciesValueLookup));
		injectField(rowConverter, "updater", new StubUpdater());
	}

	@Test
	public void traceLookupFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("trace_lookup_updates_existing_mcr_name");
	}

	@Test
	public void speciesValueLookupFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("species_value_lookup_updates_existing_mcr_name");
	}

	@Test
	public void createNewFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_new_mcr_name_when_search_chain_misses");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("mcrnames.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		configureScenario(policyContext, state, scenarioName, sourceRow);
		MCRName result = rowConverter.convertForDataRow(createBugsRow(sourceRow, scenarioName));

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("reconciliation_path"), scenarioName + ".expects.reconciliation_path"), actualPath(result));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("reconciliation_result"), scenarioName + ".expects.reconciliation_result"), actualReconciliationResult(result, sourceRow));
	}

	private void configureScenario(Map<String, Object> policyContext, Map<String, Object> state, String scenarioName, Map<String, Object> sourceRow) {
		Map<String, Object> stepHits = fixtureLoader.mapValue(policyContext.get("step_hits"), scenarioName + ".policy_context.step_hits");
		traceLookup.reset();
		speciesValueLookup.reset();
		if (Boolean.TRUE.equals(stepHits.get("trace_lookup"))) {
			traceLookup.setMatch(TestMCRName.create(fixtureLoader.integerValue(state.get("existing_row_id"), scenarioName + ".policy_context.state.existing_row_id"), null, null, null, null));
		}
		if (Boolean.TRUE.equals(stepHits.get("species_value_lookup"))) {
			speciesValueLookup.setMatch(TestMCRName.create(fixtureLoader.integerValue(state.get("existing_row_id"), scenarioName + ".policy_context.state.existing_row_id"), null, null, null, null));
		}
		traceLookup.setCurrentCode(integerCode(sourceRow));
		speciesValueLookup.setCurrentCode(integerCode(sourceRow));
	}

	private BugsMCRNames createBugsRow(Map<String, Object> sourceRow, String scenarioName) {
		BugsMCRNames bugs = new BugsMCRNames();
		bugs.setCode(doubleValue(sourceRow.get("CODE"), scenarioName + ".source_rows[0].CODE"));
		bugs.setCompareStatus(fixtureLoader.stringValue(sourceRow.get("CompareStatus"), scenarioName + ".source_rows[0].CompareStatus"));
		bugs.setMcrName(fixtureLoader.stringValue(sourceRow.get("MCRName"), scenarioName + ".source_rows[0].MCRName"));
		bugs.setMcrNameTrim(fixtureLoader.stringValue(sourceRow.get("MCRNameTrim"), scenarioName + ".source_rows[0].MCRNameTrim"));
		bugs.setMcrNumber(shortValue(sourceRow.get("MCRNumber"), scenarioName + ".source_rows[0].MCRNumber"));
		bugs.setTempCode(doubleValue(sourceRow.get("tempCODE"), scenarioName + ".source_rows[0].tempCODE"));
		return bugs;
	}

	private List<String> actualPath(MCRName result) {
		List<String> path = new ArrayList<String>();
		path.addAll(traceLookup.getRecordedPath());
		path.addAll(speciesValueLookup.getRecordedPath());
		if (result.getId() == null && result.isErrorFree()) {
			path.add("create_new");
		}
		return path;
	}

	private Map<String, Object> actualReconciliationResult(MCRName result, Map<String, Object> sourceRow) {
		Map<String, Object> reconciliationResult = new LinkedHashMap<String, Object>();
		String source = traceLookup.didMatch() ? "trace_lookup" : (speciesValueLookup.didMatch() ? "species_value_lookup" : "create_new");
		if (result.getId() == null) {
			reconciliationResult.put("result_kind", "insert_new");
		} else {
			reconciliationResult.put("result_kind", "update_existing");
		}
		reconciliationResult.put("source", source);
		reconciliationResult.put("row_id", result.getId());
		reconciliationResult.put("species_code", integerCode(sourceRow));
		return reconciliationResult;
	}

	private Integer integerCode(Map<String, Object> sourceRow) {
		return doubleValue(sourceRow.get("CODE"), "sourceRow.CODE").intValue();
	}

	private Double doubleValue(Object value, String context) {
		if (!(value instanceof Number)) {
			throw new IllegalArgumentException(context + " must be numeric");
		}
		return ((Number) value).doubleValue();
	}

	private Short shortValue(Object value, String context) {
		if (!(value instanceof Number)) {
			throw new IllegalArgumentException(context + " must be numeric");
		}
		return ((Number) value).shortValue();
	}

	private void injectField(Object target, String fieldName, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

	private static class RecordingMcrSearch implements MCRSearch {
		private final String stepName;
		private final List<String> recordedPath = new ArrayList<String>();
		private MCRName match = NO_MCR_NAME_FOUND;
		private Integer currentCode;

		RecordingMcrSearch(String stepName) {
			this.stepName = stepName;
		}

		void setMatch(MCRName match) {
			this.match = match;
		}

		void setCurrentCode(Integer currentCode) {
			this.currentCode = currentCode;
		}

		void reset() {
			recordedPath.clear();
			match = NO_MCR_NAME_FOUND;
		}

		boolean didMatch() {
			return match != NO_MCR_NAME_FOUND;
		}

		List<String> getRecordedPath() {
			return recordedPath;
		}

		@Override
		public MCRName findFor(BugsMCRNames bugsData) {
			recordedPath.add(stepName);
			return match;
		}
	}

	private static class StubUpdater extends McrNameUpdater {
		@Override
		public void update(MCRName original, BugsMCRNames bugsData) {
		}
	}
}