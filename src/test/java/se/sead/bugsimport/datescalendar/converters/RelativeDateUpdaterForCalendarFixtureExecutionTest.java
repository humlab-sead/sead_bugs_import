package se.sead.bugsimport.datescalendar.converters;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datesperiod.converters.RelativeDateMethodManager;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.periods.converters.PeriodTraceHelper;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.bugsimport.periods.seadmodel.RelativeAgeType;
import se.sead.bugsimport.sample.converters.SampleTracerHelper;
import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.model.TestAnalysisEntity;
import se.sead.model.TestDataset;
import se.sead.model.TestRelativeDate;
import se.sead.model.TestSample;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.repositories.DataTypeRepository;
import se.sead.repositories.DatasetMasterRepository;
import se.sead.repositories.DatingUncertaintyRepository;
import se.sead.repositories.MethodGroupRepository;
import se.sead.repositories.MethodRepository;
import se.sead.repositories.RelativeAgeRepository;
import se.sead.sead.data.AnalysisEntity;
import se.sead.sead.data.DataType;
import se.sead.sead.data.Dataset;
import se.sead.sead.data.DatasetMaster;
import se.sead.sead.methods.Method;
import se.sead.sead.methods.MethodGroup;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RelativeDateUpdaterForCalendarFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;

	@Before
	public void setup() {
		fixtureLoader = new PolicyFixtureLoader();
	}

	@Test
	public void createCalendarGraphFixtureMatchesCurrentJavaBehavior() throws IOException {
		Map<String, Object> fixture = fixtureLoader.loadFixture("datescalendar.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, "create_related_output_graph_for_new_calendar_row");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), "create_related_output_graph_for_new_calendar_row.expects");
		GraphExecutionResult result = executeUpdaterForScenario(scenario, "create_related_output_graph_for_new_calendar_row");

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("related_outputs"), "create_related_output_graph_for_new_calendar_row.expects.related_outputs"), result.relatedOutputs);
		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("updates_target_fields"), "create_related_output_graph_for_new_calendar_row.expects.updates_target_fields"), result.updatedTargetFields);
		assertEquals(fixtureLoader.booleanValue(expects.get("row_changed"), "create_related_output_graph_for_new_calendar_row.expects.row_changed"), result.rowChanged);
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("graph_result"), "create_related_output_graph_for_new_calendar_row.expects.graph_result"), result.graphResult);
	}

	@Test
	public void createCalendarDatasetFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertSupportingScenarioMatchesCurrentJavaBehavior("create_calendar_dataset_when_missing");
	}

	@Test
	public void updateCalendarDatasetFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertSupportingScenarioMatchesCurrentJavaBehavior("update_existing_calendar_dataset_when_values_change");
	}

	@Test
	public void keepCalendarDatasetFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertSupportingScenarioMatchesCurrentJavaBehavior("keep_existing_calendar_dataset_when_values_match");
	}

	@Test
	public void createCalendarAnalysisEntityFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertSupportingScenarioMatchesCurrentJavaBehavior("create_calendar_analysis_entity_when_missing");
	}

	@Test
	public void updateCalendarAnalysisEntityFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertSupportingScenarioMatchesCurrentJavaBehavior("update_existing_calendar_analysis_entity_when_values_change");
	}

	@Test
	public void keepCalendarAnalysisEntityFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertSupportingScenarioMatchesCurrentJavaBehavior("keep_existing_calendar_analysis_entity_when_values_match");
	}

	private void assertSupportingScenarioMatchesCurrentJavaBehavior(String scenarioName) throws IOException {
		Map<String, Object> fixture = fixtureLoader.loadFixture("datescalendar.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> args = fixtureLoader.mapValue(fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context").get("args"), scenarioName + ".policy_context.args");
		GraphExecutionResult result = executeUpdaterForScenario(scenario, scenarioName);
		String outputName = fixtureLoader.stringValue(args.get("output_name"), scenarioName + ".policy_context.args.output_name");

		Map<String, Object> actualGraphResult = new LinkedHashMap<String, Object>();
		actualGraphResult.put(outputName, result.graphResult.get(outputName));

		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("graph_result"), scenarioName + ".expects.graph_result"), actualGraphResult);
	}

	private GraphExecutionResult executeUpdaterForScenario(Map<String, Object> scenario, String scenarioName) {
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> state = fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state");

		DatesCalendar bugsData = createDatesCalendar(sourceRow, scenarioName);
		RelativeDate original = existingRelativeDate(sourceRow, state, scenarioName);
		DataType dataType = createDataType(integerValue(state.get("data_type_id")), "Calendar dates");
		Method method = createMethod(71, fixtureLoader.stringValue(state.get("method_abbreviation"), scenarioName + ".policy_context.state.method_abbreviation"));
		DatasetMaster masterSet = createDatasetMaster(integerValue(state.get("master_set_id")), "Bugs database");
		Sample sample = createSample(integerValue(state.get("physical_sample_id")), fixtureLoader.stringValue(sourceRow.get("SampleCODE"), scenarioName + ".source_rows[0].SampleCODE"));

		RelativeDateUpdaterForCalendar updater = new RelativeDateUpdaterForCalendar(
				new RecordingRelativeAgeManager(),
				new RecordingDatasetMasterRepository(masterSet),
				new RecordingDataTypeRepository(dataType),
				"Calendar dates"
		);
		setBaseField(updater, "sampleTracerHelper", new RecordingSampleTracerHelper(sample));
		setBaseField(updater, "uncertaintyRepository", new RecordingDatingUncertaintyRepository());
		setBaseField(updater, "datingMethodManager", createMethodManager(method));
		updater.update(original, bugsData);

		GraphExecutionResult result = new GraphExecutionResult();
		result.relatedOutputs.add("relative_age");
		result.relatedOutputs.add("dataset");
		result.relatedOutputs.add("analysis_entity");
		result.updatedTargetFields.add("analysis_entity_id");
		result.rowChanged = original.isUpdated();
		result.graphResult.put("relative_age", relativeAgeResult(original.getRelativeAge(), state));
		result.graphResult.put("dataset", datasetResult(original.getAnalysisEntity().getDataset()));
		result.graphResult.put("analysis_entity", analysisEntityResult(original.getAnalysisEntity(), state));
		return result;
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

	private RelativeDate existingRelativeDate(Map<String, Object> sourceRow, Map<String, Object> state, String scenarioName) {
		AnalysisEntity analysisEntity = existingAnalysisEntity(state);
		return TestRelativeDate.create(301, null, null, fixtureLoader.stringValue(sourceRow.get("Notes"), scenarioName + ".source_rows[0].Notes"), analysisEntity);
	}

	private AnalysisEntity existingAnalysisEntity(Map<String, Object> state) {
		if (state == null || !state.containsKey("existing_analysis_entity_id")) {
			return null;
		}
		Sample sample = createSample(integerValue(state.get("existing_physical_sample_id")), "existing-sample");
		Dataset dataset = existingDataset(state);
		return TestAnalysisEntity.create(integerValue(state.get("existing_analysis_entity_id")), dataset, sample);
	}

	private Dataset existingDataset(Map<String, Object> state) {
		if (state == null || !state.containsKey("existing_dataset_id")) {
			return null;
		}
		return TestDataset.create(
				integerValue(state.get("existing_dataset_id")),
				fixtureLoader.stringValue(state.get("existing_dataset_name"), "existing_dataset_name"),
				createMethod(integerValue(state.get("existing_method_id")), fixtureLoader.stringValue(state.get("existing_method_abbreviation"), "existing_method_abbreviation")),
				createDatasetMaster(integerValue(state.get("existing_master_set_id")), "Bugs database"),
				createDataType(integerValue(state.get("existing_data_type_id")), "Calendar dates")
		);
	}

	private Map<String, Object> relativeAgeResult(RelativeAge relativeAge, Map<String, Object> state) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("relative_age_id", relativeAge.getId());
		Integer existingRelativeAgeId = state == null || !state.containsKey("existing_relative_age_id") ? null : integerValue(state.get("existing_relative_age_id"));
		result.put("supporting_action", existingRelativeAgeId == null ? "create" : "reuse");
		result.put("abbreviation", relativeAge.getAbbreviation());
		result.put("name", relativeAge.getName());
		result.put("type_name", relativeAge.getType() == null ? null : relativeAge.getType().getType());
		return result;
	}

	private Map<String, Object> datasetResult(Dataset dataset) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("dataset_id", dataset.getId());
		result.put("supporting_action", dataset.getId() == null ? "create" : dataset.isUpdated() ? "update" : "keep");
		result.put("dataset_name", dataset.getName());
		result.put("data_type_id", dataset.getDataType().getId());
		result.put("method_abbreviation", dataset.getMethod().getAbbreviation());
		result.put("master_set_id", dataset.getMasterDataset().getId());
		result.put("updated", dataset.isUpdated());
		return result;
	}

	private Map<String, Object> analysisEntityResult(AnalysisEntity analysisEntity, Map<String, Object> state) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("analysis_entity_id", analysisEntity.getId());
		result.put("supporting_action", analysisEntity.getId() == null ? "create" : analysisEntityAction(analysisEntity, state));
		result.put("physical_sample_id", analysisEntity.getSample() == null ? null : analysisEntity.getSample().getId());
		result.put("dataset_id", analysisEntity.getDataset() == null ? null : analysisEntity.getDataset().getId());
		return result;
	}

	private String analysisEntityAction(AnalysisEntity analysisEntity, Map<String, Object> state) {
		Integer existingPhysicalSampleId = state == null || !state.containsKey("existing_physical_sample_id") ? null : integerValue(state.get("existing_physical_sample_id"));
		Integer existingDatasetId = state == null || !state.containsKey("existing_dataset_id") ? null : integerValue(state.get("existing_dataset_id"));
		Integer currentPhysicalSampleId = analysisEntity.getSample() == null ? null : analysisEntity.getSample().getId();
		Integer currentDatasetId = analysisEntity.getDataset() == null ? null : analysisEntity.getDataset().getId();
		if (!sameInteger(existingPhysicalSampleId, currentPhysicalSampleId) || !sameInteger(existingDatasetId, currentDatasetId)) {
			return "update";
		}
		return "keep";
	}

	private boolean sameInteger(Integer left, Integer right) {
		if (left == null) {
			return right == null;
		}
		return left.equals(right);
	}

	private void setBaseField(Object target, String fieldName, Object value) {
		try {
			Field field = target.getClass().getSuperclass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(target, value);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private Integer integerValue(Object value) {
		if (value instanceof Number) {
			return ((Number) value).intValue();
		}
		if (value instanceof String) {
			return Integer.valueOf((String) value);
		}
		throw new IllegalArgumentException("Expected numeric value");
	}

	private DataType createDataType(Integer id, String name) {
		TestDataType dataType = new TestDataType(id);
		dataType.setName(name);
		dataType.setDescription(name + " description");
		return dataType;
	}

	private DatasetMaster createDatasetMaster(Integer id, String name) {
		TestDatasetMaster datasetMaster = new TestDatasetMaster(id);
		datasetMaster.setName(name);
		return datasetMaster;
	}

	private Method createMethod(Integer id, String abbreviation) {
		TestMethod method = new TestMethod(id);
		method.setAbbreviation(abbreviation);
		method.setName(abbreviation + " name");
		method.setDescription(abbreviation + " description");
		method.setGroup(createMethodGroup(1, "Dating to period"));
		return method;
	}

	private MethodGroup createMethodGroup(Integer id, String name) {
		TestMethodGroup group = new TestMethodGroup(id);
		group.setName(name);
		group.setDescription(name + " description");
		return group;
	}

	private Sample createSample(Integer id, String sampleCode) {
		return TestSample.create(id, sampleCode, null, null, null);
	}

	private RelativeDateMethodManager createMethodManager(Method method) {
		RecordingMethodRepository methodRepository = new RecordingMethodRepository(method);
		return new RelativeDateMethodManager(
				"Dating to period",
				"UnknownCal",
				new RecordingMethodGroupRepository(),
				methodRepository,
				new RecordingPeriodTraceHelper()
		);
	}

	private static class GraphExecutionResult {
		private final List<String> relatedOutputs = new ArrayList<String>();
		private final List<String> updatedTargetFields = new ArrayList<String>();
		private boolean rowChanged;
		private final Map<String, Object> graphResult = new LinkedHashMap<String, Object>();
	}

	private static class RecordingRelativeAgeManager extends RelativeAgeManager {
		@Override
		public RelativeAge getOrCreateRelativeAge(DatesCalendar bugsData) {
			RelativeAge relativeAge = new RelativeAge();
			String abbreviation = "CAL_" + bugsData.getDate() + "_" + bugsData.getBcadbp();
			relativeAge.setAbbreviation(abbreviation);
			relativeAge.setName(abbreviation);
			RelativeAgeType type = new RelativeAgeType();
			type.setType("Calendar date");
			relativeAge.setType(type);
			return relativeAge;
		}
	}

	private static class RecordingSampleTracerHelper extends SampleTracerHelper {
		private final Sample sample;

		RecordingSampleTracerHelper(Sample sample) {
			super(null);
			this.sample = sample;
		}

		@Override
		public Sample getFromLastTrace(String traceIdentifier) {
			return sample;
		}
	}

	private static class RecordingDatingUncertaintyRepository implements DatingUncertaintyRepository {
		@Override
		public se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty findByName(String name) {
			return null;
		}

		@Override
		public se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty findOne(Integer integer) {
			throw new UnsupportedOperationException();
		}
	}

	private static class RecordingDatasetMasterRepository implements DatasetMasterRepository {
		private final DatasetMaster datasetMaster;

		RecordingDatasetMasterRepository(DatasetMaster datasetMaster) {
			this.datasetMaster = datasetMaster;
		}

		@Override
		public DatasetMaster findOne(Integer id) {
			return datasetMaster;
		}

		@Override
		public DatasetMaster findBugsMasterSet() {
			return datasetMaster;
		}
	}

	private static class RecordingDataTypeRepository implements DataTypeRepository {
		private final DataType dataType;

		RecordingDataTypeRepository(DataType dataType) {
			this.dataType = dataType;
		}

		@Override
		public DataType findOne(Integer id) {
			return dataType;
		}

		@Override
		public DataType findBugsGeochronologyDataType() {
			return dataType;
		}

		@Override
		public DataType findByName(String name) {
			return dataType;
		}
	}

	private static class RecordingMethodRepository implements MethodRepository {
		private final Method method;

		RecordingMethodRepository(Method method) {
			this.method = method;
		}

		@Override
		public Method findOne(Integer id) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Method getByNameAndGroup(String name, MethodGroup group) {
			return method;
		}

		@Override
		public Method getByAbbreviationAndGroup(String abbreviation, MethodGroup group) {
			return method;
		}

		@Override
		public Method getByAbbreviation(String abbreviation) {
			return method;
		}

		@Override
		public Method getBugsSampleDimensionMethod() {
			return method;
		}
	}

	private static class RecordingMethodGroupRepository implements MethodGroupRepository {
		@Override
		public MethodGroup findByName(String name) {
			TestMethodGroup group = new TestMethodGroup(1);
			group.setName(name);
			group.setDescription(name + " description");
			return group;
		}

		@Override
		public MethodGroup findOne(Integer id) {
			throw new UnsupportedOperationException();
		}
	}

	private static class RecordingPeriodTraceHelper extends PeriodTraceHelper {
		RecordingPeriodTraceHelper() {
			super(new RecordingRelativeAgeRepository());
		}
	}

	private static class RecordingRelativeAgeRepository implements RelativeAgeRepository {
		@Override
		public List<RelativeAge> findAll() {
			return new ArrayList<RelativeAge>();
		}

		@Override
		public RelativeAge findByAbbreviation(String abbreviation) {
			throw new UnsupportedOperationException();
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

	private static class TestDataType extends DataType {
		private TestDataType(Integer id) {
			setId(id);
		}
	}

	private static class TestDatasetMaster extends DatasetMaster {
		private TestDatasetMaster(Integer id) {
			setId(id);
		}
	}

	private static class TestMethod extends Method {
		private TestMethod(Integer id) {
			setId(id);
		}
	}

	private static class TestMethodGroup extends MethodGroup {
		private TestMethodGroup(Integer id) {
			setId(id);
		}
	}
}