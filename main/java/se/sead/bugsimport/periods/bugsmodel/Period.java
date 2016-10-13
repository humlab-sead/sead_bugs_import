package se.sead.bugsimport.periods.bugsmodel;

import se.sead.bugs.TraceableBugsData;

public class Period extends TraceableBugsData {

    private String periodCode;
    private String name;
    private String type;
    private String desc;
    private String ref;
    private String geography;
    private Integer begin;
    private String beginBCad;
    private Integer end;
    private String endBCad;
    private String yearsType;

    public String getPeriodCode() {
        return periodCode;
    }

    public void setPeriodCode(String periodCode) {
        this.periodCode = periodCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getGeography() {
        return geography;
    }

    public void setGeography(String geography) {
        this.geography = geography;
    }

    public Integer getBegin() {
        return begin;
    }

    public void setBegin(Integer begin) {
        this.begin = begin;
    }

    public String getBeginBCad() {
        return beginBCad;
    }

    public void setBeginBCad(String beginBCad) {
        this.beginBCad = beginBCad;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public String getEndBCad() {
        return endBCad;
    }

    public void setEndBCad(String endBCad) {
        this.endBCad = endBCad;
    }

    public String getYearsType() {
        return yearsType;
    }

    public void setYearsType(String yearsType) {
        this.yearsType = yearsType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Period period = (Period) o;

        if (periodCode != null ? !periodCode.equals(period.periodCode) : period.periodCode != null) return false;
        if (name != null ? !name.equals(period.name) : period.name != null) return false;
        if (type != null ? !type.equals(period.type) : period.type != null) return false;
        if (desc != null ? !desc.equals(period.desc) : period.desc != null) return false;
        if (ref != null ? !ref.equals(period.ref) : period.ref != null) return false;
        if (geography != null ? !geography.equals(period.geography) : period.geography != null) return false;
        if (begin != null ? !begin.equals(period.begin) : period.begin != null) return false;
        if (beginBCad != null ? !beginBCad.equals(period.beginBCad) : period.beginBCad != null) return false;
        if (end != null ? !end.equals(period.end) : period.end != null) return false;
        if (endBCad != null ? !endBCad.equals(period.endBCad) : period.endBCad != null) return false;
        return yearsType != null ? yearsType.equals(period.yearsType) : period.yearsType == null;

    }

    @Override
    public int hashCode() {
        int result = periodCode != null ? periodCode.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        result = 31 * result + (ref != null ? ref.hashCode() : 0);
        result = 31 * result + (geography != null ? geography.hashCode() : 0);
        result = 31 * result + (begin != null ? begin.hashCode() : 0);
        result = 31 * result + (beginBCad != null ? beginBCad.hashCode() : 0);
        result = 31 * result + (end != null ? end.hashCode() : 0);
        result = 31 * result + (endBCad != null ? endBCad.hashCode() : 0);
        result = 31 * result + (yearsType != null ? yearsType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Period{" +
                "periodCode='" + periodCode + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", desc='" + desc + '\'' +
                ", ref='" + ref + '\'' +
                ", geography='" + geography + '\'' +
                ", begin=" + begin +
                ", beginBCad='" + beginBCad + '\'' +
                ", end=" + end +
                ", endBCad='" + endBCad + '\'' +
                ", yearsType='" + yearsType + '\'' +
                '}';
    }

    @Override
    public String compressToString() {
        return "{" +
                periodCode + ',' +
                name + ',' +
                type + ',' +
                desc + ',' +
                ref + ',' +
                geography + ',' +
                begin + ',' +
                beginBCad + ',' +
                end + ',' +
                endBCad + ',' +
                yearsType + '}';
    }

    @Override
    public String bugsTable() {
        return PeriodBugsTable.TABLE_NAME;
    }

    @Override
    public String getBugsIdentifier() {
        return periodCode;
    }
}
