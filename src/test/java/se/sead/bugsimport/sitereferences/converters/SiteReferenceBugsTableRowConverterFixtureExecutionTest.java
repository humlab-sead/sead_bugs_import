package se.sead.bugsimport.sitereferences.converters;

import org.junit.Before;
import org.junit.Test;
import se.sead.bugsimport.bibliography.seadmodel.Biblio;
import se.sead.bugsimport.site.helper.SiteFromCodeDisallowDeletedSite;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.sitereferences.bugsmodel.BugsSiteRef;
import se.sead.bugsimport.sitereferences.seadmodel.SiteReference;
import se.sead.model.TestBiblio;
import se.sead.model.TestSeadSite;
import se.sead.model.TestSiteReference;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.repositories.BiblioDataRepository;
import se.sead.repositories.SiteReferenceRepository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SiteReferenceBugsTableRowConverterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;
	private RecordingSiteLookup siteLookup;
	private RecordingBiblioRepository biblioRepository;
	private RecordingSiteReferenceRepository siteReferenceRepository;
	private SiteReferenceBugsTableRowConverter converter;

	@Before
	public void setup() throws Exception {
		fixtureLoader = new PolicyFixtureLoader();
		siteLookup = new RecordingSiteLookup();
		biblioRepository = new RecordingBiblioRepository();
		siteReferenceRepository = new RecordingSiteReferenceRepository();
		converter = new SiteReferenceBugsTableRowConverter();
		injectField(converter, "siteHelper", siteLookup);
		injectField(converter, "biblioRepository", biblioRepository);
		injectField(converter, "siteReferenceRepository", siteReferenceRepository);
	}

	@Test
	public void missingSiteFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("missing_site_prerequisite_returns_guard_error");
	}

	@Test
	public void missingReferenceFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("missing_reference_prerequisite_returns_guard_error");
	}

	@Test
	public void missingBibliographyFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("missing_bibliography_prerequisite_returns_guard_error");
	}

	@Test
	public void tupleLookupFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("tuple_lookup_returns_existing_site_reference");
	}

	@Test
	public void createNewFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_new_site_reference_when_tuple_missing");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("sitereferences.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> sourceRow = fixtureLoader.mapValue(fixtureLoader.listValue(scenario.get("source_rows"), scenarioName + ".source_rows").get(0), scenarioName + ".source_rows[0]");
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		configureScenario(sourceRow, policyContext, state, scenarioName);
		SiteReference result = converter.convertForDataRow(createBugsSiteRef(sourceRow, scenarioName));

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("reconciliation_path"), scenarioName + ".expects.reconciliation_path"), actualPath(result));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("reconciliation_result"), scenarioName + ".expects.reconciliation_result"), actualReconciliationResult(result, sourceRow));
	}

	private void configureScenario(Map<String, Object> sourceRow, Map<String, Object> policyContext, Map<String, Object> state, String scenarioName) {
		Map<String, Object> stepHits = fixtureLoader.mapValue(policyContext.get("step_hits"), scenarioName + ".policy_context.step_hits");
		siteLookup.reset();
		biblioRepository.reset();
		siteReferenceRepository.reset();

		String guardErrorMessage = state != null && state.containsKey("guard_error_message")
				? fixtureLoader.stringValue(state.get("guard_error_message"), scenarioName + ".policy_context.state.guard_error_message")
				: null;
		boolean missingSite = guardErrorMessage != null && guardErrorMessage.startsWith("Missing site:");
		SeadSite matchedSite = TestSeadSite.create(601, "Matched site", null, null, null, null, null);
		if (!missingSite) {
			siteLookup.setMatch(matchedSite);
		}
		String referenceValue = fixtureLoader.stringValue(sourceRow.get("Ref"), scenarioName + ".source_rows[0].Ref");
		if (!referenceValue.isEmpty() && (guardErrorMessage == null || !guardErrorMessage.startsWith("No reference found for ref:"))) {
			Biblio reference = TestBiblio.create(701, referenceValue, "Matched", "Reference");
			biblioRepository.setMatch(reference);
			if (Boolean.TRUE.equals(stepHits.get("tuple_lookup")) && state != null && state.containsKey("existing_row_id")) {
				siteReferenceRepository.setExisting(Arrays.asList(TestSiteReference.create(
						fixtureLoader.integerValue(state.get("existing_row_id"), scenarioName + ".policy_context.state.existing_row_id"),
						matchedSite,
						reference
				)));
			}
		}
	}

	private BugsSiteRef createBugsSiteRef(Map<String, Object> sourceRow, String scenarioName) {
		BugsSiteRef bugsSiteRef = new BugsSiteRef();
		bugsSiteRef.setSiteCode(fixtureLoader.stringValue(sourceRow.get("SiteCODE"), scenarioName + ".source_rows[0].SiteCODE"));
		bugsSiteRef.setRef(fixtureLoader.stringValue(sourceRow.get("Ref"), scenarioName + ".source_rows[0].Ref"));
		return bugsSiteRef;
	}

	private List<String> actualPath(SiteReference result) {
		List<String> path = new ArrayList<String>();
		if (!result.isErrorFree()) {
			path.add("resolve_dependencies");
			return path;
		}
		path.addAll(siteReferenceRepository.getRecordedPath());
		if (result.getId() == null) {
			path.add("create_new");
		}
		return path;
	}

	private Map<String, Object> actualReconciliationResult(SiteReference result, Map<String, Object> sourceRow) {
		Map<String, Object> reconciliationResult = new LinkedHashMap<String, Object>();
		if (!result.isErrorFree()) {
			reconciliationResult.put("result_kind", "return_guard_error");
			reconciliationResult.put("persisted_action", "stop_before_write");
			reconciliationResult.put("source", "resolve_dependencies");
			Map<String, Object> issue = new LinkedHashMap<String, Object>();
			issue.put("severity", "error");
			issue.put("message", result.getErrorMessages().get(0));
			reconciliationResult.put("issue", issue);
		} else if (result.getId() == null) {
			reconciliationResult.put("result_kind", "insert_new");
			reconciliationResult.put("source", "create_new");
		} else {
			reconciliationResult.put("result_kind", "update_existing");
			reconciliationResult.put("source", "tuple_lookup");
		}
		reconciliationResult.put("row_id", result.getId());
		reconciliationResult.put("site_code", sourceRow.get("SiteCODE"));
		reconciliationResult.put("bugs_reference", sourceRow.get("Ref"));
		return reconciliationResult;
	}

	private void injectField(Object target, String fieldName, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

	private static class RecordingSiteLookup extends SiteFromCodeDisallowDeletedSite {

		private SeadSite match;

		void setMatch(SeadSite match) {
			this.match = match;
		}

		void reset() {
			match = null;
		}

		@Override
		public SeadSite getSeadSiteFromBugsCode(String bugsSiteCode) {
			return match;
		}
	}

	private static class RecordingBiblioRepository implements BiblioDataRepository {

		private Biblio match;

		void setMatch(Biblio match) {
			this.match = match;
		}

		void reset() {
			match = null;
		}

		@Override
		public Biblio getByBugsReferenceIgnoreCase(String bugsReference) {
			return match;
		}

		@Override
		public List<Biblio> findAll() {
			throw new UnsupportedOperationException();
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

	private static class RecordingSiteReferenceRepository implements SiteReferenceRepository {

		private List<SiteReference> existing = new ArrayList<SiteReference>();
		private final List<String> recordedPath = new ArrayList<String>();

		void setExisting(List<SiteReference> existing) {
			this.existing = existing;
		}

		void reset() {
			existing = new ArrayList<SiteReference>();
			recordedPath.clear();
		}

		List<String> getRecordedPath() {
			return recordedPath;
		}

		@Override
		public SiteReference findBySiteAndReference(SeadSite site, Biblio reference) {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<SiteReference> findAllBySite(SeadSite site) {
			recordedPath.add("tuple_lookup");
			return existing;
		}

		@Override
		public SiteReference findOne(Integer integer) {
			throw new UnsupportedOperationException();
		}

		@Override
		public SiteReference saveOrUpdate(SiteReference entity) {
			throw new UnsupportedOperationException();
		}
	}
}