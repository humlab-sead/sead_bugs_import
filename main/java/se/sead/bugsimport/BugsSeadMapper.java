package se.sead.bugsimport;

import org.springframework.beans.factory.annotation.Autowired;
import se.sead.bugs.AccessReader;
import se.sead.bugs.BugsTable;
import se.sead.bugs.TraceableBugsData;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.sead.model.LoggableEntity;

import java.util.List;

public abstract class BugsSeadMapper<BugsType extends TraceableBugsData, SeadType extends LoggableEntity> {

    private AccessReader accessReader;
    private BugsTable<BugsType> bugsTable;
    private BugsTableRowConverter<BugsType, SeadType> singleBugsTableRowConverterForMapper;
    private MappingResult<BugsType, SeadType> mapperResult;
    private BugsValueTranslationService dataTranslationService;

    public BugsSeadMapper(
            AccessReader accessReader,
            BugsTable<BugsType> bugsTable,
            BugsTableRowConverter<BugsType, SeadType> singleBugsTableRowConverterForMapper,
            BugsValueTranslationService dataTranslationService) {
        this.accessReader = accessReader;
        this.bugsTable = bugsTable;
        this.singleBugsTableRowConverterForMapper = singleBugsTableRowConverterForMapper;
        this.dataTranslationService = dataTranslationService;
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
        mapperResult.add(readItem, getConvertedValue(readItem));
    }

    private SeadType getConvertedValue(BugsType readItem) {
        dataTranslationService.translateValues(readItem);
        return singleBugsTableRowConverterForMapper.convertForDataRow(readItem);
    }

    public MappingResult<BugsType, SeadType> getMapperResult(){
        return mapperResult;
    }
}
