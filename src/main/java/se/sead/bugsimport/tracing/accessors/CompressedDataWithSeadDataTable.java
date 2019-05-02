package se.sead.bugsimport.tracing.accessors;

import se.sead.bugsimport.tracing.seadmodel.BugsTrace;

import java.util.List;

public class CompressedDataWithSeadDataTable extends SeadDataTableTraceAccessor {

    public CompressedDataWithSeadDataTable(String bugsTable, String seadDataTable) {
        super(bugsTable, seadDataTable);
    }

    @Override
    protected List<BugsTrace> searchBugsTraces(String traceDefinition) {
        return traceRepository.findByBugsTableAndSeadTableAndAccessInformationDataOrderByChangeDate(bugsTable, seadDataTable, traceDefinition);
    }
}
