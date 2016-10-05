package se.sead.bugsimport.sample.bugsmodel;

import se.sead.bugs.TraceableBugsData;

public class BugsSample extends TraceableBugsData {

    private String sampleCode;
    private String siteCode;
    private String x;
    private String y;
    private Double zOrDepthTop;
    private Double zOrDepthBot;
    private String refNrContext;
    private String countSheetCode;

    public String getSampleCode() {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public Double getzOrDepthTop() {
        return zOrDepthTop;
    }

    public void setzOrDepthTop(Double zOrDepthTop) {
        this.zOrDepthTop = zOrDepthTop;
    }

    public Double getzOrDepthBot() {
        return zOrDepthBot;
    }

    public void setzOrDepthBot(Double zOrDepthBot) {
        this.zOrDepthBot = zOrDepthBot;
    }

    public String getRefNrContext() {
        return refNrContext;
    }

    public void setRefNrContext(String refNrContext) {
        this.refNrContext = refNrContext;
    }

    public String getCountSheetCode() {
        return countSheetCode;
    }

    public void setCountSheetCode(String countSheetCode) {
        this.countSheetCode = countSheetCode;
    }

    @Override
    public String compressToString() {
        return "{" +
                sampleCode + ',' +
                siteCode + ',' +
                refNrContext + ',' +
                zOrDepthTop + ',' +
                zOrDepthBot + ',' +
                countSheetCode +
                '}';
    }

    @Override
    public String bugsTable() {
        return SampleBugsTable.TABLE_NAME;
    }

    @Override
    public String getBugsIdentifier() {
        return sampleCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BugsSample that = (BugsSample) o;

        if (sampleCode != null ? !sampleCode.equals(that.sampleCode) : that.sampleCode != null) return false;
        if (siteCode != null ? !siteCode.equals(that.siteCode) : that.siteCode != null) return false;
        if (x != null ? !x.equals(that.x) : that.x != null) return false;
        if (y != null ? !y.equals(that.y) : that.y != null) return false;
        if (zOrDepthTop != null ? !zOrDepthTop.equals(that.zOrDepthTop) : that.zOrDepthTop != null) return false;
        if (zOrDepthBot != null ? !zOrDepthBot.equals(that.zOrDepthBot) : that.zOrDepthBot != null) return false;
        if (refNrContext != null ? !refNrContext.equals(that.refNrContext) : that.refNrContext != null) return false;
        return countSheetCode != null ? countSheetCode.equals(that.countSheetCode) : that.countSheetCode == null;

    }

    @Override
    public int hashCode() {
        int result = sampleCode != null ? sampleCode.hashCode() : 0;
        result = 31 * result + (siteCode != null ? siteCode.hashCode() : 0);
        result = 31 * result + (x != null ? x.hashCode() : 0);
        result = 31 * result + (y != null ? y.hashCode() : 0);
        result = 31 * result + (zOrDepthTop != null ? zOrDepthTop.hashCode() : 0);
        result = 31 * result + (zOrDepthBot != null ? zOrDepthBot.hashCode() : 0);
        result = 31 * result + (refNrContext != null ? refNrContext.hashCode() : 0);
        result = 31 * result + (countSheetCode != null ? countSheetCode.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BugsSample{" +
                "sampleCode='" + sampleCode + '\'' +
                ", refNrContext='" + refNrContext + '\'' +
                '}';
    }
}
