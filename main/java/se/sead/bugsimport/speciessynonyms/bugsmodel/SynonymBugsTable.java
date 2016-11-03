package se.sead.bugsimport.speciessynonyms.bugsmodel;

import com.healthmarketscience.jackcess.Row;
import se.sead.bugs.BugsTable;

public class SynonymBugsTable extends BugsTable<Synonym> {

    public static final String TABLE_NAME = "TSynonym";

    public SynonymBugsTable(){
        super(TABLE_NAME);
    }

    @Override
    public Synonym createItem(Row accessRow) {
        Synonym synonym = new Synonym();
        synonym.setCODE(accessRow.getDouble("CODE"));
        synonym.setSynGenus(accessRow.getString("SynGenus"));
        synonym.setSynSpecies(accessRow.getString("SynSpecies"));
        synonym.setSynAuthority(accessRow.getString("SynAuthority"));
        synonym.setReference(accessRow.getString("Ref"));
        synonym.setNotes(accessRow.getString("Notes"));
        return synonym;
    }
}
