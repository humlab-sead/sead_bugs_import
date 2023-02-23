package se.sead.bugsimport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.sead.bugs.TraceableBugsData;
import se.sead.sead.model.LoggableEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Importer<BugsType extends TraceableBugsData, SeadType extends LoggableEntity> {

    private static final Logger logger = LoggerFactory.getLogger(Importer.class);

    private BugsSeadMapper<BugsType, SeadType> dataMapper;
    private Persister<BugsType, SeadType> persister;
    private List<Importer<? extends TraceableBugsData, ? extends LoggableEntity>> requiredImporters;
    private boolean hasRun = false;

    protected Importer(BugsSeadMapper<BugsType, SeadType> dataMapper, Persister<BugsType, SeadType> persister) {
        this.dataMapper = dataMapper;
        this.persister = persister;
        requiredImporters = Collections.EMPTY_LIST;
    }

    protected Importer(
            BugsSeadMapper<BugsType, SeadType> dataMapper,
            Persister<BugsType, SeadType> persister,
            Importer<? extends TraceableBugsData, ? extends LoggableEntity>... requiredImporters) {
        this.dataMapper = dataMapper;
        this.persister = persister;
        setRequiredImporters(requiredImporters);
    }

    private void setRequiredImporters(
            Importer<? extends TraceableBugsData, ? extends LoggableEntity>... requiredImporters) {
        if (requiredImporters != null && requiredImporters.length > 0 && requiredImporters[0] != null) {
            this.requiredImporters = Arrays.asList(requiredImporters);
        } else {
            this.requiredImporters = Collections.EMPTY_LIST;
        }
    }

    public void run() {
        run(0);
    }

    public void run(int indent) {
        String pad = indent < 1 ? "" : String.format("%" + indent + "c", ' ');
        if (hasRun) {
            return;
        }
        if (logger.isInfoEnabled()) {
            logger.info("{}importer {}.run() mapper: {}, persister: {}", pad, getClass().getSimpleName(), dataMapper.getClass().getSimpleName(), persister.getClass().getSimpleName());
        }
        runRequiredImporters(indent);
        MappingResult<BugsType, SeadType> mappedData = mapData();
        saveData(mappedData);

        if (logger.isInfoEnabled()) {
            logger.info("{}importer {}: done", pad, getClass().getSimpleName());
        }
        hasRun = true;
    }

    private void runRequiredImporters() {
        runRequiredImporters(0);
    }

    private void runRequiredImporters(int indent) {
        for (Importer<? extends TraceableBugsData, ? extends LoggableEntity> importer : requiredImporters) {
            if (logger.isDebugEnabled()) {
                logger.info("importer {}: starting required importer {}", getClass().getSimpleName(),
                        importer.getClass().getSimpleName());
            }
            importer.run(indent + 4);
        }
    }

    private MappingResult<BugsType, SeadType> mapData() {
        return dataMapper.importBugsData();
    }

    private void saveData(MappingResult<BugsType, SeadType> mappedData) {
        persister.persist(mappedData);
    }
}
