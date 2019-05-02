package se.sead.ecocodes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.bugs.TraceableBugsData;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.ecocodes.seadmodel.Ecocode;
import se.sead.repositories.*;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class EcocodeImporterBaseTest<T extends TraceableBugsData> {

    @Autowired
    private SpeciesRepository speciesRepository;
    @Autowired
    private EcocodeDefinitionRepository definitionRepository;
    @Autowired
    private EcocodeRepository ecocodeRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    @Test
    public void run(){
        DatabaseContentVerification<Ecocode> databaseContentVerifier = createDatabaseContentVerifier();
        BugsTracesAndErrorsVerification<T> logVerifier = createLogVerifier();
        getImporter().run();
        databaseContentVerifier.verifyDatabaseContent();
        logVerifier.verifyTraceContent();
    }

    private DatabaseContentVerification<Ecocode> createDatabaseContentVerifier(){
        return new DatabaseContentVerification<>(
                new DatabaseContentProvider(
                        speciesRepository,
                        definitionRepository,
                        ecocodeRepository
                )
        );
    }

    private BugsTracesAndErrorsVerification<T> createLogVerifier(){
        return new BugsTracesAndErrorsVerification.ByCompressed<>(
                traceRepository,
                errorRepository,
                getLogVerificationCallBack(),
                () -> getExpectedBugsData()
        );
    }

    protected abstract BugsTracesAndErrorsVerification.LogVerificationCallback<T> getLogVerificationCallBack();
    protected abstract List<T> getExpectedBugsData();
    protected abstract Importer<T, Ecocode> getImporter();
}
