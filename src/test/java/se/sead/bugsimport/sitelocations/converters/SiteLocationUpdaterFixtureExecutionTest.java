package se.sead.bugsimport.sitelocations.converters;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.sitelocations.seadmodel.SiteLocation;
import se.sead.model.TestLocation;
import se.sead.model.TestSeadSite;
import se.sead.model.TestSiteLocation;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SiteLocationUpdaterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;

	@Before
	public void setup() {
		fixtureLoader = new PolicyFixtureLoader();
	}

	@Test
	public void createSiteLocationsFixtureMatchesCurrentJavaBehavior() {
		assertScenarioMatchesCurrentJavaBehavior("create_site_locations_when_none_exist");
	}

	@Test
	public void updateSiteLocationsFixtureMatchesCurrentJavaBehavior() {
		assertScenarioMatchesCurrentJavaBehavior("mark_missing_and_append_new_site_locations_when_lists_differ");
	}

	@Test
	public void keepSiteLocationsFixtureMatchesCurrentJavaBehavior() {
		assertScenarioMatchesCurrentJavaBehavior("keep_existing_site_locations_when_lists_match");
	}

	@Test
	public void deleteAllSiteLocationsFixtureMatchesCurrentJavaBehavior() {
		assertScenarioMatchesCurrentJavaBehavior("mark_all_existing_site_locations_for_deletion_when_generated_empty");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) {
		Map<String, Object> fixture = fixtureLoader.loadFixtureUnchecked("sitelocations.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> args = fixtureLoader.mapValue(policyContext.get("args"), scenarioName + ".policy_context.args");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		SeadSite site = TestSeadSite.create(integerValue(args.get("site_id")), "fixture site", null, null, null, null, null);
		List<SiteLocation> bugsLocations = createGeneratedLocations(args, state, site, scenarioName);
		List<SiteLocation> storedLocations = createStoredLocations(state, site, scenarioName);
		SiteLocationUpdater updater = new SiteLocationUpdater(bugsLocations, storedLocations);
		List<SiteLocation> result = updater.getClearedItems();

		assertEquals(fixtureLoader.booleanValue(expects.get("row_changed"), scenarioName + ".expects.row_changed"), rowChanged(result));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("output_result"), scenarioName + ".expects.output_result"), actualOutputResult(result));
	}

	private boolean rowChanged(List<SiteLocation> rows) {
		for (int i = 0; i < rows.size(); i++) {
			SiteLocation row = rows.get(i);
			if (row.isMarkedForDeletion() || row.getId() == null) {
				return true;
			}
		}
		return false;
	}

	private List<SiteLocation> createGeneratedLocations(Map<String, Object> args, Map<String, Object> state, SeadSite site, String scenarioName) {
		List<SiteLocation> rows = new ArrayList<SiteLocation>();
		List<Integer> generatedIds = integerCsvValue(args.get("generated_location_ids"), scenarioName + ".policy_context.args.generated_location_ids");
		Map<String, Object> generatedLocations = state != null && state.containsKey("generated_locations") ? fixtureLoader.mapValue(state.get("generated_locations"), scenarioName + ".policy_context.state.generated_locations") : null;
		for (int i = 0; i < generatedIds.size(); i++) {
			Integer locationId = generatedIds.get(i);
			rows.add(TestSiteLocation.create(null, locationFor(generatedLocations, locationId), site));
		}
		return rows;
	}

	private List<SiteLocation> createStoredLocations(Map<String, Object> state, SeadSite site, String scenarioName) {
		List<SiteLocation> rows = new ArrayList<SiteLocation>();
		if (state == null || !state.containsKey("existing_site_locations")) {
			return rows;
		}
		Map<String, Object> existingLocations = fixtureLoader.mapValue(state.get("existing_site_locations"), scenarioName + ".policy_context.state.existing_site_locations");
		for (Map.Entry<String, Object> entry : existingLocations.entrySet()) {
			Map<String, Object> row = fixtureLoader.mapValue(entry.getValue(), entry.getKey());
			rows.add(TestSiteLocation.create(
					integerOrNull(row, "site_location_id"),
					TestLocation.create(integerOrNull(row, "location_id"), stringOrNull(row, "location_name"), null),
					site
			));
		}
		return rows;
	}

	private Location locationFor(Map<String, Object> generatedLocations, Integer locationId) {
		if (generatedLocations == null) {
			return TestLocation.create(locationId, null, null);
		}
		for (Map.Entry<String, Object> entry : generatedLocations.entrySet()) {
			Map<String, Object> row = fixtureLoader.mapValue(entry.getValue(), entry.getKey());
			if (locationId.equals(integerOrNull(row, "location_id"))) {
				return TestLocation.create(locationId, stringOrNull(row, "location_name"), null);
			}
		}
		return TestLocation.create(locationId, null, null);
	}

	private Map<String, Object> actualOutputResult(List<SiteLocation> rows) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		for (int i = 0; i < rows.size(); i++) {
			SiteLocation row = rows.get(i);
			Map<String, Object> output = new LinkedHashMap<String, Object>();
			String resultKind = row.isMarkedForDeletion() ? "mark_for_deletion" : row.getId() == null ? "insert_new" : "keep_existing";
			output.put("result_kind", resultKind);
			output.put("persisted_action", row.isErrorFree() ? persistedAction(resultKind) : "stop_before_list_update");
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

	private String persistedAction(String resultKind) {
		if ("keep_existing".equals(resultKind)) {
			return "keep_existing";
		}
		if ("insert_new".equals(resultKind)) {
			return "append_new";
		}
		return "mark_for_deletion";
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

	private List<Integer> integerCsvValue(Object value, String context) {
		String raw = fixtureLoader.stringValue(value, context);
		List<Integer> result = new ArrayList<Integer>();
		if (raw.trim().isEmpty()) {
			return result;
		}
		String[] parts = raw.split(",");
		for (int i = 0; i < parts.length; i++) {
			result.add(integerValue(parts[i].trim()));
		}
		return result;
	}
}