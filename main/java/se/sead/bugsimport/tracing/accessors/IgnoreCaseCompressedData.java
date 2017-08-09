package se.sead.bugsimport.tracing.accessors;

import se.sead.bugsimport.tracing.seadmodel.BugsTrace;

import java.util.List;

public class IgnoreCaseCompressedData extends CompressedData {

    public IgnoreCaseCompressedData(String bugsTable) {
        super(bugsTable);
    }

    @Override
    protected List<BugsTrace> searchBugsTraces(String traceDefinition) {
        return traceRepository.findByBugsTableAndAccessInformationIgnoreCaseData(bugsTable, traceDefinition);
    }
}
