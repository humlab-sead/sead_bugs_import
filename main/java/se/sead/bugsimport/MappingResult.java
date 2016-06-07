package se.sead.bugsimport;

import se.sead.bugs.TraceableBugsData;
import se.sead.sead.model.LoggableEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erer0001 on 2016-04-27.
 */
public class MappingResult<BugsType extends TraceableBugsData, SeadType extends LoggableEntity> {

    private List<BugsSeadMapping<BugsType, SeadType>> dataTuples;

    public MappingResult(){
        dataTuples = new ArrayList<>();
    }

    public List<BugsSeadMapping<BugsType, SeadType>> getTupleData(){return dataTuples;}

    public void add(BugsType bugsData, SeadType seadItem){
        dataTuples.add(new BugsSeadMapping<>(bugsData, seadItem));
    }

    public static class BugsSeadMapping<BugsType, SeadType extends LoggableEntity> {
        private BugsType bugsData;
        private SeadType seadData;

        BugsSeadMapping(BugsType bugsData, SeadType seadData){
            this.bugsData = bugsData;
            this.seadData = seadData;
        }

        public boolean isNewSeadData(){
            return seadData.isNewItem();
        }

        public BugsType getBugsData() {
            return bugsData;
        }

        public SeadType getSeadData() {
            return seadData;
        }

        public void setSavedSeadData(SeadType newSeadData){
            this.seadData = seadData;
        }

        public boolean canAndShouldSave(){
            return seadData.isErrorFree() && seadData.isNewItem();
        }
    }
}
