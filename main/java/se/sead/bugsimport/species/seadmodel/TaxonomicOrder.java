package se.sead.bugsimport.species.seadmodel;

import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by erer0001 on 2016-04-21.
 */
@Entity
@Table(name = "tbl_taxonomic_order", schema = "public")
@SequenceGenerator(name = "taxonomic_order_id_seq", sequenceName = "tbl_taxonomic_order_taxonomic_order_id_seq")
//@SequenceGenerator(name = "id_generator", sequenceName = "tbl_taxonomic_order_taxonomic_order_id_seq")
public class TaxonomicOrder extends LoggableEntity implements Comparable<TaxonomicOrder> {
    @Id
    @GeneratedValue(generator = "taxonomic_order_id_seq", strategy = GenerationType.AUTO)
    @Column(name = "taxonomic_order_id", nullable = false)
    private Integer id;
    @Column(name = "taxonomic_code", precision = 18, scale = 10)
    private BigDecimal code;
    //@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "taxon_id")
    private TaxaSpecies species;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "taxonomic_order_system_id")
    private TaxonomicOrderSystem orderSystem;

    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getCode() {
        return code;
    }

    public void setCode(BigDecimal code) {
        this.code = code;
    }

    public TaxaSpecies getSpecies() {
        return species;
    }

    public void setSpecies(TaxaSpecies species) {
        this.species = species;
    }

    public TaxonomicOrderSystem getOrderSystem() {
        return orderSystem;
    }

    public void setOrderSystem(TaxonomicOrderSystem orderSystem) {
        this.orderSystem = orderSystem;
    }

    @Override
    public int compareTo(TaxonomicOrder o) {
        if (o == null) {
            return -1;
        }
        if (orderSystem.equals(o.getOrderSystem())) {
            return this.getCode().compareTo(o.getCode());
        } else {
            return orderSystem.compareTo(o.getOrderSystem());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof TaxonomicOrder)) return false;

        TaxonomicOrder that = (TaxonomicOrder) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (!code.equals(that.code)) return false;
        if (!species.equals(that.species)) return false;
        return orderSystem.equals(that.orderSystem);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + code.hashCode();
        result = 31 * result + species.hashCode();
        result = 31 * result + orderSystem.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return
                "TaxonOrder{" +
                        (getId() != null ? getId() : "") +
                        "," +
                        (getCode() != null ? getCode().toPlainString() : "") +
                        "," +
                        getSpecies().toString() +
                        "}";
    }
}
