package se.sead.bugsimport.sample.converters;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.sample.bugsmodel.BugsSample;
import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.bugsimport.sample.seadmodel.SampleDimension;
import se.sead.model.TestSample;
import se.sead.model.TestSampleDimension;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.repositories.DimensionRepository;
import se.sead.repositories.MethodRepository;
import se.sead.sead.methods.Method;
import se.sead.sead.model.Dimension;

import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class DimensionUpdaterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;
	private Method depthMethod;
	private Dimension upperDimension;
	private Dimension lowerDimension;

	@Before
	public void setup() {
		fixtureLoader = new PolicyFixtureLoader();
		depthMethod = createMethod(501, "DEPTH_FROM_DATUM");
		upperDimension = createDimension(601, "upper");
		lowerDimension = createDimension(602, "lower");
	}

	@Test
	public void createSampleDimensionsFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_sample_dimensions_when_missing");
	}

	@Test
	public void updateSampleDimensionsFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("update_existing_sample_dimensions_when_values_change");
	}

	@Test
	public void keepSampleDimensionsFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("keep_existing_sample_dimensions_when_values_match");
	}

	@Test
	public void deleteSampleDimensionsFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("delete_lower_sample_dimension_when_value_missing");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("sample.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		Sample sample = createSampleWithDimensions(sourceRow, state, scenarioName);
		DimensionUpdater updater = createUpdater(sample.getDimensions());
		updater.update(sample, createBugsSample(sourceRow, scenarioName));

		assertEquals(fixtureLoader.booleanValue(expects.get("row_changed"), scenarioName + ".expects.row_changed"), rowChanged(sample, state));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("graph_result"), scenarioName + ".expects.graph_result"), actualGraphResult(sample, state));
	}

	private boolean rowChanged(Sample sample, Map<String, Object> state) {
		SampleDimension upper = findDimension(sample.getDimensions(), upperDimension);
		if (upper != null && !"keep".equals(supportingAction(upper, integerOrNull(state, "existing_upper_sample_dimension_id"), doubleOrNull(state == null ? null : state.get("existing_upper_dimension_value"), "existing_upper_dimension_value"), upper.getValue() == null ? null : upper.getValue().doubleValue()))) {
			return true;
		}
		SampleDimension lower = findDimension(sample.getDimensions(), lowerDimension);
		return lower != null && !"keep".equals(supportingAction(lower, integerOrNull(state, "existing_lower_sample_dimension_id"), doubleOrNull(state == null ? null : state.get("existing_lower_dimension_value"), "existing_lower_dimension_value"), lower.getValue() == null ? null : lower.getValue().doubleValue()));
	}

	private DimensionUpdater createUpdater(List<SampleDimension> tracedDimensions) throws Exception {
		DimensionUpdater updater = new DimensionUpdater(createMethodRepository(), createDimensionRepository());
		java.lang.reflect.Field helperField = DimensionUpdater.class.getDeclaredField("sampleDimensionTraceHelper");
		helperField.setAccessible(true);
		helperField.set(updater, new RecordingSampleDimensionTraceHelper(tracedDimensions));
		return updater;
	}

	private Sample createSampleWithDimensions(Map<String, Object> sourceRow, Map<String, Object> state, String scenarioName) {
		List<SampleDimension> dimensions = new ArrayList<SampleDimension>();
		if (state != null && state.containsKey("existing_upper_sample_dimension_id")) {
			dimensions.add(TestSampleDimension.create(
					fixtureLoader.integerValue(state.get("existing_upper_sample_dimension_id"), scenarioName + ".policy_context.state.existing_upper_sample_dimension_id"),
					upperDimension,
					depthMethod,
					bigDecimalValue(state.get("existing_upper_dimension_value"), scenarioName + ".policy_context.state.existing_upper_dimension_value")
			));
		}
		if (state != null && state.containsKey("existing_lower_sample_dimension_id")) {
			dimensions.add(TestSampleDimension.create(
					fixtureLoader.integerValue(state.get("existing_lower_sample_dimension_id"), scenarioName + ".policy_context.state.existing_lower_sample_dimension_id"),
					lowerDimension,
					depthMethod,
					bigDecimalValue(state.get("existing_lower_dimension_value"), scenarioName + ".policy_context.state.existing_lower_dimension_value")
			));
		}
		return TestSample.create(null, fixtureLoader.stringValue(sourceRow.get("SampleCODE"), scenarioName + ".source_rows[0].SampleCODE"), null, null, null, dimensions);
	}

	private BugsSample createBugsSample(Map<String, Object> sourceRow, String scenarioName) {
		BugsSample bugsSample = new BugsSample();
		bugsSample.setSampleCode(fixtureLoader.stringValue(sourceRow.get("SampleCODE"), scenarioName + ".source_rows[0].SampleCODE"));
		bugsSample.setZOrDepthTop(doubleOrNull(sourceRow.get("ZorDepthTop"), scenarioName + ".source_rows[0].ZorDepthTop"));
		bugsSample.setZOrDepthBot(doubleOrNull(sourceRow.get("ZorDepthBot"), scenarioName + ".source_rows[0].ZorDepthBot"));
		return bugsSample;
	}

	private Map<String, Object> actualGraphResult(Sample sample, Map<String, Object> state) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		Map<String, Object> sampleDimensionsResult = new LinkedHashMap<String, Object>();

		SampleDimension upper = findDimension(sample.getDimensions(), upperDimension);
		if (upper != null) {
			sampleDimensionsResult.put("upper", actualDimensionResult(upper, state, "existing_upper_sample_dimension_id", "existing_upper_dimension_value"));
		}
		SampleDimension lower = findDimension(sample.getDimensions(), lowerDimension);
		if (lower != null) {
			sampleDimensionsResult.put("lower", actualDimensionResult(lower, state, "existing_lower_sample_dimension_id", "existing_lower_dimension_value"));
		}

		result.put("sample_dimensions", sampleDimensionsResult);
		return result;
	}

	private Map<String, Object> actualDimensionResult(SampleDimension dimension, Map<String, Object> state, String idKey, String valueKey) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		Integer existingId = integerOrNull(state, idKey);
		Double existingValue = doubleOrNull(state == null ? null : state.get(valueKey), valueKey);
		Double currentValue = dimension.getValue() == null ? null : dimension.getValue().doubleValue();
		result.put("result_kind", resultKind(dimension, existingId, existingValue, currentValue));
		result.put("supporting_action", supportingAction(dimension, existingId, existingValue, currentValue));
		result.put("sample_dimension_id", dimension.getId());
		result.put("dimension_id", dimension.getDimension().getId());
		result.put("method_id", dimension.getMethod().getId());
		result.put("dimension_value", normalizeNumericValue(currentValue));
		result.put("marked_for_deletion", dimension.isMarkedForDeletion());
		return result;
	}

	private String resultKind(SampleDimension dimension, Integer existingId, Double existingValue, Double currentValue) {
		if (dimension.isMarkedForDeletion()) {
			return "delete_existing";
		}
		if (dimension.isNewItem()) {
			return "insert_new";
		}
		if (Objects.equals(existingId, dimension.getId()) && numericEquals(existingValue, currentValue)) {
			return "keep_existing";
		}
		return "update_existing";
	}

	private String supportingAction(SampleDimension dimension, Integer existingId, Double existingValue, Double currentValue) {
		if (dimension.isMarkedForDeletion()) {
			return "delete";
		}
		if (dimension.isNewItem()) {
			return "create";
		}
		if (Objects.equals(existingId, dimension.getId()) && numericEquals(existingValue, currentValue)) {
			return "keep";
		}
		return "update";
	}

	private SampleDimension findDimension(List<SampleDimension> dimensions, Dimension expectedDimension) {
		for (SampleDimension dimension : dimensions) {
			if (Objects.equals(expectedDimension, dimension.getDimension())) {
				return dimension;
			}
		}
		return null;
	}

	private MethodRepository createMethodRepository() {
		return (MethodRepository) Proxy.newProxyInstance(
				MethodRepository.class.getClassLoader(),
				new Class[]{MethodRepository.class},
				(proxy, method, args) -> {
					if ("getBugsSampleDimensionMethod".equals(method.getName())) {
						return depthMethod;
					}
					return null;
				}
		);
	}

	private DimensionRepository createDimensionRepository() {
		return (DimensionRepository) Proxy.newProxyInstance(
				DimensionRepository.class.getClassLoader(),
				new Class[]{DimensionRepository.class},
				(proxy, method, args) -> {
					if ("getUpperDepthFromUnknownReference".equals(method.getName())) {
						return upperDimension;
					}
					if ("getLowerDepthFromUnknownReference".equals(method.getName())) {
						return lowerDimension;
					}
					return null;
				}
		);
	}

	private Integer integerOrNull(Map<String, Object> state, String key) {
		if (state == null || !state.containsKey(key) || state.get(key) == null) {
			return null;
		}
		return fixtureLoader.integerValue(state.get(key), key);
	}

	private BigDecimal bigDecimalValue(Object value, String context) {
		Double numericValue = doubleOrNull(value, context);
		return numericValue == null ? null : BigDecimal.valueOf(numericValue.doubleValue());
	}

	private Double doubleOrNull(Object value, String context) {
		if (value == null) {
			return null;
		}
		if (value instanceof Number) {
			return ((Number) value).doubleValue();
		}
		if (value instanceof String) {
			return Double.valueOf((String) value);
		}
		throw new IllegalArgumentException(context + " must be numeric");
	}

	private boolean numericEquals(Double left, Double right) {
		if (left == null || right == null) {
			return left == null && right == null;
		}
		return Double.compare(left.doubleValue(), right.doubleValue()) == 0;
	}

	private Object normalizeNumericValue(Double value) {
		return value == null ? null : Integer.valueOf(value.intValue());
	}

	private Method createMethod(Integer id, String abbreviation) {
		TestMethod method = new TestMethod(id);
		method.setAbbreviation(abbreviation);
		method.setName(abbreviation);
		method.setDescription(abbreviation + " description");
		return method;
	}

	private Dimension createDimension(Integer id, String name) {
		TestDimension dimension = new TestDimension(id);
		dimension.setName(name);
		dimension.setDescription(name + " description");
		dimension.setAbbrev(name);
		return dimension;
	}

	private static class RecordingSampleDimensionTraceHelper extends SampleDimensionTraceHelper {

		private final List<SampleDimension> tracedDimensions;

		RecordingSampleDimensionTraceHelper(List<SampleDimension> tracedDimensions) {
			this.tracedDimensions = tracedDimensions;
		}

		@Override
		public List<SampleDimension> getFromSampleTrace(String bugsSampleCode) {
			return tracedDimensions;
		}
	}

	private static class TestMethod extends Method {

		private TestMethod(Integer id) {
			super.setId(id);
		}
	}

	private static class TestDimension extends Dimension {

		private TestDimension(Integer id) {
			super.setId(id);
		}
	}
}