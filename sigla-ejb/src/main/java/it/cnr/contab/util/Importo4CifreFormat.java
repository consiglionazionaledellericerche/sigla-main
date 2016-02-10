package it.cnr.contab.util;
/**
 * Formattatore di importi a 4 cifre decimali
 */

public class Importo4CifreFormat extends GenericImportoFormat {
	public final static java.text.Format format = new java.text.DecimalFormat("#,##0.0000");

public Importo4CifreFormat() {
	super();
	setPrecision(4);
}

public java.text.Format getFormat() {
	return format;
}
}