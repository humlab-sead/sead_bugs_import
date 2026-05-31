package se.sead.bugsimport.rdbsystems;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.rdbsystems.bugsmodel.BugsRDBSystem;
import se.sead.bugsimport.rdbsystems.converters.RdbSystemFromTrace;
import se.sead.bugsimport.rdbsystems.converters.RdbSystemUpdater;
import se.sead.bugsimport.rdbsystems.seadmodel.RdbSystem;
import se.sead.model.TestRdbSystem;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RdbSystemRowConverterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;
	private RecordingTraceHelper traceHelper;
	private StubUpdater updater;
	private RdbSystemRowConverter rowConverter;

	@Before
	public void setup() throws Exception {
		fixtureLoader = new PolicyFixtureLoader();
		traceHelper = new RecordingTraceHelper();
		updater = new StubUpdater();
		rowConverter = new RdbSystemRowConverter();
		injectField(rowConverter, "traceHelper", traceHelper);
		injectField(rowConverter, "updater", updater);
	}

	@Test
	public void traceHitFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("trace_hit_updates_existing_rdb_system");
	}

	@Test
	public void traceHitErrorFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("trace_hit_returns_existing_rdb_system_error");
	}

	@Test
	public void createNewFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_new_rdb_system_when_no_trace_exists");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("rdbsystem.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		configureScenario(policyContext, state, scenarioName);
		RdbSystem result = rowConverter.convertForDataRow(createBugsRow(sourceRow, scenarioName));

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("reconciliation_path"), scenarioName + ".expects.reconciliation_path"), actualPath(result));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("reconciliation_result"), scenarioName + ".expects.reconciliation_result"), actualReconciliationResult(result, sourceRow));
	}

	private void configureScenario(Map<String, Object> policyContext, Map<String, Object> state, String scenarioName) {
		Map<String, Object> stepHits = fixtureLoader.mapValue(policyContext.get("step_hits"), scenarioName + ".policy_context.step_hits");
		traceHelper.reset();
		if (Boolean.TRUE.equals(stepHits.get("trace_lookup"))) {
			RdbSystem existing = TestRdbSystem.create(
					fixtureLoader.integerValue(state.get("existing_row_id"), scenarioName + ".policy_context.state.existing_row_id"),
					null,
					null,
					null,
					null,
					null,
					null);
			if (state.containsKey("existing_error_message")) {
				existing.addError(fixtureLoader.stringValue(state.get("existing_error_message"), scenarioName + ".policy_context.state.existing_error_message"));
			}
			traceHelper.setMatch(existing);
		}
	}

	private BugsRDBSystem createBugsRow(Map<String, Object> sourceRow, String scenarioName) {
		BugsRDBSystem row = new BugsRDBSystem();
		row.setRdbSystemCode(fixtureLoader.integerValue(sourceRow.get("RDBSystemCode"), scenarioName + ".source_rows[0].RDBSystemCode"));
		row.setRdbSystem(fixtureLoader.stringValue(sourceRow.get("RDBSystem"), scenarioName + ".source_rows[0].RDBSystem"));
		row.setRdbVersion(fixtureLoader.stringValue(sourceRow.get("RDBVersion"), scenarioName + ".source_rows[0].RDBVersion"));
		row.setRdbSystemDate(fixtureLoader.integerValue(sourceRow.get("RDBSystemDate"), scenarioName + ".source_rows[0].RDBSystemDate"));
		row.setRdbFirstPublished(fixtureLoader.integerValue(sourceRow.get("RDBFirstPublished"), scenarioName + ".source_rows[0].RDBFirstPublished").shortValue());
		row.setRef(fixtureLoader.stringValue(sourceRow.get("Ref"), scenarioName + ".source_rows[0].Ref"));
		row.setCountryCode(fixtureLoader.stringValue(sourceRow.get("CountryCode"), scenarioName + ".source_rows[0].CountryCode"));
		return row;
	}

	private List<String> actualPath(RdbSystem result) {
		List<String> path = new ArrayList<String>();
		path.addAll(traceHelper.getRecordedPath());
		if (result.getId() == null && result.isErrorFree()) {
			path.add("create_new");
		}
		return path;
	}

	private Map<String, Object> actualReconciliationResult(RdbSystem result, Map<String, Object> sourceRow) {
		Map<String, Object> reconciliationResult = new LinkedHashMap<String, Object>();
		if (!result.isErrorFree()) {
			reconciliationResult.put("result_kind", "return_existing_error");
			reconciliationResult.put("persisted_action", "keep_existing_error");
			reconciliationResult.put("source", "trace_lookup");
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
		reconciliationResult.put("rdb_system_code", fixtureLoader.integerValue(sourceRow.get("RDBSystemCode"), "sourceRow.RDBSystemCode"));
		return reconciliationResult;
	}

	private void injectField(Object target, String fieldName, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

	private static class RecordingTraceHelper extends RdbSystemFromTrace {
		private RdbSystem match;
		private final List<String> recordedPath = new ArrayList<String>();

		RecordingTraceHelper() {
			super(null);
		}

		void setMatch(RdbSystem match) {
			this.match = match;
		}

		void reset() {
			match = null;
			recordedPath.clear();
		}

		List<String> getRecordedPath() {
			return recordedPath;
		}

		@Override
		public RdbSystem getFromLastTrace(String bugsIdentifier) {
			recordedPath.add("trace_lookup");
			return match;
		}
	}

	private static class StubUpdater extends RdbSystemUpdater {
		@Override
		public RdbSystem update(RdbSystem original, BugsRDBSystem bugsData) {
			return original;
		}
	}
}