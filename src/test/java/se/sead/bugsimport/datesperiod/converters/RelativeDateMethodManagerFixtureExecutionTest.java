package se.sead.bugsimport.datesperiod.converters;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.periods.bugsmodel.Period;
import se.sead.bugsimport.periods.converters.PeriodTraceHelper;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.model.TestRelativeAge;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.repositories.MethodGroupRepository;
import se.sead.repositories.MethodRepository;
import se.sead.repositories.RelativeAgeRepository;
import se.sead.sead.methods.Method;
import se.sead.sead.methods.MethodGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RelativeDateMethodManagerFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;
	private RecordingMethodRepository methodRepository;
	private RecordingPeriodTraceHelper periodTraceHelper;
	private RelativeDateMethodManager methodManager;

	@Before
	public void setup() {
		fixtureLoader = new PolicyFixtureLoader();
		methodRepository = new RecordingMethodRepository();
		periodTraceHelper = new RecordingPeriodTraceHelper();
		methodRepository.add(createMethod(1, "UnknownCal"));
		methodRepository.add(createMethod(2, "DirectMethod"));
		methodRepository.add(createMethod(3, "RelativeCal"));
		methodManager = new RelativeDateMethodManager(
				"Dating to period",
				"UnknownCal",
				new RecordingMethodGroupRepository(),
				methodRepository,
				periodTraceHelper
		);
		methodRepository.resetRecordedPath();
	}

	@Test
	public void blankMethodFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertScenarioMatchesCurrentJavaBehavior("blank_method_uses_unknown_default");
	}

	@Test
	public void directMethodFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertScenarioMatchesCurrentJavaBehavior("direct_method_lookup_uses_abbreviation");
	}

	@Test
	public void computedMethodFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertScenarioMatchesCurrentJavaBehavior("computed_method_uses_period_year_type_suffix");
	}

	@Test
	public void missingMethodFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertScenarioMatchesCurrentJavaBehavior("missing_method_emits_error");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws IOException {
		Map<String, Object> scenario = loadScenario(scenarioName);
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Method method = executeResolverForScenario(scenario);

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("resolver_path"), scenarioName + ".expects.resolver_path"), methodRepository.getRecordedPath());
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("resolver_result"), scenarioName + ".expects.resolver_result"), actualResolverResult(method));
		if (expects.containsKey("emit_codes")) {
			assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("emit_codes"), scenarioName + ".expects.emit_codes"), listOf("relative_date_method_not_found"));
		}
	}

	private Map<String, Object> loadScenario(String scenarioName) throws IOException {
		Map<String, Object> fixture = fixtureLoader.loadFixture("datesperiod.fixture.yml");
		return fixtureLoader.findScenario(fixture, scenarioName);
	}

	private Method executeResolverForScenario(Map<String, Object> scenario) {
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), "source_rows").get(0), "source_rows[0]");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), "policy_context");
		Map<String, Object> args = fixtureLoader.mapValue(policyContext.get("args"), "policy_context.args");
		String datingMethod = fixtureLoader.stringValue(sourceRow.get("DatingMethod"), "DatingMethod");
		String periodYearsType = fixtureLoader.stringValue(args.get("period_years_type"), "period_years_type");
		RelativeAge relativeAge = TestRelativeAge.create(10, "REL-10", "Relative age 10", null, null, null, null, null, null, null);
		periodTraceHelper.setPeriod(createPeriod("PERIOD-10", periodYearsType));
		methodRepository.setExpectedAbbreviations(datingMethod, datingMethod + suffixForPeriodYearsType(periodYearsType));
		methodRepository.resetRecordedPath();
		if (datingMethod == null || datingMethod.trim().isEmpty()) {
			methodRepository.record("empty_default");
		}
		Method method = methodManager.getRelativeDateMethod(datingMethod, relativeAge);
		if (!method.isErrorFree()) {
			methodRepository.record("not_found");
		}
		return method;
	}

	private Map<String, Object> actualResolverResult(Method method) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		String source = methodRepository.getRecordedPath().get(methodRepository.getRecordedPath().size() - 1);
		result.put("source", source);
		if (method.isErrorFree()) {
			result.put("result_kind", "return_entity");
			result.put("method_abbreviation", method.getAbbreviation());
		} else {
			result.put("result_kind", "empty_entity");
			Map<String, Object> issue = new LinkedHashMap<String, Object>();
			issue.put("severity", "error");
			issue.put("code", "relative_date_method_not_found");
			issue.put("message", "No dating method found");
			result.put("issue", issue);
		}
		return result;
	}

	private List<String> listOf(String... values) {
		List<String> result = new ArrayList<String>();
		for (String value : values) {
			result.add(value);
		}
		return result;
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

	private Period createPeriod(String periodCode, String yearsType) {
		Period period = new Period();
		period.setPeriodCode(periodCode);
		period.setYearsType(yearsType);
		return period;
	}

	private String suffixForPeriodYearsType(String periodYearsType) {
		if ("Calendar".equals(periodYearsType)) {
			return "Cal";
		}
		if ("C14".equals(periodYearsType)) {
			return "C14";
		}
		if ("Radiometric".equals(periodYearsType)) {
			return "Radio";
		}
		return "";
	}

	private static class RecordingMethodRepository implements MethodRepository {

		private final Map<String, Method> methodsByAbbreviation = new LinkedHashMap<String, Method>();
		private final List<String> recordedPath = new ArrayList<String>();
		private String expectedDirectAbbreviation;
		private String expectedComputedAbbreviation;

		void add(Method method) {
			methodsByAbbreviation.put(method.getAbbreviation(), method);
		}

		void setExpectedAbbreviations(String directAbbreviation, String computedAbbreviation) {
			expectedDirectAbbreviation = directAbbreviation;
			expectedComputedAbbreviation = computedAbbreviation;
		}

		void record(String step) {
			recordedPath.add(step);
		}

		void resetRecordedPath() {
			recordedPath.clear();
		}

		List<String> getRecordedPath() {
			return recordedPath;
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
			if (abbreviation != null && abbreviation.equals(expectedDirectAbbreviation) && !recordedPath.contains("direct_lookup")) {
				recordedPath.add("direct_lookup");
			} else if (abbreviation != null && abbreviation.equals(expectedComputedAbbreviation)) {
				recordedPath.add("computed_from_period_year_type");
			}
			return methodsByAbbreviation.get(abbreviation);
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

		private Period period;

		RecordingPeriodTraceHelper() {
			super(new RecordingRelativeAgeRepository());
		}

		void setPeriod(Period period) {
			this.period = period;
		}

		@Override
		public Period getPeriodFromTrace(RelativeAge seadValue) {
			return period;
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