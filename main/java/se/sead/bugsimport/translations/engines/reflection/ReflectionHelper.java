package se.sead.bugsimport.translations.engines.reflection;

import se.sead.bugsimport.translations.converters.TranslationHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionHelper {

    public enum MethodType {
          SET("set")
        , GET("get")
        ;
        private String methodPrefix;
        MethodType(String methodPrefix){
            this.methodPrefix = methodPrefix;
        }

        String getMethodPrefix(){
            return methodPrefix;
        }
    }

    interface FieldExtractor {
        Field getField(Class sourceClass, String fieldName);
    }

    private Object targetObject;
    private Class targetObjectClass;
    private String targetColumnName;
    private MethodType type;

    private Field targetColumnField;

    public ReflectionHelper(Object targetObject, String targetColumnName, MethodType type){
        this(targetObject, targetColumnName, type, new AttributeNameFieldExtractor());
    }

    public ReflectionHelper(Object targetObject, String targetColumnName, MethodType type, FieldExtractor fieldExtractor) {
        this.targetObject = targetObject;
        this.targetObjectClass = targetObject.getClass();
        this.targetColumnName = targetColumnName;
        this.type = type;
        targetColumnField = fieldExtractor.getField(targetObjectClass, targetColumnName);
    }

    public Class getTargetColumnType(){
        return targetColumnField.getType();
    }

    public Object invokeOnTarget(Object... args){
        if(argumentListContainConvertionErrors(args)){
            throw new IllegalStateException("Untranslatable item " + targetColumnName + ", " + targetObjectClass);
        }
        try {
            Method method = getMethod();
            return method.invoke(targetObject, args);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("no setter for field name " +
                    targetColumnName + " found for class: " +
                    targetObjectClass);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("cannot invoke method with parameters " +
                    targetColumnName + ", " +
                    targetObjectClass);
        }
    }

    private boolean argumentListContainConvertionErrors(Object[] args) {
        return Arrays.asList(args).stream().anyMatch(arg -> TranslationHelper.ERROR_OBJECT == arg);
    }

    private String getMethodName(Field targetColumnField) {
        return type.getMethodPrefix() + getMethodizedName(targetColumnField);
    }

    private String getMethodizedName(Field targetColumnField){
        String fieldName = targetColumnField.getName();
        return fieldName.substring(0,1).toUpperCase() +
                fieldName.substring(1);
    }

    private Method getMethod() throws NoSuchMethodException {
        String methodName = getMethodName(targetColumnField);
        if(type == MethodType.SET) {
            return targetObjectClass.getMethod(methodName, targetColumnField.getType());
        } else {
            return targetObjectClass.getMethod(methodName);
        }
    }
}
