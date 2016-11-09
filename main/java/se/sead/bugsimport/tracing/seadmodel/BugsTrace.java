package se.sead.bugsimport.tracing.seadmodel;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by erer0001 on 2016-04-28.
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name="bugs_trace", schema = "bugs_import")
@SequenceGenerator(schema = "bugs_import", name = "bugs_trace_seq", sequenceName = "bugs_trace_bugs_trace_id_seq")
public class BugsTrace extends BugsInformation{

    public static final BugsTrace NO_TRACE;

    static {
        NO_TRACE = new BugsTrace();
        NO_TRACE.setSeadId(-1);
    }

    @Id
    @GeneratedValue(generator = "bugs_trace_seq", strategy = GenerationType.IDENTITY)
    @Column(name="bugs_trace_id", nullable = false)
    private Integer id;
    @Column(name="sead_table", nullable = false)
    private String seadTable;
    @Column(name="sead_reference_id", nullable = false)
    private Integer seadId;
    @Column(name="change_date")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date changeDate;
    @Column(name="manipulation_type")
    @Enumerated(value = EnumType.STRING)
    private BugsTraceType type;

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

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public BugsTraceType getType() {
        return type;
    }

    public void setType(BugsTraceType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "BugsTrace{" +
                "id=" + id +
                ", seadTable='" + seadTable + '\'' +
                ", seadId=" + seadId +
                ", changeDate=" + changeDate +
                ", type=" + type +
                '}';
    }
}
