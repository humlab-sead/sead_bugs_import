package se.sead.model;

import se.sead.sead.model.LoggableEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

public class TestEqualityHelper<T> {

    private ComparisonConfiguration configuration;

    public TestEqualityHelper(){
        this(new ComparisonConfiguration());
    }

    TestEqualityHelper(ComparisonConfiguration configuration){
        this.configuration = configuration;
    }

    public TestEqualityHelper(boolean useCompareForBigDecimalEquality){
        this(new ComparisonConfiguration(useCompareForBigDecimalEquality));
    }

    public void addMethodInformation(ClassMethodInformation entityMethodInformation){
        configuration.addMethodInformation(entityMethodInformation);
    }

    public void setUseComparisonInsteadOfEqualsForBigDecimals(){
        configuration.useCompareForBigDecimalEquality = true;
    }

    public boolean equalsWithoutBlackListedMethods(T entity1, T entity2) {
        if ((entity1 == null && entity2 != null) || (entity1 != null && entity2 == null)) {
            return false;
        }
        if (entity1 instanceof LoggableEntity && entity2 instanceof LoggableEntity) {
            LoggableEntity firstEntity = (LoggableEntity) entity1;
            LoggableEntity secondEntity = (LoggableEntity) entity2;
            if (firstEntity.isNewItem() || secondEntity.isNewItem()) {
                EntityEqualityComparator entity1Comparator = createEntityEqualityComparator(firstEntity);
                EntityEqualityComparator entity2Comparator = createEntityEqualityComparator(secondEntity);
                return entity1Comparator.equals(entity2Comparator);
            }
        } else if(entity1 instanceof BigDecimal && entity2 instanceof BigDecimal && configuration.useCompareForBigDecimalEquality){
            return ((BigDecimal) entity1).compareTo((BigDecimal)entity2) == 0;
        } else if(entity1 instanceof Collection && entity2 instanceof Collection){
            return equals((Collection) entity1, (Collection) entity2);
        }
        return Objects.equals(entity1, entity2);
    }

    private EntityEqualityComparator createEntityEqualityComparator(LoggableEntity entity){
        return new EntityEqualityComparator(entity, configuration);
    }

    private boolean equals(Collection collection1, Collection collection2){
        if(collection1.size() != collection2.size()){
            return false;
        }
        List list1 = new ArrayList(collection1);
        List list2 = new ArrayList(collection2);
        TestEqualityHelper helper = new TestEqualityHelper(configuration);
        for (int i = 0; i < list1.size(); i++) {
            Object item1 = list1.get(i);
            Object item2 = list2.get(i);
            if(!helper.equalsWithoutBlackListedMethods(item1, item2)){
                return false;
            }
        }
        return true;
    }

    public static <T> boolean equalsWithoutIdIfNeeded(T entity1, T entity2){
        if((entity1 == null && entity2 != null) || (entity1 != null && entity2 == null)){
            return false;
        }
        TestEqualityHelper<T> helper = new TestEqualityHelper<T>();
        return helper.equalsWithoutBlackListedMethods(entity1, entity2);
    }

    private static class EntityEqualityComparator {
        private ComparisonConfiguration configuration;
        private Object entity;
        private ClassMethodInformation currentMethodInformation;

        EntityEqualityComparator(LoggableEntity entity, ComparisonConfiguration configuration){
            this.entity = entity;
            this.configuration = configuration;
            Class entityClass = ClassMethodInformation.getPersistenceClass(entity.getClass());
            this.currentMethodInformation = configuration.getClassMethodInformation(entityClass);
            if(currentMethodInformation == null){
                currentMethodInformation = new ClassMethodInformation(entityClass);
                configuration.addMethodInformation(currentMethodInformation);
            }
        }

        ClassMethodInformation getCurrentMethodInformation(){
            return currentMethodInformation;
        }

        Object getEntity(){
            return entity;
        }

        public boolean equals(Object other){
            if(other == this){ return true;}
            if(other instanceof EntityEqualityComparator){
                EntityEqualityComparator otherComparator = (EntityEqualityComparator) other;
                if(!otherComparator.getCurrentMethodInformation().getInformationClass().equals(currentMethodInformation.getInformationClass())){
                    return false;
                }
                for (Method getterMethod :
                        currentMethodInformation.getWhiteListedMethods()) {
                    try {
                        Object entity1Result = getterMethod.invoke(entity);
                        Object entity2Result = getterMethod.invoke(otherComparator.getEntity());
                        TestEqualityHelper helper = new TestEqualityHelper(configuration);
                        if(!helper.equalsWithoutBlackListedMethods(entity1Result, entity2Result)) {
                            System.err.println("reporting false for method: " + getterMethod.getName());
                            return false;
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new IllegalStateException(e);
                    }
                }
                return true;
            }
            return false;
        }

    }

    private static class ComparisonConfiguration {
        private Map<Class, ClassMethodInformation> methodInformation;
        private boolean useCompareForBigDecimalEquality;

        ComparisonConfiguration(){
            this(false);
        }

        ComparisonConfiguration(boolean useCompareForBigDecimalEquality){
            methodInformation = new HashMap<>();
            this.useCompareForBigDecimalEquality = useCompareForBigDecimalEquality;
        }

        void addMethodInformation(ClassMethodInformation entityMethodInformation){
            methodInformation.put(entityMethodInformation.getInformationClass(), entityMethodInformation);
        }

        ClassMethodInformation getClassMethodInformation(Class entityClass){
            return methodInformation.get(entityClass);
        }
    }

    public static class ClassMethodInformation {

        private static final List<String> DEFAULT_METHOD_NAME_WHITE_LIST =
                Arrays.asList("get");
        private static final List<String> DEFAULT_BLACK_LISTED_METHOD_NAMES =
                Arrays.asList("getId");
        private static final String TEST_PACKAGE_NAME;

        static {
            String testSpeciesClassName = TestTaxaSpecies.class.getName();
            TEST_PACKAGE_NAME = testSpeciesClassName.substring(0, testSpeciesClassName.lastIndexOf('.'));
        }

        private List<Method> whiteListedMethods = new ArrayList<>();
        private List<String> blackListedMethodNames = new ArrayList<>();
        private Class entityClass;

        public ClassMethodInformation(Class entityClass, String... extraBlackListedMethodNames){
            this.entityClass = getPersistenceClass(entityClass);
            blackListedMethodNames.addAll(DEFAULT_BLACK_LISTED_METHOD_NAMES);
            if(extraBlackListedMethodNames != null) {
                blackListedMethodNames.addAll(Arrays.asList(extraBlackListedMethodNames));
            }
            setupMethods();
        }

        public ClassMethodInformation(Class entityClass, List<String> extraBlackListedMethodNames){
            this(entityClass, extraBlackListedMethodNames.toArray(new String[extraBlackListedMethodNames.size()]));
        }

        private static Class getPersistenceClass(Class entityClass) {
            if(entityClass.getName().startsWith(TEST_PACKAGE_NAME)){
                return entityClass.getSuperclass();
            } else {
                return entityClass;
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
                    DEFAULT_METHOD_NAME_WHITE_LIST) {
                if (method.getName().startsWith(whiteListedPrefix)
                        &&
                        !blackListedMethodNames.contains(method.getName())){
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

