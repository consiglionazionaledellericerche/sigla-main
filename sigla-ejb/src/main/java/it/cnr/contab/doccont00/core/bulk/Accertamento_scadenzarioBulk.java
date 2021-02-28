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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;

import it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_rigaBulk;
import it.cnr.contab.pagopa.bulk.PendenzaPagopaBulk;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;

public class Accertamento_scadenzarioBulk extends Accertamento_scadenzarioBase implements IScadenzaDocumentoContabileBulk
{
	protected AccertamentoBulk accertamento = new AccertamentoBulk();
	protected java.lang.Boolean fl_aggiorna_scad_successiva;
	protected Accertamento_scadenzarioBulk scadenza_iniziale;

	private BulkList<Accertamento_scad_voceBulk> accertamento_scad_voceColl = new BulkList<Accertamento_scad_voceBulk>();	

	public static final int STATUS_NOT_CONFIRMED = 0;
	public static final int STATUS_CONFIRMED = 1;

	private int status = STATUS_CONFIRMED;

	private java.util.Dictionary tipoDocumentoKeys;

	private Integer esercizio_reversale;
	private java.lang.Long pg_reversale;

	private Integer esercizio_doc_attivo;
	private String cd_tipo_documento_amm;	
	private java.lang.Long pg_doc_attivo;
	private PendenzaPagopaBulk scadenzaPagopa;

	private boolean fromDocAmm = false;	

	private java.math.BigDecimal importoDisponibile;

	public Accertamento_scadenzarioBulk()
	{
	}
	/**
	 * <!-- @TODO: da completare -->
	 * 
	 *
	 * @param accertamento	
	 */
	public Accertamento_scadenzarioBulk(AccertamentoBulk accertamento) 
	{
		setAccertamento(accertamento);
		setEsercizio( accertamento.getEsercizio() );
		setEsercizio_originale( accertamento.getEsercizio_originale() );
		setPg_accertamento( accertamento.getPg_accertamento() );
		setCd_cds( accertamento.getCd_cds() );
	}
	public Accertamento_scadenzarioBulk(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.Integer esercizio_originale,java.lang.Long pg_accertamento,java.lang.Long pg_accertamento_scadenzario) {
		super(cd_cds,esercizio,esercizio_originale,pg_accertamento,pg_accertamento_scadenzario);
		setAccertamento(new it.cnr.contab.doccont00.core.bulk.AccertamentoBulk(cd_cds,esercizio,esercizio_originale,pg_accertamento));
	}
	/**
	 * <!-- @TODO: da completare -->
	 * 
	 *
	 * @param dettaglio	
	 */
	public void completeFrom(Nota_di_credito_rigaBulk dettaglio) {

		setDs_scadenza(dettaglio.getDs_riga_fattura());

		java.sql.Timestamp dtScadenza = dettaglio.getNotaDiCredito().getDt_scadenza();
		if (dtScadenza == null)
			dtScadenza = dettaglio.getNotaDiCredito().getDt_registrazione();
		setDt_scadenza_emissione_fattura(dtScadenza);
		setDt_scadenza_incasso(dtScadenza);

		java.math.BigDecimal imTotaleDettaglio = dettaglio.getIm_imponibile().add(dettaglio.getIm_iva());
		imTotaleDettaglio = imTotaleDettaglio.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
		setIm_associato_doc_amm(imTotaleDettaglio);
		setIm_scadenza(imTotaleDettaglio);
	}
	/**
	 * @return it.cnr.contab.doccont00.core.bulk.AccertamentoBulk
	 */
	public AccertamentoBulk getAccertamento() {
		return accertamento;
	}
	/**
	 * @return it.cnr.jada.bulk.BulkList
	 */
	public it.cnr.jada.bulk.BulkList<Accertamento_scad_voceBulk> getAccertamento_scad_voceColl() {
		return accertamento_scad_voceColl;
	}
	public BulkCollection[] getBulkLists() 
	{
		return new it.cnr.jada.bulk.BulkCollection[] { accertamento_scad_voceColl  };
	}
	public java.lang.String getCd_cds() 
	{
		AccertamentoBulk accertamento = this.getAccertamento();

		if (accertamento == null)
			return null;

		return accertamento.getCd_cds();	// Ente
	}
	/**
	 * @return java.lang.String
	 */
	public java.lang.String getCd_tipo_documento_amm() {
		return cd_tipo_documento_amm;
	}
	public java.lang.Integer getEsercizio() {
		it.cnr.contab.doccont00.core.bulk.AccertamentoBulk accertamento = this.getAccertamento();
		if (accertamento == null)
			return null;
		return accertamento.getEsercizio();
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
	@JsonIgnore
	public IDocumentoContabileBulk getFather() {
		return getAccertamento();
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Restituisce il valore della proprietà 'fl_aggiorna_scad_successiva'
	 *
	 * @return Il valore della proprietà 'fl_aggiorna_scad_successiva'
	 */
	public java.lang.Boolean getFl_aggiorna_scad_successiva() {
		return fl_aggiorna_scad_successiva;
	}
	public java.lang.Integer getEsercizio_originale() {
		it.cnr.contab.doccont00.core.bulk.AccertamentoBulk accertamento = this.getAccertamento();
		if (accertamento == null)
			return null;
		return accertamento.getEsercizio_originale();
	}
	public java.lang.Long getPg_accertamento() {
		it.cnr.contab.doccont00.core.bulk.AccertamentoBulk accertamento = this.getAccertamento();
		if (accertamento == null)
			return null;
		return accertamento.getPg_accertamento();
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
	public Accertamento_scadenzarioBulk getScadenza_iniziale() {
		return scadenza_iniziale;
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Restituisce il valore della proprietà 'status'
	 *
	 * @return Il valore della proprietà 'status'
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @return java.util.Dictionary
	 */
	public java.util.Dictionary getTipoDocumentoKeys() {
		return tipoDocumentoKeys;
	}
	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) 
	{
		Accertamento_scadenzarioBulk as = (Accertamento_scadenzarioBulk) super.initializeForInsert( bp, context );
		as.setStatus( as.STATUS_NOT_CONFIRMED );
		return as;
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Restituisce il valore della proprietà 'fromDocAmm'
	 *
	 * @return Il valore della proprietà 'fromDocAmm'
	 */
	public boolean isFromDocAmm() {
		return fromDocAmm;
	}
	/**
	 * <!-- @TODO: da completare -->
	 * 
	 *
	 * @param collScadenze	
	 * @return 
	 */
	public BulkList ordinaPerDataScadenza( BulkList collScadenze ) 
	{
		// riordino la lista delle scadenze per data scadenza incasso

		Collections.sort(collScadenze,new Comparator() {	

			public int compare(Object o1, Object o2) 
			{
				Accertamento_scadenzarioBulk os1 = (Accertamento_scadenzarioBulk) o1;
				Accertamento_scadenzarioBulk os2 = (Accertamento_scadenzarioBulk) o2;

				return os1.getDt_scadenza_incasso().compareTo( os2.getDt_scadenza_incasso());
			}
		});

		return collScadenze;
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Imposta il valore della proprietà 'accertamento'
	 *
	 * @param newAccertamento	Il valore da assegnare a 'accertamento'
	 */
	public void setAccertamento(AccertamentoBulk newAccertamento) {
		accertamento = newAccertamento;
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Imposta il valore della proprietà 'accertamento_scad_voceColl'
	 *
	 * @param newAccertamento_scad_voceColl	Il valore da assegnare a 'accertamento_scad_voceColl'
	 */
	public void setAccertamento_scad_voceColl(it.cnr.jada.bulk.BulkList<Accertamento_scad_voceBulk> newAccertamento_scad_voceColl) {
		accertamento_scad_voceColl = newAccertamento_scad_voceColl;
	}	
	public void setCd_cds(java.lang.String cd_cds) 
	{
		this.getAccertamento().setCd_cds(cd_cds);
	}
	/**
	 * @param newCd_tipo_documento_amm java.lang.String
	 */
	public void setCd_tipo_documento_amm(java.lang.String newCd_tipo_documento_amm) {
		cd_tipo_documento_amm = newCd_tipo_documento_amm;
	}
	public void setEsercizio(java.lang.Integer esercizio) {
		this.getAccertamento().setEsercizio(esercizio);
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
	 * <!-- @TODO: da completare -->
	 * Imposta il valore della proprietà 'fl_aggiorna_scad_successiva'
	 *
	 * @param newFl_aggiorna_scad_successiva	Il valore da assegnare a 'fl_aggiorna_scad_successiva'
	 */
	public void setFl_aggiorna_scad_successiva(java.lang.Boolean newFl_aggiorna_scad_successiva) {
		fl_aggiorna_scad_successiva = newFl_aggiorna_scad_successiva;
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Imposta il valore della proprietà 'fromDocAmm'
	 *
	 * @param newFromDocAmm	Il valore da assegnare a 'fromDocAmm'
	 */
	public void setFromDocAmm(boolean newFromDocAmm) {
		fromDocAmm = newFromDocAmm;
	}
	public void setEsercizio_originale(java.lang.Integer esercizio_originale) {
		this.getAccertamento().setEsercizio_originale(esercizio_originale);
	}
	public void setPg_accertamento(java.lang.Long pg_accertamento) {
		this.getAccertamento().setPg_accertamento(pg_accertamento);
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
	 * <!-- @TODO: da completare -->
	 * Imposta il valore della proprietà 'scadenza_iniziale'
	 *
	 * @param newScadenza_iniziale	Il valore da assegnare a 'scadenza_iniziale'
	 */
	public void setScadenza_iniziale(Accertamento_scadenzarioBulk newScadenza_iniziale) {
		scadenza_iniziale = newScadenza_iniziale;
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Imposta il valore della proprietà 'status'
	 *
	 * @param newStatus	Il valore da assegnare a 'status'
	 */
	public void setStatus(int newStatus) {
		status = newStatus;
	}
	/**
	 * @param newTipoDocumentoKeys java.util.Dictionary
	 */
	public void setTipoDocumentoKeys(java.util.Dictionary newTipoDocumentoKeys) {
		tipoDocumentoKeys = newTipoDocumentoKeys;
	}
	public void setToBeDeleted() 
	{
		super.setToBeDeleted();
		for ( Iterator i = accertamento_scad_voceColl.iterator(); i.hasNext(); )
			((Accertamento_scad_voceBulk) i.next()).setToBeDeleted();
		for ( Iterator i = accertamento_scad_voceColl.deleteIterator(); i.hasNext(); )
			((Accertamento_scad_voceBulk) i.next()).setToBeDeleted();

	}
	/**
	 * <!-- @TODO: da completare -->
	 * 
	 *
	 */
	public void storna() 
	{
		setIm_scadenza( new java.math.BigDecimal(0));
		setToBeUpdated();

		for ( Iterator i = accertamento_scad_voceColl.iterator(); i.hasNext(); )
			((Accertamento_scad_voceBulk) i.next()).storna();
		for ( Iterator i = accertamento_scad_voceColl.deleteIterator(); i.hasNext(); )
			((Accertamento_scad_voceBulk) i.next()).storna();
	}
	/**
	 * <!-- @TODO: da completare -->
	 * 
	 *
	 * @param docAmmBP	
	 * @throws ValidationException	
	 */
	public void validaImporto(it.cnr.contab.docamm00.bp.IDocumentoAmministrativoBP docAmmBP) throws ValidationException
	{
		if(getIm_scadenza() == null)
			throw new ValidationException( "Valorizzare l' IMPORTO della scadenza." );

		/* rimosso questo controllo per consentire comunque l'inserimento/modifica dell'importo a 0 -
	   questo è utile quando è già stato creato il mandato e pertanto non è possibile effettuare la cancellazione
	   fisica della scadenza (per i vincoli di integrità referenziale con le righe del mandato, pertanto 
	   l'unica alternativa è quella di impostare a 0 la scadenza */
		/*	
	// L'importo della scadenza puo' essere minore o uguale a zero se vengo da NOTA di CREDITO
	// STM 1063
	if(	(getIm_scadenza().compareTo(new java.math.BigDecimal(0)) < 1) &&
		((docAmmBP == null)||(!(docAmmBP instanceof it.cnr.contab.docamm00.bp.CRUDNotaDiCreditoAttivaBP))))
		throw new ValidationException( "L' IMPORTO della scadenza deve essere > 0." );		
		 */
		if(getIm_scadenza().compareTo(new java.math.BigDecimal(0)) < 0)
			throw new ValidationException( "L' IMPORTO della scadenza non puo' essere negativo." );	
	}
	/**
	 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
	 * controllo sintattico o contestuale.
	 */
	public void validate() throws ValidationException 
	{
		super.validate();

		// Controllo su campo DATA SCADENZA
		if ( getDt_scadenza_incasso() == null  )
			throw new ValidationException( "Il campo DATA SCADENZA è obbligatorio." );
		if ( getDt_scadenza_incasso().before(accertamento.getDt_registrazione()) )
			throw new ValidationException( "Non è possibile inserire una scadenza con data antecedente a quella dell'accertamento." );

		if (accertamento.getEsercizio_competenza() == null )
			throw new ValidationException( "E' necessario specificare un esercizio competenza per l'accertamento prima di inserire le scadenze");
		java.util.GregorianCalendar gc = (java.util.GregorianCalendar)java.util.GregorianCalendar.getInstance();
		gc.setTime(getDt_scadenza_incasso());
		if ( (getAccertamento().getCd_tipo_documento_cont() == null || !getAccertamento().getCd_tipo_documento_cont().equals( Numerazione_doc_contBulk.TIPO_ACR_RES)) &&
				gc.get(java.util.GregorianCalendar.YEAR) < accertamento.getEsercizio_competenza().intValue())
			throw new ValidationException( "Non è possibile inserire una scadenza con data antecedente a quella dell'esercizio di competenza." );

		// Controllo su campo IMPORTO SCADENZA
		if(getIm_scadenza() == null)
			throw new ValidationException( "Valorizzare l' IMPORTO della scadenza." );

		// Controllo su campo DESCRIZIONE
		if ( getDs_scadenza() == null || getDs_scadenza().equals("") )
			throw new ValidationException( "Il campo DESCRIZIONE è obbligatorio." );
	}
	
	public java.math.BigDecimal getImportoDisponibile() {
		return Optional.ofNullable(getIm_scadenza()).map(map -> map).orElse(BigDecimal.ZERO).
				subtract(Optional.ofNullable(getIm_associato_doc_amm()).map(map -> map).orElse(BigDecimal.ZERO));
	}

	public java.math.BigDecimal getImportoNonIncassato() {
		return Optional.ofNullable(getIm_scadenza()).map(map -> map).orElse(BigDecimal.ZERO).
				subtract(Optional.ofNullable(getIm_associato_doc_contabile()).map(map -> map).orElse(BigDecimal.ZERO));
	}
	public PendenzaPagopaBulk getScadenzaPagopa() {
		return scadenzaPagopa;
	}

	public void setScadenzaPagopa(PendenzaPagopaBulk scadenzaPagopa) {
		this.scadenzaPagopa = scadenzaPagopa;
	}

	@Override
	public Long getIdScadenzaPagopa() {
		return Optional.ofNullable(getScadenzaPagopa())
				.map(PendenzaPagopaBulk::getId)
				.orElse(null);
	}

	@Override
	public void setIdScadenzaPagopa(Long idScadenzaPagopa) {
		Optional.ofNullable(getScadenzaPagopa()).ifPresent(el->el.setId(idScadenzaPagopa));
	}
}
