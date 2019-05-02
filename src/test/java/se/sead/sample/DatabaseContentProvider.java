package se.sead.sample;

import se.sead.bugsimport.countsheets.seadmodel.SampleGroup;
import se.sead.bugsimport.sample.seadmodel.AlternativeReferenceType;
import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.bugsimport.sample.seadmodel.SampleDimension;
import se.sead.bugsimport.sample.seadmodel.SampleType;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.model.TestEqualityHelper;
import se.sead.model.TestSample;
import se.sead.model.TestSampleDimension;
import se.sead.repositories.*;
import se.sead.sead.methods.Method;
import se.sead.sead.model.Dimension;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.utils.BigDecimalDefinition;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<Sample> {

    private SampleRepository sampleRepository;
    private final SampleGroup defaultGroup;
    private final SampleType defaultType;
    private final AlternativeReferenceType defaultReferenceType;
    private final Method depthFromDatumMethod;
    private final Dimension upperBoundaryDimension;
    private final Dimension lowerBoundaryDimension;

    public DatabaseContentProvider(
            SampleRepository sampleRepository,
            SampleGroupRepository groupRepository,
            SampleTypeRepository sampleTypeRepository,
            AlternativeReferenceTypeRepository referenceTypeRepository,
            MethodRepository methodRepository,
            DimensionRepository dimensionRepository
            ) {
        this.sampleRepository = sampleRepository;
        defaultGroup = groupRepository.findOne(1);
        defaultType = sampleTypeRepository.findOne(1);
        defaultReferenceType = referenceTypeRepository.findOne(1);
        depthFromDatumMethod = methodRepository.findOne(2);
        upperBoundaryDimension = dimensionRepository.findOne(1);
        lowerBoundaryDimension = dimensionRepository.findOne(2);
    }

    @Override
    public List<Sample> getExpectedData() {

        return Arrays.asList(
                create(1, "Exists w. no dimensions"),
                create(2,
                        "Exists w. dimensions",
                        Arrays.asList(
                                TestSampleDimension.create(1,
                                        upperBoundaryDimension,
                                        depthFromDatumMethod,
                                        BigDecimalDefinition.convertToSeadCode(718d)),
                                TestSampleDimension.create(2,
                                        lowerBoundaryDimension,
                                        depthFromDatumMethod,
                                        BigDecimalDefinition.convertToSeadCode(720d))
                        )
                ),
                create(null,
                        "insert w. dimensions",
                        Arrays.asList(
                                TestSampleDimension.create(null,
                                        upperBoundaryDimension,
                                        depthFromDatumMethod,
                                        BigDecimalDefinition.convertToSeadCode(720d)),
                                TestSampleDimension.create(null,
                                        lowerBoundaryDimension,
                                        depthFromDatumMethod,
                                        BigDecimalDefinition.convertToSeadCode(722d))
                        )),
                create(null,"insert w/o dimensions"),
                create(3,
                    "Update",
                        Arrays.asList(
                                TestSampleDimension.create(3,
                                        upperBoundaryDimension,
                                        depthFromDatumMethod,
                                        BigDecimalDefinition.convertToSeadCode(1d)),
                                TestSampleDimension.create(4,
                                        lowerBoundaryDimension,
                                        depthFromDatumMethod,
                                        BigDecimalDefinition.convertToSeadCode(2d))
                        )
                ),
                create(4, "Update delete dimensions"),
                create(5, "Sead data newer"),
                create(6,
                        "Try to update newer sample dimension data",
                        Arrays.asList(
                                TestSampleDimension.create(7,
                                        upperBoundaryDimension,
                                        depthFromDatumMethod,
                                        BigDecimalDefinition.convertToSeadCode(1d)),
                                TestSampleDimension.create(8,
                                        lowerBoundaryDimension,
                                        depthFromDatumMethod,
                                        BigDecimalDefinition.convertToSeadCode(2d))
                        ))
        );
    }

    private Sample create(Integer sampleId, String sampleName){
        return create(sampleId, sampleName, Collections.EMPTY_LIST);
    }

    private Sample create(Integer sampleId, String sampleName, List<SampleDimension> dimensions){
        return TestSample.create(sampleId, sampleName, defaultGroup, defaultReferenceType, defaultType, dimensions);
    }

    @Override
    public List<Sample> getActualData() {
        return sampleRepository.findAll();
    }

    @Override
    public Comparator<Sample> getSorter() {
        return new SampleComparator();
    }

    @Override
    public TestEqualityHelper<Sample> getEqualityHelper() {
        TestEqualityHelper<Sample> equalityHelper = new TestEqualityHelper<>(true);
        equalityHelper.addMethodInformation(
                new TestEqualityHelper.ClassMethodInformation(SampleDimension.class, "getSample")
        );
        equalityHelper.addMethodInformation(
                new TestEqualityHelper.ClassMethodInformation(SeadSite.class, "getSiteLocations")
        );
        return equalityHelper;
    }

    private static class SampleComparator implements Comparator<Sample> {
        @Override
        public int compare(Sample o1, Sample o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }
}
