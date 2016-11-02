package se.sead.ecocodes.bugs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.sead.Application;
import se.sead.DataSourceFactory;
import se.sead.DefaultAccessDatabaseReader;
import se.sead.bugs.AccessReaderProvider;
import se.sead.bugsimport.Importer;
import se.sead.bugsimport.ecocodes.bugs.BugsEcocodeImporter;
import se.sead.bugsimport.ecocodes.bugs.bugsmodel.EcoBugs;
import se.sead.bugsimport.ecocodes.seadmodel.Ecocode;
import se.sead.ecocodes.EcocodeImporterBaseTest;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DefaultConfig;

import javax.sql.DataSource;
import java.util.List;

@SpringBootTest(classes = {Application.class, BugsEcocodeImportTest.Config.class})
public class BugsEcocodeImportTest extends EcocodeImporterBaseTest<EcoBugs> {

    @Configuration
    public static class Config extends DefaultConfig {

        public Config(){
            super("ecocodes/bugs", "ecocodes.mdb", "ecocodes.sql");
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
