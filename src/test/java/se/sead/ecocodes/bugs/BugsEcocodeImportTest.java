package se.sead.ecocodes.bugs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.ecocodes.bugs.BugsEcocodeImporter;
import se.sead.bugsimport.ecocodes.bugs.bugsmodel.EcoBugs;
import se.sead.bugsimport.ecocodes.seadmodel.Ecocode;
import se.sead.ecocodes.EcocodeImporterBaseTest;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DefaultConfig;

import java.util.List;

public class BugsEcocodeImportTest extends EcocodeImporterBaseTest<EcoBugs> {

    @TestConfiguration
    public static class Config extends DefaultConfig {
        public Config(){
            super("ecocodes/bugs", "ecocodes.mdb", "ecocodes.sql");
        }
    }

    @Autowired
    private BugsEcocodeImporter importer;

    @Override
    protected BugsTracesAndErrorsVerification.LogVerificationCallback<EcoBugs> getLogVerificationCallBack() {
        return new BugsEcoLogVerifier();
    }

    @Override
    protected List<EcoBugs> getExpectedBugsData() {
        return ExpectedBugsData.EXPECTED_DATA;
    }

    @Override
    protected Importer<EcoBugs, Ecocode> getImporter() {
        return importer;
    }
}
