package se.sead.bugsimport.species;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.species.bugsmodel.INDEX;
import se.sead.bugsimport.species.converters.NoDataSpeciesConverter;
import se.sead.bugsimport.species.converters.TaxaAuthorConverter;
import se.sead.bugsimport.species.converters.TaxaFamilyConverter;
import se.sead.bugsimport.species.converters.TaxaGenusConverter;
import se.sead.bugsimport.species.converters.TaxaSpeciesConverter;
import se.sead.bugsimport.species.seadmodel.TaxaAuthor;
import se.sead.bugsimport.species.seadmodel.TaxaFamily;
import se.sead.bugsimport.species.seadmodel.TaxaGenus;
import se.sead.bugsimport.species.seadmodel.TaxaOrder;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.model.TestTaxaAuthor;
import se.sead.model.TestTaxaFamily;
import se.sead.model.TestTaxaGenus;
import se.sead.model.TestTaxaOrder;
import se.sead.model.TestTaxaSpecies;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.repositories.SpeciesRepository;
import se.sead.repositories.TaxaAuthorRepository;
import se.sead.repositories.TaxaFamilyRepository;
import se.sead.repositories.TaxaGenusRepository;
import se.sead.repositories.TaxaOrderRepository;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TaxaSpeciesConverterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;
	private RecordingOrderRepository orderRepository;
	private RecordingFamilyRepository familyRepository;
	private RecordingGenusRepository genusRepository;
	private RecordingAuthorRepository authorRepository;
	private RecordingSpeciesRepository speciesRepository;
	private TaxaFamilyConverter familyConverter;
	private TaxaGenusConverter genusConverter;
	private TaxaAuthorConverter authorConverter;
	private NoDataSpeciesConverter noDataSpeciesConverter;
	private TaxaSpeciesConverter speciesConverter;

	@Before
	public void setup() throws Exception {
		fixtureLoader = new PolicyFixtureLoader();
		orderRepository = new RecordingOrderRepository();
		familyRepository = new RecordingFamilyRepository();
		genusRepository = new RecordingGenusRepository();
		authorRepository = new RecordingAuthorRepository();
		speciesRepository = new RecordingSpeciesRepository();

		familyConverter = new TaxaFamilyConverter(familyRepository.createProxy(), orderRepository.createProxy());
		genusConverter = new TaxaGenusConverter();
		injectField(genusConverter, "genusRepository", genusRepository.createProxy());
		injectField(genusConverter, "familyConverter", familyConverter);
		authorConverter = new TaxaAuthorConverter();
		injectField(authorConverter, "authorRepository", authorRepository.createProxy());
		noDataSpeciesConverter = new NoDataSpeciesConverter();
		injectField(noDataSpeciesConverter, "repository", speciesRepository.createProxy());
		injectField(noDataSpeciesConverter, "NO_DATA_CODE", Double.valueOf("9999.0000001"));
		speciesConverter = new TaxaSpeciesConverter();
		injectField(speciesConverter, "speciesRepository", speciesRepository.createProxy());
		injectField(speciesConverter, "genusConverter", genusConverter);
		injectField(speciesConverter, "authorConverter", authorConverter);
		injectField(speciesConverter, "noDataSpeciesConverter", noDataSpeciesConverter);
	}

	@Test
	public void createTaxaFamilyFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_taxa_family_when_missing");
	}

	@Test
	public void reuseTaxaFamilyFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("reuse_existing_taxa_family");
	}

	@Test
	public void createTaxaGenusFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_taxa_genus_when_missing");
	}

	@Test
	public void reuseTaxaGenusFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("reuse_existing_taxa_genus");
	}

	@Test
	public void createTaxaAuthorFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_taxa_author_when_missing");
	}

	@Test
	public void reuseTaxaAuthorFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("reuse_existing_taxa_author");
	}

	@Test
	public void nullTaxaAuthorFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("return_null_taxa_author_when_authority_missing");
	}

	@Test
	public void createTaxaSpeciesFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_taxa_species_when_missing");
	}

	@Test
	public void reuseTaxaSpeciesFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("reuse_existing_taxa_species");
	}

	@Test
	public void createTaxaSpeciesWithoutAuthorFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_taxa_species_without_author_when_authority_missing");
	}

	@Test
	public void reuseTaxaSpeciesWithoutAuthorFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("reuse_existing_taxa_species_without_author");
	}

	@Test
	public void noDataTaxaSpeciesFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("reuse_no_data_taxa_species_shortcut");
	}

	@Test
	public void createSpeciesRelatedOutputGraphFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertRelatedOutputGraphScenarioMatchesCurrentJavaBehavior("create_species_related_output_graph_when_tree_missing");
	}

	@Test
	public void reuseSpeciesRelatedOutputGraphFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertRelatedOutputGraphScenarioMatchesCurrentJavaBehavior("reuse_species_related_output_graph_when_tree_exists");
	}

	@Test
	public void mixedSpeciesRelatedOutputGraphFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertRelatedOutputGraphScenarioMatchesCurrentJavaBehavior("mixed_species_related_output_graph_when_upper_tree_exists");
	}

	@Test
	public void familyReuseSpeciesRelatedOutputGraphFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertRelatedOutputGraphScenarioMatchesCurrentJavaBehavior("reuse_family_then_create_species_related_output_graph");
	}

	@Test
	public void noAuthorSpeciesRelatedOutputGraphFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertRelatedOutputGraphScenarioMatchesCurrentJavaBehavior("reuse_upper_tree_without_author_in_species_related_output_graph");
	}

	@Test
	public void noDataSpeciesRelatedOutputGraphFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertRelatedOutputGraphScenarioMatchesCurrentJavaBehavior("reuse_no_data_species_related_output_graph_shortcut");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("species.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> args = fixtureLoader.mapValue(policyContext.get("args"), scenarioName + ".policy_context.args");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		configureScenario(state, sourceRow);
		String outputName = fixtureLoader.stringValue(args.get("output_name"), scenarioName + ".policy_context.args.output_name");
		Map<String, Object> actualGraphResult = actualGraphResult(outputName, sourceRow, scenarioName);

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("related_outputs"), scenarioName + ".expects.related_outputs"), java.util.Collections.singletonList(outputName));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("graph_result"), scenarioName + ".expects.graph_result"), actualGraphResult);
	}

	private void assertRelatedOutputGraphScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("species.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		configureScenario(state, sourceRow);
		Map<String, Object> actualGraphResult = actualRelatedOutputGraphResult(sourceRow, scenarioName);

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("related_outputs"), scenarioName + ".expects.related_outputs"), java.util.Arrays.asList("taxa_family", "taxa_genus", "taxa_author", "taxa_species"));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("graph_result"), scenarioName + ".expects.graph_result"), actualGraphResult);
	}

	private void configureScenario(Map<String, Object> state, Map<String, Object> sourceRow) {
		orderRepository.reset();
		familyRepository.reset();
		genusRepository.reset();
		authorRepository.reset();
		speciesRepository.reset();

		TaxaOrder defaultOrder = TestTaxaOrder.create(701, "ORDER PENDING CLASSIFICATION");
		orderRepository.setDefaultOrder(defaultOrder);

		TaxaFamily family = null;
		if (state != null && state.containsKey("existing_family_id")) {
			family = TestTaxaFamily.create(fixtureLoader.integerValue(state.get("existing_family_id"), "existing_family_id"), fixtureLoader.stringValue(sourceRow.get("FAMILY"), "FAMILY"), defaultOrder);
			familyRepository.setFamily(family);
		}

		TaxaGenus genus = null;
		if (state != null && state.containsKey("existing_genus_id")) {
			if (family == null) {
				family = TestTaxaFamily.create(801, fixtureLoader.stringValue(sourceRow.get("FAMILY"), "FAMILY"), defaultOrder);
			}
			genus = TestTaxaGenus.create(fixtureLoader.integerValue(state.get("existing_genus_id"), "existing_genus_id"), fixtureLoader.stringValue(sourceRow.get("GENUS"), "GENUS"), family);
			genusRepository.setGenus(genus);
		}

		TaxaAuthor author = null;
		if (state != null && state.containsKey("existing_author_id")) {
			author = TestTaxaAuthor.create(fixtureLoader.integerValue(state.get("existing_author_id"), "existing_author_id"), fixtureLoader.stringValue(sourceRow.get("AUTHORITY"), "AUTHORITY"));
			authorRepository.setAuthor(author);
		}

		if (state != null && state.containsKey("existing_species_id")) {
			if (genus == null) {
				TaxaFamily speciesFamily = family == null ? TestTaxaFamily.create(801, fixtureLoader.stringValue(sourceRow.get("FAMILY"), "FAMILY"), defaultOrder) : family;
				genus = TestTaxaGenus.create(802, fixtureLoader.stringValue(sourceRow.get("GENUS"), "GENUS"), speciesFamily);
			}
			speciesRepository.setSpecies(TestTaxaSpecies.create(
					fixtureLoader.integerValue(state.get("existing_species_id"), "existing_species_id"),
					fixtureLoader.stringValue(sourceRow.get("SPECIES"), "SPECIES"),
					genus,
					author));
		}

		if (state != null && state.containsKey("no_data_species_id")) {
			TaxaFamily noDataFamily = TestTaxaFamily.create(903, "No data", defaultOrder);
			TaxaGenus noDataGenus = TestTaxaGenus.create(fixtureLoader.integerValue(state.get("no_data_genus_id"), "no_data_genus_id"), "No data", noDataFamily);
			speciesRepository.setNoDataSpecies(TestTaxaSpecies.create(
					fixtureLoader.integerValue(state.get("no_data_species_id"), "no_data_species_id"),
					"No data",
					noDataGenus,
					null));
		}
	}

	private Map<String, Object> actualGraphResult(String outputName, Map<String, Object> sourceRow, String scenarioName) {
		INDEX bugs = createIndexRow(sourceRow, scenarioName);
		Map<String, Object> graphResult = new LinkedHashMap<String, Object>();
		if ("taxa_family".equals(outputName)) {
			TaxaFamily family = familyConverter.convertToSeadType(fixtureLoader.stringValue(sourceRow.get("FAMILY"), scenarioName + ".source_rows[0].FAMILY"));
			graphResult.put("taxa_family", taxaFamilyResult(family));
			return graphResult;
		}
		if ("taxa_genus".equals(outputName)) {
			TaxaGenus genus = genusConverter.convertToSeadType(bugs);
			graphResult.put("taxa_genus", taxaGenusResult(genus));
			return graphResult;
		}
		if ("taxa_author".equals(outputName)) {
			TaxaAuthor author = authorConverter.convertToSeadType(sourceRow.get("AUTHORITY") == null ? null : fixtureLoader.stringValue(sourceRow.get("AUTHORITY"), scenarioName + ".source_rows[0].AUTHORITY"));
			graphResult.put("taxa_author", taxaAuthorResult(author));
			return graphResult;
		}
		TaxaSpecies species = speciesConverter.convertToSeadType(bugs);
		graphResult.put("taxa_species", taxaSpeciesResult(species));
		return graphResult;
	}

	private Map<String, Object> actualRelatedOutputGraphResult(Map<String, Object> sourceRow, String scenarioName) {
		INDEX bugs = createIndexRow(sourceRow, scenarioName);
		TaxaSpecies species = speciesConverter.convertToSeadType(bugs);
		Map<String, Object> graphResult = new LinkedHashMap<String, Object>();
		TaxaGenus genus = species.getGenus();
		TaxaFamily family = genus == null ? null : genus.getFamily();
		graphResult.put("taxa_family", taxaFamilyResult(family));
		graphResult.put("taxa_genus", taxaGenusResult(genus));
		graphResult.put("taxa_author", taxaAuthorResult(species.getAuthor()));
		graphResult.put("taxa_species", taxaSpeciesResult(species));
		return graphResult;
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

	private Map<String, Object> taxaFamilyResult(TaxaFamily family) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		if (family == null) {
			result.put("result_kind", "insert_new");
			result.put("supporting_action", "create");
			result.put("family_id", null);
			result.put("family_name", null);
			result.put("order_id", null);
			return result;
		}
		result.put("result_kind", family.getId() == null ? "insert_new" : "return_existing");
		result.put("supporting_action", family.getId() == null ? "create" : "reuse");
		result.put("family_id", family.getId());
		result.put("family_name", family.getFamilyName());
		result.put("order_id", family.getOrder() == null ? null : family.getOrder().getId());
		return result;
	}

	private Map<String, Object> taxaGenusResult(TaxaGenus genus) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		if (genus == null) {
			result.put("result_kind", "insert_new");
			result.put("supporting_action", "create");
			result.put("genus_id", null);
			result.put("genus_name", null);
			result.put("family_id", null);
			return result;
		}
		result.put("result_kind", genus.getId() == null ? "insert_new" : "return_existing");
		result.put("supporting_action", genus.getId() == null ? "create" : "reuse");
		result.put("genus_id", genus.getId());
		result.put("genus_name", genus.getGenusName());
		result.put("family_id", genus.getFamily() == null ? null : genus.getFamily().getId());
		return result;
	}

	private Map<String, Object> taxaAuthorResult(TaxaAuthor author) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		if (author == null) {
			result.put("result_kind", "insert_new");
			result.put("supporting_action", "create");
			result.put("author_id", null);
			result.put("author_name", null);
			return result;
		}
		result.put("result_kind", author.getId() == null ? "insert_new" : "return_existing");
		result.put("supporting_action", author.getId() == null ? "create" : "reuse");
		result.put("author_id", author.getId());
		result.put("author_name", author.getAuthorName());
		return result;
	}

	private Map<String, Object> taxaSpeciesResult(TaxaSpecies species) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("result_kind", species.getId() == null ? "insert_new" : "return_existing");
		result.put("supporting_action", species.getId() == null ? "create" : "reuse");
		result.put("species_id", species.getId());
		result.put("species", species.getSpeciesName());
		result.put("genus_id", species.getGenus() == null ? null : species.getGenus().getId());
		result.put("author_id", species.getAuthor() == null ? null : species.getAuthor().getId());
		return result;
	}

	private void injectField(Object target, String fieldName, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

	private abstract static class RepositoryProxyFactory<T> {
		private final Class<T> type;

		RepositoryProxyFactory(Class<T> type) {
			this.type = type;
		}

		T createProxy() {
			return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, (proxy, method, args) -> {
				if (method.getDeclaringClass() == Object.class) {
					if ("toString".equals(method.getName())) {
						return getClass().getSimpleName();
					}
					if ("hashCode".equals(method.getName())) {
						return System.identityHashCode(proxy);
					}
					if ("equals".equals(method.getName())) {
						return proxy == args[0];
					}
				}
				return handle(method.getName(), args == null ? new Object[0] : args);
			}));
		}

		abstract Object handle(String methodName, Object[] args);
	}

	private static class RecordingOrderRepository extends RepositoryProxyFactory<TaxaOrderRepository> {
		private TaxaOrder defaultOrder;

		RecordingOrderRepository() {
			super(TaxaOrderRepository.class);
		}

		void setDefaultOrder(TaxaOrder defaultOrder) {
			this.defaultOrder = defaultOrder;
		}

		void reset() {
			defaultOrder = null;
		}

		@Override
		Object handle(String methodName, Object[] args) {
			if ("getImportOrder".equals(methodName)) {
				return defaultOrder;
			}
			throw new UnsupportedOperationException(methodName);
		}
	}

	private static class RecordingFamilyRepository extends RepositoryProxyFactory<TaxaFamilyRepository> {
		private TaxaFamily family;

		RecordingFamilyRepository() {
			super(TaxaFamilyRepository.class);
		}

		void setFamily(TaxaFamily family) {
			this.family = family;
		}

		void reset() {
			family = null;
		}

		@Override
		Object handle(String methodName, Object[] args) {
			if ("findByFamilyNameAndOrder".equals(methodName)) {
				return family;
			}
			throw new UnsupportedOperationException(methodName);
		}
	}

	private static class RecordingGenusRepository extends RepositoryProxyFactory<TaxaGenusRepository> {
		private TaxaGenus genus;

		RecordingGenusRepository() {
			super(TaxaGenusRepository.class);
		}

		void setGenus(TaxaGenus genus) {
			this.genus = genus;
		}

		void reset() {
			genus = null;
		}

		@Override
		Object handle(String methodName, Object[] args) {
			if ("findByGenusNameAndFamily".equals(methodName)) {
				return genus;
			}
			throw new UnsupportedOperationException(methodName);
		}
	}

	private static class RecordingAuthorRepository extends RepositoryProxyFactory<TaxaAuthorRepository> {
		private TaxaAuthor author;

		RecordingAuthorRepository() {
			super(TaxaAuthorRepository.class);
		}

		void setAuthor(TaxaAuthor author) {
			this.author = author;
		}

		void reset() {
			author = null;
		}

		@Override
		Object handle(String methodName, Object[] args) {
			if ("findByAuthorName".equals(methodName)) {
				return author;
			}
			throw new UnsupportedOperationException(methodName);
		}
	}

	private static class RecordingSpeciesRepository extends RepositoryProxyFactory<SpeciesRepository> {
		private TaxaSpecies species;
		private TaxaSpecies noDataSpecies;

		RecordingSpeciesRepository() {
			super(SpeciesRepository.class);
		}

		void setSpecies(TaxaSpecies species) {
			this.species = species;
		}

		void setNoDataSpecies(TaxaSpecies noDataSpecies) {
			this.noDataSpecies = noDataSpecies;
		}

		void reset() {
			species = null;
			noDataSpecies = null;
		}

		@Override
		Object handle(String methodName, Object[] args) {
			if ("findBySpeciesNameAndGenusGenusNameAndAuthorAuthorName".equals(methodName)) {
				return species;
			}
			if ("getBugsNoDataSpecies".equals(methodName)) {
				return noDataSpecies;
			}
			throw new UnsupportedOperationException(methodName);
		}
	}
}