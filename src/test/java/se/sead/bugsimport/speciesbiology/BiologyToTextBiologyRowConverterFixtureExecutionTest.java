package se.sead.bugsimport.speciesbiology;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.bibliography.seadmodel.Biblio;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.speciesbiology.bugsmodel.Biology;
import se.sead.bugsimport.speciesbiology.seadmodel.TextBiology;
import se.sead.model.TestBiblio;
import se.sead.model.TestTaxaSpecies;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.repositories.BiblioDataRepository;
import se.sead.repositories.TaxonomicOrderRepository;
import se.sead.repositories.TextBiologyDataRepository;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BiologyToTextBiologyRowConverterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;
	private RecordingTaxonomicOrderRepository taxonomicOrderRepository;
	private RecordingBiblioRepository biblioRepository;
	private RecordingTextBiologyRepository biologyRepository;
	private BiologyToTextBiologyRowConverter converter;

	@Before
	public void setup() throws Exception {
		fixtureLoader = new PolicyFixtureLoader();
		taxonomicOrderRepository = new RecordingTaxonomicOrderRepository();
		biblioRepository = new RecordingBiblioRepository();
		biologyRepository = new RecordingTextBiologyRepository();
		converter = new BiologyToTextBiologyRowConverter();
		injectField(converter, "taxonomicOrderRepository", taxonomicOrderRepository.proxy());
		injectField(converter, "bibliographyDataRepository", biblioRepository.proxy());
		injectField(converter, "biologyDataRepository", biologyRepository.proxy());
	}

	@Test
	public void biologyLookupFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("biology_lookup_returns_existing_text_biology");
	}

	@Test
	public void createNewFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_new_text_biology_when_tuple_missing");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("speciesbiology.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		configureScenario(sourceRow, state, scenarioName);
		TextBiology result = converter.convertForDataRow(createBiologyRow(sourceRow, scenarioName));

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("reconciliation_path"), scenarioName + ".expects.reconciliation_path"), actualPath(result));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("reconciliation_result"), scenarioName + ".expects.reconciliation_result"), actualReconciliationResult(result, sourceRow));
	}

	private void configureScenario(Map<String, Object> sourceRow, Map<String, Object> state, String scenarioName) {
		taxonomicOrderRepository.reset();
		biblioRepository.reset();
		biologyRepository.reset();
		TaxaSpecies species = TestTaxaSpecies.create(301, "species-" + fixtureLoader.integerValue(sourceRow.get("CODE"), scenarioName + ".source_rows[0].CODE"), null, null);
		Biblio reference = TestBiblio.create(302, fixtureLoader.stringValue(sourceRow.get("Ref"), scenarioName + ".source_rows[0].Ref"), "Author", "Title");
		taxonomicOrderRepository.setSpecies(species);
		biblioRepository.setReference(reference);
		if (state != null && state.containsKey("existing_row_id")) {
			biologyRepository.setExisting(new FixtureTextBiology(fixtureLoader.integerValue(state.get("existing_row_id"), scenarioName + ".policy_context.state.existing_row_id"), species, reference,
					fixtureLoader.stringValue(sourceRow.get("Data"), scenarioName + ".source_rows[0].Data")));
		}
	}

	private Biology createBiologyRow(Map<String, Object> sourceRow, String scenarioName) {
		Biology row = new Biology();
		row.setCode(fixtureLoader.integerValue(sourceRow.get("CODE"), scenarioName + ".source_rows[0].CODE").doubleValue());
		row.setRef(fixtureLoader.stringValue(sourceRow.get("Ref"), scenarioName + ".source_rows[0].Ref"));
		row.setData(fixtureLoader.stringValue(sourceRow.get("Data"), scenarioName + ".source_rows[0].Data"));
		return row;
	}

	private List<String> actualPath(TextBiology result) {
		List<String> path = new ArrayList<String>();
		path.addAll(biologyRepository.getRecordedPath());
		if (result.getId() == null && result.isErrorFree()) {
			path.add("create_new");
		}
		return path;
	}

	private Map<String, Object> actualReconciliationResult(TextBiology result, Map<String, Object> sourceRow) {
		Map<String, Object> reconciliationResult = new LinkedHashMap<String, Object>();
		if (result.getId() == null) {
			reconciliationResult.put("result_kind", "insert_new");
			reconciliationResult.put("source", "create_new");
		} else {
			reconciliationResult.put("result_kind", "update_existing");
			reconciliationResult.put("source", "biology_lookup");
		}
		reconciliationResult.put("row_id", result.getId());
		reconciliationResult.put("species_code", fixtureLoader.integerValue(sourceRow.get("CODE"), "sourceRow.CODE"));
		reconciliationResult.put("bugs_reference", sourceRow.get("Ref"));
		reconciliationResult.put("text_value", sourceRow.get("Data"));
		return reconciliationResult;
	}

	private void injectField(Object target, String fieldName, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

	private abstract static class RepositoryHandler<T> implements InvocationHandler {
		private final Class<T> type;

		RepositoryHandler(Class<T> type) {
			this.type = type;
		}

		T proxy() {
			return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, this));
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if (method.getDeclaringClass() == Object.class) {
				String name = method.getName();
				if ("toString".equals(name)) {
					return getClass().getSimpleName();
				}
				if ("hashCode".equals(name)) {
					return System.identityHashCode(this);
				}
				if ("equals".equals(name)) {
					return proxy == args[0];
				}
			}
			return handle(method, args == null ? new Object[0] : args);
		}

		protected abstract Object handle(Method method, Object[] args);
	}

	private static class RecordingTaxonomicOrderRepository extends RepositoryHandler<TaxonomicOrderRepository> {
		private TaxaSpecies species;

		RecordingTaxonomicOrderRepository() {
			super(TaxonomicOrderRepository.class);
		}

		void setSpecies(TaxaSpecies species) {
			this.species = species;
		}

		void reset() {
			species = null;
		}

		@Override
		protected Object handle(Method method, Object[] args) {
			if ("findBugsSpeciesByCode".equals(method.getName())) {
				return species;
			}
			return null;
		}
	}

	private static class RecordingBiblioRepository extends RepositoryHandler<BiblioDataRepository> {
		private Biblio reference;

		RecordingBiblioRepository() {
			super(BiblioDataRepository.class);
		}

		void setReference(Biblio reference) {
			this.reference = reference;
		}

		void reset() {
			reference = null;
		}

		@Override
		protected Object handle(Method method, Object[] args) {
			if ("getByBugsReferenceIgnoreCase".equals(method.getName())) {
				return reference;
			}
			return null;
		}
	}

	private static class RecordingTextBiologyRepository extends RepositoryHandler<TextBiologyDataRepository> {
		private TextBiology existing;
		private final List<String> recordedPath = new ArrayList<String>();

		RecordingTextBiologyRepository() {
			super(TextBiologyDataRepository.class);
		}

		void setExisting(TextBiology existing) {
			this.existing = existing;
		}

		void reset() {
			existing = null;
			recordedPath.clear();
		}

		List<String> getRecordedPath() {
			return recordedPath;
		}

		@Override
		protected Object handle(Method method, Object[] args) {
			if ("findBySpeciesAndTextIgnoreCaseAndReference".equals(method.getName())) {
				recordedPath.add("biology_lookup");
				return existing;
			}
			return null;
		}
	}

	private static class FixtureTextBiology extends TextBiology {
		private FixtureTextBiology(Integer id, TaxaSpecies species, Biblio reference, String text) {
			setId(id);
			setSpecies(species);
			setReference(reference);
			setText(text);
		}
	}
}