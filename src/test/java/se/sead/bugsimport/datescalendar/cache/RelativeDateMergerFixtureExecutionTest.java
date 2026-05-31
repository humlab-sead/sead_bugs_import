package se.sead.bugsimport.datescalendar.cache;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.BugsListSeadMapping;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datescalendar.cache.datepairs.UncertaintyDatesCalendarContainerManager;
import se.sead.bugsimport.datescalendar.cache.datepairs.DatesCalendarMappingContainer;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RelativeDateMergerFixtureExecutionTest {

	private RelativeDateMerger relativeDateMerger;
	private MappingCreator mappingCreator;
	private PolicyFixtureLoader fixtureLoader;
	private UncertaintyDatesCalendarContainerManager uncertaintyManager;

	@Before
	public void setup() {
		mappingCreator = new MappingCreator();
		fixtureLoader = new PolicyFixtureLoader();
		uncertaintyManager = new UncertaintyDatesCalendarContainerManager(new DatingUncertaintyManager(
				"From",
				"To",
				"Ca",
				"FromCa",
				"ToCa",
				new MockDatingUncertaintyRepository()
		));
		relativeDateMerger = new RelativeDateMerger(uncertaintyManager, new StubRelativeRangeAgeManager());
	}

	@Test
	public void mergeScenarioFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertMergeScenarioMatchesCurrentJavaBehavior("merge_from_to_range");
	}

	@Test
	public void mergeCaScenarioFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertMergeScenarioMatchesCurrentJavaBehavior("merge_fromca_toca_range");
	}

	@Test
	public void openEndedFromScenarioFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertMergeScenarioMatchesCurrentJavaBehavior("retain_open_ended_from_range");
	}

	@Test
	public void openEndedToScenarioFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertMergeScenarioMatchesCurrentJavaBehavior("retain_open_ended_to_range");
	}

	@Test
	public void openEndedFromCaScenarioFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertMergeScenarioMatchesCurrentJavaBehavior("retain_open_ended_fromca_range");
	}

	@Test
	public void openEndedToCaScenarioFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertMergeScenarioMatchesCurrentJavaBehavior("retain_open_ended_toca_range");
	}

	@Test
	public void caAndStandardPairsScenarioFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertMergeScenarioMatchesCurrentJavaBehavior("retain_ca_and_standard_pairs_separately");
	}

	@Test
	public void openFromCaAndOpenToScenarioFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertMergeScenarioMatchesCurrentJavaBehavior("retain_open_fromca_and_open_to_separately");
	}

	@Test
	public void notePartitionClosedAndOpenScenarioFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertMergeScenarioMatchesCurrentJavaBehavior("partition_by_note_group_for_closed_and_open_rows");
	}

	@Test
	public void notePartitionTwoClosedPairsScenarioFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertMergeScenarioMatchesCurrentJavaBehavior("partition_by_note_group_for_two_closed_pairs");
	}

	@Test
	public void datingMethodPartitionScenarioFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertMergeScenarioMatchesCurrentJavaBehavior("partition_by_dating_method_for_closed_and_open_rows");
	}

	private void assertMergeScenarioMatchesCurrentJavaBehavior(String scenarioName) throws IOException {
		Map<String, Object> fixture = fixtureLoader.loadFixture("datescalendar.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		List<BugsListSeadMapping<DatesCalendar, RelativeDate>> mappings = createMappings(scenario);
		List<DatesCalendarMappingContainer> containers = uncertaintyManager.createRanges(mappings);

		List<BugsListSeadMapping<DatesCalendar, RelativeDate>> result = relativeDateMerger.mergeItems(mappings);

		if (expects.containsKey("postprocess_results")) {
			assertEquals(FixtureExpectationHelper.toIntegerList(fixtureLoader, expects.get("retained_row_indexes"), scenarioName + ".expects.retained_row_indexes"), actualRetainedRowIndexes(scenario, containers, result));
			assertEquals(FixtureExpectationHelper.toNestedMapList(fixtureLoader, expects.get("postprocess_results"), scenarioName + ".expects.postprocess_results"), actualPostprocessResults(scenario, containers, result));
		} else {
			assertEquals(1, result.size());
			assertNotNull(result.get(0).getSeadData().get(0).getRelativeAge());
			assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("postprocess_result"), scenarioName + ".expects.postprocess_result"), actualPostprocessResult(scenario, containers.get(0), result.get(0)));
		}
	}

	@Test
	public void conflictScenarioFixtureMatchesCurrentJavaBehavior() throws IOException {
		Map<String, Object> fixture = fixtureLoader.loadFixture("datescalendar.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, "conflict_too_many_same_kind_uncertainties");
		List<BugsListSeadMapping<DatesCalendar, RelativeDate>> mappings = createMappings(scenario);

		List<BugsListSeadMapping<DatesCalendar, RelativeDate>> result = relativeDateMerger.mergeItems(mappings);

		assertEquals(1, result.size());
		assertFalse(result.get(0).isErrorFree());
		assertTrue(result.get(0).getErrorMessages().contains("Too many uncertainties of same type for a single sample."));
	}

	private List<BugsListSeadMapping<DatesCalendar, RelativeDate>> createMappings(Map<String, Object> scenario) {
		List<Object> sourceRows = fixtureLoader.listValue(scenario.get("source_rows"), "source_rows");
		List<String> repeatedNotes = repeatedNonEmptyNotes(sourceRows);
		List<BugsListSeadMapping<DatesCalendar, RelativeDate>> mappings = new ArrayList<BugsListSeadMapping<DatesCalendar, RelativeDate>>();
		for (int i = 0; i < sourceRows.size(); i++) {
			Map<String, Object> sourceRow = fixtureLoader.mapValue(sourceRows.get(i), "source_rows[" + i + "]");
			String sampleCode = fixtureLoader.stringValue(sourceRow.get("SampleCODE"), "SampleCODE");
			String uncertainty = fixtureLoader.stringValue(sourceRow.get("Uncertainty"), "Uncertainty");
			String notes = fixtureLoader.stringValue(sourceRow.get("Notes"), "Notes");
			BugsListSeadMapping<DatesCalendar, RelativeDate> mapping = mappingCreator.createMapping(sampleCode, uncertainty, notes);
			mapping.getBugsData().setCalendarCODE(fixtureLoader.stringValue(sourceRow.get("CalendarCODE"), "CalendarCODE"));
			mapping.getBugsData().setDate(fixtureLoader.integerValue(sourceRow.get("Date"), "Date"));
			mapping.getBugsData().setBcadbp(fixtureLoader.stringValue(sourceRow.get("BCADBP"), "BCADBP"));
			mapping.getBugsData().setDatingMethod(fixtureLoader.stringValue(sourceRow.get("DatingMethod"), "DatingMethod"));
			if (!notes.trim().isEmpty()) {
				mapping.getSeadData().get(0).setNotes(notes);
				if (!repeatedNotes.contains(notes)) {
					mapping.getBugsData().setNotes("");
				}
			}
			mappings.add(mapping);
		}
		return mappings;
	}

	private List<String> repeatedNonEmptyNotes(List<Object> sourceRows) {
		Map<String, Integer> counts = new LinkedHashMap<String, Integer>();
		for (int i = 0; i < sourceRows.size(); i++) {
			Map<String, Object> sourceRow = fixtureLoader.mapValue(sourceRows.get(i), "source_rows[" + i + "]");
			String notes = fixtureLoader.stringValue(sourceRow.get("Notes"), "Notes");
			if (!notes.trim().isEmpty()) {
				Integer count = counts.get(notes);
				counts.put(notes, Integer.valueOf(count == null ? 1 : count.intValue() + 1));
			}
		}
		List<String> repeated = new ArrayList<String>();
		for (Map.Entry<String, Integer> entry : counts.entrySet()) {
			if (entry.getValue().intValue() > 1) {
				repeated.add(entry.getKey());
			}
		}
		return repeated;
	}

	private Map<String, Object> actualPostprocessResult(Map<String, Object> scenario, DatesCalendarMappingContainer container, BugsListSeadMapping<DatesCalendar, RelativeDate> mergedMapping) {
		Map<String, Object> retainedSourceRow = findSourceRow(scenario, retainedCalendarCode(container));
		Map<String, Object> pairedSourceRow = pairedCalendarCode(container) == null ? null : findSourceRow(scenario, pairedCalendarCode(container));
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("retained_calendar_code", retainedSourceRow.get("CalendarCODE"));
		result.put("paired_calendar_code", pairedSourceRow == null ? null : pairedSourceRow.get("CalendarCODE"));
		result.put("relative_age_range", relativeAgeRange(retainedSourceRow, pairedSourceRow));
		result.put("dating_uncertainty_state", datingUncertaintyState(mergedMapping.getSeadData().get(0)));
		result.put("dating_uncertainty_name", mergedMapping.getSeadData().get(0).getUncertainty() == null ? null : mergedMapping.getSeadData().get(0).getUncertainty().getName());
		result.put("notes", mergedMapping.getSeadData().get(0).getNotes() == null ? "" : mergedMapping.getSeadData().get(0).getNotes());
		return result;
	}

	private List<Map<String, Object>> actualPostprocessResults(Map<String, Object> scenario, List<DatesCalendarMappingContainer> containers, List<BugsListSeadMapping<DatesCalendar, RelativeDate>> mergedMappings) {
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < mergedMappings.size(); i++) {
			assertNotNull(mergedMappings.get(i).getSeadData().get(0).getRelativeAge());
			results.add(actualPostprocessResult(scenario, findContainer(containers, mergedMappings.get(i)), mergedMappings.get(i)));
		}
		return results;
	}

	private List<Integer> actualRetainedRowIndexes(Map<String, Object> scenario, List<DatesCalendarMappingContainer> containers, List<BugsListSeadMapping<DatesCalendar, RelativeDate>> mergedMappings) {
		List<Integer> retainedIndexes = new ArrayList<Integer>();
		for (int i = 0; i < mergedMappings.size(); i++) {
			retainedIndexes.add(Integer.valueOf(sourceRowIndex(scenario, retainedCalendarCode(findContainer(containers, mergedMappings.get(i))))));
		}
		return retainedIndexes;
	}

	private DatesCalendarMappingContainer findContainer(List<DatesCalendarMappingContainer> containers, BugsListSeadMapping<DatesCalendar, RelativeDate> mergedMapping) {
		for (int i = 0; i < containers.size(); i++) {
			DatesCalendarMappingContainer container = containers.get(i);
			if (retainedMapping(container) == mergedMapping) {
				return container;
			}
		}
		throw new AssertionError("No container matched merged mapping " + mergedMapping.getBugsData().getCalendarCODE());
	}

	private BugsListSeadMapping<DatesCalendar, RelativeDate> retainedMapping(DatesCalendarMappingContainer container) {
		return container.fromDateIsSet() ? container.getFromMapping() : container.getToMapping();
	}

	private String retainedCalendarCode(DatesCalendarMappingContainer container) {
		return retainedMapping(container).getBugsData().getCalendarCODE();
	}

	private String pairedCalendarCode(DatesCalendarMappingContainer container) {
		if (container.fromDateIsSet() && container.toDateIsSet()) {
			return container.getToMapping().getBugsData().getCalendarCODE();
		}
		return null;
	}

	private Map<String, Object> findSourceRow(Map<String, Object> scenario, String calendarCode) {
		int rowIndex = sourceRowIndex(scenario, calendarCode);
		List<Object> sourceRows = fixtureLoader.listValue(scenario.get("source_rows"), "source_rows");
		return fixtureLoader.mapValue(sourceRows.get(rowIndex), "source_rows[" + rowIndex + "]");
	}

	private int sourceRowIndex(Map<String, Object> scenario, String calendarCode) {
		List<Object> sourceRows = fixtureLoader.listValue(scenario.get("source_rows"), "source_rows");
		for (int i = 0; i < sourceRows.size(); i++) {
			Map<String, Object> sourceRow = fixtureLoader.mapValue(sourceRows.get(i), "source_rows[" + i + "]");
			if (calendarCode.equals(fixtureLoader.stringValue(sourceRow.get("CalendarCODE"), "CalendarCODE"))) {
				return i;
			}
		}
		throw new AssertionError("No source row found for calendar code " + calendarCode);
	}

	private Map<String, Object> relativeAgeRange(Map<String, Object> retainedSourceRow, Map<String, Object> pairedSourceRow) {
		Map<String, Object> relativeAgeRange = new LinkedHashMap<String, Object>();
		if (pairedSourceRow == null) {
			String retainedUncertainty = fixtureLoader.stringValue(retainedSourceRow.get("Uncertainty"), "retained.Uncertainty");
			if (retainedUncertainty.startsWith("From")) {
				relativeAgeRange.put("older_date", retainedSourceRow.get("Date"));
				relativeAgeRange.put("older_era", retainedSourceRow.get("BCADBP"));
				relativeAgeRange.put("younger_date", null);
				relativeAgeRange.put("younger_era", null);
			} else {
				relativeAgeRange.put("older_date", null);
				relativeAgeRange.put("older_era", null);
				relativeAgeRange.put("younger_date", retainedSourceRow.get("Date"));
				relativeAgeRange.put("younger_era", retainedSourceRow.get("BCADBP"));
			}
			return relativeAgeRange;
		}
		relativeAgeRange.put("older_date", retainedSourceRow.get("Date"));
		relativeAgeRange.put("older_era", retainedSourceRow.get("BCADBP"));
		relativeAgeRange.put("younger_date", pairedSourceRow.get("Date"));
		relativeAgeRange.put("younger_era", pairedSourceRow.get("BCADBP"));
		return relativeAgeRange;
	}

	private String datingUncertaintyState(RelativeDate relativeDate) {
		if (relativeDate.getUncertainty() == null) {
			return "cleared";
		}
		if ("Ca".equals(relativeDate.getUncertainty().getName())) {
			return "normalized_to_standard";
		}
		return "retained";
	}

	private static class StubRelativeRangeAgeManager extends RelativeRangeAgeManager {

		StubRelativeRangeAgeManager() {
			super(null, null, null, null);
		}

		@Override
		public RelativeAge createOrGet(DatesCalendar fromData, DatesCalendar toData) {
			RelativeAge relativeAge = new RelativeAge();
			relativeAge.setAbbreviation(buildAbbreviation(fromData, toData));
			return relativeAge;
		}

		private String buildAbbreviation(DatesCalendar fromData, DatesCalendar toData) {
			return fromData.getSample() + "-" + fromData.getUncertainty() + "-" + toData.getSample() + "-" + toData.getUncertainty();
		}
	}
}