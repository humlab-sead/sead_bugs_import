package se.sead.bugsimport.periods;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.periods.bugsmodel.Period;
import se.sead.bugsimport.periods.converters.PeriodTraceHelper;
import se.sead.bugsimport.periods.converters.RelativeAgeUpdater;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.model.TestRelativeAge;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PeriodRowConverterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;
	private RecordingTraceHelper traceHelper;
	private StubUpdater updater;
	private PeriodRowConverter rowConverter;

	@Before
	public void setup() throws Exception {
		fixtureLoader = new PolicyFixtureLoader();
		traceHelper = new RecordingTraceHelper();
		updater = new StubUpdater();
		rowConverter = new PeriodRowConverter();
		injectField(rowConverter, "traceHelper", traceHelper);
		injectField(rowConverter, "updater", updater);
	}

	@Test
	public void ignoreFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("ignore_unknown_period_code_before_trace_lookup");
	}

	@Test
	public void traceHitFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("trace_hit_updates_existing_period");
	}

	@Test
	public void traceHitErrorFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("trace_hit_returns_existing_period_error");
	}

	@Test
	public void createNewFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_new_period_when_no_trace_exists");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("period.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = scenario.containsKey("policy_context") ? fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context") : new LinkedHashMap<String, Object>();
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		configureScenario(policyContext, state, scenarioName);
		RelativeAge result = rowConverter.convertForDataRow(createPeriod(sourceRow, scenarioName));

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("reconciliation_path"), scenarioName + ".expects.reconciliation_path"), actualPath(result, sourceRow));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("reconciliation_result"), scenarioName + ".expects.reconciliation_result"), actualReconciliationResult(result, sourceRow));
	}

	private void configureScenario(Map<String, Object> policyContext, Map<String, Object> state, String scenarioName) {
		Map<String, Object> stepHits = policyContext.containsKey("step_hits") ? fixtureLoader.mapValue(policyContext.get("step_hits"), scenarioName + ".policy_context.step_hits") : new LinkedHashMap<String, Object>();
		traceHelper.reset();
		if (Boolean.TRUE.equals(stepHits.get("trace_lookup"))) {
			RelativeAge existing = TestRelativeAge.create(
					fixtureLoader.integerValue(state.get("existing_row_id"), scenarioName + ".policy_context.state.existing_row_id"),
					null,
					null,
					null,
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

	private Period createPeriod(Map<String, Object> sourceRow, String scenarioName) {
		Period period = new Period();
		period.setPeriodCode(fixtureLoader.stringValue(sourceRow.get("PeriodCODE"), scenarioName + ".source_rows[0].PeriodCODE"));
		period.setName(fixtureLoader.stringValue(sourceRow.get("PeriodName"), scenarioName + ".source_rows[0].PeriodName"));
		period.setType(fixtureLoader.stringValue(sourceRow.get("PeriodType"), scenarioName + ".source_rows[0].PeriodType"));
		period.setDesc(fixtureLoader.stringValue(sourceRow.get("PeriodDesc"), scenarioName + ".source_rows[0].PeriodDesc"));
		period.setRef(fixtureLoader.stringValue(sourceRow.get("PeriodRef"), scenarioName + ".source_rows[0].PeriodRef"));
		period.setGeography(fixtureLoader.stringValue(sourceRow.get("PeriodGeog"), scenarioName + ".source_rows[0].PeriodGeog"));
		period.setBegin(fixtureLoader.integerValue(sourceRow.get("Begin"), scenarioName + ".source_rows[0].Begin"));
		period.setBeginBCad(fixtureLoader.stringValue(sourceRow.get("BeginBCAD"), scenarioName + ".source_rows[0].BeginBCAD"));
		period.setEnd(fixtureLoader.integerValue(sourceRow.get("End"), scenarioName + ".source_rows[0].End"));
		period.setEndBCad(fixtureLoader.stringValue(sourceRow.get("EndBCAD"), scenarioName + ".source_rows[0].EndBCAD"));
		period.setYearsType(fixtureLoader.stringValue(sourceRow.get("YearsType"), scenarioName + ".source_rows[0].YearsType"));
		return period;
	}

	private List<String> actualPath(RelativeAge result, Map<String, Object> sourceRow) {
		List<String> path = new ArrayList<String>();
		String periodCode = (String) sourceRow.get("PeriodCODE");
		if ("?".equals(periodCode)) {
			path.add("ignore_unknown_period_code");
			return path;
		}
		path.addAll(traceHelper.getRecordedPath());
		if (result.getId() == null && result.isErrorFree()) {
			path.add("create_new");
		}
		return path;
	}

	private Map<String, Object> actualReconciliationResult(RelativeAge result, Map<String, Object> sourceRow) {
		Map<String, Object> reconciliationResult = new LinkedHashMap<String, Object>();
		String periodCode = (String) sourceRow.get("PeriodCODE");
		if ("?".equals(periodCode)) {
			reconciliationResult.put("result_kind", "return_guard_error");
			reconciliationResult.put("persisted_action", "stop_before_write");
			reconciliationResult.put("source", "ignore_unknown_period_code");
			Map<String, Object> issue = new LinkedHashMap<String, Object>();
			issue.put("severity", "error");
			issue.put("message", result.getErrorMessages().get(0));
			reconciliationResult.put("issue", issue);
			reconciliationResult.put("row_id", null);
			reconciliationResult.put("period_code", periodCode);
			return reconciliationResult;
		}
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
			reconciliationResult.put("persisted_action", "create");
			reconciliationResult.put("source", "create_new");
		} else {
			reconciliationResult.put("result_kind", "update_existing");
			reconciliationResult.put("persisted_action", "update");
			reconciliationResult.put("source", "trace_lookup");
		}
		reconciliationResult.put("row_id", result.getId());
		reconciliationResult.put("period_code", periodCode);
		return reconciliationResult;
	}

	private void injectField(Object target, String fieldName, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

	private static class RecordingTraceHelper extends PeriodTraceHelper {
		private RelativeAge match;
		private final List<String> recordedPath = new ArrayList<String>();

		RecordingTraceHelper() {
			super(null);
		}

		void setMatch(RelativeAge match) {
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
		public RelativeAge getFromLastTrace(String bugsIdentifier) {
			recordedPath.add("trace_lookup");
			return match;
		}
	}

	private static class StubUpdater extends RelativeAgeUpdater {
		StubUpdater() {
			super(null, null, null, null);
		}

		@Override
		public void update(RelativeAge original, Period bugsData) {
		}
	}
}