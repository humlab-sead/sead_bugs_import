package se.sead.testutils;

import org.aspectj.weaver.tools.Trace;
import se.sead.bugs.TraceableBugsData;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.repositories.BugsErrorRepository;
import se.sead.repositories.BugsTraceRepository;

import java.util.List;

public abstract class BugsTracesAndErrorsVerification<BugsType extends TraceableBugsData> {

    @FunctionalInterface
    public interface LogVerificationCallback<BugsType> {
        void verifyLogData(BugsType bugsData, List<BugsTrace> traces, List<BugsError> errors);
    }
    @FunctionalInterface
    public interface ExpectedImportBugsDataProvider<BugsType extends TraceableBugsData> {
        List<BugsType> getExpectedData();
    }

    private LogVerificationCallback<BugsType> logVerificationHandler;
    private ExpectedImportBugsDataProvider<BugsType> expectedBugsData;
    protected BugsTraceRepository traceRepository;
    protected BugsErrorRepository errorRepository;

    public BugsTracesAndErrorsVerification(
            BugsTraceRepository traceRepository,
            BugsErrorRepository errorRepository,
            LogVerificationCallback<BugsType> logVerificationHandler,
            ExpectedImportBugsDataProvider<BugsType> expectedBugsData) {
        this.traceRepository = traceRepository;
        this.errorRepository = errorRepository;
        this.logVerificationHandler = logVerificationHandler;
        this.expectedBugsData = expectedBugsData;
    }

    public void verifyTraceContent(){
        for (BugsType bugsData : expectedBugsData.getExpectedData()) {
            List<BugsTrace> traces = getTraces(bugsData);
            List<BugsError> errors = getErrors(bugsData);
            try {
                logVerificationHandler.verifyLogData(bugsData, traces, errors);
            } catch (AssertionError ae){
                System.out.println(bugsData);
                throw ae;
            }
        }
    }

    protected abstract List<BugsTrace> getTraces(BugsType bugsData);
    protected abstract List<BugsError> getErrors(BugsType bugsData);

    public static class ByIdentity<BugsType extends TraceableBugsData> extends BugsTracesAndErrorsVerification<BugsType>  {

        public ByIdentity(
                BugsTraceRepository traceRepository,
                BugsErrorRepository errorRepository,
                LogVerificationCallback<BugsType> logVerificationHandler,
                ExpectedImportBugsDataProvider<BugsType> expectedBugsData) {
            super(traceRepository, errorRepository, logVerificationHandler, expectedBugsData);
        }

        @Override
        protected List<BugsTrace> getTraces(BugsType bugsData) {
            return traceRepository.findByBugsTableAndBugsIdentifierOrderByChangeDate(bugsData.bugsTable(), bugsData.getBugsIdentifier());
        }

        @Override
        protected List<BugsError> getErrors(BugsType bugsData) {
            return errorRepository.findByBugsTableAndBugsIdentifier(bugsData.bugsTable(), bugsData.getBugsIdentifier());
        }
    }

    public static class ByCompressed<BugsType extends TraceableBugsData> extends BugsTracesAndErrorsVerification<BugsType> {

        public ByCompressed(
                BugsTraceRepository traceRepository,
                BugsErrorRepository errorRepository,
                LogVerificationCallback<BugsType> logVerificationHandler,
                ExpectedImportBugsDataProvider<BugsType> expectedBugsData) {
            super(traceRepository, errorRepository, logVerificationHandler, expectedBugsData);
        }

        @Override
        protected List<BugsTrace> getTraces(BugsType bugsData) {
            return traceRepository.findByBugsTableAndCompressedBugsData(bugsData.bugsTable(), bugsData.getCompressedStringBeforeTranslation());
        }

        @Override
        protected List<BugsError> getErrors(BugsType bugsData) {
            return errorRepository.findByBugsTableAndCompressedBugsData(bugsData.bugsTable(), bugsData.getCompressedStringBeforeTranslation());
        }
    }
}
