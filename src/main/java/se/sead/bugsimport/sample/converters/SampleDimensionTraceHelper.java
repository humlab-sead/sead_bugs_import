package se.sead.bugsimport.sample.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.sample.bugsmodel.SampleBugsTable;
import se.sead.bugsimport.sample.seadmodel.SampleDimension;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.repositories.BugsTraceRepository;
import se.sead.repositories.SampleDimensionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SampleDimensionTraceHelper {

    public static final String TBL_SAMPLE_DIMENSIONS_TABLE_NAME = "tbl_sample_dimensions";

    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private SampleDimensionRepository sampleDimensionRepository;

    public List<SampleDimension> getFromSampleTrace(String bugsSampleCode){
        List<BugsTrace> existingTraces = getExistingTraces(bugsSampleCode);
        return getExistingDimensions(existingTraces);
    }

    private List<BugsTrace> getExistingTraces(String bugsSampleCode){
        List<BugsTrace> allTracesForSampleCode = traceRepository.findByBugsTableAndSeadTableAndBugsIdentifierOrderByChangeDate(SampleBugsTable.TABLE_NAME, "tbl_sample_dimensions", bugsSampleCode);
        List<BugsTrace> sampleDimensionTraces =
                allTracesForSampleCode.stream()
                        .filter(
                                trace -> trace.getSeadTable()
                                        .equals(TBL_SAMPLE_DIMENSIONS_TABLE_NAME)
                        ).collect(Collectors.toList());
        return sampleDimensionTraces;
    }

    private List<SampleDimension> getExistingDimensions(List<BugsTrace> existingTraces){
        SampleDimensionContainer dimensionContainer = new SampleDimensionContainer();
        for (BugsTrace trace :
                existingTraces) {
            SampleDimension dimension = sampleDimensionRepository.findOne(trace.getSeadId());
            dimensionContainer.add(dimension);
            if(seadDataNewerThanTrace(trace, dimension)){
                dimension.addError(SeadDataFromTraceHelper.SEAD_DATA_HAS_BEEN_UPDATED_SINCE_LAST_BUGS_IMPORT);
            }
        }
        return dimensionContainer.getStoredDimensions();
    }

    private boolean seadDataNewerThanTrace(BugsTrace trace, SampleDimension dimension){
        return SeadDataFromTraceHelper.seadDataExistsAndHasBeenEditedSinceImportByDate(dimension, trace);
    }

    private static class SampleDimensionContainer {
        private List<SampleDimension> storedDimensions;

        SampleDimensionContainer(){
            storedDimensions = new ArrayList<>();
        }

        void add(SampleDimension dimensionToAdd){
            if(dimensionToAdd == null){
                return;
            }
            if(alreadyStored(dimensionToAdd)){
                return;
            }
            storedDimensions.add(dimensionToAdd);
        }

        private boolean alreadyStored(SampleDimension dimensionToAdd){
            return
                    storedDimensions.stream()
                            .anyMatch(
                                    storedDimension -> storedDimension.getId() == dimensionToAdd.getId()
                            );
        }

        List<SampleDimension> getStoredDimensions(){
            return storedDimensions;
        }
    }
}
