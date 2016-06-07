package se.sead.bugsimport;

import se.sead.bugs.AccessReader;
import se.sead.bugs.BugsTable;
import se.sead.bugs.TraceableBugsData;
import se.sead.sead.model.LoggableEntity;

import java.util.List;

public abstract class BugsSeadMapper<BugsType extends TraceableBugsData, SeadType extends LoggableEntity> {

    private AccessReader accessReader;
    private BugsTable<BugsType> bugsTable;
    private BugsTableRowConverter<BugsType, SeadType> singleBugsTableRowConverterForMapper;
    private MappingResult<BugsType, SeadType> mapperResult;

    public BugsSeadMapper(AccessReader accessReader, BugsTable<BugsType> bugsTable, BugsTableRowConverter<BugsType, SeadType> singleBugsTableRowConverterForMapper) {
        this.accessReader = accessReader;
        this.bugsTable = bugsTable;
        this.singleBugsTableRowConverterForMapper = singleBugsTableRowConverterForMapper;
        mapperResult = new MappingResult<>();
    }

    public void importBugsData(){
        List<BugsType> readItems = accessReader.read(bugsTable);
        for (BugsType readItem :
                readItems) {
            mapResult(readItem);
        }
    }

    protected void mapResult(BugsType readItem){
        mapperResult.add(readItem, singleBugsTableRowConverterForMapper.convertForDataRow(readItem));
    }

    public MappingResult<BugsType, SeadType> getMapperResult(){
        return mapperResult;
    }
}
