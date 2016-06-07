package se.sead.bugsimport.speciesdistribution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugs.AccessReaderProvider;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.speciesdistribution.bugsmodel.Distrib;
import se.sead.bugsimport.speciesdistribution.bugsmodel.DistribBugsTable;
import se.sead.bugsimport.speciesdistribution.seadmodel.TextDistribution;

@Component
public class SpeciesDistributionBugsSeadMapper extends BugsSeadMapper<Distrib, TextDistribution> {

    @Autowired
    public SpeciesDistributionBugsSeadMapper(
            AccessReaderProvider accessReaderProvider,
            SpeciesDistributionTableRowConverter singleBugsTableRowConverterForMapper) {
        super(
                accessReaderProvider.getReader(),
                new DistribBugsTable(),
                singleBugsTableRowConverterForMapper);
    }
}
