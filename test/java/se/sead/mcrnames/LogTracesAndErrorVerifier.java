package se.sead.mcrnames;

import se.sead.bugsimport.mcrnames.bugsmodel.BugsMCRNames;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.testutils.AssertHelper;
import se.sead.testutils.BugsTracesAndErrorsVerification;

import java.util.List;

public class LogTracesAndErrorVerifier implements BugsTracesAndErrorsVerification.LogVerificationCallback<BugsMCRNames> {

    private AssertHelper assertHelper;

    LogTracesAndErrorVerifier(){
        assertHelper = new AssertHelper("tbl_mcr_names");
    }

    @Override
    public void verifyLogData(BugsMCRNames bugsData, List<BugsTrace> traces, List<BugsError> errors) {
        switch(bugsData.getCode().toString()){
            case "1.001002":
                assertHelper.assertPrestoredTrace(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "1.0":
                assertHelper.assertEmpty(traces);
                assertHelper.assertEmpty(errors);
                break;
            case "1.004014":
                assertHelper.assertInserts(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "2.0":
                assertHelper.assertPrestoredTrace(traces, 2);
                assertHelper.assertUpdates(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "3.0":
                assertHelper.assertPrestoredTrace(traces, 3);
                assertHelper.assertContainsError(errors, "Sead data has been updated since last bugs import");
                break;
            case "10.0":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No species found for code");
                break;
        }
    }
}
