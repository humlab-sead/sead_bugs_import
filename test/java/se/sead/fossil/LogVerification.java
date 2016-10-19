package se.sead.fossil;

import se.sead.bugsimport.fossil.bugsmodel.Fossil;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.testutils.AssertHelper;
import se.sead.testutils.BugsTracesAndErrorsVerification;

import java.util.List;

class LogVerification implements BugsTracesAndErrorsVerification.LogVerificationCallback<Fossil> {

    private AssertHelper abundanceAssertHelper;
    private AssertHelper datasetHelper;
    private AssertHelper analysisEntityHelper;
    private AssertHelper listAsserter;

    LogVerification(){
        abundanceAssertHelper = new AssertHelper("tbl_abundances");
        datasetHelper = new AssertHelper("tbl_datasets");
        analysisEntityHelper = new AssertHelper("tbl_analysis_entities");
        listAsserter = new AssertHelper("");
    }

    @Override
    public void verifyLogData(Fossil bugsData, List<BugsTrace> traces, List<BugsError> errors) {
        switch(bugsData.getFossilBugsCODE()){
            case "FOSI000001":
                listAsserter.assertEmpty(traces);
                listAsserter.assertContainsError(errors, "No sample specified");
                break;
            case "FOSI000002":
                listAsserter.assertEmpty(traces);
                listAsserter.assertContainsError(errors, "No species code specified");
                break;
            case "FOSI000003":
                listAsserter.assertEmpty(traces);
                listAsserter.assertContainsError(errors, "No species found for code");
                break;
            case "FOSI000004":
                listAsserter.assertEmpty(traces);
                listAsserter.assertContainsError(errors, "No sample found for code");
                break;
            case "FOSI000005":
                listAsserter.assertSize(traces, 3);
                abundanceAssertHelper.assertPrestoredTrace(traces, 12);
                datasetHelper.assertPrestoredTrace(traces, 10);
                analysisEntityHelper.assertPrestoredTrace(traces, 11);
                listAsserter.assertEmpty(errors);
                break;
            case "FOSI000006":
                listAsserter.assertSize(traces, 2);
                abundanceAssertHelper.assertPrestoredTrace(traces, 13);
                abundanceAssertHelper.assertUpdates(traces, 1);
                listAsserter.assertEmpty(errors);
                break;
            case "FOSI000007":
                listAsserter.assertSize(traces, 2);
                abundanceAssertHelper.assertInserts(traces, 1);
                analysisEntityHelper.assertInserts(traces, 1);
                listAsserter.assertEmpty(errors);
                break;
            case "FOSI000008":
                listAsserter.assertSize(traces, 1);
                abundanceAssertHelper.assertInserts(traces, 1);
                listAsserter.assertEmpty(errors);
                break;
            case "FOSI000009":
                listAsserter.assertSize(traces, 2);
                abundanceAssertHelper.assertInserts(traces, 1);
                analysisEntityHelper.assertInserts(traces, 1);
                listAsserter.assertEmpty(errors);
                break;
            case "FOSI000010":
                listAsserter.assertEmpty(traces);
                listAsserter.assertContainsError(errors, "Unsupported countsheet data type");
                break;
            case "FOSI000011":
                listAsserter.assertSize(traces, 3);
                abundanceAssertHelper.assertInserts(traces, 1);
                analysisEntityHelper.assertInserts(traces, 1);
                datasetHelper.assertInserts(traces, 1);
                listAsserter.assertEmpty(errors);
                break;
            case "FOSI000012":
                listAsserter.assertSize(traces, 1);
                abundanceAssertHelper.assertInserts(traces, 1);
                listAsserter.assertEmpty(errors);
                break;
        }
    }
}
