package se.sead.bugsimport.mcr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.mcr.bugsmodel.MCRSummaryData;
import se.sead.bugsimport.mcr.seadmodel.MCRSummary;
import se.sead.bugsimport.tracing.TracePersister;
import se.sead.repositories.MCRSummaryRepository;

@Component
public class MCRSummaryPersister extends Persister<MCRSummaryData, MCRSummary> {

    @Autowired
    private MCRSummaryRepository repository;

    @Autowired
    public MCRSummaryPersister(TracePersister tracePersister) {
        super(tracePersister);
    }

    @Override
    protected MCRSummary save(MCRSummary seadValue) {
        return repository.saveOrUpdate(seadValue);
    }
}
