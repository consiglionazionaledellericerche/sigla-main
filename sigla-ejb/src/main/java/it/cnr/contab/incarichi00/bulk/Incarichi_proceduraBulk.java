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

/*
 * Date 03/04/2008
 */
package it.cnr.contab.incarichi00.bulk;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Dictionary;
import java.util.Iterator;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.V_persona_fisicaBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk;
import it.cnr.contab.config00.contratto.bulk.Procedure_amministrativeBulk;
import it.cnr.contab.config00.contratto.bulk.Tipo_atto_amministrativoBulk;
import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.incarichi00.bulk.storage.StorageFolderProcedura;
import it.cnr.contab.incarichi00.tabrif.bulk.Incarichi_parametriBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_attivitaBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_attivita_fpBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_incaricoBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_norma_perlaBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_prestazioneBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.persistency.Persister;

public class Incarichi_proceduraBulk extends Incarichi_proceduraBase {

	public final static Dictionary ti_statoKeys = new it.cnr.jada.util.OrderedHashtable();
	private String indirizzo_unita_organizzativa;
	private boolean dataProrogaEnableOnView = Boolean.FALSE;
	private boolean utenteCollegatoUoEnte = Boolean.FALSE;
	private boolean utenteCollegatoSuperUtente = Boolean.FALSE;
	private String statoProcedura;

	public final static Dictionary applicazioneNormaFlags  = new it.cnr.jada.util.OrderedHashtable();;
	final public static String APPLICAZIONE_NORMA_YES = "Y";
	final public static String APPLICAZIONE_NORMA_NO = "N";
	
	final public static Integer FASE_INSERIMENTO_INCARICO = new Integer(0);
	final public static Integer FASE_PUBBLICAZIONE = new Integer(1);
	final public static Integer FASE_INSERIMENTO_CONTRATTO = new Integer(2);
	final public static Integer FASE_INVIO_CORTE_CONTI = new Integer(3);
	final public static Integer FASE_DEFINITIVA = new Integer(4);
	
	final public static String STATO_PROVVISORIO = "PP";
	final public static String STATO_PUBBLICATA = "PU";
	final public static String STATO_RESPINTO = "PR";
	final public static String STATO_ANNULLATO = "PN";
	final public static String STATO_ELIMINATO = "EL";
	final public static String STATO_DEFINITIVO = "PD";
	final public static String STATO_INVIATO = "IN";
	final public static String STATO_CHIUSO = "CC";

	final public static String STATO_IN_SCADENZA = "IS";
	final public static String STATO_SCADUTA = "SC";
	final public static String STATO_IN_PUBBLICAZIONE = "IP";

	static {
		ti_statoKeys.put(STATO_PROVVISORIO,"Provvisoria");
		ti_statoKeys.put(STATO_PUBBLICATA,"Pubblicata");
		ti_statoKeys.put(STATO_RESPINTO,"Respinta");
		ti_statoKeys.put(STATO_ANNULLATO,"Annullata");
		ti_statoKeys.put(STATO_ELIMINATO,"Eliminata");
		ti_statoKeys.put(STATO_DEFINITIVO,"Definitiva");
		ti_statoKeys.put(STATO_IN_SCADENZA,"In Scadenza");
		ti_statoKeys.put(STATO_IN_PUBBLICAZIONE,"Pubblicata");
		ti_statoKeys.put(STATO_INVIATO,"Inviata Corte Conti");
		ti_statoKeys.put(STATO_SCADUTA,"Scaduta");
		ti_statoKeys.put(STATO_CHIUSO,"Chiusa");

		applicazioneNormaFlags.put(APPLICAZIONE_NORMA_YES, "Si");
		applicazioneNormaFlags.put(APPLICAZIONE_NORMA_NO, "No");
	}

	public final static Dictionary ti_natura_contabileKeys = new it.cnr.jada.util.OrderedHashtable();

	private CdsBulk cds;
	private BulkList incarichi_procedura_annoColl = new BulkList();
	private BulkList archivioAllegati = new BulkList();
	private BulkList archivioAllegatiMI = new BulkList();
	private BulkList incarichi_repertorioColl = new BulkList();
	private BulkList incarichi_procedura_noteColl = new BulkList();
	private Unita_organizzativaBulk unita_organizzativa;
	private Incarichi_richiestaBulk incarichi_richiesta;
	private V_persona_fisicaBulk terzo_resp;
	private V_persona_fisicaBulk firmatario;
	private Tipo_atto_amministrativoBulk atto;
	private Procedure_amministrativeBulk procedura_amministrativa;
	private Procedure_amministrativeBulk procedura_amministrativa_beneficiario;
	private ComuneBulk citta;
	private Tipo_incaricoBulk tipo_incarico;
	private Tipo_attivitaBulk tipo_attivita;
	private Tipo_attivita_fpBulk tipo_attivita_fp, tipo_attivita_fp0, tipo_attivita_fp1, tipo_attivita_fp_orig;
	private Tipo_prestazioneBulk tipo_prestazione, tipo_prestazione_orig;
	private Incarichi_proceduraBulk incarichi_procedura_padre; 
	private V_terzo_per_compensoBulk v_terzoForSearch = new V_terzo_per_compensoBulk();
	private Incarichi_repertorioBulk incaricoRepertorioForSearch = new Incarichi_repertorioBulk();
	
	protected java.util.Collection tipologie_prestazioni;
	private Tipo_norma_perlaBulk tipo_norma_perla, tipo_norma_perla_orig;
	protected java.util.Collection tipologie_norma_perla;
	private String fl_applicazione_norma_orig;
	private Integer faseProcesso;

	public Incarichi_proceduraBulk() {
		super();
	}
	public Incarichi_proceduraBulk(java.lang.Integer esercizio, java.lang.Long pg_procedura) {
		super(esercizio, pg_procedura);
	}

// Tipo Atto Amministrativo
	public Tipo_atto_amministrativoBulk getAtto() {
		return atto;
	}
	public void setAtto(Tipo_atto_amministrativoBulk atto) {
		this.atto = atto;
	}
	public String getCd_tipo_atto() {
		if (this.getAtto() == null)
			return null;
		return this.getAtto().getCd_tipo_atto();
	}
	public void setCd_tipo_atto(String cd_tipo_atto) {
		if (this.getAtto()!=null)
			this.getAtto().setCd_tipo_atto(cd_tipo_atto);
	}

// Istituto
	public CdsBulk getCds() {
		return cds;
	}
	public void setCds(CdsBulk cds) {
		this.cds = cds;
	}
	public String getCd_cds() {
		if (this.getCds() == null)
			return null;
		return this.getCds().getCd_unita_organizzativa();
	}
	public void setCd_cds(String cd_cds) {
		if (this.getCds()!=null)
			this.getCds().setCd_unita_organizzativa(cd_cds);
	}

// Unità Organizzativa
	public Unita_organizzativaBulk getUnita_organizzativa() {
		return unita_organizzativa;
	}
	public void setUnita_organizzativa(Unita_organizzativaBulk unita_organizzativa) {
		this.unita_organizzativa = unita_organizzativa;
	}
	public String getCd_unita_organizzativa() {
		if (this.getUnita_organizzativa() == null)
			return null;
		return this.getUnita_organizzativa().getCd_unita_organizzativa();
	}
	public void setCd_unita_organizzativa(String cd_unita_organizzativa) {
		if (this.getUnita_organizzativa() != null)
			this.getUnita_organizzativa().setCd_unita_organizzativa(cd_unita_organizzativa);
	}

// Comune - Città
	public ComuneBulk getCitta() {
		return citta;
	}
	public void setCitta(ComuneBulk citta) {
		this.citta = citta;
	}
	public java.lang.Long getPg_comune() {
		if (this.getCitta() == null)
			return null;
		return this.getCitta().getPg_comune();
	}
	public void setPg_comune(java.lang.Long pg_comune) {
		if (this.getCitta() != null)
			this.getCitta().setPg_comune(pg_comune);
	}

// Elenco Repertorio Incarichi Affidati
	public BulkList getIncarichi_repertorioColl() {
		return incarichi_repertorioColl;
	}
	
	public void setIncarichi_repertorioColl(
			BulkList incarichi_repertorioColl) {
		this.incarichi_repertorioColl = incarichi_repertorioColl;
	}

// Responsabile del Procedimento
	public V_persona_fisicaBulk getTerzo_resp() {
		return terzo_resp;
	}
	public void setTerzo_resp(V_persona_fisicaBulk terzo_resp) {
		this.terzo_resp = terzo_resp;
	}
	public Integer getCd_terzo_resp() {
		if (this.getTerzo_resp() == null)
			return null;
		return this.getTerzo_resp().getCd_terzo();
	}
	public void setCd_terzo_resp(Integer cd_terzo_resp) {
		if (this.getTerzo_resp() != null)
			this.getTerzo_resp().setCd_terzo(cd_terzo_resp);
	}	

// Firmatario
	public V_persona_fisicaBulk getFirmatario() {
		return firmatario;
	}
	public void setFirmatario(V_persona_fisicaBulk firmatario) {
		this.firmatario = firmatario;
	}
	public Integer getCd_firmatario() {
		if (this.getFirmatario() == null)
			return null;
		return this.getFirmatario().getCd_terzo();
	}
	public void setCd_firmatario(Integer cd_firmatario) {
		if (this.getFirmatario() != null)
			this.getFirmatario().setCd_terzo(cd_firmatario);
	}	
	
// Repertorio Padre	
	public Incarichi_proceduraBulk getIncarichi_procedura_padre() {
		return incarichi_procedura_padre;
	}
	public void setIncarichi_procedura_padre(
			Incarichi_proceduraBulk incarichi_procedura_padre) {
		this.incarichi_procedura_padre = incarichi_procedura_padre;
	}
	public Integer getEsercizio_padre() {
		if (this.getIncarichi_procedura_padre() == null)
			return null;
		return this.getIncarichi_procedura_padre().getEsercizio();
	}
	public void setEsercizio_padre(Integer esercizio_padre) {
		if (this.getIncarichi_procedura_padre() != null)
			this.getIncarichi_procedura_padre().setEsercizio(esercizio_padre);
	}	
	public Long getPg_procedura_padre() {
		if (this.getIncarichi_procedura_padre() == null)
			return null;
		return this.getIncarichi_procedura_padre().getPg_procedura();
	}
	public void setPg_procedura_padre(Long pg_procedura_padre) {
		if (this.getIncarichi_procedura_padre() != null)
			this.getIncarichi_procedura_padre().setPg_procedura(pg_procedura_padre);
	}	

// 	Richiesta Incarico Interno
	public Incarichi_richiestaBulk getIncarichi_richiesta() {
		return incarichi_richiesta;
	}
	public void setIncarichi_richiesta(Incarichi_richiestaBulk incarichi_richiesta) {
		this.incarichi_richiesta = incarichi_richiesta;
	}

	public Integer getEsercizio_richiesta() {
		if (this.getIncarichi_richiesta() == null)
			return null;
		return this.getIncarichi_richiesta().getEsercizio();
	}
	public void setEsercizio_richiesta(Integer setEsercizio_richiesta) {
		if (this.getIncarichi_richiesta() != null)
			this.getIncarichi_richiesta().setEsercizio(setEsercizio_richiesta);
	}	

	public Long getPg_richiesta() {
		if (this.getIncarichi_richiesta() == null)
			return null;
		return this.getIncarichi_richiesta().getPg_richiesta();
	}
	public void setPg_richiesta(Long pg_richiesta) {
		if (this.getIncarichi_richiesta() != null)
			this.getIncarichi_richiesta().setPg_richiesta(pg_richiesta);
	}
	
// Procedura Amministrativa	
	public Procedure_amministrativeBulk getProcedura_amministrativa() {
		return procedura_amministrativa;
	}
	public void setProcedura_amministrativa(
			Procedure_amministrativeBulk procedura_amministrativa) {
		this.procedura_amministrativa = procedura_amministrativa;
	}
	public String getCd_proc_amm() {
		if (this.getProcedura_amministrativa() == null)
			return null;
		return this.getProcedura_amministrativa().getCd_proc_amm();
	}
	public void setCd_proc_amm(String cd_proc_amm) {
		if (this.getProcedura_amministrativa() != null)
			this.getProcedura_amministrativa().setCd_proc_amm(cd_proc_amm);
	}
	
// Procedura Amministrativa	Identificazione Beneficiario
	public Procedure_amministrativeBulk getProcedura_amministrativa_beneficiario() {
		return procedura_amministrativa_beneficiario;
	}
	public void setProcedura_amministrativa_beneficiario(Procedure_amministrativeBulk procedura_amministrativa_beneficiario) {
		this.procedura_amministrativa_beneficiario = procedura_amministrativa_beneficiario;
	}
	public String getCd_proc_amm_benef() {
		if (this.getProcedura_amministrativa_beneficiario() == null)
			return null;
		return this.getProcedura_amministrativa_beneficiario().getCd_proc_amm();
	}
	public void setCd_proc_amm_benef(String cd_proc_amm_benef) {
		if (this.getProcedura_amministrativa_beneficiario() != null)
			this.getProcedura_amministrativa_beneficiario().setCd_proc_amm(cd_proc_amm_benef);
	}

// Tipo Attività Richiesta
	public Tipo_attivitaBulk getTipo_attivita() {
		return tipo_attivita;
	}
	public void setTipo_attivita(Tipo_attivitaBulk tipo_attivita) {
		this.tipo_attivita = tipo_attivita;
	}
	public java.lang.String getCd_tipo_attivita() {
		if (this.getTipo_attivita() == null)
			return null;
		return this.getTipo_attivita().getCd_tipo_attivita();
	}
	public void setCd_tipo_attivita(java.lang.String cd_tipo_attivita) {
		if (this.getTipo_attivita() != null)
			this.getTipo_attivita().setCd_tipo_attivita(cd_tipo_attivita);
	}

// Tipo Incarico Richiesto
	public Tipo_incaricoBulk getTipo_incarico() {
		return tipo_incarico;
	}
	public void setTipo_incarico(Tipo_incaricoBulk tipo_incarico) {
		this.tipo_incarico = tipo_incarico;
	}
	public java.lang.String getCd_tipo_incarico() {
		if (this.getTipo_incarico() == null)
			return null;
		return this.getTipo_incarico().getCd_tipo_incarico();
	}
	public void setCd_tipo_incarico(java.lang.String cd_tipo_incarico) {
		if (this.getTipo_incarico() != null)
			this.getTipo_incarico().setCd_tipo_incarico(cd_tipo_incarico);
	}

//Tipo Prestazione
	public Tipo_prestazioneBulk getTipo_prestazione() {
		return tipo_prestazione;
	}
	public void setTipo_prestazione(Tipo_prestazioneBulk tipo_prestazione) {
		this.tipo_prestazione = tipo_prestazione;
	}
	public String getCd_tipo_prestazione() {
		if (this.getTipo_prestazione() == null)
			return null;
		return this.getTipo_prestazione().getCd_tipo_prestazione();
	}
	public void setCd_tipo_prestazione(String cd_tipo_prestazione) {
		if (this.getTipo_prestazione() != null)
			this.getTipo_prestazione().setCd_tipo_prestazione(cd_tipo_prestazione);
	}

	public BulkList getIncarichi_procedura_annoColl() {
		return incarichi_procedura_annoColl;
	}
	
	public void setIncarichi_procedura_annoColl(
			BulkList incarichi_procedura_annoColl) {
		this.incarichi_procedura_annoColl = incarichi_procedura_annoColl;
	}
	public BulkList getArchivioAllegati() {
		return archivioAllegati;
	}
	public void setArchivioAllegati(BulkList list) {
		archivioAllegati = list;
	}
	public BulkList getArchivioAllegatiMI() {
		return archivioAllegatiMI;
	}
	public void setArchivioAllegatiMI(BulkList list) {
		archivioAllegatiMI = list;
	}
	/**
	 * Restituisce un array di <code>BulkCollection</code> contenenti oggetti
	 * bulk da rendere persistenti insieme al ricevente.
	 * L'implementazione standard restituisce <code>null</code>.
	 * @see it.cnr.jada.comp.GenericComponent#makeBulkPersistent
	 */ 
	public BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] { 
				getIncarichi_procedura_annoColl(), getIncarichi_repertorioColl(), 
				getArchivioAllegati(), getIncarichi_procedura_noteColl() };
	}
	public int addToIncarichi_procedura_annoColl(Incarichi_procedura_annoBulk dett) {
		dett.setIncarichi_procedura(this);
		dett.setImporto_iniziale(Utility.ZERO);
		dett.setImporto_complessivo(Utility.ZERO);
		getIncarichi_procedura_annoColl().add(dett);
		return getIncarichi_procedura_annoColl().size()-1;
	}	
	public Incarichi_procedura_annoBulk removeFromIncarichi_procedura_annoColl(int index) {
		Incarichi_procedura_annoBulk dett = (Incarichi_procedura_annoBulk)getIncarichi_procedura_annoColl().remove(index);
		return dett;
	}
	public int addToArchivioAllegati(Incarichi_procedura_archivioBulk dett) {
		dett.setIncarichi_procedura(this);
		dett.setStato(Incarichi_archivioBulk.STATO_VALIDO);
		getArchivioAllegati().add(dett);
		return getArchivioAllegati().size()-1;
	}	
	public Incarichi_procedura_archivioBulk removeFromArchivioAllegati(int index) {
		Incarichi_procedura_archivioBulk dett = (Incarichi_procedura_archivioBulk)getArchivioAllegati().remove(index);
		return dett;
	}
	public int addToArchivioAllegatiMI(Incarichi_archivioBulk dett) {
		getArchivioAllegatiMI().add(dett);
		return getArchivioAllegatiMI().size()-1;
	}	
	public Incarichi_archivioBulk removeFromArchivioAllegatiMI(int index) {
		return (Incarichi_archivioBulk)getArchivioAllegatiMI().remove(index);
	}
	public boolean isROCds() {
		return getUnita_organizzativa()!=null && 
		       getUnita_organizzativa().getCd_unita_organizzativa()!=null;
	}
	public boolean isROProcAmm() {
		return isROPrimaFaseProcedura() ||
		       !getIncarichi_repertorioColl().isEmpty();
	}
	public String getIndirizzo_unita_organizzativa() {
		return indirizzo_unita_organizzativa;
	}
	public void setIndirizzo_unita_organizzativa(
			String indirizzo_unita_organizzativa) {
		this.indirizzo_unita_organizzativa = indirizzo_unita_organizzativa;
	}
	public java.util.Dictionary getTipo_naturaKeys() {
		return NaturaBulk.tipo_naturaKeys;
	}
	/**
	 * Ritorna l'importo totale ripartito tra gli esercizi contabili
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getIm_complessivo_ripartito() {
		BigDecimal totale = Utility.ZERO;
		for (Iterator i=this.getIncarichi_procedura_annoColl().iterator();i.hasNext();)
			totale = totale.add(((Incarichi_procedura_annoBulk)i.next()).getImporto_complessivo());
		return totale;
	}
	/**
	 * Ritorna TRUE se la richiesta è in stato "Provvisorio"
	 * 
	 * @return boolean
	 */
	public boolean isProceduraProvvisoria() {
    	return this.getStato()!=null&&this.getStato().equals(STATO_PROVVISORIO);
    }
	/**
	 * Ritorna TRUE se la richiesta è in stato "Definitiva"
	 * 
	 * @return boolean
	 */
    public boolean isProceduraDefinitiva() {
    	return this.getStato()!=null&&this.getStato().equals(STATO_DEFINITIVO);
/*    	
    	if (getNr_contratti()==null) return false;
    	Integer contadef = 0;
    	for (Iterator i=getIncarichi_repertorioValidiColl().iterator();i.hasNext();)
    		if (((Incarichi_repertorioBulk)i.next()).isIncaricoDefinitivo()) contadef=contadef+1;
    	return getNr_contratti().equals(contadef);
*/    	
    }
	/**
	 * Ritorna TRUE se la richiesta è in stato "Chiusa"
	 * 
	 * @return boolean
	 */
    public boolean isProceduraChiusa() {
    	return this.getStato()!=null&&this.getStato().equals(STATO_CHIUSO);
    }
	/**
	 * Ritorna TRUE se la richiesta è in stato "Respinta"
	 * 
	 * @return boolean
	 */
    public boolean isProceduraRespinta() {
    	return this.getStato()!=null&&this.getStato().equals(STATO_RESPINTO);
    }
	/**
	 * Ritorna TRUE se la richiesta è in stato "Annullato"
	 * 
	 * @return boolean
	 */
    public boolean isProceduraAnnullata() {
    	return this.getStato()!=null&&this.getStato().equals(STATO_ANNULLATO);
    }
	/**
	 * Ritorna TRUE se la richiesta è in stato "Pubblicata"
	 * 
	 * @return boolean
	 */
    public boolean isProceduraPubblicata() {
    	return this.getStato()!=null&&this.getStato().equals(STATO_PUBBLICATA);
    }
	/**
	 * Ritorna TRUE se:
	 *  - la richiesta è in stato "Definitivo"
	 *  - la data di inizio pubblicazione è trascorsa 
	 *  - la data di fine pubblicazione non è trascorsa
	 * 
	 * @return boolean
	 */
    public boolean isPubblicazioneInCorso() {
    	if (getStatoProcedura()==null) { 
	    	if (isProceduraPubblicata() &&
	     		getDt_pubblicazione()!=null &&
	    		!getDt_pubblicazione().after(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()) &&
	    		getDt_fine_pubblicazione()!=null && 
	    		!getDt_fine_pubblicazione().before(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()))
	    		setStatoProcedura(STATO_IN_PUBBLICAZIONE);
    	}
   		return getStatoProcedura() != null && getStatoProcedura().equals(STATO_IN_PUBBLICAZIONE);
    }
	/**
	 * Ritorna TRUE se:
	 *  - la data di fine pubblicazione è trascorsa
	 *  - la data di scadenza è trascorsa
	 * 
	 * @return boolean
	 */
    public boolean isProceduraScaduta() {
    	if (getStatoProcedura()==null) { 
	    	if (getDt_scadenza() != null && isProceduraPubblicata() &&
	    	    getDt_scadenza().before(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()))
	    		setStatoProcedura(Incarichi_proceduraBulk.STATO_SCADUTA);
    	}
		return getStatoProcedura() != null && getStatoProcedura().equals(Incarichi_proceduraBulk.STATO_SCADUTA); 
    }
	/**
	 * Ritorna TRUE se:
	 *  - la data di fine pubblicazione è trascorsa
	 *  - la data di scadenza non è trascorsa
	 * 
	 * @return boolean
	 */
    public boolean isProceduraInScadenza() {
    	if (getStatoProcedura()==null) { 
	    	if (getDt_scadenza() != null && isProceduraPubblicata() && 
	    		!isPubblicazioneInCorso() && !isProceduraScaduta())
	    		setStatoProcedura(Incarichi_proceduraBulk.STATO_IN_SCADENZA);
    	}
   		return getStatoProcedura() != null && getStatoProcedura().equals(Incarichi_proceduraBulk.STATO_IN_SCADENZA);
    }
	/**
	 * Ritorna una stringa che decodifica lo stato della richiesta
	 * 
	 * @return java.lang.String
	 */
	public String getStatoText(){
		if (this.isPubblicazioneInCorso())
			return ti_statoKeys.get(STATO_IN_PUBBLICAZIONE).toString();
		if (this.isProceduraDefinitiva())
			return ti_statoKeys.get(STATO_DEFINITIVO).toString();
		if (this.isProceduraInviataCorteConti())
			return ti_statoKeys.get(STATO_INVIATO).toString();
		if (this.isProceduraInScadenza())
			return ti_statoKeys.get(STATO_IN_SCADENZA).toString();
		if (this.isProceduraScaduta())
			return ti_statoKeys.get(STATO_SCADUTA).toString();
		if (this.isProceduraProvvisoria())
			return ti_statoKeys.get(STATO_PROVVISORIO).toString();
		if (this.isProceduraPubblicata())
			return ti_statoKeys.get(STATO_PUBBLICATA).toString();
		if (this.isProceduraRespinta())
			return ti_statoKeys.get(STATO_RESPINTO).toString();
		if (this.isProceduraAnnullata()) {
			if (getDt_cancellazione() != null && getDt_fine_pubblicazione() != null && 
				getDt_cancellazione().before(getDt_fine_pubblicazione()))
				return ti_statoKeys.get(STATO_ANNULLATO).toString();
			return ti_statoKeys.get(STATO_ELIMINATO).toString();
		}
		if (this.isProceduraChiusa()) {
			if (this.getIncarichi_repertorioColl().size()==1 && 
				((Incarichi_repertorioBulk)this.getIncarichi_repertorioColl().get(0)).getEsito_corte_conti()!=null && 
				((Incarichi_repertorioBulk)this.getIncarichi_repertorioColl().get(0)).getEsito_corte_conti().equals(Incarichi_repertorioBulk.ESITO_ILLEGITTIMO))
				return ti_statoKeys.get(STATO_ANNULLATO).toString();
			return ti_statoKeys.get(STATO_CHIUSO).toString();
		}
		return null;
	}
	/**
	 * Ritorna TRUE se:
	 *  - la richiesta non è in stato "Provvisorio"
	 * 
	 * @return boolean
	 */
	public boolean isROPrimaFaseProcedura(){
		return getFaseProcesso().compareTo(FASE_PUBBLICAZIONE)!=-1;
	}
	public boolean isROProcedura(){
		return isProceduraAnnullata()||isProceduraChiusa()||
			   isProceduraDefinitiva()||isProceduraScaduta();
	}
	public boolean isROIncaricoRichiesta(){
		return isROPrimaFaseProcedura() ||
		       (getProcedura_amministrativa()==null ||
		        getProcedura_amministrativa().getFl_ricerca_incarico()==null ||
		        !getProcedura_amministrativa().getFl_ricerca_incarico().booleanValue());
	}
	public boolean isRONr_Contratti(){
		return isROPrimaFaseProcedura() || !isProceduraDaPubblicare();
	}
    private boolean isFasePubblicazione() {
    	return getStato()!=null &&
		       isPubblicazioneInCorso();
    }
    private boolean isFaseInserimentoContratto() {
    	return getStato()!=null &&
		   	   isProceduraInScadenza();
    }
    private boolean isFaseDefinitiva() {
    	return getStato()!=null &&
		   	   (isProceduraInviataCorteConti()||
				isProceduraDefinitiva()||
		   	    isProceduraAnnullata()||
		   	    isProceduraRespinta()||
		   	    isProceduraScaduta()||
		   	    isProceduraChiusa());
    }
	public Integer getFaseProcesso() {
		if (faseProcesso==null) { 
			if (isFasePubblicazione())
				faseProcesso = FASE_PUBBLICAZIONE;
			else if (isFaseInserimentoContratto())
				faseProcesso = FASE_INSERIMENTO_CONTRATTO;
			else if (isFaseDefinitiva())
				faseProcesso = FASE_DEFINITIVA;
			else
				faseProcesso = FASE_INSERIMENTO_INCARICO;
		}
		return faseProcesso;
 	}
	public Incarichi_procedura_archivioBulk getBando(){
		for ( Iterator i = getArchivioAllegati().iterator(); i.hasNext(); ) {
			Incarichi_procedura_archivioBulk allegato = (Incarichi_procedura_archivioBulk)i.next();
			if (allegato.isBando()) {
				return allegato;
			}
		}
		return null;
	}
	public Incarichi_procedura_archivioBulk getDecisioneAContrattare(){
		for ( Iterator i = getArchivioAllegati().iterator(); i.hasNext(); ) {
			Incarichi_procedura_archivioBulk allegato = (Incarichi_procedura_archivioBulk)i.next();
			if (allegato.isDecisioneAContrattare()) {
				return allegato;
			}
		}
		return null;
	}
	public Incarichi_procedura_archivioBulk getProgetto(){
		for ( Iterator i = getArchivioAllegati().iterator(); i.hasNext(); ) {
			Incarichi_procedura_archivioBulk allegato = (Incarichi_procedura_archivioBulk)i.next();
			if (allegato.isProgetto()) {
				return allegato;
			}
		}
		return null;
	}
	public boolean isProceduraDaPubblicare() {
		return this != null &&
			   this.getProcedura_amministrativa() != null &&
			   this.getProcedura_amministrativa().getIncarico_ric_giorni_pubbl() != null &&
			   this.getProcedura_amministrativa().getIncarico_ric_giorni_pubbl().compareTo(new Integer(0))==1;
	}
	@Override
	public void insertedUsing(Persister persister, UserContext userContext) {
		if (getFl_meramente_occasionale().booleanValue())
			setFl_pubblica_contratto(Boolean.FALSE);
		else
			setFl_pubblica_contratto(getTipo_attivita().getFl_pubblica_contratto());
		super.insertedUsing(persister, userContext);
	}
	public boolean isDataProrogaEnableOnView() {
		return dataProrogaEnableOnView;
	}
	public void setDataProrogaEnableOnView(boolean dataProrogaEnableOnView) {
		this.dataProrogaEnableOnView = dataProrogaEnableOnView;
	}
	/*
	 * Modifica inserita per consentire di cambiare il tipo di incarico tra
	 * Occasioni con e senza partita iva anche dopo la pubblicazione.
	 * 
	 * Non è possibile cambiare la tipologia di incarico dopo la pubblicazione
	 * se la stessa è associata ad un rapporto che prevede l'inquadramento (Es. Co.Co.Co.)
	 * E' possibile cambiarlo se il tipo rapporto non prevede inquadramento (occasionali con e 
	 * senza p.iva), ma solo nell'ambito della stessa tipologia 
	 */
	public boolean isROTipoIncarico() {
		Integer faseProcesso = getFaseProcesso();  
		if (faseProcesso.compareTo(FASE_DEFINITIVA)==0 || 
			   (faseProcesso.compareTo(FASE_INSERIMENTO_INCARICO)!=0 &&
			    !isUtenteCollegatoUoEnte() && !isUtenteCollegatoSuperUtente() &&
		        getTipo_incarico()!=null &&
		        getTipo_incarico().getTipoRapporto() != null &&
		        getTipo_incarico().getTipoRapporto().getFl_inquadramento() != null &&
		        getTipo_incarico().getTipoRapporto().getFl_inquadramento().booleanValue()))
			return true;
		for (Iterator<Incarichi_repertorioBulk> i=getIncarichi_repertorioColl().iterator();i.hasNext();){
			Incarichi_repertorioBulk incarico = i.next();
			if (incarico.isIncaricoDefinitivo() || incarico.isIncaricoChiuso() || incarico.isIncaricoInviatoCorteConti())
				return true;
		}
		return false;
	}
	public boolean isRODataScadenza() {
		Integer faseProcesso = getFaseProcesso();  
		return ((!isProceduraScaduta() && 
				  (faseProcesso.compareTo(FASE_DEFINITIVA)==0 || 
				   faseProcesso.compareTo(FASE_INSERIMENTO_INCARICO)!=0)) || 
				(isProceduraScaduta() && !isUtenteCollegatoUoEnte() && !isUtenteCollegatoSuperUtente()));
	}
	/*
	 * Modifica inserita per consentire di cambiare il tipo di attività ad
	 * un utente di tipo Ente consentito fino a quando l'incarico non assume 
	 * stato "Definitivo"
	 */
	public boolean isROTipoAttivita() {
		Integer faseProcesso = getFaseProcesso();  
		if (faseProcesso.compareTo(FASE_DEFINITIVA)==0 || 
			  (faseProcesso.compareTo(FASE_INSERIMENTO_INCARICO)!=0 &&
        	   !isUtenteCollegatoUoEnte() && !isUtenteCollegatoSuperUtente()))
			return true;
		for (Iterator<Incarichi_repertorioBulk> i=getIncarichi_repertorioColl().iterator();i.hasNext();){
			Incarichi_repertorioBulk incarico = i.next();
			if (incarico.isIncaricoDefinitivo() || incarico.isIncaricoChiuso() || incarico.isIncaricoInviatoCorteConti())
				return true;
		}
		return false;
	}
	/*
	 * Modifica inserita per consentire di cambiare il tipo di natura ad
	 * un utente di tipo Ente consentito fino a quando l'incarico non assume 
	 * stato "Definitivo"
	 */
	public boolean isROTipoNatura() {
		Integer faseProcesso = getFaseProcesso();
		if (faseProcesso.compareTo(FASE_DEFINITIVA)==0 || 
			(faseProcesso.compareTo(FASE_INSERIMENTO_INCARICO)!=0 &&
        	 !isUtenteCollegatoUoEnte() && !isUtenteCollegatoSuperUtente()))
			return true;
		for (Iterator<Incarichi_repertorioBulk> i=getIncarichi_repertorioColl().iterator();i.hasNext();){
			Incarichi_repertorioBulk incarico = i.next();
			if (incarico.isIncaricoDefinitivo() || incarico.isIncaricoChiuso() || incarico.isIncaricoInviatoCorteConti())
				return true;
		}
		return false;
	}
	/*
	 * Modifica inserita per consentire di cambiare l'importo lordo ad
	 * un utente di tipo Ente consentito fino a quando l'incarico non assume 
	 * stato "Definitivo"
	 */
	public boolean isROImportoLordo() {
		Integer faseProcesso = getFaseProcesso();  
		if (faseProcesso.compareTo(FASE_DEFINITIVA)==0 || 
			   (faseProcesso.compareTo(FASE_INSERIMENTO_INCARICO)!=0 &&
        	    !isUtenteCollegatoUoEnte() && !isUtenteCollegatoSuperUtente()) ||
 	           (getNr_contratti()!=null&&getNr_contratti().compareTo(1)==1&&
 	            !getIncarichi_repertorioValidiColl().isEmpty()))
			return true;
		for (Iterator<Incarichi_repertorioBulk> i=getIncarichi_repertorioColl().iterator();i.hasNext();){
			Incarichi_repertorioBulk incarico = i.next();
			if (incarico.isIncaricoInviatoCorteConti() || incarico.isIncaricoDefinitivo() || incarico.isIncaricoChiuso())
				return true;
		}
		return false;
	}
	public boolean isUtenteCollegatoUoEnte() {
		return utenteCollegatoUoEnte;
	}
	public void setUtenteCollegatoUoEnte(boolean utenteCollegatoUoEnte) {
		this.utenteCollegatoUoEnte = utenteCollegatoUoEnte;
	}
	public boolean isUtenteCollegatoSuperUtente() {
		return utenteCollegatoSuperUtente;
	}
	public void setUtenteCollegatoSuperUtente(boolean utenteCollegatoSuperUtente) {
		this.utenteCollegatoSuperUtente = utenteCollegatoSuperUtente;
	}
	public java.math.BigDecimal getImporto_utilizzato() {
		java.math.BigDecimal totale = new java.math.BigDecimal(0);
		for (Iterator y=this.getIncarichi_repertorioColl().iterator();y.hasNext();) {
			Incarichi_repertorioBulk incarico_repertorio = (Incarichi_repertorioBulk)y.next(); 
			for (Iterator i=incarico_repertorio.getIncarichi_repertorio_annoColl().iterator();i.hasNext();)
				totale = totale.add( ((Incarichi_repertorio_annoBulk)i.next()).getImporto_utilizzato());
		}
		return totale;
	}
	public java.math.BigDecimal getImporto_lordo_iniziale() {
		if (getImporto_lordo()==null || getNr_contratti()==null) return new BigDecimal(0);
		return Utility.nvl(getImporto_lordo()).multiply(new BigDecimal(getNr_contratti()));
	}
	public java.math.BigDecimal getImporto_lordo_variazioni() {
		java.math.BigDecimal totale = BigDecimal.ZERO;
		for (Iterator<Incarichi_repertorioBulk> i=this.getIncarichi_repertorioColl().iterator();i.hasNext();)
			totale = totale.add(i.next().getImporto_lordo_variazioni());
		return totale;
	}
	public java.math.BigDecimal getImporto_lordo_procedura() {
		return getImporto_lordo_iniziale().add(getImporto_lordo_variazioni());
	}
	public int addToIncarichi_repertorioColl(Incarichi_repertorioBulk dett) {
		dett.setIncarichi_procedura(this);
		dett.setCds(this.getCds());
		dett.setUnita_organizzativa(this.getUnita_organizzativa());
		dett.setStato(Incarichi_repertorioBulk.STATO_PROVVISORIO);
		dett.setFl_pubblica_contratto(this.getFl_pubblica_contratto());
		dett.setFl_inviato_corte_conti(Boolean.FALSE);		
		dett.setImporto_lordo(this.getImporto_lordo());
		dett.setImporto_complessivo(this.getImporto_complessivo());
		for (Iterator<Incarichi_procedura_annoBulk> i=getIncarichi_procedura_annoColl().iterator();i.hasNext();){
			Incarichi_procedura_annoBulk proceduraAnno = i.next();
			if ((!i.hasNext() && this.getImporto_complessivo().subtract(dett.getIm_complessivo_ripartito()).compareTo(Utility.ZERO)!=0) ||
			    (proceduraAnno.getImporto_iniziale().compareTo(Utility.ZERO)!=0)) {
				Incarichi_repertorio_annoBulk repertorioAnno = new Incarichi_repertorio_annoBulk();
				repertorioAnno.setToBeCreated();
				dett.addToIncarichi_repertorio_annoColl(repertorioAnno);
				repertorioAnno.setEsercizio_limite(proceduraAnno.getEsercizio_limite());
				if (!i.hasNext() && this.getImporto_complessivo().subtract(dett.getIm_complessivo_ripartito()).compareTo(Utility.ZERO)!=0)
					repertorioAnno.setImporto_iniziale(this.getImporto_complessivo().subtract(dett.getIm_complessivo_ripartito()));
				else
					repertorioAnno.setImporto_iniziale(proceduraAnno.getImporto_iniziale().divide(new BigDecimal(this.getNr_contratti()),2,java.math.BigDecimal.ROUND_HALF_UP));
				repertorioAnno.setImporto_complessivo(repertorioAnno.getImporto_iniziale());
				repertorioAnno.setAnniList(proceduraAnno.getAnniList());
			}
		}
		getIncarichi_repertorioColl().add(dett);
		return getIncarichi_repertorioColl().size()-1;
	}	
	public Incarichi_repertorioBulk removeFromIncarichi_repertorioColl(int index) {
		Incarichi_repertorioBulk dett = (Incarichi_repertorioBulk)getIncarichi_repertorioColl().remove(index);
		return dett;
	}
    //	 Elenco Repertorio Incarichi Affidati Validi
	public BulkList getIncarichi_repertorioValidiColl() {
		BulkList incarichiValidiColl = new BulkList(); 
		for (Iterator i=getIncarichi_repertorioColl().iterator();i.hasNext();){
			Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)i.next();
			if (!incarico.isIncaricoAnnullato())
				incarichiValidiColl.add(incarico);
		}
		return incarichiValidiColl;
	}
	private void setStatoProcedura(String statoProcedura) {
		this.statoProcedura = statoProcedura;
	}
	private String getStatoProcedura() {
		return statoProcedura;
	}
	public boolean isROFlMeramenteOccasionale() {
		if (this.getProcedura_amministrativa()!=null &&
		    this.getProcedura_amministrativa().isMeramenteOccasionaleRequired() &&
		    getFl_meramente_occasionale().booleanValue()) 
			return true;
		
		for (Iterator<Incarichi_repertorioBulk> i=getIncarichi_repertorioColl().iterator();i.hasNext();){
			Incarichi_repertorioBulk incarico = i.next();
			if (incarico.isIncaricoInviatoCorteConti() || incarico.isIncaricoDefinitivo() || incarico.isIncaricoChiuso())
				return true;
		}
		return false;
	}	
    public boolean isMeramenteOccasionaleEnabled(){
    	return this.getProcedura_amministrativa()!=null &&
    		   this.getProcedura_amministrativa().isMeramenteOccasionaleEnabled() &&
    		   this.isProceduraForOccasionali();
    }
    public boolean isProceduraForOccasionali(){
    	return this.getTipo_incarico()!=null &&
    		   this.getTipo_incarico().getTipoRapporto()!=null &&
    		   this.getTipo_incarico().getTipoRapporto().getFl_inquadramento()!=null &&
    		   !this.getTipo_incarico().getTipoRapporto().getFl_inquadramento().booleanValue();
    }
    public boolean isProceduraForBorseStudio(){
    	return this.getTipo_incarico()!=null &&
    		   this.getTipo_incarico().getTipo_associazione()!=null &&
    		   this.getTipo_incarico().getTipo_associazione().equals(Tipo_incaricoBulk.ASS_BORSE_STUDIO) &&
    		   this.getTipo_attivita()!=null &&
    		   this.getTipo_attivita().getTipo_associazione()!=null &&
    		   this.getTipo_attivita().getTipo_associazione().equals(Tipo_attivitaBulk.ASS_BORSE_STUDIO);
    }
    public boolean isProceduraForAssegniRicerca(){
    	return this.getTipo_incarico()!=null &&
     		   this.getTipo_incarico().getTipo_associazione()!=null &&
    		   this.getTipo_incarico().getTipo_associazione().equals(Tipo_incaricoBulk.ASS_ASSEGNI_RICERCA) &&
    		   this.getTipo_attivita()!=null &&
    		   this.getTipo_attivita().getTipo_associazione()!=null &&
    		   this.getTipo_attivita().getTipo_associazione().equals(Tipo_attivitaBulk.ASS_ASSEGNI_RICERCA);
    }
    public boolean isProceduraForIncarichi(){
    	return this.getTipo_incarico()!=null &&
     		   this.getTipo_incarico().getTipo_associazione()!=null &&
    		   this.getTipo_incarico().getTipo_associazione().equals(Tipo_incaricoBulk.ASS_INCARICHI) &&
    		   this.getTipo_attivita()!=null &&
    		   this.getTipo_attivita().getTipo_associazione()!=null &&
    		   this.getTipo_attivita().getTipo_associazione().equals(Tipo_attivitaBulk.ASS_INCARICHI);
    }
    public boolean isDichiarazioneContraenteRequired(){
		return this.isProceduraForIncarichi() && !this.isProceduraForOccasionali();
    }
    public boolean isProceduraMultiIncarico(){
    	return getNr_contratti()!=null &&
    	       getNr_contratti().intValue()>1;
    }
	public java.math.BigDecimal getImporto_complessivo_iniziale() {
		if (getImporto_complessivo()==null || getNr_contratti()==null) return BigDecimal.ZERO;
		return getImporto_complessivo().multiply(new BigDecimal(getNr_contratti()));
	}
	public java.math.BigDecimal getImporto_complessivo_variazioni() {
		java.math.BigDecimal totale = BigDecimal.ZERO;
		for (Iterator<Incarichi_repertorioBulk> i=this.getIncarichi_repertorioColl().iterator();i.hasNext();)
			totale = totale.add(i.next().getImporto_complessivo_variazioni());
		return totale;
	}
	public java.math.BigDecimal getImporto_complessivo_procedura() {
		return getImporto_complessivo_iniziale().add(getImporto_complessivo_variazioni());
	}
	public java.math.BigDecimal getImporto_da_ripartire() {
		return getImporto_complessivo_procedura().subtract(getIm_complessivo_ripartito());
	}
	public boolean hasVariazioni() {
		if (getImporto_complessivo_variazioni().compareTo(BigDecimal.ZERO)!=0)
			return true;
		return false;
	}
	public TerzoBulk getTerzoForSearch() {
		if (getV_terzoForSearch()==null)
			return null;
		return getV_terzoForSearch().getTerzo();
	}
	public V_terzo_per_compensoBulk getV_terzoForSearch() {
		return v_terzoForSearch;
	}
	public void setV_terzoForSearch(V_terzo_per_compensoBulk v_terzoForSearch) {
		this.v_terzoForSearch = v_terzoForSearch;
	}
	/**
	 * Ritorna l'indirizzo del terzo contraente ottenuto dalla concatenazione di:
	 *  - Via
	 *  - Numero civico
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getIndirizzoTerzoForSearch() {

		if (getTerzoForSearch()==null)
			return null;

		String indirizzo = "";
		if (getTerzoForSearch().getVia_sede()!=null)
			indirizzo = indirizzo + getTerzoForSearch().getVia_sede();
		if (getTerzoForSearch().getNumero_civico_sede()!=null)
			indirizzo = indirizzo + " " + getTerzoForSearch().getNumero_civico_sede();
			
		return indirizzo;
	}
    public boolean isProceduraSbloccata() {
    	return this.getFl_sbloccato();
    }
	/**
	 * Ritorna TRUE se la richiesta è in stato "Inviato Corte Conti"
	 * 
	 * @return boolean
	 */
    public boolean isProceduraInviataCorteConti() {
    	return this.getStato()!=null&&this.getStato().equals(STATO_INVIATO);
    }
	public BulkList getIncarichi_procedura_noteColl() {
		return incarichi_procedura_noteColl;
	}
	public void setIncarichi_procedura_noteColl(BulkList incarichiProceduraNoteColl) {
		incarichi_procedura_noteColl = incarichiProceduraNoteColl;
	}
	public int addToIncarichi_procedura_noteColl(Incarichi_procedura_noteBulk dett) {
		dett.setIncarichi_procedura(this);
		getIncarichi_procedura_noteColl().add(dett);
		return getIncarichi_procedura_noteColl().size()-1;
	}	
	public Incarichi_procedura_noteBulk removeFromIncarichi_procedura_noteColl(int index) {
		Incarichi_procedura_noteBulk dett = (Incarichi_procedura_noteBulk)getIncarichi_procedura_noteColl().remove(index);
		return dett;
	}
    public java.util.Collection getTipologie_prestazioni() {
    	return tipologie_prestazioni;
    }
    public void setTipologie_prestazioni(java.util.Collection tipologie_prestazioni) {
		this.tipologie_prestazioni = tipologie_prestazioni;
	}
	public Tipo_attivita_fpBulk getTipo_attivita_fp() {
		return tipo_attivita_fp;
	}
	public void setTipo_attivita_fp(Tipo_attivita_fpBulk tipo_attivita_fp) {
		this.tipo_attivita_fp = tipo_attivita_fp;
	}
	@Override
	public String getCd_tipo_attivita_fp() {
		if (this.getTipo_attivita_fp() == null)
			return null;
		return this.getTipo_attivita_fp().getCd_tipo_attivita();
	}
	public void setCd_tipo_attivita_fp(java.lang.String cd_tipo_attivita_fp) {
		if (this.getTipo_attivita_fp() != null)
			this.getTipo_attivita_fp().setCd_tipo_attivita(cd_tipo_attivita_fp);
	}
	public Tipo_attivita_fpBulk getTipo_attivita_fp0() {
		return tipo_attivita_fp0;
	}
	public void setTipo_attivita_fp0(Tipo_attivita_fpBulk tipo_attivita_fp0) {
		this.tipo_attivita_fp0 = tipo_attivita_fp0;
	}
	public Tipo_attivita_fpBulk getTipo_attivita_fp1() {
		return tipo_attivita_fp1;
	}
	public void setTipo_attivita_fp1(Tipo_attivita_fpBulk tipo_attivita_fp1) {
		this.tipo_attivita_fp1 = tipo_attivita_fp1;
	}
	public Tipo_prestazioneBulk getTipo_prestazione_orig() {
		return tipo_prestazione_orig;
	}
	public void setTipo_prestazione_orig(Tipo_prestazioneBulk tipo_prestazione_orig) {
		this.tipo_prestazione_orig = tipo_prestazione_orig;
	}
    public Tipo_attivita_fpBulk getTipo_attivita_fp_orig() {
		return tipo_attivita_fp_orig;
	}
    public void setTipo_attivita_fp_orig(Tipo_attivita_fpBulk tipo_attivita_fp_orig) {
		this.tipo_attivita_fp_orig = tipo_attivita_fp_orig;
	}
	public boolean isROTipoAttivitaFp(){
		return getTipo_attivita_fp_orig()!=null?isROTipoAttivita():false;
	}
	public boolean isROTipoPrestazione(){
		return getTipo_prestazione_orig()!=null?isROTipoAttivita():false;
	}
	public Tipo_norma_perlaBulk getTipo_norma_perla() {
		return tipo_norma_perla;
	}
	public void setTipo_norma_perla(Tipo_norma_perlaBulk tipo_norma_perla) {
		this.tipo_norma_perla = tipo_norma_perla;
	}
	public String getCd_tipo_norma_perla() {
		if (this.getTipo_norma_perla() == null)
			return null;
		return this.getTipo_norma_perla().getCd_tipo_norma();
	}
	public void setCd_tipo_norma_perla(String cd_tipo_norma_perla) {
		if (this.getTipo_norma_perla() != null)
			this.getTipo_norma_perla().setCd_tipo_norma(cd_tipo_norma_perla);
	}
	public Tipo_norma_perlaBulk getTipo_norma_perla_orig() {
		return tipo_norma_perla_orig;
	}
	public void setTipo_norma_perla_orig(Tipo_norma_perlaBulk tipo_norma_perla_orig) {
		this.tipo_norma_perla_orig = tipo_norma_perla_orig;
	}
	public java.util.Collection getTipologie_norma_perla() {
		return tipologie_norma_perla;
	}
	public void setTipologie_norma_perla(java.util.Collection tipologie_norma_perla) {
		this.tipologie_norma_perla = tipologie_norma_perla;
	}
	public boolean isROTipoNormaPerla(){
		if (isROFlApplicazioneNorma()) return true;
		if (!isApplicazioneNormaAttiva()) return true;
		if (getTipo_norma_perla_orig()!=null) return isROTipoAttivita();
		return false;
	}
	public String getFl_applicazione_norma_orig() {
		return fl_applicazione_norma_orig;
	}
	public void setFl_applicazione_norma_orig(String fl_applicazione_norma_orig) {
		this.fl_applicazione_norma_orig = fl_applicazione_norma_orig;
	}
	public boolean isROFlApplicazioneNorma(){
		return getFl_applicazione_norma_orig()!=null?isROTipoAttivita():false;
	}
	public boolean isApplicazioneNormaAttiva(){
		return getFl_applicazione_norma()!=null &&
			   getFl_applicazione_norma().equals(APPLICAZIONE_NORMA_YES);
	}
	
	private class Incarichi_parametriLink implements Serializable{
		private Incarichi_parametriBulk incarichiParametri;
		private String cdProcAmm, cdTipoAtt, cdTipoInc, tipoNatura; 
		private boolean flMeraOcc=false, flArt51=false;

		public Incarichi_parametriBulk getIncarichiParametri() {
			return incarichiParametri;
		}

		public void setIncarichiParametri(Incarichi_parametriBulk parametri) {
			this.incarichiParametri = parametri;
		}

		private String getCdProcAmm() {
			return cdProcAmm;
		}

		private void setCdProcAmm(String cdProcAmm) {
			this.cdProcAmm = cdProcAmm;
		}

		private String getCdTipoAtt() {
			return cdTipoAtt;
		}

		private void setCdTipoAtt(String cdTipoAtt) {
			this.cdTipoAtt = cdTipoAtt;
		}

		private String getCdTipoInc() {
			return cdTipoInc;
		}

		private void setCdTipoInc(String cdTipoInc) {
			this.cdTipoInc = cdTipoInc;
		}

		private String getTipoNatura() {
			return tipoNatura;
		}

		private void setTipoNatura(String tipoNatura) {
			this.tipoNatura = tipoNatura;
		}

		private boolean isFlMeraOcc() {
			return flMeraOcc;
		}

		private void setFlMeraOcc(boolean flMeraOcc) {
			this.flMeraOcc = flMeraOcc;
		}

		private boolean isFlArt51() {
			return flArt51;
		}

		private void setFlArt51(boolean flArt51) {
			this.flArt51 = flArt51;
		}
		
		public boolean isEqualsFieldParameter(Incarichi_proceduraBulk procedura) {
			String cdProcAmmNew = procedura.getProcedura_amministrativa()!=null?procedura.getProcedura_amministrativa().getCd_proc_amm():null;
			String cdTipoAttNew = procedura.getTipo_attivita()!=null?procedura.getTipo_attivita().getCd_tipo_attivita():null;
			String cdTipoIncNew = procedura.getTipo_incarico()!=null?procedura.getTipo_incarico().getCd_tipo_incarico():null;
			String tipoNaturaNew = procedura.getTipo_natura();
			boolean isFlMeraOccNew = procedura.getFl_meramente_occasionale()==null?false:procedura.getFl_meramente_occasionale();
			boolean isFlArt51New = procedura.getFl_art51()==null?false:procedura.getFl_art51();

			if ((getCdProcAmm()==null && cdProcAmmNew!=null)||(getCdProcAmm()!=null && cdProcAmmNew==null)||
				(getCdProcAmm()!=null && cdProcAmmNew!=null && !getCdProcAmm().equals(cdProcAmmNew)) ||
				(getCdTipoAtt()==null && cdTipoAttNew!=null)||(getCdTipoAtt()!=null && cdTipoAttNew==null)||
				(getCdTipoAtt()!=null && cdTipoAttNew!=null && !getCdTipoAtt().equals(cdTipoAttNew)) ||			
				(getCdTipoInc()==null && cdTipoIncNew!=null)||(getCdTipoInc()!=null && cdTipoIncNew==null)||
				(getCdTipoInc()!=null && cdTipoIncNew!=null && !getCdTipoInc().equals(cdTipoIncNew)) ||			
				(getTipoNatura()==null && tipoNaturaNew!=null)||(getTipoNatura()!=null && tipoNaturaNew==null)||
				(getTipoNatura()!=null && tipoNaturaNew!=null && !getTipoNatura().equals(tipoNaturaNew)) ||			
				isFlMeraOcc()!=isFlMeraOccNew || isFlArt51()!=isFlArt51New)
				return false;
			else
				return true;
		}
		
		public void initIncarichiParametri(Incarichi_proceduraBulk procedura, Incarichi_parametriBulk incarichiParametri) {
			String cdProcAmmNew = procedura.getProcedura_amministrativa()!=null?procedura.getProcedura_amministrativa().getCd_proc_amm():null;
			String cdTipoAttNew = procedura.getTipo_attivita()!=null?procedura.getTipo_attivita().getCd_tipo_attivita():null;
			String cdTipoIncNew = procedura.getTipo_incarico()!=null?procedura.getTipo_incarico().getCd_tipo_incarico():null;
			String tipoNaturaNew = procedura.getTipo_natura();
			boolean isFlMeraOccNew = procedura.getFl_meramente_occasionale()==null?false:procedura.getFl_meramente_occasionale();
			boolean isFlArt51New = procedura.getFl_art51()==null?false:procedura.getFl_art51();

			setCdProcAmm(cdProcAmmNew);
			setCdTipoAtt(cdTipoAttNew);
			setCdTipoInc(cdTipoIncNew);
			setTipoNatura(tipoNaturaNew);
			setFlMeraOcc(isFlMeraOccNew);
			setFlArt51(isFlArt51New);

			setIncarichiParametri(incarichiParametri);
		}
			
	}

	private Incarichi_parametriLink incarichi_parametriLink;
	
	private Incarichi_parametriLink getIncarichi_parametriLink() {
		return incarichi_parametriLink;
	}
	private void setIncarichi_parametriLink(Incarichi_parametriLink incarichi_parametriLink) {
		this.incarichi_parametriLink = incarichi_parametriLink;
	}

	public StorageFolderProcedura getCMISFolder() {
		return new StorageFolderProcedura(this);
	}
	
	public boolean isEqualsFieldParameter() {
		if (this.getIncarichi_parametriLink()==null)
			return false;
		return this.getIncarichi_parametriLink().isEqualsFieldParameter(this);
	}

	public void initIncarichiParametri(Incarichi_parametriBulk incarichiParametri) {
		if (getIncarichi_parametriLink()==null)
			setIncarichi_parametriLink(new Incarichi_parametriLink());
		getIncarichi_parametriLink().initIncarichiParametri(this, incarichiParametri);
	}

	public Incarichi_parametriBulk getIncarichiParametri() {
		if (getIncarichi_parametriLink()==null)
			return null;
		return getIncarichi_parametriLink().getIncarichiParametri();
	}
	
	public Incarichi_repertorioBulk getIncaricoRepertorioForSearch() {
		return incaricoRepertorioForSearch;
	}
	
	public void setIncaricoRepertorioForSearch(Incarichi_repertorioBulk incaricoRepertorioForSearch) {
		this.incaricoRepertorioForSearch = incaricoRepertorioForSearch;
	}
}