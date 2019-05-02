package se.sead.bugsimport.tracing.accessors;

import se.sead.bugsimport.tracing.seadmodel.BugsTrace;

import java.util.List;

public class IgnoreCaseCompressedDataWithSeadDataTable extends CompressedDataWithSeadDataTable {

    public IgnoreCaseCompressedDataWithSeadDataTable(String bugsTable, String seadDataTable) {
        super(bugsTable, seadDataTable);
    }

    @Override
    protected List<BugsTrace> searchBugsTraces(String traceDefinition) {
        return traceRepository.findByBugsTableAndSeadTableAndAccessInformationIgnoreCaseDataOrderByChangeDate(bugsTable, seadDataTable, traceDefinition);
    }
}
