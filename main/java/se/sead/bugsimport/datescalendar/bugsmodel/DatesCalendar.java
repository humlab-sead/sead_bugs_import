package se.sead.bugsimport.datescalendar.bugsmodel;

import se.sead.bugs.TraceableBugsData;

public class DatesCalendar extends TraceableBugsData {

    private String sampleCODE;
    private String uncertainty;
    private String calendarCODE;
    private Integer date;
    private String bcadbp;
    private String datingMethod;
    private String notes;

    public String getSampleCODE() {
        return sampleCODE;
    }

    public void setSampleCODE(String sampleCODE) {
        this.sampleCODE = sampleCODE;
    }

    public String getUncertainty() {
        return uncertainty;
    }

    public void setUncertainty(String uncertainty) {
        this.uncertainty = uncertainty;
    }

    public String getCalendarCODE() {
        return calendarCODE;
    }

    public void setCalendarCODE(String calendarCODE) {
        this.calendarCODE = calendarCODE;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public String getBcadbp() {
        return bcadbp;
    }

    public void setBcadbp(String bcadbp) {
        this.bcadbp = bcadbp;
    }

    public String getDatingMethod() {
        return datingMethod;
    }

    public void setDatingMethod(String datingMethod) {
        this.datingMethod = datingMethod;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String compressToString() {
        return "{" +
                sampleCODE + ',' +
                uncertainty + ',' +
                calendarCODE + ',' +
                date + ',' +
                bcadbp + ',' +
                datingMethod + ',' +
                notes + '}';
    }

    @Override
    public String bugsTable() {
        return DatesCalendarBugsTable.TABLE_NAME;
    }

    @Override
    public String getBugsIdentifier() {
        return calendarCODE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DatesCalendar that = (DatesCalendar) o;

        if (sampleCODE != null ? !sampleCODE.equals(that.sampleCODE) : that.sampleCODE != null) return false;
        if (uncertainty != null ? !uncertainty.equals(that.uncertainty) : that.uncertainty != null) return false;
        if (calendarCODE != null ? !calendarCODE.equals(that.calendarCODE) : that.calendarCODE != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (bcadbp != null ? !bcadbp.equals(that.bcadbp) : that.bcadbp != null) return false;
        if (datingMethod != null ? !datingMethod.equals(that.datingMethod) : that.datingMethod != null) return false;
        return notes != null ? notes.equals(that.notes) : that.notes == null;

    }

    @Override
    public int hashCode() {
        int result = sampleCODE != null ? sampleCODE.hashCode() : 0;
        result = 31 * result + (uncertainty != null ? uncertainty.hashCode() : 0);
        result = 31 * result + (calendarCODE != null ? calendarCODE.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (bcadbp != null ? bcadbp.hashCode() : 0);
        result = 31 * result + (datingMethod != null ? datingMethod.hashCode() : 0);
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DatesCalendar{" +
                "sampleCODE='" + sampleCODE + '\'' +
                ", uncertainty='" + uncertainty + '\'' +
                ", calendarCODE='" + calendarCODE + '\'' +
                ", date=" + date +
                ", bcadbp='" + bcadbp + '\'' +
                ", datingMethod='" + datingMethod + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
