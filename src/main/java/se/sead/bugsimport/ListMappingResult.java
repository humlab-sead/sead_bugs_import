package se.sead.bugsimport;

import se.sead.bugs.TraceableBugsData;
import se.sead.sead.model.LoggableEntity;

import java.util.ArrayList;
import java.util.List;

public class ListMappingResult<BugsType extends TraceableBugsData, SeadType extends LoggableEntity> implements MappingResult<BugsType, SeadType> {

    private List<BugsListSeadMapping<BugsType, SeadType>> data;

    public ListMappingResult(){
        data = new ArrayList<>();
    }

    @Override
    public void add(BugsType bugsData, List<SeadType> seadItems){
        data.add(new BugsListSeadMapping<>(bugsData, seadItems));
    }

    protected void add(BugsListSeadMapping<BugsType, SeadType> mapping){
        data.add(mapping);
    }

    @Override
    public List<BugsListSeadMapping<BugsType, SeadType>> getData(){ return data;}

}
