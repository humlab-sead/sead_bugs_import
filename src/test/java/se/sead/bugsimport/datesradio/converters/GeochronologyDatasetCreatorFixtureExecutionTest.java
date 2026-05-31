package se.sead.bugsimport.datesradio.converters;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.datesradio.bugsmodel.DatesRadio;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.repositories.DataTypeRepository;
import se.sead.repositories.DatasetMasterRepository;
import se.sead.repositories.MethodGroupRepository;
import se.sead.repositories.MethodRepository;
import se.sead.sead.data.DataType;
import se.sead.sead.data.Dataset;
import se.sead.sead.data.DatasetMaster;
import se.sead.sead.methods.Method;
import se.sead.sead.methods.MethodGroup;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class GeochronologyDatasetCreatorFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;

	@Before
	public void setup() {
		fixtureLoader = new PolicyFixtureLoader();
	}

	@Test
	public void createDatasetFixtureMatchesCurrentJavaBehavior() throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("datesradio.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, "create_dataset_for_new_geochronology_row");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), "create_dataset_for_new_geochronology_row.expects");
		Dataset dataset = executeScenario(scenario, "create_dataset_for_new_geochronology_row");

		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("graph_result"), "create_dataset_for_new_geochronology_row.expects.graph_result"), actualGraphResult(dataset));
	}

	private Dataset executeScenario(Map<String, Object> scenario, String scenarioName) throws Exception {
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> args = fixtureLoader.mapValue(fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context").get("args"), scenarioName + ".policy_context.args");

		DataType dataType = createDataType(integerValue(args.get("data_type_id")), "Undefined other");
		DatasetMaster masterSet = createDatasetMaster(integerValue(args.get("master_set_id")), "Bugs database");
		Method method = createMethod(91, fixtureLoader.stringValue(args.get("method_abbreviation"), scenarioName + ".policy_context.args.method_abbreviation"));

		GeochronologyDatasetCreator creator = new GeochronologyDatasetCreator(new RecordingDatasetMasterRepository(masterSet), new RecordingDataTypeRepository(dataType));
		setField(creator, "methodManager", createMethodManager(method));
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

	private Map<String, Object> actualGraphResult(Dataset dataset) {
		Map<String, Object> graphResult = new LinkedHashMap<String, Object>();
		Map<String, Object> datasetResult = new LinkedHashMap<String, Object>();
		datasetResult.put("dataset_id", dataset.getId());
		datasetResult.put("dataset_name", dataset.getName());
		datasetResult.put("data_type_id", dataset.getDataType().getId());
		datasetResult.put("method_abbreviation", dataset.getMethod().getAbbreviation());
		datasetResult.put("master_set_id", dataset.getMasterDataset().getId());
		datasetResult.put("updated", dataset.isUpdated());
		graphResult.put("dataset", datasetResult);
		return graphResult;
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