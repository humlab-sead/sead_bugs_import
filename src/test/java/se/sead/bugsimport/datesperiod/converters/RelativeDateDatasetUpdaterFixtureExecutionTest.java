package se.sead.bugsimport.datesperiod.converters;

import org.junit.Before;
import org.junit.Test;
import se.sead.model.TestDataset;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.sead.data.DataType;
import se.sead.sead.data.Dataset;
import se.sead.sead.data.DatasetMaster;
import se.sead.sead.methods.Method;
import se.sead.sead.methods.MethodGroup;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RelativeDateDatasetUpdaterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;

	@Before
	public void setup() {
		fixtureLoader = new PolicyFixtureLoader();
	}

	@Test
	public void createDatasetFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertScenarioMatchesCurrentJavaBehavior("create_dataset_when_missing");
	}

	@Test
	public void updateDatasetFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertScenarioMatchesCurrentJavaBehavior("update_existing_dataset_when_values_change");
	}

	@Test
	public void keepDatasetFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertScenarioMatchesCurrentJavaBehavior("keep_existing_dataset_when_values_match");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws IOException {
		Map<String, Object> fixture = fixtureLoader.loadFixture("datesperiod.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;
		Dataset dataset = executeUpdaterForScenario(scenario, scenarioName);

		assertEquals(fixtureLoader.booleanValue(expects.get("row_changed"), scenarioName + ".expects.row_changed"), rowChanged(dataset));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("graph_result"), scenarioName + ".expects.graph_result"), actualGraphResult(dataset, state));
	}

	private boolean rowChanged(Dataset dataset) {
		return dataset.getId() == null || dataset.isUpdated();
	}

	private Dataset executeUpdaterForScenario(Map<String, Object> scenario, String scenarioName) {
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> args = fixtureLoader.mapValue(policyContext.get("args"), scenarioName + ".policy_context.args");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		DataType dataType = createDataType(integerValue(args.get("data_type_id")), "Uncalibrated dates");
		Method method = createMethod(71, fixtureLoader.stringValue(args.get("method_abbreviation"), scenarioName + ".policy_context.args.method_abbreviation"));
		DatasetMaster masterSet = createDatasetMaster(integerValue(args.get("master_set_id")), "Bugs master set");
		RelativeDateDatasetUpdater updater = new RelativeDateDatasetUpdater(
				dataType,
				method,
				fixtureLoader.stringValue(sourceRow.get("PeriodDateCODE"), scenarioName + ".source_rows[0].PeriodDateCODE"),
				masterSet
		);
		return updater.update(existingDataset(state));
	}

	private Dataset existingDataset(Map<String, Object> state) {
		if (state == null || !state.containsKey("existing_dataset_id")) {
			return null;
		}
		return TestDataset.create(
				integerValue(state.get("existing_dataset_id")),
				fixtureLoader.stringValue(state.get("existing_dataset_name"), "existing_dataset_name"),
				createMethod(integerValue(state.get("existing_method_id")), fixtureLoader.stringValue(state.get("existing_method_abbreviation"), "existing_method_abbreviation")),
				createDatasetMaster(integerValue(state.get("existing_master_set_id")), "Bugs master set"),
				createDataType(integerValue(state.get("existing_data_type_id")), "Uncalibrated dates")
		);
	}

	private Map<String, Object> actualGraphResult(Dataset dataset, Map<String, Object> state) {
		Map<String, Object> graphResult = new LinkedHashMap<String, Object>();
		Map<String, Object> datasetResult = new LinkedHashMap<String, Object>();
		datasetResult.put("dataset_id", dataset.getId());
		datasetResult.put("supporting_action", dataset.getId() == null ? "create" : dataset.isUpdated() ? "update" : "keep");
		datasetResult.put("dataset_name", dataset.getName());
		datasetResult.put("data_type_id", dataset.getDataType().getId());
		datasetResult.put("method_abbreviation", dataset.getMethod().getAbbreviation());
		datasetResult.put("master_set_id", dataset.getMasterDataset().getId());
		datasetResult.put("updated", dataset.isUpdated());
		graphResult.put("dataset", datasetResult);
		return graphResult;
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