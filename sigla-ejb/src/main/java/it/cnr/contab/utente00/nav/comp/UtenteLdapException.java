package it.cnr.contab.utente00.nav.comp;

/**
 * Eccezione applicativa generata da GestioneLoginComponent per notificare
 * che la richiesta di Login di un utente è fallita perchè l'utente è stato
 * già collegato ad una utenza LDAP.
 */
public class UtenteLdapException extends it.cnr.jada.comp.ApplicationException {
/**
 * UtenteLdapException constructor comment.
 */
public UtenteLdapException() {
	super();
}
/**
 * UtenteLdapException constructor comment.
 * @param s java.lang.String
 */
public UtenteLdapException(String s) {
	super(s);
}
/**
 * UtenteLdapException constructor comment.
 * @param s java.lang.String
 * @param detail java.lang.Throwable
 */
public UtenteLdapException(String s, Throwable detail) {
	super(s, detail);
}
/**
 * UtenteLdapException constructor comment.
 * @param detail java.lang.Throwable
 */
public UtenteLdapException(Throwable detail) {
	super(detail);
}
}
