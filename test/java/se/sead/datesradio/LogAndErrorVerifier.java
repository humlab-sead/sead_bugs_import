package se.sead.datesradio;

import se.sead.bugsimport.datesradio.bugsmodel.DatesRadio;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.testutils.AssertHelper;
import se.sead.testutils.BugsTracesAndErrorsVerification;

import java.util.List;

public class LogAndErrorVerifier implements BugsTracesAndErrorsVerification.LogVerificationCallback<DatesRadio> {

    private AssertHelper datasetAssertHelper;
    private AssertHelper analysisEntityAssertHelper;
    private AssertHelper geochronologyAssertHelper;
    private AssertHelper listHelper;

    LogAndErrorVerifier(){
        listHelper = new AssertHelper("");
        datasetAssertHelper = new AssertHelper("tbl_datasets");
        analysisEntityAssertHelper = new AssertHelper("tbl_analysis_entities");
        geochronologyAssertHelper = new AssertHelper("tbl_geochronology");
    }

    @Override
    public void verifyLogData(DatesRadio bugsData, List<BugsTrace> traces, List<BugsError> errors) {
        switch(bugsData.getDateCode()){
            case "DATE000001":
                datasetAssertHelper.assertPrestoredTrace(traces, 6);
                analysisEntityAssertHelper.assertPrestoredTrace(traces, 7);
                geochronologyAssertHelper.assertPrestoredTrace(traces, 8);
                listHelper.assertEmpty(errors);
                break;
            case "DATE000002":
                listHelper.assertEmpty(errors);
                datasetAssertHelper.assertInserts(traces, 1);
                analysisEntityAssertHelper.assertInserts(traces, 1);
                geochronologyAssertHelper.assertInserts(traces, 1);
                break;
            case "DATE000003":
                listHelper.assertEmpty(errors);
                listHelper.assertSize(traces, 4);
                datasetAssertHelper.assertPrestoredTrace(traces, 9);
                analysisEntityAssertHelper.assertPrestoredTrace(traces, 10);
                geochronologyAssertHelper.assertPrestoredTrace(traces, 11);
                geochronologyAssertHelper.assertUpdates(traces, 1);
                break;
            case "DATE000004":
                listHelper.assertSize(traces, 3);
                listHelper.assertEmpty(errors);
                datasetAssertHelper.assertPrestoredTrace(traces, 12);
                analysisEntityAssertHelper.assertPrestoredTrace(traces, 13);
                geochronologyAssertHelper.assertPrestoredTrace(traces, 14);
                break;
            case "DATE000006":
                listHelper.assertEmpty(errors);
                listHelper.assertSize(traces, 3);
                datasetAssertHelper.assertInserts(traces, 1);
                analysisEntityAssertHelper.assertInserts(traces, 1);
                geochronologyAssertHelper.assertInserts(traces, 1);
                break;
            case "DATE000007":
                assertErrorMessage(traces, errors, "No date found");
                break;
            case "DATE000008":
                assertErrorMessage(traces, errors, "Unknown uncertainty symbol");
                break;
            case "DATE000005":
            case "DATE000009":
                assertErrorMessage(traces, errors, "No method found");
                break;
            case "DATE000010":
                assertErrorMessage(traces, errors, "No sample found");
                break;
            case "DATE000011":
                assertErrorMessage(traces, errors, "No lab found");
                break;
            case "DATE000012":
                listHelper.assertSize(traces, 3);
                datasetAssertHelper.assertPrestoredTrace(traces, 15);
                analysisEntityAssertHelper.assertPrestoredTrace(traces, 16);
                geochronologyAssertHelper.assertPrestoredTrace(traces, 17);
                geochronologyAssertHelper.assertContainsError(errors, SeadDataFromTraceHelper.SEAD_DATA_HAS_BEEN_UPDATED_SINCE_LAST_BUGS_IMPORT);
                break;
        }
    }

    private void assertErrorMessage(List<BugsTrace> traces, List<BugsError> errors, String errorMessage){
        geochronologyAssertHelper.assertContainsError(errors, errorMessage);
        listHelper.assertEmpty(traces);
    }
}
