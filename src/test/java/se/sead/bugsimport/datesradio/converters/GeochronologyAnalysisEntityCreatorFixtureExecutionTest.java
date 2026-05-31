package se.sead.bugsimport.datesradio.converters;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.datesradio.bugsmodel.DatesRadio;
import se.sead.bugsimport.sample.converters.SampleTracerHelper;
import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.model.TestSample;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.repositories.DataTypeRepository;
import se.sead.repositories.DatasetMasterRepository;
import se.sead.repositories.MethodGroupRepository;
import se.sead.repositories.MethodRepository;
import se.sead.sead.data.AnalysisEntity;
import se.sead.sead.data.DataType;
import se.sead.sead.data.DatasetMaster;
import se.sead.sead.methods.Method;
import se.sead.sead.methods.MethodGroup;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class GeochronologyAnalysisEntityCreatorFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;

	@Before
	public void setup() {
		fixtureLoader = new PolicyFixtureLoader();
	}

	@Test
	public void createGraphFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_related_output_graph_for_new_geochronology_row");
	}

	@Test
	public void missingSampleFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("missing_sample_emits_geochronology_graph_issue");
	}

	@Test
	public void createAnalysisEntitySupportingOutputFixtureMatchesCurrentJavaBehavior() throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("datesradio.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, "create_analysis_entity_for_new_geochronology_row");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), "create_analysis_entity_for_new_geochronology_row.expects");
		AnalysisEntity analysisEntity = executeScenario(scenario, "create_analysis_entity_for_new_geochronology_row");

		Map<String, Object> actualGraphResult = new LinkedHashMap<String, Object>();
		actualGraphResult.put("analysis_entity", actualGraphResult(analysisEntity).get("analysis_entity"));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("graph_result"), "create_analysis_entity_for_new_geochronology_row.expects.graph_result"), actualGraphResult);
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("datesradio.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		AnalysisEntity analysisEntity = executeScenario(scenario, scenarioName);

		if (expects.containsKey("graph_result")) {
			assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("graph_result"), scenarioName + ".expects.graph_result"), actualGraphResult(analysisEntity));
		} else {
			assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("graph_issue"), scenarioName + ".expects.graph_issue"), actualGraphIssue(analysisEntity));
		}
	}

	private AnalysisEntity executeScenario(Map<String, Object> scenario, String scenarioName) throws Exception {
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> state = fixtureLoader.mapValue(fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context").get("state"), scenarioName + ".policy_context.state");

		DataType dataType = createDataType(integerValue(state.get("data_type_id")), "Undefined other");
		DatasetMaster masterSet = createDatasetMaster(integerValue(state.get("master_set_id")), "Bugs database");
		Method method = createMethod(91, fixtureLoader.stringValue(state.get("method_abbreviation"), scenarioName + ".policy_context.state.method_abbreviation"));
		Sample sample = Boolean.FALSE.equals(state.get("sample_found")) ? null : TestSample.create(integerValue(state.get("physical_sample_id")), fixtureLoader.stringValue(sourceRow.get("SampleCODE"), scenarioName + ".source_rows[0].SampleCODE"), null, null, null);

		GeochronologyDatasetCreator datasetCreator = new GeochronologyDatasetCreator(new RecordingDatasetMasterRepository(masterSet), new RecordingDataTypeRepository(dataType));
		setField(datasetCreator, "methodManager", createMethodManager(method));
		GeochronologyAnalysisEntityCreator creator = new GeochronologyAnalysisEntityCreator(new RecordingDatasetMasterRepository(masterSet), new RecordingDataTypeRepository(dataType));
		setField(creator, "sampleTracerHelper", new RecordingSampleTracerHelper(sample));
		setField(creator, "datasetCreator", datasetCreator);

		return creator.create(createDatesRadio(sourceRow, scenarioName));
	}

	private DatesRadio createDatesRadio(Map<String, Object> sourceRow, String scenarioName) {
		DatesRadio bugsData = new DatesRadio();
		bugsData.setDateCode(fixtureLoader.stringValue(sourceRow.get("DateCODE"), scenarioName + ".source_rows[0].DateCODE"));
		bugsData.setSampleCode(fixtureLoader.stringValue(sourceRow.get("SampleCODE"), scenarioName + ".source_rows[0].SampleCODE"));
		bugsData.setLabNr(fixtureLoader.stringValue(sourceRow.get("LabNr"), scenarioName + ".source_rows[0].LabNr"));
		bugsData.setUncertainty(fixtureLoader.stringValue(sourceRow.get("Uncertainty"), scenarioName + ".source_rows[0].Uncertainty"));
		bugsData.setDate(integerValue(sourceRow.get("Date")));
		bugsData.setAgeErrorOrPlusError(shortValue(sourceRow.get("AgeErrorOrPlusError")));
		bugsData.setAgeErrorMinus(integerValue(sourceRow.get("AgeErrorMinus")));
		bugsData.setDatingMethod(fixtureLoader.stringValue(sourceRow.get("DatingMethod"), scenarioName + ".source_rows[0].DatingMethod"));
		bugsData.setMaterialType(fixtureLoader.stringValue(sourceRow.get("MaterialType"), scenarioName + ".source_rows[0].MaterialType"));
		bugsData.setLabId(fixtureLoader.stringValue(sourceRow.get("LabID"), scenarioName + ".source_rows[0].LabID"));
		bugsData.setNotes(fixtureLoader.stringValue(sourceRow.get("Notes"), scenarioName + ".source_rows[0].Notes"));
		return bugsData;
	}

	private Map<String, Object> actualGraphResult(AnalysisEntity analysisEntity) {
		Map<String, Object> graphResult = new LinkedHashMap<String, Object>();
		Map<String, Object> datasetResult = new LinkedHashMap<String, Object>();
		datasetResult.put("dataset_id", analysisEntity.getDataset().getId());
		datasetResult.put("supporting_action", "create");
		datasetResult.put("dataset_name", analysisEntity.getDataset().getName());
		datasetResult.put("data_type_id", analysisEntity.getDataset().getDataType().getId());
		datasetResult.put("method_abbreviation", analysisEntity.getDataset().getMethod().getAbbreviation());
		datasetResult.put("master_set_id", analysisEntity.getDataset().getMasterDataset().getId());
		datasetResult.put("updated", analysisEntity.getDataset().isUpdated());
		Map<String, Object> analysisEntityResult = new LinkedHashMap<String, Object>();
		analysisEntityResult.put("analysis_entity_id", analysisEntity.getId());
		analysisEntityResult.put("supporting_action", "create");
		analysisEntityResult.put("physical_sample_id", analysisEntity.getSample() == null ? null : analysisEntity.getSample().getId());
		analysisEntityResult.put("dataset_id", analysisEntity.getDataset() == null ? null : analysisEntity.getDataset().getId());
		graphResult.put("dataset", datasetResult);
		graphResult.put("analysis_entity", analysisEntityResult);
		return graphResult;
	}

	private Map<String, Object> actualGraphIssue(AnalysisEntity analysisEntity) {
		Map<String, Object> graphIssue = new LinkedHashMap<String, Object>();
		graphIssue.put("severity", "error");
		graphIssue.put("message", analysisEntity.getErrorMessages().get(0));
		return graphIssue;
	}

	private void setField(Object target, String fieldName, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

	private Integer integerValue(Object value) {
		if (value instanceof Number) {
			return ((Number) value).intValue();
		}
		return Integer.valueOf((String) value);
	}

	private Short shortValue(Object value) {
		if (value instanceof Number) {
			return ((Number) value).shortValue();
		}
		return Short.valueOf((String) value);
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
		method.setGroup(createMethodGroup(1, "Dating by radiometric methods"));
		return method;
	}

	private MethodGroup createMethodGroup(Integer id, String name) {
		TestMethodGroup group = new TestMethodGroup(id);
		group.setName(name);
		group.setDescription(name + " description");
		return group;
	}

	private GeochronologyMethodManager createMethodManager(Method method) {
		return new GeochronologyMethodManager(
				"Dating by radiometric methods",
				"",
				new RecordingMethodGroupRepository(),
				new RecordingMethodRepository(method)
		);
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