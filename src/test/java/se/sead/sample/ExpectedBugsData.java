package se.sead.sample;

import se.sead.bugsimport.sample.bugsmodel.BugsSample;

import java.util.Arrays;
import java.util.List;

class ExpectedBugsData {

    static final List<BugsSample> EXPECTED_DATA =
            Arrays.asList(
                    create("SAMP000001", "SITE000001", "Exists w. no dimensions", "COUN000001"),
                    create("SAMP000002", "SITE000001", 718d, 720d, "Exists w. dimensions", "COUN000001"),
                    create("SAMP000003", "SITE000001", 720d, 722d, "insert w. dimensions", "COUN000001"),
                    create("SAMP000004", "SITE000001", 1d, 2d, "Update", "COUN000001"),
                    create("SAMP000005", "SITE000001", "Update delete dimensions", "COUN000001"),
                    create("SAMP000008", "SITE000001", "No countsheet found", "ERROR"),
                    create("SAMP000009", "SITE000001", "No countsheet specified", null),
                    create("SAMP000010", "SITE000001", "Try to update newer sead sample data", "COUN000001"),
                    create("SAMP000011", "SITE000001", 1d, 2d, "Try to update newer sample dimension data", "COUN000001"),
                    create("SAMP000012", "SITE000001", "insert w/o dimensions", "COUN000001"),
                    create("SAMP000013", "SITE000001", "1", null, null, null, "X or Y is not allowed", "COUN000001")
            );

    private static BugsSample create(
            String sampleCode,
            String siteCode,
            String refNrContext,
            String countSheetCode
    ){
        return create(sampleCode, siteCode, null, null, refNrContext, countSheetCode);
    }

    private static BugsSample create(
            String sampleCode,
            String siteCode,
            Double zOrDepthTop,
            Double zOrDepthBot,
            String refNrContext,
            String countSheetCode
    ){
        return create(sampleCode, siteCode, null, null, zOrDepthTop, zOrDepthBot, refNrContext, countSheetCode);
    }

    private static BugsSample create(
            String sampleCode,
            String siteCode,
            String x,
            String y,
            Double zOrDepthTop,
            Double zOrDepthBot,
            String refNrContext,
            String countSheetCode
    ){
        BugsSample sample = new BugsSample();
        sample.setSampleCode(sampleCode);
        sample.setSiteCode(siteCode);
        sample.setX(x);
        sample.setY(y);
        sample.setZOrDepthTop(zOrDepthTop);
        sample.setZOrDepthBot(zOrDepthBot);
        sample.setRefNrContext(refNrContext);
        sample.setCountSheetCode(countSheetCode);
        return sample;
    }
}
