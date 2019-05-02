package se.sead.bugsimport.taxaseasonality.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.taxaseasonality.seadmodel.Season;
import se.sead.bugsimport.taxaseasonality.seadmodel.TaxaSeasonality;
import se.sead.repositories.ActivityTypeRepository;
import se.sead.repositories.TaxaSeasonalityRepository;

@Component
@Order(3)
public class SeasonalityFromDatabaseSearchRule extends SeasonalityByPartsBaseSearch implements SeasonalitySearchRule {

    @Autowired
    private TaxaSeasonalityRepository seasonalityRepository;

    @Autowired
    public SeasonalityFromDatabaseSearchRule(ActivityTypeRepository activityTypeRepository){
        super(activityTypeRepository);
    }

    @Override
    protected TaxaSeasonality getImplementation(Location location, TaxaSpecies species, Season season){
        return seasonalityRepository.findByLocationAndTypeAndSpeciesAndSeason(location, adultActiveType, species, season);
    }
}
