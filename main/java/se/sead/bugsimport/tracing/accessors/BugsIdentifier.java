package se.sead.bugsimport.tracing.accessors;

import se.sead.bugsimport.tracing.seadmodel.BugsTrace;

import java.util.List;

public class BugsIdentifier extends TraceAccessor {

    public BugsIdentifier(String bugsTable) {
        super(bugsTable);
    }

    @Override
    protected List<BugsTrace> searchBugsTraces(String traceDefinition) {
        return traceRepository.findByBugsTableAndBugsIdentifierOrderByChangeDate(bugsTable, traceDefinition);
    }
}
