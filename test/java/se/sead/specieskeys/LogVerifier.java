package se.sead.specieskeys;

import se.sead.bugsimport.specieskeys.bugsmodel.Keys;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.testutils.AssertHelper;
import se.sead.testutils.BugsTracesAndErrorsVerification;

import java.util.List;

class LogVerifier implements BugsTracesAndErrorsVerification.LogVerificationCallback<Keys> {

    private AssertHelper assertHelper;

    LogVerifier(){
        assertHelper = new AssertHelper("tbl_text_identification_keys");
    }

    @Override
    public void verifyLogData(Keys bugsData, List<BugsTrace> traces, List<BugsError> errors) {
        String specification = "" + bugsData.getCode() + bugsData.getData();
        switch(specification){
            case "1.0010001Key to Subfamilies of Carabidae":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertPrestoredTrace(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "1.0010122Key to Cicindelinae":
            case "1.0020082Large to very large beetles often of brilliant colouration; hind angles of pronotum obtuse .L.16-30mm.":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertInserts(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "1.0010122Error, no reference":
            case "1.0040342Error, references does not exist. Not added.":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No reference found for: " + bugsData.getRef());
                break;
            case "9999.000001Error, species does not exist. Not added.":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No species found for code: " + bugsData.getCode());
                break;
        }
    }
}
