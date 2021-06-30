/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
	public static final String LOGIN = "LOGIN";
	public static final String CD_CDS = "cd_cds";
	public static final String CD_UNITA_ORGANIZZATIVA = "cd_unita_organizzativa";
	public static final String CD_CDR = "cd_cdr";
	public static final String ESERCIZIO = "esercizio";
	public static final String SESSION_ID = "sessionId";
	public static final String HIDDEN_COLUMNS = "hiddenColumns";
	public static final String USER = "user";
	public static final String TRANSACTIONAL = "transactional";
	public static final String BOOTSTRAP = "bootstrap";
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
		attributes.put(USER,
				Optional.ofNullable(user).orElse(LOGIN));
		Optional.ofNullable(esercizio)
				.ifPresent(integer -> attributes.put(ESERCIZIO, integer));
		Optional.ofNullable(cd_cds)
				.ifPresent(s -> attributes.put(CD_CDS, s));
		Optional.ofNullable(cd_unita_organizzativa)
				.ifPresent(s -> attributes.put(CD_UNITA_ORGANIZZATIVA, s));
		Optional.ofNullable(cd_cdr)
				.ifPresent(s -> attributes.put(CD_CDR, s));
		Optional.ofNullable(sessionId)
				.ifPresent(s -> attributes.put(SESSION_ID, s));

		attributes.put(HIDDEN_COLUMNS, new Hashtable<Object, Object>());
	}

	/**
	 * <!-- @TODO: da completare --> Restituisce il valore della propriet?
	 * 'cd_cds'
	 * 
	 * @return Il valore della propriet? 'cd_cds'
	 */
	public java.lang.String getCd_cds() {
		return (String) attributes.get(CD_CDS);
	}

	/**
	 * Restituisce il codice del cds
	 * 
	 * @param userContext
	 * @return codice del cds
	 */
	public static String getCd_cds(it.cnr.jada.UserContext userContext) {
		return Optional.ofNullable(userContext)
				.flatMap(userContext1 -> Optional.ofNullable(userContext1.getAttributes()))
				.map(attr -> attr.get(CD_CDS))
				.filter(String.class::isInstance)
				.map(String.class::cast)
				.orElseGet(() -> null);
	}

	/**
	 * <!-- @TODO: da completare --> Restituisce il valore della propriet?
	 * 'cd_unita_organizzativa'
	 * 
	 * @return Il valore della propriet? 'cd_unita_organizzativa'
	 */
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
		attributes.put(CD_UNITA_ORGANIZZATIVA, cd_unita_organizzativa);
	}
	
	public java.lang.String getCd_unita_organizzativa() {
		return (String) attributes.get(CD_UNITA_ORGANIZZATIVA);
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
		return Optional.ofNullable(userContext)
				.flatMap(userContext1 -> Optional.ofNullable(userContext1.getAttributes()))
				.map(attr -> attr.get(CD_UNITA_ORGANIZZATIVA))
				.filter(String.class::isInstance)
				.map(String.class::cast)
				.orElseGet(() -> null);
	}

	/**
	 * <!-- @TODO: da completare --> Restituisce il valore della propriet?
	 * 'esercizio'
	 * 
	 * @return Il valore della propriet? 'esercizio'
	 */
	public void setEsercizio(java.lang.Integer esercizio) {
		attributes.put(ESERCIZIO, esercizio);
	}
	
	public java.lang.Integer getEsercizio() {
		return Optional.ofNullable(attributes)
				.map(attr -> attr.get(ESERCIZIO))
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
                .map(attr -> attr.get(ESERCIZIO))
                .filter(Integer.class::isInstance)
                .map(Integer.class::cast)
                .orElseGet(() -> LocalDate.now().getYear());
	}

	public final java.lang.String getSessionId() {
		return (String) attributes.get(SESSION_ID);
	}

	public void setUser(java.lang.String user) {
		attributes.put(USER, user);
	}

	public final java.lang.String getUser() {
		return (String) attributes.get(USER);
	}

	/**
	 * Restituisce il codice dell'utente
	 * 
	 * @param userContext
	 * @return codice dell'Utente
	 */
	public static String getUser(it.cnr.jada.UserContext userContext) {
		return (String) userContext.getAttributes().get(USER);
	}

	public boolean isTransactional() {
		Boolean result = (Boolean) attributes.get(TRANSACTIONAL);
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
		attributes.put(CD_CDS, newCd_cds);
	}

	public void setTransactional(boolean newTransactional) {
		attributes.put(TRANSACTIONAL, newTransactional);
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
		return (Dictionary<Object, Object>) attributes.get(HIDDEN_COLUMNS);
	}

	public void setCd_cdr(java.lang.String cd_cdr) {
		attributes.put(CD_CDR, cd_cdr);
	}

	public java.lang.String getCd_cdr() {
		return (String) attributes.get(CD_CDR);
	}

	public static String getCd_cdr(it.cnr.jada.UserContext userContext) {
		return (String) userContext.getAttributes().get(CD_CDR);
	}

	public Hashtable<String, Serializable> getAttributes() {
		return attributes;
	}
	
	@Override
	public String getName() {
		return getUser();
	}

	public boolean isFromBootstrap() {
		return (boolean) this.getAttributes().getOrDefault(BOOTSTRAP, false);
	}

	public static boolean isFromBootstrap(it.cnr.jada.UserContext userContext) {
		return (boolean) ((CNRUserContext)userContext).isFromBootstrap();
	}
	
	@Override
	public String toString() {
		return "CNRUserContext [attributes=" + attributes + "]";
	}	
}
