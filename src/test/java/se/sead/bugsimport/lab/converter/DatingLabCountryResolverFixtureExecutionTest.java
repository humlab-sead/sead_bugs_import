package se.sead.bugsimport.lab.converter;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.lab.bugsmodel.BugsLab;
import se.sead.bugsimport.lab.seadmodel.DatingLab;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.model.TestLocation;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.repositories.LocationRepository;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DatingLabCountryResolverFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;
	private RecordingLocationRepository locationRepository;
	private DatingLabUpdater updater;

	@Before
	public void setup() throws Exception {
		fixtureLoader = new PolicyFixtureLoader();
		locationRepository = new RecordingLocationRepository();
		locationRepository.add(TestLocation.create(10, "Sweden", null));
		updater = new DatingLabUpdater();
		Field locationRepositoryField = DatingLabUpdater.class.getDeclaredField("locationRepository");
		locationRepositoryField.setAccessible(true);
		locationRepositoryField.set(updater, locationRepository);
	}

	@Test
	public void blankCountryFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("blank_country_emits_error");
	}

	@Test
	public void placeholderCountryFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("placeholder_country_is_ignored");
	}

	@Test
	public void directCountryLookupFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("country_lookup_returns_entity");
	}

	@Test
	public void missingCountryFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("missing_country_emits_error");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> scenario = loadScenario(scenarioName);
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		DatingLab datingLab = executeUpdaterForScenario(scenario);

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("resolver_path"), scenarioName + ".expects.resolver_path"), locationRepository.getRecordedPath());
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("resolver_result"), scenarioName + ".expects.resolver_result"), actualResolverResult(datingLab));
		if (expects.containsKey("emit_codes")) {
			assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("emit_codes"), scenarioName + ".expects.emit_codes"), listOf(errorCodeFor(datingLab.getErrorMessages().get(0))));
		}
	}

	private Map<String, Object> loadScenario(String scenarioName) throws IOException {
		Map<String, Object> fixture = fixtureLoader.loadFixture("lab.fixture.yml");
		return fixtureLoader.findScenario(fixture, scenarioName);
	}

	private DatingLab executeUpdaterForScenario(Map<String, Object> scenario) {
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), "source_rows").get(0), "source_rows[0]");
		BugsLab bugsLab = new BugsLab();
		bugsLab.setLabId(fixtureLoader.stringValue(sourceRow.get("LabID"), "LabID"));
		bugsLab.setLabName(fixtureLoader.stringValue(sourceRow.get("Labname"), "Labname"));
		bugsLab.setCountry(fixtureLoader.stringValue(sourceRow.get("Country"), "Country"));
		DatingLab datingLab = new DatingLab();
		locationRepository.resetRecordedPath();
		if (bugsLab.getCountry() == null || bugsLab.getCountry().trim().isEmpty()) {
			locationRepository.record("blank_country");
		}
		if ("Country".equals(bugsLab.getCountry())) {
			locationRepository.record("placeholder_country");
		}
		updater.update(datingLab, bugsLab);
		if (!datingLab.getErrorMessages().isEmpty() && "No country found".equals(datingLab.getErrorMessages().get(0)) && locationRepository.wasLookupAttempted()) {
			locationRepository.record("not_found");
		}
		return datingLab;
	}

	private Map<String, Object> actualResolverResult(DatingLab datingLab) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		String source = locationRepository.getRecordedPath().get(locationRepository.getRecordedPath().size() - 1);
		result.put("source", source);
		if (datingLab.isErrorFree()) {
			result.put("result_kind", "return_entity");
			result.put("country_name", datingLab.getCountry().getName());
		} else {
			result.put("result_kind", "empty_entity");
			Map<String, Object> issue = new LinkedHashMap<String, Object>();
			issue.put("severity", severityFor(datingLab.getErrorMessages().get(0)));
			issue.put("code", errorCodeFor(datingLab.getErrorMessages().get(0)));
			issue.put("message", datingLab.getErrorMessages().get(0));
			result.put("issue", issue);
		}
		return result;
	}

	private String severityFor(String errorMessage) {
		if (errorMessage.startsWith("IGNORED:")) {
			return "ignored";
		}
		return "error";
	}

	private String errorCodeFor(String errorMessage) {
		if ("No country specified".equals(errorMessage)) {
			return "country_not_specified";
		}
		if ("No country found".equals(errorMessage)) {
			return "country_not_found";
		}
		if (errorMessage.startsWith("IGNORED:")) {
			return "country_placeholder_ignored";
		}
		throw new IllegalArgumentException("Unsupported error message: " + errorMessage);
	}

	private List<String> listOf(String... values) {
		List<String> result = new ArrayList<String>();
		for (String value : values) {
			result.add(value);
		}
		return result;
	}

	private static class RecordingLocationRepository implements LocationRepository {

		private final Map<String, Location> countriesByName = new LinkedHashMap<String, Location>();
		private final List<String> recordedPath = new ArrayList<String>();
		private boolean lookupAttempted;

		void add(Location location) {
			countriesByName.put(location.getName(), location);
		}

		void record(String step) {
			recordedPath.add(step);
		}

		void resetRecordedPath() {
			recordedPath.clear();
			lookupAttempted = false;
		}

		boolean wasLookupAttempted() {
			return lookupAttempted;
		}

		List<String> getRecordedPath() {
			return recordedPath;
		}

		@Override
		public Location findCountryByName(String countryName) {
			lookupAttempted = true;
			recordedPath.add("direct_lookup");
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