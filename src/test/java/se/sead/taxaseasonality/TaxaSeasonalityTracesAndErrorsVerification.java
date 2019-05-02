package se.sead.taxaseasonality;

import se.sead.bugsimport.taxaseasonality.bugsmodel.SeasonActiveAdult;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.testutils.AssertHelper;
import se.sead.testutils.BugsTracesAndErrorsVerification;

import java.util.List;

public class TaxaSeasonalityTracesAndErrorsVerification implements BugsTracesAndErrorsVerification.LogVerificationCallback<SeasonActiveAdult> {

    private AssertHelper assertHelper;

    TaxaSeasonalityTracesAndErrorsVerification(){
        assertHelper = new AssertHelper("tbl_taxa_seasonality");
    }

    @Override
    public void verifyLogData(SeasonActiveAdult bugsData, List<BugsTrace> traces, List<BugsError> errors) {
        switch(Double.toString(bugsData.getCode())){
            case "1.0000001":
            case "1.0000002":
            case "1.0000003":
            case "1.0000004":
            case "1.0000005":
            case "1.0000006":
            case "1.0000007":
            case "1.0000008":
            case "1.0000009":
            case "1.0000010":
            case "1.0000011":
            case "1.0000012":
            case "1.0000015":
            case "1.0000016":
            case "1.0000017":
            case "1.0000022":
                assertHelper.assertInserts(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "1.0000013":
                assertHelper.assertInserts(traces, 3);
                assertHelper.assertEmpty(errors);
                break;
            case "1.0000014":
                assertHelper.assertInserts(traces, 5);
                assertHelper.assertEmpty(errors);
                break;
            case "1.0000018":
                assertHelper.assertContainsError(errors, "Country cannot be empty");
                assertHelper.assertEmpty(traces);
                break;
            case "1.0000019":
                assertHelper.assertContainsError(errors, "Season cannot be empty");
                assertHelper.assertEmpty(traces);
                break;
            case "2.0000000":
                assertHelper.assertContainsError(errors, "No species found");
                assertHelper.assertEmpty(traces);
                break;
            case "1.0000020":
                assertHelper.assertContainsError(errors, "Unknown country");
                assertHelper.assertEmpty(traces);
                break;
            case "1.0000021":
                assertHelper.assertContainsError(errors, "Unknown season");
                assertHelper.assertEmpty(traces);
                break;
            case "1.0000023":
                assertHelper.assertPrestoredTrace(traces, 6);
                assertHelper.assertSize(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "1.0000024":
                assertHelper.assertContainsError(errors, SeadDataFromTraceHelper.SEAD_DATA_HAS_BEEN_UPDATED_SINCE_LAST_BUGS_IMPORT);
                assertHelper.assertPrestoredTrace(traces, 7);
                assertHelper.assertSize(traces, 1);
                break;
            case "1.0000025":
                assertHelper.assertPrestoredTrace(traces, 8);
                assertHelper.assertInserts(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
            case "1.0000026":
                assertHelper.assertPrestoredTrace(traces, 9, 10, 11);
                assertHelper.assertInserts(traces, 1);
                assertHelper.assertEmpty(errors);
                break;
        }
    }
}
