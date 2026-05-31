package se.sead.bugsimport.sitelocations.converters.locations;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.site.helper.SiteFromCodeDisallowDeletedSite;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.sitelocations.bugsmodel.BugsSiteLocation;
import se.sead.bugsimport.sitelocations.converters.SiteLocationBugsTableRowConverter;
import se.sead.bugsimport.sitelocations.seadmodel.SiteLocation;
import se.sead.model.TestLocation;
import se.sead.model.TestSeadSite;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.repositories.SiteLocationRepository;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SiteLocationBugsTableRowConverterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;
	private RecordingSiteLookup siteLookup;
	private RecordingLocationManager locationManager;
	private RecordingSiteLocationRepository repository;
	private SiteLocationBugsTableRowConverter converter;

	@Before
	public void setup() throws Exception {
		fixtureLoader = new PolicyFixtureLoader();
		siteLookup = new RecordingSiteLookup();
		locationManager = new RecordingLocationManager();
		repository = new RecordingSiteLocationRepository();
		converter = new SiteLocationBugsTableRowConverter();
		injectField(converter, "siteFromBugsCodeHelper", siteLookup);
		injectField(converter, "locationManager", locationManager);
		injectField(converter, "siteLocationRepository", repository.proxy());
	}

	@Test
	public void missingImportedSiteFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("return_single_error_row_when_imported_site_missing");
	}

	@Test
	public void locationManagerErrorFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("return_generated_rows_when_location_manager_reports_error");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("sitelocations.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> args = fixtureLoader.mapValue(policyContext.get("args"), scenarioName + ".policy_context.args");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		configureScenario(args, state, scenarioName);
		List<SiteLocation> result = converter.convertListForDataRow(createBugsSiteLocation(sourceRow, scenarioName));

		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("output_result"), scenarioName + ".expects.output_result"), actualOutputResult(result));
	}

	private void configureScenario(Map<String, Object> args, Map<String, Object> state, String scenarioName) {
		repository.reset();
		locationManager.reset();
		Integer siteId = integerOrNull(args, "site_id");
		if (siteId == null) {
			siteLookup.setResolvedSite(null);
			return;
		}
		SeadSite site = TestSeadSite.create(siteId, "fixture site", null, null, null, null, null);
		siteLookup.setResolvedSite(site);
		if (state != null && state.containsKey("generated_locations")) {
			locationManager.setContainer(containerFor(site, fixtureLoader.mapValue(state.get("generated_locations"), scenarioName + ".policy_context.state.generated_locations")));
		}
	}

	private LocationContainer containerFor(SeadSite site, Map<String, Object> generatedLocations) {
		LocationContainer container = new LocationContainer(site);
		for (Map.Entry<String, Object> entry : generatedLocations.entrySet()) {
			Map<String, Object> row = fixtureLoader.mapValue(entry.getValue(), entry.getKey());
			SiteLocation siteLocation = new SiteLocation();
			siteLocation.setSite(site);
			siteLocation.setLocation(TestLocation.create(integerOrNull(row, "location_id"), stringOrNull(row, "location_name"), null));
			String errorMessage = stringOrNull(row, "error_message");
			if (errorMessage != null) {
				siteLocation.addError(errorMessage);
			}
			container.add(siteLocation);
		}
		return container;
	}

	private BugsSiteLocation createBugsSiteLocation(Map<String, Object> sourceRow, String scenarioName) {
		BugsSiteLocation bugs = new BugsSiteLocation();
		bugs.setSiteCode(stringOrNull(sourceRow, "SiteCODE"));
		bugs.setCountry(stringOrNull(sourceRow, "Country"));
		bugs.setRegion(stringOrNull(sourceRow, "Region"));
		return bugs;
	}

	private Map<String, Object> actualOutputResult(List<SiteLocation> rows) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		boolean hasErrors = false;
		for (int i = 0; i < rows.size(); i++) {
			if (!rows.get(i).isErrorFree()) {
				hasErrors = true;
				break;
			}
		}
		for (int i = 0; i < rows.size(); i++) {
			SiteLocation row = rows.get(i);
			Map<String, Object> output = new LinkedHashMap<String, Object>();
			String resultKind = row.isErrorFree() ? (hasErrors ? "return_generated" : "insert_new") : "error";
			output.put("result_kind", resultKind);
			output.put("persisted_action", "insert_new".equals(resultKind) ? "append_new" : "error".equals(resultKind) || "return_generated".equals(resultKind) ? "stop_before_list_update" : resultKind);
			output.put("site_location_id", row.getId());
			output.put("site_id", row.getSite() == null ? null : row.getSite().getId());
			output.put("location_id", row.getLocation() == null ? null : row.getLocation().getId());
			output.put("location_name", row.getLocation() == null ? null : row.getLocation().getName());
			output.put("marked_for_deletion", row.isMarkedForDeletion());
			output.put("is_error", !row.isErrorFree());
			output.put("error_message", row.isErrorFree() ? null : row.getErrorMessages().get(0));
			result.put("row_" + Integer.valueOf(i + 1), output);
		}
		return result;
	}

	private void injectField(Object target, String fieldName, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

	private Integer integerValue(Object value) {
		if (value instanceof Number) {
			return ((Number) value).intValue();
		}
		if (value instanceof String) {
			return Integer.valueOf((String) value);
		}
		throw new IllegalArgumentException("Value must be numeric");
	}

	private Integer integerOrNull(Map<String, Object> row, String key) {
		if (row == null || !row.containsKey(key) || row.get(key) == null) {
			return null;
		}
		return integerValue(row.get(key));
	}

	private String stringOrNull(Map<String, Object> row, String key) {
		if (row == null || !row.containsKey(key) || row.get(key) == null) {
			return null;
		}
		return fixtureLoader.stringValue(row.get(key), key);
	}

	private static class RecordingSiteLookup extends SiteFromCodeDisallowDeletedSite {

		private SeadSite resolvedSite;

		void setResolvedSite(SeadSite resolvedSite) {
			this.resolvedSite = resolvedSite;
		}

		@Override
		public SeadSite getSeadSiteFromBugsCode(String bugsSiteCode) {
			return resolvedSite;
		}
	}

	private static class RecordingLocationManager extends LocationManager {

		private LocationContainer container;

		void setContainer(LocationContainer container) {
			this.container = container;
		}

		void reset() {
			this.container = null;
		}

		@Override
		public LocationContainer getLocations(SeadSite site, BugsSiteLocation bugsSiteLocations) {
			return container == null ? new LocationContainer(site) : container;
		}
	}

	private static class RecordingSiteLocationRepository {

		private int findBySiteCalls;

		void reset() {
			findBySiteCalls = 0;
		}

		SiteLocationRepository proxy() {
			return (SiteLocationRepository) Proxy.newProxyInstance(
					SiteLocationRepository.class.getClassLoader(),
					new Class[]{SiteLocationRepository.class},
					(proxy, method, args) -> {
						if (method.getDeclaringClass() == Object.class) {
							if ("toString".equals(method.getName())) {
								return "RecordingSiteLocationRepository";
							}
							if ("hashCode".equals(method.getName())) {
								return System.identityHashCode(proxy);
							}
							if ("equals".equals(method.getName())) {
								return proxy == args[0];
							}
						}
						if ("findBySite".equals(method.getName())) {
							findBySiteCalls++;
							return java.util.Collections.emptyList();
						}
						return null;
					}
			);
		}
	}
}