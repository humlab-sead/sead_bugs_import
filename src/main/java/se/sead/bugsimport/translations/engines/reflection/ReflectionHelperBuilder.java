package se.sead.bugsimport.translations.engines.reflection;

public class ReflectionHelperBuilder {

    private static ReflectionHelper.FieldExtractor attributeNameFieldExtractor = new AttributeNameFieldExtractor();
    private static BugsColumnAnnotationFieldExtractor bugsColumnAnnotationFieldExtractor = new BugsColumnAnnotationFieldExtractor();

    public static ReflectionHelper build(
            Object targetObject,
            String targetColumn,
            ReflectionHelper.MethodType type
    ){
        if(bugsColumnAnnotationFieldExtractor.canHandleObject(targetObject)){
            return new ReflectionHelper(targetObject, targetColumn, type, bugsColumnAnnotationFieldExtractor);
        } else {
            return new ReflectionHelper(targetObject, targetColumn, type, attributeNameFieldExtractor);
        }
    }
}
