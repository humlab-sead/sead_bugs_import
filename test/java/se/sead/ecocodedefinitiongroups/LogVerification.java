package se.sead.ecocodedefinitiongroups;

import se.sead.bugsimport.ecocodedefinitiongroups.bugsmodel.EcoDefGroups;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.testutils.AssertHelper;
import se.sead.testutils.BugsTracesAndErrorsVerification;

import java.util.List;

public class LogVerification implements BugsTracesAndErrorsVerification.LogVerificationCallback<EcoDefGroups> {

    private AssertHelper assertHelper;

    LogVerification(){
        assertHelper = new AssertHelper("tbl_ecocode_groups");
    }

    @Override
    public void verifyLogData(EcoDefGroups bugsData, List<BugsTrace> traces, List<BugsError> errors) {
        switch(bugsData.getEcoGroupCode()){
            case "Ext":
                assertHelper.assertSize(traces, 1);
                assertHelper.assertPrestoredTrace(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "Fai":
                assertHelper.assertEmpty(traces);
                assertHelper.assertContainsError(errors, "No ecocode group name");
                break;
            case "New":
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
        }
    }
}
