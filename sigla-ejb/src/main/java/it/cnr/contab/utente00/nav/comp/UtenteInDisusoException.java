package it.cnr.contab.utente00.nav.comp;

/**
 * Eccezione applicativa generata da GestioneLoginComponent per notificare
 * che la richiesta di Login di un utente è fallita perchè l'utente non è
 * utilizzato da più di n mesi.
 */
public class UtenteInDisusoException extends it.cnr.jada.comp.ApplicationException {
/**
 * UtenteInDisusoException constructor comment.
 */
public UtenteInDisusoException() {
	super();
}
/**
 * UtenteInDisusoException constructor comment.
 * @param s java.lang.String
 */
public UtenteInDisusoException(String s) {
	super(s);
}
/**
 * UtenteInDisusoException constructor comment.
 * @param s java.lang.String
 * @param detail java.lang.Throwable
 */
public UtenteInDisusoException(String s, Throwable detail) {
	super(s, detail);
}
/**
 * UtenteInDisusoException constructor comment.
 * @param detail java.lang.Throwable
 */
public UtenteInDisusoException(Throwable detail) {
	super(detail);
}
}
