package se.sead.sitelocations;

import se.sead.bugsimport.sitelocations.bugsmodel.BugsSiteLocation;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.testutils.AssertHelper;
import se.sead.testutils.BugsTracesAndErrorsVerification;

import java.util.List;

public class SiteLocationTracesAndErrors implements BugsTracesAndErrorsVerification.LogVerificationCallback<BugsSiteLocation> {

    private AssertHelper siteLocationHelper;
    private AssertHelper locationHelper;
    private boolean canCreateCountries;

    SiteLocationTracesAndErrors(boolean canCreateCountries){
        siteLocationHelper = new AssertHelper("tbl_site_locations");
        locationHelper = new AssertHelper("tbl_locations");
        this.canCreateCountries = canCreateCountries;
    }

    @Override
    public void verifyLogData(BugsSiteLocation bugsData, List<BugsTrace> traces, List<BugsError> errors) {
        switch(bugsData.getSiteCode()){
            case "SITE000001":
                siteLocationHelper.assertEmpty(traces);
                siteLocationHelper.assertEmpty(errors);
                break;
            case "SITE000002":
                siteLocationHelper.assertInserts(traces, 1);
                siteLocationHelper.assertSize(traces, 1);
                siteLocationHelper.assertEmpty(errors);
                break;
            case "SITE000003":
                siteLocationHelper.assertUpdates(traces,1);
                siteLocationHelper.assertSize(traces, 2);
                siteLocationHelper.assertEmpty(errors);
                break;
            case "SITE000004":
                siteLocationHelper.assertUpdates(traces, 1);
                siteLocationHelper.assertSize(traces, 2);
                siteLocationHelper.assertEmpty(errors);
                break;
            case "SITE000005":
                siteLocationHelper.assertInserts(traces, 2);
                siteLocationHelper.assertSize(traces, 2);
                siteLocationHelper.assertEmpty(errors);
                break;
            case "SITE000006":
                siteLocationHelper.assertUpdates(traces, 2);
                siteLocationHelper.assertSize(traces, 4);
                siteLocationHelper.assertEmpty(errors);
                break;
            case "SITE000007":
                siteLocationHelper.assertInserts(traces, 1);
                siteLocationHelper.assertSize(traces, 1);
                siteLocationHelper.assertEmpty(errors);
                break;
            case "SITE000008":
                siteLocationHelper.assertUpdates(traces, 1);
                siteLocationHelper.assertSize(traces, 2);
                siteLocationHelper.assertEmpty(errors);
                break;
            case "SITE000009":
                siteLocationHelper.assertEmpty(traces);
                siteLocationHelper.assertContainsError(errors, "Sead data has been updated since last bugs import");
                siteLocationHelper.assertSize(errors, 1);
                break;
            case "SITE000010":
                siteLocationHelper.assertEmpty(traces);
                siteLocationHelper.assertContainsError(errors, "Sead site has been updated since last bugs import");
                siteLocationHelper.assertSize(errors, 2);
                break;
            case "SITE000011":
                if(canCreateCountries){
                    siteLocationHelper.assertInserts(traces, 2);
                    locationHelper.assertInserts(traces, 1);
                    siteLocationHelper.assertSize(traces, 3);
                    siteLocationHelper.assertEmpty(errors);
                } else {
                    siteLocationHelper.assertEmpty(traces);
                    siteLocationHelper.assertContainsError(errors, "Creation of new country locations not allowed");
                    siteLocationHelper.assertSize(errors, 1);
                }
                break;
            case "SITE000012":
                siteLocationHelper.assertInserts(traces, 2);
                locationHelper.assertInserts(traces, 1);
                siteLocationHelper.assertSize(traces, 3);
                siteLocationHelper.assertEmpty(errors);
                break;
            case "SITE000013":
            case "SITE000014":
                siteLocationHelper.assertEmpty(traces);
                siteLocationHelper.assertContainsError(errors, "Location name cannot be empty");
                siteLocationHelper.assertSize(errors, 1);
                break;
            case "SITE000015":
                siteLocationHelper.assertInserts(traces, 2);
                siteLocationHelper.assertSize(traces, 2);
                siteLocationHelper.assertEmpty(errors);
                break;
            case "SITE000016":
                siteLocationHelper.assertEmpty(traces);
                siteLocationHelper.assertContainsError(errors, "Could not find imported site");
                siteLocationHelper.assertSize(errors, 1);
                break;
        }
    }
}
