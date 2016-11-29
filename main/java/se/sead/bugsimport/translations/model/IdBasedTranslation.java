package se.sead.bugsimport.translations.model;

import javax.persistence.*;

@Entity
@Table(name="id_based_translations", schema="bugs_import")
@SequenceGenerator(name = "bugs_id_based_translation_id_seq", sequenceName = "id_based_translations_id_based_translation_id_seq", schema="bugs_import")
public class IdBasedTranslation {

    @Id
    @GeneratedValue(generator = "bugs_id_based_translation_id_seq", strategy = GenerationType.IDENTITY)
    @Column(name="id_based_translation_id")
    private Integer id;

    @Column
    private String bugsDefinition;
    @Column
    private String bugsTable;
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

    public String getBugsDefinition() {
        return bugsDefinition;
    }

    public void setBugsDefinition(String bugsDefinition) {
        this.bugsDefinition = bugsDefinition;
    }

    public String getBugsTable() {
        return bugsTable;
    }

    public void setBugsTable(String bugsTable) {
        this.bugsTable = bugsTable;
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
