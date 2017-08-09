package se.sead.ecocodes;

import se.sead.bugs.TraceableBugsData;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.testutils.AssertHelper;
import se.sead.testutils.BugsTracesAndErrorsVerification;

import java.util.List;

public abstract class LogVerifier<T extends TraceableBugsData> implements BugsTracesAndErrorsVerification.LogVerificationCallback<T> {

    private AssertHelper assertHelper;

    protected LogVerifier(){
        assertHelper = new AssertHelper("tbl_ecocodes");
    }

    @Override
    public void verifyLogData(T bugsData, List<BugsTrace> traces, List<BugsError> errors) {
        switch(bugsData.compressToString()){
            case "{1.0,Def}":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertPrestoredTrace(traces, 4);
                assertHelper.assertEmpty(errors);
                break;
            case "{1.0,null}":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, getNoCodeSpecifiedMessage());
                break;
            case "{1.0,Error}":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, getNoCodeFoundMessage());
                break;
            case "{2.0,Def 2}":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertInserts(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "{3.0,Def}":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertPrestoredTrace(traces, 5);
                assertHelper.assertEmpty(errors);
                break;
            case "{3.0,Def 2}":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertInserts(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "{2.0,def 3}":
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

    protected abstract String getNoCodeSpecifiedMessage();
    protected abstract String getNoCodeFoundMessage();
}
