package se.sead.datesperiod;

import se.sead.bugsimport.datesperiod.bugsmodel.DatesPeriod;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.testutils.AssertHelper;
import se.sead.testutils.BugsTracesAndErrorsVerification;

import java.util.List;

class LogVerification implements BugsTracesAndErrorsVerification.LogVerificationCallback<DatesPeriod> {

    private AssertHelper relativeDatesAssertHelper;
    private AssertHelper datasetAssertHelper;
    private AssertHelper analysisAssertHelper;

    LogVerification(){
        relativeDatesAssertHelper = new AssertHelper("tbl_relative_dates");
        datasetAssertHelper = new AssertHelper("tbl_datasets");
        analysisAssertHelper = new AssertHelper("tbl_analysis_entities");
    }

    @Override
    public void verifyLogData(DatesPeriod bugsData, List<BugsTrace> traces, List<BugsError> errors) {
        switch(bugsData.getPeriodDateCode()){
            case "PERI000001":
                relativeDatesAssertHelper.assertEmpty(traces);
                relativeDatesAssertHelper.assertContainsError(errors, "No dating method found");
                break;
            case "PERI000002":
                relativeDatesAssertHelper.assertEmpty(traces);
                relativeDatesAssertHelper.assertContainsError(errors, "No period code");
                break;
            case "PERI000003":
                relativeDatesAssertHelper.assertEmpty(traces);
                relativeDatesAssertHelper.assertContainsError(errors, "No period found for code");
                break;
            case "PERI000004":
                relativeDatesAssertHelper.assertEmpty(traces);
                relativeDatesAssertHelper.assertContainsError(errors, "No sample specified");
                break;
            case "PERI000005":
                relativeDatesAssertHelper.assertEmpty(traces);
                relativeDatesAssertHelper.assertContainsError(errors, "No sample found for code");
                break;
            case "PERI000006":
                relativeDatesAssertHelper.assertEmpty(traces);
                relativeDatesAssertHelper.assertContainsError(errors, "No uncertainty found for definition");
                break;
            case "PERI000007":
            case "PERI000009":
            case "PERI000014":
            case "PERI000015":
                relativeDatesAssertHelper.assertSize(traces, 3);
                relativeDatesAssertHelper.assertInserts(traces, 1);
                analysisAssertHelper.assertInserts(traces, 1);
                datasetAssertHelper.assertInserts(traces, 1);
                relativeDatesAssertHelper.assertEmpty(errors);
                break;
            case "PERI000011":
                relativeDatesAssertHelper.assertSize(traces, 1);
                relativeDatesAssertHelper.assertPrestoredTrace(traces, 7);
                relativeDatesAssertHelper.assertEmpty(errors);
                break;
            case "PERI000012":
                relativeDatesAssertHelper.assertSize(traces, 3);
                relativeDatesAssertHelper.assertPrestoredTrace(traces, 8);
                relativeDatesAssertHelper.assertUpdates(traces, 1);
                datasetAssertHelper.assertUpdates(traces, 1);
                relativeDatesAssertHelper.assertEmpty(errors);
                break;
            case "PERI000013":
                relativeDatesAssertHelper.assertSize(traces, 1);
                relativeDatesAssertHelper.assertPrestoredTrace(traces, 9);
                relativeDatesAssertHelper.assertContainsError(errors, SeadDataFromTraceHelper.SEAD_DATA_HAS_BEEN_UPDATED_SINCE_LAST_BUGS_IMPORT);
                break;
        }
    }
}
