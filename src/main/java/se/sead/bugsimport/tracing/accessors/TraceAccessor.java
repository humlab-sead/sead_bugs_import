package se.sead.bugsimport.tracing.accessors;

import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.repositories.BugsTraceRepository;

import java.util.List;

public abstract class TraceAccessor {

    protected BugsTraceRepository traceRepository;
    protected String bugsTable;

    protected TraceAccessor(String bugsTable){
        this.bugsTable = bugsTable;
    }

    public void setBugsTraceRepository(BugsTraceRepository repository){
        traceRepository = repository;
    }

    public List<BugsTrace> getBugsTraces(String traceDefinition){
        if(traceRepository == null){
            throw new NullPointerException("Non-initialized repository");
        }
        return searchBugsTraces(traceDefinition);
    }

    protected abstract List<BugsTrace> searchBugsTraces(String traceDefinition);
}
