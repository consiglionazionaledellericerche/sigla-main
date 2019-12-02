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

import it.cnr.jada.bulk.*;

import java.util.*;

public class AccertamentoPGiroBulk extends AccertamentoBulk {
	private List unitaOrganizzativaOptions;
	private String cd_uo_ente;
	private Ass_obb_acr_pgiroBulk associazione;
	private java.sql.Timestamp dt_scadenza;

	private Integer esercizio_reversale;
	private java.lang.Long pg_reversale;

	private Integer esercizio_doc_attivo;
	private java.lang.Long pg_doc_attivo;
	private String cd_tipo_documento_amm;
	private java.util.Hashtable tipoDocumentoKeys;

	public final static Dictionary competenzaResiduoKeys;

	static 
	{
		competenzaResiduoKeys = new Hashtable();
		competenzaResiduoKeys.put(Numerazione_doc_contBulk.TIPO_ACR,		"Competenza");
		competenzaResiduoKeys.put(Numerazione_doc_contBulk.TIPO_ACR_RES,	"Residuo");
	};

	protected boolean fl_isTronco = false;
	
	private it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voceContr = new it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk();
	
public AccertamentoPGiroBulk() {
	super();
	initialize();
}
public AccertamentoPGiroBulk(String cd_cds, Integer esercizio, Integer esercizio_originale, Long pg_accertamento) {
	super(cd_cds, esercizio, esercizio_originale, pg_accertamento);
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
 * Restituisce il valore della proprietà 'esercizio_doc_attivo'
 *
 * @return Il valore della proprietà 'esercizio_doc_attivo'
 */
public java.lang.Integer getEsercizio_doc_attivo() {
	return esercizio_doc_attivo;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'esercizio_reversale'
 *
 * @return Il valore della proprietà 'esercizio_reversale'
 */
public java.lang.Integer getEsercizio_reversale() {
	return esercizio_reversale;
}
public String getManagerName() {

	return "CRUDAccertamentoPGiroBP";
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'pg_doc_attivo'
 *
 * @return Il valore della proprietà 'pg_doc_attivo'
 */
public java.lang.Long getPg_doc_attivo() {
	return pg_doc_attivo;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'pg_reversale'
 *
 * @return Il valore della proprietà 'pg_reversale'
 */
public java.lang.Long getPg_reversale() {
	return pg_reversale;
}
public String getTi_competenza_residuo() 
{

	if (Numerazione_doc_contBulk.TIPO_ACR_RES.equals( getCd_tipo_documento_cont()))
		return MandatoBulk.TIPO_RESIDUO;
	return MandatoBulk.TIPO_COMPETENZA;		
}
/**
 * @return java.util.Hashtable
 */
public java.util.Hashtable getTipoDocumentoKeys() {
	return tipoDocumentoKeys;
}
/**
 * @return java.util.List
 */
public java.util.List getUnitaOrganizzativaOptions() {
	return unitaOrganizzativaOptions;
}
private void initialize () {
	setFl_pgiro( new Boolean( true ));
}
/**
 * Inizializza l'Oggetto Bulk.
 * @param bp Il Business Process in uso
 * @param context Il contesto dell'azione
 * @return OggettoBulk L'oggetto bulk inizializzato
 */
public OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context)
{
	super.initialize( bp, context );
//	it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk aUoScrivania = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);	
//	setCd_uo_origine(aUoScrivania.getCd_unita_organizzativa());		
//	setCd_cds_origine(aUoScrivania.getUnita_padre().getCd_unita_organizzativa());
	return this;
}
/**
 * Inizializza l'Oggetto Bulk per la ricerca libera.
 * @param bp Il Business Process in uso
 * @param context Il contesto dell'azione
 * @return OggettoBulk L'oggetto bulk inizializzato
 */
public OggettoBulk initializeForFreeSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	return super.initializeForSearch(bp,context);
}
/**
 * Inizializza l'Oggetto Bulk per l'inserimento.
 * @param bp Il Business Process in uso
 * @param context Il contesto dell'azione
 * @return OggettoBulk L'oggetto bulk inizializzato
 */
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	super.initializeForInsert( bp, context );
	it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk aUoScrivania = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);		
	setUnita_organizzativa( aUoScrivania );
	setCd_cds( getUnita_organizzativa().getCd_unita_padre());

	setCd_uo_origine(aUoScrivania.getCd_unita_organizzativa());		
	setCd_cds_origine(aUoScrivania.getUnita_padre().getCd_unita_organizzativa());
	
	return this;
}
/**
 * Inizializza l'Oggetto Bulk per la ricerca.
 * @param bp Il Business Process in uso
 * @param context Il contesto dell'azione
 * @return OggettoBulk L'oggetto bulk inizializzato
 */
public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	super.initializeForSearch( bp, context);
	setUnita_organizzativa( it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context));
	return this;
}
public boolean isControparteRiportatata()
{
	if ( getAssociazione() != null &&  getAssociazione().getImpegno() != null &&
		  "Y".equals(getAssociazione().getImpegno().getRiportato())) 		  		  
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
	return getAssociazione() != null && getAssociazione().getPg_obbligazione() != null;
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
 * Imposta il valore della proprietà 'esercizio_doc_attivo'
 *
 * @param newEsercizio_doc_attivo	Il valore da assegnare a 'esercizio_doc_attivo'
 */
public void setEsercizio_doc_attivo(java.lang.Integer newEsercizio_doc_attivo) {
	esercizio_doc_attivo = newEsercizio_doc_attivo;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'esercizio_reversale'
 *
 * @param newEsercizio_reversale	Il valore da assegnare a 'esercizio_reversale'
 */
public void setEsercizio_reversale(java.lang.Integer newEsercizio_reversale) {
	esercizio_reversale = newEsercizio_reversale;
}
/**
 * @param newFl_isTronco boolean
 */
public void setFl_isTronco(boolean newFl_isTronco) {
	fl_isTronco = newFl_isTronco;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'pg_doc_attivo'
 *
 * @param newPg_doc_attivo	Il valore da assegnare a 'pg_doc_attivo'
 */
public void setPg_doc_attivo(java.lang.Long newPg_doc_attivo) {
	pg_doc_attivo = newPg_doc_attivo;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'pg_reversale'
 *
 * @param newPg_reversale	Il valore da assegnare a 'pg_reversale'
 */
public void setPg_reversale(java.lang.Long newPg_reversale) {
	pg_reversale = newPg_reversale;
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
public void validate() throws ValidationException {
	
	// controllo su campo ESERCIZIO 
	if ( getEsercizio() == null )
			throw new ValidationException( "Il campo ESERCIZIO deve essere selezionato." );

	// controllo su campo DATA REGISTRAZIONE
	if ( getDt_registrazione() == null )
		throw new ValidationException( "Il campo DATA è obbligatorio." );

	java.sql.Timestamp dataRegistrazione = getDt_registrazione();
//	java.sql.Timestamp dataSistema = new java.sql.Timestamp(System.currentTimeMillis());
	java.sql.Timestamp dataSistema;
	try
	{
		dataSistema = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
	}
	catch ( Exception e )
	{
		throw new ValidationException( "Impossibile recuperare la data di sistema!");
	}		

	if (dataRegistrazione.after(dataSistema))
		throw new ValidationException( "Non è possibile inserire una data di registrazione posteriore a quella di sistema." );
			
	// controllo su campo DESCRIZIONE
	if ( getDs_accertamento() == null || getDs_accertamento().equals("") )
		throw new ValidationException( "Il campo DESCRIZIONE è obbligatorio." );

	// controllo su campo DEBITORE
	if ( getDebitore().getCd_terzo() == null || getDebitore().getCd_terzo().equals("") )
		throw new ValidationException( "Il campo DEBITORE è obbligatorio." );
		
	// controlli su campo IMPORTO	
	if (  getIm_accertamento() == null ) 
		throw new ValidationException( "L' IMPORTO dell'Annotazione d'Entrata su Partita di Giro non può essere nullo." );
	
	if ( this.isAssociataADocAmm() == false && ( ( getIm_accertamento() == null ) || 
		 (getIm_accertamento().compareTo(new java.math.BigDecimal(0)) <= 0) ))
		throw new ValidationException( "L' IMPORTO dell'Annotazione d'Entrata su Partita di Giro deve essere maggiore di 0." );

	// controllo su campo CAPITOLO
	if ( getCapitolo().getCd_voce() == null || getCapitolo().getCd_voce().equals("") )
		throw new ValidationException( "Il campo CAPITOLO è obbligatorio." );

	// controllo su campo DATA SCADENZA
	if ( getDt_scadenza() == null )
		throw new ValidationException( "Il campo DATA SCADENZA è obbligatorio." );
/*	if ( !Numerazione_doc_contBulk.TIPO_ACR_RES.equals( getCd_tipo_documento_cont()))
	{ */
		if ( //  data obbligazione != data scadenza && data_obbligazione >= data_scadenza
		!(getDt_registrazione().after( getDt_scadenza() ) && getDt_registrazione().before( getDt_scadenza() )) &&
		  getDt_registrazione().after( getDt_scadenza() ))
		throw new ValidationException( "Non è possibile inserire una data scadenza antecedente a quella di registrazione dell'Annotazione d'Entrata su Partita di Giro." );
		
		java.util.GregorianCalendar gc = (java.util.GregorianCalendar)java.util.GregorianCalendar.getInstance();
		gc.setTime(getDt_scadenza());
		if (gc.get(java.util.GregorianCalendar.YEAR) < getEsercizio().intValue())
			throw new ValidationException( "Non è possibile inserire una data scadenza antecedente all'esercizio di creazione dell'Annotazione d'Entrata su Partita di Giro." );
/*	}		*/

}
public it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk getElemento_voceContr() {
	return elemento_voceContr;
}
public void setElemento_voceContr(
		it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voceContr) {
	this.elemento_voceContr = elemento_voceContr;
} 
}
