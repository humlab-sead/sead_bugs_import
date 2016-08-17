package se.sead.testutils;

import se.sead.bugs.TraceableBugsData;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.repositories.BugsErrorRepository;
import se.sead.repositories.BugsTraceRepository;

import java.util.List;

public class BugsTracesAndErrorsVerification<BugsType extends TraceableBugsData> {

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
    private BugsTraceRepository traceRepository;
    private BugsErrorRepository errorRepository;

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
            List<BugsTrace> traces = traceRepository.findByBugsTableAndCompressedBugsData(bugsData.bugsTable(), bugsData.compressToString());
            List<BugsError> errors = errorRepository.findByBugsTableAndCompressedBugsData(bugsData.bugsTable(), bugsData.compressToString());
            logVerificationHandler.verifyLogData(bugsData, traces, errors);
        }

    }
}
