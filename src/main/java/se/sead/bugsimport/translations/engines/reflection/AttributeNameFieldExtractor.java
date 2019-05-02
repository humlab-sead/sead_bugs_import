package se.sead.bugsimport.translations.engines.reflection;

import java.lang.reflect.Field;

public class AttributeNameFieldExtractor implements ReflectionHelper.FieldExtractor {
    @Override
    public Field getField(Class sourceClass, String fieldName) {
        try {
            return sourceClass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("no field named " +
                    fieldName + " for type " +
                    sourceClass);
        }
    }
}
