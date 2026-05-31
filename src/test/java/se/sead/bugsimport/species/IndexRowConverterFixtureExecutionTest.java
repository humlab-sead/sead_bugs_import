package se.sead.bugsimport.species;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.species.bugsmodel.INDEX;
import se.sead.bugsimport.species.converters.TaxaSpeciesConverter;
import se.sead.bugsimport.species.converters.TaxonomicOrderConverter;
import se.sead.bugsimport.species.seadmodel.TaxonomicOrder;
import se.sead.model.TestTaxaSpecies;
import se.sead.model.TestTaxonomicOrderSystem;
import se.sead.model.TestTaxonomyOrder;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.utils.BigDecimalDefinition;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class IndexRowConverterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;
	private RecordingTaxonomicOrderConverter taxonomicOrderConverter;
	private RecordingTaxaSpeciesConverter taxaSpeciesConverter;
	private INDEXtoTaxonomicOrderRowConverter rowConverter;

	@Before
	public void setup() throws Exception {
		fixtureLoader = new PolicyFixtureLoader();
		taxonomicOrderConverter = new RecordingTaxonomicOrderConverter();
		taxaSpeciesConverter = new RecordingTaxaSpeciesConverter();
		rowConverter = new INDEXtoTaxonomicOrderRowConverter();
		injectField(rowConverter, "taxonomicOrderConverter", taxonomicOrderConverter);
		injectField(rowConverter, "taxaConverter", taxaSpeciesConverter);
	}

	@Test
	public void codeLookupFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("code_lookup_returns_existing_taxonomic_order");
	}

	@Test
	public void createNewFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_new_taxonomic_order_when_code_lookup_misses");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("species.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		configureScenario(sourceRow, state, scenarioName);
		TaxonomicOrder result = rowConverter.convertForDataRow(createIndexRow(sourceRow, scenarioName));

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("reconciliation_path"), scenarioName + ".expects.reconciliation_path"), actualPath(result));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("reconciliation_result"), scenarioName + ".expects.reconciliation_result"), actualReconciliationResult(result, sourceRow));
	}

	private void configureScenario(Map<String, Object> sourceRow, Map<String, Object> state, String scenarioName) {
		taxonomicOrderConverter.reset();
		taxaSpeciesConverter.reset();
		TaxonomicOrder order;
		if (state != null && state.containsKey("existing_row_id")) {
			order = TestTaxonomyOrder.create(
					fixtureLoader.integerValue(state.get("existing_row_id"), scenarioName + ".policy_context.state.existing_row_id"),
					null,
					BigDecimalDefinition.convertToSeadCode(((Number) sourceRow.get("CODE")).doubleValue()),
					TestTaxonomicOrderSystem.create(501, "BugsCEP taxonomic order"));
		} else {
			order = TestTaxonomyOrder.create(
					null,
					null,
					BigDecimalDefinition.convertToSeadCode(((Number) sourceRow.get("CODE")).doubleValue()),
					TestTaxonomicOrderSystem.create(501, "BugsCEP taxonomic order"));
			taxaSpeciesConverter.setSpecies(TestTaxaSpecies.create(null, fixtureLoader.stringValue(sourceRow.get("SPECIES"), scenarioName + ".source_rows[0].SPECIES"), null, null));
		}
		taxonomicOrderConverter.setOrder(order);
	}

	private INDEX createIndexRow(Map<String, Object> sourceRow, String scenarioName) {
		INDEX bugs = new INDEX();
		bugs.setCode(((Number) sourceRow.get("CODE")).doubleValue());
		bugs.setFamily(fixtureLoader.stringValue(sourceRow.get("FAMILY"), scenarioName + ".source_rows[0].FAMILY"));
		bugs.setGenus(fixtureLoader.stringValue(sourceRow.get("GENUS"), scenarioName + ".source_rows[0].GENUS"));
		bugs.setSpecies(fixtureLoader.stringValue(sourceRow.get("SPECIES"), scenarioName + ".source_rows[0].SPECIES"));
		bugs.setAuthority(sourceRow.get("AUTHORITY") == null ? null : fixtureLoader.stringValue(sourceRow.get("AUTHORITY"), scenarioName + ".source_rows[0].AUTHORITY"));
		return bugs;
	}

	private List<String> actualPath(TaxonomicOrder result) {
		List<String> path = new ArrayList<String>();
		path.addAll(taxonomicOrderConverter.getRecordedPath());
		if (result.getId() == null && result.isErrorFree()) {
			path.add("create_new");
		}
		return path;
	}

	private Map<String, Object> actualReconciliationResult(TaxonomicOrder result, Map<String, Object> sourceRow) {
		Map<String, Object> reconciliationResult = new LinkedHashMap<String, Object>();
		if (result.getId() == null) {
			reconciliationResult.put("result_kind", "insert_new");
			reconciliationResult.put("source", "create_new");
		} else {
			reconciliationResult.put("result_kind", "update_existing");
			reconciliationResult.put("source", "code_lookup");
		}
		reconciliationResult.put("row_id", result.getId());
		reconciliationResult.put("species_code", ((Number) sourceRow.get("CODE")).intValue());
		return reconciliationResult;
	}

	private void injectField(Object target, String fieldName, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

	private static class RecordingTaxonomicOrderConverter extends TaxonomicOrderConverter {
		private final List<String> recordedPath = new ArrayList<String>();
		private TaxonomicOrder order;

		void setOrder(TaxonomicOrder order) {
			this.order = order;
		}

		void reset() {
			recordedPath.clear();
			order = null;
		}

		List<String> getRecordedPath() {
			return recordedPath;
		}

		@Override
		public TaxonomicOrder convertToSeadType(Double code) {
			recordedPath.add("code_lookup");
			return order;
		}
	}

	private static class RecordingTaxaSpeciesConverter extends TaxaSpeciesConverter {
		private se.sead.bugsimport.species.seadmodel.TaxaSpecies species;

		void setSpecies(se.sead.bugsimport.species.seadmodel.TaxaSpecies species) {
			this.species = species;
		}

		void reset() {
			species = null;
		}

		@Override
		public se.sead.bugsimport.species.seadmodel.TaxaSpecies convertToSeadType(INDEX bugsData) {
			return species;
		}
	}
}