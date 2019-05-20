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
                repository,
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
        private DatingLabRepository repository;
        public DatingLabManager(
                DatingLabRepository repository,
                DatingLabTraceHelper traceHelper,
                DatingLab seadUnknownLab,
                DatingLab noLabFoundErrorContainer,
                String unknownBugsLabIdentifier) {
            this.repository = repository;
            this.traceHelper = traceHelper;
            this.seadUnknownLab = seadUnknownLab;
            this.unknownBugsLabIdentifier = unknownBugsLabIdentifier;
            this.noLabFoundErrorContainer = noLabFoundErrorContainer;
        }

        DatingLab getSeadLabFromCode(String bugsLabIdentifier){
            if(isEmpty(bugsLabIdentifier) || unknownBugsLabIdentifier.equals(bugsLabIdentifier)){
                return seadUnknownLab;
            }
            DatingLab datingLab = traceHelper.getFromLastTrace(bugsLabIdentifier);
            if(datingLab == null){
                datingLab = repository.findByLabId(bugsLabIdentifier);
            }
            if(datingLab == null){
                return noLabFoundErrorContainer;
            }
            return datingLab;
        }

        private static boolean isEmpty(String identifier){
            return identifier == null ||
                    identifier.trim().isEmpty();
        }
    }
}
