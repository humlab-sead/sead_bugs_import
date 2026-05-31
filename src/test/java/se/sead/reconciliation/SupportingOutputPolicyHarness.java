package se.sead.reconciliation;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SupportingOutputPolicyHarness {

	private static final Path POLICY_DIRECTORY = Paths.get("doc", "reconciliation_policies");

	public SupportingOutputResult execute(String policyName, Map<String, Object> args, Map<String, Object> state, Map<String, Object> sourceRow) throws IOException {
		Map<String, Object> policy = loadPolicy(policyName);
		List<Object> relatedOutputs = listValue(policy.get("related_outputs"), policyName + ".related_outputs");
		SupportingOutputResult result = new SupportingOutputResult();
		String outputName = stringOrNull(args, "output_name");
		for (int i = 0; i < relatedOutputs.size(); i++) {
			Map<String, Object> relatedOutput = mapValue(relatedOutputs.get(i), policyName + ".related_outputs[" + i + "]");
			String name = stringValue(relatedOutput.get("name"), policyName + ".related_outputs[" + i + "].name");
			if (outputName != null && !outputName.equals(name)) {
				continue;
			}
			if ("sample_dimensions".equals(name)) {
				result.relatedOutputs.add(name);
				result.graphResult.put("sample_dimensions", sampleDimensionsResult(policyName, args, state, sourceRow));
				return result;
			}
			if ("contacts".equals(name)) {
				result.relatedOutputs.add(name);
				result.graphResult.put("contacts", contactsResult(policyName, args, state));
				return result;
			}
			if ("fossil".equals(policyName) && "dataset".equals(name)) {
				result.relatedOutputs.add(name);
				result.graphResult.put("dataset", fossilDatasetResult(state));
				return result;
			}
			if ("dataset".equals(name)) {
				result.relatedOutputs.add(name);
				result.graphResult.put("dataset", datasetResult(policyName, args, state, sourceRow));
				return result;
			}
			if ("fossil".equals(policyName) && "analysis_entity".equals(name)) {
				result.relatedOutputs.add(name);
				result.graphResult.put("analysis_entity", fossilAnalysisEntityResult(state));
				return result;
			}
			if ("analysis_entity".equals(name)) {
				result.relatedOutputs.add(name);
				result.graphResult.put("analysis_entity", analysisEntityResult(policyName, args, state, sourceRow));
				return result;
			}
			if ("relative_age".equals(name)) {
				result.relatedOutputs.add(name);
				result.graphResult.put("relative_age", relativeAgeResult(policyName, state, sourceRow));
				return result;
			}
			if ("species".equals(policyName) && "taxa_family".equals(name)) {
				result.relatedOutputs.add(name);
				result.graphResult.put("taxa_family", taxaFamilyResult(args, state, sourceRow));
				return result;
			}
			if ("species".equals(policyName) && "taxa_genus".equals(name)) {
				result.relatedOutputs.add(name);
				result.graphResult.put("taxa_genus", taxaGenusResult(state, sourceRow));
				return result;
			}
			if ("species".equals(policyName) && "taxa_author".equals(name)) {
				result.relatedOutputs.add(name);
				result.graphResult.put("taxa_author", taxaAuthorResult(state, sourceRow));
				return result;
			}
			if ("species".equals(policyName) && "taxa_species".equals(name)) {
				result.relatedOutputs.add(name);
				result.graphResult.put("taxa_species", taxaSpeciesResult(state, sourceRow));
				return result;
			}
		}
		throw new IllegalArgumentException("Policy '" + policyName + "' must define supporting output '" + (outputName == null ? "dataset" : outputName) + "'");
	}

	private Map<String, Object> taxaFamilyResult(Map<String, Object> args, Map<String, Object> state, Map<String, Object> sourceRow) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		Integer familyId = integerOrNull(state, "existing_family_id");
		result.put("result_kind", familyId == null ? "insert_new" : "return_existing");
		result.put("supporting_action", familyId == null ? "create" : "reuse");
		result.put("family_id", familyId);
		result.put("family_name", stringValue(sourceRow.get("FAMILY"), "FAMILY"));
		result.put("order_id", integerValue(args.get("import_order_id"), "import_order_id"));
		return result;
	}

	private Map<String, Object> taxaGenusResult(Map<String, Object> state, Map<String, Object> sourceRow) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		Integer genusId = integerOrNull(state, "existing_genus_id");
		result.put("result_kind", genusId == null ? "insert_new" : "return_existing");
		result.put("supporting_action", genusId == null ? "create" : "reuse");
		result.put("genus_id", genusId);
		result.put("genus_name", stringValue(sourceRow.get("GENUS"), "GENUS"));
		result.put("family_id", integerOrNull(state, "existing_family_id"));
		return result;
	}

	private Map<String, Object> taxaAuthorResult(Map<String, Object> state, Map<String, Object> sourceRow) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		Integer authorId = integerOrNull(state, "existing_author_id");
		result.put("result_kind", authorId == null ? "insert_new" : "return_existing");
		result.put("supporting_action", authorId == null ? "create" : "reuse");
		result.put("author_id", authorId);
		result.put("author_name", stringOrNull(sourceRow, "AUTHORITY"));
		return result;
	}

	private Map<String, Object> taxaSpeciesResult(Map<String, Object> state, Map<String, Object> sourceRow) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		boolean noDataSpecies = numericEquals(doubleOrNull(sourceRow, "CODE"), Double.valueOf("9999.0000001")) && integerOrNull(state, "no_data_species_id") != null;
		Integer speciesId = noDataSpecies ? integerOrNull(state, "no_data_species_id") : integerOrNull(state, "existing_species_id");
		Integer genusId = noDataSpecies ? integerOrNull(state, "no_data_genus_id") : integerOrNull(state, "existing_genus_id");
		Integer authorId = noDataSpecies ? integerOrNull(state, "no_data_author_id") : integerOrNull(state, "existing_author_id");
		result.put("result_kind", speciesId == null ? "insert_new" : "return_existing");
		result.put("supporting_action", speciesId == null ? "create" : "reuse");
		result.put("species_id", speciesId);
		result.put("species", stringValue(sourceRow.get("SPECIES"), "SPECIES"));
		result.put("genus_id", genusId);
		result.put("author_id", authorId);
		return result;
	}

	private Map<String, Object> contactsResult(String policyName, Map<String, Object> args, Map<String, Object> state) {
		if (!"datasetcontacts".equals(policyName)) {
			throw new IllegalArgumentException("Policy '" + policyName + "' does not support contacts in this harness");
		}
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		Map<String, Map<String, Object>> cacheIndex = new LinkedHashMap<String, Map<String, Object>>();
		seedContactIndex(cacheIndex, mapOrNull(state, "cached_contacts"));
		Map<String, Map<String, Object>> repositoryIndex = createContactIndex(mapOrNull(state, "repository_contacts"));

		List<Map<String, Object>> generatedContacts = new ArrayList<Map<String, Object>>();
		generatedContacts.addAll(parseIdentifiedBy(stringOrNull(args, "identified_by")));
		generatedContacts.addAll(parseSpecimenRepository(stringOrNull(args, "specimen_repository")));

		for (int i = 0; i < generatedContacts.size(); i++) {
			Map<String, Object> generatedContact = generatedContacts.get(i);
			String contactKey = contactKey(stringOrNull(generatedContact, "first_name"), stringOrNull(generatedContact, "last_name"));
			Map<String, Object> resolvedContact = cacheIndex.get(contactKey);
			String source = "cache";
			if (resolvedContact == null) {
				resolvedContact = repositoryIndex.get(contactKey);
				source = resolvedContact == null ? "generated_new" : "repository";
				if (resolvedContact == null) {
					resolvedContact = generatedContact;
				}
				cacheIndex.put(contactKey, resolvedContact);
			}

			Map<String, Object> contactResult = new LinkedHashMap<String, Object>();
			contactResult.put("result_kind", "generated_new".equals(source) ? "insert_new" : "return_existing");
			contactResult.put("supporting_action", "generated_new".equals(source) ? "create" : "reuse");
			contactResult.put("source", source);
			contactResult.put("contact_id", integerOrNull(resolvedContact, "contact_id"));
			contactResult.put("first_name", stringOrNull(resolvedContact, "first_name"));
			contactResult.put("last_name", stringOrNull(resolvedContact, "last_name"));
			result.put("contact_" + Integer.valueOf(i + 1), contactResult);
		}
		return result;
	}

	private Map<String, Object> sampleDimensionsResult(String policyName, Map<String, Object> args, Map<String, Object> state, Map<String, Object> sourceRow) {
		if (!"sample".equals(policyName)) {
			throw new IllegalArgumentException("Policy '" + policyName + "' does not support sample_dimensions in this harness");
		}
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		Integer methodId = integerValue(args.get("method_id"), "method_id");
		Integer upperDimensionId = integerValue(args.get("upper_dimension_id"), "upper_dimension_id");
		Integer lowerDimensionId = integerValue(args.get("lower_dimension_id"), "lower_dimension_id");

		Map<String, Object> upper = sampleDimensionResult(
				integerOrNull(state, "existing_upper_sample_dimension_id"),
				doubleOrNull(state, "existing_upper_dimension_value"),
				doubleOrNull(sourceRow, "ZorDepthTop"),
				upperDimensionId,
				methodId
		);
		if (upper != null) {
			result.put("upper", upper);
		}

		Map<String, Object> lower = sampleDimensionResult(
				integerOrNull(state, "existing_lower_sample_dimension_id"),
				doubleOrNull(state, "existing_lower_dimension_value"),
				doubleOrNull(sourceRow, "ZorDepthBot"),
				lowerDimensionId,
				methodId
		);
		if (lower != null) {
			result.put("lower", lower);
		}
		return result;
	}

	private Map<String, Object> sampleDimensionResult(Integer existingId, Double existingValue, Double incomingValue, Integer dimensionId, Integer methodId) {
		if (existingId == null && incomingValue == null) {
			return null;
		}
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("result_kind", sampleDimensionResultKind(existingId, existingValue, incomingValue));
		result.put("supporting_action", sampleDimensionAction(existingId, existingValue, incomingValue));
		result.put("sample_dimension_id", existingId);
		result.put("dimension_id", dimensionId);
		result.put("method_id", methodId);
		result.put("dimension_value", normalizeNumericValue(incomingValue));
		result.put("marked_for_deletion", Boolean.valueOf(existingId != null && incomingValue == null && existingValue != null));
		return result;
	}

	private String sampleDimensionResultKind(Integer existingId, Double existingValue, Double incomingValue) {
		if (existingId == null) {
			return "insert_new";
		}
		if (incomingValue == null && existingValue != null) {
			return "delete_existing";
		}
		if (numericEquals(existingValue, incomingValue)) {
			return "keep_existing";
		}
		return "update_existing";
	}

	private String sampleDimensionAction(Integer existingId, Double existingValue, Double incomingValue) {
		if (existingId == null) {
			return "create";
		}
		if (incomingValue == null && existingValue != null) {
			return "delete";
		}
		if (numericEquals(existingValue, incomingValue)) {
			return "keep";
		}
		return "update";
	}

	private Map<String, Object> relativeAgeResult(String policyName, Map<String, Object> state, Map<String, Object> sourceRow) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		Integer relativeAgeId = integerOrNull(state, "existing_relative_age_id");
		result.put("relative_age_id", relativeAgeId);
		result.put("supporting_action", relativeAgeId == null ? "create" : "reuse");
		if ("datescalendar".equals(policyName)) {
			String abbreviation = "CAL_" + integerValue(sourceRow.get("Date"), "Date") + "_" + stringValue(sourceRow.get("BCADBP"), "BCADBP");
			result.put("abbreviation", abbreviation);
			result.put("name", abbreviation);
			result.put("type_name", "Calendar date");
			return result;
		}
		throw new IllegalArgumentException("Policy '" + policyName + "' does not support relative_age in this harness");
	}

	private Map<String, Object> fossilDatasetResult(Map<String, Object> state) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		boolean allowDatasetUpdates = state == null || !state.containsKey("allow_dataset_updates") || Boolean.TRUE.equals(state.get("allow_dataset_updates"));
		result.put("dataset_id", allowDatasetUpdates ? Integer.valueOf(20) : null);
		result.put("updated_dataset_id", allowDatasetUpdates ? null : Integer.valueOf(20));
		result.put("supporting_action", allowDatasetUpdates ? "reuse" : "create");
		return result;
	}

	private Map<String, Object> fossilAnalysisEntityResult(Map<String, Object> state) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		Integer analysisEntityId = integerOrNull(state, "existing_analysis_entity_id");
		if (analysisEntityId != null) {
			result.put("analysis_entity_id", analysisEntityId);
		}
		boolean allowDatasetUpdates = state == null || !state.containsKey("allow_dataset_updates") || Boolean.TRUE.equals(state.get("allow_dataset_updates"));
		result.put("supporting_action", analysisEntityId == null ? "create" : "reuse");
		result.put("physical_sample_id", integerOrDefault(state, "physical_sample_id", Integer.valueOf(10)));
		result.put("dataset_id", allowDatasetUpdates ? Integer.valueOf(20) : null);
		return result;
	}

	private Map<String, Object> analysisEntityResult(String policyName, Map<String, Object> args, Map<String, Object> state, Map<String, Object> sourceRow) {
		Map<String, Object> dataset = datasetResult(policyName, args, state, sourceRow);
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		Integer analysisEntityId = integerOrNull(state, "existing_analysis_entity_id");
		Integer physicalSampleId = integerValue(args.get("physical_sample_id"), "physical_sample_id");
		Integer datasetId = integerObject(dataset.get("dataset_id"));
		result.put("analysis_entity_id", analysisEntityId);
		result.put("physical_sample_id", physicalSampleId);
		result.put("dataset_id", datasetId);
		result.put("supporting_action", analysisEntityAction(state, analysisEntityId, physicalSampleId, datasetId));
		return result;
	}

	private Map<String, Object> datasetResult(String policyName, Map<String, Object> args, Map<String, Object> state, Map<String, Object> sourceRow) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		Integer datasetId = state != null && state.containsKey("existing_dataset_id") ? integerValue(state.get("existing_dataset_id"), "existing_dataset_id") : null;
		String datasetName = datasetName(policyName, sourceRow);
		Integer dataTypeId = integerValue(args.get("data_type_id"), "data_type_id");
		String methodAbbreviation = stringValue(args.get("method_abbreviation"), "method_abbreviation");
		Integer masterSetId = integerValue(args.get("master_set_id"), "master_set_id");
		boolean updated = "datesradio".equals(policyName) ? false : datasetId == null
				|| !datasetName.equals(stringOrNull(state, "existing_dataset_name"))
				|| dataTypeId.intValue() != integerOrDefault(state, "existing_data_type_id", dataTypeId).intValue()
				|| !methodAbbreviation.equals(stringOrNull(state, "existing_method_abbreviation"))
				|| masterSetId.intValue() != integerOrDefault(state, "existing_master_set_id", masterSetId).intValue();

		result.put("dataset_id", datasetId);
		result.put("dataset_name", datasetName);
		result.put("data_type_id", dataTypeId);
		result.put("method_abbreviation", methodAbbreviation);
		result.put("master_set_id", masterSetId);
		result.put("updated", updated);
		result.put("supporting_action", datasetAction(datasetId, updated));
		return result;
	}

	private String datasetAction(Integer datasetId, boolean updated) {
		if (datasetId == null) {
			return "create";
		}
		return updated ? "update" : "keep";
	}

	private String analysisEntityAction(Map<String, Object> state, Integer analysisEntityId, Integer physicalSampleId, Integer datasetId) {
		if (analysisEntityId == null) {
			return "create";
		}
		Integer existingPhysicalSampleId = integerOrNull(state, "existing_physical_sample_id");
		Integer existingDatasetId = integerOrNull(state, "existing_dataset_id");
		if (!sameInteger(existingPhysicalSampleId, physicalSampleId) || !sameInteger(existingDatasetId, datasetId)) {
			return "update";
		}
		return "keep";
	}

	private Integer integerObject(Object value) {
		if (value == null) {
			return null;
		}
		return integerValue(value, "value");
	}

	private boolean sameInteger(Integer left, Integer right) {
		if (left == null) {
			return right == null;
		}
		return left.equals(right);
	}

	private String datasetName(String policyName, Map<String, Object> sourceRow) {
		if ("datesradio".equals(policyName)) {
			return stringValue(sourceRow.get("DateCODE"), "DateCODE");
		}
		if ("datescalendar".equals(policyName)) {
			return stringValue(sourceRow.get("CalendarCODE"), "CalendarCODE");
		}
		return stringValue(sourceRow.get("PeriodDateCODE"), "PeriodDateCODE");
	}

	private String stringOrNull(Map<String, Object> state, String key) {
		if (state == null || !state.containsKey(key) || state.get(key) == null) {
			return null;
		}
		return stringValue(state.get(key), key);
	}

	private Map<String, Object> mapOrNull(Map<String, Object> state, String key) {
		if (state == null || !state.containsKey(key) || state.get(key) == null) {
			return null;
		}
		return mapValue(state.get(key), key);
	}

	private void seedContactIndex(Map<String, Map<String, Object>> index, Map<String, Object> contacts) {
		Map<String, Map<String, Object>> contactIndex = createContactIndex(contacts);
		for (Map.Entry<String, Map<String, Object>> entry : contactIndex.entrySet()) {
			index.put(entry.getKey(), entry.getValue());
		}
	}

	private Map<String, Map<String, Object>> createContactIndex(Map<String, Object> contacts) {
		Map<String, Map<String, Object>> index = new LinkedHashMap<String, Map<String, Object>>();
		if (contacts == null) {
			return index;
		}
		for (Map.Entry<String, Object> entry : contacts.entrySet()) {
			Map<String, Object> contact = mapValue(entry.getValue(), entry.getKey());
			index.put(contactKey(stringOrNull(contact, "first_name"), stringOrNull(contact, "last_name")), contact);
		}
		return index;
	}

	private List<Map<String, Object>> parseIdentifiedBy(String identifiedBy) {
		List<Map<String, Object>> contacts = new ArrayList<Map<String, Object>>();
		if (identifiedBy == null || identifiedBy.isEmpty()) {
			return contacts;
		}
		String separator = identifiedBy.contains(",") ? "," : identifiedBy.contains(";") ? ";" : identifiedBy.contains("&") ? "&" : null;
		if (separator == null) {
			contacts.add(contactValue(null, identifiedBy));
			return contacts;
		}
		String[] splitValues = identifiedBy.split(separator);
		for (int i = 0; i < splitValues.length; i++) {
			contacts.add(contactValue(null, splitValues[i].trim()));
		}
		return contacts;
	}

	private List<Map<String, Object>> parseSpecimenRepository(String specimenRepository) {
		List<Map<String, Object>> contacts = new ArrayList<Map<String, Object>>();
		if (specimenRepository == null || specimenRepository.isEmpty()) {
			return contacts;
		}
		contacts.add(contactValue(specimenRepository, null));
		return contacts;
	}

	private Map<String, Object> contactValue(String firstName, String lastName) {
		Map<String, Object> contact = new LinkedHashMap<String, Object>();
		contact.put("contact_id", null);
		contact.put("first_name", firstName);
		contact.put("last_name", lastName);
		return contact;
	}

	private String contactKey(String firstName, String lastName) {
		return String.valueOf(firstName) + "|" + String.valueOf(lastName);
	}

	private Integer integerOrDefault(Map<String, Object> state, String key, Integer fallback) {
		if (state == null || !state.containsKey(key) || state.get(key) == null) {
			return fallback;
		}
		return integerValue(state.get(key), key);
	}

	private Integer integerOrNull(Map<String, Object> state, String key) {
		if (state == null || !state.containsKey(key) || state.get(key) == null) {
			return null;
		}
		return integerValue(state.get(key), key);
	}

	private Double doubleOrNull(Map<String, Object> values, String key) {
		if (values == null || !values.containsKey(key) || values.get(key) == null) {
			return null;
		}
		Object value = values.get(key);
		if (value instanceof Number) {
			return ((Number) value).doubleValue();
		}
		if (value instanceof String) {
			return Double.valueOf((String) value);
		}
		throw new IllegalArgumentException(key + " must be numeric or numeric string");
	}

	private boolean numericEquals(Double left, Double right) {
		if (left == null || right == null) {
			return left == null && right == null;
		}
		return Double.compare(left.doubleValue(), right.doubleValue()) == 0;
	}

	private Object normalizeNumericValue(Double value) {
		return value == null ? null : Integer.valueOf(value.intValue());
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> loadPolicy(String policyName) throws IOException {
		Yaml yaml = new Yaml();
		Path policyFile = POLICY_DIRECTORY.resolve(policyName + ".policy.yml");
		try (InputStream inputStream = Files.newInputStream(policyFile)) {
			return mapValue(yaml.load(inputStream), policyFile.toString());
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

	private Integer integerValue(Object value, String context) {
		if (value instanceof Number) {
			return ((Number) value).intValue();
		}
		if (value instanceof String) {
			return Integer.valueOf((String) value);
		}
		throw new IllegalArgumentException(context + " must be numeric or numeric string");
	}

	public static class SupportingOutputResult {
		private final List<String> relatedOutputs = new ArrayList<String>();
		private final Map<String, Object> graphResult = new LinkedHashMap<String, Object>();

		public List<String> getRelatedOutputs() {
			return relatedOutputs;
		}

		public Map<String, Object> getGraphResult() {
			return graphResult;
		}
	}
}