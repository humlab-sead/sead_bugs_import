package se.sead.bugsimport.datesradio.converters;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.datesradio.bugsmodel.DatesRadio;
import se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty;
import se.sead.bugsimport.datesradio.seadmodel.Geochronology;
import se.sead.bugsimport.lab.seadmodel.DatingLab;
import se.sead.model.TestAnalysisEntity;
import se.sead.model.TestDatingLab;
import se.sead.model.TestGeochronology;
import se.sead.model.TestSample;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.repositories.DataTypeRepository;
import se.sead.repositories.DatasetMasterRepository;
import se.sead.repositories.DatingUncertaintyRepository;
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

public class GeochronologyUpdaterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;

	@Before
	public void setup() {
		fixtureLoader = new PolicyFixtureLoader();
	}

	@Test
	public void missingMethodGraphIssueFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("missing_method_emits_geochronology_graph_issue");
	}

	@Test
	public void missingDateGraphIssueFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("missing_date_emits_geochronology_graph_issue");
	}

	@Test
	public void unknownUncertaintyGraphIssueFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("unknown_uncertainty_emits_geochronology_graph_issue");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("datesradio.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		ExecutionResult result = executeScenario(scenario, scenarioName);

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("related_outputs"), scenarioName + ".expects.related_outputs"), result.relatedOutputs);
		assertEquals(fixtureLoader.booleanValue(expects.get("row_changed"), scenarioName + ".expects.row_changed"), result.rowChanged);
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("graph_issue"), scenarioName + ".expects.graph_issue"), result.graphIssue);
	}

	private ExecutionResult executeScenario(Map<String, Object> scenario, String scenarioName) throws Exception {
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> state = fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state");

		DatesRadio bugsData = createDatesRadio(sourceRow, scenarioName);
		Geochronology original = TestGeochronology.create(null, null, null, null, null, null, null, null, null);

		GeochronologyUpdater updater = new GeochronologyUpdater();
		setField(updater, "analysisEntityCreator", createAnalysisEntityCreator(sourceRow, state, scenarioName));
		setField(updater, "datingUncertaintyRepository", new RecordingDatingUncertaintyRepository(state));
		setField(updater, "datingLabManagerFactory", new RecordingDatingLabManagerFactory());
		updater.update(original, bugsData);

		ExecutionResult result = new ExecutionResult();
		result.relatedOutputs.add("dataset");
		result.relatedOutputs.add("analysis_entity");
		result.rowChanged = original.isUpdated();
		result.graphIssue.put("severity", "error");
		result.graphIssue.put("message", original.getErrorMessages().get(0));
		return result;
	}

	private GeochronologyAnalysisEntityCreator createAnalysisEntityCreator(Map<String, Object> sourceRow, Map<String, Object> state, String scenarioName) throws Exception {
		DataType dataType = createDataType(
				fixtureLoader.integerValue(state.get("data_type_id"), scenarioName + ".policy_context.state.data_type_id"),
				"Undefined other"
		);
		DatasetMaster masterSet = createDatasetMaster(
				fixtureLoader.integerValue(state.get("master_set_id"), scenarioName + ".policy_context.state.master_set_id"),
				"Bugs database"
		);
		GeochronologyDatasetCreator datasetCreator = new GeochronologyDatasetCreator(
				new RecordingDatasetMasterRepository(masterSet),
				new RecordingDataTypeRepository(dataType)
		);
		Method method = Boolean.FALSE.equals(state.get("method_found"))
				? null
				: createMethod(
						91,
						fixtureLoader.stringValue(state.get("method_abbreviation"), scenarioName + ".policy_context.state.method_abbreviation")
				);
		setField(datasetCreator, "methodManager", createMethodManager(method));

		GeochronologyAnalysisEntityCreator creator = new GeochronologyAnalysisEntityCreator(
				new RecordingDatasetMasterRepository(masterSet),
				new RecordingDataTypeRepository(dataType)
		);
		setField(creator, "datasetCreator", datasetCreator);
		AnalysisEntity sampleEntity = TestAnalysisEntity.create(null, null,
				TestSample.create(fixtureLoader.integerValue(state.get("physical_sample_id"), scenarioName + ".policy_context.state.physical_sample_id"),
						fixtureLoader.stringValue(sourceRow.get("SampleCODE"), scenarioName + ".source_rows[0].SampleCODE"), null, null, null));
		setField(creator, "sampleTracerHelper", new RecordingSampleTracerHelper(sampleEntity.getSample()));
		return creator;
	}

	private DatesRadio createDatesRadio(Map<String, Object> sourceRow, String scenarioName) {
		DatesRadio bugsData = new DatesRadio();
		bugsData.setDateCode(fixtureLoader.stringValue(sourceRow.get("DateCODE"), scenarioName + ".source_rows[0].DateCODE"));
		bugsData.setSampleCode(fixtureLoader.stringValue(sourceRow.get("SampleCODE"), scenarioName + ".source_rows[0].SampleCODE"));
		bugsData.setLabNr(fixtureLoader.stringValue(sourceRow.get("LabNr"), scenarioName + ".source_rows[0].LabNr"));
		bugsData.setUncertainty(fixtureLoader.stringValue(sourceRow.get("Uncertainty"), scenarioName + ".source_rows[0].Uncertainty"));
		Object date = sourceRow.get("Date");
		bugsData.setDate(date == null ? null : fixtureLoader.integerValue(date, scenarioName + ".source_rows[0].Date"));
		Object older = sourceRow.get("AgeErrorOrPlusError");
		bugsData.setAgeErrorOrPlusError(older == null ? null : Short.valueOf(fixtureLoader.integerValue(older, scenarioName + ".source_rows[0].AgeErrorOrPlusError").shortValue()));
		Object younger = sourceRow.get("AgeErrorMinus");
		bugsData.setAgeErrorMinus(younger == null ? null : fixtureLoader.integerValue(younger, scenarioName + ".source_rows[0].AgeErrorMinus"));
		bugsData.setDatingMethod(fixtureLoader.stringValue(sourceRow.get("DatingMethod"), scenarioName + ".source_rows[0].DatingMethod"));
		bugsData.setMaterialType(fixtureLoader.stringValue(sourceRow.get("MaterialType"), scenarioName + ".source_rows[0].MaterialType"));
		bugsData.setLabId(fixtureLoader.stringValue(sourceRow.get("LabID"), scenarioName + ".source_rows[0].LabID"));
		bugsData.setNotes(fixtureLoader.stringValue(sourceRow.get("Notes"), scenarioName + ".source_rows[0].Notes"));
		return bugsData;
	}

	private void setField(Object target, String fieldName, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
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

	private static class ExecutionResult {
		private final java.util.List<String> relatedOutputs = new java.util.ArrayList<String>();
		private boolean rowChanged;
		private final Map<String, Object> graphIssue = new LinkedHashMap<String, Object>();
	}

	private static class RecordingDatingUncertaintyRepository implements DatingUncertaintyRepository {

		private final boolean uncertaintyFound;

		RecordingDatingUncertaintyRepository(Map<String, Object> state) {
			this.uncertaintyFound = state == null || !state.containsKey("uncertainty_found") || Boolean.TRUE.equals(state.get("uncertainty_found"));
		}

		@Override
		public DatingUncertainty findByName(String name) {
			if (name == null || name.trim().isEmpty()) {
				return null;
			}
			if (!uncertaintyFound) {
				return null;
			}
			DatingUncertainty uncertainty = new DatingUncertainty();
			uncertainty.setName(name);
			return uncertainty;
		}

		@Override
		public DatingUncertainty findOne(Integer id) {
			throw new UnsupportedOperationException();
		}
	}

	private static class RecordingSampleTracerHelper extends se.sead.bugsimport.sample.converters.SampleTracerHelper {

		private final se.sead.bugsimport.sample.seadmodel.Sample sample;

		RecordingSampleTracerHelper(se.sead.bugsimport.sample.seadmodel.Sample sample) {
			super(null);
			this.sample = sample;
		}

		@Override
		public se.sead.bugsimport.sample.seadmodel.Sample getFromLastTrace(String traceIdentifier) {
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

	private static class RecordingDatingLabManagerFactory extends DatingLabManagerFactory {
		RecordingDatingLabManagerFactory() {
			super(null, null, "Unknown", "Unknown");
		}

		@Override
		DatingLabManager createManager() {
			DatingLab lab = TestDatingLab.create(501, "Ua-OK", "Lab ok", null);
			return new DatingLabManager(null, null, lab, lab, "Unknown") {
				@Override
				DatingLab getSeadLabFromCode(String bugsLabIdentifier) {
					return lab;
				}
			};
		}
	}
}