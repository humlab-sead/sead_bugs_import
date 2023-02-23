package se.sead.bugsimport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import se.sead.bugs.*;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.sead.model.LoggableEntity;

import java.util.List;

public abstract class BugsSeadMapper<BugsType extends TraceableBugsData, SeadType extends LoggableEntity> {

    private static final Logger logger = LoggerFactory.getLogger(BugsSeadMapper.class);

    private BugsTable<BugsType> bugsTable;
    private BugsTableRowConverter<BugsType, SeadType> singleBugsTableRowConverterForMapper;

    @Autowired
    private BugsValueTranslationService dataTranslationService;
    @Autowired
    private AccessDatabaseProvider accessDatabaseProvider;

    public BugsSeadMapper(
            BugsTable<BugsType> bugsTable,
            BugsTableRowConverter<BugsType, SeadType> singleBugsTableRowConverterForMapper) {
        this.bugsTable = bugsTable;
        this.singleBugsTableRowConverterForMapper = singleBugsTableRowConverterForMapper;
    }

    public MappingResult<BugsType, SeadType> importBugsData() {

        MappingResult<BugsType, SeadType> resultContainer = initMapperResultContainer();
        List<BugsType> readItems = readItems();
        for (BugsType readItem : readItems) {
            if (logger.isDebugEnabled()) {
                logger.debug("mapping row item: {}", readItem);
            }
            try {
                resultContainer.add(readItem, getConvertedValue(readItem));
            } catch (Throwable t) {
                // logger.error("THROW DISABLED BY ROGER: Throwing error for : {}", readItem);
                throw t;
            }
        }
        return resultContainer;
    }

    protected List<BugsType> readItems() {
        AccessReader reader = new AccessReader(getAccessReader());
        return reader.read(bugsTable);
    }

    protected MappingResult<BugsType, SeadType> initMapperResultContainer() {
        return new ListMappingResult<>();
    }

    private AccessDatabase getAccessReader() {
        return accessDatabaseProvider.getDatabase();
    }

    private List<SeadType> getConvertedValue(BugsType readItem) {
        BugsType unalteredVersion = readItem;
        try {
            dataTranslationService.translateValues(readItem);
            return singleBugsTableRowConverterForMapper.convertListForDataRow(readItem);
        } catch (DataAccessException dae) {
            if (logger.isErrorEnabled()) {
                logger.error("data access error during convert for object {} - {}", getClass().getSimpleName(),
                        unalteredVersion);
            }
            throw dae;
        }
    }
}
