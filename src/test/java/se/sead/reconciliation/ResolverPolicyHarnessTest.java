package se.sead.reconciliation;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ResolverPolicyHarnessTest {

	private PolicyFixtureLoader fixtureLoader;
	private ResolverPolicyHarness resolverPolicyHarness;

	@Before
	public void setup() {
		fixtureLoader = new PolicyFixtureLoader();
		resolverPolicyHarness = new ResolverPolicyHarness();
	}

	@Test
	public void blankLabResolverScenarioMatchesPolicyPath() throws IOException {
		assertScenarioMatchesPolicy("blank_lab_id_uses_unknown_shortcut");
	}

	@Test
	public void directLookupResolverScenarioMatchesPolicyPath() throws IOException {
		assertScenarioMatchesPolicy("direct_lookup_after_trace_miss");
	}

	@Test
	public void missingLabResolverScenarioMatchesPolicyPath() throws IOException {
		assertScenarioMatchesPolicy("datesradio.fixture.yml", "datesradio", "missing_lab_emits_error");
	}

	@Test
	public void blankDatesPeriodMethodResolverScenarioMatchesPolicyPath() throws IOException {
		assertScenarioMatchesPolicy("datesperiod.fixture.yml", "datesperiod", "blank_method_uses_unknown_default");
	}

	@Test
	public void directDatesPeriodMethodResolverScenarioMatchesPolicyPath() throws IOException {
		assertScenarioMatchesPolicy("datesperiod.fixture.yml", "datesperiod", "direct_method_lookup_uses_abbreviation");
	}

	@Test
	public void computedDatesPeriodMethodResolverScenarioMatchesPolicyPath() throws IOException {
		assertScenarioMatchesPolicy("datesperiod.fixture.yml", "datesperiod", "computed_method_uses_period_year_type_suffix");
	}

	@Test
	public void missingDatesPeriodMethodResolverScenarioMatchesPolicyPath() throws IOException {
		assertScenarioMatchesPolicy("datesperiod.fixture.yml", "datesperiod", "missing_method_emits_error");
	}

	@Test
	public void blankLabCountryResolverScenarioMatchesPolicyPath() throws IOException {
		assertScenarioMatchesPolicy("lab.fixture.yml", "lab", "blank_country_emits_error");
	}

	@Test
	public void placeholderLabCountryResolverScenarioMatchesPolicyPath() throws IOException {
		assertScenarioMatchesPolicy("lab.fixture.yml", "lab", "placeholder_country_is_ignored");
	}

	@Test
	public void directLabCountryResolverScenarioMatchesPolicyPath() throws IOException {
		assertScenarioMatchesPolicy("lab.fixture.yml", "lab", "country_lookup_returns_entity");
	}

	@Test
	public void missingLabCountryResolverScenarioMatchesPolicyPath() throws IOException {
		assertScenarioMatchesPolicy("lab.fixture.yml", "lab", "missing_country_emits_error");
	}

	private void assertScenarioMatchesPolicy(String fixtureFileName, String policyName, String scenarioName) throws IOException {
		Map<String, Object> fixture = fixtureLoader.loadFixture(fixtureFileName);
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");

		Map<String, Object> args = policyContext.containsKey("args") ? fixtureLoader.mapValue(policyContext.get("args"), scenarioName + ".policy_context.args") : new HashMap<String, Object>();
		Map<String, Object> stepHitsRaw = fixtureLoader.mapValue(policyContext.get("step_hits"), scenarioName + ".policy_context.step_hits");
		Map<String, Boolean> stepHits = new HashMap<String, Boolean>();
		for (Map.Entry<String, Object> entry : stepHitsRaw.entrySet()) {
			stepHits.put(entry.getKey(), (Boolean) entry.getValue());
		}

		ResolverPolicyHarness.ResolverResult result = resolverPolicyHarness.execute(
				policyName,
				fixtureLoader.stringValue(expects.get("resolver"), scenarioName + ".expects.resolver"),
				args,
				stepHits
		);

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("resolver_path"), scenarioName + ".expects.resolver_path"), result.getPath());
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("resolver_result"), scenarioName + ".expects.resolver_result"), result.getResolverResult());
		if (expects.containsKey("emit_codes")) {
			assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("emit_codes"), scenarioName + ".expects.emit_codes"), result.getEmitCodes());
		}
	}

	private void assertScenarioMatchesPolicy(String scenarioName) throws IOException {
		assertScenarioMatchesPolicy("datesradio.fixture.yml", "datesradio", scenarioName);
	}
}