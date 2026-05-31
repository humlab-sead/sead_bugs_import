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

public class OutputPolicyHarness {

	private static final Path POLICY_DIRECTORY = Paths.get("doc", "reconciliation_policies");

	public OutputResult execute(String policyName, Map<String, Object> args, Map<String, Object> state, Map<String, Object> sourceRow) throws IOException {
		loadPolicy(policyName);
		if ("datasetcontacts".equals(policyName)) {
			return datasetContactsResult(args, state);
		}
		if ("siteotherproxies".equals(policyName)) {
			return siteOtherProxiesResult(args, state, sourceRow);
		}
		if ("sitelocations".equals(policyName)) {
			return siteLocationsResult(args, state);
		}
		throw new IllegalArgumentException("Policy '" + policyName + "' does not support output_result in this harness");
	}

	private OutputResult siteLocationsResult(Map<String, Object> args, Map<String, Object> state) {
		OutputResult result = new OutputResult();
		Integer siteId = integerOrNull(args, "site_id");
		Map<String, Object> siteLookup = mapOrNull(state, "site_lookup");
		if (siteLookup != null && "missing_imported_site".equals(stringOrNull(siteLookup, "status"))) {
			result.rowChanged = false;
			result.outputResult.put("row_1", siteLocationRow(
					"error",
					null,
					null,
					null,
					null,
					false,
					true,
					stringOrNull(siteLookup, "error_message")
			));
			return result;
		}

		Map<String, Object> generatedLocations = mapOrNull(state, "generated_locations");
		if (generatedLocations != null && containsGeneratedLocationErrors(generatedLocations)) {
			result.rowChanged = false;
			for (Map.Entry<String, Object> entry : generatedLocations.entrySet()) {
				Map<String, Object> row = mapValue(entry.getValue(), entry.getKey());
				String errorMessage = stringOrNull(row, "error_message");
				result.outputResult.put("row_" + Integer.valueOf(result.outputResult.size() + 1), siteLocationRow(
						errorMessage == null ? "return_generated" : "error",
						null,
						siteId,
						integerOrNull(row, "location_id"),
						stringOrNull(row, "location_name"),
						false,
						errorMessage != null,
						errorMessage
				));
			}
			return result;
		}

		List<Integer> generatedLocationIds = integerCsvValue(args.get("generated_location_ids"), "generated_location_ids");
		Map<String, Object> existingRows = mapOrNull(state, "existing_site_locations");
		List<Integer> existingLocationIds = new ArrayList<Integer>();
		List<Map<String, Object>> rowsToDelete = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> rowsToInsert = new ArrayList<Map<String, Object>>();

		if (existingRows != null) {
			for (Map.Entry<String, Object> entry : existingRows.entrySet()) {
				Map<String, Object> row = mapValue(entry.getValue(), entry.getKey());
				Integer locationId = integerOrNull(row, "location_id");
				existingLocationIds.add(locationId);
				if (!generatedLocationIds.contains(locationId)) {
					rowsToDelete.add(siteLocationRow(
							"mark_for_deletion",
							integerOrNull(row, "site_location_id"),
							siteId,
							locationId,
							stringOrNull(row, "location_name"),
							true,
							false,
							null
					));
				}
			}
		}

		for (int i = 0; i < generatedLocationIds.size(); i++) {
			Integer locationId = generatedLocationIds.get(i);
			if (!existingLocationIds.contains(locationId)) {
				rowsToInsert.add(siteLocationRow(
						"insert_new",
						null,
						siteId,
						locationId,
						locationNameFor(state, locationId),
						false,
						false,
						null
				));
			}
		}

		if (rowsToDelete.isEmpty() && rowsToInsert.isEmpty()) {
			result.rowChanged = false;
			if (existingRows != null) {
				for (Map.Entry<String, Object> entry : existingRows.entrySet()) {
					Map<String, Object> row = mapValue(entry.getValue(), entry.getKey());
					result.outputResult.put("row_" + Integer.valueOf(result.outputResult.size() + 1), siteLocationRow(
							"keep_existing",
							integerOrNull(row, "site_location_id"),
							siteId,
							integerOrNull(row, "location_id"),
							stringOrNull(row, "location_name"),
							false,
							false,
							null
					));
				}
			}
			return result;
		}

		result.rowChanged = true;

		for (int i = 0; i < rowsToDelete.size(); i++) {
			result.outputResult.put("row_" + Integer.valueOf(result.outputResult.size() + 1), rowsToDelete.get(i));
		}
		for (int i = 0; i < rowsToInsert.size(); i++) {
			result.outputResult.put("row_" + Integer.valueOf(result.outputResult.size() + 1), rowsToInsert.get(i));
		}
		return result;
	}

	private OutputResult siteOtherProxiesResult(Map<String, Object> args, Map<String, Object> state, Map<String, Object> sourceRow) {
		OutputResult result = new OutputResult();
		Integer siteId = integerValue(args.get("site_id"), "site_id");
		Map<String, Object> recordTypes = mapOrNull(state, "record_types");
		Map<String, Object> existingRows = mapOrNull(state, "existing_site_other_records");
		Map<Integer, String> generatedRecordTypes = enabledRecordTypes(recordTypes, sourceRow);

		List<Integer> existingRecordTypeIds = new ArrayList<Integer>();
		if (existingRows != null) {
			for (Map.Entry<String, Object> entry : existingRows.entrySet()) {
				Map<String, Object> row = mapValue(entry.getValue(), entry.getKey());
				Integer recordTypeId = integerOrNull(row, "record_type_id");
				boolean keepExisting = generatedRecordTypes.containsKey(recordTypeId);
				result.outputResult.put("row_" + Integer.valueOf(result.outputResult.size() + 1), siteOtherRow(
						keepExisting ? "keep_existing" : "mark_for_deletion",
						integerOrNull(row, "site_other_record_id"),
						siteId,
						recordTypeId,
						stringOrNull(row, "record_type_name"),
						!keepExisting
				));
				existingRecordTypeIds.add(recordTypeId);
			}
		}

		for (Map.Entry<Integer, String> generated : generatedRecordTypes.entrySet()) {
			if (existingRecordTypeIds.contains(generated.getKey())) {
				continue;
			}
			result.outputResult.put("row_" + Integer.valueOf(result.outputResult.size() + 1), siteOtherRow(
					"insert_new",
					null,
					siteId,
					generated.getKey(),
					generated.getValue(),
					false
			));
		}
		result.rowChanged = hasOutputChanges(result.outputResult);
		return result;
	}

	private OutputResult datasetContactsResult(Map<String, Object> args, Map<String, Object> state) {
		OutputResult result = new OutputResult();
		Integer datasetId = integerValue(args.get("dataset_id"), "dataset_id");
		Integer identifiedByTypeId = integerValue(args.get("identified_by_type_id"), "identified_by_type_id");
		Integer specimenRepositoryTypeId = integerValue(args.get("specimen_repository_type_id"), "specimen_repository_type_id");

		Map<String, Map<String, Object>> cacheIndex = new LinkedHashMap<String, Map<String, Object>>();
		seedContactIndex(cacheIndex, mapOrNull(state, "cached_contacts"));
		Map<String, Map<String, Object>> repositoryIndex = createContactIndex(mapOrNull(state, "repository_contacts"));
		Map<String, Map<String, Object>> existingIndex = new LinkedHashMap<String, Map<String, Object>>();
		Map<String, Object> existingDatasetContacts = mapOrNull(state, "existing_dataset_contacts");
		if (existingDatasetContacts != null) {
			for (Map.Entry<String, Object> entry : existingDatasetContacts.entrySet()) {
				Map<String, Object> datasetContact = mapValue(entry.getValue(), entry.getKey());
				result.outputResult.put("row_" + Integer.valueOf(result.outputResult.size() + 1), outputRow(
						"keep_existing",
						integerOrNull(datasetContact, "dataset_contact_id"),
						datasetId,
						integerOrNull(datasetContact, "contact_id"),
						integerOrNull(datasetContact, "contact_type_id"),
						stringOrNull(datasetContact, "first_name"),
						stringOrNull(datasetContact, "last_name")
				));
				existingIndex.put(outputMatchKey(integerOrNull(datasetContact, "contact_type_id"), stringOrNull(datasetContact, "first_name"), stringOrNull(datasetContact, "last_name")), datasetContact);
			}
		}

		List<Map<String, Object>> generatedContacts = new ArrayList<Map<String, Object>>();
		generatedContacts.addAll(parseIdentifiedBy(stringOrNull(args, "identified_by"), identifiedByTypeId));
		generatedContacts.addAll(parseSpecimenRepository(stringOrNull(args, "specimen_repository"), specimenRepositoryTypeId));

		for (int i = 0; i < generatedContacts.size(); i++) {
			Map<String, Object> generatedContact = generatedContacts.get(i);
			String outputKey = outputMatchKey(integerOrNull(generatedContact, "contact_type_id"), stringOrNull(generatedContact, "first_name"), stringOrNull(generatedContact, "last_name"));
			if (existingIndex.containsKey(outputKey)) {
				continue;
			}
			Map<String, Object> resolvedContact = resolveContact(generatedContact, cacheIndex, repositoryIndex);
			result.outputResult.put("row_" + Integer.valueOf(result.outputResult.size() + 1), outputRow(
					"insert_new",
					null,
					datasetId,
					integerOrNull(resolvedContact, "contact_id"),
					integerOrNull(generatedContact, "contact_type_id"),
					stringOrNull(resolvedContact, "first_name"),
					stringOrNull(resolvedContact, "last_name")
			));
		}
		result.rowChanged = hasOutputChanges(result.outputResult);
		return result;
	}

	private Map<String, Object> outputRow(String resultKind, Integer datasetContactId, Integer datasetId, Integer contactId, Integer contactTypeId, String firstName, String lastName) {
		Map<String, Object> row = new LinkedHashMap<String, Object>();
		row.put("result_kind", resultKind);
		row.put("persisted_action", persistedAction(resultKind));
		row.put("dataset_contact_id", datasetContactId);
		row.put("dataset_id", datasetId);
		row.put("contact_id", contactId);
		row.put("contact_type_id", contactTypeId);
		row.put("first_name", firstName);
		row.put("last_name", lastName);
		return row;
	}

	private Map<String, Object> siteOtherRow(String resultKind, Integer siteOtherRecordId, Integer siteId, Integer recordTypeId, String recordTypeName, boolean markedForDeletion) {
		Map<String, Object> row = new LinkedHashMap<String, Object>();
		row.put("result_kind", resultKind);
		row.put("persisted_action", persistedAction(resultKind));
		row.put("site_other_record_id", siteOtherRecordId);
		row.put("site_id", siteId);
		row.put("record_type_id", recordTypeId);
		row.put("record_type_name", recordTypeName);
		row.put("marked_for_deletion", Boolean.valueOf(markedForDeletion));
		return row;
	}

	private Map<String, Object> siteLocationRow(String resultKind, Integer siteLocationId, Integer siteId, Integer locationId, String locationName, boolean markedForDeletion, boolean isError, String errorMessage) {
		Map<String, Object> row = new LinkedHashMap<String, Object>();
		row.put("result_kind", resultKind);
		row.put("persisted_action", persistedAction(resultKind));
		row.put("site_location_id", siteLocationId);
		row.put("site_id", siteId);
		row.put("location_id", locationId);
		row.put("location_name", locationName);
		row.put("marked_for_deletion", Boolean.valueOf(markedForDeletion));
		row.put("is_error", Boolean.valueOf(isError));
		row.put("error_message", errorMessage);
		return row;
	}

	private String persistedAction(String resultKind) {
		if ("keep_existing".equals(resultKind)) {
			return "keep_existing";
		}
		if ("insert_new".equals(resultKind)) {
			return "append_new";
		}
		if ("mark_for_deletion".equals(resultKind)) {
			return "mark_for_deletion";
		}
		if ("error".equals(resultKind) || "return_generated".equals(resultKind)) {
			return "stop_before_list_update";
		}
		return resultKind;
	}

	private boolean hasOutputChanges(Map<String, Object> outputRows) {
		for (Map.Entry<String, Object> entry : outputRows.entrySet()) {
			Map<String, Object> row = mapValue(entry.getValue(), entry.getKey());
			String resultKind = stringOrNull(row, "result_kind");
			if (!"keep_existing".equals(resultKind)) {
				return true;
			}
		}
		return false;
	}

	private boolean containsGeneratedLocationErrors(Map<String, Object> generatedLocations) {
		for (Map.Entry<String, Object> entry : generatedLocations.entrySet()) {
			Map<String, Object> row = mapValue(entry.getValue(), entry.getKey());
			if (stringOrNull(row, "error_message") != null) {
				return true;
			}
		}
		return false;
	}

	private String locationNameFor(Map<String, Object> state, Integer locationId) {
		Map<String, Object> generatedLocations = mapOrNull(state, "generated_locations");
		if (generatedLocations == null) {
			return null;
		}
		for (Map.Entry<String, Object> entry : generatedLocations.entrySet()) {
			Map<String, Object> row = mapValue(entry.getValue(), entry.getKey());
			if (locationId.equals(integerOrNull(row, "location_id"))) {
				return stringOrNull(row, "location_name");
			}
		}
		return null;
	}

	private Map<Integer, String> enabledRecordTypes(Map<String, Object> recordTypes, Map<String, Object> sourceRow) {
		Map<Integer, String> enabled = new LinkedHashMap<Integer, String>();
		addIfEnabled(enabled, recordTypes, sourceRow, "HasPollen", "Pollen");
		addIfEnabled(enabled, recordTypes, sourceRow, "HasPlantMacro", "PlantMacro");
		addIfEnabled(enabled, recordTypes, sourceRow, "HasDiatoms", "Diatoms");
		addIfEnabled(enabled, recordTypes, sourceRow, "HasChironomids", "Chironomids");
		addIfEnabled(enabled, recordTypes, sourceRow, "HasSoilChemistry", "SoilChemistry");
		addIfEnabled(enabled, recordTypes, sourceRow, "HasIsotopes", "Isotopes");
		addIfEnabled(enabled, recordTypes, sourceRow, "HasAnimalBones", "AnimalBones");
		addIfEnabled(enabled, recordTypes, sourceRow, "HasArchaeology", "Archaeology");
		addIfEnabled(enabled, recordTypes, sourceRow, "HasMolluscs", "Molluscs");
		return enabled;
	}

	private void addIfEnabled(Map<Integer, String> enabled, Map<String, Object> recordTypes, Map<String, Object> sourceRow, String sourceField, String recordTypeKey) {
		if (!booleanValue(sourceRow.get(sourceField), sourceField)) {
			return;
		}
		Map<String, Object> recordType = mapValue(recordTypes.get(recordTypeKey), recordTypeKey);
		enabled.put(integerOrNull(recordType, "record_type_id"), stringOrNull(recordType, "record_type_name"));
	}

	private Map<String, Object> resolveContact(Map<String, Object> generatedContact, Map<String, Map<String, Object>> cacheIndex, Map<String, Map<String, Object>> repositoryIndex) {
		String contactKey = contactKey(stringOrNull(generatedContact, "first_name"), stringOrNull(generatedContact, "last_name"));
		Map<String, Object> cached = cacheIndex.get(contactKey);
		if (cached != null) {
			return cached;
		}
		Map<String, Object> stored = repositoryIndex.get(contactKey);
		if (stored != null) {
			cacheIndex.put(contactKey, stored);
			return stored;
		}
		cacheIndex.put(contactKey, generatedContact);
		return generatedContact;
	}

	private List<Map<String, Object>> parseIdentifiedBy(String identifiedBy, Integer identifiedByTypeId) {
		List<Map<String, Object>> contacts = new ArrayList<Map<String, Object>>();
		if (identifiedBy == null || identifiedBy.isEmpty()) {
			return contacts;
		}
		String separator = identifiedBy.contains(",") ? "," : identifiedBy.contains(";") ? ";" : identifiedBy.contains("&") ? "&" : null;
		if (separator == null) {
			contacts.add(contactValue(null, identifiedBy, identifiedByTypeId));
			return contacts;
		}
		String[] splitValues = identifiedBy.split(separator);
		for (int i = 0; i < splitValues.length; i++) {
			contacts.add(contactValue(null, splitValues[i].trim(), identifiedByTypeId));
		}
		return contacts;
	}

	private List<Map<String, Object>> parseSpecimenRepository(String specimenRepository, Integer specimenRepositoryTypeId) {
		List<Map<String, Object>> contacts = new ArrayList<Map<String, Object>>();
		if (specimenRepository == null || specimenRepository.isEmpty()) {
			return contacts;
		}
		contacts.add(contactValue(specimenRepository, null, specimenRepositoryTypeId));
		return contacts;
	}

	private Map<String, Object> contactValue(String firstName, String lastName, Integer contactTypeId) {
		Map<String, Object> contact = new LinkedHashMap<String, Object>();
		contact.put("contact_id", null);
		contact.put("first_name", firstName);
		contact.put("last_name", lastName);
		contact.put("contact_type_id", contactTypeId);
		return contact;
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

	private String outputMatchKey(Integer contactTypeId, String firstName, String lastName) {
		return String.valueOf(contactTypeId) + "|" + String.valueOf(firstName) + "|" + String.valueOf(lastName);
	}

	private String contactKey(String firstName, String lastName) {
		return String.valueOf(firstName) + "|" + String.valueOf(lastName);
	}

	private String stringOrNull(Map<String, Object> values, String key) {
		if (values == null || !values.containsKey(key) || values.get(key) == null) {
			return null;
		}
		return stringValue(values.get(key), key);
	}

	private Integer integerOrNull(Map<String, Object> values, String key) {
		if (values == null || !values.containsKey(key) || values.get(key) == null) {
			return null;
		}
		return integerValue(values.get(key), key);
	}

	private Map<String, Object> mapOrNull(Map<String, Object> values, String key) {
		if (values == null || !values.containsKey(key) || values.get(key) == null) {
			return null;
		}
		return mapValue(values.get(key), key);
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

	private boolean booleanValue(Object value, String context) {
		if (!(value instanceof Boolean)) {
			throw new IllegalArgumentException(context + " must be boolean");
		}
		return (Boolean) value;
	}

	@SuppressWarnings("unchecked")
	private List<Integer> integerListValue(Object value, String context) {
		if (!(value instanceof List)) {
			throw new IllegalArgumentException(context + " must be a list");
		}
		List<Object> rawValues = (List<Object>) value;
		List<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < rawValues.size(); i++) {
			result.add(integerValue(rawValues.get(i), context + "[" + i + "]"));
		}
		return result;
	}

	private List<Integer> integerCsvValue(Object value, String context) {
		String raw = stringValue(value, context);
		List<Integer> result = new ArrayList<Integer>();
		if (raw.trim().isEmpty()) {
			return result;
		}
		String[] parts = raw.split(",");
		for (int i = 0; i < parts.length; i++) {
			result.add(integerValue(parts[i].trim(), context + "[" + i + "]"));
		}
		return result;
	}

	public static class OutputResult {

		private final Map<String, Object> outputResult = new LinkedHashMap<String, Object>();
		private boolean rowChanged;

		public Map<String, Object> getOutputResult() {
			return outputResult;
		}

		public boolean isRowChanged() {
			return rowChanged;
		}
	}
}