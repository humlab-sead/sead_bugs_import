package se.sead.bugsimport.tracing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugs.TraceableBugsData;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.bugsimport.tracing.seadmodel.BugsTraceType;
import se.sead.repositories.BugsErrorRepository;
import se.sead.repositories.BugsTraceRepository;
import se.sead.sead.model.LoggableEntity;

import java.util.List;
import java.util.logging.LogManager;

@Service
public class TracePersister {

    private TraceEventManager traceEventManager;
    private TraceSaver traceSaver;
    private ErrorSaver errorSaver;

    @Autowired
    public TracePersister(
            TraceEventManager traceEventManager,
            BugsTraceRepository traceRepository,
            BugsErrorRepository errorRepository){
        this.traceEventManager = traceEventManager;
        this.traceSaver = new TraceSaver(traceRepository);
        this.errorSaver = new ErrorSaver(errorRepository);
    }

    public void saveCurrentEventFor(final TraceableBugsData bugsData){
        List<BugsTrace> traces = traceEventManager.getTracesAndReset();
        traces.stream()
                .forEach(trace -> {
                    traceSaver.saveTrace(trace, bugsData);
                }
            );

    }

    public void saveErrorLog(final TraceableBugsData bugsData, final List<String> errorMessages){
        errorMessages.stream()
                .forEach(error -> errorSaver.saveError(bugsData, error));

    }

    private static class TraceSaver {

        private BugsTraceRepository traceRepository;

        TraceSaver(BugsTraceRepository traceRepository){
            this.traceRepository = traceRepository;
        }

        void saveTrace(BugsTrace trace, TraceableBugsData bugsData){
            trace.setBugsData(bugsData);
            traceRepository.save(trace);
        }

        private void updateType(BugsTrace trace, boolean entityIsFlagged){
            if(entityIsFlagged && trace.getType() == BugsTraceType.INSERT ){
                trace.setType(BugsTraceType.INSERT_FLAGGED);
            } else if(entityIsFlagged && trace.getType() == BugsTraceType.UPDATE){
                trace.setType(BugsTraceType.UPDATE_FLAGGED);
            }
        }
    }

    private static class ErrorSaver {

        private BugsErrorRepository errorRepository;

        ErrorSaver(BugsErrorRepository errorRepository) {
            this.errorRepository = errorRepository;
        }

        void saveError(TraceableBugsData bugsData, String errorMessage){
            BugsError errorCarrier = createErrorCarrier(bugsData, errorMessage);
            errorRepository.save(errorCarrier);
        }

        private BugsError createErrorCarrier(TraceableBugsData bugsData, String message) {
            BugsError error = new BugsError();
            error.setMessage(message);
            error.setBugsData(bugsData);
            return error;
        }
    }
}
