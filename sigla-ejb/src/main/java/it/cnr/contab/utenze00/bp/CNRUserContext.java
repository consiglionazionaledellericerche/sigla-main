package it.cnr.contab.utenze00.bp;

import java.io.Serializable;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Optional;

/**
 * Classe che definisce il conesto UTENTE in relazione alla configurazione di
 * sessione e di scrivania corrente
 */

public class CNRUserContext implements it.cnr.jada.UserContext, Principal {
	private static final long serialVersionUID = 1L;
	private Hashtable<String, Serializable> attributes;
	
	public CNRUserContext() {
		super();
		attributes = new Hashtable<String, Serializable>(7);
	}
	/**
	 * Costruttore dello User Context
	 * 
	 * @param user
	 *            codice utente
	 * @param sessionId
	 *            identificativo della sessione
	 * @param esercizio
	 *            esercizio di scrivania
	 * @param cd_unita_organizzativa
	 *            codice dell'UO di scrivania
	 * @param cd_cds
	 *            codice del cds di scrivania
	 */
	public CNRUserContext(String user, String sessionId, Integer esercizio,
			String cd_unita_organizzativa, String cd_cds, String cd_cdr) {
		attributes = new Hashtable<String, Serializable>(7);
		attributes.put("user", user);
		if (esercizio != null)
			attributes.put("esercizio", esercizio);
		if (cd_unita_organizzativa != null)
			attributes.put("cd_unita_organizzativa", cd_unita_organizzativa);
		if (sessionId != null)		
			attributes.put("sessionId", sessionId);
		if (cd_cds != null)		
			attributes.put("cd_cds", cd_cds);
		if (cd_cdr != null)				
			attributes.put("cd_cdr", cd_cdr);
		attributes.put("hiddenColumns", new Hashtable<Object, Object>());
	}

	/**
	 * <!-- @TODO: da completare --> Restituisce il valore della propriet?
	 * 'cd_cds'
	 * 
	 * @return Il valore della propriet? 'cd_cds'
	 */
	public java.lang.String getCd_cds() {
		return (String) attributes.get("cd_cds");
	}

	/**
	 * Restituisce il codice del cds
	 * 
	 * @param userContext
	 * @return codice del cds
	 */
	public static String getCd_cds(it.cnr.jada.UserContext userContext) {
		return (String) userContext.getAttributes().get("cd_cds");
	}

	/**
	 * <!-- @TODO: da completare --> Restituisce il valore della propriet?
	 * 'cd_unita_organizzativa'
	 * 
	 * @return Il valore della propriet? 'cd_unita_organizzativa'
	 */
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
		attributes.put("cd_unita_organizzativa", cd_unita_organizzativa);
	}
	
	public java.lang.String getCd_unita_organizzativa() {
		return (String) attributes.get("cd_unita_organizzativa");
	}

	/**
	 * Restituisce il codice dell'UO
	 * 
	 * 
	 * @param userContext
	 * @return codice dell'UO
	 */
	public static String getCd_unita_organizzativa(
			it.cnr.jada.UserContext userContext) {
		return (String) userContext.getAttributes().get("cd_unita_organizzativa");
	}

	/**
	 * <!-- @TODO: da completare --> Restituisce il valore della propriet?
	 * 'esercizio'
	 * 
	 * @return Il valore della propriet? 'esercizio'
	 */
	public void setEsercizio(java.lang.Integer esercizio) {
		attributes.put("esercizio", esercizio);
	}
	
	public java.lang.Integer getEsercizio() {
		return Optional.ofNullable(attributes)
				.map(attr -> attr.get("esercizio"))
				.filter(Integer.class::isInstance)
				.map(Integer.class::cast)
				.orElseGet(() -> LocalDate.now().getYear());
	}

	/**
	 * Restituisce il codice dell'esercizio
	 * 
	 * 
	 * @param userContext
	 * @return codice dell'esercizio
	 */
	public static java.lang.Integer getEsercizio(
			it.cnr.jada.UserContext userContext) {
        return Optional.ofNullable(userContext)
                .flatMap(userContext1 -> Optional.ofNullable(userContext1.getAttributes()))
                .map(attr -> attr.get("esercizio"))
                .filter(Integer.class::isInstance)
                .map(Integer.class::cast)
                .orElseGet(() -> LocalDate.now().getYear());
	}

	public final java.lang.String getSessionId() {
		return (String) attributes.get("sessionId");
	}

	public void setUser(java.lang.String user) {
		attributes.put("user", user);
	}

	public final java.lang.String getUser() {
		return (String) attributes.get("user");
	}

	/**
	 * Restituisce il codice dell'utente
	 * 
	 * @param userContext
	 * @return codice dell'Utente
	 */
	public static String getUser(it.cnr.jada.UserContext userContext) {
		return (String) userContext.getAttributes().get("user");
	}

	public boolean isTransactional() {
		Boolean result = (Boolean) attributes.get("transactional");
		if (result == null)
			return false;
		return result;
	}

	/**
	 * <!-- @TODO: da completare --> Imposta il valore della propriet? 'cd_cds'
	 * 
	 * @param newCd_cds
	 *            Il valore da assegnare a 'cd_cds'
	 */
	public void setCd_cds(java.lang.String newCd_cds) {
		attributes.put("cd_cds", newCd_cds);
	}

	public void setTransactional(boolean newTransactional) {
		attributes.put("transactional", newTransactional);
	}

	/**
	 * writeTo method comment.
	 */
	public void writeTo(java.io.PrintWriter writer) {
		writer.print("USER: ");
		writer.print(getUser());
	}

	@SuppressWarnings("unchecked")
	public Dictionary<Object, Object> getHiddenColumns() {
		return (Dictionary<Object, Object>) attributes.get("hiddenColumns");
	}

	public void setCd_cdr(java.lang.String cd_cdr) {
		attributes.put("cd_cdr", cd_cdr);
	}

	public java.lang.String getCd_cdr() {
		return (String) attributes.get("cd_cdr");
	}

	public static String getCd_cdr(it.cnr.jada.UserContext userContext) {
		return (String) userContext.getAttributes().get("cd_cdr");
	}

	public Hashtable<String, Serializable> getAttributes() {
		return attributes;
	}
	
	@Override
	public String getName() {
		return getUser();
	}

	public boolean isFromBootstrap() {
		return (boolean) this.getAttributes().getOrDefault("bootstrap", false);		
	}

	public static boolean isFromBootstrap(it.cnr.jada.UserContext userContext) {
		return (boolean) ((CNRUserContext)userContext).isFromBootstrap();
	}
	
	@Override
	public String toString() {
		return "CNRUserContext [attributes=" + attributes + "]";
	}	
}
