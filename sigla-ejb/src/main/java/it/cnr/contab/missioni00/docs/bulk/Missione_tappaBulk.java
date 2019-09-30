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

import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.OrderedHashtable;

import java.util.Dictionary;
import java.util.Iterator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(value=Include.NON_NULL)
public class Missione_tappaBulk extends Missione_tappaBase 
{
	@JsonIgnore
	protected MissioneBulk missione;
	private NazioneBulk nazione = new NazioneBulk();
	private DivisaBulk divisa_tappa = new DivisaBulk();		

	/**** Gestione comune estero, comune altro e comune proprio ****/
	public final static String COMUNE_ESTERO = "E";
	public final static String COMUNE_ALTRO = "A";
	public final static String COMUNE_PROPRIO = "P";			
	public final static java.util.Dictionary comuneKeys;
	private String comune;	
	static
	{
		comuneKeys = new OrderedHashtable();
		comuneKeys.put(COMUNE_ESTERO, "Comune estero"); 
		comuneKeys.put(COMUNE_PROPRIO, "Comune proprio");
		comuneKeys.put(COMUNE_ALTRO, "Comune altro");		
	}
	/***************************************************************/

	/**** Gestione flag navigazione, flag vitto gratuito, flag alloggio gratuito e flag vitto alloggio gratuito ***/
	public final static String NESSUNO = "X";	
	public final static String NAVIGAZIONE = "N";
	public final static String VITTO_GRATUITO = "V";
	public final static String ALLOGGIO_GRATUITO = "A";
	public final static String VITTO_ALLOGGIO_GRATUITO = "E";				
	public final static Dictionary flag_speseKeys;
	private String flag_spese;	
	static
	{
		flag_speseKeys = new OrderedHashtable();
		flag_speseKeys.put(NESSUNO, "Nessuno");		
		flag_speseKeys.put(NAVIGAZIONE, "Navigazione");
		flag_speseKeys.put(VITTO_GRATUITO, "Vitto gratuito");
		flag_speseKeys.put(ALLOGGIO_GRATUITO, "Alloggio gratuito");
		flag_speseKeys.put(VITTO_ALLOGGIO_GRATUITO, "Vitto e Alloggio gratuito");				
	}
	/***************************************************************/

	/**** Gestione scelta tipo di trattamento di missione ***/
	public final static String DOCUMENTATO = "D";	
	public final static String ALTERNATIVO = "A";
	public final static Dictionary flag_tipo_rimborsoKeys;
	private String flag_tipo_rimborso;	
	static
	{
		flag_tipo_rimborsoKeys = new OrderedHashtable();
		flag_tipo_rimborsoKeys.put(DOCUMENTATO, "Trattamento con rimborso documentato");		
		flag_tipo_rimborsoKeys.put(ALTERNATIVO, "Trattamento alternativo");
	}
	/***************************************************************/

	/**** Gestione edita, conferma, annulla tappa ***/	
	public static final int STATUS_NOT_CONFIRMED = 0;
	public static final int STATUS_CONFIRMED = 1;
	private int status = STATUS_NOT_CONFIRMED;
	protected Missione_tappaBulk tappaIniziale;
	/***************************************************************/	
	public Missione_tappaBulk() {
		super();
	}
	public Missione_tappaBulk(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.sql.Timestamp dt_inizio_tappa,java.lang.Integer esercizio,java.lang.Long pg_missione) {
		super(cd_cds,cd_unita_organizzativa,dt_inizio_tappa,esercizio,pg_missione);
	}
	public java.lang.String getCd_divisa_tappa() {
		it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk divisa_tappa = this.getDivisa_tappa();
		if (divisa_tappa == null)
			return null;
		return divisa_tappa.getCd_divisa();
	}
	//
	// La PROPERTY "Comune" viene usata per gestire il RADIO GROUP dei comuni estero, 
	// altro e proprio
	//

	public java.lang.String getComune() 
	{
		return comune;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (05/03/2002 16.00.59)
	 * @return java.util.Dictionary
	 */
	public final static java.util.Dictionary getComuneKeys() {
		return comuneKeys;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (05/03/2002 16.00.59)
	 * @return it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk
	 */
	public it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk getDivisa_tappa() {
		return divisa_tappa;
	}
	//
	// La PROPERTY "flag_spese" viene usata per gestire il RADIO GROUP di navigazione,
	// vitto gratuito, alloggio gratuito, vitto e alloggio gratuito
	//

	public java.lang.String getFlag_spese() 
	{
		if((getFl_navigazione()==null) && (getFl_vitto_gratuito()==null) && 
				(getFl_alloggio_gratuito()== null) && (getFl_vitto_alloggio_gratuito()== null))
			return NESSUNO;

		if((!getFl_navigazione().booleanValue()) && (!getFl_vitto_gratuito().booleanValue()) && 
				(!getFl_alloggio_gratuito().booleanValue()) && (!getFl_vitto_alloggio_gratuito().booleanValue()))
			return NESSUNO;

		if(getFl_navigazione().booleanValue())
			return NAVIGAZIONE;

		if(getFl_vitto_gratuito().booleanValue())
			return VITTO_GRATUITO;

		if(getFl_alloggio_gratuito().booleanValue())
			return ALLOGGIO_GRATUITO;

		if(getFl_vitto_alloggio_gratuito().booleanValue())
			return VITTO_ALLOGGIO_GRATUITO;		

		return null;		
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (05/03/2002 16.00.59)
	 * @return java.util.Dictionary
	 */
	public final static java.util.Dictionary getFlag_speseKeys() {
		return flag_speseKeys;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (05/03/2002 16.00.59)
	 * @return it.cnr.contab.missioni00.docs.bulk.MissioneBulk
	 */
	public MissioneBulk getMissione() {
		return missione;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (05/03/2002 16.00.59)
	 * @return it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk
	 */
	public it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk getNazione() {
		return nazione;
	}
	public java.lang.Long getPg_missione() 
	{
		MissioneBulk missione = this.getMissione();
		if (missione == null)
			return null;
		return missione.getPg_missione();
	}
	public java.lang.Long getPg_nazione() {
		it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk nazione = this.getNazione();
		if (nazione == null)
			return null;
		return nazione.getPg_nazione();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (05/03/2002 16.00.59)
	 * @return int
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (05/03/2002 16.00.59)
	 * @return it.cnr.contab.missioni00.docs.bulk.Missione_tappaBulk
	 */
	public Missione_tappaBulk getTappaIniziale() {
		return tappaIniziale;
	}
	public boolean isComuneAltro() 
	{
		return (getFl_comune_altro() != null) && (getFl_comune_altro().booleanValue());
	}
	public boolean isComuneProprio() 
	{
		return (getFl_comune_proprio() != null) && (getFl_comune_proprio().booleanValue());
	}
	public boolean isEstera() 
	{
		return (getFl_comune_estero() != null) && (getFl_comune_estero().booleanValue());
	}
	public boolean isRONazione() 
	{
		return nazione == null || nazione.getCrudStatus() == NORMAL; 	
	}
	public void setCd_divisa_tappa(java.lang.String cd_divisa_tappa) {
		this.getDivisa_tappa().setCd_divisa(cd_divisa_tappa);
	}
	//
	// La PROPERTY "Comune" viene usata per gestire il RADIO GROUP dei comuni estero, 
	// altro e proprio
	//

	public void setComune(java.lang.String newComune) 
	{
		comune = newComune;
	}
	public void setDivisa_tappa(DivisaBulk newDivisa_tappa) 
	{
		divisa_tappa = newDivisa_tappa;
		if((newDivisa_tappa == null) || (newDivisa_tappa.getCd_divisa() == null))
			setCambio_tappa(null);	
	}
	//
	// La PROPERTY "flag_spese" viene usata per gestire il RADIO GROUP di navigazione,
	// vitto gratuito, alloggio gratuito, vitto e alloggio gratuito
	//

	public void setFlag_spese(java.lang.String newFlag_spese) 
	{
		if (newFlag_spese.equals(NESSUNO)) 
		{
			setFl_navigazione(new Boolean(false));
			setFl_vitto_gratuito(new Boolean(false));
			setFl_alloggio_gratuito(new Boolean(false));
			setFl_vitto_alloggio_gratuito(new Boolean(false));
		}
		if (newFlag_spese.equals(NAVIGAZIONE)) 
		{
			setFl_navigazione(new Boolean(true));
			setFl_vitto_gratuito(new Boolean(false));
			setFl_alloggio_gratuito(new Boolean(false));
			setFl_vitto_alloggio_gratuito(new Boolean(false));
		}
		if (newFlag_spese.equals(VITTO_GRATUITO)) 
		{
			setFl_navigazione(new Boolean(false));
			setFl_vitto_gratuito(new Boolean(true));
			setFl_alloggio_gratuito(new Boolean(false));
			setFl_vitto_alloggio_gratuito(new Boolean(false));
		}
		if (newFlag_spese.equals(ALLOGGIO_GRATUITO)) 
		{
			setFl_navigazione(new Boolean(false));
			setFl_vitto_gratuito(new Boolean(false));
			setFl_alloggio_gratuito(new Boolean(true));
			setFl_vitto_alloggio_gratuito(new Boolean(false));
		}
		if (newFlag_spese.equals(VITTO_ALLOGGIO_GRATUITO)) 
		{
			setFl_navigazione(new Boolean(false));
			setFl_vitto_gratuito(new Boolean(false));
			setFl_alloggio_gratuito(new Boolean(false));
			setFl_vitto_alloggio_gratuito(new Boolean(true));
		}
	}
	//
	//La PROPERTY "flag_tipo_rimborso" viene usata per gestire il RADIO GROUP 
	//del tipo di trattamento di rimborso prescelto
	//

	public java.lang.String getFlag_tipo_rimborso() 
	{
		if(getFl_rimborso()== null)
			return DOCUMENTATO;

		if(getFl_rimborso().booleanValue())
			return ALTERNATIVO;

		if(!getFl_rimborso().booleanValue())
			return DOCUMENTATO;

		return null;		
	}
	public final static java.util.Dictionary getFlag_tipo_rimborsoKeys() {
		return flag_tipo_rimborsoKeys;
	}
	public void setFlag_tipo_rimborso(java.lang.String newFlag_tipo_rimborso) 
	{
		if (newFlag_tipo_rimborso.equals(ALTERNATIVO)) 
		{
			setFl_rimborso (new Boolean(true));
		}
		if (newFlag_tipo_rimborso.equals(DOCUMENTATO)) 
		{
			setFl_rimborso (new Boolean(false));
		}
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (05/03/2002 16.00.59)
	 * @param newMissione it.cnr.contab.missioni00.docs.bulk.MissioneBulk
	 */
	public void setMissione(MissioneBulk newMissione) {
		missione = newMissione;
	}
	public void setNazione(it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk newNazione) {
		nazione = newNazione;
		if((newNazione == null) || (newNazione.getPg_nazione() == null))
			setDivisa_tappa(new DivisaBulk());
	}
	public void setPg_nazione(java.lang.Long pg_nazione) {
		this.getNazione().setPg_nazione(pg_nazione);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (05/03/2002 16.00.59)
	 * @param newStatus int
	 */
	public void setStatus(int newStatus) {
		status = newStatus;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (05/03/2002 16.00.59)
	 * @param newTappaIniziale it.cnr.contab.missioni00.docs.bulk.Missione_tappaBulk
	 */
	public void setTappaIniziale(Missione_tappaBulk newTappaIniziale) {
		tappaIniziale = newTappaIniziale;
	}
	//
	// Validazione tappa da Confermare.
	//

	public void validaTappa() throws ValidationException 
	{
		if ( getDt_inizio_tappa() == null  )
			throw new ValidationException( "Selezionare il giorno al quale si riferisce la tappa !" );

		if ( getPg_nazione() == null ) 
			throw new ValidationException( "Selezionare una Nazione !" );

		if ( getCd_divisa_tappa() == null ) 
			throw new ValidationException( "Manca la valuta !" );

		if ( getCambio_tappa() == null ) 
			throw new ValidationException( "Valorizzare il Cambio !" );

		if ( getCambio_tappa().compareTo(new java.math.BigDecimal(0)) < 1 )		
			throw new ValidationException( "Il cambio deve essere > 0 !" );

		// Se ho selezionato comune proprio o comune altro la nazione deve essere
		// Italia		
		if ((!getFl_comune_estero().booleanValue()) && (!getNazione().getTi_nazione().equals(NazioneBulk.ITALIA)))
			throw new ValidationException( "La nazione non e' valida !" );

		if((getDt_ingresso_estero() != null) && (getDt_uscita_estero() != null) &&
				(getDt_uscita_estero().before(getDt_ingresso_estero())))
			throw new ValidationException( "La data ingresso estero non puo' essere successiva a quella di uscita !" );

		// Verifico che l'utente non abbia inserito un'altra tappa per lo stesso
		// giorno
		int i=0;	
		for (Iterator j = getMissione().getTappeMissioneColl().iterator(); j.hasNext();)
		{
			Missione_tappaBulk tappa = (Missione_tappaBulk) j.next();
			if(tappa.getDt_inizio_tappa().equals(getDt_inizio_tappa()))
				i++;

			// 1 esistera' sicuramente perche' e' quella che sto inserendo
			if(i > 1)		
				throw new ValidationException( "La tappa del giorno selezionato e' gia' stata configurata !" );
		}
		// Se ho settato "Allogio gratuito" o "Vitto e alloggio gratuito" non posso scegliere il trattamento alternativo
		if((getFl_alloggio_gratuito().booleanValue()||getFl_vitto_alloggio_gratuito().booleanValue())&& getFl_rimborso().booleanValue())
			throw new ValidationException( "Non è possibile scegliere il Trattamento alternativo quando l'Alloggio è gratuito !" );
	}
}
