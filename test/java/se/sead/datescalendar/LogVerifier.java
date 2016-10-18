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

    LogVerifier(){
        listHelper = new AssertHelper("");
        relativeAgeAssertHelper = new AssertHelper("tbl_relative_ages");
        relativeDatesAssertHelper = new AssertHelper("tbl_relative_dates");
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
                listHelper.assertContainsError(errors, "No method found");
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
                listHelper.assertSize(traces, 1);
                relativeDatesAssertHelper.assertInserts(traces, 1);
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
            case "CALE000013":
            case "CALE000014":
                listHelper.assertSize(traces, 1);
                relativeDatesAssertHelper.assertInserts(traces, 1);
                listHelper.assertEmpty(errors);
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
                listHelper.assertSize(traces, 2);
                relativeDatesAssertHelper.assertInserts(traces, 1);
                relativeAgeAssertHelper.assertInserts(traces, 1);
                listHelper.assertEmpty(errors);
                break;
        }
    }
}
