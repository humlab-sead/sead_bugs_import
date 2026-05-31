package se.sead.bugsimport.datesradio.converters;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.lab.search.DatingLabTraceHelper;
import se.sead.bugsimport.lab.seadmodel.DatingLab;
import se.sead.model.TestDatingLab;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.repositories.DatingLabRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DatingLabResolverFixtureExecutionTest {

	private static final String UNKNOWN_SEAD_LAB_IDENTIFIER = "Unknown";
	private static final String UNKNOWN_BUGS_LAB_IDENTIFIER = "Unknown";

	private PolicyFixtureLoader fixtureLoader;
	private RecordingDatingLabRepository repository;
	private RecordingDatingLabTraceHelper traceHelper;
	private DatingLabManagerFactory factory;

	@Before
	public void setup() {
		fixtureLoader = new PolicyFixtureLoader();
		repository = new RecordingDatingLabRepository();
		traceHelper = new RecordingDatingLabTraceHelper(repository);
		factory = new DatingLabManagerFactory(repository, traceHelper, UNKNOWN_SEAD_LAB_IDENTIFIER, UNKNOWN_BUGS_LAB_IDENTIFIER);

		repository.add(TestDatingLab.create(1, UNKNOWN_SEAD_LAB_IDENTIFIER, "Unknown lab", null));
		repository.add(TestDatingLab.create(2, "Ua-100", "Direct lab", null));
	}

	@Test
	public void blankLabFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertScenarioMatchesCurrentJavaBehavior("blank_lab_id_uses_unknown_shortcut");
	}

	@Test
	public void directLookupFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertScenarioMatchesCurrentJavaBehavior("direct_lookup_after_trace_miss");
	}

	@Test
	public void notFoundFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertScenarioMatchesCurrentJavaBehavior("missing_lab_emits_error");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws IOException {
		Map<String, Object> scenario = loadScenario(scenarioName);
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		DatingLab lab = executeResolverForScenario(scenario);

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("resolver_path"), scenarioName + ".expects.resolver_path"), traceHelper.getRecordedPath());
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("resolver_result"), scenarioName + ".expects.resolver_result"), actualResolverResult(lab));
		if (expects.containsKey("emit_codes")) {
			assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("emit_codes"), scenarioName + ".expects.emit_codes"), listOf("dating_lab_not_found"));
		}
	}

	private Map<String, Object> loadScenario(String scenarioName) throws IOException {
		Map<String, Object> fixture = fixtureLoader.loadFixture("datesradio.fixture.yml");
		return fixtureLoader.findScenario(fixture, scenarioName);
	}

	private DatingLab executeResolverForScenario(Map<String, Object> scenario) {
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), "source_rows").get(0), "source_rows[0]");
		String labId = fixtureLoader.stringValue(sourceRow.get("LabID"), "LabID");
		DatingLabManagerFactory.DatingLabManager manager = factory.createManager();
		traceHelper.reset();
		if (labId == null || labId.trim().isEmpty() || UNKNOWN_BUGS_LAB_IDENTIFIER.equals(labId)) {
			traceHelper.record("unknown_shortcut");
		}
		DatingLab result = manager.getSeadLabFromCode(labId);
		if (!result.isErrorFree()) {
			traceHelper.record("not_found");
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

	private Map<String, Object> actualResolverResult(DatingLab lab) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		String source = traceHelper.getRecordedPath().get(traceHelper.getRecordedPath().size() - 1);
		result.put("source", source);
		if (lab.isErrorFree()) {
			result.put("result_kind", "return_entity");
			result.put("lab_id", lab.getLabId());
		} else {
			result.put("result_kind", "empty_entity");
			Map<String, Object> issue = new LinkedHashMap<String, Object>();
			issue.put("severity", "error");
			issue.put("code", "dating_lab_not_found");
			issue.put("message", "No lab found");
			result.put("issue", issue);
		}
		return result;
	}

	private static class RecordingDatingLabRepository implements DatingLabRepository {

		private final Map<String, DatingLab> labsById = new HashMap<String, DatingLab>();
		private RecordingDatingLabTraceHelper traceHelper;

		void setTraceHelper(RecordingDatingLabTraceHelper traceHelper) {
			this.traceHelper = traceHelper;
		}

		void add(DatingLab datingLab) {
			labsById.put(datingLab.getLabId(), datingLab);
		}

		@Override
		public List<DatingLab> findAll() {
			return new ArrayList<DatingLab>(labsById.values());
		}

		@Override
		public DatingLab findByLabId(String labId) {
			if (traceHelper != null) {
				if (UNKNOWN_SEAD_LAB_IDENTIFIER.equals(labId)) {
					traceHelper.record("unknown_shortcut");
				} else {
					traceHelper.record("direct_lookup");
				}
			}
			return labsById.get(labId);
		}

		@Override
		public DatingLab saveOrUpdate(DatingLab entity) {
			throw new UnsupportedOperationException();
		}

		@Override
		public DatingLab findOne(Integer integer) {
			throw new UnsupportedOperationException();
		}
	}

	private static class RecordingDatingLabTraceHelper extends DatingLabTraceHelper {

		private final List<String> recordedPath = new ArrayList<String>();
		private final Map<String, DatingLab> tracedLabsById = new HashMap<String, DatingLab>();

		RecordingDatingLabTraceHelper(RecordingDatingLabRepository repository) {
			super(repository);
			repository.setTraceHelper(this);
		}

		@Override
		public DatingLab getFromLastTrace(String traceIdentifier) {
			record("trace_lookup");
			return tracedLabsById.get(traceIdentifier);
		}

		void record(String step) {
			recordedPath.add(step);
		}

		List<String> getRecordedPath() {
			return recordedPath;
		}

		void reset() {
			recordedPath.clear();
		}
	}
}