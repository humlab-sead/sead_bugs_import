package se.sead.datescalendar;

import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.testutils.AssertHelper;
import se.sead.testutils.BugsTracesAndErrorsVerification;

import java.util.List;

public class LogVerifier implements BugsTracesAndErrorsVerification.LogVerificationCallback<DatesCalendar> {

    private AssertHelper listHelper;
    private AssertHelper relativeAgeAssertHelper;
    private AssertHelper relativeDatesAssertHelper;
    private AssertHelper datasetAssertHelper;
    private AssertHelper analysisEntityHelper;

    LogVerifier(){
        listHelper = new AssertHelper("");
        relativeAgeAssertHelper = new AssertHelper("tbl_relative_ages");
        relativeDatesAssertHelper = new AssertHelper("tbl_relative_dates");
        datasetAssertHelper = new AssertHelper("tbl_datasets");
        analysisEntityHelper = new AssertHelper("tbl_analysis_entities");
    }

    @Override
    public void verifyLogData(DatesCalendar bugsData, List<BugsTrace> traces, List<BugsError> errors) {
        switch(bugsData.getCalendarCODE()){
            case "CALE000003":
                listHelper.assertEmpty(traces);
                listHelper.assertContainsError(errors, "No sample specified");
                break;
            case "CALE000004":
                listHelper.assertEmpty(traces);
                listHelper.assertContainsError(errors, "No sample found for code");
                break;
            case "CALE000006":
                listHelper.assertEmpty(traces);
                listHelper.assertContainsError(errors, "No dating method found");
                break;
            case "CALE000007":
                listHelper.assertEmpty(traces);
                listHelper.assertContainsError(errors, "No BCADBP specified");
                break;
            case "CALE000009":
                listHelper.assertEmpty(traces);
                listHelper.assertContainsError(errors, "No uncertainty found for definition");
                break;
            case "CALE000010":
            case "CALE000013":
            case "CALE000014":
            case "CALE000025":
            case "CALE000021":
                listHelper.assertSize(traces, 3);
                relativeDatesAssertHelper.assertInserts(traces, 1);
                datasetAssertHelper.assertInserts(traces, 1);
                analysisEntityHelper.assertInserts(traces, 1);
                listHelper.assertEmpty(errors);
                break;
            case "CALE000011":
                listHelper.assertSize(traces, 2);
                relativeDatesAssertHelper.assertPrestoredTrace(traces, 5);
                relativeDatesAssertHelper.assertUpdates(traces, 1);
                listHelper.assertEmpty(errors);
                break;
            case "CALE000012":
                listHelper.assertSize(traces, 1);
                relativeDatesAssertHelper.assertPrestoredTrace(traces, 6);
                listHelper.assertContainsError(errors, SeadDataFromTraceHelper.SEAD_DATA_HAS_BEEN_UPDATED_SINCE_LAST_BUGS_IMPORT);
                break;
            case "CALE000015":
                listHelper.assertSize(traces, 1);
                relativeDatesAssertHelper.assertPrestoredTrace(traces, 4);
                listHelper.assertEmpty(errors);
                break;
            case "CALE000016":
                listHelper.assertSize(traces, 2);
                relativeDatesAssertHelper.assertPrestoredTrace(traces, 7);
                relativeDatesAssertHelper.assertUpdates(traces, 1);
                listHelper.assertEmpty(errors);
                break;
            case "CALE000017":
            case "CALE000018":
            case "CALE000019":
            case "CALE000023":
            case "CALE000024":
            case "CALE000029":
            case "CALE000031":
            case "CALE000033":
            case "CALE000035":
            case "CALE000036":
                listHelper.assertSize(traces, 4);
                relativeDatesAssertHelper.assertInserts(traces, 1);
                relativeAgeAssertHelper.assertInserts(traces, 1);
                datasetAssertHelper.assertInserts(traces, 1);
                analysisEntityHelper.assertInserts(traces, 1);
                listHelper.assertEmpty(errors);
                break;
            case "CALE000020":
            case "CALE000022":
            case "CALE000027":
            case "CALE000028":
            case "CALE000030":
            case "CALE000032":
            case "CALE000034":
                listHelper.assertEmpty(traces);
                listHelper.assertEmpty(errors);
                break;
            case "CALE000026":
                System.out.println("CALE00026");
                listHelper.assertEmpty(traces);
                listHelper.assertContainsError(errors, "Too many uncertainties of same type for a single sample.");
                break;
        }
    }
}
