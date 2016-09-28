package se.sead.attributes;

import se.sead.bugsimport.attributes.bugsmodel.BugsAttributes;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.testutils.AssertHelper;
import se.sead.testutils.BugsTracesAndErrorsVerification;

import java.util.List;

public class TracesAndErrorVerifier implements BugsTracesAndErrorsVerification.LogVerificationCallback<BugsAttributes> {

    private AssertHelper assertHelper;

    TracesAndErrorVerifier(){
        assertHelper = new AssertHelper("tbl_taxa_measured_attributes");
    }

    @Override
    public void verifyLogData(BugsAttributes bugsData, List<BugsTrace> traces, List<BugsError> errors) {
        switch(bugsData.compressToString()){
            case "{1.0,Stored,Max,1,mm}":
                assertHelper.assertPrestoredTrace(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "{1.0,Stored,Min,1,mm}":
                assertHelper.assertPrestoredTrace(traces, 2);
                assertHelper.assertEmpty(errors);
                break;
            case "{2.0,Stored,Max,1,mm}":
                assertHelper.assertPrestoredTrace(traces, 3);
                assertHelper.assertEmpty(errors);
                break;
            case "{4.0,Stored w/o trace,Max,1,mm}":
            case "{4.0,Stored w/o trace,Min,1,mm}":
                assertHelper.assertEmpty(traces);
                assertHelper.assertEmpty(errors);
                break;
            case "{6.0,Stored,Max,1,mm}":
                assertHelper.assertPrestoredTrace(traces, 6);
                assertHelper.assertEmpty(errors);
                break;
            case "{1.0,,Max,1,mm}":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No attribute type");
                break;
            case "{1.0,No value,Max,null,mm}":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No attribute value");
                break;
            case "{1.0,No unit,Max,1,null}":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No attribute unit");
                break;
            case "{2.0,New,Min,1,mm}":
            case "{3.0,New,Max,1,mm}":
            case "{3.0,New,Min,1,mm}":
                assertHelper.assertInserts(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "{5.0,Changed,Max,2,mm}":
            case "{5.0,Changed,Min,2,mm}":
            case "{6.0,Changed,Min,2,mm}":
                assertHelper.assertUpdates(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "{7.0,Changed after import,Max,2,mm}":
                assertHelper.assertPrestoredTrace(traces, 8);
                assertHelper.assertContainsError(errors, "Sead data has been updated since last bugs import");
                break;
            case "{7.0,Stored,Min,1,mm}":
                assertHelper.assertPrestoredTrace(traces, 9);
                assertHelper.assertContainsError(errors, "Sead data has been updated since last bugs import");
                break;
            case "{99.0,No species exist,Max,1,mm}":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No species found for code");
                break;
        }
    }
}
