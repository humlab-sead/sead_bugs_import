package se.sead.bugsimport.mcr;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.mcr.bugsmodel.MCRSummaryData;
import se.sead.bugsimport.mcr.seadmodel.MCRSummary;
import se.sead.bugsimport.species.converters.TaxonomicOrderConverter;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.species.seadmodel.TaxonomicOrder;
import se.sead.model.TestMCRSummary;
import se.sead.model.TestTaxaSpecies;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.repositories.MCRSummaryRepository;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MCRSummaryTableRowConverterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;
	private RecordingTaxonomicOrderConverter orderConverter;
	private RecordingSummaryRepository summaryRepository;
	private MCRSummaryTableRowConverter rowConverter;

	@Before
	public void setup() throws Exception {
		fixtureLoader = new PolicyFixtureLoader();
		orderConverter = new RecordingTaxonomicOrderConverter();
		summaryRepository = new RecordingSummaryRepository();
		rowConverter = new MCRSummaryTableRowConverter();
		injectField(rowConverter, "orderConverter", orderConverter);
		injectField(rowConverter, "summaryRepository", summaryRepository.createProxy());
	}

	@Test
	public void speciesLookupFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("species_lookup_returns_existing_summary_as_is");
	}

	@Test
	public void createNewFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_new_summary_when_species_lookup_misses");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("mcrsummary.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		configureScenario(sourceRow, policyContext, state, scenarioName);
		MCRSummary result = rowConverter.convertForDataRow(createSummaryData(sourceRow, scenarioName));

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("reconciliation_path"), scenarioName + ".expects.reconciliation_path"), actualPath(result));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("reconciliation_result"), scenarioName + ".expects.reconciliation_result"), actualReconciliationResult(result, sourceRow));
	}

	private void configureScenario(Map<String, Object> sourceRow, Map<String, Object> policyContext, Map<String, Object> state, String scenarioName) {
		Map<String, Object> stepHits = fixtureLoader.mapValue(policyContext.get("step_hits"), scenarioName + ".policy_context.step_hits");
		TaxaSpecies species = TestTaxaSpecies.create(501, "fixture species " + integerCode(sourceRow, scenarioName), null, null);
		orderConverter.setSpecies(species);
		summaryRepository.reset();
		if (Boolean.TRUE.equals(stepHits.get("species_lookup"))) {
			summaryRepository.setMatch(TestMCRSummary.create(
					fixtureLoader.integerValue(state.get("existing_row_id"), scenarioName + ".policy_context.state.existing_row_id"),
					species,
					fixtureLoader.integerValue(sourceRow.get("TMaxLo"), scenarioName + ".source_rows[0].TMaxLo"),
					fixtureLoader.integerValue(sourceRow.get("TMaxHi"), scenarioName + ".source_rows[0].TMaxHi"),
					fixtureLoader.integerValue(sourceRow.get("TMinLo"), scenarioName + ".source_rows[0].TMinLo"),
					fixtureLoader.integerValue(sourceRow.get("TMinHi"), scenarioName + ".source_rows[0].TMinHi"),
					fixtureLoader.integerValue(sourceRow.get("TRangeLo"), scenarioName + ".source_rows[0].TRangeLo"),
					fixtureLoader.integerValue(sourceRow.get("TRangeHi"), scenarioName + ".source_rows[0].TRangeHi"),
					fixtureLoader.integerValue(sourceRow.get("COGMidTMax"), scenarioName + ".source_rows[0].COGMidTMax"),
					fixtureLoader.integerValue(sourceRow.get("COGMidTRange"), scenarioName + ".source_rows[0].COGMidTRange")));
		}
	}

	private MCRSummaryData createSummaryData(Map<String, Object> sourceRow, String scenarioName) {
		MCRSummaryData data = new MCRSummaryData();
		data.setCode(doubleValue(sourceRow.get("CODE"), scenarioName + ".source_rows[0].CODE"));
		data.setMaxLo(shortValue(sourceRow.get("TMaxLo"), scenarioName + ".source_rows[0].TMaxLo"));
		data.setMaxHi(shortValue(sourceRow.get("TMaxHi"), scenarioName + ".source_rows[0].TMaxHi"));
		data.setMinLo(shortValue(sourceRow.get("TMinLo"), scenarioName + ".source_rows[0].TMinLo"));
		data.setMinHi(shortValue(sourceRow.get("TMinHi"), scenarioName + ".source_rows[0].TMinHi"));
		data.setRangeLo(shortValue(sourceRow.get("TRangeLo"), scenarioName + ".source_rows[0].TRangeLo"));
		data.setRangeHi(shortValue(sourceRow.get("TRangeHi"), scenarioName + ".source_rows[0].TRangeHi"));
		data.setCogMidTMax(shortValue(sourceRow.get("COGMidTMax"), scenarioName + ".source_rows[0].COGMidTMax"));
		data.setCogMidTRange(shortValue(sourceRow.get("COGMidTRange"), scenarioName + ".source_rows[0].COGMidTRange"));
		return data;
	}

	private List<String> actualPath(MCRSummary result) {
		List<String> path = new ArrayList<String>();
		path.addAll(summaryRepository.getRecordedPath());
		if (result.getId() == null && result.isErrorFree()) {
			path.add("create_new");
		}
		return path;
	}

	private Map<String, Object> actualReconciliationResult(MCRSummary result, Map<String, Object> sourceRow) {
		Map<String, Object> reconciliationResult = new LinkedHashMap<String, Object>();
		if (result.getId() == null) {
			reconciliationResult.put("result_kind", "insert_new");
			reconciliationResult.put("source", "create_new");
		} else {
			reconciliationResult.put("result_kind", "return_as_is");
			reconciliationResult.put("source", "species_lookup");
		}
		reconciliationResult.put("row_id", result.getId());
		reconciliationResult.put("species_code", integerCode(sourceRow, "sourceRow"));
		return reconciliationResult;
	}

	private Integer integerCode(Map<String, Object> sourceRow, String context) {
		return doubleValue(sourceRow.get("CODE"), context + ".CODE").intValue();
	}

	private Double doubleValue(Object value, String context) {
		if (!(value instanceof Number)) {
			throw new IllegalArgumentException(context + " must be numeric");
		}
		return ((Number) value).doubleValue();
	}

	private Short shortValue(Object value, String context) {
		if (!(value instanceof Number)) {
			throw new IllegalArgumentException(context + " must be numeric");
		}
		return ((Number) value).shortValue();
	}

	private void injectField(Object target, String fieldName, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

	private static class RecordingTaxonomicOrderConverter extends TaxonomicOrderConverter {
		private TaxaSpecies species;

		void setSpecies(TaxaSpecies species) {
			this.species = species;
		}

		@Override
		public TaxonomicOrder convertToSeadType(Double code) {
			TaxonomicOrder order = new TaxonomicOrder();
			order.setSpecies(species);
			return order;
		}
	}

	private static class RecordingSummaryRepository {
		private MCRSummary match;
		private final List<String> recordedPath = new ArrayList<String>();

		void setMatch(MCRSummary match) {
			this.match = match;
		}

		void reset() {
			match = null;
			recordedPath.clear();
		}

		List<String> getRecordedPath() {
			return recordedPath;
		}

		MCRSummaryRepository createProxy() {
			return (MCRSummaryRepository) Proxy.newProxyInstance(
					MCRSummaryRepository.class.getClassLoader(),
					new Class[]{MCRSummaryRepository.class},
					(proxy, method, args) -> {
						if (method.getDeclaringClass() == Object.class) {
							if ("toString".equals(method.getName())) {
								return "RecordingSummaryRepository";
							}
							if ("hashCode".equals(method.getName())) {
								return System.identityHashCode(proxy);
							}
							if ("equals".equals(method.getName())) {
								return proxy == args[0];
							}
						}
						if ("findBySpecies".equals(method.getName())) {
							recordedPath.add("species_lookup");
							return match;
						}
						throw new UnsupportedOperationException(method.getName());
					});
		}
	}
}