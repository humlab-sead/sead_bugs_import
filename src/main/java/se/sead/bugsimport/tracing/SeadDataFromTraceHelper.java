package se.sead.bugsimport.tracing;

import org.springframework.beans.factory.annotation.Autowired;
import se.sead.bugs.TraceableBugsData;
import se.sead.bugsimport.tracing.accessors.*;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.repositories.BugsTraceRepository;
import se.sead.repositories.CreateAndReadRepository;
import se.sead.sead.model.LoggableEntity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public abstract class SeadDataFromTraceHelper<BugsType extends TraceableBugsData, SeadType extends LoggableEntity> {

    public static final String SEAD_DATA_HAS_BEEN_UPDATED_SINCE_LAST_BUGS_IMPORT = "Sead data has been updated since last bugs import";

    @Autowired
    protected BugsTraceRepository traceRepository;
    private CreateAndReadRepository<SeadType, Integer> accessRepository;
    private TraceAccessor traceAccessor;

    public SeadDataFromTraceHelper(String bugsTableName, boolean useCompressedDataForIdentification, CreateAndReadRepository<SeadType, Integer> accessRepository){
        this(InternalFactory.createTraceAccessor(bugsTableName, useCompressedDataForIdentification), accessRepository);
    }

    public SeadDataFromTraceHelper(String bugsTableName, String seadTableName, boolean useCompressedDataForIdentification, CreateAndReadRepository<SeadType, Integer> accessRepository){
        this(InternalFactory.createSeadTableTraceAccessor(bugsTableName, seadTableName, useCompressedDataForIdentification), accessRepository);
    }

    public SeadDataFromTraceHelper(TraceAccessor traceAccessor, CreateAndReadRepository<SeadType, Integer> accessRepository){
        this.traceAccessor = traceAccessor;
        this.accessRepository = accessRepository;
    }

    public SeadType getFromLastTrace(String traceIdentifier){
        BugsTrace latest = getLatest(traceIdentifier);
        SeadType seadData = getSeadDataFromTrace(latest);
        if(seadDataExistsAndHasBeenEditedSinceImport(seadData, latest)){
            seadData.addError(SEAD_DATA_HAS_BEEN_UPDATED_SINCE_LAST_BUGS_IMPORT);
        }
        return seadData;
    }

    public BugsTrace getLatest(String traceIdentifier){
        List<BugsTrace> traces = getBugsTraces(traceIdentifier);
        if(traces.isEmpty()){
            return BugsTrace.NO_TRACE;
        } else {
            return getSelectedResult(traces);
        }
    }

    private List<BugsTrace> getBugsTraces(String traceIdentifier) {
        traceAccessor.setBugsTraceRepository(traceRepository);
        return traceAccessor.getBugsTraces(traceIdentifier);
    }

    protected BugsTrace getSelectedResult(List<BugsTrace> traces){
        return traces.get(0);
    }

    public SeadType getSeadDataFromTrace(BugsTrace latest){
        return accessRepository.findOne(latest.getSeadId());
    }

    public static boolean seadDataExistsAndHasBeenEditedSinceImport(LoggableEntity loggableEntity, BugsTrace comparableTrace){
        return seadDataExistsAndHasBeenEditedSinceImportByDate(loggableEntity, comparableTrace);
    }

    public static boolean seadDataExistsAndHasBeenEditedSinceImportByDate(LoggableEntity loggableEntity, BugsTrace comparableTrace){
        if(loggableEntity == null || comparableTrace == BugsTrace.NO_TRACE){
            return false;
        }
        Instant loggableEntityDateUpdated = loggableEntity.getDateUpdated().toInstant().truncatedTo(ChronoUnit.DAYS);
        Instant traceDateChanged = comparableTrace.getChangeDate().toInstant().truncatedTo(ChronoUnit.DAYS);
        return loggableEntityDateUpdated.isAfter(traceDateChanged);
    }

    static class InternalFactory {
        static TraceAccessor createTraceAccessor(String bugsTable, boolean useCompressedIdentifier){
            if(useCompressedIdentifier){
                return new CompressedData(bugsTable);
            } else {
                return new BugsIdentifier(bugsTable);
            }
        }

        static TraceAccessor createSeadTableTraceAccessor(String bugsTable, String seadTableName, boolean useCompressedIdentifier){
            if(useCompressedIdentifier){
                return new CompressedDataWithSeadDataTable(bugsTable, seadTableName);
            } else {
                return new BugsIdentifierWithSeadDataTable(bugsTable, seadTableName);
            }
        }
    }
}
