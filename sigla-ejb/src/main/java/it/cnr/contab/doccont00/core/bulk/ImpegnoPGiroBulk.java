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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.contab.utenze00.bp.*;
import it.cnr.jada.bulk.*;

import java.util.*;

public class ImpegnoPGiroBulk extends ObbligazioneBulk {
	private List unitaOrganizzativaOptions;
	private String cd_uo_ente;
	private Ass_obb_acr_pgiroBulk associazione;
	private java.sql.Timestamp dt_scadenza;	

	private Integer esercizio_mandato;
	private java.lang.Long pg_mandato;

	private Integer esercizio_doc_passivo;
	private java.lang.Long pg_doc_passivo;
	private String cd_tipo_documento_amm;
	public static java.util.Hashtable tipoDocumentoKeys;

	public final static Dictionary competenzaResiduoKeys;

	static 
	{
		competenzaResiduoKeys = new Hashtable();
		competenzaResiduoKeys.put(Numerazione_doc_contBulk.TIPO_IMP,		"Competenza");
		competenzaResiduoKeys.put(Numerazione_doc_contBulk.TIPO_IMP_RES,	"Residuo");
	};
	private it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voceContr = new it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk();
	protected boolean fl_isTronco = false;
public ImpegnoPGiroBulk() {
	super();
	initialize();	
}
public ImpegnoPGiroBulk(String cd_cds, Integer esercizio, Integer esercizio_originale, Long pg_obbligazione) {
	super(cd_cds, esercizio, esercizio_originale, pg_obbligazione);
	initialize();
}
/**
 * @return it.cnr.contab.doccont00.core.bulk.Ass_obb_acr_pgiroBulk
 */
public Ass_obb_acr_pgiroBulk getAssociazione() {
	return associazione;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'cd_tipo_documento_amm'
 *
 * @return Il valore della proprietà 'cd_tipo_documento_amm'
 */
public java.lang.String getCd_tipo_documento_amm() {
	return cd_tipo_documento_amm;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'cd_uo_ente'
 *
 * @return Il valore della proprietà 'cd_uo_ente'
 */
public java.lang.String getCd_uo_ente() {
	return cd_uo_ente;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'dt_scadenza'
 *
 * @return Il valore della proprietà 'dt_scadenza'
 */
public java.sql.Timestamp getDt_scadenza() {
	return dt_scadenza;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'esercizio_doc_passivo'
 *
 * @return Il valore della proprietà 'esercizio_doc_passivo'
 */
public java.lang.Integer getEsercizio_doc_passivo() {
	return esercizio_doc_passivo;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'esercizio_mandato'
 *
 * @return Il valore della proprietà 'esercizio_mandato'
 */
public java.lang.Integer getEsercizio_mandato() {
	return esercizio_mandato;
}
public String getManagerName() {

	return "CRUDImpegnoPGiroBP";
}
/**
 * @return java.lang.Long
 */
public java.lang.Long getPg_doc_passivo() {
	return pg_doc_passivo;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'pg_mandato'
 *
 * @return Il valore della proprietà 'pg_mandato'
 */
public java.lang.Long getPg_mandato() {
	return pg_mandato;
}
/**
 * @return java.util.Hashtable
 */
public final static java.util.Hashtable getTipoDocumentoKeys() {
	return tipoDocumentoKeys;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'unitaOrganizzativaOptions'
 *
 * @return Il valore della proprietà 'unitaOrganizzativaOptions'
 */
public java.util.List getUnitaOrganizzativaOptions() {
	return unitaOrganizzativaOptions;
}
private void initialize () {
	setFl_pgiro( new Boolean( true ));
}
/**
 * Inizializza l'Oggetto Bulk per la ricerca libera.
 * @param bp Il Business Process in uso
 * @param context Il contesto dell'azione
 * @return OggettoBulk L'oggetto bulk inizializzato
 */
public OggettoBulk initializeForFreeSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
//	return super.initializeForSearch(bp,context);
	setEsercizio( ((CNRUserContext)context.getUserContext()).getEsercizio() );
	setEsercizio_competenza( ((CNRUserContext)context.getUserContext()).getEsercizio() );
	return this;
}
/**
 * Inizializza l'Oggetto Bulk per l'inserimento.
 * @param bp Il Business Process in uso
 * @param context Il contesto dell'azione
 * @return OggettoBulk L'oggetto bulk inizializzato
 */
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	super.initializeForInsert( bp, context );
	setStato_obbligazione( STATO_OBB_DEFINITIVO );
	return this;
}
/**
 * Inizializza l'Oggetto Bulk per la ricerca.
 * @param bp Il Business Process in uso
 * @param context Il contesto dell'azione
 * @return OggettoBulk L'oggetto bulk inizializzato
 */
public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
//	super.initializeForSearch( bp, context);
	setEsercizio( ((CNRUserContext)context.getUserContext()).getEsercizio() );
	setEsercizio_competenza( ((CNRUserContext)context.getUserContext()).getEsercizio() );
	setUnita_organizzativa( it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context));
	return this;
}
public boolean isControparteRiportatata()
{
	
	if ( getAssociazione() != null &&  getAssociazione().getAccertamento() != null &&
		  "Y".equals(getAssociazione().getAccertamento().getRiportato())) 		  		  
		return true;
	return false;
}
/**
 * @return boolean
 */
public boolean isFl_isTronco() {
	return fl_isTronco;
}
public boolean isInitialized()
{
	return getAssociazione() != null && getAssociazione().getPg_accertamento() != null;
}
/**
 * @param newAssociazione it.cnr.contab.doccont00.core.bulk.Ass_obb_acr_pgiroBulk
 */
public void setAssociazione(Ass_obb_acr_pgiroBulk newAssociazione) {
	associazione = newAssociazione;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'cd_tipo_documento_amm'
 *
 * @param newCd_tipo_documento_amm	Il valore da assegnare a 'cd_tipo_documento_amm'
 */
public void setCd_tipo_documento_amm(java.lang.String newCd_tipo_documento_amm) {
	cd_tipo_documento_amm = newCd_tipo_documento_amm;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'cd_uo_ente'
 *
 * @param newCd_uo_ente	Il valore da assegnare a 'cd_uo_ente'
 */
public void setCd_uo_ente(java.lang.String newCd_uo_ente) {
	cd_uo_ente = newCd_uo_ente;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'dt_scadenza'
 *
 * @param newDt_scadenza	Il valore da assegnare a 'dt_scadenza'
 */
public void setDt_scadenza(java.sql.Timestamp newDt_scadenza) {
	dt_scadenza = newDt_scadenza;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'esercizio_doc_passivo'
 *
 * @param newEsercizio_doc_passivo	Il valore da assegnare a 'esercizio_doc_passivo'
 */
public void setEsercizio_doc_passivo(java.lang.Integer newEsercizio_doc_passivo) {
	esercizio_doc_passivo = newEsercizio_doc_passivo;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'esercizio_mandato'
 *
 * @param newEsercizio_mandato	Il valore da assegnare a 'esercizio_mandato'
 */
public void setEsercizio_mandato(java.lang.Integer newEsercizio_mandato) {
	esercizio_mandato = newEsercizio_mandato;
}
/**
 * @param newFl_isTronco boolean
 */
public void setFl_isTronco(boolean newFl_isTronco) {
	fl_isTronco = newFl_isTronco;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'pg_doc_passivo'
 *
 * @param newPg_doc_passivo	Il valore da assegnare a 'pg_doc_passivo'
 */
public void setPg_doc_passivo(java.lang.Long newPg_doc_passivo) {
	pg_doc_passivo = newPg_doc_passivo;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'pg_mandato'
 *
 * @param newPg_mandato	Il valore da assegnare a 'pg_mandato'
 */
public void setPg_mandato(java.lang.Long newPg_mandato) {
	pg_mandato = newPg_mandato;
}
/**
 * @param newUnitaOrganizzativaOptions java.util.List
 */
public void setUnitaOrganizzativaOptions(java.util.List newUnitaOrganizzativaOptions) {
	unitaOrganizzativaOptions = newUnitaOrganizzativaOptions;
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws ValidationException
{
	super.validate();
	// controllo su campo DATA SCADENZA
	if ( getDt_scadenza() == null )
		throw new ValidationException( "Il campo DATA SCADENZA è obbligatorio." );

/*	if ( !Numerazione_doc_contBulk.TIPO_IMP_RES.equals( getCd_tipo_documento_cont()))
	{
		*/
		if ( //  data obbligazione != data scadenza && data_obbligazione >= data_scadenza
			!(getDt_registrazione().after( getDt_scadenza() ) && getDt_registrazione().before( getDt_scadenza() )) &&
			  getDt_registrazione().after( getDt_scadenza() ))
			throw new ValidationException( "Non è possibile inserire una data scadenza antecedente a quella di registrazione dell'Annotazione di Spesa su Partita di Giro." );
		
		java.util.GregorianCalendar gc = (java.util.GregorianCalendar)java.util.GregorianCalendar.getInstance();
		gc.setTime(getDt_scadenza());
		if (gc.get(java.util.GregorianCalendar.YEAR) < getEsercizio().intValue())
			throw new ValidationException( "Non è possibile inserire una data scadenza antecedente all'esercizio di creazione dell'Annotazione di Spesa su Partita di Giro." );
/*	}	*/

	// controlli su campo IMPORTO	
	if (  getIm_obbligazione() == null ) 
		throw new ValidationException( "L' IMPORTO dell'Annotazione di Spesa su Partita di Giro non può essere nullo." );
	// eliminato il controllo sull'importo: si può inserire anche 0
	if ( this.isAssociataADocAmm() == false &&  getIm_obbligazione() == null )  
			throw new ValidationException( "L' IMPORTO dell'Annotazione di Spesa su Partita di Giro deve essere maggiore di 0." );
	
	/*if ( this.isAssociataADocAmm() == false && ( ( getIm_obbligazione() == null )  
	|| (getIm_obbligazione().compareTo(new java.math.BigDecimal(0)) <= 0) ))
	throw new ValidationException( "L' IMPORTO dell'Annotazione di Spesa su Partita di Giro deve essere maggiore di 0." );*/


}
public it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk getElemento_voceContr() {
	return elemento_voceContr;
}
public void setElemento_voceContr(
		it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voceContr) {
	this.elemento_voceContr = elemento_voceContr;
}
}
