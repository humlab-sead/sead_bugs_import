package se.sead.bugsimport.mcr.bugsmodel;

import se.sead.bugs.TraceableBugsData;

public class MCRSummaryData extends TraceableBugsData {

    private Double code;
    private Short maxLo;
    private Short maxHi;
    private Short minLo;
    private Short minHi;
    private Short rangeLo;
    private Short rangeHi;
    private Short cogMidTMax;
    private Short cogMidTRange;

    public Double getCode() {
        return code;
    }

    public void setCode(Double code) {
        this.code = code;
    }

    public Short getMaxLo() {
        return maxLo;
    }

    public void setMaxLo(Short maxLo) {
        this.maxLo = maxLo;
    }

    public Short getMaxHi() {
        return maxHi;
    }

    public void setMaxHi(Short maxHi) {
        this.maxHi = maxHi;
    }

    public Short getMinLo() {
        return minLo;
    }

    public void setMinLo(Short minLo) {
        this.minLo = minLo;
    }

    public Short getMinHi() {
        return minHi;
    }

    public void setMinHi(Short minHi) {
        this.minHi = minHi;
    }

    public Short getRangeLo() {
        return rangeLo;
    }

    public void setRangeLo(Short rangeLo) {
        this.rangeLo = rangeLo;
    }

    public Short getRangeHi() {
        return rangeHi;
    }

    public void setRangeHi(Short rangeHi) {
        this.rangeHi = rangeHi;
    }

    public Short getCogMidTMax() {
        return cogMidTMax;
    }

    public void setCogMidTMax(Short cogMidTMax) {
        this.cogMidTMax = cogMidTMax;
    }

    public Short getCogMidTRange() {
        return cogMidTRange;
    }

    public void setCogMidTRange(Short cogMidTRange) {
        this.cogMidTRange = cogMidTRange;
    }

    @Override
    public String compressToString() {
        return "{" +
                code + ',' +
                maxLo + ',' +
                maxHi + ',' +
                minLo + ',' +
                minHi + ',' +
                rangeLo + ',' +
                rangeHi + ',' +
                cogMidTMax + ',' +
                cogMidTRange + ",}"
                ;
    }

    @Override
    public String bugsTable() {
        return MCRSummaryBugsTable.TABLE_NAME;
    }

    @Override
    public String toString() {
        return "MCRSummary{" +
                "code=" + code +
                ", maxLo=" + maxLo +
                ", maxHi=" + maxHi +
                ", minLo=" + minLo +
                ", minHi=" + minHi +
                ", rangeLo=" + rangeLo +
                ", rangeHi=" + rangeHi +
                ", cogMidTMax=" + cogMidTMax +
                ", cogMidTRange=" + cogMidTRange +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MCRSummaryData that = (MCRSummaryData) o;

        if (!code.equals(that.code)) return false;
        if (maxLo != null ? !maxLo.equals(that.maxLo) : that.maxLo != null) return false;
        if (maxHi != null ? !maxHi.equals(that.maxHi) : that.maxHi != null) return false;
        if (minLo != null ? !minLo.equals(that.minLo) : that.minLo != null) return false;
        if (minHi != null ? !minHi.equals(that.minHi) : that.minHi != null) return false;
        if (rangeLo != null ? !rangeLo.equals(that.rangeLo) : that.rangeLo != null) return false;
        if (rangeHi != null ? !rangeHi.equals(that.rangeHi) : that.rangeHi != null) return false;
        if (cogMidTMax != null ? !cogMidTMax.equals(that.cogMidTMax) : that.cogMidTMax != null) return false;
        return cogMidTRange != null ? cogMidTRange.equals(that.cogMidTRange) : that.cogMidTRange == null;

    }

    @Override
    public int hashCode() {
        int result = code.hashCode();
        result = 31 * result + (maxLo != null ? maxLo.hashCode() : 0);
        result = 31 * result + (maxHi != null ? maxHi.hashCode() : 0);
        result = 31 * result + (minLo != null ? minLo.hashCode() : 0);
        result = 31 * result + (minHi != null ? minHi.hashCode() : 0);
        result = 31 * result + (rangeLo != null ? rangeLo.hashCode() : 0);
        result = 31 * result + (rangeHi != null ? rangeHi.hashCode() : 0);
        result = 31 * result + (cogMidTMax != null ? cogMidTMax.hashCode() : 0);
        result = 31 * result + (cogMidTRange != null ? cogMidTRange.hashCode() : 0);
        return result;
    }
}

