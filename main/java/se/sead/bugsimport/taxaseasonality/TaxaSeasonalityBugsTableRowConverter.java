package se.sead.bugsimport.taxaseasonality;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.species.converters.TaxonomicOrderConverter;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.taxaseasonality.bugsmodel.SeasonActiveAdult;
import se.sead.bugsimport.taxaseasonality.converters.*;
import se.sead.bugsimport.taxaseasonality.seadmodel.ActivityType;
import se.sead.bugsimport.taxaseasonality.seadmodel.Season;
import se.sead.bugsimport.taxaseasonality.seadmodel.TaxaSeasonality;
import se.sead.repositories.ActivityTypeRepository;
import se.sead.repositories.TaxaSeasonalityRepository;

@Component
public class TaxaSeasonalityBugsTableRowConverter implements BugsTableRowConverter<SeasonActiveAdult, TaxaSeasonality> {

    @Autowired
    private SeasonActiveAdultCountryConverter countryConverter;
    @Autowired
    private SeasonActiveAdultSpeciesConverter speciesConverter;
    @Autowired
    private SeasonActiveAdultSeasonConverter seasonConverter;
    @Autowired
    private TaxaSeasonalityRepository seasonalityRepository;
    @Autowired
    private SeasonActiveTraceHelper traceHelper;

    private ActivityType adultActiveType;

    @Autowired
    public TaxaSeasonalityBugsTableRowConverter(ActivityTypeRepository activityTypeRepository){
        adultActiveType = activityTypeRepository.findAdultActiveType();
        if(adultActiveType == null) {
            throw new IllegalStateException("No adult active activity type found");
        }
    }

    @Override
    public TaxaSeasonality convertForDataRow(SeasonActiveAdult bugsData) {
        TaxaSeasonality fromTrace = traceHelper.getFromLastTrace(bugsData.compressToString());
        if(fromTrace == null) {
            Location location = countryConverter.getLocation(bugsData);
            TaxaSpecies species = speciesConverter.getSpecies(bugsData);
            Season season = seasonConverter.getSeason(bugsData);
            TaxaSeasonality previousStoredVersion = getPreviousTaxaSeasonality(location, species, season);
            if (previousStoredVersion == null) {
                return new TaxaSeasonalityCreator(location, adultActiveType, season, species).create();
            } else {
                return previousStoredVersion;
            }
        } else {
            return fromTrace;
        }
    }

    private TaxaSeasonality getPreviousTaxaSeasonality(Location location, TaxaSpecies species, Season season) {
        if(allErrorFree(location, species, season)) {

            return seasonalityRepository.findByLocationAndTypeAndSpeciesAndSeason(location, adultActiveType, species, season);
        } else {
            return null;
        }
    }

    private boolean allErrorFree(Location location, TaxaSpecies species, Season season){
        return location.isErrorFree() &&
                species.isErrorFree() &&
                season.isErrorFree();
    }


}
