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

public class RelativeDateOutputGraphFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;

	@Before
	public void setup() {
		fixtureLoader = new PolicyFixtureLoader();
	}

	@Test
	public void createGraphFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertScenarioMatchesCurrentJavaBehavior("create_related_output_graph_when_support_rows_missing");
	}

	@Test
	public void updateGraphFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertScenarioMatchesCurrentJavaBehavior("update_related_output_graph_when_values_change");
	}

	@Test
	public void keepGraphFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertScenarioMatchesCurrentJavaBehavior("keep_related_output_graph_when_values_match");
	}

	@Test
	public void blankSampleGraphIssueFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertScenarioMatchesCurrentJavaBehavior("blank_sample_code_emits_datesperiod_graph_issue");
	}

	@Test
	public void missingSampleTraceGraphIssueFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertScenarioMatchesCurrentJavaBehavior("missing_sample_trace_emits_datesperiod_graph_issue");
	}

	@Test
	public void unknownUncertaintyGraphIssueFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertScenarioMatchesCurrentJavaBehavior("unknown_uncertainty_emits_datesperiod_graph_issue");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws IOException {
		Map<String, Object> fixture = fixtureLoader.loadFixture("datesperiod.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		GraphExecutionResult result = executeUpdaterForScenario(scenario, scenarioName);

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("related_outputs"), scenarioName + ".expects.related_outputs"), result.relatedOutputs);
		if (expects.containsKey("updates_target_fields")) {
			assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("updates_target_fields"), scenarioName + ".expects.updates_target_fields"), result.updatedTargetFields);
		}
		if (expects.containsKey("row_changed")) {
			assertEquals(fixtureLoader.booleanValue(expects.get("row_changed"), scenarioName + ".expects.row_changed"), result.rowChanged);
		}
		if (expects.containsKey("graph_result")) {
			assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("graph_result"), scenarioName + ".expects.graph_result"), result.graphResult);
		}
		if (expects.containsKey("graph_issue")) {
			assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("graph_issue"), scenarioName + ".expects.graph_issue"), result.graphIssue);
		}
	}

	private GraphExecutionResult executeUpdaterForScenario(Map<String, Object> scenario, String scenarioName) {
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		RelativeDate original = existingRelativeDate(sourceRow, state, scenarioName);
		DataType dataType = createDataType(integerValue(state.get("data_type_id")), "Uncalibrated dates");
		Method method = createMethod(71, fixtureLoader.stringValue(state.get("method_abbreviation"), scenarioName + ".policy_context.state.method_abbreviation"));
		DatasetMaster masterSet = createDatasetMaster(integerValue(state.get("master_set_id")), "Bugs database");
		Sample sample = Boolean.FALSE.equals(state.get("sample_found"))
				? null
				: createSample(integerValue(state.get("physical_sample_id")), fixtureLoader.stringValue(sourceRow.get("SampleCODE"), scenarioName + ".source_rows[0].SampleCODE"));

		BaseRelativeDateUpdater updater = new GraphFixtureUpdater(
				new RecordingSampleTracerHelper(sample),
				new RecordingDatingUncertaintyRepository(state),
				state != null && state.containsKey("uncertainty_found"),
				createMethodManager(method),
				new RecordingDatasetMasterRepository(masterSet),
				original,
				sourceRow,
				dataType
		);
		updater.update();

		GraphExecutionResult result = new GraphExecutionResult();
		result.relatedOutputs.add("dataset");
		result.relatedOutputs.add("analysis_entity");
		result.rowChanged = original.isUpdated();
		if (original.isErrorFree()) {
			result.updatedTargetFields.add("analysis_entity_id");
			result.graphResult.put("dataset", datasetResult(original.getAnalysisEntity().getDataset()));
			result.graphResult.put("analysis_entity", analysisEntityResult(original.getAnalysisEntity()));
		} else {
			result.graphIssue.put("severity", "error");
			result.graphIssue.put("message", original.getErrorMessages().get(0));
		}
		return result;
	}

	private RelativeDate existingRelativeDate(Map<String, Object> sourceRow, Map<String, Object> state, String scenarioName) {
		AnalysisEntity analysisEntity = existingAnalysisEntity(state);
		RelativeAge relativeAge = TestRelativeAge.create(401, "PERIOD-401", "Period 401", null, null, null, null, "desc", null, null);
		return TestRelativeDate.create(301, null, relativeAge, fixtureLoader.stringValue(sourceRow.get("Notes"), scenarioName + ".source_rows[0].Notes"), analysisEntity);
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
				createDataType(integerValue(state.get("existing_data_type_id")), "Uncalibrated dates")
		);
	}

	private Map<String, Object> datasetResult(Dataset dataset) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("dataset_id", dataset.getId());
		result.put("dataset_name", dataset.getName());
		result.put("data_type_id", dataset.getDataType().getId());
		result.put("method_abbreviation", dataset.getMethod().getAbbreviation());
		result.put("master_set_id", dataset.getMasterDataset().getId());
		result.put("updated", dataset.isUpdated());
		return result;
	}

	private Map<String, Object> analysisEntityResult(AnalysisEntity analysisEntity) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("analysis_entity_id", analysisEntity.getId());
		result.put("physical_sample_id", analysisEntity.getSample() == null ? null : analysisEntity.getSample().getId());
		result.put("dataset_id", analysisEntity.getDataset() == null ? null : analysisEntity.getDataset().getId());
		return result;
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
		private final Map<String, Object> graphIssue = new LinkedHashMap<String, Object>();
	}

	private static class GraphFixtureUpdater extends BaseRelativeDateUpdater {

		private final Map<String, Object> sourceRow;
		private final DataType dataType;
		private final boolean useUncertainty;

		GraphFixtureUpdater(
				SampleTracerHelper sampleTracerHelper,
				RecordingDatingUncertaintyRepository datingUncertaintyRepository,
				boolean useUncertainty,
				RelativeDateMethodManager datingMethodManager,
				DatasetMasterRepository datasetMasterRepository,
				RelativeDate original,
				Map<String, Object> sourceRow,
				DataType dataType) {
			super(datingUncertaintyRepository, datasetMasterRepository, sampleTracerHelper, datingMethodManager, original);
			this.sourceRow = sourceRow;
			this.dataType = dataType;
				this.useUncertainty = useUncertainty;
		}

		@Override
		protected String getBugsSampleCode() {
			return (String) sourceRow.get("SampleCODE");
		}

		@Override
		protected String getBugsUncertainty() {
			return useUncertainty ? (String) sourceRow.get("Uncertainty") : null;
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

	private static class RecordingDatingUncertaintyRepository implements se.sead.repositories.DatingUncertaintyRepository {

		private final boolean uncertaintyFound;

		RecordingDatingUncertaintyRepository(Map<String, Object> state) {
			this.uncertaintyFound = state == null || !state.containsKey("uncertainty_found") || Boolean.TRUE.equals(state.get("uncertainty_found"));
		}

		@Override
		public se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty findByName(String name) {
			if (name == null || name.trim().isEmpty()) {
				return null;
			}
			if (!uncertaintyFound) {
				return null;
			}
			se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty uncertainty = new se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty();
			uncertainty.setName(name);
			return uncertainty;
		}

		@Override
		public se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty findOne(Integer id) {
			throw new UnsupportedOperationException();
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