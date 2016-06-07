package se.sead.bugsimport.tracing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugs.TraceableBugsData;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.repositories.BugsErrorRepository;
import se.sead.repositories.BugsTraceRepository;
import se.sead.sead.model.LoggableEntity;

import java.util.List;

@Service
public class TracePersister {

    @Autowired
    private TraceEventManager traceEventManager;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    public void saveCurrentEventFor(final TraceableBugsData bugsData){
        List<BugsTrace> traces = traceEventManager.getTracesAndReset();
        traces.stream()
                .forEach(trace -> {
                    trace.setBugsData(bugsData);
                    traceRepository.save(trace);
                }
                );

    }

    public void saveErrorLog(final TraceableBugsData bugsData, final LoggableEntity messageHolder){
        for (String message :
                messageHolder.getErrors()) {
            BugsError errorCarrier = createErrorCarrier(bugsData, message);
            errorRepository.save(errorCarrier);
        }
    }

    private BugsError createErrorCarrier(TraceableBugsData bugsData, String message) {
        BugsError error = new BugsError();
        error.setMessage(message);
        error.setBugsData(bugsData);
        return error;
    }

}
