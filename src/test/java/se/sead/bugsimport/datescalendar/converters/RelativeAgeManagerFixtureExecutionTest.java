package se.sead.bugsimport.datescalendar.converters;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.periods.converters.RelativeAgeCache;
import se.sead.bugsimport.periods.converters.RelativeAgeUpdater;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.bugsimport.periods.seadmodel.RelativeAgeType;
import se.sead.model.TestRelativeAge;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.repositories.RelativeAgeRepository;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RelativeAgeManagerFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;

	@Before
	public void setup() {
		fixtureLoader = new PolicyFixtureLoader();
	}

	@Test
	public void createSingleRelativeAgeFixtureMatchesCurrentJavaBehavior() throws IOException {
		Map<String, Object> fixture = fixtureLoader.loadFixture("datescalendar.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, "create_single_relative_age_when_missing");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), "create_single_relative_age_when_missing.expects");
		RelativeAge relativeAge = executeScenario(scenario, "create_single_relative_age_when_missing");

		assertEquals(fixtureLoader.booleanValue(expects.get("row_changed"), "create_single_relative_age_when_missing.expects.row_changed"), rowChanged(relativeAge));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("graph_result"), "create_single_relative_age_when_missing.expects.graph_result"), actualGraphResult(relativeAge));
	}

	@Test
	public void reuseSingleRelativeAgeFixtureMatchesCurrentJavaBehavior() throws IOException {
		Map<String, Object> fixture = fixtureLoader.loadFixture("datescalendar.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, "reuse_single_relative_age_when_present");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), "reuse_single_relative_age_when_present.expects");
		RelativeAge relativeAge = executeScenario(scenario, "reuse_single_relative_age_when_present");

		assertEquals(fixtureLoader.booleanValue(expects.get("row_changed"), "reuse_single_relative_age_when_present.expects.row_changed"), rowChanged(relativeAge));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("graph_result"), "reuse_single_relative_age_when_present.expects.graph_result"), actualGraphResult(relativeAge));
	}

	private boolean rowChanged(RelativeAge relativeAge) {
		return relativeAge.getId() == null;
	}

	private RelativeAge executeScenario(Map<String, Object> scenario, String scenarioName) {
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> state = policyContext.containsKey("state")
				? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state")
				: Collections.<String, Object>emptyMap();
		RelativeAgeManager manager = new RelativeAgeManager();
		setField(manager, "relativeAgeRepository", new RecordingRelativeAgeRepository(existingRelativeAge(state, sourceRow, scenarioName)));
		setField(manager, "updater", new RecordingRelativeAgeUpdater());
		setField(manager, "typeManager", new RelativeAgeTypeManager("Calendar date range", "Calendar date"));
		setField(manager, "ageCache", new RelativeAgeCache());
		return manager.getOrCreateRelativeAge(createDatesCalendar(sourceRow, scenarioName));
	}

	private RelativeAge existingRelativeAge(Map<String, Object> state, Map<String, Object> sourceRow, String scenarioName) {
		if (!state.containsKey("existing_relative_age_id")) {
			return null;
		}
		String abbreviation = "CAL_"
				+ fixtureLoader.integerValue(sourceRow.get("Date"), scenarioName + ".source_rows[0].Date")
				+ "_"
				+ fixtureLoader.stringValue(sourceRow.get("BCADBP"), scenarioName + ".source_rows[0].BCADBP");
		RelativeAgeType type = new RelativeAgeType();
		type.setType("Calendar date");
		return TestRelativeAge.create(
				fixtureLoader.integerValue(state.get("existing_relative_age_id"), scenarioName + ".policy_context.state.existing_relative_age_id"),
				abbreviation,
				abbreviation,
				null,
				null,
				null,
				null,
				null,
				type,
				null
		);
	}

	private DatesCalendar createDatesCalendar(Map<String, Object> sourceRow, String scenarioName) {
		DatesCalendar bugsData = new DatesCalendar();
		bugsData.setSample(fixtureLoader.stringValue(sourceRow.get("SampleCODE"), scenarioName + ".source_rows[0].SampleCODE"));
		bugsData.setUncertainty(fixtureLoader.stringValue(sourceRow.get("Uncertainty"), scenarioName + ".source_rows[0].Uncertainty"));
		bugsData.setCalendarCODE(fixtureLoader.stringValue(sourceRow.get("CalendarCODE"), scenarioName + ".source_rows[0].CalendarCODE"));
		bugsData.setDate(fixtureLoader.integerValue(sourceRow.get("Date"), scenarioName + ".source_rows[0].Date"));
		bugsData.setBcadbp(fixtureLoader.stringValue(sourceRow.get("BCADBP"), scenarioName + ".source_rows[0].BCADBP"));
		bugsData.setDatingMethod(fixtureLoader.stringValue(sourceRow.get("DatingMethod"), scenarioName + ".source_rows[0].DatingMethod"));
		bugsData.setNotes(fixtureLoader.stringValue(sourceRow.get("Notes"), scenarioName + ".source_rows[0].Notes"));
		return bugsData;
	}

	private Map<String, Object> actualGraphResult(RelativeAge relativeAge) {
		Map<String, Object> graphResult = new LinkedHashMap<String, Object>();
		Map<String, Object> relativeAgeResult = new LinkedHashMap<String, Object>();
		relativeAgeResult.put("relative_age_id", relativeAge.getId());
		relativeAgeResult.put("supporting_action", relativeAge.getId() == null ? "create" : "reuse");
		relativeAgeResult.put("abbreviation", relativeAge.getAbbreviation());
		relativeAgeResult.put("name", relativeAge.getName());
		relativeAgeResult.put("type_name", relativeAge.getType() == null ? null : relativeAge.getType().getType());
		graphResult.put("relative_age", relativeAgeResult);
		return graphResult;
	}

	private void setField(Object target, String fieldName, Object value) {
		try {
			Field field = target.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(target, value);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private static class RecordingRelativeAgeRepository implements RelativeAgeRepository {

		private final RelativeAge existingRelativeAge;

		RecordingRelativeAgeRepository(RelativeAge existingRelativeAge) {
			this.existingRelativeAge = existingRelativeAge;
		}

		@Override
		public java.util.List<RelativeAge> findAll() {
			return Collections.emptyList();
		}

		@Override
		public RelativeAge findByAbbreviation(String abbreviation) {
			if (existingRelativeAge != null && abbreviation.equals(existingRelativeAge.getAbbreviation())) {
				return existingRelativeAge;
			}
			return null;
		}

		@Override
		public RelativeAge findOne(Integer integer) {
			throw new UnsupportedOperationException();
		}

		@Override
		public RelativeAge saveOrUpdate(RelativeAge entity) {
			throw new UnsupportedOperationException();
		}
	}

	private static class RecordingRelativeAgeUpdater extends RelativeAgeUpdater {
		RecordingRelativeAgeUpdater() {
			super(null, null, null, null);
		}

		@Override
		public void update(RelativeAge original, se.sead.bugsimport.periods.bugsmodel.Period bugsData) {
			original.setAbbreviation(bugsData.getPeriodCode());
			original.setName(bugsData.getPeriodCode());
			RelativeAgeType type = new RelativeAgeType();
			type.setType(bugsData.getType());
			original.setType(type);
		}
	}
}