package it.cnr.contab.util;
/**
 * Formattatore di importi a 2 cifre decimali positivi
 */

public class Importo2CifrePositivoFormat extends Importo2CifreFormat {
public Importo2CifrePositivoFormat() {
	super();
}

public Object parseObject(String source) throws java.text.ParseException{

	Object obj = super.parseObject(source);
	if (obj != null && obj instanceof java.math.BigDecimal) {
		java.math.BigDecimal bd = (java.math.BigDecimal)obj;
		if (bd.signum() < 0)
			throw new it.cnr.jada.bulk.ValidationParseException("sono ammessi solo valori positivi!", 0);
	}
	return obj;
	
}
}