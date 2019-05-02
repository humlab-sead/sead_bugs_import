package se.sead.countsheets;

import se.sead.bugsimport.countsheets.bugsmodel.Countsheet;

import java.util.Arrays;
import java.util.List;

public class ExpectedBugsData {

    static final List<Countsheet> EXPECTED_DATA =
            Arrays.asList(
                    create("COUN000482", "Abingdon Stert Street_bugsdata", "SITE000006", "Archaeological contexts", "Presence/Absence"),
                    create("COUN000384", "Abydos_PA_bugsdata.xls", "SITE000007", "Archaeological contexts", "Presence/Absence"),
                    create("COUN000030", "Aghnadarragh_bugsdata.xls", "SITE000009", "Stratigraphic sequence", "Abundances"),
                    create("COUN000006", "Akrotiri_PA_bugsdata.XLS", "SITE000010", "Archaeological contexts", "Presence/Absence"),
                    create("COUN000927", "Akrotiri Silk", "SITE000010", "Archaeological contexts", "Abundances"),
                    create("COUN001334", "West House", "SITE000010", "Archaeological contexts", "Abundances"),
                    create("COUN001335", "West House Samples", "SITE000010", "Archaeological contexts", "Abundances"),
                    create("COUN001336", "House of the Ladies", "SITE000010", "Archaeological contexts", "Abundances"),
                    create("COUN001337", "Pessos 17", "SITE000010", null, "Abundances"),
                    create("COUN001338", "Delta Building", "SITE000010", "Unknown context", "Abundances"),
                    create("COUN000795", "Dendermonde", "SITE000158", "Other Palaeo", "Presence/Absence"),
                    create("COUN000132", "Garden Under Sandet 1995_bugsdata.XLS", "SITE000251", "Archaeological contexts", "Abundances"),
                    create("COUN000760", "GUS modern converted bugsdata.xls", "SITE000251", "Pitfall traps", "Abundances"),
                    create("COUN000820", "GUS Full List Bugs Format.xls", "SITE000251", "Archaeological contexts", "Abundances"),
                    create("COUN000925", "Knepp cattle Dung 09", "SITE000969", "Other Modern", "Presence/Absence"),
                    create("COUN000926", "Barmston", "SITE000970", "Stratigraphic sequence", "Presence/Absence"),
                    create("COUNNONAME", null, "SITE000006", "Stratigraphic sequence", "Abundances"),
                    create("COUNUPDATE", "New name", "SITE000006", "Stratigraphic sequence", "Abundances"),
                    create("COUNBYNAME", "Name exists outside of bugs", "SITE000010", "Stratigraphic sequence", "Abundances")
            );

    private static Countsheet create(String code, String name, String siteCode, String context, String type){
        Countsheet sheet = new Countsheet();
        sheet.setSiteCode(siteCode);
        sheet.setType(type);
        sheet.setName(name);
        sheet.setContext(context);
        sheet.setCode(code);
        return sheet;
    }
}
