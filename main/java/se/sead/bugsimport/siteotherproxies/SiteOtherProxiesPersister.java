package se.sead.bugsimport.siteotherproxies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.siteotherproxies.bugsmodel.SiteOtherProxies;
import se.sead.bugsimport.siteotherproxies.seadmodel.SiteOtherRecord;
import se.sead.bugsimport.tracing.TracePersister;
import se.sead.repositories.SiteOtherRecordRepository;

@Component
public class SiteOtherProxiesPersister extends Persister<SiteOtherProxies, SiteOtherRecord> {

    @Autowired
    private SiteOtherRecordRepository siteOtherRecordRepository;

    @Autowired
    protected SiteOtherProxiesPersister(TracePersister tracePersister) {
        super(tracePersister);
    }

    @Override
    protected SiteOtherRecord save(SiteOtherRecord seadValue) {
        return siteOtherRecordRepository.saveOrUpdate(seadValue);
    }
}
