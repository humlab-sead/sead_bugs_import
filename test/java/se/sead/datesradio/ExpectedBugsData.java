package se.sead.datesradio;

import se.sead.bugsimport.datesradio.bugsmodel.DatesRadio;

import java.util.Arrays;
import java.util.List;

public class ExpectedBugsData {

    static final List<DatesRadio> EXPECTED_DATA =
            Arrays.asList(
                    create("DATE000001", "SAMP000001", "lab-00001", null, 6000, (short)70, null, "C14 Std", null, "A", "previously inserted not updated bugs dating"),
                    create("DATE000002", "SAMP000001", "lab-00002", null, 6000, (short)70, null, "C14 Std", null, "A", "insert new dating item"),
                    create("DATE000003", "SAMP000001", "lab-00003", null, 6000, (short)80, null, "C14 Std", null, "A", "updated"),
                    create("DATE000004", "SAMP000001", "lab-00004", ">", 6000, (short)70, null, "C14 Std", null, "A", "previously inserted not updated with uncertainty"),
                    create("DATE000005", "SAMP000001", "lab-00005", "?", 6000, (short)70, null, null, null, "A", "insert with uncertainty, default method"),
                    create("DATE000006", "SAMP000001", "lab-00006", null, 6000, (short)70, 50, "C14 Std", null, "A", "insert with upper and lower errors"),
                    create("DATE000007", "SAMP000001", "lab-00007", null, null, null, null, "C14 Std", null, "A", "No date error"),
                    create("DATE000008", "SAMP000001", "lab-00008", "!", 6000, null, null, "C14 Std", null, "A", "Unknown uncertainty symbol"),
                    create("DATE000009", "SAMP000001", "lab-00009", null, 6000, null, null, "Error", null, "A", "Unknown dating method"),
                    create("DATE000010", "SAMP000002", "lab-00010", null, 6000, null, null, "C14 Std", null, "A", "Unknown sample id"),
                    create("DATE000011", "SAMP000001", "lab-00011", null, 6000, null, null, "C14 Std", null, "B", "Unknown lab"),
                    create("DATE000012", "SAMP000001", "lab-00012", null, 5999, null, null, "C14 Std", null, "A", "Sead data updated after import"),
                    create("DATE000013", "SAMP000001", "abc-1", ">", 6000, (short)100, null, "C14 Std", null, "Unknown", "Lab not set"),
                    create("DATE000014", "SAMP000001", "abc-2", ">", 6000, (short)100, null, "C14 Std", null, null, "Lab not set")
            );

    private static DatesRadio create(
            String dateCode,
            String sampleCode,
            String labNr,
            String uncertainty,
            Integer date,
            Short ageErroOrPlusError,
            Integer ageErrorMinus,
            String datingMethod,
            String materialType,
            String labId,
            String notes
    ){
        DatesRadio dr = new DatesRadio();
        dr.setDateCode(dateCode);
        dr.setSampleCode(sampleCode);
        dr.setLabNr(labNr);
        dr.setUncertainty(uncertainty);
        dr.setDate(date);
        dr.setAgeErrorOrPlusError(ageErroOrPlusError);
        dr.setAgeErrorMinus(ageErrorMinus);
        dr.setDatingMethod(datingMethod);
        dr.setMaterialType(materialType);
        dr.setLabId(labId);
        dr.setNotes(notes);
        return dr;
    }
}
