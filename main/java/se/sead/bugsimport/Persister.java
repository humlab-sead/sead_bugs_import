package se.sead.bugsimport;

import org.springframework.beans.factory.annotation.Autowired;
import se.sead.bugs.TraceableBugsData;
import se.sead.bugsimport.tracing.TracePersister;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.PersistenceException;

public abstract class Persister<BugsType extends TraceableBugsData, SeadType extends LoggableEntity> {

    @Autowired
    private TracePersister tracePersister;

    public void persist(MappingResult<BugsType, SeadType> mapperResult){
        for (MappingResult.BugsListSeadMapping<BugsType, SeadType> dataMapping :
                mapperResult.getData()) {
            if(!dataMapping.isErrorFree()){
                insertErrorLog(dataMapping);
            } else if(dataMapping.isNewSeadData() || dataMapping.isUpdatedSeadData()) {
                doSave(dataMapping);
            }
        }
    }

    private void doSave(MappingResult.BugsListSeadMapping<BugsType, SeadType> mappedData){
        for (SeadType seadData:
                mappedData.getSeadData()) {
            try {
                SeadType savedItem = save(seadData);
                mappedData.setSavedData(seadData, savedItem);
                insertTraceLog(mappedData);
            } catch (PersistenceException pe) {
                seadData.addError(pe.getMessage());
                insertErrorLog(mappedData);
            }
        }
    }

    protected abstract SeadType save(SeadType seadValue);


    private void insertTraceLog(MappingResult.BugsListSeadMapping<BugsType, SeadType> mappedData){
        tracePersister.saveCurrentEventFor(mappedData.getBugsData());
    }

    private void insertErrorLog(MappingResult.BugsListSeadMapping<BugsType, SeadType> dataContainer){
        tracePersister.saveErrorLog(dataContainer.getBugsData(), dataContainer.getErrorMessages());
    }

}
