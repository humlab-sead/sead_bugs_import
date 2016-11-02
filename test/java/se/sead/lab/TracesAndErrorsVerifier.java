package se.sead.lab;

import se.sead.bugsimport.lab.bugsmodel.BugsLab;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.testutils.AssertHelper;
import se.sead.testutils.BugsTracesAndErrorsVerification;

import java.util.List;

public class TracesAndErrorsVerifier implements BugsTracesAndErrorsVerification.LogVerificationCallback<BugsLab> {

    private AssertHelper assertHelper;

    TracesAndErrorsVerifier(){
        assertHelper = new AssertHelper("tbl_dating_labs");
    }

    @Override
    public void verifyLogData(BugsLab bugsData, List<BugsTrace> traces, List<BugsError> errors) {
        switch(bugsData.getLabId()){
            case "A":
                assertHelper.assertPrestoredTrace(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "B":
                assertHelper.assertEmpty(traces);
                assertHelper.assertEmpty(errors);
                break;
            case "BC*":
                assertHelper.assertInserts(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "Birm":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No country found");
                break;
            case "Test":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No lab name");
                break;
            case "Test 2":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No country specified");
                break;
            case "Test 3":
                assertHelper.assertUpdates(traces, 1);
                assertHelper.assertPrestoredTrace(traces, 2);
                assertHelper.assertEmpty(errors);
                break;
            case "Test 4":
                assertHelper.assertPrestoredTrace(traces, 3);
                assertHelper.assertContainsError(errors, SeadDataFromTraceHelper.SEAD_DATA_HAS_BEEN_UPDATED_SINCE_LAST_BUGS_IMPORT);
                break;
        }
    }
}
