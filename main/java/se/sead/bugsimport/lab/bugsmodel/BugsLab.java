package se.sead.bugsimport.lab.bugsmodel;

import se.sead.bugs.BugsColumn;
import se.sead.bugs.TraceableBugsData;

public class BugsLab extends TraceableBugsData {

    @BugsColumn("LabID")
    private String labId;
    @BugsColumn("Labname")
    private String labName;
    @BugsColumn("Address")
    private String address;
    @BugsColumn("Country")
    private String country;
    @BugsColumn("Telephone")
    private String telephone;
    @BugsColumn("Website")
    private String website;
    @BugsColumn("email")
    private String email;

    public String getLabId() {
        return labId;
    }

    public void setLabId(String labId) {
        this.labId = labId;
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String compressToString() {
        return "{" +
                labId + ',' +
                labName + ',' +
                country + '}';
    }

    @Override
    public String bugsTable() {
        return LabBugsTable.TABLE_NAME;
    }

    @Override
    public String getBugsIdentifier() {
        return labId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BugsLab bugsLab = (BugsLab) o;

        if (labId != null ? !labId.equals(bugsLab.labId) : bugsLab.labId != null) return false;
        if (labName != null ? !labName.equals(bugsLab.labName) : bugsLab.labName != null) return false;
        if (address != null ? !address.equals(bugsLab.address) : bugsLab.address != null) return false;
        if (country != null ? !country.equals(bugsLab.country) : bugsLab.country != null) return false;
        if (telephone != null ? !telephone.equals(bugsLab.telephone) : bugsLab.telephone != null) return false;
        if (website != null ? !website.equals(bugsLab.website) : bugsLab.website != null) return false;
        return email != null ? email.equals(bugsLab.email) : bugsLab.email == null;

    }

    @Override
    public int hashCode() {
        int result = labId != null ? labId.hashCode() : 0;
        result = 31 * result + (labName != null ? labName.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (telephone != null ? telephone.hashCode() : 0);
        result = 31 * result + (website != null ? website.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BugsLab{" +
                "labId='" + labId + '\'' +
                ", labName='" + labName + '\'' +
                ", address='" + address + '\'' +
                ", country='" + country + '\'' +
                ", telephone='" + telephone + '\'' +
                ", website='" + website + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
