package se.sead.bugsimport.speciesdistribution;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.bibliography.seadmodel.Biblio;
import se.sead.bugsimport.species.converters.TaxonomicOrderConverter;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.species.seadmodel.TaxonomicOrder;
import se.sead.bugsimport.speciesdistribution.bugsmodel.Distrib;
import se.sead.bugsimport.speciesdistribution.seadmodel.TextDistribution;
import se.sead.model.TestBiblio;
import se.sead.model.TestTaxaSpecies;
import se.sead.model.TestTaxonomyOrder;
import se.sead.model.TestTextDistribution;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.repositories.BiblioDataRepository;
import se.sead.repositories.TextDistributionRepository;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SpeciesDistributionTableRowConverterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;
	private SpeciesDistributionTableRowConverter rowConverter;
	private RecordingDistributionRepository distributionRepository;
	private TaxaSpecies species;
	private Biblio reference;

	@Before
	public void setup() throws Exception {
		fixtureLoader = new PolicyFixtureLoader();
		species = TestTaxaSpecies.create(21, "species-21", null, null);
		reference = TestBiblio.create(31, "REF-101", "Author", "Title");
		distributionRepository = new RecordingDistributionRepository();
		rowConverter = new SpeciesDistributionTableRowConverter();
		setField(rowConverter, "orderConverter", new RecordingTaxonomicOrderConverter(species));
		setField(rowConverter, "biblioDataRepository", new RecordingBiblioDataRepository(reference));
		setField(rowConverter, "distributionRepository", distributionRepository);
	}

	@Test
	public void tupleLookupFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("tuple_lookup_updates_existing_distribution");
	}

	@Test
	public void createNewFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_new_distribution_when_tuple_missing");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("speciesdistribution.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> stepHits = fixtureLoader.mapValue(policyContext.get("step_hits"), scenarioName + ".policy_context.step_hits");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		configureRepository(sourceRow, stepHits, state, scenarioName);
		Distrib bugsData = createDistrib(sourceRow, scenarioName);
		TextDistribution result = rowConverter.convertForDataRow(bugsData);

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("reconciliation_path"), scenarioName + ".expects.reconciliation_path"), distributionRepository.getRecordedPath());
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("reconciliation_result"), scenarioName + ".expects.reconciliation_result"), actualReconciliationResult(result, bugsData));
	}

	private void configureRepository(Map<String, Object> sourceRow, Map<String, Object> stepHits, Map<String, Object> state, String scenarioName) {
		distributionRepository.reset();
		if (Boolean.TRUE.equals(stepHits.get("distribution_lookup"))) {
			distributionRepository.setMatch(TestTextDistribution.create(
					fixtureLoader.integerValue(state.get("existing_row_id"), scenarioName + ".policy_context.state.existing_row_id"),
					species,
					reference,
					fixtureLoader.stringValue(sourceRow.get("Data"), scenarioName + ".source_rows[0].Data")
			));
		}
	}

	private Distrib createDistrib(Map<String, Object> sourceRow, String scenarioName) {
		Distrib distrib = new Distrib();
		distrib.setCode(doubleValue(sourceRow.get("CODE"), scenarioName + ".source_rows[0].CODE"));
		distrib.setRef(fixtureLoader.stringValue(sourceRow.get("Ref"), scenarioName + ".source_rows[0].Ref"));
		distrib.setData(fixtureLoader.stringValue(sourceRow.get("Data"), scenarioName + ".source_rows[0].Data"));
		return distrib;
	}

	private Double doubleValue(Object value, String context) {
		if (value instanceof Number) {
			return ((Number) value).doubleValue();
		}
		if (value instanceof String) {
			return Double.valueOf((String) value);
		}
		throw new IllegalArgumentException(context + " must be numeric or numeric string");
	}

	private Map<String, Object> actualReconciliationResult(TextDistribution result, Distrib bugsData) {
		Map<String, Object> reconciliationResult = new LinkedHashMap<String, Object>();
		if (result.getId() == null) {
			reconciliationResult.put("result_kind", "insert_new");
			reconciliationResult.put("source", "create_new");
		} else {
			reconciliationResult.put("result_kind", "update_existing");
			reconciliationResult.put("source", "distribution_lookup");
		}
		reconciliationResult.put("row_id", result.getId());
		reconciliationResult.put("species_code", bugsData.getCode() == null ? null : bugsData.getCode().intValue());
		return reconciliationResult;
	}

	private void setField(Object target, String name, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(name);
		field.setAccessible(true);
		field.set(target, value);
	}

	private static class RecordingTaxonomicOrderConverter extends TaxonomicOrderConverter {

		private final TaxonomicOrder taxonomicOrder;

		RecordingTaxonomicOrderConverter(TaxaSpecies species) {
			taxonomicOrder = TestTaxonomyOrder.create(11, species, new BigDecimal("101"), null);
		}

		@Override
		public TaxonomicOrder convertToSeadType(Double code) {
			return taxonomicOrder;
		}
	}

	private static class RecordingBiblioDataRepository implements BiblioDataRepository {

		private final Biblio reference;

		RecordingBiblioDataRepository(Biblio reference) {
			this.reference = reference;
		}

		@Override
		public Biblio getByBugsReferenceIgnoreCase(String bugsReference) {
			return reference;
		}

		@Override
		public List<Biblio> findAll() {
			return Collections.singletonList(reference);
		}

		@Override
		public Biblio findOne(Integer integer) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Biblio saveOrUpdate(Biblio entity) {
			throw new UnsupportedOperationException();
		}
	}

	private static class RecordingDistributionRepository implements TextDistributionRepository {

		private final List<String> recordedPath = new java.util.ArrayList<String>();
		private TextDistribution match;

		void setMatch(TextDistribution match) {
			this.match = match;
		}

		void reset() {
			recordedPath.clear();
			match = null;
		}

		List<String> getRecordedPath() {
			List<String> path = new java.util.ArrayList<String>(recordedPath);
			if (match == null) {
				path.add("create_new");
			}
			return path;
		}

		@Override
		public List<TextDistribution> findBySpecies(TaxaSpecies species) {
			return Collections.emptyList();
		}

		@Override
		public TextDistribution findByDistributionAndSpeciesAndReference(String distribution, TaxaSpecies species, Biblio reference) {
			recordedPath.add("distribution_lookup");
			return match;
		}

		@Override
		public TextDistribution findOne(Integer integer) {
			throw new UnsupportedOperationException();
		}

		@Override
		public TextDistribution saveOrUpdate(TextDistribution entity) {
			throw new UnsupportedOperationException();
		}
	}
}