package se.sead.bugsimport.tracing;

import org.springframework.beans.factory.annotation.Autowired;
import se.sead.bugs.TraceableBugsData;
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
    private BugsTraceRepository traceRepository;
    private CreateAndReadRepository<SeadType, Integer> accessRepository;
    private String bugsTableName;

    private boolean useCompressedDataForIdentification;
    private String seadTableName = null;

    public SeadDataFromTraceHelper(boolean useCompressedDataForIdentification, CreateAndReadRepository<SeadType, Integer> accessRepository){
        this(null, useCompressedDataForIdentification, accessRepository);
    }

    public SeadDataFromTraceHelper(String bugsTableName, boolean useCompressedDataForIdentification, CreateAndReadRepository<SeadType, Integer> accessRepository){
        this.bugsTableName = bugsTableName;
        this.useCompressedDataForIdentification = useCompressedDataForIdentification;
        this.accessRepository = accessRepository;
    }

    public SeadDataFromTraceHelper(String bugsTableName, String seadTableName, boolean useCompressedDataForIdentification, CreateAndReadRepository<SeadType, Integer> accessRepository){
        this(bugsTableName, useCompressedDataForIdentification, accessRepository);
        this.seadTableName = seadTableName;
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
        List<BugsTrace> traces;
        if(useCompressedDataForIdentification && seadTableName != null) {
            traces = traceRepository.findByBugsTableAndSeadTableAndCompressedBugsDataOrderByChangeDate(getBugsTableName(), getSeadTableName(), traceIdentifier);
        } else if(useCompressedDataForIdentification && seadTableName == null){
            traces = traceRepository.findByBugsTableAndCompressedBugsDataOrderByChangeDate(getBugsTableName(), traceIdentifier);
        } else if(!useCompressedDataForIdentification && seadTableName != null) {
            traces = traceRepository.findByBugsTableAndSeadTableAndBugsIdentifierOrderByChangeDate(getBugsTableName(), getSeadTableName(), traceIdentifier);
        } else {
            traces = traceRepository.findByBugsTableAndBugsIdentifierOrderByChangeDate(getBugsTableName(), traceIdentifier);
        }
        return traces;
    }

    protected BugsTrace getSelectedResult(List<BugsTrace> traces){
        return traces.get(0);
    }

    protected String getBugsTableName(){
        return bugsTableName;
    }

    protected String getSeadTableName(){
        return seadTableName;
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
}
