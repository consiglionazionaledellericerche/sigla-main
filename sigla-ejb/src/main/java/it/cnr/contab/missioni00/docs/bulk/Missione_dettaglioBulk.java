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

package it.cnr.contab.missioni00.docs.bulk;

import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.contab.missioni00.tabrif.bulk.Missione_diariaBulk;
import it.cnr.contab.missioni00.tabrif.bulk.Missione_rimborso_kmBulk;
import it.cnr.contab.missioni00.tabrif.bulk.Missione_tipo_pastoBulk;
import it.cnr.contab.missioni00.tabrif.bulk.Missione_tipo_spesaBulk;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.StrServ;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(value=Include.NON_NULL)
public class Missione_dettaglioBulk extends Missione_dettaglioBase 
{
	public final static String TIPO_DIARIA = "D";
	public final static String TIPO_SPESA = "S";		
	public final static String TIPO_RIMBORSO = "R";
	@JsonIgnore
	protected MissioneBulk missione;

	/********************* SPESA *****************************/
	@JsonIgnore
	private DivisaBulk divisa_spesa = new DivisaBulk();

	@JsonIgnore
	protected Missione_tipo_spesaBulk tipo_spesa;
	@JsonIgnore
	protected Missione_tipo_pastoBulk tipo_pasto;
	@JsonIgnore
	protected Missione_rimborso_kmBulk tipo_auto;

	private final static java.util.Dictionary tipoAutoKeys;
	public final static String TIPO_P       = "P";
	public final static String TIPO_A       = "A";
	static 
	{
		tipoAutoKeys = new it.cnr.jada.util.OrderedHashtable();
		tipoAutoKeys.put(TIPO_P,"Propria");
		tipoAutoKeys.put(TIPO_A,"Amministrativa");
	}

	/**** Gestione edita, conferma, annulla dettaglio di spesa ***/	
	public static final int STATUS_NOT_CONFIRMED = 0;
	public static final int STATUS_CONFIRMED = 1;
	@JsonIgnore
	private String allegatiDocumentale;
	@JsonIgnore
	private BulkList<AllegatoMissioneDettaglioSpesaBulk> dettaglioSpesaAllegati = new BulkList<AllegatoMissioneDettaglioSpesaBulk>();
	private int status = STATUS_NOT_CONFIRMED;
	@JsonIgnore
	protected Missione_dettaglioBulk spesaIniziale;
	/***************************************************************/


	/********************* DIARIA *****************************/
	@JsonIgnore
	protected Missione_diariaBulk diaria;
	public Missione_dettaglioBulk() {
		super();
	}
	public Missione_dettaglioBulk(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.sql.Timestamp dt_inizio_tappa,java.lang.Integer esercizio,java.lang.Long pg_missione,java.lang.Long pg_riga) {
		super(cd_cds,cd_unita_organizzativa,dt_inizio_tappa,esercizio,pg_missione,pg_riga);
	}
	//
	// Calcolo l' importo di una spesa con rimborso chilometrico.
	// importo = chilometri * indennita
	//

	public void calcolaImportoRimborsoKm()
	{
		java.math.BigDecimal importoTotaleAuto = getChilometri().multiply(getIndennita_chilometrica());

		importoTotaleAuto = importoTotaleAuto.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
		setIm_spesa_divisa(importoTotaleAuto);
		setIm_spesa_euro(importoTotaleAuto);
		setIm_totale_spesa(importoTotaleAuto);

		return;
	}	
	//
	//	Se la spesa selezionata e' un trasporto pongo :
	//	im_maggiorazione = percentuale maggiorazione * importo base maggiorazione
	//

	public void calcolaMaggiorazioneTrasporto()
	{
		if(getIm_base_maggiorazione() == null)
			setIm_base_maggiorazione(new java.math.BigDecimal(0));

		setIm_maggiorazione(getPercentuale_maggiorazione().multiply(getIm_base_maggiorazione()).divide(new java.math.BigDecimal(100), 2, java.math.BigDecimal.ROUND_HALF_UP));
		setIm_maggiorazione(getIm_maggiorazione().setScale(2, java.math.BigDecimal.ROUND_HALF_UP));

		return;
	}	
	public void convertiMaggiorazioneInEuro(DivisaBulk divisaDefault) 
	{
		setIm_maggiorazione_euro(new java.math.BigDecimal(0));

		if(getCd_divisa_spesa().equals(divisaDefault.getCd_divisa()))		
			setIm_maggiorazione_euro(getIm_maggiorazione());		// la maggiorazione e' gia' in EURO
		else
		{
			// Converto l'importo della maggiorazione da valuta straniera (CD_DIVISA) a EURO
			if(getDivisa_spesa().getFl_calcola_con_diviso().booleanValue())
				setIm_maggiorazione_euro(getIm_maggiorazione().divide(getCambio_spesa(), 2, java.math.BigDecimal.ROUND_HALF_UP));
			else 
				setIm_maggiorazione_euro(getIm_maggiorazione().multiply(getCambio_spesa()));
		}
		setIm_maggiorazione_euro(getIm_maggiorazione_euro().setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
		return ;
	}
	public java.lang.String getCd_divisa_spesa() {
		it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk divisa_spesa = this.getDivisa_spesa();
		if (divisa_spesa == null)
			return null;
		return divisa_spesa.getCd_divisa();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (14/03/2002 16.44.24)
	 * @return it.cnr.contab.missioni00.tabrif.bulk.Missione_diariaBulk
	 */
	public it.cnr.contab.missioni00.tabrif.bulk.Missione_diariaBulk getDiaria() {
		return diaria;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (13/03/2002 11.10.28)
	 * @return it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk
	 */
	public it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk getDivisa_spesa() {
		return divisa_spesa;
	}
	public java.math.BigDecimal getImportoSpesaEuro(DivisaBulk divisaDefault) 
	{
		java.math.BigDecimal importoSpesaEuro = new java.math.BigDecimal(0);

		if(getCd_divisa_spesa().equals(divisaDefault.getCd_divisa()))		
			importoSpesaEuro = getIm_spesa_divisa();		// l'importo della spesa e' stato inserito in EURO
		else
		{
			// Converto l'importo della spesa inserito da valuta straniera a euro
			setIm_spesa_divisa(getIm_spesa_divisa().setScale(2, java.math.BigDecimal.ROUND_HALF_UP));

			if(getDivisa_spesa().getFl_calcola_con_diviso().booleanValue())
				importoSpesaEuro = getIm_spesa_divisa().divide(getCambio_spesa(), java.math.BigDecimal.ROUND_HALF_UP);
			else 
				importoSpesaEuro = getIm_spesa_divisa().multiply(getCambio_spesa());
		}
		importoSpesaEuro = importoSpesaEuro.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
		return importoSpesaEuro;
	}
	public java.math.BigDecimal getIndennita_chilometrica()
	{
		if(getTipo_auto() == null)
			return new java.math.BigDecimal(0);	

		return getTipo_auto().getIndennita_chilometrica();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (07/02/2002 14.14.40)
	 * @return it.cnr.contab.missioni00.docs.bulk.MissioneBulk
	 */
	public MissioneBulk getMissione() {
		return missione;
	}

	@Override
	public java.lang.Long getPg_missione() 
	{
			MissioneBulk missione = this.getMissione();
			if (missione == null)
				return null;
			return missione.getPg_missione();
	}
	@Override
	public java.lang.Integer getEsercizio(){
		MissioneBulk missione = this.getMissione();
		if (missione == null)
			return null;
		return missione.getEsercizio();
	}
	@Override
	public java.lang.String getCd_cds(){
		MissioneBulk missione = this.getMissione();
		if (missione == null)
			return null;
		return missione.getCd_cds();
	}
	@Override
	public java.lang.String getCd_unita_organizzativa(){
		MissioneBulk missione = this.getMissione();
		if (missione == null)
			return null;
		return missione.getCd_unita_organizzativa();
	}
	@Override
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
		this.getMissione().setCd_unita_organizzativa(cd_unita_organizzativa);
	}
	@Override
	public void setCd_cds(java.lang.String cd_cds) {
		this.getMissione().setCd_cds(cd_cds);
	}
	@Override
	public void setEsercizio(java.lang.Integer esercizio) {
		this.getMissione().setEsercizio(esercizio);
	}
	@Override
	public void setPg_missione(java.lang.Long pg_missione) {
		this.getMissione().setPg_missione(pg_missione);
	}
	
	/**
	 * Insert the method's description here.
	 * Creation date: (13/03/2002 16.02.59)
	 * @return it.cnr.contab.missioni00.docs.bulk.Missione_dettaglioBulk
	 */
	public Missione_dettaglioBulk getSpesaIniziale() {
		return spesaIniziale;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (13/03/2002 16.43.11)
	 * @return int
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (13/03/2002 12.14.52)
	 * @return it.cnr.contab.missioni00.tabrif.bulk.Missione_rimborso_kmBulk
	 */
	public it.cnr.contab.missioni00.tabrif.bulk.Missione_rimborso_kmBulk getTipo_auto() {
		return tipo_auto;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (13/03/2002 12.13.39)
	 * @return it.cnr.contab.missioni00.tabrif.bulk.Missione_tipo_pastoBulk
	 */
	public it.cnr.contab.missioni00.tabrif.bulk.Missione_tipo_pastoBulk getTipo_pasto() {
		return tipo_pasto;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (11/03/2002 15.34.32)
	 * @return it.cnr.contab.missioni00.tabrif.bulk.Missione_tipo_spesaBulk
	 */
	public it.cnr.contab.missioni00.tabrif.bulk.Missione_tipo_spesaBulk getTipo_spesa() {
		return tipo_spesa;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (13/06/2002 12.03.54)
	 * @return java.util.Dictionary
	 */
	public final static java.util.Dictionary getTipoAutoKeys() {
		return tipoAutoKeys;
	}
	public void impostaTipologiaSpesa()
	{
		if(isRimborsoKm())
			setTi_cd_ti_spesa("R");
		else if(isPasto())
			setTi_cd_ti_spesa("P");
		else if(isTrasporto())		
			setTi_cd_ti_spesa("T");
		else if(isAlloggio())
			setTi_cd_ti_spesa("A");	
		else
			setTi_cd_ti_spesa("N");				
	}
	//
	//	Inizializzo le variabili settate alla selezione di un tipo spesa
	//

	public void inizializzaPerCambioTipoSpesa(Missione_tipo_spesaBulk oldTipoSpesa, Missione_tipo_spesaBulk newTipoSpesa) 
	{
		if((newTipoSpesa == null) || (newTipoSpesa.getCd_ti_spesa() == null))
		{
			setCd_ti_spesa(null);
			setCd_ti_pasto(null);		
			setTipo_pasto(new Missione_tipo_pastoBulk());
			setTi_auto(null);
			setTipo_auto(new Missione_rimborso_kmBulk());
			setIm_base_maggiorazione(new java.math.BigDecimal(0));
		}
		if(oldTipoSpesa!=null && newTipoSpesa!=null && newTipoSpesa.getFl_rimborso_km()!=null && 
				newTipoSpesa.getFl_rimborso_km().booleanValue())
		{
			setIm_spesa_divisa(new java.math.BigDecimal(0));				
		}		
		if(oldTipoSpesa!=null && oldTipoSpesa.getFl_rimborso_km()!=null && 
				oldTipoSpesa.getFl_rimborso_km().booleanValue())
		{
			setIm_spesa_divisa(new java.math.BigDecimal(0));		
			setDivisa_spesa(new DivisaBulk());
			setCambio_spesa(null);
		}	
	}
	public boolean isAlloggio()
	{
		if(getTipo_spesa() == null)
			return false;

		if((getTipo_spesa().getFl_alloggio() != null) && (getTipo_spesa().getFl_alloggio().booleanValue()))
			return true;

		return false;	
	}
	public boolean isPasto()
	{
		if(getTipo_spesa() == null)
			return false;

		if((getTipo_spesa().getFl_pasto() != null) && (getTipo_spesa().getFl_pasto().booleanValue()))
			return true;

		return false;	
	}
	public boolean isRimborsoKm()
	{
		if(getTipo_spesa() == null)
			return false;

		if((getTipo_spesa().getFl_rimborso_km() != null) && (getTipo_spesa().getFl_rimborso_km().booleanValue()))
			return true;

		return false;	
	}
	public boolean isRODivisa_spesa() 
	{
		return divisa_spesa == null || divisa_spesa.getCrudStatus() == NORMAL; 
	}
	public boolean isROImportiDiaria()
	{
		if(getDt_inizio_tappa()==null)
			return false;

		if(getMissione() == null)
			return false;
		
		if(getMissione().getTappeMissioneHash() == null)
			return false;

		Missione_tappaBulk tappa = (Missione_tappaBulk)getMissione().getTappeMissioneHash().get(getDt_inizio_tappa());
		if(tappa != null &&tappa.getFl_no_diaria() != null && tappa.getFl_no_diaria().booleanValue())
			return true;

		return false;
	}
	public boolean isMissioneFromGemis()
	{
		if(getMissione() != null && getMissione().isMissioneFromGemis() )
			return true;

		return false;
	}
	public boolean isROTipo_auto() 
	{
		return tipo_auto == null || tipo_auto.getCrudStatus() == NORMAL; 
	}
	public boolean isROTipo_pasto() 
	{
		return tipo_pasto == null || tipo_pasto.getCrudStatus() == NORMAL; 
	}
	public boolean isROTipo_spesa() 
	{
		return tipo_spesa == null || tipo_spesa.getCrudStatus() == NORMAL; 
	}
	public boolean isSpesaAnticipata()
	{
		if(getFl_spesa_anticipata() == null)
			return false;

		if(getFl_spesa_anticipata().booleanValue())
			return true;

		return false;	
	}
	public boolean isTrasporto()
	{
		if(getTipo_spesa() == null)
			return false;

		if((getTipo_spesa().getFl_trasporto() != null) && (getTipo_spesa().getFl_trasporto().booleanValue()))
			return true;

		return false;	
	}
	public void setCd_divisa_spesa(java.lang.String cd_divisa_spesa) {
		this.getDivisa_spesa().setCd_divisa(cd_divisa_spesa);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (14/03/2002 16.44.24)
	 * @param newDiaria it.cnr.contab.missioni00.tabrif.bulk.Missione_diariaBulk
	 */
	public void setDiaria(it.cnr.contab.missioni00.tabrif.bulk.Missione_diariaBulk newDiaria) {
		diaria = newDiaria;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (13/03/2002 11.10.28)
	 * @param newDivisa_spesa it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk
	 */
	public void setDivisa_spesa(it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk newDivisa_spesa) 
	{
		divisa_spesa = newDivisa_spesa;
		if((divisa_spesa == null) || (divisa_spesa.getCd_divisa() == null))
			setCambio_spesa(null);	
	}
	public void setIndennita_chilometrica(java.math.BigDecimal indennita_chilometrica) 
	{
		if(this.getTipo_auto() != null)	
			this.getTipo_auto().setIndennita_chilometrica(indennita_chilometrica);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (07/02/2002 14.14.40)
	 * @param newMissione it.cnr.contab.missioni00.docs.bulk.MissioneBulk
	 */
	public void setMissione(MissioneBulk newMissione) {
		missione = newMissione;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (13/03/2002 16.02.59)
	 * @param newSpesaIniziale it.cnr.contab.missioni00.docs.bulk.Missione_dettaglioBulk
	 */
	public void setSpesaIniziale(Missione_dettaglioBulk newSpesaIniziale) {
		spesaIniziale = newSpesaIniziale;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (13/03/2002 16.43.11)
	 * @param newStatus int
	 */
	public void setStatus(int newStatus) {
		status = newStatus;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (13/03/2002 12.14.52)
	 * @param newTipo_auto it.cnr.contab.missioni00.tabrif.bulk.Missione_rimborso_kmBulk
	 */
	public void setTipo_auto(it.cnr.contab.missioni00.tabrif.bulk.Missione_rimborso_kmBulk newTipo_auto) 
	{
		tipo_auto = newTipo_auto;

		if(getTi_auto() == null)
		{
			setIndennita_chilometrica(new java.math.BigDecimal(0));
			setChilometri(new java.math.BigDecimal(0));
		}	
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (13/03/2002 12.13.39)
	 * @param newTipo_pasto it.cnr.contab.missioni00.tabrif.bulk.Missione_tipo_pastoBulk
	 */
	public void setTipo_pasto(it.cnr.contab.missioni00.tabrif.bulk.Missione_tipo_pastoBulk newTipo_pasto) {
		tipo_pasto = newTipo_pasto;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (11/03/2002 15.34.32)
	 * @param newTipo_spesa it.cnr.contab.missioni00.tabrif.bulk.Missione_tipo_spesaBulk
	 */
	public void setTipo_spesa(it.cnr.contab.missioni00.tabrif.bulk.Missione_tipo_spesaBulk newTipo_spesa) 
	{
		inizializzaPerCambioTipoSpesa(tipo_spesa, newTipo_spesa);		
		tipo_spesa = newTipo_spesa;
	} 
	//
	// Validazione dettaglio spesa da Confermare.
	//

	public void validaSpesa() throws ValidationException 
	{
		if(getDt_inizio_tappa() == null)
			throw new ValidationException( "Selezionare il giorno al quale si riferisce la spesa !" );

		if(getCd_ti_spesa() == null) 
			throw new ValidationException( "Selezionare un Tipo Spesa !" );

		// Se la spesa e' un rimborso km viene caricato il cambio e divisa
		// di default (EURO) mentre l'importo e' calcolato (km*indennita')	
		if(!isRimborsoKm())
		{
			if((getIm_spesa_divisa() == null)  || (getIm_spesa_divisa().compareTo(new java.math.BigDecimal(0)) < 1 && !isDettaglioMissioneFromGemis()))
				throw new ValidationException( "L'importo della spesa deve essere > 0!" );

			if (getIm_spesa_divisa().compareTo(new java.math.BigDecimal(0)) < 0){
				throw new ValidationException( "L'importo della spesa non può essere negativo" );
			}
			
			if(getCd_divisa_spesa() == null)
				throw new ValidationException( "Selezionare la valuta!" );

			if((getCambio_spesa() == null) || (getCambio_spesa().compareTo(new java.math.BigDecimal(0)) < 1))
				throw new ValidationException( "Il cambio deve essere > 0 !" );			
		}

		if(getTipo_spesa() == null)
			return;

		if(getTipo_spesa().getFl_giustificativo_richiesto().booleanValue())
		{
			if((getId_giustificativo() == null) && (getDs_giustificativo() == null) && (getDs_no_giustificativo() == null))
				throw new ValidationException( "Il tipo spesa richiede un giustificativo! Valorizzare il campo Id.giustificativo e descrizione giustificativo oppure mancanza giustificativo." );
			if((getId_giustificativo() != null) && (getDs_giustificativo() == null))
				throw new ValidationException( "Il tipo spesa richiede un giustificativo! Valorizzare la descrizione del giustificativo." );
			if((getId_giustificativo() == null) && (getDs_giustificativo() != null))
				throw new ValidationException( "Il tipo spesa richiede un giustificativo! Valorizzare l'identificativo del giustificativo." );			
		}

		if((isPasto()) && (getCd_ti_pasto() == null))
			throw new ValidationException( "Selezionare un tipo pasto !" );

		if((isRimborsoKm()) && (getTi_auto() == null))
			throw new ValidationException( "Selezionare un tipo auto !" );

		if((isRimborsoKm()) && ((getChilometri() == null) || (getChilometri().compareTo(new java.math.BigDecimal(0)) == 0)))		
			throw new ValidationException( "Valorizzare i Km !" );

		if((isRimborsoKm()) && ((getChilometri() == null) || (getChilometri().compareTo(new java.math.BigDecimal(0)) < 0)))		
			throw new ValidationException( "il numero di Km deve essere > 0!" );		
	}
	/**
	 * Il metodo ritorna il valore dell'attributo 'dettaglioSpesaAllegati'
	 */
	public it.cnr.jada.bulk.BulkList<AllegatoMissioneDettaglioSpesaBulk> getDettaglioSpesaAllegati() {
		return dettaglioSpesaAllegati;
	}
	/**
	 * Il metodo imposta il valore dell'attributo 'dettaglioSpesaAllegati'
	 */
	public void setDettaglioSpesaAllegati(it.cnr.jada.bulk.BulkList<AllegatoMissioneDettaglioSpesaBulk> newDettaglioSpesaAllegati) {
		dettaglioSpesaAllegati = newDettaglioSpesaAllegati;
	}
	public String getAllegatiDocumentale() {
		return allegatiDocumentale;
	}
	public void setAllegatiDocumentale(String allegatiDocumentale) {
		this.allegatiDocumentale = allegatiDocumentale;
	}
	/**
	 * Restituisce un array di <code>BulkCollection</code> contenenti oggetti
	 * bulk da rendere persistenti insieme al ricevente.
	 * L'implementazione standard restituisce <code>null</code>.
	 * @see it.cnr.jada.comp.GenericComponent#makeBulkPersistent
	 */ 
	public BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] { 
				dettaglioSpesaAllegati };
	}
	/**
	 * Il metodo rimuove dalla collection dei dettagli di spesa un solo dettaglio
	 */
	public AllegatoMissioneDettaglioSpesaBulk removeFromDettaglioSpesaAllegati(int index) 
	{
		AllegatoMissioneDettaglioSpesaBulk allegato = (AllegatoMissioneDettaglioSpesaBulk)dettaglioSpesaAllegati.remove(index);
		allegato.setToBeDeleted();

		return allegato;
	}
	public int addToDettaglioSpesaAllegati(AllegatoMissioneDettaglioSpesaBulk allegato) {
		dettaglioSpesaAllegati.add(allegato);
		return dettaglioSpesaAllegati.size()-1;		
	}
	public String constructCMISNomeFile() {
		StringBuffer nomeFile = new StringBuffer();
		nomeFile = nomeFile.append(StrServ.lpad(this.getPg_riga().toString(),4,"0"));
		return nomeFile.toString();
	}
	public Boolean isDettaglioMissioneFromGemis(){
		if (getIdFolderDettagliGemis() != null){
			return true;
		}
		return false;
	}
}
