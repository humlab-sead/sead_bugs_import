package se.sead.sitelocations;

import se.sead.bugsimport.sitelocations.bugsmodel.BugsSiteLocation;

import java.util.Arrays;
import java.util.List;

public class ExpectedBugsData {

    static final List<BugsSiteLocation> EXPECTED_BUGS_DATA =
            Arrays.asList(
                    createSiteLocation("SITE000001", "Country", "Region"),
                    createSiteLocation("SITE000002", "Country", "Region"),
                    createSiteLocation("SITE000003", "Other country", "Region"),
                    createSiteLocation("SITE000004", "Country", "Other region"),
                    createSiteLocation("SITE000005", "Country", "Region"),
                    createSiteLocation("SITE000006", "Other country", "Other region"),
                    createSiteLocation("SITE000007", "Skåne", "Møn"),
                    createSiteLocation("SITE000008", "Country", "Region type"),
                    createSiteLocation("SITE000009", "Country", "Other region"),
                    createSiteLocation("SITE000010", "Country", "Other region"),
                    createSiteLocation("SITE000011", "Nonexisting", "Region"),
                    createSiteLocation("SITE000012", "Country", "Nonexisting"),
                    createSiteLocation("SITE000016", "Country", "Region"),
                    createSiteLocation("SITE000013", null, "Region"),
                    createSiteLocation("SITE000014", "Country", null),
                    createSiteLocation("SITE000015", "Country", "Region"),
                    createSiteLocation("SITE000017", "Nonexisting", "Region")
            );

    private static BugsSiteLocation createSiteLocation(
            String siteCode,
            String country,
            String region
    ) {
        BugsSiteLocation site = new BugsSiteLocation();
        site.setSiteCode(siteCode);
        site.setCountry(country);
        site.setRegion(region);
        return site;
    }
}
