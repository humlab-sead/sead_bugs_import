package se.sead.rdbsystems;

import se.sead.bugsimport.rdbsystems.bugsmodel.BugsRDBSystem;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.testutils.AssertHelper;
import se.sead.testutils.BugsTracesAndErrorsVerification;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class LogTraceAndErrorVerifier implements BugsTracesAndErrorsVerification.LogVerificationCallback<BugsRDBSystem> {

    private AssertHelper rdbSystemAssertHelper;

    LogTraceAndErrorVerifier(){
        rdbSystemAssertHelper = new AssertHelper("tbl_rdb_systems");
    }

    @Override
    public void verifyLogData(BugsRDBSystem bugsData, List<BugsTrace> traces, List<BugsError> errors) {
        switch(bugsData.getRdbSystemCode()){
            case 1:
                rdbSystemAssertHelper.assertEmpty(errors);
                rdbSystemAssertHelper.assertPrestoredTrace(traces, 1);
                break;
            case 2:
            case 3:
            case 4:
                rdbSystemAssertHelper.assertInserts(traces, 1);
                rdbSystemAssertHelper.assertEmpty(errors);
                break;
            case 5:
                rdbSystemAssertHelper.assertPrestoredTrace(traces, 2);
                rdbSystemAssertHelper.assertUpdates(traces, 1);
                rdbSystemAssertHelper.assertEmpty(errors);
                break;
            case 6:
                rdbSystemAssertHelper.assertEmpty(traces);
                rdbSystemAssertHelper.assertContainsError(errors, "Country cannot be empty");
                break;
            case 7:
                rdbSystemAssertHelper.assertEmpty(traces);
                rdbSystemAssertHelper.assertContainsError(errors, "No system name found");
                break;
            case 8:
                rdbSystemAssertHelper.assertEmpty(traces);
                rdbSystemAssertHelper.assertContainsError(errors, "Unknown country");
                break;
            case 9:
                rdbSystemAssertHelper.assertPrestoredTrace(traces, 3);
                rdbSystemAssertHelper.assertContainsError(errors, "Sead data has been updated since last bugs import");
                break;
        }
    }
}
