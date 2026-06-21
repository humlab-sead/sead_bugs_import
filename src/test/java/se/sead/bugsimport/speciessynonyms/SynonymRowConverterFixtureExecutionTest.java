package se.sead.bugsimport.speciessynonyms;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.speciesassociation.seadmodel.SpeciesAssociation;
import se.sead.bugsimport.speciessynonyms.bugsmodel.Synonym;
import se.sead.model.TestSpeciesAssociation;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SynonymRowConverterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;
	private RecordingSynonymTraceHelper traceHelper;
	private StubSynonymCreator creator;
	private SynonymRowConverter rowConverter;

	@Before
	public void setup() throws Exception {
		fixtureLoader = new PolicyFixtureLoader();
		traceHelper = new RecordingSynonymTraceHelper();
		creator = new StubSynonymCreator();
		rowConverter = new SynonymRowConverter();
		injectField(rowConverter, "traceHelper", traceHelper);
		injectField(rowConverter, "creator", creator);
	}

	@Test
	public void traceHitFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("trace_hit_returns_existing_synonym_association");
	}

	@Test
	public void traceHitErrorFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("trace_hit_returns_existing_synonym_error");
	}

	@Test
	public void createNewFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_new_synonym_association_when_no_trace_exists");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("speciessynonyms.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		configureScenario(policyContext, state, scenarioName);
		SpeciesAssociation result = rowConverter.convertForDataRow(createSynonym(sourceRow, scenarioName));

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("reconciliation_path"), scenarioName + ".expects.reconciliation_path"), actualPath(result));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("reconciliation_result"), scenarioName + ".expects.reconciliation_result"), actualReconciliationResult(result, sourceRow));
	}

	private void configureScenario(Map<String, Object> policyContext, Map<String, Object> state, String scenarioName) {
		Map<String, Object> stepHits = fixtureLoader.mapValue(policyContext.get("step_hits"), scenarioName + ".policy_context.step_hits");
		traceHelper.reset();
		if (Boolean.TRUE.equals(stepHits.get("trace_lookup"))) {
			SpeciesAssociation association = TestSpeciesAssociation.create(
					fixtureLoader.integerValue(state.get("existing_row_id"), scenarioName + ".policy_context.state.existing_row_id"),
					null,
					null,
					null,
					null);
			if (state != null && state.containsKey("existing_error_message")) {
				association.addError(fixtureLoader.stringValue(state.get("existing_error_message"), scenarioName + ".policy_context.state.existing_error_message"));
			}
			traceHelper.setMatch(association);
		}
	}

	private Synonym createSynonym(Map<String, Object> sourceRow, String scenarioName) {
		Synonym synonym = new Synonym();
		synonym.setCode(fixtureLoader.integerValue(sourceRow.get("CODE"), scenarioName + ".source_rows[0].CODE").doubleValue());
		synonym.setSynGenus(fixtureLoader.stringValue(sourceRow.get("SynGenus"), scenarioName + ".source_rows[0].SynGenus"));
		synonym.setSynSpecies(fixtureLoader.stringValue(sourceRow.get("SynSpecies"), scenarioName + ".source_rows[0].SynSpecies"));
		synonym.setSynAuthority(fixtureLoader.stringValue(sourceRow.get("SynAuthority"), scenarioName + ".source_rows[0].SynAuthority"));
		synonym.setReference(fixtureLoader.stringValue(sourceRow.get("Ref"), scenarioName + ".source_rows[0].Ref"));
		synonym.setNotes(fixtureLoader.stringValue(sourceRow.get("Notes"), scenarioName + ".source_rows[0].Notes"));
		return synonym;
	}

	private List<String> actualPath(SpeciesAssociation result) {
		List<String> path = new ArrayList<String>();
		path.addAll(traceHelper.getRecordedPath());
		if (result.getId() == null) {
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
			reconciliationResult.put("result_kind", "return_as_is");
			reconciliationResult.put("persisted_action", "keep_existing");
			reconciliationResult.put("source", "trace_lookup");
		}
		reconciliationResult.put("row_id", result.getId());
		reconciliationResult.put("species_code", fixtureLoader.integerValue(sourceRow.get("CODE"), "sourceRow.CODE"));
		reconciliationResult.put("synonym_genus", sourceRow.get("SynGenus"));
		reconciliationResult.put("synonym_species", sourceRow.get("SynSpecies"));
		reconciliationResult.put("synonym_authority", sourceRow.get("SynAuthority"));
		reconciliationResult.put("bugs_reference", sourceRow.get("Ref"));
		return reconciliationResult;
	}

	private void injectField(Object target, String fieldName, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

	private static class RecordingSynonymTraceHelper extends SynonymRowConverter.SynonymTraceHelper {

		private SpeciesAssociation match;
		private final List<String> recordedPath = new ArrayList<String>();

		RecordingSynonymTraceHelper() {
			super(null);
		}

		void setMatch(SpeciesAssociation match) {
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
		public SpeciesAssociation getFromLastTrace(String bugsIdentifier) {
			recordedPath.add("trace_lookup");
			return match;
		}
	}

	private static class StubSynonymCreator extends SynonymCreator {
		@Override
		public SpeciesAssociation create(Synonym synonym) {
			return TestSpeciesAssociation.create(null, null, null, null, null);
		}
	}
}