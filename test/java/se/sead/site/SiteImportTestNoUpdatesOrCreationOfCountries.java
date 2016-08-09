package se.sead.site;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.sead.Application;
import se.sead.DataSourceFactory;
import se.sead.DefaultAccessDatabaseReader;
import se.sead.bugs.AccessReaderProvider;
import se.sead.bugsimport.country.seadmodel.Location;
import se.sead.bugsimport.site.SiteImporter;
import se.sead.bugsimport.site.bugsmodel.BugsSite;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.site.seadmodel.SiteLocation;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.model.TestEqualityHelper;
import se.sead.repositories.BugsTraceRepository;
import se.sead.repositories.LocationTypeRepository;
import se.sead.repositories.SiteRepository;
import se.sead.repositories.TypeTranslationRepository;
import se.sead.util.DefaultConfig;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@TestPropertySource(properties = {
        "allow.site.updates:false",
        "allow.create.country:false"
})
public class SiteImportTestNoUpdatesOrCreationOfCountries extends SiteImportTest {

    @Override
    @Test
    public void runImporter(){
        doTest();
    }


}
