package se.sead.bugsimport.tracing.seadmodel;

import se.sead.bugs.TraceableBugsData;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BugsInformation {

    @Column(name="bugs_table", nullable = false)
    private String bugsTable;
    @Column(name="bugs_data", nullable = false)
    private String compressedBugsData;

    public final String getBugsTable() {
        return bugsTable;
    }

    public final String getCompressedBugsData() {
        return compressedBugsData;
    }

    public void setBugsData(TraceableBugsData bugsData){
        bugsTable = bugsData.bugsTable();
        compressedBugsData = bugsData.compressToString();
    }
}
