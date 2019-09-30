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

import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.OrderedHashtable;

/**
 * Insert the type's description here.
 * Creation date: (6/17/2002 12:41:01 PM)
 * @author: Roberto Peli
 */
public class Filtro_ricerca_doc_amm_protocollabileVBulk extends Filtro_ricerca_doc_ammVBulk {

	private java.sql.Timestamp dt_stampa = null;
	private Long pgProtocollazioneIVA = null;
	private Long pgStampa = null;
	private CompoundFindClause sqlClauses = null;
/**
 * Filtro_ricerca_doc_amm_protocollabileVBulk constructor comment.
 */
public Filtro_ricerca_doc_amm_protocollabileVBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (7/16/2002 11:16:55 AM)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDt_stampa() {
	return dt_stampa;
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
 * Creation date: (12/2/2002 11:41:08 AM)
 * @return java.lang.Long
 */
public java.lang.Long getPgProtocollazioneIVA() {
	return pgProtocollazioneIVA;
}
/**
 * Insert the method's description here.
 * Creation date: (12/2/2002 11:41:08 AM)
 * @return java.lang.Long
 */
public java.lang.Long getPgStampa() {
	return pgStampa;
}
/**
 * Insert the method's description here.
 * Creation date: (12/2/2002 11:41:25 AM)
 * @return CompoundFindClause
 */
public CompoundFindClause getSQLClauses() {
	return sqlClauses;
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:18:42 AM)
 * @param newFl_fornitore java.lang.Boolean
 */
 
private void initDataStampa(
	it.cnr.contab.docamm00.bp.ListaDocumentiAmministrativiBP bp,
	it.cnr.jada.action.ActionContext context) {

	java.sql.Timestamp dataOdierna = getDataOdierna();
	java.util.Calendar gc = java.util.Calendar.getInstance();
	gc.setTime(dataOdierna);
	int anno = gc.get(java.util.Calendar.YEAR);
	int esScrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).intValue();
	if (anno != esScrivania) {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
		try {
			setDt_stampa(getDataOdierna(new java.sql.Timestamp(sdf.parse("31/12/" + esScrivania).getTime())));
		} catch (java.text.ParseException e) {
			
		}
	} else
		setDt_stampa(dataOdierna);
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

	initDataStampa(bp, context);
	
	return this;
}
/**
 * Insert the method's description here.
 * Creation date: (7/16/2002 11:16:55 AM)
 * @param newDt_stampa java.sql.Timestamp
 */
public void setDt_stampa(java.sql.Timestamp newDt_stampa) {
	dt_stampa = newDt_stampa;
}
/**
 * Insert the method's description here.
 * Creation date: (12/2/2002 11:41:08 AM)
 * @param newPgProtocollazioneIVA java.lang.Long
 */
public void setPgProtocollazioneIVA(java.lang.Long newPgProtocollazioneIVA) {
	pgProtocollazioneIVA = newPgProtocollazioneIVA;
}
/**
 * Insert the method's description here.
 * Creation date: (12/2/2002 11:41:08 AM)
 * @param newPgStampa java.lang.Long
 */
public void setPgStampa(java.lang.Long newPgStampa) {
	pgStampa = newPgStampa;
}
/**
 * Insert the method's description here.
 * Creation date: (12/2/2002 11:41:50 AM)
 * @param newClauses CompoundFindClause
 */
public void setSQLClauses(CompoundFindClause newClauses) {
	sqlClauses = newClauses;
}
}
