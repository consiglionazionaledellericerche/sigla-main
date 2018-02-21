package it.cnr.contab.utente00.nav.comp;

/**
 * Eccezione applicativa generata da GestioneLoginComponent per notificare
 * che la richiesta di Login di un utente è fallita perchè l'utente non ha 
 * nessuna utenza/profilo in SIGLA.
 */
public class UtenteLdapNonUtenteSiglaException extends it.cnr.jada.comp.ApplicationException {
/**
 * UtenteLdapException constructor comment.
 */
public UtenteLdapNonUtenteSiglaException() {
	super();
}
/**
 * UtenteLdapException constructor comment.
 * @param s java.lang.String
 */
public UtenteLdapNonUtenteSiglaException(String s) {
	super(s);
}
/**
 * UtenteLdapException constructor comment.
 * @param s java.lang.String
 * @param detail java.lang.Throwable
 */
public UtenteLdapNonUtenteSiglaException(String s, Throwable detail) {
	super(s, detail);
}
/**
 * UtenteLdapException constructor comment.
 * @param detail java.lang.Throwable
 */
public UtenteLdapNonUtenteSiglaException(Throwable detail) {
	super(detail);
}
}
