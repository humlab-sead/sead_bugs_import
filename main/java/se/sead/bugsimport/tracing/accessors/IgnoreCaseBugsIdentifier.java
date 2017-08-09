package se.sead.bugsimport.tracing.accessors;

import se.sead.bugsimport.tracing.seadmodel.BugsTrace;

import java.util.List;

public class IgnoreCaseBugsIdentifier extends BugsIdentifier {

    public IgnoreCaseBugsIdentifier(String bugsTable) {
        super(bugsTable);
    }

    @Override
    protected List<BugsTrace> searchBugsTraces(String traceDefinition) {
        return traceRepository.findByBugsTableAndBugsIdentifierIgnoreCaseOrderByChangeDate(bugsTable, traceDefinition);
    }
}
