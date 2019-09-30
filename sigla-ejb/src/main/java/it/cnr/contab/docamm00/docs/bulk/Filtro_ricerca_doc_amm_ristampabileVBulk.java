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

package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.OrderedHashtable;

/**
 * Insert the type's description here.
 * Creation date: (6/17/2002 12:41:01 PM)
 * @author: Roberto Peli
 */
public class Filtro_ricerca_doc_amm_ristampabileVBulk extends Filtro_ricerca_doc_ammVBulk {

	private CompoundFindClause sqlClauses = null;
	private Long pgStampa = null;

	private Tipo_sezionaleBulk tipo_sezionale = null;
	private java.util.Collection sezionali = null;
	
	public static final String CLAUSE_PROTOCOLLI = "Numeri protocollo";
	public static final String CLAUSE_PROTOCOLLI_GENERALE = "Numeri protocollo generale";
	public static final String CLAUSE_DATE = "Date";

	public static final java.util.Dictionary CLAUSES;

	static {
		
		CLAUSES = new OrderedHashtable();
		CLAUSES.put(CLAUSE_PROTOCOLLI,CLAUSE_PROTOCOLLI);
		CLAUSES.put(CLAUSE_PROTOCOLLI_GENERALE,CLAUSE_PROTOCOLLI_GENERALE);
		CLAUSES.put(CLAUSE_DATE, CLAUSE_DATE);
	}

	private java.sql.Timestamp dt_da_stampa = null;
	private java.sql.Timestamp dt_a_stampa = null;

	private Long da_protocollo_iva = null;
	private Long a_protocollo_iva = null;

	private Long da_protocollo_iva_generale = null;
	private Long a_protocollo_iva_generale = null;

	private String clause = null;
/**
 * Filtro_ricerca_doc_amm_protocollabileVBulk constructor comment.
 */
public Filtro_ricerca_doc_amm_ristampabileVBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (11/26/2002 3:40:40 PM)
 * @return java.lang.Long
 */
public java.lang.Long getA_protocollo_iva() {
	return a_protocollo_iva;
}
/**
 * Insert the method's description here.
 * Creation date: (12/3/2002 10:09:09 AM)
 * @return java.lang.Long
 */
public java.lang.Long getA_protocollo_iva_generale() {
	return a_protocollo_iva_generale;
}
/**
 * Insert the method's description here.
 * Creation date: (11/26/2002 3:40:40 PM)
 * @return java.lang.String
 */
public java.lang.String getClause() {
	return clause;
}
public java.util.Dictionary getClauses() {
	return CLAUSES;
}
/**
 * Insert the method's description here.
 * Creation date: (11/26/2002 3:40:40 PM)
 * @return java.lang.Long
 */
public java.lang.Long getDa_protocollo_iva() {
	return da_protocollo_iva;
}
/**
 * Insert the method's description here.
 * Creation date: (12/3/2002 10:09:09 AM)
 * @return java.lang.Long
 */
public java.lang.Long getDa_protocollo_iva_generale() {
	return da_protocollo_iva_generale;
}
/**
 * Insert the method's description here.
 * Creation date: (11/26/2002 3:40:40 PM)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDt_a_stampa() {
	return dt_a_stampa;
}
/**
 * Insert the method's description here.
 * Creation date: (11/26/2002 3:40:40 PM)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDt_da_stampa() {
	return dt_da_stampa;
}
/**
 * Insert the method's description here.
 * Creation date: (3/12/2002 10:20:26 AM)
 * @return java.util.Collection
 */
public java.util.Dictionary getGroups() {

	OrderedHashtable gruppi = new OrderedHashtable();
	gruppi.put(DOC_ATT_GRUOP,DOC_ATT_GRUOP);
	return gruppi;
}
/**
 * Insert the method's description here.
 * Creation date: (12/2/2002 4:18:52 PM)
 * @return java.lang.Long
 */
public java.lang.Long getPgStampa() {
	return pgStampa;
}
/**
 * Insert the method's description here.
 * Creation date: (12/3/2002 10:09:09 AM)
 * @return java.util.Collection
 */
public java.util.Collection getSezionali() {
	return sezionali;
}
/**
 * Insert the method's description here.
 * Creation date: (12/2/2002 4:18:52 PM)
 * @return it.cnr.jada.persistency.sql.CompoundFindClause
 */
public it.cnr.jada.persistency.sql.CompoundFindClause getSQLClauses() {
	return sqlClauses;
}
/**
 * Insert the method's description here.
 * Creation date: (12/3/2002 10:09:09 AM)
 * @return it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk
 */
public it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk getTipo_sezionale() {
	return tipo_sezionale;
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:18:42 AM)
 * @param newFl_fornitore java.lang.Boolean
 */
public Filtro_ricerca_doc_ammVBulk initializeForSearch(
	it.cnr.contab.docamm00.bp.ListaDocumentiAmministrativiBP bp,
	it.cnr.jada.action.ActionContext context) {

	super.initializeForSearch(bp, context);

	setClause(CLAUSE_PROTOCOLLI);
	
	return this;
}
/**
 * Insert the method's description here.
 * Creation date: (3/12/2002 10:20:26 AM)
 * @return java.util.Collection
 */
public boolean isProtocolliClause() {

	return CLAUSE_PROTOCOLLI.equalsIgnoreCase(getClause());
}
/**
 * Insert the method's description here.
 * Creation date: (3/12/2002 10:20:26 AM)
 * @return java.util.Collection
 */
public boolean isProtocolliGeneraleClause() {

	return CLAUSE_PROTOCOLLI_GENERALE.equalsIgnoreCase(getClause());
}
/**
 * Insert the method's description here.
 * Creation date: (3/12/2002 10:20:26 AM)
 * @return java.util.Collection
 */
public boolean isRODate() {

	return isProtocolliClause() || isProtocolliGeneraleClause();
}
/**
 * Insert the method's description here.
 * Creation date: (3/12/2002 10:20:26 AM)
 * @return java.util.Collection
 */
public boolean isROProtocolli() {

	return !isProtocolliClause();
}
/**
 * Insert the method's description here.
 * Creation date: (3/12/2002 10:20:26 AM)
 * @return java.util.Collection
 */
public boolean isROProtocolliGenerale() {

	return !isProtocolliGeneraleClause();
}
/**
 * Insert the method's description here.
 * Creation date: (3/12/2002 10:20:26 AM)
 * @return java.util.Collection
 */
public boolean isROTipo_sezionale() {

	return !isRODate();
}
/**
 * Insert the method's description here.
 * Creation date: (11/26/2002 3:40:40 PM)
 * @param newA_protocollo_iva java.lang.Long
 */
public void setA_protocollo_iva(java.lang.Long newA_protocollo_iva) {
	a_protocollo_iva = newA_protocollo_iva;
}
/**
 * Insert the method's description here.
 * Creation date: (12/3/2002 10:09:09 AM)
 * @param newA_protocollo_iva_generale java.lang.Long
 */
public void setA_protocollo_iva_generale(java.lang.Long newA_protocollo_iva_generale) {
	a_protocollo_iva_generale = newA_protocollo_iva_generale;
}
/**
 * Insert the method's description here.
 * Creation date: (11/26/2002 3:40:40 PM)
 * @param newClause java.lang.String
 */
public void setClause(java.lang.String newClause) {
	clause = newClause;
}
/**
 * Insert the method's description here.
 * Creation date: (11/26/2002 3:40:40 PM)
 * @param newDa_protocollo_iva java.lang.Long
 */
public void setDa_protocollo_iva(java.lang.Long newDa_protocollo_iva) {
	da_protocollo_iva = newDa_protocollo_iva;
}
/**
 * Insert the method's description here.
 * Creation date: (12/3/2002 10:09:09 AM)
 * @param newDa_protocollo_iva_generale java.lang.Long
 */
public void setDa_protocollo_iva_generale(java.lang.Long newDa_protocollo_iva_generale) {
	da_protocollo_iva_generale = newDa_protocollo_iva_generale;
}
/**
 * Insert the method's description here.
 * Creation date: (11/26/2002 3:40:40 PM)
 * @param newDt_a_stampa java.sql.Timestamp
 */
public void setDt_a_stampa(java.sql.Timestamp newDt_a_stampa) {
	dt_a_stampa = newDt_a_stampa;
}
/**
 * Insert the method's description here.
 * Creation date: (11/26/2002 3:40:40 PM)
 * @param newDt_da_stampa java.sql.Timestamp
 */
public void setDt_da_stampa(java.sql.Timestamp newDt_da_stampa) {
	dt_da_stampa = newDt_da_stampa;
}
/**
 * Insert the method's description here.
 * Creation date: (12/2/2002 4:18:52 PM)
 * @param newPgStampa java.lang.Long
 */
public void setPgStampa(java.lang.Long newPgStampa) {
	pgStampa = newPgStampa;
}
/**
 * Insert the method's description here.
 * Creation date: (12/3/2002 10:09:09 AM)
 * @param newSezionali java.util.Collection
 */
public void setSezionali(java.util.Collection newSezionali) {
	sezionali = newSezionali;
}
/**
 * Insert the method's description here.
 * Creation date: (12/2/2002 4:18:52 PM)
 * @param newClauses it.cnr.jada.persistency.sql.CompoundFindClause
 */
public void setSQLClauses(it.cnr.jada.persistency.sql.CompoundFindClause newClauses) {
	sqlClauses = newClauses;
}
/**
 * Insert the method's description here.
 * Creation date: (12/3/2002 10:09:09 AM)
 * @param newSezionale it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk
 */
public void setTipo_sezionale(it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk newSezionale) {
	tipo_sezionale = newSezionale;
}
/**
 * Insert the method's description here.
 * Creation date: (3/25/2002 3:04:27 PM)
 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public void validateClauses() 
	throws it.cnr.jada.bulk.ValidationException {

	if (isProtocolliClause()) {
		if (getTipo_sezionale() == null)
			throw new it.cnr.jada.bulk.ValidationException("Specificare un sezionale!");
		if (getDa_protocollo_iva() == null ||
			getA_protocollo_iva() == null)
			throw new it.cnr.jada.bulk.ValidationException("Specificare sia il numero minimo sia il numero massimo di protocollo valido!");
		if (getDa_protocollo_iva().longValue() > getA_protocollo_iva().longValue())
			throw new it.cnr.jada.bulk.ValidationException("Specificare un intervallo di numerazione valido per i protocolli!");
	} else if (isProtocolliGeneraleClause()) {
		if (getTipo_sezionale() == null)
			throw new it.cnr.jada.bulk.ValidationException("Specificare un sezionale!");
		if (getDa_protocollo_iva_generale() == null ||
			getA_protocollo_iva_generale() == null)
			throw new it.cnr.jada.bulk.ValidationException("Specificare sia il numero minimo sia il numero massimo di protocollo generale valido!");
		if (getDa_protocollo_iva_generale().longValue() > getA_protocollo_iva_generale().longValue())
			throw new it.cnr.jada.bulk.ValidationException("Specificare un intervallo di numerazione valido per i protocolli generali!");

	} else {
		if (getDt_da_stampa() == null ||
			getDt_a_stampa() == null)
			throw new it.cnr.jada.bulk.ValidationException("Specificare sia la data di inizio stampa sia la data di fine stampa!");
		if (getDt_da_stampa().after(getDt_a_stampa()))
			throw new it.cnr.jada.bulk.ValidationException("Specificare un intervallo di tempo valido!");
	}	
		
}
}
