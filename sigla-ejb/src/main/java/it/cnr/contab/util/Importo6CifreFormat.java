package it.cnr.contab.util;
/**
 * Formattatore di importi a 6 cifre decimali
 */

public class Importo6CifreFormat extends GenericImportoFormat {
	public final static java.text.Format format = new java.text.DecimalFormat("#,##0.000000");

public Importo6CifreFormat() {
	super();
	setPrecision(6);
}

public java.text.Format getFormat() {
	return format;
}
}