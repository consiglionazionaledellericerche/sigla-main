package it.cnr.contab.anagraf00.util;

/**
 * Eccezione che notifica l'inesattezza del codice fiscale inserito.
 */

public class ExCodiceFiscale extends it.cnr.jada.comp.ComponentException {
	public ExCodiceFiscale(String s) {
	super(s);
}
}
