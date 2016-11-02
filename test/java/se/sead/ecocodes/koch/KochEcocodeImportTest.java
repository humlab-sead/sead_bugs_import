package se.sead.ecocodes.koch;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.sead.Application;
import se.sead.DataSourceFactory;
import se.sead.DefaultAccessDatabaseReader;
import se.sead.bugs.AccessReaderProvider;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.ecocodes.koch.KochEcocodesImporter;
import se.sead.bugsimport.ecocodes.koch.bugsmodel.EcoKoch;
import se.sead.bugsimport.ecocodes.seadmodel.Ecocode;
import se.sead.ecocodes.EcocodeImporterBaseTest;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DefaultConfig;

import javax.sql.DataSource;
import java.util.List;

@SpringBootTest(classes = {Application.class, KochEcocodeImportTest.Config.class})
public class KochEcocodeImportTest extends EcocodeImporterBaseTest<EcoKoch>{

    @Configuration
    public static class Config extends DefaultConfig {

        public Config(){
            super("ecocodes/koch", "ecocodes.mdb", "ecocodes.sql");
        }

        @Bean
        @Override
        public AccessReaderProvider getDatabaseReader() {
            return new DefaultAccessDatabaseReader(getMdbFile());
        }

        @Bean
        @Override
        public DataSource createDataSource() {
            return DataSourceFactory.createDefault(getDataFile());
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
