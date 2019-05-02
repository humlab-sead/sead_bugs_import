package se.sead.bugsimport.taxaseasonality.search;

import org.springframework.beans.factory.annotation.Autowired;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.taxaseasonality.bugsmodel.SeasonActiveAdult;
import se.sead.bugsimport.taxaseasonality.converters.SeasonActiveAdultCountryConverter;
import se.sead.bugsimport.taxaseasonality.converters.SeasonActiveAdultSeasonConverter;
import se.sead.bugsimport.taxaseasonality.converters.SeasonActiveAdultSpeciesConverter;
import se.sead.bugsimport.taxaseasonality.converters.TaxaSeasonalityCreator;
import se.sead.bugsimport.taxaseasonality.seadmodel.ActivityType;
import se.sead.bugsimport.taxaseasonality.seadmodel.Season;
import se.sead.bugsimport.taxaseasonality.seadmodel.TaxaSeasonality;
import se.sead.repositories.ActivityTypeRepository;

abstract class SeasonalityByPartsBaseSearch implements SeasonalitySearchRule {

    @Autowired
    private SeasonActiveAdultCountryConverter countryConverter;
    @Autowired
    private SeasonActiveAdultSpeciesConverter speciesConverter;
    @Autowired
    private SeasonActiveAdultSeasonConverter seasonConverter;
    protected ActivityType adultActiveType;
    private TaxaSeasonality found;

    protected SeasonalityByPartsBaseSearch(ActivityTypeRepository activityTypeRepository){
        adultActiveType = activityTypeRepository.findAdultActiveType();
        if(adultActiveType == null){
            adultActiveType = new ActivityType();
            adultActiveType.setName("Unknown");
        }
    }

    @Override
    public void init() {
        found = null;
    }

    @Override
    public boolean findFor(SeasonActiveAdult bugsData) {
        Location location = getLocation(bugsData);
        TaxaSpecies species = getSpecies(bugsData);
        Season season = getSeason(bugsData);

        if(allErrorFree(location, species, season)) {
            found = getImplementation(location, species, season);
        } else {
            found = createErrorCarrier(location, species, season);
        }
        return found != null;
    }

    protected Location getLocation(SeasonActiveAdult bugsData){
        return countryConverter.getLocation(bugsData);
    }

    protected TaxaSpecies getSpecies(SeasonActiveAdult bugsData){
        return speciesConverter.getSpecies(bugsData);
    }

    protected Season getSeason(SeasonActiveAdult bugsData){
        return seasonConverter.getSeason(bugsData);
    }

    protected boolean allErrorFree(Location location, TaxaSpecies species, Season season){
        return location.isErrorFree() &&
                species.isErrorFree() &&
                season.isErrorFree();
    }

    protected abstract TaxaSeasonality getImplementation(Location location, TaxaSpecies species, Season season);

    protected TaxaSeasonality createErrorCarrier(Location location, TaxaSpecies species, Season season) {
        return new TaxaSeasonalityCreator(location, adultActiveType, season, species).create();
    }

    @Override
    public TaxaSeasonality getFound() {
        return found;
    }
}
