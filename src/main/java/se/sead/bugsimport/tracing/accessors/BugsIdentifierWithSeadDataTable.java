package se.sead.bugsimport.tracing.accessors;

import se.sead.bugsimport.tracing.seadmodel.BugsTrace;

import java.util.List;

public class BugsIdentifierWithSeadDataTable extends SeadDataTableTraceAccessor {

    public BugsIdentifierWithSeadDataTable(String bugsTable, String seadDataTable) {
        super(bugsTable, seadDataTable);
    }

    @Override
    protected List<BugsTrace> searchBugsTraces(String traceDefinition) {
        return traceRepository.findByBugsTableAndSeadTableAndBugsIdentifierOrderByChangeDate(bugsTable, seadDataTable, traceDefinition);
    }
}
