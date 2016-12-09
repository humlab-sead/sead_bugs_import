package se.sead.bugsimport.translations.engines.reflection;

import se.sead.bugs.BugsColumn;

import java.lang.reflect.Field;

public class BugsColumnAnnotationFieldExtractor implements ReflectionHelper.FieldExtractor{
    @Override
    public Field getField(Class sourceClass, String fieldName) {
        for (Field field :
                getAllFields(sourceClass)) {
            BugsColumn annotation = field.getAnnotation(BugsColumn.class);
            if(isCorrectAnnotation(annotation, fieldName)){
                return field;
            }
        }
        throw new NoFieldWithNameException("no field named " +
                fieldName + " for type " +
                sourceClass);
    }

    private Field[] getAllFields(Class targetObjectClass) {
        return targetObjectClass.getDeclaredFields();
    }

    private boolean isCorrectAnnotation(BugsColumn annotation, String fieldName) {
        if(annotation == null){
            return false;
        }
        return annotation.value().equals(fieldName);
    }

    boolean canHandleObject(Object targetObject){
        for (Field field :
                getAllFields(targetObject.getClass())) {
            BugsColumn annotation = field.getAnnotation(BugsColumn.class);
            if(annotation != null){
                return true;
            }
        }
        return false;
    }
}
