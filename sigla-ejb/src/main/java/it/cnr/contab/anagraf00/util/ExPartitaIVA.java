package it.cnr.contab.anagraf00.util;

/**
 * Eccezione che notifica l'inesattezza della partita I.V.A. inserita.
 */

public class ExPartitaIVA extends Exception {
	public ExPartitaIVA(String s) {
	super(s);
}
}
