package se.sead.site;

import org.junit.Test;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {
        "allow.site.updates:true",
        "allow.create.country:true"
})
public class SiteImportTestCanUpdateCanCreateCountry extends SiteImportTest {
    @Override
    @Test
    public void runImporter() {
        doTest();
    }
}
