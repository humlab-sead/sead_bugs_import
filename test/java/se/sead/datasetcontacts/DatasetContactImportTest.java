package se.sead.datasetcontacts;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import se.sead.bugsimport.datasetcontacts.DatasetContactImporter;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.repositories.*;
import se.sead.sead.contact.Contact;
import se.sead.sead.data.DatasetContact;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

import java.util.List;

import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DatasetContactImportTest.Config.class})
@ActiveProfiles("test")
@Transactional
public class DatasetContactImportTest {
    @TestConfiguration
    static class Config extends DefaultConfig {

        Config(){
            super("datasetcontacts");
        }
    }

    private DatabaseContentVerification<DatasetContact> datasetContactDatabaseContentVerification;
    private DatabaseContentVerification<Contact> contactDatabaseContentVerification;
    private VerifyLogs logVerifier;

    @Autowired
    private ContactTypeRepository contactTypeRepository;
    @Autowired
    private MethodRepository methodRepository;
    @Autowired
    private DatasetMasterRepository datasetMasterRepository;
    @Autowired
    private DataTypeRepository dataTypeRepository;
    @Autowired
    private DatasetContactRepository datasetContactRepository;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private DatasetContactImporter importer;

    @Autowired
    private BugsErrorRepository errorRepository;

    @Before
    public void setup() {
        datasetContactDatabaseContentVerification =
                new DatabaseContentVerification<>(
                    new DatasetContactDatabaseContentProvider(
                        contactTypeRepository,
                        methodRepository,
                        datasetMasterRepository,
                        dataTypeRepository,
                        datasetContactRepository
                    )
                );
        contactDatabaseContentVerification =
                new DatabaseContentVerification<>(
                        new ContactDatabaseContentProvider(
                                contactRepository
                        )
                );
        logVerifier = new VerifyLogs(errorRepository);
    }

    @Test
    public void run(){
        importer.run();
        datasetContactDatabaseContentVerification.verifyDatabaseContent();
        contactDatabaseContentVerification.verifyDatabaseContent();
        logVerifier.verifyErrors();
    }

    private static class VerifyLogs {
        private BugsErrorRepository errorRepository;

        private VerifyLogs(BugsErrorRepository errorRepository) {
            this.errorRepository = errorRepository;
        }

        void verifyErrors(){
            List<BugsError> errors = errorRepository.findByBugsTableAndBugsIdentifier("TCountsheet", "COUN000006");
            assertFalse(errors.isEmpty());
        }
    }
}
