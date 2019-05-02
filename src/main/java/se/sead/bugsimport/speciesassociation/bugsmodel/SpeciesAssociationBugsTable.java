package se.sead.bugsimport.speciesassociation.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class SpeciesAssociationBugsTable extends BugsTable<BugsSpeciesAssociation> {

    public static final String TABLE_NAME = "TSpeciesAssociations";

    public SpeciesAssociationBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public BugsSpeciesAssociation createItem(Row accessRow) {
        BugsSpeciesAssociation association = new BugsSpeciesAssociation();
        association.setSpeciesAssociationID(accessRow.getInt("SpeciesAssociationID"));
        association.setCode(accessRow.getDouble("CODE"));
        association.setAssociatedSpeciesCODE(accessRow.getDouble("AssociatedSpeciesCODE"));
        association.setAssociationType(accessRow.getString("AssociationType"));
        association.setRef(accessRow.getString("Ref"));
        return association;
    }
}
