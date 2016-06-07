package se.sead.bugsimport;

import se.sead.bugs.TraceableBugsData;
import se.sead.bugsimport.tracing.TracePersister;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.PersistenceException;

/**
 * Created by erer0001 on 2016-05-13.
 */
public abstract class Persister<BugsType extends TraceableBugsData, SeadType extends LoggableEntity> {

    private TracePersister tracePersister;

    protected Persister(TracePersister tracePersister){
        this.tracePersister = tracePersister;
    }

    public void persist(BugsSeadMapper<BugsType, SeadType> mapper){
        MappingResult<BugsType, SeadType> mapperResult = mapper.getMapperResult();
        for (MappingResult.BugsSeadMapping<BugsType, SeadType> mappedData:
             mapperResult.getTupleData()) {
            if(mappedData.canAndShouldSave()){
                doSave(mappedData);
            } else {
                insertErrorLog(mappedData);
            }
        }
    }

    private void doSave(MappingResult.BugsSeadMapping<BugsType, SeadType> mappedData) {
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


    private void insertTraceLog(MappingResult.BugsSeadMapping<BugsType, SeadType> mappedData){
        tracePersister.saveCurrentEventFor(mappedData.getBugsData());
    }

    private void insertErrorLog(MappingResult.BugsSeadMapping<BugsType, SeadType> dataContainer){
        tracePersister.saveErrorLog(dataContainer.getBugsData(), dataContainer.getSeadData());
    }
}
