package se.sead.bugsimport.tracing.seadmodel;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import se.sead.bugs.TraceableBugsData;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@MappedSuperclass
@DynamicInsert
@DynamicUpdate
public abstract class BugsInformation {

    @Column(name="bugs_table", nullable = false)
    private String bugsTable;
    @Column(name="bugs_data", nullable = false)
    private String compressedBugsData;
    @Column(name="bugs_identifier")
    private String bugsIdentifier;
    @Column(name="change_date")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date changeDate;

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

    public void setBugsData(TraceableBugsData bugsData){
        bugsTable = bugsData.bugsTable();
        compressedBugsData = bugsData.getCompressedStringBeforeTranslation();
        bugsIdentifier = bugsData.getBugsIdentifier();
        if(compressedBugsData == null) {
            compressedBugsData = bugsData.compressToString();
        }
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
