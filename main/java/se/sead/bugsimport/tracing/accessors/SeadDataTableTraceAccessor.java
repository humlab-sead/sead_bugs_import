package se.sead.bugsimport.tracing.accessors;

public abstract class SeadDataTableTraceAccessor extends TraceAccessor {

    protected String seadDataTable;

    protected SeadDataTableTraceAccessor(String bugsTable, String seadDataTable) {
        super(bugsTable);
        this.seadDataTable = seadDataTable;
    }
}
