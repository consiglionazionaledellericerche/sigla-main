package it.cnr.contab.spring.storage;

import it.cnr.contab.cmis.CMISTypeName;
import it.cnr.contab.cmis.annotation.CMISPolicy;
import it.cnr.contab.cmis.annotation.CMISProperties;
import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.contab.cmis.annotation.CMISType;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.Introspector;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by mspasiano on 6/15/17.
 */
@Service
public class StoreBulkInfo {
    private Map<Class<?>, String> cacheObjType = new Hashtable<Class<?>, String>();
    private Map<Class<?>, Set<String>> cacheObjAspect = new Hashtable<Class<?>, Set<String>>();
    private Map<Class<?>, Map<String, PropertyValue>> cacheObjPropertyDef = new Hashtable<Class<?>, Map<String, PropertyValue>>();
    private Map<Class<?>, Map<String, PropertyValue>> cacheObjAspectPropertyDef = new Hashtable<Class<?>, Map<String, PropertyValue>>();

    private class PropertyValue {
        private final Field field;
        private final Method method;

        public PropertyValue(Field field, Method method) {
            super();
            this.field = field;
            this.method = method;
        }

        public Object getValue(OggettoBulk oggettoBulk) {
            return Optional.ofNullable(field)
                    .map(field1 -> {
                        try {
                            return Introspector.getPropertyValue(oggettoBulk, field1.getName());
                        } catch (IntrospectionException|InvocationTargetException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }).orElse(
                            Optional.ofNullable(method)
                                    .map(method1 -> {
                                        try {
                                            return Introspector.invoke(oggettoBulk, method1);
                                        } catch (NoSuchMethodException|IllegalAccessException|InvocationTargetException e) {
                                            throw new IllegalArgumentException(e);
                                        }
                                    })
                                    .orElse(null)
                    );
        }

    }
    private List<Class<?>> getClassHierarchy(Class<?> targetClass, boolean reverse, boolean includeSelf) {
        List<Class<?>> hierarchy = new ArrayList<Class<?>>();
        if (!includeSelf) {
            targetClass = targetClass.getSuperclass();
        }
        while (targetClass != null) {
            if (reverse) {
                hierarchy.add(targetClass);
            } else {
                hierarchy.add(0, targetClass);
            }
            targetClass = targetClass.getSuperclass();
        }
        return hierarchy;
    }

    public String getType(OggettoBulk oggettoBulk){
        if (!(oggettoBulk instanceof CMISTypeName) && cacheObjType.containsKey(oggettoBulk.getClass()))
            return cacheObjType.get(oggettoBulk.getClass());
        if (oggettoBulk instanceof CMISTypeName)
            cacheObjType.put(oggettoBulk.getClass(), ((CMISTypeName)oggettoBulk).getTypeName());
        else{
            CMISType cmisType = oggettoBulk.getClass().getAnnotation(CMISType.class);
            if (cmisType == null && !(oggettoBulk instanceof CMISTypeName))
                throw new RuntimeException("Type is missing!");
            cacheObjType.put(oggettoBulk.getClass(), cmisType.name());
        }
        return cacheObjType.get(oggettoBulk.getClass());
    }

    public List<String> getAspect(OggettoBulk oggettoBulk) throws StorageException{
        if (cacheObjAspect.containsKey(oggettoBulk.getClass()))
            return new ArrayList(cacheObjAspect.get(oggettoBulk.getClass()));
        Set<String> results = new HashSet<String>();
        List<Class<?>> classHierarchy = getClassHierarchy(oggettoBulk.getClass(), false, true);
        for (Class<?> class1 : classHierarchy) {
            List<Field> attributi = Arrays.asList(class1.getDeclaredFields());
            for (Field field : attributi) {
                if (field.isAnnotationPresent(CMISPolicy.class)){
                    results.add(field.getAnnotation(CMISPolicy.class).name());
                }
            }
            List<Method> methods = Arrays.asList(class1.getMethods());
            for (Method method : methods) {
                if (method.isAnnotationPresent(CMISPolicy.class)){
                    results.add(method.getAnnotation(CMISPolicy.class).name());
                }
            }
        }
        cacheObjAspect.put(oggettoBulk.getClass(), results);
        return new ArrayList(cacheObjAspect.get(oggettoBulk.getClass()));
    }

    public Map<String, Object> getPropertyValue(OggettoBulk oggettoBulk){
        return getPropertyDefinition(oggettoBulk).entrySet().stream()
                .collect(
                        HashMap::new, (m,v)->m.put(v.getKey(), v.getValue().getValue(oggettoBulk)), HashMap::putAll
                );
    }

    public Map<String, PropertyValue> getPropertyDefinition(OggettoBulk oggettoBulk){
        if (cacheObjPropertyDef.containsKey(oggettoBulk.getClass()))
            return cacheObjPropertyDef.get(oggettoBulk.getClass());
        Map<String, PropertyValue> results = new HashMap<String, PropertyValue>();
        List<Class<?>> classHierarchy = getClassHierarchy(oggettoBulk.getClass(), false, true);
        for (Class<?> class1 : classHierarchy) {
            List<Field> attributi = Arrays.asList(class1.getDeclaredFields());
            for (Field field : attributi) {
                if (field.isAnnotationPresent(CMISProperty.class)){
                    results.put(field.getAnnotation(CMISProperty.class).name(), new PropertyValue(field, null));
                }
                if (field.isAnnotationPresent(CMISProperties.class)){
                    List<CMISProperty> properties = Arrays.asList(field.getAnnotation(CMISProperties.class).property());
                    for (CMISProperty cmisProperty : properties) {
                        results.put(cmisProperty.name(), new PropertyValue(field, null));
                    }
                }
            }
            List<Method> methods = Arrays.asList(class1.getMethods());
            for (Method method : methods) {
                if (method.isAnnotationPresent(CMISProperty.class)){
                    results.put(method.getAnnotation(CMISProperty.class).name(), new PropertyValue(null, method));
                }
                if (method.isAnnotationPresent(CMISProperties.class)){
                    List<CMISProperty> properties = Arrays.asList(method.getAnnotation(CMISProperties.class).property());
                    for (CMISProperty cmisProperty : properties) {
                        results.put(cmisProperty.name(), new PropertyValue(null, method));
                    }
                }
            }
        }
        cacheObjPropertyDef.put(oggettoBulk.getClass(), results);
        return cacheObjPropertyDef.get(oggettoBulk.getClass());
    }

    public Map<String, Object> getAspectPropertyValue(OggettoBulk oggettoBulk){
        return getAspectPropertyDefinition(oggettoBulk).entrySet().stream()
                .collect(
                        HashMap::new, (m,v)->m.put(v.getKey(), v.getValue().getValue(oggettoBulk)), HashMap::putAll
                );

    }
    public Map<String, PropertyValue> getAspectPropertyDefinition(OggettoBulk oggettoBulk){
        if (cacheObjAspectPropertyDef.containsKey(oggettoBulk.getClass()))
            return cacheObjAspectPropertyDef.get(oggettoBulk.getClass());
        Map<String, PropertyValue> results = new HashMap<String, PropertyValue>();
        List<Class<?>> classHierarchy = getClassHierarchy(oggettoBulk.getClass(), false, true);
        for (Class<?> class1 : classHierarchy) {
            List<Field> attributi = Arrays.asList(class1.getDeclaredFields());
            for (Field field : attributi) {
                if (field.isAnnotationPresent(CMISPolicy.class)){
                    List<CMISProperty> properties = Arrays.asList(field.getAnnotation(CMISPolicy.class).property());
                    for (CMISProperty cmisProperty : properties) {
                        results.put(cmisProperty.name(), new PropertyValue(field, null));
                    }
                }
            }
            List<Method> methods = Arrays.asList(class1.getMethods());
            for (Method method : methods) {
                if (method.isAnnotationPresent(CMISPolicy.class)){
                    List<CMISProperty> properties = Arrays.asList(method.getAnnotation(CMISPolicy.class).property());
                    for (CMISProperty cmisProperty : properties) {
                        results.put(cmisProperty.name(), new PropertyValue(null, method));
                    }
                }
            }
        }
        cacheObjAspectPropertyDef.put(oggettoBulk.getClass(), results);
        return cacheObjAspectPropertyDef.get(oggettoBulk.getClass());
    }
}