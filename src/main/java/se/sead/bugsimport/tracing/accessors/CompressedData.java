package se.sead.bugsimport.tracing.accessors;

import se.sead.bugsimport.tracing.seadmodel.BugsTrace;

import java.util.List;

public class CompressedData extends TraceAccessor {

    public CompressedData(String bugsTable) {
        super(bugsTable);
    }

    @Override
    protected List<BugsTrace> searchBugsTraces(String traceDefinition) {
        return traceRepository.findByBugsTableAndAccessInformationData(bugsTable, traceDefinition);
    }
}
