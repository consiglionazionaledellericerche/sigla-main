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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.cnr.contab.config00.bulk.CigBulk;
import it.cnr.contab.doccont00.core.DatiFinanziariScadenzeDTO;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.siopeplus.StMotivoEsclusioneCigSiope;
@JsonInclude(value=Include.NON_NULL)
public class Obbligazione_scadenzarioBulk extends Obbligazione_scadenzarioBase implements Cloneable, IScadenzaDocumentoContabileBulk {

	private BulkList obbligazione_scad_voceColl = new BulkList();
	private ObbligazioneBulk obbligazione = new ObbligazioneBulk();	
	
	protected java.lang.Boolean fl_aggiorna_scad_successiva;

	protected Obbligazione_scadenzarioBulk scadenza_iniziale;

	public static final int STATUS_NOT_CONFIRMED = 0;
	public static final int STATUS_CONFIRMED = 1;

	private int status = STATUS_CONFIRMED;

	private Integer esercizio_mandato;
	private java.lang.Long pg_mandato;

	private DatiFinanziariScadenzeDTO datiFinanziariScadenzeDTO;
	private Integer esercizio_doc_passivo;
	private java.lang.Long pg_doc_passivo;
	private String cd_tipo_documento_amm;
	private java.util.Hashtable tipoDocumentoKeys;	
	private CigBulk cig = new CigBulk();
	private String motivo_assenza_cig;

	private boolean fromDocAmm = false;

	private java.math.BigDecimal importoDisponibile;
/**
 * Obbligazione_scadenzarioBulk constructor comment.
 */
public Obbligazione_scadenzarioBulk() {
	super();
}
/**
 * Obbligazione_scadenzarioBulk constructor comment.
 * @param obbligazione L'obbligazione a cui è associato il dettaglio
 */
public Obbligazione_scadenzarioBulk(ObbligazioneBulk obbligazione) 
{
	setObbligazione(obbligazione);
	setEsercizio( obbligazione.getEsercizio() );
	setEsercizio_originale( obbligazione.getEsercizio_originale() );
	setPg_obbligazione( obbligazione.getPg_obbligazione() );
	setCd_cds( obbligazione.getCd_cds() );
}
/**
 * Obbligazione_scadenzarioBulk constructor comment.
 * @param cd_cds Il centro di spesa
 * @param esercizio L'esercizio
 * @param esercizio_originale L'esercizio originale dell'impegno
 * @param pg_obbligazione Il numero dell'obbligazione
 * @param pg_obbligazione_scadenzario Il numero del dettaglio dell'obbligazione
 */
public Obbligazione_scadenzarioBulk(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.Integer esercizio_originale,java.lang.Long pg_obbligazione,java.lang.Long pg_obbligazione_scadenzario) {
	super(cd_cds,esercizio,esercizio_originale,pg_obbligazione,pg_obbligazione_scadenzario);
	setObbligazione(new it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk(cd_cds,esercizio,esercizio_originale,pg_obbligazione));
}
public Object clone() 
{
	Obbligazione_scadenzarioBulk nuova = new Obbligazione_scadenzarioBulk();
	
	nuova.setEsercizio( getEsercizio());
	nuova.setFl_aggiorna_scad_successiva( getFl_aggiorna_scad_successiva());
	nuova.setIm_scadenza( getIm_scadenza());
	
	
	
	return nuova;
}
public void completeFrom(it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attiva_rigaBulk dettaglio) {

	setDs_scadenza(dettaglio.getDs_riga_fattura());

	java.sql.Timestamp dtScadenza = dettaglio.getNotaDiCredito().getDt_scadenza();
	if (dtScadenza == null)
		dtScadenza = dettaglio.getNotaDiCredito().getDt_registrazione();
	setDt_scadenza(dtScadenza);

	java.math.BigDecimal imTotaleDettaglio = dettaglio.getIm_imponibile().add(dettaglio.getIm_iva());
	imTotaleDettaglio = imTotaleDettaglio.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
	setIm_associato_doc_amm(imTotaleDettaglio);
	setIm_scadenza(imTotaleDettaglio);
}
/**
 * Restituisce un array di <code>BulkCollection</code> contenenti oggetti
 * bulk da rendere persistenti insieme al ricevente.
 * L'implementazione standard restituisce <code>null</code>.
 * @see it.cnr.jada.comp.GenericComponent#makeBulkPersistent
 */ 
public BulkCollection[] getBulkLists() {
	return new it.cnr.jada.bulk.BulkCollection[] { 
			obbligazione_scad_voceColl  };
}
public java.lang.String getCd_cds() {
	it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = this.getObbligazione();
	if (obbligazione == null)
		return null;
	it.cnr.contab.config00.sto.bulk.CdsBulk cds = obbligazione.getCds();
	if (cds == null)
		return null;
	return cds.getCd_unita_organizzativa();
}
/**
 * @return java.lang.String
 */
public java.lang.String getCd_tipo_documento_amm() {
	return cd_tipo_documento_amm;
}
public java.lang.String getCd_vocePerImpegno() 
{
	if ( obbligazione_scad_voceColl != null && obbligazione_scad_voceColl.size() > 0 )
		return ((Obbligazione_scad_voceBulk)obbligazione_scad_voceColl.get(0)).getCd_voce();
	return null;
}
public java.lang.Integer getEsercizio() {
	it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = this.getObbligazione();
	if (obbligazione == null)
		return null;
	return obbligazione.getEsercizio();
}
public java.lang.Integer getEsercizio_doc_passivo() {
	return esercizio_doc_passivo;
}
public java.lang.Integer getEsercizio_mandato() {
	return esercizio_mandato;
}
/**
 * @return java.lang.Integer
 */
public IDocumentoContabileBulk getFather() {
	return getObbligazione();
}
public java.lang.Boolean getFl_aggiorna_scad_successiva() {
	return fl_aggiorna_scad_successiva;
}
/**
 * Insert the method's description here.
 * Creation date: (24/07/2002 15.10.20)
 * @author: Alfonso Ardire
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getImportoDisponibile() {	
	return Optional.ofNullable(getIm_scadenza()).orElse(BigDecimal.ZERO).subtract(
			Optional.ofNullable(getIm_associato_doc_amm()).orElse(BigDecimal.ZERO));
}
/**
 * @return it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk
 */
public ObbligazioneBulk getObbligazione() {
	return obbligazione;
}
/**
 * @return it.cnr.jada.bulk.BulkCollection
 */
public it.cnr.jada.bulk.BulkList<Obbligazione_scad_voceBulk> getObbligazione_scad_voceColl() {
	return obbligazione_scad_voceColl;
}
public java.lang.Long getPg_doc_passivo() {
	return pg_doc_passivo;
}
public java.lang.Long getPg_mandato() {
	return pg_mandato;
}
public java.lang.Integer getEsercizio_originale() {
	it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = this.getObbligazione();
	if (obbligazione == null)
		return null;
	return obbligazione.getEsercizio_originale();
}
public java.lang.Long getPg_obbligazione() {
	it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = this.getObbligazione();
	if (obbligazione == null)
		return null;
	return obbligazione.getPg_obbligazione();
}
public Obbligazione_scadenzarioBulk getScadenza_iniziale() {
	return scadenza_iniziale;
}
public int getStatus() {
	return status;
}
/**
 * @return java.util.Hashtable
 */
public java.util.Hashtable getTipoDocumentoKeys() {
	return tipoDocumentoKeys;
}

public final static Map<String,String> motivoEsclusioneCigSIOPEKeys = Arrays.asList(StMotivoEsclusioneCigSiope.values())
.stream()
.collect(Collectors.toMap(
        StMotivoEsclusioneCigSiope::name,
        StMotivoEsclusioneCigSiope::value,
        (oldValue, newValue) -> oldValue,
        Hashtable::new
));


public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) 
{
	Obbligazione_scadenzarioBulk os = (Obbligazione_scadenzarioBulk) super.initializeForInsert( bp, context );
	os.setStatus( os.STATUS_NOT_CONFIRMED );
	os.setFlAssociataOrdine(Boolean.FALSE);
	os.setFlScollegaDocumenti(Boolean.FALSE);
	return os;
}
public boolean isFromDocAmm() {
	return fromDocAmm;
}
public BulkList ordinaPerDataScadenza( BulkList collScadenze ) 
{
	// riordino la lista delle scadenze per data scadenza incasso
	
	Collections.sort(collScadenze,new Comparator() {	

		public int compare(Object o1, Object o2) 
		{
			Obbligazione_scadenzarioBulk os1 = (Obbligazione_scadenzarioBulk) o1;
			Obbligazione_scadenzarioBulk os2 = (Obbligazione_scadenzarioBulk) o2;
			
			return os1.getDt_scadenza().compareTo( os2.getDt_scadenza());
		}
		public boolean equals(Object o)  
		{
			return (getDt_scadenza() == ((Obbligazione_scadenzarioBulk)o).getDt_scadenza());
		}
	});

	return collScadenze;
}
public void setCd_cds(java.lang.String cd_cds) {
	this.getObbligazione().getCds().setCd_unita_organizzativa(cd_cds);
}
/**
 * @param newCd_tipo_documento_amm java.lang.String
 */
public void setCd_tipo_documento_amm(java.lang.String newCd_tipo_documento_amm) {
	cd_tipo_documento_amm = newCd_tipo_documento_amm;
}
public void setEsercizio(java.lang.Integer esercizio) {
	this.getObbligazione().setEsercizio(esercizio);
}
public void setEsercizio_doc_passivo(java.lang.Integer newEsercizio_doc_passivo) {
	esercizio_doc_passivo = newEsercizio_doc_passivo;
}
public void setEsercizio_mandato(java.lang.Integer newEsercizio_mandato) {
	esercizio_mandato = newEsercizio_mandato;
}
public void setFl_aggiorna_scad_successiva(java.lang.Boolean newFl_aggiorna_scad_successiva) {
	fl_aggiorna_scad_successiva = newFl_aggiorna_scad_successiva;
}
public void setFromDocAmm(boolean newFromDocAmm) {
	fromDocAmm = newFromDocAmm;
}
/**
 * @param newObbligazione it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk
 */
public void setObbligazione(ObbligazioneBulk newObbligazione) {
	obbligazione = newObbligazione;
}
/**
 * @param newObbligazione_scad_voceColl it.cnr.jada.bulk.BulkCollection
 */
public void setObbligazione_scad_voceColl(it.cnr.jada.bulk.BulkList newObbligazione_scad_voceColl) {
	obbligazione_scad_voceColl = newObbligazione_scad_voceColl;
}
public void setPg_doc_passivo(java.lang.Long newPg_doc_passivo) {
	pg_doc_passivo = newPg_doc_passivo;
}
public void setPg_mandato(java.lang.Long newPg_mandato) {
	pg_mandato = newPg_mandato;
}
public void setEsercizio_originale(java.lang.Integer esercizio_originale) {
	this.getObbligazione().setEsercizio_originale(esercizio_originale);
}
public void setPg_obbligazione(java.lang.Long pg_obbligazione) {
	this.getObbligazione().setPg_obbligazione(pg_obbligazione);
}
public void setScadenza_iniziale(Obbligazione_scadenzarioBulk newScadenza_iniziale) {
	scadenza_iniziale = newScadenza_iniziale;
}
public void setStatus(int newStatus) {
	status = newStatus;
}
/**
 * @param newTipoDocumentoKeys java.util.Hashtable
 */
public void setTipoDocumentoKeys(java.util.Hashtable newTipoDocumentoKeys) {
	tipoDocumentoKeys = newTipoDocumentoKeys;
}
public void setToBeDeleted() 
{
	super.setToBeDeleted();
	for ( Iterator i = obbligazione_scad_voceColl.iterator(); i.hasNext(); )
		((Obbligazione_scad_voceBulk) i.next()).setToBeDeleted();
	for ( Iterator i = obbligazione_scad_voceColl.deleteIterator(); i.hasNext(); )
		((Obbligazione_scad_voceBulk) i.next()).setToBeDeleted();
		
}
// Metodo per stornare gli importi della scadenza dell'obbligazione
public void storna() 
{
	setIm_scadenza( new java.math.BigDecimal(0));
	setToBeUpdated();
	for ( Iterator i = obbligazione_scad_voceColl.iterator(); i.hasNext(); )
		((Obbligazione_scad_voceBulk) i.next()).storna();
	for ( Iterator i = obbligazione_scad_voceColl.deleteIterator(); i.hasNext(); )
		((Obbligazione_scad_voceBulk) i.next()).storna();

}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws ValidationException {
	super.validate();

	if (getFlAssociataOrdine() == null)
		setFlAssociataOrdine(Boolean.FALSE);
	if (getFlScollegaDocumenti() == null)
		setFlScollegaDocumenti(Boolean.FALSE);

	// controllo su campo DATA SCADENZA
	if ( getDt_scadenza() == null || getDt_scadenza().equals("") )
		throw new ValidationException( "Il campo DATA SCADENZA è obbligatorio." );

	if ( //  data obbligazione != data scadenza && data_obbligazione >= data_scadenza
		!(obbligazione.getDt_registrazione().after( getDt_scadenza() ) && obbligazione.getDt_registrazione().before( getDt_scadenza() )) &&
		  obbligazione.getDt_registrazione().after( getDt_scadenza() ))
		throw new ValidationException( "Non è possibile inserire una scadenza con data antecedente a quella di registrazione dell'impegno." );

	java.util.GregorianCalendar gc = (java.util.GregorianCalendar)java.util.GregorianCalendar.getInstance();
	gc.setTime(getDt_scadenza());
	if (gc.get(java.util.GregorianCalendar.YEAR) < obbligazione.getEsercizio_competenza().intValue())
		throw new ValidationException( "Non è possibile inserire una scadenza con data antecedente a quella dell'esercizio di competenza." );

	// controllo su campo IMPORTO SCADENZA
	if ( getIm_scadenza() == null  )
		throw new ValidationException( "Il campo IMPORTO SCADENZA è obbligatorio." );

		
	// controllo su campo DESCRIZIONE
	if ( getDs_scadenza() == null || getDs_scadenza().equals("") )
		throw new ValidationException( "Il campo DESCRIZIONE è obbligatorio." );


	if ( obbligazione.getFl_calcolo_automatico() != null && !obbligazione.getFl_calcolo_automatico().booleanValue())
		for ( Iterator i = obbligazione_scad_voceColl.iterator(); i.hasNext(); )
			((Obbligazione_scad_voceBulk) i.next()).validate();
}
public DatiFinanziariScadenzeDTO getDatiFinanziariScadenzeDTO() {
	return datiFinanziariScadenzeDTO;
}
public void setDatiFinanziariScadenzeDTO(DatiFinanziariScadenzeDTO datiFinanziariScadenzeDTO) {
	this.datiFinanziariScadenzeDTO = datiFinanziariScadenzeDTO;
}
public java.math.BigDecimal getImportoNonPagato() {
	return Optional.ofNullable(getIm_scadenza()).map(map -> map).orElse(BigDecimal.ZERO).
			subtract(Optional.ofNullable(getIm_associato_doc_contabile()).map(map -> map).orElse(BigDecimal.ZERO));
}
public CigBulk getCig() {
	return cig;
}
public void setCig(CigBulk cig) {
	if (cig == null){
		cig = new CigBulk();
	}
	
	this.cig = cig;
}
public String getMotivo_assenza_cig() {
	return motivo_assenza_cig;
}
public void setMotivo_assenza_cig(String motivo_assenza_cig) {
	this.motivo_assenza_cig = motivo_assenza_cig;
}
}
