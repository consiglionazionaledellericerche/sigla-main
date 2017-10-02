package it.cnr.contab.spring.storage.converter;

import java.io.Serializable;

public interface Converter<H extends Serializable,T> {
	public H convert(T obj);
}
