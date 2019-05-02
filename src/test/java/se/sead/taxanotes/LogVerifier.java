package se.sead.taxanotes;

import se.sead.bugsimport.taxanotes.bugsmodel.TaxoNotes;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.testutils.AssertHelper;
import se.sead.testutils.BugsTracesAndErrorsVerification;

import java.util.List;

class LogVerifier implements BugsTracesAndErrorsVerification.LogVerificationCallback<TaxoNotes> {

    private AssertHelper assertHelper;

    LogVerifier(){
        assertHelper = new AssertHelper("tbl_taxonomy_notes");
    }

    @Override
    public void verifyLogData(TaxoNotes bugsData, List<BugsTrace> traces, List<BugsError> errors) {
        Double code = bugsData.getCode();
        String switchCarrier;
        if(code != null ){
            switchCarrier = code.toString();
        } else {
            switchCarrier = "null";
        }
        switch(switchCarrier){
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
                assertHelper.assertContainsError(errors, "No note provided");
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
