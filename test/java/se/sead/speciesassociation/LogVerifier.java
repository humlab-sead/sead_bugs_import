package se.sead.speciesassociation;

import se.sead.bugsimport.speciesassociation.bugsmodel.BugsSpeciesAssociation;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.testutils.AssertHelper;
import se.sead.testutils.BugsTracesAndErrorsVerification;

import java.util.List;

class LogVerifier implements BugsTracesAndErrorsVerification.LogVerificationCallback<BugsSpeciesAssociation> {

    private AssertHelper assertHelper;

    LogVerifier(){
        assertHelper = new AssertHelper("tbl_species_associations");
    }

    @Override
    public void verifyLogData(BugsSpeciesAssociation bugsData, List<BugsTrace> traces, List<BugsError> errors) {
        switch(bugsData.getSpeciesAssociationID()){
            case 1:
                assertHelper.assertSize(traces, 1);
                assertHelper.assertPrestoredTrace(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case 2:
            case 3:
                assertHelper.assertSize(traces, 1);
                assertHelper.assertInserts(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case 4:
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No target association");
                break;
            case 5:
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No source association");
                break;
            case 6:
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "Target species not found");
                break;
            case 7:
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "Source species not found");
                break;
            case 9:
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "Reference not found");
                break;
            case 10:
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "Association type not found");
                break;
            case 11:
                assertHelper.assertSize(traces, 2);
                assertHelper.assertPrestoredTrace(traces, 2);
                assertHelper.assertUpdates(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case 12:
                assertHelper.assertSize(traces, 1);
                assertHelper.assertPrestoredTrace(traces, 3);
                assertHelper.assertContainsError(errors, SeadDataFromTraceHelper.SEAD_DATA_HAS_BEEN_UPDATED_SINCE_LAST_BUGS_IMPORT);
                break;
        }
    }
}
