package se.sead.bugsimport;

import org.springframework.beans.factory.annotation.Autowired;
import se.sead.bugs.AccessReader;
import se.sead.bugs.AccessReaderProvider;
import se.sead.bugs.BugsTable;
import se.sead.bugs.TraceableBugsData;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.sead.model.LoggableEntity;

import java.util.List;

public abstract class BugsSeadMapper<BugsType extends TraceableBugsData, SeadType extends LoggableEntity> {

    private BugsTable<BugsType> bugsTable;
    private BugsTableRowConverter<BugsType, SeadType> singleBugsTableRowConverterForMapper;
    private MappingResult<BugsType, SeadType> mapperResult;

    @Autowired
    private BugsValueTranslationService dataTranslationService;
    @Autowired
    private AccessReaderProvider accessReaderProvider;

    public BugsSeadMapper(
            BugsTable<BugsType> bugsTable,
            BugsTableRowConverter<BugsType, SeadType> singleBugsTableRowConverterForMapper) {
        this.bugsTable = bugsTable;
        this.singleBugsTableRowConverterForMapper = singleBugsTableRowConverterForMapper;
        mapperResult = new MappingResult<>();
    }

    public void importBugsData(){
        List<BugsType> readItems = getAccessReader().read(bugsTable);
        for (BugsType readItem :
                readItems) {
            mapResult(readItem);
        }
    }

    private AccessReader getAccessReader() {
        return accessReaderProvider.getReader();
    }

    protected void mapResult(BugsType readItem){
        mapperResult.add(readItem, getConvertedValue(readItem));
    }

    private List<SeadType> getConvertedValue(BugsType readItem) {
        dataTranslationService.translateValues(readItem);
        return singleBugsTableRowConverterForMapper.convertListForDataRow(readItem);
    }

    public MappingResult<BugsType, SeadType> getMapperResult(){
        return mapperResult;
    }
}
