package se.sead.bugsimport.rdb;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.locations.country.CountryImportTraceHelper;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.rdb.bugsmodel.BugsRDB;
import se.sead.bugsimport.rdb.converter.RdbUpdater;
import se.sead.bugsimport.rdb.search.RdbSearch;
import se.sead.bugsimport.rdb.seadmodel.Rdb;
import se.sead.bugsimport.rdbcodes.converter.BugsRdbCodeTraceHelper;
import se.sead.bugsimport.rdbcodes.seadmodel.RdbCode;
import se.sead.bugsimport.rdbsystems.seadmodel.RdbSystem;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.model.TestLocation;
import se.sead.model.TestRdb;
import se.sead.model.TestRdbCode;
import se.sead.model.TestRdbSystem;
import se.sead.model.TestTaxaSpecies;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.repositories.TaxonomicOrderRepository;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RdbBugsRowConverterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;
	private RecordingRdbSearch traceSearch;
	private RecordingRdbSearch historyGuardSearch;
	private RecordingRdbSearch repositoryLookupSearch;
	private RdbBugsRowConverter rowConverter;

	@Before
	public void setup() throws Exception {
		fixtureLoader = new PolicyFixtureLoader();
		traceSearch = new RecordingRdbSearch("trace_lookup");
		historyGuardSearch = new RecordingRdbSearch("updated_since_last_import_guard");
		repositoryLookupSearch = new RecordingRdbSearch("repository_lookup");

		RdbUpdater updater = new RdbUpdater();
		Location country = TestLocation.create(31, "Fixture Country", null);
		RdbSystem system = TestRdbSystem.create(5, "System 5", "v1", 2020, (short) 1, null, null);
		RdbCode rdbCode = TestRdbCode.create(41, "A", "Fixture code", system);
		TaxaSpecies species = TestTaxaSpecies.create(51, "fixture species", null, null);

		injectField(updater, "countryTraceHelper", new RecordingCountryImportTraceHelper(country));
		injectField(updater, "codeTraceHelper", new RecordingBugsRdbCodeTraceHelper(rdbCode));
		injectField(updater, "taxonomicOrderRepository", createTaxonomicOrderRepository(species));

		rowConverter = new RdbBugsRowConverter();
		injectField(rowConverter, "rdbUpdater", updater);
		injectField(rowConverter, "rdbSearchStrategies", Arrays.<RdbSearch>asList(traceSearch, historyGuardSearch, repositoryLookupSearch));
	}

	@Test
	public void traceHitFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("trace_hit_updates_existing_rdb");
	}

	@Test
	public void historyGuardFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("history_conflict_guard_returns_error_carrier");
	}

	@Test
	public void repositoryLookupFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("repository_lookup_updates_existing_rdb");
	}

	@Test
	public void createNewFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_new_rdb_when_no_match_exists");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("rdb.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		configureSearches(sourceRow, policyContext, state, scenarioName);
		Rdb result = rowConverter.convertForDataRow(createBugsRdb(sourceRow, scenarioName));

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("reconciliation_path"), scenarioName + ".expects.reconciliation_path"), actualPath(result));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("reconciliation_result"), scenarioName + ".expects.reconciliation_result"), actualReconciliationResult(result));
	}

	private void configureSearches(Map<String, Object> sourceRow, Map<String, Object> policyContext, Map<String, Object> state, String scenarioName) {
		Map<String, Object> stepHits = fixtureLoader.mapValue(policyContext.get("step_hits"), scenarioName + ".policy_context.step_hits");
		traceSearch.reset();
		historyGuardSearch.reset();
		repositoryLookupSearch.reset();
		if (Boolean.TRUE.equals(stepHits.get("trace_lookup"))) {
			traceSearch.setMatch(createMatchedRdb(sourceRow, state, scenarioName, false));
		}
		if (Boolean.TRUE.equals(stepHits.get("updated_since_last_import_guard"))) {
			historyGuardSearch.setMatch(createMatchedRdb(sourceRow, state, scenarioName, true));
		}
		if (Boolean.TRUE.equals(stepHits.get("repository_lookup"))) {
			repositoryLookupSearch.setMatch(createMatchedRdb(sourceRow, state, scenarioName, false));
		}
	}

	private Rdb createMatchedRdb(Map<String, Object> sourceRow, Map<String, Object> state, String scenarioName, boolean guardError) {
		Location country = TestLocation.create(61, fixtureLoader.stringValue(sourceRow.get("CountryCode"), scenarioName + ".source_rows[0].CountryCode"), null);
		TaxaSpecies species = TestTaxaSpecies.create(71, "species " + doubleValue(sourceRow.get("CODE"), scenarioName + ".source_rows[0].CODE"), null, null);
		RdbSystem system = TestRdbSystem.create(5, "System 5", "v1", 2020, (short) 1, null, null);
		RdbCode code = TestRdbCode.create(81, "A", "Fixture code", system);
		Rdb rdb = TestRdb.create(
				state != null && state.containsKey("existing_row_id") ? fixtureLoader.integerValue(state.get("existing_row_id"), scenarioName + ".policy_context.state.existing_row_id") : null,
				species,
				country,
				code
		);
		if (guardError && state != null && state.containsKey("guard_error_message")) {
			rdb.addError(fixtureLoader.stringValue(state.get("guard_error_message"), scenarioName + ".policy_context.state.guard_error_message"));
		}
		if (!guardError && state != null && state.containsKey("existing_error_message")) {
			rdb.addError(fixtureLoader.stringValue(state.get("existing_error_message"), scenarioName + ".policy_context.state.existing_error_message"));
		}
		return rdb;
	}

	private BugsRDB createBugsRdb(Map<String, Object> sourceRow, String scenarioName) {
		BugsRDB bugs = new BugsRDB();
		bugs.setCode(doubleValue(sourceRow.get("CODE"), scenarioName + ".source_rows[0].CODE"));
		bugs.setCountryCode(fixtureLoader.stringValue(sourceRow.get("CountryCode"), scenarioName + ".source_rows[0].CountryCode"));
		bugs.setRdbCode(fixtureLoader.integerValue(sourceRow.get("RDBCode"), scenarioName + ".source_rows[0].RDBCode"));
		return bugs;
	}

	private Double doubleValue(Object value, String context) {
		if (!(value instanceof Number)) {
			throw new IllegalArgumentException(context + " must be numeric");
		}
		return ((Number) value).doubleValue();
	}

	private List<String> actualPath(Rdb result) {
		List<String> path = new ArrayList<String>();
		path.addAll(traceSearch.getRecordedPath());
		path.addAll(historyGuardSearch.getRecordedPath());
		path.addAll(repositoryLookupSearch.getRecordedPath());
		if (matchedRuleName() == null && result.getId() == null && result.isErrorFree()) {
			path.add("create_new");
		}
		return path;
	}

	private Map<String, Object> actualReconciliationResult(Rdb result) {
		Map<String, Object> reconciliationResult = new LinkedHashMap<String, Object>();
		String matchedRuleName = matchedRuleName();
		if (!result.isErrorFree()) {
			if ("updated_since_last_import_guard".equals(matchedRuleName) && result.getId() == null) {
				reconciliationResult.put("result_kind", "return_guard_error");
			} else {
				reconciliationResult.put("result_kind", "return_existing_error");
			}
			reconciliationResult.put("source", matchedRuleName);
			Map<String, Object> issue = new LinkedHashMap<String, Object>();
			issue.put("severity", "error");
			issue.put("message", result.getErrorMessages().get(0));
			reconciliationResult.put("issue", issue);
		} else if (result.getId() == null) {
			reconciliationResult.put("result_kind", "insert_new");
			reconciliationResult.put("source", "create_new");
		} else {
			reconciliationResult.put("result_kind", "update_existing");
			reconciliationResult.put("source", matchedRuleName);
		}
		reconciliationResult.put("row_id", result.getId());
		reconciliationResult.put("rdb_code", traceSearch.getCurrentSourceRdbCode());
		return reconciliationResult;
	}

	private String matchedRuleName() {
		if (traceSearch.didMatch()) {
			return "trace_lookup";
		}
		if (historyGuardSearch.didMatch()) {
			return "updated_since_last_import_guard";
		}
		if (repositoryLookupSearch.didMatch()) {
			return "repository_lookup";
		}
		return null;
	}

	private void injectField(Object target, String fieldName, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

	private TaxonomicOrderRepository createTaxonomicOrderRepository(final TaxaSpecies species) {
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
					return null;
				}
		);
	}

	private static class RecordingRdbSearch implements RdbSearch {

		private final String ruleName;
		private Rdb match;
		private final List<String> recordedPath = new ArrayList<String>();
		private Integer currentSourceRdbCode;
		private boolean didMatch;

		RecordingRdbSearch(String ruleName) {
			this.ruleName = ruleName;
			this.match = NO_RDB_FOUND;
		}

		void setMatch(Rdb match) {
			this.match = match;
		}

		void reset() {
			recordedPath.clear();
			match = NO_RDB_FOUND;
			currentSourceRdbCode = null;
			didMatch = false;
		}

		List<String> getRecordedPath() {
			return recordedPath;
		}

		Integer getCurrentSourceRdbCode() {
			return currentSourceRdbCode;
		}

		boolean didMatch() {
			return didMatch;
		}

		@Override
		public Rdb findFor(BugsRDB rdb) {
			currentSourceRdbCode = rdb.getRdbCode();
			recordedPath.add(ruleName);
			didMatch = match != NO_RDB_FOUND;
			return match;
		}
	}

	private static class RecordingCountryImportTraceHelper extends CountryImportTraceHelper {

		private final Location country;

		RecordingCountryImportTraceHelper(Location country) {
			super(null);
			this.country = country;
		}

		@Override
		public Location getFromLastTrace(String bugsIdentifier) {
			return country;
		}
	}

	private static class RecordingBugsRdbCodeTraceHelper extends BugsRdbCodeTraceHelper {

		private final RdbCode rdbCode;

		RecordingBugsRdbCodeTraceHelper(RdbCode rdbCode) {
			super(null);
			this.rdbCode = rdbCode;
		}

		@Override
		public RdbCode getFromLastTrace(Integer bugsRdbCodeId) {
			return rdbCode;
		}
	}
}