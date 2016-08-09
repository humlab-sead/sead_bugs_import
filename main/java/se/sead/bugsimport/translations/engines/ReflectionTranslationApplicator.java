package se.sead.bugsimport.translations.engines;

import se.sead.bugs.TraceableBugsData;
import se.sead.bugsimport.translations.BugsValueTranslationService;
import se.sead.bugsimport.translations.converters.TranslationHelper;

public class ReflectionTranslationApplicator {

    protected BugsValueTranslationService.TranslationWrapper translation;

    public ReflectionTranslationApplicator(BugsValueTranslationService.TranslationWrapper translation){
        this.translation = translation;
    }

    public void translate(TraceableBugsData targetObject) {
        if(translation.shouldDoTranslation(targetObject)){
            doTranslate(targetObject);
        }
    }

    private void doTranslate(Object targetObject) {
        ReflectionHelper reflectionHelper = new ReflectionHelper(targetObject, translation.getTargetColumn(), ReflectionHelper.MethodType.SET);
        reflectionHelper.invokeOnTarget(getCorrectedValue(reflectionHelper.getTargetColumnType()));
    }

    protected Object getCorrectedValue(Class targetType){
        return  TranslationHelper.convertToType(targetType, translation.getTargetColumnValue());
    }

}
