package se.sead.datesperiod;

import se.sead.bugsimport.datesperiod.bugsmodel.DatesPeriod;

import java.util.Arrays;
import java.util.List;

class ExpectedBugsData {

    static final List<DatesPeriod> EXPECTED_DATA =
            Arrays.asList(
                    create("PERI000001", "SAMP000001", null, "EXISTING", "ERROR", "No dating method found"),
                    create("PERI000002", "SAMP000001", null, null, "ArchPer", "No period code"),
                    create("PERI000003", "SAMP000001", null, "ERROR", "GeolPer", "No period found for code"),
                    create("PERI000004", null, null, "EXISTING", "GeolPer", "No sample specified"),
                    create("PERI000005", "SAMP999999", null, "EXISTING", "GeolPer", "No sample found for code"),
                    create("PERI000006", "SAMP000001", "ERROR", "EXISTING", "GeolPer", "No uncertainty found for definition"),
                    create("PERI000007", "SAMP000001", ">", "EXISTING", null, "No method is ok -insert"),
                    create("PERI000009", "SAMP000001", null, "EXISTING", "GeolPer", "uncertainty is null, is ok -insert"),
                    create("PERI000011", "SAMP000001", null, "EXISTING", "GeolPer", "Already stored"),
                    create("PERI000012", "SAMP000001", null, "EXISTING", "GeolPer", "Update"),
                    create("PERI000013", "SAMP000001", ">", "EXISTING", "GeolPer", "Try update on sead changed data"),
                    create("PERI000014", "SAMP000001", null, "CALPER", "ArchPer", "ArchPer version calendar"),
                    create("PERI000015", "SAMP000001", null, "RADIO", "GeolPer", "GeolPer version radiometric"),
                    create("PERI000016", "SAMP000001", null, "EH", "Strat(Geol)", null)
            );

    private static DatesPeriod create(
            String periodDateCode,
            String sampleCode,
            String uncertainty,
            String periodCode,
            String datingMethod,
            String notes
    ) {
        DatesPeriod datesPeriod = new DatesPeriod();
        datesPeriod.setPeriodDateCode(periodDateCode);
        datesPeriod.setSampleCode(sampleCode);
        datesPeriod.setUncertainty(uncertainty);
        datesPeriod.setPeriodCode(periodCode);
        datesPeriod.setDatingMethod(datingMethod);
        datesPeriod.setNotes(notes);
        return datesPeriod;
    }
}
