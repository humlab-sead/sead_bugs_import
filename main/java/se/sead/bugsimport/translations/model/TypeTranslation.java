package se.sead.bugsimport.translations.model;

import javax.persistence.*;

@Entity
@Table(name="bugs_type_translations", schema = "bugs_import")
@SequenceGenerator(name="bugs_type_translation_id_seq", sequenceName = "bugs_type_translation_id_seq", schema = "bugs_import")
public class TypeTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "bugs_type_translation_id_seq")
    @Column(name="type_translation_id")
    private Integer id;
    @Column
    private String bugsTable;
    @Column
    private String bugsColumn;
    @Column
    private String triggeringColumnValue;
    @Column
    private String targetColumn;
    @Column
    private String replacementValue;

    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public String getBugsTable() {
        return bugsTable;
    }

    public void setBugsTable(String bugsTable) {
        this.bugsTable = bugsTable;
    }

    public String getBugsColumn() {
        return bugsColumn;
    }

    public void setBugsColumn(String bugsColumn) {
        this.bugsColumn = bugsColumn;
    }

    public String getTriggeringColumnValue() {
        return triggeringColumnValue;
    }

    public void setTriggeringColumnValue(String triggeringColumnValue) {
        this.triggeringColumnValue = triggeringColumnValue;
    }

    public String getTargetColumn() {
        return targetColumn;
    }

    public void setTargetColumn(String targetColumn) {
        this.targetColumn = targetColumn;
    }

    public String getReplacementValue() {
        return replacementValue;
    }

    public void setReplacementValue(String replacementValue) {
        this.replacementValue = replacementValue;
    }
}
