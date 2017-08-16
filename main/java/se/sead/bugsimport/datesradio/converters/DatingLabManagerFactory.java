package se.sead.bugsimport.datesradio.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.lab.seadmodel.DatingLab;
import se.sead.bugsimport.lab.search.DatingLabTraceHelper;
import se.sead.repositories.DatingLabRepository;

@Component
public class DatingLabManagerFactory {

    private DatingLabRepository repository;
    private DatingLabTraceHelper datingLabTraceHelper;
    private String unknownBugsLabIdentifier;
    private String unknownDatingLabSeadIdentifier;

    private DatingLab unknownSeadLab;
    private DatingLab noMatchingSeadLabFound;

    @Autowired
    public DatingLabManagerFactory(
            DatingLabRepository datingLabRepository,
            DatingLabTraceHelper datingLabTraceHelper,
            @Value("${sead.unknown.lab.identifier:Unknown}")
            String unknownDatingLabSeadIdentifier,
            @Value("${bugs.unknown.lab.identifier:Unknown}")
            String unknownDatingLabBugsIdentifier
    ){
        this.repository = datingLabRepository;
        this.datingLabTraceHelper = datingLabTraceHelper;
        this.unknownBugsLabIdentifier = unknownDatingLabBugsIdentifier;
        this.unknownDatingLabSeadIdentifier = unknownDatingLabSeadIdentifier;
        createNoMatchingSeadLabFound();
    }

    private void createNoMatchingSeadLabFound(){
        noMatchingSeadLabFound = new DatingLab();
        noMatchingSeadLabFound.addError("No lab found");
    }

    DatingLabManager createManager(){
        return new DatingLabManager(
                datingLabTraceHelper,
                getUnknownSeadLab(),
                noMatchingSeadLabFound,
                unknownBugsLabIdentifier);
    }

    private DatingLab getUnknownSeadLab(){
        if(unknownSeadLab == null){
            unknownSeadLab = repository.findByLabId(unknownDatingLabSeadIdentifier);
            unknownSeadLab.setFlagged(true);
        }
        return unknownSeadLab;
    }

    static class DatingLabManager {
        private DatingLabTraceHelper traceHelper;
        private DatingLab seadUnknownLab;
        private String unknownBugsLabIdentifier;
        private DatingLab noLabFoundErrorContainer;

        public DatingLabManager(
                DatingLabTraceHelper traceHelper,
                DatingLab seadUnknownLab,
                DatingLab noLabFoundErrorContainer,
                String unknownBugsLabIdentifier) {
            this.traceHelper = traceHelper;
            this.seadUnknownLab = seadUnknownLab;
            this.unknownBugsLabIdentifier = unknownBugsLabIdentifier;
            this.noLabFoundErrorContainer = noLabFoundErrorContainer;
        }

        DatingLab getSeadLabFromCode(String bugsLabIdentifier){
            if(isEmpty(bugsLabIdentifier) || unknownBugsLabIdentifier.equals(bugsLabIdentifier)){
                return seadUnknownLab;
            }
            DatingLab fromLastTrace = traceHelper.getFromLastTrace(bugsLabIdentifier);
            if(fromLastTrace == null){
                return noLabFoundErrorContainer;
            }
            return fromLastTrace;
        }

        private static boolean isEmpty(String identifier){
            return identifier == null ||
                    identifier.trim().isEmpty();
        }
    }
}
