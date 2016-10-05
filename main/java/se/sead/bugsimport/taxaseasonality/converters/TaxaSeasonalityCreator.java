package se.sead.bugsimport.taxaseasonality.converters;

import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.taxaseasonality.seadmodel.ActivityType;
import se.sead.bugsimport.taxaseasonality.seadmodel.Season;
import se.sead.bugsimport.taxaseasonality.seadmodel.TaxaSeasonality;
import se.sead.utils.ErrorCopier;

public class TaxaSeasonalityCreator {

    private Location location;
    private ActivityType activityType;
    private Season season;
    private TaxaSpecies species;

    public TaxaSeasonalityCreator(Location location, ActivityType type, Season season, TaxaSpecies species){
        this.location = location;
        this.activityType = type;
        this.season = season;
        this.species = species;
    }

    public TaxaSeasonality create(){
        TaxaSeasonality seasonality = new TaxaSeasonality();
        seasonality.setLocation(location);
        seasonality.setType(activityType);
        seasonality.setSpecies(species);
        seasonality.setSeason(season);
        addErrors(seasonality);
        return seasonality;
    }

    private void addErrors(TaxaSeasonality seasonality){
        addLocationErrors(seasonality);
        addTypeErrors(seasonality);
        addSpeciesErrors(seasonality);
        addSeasonErrors(seasonality);
    }

    private void addLocationErrors(TaxaSeasonality seasonality) {
        ErrorCopier.copyPotentialErrors(seasonality, location);
    }

    private void addTypeErrors(TaxaSeasonality seasonality){
        ErrorCopier.copyPotentialErrors(seasonality, activityType);
    }

    private void addSpeciesErrors(TaxaSeasonality seasonality){
        ErrorCopier.copyPotentialErrors(seasonality, species);
    }

    private void addSeasonErrors(TaxaSeasonality seasonality){
        ErrorCopier.copyPotentialErrors(seasonality, season);
    }
}
