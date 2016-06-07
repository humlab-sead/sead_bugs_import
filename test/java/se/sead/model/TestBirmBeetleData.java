package se.sead.model;

import se.sead.bugsimport.mcr.seadmodel.BirmBeetleData;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;

/**
 * Created by erer0001 on 2016-05-13.
 */
public class TestBirmBeetleData extends BirmBeetleData {

    private TestBirmBeetleData(Integer id){
        super.setId(id);
    }

    public static BirmBeetleData create(Integer id, String mcrData, Integer rowNumber, TaxaSpecies species){
        BirmBeetleData data = new TestBirmBeetleData(id);
        data.setMcrData(mcrData);
        data.setRowNumber(rowNumber.shortValue());
        data.setSpecies(species);
        return data;
    }
}
