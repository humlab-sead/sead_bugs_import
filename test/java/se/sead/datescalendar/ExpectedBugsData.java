package se.sead.datescalendar;

import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;

import java.util.Arrays;
import java.util.List;

class ExpectedBugsData {

    static final List<DatesCalendar> EXPECTED_DATA =
            Arrays.asList(
                    create(null, "From", "CALE000003", 100, "AD", "ArchCal", "No sample specified"),
                    create("SAMP999999", "From", "CALE000004", 100, "AD", "ArchCal", "No sample found for code"),
                    create("SAMP000001", "From", "CALE000006", 100, "AD", "Error", "No method found"),
                    create("SAMP000001", "From", "CALE000007", 100, null, "ArchCal", "No BCADBP specified"),
                    create("SAMP000001", "Error", "CALE000009", 100, "AD", "ArchCal", "No uncertainty found for definition"),
                    create("SAMP000001", "From", "CALE000010", 120, "AD", "ArchCal", "Insert"),
                    create("SAMP000001", "From", "CALE000011", 120, "AD", "ArchCal", "update change uncertainty"),
                    create("SAMP000001", "From", "CALE000012", 120, "AD", "ArchCal", "try to update sead changed data"),
                    create("SAMP000001", "From", "CALE000013", 100, "AD", null, "No method is ok -insert"),
                    create("SAMP000001", null, "CALE000014", 100, "AD", "ArchCal", "No uncertainty is ok -insert"),
                    create("SAMP000001", "From", "CALE000015", 100, "AD", "ArchCal", "Already stored"),
                    create("SAMP000001", "From", "CALE000016", 120, "AD", "ArchCal", "update change date"),
                    create("SAMP000001", "From", "CALE000017", 100, "BC", "ArchCal", "insert bc version"),
                    create("SAMP000001", "From", "CALE000018", 100, "BP", "GeolCal", "insert bp version"),
                    create("SAMP000002", "From", "CALE000019", 100, "AD", "ArchCal", "range"),
                    create("SAMP000002", "To", "CALE000020", 200, "AD", "ArchCal", "range"),
                    create("SAMP000003", "From ca.", "CALE000021", 100, "AD", "ArchCal", null),
                    create("SAMP000003", "To ca.", "CALE000022", 200, "AD", "ArchCal", "existing calendar range but with uncertainty")
            );

    private static DatesCalendar create(
            String sampleCode,
            String uncertainty,
            String calendarCode,
            Integer date,
            String bcadbp,
            String datingMethod,
            String notes
    ) {
        DatesCalendar datesCalendar = new DatesCalendar();
        datesCalendar.setSampleCODE(sampleCode);
        datesCalendar.setUncertainty(uncertainty);
        datesCalendar.setCalendarCODE(calendarCode);
        datesCalendar.setDate(date);
        datesCalendar.setBcadbp(bcadbp);
        datesCalendar.setDatingMethod(datingMethod);
        datesCalendar.setNotes(notes);
        return datesCalendar;
    }

}
