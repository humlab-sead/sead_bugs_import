package se.sead.bugsimport.datesperiod.converters;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.periods.converters.PeriodTraceHelper;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.bugsimport.sample.converters.SampleTracerHelper;
import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.model.TestAnalysisEntity;
import se.sead.model.TestDataset;
import se.sead.model.TestRelativeAge;
import se.sead.model.TestRelativeDate;
import se.sead.model.TestSample;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.repositories.DatasetMasterRepository;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RelativeDateAnalysisEntityFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;

	@Before
	public void setup() {
		fixtureLoader = new PolicyFixtureLoader();
	}

	@Test
	public void createAnalysisEntityFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertScenarioMatchesCurrentJavaBehavior("create_analysis_entity_when_missing");
	}

	@Test
	public void updateAnalysisEntityFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertScenarioMatchesCurrentJavaBehavior("update_existing_analysis_entity_when_values_change");
	}

	@Test
	public void keepAnalysisEntityFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertScenarioMatchesCurrentJavaBehavior("keep_existing_analysis_entity_when_values_match");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws IOException {
		Map<String, Object> fixture = fixtureLoader.loadFixture("datesperiod.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;
		AnalysisEntity analysisEntity = executeUpdaterForScenario(scenario, scenarioName);

		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("graph_result"), scenarioName + ".expects.graph_result"), actualGraphResult(analysisEntity, state));
	}

	private AnalysisEntity executeUpdaterForScenario(Map<String, Object> scenario, String scenarioName) {
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> args = fixtureLoader.mapValue(policyContext.get("args"), scenarioName + ".policy_context.args");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		RelativeDate original = existingRelativeDate(sourceRow, args, state, scenarioName);
		DataType dataType = createDataType(integerValue(args.get("data_type_id")), "Uncalibrated dates");
		Method method = createMethod(71, fixtureLoader.stringValue(args.get("method_abbreviation"), scenarioName + ".policy_context.args.method_abbreviation"));
		DatasetMaster masterSet = createDatasetMaster(integerValue(args.get("master_set_id")), "Bugs database");
		Sample sample = createSample(integerValue(args.get("physical_sample_id")), fixtureLoader.stringValue(sourceRow.get("SampleCODE"), scenarioName + ".source_rows[0].SampleCODE"));

		BaseRelativeDateUpdater updater = new AnalysisEntityFixtureUpdater(
				new RecordingSampleTracerHelper(sample),
				createMethodManager(method),
				new RecordingDatasetMasterRepository(masterSet),
				original,
				sourceRow,
				dataType
		);
		updater.update();
		return original.getAnalysisEntity();
	}

	private RelativeDate existingRelativeDate(Map<String, Object> sourceRow, Map<String, Object> args, Map<String, Object> state, String scenarioName) {
		AnalysisEntity analysisEntity = existingAnalysisEntity(state, scenarioName);
		RelativeAge relativeAge = TestRelativeAge.create(401, "PERIOD-401", "Period 401", null, null, null, null, "desc", null, null);
		return TestRelativeDate.create(301, null, relativeAge, fixtureLoader.stringValue(sourceRow.get("Notes"), scenarioName + ".source_rows[0].Notes"), analysisEntity);
	}

	private AnalysisEntity existingAnalysisEntity(Map<String, Object> state, String scenarioName) {
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
				createDataType(integerValue(state.get("existing_data_type_id")), "Uncalibrated dates")
		);
	}

	private Map<String, Object> actualGraphResult(AnalysisEntity analysisEntity, Map<String, Object> state) {
		Map<String, Object> graphResult = new LinkedHashMap<String, Object>();
		Map<String, Object> analysisEntityResult = new LinkedHashMap<String, Object>();
		analysisEntityResult.put("analysis_entity_id", analysisEntity.getId());
		analysisEntityResult.put("supporting_action", analysisEntityAction(analysisEntity, state));
		analysisEntityResult.put("physical_sample_id", analysisEntity.getSample() == null ? null : analysisEntity.getSample().getId());
		analysisEntityResult.put("dataset_id", analysisEntity.getDataset() == null ? null : analysisEntity.getDataset().getId());
		graphResult.put("analysis_entity", analysisEntityResult);
		return graphResult;
	}

	private String analysisEntityAction(AnalysisEntity analysisEntity, Map<String, Object> state) {
		if (analysisEntity.getId() == null) {
			return "create";
		}
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

	private static class AnalysisEntityFixtureUpdater extends BaseRelativeDateUpdater {

		private final Map<String, Object> sourceRow;
		private final DataType dataType;

		AnalysisEntityFixtureUpdater(
				SampleTracerHelper sampleTracerHelper,
				RelativeDateMethodManager datingMethodManager,
				DatasetMasterRepository datasetMasterRepository,
				RelativeDate original,
				Map<String, Object> sourceRow,
				DataType dataType) {
			super(null, datasetMasterRepository, sampleTracerHelper, datingMethodManager, original);
			this.sourceRow = sourceRow;
			this.dataType = dataType;
		}

		@Override
		protected String getBugsSampleCode() {
			return (String) sourceRow.get("SampleCODE");
		}

		@Override
		protected String getBugsUncertainty() {
			return null;
		}

		@Override
		protected boolean setRelativeAge() {
			return false;
		}

		@Override
		protected DataType getDataType() {
			return dataType;
		}

		@Override
		protected String getBugsDatesId() {
			return (String) sourceRow.get("PeriodDateCODE");
		}

		@Override
		protected String getBugsDatingMethod() {
			return (String) sourceRow.get("DatingMethod");
		}

		@Override
		protected String getBugsNotes() {
			return (String) sourceRow.get("Notes");
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

	private static class RecordingMethodRepository implements MethodRepository {

		private final Method method;

		RecordingMethodRepository(Method method) {
			this.method = method;
		}

		@Override
		public Method getByNameAndGroup(String name, MethodGroup group) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Method getByAbbreviationAndGroup(String abbreviation, MethodGroup group) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Method getByAbbreviation(String abbreviation) {
			return method;
		}

		@Override
		public Method findOne(Integer id) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Method getBugsSampleDimensionMethod() {
			throw new UnsupportedOperationException();
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