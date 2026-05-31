package se.sead.bugsimport.fossil.converters;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.countsheets.seadmodel.SampleGroup;
import se.sead.bugsimport.fossil.bugsmodel.Fossil;
import se.sead.bugsimport.sample.converters.SampleTracerHelper;
import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.model.TestAnalysisEntity;
import se.sead.model.TestDataset;
import se.sead.model.TestSample;
import se.sead.model.TestSampleGroup;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.repositories.AnalysisEntityRepository;
import se.sead.repositories.SampleRepository;
import se.sead.sead.data.Abundance;
import se.sead.sead.data.AnalysisEntity;
import se.sead.sead.data.Dataset;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AnalysisEntityManagerFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;
	private Sample sample;

	@Before
	public void setup() {
		fixtureLoader = new PolicyFixtureLoader();
		sample = TestSample.create(10, "sample-10", createSampleGroup(), null, null);
	}

	@Test
	public void fossilFixtureMatchesCloneUpdatedDatasetBehavior() throws IOException {
		assertScenarioMatchesDatasetMode("clone_dataset_when_updates_disabled", false, "clone_updated_dataset");
	}

	@Test
	public void fossilFixtureMatchesReuseExistingDatasetBehavior() throws IOException {
		assertScenarioMatchesDatasetMode("reuse_dataset_when_updates_enabled", true, "reuse_existing_dataset");
	}

	@Test
	public void fossilFixtureMatchesExistingAnalysisEntityReuseBehavior() throws IOException {
		assertScenarioMatchesDatasetMode("reuse_existing_analysis_entity_when_present", true, "reuse_existing_dataset");
	}

	@Test
	public void fossilFixtureMatchesMissingSampleTraceGraphIssue() throws IOException {
		assertScenarioMatchesDatasetMode("missing_sample_trace_emits_graph_issue", true, null);
	}

	@Test
	public void fossilFixtureMatchesDuplicateAnalysisEntitiesGraphIssue() throws IOException {
		assertScenarioMatchesDatasetMode("duplicate_analysis_entities_emit_graph_issue", true, "reuse_existing_dataset");
	}

	@Test
	public void fossilFixtureMatchesMissingSampleCodeGraphIssue() throws IOException {
		assertScenarioMatchesDatasetMode("missing_sample_code_emits_graph_issue", true, null);
	}

	@Test
	public void cloneFossilDatasetSupportingOutputFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertSupportingScenarioMatchesCurrentJavaBehavior("clone_fossil_dataset_when_updates_disabled");
	}

	@Test
	public void reuseFossilDatasetSupportingOutputFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertSupportingScenarioMatchesCurrentJavaBehavior("reuse_existing_fossil_dataset_when_updates_enabled");
	}

	@Test
	public void createFossilAnalysisEntitySupportingOutputFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertSupportingScenarioMatchesCurrentJavaBehavior("create_fossil_analysis_entity_when_missing");
	}

	@Test
	public void reuseFossilAnalysisEntitySupportingOutputFixtureMatchesCurrentJavaBehavior() throws IOException {
		assertSupportingScenarioMatchesCurrentJavaBehavior("reuse_existing_fossil_analysis_entity_when_present");
	}

	private void assertSupportingScenarioMatchesCurrentJavaBehavior(String scenarioName) throws IOException {
		Map<String, Object> fixture = fixtureLoader.loadFixture("fossil.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), "source_rows").get(0), "source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> args = fixtureLoader.mapValue(policyContext.get("args"), scenarioName + ".policy_context.args");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		RecordingDatasetManager datasetManager = new RecordingDatasetManager();
		RecordingAnalysisEntityRepository analysisEntityRepository = new RecordingAnalysisEntityRepository();
		if (state != null && state.containsKey("existing_analysis_entity_id")) {
			analysisEntityRepository.add(TestAnalysisEntity.create(
					fixtureLoader.integerValue(state.get("existing_analysis_entity_id"), scenarioName + ".policy_context.state.existing_analysis_entity_id"),
					datasetManager.getExistingDataset(),
					sample
			));
		}
		boolean allowDatasetUpdates = state == null || !state.containsKey("allow_dataset_updates") || Boolean.TRUE.equals(state.get("allow_dataset_updates"));
		AnalysisEntityManager analysisEntityManager = new AnalysisEntityManager(
				new RecordingSampleTracerHelper(sample),
				analysisEntityRepository,
				datasetManager,
				allowDatasetUpdates
		);
		analysisEntityManager.initCache();

		Fossil fossil = new Fossil();
		fossil.setFossilBugsCODE(fixtureLoader.stringValue(sourceRow.get("FossilBugsCODE"), "FossilBugsCODE"));
		fossil.setSampleCODE(fixtureLoader.stringValue(sourceRow.get("SampleCODE"), "SampleCODE"));
		fossil.setAbundance(fixtureLoader.integerValue(sourceRow.get("Abundance"), "Abundance"));
		Abundance abundance = new Abundance();
		analysisEntityManager.setAnalysisEntity(abundance, fossil, true);

		String outputName = fixtureLoader.stringValue(args.get("output_name"), scenarioName + ".policy_context.args.output_name");
		Map<String, Object> actualGraphResult = new LinkedHashMap<String, Object>();
		if ("dataset".equals(outputName)) {
			actualGraphResult.put("dataset", actualGraphResult(abundance).get("dataset"));
		} else {
			actualGraphResult.put("analysis_entity", actualGraphResult(abundance).get("analysis_entity"));
		}

		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("graph_result"), scenarioName + ".expects.graph_result"), actualGraphResult);
	}

	private void assertScenarioMatchesDatasetMode(String scenarioName, boolean expectedAllowDatasetUpdates, String expectedDatasetLinkMode) throws IOException {
		Map<String, Object> fixture = fixtureLoader.loadFixture("fossil.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), "source_rows").get(0), "source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> config = fixtureLoader.mapValue(policyContext.get("config"), scenarioName + ".policy_context.config");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;
		boolean allowDatasetUpdates = (Boolean) config.get("allow_dataset_updates");

		assertEquals(expectedAllowDatasetUpdates, allowDatasetUpdates);
		if (expectedDatasetLinkMode != null) {
			assertEquals(expectedDatasetLinkMode, fixtureLoader.stringValue(expects.get("dataset_link_mode"), scenarioName + ".expects.dataset_link_mode"));
		}

		RecordingDatasetManager datasetManager = new RecordingDatasetManager();
		RecordingAnalysisEntityRepository analysisEntityRepository = new RecordingAnalysisEntityRepository();
		if (state != null && state.containsKey("existing_analysis_entity_id")) {
			analysisEntityRepository.add(TestAnalysisEntity.create(
					fixtureLoader.integerValue(state.get("existing_analysis_entity_id"), scenarioName + ".policy_context.state.existing_analysis_entity_id"),
					datasetManager.getExistingDataset(),
					sample
			));
		}
		if (state != null && state.containsKey("matching_analysis_entity_count")) {
			int matchingCount = fixtureLoader.integerValue(state.get("matching_analysis_entity_count"), scenarioName + ".policy_context.state.matching_analysis_entity_count");
			for (int index = 0; index < matchingCount; index++) {
				analysisEntityRepository.add(TestAnalysisEntity.create(30 + index, datasetManager.getExistingDataset(), sample));
			}
		}
		AnalysisEntityManager analysisEntityManager = new AnalysisEntityManager(
				new RecordingSampleTracerHelper(state != null && Boolean.FALSE.equals(state.get("sample_found")) ? null : sample),
				analysisEntityRepository,
				datasetManager,
				allowDatasetUpdates
		);
		analysisEntityManager.initCache();

		Fossil fossil = new Fossil();
		fossil.setFossilBugsCODE(fixtureLoader.stringValue(sourceRow.get("FossilBugsCODE"), "FossilBugsCODE"));
		fossil.setSampleCODE(fixtureLoader.stringValue(sourceRow.get("SampleCODE"), "SampleCODE"));
		fossil.setAbundance(fixtureLoader.integerValue(sourceRow.get("Abundance"), "Abundance"));
		Abundance abundance = new Abundance();

		boolean changed = analysisEntityManager.setAnalysisEntity(abundance, fossil, true);

		if (expects.containsKey("graph_issue")) {
			boolean expectedChanged = expects.containsKey("row_changed")
					? fixtureLoader.booleanValue(expects.get("row_changed"), scenarioName + ".expects.row_changed")
					: false;
			assertEquals(expectedChanged, changed);
			assertNull(abundance.getAnalysisEntity());
			assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("graph_issue"), scenarioName + ".expects.graph_issue"), actualGraphIssue(abundance));
			assertEquals(0, datasetManager.getUpdateDatasetCalls());
			return;
		}

		assertTrue(changed);
		assertNotNull(abundance.getAnalysisEntity());
		assertNotNull(abundance.getAnalysisEntity().getDataset());
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("graph_result"), scenarioName + ".expects.graph_result"), actualGraphResult(abundance));
		if ("clone_updated_dataset".equals(expectedDatasetLinkMode)) {
			assertNull(abundance.getAnalysisEntity().getDataset().getId());
			assertNotNull(abundance.getAnalysisEntity().getDataset().getUpdatedDataset());
			assertEquals(Integer.valueOf(20), abundance.getAnalysisEntity().getDataset().getUpdatedDataset().getId());
			assertEquals(1, datasetManager.getUpdateDatasetCalls());
		} else {
			assertEquals(Integer.valueOf(20), abundance.getAnalysisEntity().getDataset().getId());
			assertNull(abundance.getAnalysisEntity().getDataset().getUpdatedDataset());
			assertEquals(0, datasetManager.getUpdateDatasetCalls());
		}
		assertEquals(sample, abundance.getAnalysisEntity().getSample());
		assertEquals(abundance.getAnalysisEntity(), abundance.getAnalysisEntity().getAbundances().get(0).getAnalysisEntity());
	}

	private Map<String, Object> actualGraphResult(Abundance abundance) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		Map<String, Object> dataset = new LinkedHashMap<String, Object>();
		dataset.put("dataset_id", abundance.getAnalysisEntity().getDataset().getId());
		dataset.put("updated_dataset_id", abundance.getAnalysisEntity().getDataset().getUpdatedDataset() == null ? null : abundance.getAnalysisEntity().getDataset().getUpdatedDataset().getId());
		Map<String, Object> analysisEntity = new LinkedHashMap<String, Object>();
		if (abundance.getAnalysisEntity().getId() != null) {
			analysisEntity.put("analysis_entity_id", abundance.getAnalysisEntity().getId());
		}
		analysisEntity.put("physical_sample_id", abundance.getAnalysisEntity().getSample().getId());
		analysisEntity.put("dataset_id", abundance.getAnalysisEntity().getDataset().getId());
		result.put("dataset", dataset);
		result.put("analysis_entity", analysisEntity);
		return result;
	}

	private Map<String, Object> actualGraphIssue(Abundance abundance) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("severity", "error");
		result.put("message", abundance.getErrorMessages().get(0));
		return result;
	}

	private SampleGroup createSampleGroup() {
		return TestSampleGroup.create(5, "group-5", "desc", null, null, null);
	}

	private static class RecordingDatasetManager extends DatasetManagerWithCreation {

		private final Dataset existingDataset;
		private int updateDatasetCalls;

		RecordingDatasetManager() {
			super(null, null, null, null, null, new DatasetCache());
			existingDataset = TestDataset.create(20, "dataset-20", null, null, null);
		}

		@Override
		public Dataset getOrCreateFor(SampleGroup sampleGroup) {
			return existingDataset;
		}

		@Override
		public Dataset updateDataset(Dataset originalDataset) {
			updateDatasetCalls++;
			return TestDataset.create(null, originalDataset.getName(), originalDataset.getMethod(), originalDataset.getMasterDataset(), originalDataset.getDataType(), originalDataset);
		}

		int getUpdateDatasetCalls() {
			return updateDatasetCalls;
		}

		Dataset getExistingDataset() {
			return existingDataset;
		}
	}

	private static class RecordingSampleTracerHelper extends SampleTracerHelper {

		private final Sample sample;

		RecordingSampleTracerHelper(Sample sample) {
			super(new RecordingSampleRepository());
			this.sample = sample;
		}

		@Override
		public Sample getFromLastTrace(String traceIdentifier) {
			return sample;
		}
	}

	private static class RecordingSampleRepository implements SampleRepository {
		@Override
		public List<Sample> findAll() {
			return Collections.emptyList();
		}

		@Override
		public Sample findOne(Integer integer) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Sample saveOrUpdate(Sample entity) {
			throw new UnsupportedOperationException();
		}
	}

	private static class RecordingAnalysisEntityRepository implements AnalysisEntityRepository {

		private final List<AnalysisEntity> existingEntities = new ArrayList<AnalysisEntity>();

		void add(AnalysisEntity analysisEntity) {
			existingEntities.add(analysisEntity);
		}

		@Override
		public List<AnalysisEntity> findBySampleAndDataset(Sample sample, Dataset dataset) {
			List<AnalysisEntity> matches = new ArrayList<AnalysisEntity>();
			for (AnalysisEntity existingEntity : existingEntities) {
				if (sample.equals(existingEntity.getSample()) && dataset.equals(existingEntity.getDataset())) {
					matches.add(existingEntity);
				}
			}
			return matches;
		}

		@Override
		public List<AnalysisEntity> findAll() {
			return new ArrayList<AnalysisEntity>();
		}

		@Override
		public AnalysisEntity findOne(Integer integer) {
			throw new UnsupportedOperationException();
		}

		@Override
		public AnalysisEntity saveOrUpdate(AnalysisEntity entity) {
			throw new UnsupportedOperationException();
		}
	}
}