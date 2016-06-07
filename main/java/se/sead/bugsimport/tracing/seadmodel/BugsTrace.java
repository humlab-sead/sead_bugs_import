package se.sead.bugsimport.tracing.seadmodel;

import se.sead.bugs.TraceableBugsData;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by erer0001 on 2016-04-28.
 */
@Entity
@Table(name="bugs_trace", schema = "bugs_import")
@SequenceGenerator(schema = "bugs_import", name = "bugs_trace_seq", sequenceName = "bugs_trace_bugs_trace_id_seq")
public class BugsTrace extends BugsInformation{

    @Id
    @GeneratedValue(generator = "bugs_trace_seq", strategy = GenerationType.AUTO)
    @Column(name="bugs_trace_id", nullable = false)
    private Integer id;
    @Column(name="sead_table", nullable = false)
    private String seadTable;
    @Column(name="sead_reference_id", nullable = false)
    private Integer seadId;
    @Column(name="import_date")
    private Date importDate;

    public Integer getId() {
        return id;
    }

    public String getSeadTable() {
        return seadTable;
    }

    public void setSeadTable(String seadTable) {
        this.seadTable = seadTable;
    }

    public Integer getSeadId() {
        return seadId;
    }

    public void setSeadId(Integer seadId) {
        this.seadId = seadId;
    }

    public Date getImportDate() {
        return importDate;
    }

    @Override
    public String toString() {
        return "BugsTrace{" +
                "id=" + id +
                ", bugsTable='" + getBugsTable() + '\'' +
                ", bugsData='" + getCompressedBugsData() + '\'' +
                ", seadTable='" + seadTable + '\'' +
                ", seadId=" + seadId +
                '}';
    }
}
