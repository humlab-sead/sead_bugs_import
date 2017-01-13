package se.sead.bugsimport.tracing.seadmodel;

import se.sead.bugs.TraceableBugsData;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@MappedSuperclass
public abstract class BugsInformation {

    @Column(name="bugs_table", nullable = false)
    private String bugsTable;
    @Column(name="bugs_data", nullable = false)
    private String compressedBugsData;
    @Column(name="bugs_identifier")
    private String bugsIdentifier;
    @Column(name="change_date", insertable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date changeDate;
    @Column(name = "translated_compressed_data")
    private String translatedCompressedData;

    public final String getBugsTable() {
        return bugsTable;
    }

    public final String getCompressedBugsData() {
        return compressedBugsData;
    }

    public final String getBugsIdentifier(){ return bugsIdentifier;}

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public String getTranslatedCompressedData() {
        return translatedCompressedData;
    }

    public void setBugsData(TraceableBugsData bugsData){
        bugsTable = bugsData.bugsTable();
        compressedBugsData = bugsData.getCompressedStringBeforeTranslation();
        translatedCompressedData = bugsData.compressToString();
        if(compressedBugsData == null) {
            compressedBugsData = translatedCompressedData;
        }
        bugsIdentifier = bugsData.getBugsIdentifier();
    }

    @Override
    public String toString() {
        return "BugsInformation{" +
                "bugsTable='" + bugsTable + '\'' +
                ", compressedBugsData='" + compressedBugsData + '\'' +
                ", bugsIdentifier='" + bugsIdentifier + '\'' +
                '}';
    }
}
