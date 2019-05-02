package se.sead.bugsimport.mcr.seadmodel;

import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

/**
 * Created by erer0001 on 2016-05-13.
 */
@Entity
@Table(name="tbl_mcrdata_birmbeetledat")
@SequenceGenerator(name = "birmbeetledat_id_seq", sequenceName = "tbl_mcrdata_birmbeetledat_mcrdata_birmbeetledat_id_seq")
public class BirmBeetleData extends LoggableEntity implements Comparable<BirmBeetleData>{

    @Id
    @GeneratedValue(generator = "birmbeetledat_id_seq", strategy = GenerationType.IDENTITY)
    @Column(name="mcrdata_birmbeetledat_id", nullable = false)
    private Integer id;

    @Column(name="mcr_data")
    private String mcrData;
    @Column(name="mcr_row", nullable = false)
    private Short rowNumber;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "taxon_id", nullable = false)
    private TaxaSpecies species;

    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id){
        this.id = id;
    }

    public String getMcrData() {
        return mcrData;
    }

    public void setMcrData(String mcrData) {
        this.mcrData = mcrData;
    }

    public Short getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Short rowNumber) {
        this.rowNumber = rowNumber;
    }

    public TaxaSpecies getSpecies() {
        return species;
    }

    public void setSpecies(TaxaSpecies species) {
        this.species = species;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof BirmBeetleData)) return false;

        BirmBeetleData that = (BirmBeetleData) o;

        if (!id.equals(that.id)) return false;
        if (!mcrData.equals(that.mcrData)) return false;
        if (rowNumber != null ? !rowNumber.equals(that.rowNumber) : that.rowNumber != null) return false;
        return species != null ? species.equals(that.species) : that.species == null;

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + mcrData.hashCode();
        result = 31 * result + (rowNumber != null ? rowNumber.hashCode() : 0);
        result = 31 * result + (species != null ? species.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(BirmBeetleData o) {
        return rowNumber.compareTo(o.getRowNumber());
    }

    @Override
    public String toString(){
        return "BirmBeetleData{" +
                species + "," +
                rowNumber + "," +
                mcrData + "}";
    }
}
