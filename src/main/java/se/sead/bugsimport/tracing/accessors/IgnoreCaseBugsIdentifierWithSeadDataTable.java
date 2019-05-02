package se.sead.bugsimport.tracing.accessors;

import se.sead.bugsimport.tracing.seadmodel.BugsTrace;

import java.util.List;

public class IgnoreCaseBugsIdentifierWithSeadDataTable extends BugsIdentifierWithSeadDataTable {

    public IgnoreCaseBugsIdentifierWithSeadDataTable(String bugsTable, String seadDataTable) {
        super(bugsTable, seadDataTable);
    }

    @Override
    protected List<BugsTrace> searchBugsTraces(String traceDefinition) {
        return traceRepository.findByBugsTableAndSeadTableAndBugsIdentifierIgnoreCaseOrderByChangeDate(bugsTable, seadDataTable, traceDefinition);
    }
}
