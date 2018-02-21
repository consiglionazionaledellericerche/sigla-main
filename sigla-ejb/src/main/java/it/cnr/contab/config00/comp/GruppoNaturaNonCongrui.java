package it.cnr.contab.config00.comp;
/**
 * Eccezione speciale per non congruità tra Natura e Gruppo conto economico
 */
public class GruppoNaturaNonCongrui extends Exception {
/**
 * GruppoNaturaNonCongrui constructor comment.
 */
public GruppoNaturaNonCongrui() {
	super();
}

/**
 * GruppoNaturaNonCongrui constructor comment.
 * @param s java.lang.String
 */
public GruppoNaturaNonCongrui(String s) {
	super(s);
}
}