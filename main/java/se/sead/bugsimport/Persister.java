package se.sead.bugsimport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import se.sead.bugs.TraceableBugsData;
import se.sead.bugsimport.tracing.TraceEventManager;
import se.sead.bugsimport.tracing.TracePersister;
import se.sead.sead.model.LoggableEntity;
import se.sead.utils.errorlog.ExceptionErrorLog;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Persister<BugsType extends TraceableBugsData, SeadType extends LoggableEntity> {

    @Autowired
    private TracePersister tracePersister;

    public void persist(MappingResult<BugsType, SeadType> mapperResult){
        for (BugsListSeadMapping<BugsType, SeadType> dataMapping :
                mapperResult.getData()) {
            if(!dataMapping.isErrorFree()){
                insertErrorLog(dataMapping);
            } else if(dataMapping.isNewSeadData() || dataMapping.isUpdatedSeadData()) {
                doSave(dataMapping);
            }
        }
    }

    private void doSave(BugsListSeadMapping<BugsType, SeadType> mappedData){
        ArrayList<SeadType> stuff = new ArrayList<>(mappedData.getSeadData());
        for (SeadType seadData:
                stuff) {
            try {
                SeadType savedItem = save(seadData);
                mappedData.setSavedData(seadData, savedItem);
                insertTraceLog(mappedData);
                if(seadData.isFlagged()){
                    insertErrorLogForFlag(mappedData, savedItem);
                }
            } catch (DataAccessException dae) {
                seadData.addError(new ExceptionErrorLog(dae));
                insertErrorLog(mappedData);
            }
        }
        mappedData.resyncStoredItems();
    }

    protected abstract SeadType save(SeadType seadValue);


    private void insertTraceLog(BugsListSeadMapping<BugsType, SeadType> mappedData){
        tracePersister.saveCurrentEventFor(mappedData.getBugsData());
    }

    private void insertErrorLog(BugsListSeadMapping<BugsType, SeadType> dataContainer){
        tracePersister.saveErrorLog(dataContainer.getBugsData(), dataContainer.getErrorMessages());
    }

    private void insertErrorLogForFlag(BugsListSeadMapping<BugsType, SeadType> dataContainer, SeadType currentDataPoint){
        String tableName = TraceEventManager.getTableNameFromClassAnnotation(currentDataPoint);
        tracePersister.saveErrorLog(dataContainer.getBugsData(), Arrays.asList(
                "FLAGGED: saved sead index: " + tableName + ":" + currentDataPoint.getId()
        ));
    }
}
