package se.sead.country;

import se.sead.bugsimport.locations.country.bugsmodel.Country;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.testutils.AssertHelper;
import se.sead.testutils.BugsTracesAndErrorsVerification;

import java.util.List;

class LogVerifier implements BugsTracesAndErrorsVerification.LogVerificationCallback<Country> {

    private AssertHelper assertHelper;

    LogVerifier(){
        assertHelper = new AssertHelper("tbl_locations");
    }

    @Override
    public void verifyLogData(Country bugsData, List<BugsTrace> traces, List<BugsError> errors) {
        switch(bugsData.getCountryCode()){
            case "Eg":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertPrestoredTrace(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "Ger":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertUpdates(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "Int":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertInserts(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "Swe":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "Location name cannot be empty");
                break;
        }
    }
}
