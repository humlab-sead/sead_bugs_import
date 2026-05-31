package se.sead.bugsimport.site.conversion;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.locations.seadmodel.LocationType;
import se.sead.bugsimport.site.bugsmodel.BugsSite;
import se.sead.bugsimport.site.helper.SiteFromCodeAllowDeletedSite;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.model.TestLocation;
import se.sead.model.TestSeadSite;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.repositories.LocationRepository;
import se.sead.repositories.LocationTypeRepository;
import se.sead.repositories.SiteRepository;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BugsSiteTableConverterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;
	private RecordingSiteLookup siteLookup;
	private RecordingLocationRepository locationRepository;
	private RecordingSiteRepository siteRepository;
	private BugsSiteTableConverter converter;

	@Before
	public void setup() throws Exception {
		fixtureLoader = new PolicyFixtureLoader();
		siteLookup = new RecordingSiteLookup();
		locationRepository = new RecordingLocationRepository();
		siteRepository = new RecordingSiteRepository();
		converter = new BugsSiteTableConverter();
		injectField(converter, "locationTypeRepository", createLocationTypeRepository());
		injectField(converter, "locationRepository", locationRepository.proxy());
		injectField(converter, "siteRepository", siteRepository.proxy());
		injectField(converter, "importSiteHelper", siteLookup);
		injectField(converter, "canCreateCountry", Boolean.FALSE);
		injectField(converter, "allowSiteUpdates", Boolean.FALSE);
	}

	@Test
	public void missingCountryFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("missing_country_prerequisite_returns_guard_error");
	}

	@Test
	public void updateDisallowedFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("trace_hit_updates_disallowed_returns_guard_error");
	}

	@Test
	public void traceHitFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("trace_hit_updates_existing_site");
	}

	@Test
	public void traceErrorFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("trace_hit_returns_existing_error_row");
	}

	@Test
	public void singleSiteByNameAndLocationFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("name_location_lookup_returns_site_exists_error");
	}

	@Test
	public void multipleSitesByNameAndLocationFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("name_location_lookup_returns_multiple_sites_error");
	}

	@Test
	public void createNewFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_new_site_when_no_match_exists");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("site.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		configureScenario(sourceRow, policyContext, state, scenarioName);
		SeadSite result = converter.convertForDataRow(createBugsSite(sourceRow, scenarioName));

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("reconciliation_path"), scenarioName + ".expects.reconciliation_path"), actualPath(result));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("reconciliation_result"), scenarioName + ".expects.reconciliation_result"), actualReconciliationResult(result, sourceRow));
	}

	private void configureScenario(Map<String, Object> sourceRow, Map<String, Object> policyContext, Map<String, Object> state, String scenarioName) {
		Map<String, Object> stepHits = fixtureLoader.mapValue(policyContext.get("step_hits"), scenarioName + ".policy_context.step_hits");
		siteLookup.reset();
		locationRepository.reset();
		siteRepository.reset();
		injectBooleanField(converter, "canCreateCountry", Boolean.FALSE);
		injectBooleanField(converter, "allowSiteUpdates", Boolean.FALSE);

		String countryName = nullableStringValue(sourceRow.get("Country"), scenarioName + ".source_rows[0].Country");
		Location country = countryName == null ? null : TestLocation.create(1001, countryName, countryType());
		Location region = TestLocation.create(1002, fixtureLoader.stringValue(sourceRow.get("Region"), scenarioName + ".source_rows[0].Region"), regionType());
		locationRepository.setCountry(country);
		locationRepository.setRegions(Arrays.asList(region));

		if (Boolean.TRUE.equals(stepHits.get("trace_lookup"))) {
			if (state != null && state.containsKey("existing_error_message")) {
				SeadSite errorSite = new SeadSite();
				errorSite.addError(fixtureLoader.stringValue(state.get("existing_error_message"), scenarioName + ".policy_context.state.existing_error_message"));
				siteLookup.setResolvedSite(errorSite);
			} else {
				siteLookup.setResolvedSite(existingSite(sourceRow, state, scenarioName, scenarioName.startsWith("trace_hit_updates_disallowed")));
			}
		}

		if (Boolean.TRUE.equals(stepHits.get("name_location_lookup"))) {
			String errorMessage = state == null ? null : fixtureLoader.stringValue(state.get("existing_error_message"), scenarioName + ".policy_context.state.existing_error_message");
			if ("Site name exists for non-imported site".equals(errorMessage)) {
				siteRepository.setByNameAndLocations(Arrays.asList(existingSite(sourceRow, null, scenarioName, false)));
			} else if ("More than one site found by name and location".equals(errorMessage)) {
				siteRepository.setByNameAndLocations(Arrays.asList(existingSite(sourceRow, null, scenarioName, false), existingSite(sourceRow, null, scenarioName, false)));
			}
		}
	}

	private SeadSite existingSite(Map<String, Object> sourceRow, Map<String, Object> state, String scenarioName, boolean createChangedVersion) {
		SeadSite site = TestSeadSite.create(
				state != null && state.containsKey("existing_row_id") ? fixtureLoader.integerValue(state.get("existing_row_id"), scenarioName + ".policy_context.state.existing_row_id") : 903,
				createChangedVersion ? fixtureLoader.stringValue(sourceRow.get("SiteName"), scenarioName + ".source_rows[0].SiteName") + " old" : fixtureLoader.stringValue(sourceRow.get("SiteName"), scenarioName + ".source_rows[0].SiteName"),
				createChangedVersion ? fixtureLoader.stringValue(sourceRow.get("NGR"), scenarioName + ".source_rows[0].NGR") + " old" : fixtureLoader.stringValue(sourceRow.get("NGR"), scenarioName + ".source_rows[0].NGR"),
				createChangedVersion ? new BigDecimal("99.1") : bigDecimalValue(sourceRow.get("LatDD")),
				createChangedVersion ? new BigDecimal("99.2") : bigDecimalValue(sourceRow.get("LongDD")),
				createChangedVersion ? new BigDecimal("99.3") : bigDecimalValue(sourceRow.get("Alt")),
				createChangedVersion ? fixtureLoader.stringValue(sourceRow.get("Interp"), scenarioName + ".source_rows[0].Interp") + " old" : fixtureLoader.stringValue(sourceRow.get("Interp"), scenarioName + ".source_rows[0].Interp")
		);
		return site;
	}

	private BugsSite createBugsSite(Map<String, Object> sourceRow, String scenarioName) {
		BugsSite bugsSite = new BugsSite();
		bugsSite.setCode(fixtureLoader.stringValue(sourceRow.get("SiteCODE"), scenarioName + ".source_rows[0].SiteCODE"));
		bugsSite.setName(nullableStringValue(sourceRow.get("SiteName"), scenarioName + ".source_rows[0].SiteName"));
		bugsSite.setRegion(nullableStringValue(sourceRow.get("Region"), scenarioName + ".source_rows[0].Region"));
		bugsSite.setCountry(nullableStringValue(sourceRow.get("Country"), scenarioName + ".source_rows[0].Country"));
		bugsSite.setNgr(nullableStringValue(sourceRow.get("NGR"), scenarioName + ".source_rows[0].NGR"));
		bugsSite.setLatDD(floatValue(sourceRow.get("LatDD")));
		bugsSite.setLongDD(floatValue(sourceRow.get("LongDD")));
		bugsSite.setAlt(floatValue(sourceRow.get("Alt")));
		bugsSite.setIDBy(nullableStringValue(sourceRow.get("IDBy"), scenarioName + ".source_rows[0].IDBy"));
		bugsSite.setInterp(nullableStringValue(sourceRow.get("Interp"), scenarioName + ".source_rows[0].Interp"));
		bugsSite.setSpecimens(nullableStringValue(sourceRow.get("Specimens"), scenarioName + ".source_rows[0].Specimens"));
		return bugsSite;
	}

	private List<String> actualPath(SeadSite result) {
		List<String> path = new ArrayList<String>();
		if (!result.isErrorFree() && "No country exists for site".equals(result.getErrorMessages().get(0))) {
			path.add("resolve_locations");
			return path;
		}
		path.addAll(siteLookup.getRecordedPath());
		path.addAll(siteRepository.getRecordedPath());
		if (!siteLookup.didMatch() && !siteRepository.didMatch() && result.getId() == null && result.isErrorFree()) {
			path.add("create_new");
		}
		return path;
	}

	private Map<String, Object> actualReconciliationResult(SeadSite result, Map<String, Object> sourceRow) {
		Map<String, Object> reconciliationResult = new LinkedHashMap<String, Object>();
		String matchedRuleName = matchedRuleName();
		if (!result.isErrorFree()) {
			String errorMessage = result.getErrorMessages().get(0);
			if ("No country exists for site".equals(errorMessage)) {
				reconciliationResult.put("result_kind", "return_guard_error");
				reconciliationResult.put("source", "resolve_locations");
			} else if ("Bugs data is updated but updates are disallowed.".equals(errorMessage)) {
				reconciliationResult.put("result_kind", "return_guard_error");
				reconciliationResult.put("source", matchedRuleName);
			} else {
				reconciliationResult.put("result_kind", "return_existing_error");
				reconciliationResult.put("source", matchedRuleName);
			}
			Map<String, Object> issue = new LinkedHashMap<String, Object>();
			issue.put("severity", "error");
			issue.put("message", errorMessage);
			reconciliationResult.put("issue", issue);
		} else if (result.getId() == null) {
			reconciliationResult.put("result_kind", "insert_new");
			reconciliationResult.put("source", "create_new");
		} else {
			reconciliationResult.put("result_kind", "update_existing");
			reconciliationResult.put("source", matchedRuleName);
		}
		reconciliationResult.put("row_id", result.getId());
		reconciliationResult.put("site_code", siteLookup.getCurrentSiteCode() == null ? sourceRow.get("SiteCODE") : siteLookup.getCurrentSiteCode());
		return reconciliationResult;
	}

	private void injectBooleanField(Object target, String fieldName, Boolean value) {
		try {
			injectField(target, fieldName, value);
		} catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
	}

	private String nullableStringValue(Object value, String context) {
		if (value == null) {
			return null;
		}
		return fixtureLoader.stringValue(value, context);
	}

	private String matchedRuleName() {
		if (siteLookup.didMatch()) {
			return "trace_lookup";
		}
		if (siteRepository.didMatch()) {
			return "name_location_lookup";
		}
		return null;
	}

	private void injectField(Object target, String fieldName, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

	private LocationTypeRepository createLocationTypeRepository() {
		LocationType countryType = countryType();
		LocationType regionType = regionType();
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
					if ("getAdministrativeRegionType".equals(method.getName())) {
						return regionType;
					}
					return null;
				}
		);
	}

	private LocationType countryType() {
		TestLocationType type = new TestLocationType(1);
		type.setName("Country");
		type.setDescription("Country type");
		return type;
	}

	private LocationType regionType() {
		TestLocationType type = new TestLocationType(2);
		type.setName("Sub-country administrative region");
		type.setDescription("Region type");
		return type;
	}

	private Float floatValue(Object value) {
		if (!(value instanceof Number)) {
			throw new IllegalArgumentException("Value must be numeric");
		}
		return ((Number) value).floatValue();
	}

	private BigDecimal bigDecimalValue(Object value) {
		return value == null ? null : new BigDecimal(String.valueOf(((Number) value).doubleValue()));
	}

	private static class RecordingSiteLookup extends SiteFromCodeAllowDeletedSite {

		private SeadSite resolvedSite;
		private final List<String> recordedPath = new ArrayList<String>();
		private String currentSiteCode;
		private boolean didMatch;

		void setResolvedSite(SeadSite resolvedSite) {
			this.resolvedSite = resolvedSite;
		}

		void reset() {
			resolvedSite = null;
			recordedPath.clear();
			currentSiteCode = null;
			didMatch = false;
		}

		List<String> getRecordedPath() {
			return recordedPath;
		}

		String getCurrentSiteCode() {
			return currentSiteCode;
		}

		boolean didMatch() {
			return didMatch;
		}

		@Override
		public SeadSite getSeadSiteFromBugsCode(String bugsSiteCode) {
			recordedPath.add("trace_lookup");
			currentSiteCode = bugsSiteCode;
			didMatch = resolvedSite != null;
			return resolvedSite;
		}
	}

	private static class RecordingLocationRepository {

		private Location country;
		private List<Location> regions = new ArrayList<Location>();

		void setCountry(Location country) {
			this.country = country;
		}

		void setRegions(List<Location> regions) {
			this.regions = regions;
		}

		void reset() {
			country = null;
			regions = new ArrayList<Location>();
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
							return country;
						}
						if ("findAllByName".equals(method.getName())) {
							return regions;
						}
						return null;
					}
			);
		}
	}

	private static class RecordingSiteRepository {

		private List<SeadSite> byNameAndLocations = new ArrayList<SeadSite>();
		private final List<String> recordedPath = new ArrayList<String>();
		private boolean didMatch;

		void setByNameAndLocations(List<SeadSite> byNameAndLocations) {
			this.byNameAndLocations = byNameAndLocations;
		}

		void reset() {
			byNameAndLocations = new ArrayList<SeadSite>();
			recordedPath.clear();
			didMatch = false;
		}

		List<String> getRecordedPath() {
			return recordedPath;
		}

		boolean didMatch() {
			return didMatch;
		}

		SiteRepository proxy() {
			return (SiteRepository) Proxy.newProxyInstance(
					SiteRepository.class.getClassLoader(),
					new Class[]{SiteRepository.class},
					(proxy, method, args) -> {
						if (method.getDeclaringClass() == Object.class) {
							if ("toString".equals(method.getName())) {
								return "RecordingSiteRepository";
							}
							if ("hashCode".equals(method.getName())) {
								return System.identityHashCode(proxy);
							}
							if ("equals".equals(method.getName())) {
								return proxy == args[0];
							}
						}
						if ("findByNameAndLocations".equals(method.getName())) {
							recordedPath.add("name_location_lookup");
							didMatch = !byNameAndLocations.isEmpty();
							return byNameAndLocations;
						}
						if ("findAllByName".equals(method.getName())) {
							recordedPath.add("name_location_lookup");
							didMatch = !byNameAndLocations.isEmpty();
							return byNameAndLocations;
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