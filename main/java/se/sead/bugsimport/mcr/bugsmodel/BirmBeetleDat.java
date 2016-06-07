package se.sead.bugsimport.mcr.bugsmodel;

import se.sead.bugs.TraceableBugsData;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by erer0001 on 2016-05-12.
 */
public class BirmBeetleDat implements TraceableBugsData {

    private Set<Field> fieldData;
    private Short row;
    private Double bugsCode;

    public BirmBeetleDat(){
        fieldData = new TreeSet<>();
    }

    public Short getRow() {
        return row;
    }

    public void setRow(Short row) {
        this.row = row;
    }

    public Double getBugsCode() {
        return bugsCode;
    }

    public void setBugsCode(Double bugsCode) {
        this.bugsCode = bugsCode;
    }

    public Set<Field> getFieldData() {
        return fieldData;
    }

    public void setFieldData(int position, String fieldValue){
        fieldData.add(new Field(position, fieldValue));
    }

    @Override
    public String compressToString() {
        return "{" +
                bugsCode + "," +
                row + "," +
                compressFieldValues() +
                "}";
    }

    public String compressFieldValues() {
        return String.join("", fieldData.stream().map(field -> field.getValue()).collect(Collectors.toList()));
    }

    @Override
    public String bugsTable() {
        return BirmBeetleDatBugsTable.TABLE_NAME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BirmBeetleDat that = (BirmBeetleDat) o;

        if (fieldData != null ? !fieldData.equals(that.fieldData) : that.fieldData != null) return false;
        if (row != null ? !row.equals(that.row) : that.row != null) return false;
        return bugsCode != null ? bugsCode.equals(that.bugsCode) : that.bugsCode == null;

    }

    @Override
    public int hashCode() {
        int result = fieldData != null ? fieldData.hashCode() : 0;
        result = 31 * result + (row != null ? row.hashCode() : 0);
        result = 31 * result + (bugsCode != null ? bugsCode.hashCode() : 0);
        return result;
    }

    @Override
    public String toString(){
        return "BirmBeetleDat" +
                compressToString();
    }

    private static class Field implements Comparable<Field>{
        private int order;
        private String value;

        Field(int order, String value) {
            this.order = order;
            this.value = value;
        }

        public int getOrder() {
            return order;
        }

        public String getValue() {
            return value;
        }

        @Override
        public int compareTo(Field o) {
            return Integer.compare(order, o.getOrder());
        }
    }
}
