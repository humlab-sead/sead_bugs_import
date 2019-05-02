package se.sead.bugsimport.translations.engines;

import se.sead.bugs.TraceableBugsData;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.converters.TranslationHelper;
import se.sead.bugsimport.translations.engines.reflection.ReflectionHelper;
import se.sead.bugsimport.translations.engines.reflection.ReflectionHelperBuilder;
import se.sead.bugsimport.translations.engines.reflection.ReflectionTranslationApplicator;
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.repositories.TypeTranslationRepository;

import java.util.List;
import java.util.Objects;

public class TypeTranslationEngine implements BugsValueTranslationService.TranslationEngine {

    private TypeTranslationRepository repository;

    public TypeTranslationEngine(TypeTranslationRepository repository){
        this.repository = repository;
    }

    @Override
    public void translate(TraceableBugsData bugsData) {
        List<TypeTranslation> typeBasedTranslations = getTypeBasedTranslations(bugsData);
        for (TypeTranslation typeTranslation :
                typeBasedTranslations) {
            applyTranslation(bugsData, typeTranslation);
        }
    }

    private List<TypeTranslation> getTypeBasedTranslations(TraceableBugsData bugsData){
        return repository.getByBugsTable(bugsData.bugsTable());
    }

    private void applyTranslation(TraceableBugsData bugsData, TypeTranslation typeTranslation){
        ReflectionTranslationApplicator applicator = new ReflectionTranslationApplicator(new TypeTranslationWrapper(typeTranslation));
        applicator.translate(bugsData);
    }

    private static class TypeTranslationWrapper implements BugsValueTranslationService.TranslationWrapper {

        private TypeTranslation translation;
        TypeTranslationWrapper(TypeTranslation translation){
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
            if(!translation.getBugsTable().equals(sourceObject.bugsTable())){
                return false;
            }
            ReflectionHelper sourceColumnReflectionHelper = getReflectionHelper(sourceObject);
            Object currentSourceColumnValue = sourceColumnReflectionHelper.invokeOnTarget();
            Object convertedValue;
            if(currentSourceColumnValue == null){
                Class targetColumnType = sourceColumnReflectionHelper.getTargetColumnType();
                convertedValue = TranslationHelper.convertToType(targetColumnType, translation.getTriggeringColumnValue());
            } else {
                convertedValue = TranslationHelper.convertToType(currentSourceColumnValue, translation.getTriggeringColumnValue());
            }
            return Objects.equals(convertedValue, currentSourceColumnValue);
        }

        private ReflectionHelper getReflectionHelper(TraceableBugsData sourceObject) {
            return ReflectionHelperBuilder.build(sourceObject, translation.getBugsColumn(), ReflectionHelper.MethodType.GET);
        }
    }
}
