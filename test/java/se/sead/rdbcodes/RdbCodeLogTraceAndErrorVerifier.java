package se.sead.rdbcodes;

import se.sead.bugsimport.rdbcodes.bugsmodel.BugsRDBCodes;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.testutils.AssertHelper;
import se.sead.testutils.BugsTracesAndErrorsVerification;

import java.util.List;

public class RdbCodeLogTraceAndErrorVerifier implements BugsTracesAndErrorsVerification.LogVerificationCallback<BugsRDBCodes> {

    private AssertHelper rdbCodeAssertHelper;

    RdbCodeLogTraceAndErrorVerifier(){
        rdbCodeAssertHelper = new AssertHelper("tbl_rdb_codes");
    }

    @Override
    public void verifyLogData(BugsRDBCodes bugsData, List<BugsTrace> traces, List<BugsError> errors) {
        switch(bugsData.getRdbCode()){
            case 1:
                rdbCodeAssertHelper.assertInserts(traces, 1);
                rdbCodeAssertHelper.assertEmpty(errors);
                break;
            case 2:
                rdbCodeAssertHelper.assertPrestoredTrace(traces, 5);
                rdbCodeAssertHelper.assertInserts(traces, 0);
                rdbCodeAssertHelper.assertEmpty(errors);
                break;
            case 3:
                rdbCodeAssertHelper.assertEmpty(traces);
                rdbCodeAssertHelper.assertEmpty(errors);
                break;
            case 4:
                rdbCodeAssertHelper.assertPrestoredTrace(traces, 6);
                rdbCodeAssertHelper.assertUpdates(traces, 1);
                rdbCodeAssertHelper.assertEmpty(errors);
                break;
            case 5:
                rdbCodeAssertHelper.assertEmpty(traces);
                rdbCodeAssertHelper.assertContainsError(errors, "Empty category");
                break;
            case 6:
                rdbCodeAssertHelper.assertEmpty(traces);
                rdbCodeAssertHelper.assertContainsError(errors, "No rdb system found");
                break;
            case 7:
                rdbCodeAssertHelper.assertPrestoredTrace(traces, 7);
                rdbCodeAssertHelper.assertContainsError(errors, "Sead data has been updated since last bugs import");
                break;
        }
    }
}
