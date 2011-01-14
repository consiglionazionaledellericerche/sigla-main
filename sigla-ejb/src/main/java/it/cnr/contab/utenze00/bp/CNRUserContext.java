package it.cnr.contab.utenze00.bp;

import it.cnr.jada.util.OrderedHashtable;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Classe che definisce il conesto UTENTE in relazione alla configurazione di sessione e di scrivania corrente
 */

public class CNRUserContext implements it.cnr.jada.UserContext {
	private java.lang.Integer esercizio;
	private java.lang.String cd_unita_organizzativa;
	private final java.lang.String user;
	private final java.lang.String sessionId;
	private java.lang.String cd_cds;
	private java.lang.String cd_cdr;
	private boolean transactional;
	private Dictionary hiddenColumns = new Hashtable();

/**
 * Costruttore dello User Context
 *
 * @param user codice utente
 * @param sessionId	identificativo della sessione
 * @param esercizio	esercizio di scrivania
 * @param cd_unita_organizzativa codice dell'UO di scrivania
 * @param cd_cds codice del cds di scrivania
 */
public CNRUserContext(String user,String sessionId,Integer esercizio,String cd_unita_organizzativa,String cd_cds, String cd_cdr) {
	this.user = user;
	this.esercizio = esercizio;
	this.cd_unita_organizzativa = cd_unita_organizzativa;
	this.sessionId = sessionId;
	this.cd_cds = cd_cds;
	this.cd_cdr = cd_cdr;
}

/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della propriet? 'cd_cds'
 *
 * @return Il valore della propriet? 'cd_cds'
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}

/**
 * Restituisce il codice del cds
 *
 * @param userContext	
 * @return codice del cds
 */
public static String getCd_cds(it.cnr.jada.UserContext userContext) {
	return ((CNRUserContext)userContext).getCd_cds();
}

/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della propriet? 'cd_unita_organizzativa'
 *
 * @return Il valore della propriet? 'cd_unita_organizzativa'
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}

/**
 * Restituisce il codice dell'UO
 * 
 *
 * @param userContext	
 * @return codice dell'UO
 */
public static String getCd_unita_organizzativa(it.cnr.jada.UserContext userContext) {
	return ((CNRUserContext)userContext).getCd_unita_organizzativa();
}

/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della propriet? 'esercizio'
 *
 * @return Il valore della propriet? 'esercizio'
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}

/**
 * Restituisce il codice dell'esercizio
 * 
 *
 * @param userContext	
 * @return codice dell'esercizio
 */
public static java.lang.Integer getEsercizio(it.cnr.jada.UserContext userContext) {
	return ((CNRUserContext)userContext).getEsercizio();
}

public final java.lang.String getSessionId() {
	return sessionId;
}

public final java.lang.String getUser() {
	return user;
}

/**
 * Restituisce il codice dell'utente
 * 
 * @param userContext	
 * @return codice dell'Utente
 */
public static String getUser(it.cnr.jada.UserContext userContext) {
	return ((CNRUserContext)userContext).getUser();
}

public boolean isTransactional() {
	return transactional;
}

/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della propriet? 'cd_cds'
 *
 * @param newCd_cds	Il valore da assegnare a 'cd_cds'
 */
public void setCd_cds(java.lang.String newCd_cds) {
	cd_cds = newCd_cds;
}

public void setTransactional(boolean newTransactional) {
	transactional = newTransactional;
}

/**
 * writeTo method comment.
 */
public void writeTo(java.io.PrintWriter writer) {
	writer.print("USER: ");
	writer.print(user);
}

public Dictionary getHiddenColumns() {
	return hiddenColumns;
}

public java.lang.String getCd_cdr() {
	return cd_cdr;
}
public static String getCd_cdr(it.cnr.jada.UserContext userContext) {
	return ((CNRUserContext)userContext).getCd_cdr();
}

}
