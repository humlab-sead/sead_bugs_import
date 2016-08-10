package se.sead.bugsimport;

import se.sead.bugs.TraceableBugsData;
import se.sead.bugsimport.tracing.TracePersister;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.PersistenceException;

public abstract class Persister<BugsType extends TraceableBugsData, SeadType extends LoggableEntity> {

    private TracePersister tracePersister;

    protected Persister(TracePersister tracePersister){
        this.tracePersister = tracePersister;
    }

    public void persist(BugsSeadMapper<BugsType, SeadType> mapper){
        MappingResult<BugsType, SeadType> mapperResult = mapper.getMapperResult();
        for (MappingResult.BugsListSeadMapping<BugsType, SeadType> dataMapping :
                mapperResult.getData()) {
            if (dataMapping.isErrorFree() && (dataMapping.isNewSeadData() || dataMapping.isUpdatedSeadData())) {
                doSave(dataMapping);
            } else {
                insertErrorLog(dataMapping);
            }
        }
//        for (MappingResult.BugsSingleSeadMapping<BugsType, SeadType> mappedData:
//             mapperResult.getTupleData()) {
//            if(mappedData.isErrorFree()){
//                if(mappedData.isNewSeadData() || mappedData.isUpdatedSeadData()) {
//                    doSave(mappedData);
//                }
//            } else {
//                insertErrorLog(mappedData);
//            }
//        }
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
    @Deprecated
    private void doSave(MappingResult.BugsSingleSeadMapping<BugsType, SeadType> mappedData) {
        try {
            SeadType savedItem = save(mappedData.getSeadData());
            mappedData.setSavedSeadData(savedItem);
            insertTraceLog(mappedData);
        } catch (PersistenceException pe){
            mappedData.getSeadData().addError(pe.getMessage());
            insertErrorLog(mappedData);
        }
    }

    protected abstract SeadType save(SeadType seadValue);


    private void insertTraceLog(MappingResult.BugsSingleSeadMapping<BugsType, SeadType> mappedData){
        tracePersister.saveCurrentEventFor(mappedData.getBugsData());
    }

    private void insertTraceLog(MappingResult.BugsListSeadMapping<BugsType, SeadType> mappedData){
        tracePersister.saveCurrentEventFor(mappedData.getBugsData());
    }

    private void insertErrorLog(MappingResult.BugsSingleSeadMapping<BugsType, SeadType> dataContainer){
        tracePersister.saveErrorLog(dataContainer.getBugsData(), dataContainer.getSeadData());
    }

    private void insertErrorLog(MappingResult.BugsListSeadMapping<BugsType, SeadType> dataContainer){
        tracePersister.saveErrorLog(dataContainer.getBugsData(), dataContainer.getErrorMessages());
    }

}
