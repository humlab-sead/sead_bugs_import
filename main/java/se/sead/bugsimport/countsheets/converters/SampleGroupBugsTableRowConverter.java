package se.sead.bugsimport.countsheets.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.countsheets.bugsmodel.Countsheet;
import se.sead.bugsimport.countsheets.seadmodel.SampleGroup;
import se.sead.bugsimport.site.helper.SiteFromCodeDisallowDeletedSite;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.repositories.SampleGroupRepository;
import se.sead.repositories.SamplingContextRepository;
import se.sead.sead.methods.MethodManager;
import se.sead.sead.model.SamplingContext;

import java.util.List;

@Component
public class SampleGroupBugsTableRowConverter implements BugsTableRowConverter<Countsheet, SampleGroup> {

    @Autowired
    private SiteFromCodeDisallowDeletedSite siteFromBugsCodeHelper;

    @Autowired
    private SampleGroupBugsTraceReader sampleGroupTraceReader;

    @Autowired
    private MethodManager methodManager;

    @Autowired
    private SamplingContextRepository contextRepository;

    @Autowired
    private SampleGroupRepository sampleGroupRepository;

    @Override
    public SampleGroup convertForDataRow(Countsheet bugsData) {
        SeadSite site = siteFromBugsCodeHelper.getSeadSiteFromBugsCode(bugsData.getSiteCode());
        SamplingContext context = contextRepository.findByNameIgnoreCase(bugsData.getSheetContext());
        return createOrUpdate(bugsData, site, context);
    }

    private SampleGroup createOrUpdate(Countsheet bugsData, SeadSite site, SamplingContext context){
        SampleGroup previousGroup = sampleGroupTraceReader.getSampleGroup(bugsData);
        List<SampleGroup> previousByName = sampleGroupRepository.findAllBySiteAndNameIgnoreCase(site, bugsData.getName());
        if(previousGroup == null && previousByName.isEmpty()){
            return create(bugsData, site, context);
        } else if(previousGroup == null && !previousByName.isEmpty()){
            SampleGroup group = new SampleGroup();
            group.addError("Name found for site, but no bugs import marker: change name? countsheet=" + bugsData.getName() + "site=" + bugsData.getSiteCode());
            return group;
        }
        return update(bugsData, site, context, previousGroup);
    }

    private SampleGroup create(Countsheet bugsData, SeadSite site, SamplingContext context){
        return new SampleGroupCreator(
                bugsData,
                site,
                context,
                methodManager.getSamplingMethodName()
        ).create();
    }

    private SampleGroup update(Countsheet bugsData, SeadSite site, SamplingContext context, SampleGroup previousGroup) {
        return new SampleGroupUpdater(
                bugsData,
                site,
                context,
                methodManager.getSamplingMethodName()
        ).update(previousGroup);
    }
}
