package se.sead.bugsimport.species.seadmodel;

import javax.persistence.*;

@Entity
@Table(name = "tbl_taxonomic_order_systems", schema = "public")
@SequenceGenerator(name="taxonomic_order_system_id_seq", sequenceName = "tbl_taxonomic_order_systems_taxonomic_order_system_id_seq")
public class TaxonomicOrderSystem implements Comparable<TaxonomicOrderSystem>{
    @Id
    @GeneratedValue(generator = "taxonomic_order_system_id_seq", strategy = GenerationType.IDENTITY)
    @Column(name="taxonomic_order_system_id", nullable = false)
    private Integer id;
    @Column(name="system_name")
    private String systemName;

    public Integer getId() {
        return id;
    }
    protected void setId(Integer id){ this.id = id;}

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    @Override
    public int compareTo(TaxonomicOrderSystem o) {
        if(o == null){
            return -1;
        }
        return systemName.compareTo(o.getSystemName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof TaxonomicOrderSystem)) return false;

        TaxonomicOrderSystem that = (TaxonomicOrderSystem) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
