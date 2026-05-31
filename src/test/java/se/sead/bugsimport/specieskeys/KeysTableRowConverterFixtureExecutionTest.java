package se.sead.bugsimport.specieskeys;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.bibliography.seadmodel.Biblio;
import se.sead.bugsimport.species.converters.TaxonomicOrderConverter;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.species.seadmodel.TaxonomicOrder;
import se.sead.bugsimport.specieskeys.bugsmodel.Keys;
import se.sead.bugsimport.specieskeys.seadmodel.TextIdentificationKeys;
import se.sead.model.TestBiblio;
import se.sead.model.TestTaxaSpecies;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.repositories.BiblioDataRepository;
import se.sead.repositories.TextIdentificationKeysRepository;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class KeysTableRowConverterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;
	private StubTaxonomicOrderConverter orderConverter;
	private RecordingBiblioRepository biblioRepository;
	private RecordingKeysRepository keysRepository;
	private KeysTableRowConverter converter;

	@Before
	public void setup() throws Exception {
		fixtureLoader = new PolicyFixtureLoader();
		orderConverter = new StubTaxonomicOrderConverter();
		biblioRepository = new RecordingBiblioRepository();
		keysRepository = new RecordingKeysRepository();
		converter = new KeysTableRowConverter();
		injectField(converter, "orderConverter", orderConverter);
		injectField(converter, "biblioRepository", biblioRepository.proxy());
		injectField(converter, "keysRepository", keysRepository.proxy());
	}

	@Test
	public void identificationKeyLookupFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("identification_key_lookup_returns_existing_text_key");
	}

	@Test
	public void createNewFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_new_text_key_when_tuple_missing");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("specieskeys.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		configureScenario(sourceRow, state, scenarioName);
		TextIdentificationKeys result = converter.convertForDataRow(createKeysRow(sourceRow, scenarioName));

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("reconciliation_path"), scenarioName + ".expects.reconciliation_path"), actualPath(result));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("reconciliation_result"), scenarioName + ".expects.reconciliation_result"), actualReconciliationResult(result, sourceRow));
	}

	private void configureScenario(Map<String, Object> sourceRow, Map<String, Object> state, String scenarioName) {
		orderConverter.reset();
		biblioRepository.reset();
		keysRepository.reset();
		TaxaSpecies species = TestTaxaSpecies.create(401, "species-" + fixtureLoader.integerValue(sourceRow.get("CODE"), scenarioName + ".source_rows[0].CODE"), null, null);
		Biblio reference = TestBiblio.create(402, fixtureLoader.stringValue(sourceRow.get("Ref"), scenarioName + ".source_rows[0].Ref"), "Author", "Title");
		orderConverter.setSpecies(species);
		biblioRepository.setReference(reference);
		if (state != null && state.containsKey("existing_row_id")) {
			keysRepository.setExisting(new FixtureTextIdentificationKeys(fixtureLoader.integerValue(state.get("existing_row_id"), scenarioName + ".policy_context.state.existing_row_id"), species, reference,
					fixtureLoader.stringValue(sourceRow.get("Data"), scenarioName + ".source_rows[0].Data")));
		}
	}

	private Keys createKeysRow(Map<String, Object> sourceRow, String scenarioName) {
		Keys row = new Keys();
		row.setCode(fixtureLoader.integerValue(sourceRow.get("CODE"), scenarioName + ".source_rows[0].CODE").doubleValue());
		row.setRef(fixtureLoader.stringValue(sourceRow.get("Ref"), scenarioName + ".source_rows[0].Ref"));
		row.setData(fixtureLoader.stringValue(sourceRow.get("Data"), scenarioName + ".source_rows[0].Data"));
		return row;
	}

	private List<String> actualPath(TextIdentificationKeys result) {
		List<String> path = new ArrayList<String>();
		path.addAll(keysRepository.getRecordedPath());
		if (result.getId() == null && result.isErrorFree()) {
			path.add("create_new");
		}
		return path;
	}

	private Map<String, Object> actualReconciliationResult(TextIdentificationKeys result, Map<String, Object> sourceRow) {
		Map<String, Object> reconciliationResult = new LinkedHashMap<String, Object>();
		if (result.getId() == null) {
			reconciliationResult.put("result_kind", "insert_new");
			reconciliationResult.put("source", "create_new");
		} else {
			reconciliationResult.put("result_kind", "update_existing");
			reconciliationResult.put("source", "identification_key_lookup");
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

	private static class StubTaxonomicOrderConverter extends TaxonomicOrderConverter {
		private TaxaSpecies species;

		void setSpecies(TaxaSpecies species) {
			this.species = species;
		}

		void reset() {
			species = null;
		}

		@Override
		public TaxonomicOrder convertToSeadType(Double code) {
			if (species == null) {
				return null;
			}
			TaxonomicOrder order = new TaxonomicOrder();
			order.setSpecies(species);
			return order;
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

	private static class RecordingKeysRepository extends RepositoryHandler<TextIdentificationKeysRepository> {
		private TextIdentificationKeys existing;
		private final List<String> recordedPath = new ArrayList<String>();

		RecordingKeysRepository() {
			super(TextIdentificationKeysRepository.class);
		}

		void setExisting(TextIdentificationKeys existing) {
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
			if ("findByKeysAndSpeciesAndReference".equals(method.getName())) {
				recordedPath.add("identification_key_lookup");
				return existing;
			}
			return null;
		}
	}

	private static class FixtureTextIdentificationKeys extends TextIdentificationKeys {
		private FixtureTextIdentificationKeys(Integer id, TaxaSpecies species, Biblio reference, String keys) {
			setId(id);
			setSpecies(species);
			setReference(reference);
			setKeys(keys);
		}
	}
}