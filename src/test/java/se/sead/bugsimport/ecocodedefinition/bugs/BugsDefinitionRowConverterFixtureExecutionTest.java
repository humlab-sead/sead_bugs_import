package se.sead.bugsimport.ecocodedefinition.bugs;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.ecocodedefinition.bugs.bugsmodel.EcoDefBugs;
import se.sead.bugsimport.ecocodedefinition.seadmodel.EcocodeDefinition;
import se.sead.model.TestEcocodeDefinition;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BugsDefinitionRowConverterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;
	private RecordingTraceHelper traceHelper;
	private StubUpdater updater;
	private BugsDefinitionRowConverter rowConverter;

	@Before
	public void setup() throws Exception {
		fixtureLoader = new PolicyFixtureLoader();
		traceHelper = new RecordingTraceHelper();
		updater = new StubUpdater();
		rowConverter = new BugsDefinitionRowConverter();
		injectField(rowConverter, "traceHelper", traceHelper);
		injectField(rowConverter, "updater", updater);
	}

	@Test
	public void traceHitFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("trace_hit_updates_existing_bugs_definition");
	}

	@Test
	public void traceHitErrorFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("trace_hit_returns_existing_bugs_definition_error");
	}

	@Test
	public void createNewFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_new_bugs_definition_when_no_trace_exists");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("ecocodedefinition_bugs.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		configureScenario(policyContext, state, scenarioName);
		EcocodeDefinition result = rowConverter.convertForDataRow(createBugsRow(sourceRow, scenarioName));

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("reconciliation_path"), scenarioName + ".expects.reconciliation_path"), actualPath(result));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("reconciliation_result"), scenarioName + ".expects.reconciliation_result"), actualReconciliationResult(result, sourceRow));
	}

	private void configureScenario(Map<String, Object> policyContext, Map<String, Object> state, String scenarioName) {
		Map<String, Object> stepHits = fixtureLoader.mapValue(policyContext.get("step_hits"), scenarioName + ".policy_context.step_hits");
		traceHelper.reset();
		if (Boolean.TRUE.equals(stepHits.get("trace_lookup"))) {
			EcocodeDefinition existing = TestEcocodeDefinition.create(
					fixtureLoader.integerValue(state.get("existing_row_id"), scenarioName + ".policy_context.state.existing_row_id"),
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

	private EcoDefBugs createBugsRow(Map<String, Object> sourceRow, String scenarioName) {
		EcoDefBugs row = new EcoDefBugs();
		row.setBugsEcoCODE(fixtureLoader.stringValue(sourceRow.get("BugsEcoCODE"), scenarioName + ".source_rows[0].BugsEcoCODE"));
		row.setEcoLabel(fixtureLoader.stringValue(sourceRow.get("EcoLabel"), scenarioName + ".source_rows[0].EcoLabel"));
		row.setDefinition(fixtureLoader.stringValue(sourceRow.get("Definition"), scenarioName + ".source_rows[0].Definition"));
		row.setNotes(fixtureLoader.stringValue(sourceRow.get("Notes"), scenarioName + ".source_rows[0].Notes"));
		row.setSortOrder((short) fixtureLoader.integerValue(sourceRow.get("SortOrder"), scenarioName + ".source_rows[0].SortOrder").intValue());
		return row;
	}

	private List<String> actualPath(EcocodeDefinition result) {
		List<String> path = new ArrayList<String>();
		path.addAll(traceHelper.getRecordedPath());
		if (result.getId() == null && result.isErrorFree()) {
			path.add("create_new");
		}
		return path;
	}

	private Map<String, Object> actualReconciliationResult(EcocodeDefinition result, Map<String, Object> sourceRow) {
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
			reconciliationResult.put("persisted_action", "create");
			reconciliationResult.put("source", "create_new");
		} else {
			reconciliationResult.put("result_kind", "update_existing");
			reconciliationResult.put("persisted_action", "update");
			reconciliationResult.put("source", "trace_lookup");
		}
		reconciliationResult.put("row_id", result.getId());
		reconciliationResult.put("bugs_ecocode_code", sourceRow.get("BugsEcoCODE"));
		return reconciliationResult;
	}

	private void injectField(Object target, String fieldName, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

	private static class RecordingTraceHelper extends BugsDefinitionRowConverter.BugsDefinitionTraceHelper {
		private EcocodeDefinition match;
		private final List<String> recordedPath = new ArrayList<String>();

		RecordingTraceHelper() {
			super(null);
		}

		void setMatch(EcocodeDefinition match) {
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
		public EcocodeDefinition getFromLastTrace(String bugsIdentifier) {
			recordedPath.add("trace_lookup");
			return match;
		}
	}

	private static class StubUpdater extends BugsDefinitionUpdater {
		@Override
		public void update(EcocodeDefinition original, EcoDefBugs bugsData) {
		}
	}
}