package se.sead.bugsimport.speciesassociation;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.speciesassociation.bugsmodel.BugsSpeciesAssociation;
import se.sead.bugsimport.speciesassociation.converters.SpeciesAssociationTraceHelper;
import se.sead.bugsimport.speciesassociation.converters.SpeciesAssociationUpdater;
import se.sead.bugsimport.speciesassociation.seadmodel.SpeciesAssociation;
import se.sead.model.TestSpeciesAssociation;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SpeciesAssociationRowConverterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;
	private RecordingSpeciesAssociationTraceHelper traceHelper;
	private RecordingSpeciesAssociationUpdater updater;
	private SpeciesAssociationRowConverter rowConverter;

	@Before
	public void setup() throws Exception {
		fixtureLoader = new PolicyFixtureLoader();
		traceHelper = new RecordingSpeciesAssociationTraceHelper();
		updater = new RecordingSpeciesAssociationUpdater();
		rowConverter = new SpeciesAssociationRowConverter();
		injectField(rowConverter, "traceHelper", traceHelper);
		injectField(rowConverter, "updater", updater);
	}

	@Test
	public void traceHitFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("trace_hit_updates_existing_species_association");
	}

	@Test
	public void traceHitErrorFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("trace_hit_returns_existing_error_row");
	}

	@Test
	public void createNewFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_new_species_association_when_no_trace_exists");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("speciesassociation.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		configureScenario(policyContext, state, scenarioName);
		SpeciesAssociation result = rowConverter.convertForDataRow(createBugsSpeciesAssociation(sourceRow, scenarioName));

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("reconciliation_path"), scenarioName + ".expects.reconciliation_path"), actualPath(result));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("reconciliation_result"), scenarioName + ".expects.reconciliation_result"), actualReconciliationResult(result, sourceRow));
	}

	private void configureScenario(Map<String, Object> policyContext, Map<String, Object> state, String scenarioName) {
		Map<String, Object> stepHits = fixtureLoader.mapValue(policyContext.get("step_hits"), scenarioName + ".policy_context.step_hits");
		traceHelper.reset();
		updater.reset();
		if (Boolean.TRUE.equals(stepHits.get("trace_lookup"))) {
			traceHelper.setMatch(createMatchedAssociation(state, scenarioName));
		}
	}

	private SpeciesAssociation createMatchedAssociation(Map<String, Object> state, String scenarioName) {
		SpeciesAssociation association = TestSpeciesAssociation.create(
				fixtureLoader.integerValue(state.get("existing_row_id"), scenarioName + ".policy_context.state.existing_row_id"),
				null,
				null,
				null,
				null
		);
		if (state != null && state.containsKey("existing_error_message")) {
			association.addError(fixtureLoader.stringValue(state.get("existing_error_message"), scenarioName + ".policy_context.state.existing_error_message"));
		}
		return association;
	}

	private BugsSpeciesAssociation createBugsSpeciesAssociation(Map<String, Object> sourceRow, String scenarioName) {
		BugsSpeciesAssociation row = new BugsSpeciesAssociation();
		row.setSpeciesAssociationID(fixtureLoader.integerValue(sourceRow.get("SpeciesAssociationID"), scenarioName + ".source_rows[0].SpeciesAssociationID"));
		row.setCode(fixtureLoader.integerValue(sourceRow.get("CODE"), scenarioName + ".source_rows[0].CODE").doubleValue());
		row.setAssociatedSpeciesCODE(fixtureLoader.integerValue(sourceRow.get("AssociatedSpeciesCODE"), scenarioName + ".source_rows[0].AssociatedSpeciesCODE").doubleValue());
		row.setAssociationType(fixtureLoader.stringValue(sourceRow.get("AssociationType"), scenarioName + ".source_rows[0].AssociationType"));
		row.setRef(fixtureLoader.stringValue(sourceRow.get("Ref"), scenarioName + ".source_rows[0].Ref"));
		return row;
	}

	private List<String> actualPath(SpeciesAssociation result) {
		List<String> path = new ArrayList<String>();
		path.addAll(traceHelper.getRecordedPath());
		if (result.getId() == null && result.isErrorFree()) {
			path.add("create_new");
		}
		return path;
	}

	private Map<String, Object> actualReconciliationResult(SpeciesAssociation result, Map<String, Object> sourceRow) {
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
		reconciliationResult.put("species_association_id", fixtureLoader.integerValue(sourceRow.get("SpeciesAssociationID"), "sourceRow.SpeciesAssociationID"));
		reconciliationResult.put("species_code", fixtureLoader.integerValue(sourceRow.get("CODE"), "sourceRow.CODE"));
		reconciliationResult.put("associated_species_code", fixtureLoader.integerValue(sourceRow.get("AssociatedSpeciesCODE"), "sourceRow.AssociatedSpeciesCODE"));
		reconciliationResult.put("association_type", sourceRow.get("AssociationType"));
		reconciliationResult.put("bugs_reference", sourceRow.get("Ref"));
		return reconciliationResult;
	}

	private void injectField(Object target, String fieldName, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

	private static class RecordingSpeciesAssociationTraceHelper extends SpeciesAssociationTraceHelper {

		private SpeciesAssociation match;
		private final List<String> recordedPath = new ArrayList<String>();

		RecordingSpeciesAssociationTraceHelper() {
			super(null);
		}

		void setMatch(SpeciesAssociation match) {
			this.match = match;
		}

		void reset() {
			recordedPath.clear();
			match = null;
		}

		List<String> getRecordedPath() {
			return recordedPath;
		}

		@Override
		public SpeciesAssociation getFromLastTrace(String bugsIdentifier) {
			recordedPath.add("trace_lookup");
			return match;
		}
	}

	private static class RecordingSpeciesAssociationUpdater extends SpeciesAssociationUpdater {

		private int calls;

		void reset() {
			calls = 0;
		}

		@Override
		public void update(SpeciesAssociation original, BugsSpeciesAssociation bugsData) {
			calls++;
		}
	}
}