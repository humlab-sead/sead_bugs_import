package se.sead.reconciliation;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class PolicyFixtureLoader {

	private static final Path FIXTURE_DIRECTORY = Paths.get("doc", "reconciliation_policies", "fixtures");

	public Map<String, Object> loadFixture(String fileName) throws IOException {
		Path fixtureFile = FIXTURE_DIRECTORY.resolve(fileName);
		Yaml yaml = new Yaml();
		try (InputStream inputStream = Files.newInputStream(fixtureFile)) {
			return mapValue(yaml.load(inputStream), fixtureFile.toString());
		}
	}

	public Map<String, Object> loadFixtureUnchecked(String fileName) {
		try {
			return loadFixture(fileName);
		} catch (IOException exception) {
			throw new IllegalStateException("Could not load fixture '" + fileName + "'", exception);
		}
	}

	public Map<String, Object> findScenario(Map<String, Object> fixture, String scenarioName) {
		List<Object> scenarios = listValue(fixture.get("scenarios"), "scenarios");
		for (Object scenarioValue : scenarios) {
			Map<String, Object> scenario = mapValue(scenarioValue, scenarioName);
			if (scenarioName.equals(stringValue(scenario.get("name"), scenarioName + ".name"))) {
				return scenario;
			}
		}
		throw new IllegalArgumentException("No scenario named '" + scenarioName + "'");
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> mapValue(Object value, String context) {
		if (!(value instanceof Map)) {
			throw new IllegalArgumentException(context + " must be a mapping");
		}
		return (Map<String, Object>) value;
	}

	@SuppressWarnings("unchecked")
	public List<Object> listValue(Object value, String context) {
		if (!(value instanceof List)) {
			throw new IllegalArgumentException(context + " must be a list");
		}
		return (List<Object>) value;
	}

	public String stringValue(Object value, String context) {
		if (!(value instanceof String)) {
			throw new IllegalArgumentException(context + " must be a string");
		}
		return (String) value;
	}

	public Integer integerValue(Object value, String context) {
		if (!(value instanceof Number)) {
			throw new IllegalArgumentException(context + " must be numeric");
		}
		return ((Number) value).intValue();
	}

	public Boolean booleanValue(Object value, String context) {
		if (!(value instanceof Boolean)) {
			throw new IllegalArgumentException(context + " must be boolean");
		}
		return (Boolean) value;
	}
}