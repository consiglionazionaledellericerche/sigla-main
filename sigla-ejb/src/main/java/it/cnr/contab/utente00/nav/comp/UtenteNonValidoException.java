package it.cnr.contab.utente00.nav.comp;

/**
 * Eccezione applicativa generata da GestioneLoginComponent per notificare
 * che la richiesta di Login di un utente è fallita perchè l'utente non è
 * più (o ancora) valido.
 */
public class UtenteNonValidoException extends it.cnr.jada.comp.ApplicationException {
/**
 * UtenteNonValidoException constructor comment.
 */
public UtenteNonValidoException() {
	super();
}
/**
 * UtenteNonValidoException constructor comment.
 * @param s java.lang.String
 */
public UtenteNonValidoException(String s) {
	super(s);
}
/**
 * UtenteNonValidoException constructor comment.
 * @param s java.lang.String
 * @param detail java.lang.Throwable
 */
public UtenteNonValidoException(String s, Throwable detail) {
	super(s, detail);
}
/**
 * UtenteNonValidoException constructor comment.
 * @param detail java.lang.Throwable
 */
public UtenteNonValidoException(Throwable detail) {
	super(detail);
}
}
