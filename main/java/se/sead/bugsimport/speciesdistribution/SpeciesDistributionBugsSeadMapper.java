package se.sead.bugsimport.speciesdistribution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.MappingResult;
import se.sead.bugsimport.SetMappingResult;
import se.sead.bugsimport.speciesdistribution.bugsmodel.Distrib;
import se.sead.bugsimport.speciesdistribution.bugsmodel.DistribBugsTable;
import se.sead.bugsimport.speciesdistribution.seadmodel.TextDistribution;

@Component
public class SpeciesDistributionBugsSeadMapper extends BugsSeadMapper<Distrib, TextDistribution> {

    @Autowired
    public SpeciesDistributionBugsSeadMapper(SpeciesDistributionTableRowConverter singleBugsTableRowConverterForMapper) {
        super(
                new DistribBugsTable(),
                singleBugsTableRowConverterForMapper
        );
    }

    @Override
    protected MappingResult<Distrib, TextDistribution> initMapperResultContainer() {
        return new SetMappingResult<>();
    }
}
