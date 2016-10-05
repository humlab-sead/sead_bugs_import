package se.sead.bugsimport.sample.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.sample.bugsmodel.BugsSample;
import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.bugsimport.sample.seadmodel.SampleDimension;
import se.sead.repositories.DimensionRepository;
import se.sead.repositories.MethodRepository;
import se.sead.sead.methods.Method;
import se.sead.sead.model.Dimension;
import se.sead.utils.BigDecimalDefinition;
import se.sead.utils.ErrorCopier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class DimensionUpdater {

    @Autowired
    private SampleDimensionTraceHelper sampleDimensionTraceHelper;

    private final Method depthFromDatumMethod;
    private final Dimension upperDimension;
    private final Dimension lowerDimension;

    @Autowired
    public DimensionUpdater(
            MethodRepository methodRepository,
            DimensionRepository dimensionRepository){
        depthFromDatumMethod = methodRepository.getBugsSampleDimensionMethod();
        upperDimension = dimensionRepository.getUpperDepthFromUnknownReference();
        lowerDimension = dimensionRepository.getLowerDepthFromUnknownReference();
    }

    public boolean update(Sample original, BugsSample bugsData){
        List<SampleDimension> fromSampleTrace = sampleDimensionTraceHelper.getFromSampleTrace(bugsData.getSampleCode());
        if(fromSampleTrace.isEmpty()){
            fromSampleTrace = new ArrayList<>();
        }
        Updater updater = new Updater(fromSampleTrace, bugsData);
        boolean updated = updater.update();
        copyErrors(original, fromSampleTrace);
        reassociateSampleAndDimensions(original, fromSampleTrace);
        return updated;
    }

    private void copyErrors(Sample original, List<SampleDimension> fromSampleTrace) {
        fromSampleTrace.forEach(sampleDimension -> ErrorCopier.copyPotentialErrors(original, sampleDimension));
    }

    private void reassociateSampleAndDimensions(Sample original, List<SampleDimension> dimensions){
        dimensions.forEach(dimension -> dimension.setSample(original));
        original.setDimensions(dimensions);
    }

    private class Updater {

        private List<SampleDimension> originals;
        private BugsSample bugsData;
        private boolean updated;

        Updater(List<SampleDimension> originals, BugsSample bugsData){
            this.originals = originals;
            this.bugsData = bugsData;
        }

        boolean update(){
            updated = setZorDepthToTopValue();
            updated = setZOrDepthToBotValue() || updated;
            return updated;
        }

        private boolean setZorDepthToTopValue(){
            Optional<SampleDimension> originalUpperDimension = findBoundDimension(upperDimension);
            ValueSetter valueSetter = new ValueSetter(originals, originalUpperDimension, bugsData.getzOrDepthTop(), upperDimension);
            return valueSetter.update();
        }

        private Optional<SampleDimension> findBoundDimension(Dimension dimension){
            Optional<SampleDimension> firstUpperBoundDimension = originals.stream()
                    .filter(
                            sampleDimension ->
                                    matchMethod(sampleDimension) && matchDimension(sampleDimension, dimension)
                    )
                    .findFirst();
            return firstUpperBoundDimension;
        }

        private boolean matchMethod(SampleDimension dimension){
            return Objects.equals(depthFromDatumMethod, dimension.getMethod());
        }

        private boolean matchDimension(SampleDimension dimension, Dimension type){
            return Objects.equals(type, dimension.getDimension());
        }

        private boolean setZOrDepthToBotValue(){
            Optional<SampleDimension> originalLowerDimension = findBoundDimension(lowerDimension);
            ValueSetter valueSetter = new ValueSetter(originals, originalLowerDimension, bugsData.getzOrDepthBot(), lowerDimension);
            return valueSetter.update();
        }
    }

    private class ValueSetter {
        private List<SampleDimension> originalDimensions;
        private Double value;
        private Optional<SampleDimension> originalOptional;
        private SampleDimension original;
        private Dimension targetDimension;

        ValueSetter(
                List<SampleDimension> allOriginalDimensions,
                Optional<SampleDimension> currentOriginal,
                Double value,
                Dimension targetDimension){
            this.originalDimensions = allOriginalDimensions;
            originalOptional = currentOriginal;
            this.value = value;
            this.targetDimension = targetDimension;
        }

        boolean update(){
            if(originalOptional.isPresent()){
                original = originalOptional.get();
            } else if(value == null){
                return false;
            } else {
                original = createSampleDimension();
            }
            BigDecimal originalValue = original.getValue();
            original.setValue(BigDecimalDefinition.convertToSeadCode(value));
            if(originalValue != null && value == null){
                original.markForDeletion();
                return true;
            } else {
                return !BigDecimalDefinition.equalBigDecimalNumericValues(originalValue, original.getValue());
            }
        }

        private SampleDimension createSampleDimension(){
            SampleDimension newDimension = new SampleDimension();
            newDimension.setDimension(targetDimension);
            newDimension.setMethod(depthFromDatumMethod);
            originalDimensions.add(newDimension);
            return newDimension;
        }
    }
}
