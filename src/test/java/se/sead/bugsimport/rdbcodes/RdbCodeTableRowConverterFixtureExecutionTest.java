package se.sead.bugsimport.rdbcodes;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.rdbcodes.bugsmodel.BugsRDBCodes;
import se.sead.bugsimport.rdbcodes.converter.RdbCodeUpdater;
import se.sead.bugsimport.rdbcodes.search.SearchStrategy;
import se.sead.bugsimport.rdbcodes.seadmodel.RdbCode;
import se.sead.bugsimport.rdbsystems.converters.RdbSystemFromTrace;
import se.sead.bugsimport.rdbsystems.seadmodel.RdbSystem;
import se.sead.model.TestRdbCode;
import se.sead.model.TestRdbSystem;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RdbCodeTableRowConverterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;
	private RecordingSearchStrategy traceSearch;
	private RecordingSearchStrategy duplicateValueGuard;
	private RdbCodeTableRowConverter rowConverter;

	@Before
	public void setup() throws Exception {
		fixtureLoader = new PolicyFixtureLoader();
		traceSearch = new RecordingSearchStrategy("trace_lookup");
		duplicateValueGuard = new RecordingSearchStrategy("duplicate_value_guard");
		RdbCodeUpdater updater = new RdbCodeUpdater();
		Field helperField = RdbCodeUpdater.class.getDeclaredField("rdbSystemFromTraceHelper");
		helperField.setAccessible(true);
		helperField.set(updater, new RecordingRdbSystemFromTrace());
		rowConverter = new RdbCodeTableRowConverter();
		Field updaterField = RdbCodeTableRowConverter.class.getDeclaredField("codeUpdater");
		updaterField.setAccessible(true);
		updaterField.set(rowConverter, updater);
		Field searchesField = RdbCodeTableRowConverter.class.getDeclaredField("searchStrategies");
		searchesField.setAccessible(true);
		searchesField.set(rowConverter, Arrays.<SearchStrategy>asList(traceSearch, duplicateValueGuard));
	}

	@Test
	public void traceHitFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("trace_hit_updates_existing_rdb_code");
	}

	@Test
	public void traceErrorFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("trace_hit_returns_existing_error_row");
	}

	@Test
	public void duplicateGuardFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("duplicate_value_guard_returns_error_carrier");
	}

	@Test
	public void createNewFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_new_rdb_code_when_no_match_exists");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("rdbcode.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		configureSearches(sourceRow, policyContext, state, scenarioName);
		RdbCode result = rowConverter.convertForDataRow(createBugsRdbCode(sourceRow, scenarioName));

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("reconciliation_path"), scenarioName + ".expects.reconciliation_path"), actualPath(result));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("reconciliation_result"), scenarioName + ".expects.reconciliation_result"), actualReconciliationResult(result));
	}

	private void configureSearches(Map<String, Object> sourceRow, Map<String, Object> policyContext, Map<String, Object> state, String scenarioName) {
		Map<String, Object> stepHits = fixtureLoader.mapValue(policyContext.get("step_hits"), scenarioName + ".policy_context.step_hits");
		traceSearch.reset();
		duplicateValueGuard.reset();
		if (Boolean.TRUE.equals(stepHits.get("trace_lookup"))) {
			traceSearch.setMatch(createMatchedCode(sourceRow, state, scenarioName, false));
		}
		if (Boolean.TRUE.equals(stepHits.get("duplicate_value_guard"))) {
			duplicateValueGuard.setMatch(createMatchedCode(sourceRow, state, scenarioName, true));
		}
	}

	private RdbCode createMatchedCode(Map<String, Object> sourceRow, Map<String, Object> state, String scenarioName, boolean guardError) {
		RdbSystem system = TestRdbSystem.create(5, "System 5", "v1", 2020, (short) 1, null, null);
		RdbCode code = TestRdbCode.create(
				state != null && state.containsKey("existing_row_id") ? fixtureLoader.integerValue(state.get("existing_row_id"), scenarioName + ".policy_context.state.existing_row_id") : null,
				fixtureLoader.stringValue(sourceRow.get("Category"), scenarioName + ".source_rows[0].Category"),
				fixtureLoader.stringValue(sourceRow.get("RDBDefinition"), scenarioName + ".source_rows[0].RDBDefinition"),
				system
		);
		if (guardError && state != null && state.containsKey("guard_error_message")) {
			code.addError(fixtureLoader.stringValue(state.get("guard_error_message"), scenarioName + ".policy_context.state.guard_error_message"));
		}
		if (!guardError && state != null && state.containsKey("existing_error_message")) {
			code.addError(fixtureLoader.stringValue(state.get("existing_error_message"), scenarioName + ".policy_context.state.existing_error_message"));
		}
		return code;
	}

	private BugsRDBCodes createBugsRdbCode(Map<String, Object> sourceRow, String scenarioName) {
		BugsRDBCodes bugs = new BugsRDBCodes();
		bugs.setRdbCode(fixtureLoader.integerValue(sourceRow.get("RDBCode"), scenarioName + ".source_rows[0].RDBCode"));
		bugs.setCategory(fixtureLoader.stringValue(sourceRow.get("Category"), scenarioName + ".source_rows[0].Category"));
		bugs.setRdbDefinition(fixtureLoader.stringValue(sourceRow.get("RDBDefinition"), scenarioName + ".source_rows[0].RDBDefinition"));
		bugs.setRdbSystemCode(fixtureLoader.integerValue(sourceRow.get("RDBSystemCode"), scenarioName + ".source_rows[0].RDBSystemCode"));
		return bugs;
	}

	private List<String> actualPath(RdbCode result) {
		List<String> path = new ArrayList<String>();
		path.addAll(traceSearch.getRecordedPath());
		path.addAll(duplicateValueGuard.getRecordedPath());
		if (result.getId() == null && result.isErrorFree()) {
			path.add("create_new");
		}
		return path;
	}

	private Map<String, Object> actualReconciliationResult(RdbCode result) {
		Map<String, Object> reconciliationResult = new LinkedHashMap<String, Object>();
		if (!result.isErrorFree()) {
			String source = duplicateValueGuard.getRecordedPath().isEmpty() ? "trace_lookup" : "duplicate_value_guard";
			reconciliationResult.put("result_kind", result.getId() == null ? "return_guard_error" : "return_existing_error");
			reconciliationResult.put("persisted_action", result.getId() == null ? "stop_before_write" : "keep_existing_error");
			reconciliationResult.put("source", source);
			Map<String, Object> issue = new LinkedHashMap<String, Object>();
			issue.put("severity", "error");
			issue.put("message", result.getErrorMessages().get(0));
			reconciliationResult.put("issue", issue);
		} else if (result.getId() == null) {
			reconciliationResult.put("result_kind", "insert_new");
			reconciliationResult.put("source", "create_new");
		} else {
			reconciliationResult.put("result_kind", "update_existing");
			reconciliationResult.put("source", "trace_lookup");
		}
		reconciliationResult.put("row_id", result.getId());
		reconciliationResult.put("rdb_code", traceSearch.getCurrentSourceId());
		return reconciliationResult;
	}

	private static class RecordingSearchStrategy implements SearchStrategy {

		private final String ruleName;
		private RdbCode match;
		private final List<String> recordedPath = new ArrayList<String>();
		private Integer currentSourceId;

		RecordingSearchStrategy(String ruleName) {
			this.ruleName = ruleName;
			this.match = NO_CODE_FOUND;
		}

		void setMatch(RdbCode match) {
			this.match = match;
		}

		void reset() {
			recordedPath.clear();
			match = NO_CODE_FOUND;
			currentSourceId = null;
		}

		List<String> getRecordedPath() {
			return recordedPath;
		}

		Integer getCurrentSourceId() {
			return currentSourceId;
		}

		@Override
		public RdbCode get(BugsRDBCodes bugsData) {
			currentSourceId = bugsData.getRdbCode();
			recordedPath.add(ruleName);
			return match;
		}
	}

	private static class RecordingRdbSystemFromTrace extends RdbSystemFromTrace {

		RecordingRdbSystemFromTrace() {
			super(null);
		}

		@Override
		public RdbSystem getFromLastTrace(String bugsIdentifier) {
			return TestRdbSystem.create(5, "System 5", "v1", 2020, (short) 1, null, null);
		}
	}
}