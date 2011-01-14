package it.cnr.contab.cmis.converter;

import java.io.Serializable;

public class DefaultConverter implements Converter<Serializable, Object> {

	public Serializable convert(Object obj) {
		return (Serializable)obj;
	}
}
