package se.sead.bugsimport.lab;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.lab.bugsmodel.BugsLab;
import se.sead.bugsimport.lab.converter.DatingLabUpdater;
import se.sead.bugsimport.lab.search.DatingLabSearch;
import se.sead.bugsimport.lab.seadmodel.DatingLab;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.model.TestDatingLab;
import se.sead.model.TestLocation;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.repositories.LocationRepository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class LabRowConverterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;
	private RecordingDatingLabSearch traceSearch;
	private RecordingDatingLabSearch labIdSearch;
	private LabRowConverter rowConverter;

	@Before
	public void setup() throws Exception {
		fixtureLoader = new PolicyFixtureLoader();
		traceSearch = new RecordingDatingLabSearch("trace_lookup");
		labIdSearch = new RecordingDatingLabSearch("international_lab_id_lookup");
		DatingLabUpdater updater = new DatingLabUpdater();
		Field locationRepositoryField = DatingLabUpdater.class.getDeclaredField("locationRepository");
		locationRepositoryField.setAccessible(true);
		locationRepositoryField.set(updater, new RecordingLocationRepository());
		rowConverter = new LabRowConverter();
		Field searchesField = LabRowConverter.class.getDeclaredField("searchStrategies");
		searchesField.setAccessible(true);
		searchesField.set(rowConverter, Arrays.<DatingLabSearch>asList(traceSearch, labIdSearch));
		Field updaterField = LabRowConverter.class.getDeclaredField("updater");
		updaterField.setAccessible(true);
		updaterField.set(rowConverter, updater);
	}

	@Test
	public void traceHitFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("trace_hit_updates_existing_lab");
	}

	@Test
	public void traceHitErrorFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("trace_hit_returns_existing_error_row");
	}

	@Test
	public void labIdHitFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("lab_id_lookup_updates_existing_lab");
	}

	@Test
	public void createNewFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_new_lab_when_no_match_exists");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("lab.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		configureSearches(sourceRow, policyContext, state, scenarioName);
		DatingLab result = rowConverter.convertForDataRow(createBugsLab(sourceRow, scenarioName));

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("reconciliation_path"), scenarioName + ".expects.reconciliation_path"), actualPath(result));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("reconciliation_result"), scenarioName + ".expects.reconciliation_result"), actualReconciliationResult(result));
	}

	private void configureSearches(Map<String, Object> sourceRow, Map<String, Object> policyContext, Map<String, Object> state, String scenarioName) {
		Map<String, Object> stepHits = fixtureLoader.mapValue(policyContext.get("step_hits"), scenarioName + ".policy_context.step_hits");
		traceSearch.reset();
		labIdSearch.reset();
		if (Boolean.TRUE.equals(stepHits.get("trace_lookup"))) {
			traceSearch.setMatch(createMatchedLab(sourceRow, state, scenarioName, "original trace name"));
		} else {
			traceSearch.setMatch(DatingLabSearch.NO_LAB_FOUND);
		}
		if (Boolean.TRUE.equals(stepHits.get("international_lab_id_lookup"))) {
			labIdSearch.setMatch(createMatchedLab(sourceRow, state, scenarioName, "original existing name"));
		} else {
			labIdSearch.setMatch(DatingLabSearch.NO_LAB_FOUND);
		}
	}

	private DatingLab createMatchedLab(Map<String, Object> sourceRow, Map<String, Object> state, String scenarioName, String fallbackName) {
		DatingLab datingLab = TestDatingLab.create(
				fixtureLoader.integerValue(state.get("existing_row_id"), scenarioName + ".policy_context.state.existing_row_id"),
				fixtureLoader.stringValue(sourceRow.get("LabID"), scenarioName + ".source_rows[0].LabID"),
				fallbackName,
				null
		);
		if (state != null && state.containsKey("existing_error_message")) {
			datingLab.addError(fixtureLoader.stringValue(state.get("existing_error_message"), scenarioName + ".policy_context.state.existing_error_message"));
		}
		return datingLab;
	}

	private BugsLab createBugsLab(Map<String, Object> sourceRow, String scenarioName) {
		BugsLab bugsLab = new BugsLab();
		bugsLab.setLabId(fixtureLoader.stringValue(sourceRow.get("LabID"), scenarioName + ".source_rows[0].LabID"));
		bugsLab.setLabName(fixtureLoader.stringValue(sourceRow.get("Labname"), scenarioName + ".source_rows[0].Labname"));
		bugsLab.setCountry(fixtureLoader.stringValue(sourceRow.get("Country"), scenarioName + ".source_rows[0].Country"));
		return bugsLab;
	}

	private List<String> actualPath(DatingLab result) {
		List<String> path = new ArrayList<String>();
		path.addAll(traceSearch.getRecordedPath());
		path.addAll(labIdSearch.getRecordedPath());
		if (result.getId() == null) {
			path.add("create_new");
		}
		return path;
	}

	private Map<String, Object> actualReconciliationResult(DatingLab result) {
		Map<String, Object> reconciliationResult = new LinkedHashMap<String, Object>();
		if (!result.isErrorFree()) {
			reconciliationResult.put("result_kind", "return_existing_error");
			reconciliationResult.put("persisted_action", "keep_existing_error");
			reconciliationResult.put("source", labIdSearch.getRecordedPath().isEmpty() ? "trace_lookup" : "international_lab_id_lookup");
			Map<String, Object> issue = new LinkedHashMap<String, Object>();
			issue.put("severity", "error");
			issue.put("message", result.getErrorMessages().get(0));
			reconciliationResult.put("issue", issue);
		} else if (result.getId() == null) {
			reconciliationResult.put("result_kind", "insert_new");
			reconciliationResult.put("source", "create_new");
		} else if (labIdSearch.getRecordedPath().isEmpty()) {
			reconciliationResult.put("result_kind", "update_existing");
			reconciliationResult.put("source", "trace_lookup");
		} else {
			reconciliationResult.put("result_kind", "update_existing");
			reconciliationResult.put("source", "international_lab_id_lookup");
		}
		reconciliationResult.put("row_id", result.getId());
		reconciliationResult.put("lab_id", result.getLabId());
		return reconciliationResult;
	}

	private static class RecordingDatingLabSearch implements DatingLabSearch {

		private final String ruleName;
		private DatingLab match;
		private final List<String> recordedPath = new ArrayList<String>();

		RecordingDatingLabSearch(String ruleName) {
			this.ruleName = ruleName;
			this.match = NO_LAB_FOUND;
		}

		void setMatch(DatingLab match) {
			this.match = match;
		}

		void reset() {
			recordedPath.clear();
			match = NO_LAB_FOUND;
		}

		List<String> getRecordedPath() {
			return recordedPath;
		}

		@Override
		public DatingLab findFor(BugsLab bugsLab) {
			recordedPath.add(ruleName);
			return match;
		}
	}

	private static class RecordingLocationRepository implements LocationRepository {

		private final Map<String, Location> countriesByName = new LinkedHashMap<String, Location>();

		RecordingLocationRepository() {
			countriesByName.put("Sweden", TestLocation.create(10, "Sweden", null));
		}

		@Override
		public Location findCountryByName(String countryName) {
			return countriesByName.get(countryName);
		}

		@Override
		public List<Location> findByTypeOrderByName(se.sead.bugsimport.locations.seadmodel.LocationType type) {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<Location> findAllByName(String name) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Location findOne(Integer integer) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Location saveOrUpdate(Location entity) {
			throw new UnsupportedOperationException();
		}
	}
}