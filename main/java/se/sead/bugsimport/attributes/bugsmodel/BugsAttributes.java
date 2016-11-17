package se.sead.bugsimport.attributes.bugsmodel;

import se.sead.bugs.BugsColumn;
import se.sead.bugs.TraceableBugsData;

public class BugsAttributes extends TraceableBugsData {

    @BugsColumn("CODE")
    private Double code;
    @BugsColumn("AttribType")
    private String type;
    @BugsColumn("AttribMeasure")
    private String measure;
    @BugsColumn("Value")
    private Float value;
    @BugsColumn("AttribUnits")
    private String units;

    public Double getCode() {
        return code;
    }

    public void setCode(Double code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    @Override
    public String compressToString() {
        return "{" +
                (code != null ? code.toString() : "") + ',' +
                type + ',' +
                measure + ',' +
                value + ',' +
                units +
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
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (measure != null ? !measure.equals(that.measure) : that.measure != null)
            return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        return units != null ? units.equals(that.units) : that.units == null;

    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (measure != null ? measure.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (units != null ? units.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BugsAttributes{" +
                "code=" + code +
                ", type='" + type + '\'' +
                ", measure='" + measure + '\'' +
                ", value=" + value +
                ", units='" + units + '\'' +
                '}';
    }
}
