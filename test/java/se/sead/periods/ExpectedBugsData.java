package se.sead.periods;

import se.sead.bugsimport.periods.bugsmodel.Period;

import java.util.Arrays;
import java.util.List;

class ExpectedBugsData {

    static final List<Period> EXPECTED_DATA =
            Arrays.asList(
                create("?", "unknown/not given", "Other", "No data", null, null, null, null, null, null, "?"),
                create("ERR_NO_BP_ETC", "No age type", "Geological", "No BP etc specified", null, "UK", 0, null, 0, null, "Radiometric"),
                create("ERR_NO_LOC", "No location", "Geological", "No location found", null, "Error", 0, "BP", 1, "BP", "Radiometric"),
                create("ERR_NO_NAME", null, "Geological", "This period has no name", null, "UK", 0, "BP", 1, "BP", "Radiometric"),
                create("ERR_NO_TYPE", "No type", null, "Type not found", null, "UK", 0, "BP", 1, "BP", "Radiometric"),
                create("EXIST", "Existing", "Geological", "Previously inserted", "Ref 1000", "UK", 478000, "BP", 423000, "BP", "Radiometric"),
                create("INSERT", "New", "Archaeological", "BP insert", null, "UK", 800, "BP", 1100, "BP", "Radiometric"),
                create("INSERTCAL", "New calendar", "Archaeological", "Calendar insert", null, "UK", 2500, "AD", 600, "AD", "Calendar"),
                create("U_NEWER_SEAD", "Sead data updated", "Geological", "Sead data has changed", "Ref 1000", null, 12000, "BP", 10500, "BP", "Radiometric"),
                create("UPDATE", "Updated", "Archaeological", "Update", null, "Eastern Mediterranean", 666, "BP", 1918, "BP", "Radiometric")
            );

    private static Period create(
            String periodCode,
            String periodName,
            String periodType,
            String periodDesc,
            String ref,
            String periodGeog,
            Integer begin,
            String beginBCad,
            Integer end,
            String endBCad,
            String yearsType
    ){
        Period period = new Period();
        period.setPeriodCode(periodCode);
        period.setName(periodName);
        period.setType(periodType);
        period.setDesc(periodDesc);
        period.setRef(ref);
        period.setGeography(periodGeog);
        period.setBegin(begin);
        period.setBeginBCad(beginBCad);
        period.setEnd(end);
        period.setEndBCad(endBCad);
        period.setYearsType(yearsType);
        return period;
    }
}
