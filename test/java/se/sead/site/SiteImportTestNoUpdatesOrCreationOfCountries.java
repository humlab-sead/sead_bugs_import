package se.sead.site;

import org.junit.Test;
import org.springframework.test.context.TestPropertySource;

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
