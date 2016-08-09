package se.sead.bugsimport.translations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.bugs.TraceableBugsData;
import se.sead.bugsimport.translations.engines.IdBasedTranslationEngine;
import se.sead.bugsimport.translations.engines.TypeTranslationEngine;
import se.sead.repositories.IdBasedTranslationRepository;
import se.sead.repositories.TypeTranslationRepository;

import java.util.Arrays;
import java.util.List;

@Service
public class BugsValueTranslationService {

    public interface TranslationWrapper {
        String getTargetColumn();
        String getTargetColumnValue();
        boolean shouldDoTranslation(TraceableBugsData sourceObject);
    }

    public interface TranslationEngine {
        void translate(TraceableBugsData bugsData);
    }

    private List<TranslationEngine> translationEngines;

    @Autowired
    public BugsValueTranslationService(IdBasedTranslationRepository idBasedTranslationRepository,
                                       TypeTranslationRepository typeTranslationRepository){
        translationEngines = Arrays.asList(
                new IdBasedTranslationEngine(idBasedTranslationRepository),
                new TypeTranslationEngine(typeTranslationRepository)
        );
    }

    public void translateValues(TraceableBugsData bugsData){
        bugsData.setCompressedStringBeforeTranslation(bugsData.compressToString());
        translationEngines.forEach(engine -> engine.translate(bugsData));
    }
}
