package se.sead.bugsimport.species.seadmodel;

import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

/**
 * Created by erer0001 on 2016-04-21.
 */
@Entity
@Table(name="tbl_taxa_tree_families", schema = "public")
@SequenceGenerator(name="taxa_family_id_seq", sequenceName = "tbl_taxa_tree_families_family_id_seq")
public class TaxaFamily extends LoggableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "taxa_family_id_seq")
    @Column(name="family_id")
    private Integer id;
    @Column(name="family_name")
    private String familyName;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="order_id")
    private TaxaOrder orderId;

    public TaxaFamily(){}

    public Integer getId() {
        return id;
    }

    protected void setId(Integer id){
        this.id = id;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public TaxaOrder getOrderId() {
        return orderId;
    }

    public void setOrderId(TaxaOrder orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof TaxaFamily)) return false;

        TaxaFamily that = (TaxaFamily) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (!familyName.equals(that.familyName)) return false;
        return orderId.equals(that.orderId);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + familyName.hashCode();
        result = 31 * result + orderId.hashCode();
        return result;
    }
}
