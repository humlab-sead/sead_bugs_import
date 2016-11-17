package se.sead.bugsimport.countsheets.bugsmodel;

import se.sead.bugs.TraceableBugsData;

public class Countsheet extends TraceableBugsData {

    private String code;
    private String name;
    private String siteCode;
    private String sheetContext;
    private String sheetType;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSheetContext() {
        return sheetContext;
    }

    public void setSheetContext(String sheetContext) {
        this.sheetContext = sheetContext;
    }

    public String getSheetType() {
        return sheetType;
    }

    public void setSheetType(String sheetType) {
        this.sheetType = sheetType;
    }

    @Override
    public String compressToString() {
        return "{" +
                code + ',' +
                name + ',' +
                siteCode + ',' +
                sheetContext + ',' +
                sheetType +
                "}";
    }

    @Override
    public String bugsTable() {
        return CountsheetBugsTable.TABLE_NAME;
    }

    @Override
    public String getBugsIdentifier() {
        return code;
    }

    @Override
    public String toString(){
        return "Countsheet" + compressToString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Countsheet)) return false;

        Countsheet that = (Countsheet) o;

        if (!code.equals(that.code)) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (!siteCode.equals(that.siteCode)) return false;
        if (sheetContext != null ? !sheetContext.equals(that.sheetContext) : that.sheetContext != null) return false;
        return sheetType != null ? sheetType.equals(that.sheetType) : that.sheetType == null;

    }

    @Override
    public int hashCode() {
        int result = code.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + siteCode.hashCode();
        result = 31 * result + (sheetContext != null ? sheetContext.hashCode() : 0);
        result = 31 * result + (sheetType != null ? sheetType.hashCode() : 0);
        return result;
    }
}
