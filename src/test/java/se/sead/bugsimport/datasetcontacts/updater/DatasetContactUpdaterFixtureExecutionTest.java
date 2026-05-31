package se.sead.bugsimport.datasetcontacts.updater;

import org.junit.Before;
import org.junit.Test;
import se.sead.model.TestContact;
import se.sead.model.TestDataset;
import se.sead.model.TestDatasetContact;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.repositories.ContactRepository;
import se.sead.repositories.ContactTypeRepository;
import se.sead.repositories.DatasetContactRepository;
import se.sead.sead.contact.Contact;
import se.sead.sead.contact.ContactType;
import se.sead.sead.data.Dataset;
import se.sead.sead.data.DatasetContact;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DatasetContactUpdaterFixtureExecutionTest {

	private PolicyFixtureLoader fixtureLoader;
	private ContactType identifiedByType;
	private ContactType specimenRepositoryType;

	@Before
	public void setup() {
		fixtureLoader = new PolicyFixtureLoader();
		identifiedByType = createContactType(601, "Identified by");
		specimenRepositoryType = createContactType(602, "Specimen repository");
	}

	@Test
	public void createDatasetContactsFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_dataset_contacts_when_no_existing_rows");
	}

	@Test
	public void appendOnlyUnmatchedDatasetContactsFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("keep_existing_and_append_only_unmatched_generated_contacts");
	}

	@Test
	public void keepOnlyExistingDatasetContactsFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("keep_only_existing_contacts_when_all_generated_match");
	}

	@Test
	public void keepExistingWhenNoGeneratedDatasetContactsFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("keep_existing_contacts_when_no_generated_contacts");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("datasetcontacts.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> args = fixtureLoader.mapValue(policyContext.get("args"), scenarioName + ".policy_context.args");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		Dataset dataset = createDataset(args);
		DatasetContactUpdater updater = createUpdater(args, state, dataset);
		updater.update(dataset, "fixture-site");

		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("output_result"), scenarioName + ".expects.output_result"), actualOutputResult(dataset.getContacts()));
	}

	private DatasetContactUpdater createUpdater(Map<String, Object> args, Map<String, Object> state, Dataset dataset) throws Exception {
		Map<String, Object> cachedContacts = state == null || !state.containsKey("cached_contacts") ? null : fixtureLoader.mapValue(state.get("cached_contacts"), "cached_contacts");
		Map<String, Object> repositoryContacts = state == null || !state.containsKey("repository_contacts") ? null : fixtureLoader.mapValue(state.get("repository_contacts"), "repository_contacts");
		Map<String, Object> existingDatasetContacts = state == null || !state.containsKey("existing_dataset_contacts") ? null : fixtureLoader.mapValue(state.get("existing_dataset_contacts"), "existing_dataset_contacts");

		DatasetContactUpdater updater = new DatasetContactUpdater(
				createContactTypeRepository(),
				new RecordingContactAccessor(cachedContacts, repositoryContacts),
				createDatasetContactRepository(existingDatasetContacts, dataset)
		);
		injectField(updater, "siteContactReader", new RecordingSiteContactReader(stringOrNull(args, "identified_by"), stringOrNull(args, "specimen_repository")));
		return updater;
	}

	private Map<String, Object> actualOutputResult(List<DatasetContact> contacts) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		if (contacts == null) {
			return result;
		}
		for (int i = 0; i < contacts.size(); i++) {
			DatasetContact datasetContact = contacts.get(i);
			Map<String, Object> row = new LinkedHashMap<String, Object>();
			row.put("result_kind", datasetContact.getId() == null ? "insert_new" : "keep_existing");
			row.put("dataset_contact_id", datasetContact.getId());
			row.put("dataset_id", datasetContact.getDataset() == null ? null : datasetContact.getDataset().getId());
			row.put("contact_id", datasetContact.getContact() == null ? null : datasetContact.getContact().getId());
			row.put("contact_type_id", datasetContact.getType() == null ? null : datasetContact.getType().getId());
			row.put("first_name", datasetContact.getContact() == null ? null : datasetContact.getContact().getFirstName());
			row.put("last_name", datasetContact.getContact() == null ? null : datasetContact.getContact().getLastName());
			result.put("row_" + Integer.valueOf(i + 1), row);
		}
		return result;
	}

	private Dataset createDataset(Map<String, Object> args) {
		return TestDataset.create(integerValue(args.get("dataset_id")), "fixture dataset", null, null, null);
	}

	private ContactTypeRepository createContactTypeRepository() {
		return (ContactTypeRepository) Proxy.newProxyInstance(
				ContactTypeRepository.class.getClassLoader(),
				new Class[]{ContactTypeRepository.class},
				(proxy, method, args) -> {
					if ("getIdentifiedByType".equals(method.getName())) {
						return identifiedByType;
					}
					if ("getSpecimentRepositoryType".equals(method.getName())) {
						return specimenRepositoryType;
					}
					return null;
				}
		);
	}

	private DatasetContactRepository createDatasetContactRepository(Map<String, Object> existingDatasetContacts, Dataset dataset) {
		List<DatasetContact> rows = new ArrayList<DatasetContact>();
		if (existingDatasetContacts != null) {
			for (Map.Entry<String, Object> entry : existingDatasetContacts.entrySet()) {
				Map<String, Object> row = fixtureLoader.mapValue(entry.getValue(), entry.getKey());
				rows.add(TestDatasetContact.create(
						integerOrNull(row, "dataset_contact_id"),
						TestContact.create(integerOrNull(row, "contact_id"), null, null, null, null, stringOrNull(row, "first_name"), stringOrNull(row, "last_name"), null),
						contactType(integerOrNull(row, "contact_type_id")),
						dataset
				));
			}
		}
		return (DatasetContactRepository) Proxy.newProxyInstance(
				DatasetContactRepository.class.getClassLoader(),
				new Class[]{DatasetContactRepository.class},
				(proxy, method, args) -> {
					if ("findByDataset".equals(method.getName())) {
						return new ArrayList<DatasetContact>(rows);
					}
					if ("findAll".equals(method.getName())) {
						return new ArrayList<DatasetContact>(rows);
					}
					return null;
				}
		);
	}

	private ContactType contactType(Integer id) {
		if (id == null) {
			return null;
		}
		if (id.intValue() == identifiedByType.getId().intValue()) {
			return identifiedByType;
		}
		if (id.intValue() == specimenRepositoryType.getId().intValue()) {
			return specimenRepositoryType;
		}
		return createContactType(id, "fixture type " + id);
	}

	private ContactType createContactType(Integer id, String name) {
		TestContactType type = new TestContactType(id);
		type.setName(name);
		type.setDescription(name + " description");
		return type;
	}

	private void injectField(Object target, String fieldName, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

	private Integer integerValue(Object value) {
		if (value instanceof Number) {
			return ((Number) value).intValue();
		}
		if (value instanceof String) {
			return Integer.valueOf((String) value);
		}
		throw new IllegalArgumentException("Value must be numeric");
	}

	private Integer integerOrNull(Map<String, Object> row, String key) {
		if (row == null || !row.containsKey(key) || row.get(key) == null) {
			return null;
		}
		return integerValue(row.get(key));
	}

	private String stringOrNull(Map<String, Object> row, String key) {
		if (row == null || !row.containsKey(key) || row.get(key) == null) {
			return null;
		}
		return fixtureLoader.stringValue(row.get(key), key);
	}

	private static class RecordingSiteContactReader extends SiteContactReader {

		private final String identifiedBy;
		private final String specimenRepository;

		RecordingSiteContactReader(String identifiedBy, String specimenRepository) {
			super(null);
			this.identifiedBy = identifiedBy;
			this.specimenRepository = specimenRepository;
		}

		@Override
		public SiteContactStringData parse(String bugsSiteCode) {
			SiteContactStringData data = new SiteContactStringData();
			data.setIdentifiedBy(identifiedBy);
			data.setSpecimenRepository(specimenRepository);
			return data;
		}
	}

	private static class RecordingContactAccessor extends ContactCacheAndRepositoryAccessor {

		RecordingContactAccessor(Map<String, Object> cachedContacts, Map<String, Object> repositoryContacts) {
			super(createRepository(repositoryContacts));
			if (cachedContacts != null) {
				for (Map.Entry<String, Object> entry : cachedContacts.entrySet()) {
					Map<String, Object> contactData = castMap(entry.getValue(), entry.getKey());
					setContact(createContact(contactData));
				}
			}
		}

		private static ContactRepository createRepository(Map<String, Object> repositoryContacts) {
			Map<String, Contact> repositoryIndex = new LinkedHashMap<String, Contact>();
			if (repositoryContacts != null) {
				for (Map.Entry<String, Object> entry : repositoryContacts.entrySet()) {
					Map<String, Object> contactData = castMap(entry.getValue(), entry.getKey());
					Contact contact = createContact(contactData);
					repositoryIndex.put(contactKey(contact.getFirstName(), contact.getLastName()), contact);
				}
			}
			return (ContactRepository) Proxy.newProxyInstance(
					ContactRepository.class.getClassLoader(),
					new Class[]{ContactRepository.class},
					(proxy, method, args) -> {
						if ("getByFirstNameAndLastName".equals(method.getName())) {
							return repositoryIndex.get(contactKey((String) args[0], (String) args[1]));
						}
						return null;
					}
			);
		}

		@SuppressWarnings("unchecked")
		private static Map<String, Object> castMap(Object value, String context) {
			if (!(value instanceof Map)) {
				throw new IllegalArgumentException(context + " must be a mapping");
			}
			return (Map<String, Object>) value;
		}

		private static Contact createContact(Map<String, Object> contactData) {
			return TestContact.create(
					contactData.containsKey("contact_id") && contactData.get("contact_id") != null ? ((Number) contactData.get("contact_id")).intValue() : null,
					null,
					null,
					null,
					null,
					contactData.containsKey("first_name") && contactData.get("first_name") != null ? (String) contactData.get("first_name") : null,
					contactData.containsKey("last_name") && contactData.get("last_name") != null ? (String) contactData.get("last_name") : null,
					null
			);
		}

		private static String contactKey(String firstName, String lastName) {
			return String.valueOf(firstName) + "|" + String.valueOf(lastName);
		}
	}

	private static class TestContactType extends ContactType {

		private TestContactType(Integer id) {
			super.setId(id);
		}
	}
}