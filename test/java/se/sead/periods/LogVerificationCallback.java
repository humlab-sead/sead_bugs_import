package se.sead.periods;

import se.sead.bugsimport.periods.bugsmodel.Period;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.testutils.AssertHelper;
import se.sead.testutils.BugsTracesAndErrorsVerification;

import java.util.List;

class LogVerificationCallback implements BugsTracesAndErrorsVerification.LogVerificationCallback<Period> {

    private AssertHelper assertHelper;

    LogVerificationCallback(){
        assertHelper = new AssertHelper("tbl_relative_ages");
    }

    @Override
    public void verifyLogData(Period bugsData, List<BugsTrace> traces, List<BugsError> errors) {
        switch(bugsData.getPeriodCode()){
            case "?":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "IGNORED: This item is ignored");
                break;
            case "ERR_NO_LOC":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No location found");
                break;
            case "ERR_NO_NAME":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No period name given");
                break;
            case "ERR_NO_TYPE":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No type found");
                break;
            case "EXIST":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertPrestoredTrace(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "NO_BP_ETC":
            case "INSERT":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertInserts(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "INSERTCAL":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertInserts(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "U_NEWER_SEAD":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertPrestoredTrace(traces, 2);
                assertHelper.assertContainsError(errors, SeadDataFromTraceHelper.SEAD_DATA_HAS_BEEN_UPDATED_SINCE_LAST_BUGS_IMPORT);
                break;
            case "UPDATE":
                assertHelper.assertSize(traces, 2);
                assertHelper.assertUpdates(traces, 1);
                assertHelper.assertPrestoredTrace(traces, 3);
                assertHelper.assertEmpty(errors);
                break;
        }
    }
}
