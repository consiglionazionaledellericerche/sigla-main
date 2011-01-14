package it.cnr.contab.fondecon00.comp;

public class ErroreSquadraturaFondo extends it.cnr.jada.comp.ComponentException {

	private it.cnr.jada.bulk.OggettoBulk sospeso;

/**
 * ErroreDiSquadratura constructor comment.
 */
public ErroreSquadraturaFondo() {
	super();
}
/**
 * ErroreDiSquadratura constructor comment.
 * @param s java.lang.String
 */
public ErroreSquadraturaFondo(String s) {
	super(s);
}
/**
 * ErroreDiSquadratura constructor comment.
 * @param s java.lang.String
 */
public ErroreSquadraturaFondo(String s, it.cnr.jada.bulk.OggettoBulk o) {
	super(s);
	setSospeso(o);
}
/**
 * Insert the method's description here.
 * Creation date: (19/03/2002 17.13.59)
 * @return it.cnr.jada.bulk.OggettoBulk
 */
public it.cnr.jada.bulk.OggettoBulk getSospeso() {
	return sospeso;
}
/**
 * Insert the method's description here.
 * Creation date: (19/03/2002 17.13.59)
 * @param newSospeso it.cnr.jada.bulk.OggettoBulk
 */
public void setSospeso(it.cnr.jada.bulk.OggettoBulk newSospeso) {
	sospeso = newSospeso;
}
}
