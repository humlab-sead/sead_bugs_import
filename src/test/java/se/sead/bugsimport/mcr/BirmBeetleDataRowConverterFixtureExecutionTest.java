package se.sead.bugsimport.mcr;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.mcr.bugsmodel.BirmBeetleDat;
import se.sead.bugsimport.mcr.seadmodel.BirmBeetleData;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.model.TestBirmBeetleData;
import se.sead.model.TestTaxaSpecies;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.repositories.BirmBeetleDataRepository;
import se.sead.repositories.TaxonomicOrderRepository;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BirmBeetleDataRowConverterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;
	private RecordingTaxonomicOrderRepository taxonomicOrderRepository;
	private RecordingBirmRepository birmRepository;
	private BirmBeetleDatToBirmBeetleRowConverter rowConverter;

	@Before
	public void setup() throws Exception {
		fixtureLoader = new PolicyFixtureLoader();
		taxonomicOrderRepository = new RecordingTaxonomicOrderRepository();
		birmRepository = new RecordingBirmRepository();
		rowConverter = new BirmBeetleDatToBirmBeetleRowConverter();
		injectField(rowConverter, "taxonomicOrderRepository", taxonomicOrderRepository.createProxy());
		injectField(rowConverter, "mcrDataRepository", birmRepository.createProxy());
	}

	@Test
	public void compositeLookupFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("composite_lookup_returns_existing_birm_data");
	}

	@Test
	public void createNewFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_new_birm_data_when_composite_lookup_misses");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("birmbeetledata.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		configureScenario(sourceRow, policyContext, state, scenarioName);
		BirmBeetleData result = rowConverter.convertForDataRow(createBugsRow(sourceRow, scenarioName));

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("reconciliation_path"), scenarioName + ".expects.reconciliation_path"), actualPath(result));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("reconciliation_result"), scenarioName + ".expects.reconciliation_result"), actualReconciliationResult(result, sourceRow));
	}

	private void configureScenario(Map<String, Object> sourceRow, Map<String, Object> policyContext, Map<String, Object> state, String scenarioName) {
		Map<String, Object> stepHits = fixtureLoader.mapValue(policyContext.get("step_hits"), scenarioName + ".policy_context.step_hits");
		TaxaSpecies species = TestTaxaSpecies.create(401, "species-" + integerCode(sourceRow), null, null);
		taxonomicOrderRepository.setSpecies(species);
		birmRepository.reset();
		if (Boolean.TRUE.equals(stepHits.get("composite_lookup"))) {
			birmRepository.setMatch(TestBirmBeetleData.create(
					fixtureLoader.integerValue(state.get("existing_row_id"), scenarioName + ".policy_context.state.existing_row_id"),
					buildMcrData(sourceRow),
					fixtureLoader.integerValue(sourceRow.get("MCRRow"), scenarioName + ".source_rows[0].MCRRow"),
					species));
		}
	}

	private BirmBeetleDat createBugsRow(Map<String, Object> sourceRow, String scenarioName) {
		BirmBeetleDat bugs = new BirmBeetleDat();
		bugs.setBugsCode(fixtureLoader.integerValue(sourceRow.get("CODE"), scenarioName + ".source_rows[0].CODE").doubleValue());
		bugs.setRow(fixtureLoader.integerValue(sourceRow.get("MCRRow"), scenarioName + ".source_rows[0].MCRRow").shortValue());
		bugs.setFieldData(1, String.valueOf(fixtureLoader.integerValue(sourceRow.get("Field1"), scenarioName + ".source_rows[0].Field1")));
		bugs.setFieldData(2, String.valueOf(fixtureLoader.integerValue(sourceRow.get("Field2"), scenarioName + ".source_rows[0].Field2")));
		return bugs;
	}

	private String buildMcrData(Map<String, Object> sourceRow) {
		return String.valueOf(fixtureLoader.integerValue(sourceRow.get("Field1"), "sourceRow.Field1"))
				+ fixtureLoader.integerValue(sourceRow.get("Field2"), "sourceRow.Field2");
	}

	private List<String> actualPath(BirmBeetleData result) {
		List<String> path = new ArrayList<String>();
		path.addAll(birmRepository.getRecordedPath());
		if (result.getId() == null && result.isErrorFree()) {
			path.add("create_new");
		}
		return path;
	}

	private Map<String, Object> actualReconciliationResult(BirmBeetleData result, Map<String, Object> sourceRow) {
		Map<String, Object> reconciliationResult = new LinkedHashMap<String, Object>();
		if (!result.isErrorFree()) {
			reconciliationResult.put("result_kind", "return_existing_error");
			reconciliationResult.put("persisted_action", "keep_existing_error");
			reconciliationResult.put("source", "composite_lookup");
			Map<String, Object> issue = new LinkedHashMap<String, Object>();
			issue.put("severity", "error");
			issue.put("message", result.getErrorMessages().get(0));
			reconciliationResult.put("issue", issue);
		} else if (result.getId() == null) {
			reconciliationResult.put("result_kind", "insert_new");
			reconciliationResult.put("persisted_action", "create");
			reconciliationResult.put("source", "create_new");
		} else {
			reconciliationResult.put("result_kind", "return_as_is");
			reconciliationResult.put("persisted_action", "keep_existing");
			reconciliationResult.put("source", "composite_lookup");
		}
		reconciliationResult.put("row_id", result.getId());
		reconciliationResult.put("species_code", integerCode(sourceRow));
		return reconciliationResult;
	}

	private Integer integerCode(Map<String, Object> sourceRow) {
		return fixtureLoader.integerValue(sourceRow.get("CODE"), "sourceRow.CODE");
	}

	private void injectField(Object target, String fieldName, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

	private static class RecordingTaxonomicOrderRepository {
		private TaxaSpecies species;

		void setSpecies(TaxaSpecies species) {
			this.species = species;
		}

		TaxonomicOrderRepository createProxy() {
			return (TaxonomicOrderRepository) Proxy.newProxyInstance(
					TaxonomicOrderRepository.class.getClassLoader(),
					new Class[]{TaxonomicOrderRepository.class},
					(proxy, method, args) -> {
						if (method.getDeclaringClass() == Object.class) {
							if ("toString".equals(method.getName())) {
								return "RecordingTaxonomicOrderRepository";
							}
							if ("hashCode".equals(method.getName())) {
								return System.identityHashCode(proxy);
							}
							if ("equals".equals(method.getName())) {
								return proxy == args[0];
							}
						}
						if ("findBugsSpeciesByCode".equals(method.getName())) {
							return species;
						}
						throw new UnsupportedOperationException(method.getName());
					});
		}
	}

	private static class RecordingBirmRepository {
		private BirmBeetleData match;
		private final List<String> recordedPath = new ArrayList<String>();

		void setMatch(BirmBeetleData match) {
			this.match = match;
		}

		void reset() {
			match = null;
			recordedPath.clear();
		}

		List<String> getRecordedPath() {
			return recordedPath;
		}

		BirmBeetleDataRepository createProxy() {
			return (BirmBeetleDataRepository) Proxy.newProxyInstance(
					BirmBeetleDataRepository.class.getClassLoader(),
					new Class[]{BirmBeetleDataRepository.class},
					(proxy, method, args) -> {
						if (method.getDeclaringClass() == Object.class) {
							if ("toString".equals(method.getName())) {
								return "RecordingBirmRepository";
							}
							if ("hashCode".equals(method.getName())) {
								return System.identityHashCode(proxy);
							}
							if ("equals".equals(method.getName())) {
								return proxy == args[0];
							}
						}
						if ("findBySpeciesAndRowNumber".equals(method.getName())) {
							recordedPath.add("composite_lookup");
							return match;
						}
						throw new UnsupportedOperationException(method.getName());
					});
		}
	}
}