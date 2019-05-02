package se.sead.bugsimport.datescalendar.bugsmodel;

import se.sead.bugs.BugsColumn;
import se.sead.bugs.TraceableBugsData;

public class DatesCalendar extends TraceableBugsData {

    @BugsColumn("SampleCODE")
    private String sample;
    @BugsColumn("Uncertainty")
    private String uncertainty;
    @BugsColumn("CalendarCODE")
    private String calendarCODE;
    @BugsColumn("Date")
    private Integer date;
    @BugsColumn("BCADBP")
    private String bcadbp;
    @BugsColumn("DatingMethod")
    private String datingMethod;
    @BugsColumn("Notes")
    private String notes;

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
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
                sample + ',' +
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

        if (sample != null ? !sample.equals(that.sample) : that.sample != null) return false;
        if (uncertainty != null ? !uncertainty.equals(that.uncertainty) : that.uncertainty != null) return false;
        if (calendarCODE != null ? !calendarCODE.equals(that.calendarCODE) : that.calendarCODE != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (bcadbp != null ? !bcadbp.equals(that.bcadbp) : that.bcadbp != null) return false;
        if (datingMethod != null ? !datingMethod.equals(that.datingMethod) : that.datingMethod != null) return false;
        return notes != null ? notes.equals(that.notes) : that.notes == null;

    }

    @Override
    public int hashCode() {
        int result = sample != null ? sample.hashCode() : 0;
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
                "sample='" + sample + '\'' +
                ", uncertainty='" + uncertainty + '\'' +
                ", calendarCODE='" + calendarCODE + '\'' +
                ", date=" + date +
                ", bcadbp='" + bcadbp + '\'' +
                ", datingMethod='" + datingMethod + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
