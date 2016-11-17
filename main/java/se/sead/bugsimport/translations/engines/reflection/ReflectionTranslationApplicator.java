package se.sead.bugsimport.translations.engines.reflection;

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
        ReflectionHelper reflectionHelper = getReflectionHelper(targetObject);
        reflectionHelper.invokeOnTarget(getCorrectedValue(reflectionHelper.getTargetColumnType()));
    }

    private ReflectionHelper getReflectionHelper(Object targetObject) {
        return ReflectionHelperBuilder.build(targetObject, translation.getTargetColumn(), ReflectionHelper.MethodType.SET);
    }

    protected Object getCorrectedValue(Class targetType){
        return  TranslationHelper.convertToType(targetType, translation.getTargetColumnValue());
    }

}
