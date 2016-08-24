package se.sead.bugsimport.taxaseasonality;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.MappingResult;
import se.sead.bugsimport.species.converters.TaxonomicOrderConverter;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.species.seadmodel.TaxonomicOrder;
import se.sead.bugsimport.taxaseasonality.bugsmodel.SeasonActiveAdult;
import se.sead.bugsimport.taxaseasonality.bugsmodel.SeasonActiveAdultBugsTable;
import se.sead.bugsimport.taxaseasonality.seadmodel.TaxaSeasonality;
import se.sead.repositories.TaxaSeasonalityRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TaxaSeasonalityBugsMapper extends BugsSeadMapper<SeasonActiveAdult, TaxaSeasonality> {

    @Autowired
    public TaxaSeasonalityBugsMapper(
        TaxaSeasonalityBugsTableRowConverter rowConverter
    ){
        super(new SeasonActiveAdultBugsTable(), rowConverter);
    }
}
