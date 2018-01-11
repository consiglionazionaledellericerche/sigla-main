package it.cnr.contab.util;
/**
 * Formattatore di importi a 4 cifre decimali
 */

public class Importo5CifreFormat extends GenericImportoFormat {
	public final static java.text.Format format = new java.text.DecimalFormat("#,##0.00000");

public Importo5CifreFormat() {
	super();
	setPrecision(5);
}

public java.text.Format getFormat() {
	return format;
}
}