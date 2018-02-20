package it.cnr.contab.utente00.nav.comp;

/**
 * Eccezione applicativa generata da GestioneLoginComponent per notificare
 * che la richiesta di Login di un utente è fallita a causa della scadenza
 * trimestrale della password.
 */
public class PasswordLdapScadutaException extends it.cnr.jada.comp.ApplicationException {
/**
 * PasswordScadutaException constructor comment.
 */
public PasswordLdapScadutaException() {
	super();
}
/**
 * PasswordLdapScadutaException constructor comment.
 * @param s java.lang.String
 */
public PasswordLdapScadutaException(String s) {
	super(s);
}
/**
 * PasswordLdapScadutaException constructor comment.
 * @param s java.lang.String
 * @param detail java.lang.Throwable
 */
public PasswordLdapScadutaException(String s, Throwable detail) {
	super(s, detail);
}
/**
 * PasswordLdapScadutaException constructor comment.
 * @param detail java.lang.Throwable
 */
public PasswordLdapScadutaException(Throwable detail) {
	super(detail);
}
}
