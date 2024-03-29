package se.sead.ecocodedefinition.koch;

import se.sead.bugsimport.ecocodedefinition.koch.bugsmodel.EcoDefKoch;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.testutils.AssertHelper;
import se.sead.testutils.BugsTracesAndErrorsVerification;

import java.util.List;

class LogVerifier implements BugsTracesAndErrorsVerification.LogVerificationCallback<EcoDefKoch> {

    private AssertHelper assertHelper;

    LogVerifier(){
        assertHelper = new AssertHelper("tbl_ecocode_definitions");
    }

    @Override
    public void verifyLogData(EcoDefKoch bugsData, List<BugsTrace> traces, List<BugsError> errors) {
        switch(bugsData.getBugsKochCode()){
            case "Exists":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertPrestoredTrace(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "New":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertInserts(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "NoG":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No group specified");
                break;
            case "NoG2":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No group found for code");
                break;
            case "NoN":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertInserts(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "Upd":
                assertHelper.assertSize(traces, 2);
                assertHelper.assertPrestoredTrace(traces, 2);
                assertHelper.assertUpdates(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "UpdE":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertPrestoredTrace(traces, 3);
                assertHelper.assertContainsError(errors, SeadDataFromTraceHelper.SEAD_DATA_HAS_BEEN_UPDATED_SINCE_LAST_BUGS_IMPORT);
                break;
        }
    }
}
