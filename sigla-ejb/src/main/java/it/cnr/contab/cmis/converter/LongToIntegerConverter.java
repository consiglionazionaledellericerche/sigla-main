package it.cnr.contab.cmis.converter;

public class LongToIntegerConverter implements Converter<Integer, Long> {

	public Integer convert(Long value) {
		if (value == null)
			return null;
		return Integer.valueOf(value.intValue());
	}

}
