package se.sead.bugsimport.taxaseasonality.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.taxaseasonality.converters.TaxaSeasonalityCreator;
import se.sead.bugsimport.taxaseasonality.seadmodel.Season;
import se.sead.bugsimport.taxaseasonality.seadmodel.TaxaSeasonality;
import se.sead.repositories.ActivityTypeRepository;

@Component
@Order
public class CreateNewSeasonalityRule extends SeasonalityByPartsBaseSearch implements SeasonalitySearchRule {

    @Autowired
    public CreateNewSeasonalityRule(ActivityTypeRepository activityTypeRepository){
        super(activityTypeRepository);
    }

    @Override
    protected TaxaSeasonality getImplementation(Location location, TaxaSpecies species, Season season) {
        return new TaxaSeasonalityCreator(location, adultActiveType, season, species).create();
    }
}
