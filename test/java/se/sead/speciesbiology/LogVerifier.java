package se.sead.speciesbiology;

import se.sead.bugsimport.speciesbiology.bugsmodel.Biology;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.testutils.AssertHelper;
import se.sead.testutils.BugsTracesAndErrorsVerification;

import java.util.List;

class LogVerifier implements BugsTracesAndErrorsVerification.LogVerificationCallback<Biology> {

    private AssertHelper assertHelper;

    LogVerifier(){
        assertHelper = new AssertHelper("tbl_text_biology");
    }

    @Override
    public void verifyLogData(Biology bugsData, List<BugsTrace> traces, List<BugsError> errors) {
        Double code = bugsData.getCode();
        String switchOperator;
        if(code != null){
            switchOperator = code.toString();
        } else {
            switchOperator = "null";
        }
        switch(switchOperator){
            case "1.0":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertPrestoredTrace(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "2.0":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertInserts(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "3.0":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "Reference does not exist");
                break;
            case "4.0":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "Species does not exist");
                break;
            case "5.0":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No data provided");
                break;
            case "6.0":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No reference provided");
                break;
            case "null":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No species provided");
                break;
        }
    }
}
