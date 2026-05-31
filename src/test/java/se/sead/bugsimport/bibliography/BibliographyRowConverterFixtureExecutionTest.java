package se.sead.bugsimport.bibliography;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.bibliography.bugsmodel.BugsBiblio;
import se.sead.bugsimport.bibliography.seadmodel.Biblio;
import se.sead.model.TestBiblio;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.repositories.BiblioDataRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BibliographyRowConverterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;
	private RecordingBiblioTraceHelper traceHelper;
	private RecordingBiblioDataRepository repository;
	private BibliographyRowConverter rowConverter;

	@Before
	public void setup() {
		fixtureLoader = new PolicyFixtureLoader();
		traceHelper = new RecordingBiblioTraceHelper();
		repository = new RecordingBiblioDataRepository();
		rowConverter = new BibliographyRowConverter(traceHelper, repository);
	}

	@Test
	public void traceHitFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("trace_hit_updates_existing_biblio");
	}

	@Test
	public void traceHitErrorFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("trace_hit_returns_existing_error_row");
	}

	@Test
	public void databaseLookupFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("database_lookup_updates_existing_biblio");
	}

	@Test
	public void createNewFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_new_biblio_when_no_match_exists");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("bibliography.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		configureSearches(sourceRow, policyContext, state, scenarioName);
		Biblio result = rowConverter.convertForDataRow(createBugsBiblio(sourceRow, scenarioName));

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("reconciliation_path"), scenarioName + ".expects.reconciliation_path"), actualPath(result));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("reconciliation_result"), scenarioName + ".expects.reconciliation_result"), actualReconciliationResult(result));
	}

	private void configureSearches(Map<String, Object> sourceRow, Map<String, Object> policyContext, Map<String, Object> state, String scenarioName) {
		Map<String, Object> stepHits = fixtureLoader.mapValue(policyContext.get("step_hits"), scenarioName + ".policy_context.step_hits");
		traceHelper.reset();
		repository.reset();
		if (Boolean.TRUE.equals(stepHits.get("trace_lookup"))) {
			traceHelper.setMatch(createMatchedBiblio(sourceRow, state, scenarioName, "trace author", "trace title"));
		}
		if (Boolean.TRUE.equals(stepHits.get("database_lookup"))) {
			repository.setMatch(createMatchedBiblio(sourceRow, state, scenarioName, "db author", "db title"));
		}
	}

	private Biblio createMatchedBiblio(Map<String, Object> sourceRow, Map<String, Object> state, String scenarioName, String fallbackAuthor, String fallbackTitle) {
		Biblio biblio = TestBiblio.create(
				fixtureLoader.integerValue(state.get("existing_row_id"), scenarioName + ".policy_context.state.existing_row_id"),
				fixtureLoader.stringValue(sourceRow.get("REFERENCE"), scenarioName + ".source_rows[0].REFERENCE"),
				fallbackAuthor,
				fallbackTitle
		);
		if (state != null && state.containsKey("existing_error_message")) {
			biblio.addError(fixtureLoader.stringValue(state.get("existing_error_message"), scenarioName + ".policy_context.state.existing_error_message"));
		}
		return biblio;
	}

	private BugsBiblio createBugsBiblio(Map<String, Object> sourceRow, String scenarioName) {
		BugsBiblio bugsBiblio = new BugsBiblio();
		bugsBiblio.setReference(fixtureLoader.stringValue(sourceRow.get("REFERENCE"), scenarioName + ".source_rows[0].REFERENCE"));
		bugsBiblio.setAuthor(fixtureLoader.stringValue(sourceRow.get("AUTHOR"), scenarioName + ".source_rows[0].AUTHOR"));
		bugsBiblio.setTitle(fixtureLoader.stringValue(sourceRow.get("TITLE"), scenarioName + ".source_rows[0].TITLE"));
		bugsBiblio.setNotes(fixtureLoader.stringValue(sourceRow.get("Notes"), scenarioName + ".source_rows[0].Notes"));
		return bugsBiblio;
	}

	private List<String> actualPath(Biblio result) {
		List<String> path = new ArrayList<String>();
		path.addAll(traceHelper.getRecordedPath());
		path.addAll(repository.getRecordedPath());
		if (result.getId() == null) {
			path.add("create_new");
		}
		return path;
	}

	private Map<String, Object> actualReconciliationResult(Biblio result) {
		Map<String, Object> reconciliationResult = new LinkedHashMap<String, Object>();
		if (!result.isErrorFree()) {
			reconciliationResult.put("result_kind", "return_existing_error");
			reconciliationResult.put("source", repository.getRecordedPath().isEmpty() ? "trace_lookup" : "database_lookup");
			Map<String, Object> issue = new LinkedHashMap<String, Object>();
			issue.put("severity", "error");
			issue.put("message", result.getErrorMessages().get(0));
			reconciliationResult.put("issue", issue);
		} else if (result.getId() == null) {
			reconciliationResult.put("result_kind", "insert_new");
			reconciliationResult.put("source", "create_new");
		} else if (repository.getRecordedPath().isEmpty()) {
			reconciliationResult.put("result_kind", "update_existing");
			reconciliationResult.put("source", "trace_lookup");
		} else {
			reconciliationResult.put("result_kind", "update_existing");
			reconciliationResult.put("source", "database_lookup");
		}
		reconciliationResult.put("row_id", result.getId());
		reconciliationResult.put("bugs_reference", result.getBugsReference());
		return reconciliationResult;
	}

	private static class RecordingBiblioTraceHelper extends BibliographyRowConverter.BiblioTraceHelper {

		private Biblio match;
		private final List<String> recordedPath = new ArrayList<String>();

		RecordingBiblioTraceHelper() {
			super(null);
		}

		void setMatch(Biblio match) {
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
		public Biblio getFromLastTrace(String bugsIdentifier) {
			recordedPath.add("trace_lookup");
			return match;
		}
	}

	private static class RecordingBiblioDataRepository implements BiblioDataRepository {

		private Biblio match;
		private final List<String> recordedPath = new ArrayList<String>();

		void setMatch(Biblio match) {
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
		public Biblio getByBugsReferenceIgnoreCase(String bugsReference) {
			recordedPath.add("database_lookup");
			return match;
		}

		@Override
		public List<Biblio> findAll() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Biblio findOne(Integer integer) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Biblio saveOrUpdate(Biblio entity) {
			throw new UnsupportedOperationException();
		}
	}
}