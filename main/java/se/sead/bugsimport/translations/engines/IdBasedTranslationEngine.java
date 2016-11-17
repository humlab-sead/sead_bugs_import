package se.sead.bugsimport.translations.engines;

import se.sead.bugs.TraceableBugsData;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.engines.reflection.ReflectionTranslationApplicator;
import se.sead.bugsimport.translations.model.IdBasedTranslation;
import se.sead.repositories.IdBasedTranslationRepository;

import java.util.List;

public class IdBasedTranslationEngine implements BugsValueTranslationService.TranslationEngine {

    private IdBasedTranslationRepository repository;

    public IdBasedTranslationEngine(IdBasedTranslationRepository repository){
        this.repository = repository;
    }

    @Override
    public void translate(TraceableBugsData bugsData) {
        List<IdBasedTranslation> idBasedTranslations = getIdBasedTranslations(bugsData);
        for (IdBasedTranslation translation :
                idBasedTranslations) {
            applyTranslation(bugsData, translation);
        }
    }

    private List<IdBasedTranslation> getIdBasedTranslations(TraceableBugsData bugsData){
        final String identifier = bugsData.getBugsIdentifier();
        if(identifier == null){
            return repository.getAllByBugsTableAndBugsDefinition(bugsData.bugsTable(), bugsData.getCompressedStringBeforeTranslation());
        } else {
            return repository.getAllByBugsTableAndBugsDefinition(bugsData.bugsTable(), identifier);
        }

    }

    private void applyTranslation(TraceableBugsData bugsData, IdBasedTranslation translation) {
        ReflectionTranslationApplicator applicator = new ReflectionTranslationApplicator(new IdBasedTranslationWrapper(translation));
        applicator.translate(bugsData);
    }

    private static class IdBasedTranslationWrapper implements BugsValueTranslationService.TranslationWrapper {
        private IdBasedTranslation translation;
        IdBasedTranslationWrapper(IdBasedTranslation translation){
            this.translation = translation;
        }

        @Override
        public String getTargetColumn() {
            return translation.getTargetColumn();
        }

        @Override
        public String getTargetColumnValue() {
            return translation.getReplacementValue();
        }

        @Override
        public boolean shouldDoTranslation(TraceableBugsData sourceObject) {
            return true;
        }
    }
}
