package se.sead.reconciliation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class FixtureExpectationHelper {

	private FixtureExpectationHelper() {
	}

	public static List<String> toStringList(PolicyFixtureLoader fixtureLoader, Object value, String context) {
		List<Object> rawValues = fixtureLoader.listValue(value, context);
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < rawValues.size(); i++) {
			result.add(fixtureLoader.stringValue(rawValues.get(i), context + "[" + i + "]"));
		}
		return result;
	}

	public static List<Integer> toIntegerList(PolicyFixtureLoader fixtureLoader, Object value, String context) {
		List<Object> rawValues = fixtureLoader.listValue(value, context);
		List<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < rawValues.size(); i++) {
			result.add(fixtureLoader.integerValue(rawValues.get(i), context + "[" + i + "]"));
		}
		return result;
	}

	public static Map<String, Object> toNestedMap(PolicyFixtureLoader fixtureLoader, Object value, String context) {
		Map<String, Object> rawMap = fixtureLoader.mapValue(value, context);
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		for (Map.Entry<String, Object> entry : rawMap.entrySet()) {
			if (entry.getValue() instanceof Map) {
				result.put(entry.getKey(), toNestedMap(fixtureLoader, entry.getValue(), context + "." + entry.getKey()));
			} else if (entry.getValue() instanceof Number) {
				result.put(entry.getKey(), ((Number) entry.getValue()).intValue());
			} else {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}

	public static List<Map<String, Object>> toNestedMapList(PolicyFixtureLoader fixtureLoader, Object value, String context) {
		List<Object> rawValues = fixtureLoader.listValue(value, context);
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < rawValues.size(); i++) {
			result.add(toNestedMap(fixtureLoader, rawValues.get(i), context + "[" + i + "]"));
		}
		return result;
	}
}