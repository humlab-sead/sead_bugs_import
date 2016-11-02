package se.sead.datesperiod;

import se.sead.bugsimport.datesperiod.bugsmodel.DatesPeriod;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.testutils.AssertHelper;
import se.sead.testutils.BugsTracesAndErrorsVerification;

import java.util.List;

class LogVerification implements BugsTracesAndErrorsVerification.LogVerificationCallback<DatesPeriod> {

    private AssertHelper assertHelper;

    LogVerification(){
        assertHelper = new AssertHelper("tbl_relative_dates");
    }

    @Override
    public void verifyLogData(DatesPeriod bugsData, List<BugsTrace> traces, List<BugsError> errors) {
        switch(bugsData.getPeriodDateCode()){
            case "PERI000001":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No dating method found");
                break;
            case "PERI000002":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No period code");
                break;
            case "PERI000003":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No period found for code");
                break;
            case "PERI000004":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No sample specified");
                break;
            case "PERI000005":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No sample found for code");
                break;
            case "PERI000006":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No uncertainty found for definition");
                break;
            case "PERI000007":
            case "PERI000009":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertInserts(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "PERI000011":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertPrestoredTrace(traces, 5);
                assertHelper.assertEmpty(errors);
                break;
            case "PERI000012":
                assertHelper.assertSize(traces, 2);
                assertHelper.assertPrestoredTrace(traces, 6);
                assertHelper.assertUpdates(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "PERI000013":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertPrestoredTrace(traces, 7);
                assertHelper.assertContainsError(errors, SeadDataFromTraceHelper.SEAD_DATA_HAS_BEEN_UPDATED_SINCE_LAST_BUGS_IMPORT);
                break;
        }
    }
}
