package it.cnr.contab.utente00.nav.comp;

/**
 * Eccezione applicativa generata da GestioneLoginComponent per notificare
 * che la richiesta di Login di un utente è fallita a causa della scadenza
 * trimestrale della password.
 */
public class PasswordScadutaException extends it.cnr.jada.comp.ApplicationException {
/**
 * PasswordScadutaException constructor comment.
 */
public PasswordScadutaException() {
	super();
}
/**
 * PasswordScadutaException constructor comment.
 * @param s java.lang.String
 */
public PasswordScadutaException(String s) {
	super(s);
}
/**
 * PasswordScadutaException constructor comment.
 * @param s java.lang.String
 * @param detail java.lang.Throwable
 */
public PasswordScadutaException(String s, Throwable detail) {
	super(s, detail);
}
/**
 * PasswordScadutaException constructor comment.
 * @param detail java.lang.Throwable
 */
public PasswordScadutaException(Throwable detail) {
	super(detail);
}
}
