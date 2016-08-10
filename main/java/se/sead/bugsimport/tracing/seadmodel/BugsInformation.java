package se.sead.bugsimport.tracing.seadmodel;

import org.springframework.stereotype.Component;
import se.sead.bugs.TraceableBugsData;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BugsInformation {

    @Column(name="bugs_table", nullable = false)
    private String bugsTable;
    @Column(name="bugs_data", nullable = false)
    private String compressedBugsData;
    @Column(name="bugs_identifier")
    private String bugsIdentifier;

    public final String getBugsTable() {
        return bugsTable;
    }

    public final String getCompressedBugsData() {
        return compressedBugsData;
    }

    public final String getBugsIdentifier(){ return bugsIdentifier;}

    public void setBugsData(TraceableBugsData bugsData){
        bugsTable = bugsData.bugsTable();
        compressedBugsData = bugsData.getCompressedStringBeforeTranslation();
        bugsIdentifier = bugsData.getBugsIdentifier();
        if(compressedBugsData == null) {
            compressedBugsData = bugsData.compressToString();
        }
    }
}
