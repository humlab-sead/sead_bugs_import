package se.sead.sitereferences;

import se.sead.bugsimport.sitereferences.bugsmodel.BugsSiteRef;

import java.util.Arrays;
import java.util.List;

public class ExpectedBugsSiteRefData {

    static final List<BugsSiteRef> EXPECTED_BUGS_DATA =
            Arrays.asList(
                create("SITE000006", "Robinson (1979b)"),
                create("SITE000008", "Lemdahl & Nilsson (1982)"),
                create("SITE000008","Lemdahl (1983)"),
                create("SITE000009","McCabe et al. (1987)"),
                create("SITE000010","Panagiotakopulu (2000)"),
                create("SITE000010","Panagiotakopulu & Buckland (1991)"),
                create("SITE000010", "Lemdahl (1983)"),
                create("SITE000011","Shotton et al. (1977)"),
                create("SITE000012","Girling (1986a)"),
                create("SITE000013", null)
            );

    private static BugsSiteRef create(String code, String ref){
        BugsSiteRef siteRef = new BugsSiteRef();
        siteRef.setSiteCode(code);
        siteRef.setRef(ref);
        return siteRef;
    }
}
