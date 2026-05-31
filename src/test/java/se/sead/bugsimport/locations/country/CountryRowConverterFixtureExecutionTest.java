package se.sead.bugsimport.locations.country;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.locations.country.bugsmodel.Country;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.locations.seadmodel.LocationType;
import se.sead.model.TestLocation;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.repositories.LocationRepository;
import se.sead.repositories.LocationTypeRepository;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CountryRowConverterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;
	private RecordingTraceHelper traceHelper;
	private RecordingLocationRepository locationRepository;
	private CountryRowConverter converter;

	@Before
	public void setup() throws Exception {
		fixtureLoader = new PolicyFixtureLoader();
		traceHelper = new RecordingTraceHelper();
		locationRepository = new RecordingLocationRepository();
		converter = new CountryRowConverter(createLocationTypeRepository());
		injectField(converter, "traceHelper", traceHelper);
		injectField(converter, "locationRepository", locationRepository.proxy());
	}

	@Test
	public void traceHitFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("trace_hit_updates_existing_country");
	}

	@Test
	public void existingLookupFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("existing_country_lookup_updates_existing_country");
	}

	@Test
	public void existingLookupErrorFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("existing_country_lookup_returns_existing_error");
	}

	@Test
	public void createNewFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_new_country_when_no_match_exists");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("country.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		configureScenario(sourceRow, policyContext, state, scenarioName);
		Location result = converter.convertForDataRow(createCountry(sourceRow, scenarioName));

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("reconciliation_path"), scenarioName + ".expects.reconciliation_path"), actualPath(result));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("reconciliation_result"), scenarioName + ".expects.reconciliation_result"), actualReconciliationResult(result));
	}

	private void configureScenario(Map<String, Object> sourceRow, Map<String, Object> policyContext, Map<String, Object> state, String scenarioName) {
		Map<String, Object> stepHits = fixtureLoader.mapValue(policyContext.get("step_hits"), scenarioName + ".policy_context.step_hits");
		traceHelper.reset();
		locationRepository.reset();
		if (Boolean.TRUE.equals(stepHits.get("trace_lookup"))) {
			traceHelper.setMatch(existingLocation(sourceRow, state, scenarioName));
		}
		if (Boolean.TRUE.equals(stepHits.get("existing_country_lookup"))) {
			locationRepository.setCountryByName(existingLocation(sourceRow, state, scenarioName));
		}
	}

	private Location existingLocation(Map<String, Object> sourceRow, Map<String, Object> state, String scenarioName) {
		Location location = TestLocation.create(
				state == null ? null : fixtureLoader.integerValue(state.get("existing_row_id"), scenarioName + ".policy_context.state.existing_row_id"),
				fixtureLoader.stringValue(sourceRow.get("Country"), scenarioName + ".source_rows[0].Country"),
				null
		);
		if (state != null && state.containsKey("existing_error_message")) {
			location.addError(fixtureLoader.stringValue(state.get("existing_error_message"), scenarioName + ".policy_context.state.existing_error_message"));
		}
		return location;
	}

	private Country createCountry(Map<String, Object> sourceRow, String scenarioName) {
		Country country = new Country();
		country.setCountryCode(fixtureLoader.stringValue(sourceRow.get("CountryCode"), scenarioName + ".source_rows[0].CountryCode"));
		country.setCountry(fixtureLoader.stringValue(sourceRow.get("Country"), scenarioName + ".source_rows[0].Country"));
		return country;
	}

	private List<String> actualPath(Location result) {
		List<String> path = new ArrayList<String>();
		path.addAll(traceHelper.getRecordedPath());
		path.addAll(locationRepository.getRecordedPath());
		if (!traceHelper.didMatch() && !locationRepository.didMatch() && result.getId() == null && result.isErrorFree()) {
			path.add("create_new");
		}
		return path;
	}

	private Map<String, Object> actualReconciliationResult(Location result) {
		Map<String, Object> reconciliationResult = new LinkedHashMap<String, Object>();
		String matchedRuleName = matchedRuleName();
		if (!result.isErrorFree()) {
			reconciliationResult.put("result_kind", "return_existing_error");
			reconciliationResult.put("persisted_action", "keep_existing_error");
			reconciliationResult.put("source", matchedRuleName);
			Map<String, Object> issue = new LinkedHashMap<String, Object>();
			issue.put("severity", "error");
			issue.put("message", result.getErrorMessages().get(0));
			reconciliationResult.put("issue", issue);
		} else if (result.getId() == null && result.isErrorFree()) {
			reconciliationResult.put("result_kind", "insert_new");
			reconciliationResult.put("source", "create_new");
		} else {
			reconciliationResult.put("result_kind", "update_existing");
			reconciliationResult.put("source", matchedRuleName);
		}
		assertTrue("Expected existing-country lookup branch to mark the row updated", !"existing_country_lookup".equals(matchedRuleName) || result.isUpdated());
		reconciliationResult.put("row_id", result.getId());
		reconciliationResult.put("country_code", traceHelper.getCurrentCountryCode());
		return reconciliationResult;
	}

	private String matchedRuleName() {
		if (traceHelper.didMatch()) {
			return "trace_lookup";
		}
		if (locationRepository.didMatch()) {
			return "existing_country_lookup";
		}
		return null;
	}

	private void injectField(Object target, String fieldName, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

	private LocationTypeRepository createLocationTypeRepository() {
		LocationType countryType = new TestLocationType(1);
		countryType.setName("Country");
		countryType.setDescription("Country type");
		return (LocationTypeRepository) Proxy.newProxyInstance(
				LocationTypeRepository.class.getClassLoader(),
				new Class[]{LocationTypeRepository.class},
				(proxy, method, args) -> {
					if (method.getDeclaringClass() == Object.class) {
						if ("toString".equals(method.getName())) {
							return "RecordingLocationTypeRepository";
						}
						if ("hashCode".equals(method.getName())) {
							return System.identityHashCode(proxy);
						}
						if ("equals".equals(method.getName())) {
							return proxy == args[0];
						}
					}
					if ("getCountryType".equals(method.getName())) {
						return countryType;
					}
					return null;
				}
		);
	}

	private static class RecordingTraceHelper extends CountryImportTraceHelper {

		private Location match;
		private final List<String> recordedPath = new ArrayList<String>();
		private String currentCountryCode;
		private boolean didMatch;

		private RecordingTraceHelper() {
			super(null);
		}

		void setMatch(Location match) {
			this.match = match;
		}

		void reset() {
			recordedPath.clear();
			match = null;
			currentCountryCode = null;
			didMatch = false;
		}

		List<String> getRecordedPath() {
			return recordedPath;
		}

		String getCurrentCountryCode() {
			return currentCountryCode;
		}

		boolean didMatch() {
			return didMatch;
		}

		@Override
		public Location getFromLastTrace(String bugsIdentifier) {
			recordedPath.add("trace_lookup");
			currentCountryCode = bugsIdentifier;
			didMatch = match != null;
			return match;
		}
	}

	private static class RecordingLocationRepository {

		private Location countryByName;
		private final List<String> recordedPath = new ArrayList<String>();
		private boolean didMatch;

		void setCountryByName(Location countryByName) {
			this.countryByName = countryByName;
		}

		void reset() {
			recordedPath.clear();
			countryByName = null;
			didMatch = false;
		}

		List<String> getRecordedPath() {
			return recordedPath;
		}

		boolean didMatch() {
			return didMatch;
		}

		LocationRepository proxy() {
			return (LocationRepository) Proxy.newProxyInstance(
					LocationRepository.class.getClassLoader(),
					new Class[]{LocationRepository.class},
					(proxy, method, args) -> {
						if (method.getDeclaringClass() == Object.class) {
							if ("toString".equals(method.getName())) {
								return "RecordingLocationRepository";
							}
							if ("hashCode".equals(method.getName())) {
								return System.identityHashCode(proxy);
							}
							if ("equals".equals(method.getName())) {
								return proxy == args[0];
							}
						}
						if ("findCountryByName".equals(method.getName())) {
							recordedPath.add("existing_country_lookup");
							didMatch = countryByName != null;
							return countryByName;
						}
						return null;
					}
			);
		}
	}

	private static class TestLocationType extends LocationType {

		private TestLocationType(Integer id) {
			super.setId(id);
		}
	}
}