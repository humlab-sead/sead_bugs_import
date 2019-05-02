package se.sead.bugsimport.taxaseasonality;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.MappingResult;
import se.sead.bugsimport.SetMappingResult;
import se.sead.bugsimport.taxaseasonality.bugsmodel.SeasonActiveAdult;
import se.sead.bugsimport.taxaseasonality.bugsmodel.SeasonActiveAdultBugsTable;
import se.sead.bugsimport.taxaseasonality.seadmodel.TaxaSeasonality;

@Component
public class TaxaSeasonalityBugsMapper extends BugsSeadMapper<SeasonActiveAdult, TaxaSeasonality> {

    @Autowired
    public TaxaSeasonalityBugsMapper(
        TaxaSeasonalityBugsTableRowConverter rowConverter
    ){
        super(new SeasonActiveAdultBugsTable(), rowConverter);
    }

    @Override
    protected MappingResult<SeasonActiveAdult, TaxaSeasonality> initMapperResultContainer() {
        return new SetMappingResult<>();
    }
}
