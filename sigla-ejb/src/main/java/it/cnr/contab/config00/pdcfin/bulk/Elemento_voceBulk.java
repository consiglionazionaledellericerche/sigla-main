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

package it.cnr.contab.config00.pdcfin.bulk;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Dictionary;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
@JsonInclude(value=Include.NON_NULL)
public class Elemento_voceBulk extends Elemento_voceBase implements IVoceBilancioBulk {
	private java.util.Hashtable ti_appartenenzaKeys;
	private java.util.Hashtable ti_gestioneKeys; 
	private java.util.Hashtable ti_elemento_voceKeys;
	private java.util.Hashtable associazioni = new java.util.Hashtable();

	public static final String INDICAZIONE_TROVATO_BLOCCANTE ="E";
	public static final String INDICAZIONE_TROVATO_NON_BLOCCANTE ="W";
	public static final String INDICAZIONE_TROVATO_NESSUNA ="N";

	public final static Dictionary indicazioneTrovatoKeys;
	static {
		indicazioneTrovatoKeys = new it.cnr.jada.util.OrderedHashtable();
		indicazioneTrovatoKeys.put(INDICAZIONE_TROVATO_BLOCCANTE,"Sì - Bloccante");
		indicazioneTrovatoKeys.put(INDICAZIONE_TROVATO_NON_BLOCCANTE,"Sì - Non Bloccante");
		indicazioneTrovatoKeys.put(INDICAZIONE_TROVATO_NESSUNA,"No");	
	};

	public static final String BLOCCO_IMPEGNI_NATFIN_COMPETENZA = "C";
	public static final String BLOCCO_IMPEGNI_NATFIN_RESIDUI = "R";
	public static final String BLOCCO_IMPEGNI_NATFIN_ALL = "A";
	public static final String BLOCCO_IMPEGNI_NATFIN_NOTHING = "N";

	public final static Dictionary blocco_impegni_natfinKeys;
	static {
		blocco_impegni_natfinKeys = new it.cnr.jada.util.OrderedHashtable();
		blocco_impegni_natfinKeys.put(BLOCCO_IMPEGNI_NATFIN_COMPETENZA,"Solo Competenza");
		blocco_impegni_natfinKeys.put(BLOCCO_IMPEGNI_NATFIN_RESIDUI,"Solo Residui");
		blocco_impegni_natfinKeys.put(BLOCCO_IMPEGNI_NATFIN_ALL,"Competenza/Residui");
		blocco_impegni_natfinKeys.put(BLOCCO_IMPEGNI_NATFIN_NOTHING,"Nessuno");

	};

	protected Elemento_voceBulk elemento_padre;
	protected Capoconto_finBulk capoconto_fin = new Capoconto_finBulk();
	private Classificazione_entrateBulk classificazione_entrate;
	private Classificazione_speseBulk classificazione_spese;
	private V_classificazione_vociBulk v_classificazione_voci;
	private boolean isGestoreIstat;
	protected Voce_piano_economico_prgBulk voce_piano_economico;
	protected String cd_voce_piano_associato;

	/**
	 * Questo campo viene valorizzato dalla view V_ELEMENTO_VOCE_ASSESTATO
	 * Indica il centro responsabilità sul quale ci sono disponibilità
	 */
	protected String cd_cdr_bilancio;

	/**
	 * Questo campo viene valorizzato dalla view V_ELEMENTO_VOCE_ASSESTATO
	 * Indica l'esercizio di bilancio sul quale ci sono disponibilità
	 */
	protected Integer esercizio_bilancio;

	/**
	 * Questo campo viene valorizzato dalla view V_ELEMENTO_VOCE_ASSESTATO
	 * Indica l'importo assestato previsto in bilancio
	 */
	protected BigDecimal importo_assestato_bilancio;

	/**
	 * Questo campo viene valorizzato dalla view V_ELEMENTO_VOCE_ASSESTATO
	 * Indica l'importo utilizzato previsto in bilancio
	 */
	protected BigDecimal importo_utilizzato_bilancio;

	/**
	 * Questo campo viene valorizzato dalla view V_ELEMENTO_VOCE_ASSESTATO
	 * Indica l'importo disponibile previsto in bilancio
	 */
	protected BigDecimal importo_disponibile_bilancio;

	public Elemento_voceBulk( ) 
	{
		super();
	}
	public Elemento_voceBulk(java.lang.String cd_elemento_voce,java.lang.Integer esercizio,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
		super(cd_elemento_voce,esercizio,ti_appartenenza,ti_gestione);
	}
	/**
	 * Dal codice del capitolo ricevente di entrata CDS estra il codice del titolo
	 * @return il codice del titolo
	 */

	public String estraiCodiceTitoloPerCapitoloEntrataCDS() {
		return getCd_elemento_voce().substring(0,2);
	}
	/**
	 * @return it.cnr.contab.config00.pdcfin.bulk.Capoconto_finBulk
	 */
	public Capoconto_finBulk getCapoconto_fin() {
		return capoconto_fin;
	}
	/**
	 * Restituisce il valore della proprietà 'cd_ds_elemento_voce'
	 *
	 * @return Il valore della proprietà 'cd_ds_elemento_voce'
	 */
	public String getCd_ds_elemento_voce() {
		return getCd_elemento_voce() + " - " + getDs_elemento_voce();
	}
	@Override
	public Integer getEsercizio_elemento_padre() {
		it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_padre = this.getElemento_padre();
		if (elemento_padre == null)
			return null;
		return elemento_padre.getEsercizio();
	}
	@Override
	public String getTi_gestione_elemento_padre() {
		it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_padre = this.getElemento_padre();
		if (elemento_padre == null)
			return null;
		return elemento_padre.getTi_gestione();
	}
	@Override
	public String getTi_appartenenza_elemento_padre() {
		it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_padre = this.getElemento_padre();
		if (elemento_padre == null)
			return null;
		return elemento_padre.getTi_appartenenza();
	}
	public java.lang.String getCd_elemento_padre() {
		it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_padre = this.getElemento_padre();
		if (elemento_padre == null)
			return null;
		return elemento_padre.getCd_elemento_voce();
	}
	/**
	 * @return it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk
	 */
	public Elemento_voceBulk getElemento_padre() {
		return elemento_padre;
	}
	/**
	 * @return java.util.Hashtable
	 */
	public java.util.Hashtable getTi_appartenenzaKeys() {
		return ti_appartenenzaKeys;
	}
	/**
	 * @return java.util.Hashtable
	 */
	public java.util.Hashtable getTi_elemento_voceKeys() {
		return ti_elemento_voceKeys;
	}
	/**
	 * @return java.util.Hashtable
	 */
	public java.util.Hashtable getTi_gestioneKeys() {
		return ti_gestioneKeys;
	}
	/**
	 * Inizializza l'attributo <code>esercizio</code>.
	 * @param bp business process corrente
	 * @param context contesto dell'Action che e' stata generata
	 * @return OggettoBulk elemento voce con esercizio inizializzato
	 */
	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
		setFl_voce_sac(Boolean.FALSE);
		setFl_voce_non_sogg_imp_aut(Boolean.FALSE);
		setFl_recon(Boolean.FALSE);
		setFl_inv_beni_patr(Boolean.FALSE);
		setFl_voce_fondo(Boolean.FALSE);
		setFl_check_terzo_siope(Boolean.FALSE);
		setFl_inv_beni_comp(Boolean.FALSE);
		setFl_limite_spesa(Boolean.FALSE);
		setFl_soggetto_prelievo(Boolean.FALSE);
		setPerc_prelievo_pdgp_entrate(Utility.ZERO);
		setFl_prelievo(Boolean.FALSE);
		setFl_solo_competenza(Boolean.FALSE);
		setFl_solo_residuo(Boolean.FALSE);
		setFl_trovato( INDICAZIONE_TROVATO_NESSUNA );
		setFl_azzera_residui(Boolean.FALSE);
		setFl_limite_ass_obblig(Boolean.FALSE);
		setFl_voce_personale(Boolean.FALSE);
		setFl_missioni(Boolean.FALSE);
		setFl_partita_giro(Boolean.FALSE);
		setFlComunicaPagamenti(Boolean.FALSE);
		setFl_limite_competenza(Boolean.FALSE);
		return this;
	}
	/**
	 * Inizializza l'attributo <code>esercizio</code>.
	 * @param bp business process corrente
	 * @param context contesto dell'Action che e' stata generata
	 * @return OggettoBulk elemento voce con esercizio inizializzato
	 */
	public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
		return this;
	}


	/**
	 * Determina quando abilitare o meno nell'interfaccia utente il campo capoconto_fin
	 * @return boolean true quando il campo deve essere disabilitato
	 */

	public boolean isROCapocontoFin() {
		return capoconto_fin == null || capoconto_fin.getCrudStatus() == NORMAL;
	}
	/**
	 * Determina quando abilitare o meno nell'interfaccia utente il campo elemento_padre
	 * @return boolean true quando il campo deve essere disabilitato
	 */

	public boolean isROElemento_padre() {
		return elemento_padre == null || elemento_padre.getCrudStatus() == NORMAL;
	}
	/**
	 * @param newCapoconto_fin it.cnr.contab.config00.pdcfin.bulk.Capoconto_finBulk
	 */
	public void setCapoconto_fin(Capoconto_finBulk newCapoconto_fin) {
		capoconto_fin = newCapoconto_fin;
	}
	public void setEsercizio_elemento_padre(Integer esercizio_elemento_padre) {
		if (this.getElemento_padre()!=null)
			this.getElemento_padre().setEsercizio(esercizio_elemento_padre);
	}
	public void setTi_gestione_elemento_padre(String ti_gestione_elemento_padre) {
		if (this.getElemento_padre()!=null)
			this.getElemento_padre().setTi_gestione(ti_gestione_elemento_padre);
	}
	public void setTi_appartenenza_elemento_padre(String ti_appartenenza_elemento_padre) {
		if (this.getElemento_padre()!=null)
			this.getElemento_padre().setTi_appartenenza(ti_appartenenza_elemento_padre);
	}
	public void setCd_elemento_padre(java.lang.String cd_elemento_padre) {
		if (this.getElemento_padre()!=null)
			this.getElemento_padre().setCd_elemento_voce(cd_elemento_padre);
	}
	/**
	 * @param newElemento_padre it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk
	 */
	public void setElemento_padre(Elemento_voceBulk newElemento_padre) {
		elemento_padre = newElemento_padre;
	}
	/**
	 * @param newTi_appartenenzaKeys java.util.Hashtable
	 */
	public void setTi_appartenenzaKeys(java.util.Hashtable newTi_appartenenzaKeys) {
		ti_appartenenzaKeys = newTi_appartenenzaKeys;
	}
	/**
	 * @param newTi_elemento_voceKeys java.util.Hashtable
	 */
	public void setTi_elemento_voceKeys(java.util.Hashtable newTi_elemento_voceKeys) {
		ti_elemento_voceKeys = newTi_elemento_voceKeys;
	}
	/**
	 * @param newTi_gestioneKeys java.util.Hashtable
	 */
	public void setTi_gestioneKeys(java.util.Hashtable newTi_gestioneKeys) {
		ti_gestioneKeys = newTi_gestioneKeys;
	}
	/**
	 * Esegue la validazione formale dei campi di input
	 */



	public void validate() throws ValidationException 
	{
		if ( getEsercizio() == null  )
			throw new ValidationException( "Il campo ESERCIZIO è obbligatorio." );
		if ( getEsercizio().toString().length() != 4 )
			throw new ValidationException( "Il campo ESERCIZIO deve essere di quattro cifre. " );
		if ( getDs_elemento_voce() == null  )
			throw new ValidationException( "Il campo DESCRIZIONE è obbligatorio." );
		/*if ( !isNullOrEmpty( getCd_proprio_elemento() ) )
	{
		long cdLong;
		try
		{
			cdLong = Long.parseLong( getCd_proprio_elemento() );
		}
		catch (java.lang.NumberFormatException e)
		{
			throw new ValidationException( "Il campo CODICE deve essere numerico. " );			
		}
		if ( cdLong < 0 )
			throw new ValidationException( "Il campo CODICE deve essere maggiore di 0. " );			
	}*/		

	}

	/**
	 * @return
	 */
	public Classificazione_entrateBulk getClassificazione_entrate() {
		return classificazione_entrate;
	}

	/**
	 * @param v_classificazione_entrate
	 */
	public void setClassificazione_entrate(Classificazione_entrateBulk v_classificazione_entrate) {
		classificazione_entrate = v_classificazione_entrate;
	}

	public java.lang.Integer getEsercizio_cla_e() {
		Classificazione_entrateBulk classificazione_entrate = this.getClassificazione_entrate();
		if (classificazione_entrate == null)
			return null;
		return classificazione_entrate.getEsercizio();
	}

	public java.lang.String getCod_cla_e() {
		Classificazione_entrateBulk classificazione_entrate = this.getClassificazione_entrate();
		if (classificazione_entrate == null)
			return null;
		return classificazione_entrate.getCodice_cla_e();
	}

	public void setEsercizio_cla_e(java.lang.Integer v_esercizio_cla_e) {
		this.getClassificazione_entrate().setEsercizio(v_esercizio_cla_e);
	}

	public void setCod_cla_e(java.lang.String v_cod_cla_e) {
		this.getClassificazione_entrate().setCodice_cla_e(v_cod_cla_e);
	}

	/**
	 * @return
	 */
	public Classificazione_speseBulk getClassificazione_spese() {
		return classificazione_spese;
	}

	/**
	 * @param newClassificazione_spese
	 */
	public void setClassificazione_spese(Classificazione_speseBulk newClassificazione_spese) {
		classificazione_spese = newClassificazione_spese;
	}

	public java.lang.Integer getEsercizio_cla_s() {
		Classificazione_speseBulk classificazione_spese = this.getClassificazione_spese();
		if (classificazione_spese == null)
			return null;
		return classificazione_spese.getEsercizio();
	}

	public java.lang.String getCod_cla_s() {
		Classificazione_speseBulk classificazione_spese = this.getClassificazione_spese();
		if (classificazione_spese == null)
			return null;
		return classificazione_spese.getCodice_cla_s();
	}

	public void setEsercizio_cla_s(java.lang.Integer v_esercizio_cla_s) {
		this.getClassificazione_spese().setEsercizio(v_esercizio_cla_s);
	}

	public void setCod_cla_s(java.lang.String v_cod_cla_s) {		
		this.getClassificazione_spese().setCodice_cla_s(v_cod_cla_s);
	}	

	public V_classificazione_vociBulk getV_classificazione_voci() {
		return v_classificazione_voci;
	}

	public void setV_classificazione_voci(V_classificazione_vociBulk v_class) {
		v_classificazione_voci = v_class;
	}

	public java.lang.Integer getId_classificazione() {
		if (getV_classificazione_voci() == null)
			return null;
		return getV_classificazione_voci().getId_classificazione();
	}

	public void setId_classificazione(java.lang.Integer v_id_classificazione) {
		getV_classificazione_voci().setId_classificazione(v_id_classificazione);
	}

	public void setGestoreIstat(boolean b) {
		isGestoreIstat = b;
	}

	public boolean isGestoreIstat() {
		return isGestoreIstat;
	}

	public boolean isGestoreOk(UserContext context) throws ComponentException, RemoteException {
		setGestoreIstat(UtenteBulk.isGestoreIstatSiope(context));
		return isGestoreIstat;		

	}

	public boolean isObbligatoriaIndicazioneTrovato() {
		if (getFl_trovato() == null)
			return false;
		return getFl_trovato().equals(INDICAZIONE_TROVATO_BLOCCANTE);		
	}
	public boolean isFacoltativaIndicazioneTrovato() {
		if (getFl_trovato() == null)
			return false;
		return getFl_trovato().equals(INDICAZIONE_TROVATO_NON_BLOCCANTE);		
	}
	public boolean isInibitaIndicazioneTrovato() {
		if (getFl_trovato() == null)
			return true;
		return getFl_trovato().equals(INDICAZIONE_TROVATO_NESSUNA);		
	}
	public boolean isVocePerTrovati() {
		return isObbligatoriaIndicazioneTrovato() || isFacoltativaIndicazioneTrovato();
	}

	public String getCd_voce() {
		return getCd_elemento_voce();
	}

	public String getCd_titolo_capitolo() {
		return getCd_elemento_voce();
	}

	public String getCd_funzione() {
		return String.valueOf("01");
	}

	public Voce_piano_economico_prgBulk getVoce_piano_economico() {
		return voce_piano_economico;
	}

	public void setVoce_piano_economico(Voce_piano_economico_prgBulk voce_piano_economico) {
		this.voce_piano_economico = voce_piano_economico;
	}	

	@Override
	public String getCd_unita_piano() {
		Voce_piano_economico_prgBulk vocePiano = this.getVoce_piano_economico();
		if (vocePiano == null)
			return null;
		return vocePiano.getCd_unita_organizzativa();
	}
	
	@Override
	public void setCd_unita_piano(String cd_unita_piano) {
		this.getVoce_piano_economico().setCd_unita_organizzativa(cd_unita_piano);
	}
	
	@Override
	public String getCd_voce_piano() {
		Voce_piano_economico_prgBulk vocePiano = this.getVoce_piano_economico();
		if (vocePiano == null)
			return null;
		return vocePiano.getCd_voce_piano();
	}
	
	@Override
	public void setCd_voce_piano(String cd_voce_piano) {
		this.getVoce_piano_economico().setCd_voce_piano(cd_voce_piano);
	}

	public boolean isVoceEntrata(){
		return Elemento_voceHome.GESTIONE_ENTRATE.equals(this.getTi_gestione());
	}

	public boolean isVoceSpesa(){
		return Elemento_voceHome.GESTIONE_SPESE.equals(this.getTi_gestione());
	}

	public boolean isAttivoBloccoResiduiNatfinCompetenza() {
		return BLOCCO_IMPEGNI_NATFIN_COMPETENZA.equals(getBlocco_impegni_natfin()) ||
				BLOCCO_IMPEGNI_NATFIN_ALL.equals(getBlocco_impegni_natfin());
	}

	public boolean isAttivoBloccoResiduiNatfinResidui() {
		return BLOCCO_IMPEGNI_NATFIN_RESIDUI.equals(getBlocco_impegni_natfin()) ||
				BLOCCO_IMPEGNI_NATFIN_ALL.equals(getBlocco_impegni_natfin());
	}

	public String getCd_voce_piano_associato() {
		return cd_voce_piano_associato;
	}

	public void setCd_voce_piano_associato(String cd_voce_piano_associato) {
			this.cd_voce_piano_associato = cd_voce_piano_associato;
	}

	public String getCd_cdr_bilancio() {
		return cd_cdr_bilancio;
	}

	public void setCd_cdr_bilancio(String cd_cdr_bilancio) {
		this.cd_cdr_bilancio = cd_cdr_bilancio;
	}

	public Integer getEsercizio_bilancio() {
		return esercizio_bilancio;
	}

	public void setEsercizio_bilancio(Integer esercizio_bilancio) {
		this.esercizio_bilancio = esercizio_bilancio;
	}

	public BigDecimal getImporto_assestato_bilancio() {
		return importo_assestato_bilancio;
	}

	public void setImporto_assestato_bilancio(BigDecimal importo_assestato_bilancio) {
		this.importo_assestato_bilancio = importo_assestato_bilancio;
	}

	public BigDecimal getImporto_utilizzato_bilancio() {
		return importo_utilizzato_bilancio;
	}

	public void setImporto_utilizzato_bilancio(BigDecimal importo_utilizzato_bilancio) {
		this.importo_utilizzato_bilancio = importo_utilizzato_bilancio;
	}

	public BigDecimal getImporto_disponibile_bilancio() {
		return importo_disponibile_bilancio;
	}

	public void setImporto_disponibile_bilancio(BigDecimal importo_disponibile_bilancio) {
		this.importo_disponibile_bilancio = importo_disponibile_bilancio;
	}
}
