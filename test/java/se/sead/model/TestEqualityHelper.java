package se.sead.model;

import se.sead.sead.model.LoggableEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TestEqualityHelper {

    private static final String GETTER_PREFIX = "get";

    public static <T> boolean equalsWithoutIdIfNeeded(T entity1, T entity2){
        if((entity1 == null && entity2 != null) || (entity1 != null && entity2 == null)){
            return false;
        }
        if(entity1 instanceof LoggableEntity && entity2 instanceof LoggableEntity){
            LoggableEntity firstEntity = (LoggableEntity) entity1;
            LoggableEntity secondEntity = (LoggableEntity) entity2;
            if( firstEntity.isNewItem() || secondEntity.isNewItem()) {
                EntityEqualityComparator entity1Comparator = new EntityEqualityComparator(firstEntity);
                EntityEqualityComparator entity2Comparator = new EntityEqualityComparator(secondEntity);
                return entity1Comparator.equals(entity2Comparator);
            }
        }
        return Objects.equals(entity1, entity2);
    }

    private static class EntityEqualityComparator {
        private Object entity;
        private ClassMethodInformation methodInformation;

        private EntityEqualityComparator(LoggableEntity entity){
            this.entity = entity;
            methodInformation = new ClassMethodInformation(entity.getClass());
        }

        ClassMethodInformation getMethodInformation(){
            return methodInformation;
        }

        Object getEntity(){
            return entity;
        }

        public boolean equals(Object other){
            if(other == this){ return true;}
            if(other instanceof EntityEqualityComparator){
                EntityEqualityComparator otherComparator = (EntityEqualityComparator) other;
                if(!otherComparator.getMethodInformation().getInformationClass().equals(methodInformation.getInformationClass())){
                    return false;
                }
                for (Method getterMethod :
                        methodInformation.getWhiteListedMethods()) {
                    try {
                        Object entity1Result = getterMethod.invoke(entity);
                        Object entity2Result = getterMethod.invoke(otherComparator.getEntity());
                        if(!TestEqualityHelper.equalsWithoutIdIfNeeded(entity1Result, entity2Result))
                            return false;
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new IllegalStateException(e);
                    }
                }
                return true;
            }
            return false;
        }

    }

    private static class ClassMethodInformation {

        private static final List<String> METHOD_NAME_WHITE_LIST =
                Arrays.asList("get");
        private static final List<String> BLACK_LISTED_METHOD_NAMES =
                Arrays.asList("getId");
        private static final String TEST_PACKAGE_NAME;

        static {
            String testSpeciesClassName = TestTaxaSpecies.class.getName();
            TEST_PACKAGE_NAME = testSpeciesClassName.substring(0, testSpeciesClassName.lastIndexOf('.'));
        }

        private List<Method> whiteListedMethods = new ArrayList<>();
        private Class entityClass;

        ClassMethodInformation(Class entityClass){
            setEntityClass(entityClass);
            setupMethods();
        }

        private void setEntityClass(Class entityClass) {
            this.entityClass = entityClass;
            if(entityClass.getName().startsWith(TEST_PACKAGE_NAME)){
                this.entityClass = this.entityClass.getSuperclass();
            }
        }

        private void setupMethods(){
            for (Method method :
                    entityClass.getDeclaredMethods()) {
                if(isWhiteListedMethod(method)){
                    whiteListedMethods.add(method);
                }
            }
        }

        private boolean isWhiteListedMethod(Method method) {
            for (String whiteListedPrefix :
                    METHOD_NAME_WHITE_LIST) {
                if (method.getName().startsWith(whiteListedPrefix)
                        &&
                        !BLACK_LISTED_METHOD_NAMES.contains(method.getName())){
                    return true;
                }
            }
            return false;
        }

        Class getInformationClass(){
            return entityClass;
        }

        List<Method> getWhiteListedMethods(){
            return whiteListedMethods;
        }

    }
}

