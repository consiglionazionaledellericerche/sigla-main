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

import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.OrderedHashtable;

import java.util.Dictionary;

/**
 * Insert the type's description here.
 * Creation date: (6/17/2002 12:41:01 PM)
 * @author: Roberto Peli
 */
public class Filtro_ricerca_doc_amm_fatturazione_elettronicaVBulk extends Filtro_ricerca_doc_ammVBulk {

	private String codiceUnivocoUfficioIpa = null;
	private Long pgProtocollazioneIVA = null;
	private Long pgStampa = null;
	private String optionStatoFirmaFattElett = null;
	private CompoundFindClause sqlClauses = null;
	public final static Dictionary optionsStatoFirmaFattElett;
	public final static String STATO_FIRMA_FIRMATE = "S";
	public final static String STATO_FIRMA_DA_FIRMARE = "N";
	public final static String STATO_FIRMA_TUTTE = "T";


	static {
		optionsStatoFirmaFattElett = new it.cnr.jada.util.OrderedHashtable();
		optionsStatoFirmaFattElett.put(STATO_FIRMA_FIRMATE,"Firmate");
		optionsStatoFirmaFattElett.put(STATO_FIRMA_DA_FIRMARE,"Da Firmare");
		optionsStatoFirmaFattElett.put(STATO_FIRMA_TUTTE,"Tutte");	
    };

 /**
 * Filtro_ricerca_doc_amm_protocollabileVBulk constructor comment.
 */
public Filtro_ricerca_doc_amm_fatturazione_elettronicaVBulk() {
	super();
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
public Filtro_ricerca_doc_ammVBulk initializeForSearch(
	it.cnr.contab.docamm00.bp.ListaDocumentiAmministrativiBP bp,
	it.cnr.jada.action.ActionContext context) {

	super.initializeForSearch(bp, context);
	setOptionStatoFirmaFattElett(STATO_FIRMA_DA_FIRMARE);
//	initDataStampa(bp, context);
	
	return this;
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
public String getCodiceUnivocoUfficioIpa() {
	return codiceUnivocoUfficioIpa;
}
public void setCodiceUnivocoUfficioIpa(String codiceUnivocoUfficioIpa) {
	this.codiceUnivocoUfficioIpa = codiceUnivocoUfficioIpa;
}
public String getOptionStatoFirmaFattElett() {
	return optionStatoFirmaFattElett;
}
public void setOptionStatoFirmaFattElett(String optionStatoFirmaFattElett) {
	this.optionStatoFirmaFattElett = optionStatoFirmaFattElett;
}
public boolean isDaFirmare(){
	if (getOptionStatoFirmaFattElett() != null && getOptionStatoFirmaFattElett().equals(STATO_FIRMA_DA_FIRMARE)){
		return true;
	}
	return false;
}
public boolean isFirmata(){
	if (getOptionStatoFirmaFattElett() != null && getOptionStatoFirmaFattElett().equals(STATO_FIRMA_FIRMATE)){
		return true;
	}
	return false;
}
public boolean isTuttiStatiFirma(){
	if ((getOptionStatoFirmaFattElett() == null ) || (getOptionStatoFirmaFattElett() != null && getOptionStatoFirmaFattElett().equals(STATO_FIRMA_TUTTE))){
		return true;
	}
	return false;
}
}
