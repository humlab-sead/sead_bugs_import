package se.sead.bugsimport.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.sample.bugsmodel.BugsSample;
import se.sead.bugsimport.sample.converters.SampleTracerHelper;
import se.sead.bugsimport.sample.converters.SampleUpdater;
import se.sead.bugsimport.sample.seadmodel.AlternativeReferenceType;
import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.bugsimport.sample.seadmodel.SampleType;
import se.sead.repositories.AlternativeReferenceTypeRepository;
import se.sead.repositories.SampleTypeRepository;

@Component
public class SampleRowConverter implements BugsTableRowConverter<BugsSample, Sample> {

    @Autowired
    private SampleTracerHelper sampleTracerHelper;
    @Autowired
    private SampleUpdater updater;

    private final SampleType defaultBugsType;
    private final AlternativeReferenceType defaultReferenceType;

    public SampleRowConverter(
            AlternativeReferenceTypeRepository referenceTypeRepository,
            SampleTypeRepository typeRepository){
        defaultBugsType = typeRepository.getUnspecifiedType();
        defaultReferenceType = referenceTypeRepository.findOtherAlternativeSampleName();
    }

    @Override
    public Sample convertForDataRow(BugsSample bugsData) {
        Sample fromLastTrace = sampleTracerHelper.getFromLastTrace(bugsData.getBugsIdentifier());

        if(fromLastTrace == null){
            return create(bugsData);
        } else if(fromLastTrace.isErrorFree()){
            return update(fromLastTrace, bugsData);
        } else {
            return fromLastTrace;
        }
    }

    private Sample create(BugsSample bugsData) {
        Sample sample = new Sample();
        sample.setType(defaultBugsType);
        sample.setReferenceType(defaultReferenceType);
        return update(sample, bugsData);
    }

    private Sample update(Sample original, BugsSample bugsData) {
        updater.update(original, bugsData);
        return original;
    }
}
