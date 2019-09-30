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

package it.cnr.contab.chiusura00.bulk;

import java.util.*;
import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

/**
 * Oggetto bulk che mappa le tre tabelle:
 * - V_OBB_ACC_ANNULLA
 * - V_OBB_ACC_RIPORTA
 * - V_OBB_ACC_DERIPORTA
 * Queste tre tabelle hanno la stessa interfaccia e quindi sono state mappate
 * sullo stesso Bulk; solo nel momento di effettuare la query viene specificata
 * quale tabella utilizzare.
 */

public class V_obb_acc_xxxBulk extends V_obb_acc_xxxBase {

	public final static Dictionary ti_gestioneKeys = it.cnr.contab.doccont00.core.bulk.SospesoBulk.ti_entrata_spesaKeys;
	public final static Dictionary ti_competenza_residuoKeys;
	public final static String TIPO_COMPETENZA 	= "C";
	public final static String TIPO_RESIDUO 		= "R";		

	static 
	{
		ti_competenza_residuoKeys = new Hashtable();
		ti_competenza_residuoKeys.put(TIPO_COMPETENZA,	"Competenza");
		ti_competenza_residuoKeys.put(TIPO_RESIDUO,	"Residuo");
	};


	protected Boolean fl_ente = Boolean.FALSE;
	protected String ti_competenza_residuo;
	protected Elemento_voceBulk elemento_voce = new Elemento_voceBulk();
	protected TerzoBulk terzo = new TerzoBulk();
	protected String cd_cds_ente;
	protected String cd_cds_scrivania;
	protected Long pg_call;
	protected Long pg_doc_da;
	protected Long pg_doc_a;
	protected Elemento_voceBulk nuovo_ev = new Elemento_voceBulk();
	protected Voce_fBulk nuova_voce = new Voce_fBulk();		
	
public V_obb_acc_xxxBulk() {
	super();
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'cd_cds_ente'
 *
 * @return Il valore della proprietà 'cd_cds_ente'
 */
public java.lang.String getCd_cds_ente() {
	return cd_cds_ente;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'cd_cds_scrivania'
 *
 * @return Il valore della proprietà 'cd_cds_scrivania'
 */
public java.lang.String getCd_cds_scrivania() {
	return cd_cds_scrivania;
}
public java.lang.String getCd_elemento_voce() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
	if (elemento_voce == null)
		return null;
	return elemento_voce.getCd_elemento_voce();
}
public Integer getCd_terzo() {
	TerzoBulk terzo = this.getTerzo();
	if (terzo == null)
		return null;
	return terzo.getCd_terzo();
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'elemento_voce'
 *
 * @return Il valore della proprietà 'elemento_voce'
 */
public it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk getElemento_voce() {
	return elemento_voce;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'fl_ente'
 *
 * @return Il valore della proprietà 'fl_ente'
 */
public java.lang.Boolean getFl_ente() {
	return fl_ente;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'nuova_voce'
 *
 * @return Il valore della proprietà 'nuova_voce'
 */
public it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk getNuova_voce() {
	return nuova_voce;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'nuovo_ev'
 *
 * @return Il valore della proprietà 'nuovo_ev'
 */
public it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk getNuovo_ev() {
	return nuovo_ev;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'pg_call'
 *
 * @return Il valore della proprietà 'pg_call'
 */
public java.lang.Long getPg_call() {
	return pg_call;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'pg_doc_a'
 *
 * @return Il valore della proprietà 'pg_doc_a'
 */
public java.lang.Long getPg_doc_a() {
	return pg_doc_a;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'pg_doc_da'
 *
 * @return Il valore della proprietà 'pg_doc_da'
 */
public java.lang.Long getPg_doc_da() {
	return pg_doc_da;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'terzo'
 *
 * @return Il valore della proprietà 'terzo'
 */
public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getTerzo() {
	return terzo;
}
public java.lang.String getTi_appartenenza() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
	if (elemento_voce == null)
		return null;
	return elemento_voce.getTi_appartenenza();
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'ti_competenza_residuo'
 *
 * @return Il valore della proprietà 'ti_competenza_residuo'
 */
public java.lang.String getTi_competenza_residuo() {
	return ti_competenza_residuo;
}
public java.lang.String getTi_gestione() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
	if (elemento_voce == null)
		return null;
	return elemento_voce.getTi_gestione();
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'enteInScrivania'
 *
 * @return Il valore della proprietà 'enteInScrivania'
 */
public boolean isEnteInScrivania() 
{
	if (getCd_cds_ente() == null || getCd_cds_scrivania() == null)
		return false;
	return getCd_cds_ente().equals( getCd_cds_scrivania());
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rOTerzo'
 *
 * @return Il valore della proprietà 'rOTerzo'
 */
public boolean isROCd_voce() {
	
	return (isEnteInScrivania()) ?
				false :
				(getFl_ente() != null && !getFl_ente().booleanValue());
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rOElemento_voce'
 *
 * @return Il valore della proprietà 'rOElemento_voce'
 */
public boolean isROElemento_voce() {
	return elemento_voce == null || elemento_voce.getCrudStatus() == NORMAL;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rONuova_voce'
 *
 * @return Il valore della proprietà 'rONuova_voce'
 */
public boolean isRONuova_voce() {
	return nuova_voce == null || nuova_voce.getCrudStatus() == NORMAL;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rONuovo_ev'
 *
 * @return Il valore della proprietà 'rONuovo_ev'
 */
public boolean isRONuovo_ev() {
	return nuovo_ev == null || nuovo_ev.getCrudStatus() == NORMAL;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rOTerzo'
 *
 * @return Il valore della proprietà 'rOTerzo'
 */
public boolean isROTerzo() {
	return terzo == null || terzo.getCrudStatus() == NORMAL;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'cd_cds_ente'
 *
 * @param newCd_cds_ente	Il valore da assegnare a 'cd_cds_ente'
 */
public void setCd_cds_ente(java.lang.String newCd_cds_ente) {
	cd_cds_ente = newCd_cds_ente;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'cd_cds_scrivania'
 *
 * @param newCd_cds_scrivania	Il valore da assegnare a 'cd_cds_scrivania'
 */
public void setCd_cds_scrivania(java.lang.String newCd_cds_scrivania) {
	cd_cds_scrivania = newCd_cds_scrivania;
}
public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
	this.getElemento_voce().setCd_elemento_voce(cd_elemento_voce);
}
public void setCd_terzo(Integer cd_terzo) {
	this.getTerzo().setCd_terzo(cd_terzo);
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'elemento_voce'
 *
 * @param newElemento_voce	Il valore da assegnare a 'elemento_voce'
 */
public void setElemento_voce(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk newElemento_voce) {
	elemento_voce = newElemento_voce;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'fl_ente'
 *
 * @param newFl_ente	Il valore da assegnare a 'fl_ente'
 */
public void setFl_ente(java.lang.Boolean newFl_ente) {
	fl_ente = newFl_ente;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'nuova_voce'
 *
 * @param newNuova_voce	Il valore da assegnare a 'nuova_voce'
 */
public void setNuova_voce(it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk newNuova_voce) {
	nuova_voce = newNuova_voce;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'nuovo_ev'
 *
 * @param newNuovo_ev	Il valore da assegnare a 'nuovo_ev'
 */
public void setNuovo_ev(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk newNuovo_ev) {
	nuovo_ev = newNuovo_ev;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'pg_call'
 *
 * @param newPg_call	Il valore da assegnare a 'pg_call'
 */
public void setPg_call(java.lang.Long newPg_call) {
	pg_call = newPg_call;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'pg_doc_a'
 *
 * @param newPg_doc_a	Il valore da assegnare a 'pg_doc_a'
 */
public void setPg_doc_a(java.lang.Long newPg_doc_a) {
	pg_doc_a = newPg_doc_a;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'pg_doc_da'
 *
 * @param newPg_doc_da	Il valore da assegnare a 'pg_doc_da'
 */
public void setPg_doc_da(java.lang.Long newPg_doc_da) {
	pg_doc_da = newPg_doc_da;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'terzo'
 *
 * @param newTerzo	Il valore da assegnare a 'terzo'
 */
public void setTerzo(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newTerzo) {
	terzo = newTerzo;
}
public void setTi_appartenenza(java.lang.String ti_appartenenza) {
	this.getElemento_voce().setTi_appartenenza(ti_appartenenza);
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'ti_competenza_residuo'
 *
 * @param newTi_competenza_residuo	Il valore da assegnare a 'ti_competenza_residuo'
 */
public void setTi_competenza_residuo(java.lang.String newTi_competenza_residuo) {
	ti_competenza_residuo = newTi_competenza_residuo;
}
public void setTi_gestione(java.lang.String ti_gestione) {
	this.getElemento_voce().setTi_gestione(ti_gestione);
}
/**
 * Esegue la validazione del Bulk nel caso di funzione di riporta con cambio di
 * elemento_voce o voce_f;
 * Se in scrivania c'e' 999 l'utenet deve aver selezionato una nuova voce_f
 * altriemnti deve aver selezionato un nuov elemento_voce
 * 
 *
 * @throws ValidationException	
 */
public void validatePerRiportaEvoluto() throws ValidationException
{
	if ( isEnteInScrivania() && getNuova_voce().getCd_voce() == null)
		throw new ValidationException( "Deve essere specificata il nuovo capitolo finanziario" );
	else if ( !isEnteInScrivania() && getNuovo_ev().getCd_elemento_voce() == null)
		throw new ValidationException( "Deve essere specificata la nuova voce del piano" );	

}
}
