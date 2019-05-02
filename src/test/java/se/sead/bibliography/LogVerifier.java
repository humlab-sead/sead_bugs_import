package se.sead.bibliography;

import se.sead.bugsimport.bibliography.bugsmodel.BugsBiblio;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.testutils.AssertHelper;
import se.sead.testutils.BugsTracesAndErrorsVerification;

import java.util.List;

public class LogVerifier implements BugsTracesAndErrorsVerification.LogVerificationCallback<BugsBiblio> {

    private AssertHelper assertHelper;

    LogVerifier(){
        assertHelper = new AssertHelper("tbl_biblio");
    }

    @Override
    public void verifyLogData(BugsBiblio bugsData, List<BugsTrace> traces, List<BugsError> errors) {
        switch(bugsData.getReference()){
            case "Exists 2000":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertPrestoredTrace(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "Exists 2001":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertPrestoredTrace(traces, 2);
                assertHelper.assertContainsError(errors, SeadDataFromTraceHelper.SEAD_DATA_HAS_BEEN_UPDATED_SINCE_LAST_BUGS_IMPORT);
                break;
            case "NonExists 2000":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertInserts(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "Exists 2012":
                assertHelper.assertEmpty(traces);
                assertHelper.assertEmpty(errors);
                break;
        }
    }
}
