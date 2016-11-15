package se.sead.bugsimport.sitereferences.bugsmodel;

import se.sead.bugs.TraceableBugsData;

public class BugsSiteRef extends TraceableBugsData {

    private String siteCode;
    private String ref;

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    @Override
    public String compressToString() {
        return "{" + siteCode + ref + "}";
    }

    @Override
    public String bugsTable() {
        return SiteRefBugsTable.TABLE_NAME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BugsSiteRef siteRef = (BugsSiteRef) o;

        if (siteCode != null ? !siteCode.equals(siteRef.siteCode) : siteRef.siteCode != null) return false;
        return ref != null ? ref.equals(siteRef.ref) : siteRef.ref == null;

    }

    @Override
    public int hashCode() {
        int result = siteCode != null ? siteCode.hashCode() : 0;
        result = 31 * result + (ref != null ? ref.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BugsSiteRef{" +
                "siteCode='" + siteCode + '\'' +
                ", ref='" + ref + '\'' +
                '}';
    }
}
