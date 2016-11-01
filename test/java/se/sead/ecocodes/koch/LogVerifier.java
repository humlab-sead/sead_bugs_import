package se.sead.ecocodes.koch;

import se.sead.bugsimport.ecocodes.koch.bugsmodel.EcoKoch;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.testutils.AssertHelper;
import se.sead.testutils.BugsTracesAndErrorsVerification;

import java.util.List;

class LogVerifier implements BugsTracesAndErrorsVerification.LogVerificationCallback<EcoKoch> {

    private AssertHelper assertHelper;

    LogVerifier(){
        assertHelper = new AssertHelper("tbl_ecocodes");
    }

    @Override
    public void verifyLogData(EcoKoch bugsData, List<BugsTrace> traces, List<BugsError> errors) {
        switch(bugsData.compressToString()){
            case "{1.0,Def}":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertPrestoredTrace(traces, 3);
                assertHelper.assertEmpty(errors);
                break;
            case "{1.0,null}":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No koch code specified");
                break;
            case "{1.0,Error}":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No koch code found");
                break;
            case "{2.0,Def 2}":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertInserts(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "{3.0,Def}":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertPrestoredTrace(traces, 4);
                assertHelper.assertEmpty(errors);
                break;
            case "{3.0,Def 2}":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertInserts(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "{99.0,Def}":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No species found for code");
                break;
            default:
                throw new AssertionError();
        }
    }
}
