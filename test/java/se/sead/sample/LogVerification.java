package se.sead.sample;

import se.sead.bugsimport.sample.bugsmodel.BugsSample;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.testutils.AssertHelper;
import se.sead.testutils.BugsTracesAndErrorsVerification;

import java.util.List;

class LogVerification implements BugsTracesAndErrorsVerification.LogVerificationCallback<BugsSample> {

    private AssertHelper sampleAssertHelper;
    private AssertHelper sampleDimensionHelper;

    LogVerification(){
        sampleAssertHelper = new AssertHelper("tbl_physical_samples");
        sampleDimensionHelper = new AssertHelper("tbl_sample_dimensions");
    }

    @Override
    public void verifyLogData(BugsSample bugsData, List<BugsTrace> traces, List<BugsError> errors) {
        switch(bugsData.getSampleCode()){
            case "SAMP000001":
                sampleAssertHelper.assertPrestoredTrace(traces, 4);
                sampleAssertHelper.assertEmpty(errors);
                break;
            case "SAMP000002":
                sampleAssertHelper.assertPrestoredTrace(traces, 5);
                sampleAssertHelper.assertEmpty(errors);
                sampleDimensionHelper.assertPrestoredTrace(traces, 6, 7);
                break;
            case "SAMP000003":
                sampleAssertHelper.assertInserts(traces, 1);
                sampleAssertHelper.assertEmpty(errors);
                sampleDimensionHelper.assertInserts(traces, 2);
                break;
            case "SAMP000004":
                sampleAssertHelper.assertUpdates(traces, 1);
                sampleAssertHelper.assertEmpty(errors);
                sampleDimensionHelper.assertUpdates(traces, 1);
                break;
            case "SAMP000005":
                sampleAssertHelper.assertPrestoredTrace(traces, 11);
                sampleAssertHelper.assertEmpty(errors);
                sampleAssertHelper.assertPrestoredTrace(traces, 12, 13);
                sampleDimensionHelper.assertDeletes(traces, 2);
                break;
            case "SAMP000008":
                sampleAssertHelper.assertEmpty(traces);
                sampleAssertHelper.assertContainsError(errors, "No countsheet found");
                break;
            case "SAMP000009":
                sampleAssertHelper.assertEmpty(traces);
                sampleAssertHelper.assertContainsError(errors, "No countsheet specified");
                break;
            case "SAMP000010":
                sampleAssertHelper.assertPrestoredTrace(traces, 14);
                sampleAssertHelper.assertContainsError(errors, SeadDataFromTraceHelper.SEAD_DATA_HAS_BEEN_UPDATED_SINCE_LAST_BUGS_IMPORT);
                break;
            case "SAMP000011":
                sampleAssertHelper.assertPrestoredTrace(traces, 15);
                sampleDimensionHelper.assertPrestoredTrace(traces, 16, 17);
                sampleDimensionHelper.assertContainsError(errors, SeadDataFromTraceHelper.SEAD_DATA_HAS_BEEN_UPDATED_SINCE_LAST_BUGS_IMPORT);
                break;
            case "SAMP000012":
                sampleAssertHelper.assertInserts(traces, 1);
                sampleAssertHelper.assertEmpty(errors);
                break;
            case "SAMP000013":
                sampleAssertHelper.assertEmpty(traces);
                sampleAssertHelper.assertContainsError(errors, "X or Y values is probably an error");
                break;
        }
    }
}
