package se.sead.bugsimport.attributes.bugsmodel;

import se.sead.bugs.TraceableBugsData;

public class BugsAttributes extends TraceableBugsData {

    private Double code;
    private String attribType;
    private String attribMeasure;
    private Float value;
    private String attribUnits;

    public Double getCode() {
        return code;
    }

    public void setCode(Double code) {
        this.code = code;
    }

    public String getAttribType() {
        return attribType;
    }

    public void setAttribType(String attribType) {
        this.attribType = attribType;
    }

    public String getAttribMeasure() {
        return attribMeasure;
    }

    public void setAttribMeasure(String attribMeasure) {
        this.attribMeasure = attribMeasure;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public String getAttribUnits() {
        return attribUnits;
    }

    public void setAttribUnits(String attribUnits) {
        this.attribUnits = attribUnits;
    }

    @Override
    public String compressToString() {
        return "{" +
                (code != null ? code.toString() : "") + ',' +
                attribType + ',' +
                attribMeasure + ',' +
                value + ',' +
                attribUnits +
                '}';
    }

    @Override
    public String getBugsIdentifier() {
        return code != null ? code.toString() : null;
    }

    @Override
    public String bugsTable() {
        return AttributesBugsTable.TABLE_NAME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BugsAttributes that = (BugsAttributes) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (attribType != null ? !attribType.equals(that.attribType) : that.attribType != null) return false;
        if (attribMeasure != null ? !attribMeasure.equals(that.attribMeasure) : that.attribMeasure != null)
            return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        return attribUnits != null ? attribUnits.equals(that.attribUnits) : that.attribUnits == null;

    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (attribType != null ? attribType.hashCode() : 0);
        result = 31 * result + (attribMeasure != null ? attribMeasure.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (attribUnits != null ? attribUnits.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BugsAttributes{" +
                "code=" + code +
                ", attribType='" + attribType + '\'' +
                ", attribMeasure='" + attribMeasure + '\'' +
                ", value=" + value +
                ", attribUnits='" + attribUnits + '\'' +
                '}';
    }
}
