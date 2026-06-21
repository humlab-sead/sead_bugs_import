package se.sead.bugsimport.ecocodedefinitiongroups;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.ecocodedefinitiongroups.bugsmodel.EcoDefGroups;
import se.sead.bugsimport.ecocodedefinitiongroups.converters.EcocodeGroupUpdater;
import se.sead.bugsimport.ecocodedefinitiongroups.seadmodel.EcocodeGroup;
import se.sead.bugsimport.ecocodedefinitiongroups.seadmodel.EcocodeSystem;
import se.sead.model.TestEcocodeGroup;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.repositories.EcocodeSystemRepository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class EcocodeGroupRowConverterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;
	private RecordingTraceHelper traceHelper;
	private EcocodeGroupRowConverter converter;

	@Before
	public void setup() throws Exception {
		fixtureLoader = new PolicyFixtureLoader();
		traceHelper = new RecordingTraceHelper();
		converter = new EcocodeGroupRowConverter();
		EcocodeGroupUpdater updater = new EcocodeGroupUpdater();
		injectField(updater, "systemRepository", new RecordingEcocodeSystemRepository());
		injectField(converter, "traceHelper", traceHelper);
		injectField(converter, "updater", updater);
	}

	@Test
	public void traceHitFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("trace_hit_updates_existing_ecocode_group");
	}

	@Test
	public void traceHitErrorFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("trace_hit_returns_existing_ecocode_group_error");
	}

	@Test
	public void createNewFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_new_ecocode_group_when_no_trace_exists");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("ecocodegroup.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		configureScenario(sourceRow, policyContext, state, scenarioName);
		EcocodeGroup result = converter.convertForDataRow(createBugsRow(sourceRow, scenarioName));

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("reconciliation_path"), scenarioName + ".expects.reconciliation_path"), actualPath(result));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("reconciliation_result"), scenarioName + ".expects.reconciliation_result"), actualReconciliationResult(result, sourceRow));
	}

	private void configureScenario(Map<String, Object> sourceRow, Map<String, Object> policyContext, Map<String, Object> state, String scenarioName) {
		Map<String, Object> stepHits = fixtureLoader.mapValue(policyContext.get("step_hits"), scenarioName + ".policy_context.step_hits");
		traceHelper.reset();
		if (Boolean.TRUE.equals(stepHits.get("trace_lookup"))) {
			EcocodeGroup existing = TestEcocodeGroup.create(
					fixtureLoader.integerValue(state.get("existing_row_id"), scenarioName + ".policy_context.state.existing_row_id"),
					fixtureLoader.stringValue(sourceRow.get("EcoGroupCode"), scenarioName + ".source_rows[0].EcoGroupCode"),
					fixtureLoader.stringValue(sourceRow.get("EcoName"), scenarioName + ".source_rows[0].EcoName"),
					createKochSystem()
			);
			if (state != null && state.containsKey("existing_error_message")) {
				existing.addError(fixtureLoader.stringValue(state.get("existing_error_message"), scenarioName + ".policy_context.state.existing_error_message"));
			}
			traceHelper.setMatch(existing);
		}
	}

	private EcoDefGroups createBugsRow(Map<String, Object> sourceRow, String scenarioName) {
		EcoDefGroups bugsRow = new EcoDefGroups();
		bugsRow.setEcoGroupCode(fixtureLoader.stringValue(sourceRow.get("EcoGroupCode"), scenarioName + ".source_rows[0].EcoGroupCode"));
		bugsRow.setEcoName(fixtureLoader.stringValue(sourceRow.get("EcoName"), scenarioName + ".source_rows[0].EcoName"));
		return bugsRow;
	}

	private List<String> actualPath(EcocodeGroup result) {
		List<String> path = new ArrayList<String>();
		path.addAll(traceHelper.getRecordedPath());
		if (result.getId() == null) {
			path.add("create_new");
		}
		return path;
	}

	private Map<String, Object> actualReconciliationResult(EcocodeGroup result, Map<String, Object> sourceRow) {
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
		reconciliationResult.put("ecocode_group_code", sourceRow.get("EcoGroupCode"));
		return reconciliationResult;
	}

	private EcocodeSystem createKochSystem() {
		TestEcocodeSystem system = new TestEcocodeSystem(77);
		system.setName("Koch Ecology Codes");
		return system;
	}

	private void injectField(Object target, String fieldName, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

	private static class RecordingTraceHelper extends EcocodeGroupRowConverter.EcocodeGroupTraceHelper {

		private EcocodeGroup match;
		private final List<String> recordedPath = new ArrayList<String>();

		RecordingTraceHelper() {
			super(null);
		}

		void setMatch(EcocodeGroup match) {
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
		public EcocodeGroup getFromLastTrace(String bugsIdentifier) {
			recordedPath.add("trace_lookup");
			return match;
		}
	}

	private static class RecordingEcocodeSystemRepository implements EcocodeSystemRepository {
		@Override
		public EcocodeSystem findKochSystem() {
			TestEcocodeSystem system = new TestEcocodeSystem(77);
			system.setName("Koch Ecology Codes");
			return system;
		}
	}

	private static class TestEcocodeSystem extends EcocodeSystem {
		private TestEcocodeSystem(Integer id) {
			setId(id);
		}
	}
}