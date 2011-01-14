package it.cnr.contab.anagraf00.util;

/**
 * Eccezione rilevata al sistema programmatico che il codice terzo ricercato non esiste
 */

public class TerzoNonPresenteSIPException extends it.cnr.jada.comp.ComponentException {
	public TerzoNonPresenteSIPException() {
		super();
	}
	public TerzoNonPresenteSIPException(String s) {
		super(s);
	}
}