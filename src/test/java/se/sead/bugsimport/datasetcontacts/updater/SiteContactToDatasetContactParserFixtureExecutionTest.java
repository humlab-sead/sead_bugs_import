package se.sead.bugsimport.datasetcontacts.updater;

import org.junit.Before;
import org.junit.Test;
import se.sead.reconciliation.FixtureExpectationHelper;
import se.sead.reconciliation.PolicyFixtureLoader;
import se.sead.repositories.ContactRepository;
import se.sead.repositories.ContactTypeRepository;
import se.sead.sead.contact.Contact;
import se.sead.sead.contact.ContactType;
import se.sead.sead.data.DatasetContact;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SiteContactToDatasetContactParserFixtureExecutionTest {

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
	public void createContactFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_identified_by_contact_when_unmatched");
	}

	@Test
	public void repositoryContactFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("reuse_repository_contact_for_identified_by_name");
	}

	@Test
	public void cachedContactFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("reuse_cached_contact_before_repository_lookup");
	}

	@Test
	public void parseOrderFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_identified_by_and_specimen_contacts_in_parse_order");
	}

	@Test
	public void createSpecimenRepositoryContactFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("create_specimen_repository_contact_when_unmatched");
	}

	@Test
	public void repositorySpecimenRepositoryContactFixtureMatchesCurrentJavaBehavior() throws Exception {
		assertScenarioMatchesCurrentJavaBehavior("reuse_repository_specimen_repository_contact");
	}

	private void assertScenarioMatchesCurrentJavaBehavior(String scenarioName) throws Exception {
		Map<String, Object> fixture = fixtureLoader.loadFixture("datasetcontacts.fixture.yml");
		Map<String, Object> scenario = fixtureLoader.findScenario(fixture, scenarioName);
		Map<String, Object> expects = fixtureLoader.mapValue(scenario.get("expects"), scenarioName + ".expects");
		Map<String, Object> policyContext = fixtureLoader.mapValue(scenario.get("policy_context"), scenarioName + ".policy_context");
		Map<String, Object> args = fixtureLoader.mapValue(policyContext.get("args"), scenarioName + ".policy_context.args");
		Map<String, Object> state = policyContext.containsKey("state") ? fixtureLoader.mapValue(policyContext.get("state"), scenarioName + ".policy_context.state") : null;

		RecordingContactAccessor accessor = new RecordingContactAccessor(
				state == null || !state.containsKey("cached_contacts") ? null : fixtureLoader.mapValue(state.get("cached_contacts"), scenarioName + ".policy_context.state.cached_contacts"),
				state == null || !state.containsKey("repository_contacts") ? null : fixtureLoader.mapValue(state.get("repository_contacts"), scenarioName + ".policy_context.state.repository_contacts")
		);
		SiteContactToDatasetContactParser parser = new SiteContactToDatasetContactParser(createContactTypeRepository(), accessor);
		List<DatasetContact> contacts = parser.parseIntoNonDatabaseSyncedContacts(createContactData(args));

		assertEquals(FixtureExpectationHelper.toStringList(fixtureLoader, expects.get("related_outputs"), scenarioName + ".expects.related_outputs"), java.util.Collections.singletonList("contacts"));
		assertEquals(fixtureLoader.booleanValue(expects.get("row_changed"), scenarioName + ".expects.row_changed"), rowChanged(actualGraphResult(contacts, accessor)));
		assertEquals(FixtureExpectationHelper.toNestedMap(fixtureLoader, expects.get("graph_result"), scenarioName + ".expects.graph_result"), actualGraphResult(contacts, accessor));
	}

	private boolean rowChanged(Map<String, Object> graphResult) {
		Map<String, Object> contacts = fixtureLoader.mapValue(graphResult.get("contacts"), "graph_result.contacts");
		for (Map.Entry<String, Object> entry : contacts.entrySet()) {
			Map<String, Object> contact = fixtureLoader.mapValue(entry.getValue(), entry.getKey());
			String supportingAction = fixtureLoader.stringValue(contact.get("supporting_action"), entry.getKey() + ".supporting_action");
			if (!"reuse".equals(supportingAction) && !"keep".equals(supportingAction)) {
				return true;
			}
		}
		return false;
	}

	private SiteContactReader.SiteContactStringData createContactData(Map<String, Object> args) {
		SiteContactReader.SiteContactStringData contactData = new SiteContactReader.SiteContactStringData();
		contactData.setIdentifiedBy(stringOrNull(args, "identified_by"));
		contactData.setSpecimenRepository(stringOrNull(args, "specimen_repository"));
		return contactData;
	}

	private Map<String, Object> actualGraphResult(List<DatasetContact> contacts, RecordingContactAccessor accessor) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		Map<String, Object> contactsResult = new LinkedHashMap<String, Object>();
		List<RecordingContactAccessor.Resolution> resolutions = accessor.getResolutions();
		for (int i = 0; i < contacts.size(); i++) {
			DatasetContact datasetContact = contacts.get(i);
			RecordingContactAccessor.Resolution resolution = resolutions.get(i);
			Map<String, Object> contactResult = new LinkedHashMap<String, Object>();
			contactResult.put("result_kind", "generated_new".equals(resolution.source) ? "insert_new" : "return_existing");
			contactResult.put("supporting_action", "generated_new".equals(resolution.source) ? "create" : "reuse");
			contactResult.put("source", resolution.source);
			contactResult.put("contact_id", datasetContact.getContact().getId());
			contactResult.put("first_name", datasetContact.getContact().getFirstName());
			contactResult.put("last_name", datasetContact.getContact().getLastName());
			contactsResult.put("contact_" + Integer.valueOf(i + 1), contactResult);
		}
		result.put("contacts", contactsResult);
		return result;
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

	private ContactType createContactType(Integer id, String name) {
		TestContactType type = new TestContactType(id);
		type.setName(name);
		type.setDescription(name + " description");
		return type;
	}

	private String stringOrNull(Map<String, Object> values, String key) {
		if (values == null || !values.containsKey(key) || values.get(key) == null) {
			return null;
		}
		return fixtureLoader.stringValue(values.get(key), key);
	}

	private static class RecordingContactAccessor extends ContactCacheAndRepositoryAccessor {

		private final Map<String, Contact> repositoryIndex = new LinkedHashMap<String, Contact>();
		private final Map<String, Contact> expectedCacheIndex = new LinkedHashMap<String, Contact>();
		private final List<Resolution> resolutions = new ArrayList<Resolution>();

		RecordingContactAccessor(Map<String, Object> cachedContacts, Map<String, Object> repositoryContacts) {
			super(createRepository(repositoryContacts));
			seedExpectedIndex(expectedCacheIndex, cachedContacts);
			for (Contact contact : expectedCacheIndex.values()) {
				setContact(contact);
			}
			seedExpectedIndex(repositoryIndex, repositoryContacts);
		}

		@Override
		public Contact getFromCacheOrRepository(Contact contact) {
			String key = key(contact.getFirstName(), contact.getLastName());
			String source = expectedCacheIndex.containsKey(key) ? "cache" : repositoryIndex.containsKey(key) ? "repository" : "generated_new";
			Contact resolved = super.getFromCacheOrRepository(contact);
			expectedCacheIndex.put(key, resolved);
			resolutions.add(new Resolution(source, resolved));
			return resolved;
		}

		List<Resolution> getResolutions() {
			return resolutions;
		}

		private static ContactRepository createRepository(Map<String, Object> repositoryContacts) {
			Map<String, Contact> repositoryIndex = new LinkedHashMap<String, Contact>();
			seedExpectedIndex(repositoryIndex, repositoryContacts);
			return (ContactRepository) Proxy.newProxyInstance(
					ContactRepository.class.getClassLoader(),
					new Class[]{ContactRepository.class},
					(proxy, method, args) -> {
						if ("getByFirstNameAndLastName".equals(method.getName())) {
							return repositoryIndex.get(key((String) args[0], (String) args[1]));
						}
						return null;
					}
			);
		}

		private static void seedExpectedIndex(Map<String, Contact> index, Map<String, Object> contacts) {
			if (contacts == null) {
				return;
			}
			for (Map.Entry<String, Object> entry : contacts.entrySet()) {
				Map<String, Object> contactData = castMap(entry.getValue(), entry.getKey());
				Contact contact = createContact(contactData);
				index.put(key(contact.getFirstName(), contact.getLastName()), contact);
			}
		}

		@SuppressWarnings("unchecked")
		private static Map<String, Object> castMap(Object value, String context) {
			if (!(value instanceof Map)) {
				throw new IllegalArgumentException(context + " must be a mapping");
			}
			return (Map<String, Object>) value;
		}

		private static Contact createContact(Map<String, Object> contactData) {
			Integer id = contactData.containsKey("contact_id") && contactData.get("contact_id") != null ? ((Number) contactData.get("contact_id")).intValue() : null;
			TestContact contact = new TestContact(id);
			contact.setFirstName(contactData.containsKey("first_name") && contactData.get("first_name") != null ? (String) contactData.get("first_name") : null);
			contact.setLastName(contactData.containsKey("last_name") && contactData.get("last_name") != null ? (String) contactData.get("last_name") : null);
			return contact;
		}

		private static String key(String firstName, String lastName) {
			return String.valueOf(firstName) + "|" + String.valueOf(lastName);
		}

		private static class Resolution {

			private final String source;
			private final Contact contact;

			private Resolution(String source, Contact contact) {
				this.source = source;
				this.contact = contact;
			}
		}
	}

	private static class TestContact extends Contact {

		private TestContact(Integer id) {
			super.setId(id);
		}
	}

	private static class TestContactType extends ContactType {

		private TestContactType(Integer id) {
			super.setId(id);
		}
	}
}