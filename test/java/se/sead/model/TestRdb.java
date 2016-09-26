package se.sead.model;

import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.rdb.seadmodel.Rdb;
import se.sead.bugsimport.rdbcodes.seadmodel.RdbCode;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;

public class TestRdb extends Rdb {

    private TestRdb(Integer id){
        super.setId(id);
    }

    public static Rdb create(Integer id, TaxaSpecies species, Location country, RdbCode code){
        Rdb rdb = new TestRdb(id);
        rdb.setCountry(country);
        rdb.setSpecies(species);
        rdb.setRdbCode(code);
        return rdb;
    }
}
