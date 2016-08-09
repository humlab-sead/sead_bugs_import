package se.sead.bugsimport.translations.engines;

import se.sead.bugsimport.translations.converters.TranslationHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

class ReflectionHelper {

    enum MethodType {
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

    private Object targetObject;
    private Class targetObjectClass;
    private String targetColumnName;
    private MethodType type;

    private Field targetColumnField;

    ReflectionHelper(Object targetObject, String targetColumnName, MethodType type) {
        this.targetObject = targetObject;
        this.targetObjectClass = targetObject.getClass();
        this.targetColumnName = targetColumnName;
        this.type = type;
        targetColumnField = getField();
    }

    private Field getField() {
        try {
            return targetObjectClass.getDeclaredField(targetColumnName);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("no field named " +
                    targetColumnName + " for type " +
                    targetObjectClass);
        }
    }

    Class getTargetColumnType(){
        return targetColumnField.getType();
    }

    Object invokeOnTarget(Object... args){
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
