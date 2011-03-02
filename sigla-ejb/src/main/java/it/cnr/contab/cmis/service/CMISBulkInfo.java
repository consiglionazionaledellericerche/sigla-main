package it.cnr.contab.cmis.service;

import it.cnr.cmisdl.service.DictionaryService;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CMISBulkInfo<T extends Serializable> {
	private transient static final Log logger = LogFactory.getLog(CMISBulkInfo.class);
	private DictionaryService dictionaryService;
	private Map<Class<?>, ObjectType> cacheObjType = new Hashtable<Class<?>, ObjectType>();
	private Map<Class<?>, Set<String>> cacheObjAspect = new Hashtable<Class<?>, Set<String>>();
	private Map<Class<?>, Set<PropertyDefinitionBulk<T>>> cacheObjPropertyDef = new Hashtable<Class<?>, Set<PropertyDefinitionBulk<T>>>();
	private Map<Class<?>, Set<PropertyDefinitionBulk<T>>> cacheObjAspectPropertyDef = new Hashtable<Class<?>, Set<PropertyDefinitionBulk<T>>>();

	public void setDictionaryService(DictionaryService dictionaryService) {
		this.dictionaryService = dictionaryService;
	}
	public ObjectType getType(Credentials systemCredentials, String typeId) throws TypeNotFoundException{
		ObjectType type = dictionaryService.getType(systemCredentials, typeId);
		try{
			if (type == null || type.getId() == null)
				throw new TypeNotFoundException(typeId);
		}catch(NullPointerException ex){
			throw new TypeNotFoundException(typeId, ex);
		}
		return type;
	}

	public ObjectType getChildType(Credentials systemCredentials, String parentTypeId, String typeId){
		Iterator<ObjectType> types = dictionaryService.getTypeChild(systemCredentials, parentTypeId);
		for (Iterator<ObjectType> iterator = types; iterator.hasNext();) {
			ObjectType type2 = iterator.next();
			try{
				if (type2.getId().equals(typeId))
					return type2;
			}catch(NullPointerException ex){
				throw new TypeNotFoundException(typeId, ex);
			}
		}
		return null;
	}

	public ObjectType getType(Credentials systemCredentials, OggettoBulk oggettoBulk){
		if (cacheObjType.containsKey(oggettoBulk.getClass()))
			return cacheObjType.get(oggettoBulk.getClass());
		CMISType cmisType = oggettoBulk.getClass().getAnnotation(CMISType.class);
		if (cmisType == null)
			throw new RuntimeException("Type is missing!");
		if (cmisType.parentName().length != 0 &&
				cmisType.parentName()[0].length() != 0){
			List<String> typeNames = Arrays.asList(cmisType.parentName());
			ObjectType parentType = null;
			for (String parentTypeName : typeNames) {
				if (parentType == null){
					parentType = getType(systemCredentials, parentTypeName);
					continue;
				}
				parentType = getChildType(systemCredentials, parentType.getId(), parentTypeName);
			}
			return cacheObjType.put(oggettoBulk.getClass(), getChildType(systemCredentials, parentType.getId(), cmisType.name()));
		}
		cacheObjType.put(oggettoBulk.getClass(), getType(systemCredentials, cmisType.name()));
		return cacheObjType.get(oggettoBulk.getClass());
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getAspect(Credentials systemCredentials, OggettoBulk oggettoBulk) throws IllegalArgumentException, IllegalAccessException{
		if (cacheObjAspect.containsKey(oggettoBulk.getClass()))
			return new ArrayList(cacheObjAspect.get(oggettoBulk.getClass()));
		Set<String> results = new HashSet<String>();
		List<Class<?>> classHierarchy = getClassHierarchy(oggettoBulk.getClass(), false, true);
		for (Class<?> class1 : classHierarchy) {
			List<Field> attributi = Arrays.asList(class1.getDeclaredFields());
			for (Field field : attributi) {
				if (field.isAnnotationPresent(CMISPolicy.class)){
					ObjectType aspect = getType(systemCredentials, field.getAnnotation(CMISPolicy.class).name());
					results.add(aspect.getId());
				}
			}
			List<Method> methods = Arrays.asList(class1.getMethods());
			for (Method method : methods) {
				if (method.isAnnotationPresent(CMISPolicy.class)){
					ObjectType aspect = getType(systemCredentials, method.getAnnotation(CMISPolicy.class).name());
					results.add(aspect.getId());
				}
			}			
		}
		cacheObjAspect.put(oggettoBulk.getClass(), results);
		return new ArrayList(cacheObjAspect.get(oggettoBulk.getClass()));
	}
	
	@SuppressWarnings("unchecked")
	public PropertyDefinitionBulk<T> createPropertyDefinition(Credentials systemCredentials, String typeId, String propertyTypeId, Class<T> value, Field field, Method method){
		return PropertyDefinitionBulk.construct((PropertyDefinition<T>) getType(systemCredentials, typeId).
									getPropertyDefinitions().get(propertyTypeId),field, method);
	}

	@SuppressWarnings("unchecked")
	public Set<PropertyDefinitionBulk<T>> getPropertyDefinition(Credentials systemCredentials, OggettoBulk oggettoBulk){
		if (cacheObjPropertyDef.containsKey(oggettoBulk.getClass()))
			return cacheObjPropertyDef.get(oggettoBulk.getClass());
		Set<PropertyDefinitionBulk<T>> results = new HashSet<PropertyDefinitionBulk<T>>();
		List<Class<?>> classHierarchy = getClassHierarchy(oggettoBulk.getClass(), false, true);
		for (Class<?> class1 : classHierarchy) {
			List<Field> attributi = Arrays.asList(class1.getDeclaredFields());
			for (Field field : attributi) {
				if (field.isAnnotationPresent(CMISProperty.class)){
						results.add(createPropertyDefinition(systemCredentials,
								getType(systemCredentials, oggettoBulk).getId(), 
								field.getAnnotation(CMISProperty.class).name(), (Class<T>) field.getType(),field, null));
				}
				if (field.isAnnotationPresent(CMISProperties.class)){
					List<CMISProperty> properties = Arrays.asList(field.getAnnotation(CMISProperties.class).property());
					for (CMISProperty cmisProperty : properties) {
							results.add(createPropertyDefinition(systemCredentials,
									getType(systemCredentials, oggettoBulk).getId(), 
									cmisProperty.name(), (Class<T>) field.getType(), field, null));
					}
				}
			}
			List<Method> methods = Arrays.asList(class1.getMethods());
			for (Method method : methods) {
				if (method.isAnnotationPresent(CMISProperty.class)){
					results.add(createPropertyDefinition(systemCredentials,
							getType(systemCredentials, oggettoBulk).getId(), 
							method.getAnnotation(CMISProperty.class).name(), (Class<T>) method.getReturnType(), null, method));
				}
				if (method.isAnnotationPresent(CMISProperties.class)){
					List<CMISProperty> properties = Arrays.asList(method.getAnnotation(CMISProperties.class).property());
					for (CMISProperty cmisProperty : properties) {
						results.add(createPropertyDefinition(systemCredentials,
								getType(systemCredentials, oggettoBulk).getId(), 
								cmisProperty.name(), (Class<T>) method.getReturnType(), null, method));
					}
				}
			}			
		}
		cacheObjPropertyDef.put(oggettoBulk.getClass(), results);
		return cacheObjPropertyDef.get(oggettoBulk.getClass());
	}	

	public List<Property<?>> getProperty(Credentials systemCredentials, OggettoBulk oggettoBulk){
		List<Property<?>> results = new ArrayList<Property<?>>();
 		Set<PropertyDefinitionBulk<T>> propertiesDef = getPropertyDefinition(systemCredentials, oggettoBulk);
		for(PropertyDefinitionBulk<T>  propertyDefinition : propertiesDef){
			try {
				results.add(propertyDefinition.createProperty(systemCredentials, oggettoBulk));
			} catch (PropertyNullValueException e) {
				continue;
			}catch (Exception e){
				throw new RuntimeException(e);
			}
		}
		return results;
	}

	public Set<PropertyDefinitionBulk<T>> getAspectPropertyDefinition(Credentials systemCredentials, OggettoBulk oggettoBulk){
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
						results.add(createPropertyDefinition(systemCredentials,
								getType(systemCredentials, field.getAnnotation(CMISPolicy.class).name()).getId(), 
								cmisProperty.name(), (Class<T>) field.getType(),field, null));
					}
				}
			}
			List<Method> methods = Arrays.asList(class1.getMethods());
			for (Method method : methods) {
				if (method.isAnnotationPresent(CMISPolicy.class)){
					List<CMISProperty> properties = Arrays.asList(method.getAnnotation(CMISPolicy.class).property());
					for (CMISProperty cmisProperty : properties) {
						results.add(createPropertyDefinition(systemCredentials,
								getType(systemCredentials, method.getAnnotation(CMISPolicy.class).name()).getId(), 
								cmisProperty.name(), (Class<T>) method.getReturnType(),null, method));
					}
				}
			}			
		}
		cacheObjAspectPropertyDef.put(oggettoBulk.getClass(), results);
		return cacheObjAspectPropertyDef.get(oggettoBulk.getClass());
	}	
	public List<Property<?>> getAspectProperty(Credentials systemCredentials, OggettoBulk oggettoBulk){
		List<Property<?>> results = new ArrayList<Property<?>>();
 		Set<PropertyDefinitionBulk<T>> propertiesDef = getAspectPropertyDefinition(systemCredentials, oggettoBulk);
		for(PropertyDefinitionBulk<T>  propertyDefinition : propertiesDef){
			try {
				results.add(propertyDefinition.createProperty(systemCredentials, oggettoBulk));
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
