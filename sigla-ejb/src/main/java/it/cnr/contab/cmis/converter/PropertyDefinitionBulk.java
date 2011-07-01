package it.cnr.contab.cmis.converter;

import it.cnr.cmisdl.service.DictionaryService;
import it.cnr.contab.cmis.service.PropertyNullValueException;
import it.cnr.contab.service.SpringUtil;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.Introspector;

import java.beans.IntrospectionException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.commons.httpclient.Credentials;

public class PropertyDefinitionBulk<T extends Serializable>{
	private static final long serialVersionUID = 1L;
	private final PropertyDefinition<T> propertyDefinition;
	private final Field field;
	private final Method method;
	private final String beanNameConverter;
	
	public static <T extends Serializable> PropertyDefinitionBulk<T> construct(PropertyDefinition<T> propertyDefinition, Field field, Method method, String beanNameConverter) {
		return new PropertyDefinitionBulk<T>(propertyDefinition, field, method, beanNameConverter);
	}
	
	public PropertyDefinitionBulk(PropertyDefinition<T> propertyDefinition, Field field, Method method, String beanNameConverter) {
		super();
		this.field = field;
		this.method = method;
		this.propertyDefinition = propertyDefinition;
		this.beanNameConverter = beanNameConverter;
	}

	@SuppressWarnings("unchecked")
	public Property<T> createProperty(Credentials systemCredentials, OggettoBulk oggettoBulk) throws PropertyNullValueException, IntrospectionException, InvocationTargetException, 
					NoSuchMethodException, IllegalAccessException{
		T value = null;
		if (field != null)
			value = (T) Introspector.getPropertyValue(oggettoBulk, field.getName());
		if (method != null)
			value = (T)Introspector.invoke(oggettoBulk, method);
		if (value == null)
			throw new PropertyNullValueException();	
		value = (T) SpringUtil.getBean(beanNameConverter, Converter.class).convert(value);
		return SpringUtil.getBean("dictionaryService", DictionaryService.class).createProperty(systemCredentials, propertyDefinition, Arrays.asList(value));
	}
}
