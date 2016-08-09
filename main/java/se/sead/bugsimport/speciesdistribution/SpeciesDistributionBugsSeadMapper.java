package se.sead.bugsimport.speciesdistribution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugs.AccessReaderProvider;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.speciesdistribution.bugsmodel.Distrib;
import se.sead.bugsimport.speciesdistribution.bugsmodel.DistribBugsTable;
import se.sead.bugsimport.speciesdistribution.seadmodel.TextDistribution;
import se.sead.bugsimport.translations.BugsValueTranslationService;

@Component
public class SpeciesDistributionBugsSeadMapper extends BugsSeadMapper<Distrib, TextDistribution> {

    @Autowired
    public SpeciesDistributionBugsSeadMapper(
            AccessReaderProvider accessReaderProvider,
            SpeciesDistributionTableRowConverter singleBugsTableRowConverterForMapper,
            BugsValueTranslationService dataTranslationService) {
        super(
                accessReaderProvider.getReader(),
                new DistribBugsTable(),
                singleBugsTableRowConverterForMapper,
                dataTranslationService);
    }
}
