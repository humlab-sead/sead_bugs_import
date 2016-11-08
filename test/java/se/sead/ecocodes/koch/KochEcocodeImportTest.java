package se.sead.ecocodes.koch;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.ecocodes.koch.KochEcocodesImporter;
import se.sead.bugsimport.ecocodes.koch.bugsmodel.EcoKoch;
import se.sead.bugsimport.ecocodes.seadmodel.Ecocode;
import se.sead.ecocodes.EcocodeImporterBaseTest;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DefaultConfig;

import java.util.List;

public class KochEcocodeImportTest extends EcocodeImporterBaseTest<EcoKoch>{

    @TestConfiguration
    public static class Config extends DefaultConfig {

        public Config(){
            super("ecocodes/koch", "ecocodes.mdb", "ecocodes.sql");
        }
    }

    @Autowired
    private KochEcocodesImporter importer;

    @Override
    protected BugsTracesAndErrorsVerification.LogVerificationCallback<EcoKoch> getLogVerificationCallBack() {
        return new KochEcoLogVerifier();
    }

    @Override
    protected List<EcoKoch> getExpectedBugsData() {
        return ExpectedBugsData.EXPECTED_DATA;
    }

    @Override
    protected Importer<EcoKoch, Ecocode> getImporter() {
        return importer;
    }

}
