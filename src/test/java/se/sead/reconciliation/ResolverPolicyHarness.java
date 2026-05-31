package se.sead.reconciliation;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ResolverPolicyHarness {

	private static final Path POLICY_DIRECTORY = Paths.get("doc", "reconciliation_policies");

	public ResolverResult execute(String policyName, String resolverName, Map<String, Object> args, Map<String, Boolean> stepHits) throws IOException {
		Map<String, Object> policy = loadPolicy(policyName);
		List<Object> resolvers = listValue(policy.get("resolvers"), policyName + ".resolvers");
		for (Object resolverValue : resolvers) {
			Map<String, Object> resolver = mapValue(resolverValue, resolverName);
			if (resolverName.equals(stringValue(resolver.get("name"), resolverName + ".name"))) {
				return executeResolver(resolver, args, stepHits);
			}
		}
		throw new IllegalArgumentException("No resolver named '" + resolverName + "' in policy '" + policyName + "'");
	}

	private ResolverResult executeResolver(Map<String, Object> resolver, Map<String, Object> args, Map<String, Boolean> stepHits) {
		ResolverResult result = new ResolverResult();
		Map<String, Object> config = defaultConfig(resolver);
		List<Object> steps = listValue(resolver.get("steps"), "resolver.steps");
		for (Object stepValue : steps) {
			Map<String, Object> step = mapValue(stepValue, "resolver.step");
			String stepName = stringValue(step.get("name"), "resolver.step.name");
			if (step.containsKey("when") && !matchesWhen(stringValue(step.get("when"), stepName + ".when"), args, config)) {
				continue;
			}
			result.path.add(stepName);
			String action = stringValue(step.get("action"), stepName + ".action");
			if ("emit_issue".equals(action)) {
				Map<String, Object> emit = mapValue(step.get("emit"), stepName + ".emit");
				result.emitCodes.add(stringValue(emit.get("code"), stepName + ".emit.code"));
				result.resolverResult = createResolverResult(step, stepName, action, args, config);
				return result;
			}
			if (Boolean.TRUE.equals(stepHits.get(stepName))) {
				result.resolverResult = createResolverResult(step, stepName, action, args, config);
				return result;
			}
		}
		return result;
	}

	private Map<String, Object> createResolverResult(Map<String, Object> step, String stepName, String action, Map<String, Object> args, Map<String, Object> config) {
		Map<String, Object> resolverResult = new LinkedHashMap<String, Object>();
		resolverResult.put("result_kind", stringValue(step.get("return"), stepName + ".return"));
		resolverResult.put("source", stepName);
		if ("emit_issue".equals(action)) {
			Map<String, Object> emit = mapValue(step.get("emit"), stepName + ".emit");
			Map<String, Object> issue = new LinkedHashMap<String, Object>();
			issue.put("severity", stringValue(emit.get("severity"), stepName + ".emit.severity"));
			issue.put("code", stringValue(emit.get("code"), stepName + ".emit.code"));
			issue.put("message", stringValue(emit.get("message"), stepName + ".emit.message"));
			resolverResult.put("issue", issue);
			return resolverResult;
		}
		if ("trace_lookup".equals(action)) {
			resolverResult.put("lab_id", args.get("lab_id"));
			return resolverResult;
		}
		Map<String, Object> bind = mapValue(step.get("bind"), stepName + ".bind");
		for (Map.Entry<String, Object> entry : bind.entrySet()) {
			resolverResult.put(entry.getKey(), resolveBindValue(entry.getValue(), args, config));
		}
		return resolverResult;
	}

	private Object resolveBindValue(Object value, Map<String, Object> args, Map<String, Object> config) {
		String bindValue = stringValue(value, "resolver.bind");
		if (bindValue.contains(" + suffix(")) {
			int separatorIndex = bindValue.indexOf(" + suffix(");
			String prefixArg = bindValue.substring(0, separatorIndex).trim();
			String suffixArg = bindValue.substring(separatorIndex + " + suffix(".length(), bindValue.length() - 1).trim();
			Object prefixValue = args.get(prefixArg);
			Object suffixValue = args.get(suffixArg);
			return String.valueOf(prefixValue) + suffixForPeriodYearsType(suffixValue == null ? null : String.valueOf(suffixValue));
		}
		if (bindValue.startsWith("config.")) {
			return config.get(bindValue.substring("config.".length()));
		}
		return args.get(bindValue);
	}

	private boolean matchesWhen(String whenExpression, Map<String, Object> args, Map<String, Object> config) {
		Object labIdValue = args.get("lab_id");
		String labId = labIdValue == null ? null : String.valueOf(labIdValue);
		if (whenExpression.contains("lab_id is None") && whenExpression.contains("lab_id.strip() == ''") && whenExpression.contains("config.unknown_bugs_lab_identifier")) {
			return labId == null || labId.trim().isEmpty() || labId.equals(String.valueOf(config.get("unknown_bugs_lab_identifier")));
		}
		Object datingMethodValue = args.get("dating_method");
		String datingMethod = datingMethodValue == null ? null : String.valueOf(datingMethodValue);
		if (whenExpression.contains("dating_method is None") && whenExpression.contains("dating_method.strip() == ''")) {
			return datingMethod == null || datingMethod.trim().isEmpty();
		}
		Object countryNameValue = args.get("country_name");
		String countryName = countryNameValue == null ? null : String.valueOf(countryNameValue);
		if (whenExpression.contains("country_name is None") && whenExpression.contains("country_name.strip() == ''")) {
			return countryName == null || countryName.trim().isEmpty();
		}
		if (whenExpression.contains("country_name == 'Country'")) {
			return "Country".equals(countryName);
		}
		throw new IllegalArgumentException("Unsupported resolver when expression: " + whenExpression);
	}

	private String suffixForPeriodYearsType(String periodYearsType) {
		if ("Calendar".equals(periodYearsType)) {
			return "Cal";
		}
		if ("C14".equals(periodYearsType)) {
			return "C14";
		}
		if ("Radiometric".equals(periodYearsType)) {
			return "Radio";
		}
		return "";
	}

	private Map<String, Object> defaultConfig(Map<String, Object> resolver) {
		Map<String, Object> config = new HashMap<String, Object>();
		if (resolver.containsKey("config")) {
			List<Object> configItems = listValue(resolver.get("config"), "resolver.config");
			for (Object configValue : configItems) {
				Map<String, Object> configItem = mapValue(configValue, "resolver.config.item");
				config.put(stringValue(configItem.get("name"), "resolver.config.name"), configItem.get("default"));
			}
		}
		return config;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> loadPolicy(String policyName) throws IOException {
		Yaml yaml = new Yaml();
		Path policyFile = POLICY_DIRECTORY.resolve(policyName + ".policy.yml");
		try (InputStream inputStream = Files.newInputStream(policyFile)) {
			Object loaded = yaml.load(inputStream);
			return mapValue(loaded, policyFile.toString());
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> mapValue(Object value, String context) {
		if (!(value instanceof Map)) {
			throw new IllegalArgumentException(context + " must be a mapping");
		}
		return (Map<String, Object>) value;
	}

	@SuppressWarnings("unchecked")
	private List<Object> listValue(Object value, String context) {
		if (!(value instanceof List)) {
			throw new IllegalArgumentException(context + " must be a list");
		}
		return (List<Object>) value;
	}

	private String stringValue(Object value, String context) {
		if (!(value instanceof String)) {
			throw new IllegalArgumentException(context + " must be a string");
		}
		return (String) value;
	}

	public static class ResolverResult {
		private final List<String> path = new ArrayList<String>();
		private final List<String> emitCodes = new ArrayList<String>();
		private Map<String, Object> resolverResult = new LinkedHashMap<String, Object>();

		public List<String> getPath() {
			return path;
		}

		public List<String> getEmitCodes() {
			return emitCodes;
		}

		public Map<String, Object> getResolverResult() {
			return resolverResult;
		}
	}
}