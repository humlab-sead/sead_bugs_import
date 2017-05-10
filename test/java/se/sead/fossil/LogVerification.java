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
    private boolean allowDatasetUpdate;

    LogVerification(boolean allowDatasetUpdate){
        abundanceAssertHelper = new AssertHelper("tbl_abundances");
        datasetHelper = new AssertHelper("tbl_datasets");
        analysisEntityHelper = new AssertHelper("tbl_analysis_entities");
        listAsserter = new AssertHelper("");
        this.allowDatasetUpdate = allowDatasetUpdate;
    }

    @Override
    public void verifyLogData(Fossil bugsData, List<BugsTrace> traces, List<BugsError> errors) {
        switch(bugsData.getFossilBugsCODE()){
            case "ERRO000001":
                listAsserter.assertEmpty(traces);
                listAsserter.assertContainsError(errors, "No species specified");
                break;
            case "ERRO000002":
                listAsserter.assertEmpty(traces);
                listAsserter.assertContainsError(errors, "No sample specified");
                break;
            case "ERRO000003":
                listAsserter.assertEmpty(traces);
                listAsserter.assertContainsError(errors, "No species found for code");
                break;
            case "FOSI000001":
                listAsserter.assertEmpty(errors);
                listAsserter.assertSize(traces, 3);
                datasetHelper.assertPrestoredTrace(traces, 10);
                analysisEntityHelper.assertPrestoredTrace(traces, 11);
                abundanceAssertHelper.assertPrestoredTrace(traces, 12);
                break;
            case "FOSI000002":
                listAsserter.assertEmpty(errors);
                listAsserter.assertSize(traces, 3);
                datasetHelper.assertPrestoredTrace(traces, 13);
                analysisEntityHelper.assertPrestoredTrace(traces, 14);
                abundanceAssertHelper.assertPrestoredTrace(traces, 15);
                break;
            case "FOSI000003":
                listAsserter.assertEmpty(errors);
                listAsserter.assertSize(traces, 1);
                abundanceAssertHelper.assertPrestoredTrace(traces, 16);
                break;
            case "FOSI000004":
                listAsserter.assertEmpty(errors);
                listAsserter.assertSize(traces, 3);
                datasetHelper.assertInserts(traces, 1);
                analysisEntityHelper.assertInserts(traces, 1);
                abundanceAssertHelper.assertInserts(traces, 1);
                break;
            case "FOSI000005":
                listAsserter.assertEmpty(errors);
                listAsserter.assertSize(traces, 2);
                datasetHelper.assertUpdates(traces, 1);
                abundanceAssertHelper.assertInserts(traces, 1);
                break;
            case "FOSI000006":
                listAsserter.assertEmpty(errors);
                listAsserter.assertSize(traces, 3);
                datasetHelper.assertPrestoredTrace(traces, 17);
                analysisEntityHelper.assertPrestoredTrace(traces, 18);
                abundanceAssertHelper.assertPrestoredTrace(traces, 19);
                break;
            case "FOSI000007":
                listAsserter.assertEmpty(errors);
                listAsserter.assertSize(traces, 1);
                abundanceAssertHelper.assertPrestoredTrace(traces, 20);
                break;
            case "FOSI000008":
                listAsserter.assertEmpty(errors);
                if(allowDatasetUpdate){
                    listAsserter.assertSize(traces, 1);
                    abundanceAssertHelper.assertInserts(traces, 1);
                } else {
                    listAsserter.assertSize(traces, 3);
                    datasetHelper.assertInserts(traces, 1);
                    analysisEntityHelper.assertInserts(traces, 1);
                    abundanceAssertHelper.assertInserts(traces, 1);
                }
                break;
        }
    }
}
