package se.sead.bugsimport.siteotherproxies.converters;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.siteotherproxies.bugsmodel.SiteOtherProxies;
import se.sead.bugsimport.siteotherproxies.seadmodel.SiteOtherRecord;
import se.sead.model.TestSeadSite;
import se.sead.model.TestSiteOtherRecord;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.sead.recordtypes.RecordType;
import se.sead.sead.recordtypes.RecordTypeCache;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SiteOtherRecordUpdaterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;

	@Before
	public void setup() {
		fixtureLoader = new PolicyFixtureLoader();
	}

	@Test
	public void createSiteOtherRecordsFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_site_other_records_when_none_exist");
	}

	@Test
	public void updateSiteOtherRecordsFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("keep_existing_mark_missing_and_append_new_site_other_records");
	}

	@Test
	public void keepSiteOtherRecordsFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("keep_existing_site_other_records_when_enabled_proxies_match");
	}

	@Test
	public void deleteSiteOtherRecordsFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("mark_existing_site_other_records_for_deletion_when_no_proxies_enabled");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("siteotherproxies.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> args = fixtureLoader.mapValue(policyContext.get("args"), scenarioName + ".policy_context.args");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		Map<String, Object> recordTypes = fixtureLoader.mapValue(state.get("record_types"), scenarioName + ".policy_context.state.record_types");
		SeadSite site = TestSeadSite.create(integerValue(args.get("site_id")), "fixture site", null, null, null, null, null);
		List<SiteOtherRecord> priorVersions = existingSiteOtherRecords(state, site, scenarioName);
		SiteOtherRecordUpdater updater = new SiteOtherRecordUpdater(createBugsData(sourceRow), new RecordingRecordTypeCache(recordTypes), site);
		updater.update(priorVersions);

		assertEquals(fixtureLoader.booleanValue(expects.get("row_changed"), scenarioName + ".expects.row_changed"), rowChanged(priorVersions));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("output_result"), scenarioName + ".expects.output_result"), actualOutputResult(priorVersions));
	}

	private boolean rowChanged(List<SiteOtherRecord> rows) {
		for (int i = 0; i < rows.size(); i++) {
			SiteOtherRecord row = rows.get(i);
			if (row.isMarkedForDeletion() || row.getId() == null) {
				return true;
			}
		}
		return false;
	}

	private SiteOtherProxies createBugsData(Map<String, Object> sourceRow) {
		SiteOtherProxies bugsData = new SiteOtherProxies();
		bugsData.setSiteCode(stringValue(sourceRow.get("SiteCODE")));
		addIfEnabled(bugsData, sourceRow, "HasPollen", BugsProxyToSeadRecordTypeNames.Pollen);
		addIfEnabled(bugsData, sourceRow, "HasPlantMacro", BugsProxyToSeadRecordTypeNames.PlantMacro);
		addIfEnabled(bugsData, sourceRow, "HasDiatoms", BugsProxyToSeadRecordTypeNames.Diatoms);
		addIfEnabled(bugsData, sourceRow, "HasChironomids", BugsProxyToSeadRecordTypeNames.Chironomids);
		addIfEnabled(bugsData, sourceRow, "HasSoilChemistry", BugsProxyToSeadRecordTypeNames.SoilChemistry);
		addIfEnabled(bugsData, sourceRow, "HasIsotopes", BugsProxyToSeadRecordTypeNames.Isotopes);
		addIfEnabled(bugsData, sourceRow, "HasAnimalBones", BugsProxyToSeadRecordTypeNames.AnimalBones);
		addIfEnabled(bugsData, sourceRow, "HasArchaeology", BugsProxyToSeadRecordTypeNames.Archaeology);
		addIfEnabled(bugsData, sourceRow, "HasMolluscs", BugsProxyToSeadRecordTypeNames.Molluscs);
		return bugsData;
	}

	private void addIfEnabled(SiteOtherProxies bugsData, Map<String, Object> sourceRow, String fieldName, BugsProxyToSeadRecordTypeNames proxyName) {
		if (booleanValue(sourceRow.get(fieldName))) {
			bugsData.addProxy(proxyName);
		}
	}

	private List<SiteOtherRecord> existingSiteOtherRecords(Map<String, Object> state, SeadSite site, String scenarioName) {
		List<SiteOtherRecord> rows = new ArrayList<SiteOtherRecord>();
		if (state == null || !state.containsKey("existing_site_other_records")) {
			return rows;
		}
		Map<String, Object> existingRows = fixtureLoader.mapValue(state.get("existing_site_other_records"), scenarioName + ".policy_context.state.existing_site_other_records");
		Map<String, Object> recordTypes = fixtureLoader.mapValue(state.get("record_types"), scenarioName + ".policy_context.state.record_types");
		for (Map.Entry<String, Object> entry : existingRows.entrySet()) {
			Map<String, Object> row = fixtureLoader.mapValue(entry.getValue(), entry.getKey());
			rows.add(TestSiteOtherRecord.create(
					integerOrNull(row, "site_other_record_id"),
					recordTypeById(recordTypes, integerOrNull(row, "record_type_id")),
					site,
					null
			));
		}
		return rows;
	}

	private Map<String, Object> actualOutputResult(List<SiteOtherRecord> rows) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		for (int i = 0; i < rows.size(); i++) {
			SiteOtherRecord row = rows.get(i);
			Map<String, Object> output = new LinkedHashMap<String, Object>();
			String resultKind = row.isMarkedForDeletion() ? "mark_for_deletion" : row.getId() == null ? "insert_new" : "keep_existing";
			output.put("result_kind", resultKind);
			output.put("persisted_action", "insert_new".equals(resultKind) ? "append_new" : "keep_existing".equals(resultKind) ? "keep_existing" : "mark_for_deletion");
			output.put("site_other_record_id", row.getId());
			output.put("site_id", row.getSite() == null ? null : row.getSite().getId());
			output.put("record_type_id", row.getRecordType() == null ? null : row.getRecordType().getId());
			output.put("record_type_name", row.getRecordType() == null ? null : row.getRecordType().getName());
			output.put("marked_for_deletion", row.isMarkedForDeletion());
			result.put("row_" + Integer.valueOf(i + 1), output);
		}
		return result;
	}

	private RecordType recordTypeById(Map<String, Object> recordTypes, Integer recordTypeId) {
		for (Map.Entry<String, Object> entry : recordTypes.entrySet()) {
			Map<String, Object> row = fixtureLoader.mapValue(entry.getValue(), entry.getKey());
			if (recordTypeId.equals(integerOrNull(row, "record_type_id"))) {
				return createRecordType(recordTypeId, stringOrNull(row, "record_type_name"));
			}
		}
		return createRecordType(recordTypeId, "record type " + recordTypeId);
	}

	private RecordType createRecordType(Integer id, String name) {
		TestRecordType type = new TestRecordType(id);
		type.setName(name);
		type.setDescription(name + " description");
		return type;
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

	private String stringValue(Object value) {
		if (!(value instanceof String)) {
			throw new IllegalArgumentException("Value must be a string");
		}
		return (String) value;
	}

	private String stringOrNull(Map<String, Object> row, String key) {
		if (row == null || !row.containsKey(key) || row.get(key) == null) {
			return null;
		}
		return stringValue(row.get(key));
	}

	private boolean booleanValue(Object value) {
		if (!(value instanceof Boolean)) {
			throw new IllegalArgumentException("Value must be boolean");
		}
		return (Boolean) value;
	}

	private static class RecordingRecordTypeCache extends RecordTypeCache {

		private final Map<String, RecordType> recordTypes = new LinkedHashMap<String, RecordType>();

		private RecordingRecordTypeCache(Map<String, Object> recordTypes) {
			for (Map.Entry<String, Object> entry : recordTypes.entrySet()) {
				Map<String, Object> row = castMap(entry.getValue(), entry.getKey());
				RecordType type = new TestRecordType(((Number) row.get("record_type_id")).intValue());
				type.setName((String) row.get("record_type_name"));
				type.setDescription(type.getName() + " description");
				this.recordTypes.put(type.getName(), type);
			}
		}

		@Override
		public RecordType getByName(String typeName) {
			RecordType type = recordTypes.get(typeName);
			if (type == null) {
				throw new IllegalStateException("No record type found for name: " + typeName);
			}
			return type;
		}

		@SuppressWarnings("unchecked")
		private static Map<String, Object> castMap(Object value, String context) {
			if (!(value instanceof Map)) {
				throw new IllegalArgumentException(context + " must be a mapping");
			}
			return (Map<String, Object>) value;
		}
	}

	private static class TestRecordType extends RecordType {

		private TestRecordType(Integer id) {
			super.setId(id);
		}
	}
}