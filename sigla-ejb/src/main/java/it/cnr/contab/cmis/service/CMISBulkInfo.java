package it.cnr.contab.cmis.service;

import it.cnr.contab.cmis.CMISTypeName;
import it.cnr.contab.cmis.annotation.CMISPolicy;
import it.cnr.contab.cmis.annotation.CMISProperties;
import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.contab.cmis.annotation.CMISType;
import it.cnr.contab.cmis.converter.PropertyDefinitionBulk;
import it.cnr.jada.bulk.OggettoBulk;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CMISBulkInfo<T extends Serializable> {
	private transient static final Logger logger = LoggerFactory.getLogger(CMISBulkInfo.class);
	private Map<Class<?>, ObjectType> cacheObjType = new Hashtable<Class<?>, ObjectType>();
	private Map<Class<?>, Set<String>> cacheObjAspect = new Hashtable<Class<?>, Set<String>>();
	private Map<Class<?>, Set<PropertyDefinitionBulk<T>>> cacheObjPropertyDef = new Hashtable<Class<?>, Set<PropertyDefinitionBulk<T>>>();
	private Map<Class<?>, Set<PropertyDefinitionBulk<T>>> cacheObjAspectPropertyDef = new Hashtable<Class<?>, Set<PropertyDefinitionBulk<T>>>();

	
	public ObjectType getType(Session session, String typeId) throws TypeNotFoundException{
		ObjectType type = session.getTypeDefinition(typeId);
		try{
			if (type == null || type.getId() == null)
				throw new TypeNotFoundException(typeId);
		}catch(NullPointerException ex){
			throw new TypeNotFoundException(typeId, ex);
		}
		return type;
	}

	public ObjectType getChildType(Session session, String parentTypeId, String typeId){
		ItemIterable<ObjectType> types = session.getTypeChildren(parentTypeId, true);
		for (ObjectType type2 : types) {
			try{
				if (type2.getId().equals(typeId))
					return type2;
			}catch(NullPointerException ex){
				throw new TypeNotFoundException(typeId, ex);
			}			
		}
		return null;
	}

	public ObjectType getType(Session session, OggettoBulk oggettoBulk){
		if (!(oggettoBulk instanceof CMISTypeName) && cacheObjType.containsKey(oggettoBulk.getClass()))
			return cacheObjType.get(oggettoBulk.getClass());
		if (oggettoBulk instanceof CMISTypeName)
			cacheObjType.put(oggettoBulk.getClass(), getType(session, ((CMISTypeName)oggettoBulk).getTypeName()));
		else{
			CMISType cmisType = oggettoBulk.getClass().getAnnotation(CMISType.class);
			if (cmisType == null && !(oggettoBulk instanceof CMISTypeName))
				throw new RuntimeException("Type is missing!");
			if (cmisType != null && cmisType.parentName().length != 0 &&
					cmisType.parentName()[0].length() != 0){
				List<String> typeNames = Arrays.asList(cmisType.parentName());
				ObjectType parentType = null;
				for (String parentTypeName : typeNames) {
					if (parentType == null){
						parentType = getType(session, parentTypeName);
						continue;
					}
					parentType = getChildType(session, parentType.getId(), parentTypeName);
				}
				return cacheObjType.put(oggettoBulk.getClass(), getChildType(session, parentType.getId(), cmisType.name()));
			}
			cacheObjType.put(oggettoBulk.getClass(), getType(session, cmisType.name()));

		}
		return cacheObjType.get(oggettoBulk.getClass());
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getAspect(Session session, OggettoBulk oggettoBulk) throws IllegalArgumentException, IllegalAccessException{
		if (cacheObjAspect.containsKey(oggettoBulk.getClass()))
			return new ArrayList(cacheObjAspect.get(oggettoBulk.getClass()));
		Set<String> results = new HashSet<String>();
		List<Class<?>> classHierarchy = getClassHierarchy(oggettoBulk.getClass(), false, true);
		for (Class<?> class1 : classHierarchy) {
			List<Field> attributi = Arrays.asList(class1.getDeclaredFields());
			for (Field field : attributi) {
				if (field.isAnnotationPresent(CMISPolicy.class)){
					ObjectType aspect = getType(session, field.getAnnotation(CMISPolicy.class).name());
					results.add(aspect.getId());
				}
			}
			List<Method> methods = Arrays.asList(class1.getMethods());
			for (Method method : methods) {
				if (method.isAnnotationPresent(CMISPolicy.class)){
					ObjectType aspect = getType(session, method.getAnnotation(CMISPolicy.class).name());
					results.add(aspect.getId());
				}
			}			
		}
		cacheObjAspect.put(oggettoBulk.getClass(), results);
		return new ArrayList(cacheObjAspect.get(oggettoBulk.getClass()));
	}
	
	@SuppressWarnings("unchecked")
	public PropertyDefinitionBulk<T> createPropertyDefinition(Session session, String typeId, String propertyTypeId, Class<T> value, Field field, Method method){
		return PropertyDefinitionBulk.construct((PropertyDefinition<T>) getType(session, typeId).
									getPropertyDefinitions().get(propertyTypeId),field, method);
	}

	@SuppressWarnings("unchecked")
	public Set<PropertyDefinitionBulk<T>> getPropertyDefinition(Session session, OggettoBulk oggettoBulk){
		if (cacheObjPropertyDef.containsKey(oggettoBulk.getClass()))
			return cacheObjPropertyDef.get(oggettoBulk.getClass());
		Set<PropertyDefinitionBulk<T>> results = new HashSet<PropertyDefinitionBulk<T>>();
		List<Class<?>> classHierarchy = getClassHierarchy(oggettoBulk.getClass(), false, true);
		for (Class<?> class1 : classHierarchy) {
			List<Field> attributi = Arrays.asList(class1.getDeclaredFields());
			for (Field field : attributi) {
				if (field.isAnnotationPresent(CMISProperty.class)){
						results.add(createPropertyDefinition(session,
								getType(session, oggettoBulk).getId(), 
								field.getAnnotation(CMISProperty.class).name(), (Class<T>) field.getType(),field, null));
				}
				if (field.isAnnotationPresent(CMISProperties.class)){
					List<CMISProperty> properties = Arrays.asList(field.getAnnotation(CMISProperties.class).property());
					for (CMISProperty cmisProperty : properties) {
							results.add(createPropertyDefinition(session,
									getType(session, oggettoBulk).getId(), 
									cmisProperty.name(), (Class<T>) field.getType(), field, null));
					}
				}
			}
			List<Method> methods = Arrays.asList(class1.getMethods());
			for (Method method : methods) {
				if (method.isAnnotationPresent(CMISProperty.class)){
					results.add(createPropertyDefinition(session,
							getType(session, oggettoBulk).getId(), 
							method.getAnnotation(CMISProperty.class).name(), (Class<T>) method.getReturnType(), null, method));
				}
				if (method.isAnnotationPresent(CMISProperties.class)){
					List<CMISProperty> properties = Arrays.asList(method.getAnnotation(CMISProperties.class).property());
					for (CMISProperty cmisProperty : properties) {
						results.add(createPropertyDefinition(session,
								getType(session, oggettoBulk).getId(), 
								cmisProperty.name(), (Class<T>) method.getReturnType(), null, method));
					}
				}
			}			
		}
		cacheObjPropertyDef.put(oggettoBulk.getClass(), results);
		return cacheObjPropertyDef.get(oggettoBulk.getClass());
	}	

	public List<Property<?>> getProperty(Session session, OggettoBulk oggettoBulk){
		List<Property<?>> results = new ArrayList<Property<?>>();
 		Set<PropertyDefinitionBulk<T>> propertiesDef = getPropertyDefinition(session, oggettoBulk);
		for(PropertyDefinitionBulk<T>  propertyDefinition : propertiesDef){
			try {
				results.add(propertyDefinition.createProperty(session, oggettoBulk));
			} catch (PropertyNullValueException e) {
				continue;
			}catch (Exception e){
				throw new RuntimeException(e);
			}
		}
		return results;
	}

	public Set<PropertyDefinitionBulk<T>> getAspectPropertyDefinition(Session session, OggettoBulk oggettoBulk){
		if (cacheObjAspectPropertyDef.containsKey(oggettoBulk.getClass()))
			return cacheObjAspectPropertyDef.get(oggettoBulk.getClass());
		Set<PropertyDefinitionBulk<T>> results = new HashSet<PropertyDefinitionBulk<T>>();
		List<Class<?>> classHierarchy = getClassHierarchy(oggettoBulk.getClass(), false, true);
		for (Class<?> class1 : classHierarchy) {
			List<Field> attributi = Arrays.asList(class1.getDeclaredFields());
			for (Field field : attributi) {
				if (field.isAnnotationPresent(CMISPolicy.class)){
					List<CMISProperty> properties = Arrays.asList(field.getAnnotation(CMISPolicy.class).property());
					for (CMISProperty cmisProperty : properties) {
						results.add(createPropertyDefinition(session,
								getType(session, field.getAnnotation(CMISPolicy.class).name()).getId(), 
								cmisProperty.name(), (Class<T>) field.getType(),field, null));
					}
				}
			}
			List<Method> methods = Arrays.asList(class1.getMethods());
			for (Method method : methods) {
				if (method.isAnnotationPresent(CMISPolicy.class)){
					List<CMISProperty> properties = Arrays.asList(method.getAnnotation(CMISPolicy.class).property());
					for (CMISProperty cmisProperty : properties) {
						results.add(createPropertyDefinition(session,
								getType(session, method.getAnnotation(CMISPolicy.class).name()).getId(), 
								cmisProperty.name(), (Class<T>) method.getReturnType(),null, method));
					}
				}
			}			
		}
		cacheObjAspectPropertyDef.put(oggettoBulk.getClass(), results);
		return cacheObjAspectPropertyDef.get(oggettoBulk.getClass());
	}	
	public List<Property<?>> getAspectProperty(Session session, OggettoBulk oggettoBulk){
		List<Property<?>> results = new ArrayList<Property<?>>();
 		Set<PropertyDefinitionBulk<T>> propertiesDef = getAspectPropertyDefinition(session, oggettoBulk);
		for(PropertyDefinitionBulk<T>  propertyDefinition : propertiesDef){
			try {
				results.add(propertyDefinition.createProperty(session, oggettoBulk));
			} catch (PropertyNullValueException e) {
				continue;
			}catch (Exception e){
				throw new RuntimeException(e);
			}
		}
		return results;
	}
	
	public List<Class<?>> getClassHierarchy(Class<?> targetClass, boolean reverse, boolean includeSelf) {
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
}
