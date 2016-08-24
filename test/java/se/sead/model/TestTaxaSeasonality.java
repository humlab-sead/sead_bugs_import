package se.sead.model;

import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.taxaseasonality.seadmodel.ActivityType;
import se.sead.bugsimport.taxaseasonality.seadmodel.Season;
import se.sead.bugsimport.taxaseasonality.seadmodel.TaxaSeasonality;

public class TestTaxaSeasonality extends TaxaSeasonality {

    private TestTaxaSeasonality(Integer id){
        setId(id);
    }

    public static TaxaSeasonality create(Integer id, ActivityType type, Season season, TaxaSpecies species, Location location){
        TaxaSeasonality seasonality = new TestTaxaSeasonality(id);
        seasonality.setLocation(location);
        seasonality.setSeason(season);
        seasonality.setSpecies(species);
        seasonality.setType(type);
        return seasonality;
    }
}
