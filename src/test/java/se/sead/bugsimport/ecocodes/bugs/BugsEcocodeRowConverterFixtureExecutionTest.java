package se.sead.bugsimport.ecocodes.bugs;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.ecocodes.bugs.bugsmodel.EcoBugs;
import se.sead.bugsimport.ecocodes.seadmodel.Ecocode;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BugsEcocodeRowConverterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;
	private RecordingTraceHelper traceHelper;
	private BugsEcocodeRowConverter converter;

	@Before
	public void setup() throws Exception {
		fixtureLoader = new PolicyFixtureLoader();
		traceHelper = new RecordingTraceHelper();
		converter = new BugsEcocodeRowConverter();
		injectField(converter, "traceHelper", traceHelper);
		injectField(converter, "creator", new StubCreator());
	}

	@Test
	public void traceHitFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("trace_hit_returns_existing_bugs_ecocode");
	}

	@Test
	public void createNewFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_new_bugs_ecocode_when_no_trace_exists");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("ecocode_bugs.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		configureScenario(state, scenarioName);
		Ecocode result = converter.convertForDataRow(createBugsRow(sourceRow, scenarioName));

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("reconciliation_path"), scenarioName + ".expects.reconciliation_path"), actualPath(result));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("reconciliation_result"), scenarioName + ".expects.reconciliation_result"), actualReconciliationResult(result, sourceRow));
	}

	private void configureScenario(Map<String, Object> state, String scenarioName) {
		traceHelper.reset();
		if (state != null && state.containsKey("existing_row_id")) {
			Ecocode existing = new EcocodeFixtureValue(fixtureLoader.integerValue(state.get("existing_row_id"), scenarioName + ".policy_context.state.existing_row_id"));
			traceHelper.setMatch(existing);
		}
	}

	private EcoBugs createBugsRow(Map<String, Object> sourceRow, String scenarioName) {
		EcoBugs row = new EcoBugs();
		row.setCode(fixtureLoader.integerValue(sourceRow.get("CODE"), scenarioName + ".source_rows[0].CODE").doubleValue());
		row.setBugsEcoCode(fixtureLoader.stringValue(sourceRow.get("BugsEcoCODE"), scenarioName + ".source_rows[0].BugsEcoCODE"));
		return row;
	}

	private List<String> actualPath(Ecocode result) {
		List<String> path = new ArrayList<String>();
		path.addAll(traceHelper.getRecordedPath());
		if (result.getId() == null) {
			path.add("create_new");
		}
		return path;
	}

	private Map<String, Object> actualReconciliationResult(Ecocode result, Map<String, Object> sourceRow) {
		Map<String, Object> reconciliationResult = new LinkedHashMap<String, Object>();
		if (result.getId() == null) {
			reconciliationResult.put("result_kind", "insert_new");
			reconciliationResult.put("source", "create_new");
		} else {
			reconciliationResult.put("result_kind", "update_existing");
			reconciliationResult.put("source", "trace_lookup");
		}
		reconciliationResult.put("row_id", result.getId());
		reconciliationResult.put("species_code", fixtureLoader.integerValue(sourceRow.get("CODE"), "sourceRow.CODE"));
		reconciliationResult.put("bugs_ecocode_code", sourceRow.get("BugsEcoCODE"));
		return reconciliationResult;
	}

	private void injectField(Object target, String fieldName, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

	private static class RecordingTraceHelper extends BugsEcocodeRowConverter.BugsEcocodesTraceHelper {
		private Ecocode match;
		private final List<String> recordedPath = new ArrayList<String>();

		RecordingTraceHelper() {
			super(null);
		}

		void setMatch(Ecocode match) {
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
		public Ecocode getFromLastTrace(String bugsIdentifier) {
			recordedPath.add("trace_lookup");
			return match;
		}
	}

	private static class StubCreator extends BugsEcocodeCreator {
		@Override
		public Ecocode create(EcoBugs bugsData) {
			return new EcocodeFixtureValue(null);
		}
	}

	private static class EcocodeFixtureValue extends Ecocode {
		private EcocodeFixtureValue(Integer id) {
			setId(id);
		}
	}
}