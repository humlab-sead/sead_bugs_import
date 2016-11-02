package se.sead.rdb;

import se.sead.bugsimport.rdb.bugsmodel.BugsRDB;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.testutils.AssertHelper;
import se.sead.testutils.BugsTracesAndErrorsVerification;

import java.util.List;

public class RdbLogTraceAndErrorVerifier implements BugsTracesAndErrorsVerification.LogVerificationCallback<BugsRDB> {

    private AssertHelper assertHelper;

    RdbLogTraceAndErrorVerifier(){
        assertHelper = new AssertHelper("tbl_rdb");
    }

    @Override
    public void verifyLogData(BugsRDB bugsData, List<BugsTrace> traces, List<BugsError> errors) {
        switch(bugsData.compressToString()){
            case "{1.0010001,UK,1}":
                assertHelper.assertPrestoredTrace(traces, 6);
                assertHelper.assertEmpty(errors);
                break;
            case "{1.0010001,Svw,1}":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No country specified");
                break;
            case "{1.0010122,Swe,2}":
                assertHelper.assertEmpty(traces);
                assertHelper.assertEmpty(errors);
                break;
            case "{99,UK,2}":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No species found");
                break;
            case "{1.0010001,UK,3}":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No RDB code found");
                break;
            case "{1.0010001,Swe,2}":
                assertHelper.assertInserts(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "{1.0010123,UK,1}":
                assertHelper.assertPrestoredTrace(traces, 8);
                assertHelper.assertContainsError(errors, "Sead data has been updated since last bugs import");
                break;
            case "{1.0010123,UK,2}":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, SeadDataFromTraceHelper.SEAD_DATA_HAS_BEEN_UPDATED_SINCE_LAST_BUGS_IMPORT);
                break;
            case "{null,Swe,1}":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No species specified");
                break;
            case "{1.0010122,null,1}":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No country specified");
                break;
            case "{1.0010122,Swe,null}":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No RDB code specified");
                break;
        }
    }
}
