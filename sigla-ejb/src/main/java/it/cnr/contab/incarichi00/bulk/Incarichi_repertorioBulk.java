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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 26/07/2007
 */
package it.cnr.contab.incarichi00.bulk;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk;
import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.incarichi00.bulk.storage.StorageFolderAssegniRicerca;
import it.cnr.contab.incarichi00.bulk.storage.StorageFolderBorseStudio;
import it.cnr.contab.incarichi00.bulk.storage.StorageFolderContrattiModel;
import it.cnr.contab.incarichi00.bulk.storage.StorageFolderIncarico;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(value=Include.NON_NULL)
public class Incarichi_repertorioBulk extends Incarichi_repertorioBase {
	public final static Dictionary ti_statoKeys = new it.cnr.jada.util.OrderedHashtable();
	public final static Dictionary ti_esitoCorteContiKeys = new it.cnr.jada.util.OrderedHashtable();
	private java.util.Collection sediAce;

	private boolean dataProrogaEnableOnView = Boolean.FALSE;
	private boolean utenteCollegatoUoEnte = Boolean.FALSE;
	
	final public static String STATO_PROVVISORIO = "PP";
	final public static String STATO_ANNULLATO = "PN";
	final public static String STATO_ELIMINATO = "EL";
	final public static String STATO_DEFINITIVO = "PD";
	final public static String STATO_INVIATO = "IN";

	public Collection getSediAce() {
		return sediAce;
	}

	public void setSediAce(Collection sediAce) {
		this.sediAce = sediAce;
	}

	final public static String STATO_CHIUSO = "CC";

	static {
		ti_statoKeys.put(STATO_PROVVISORIO,"Provvisorio");
		ti_statoKeys.put(STATO_ANNULLATO,"Annullato");
		ti_statoKeys.put(STATO_ELIMINATO,"Eliminato");
		ti_statoKeys.put(STATO_DEFINITIVO,"Definitivo");
		ti_statoKeys.put(STATO_INVIATO,"Inviato Corte Conti");
		ti_statoKeys.put(STATO_CHIUSO,"Chiuso");
	}

	final public static String ESITO_LEGITTIMO = "LEG";
	final public static String ESITO_ILLEGITTIMO = "ILL";

	static {
		ti_esitoCorteContiKeys.put(ESITO_LEGITTIMO,"Legittimo");
		ti_esitoCorteContiKeys.put(ESITO_ILLEGITTIMO,"Illegittimo");
	}

	private CdsBulk cds;
	private BulkList incarichi_repertorio_annoColl = new BulkList();
	private BulkList archivioAllegati = new BulkList();
	private BulkList incarichi_repertorio_varColl = new BulkList();
	private BulkList incarichi_repertorio_rappColl = new BulkList();
	private BulkList incarichi_repertorio_rapp_detColl = new BulkList();
	private Unita_organizzativaBulk unita_organizzativa;
	private Incarichi_proceduraBulk incarichi_procedura;
	private V_terzo_per_compensoBulk v_terzo = new V_terzo_per_compensoBulk();
	private TerzoBulk terzoForColumnMap;
	private Tipo_rapportoBulk tipo_rapporto;
	private Tipo_trattamentoBulk tipo_trattamento = new Tipo_trattamentoBulk();
	private java.util.Collection tipiRapporto;

	private BulkList associazioneUO = new BulkList();
	private BulkList associazioneUODisponibili = new BulkList();

	private java.sql.Timestamp dt_efficacia_orig;
	
	public Incarichi_repertorioBulk() {
		super();
	}
	public Incarichi_repertorioBulk(java.lang.Integer esercizio, java.lang.Long pg_repertorio) {
		super(esercizio, pg_repertorio);
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

// Contraente
	public TerzoBulk getTerzo() {
		if (getV_terzo()==null)
			return null;
		return getV_terzo().getTerzo();
	}
	public V_terzo_per_compensoBulk getV_terzo() {
		return v_terzo;
	}
	public void setV_terzo(V_terzo_per_compensoBulk v_terzo) {
		this.v_terzo = v_terzo;
	}
	public Integer getCd_terzo() {
		return super.getCd_terzo();
	}
	public void setCd_terzo(Integer cd_terzo) {
		super.setCd_terzo(cd_terzo);
	}	

// 	Procedura Incarico Associata
	public Incarichi_proceduraBulk getIncarichi_procedura() {
		return incarichi_procedura;
	}
	public void setIncarichi_procedura(Incarichi_proceduraBulk incarichi_procedura) {
		this.incarichi_procedura = incarichi_procedura;
	}
	public Integer getEsercizio_procedura() {
		if (this.getIncarichi_procedura() == null)
			return null;
		return this.getIncarichi_procedura().getEsercizio();
	}
	public void setEsercizio_procedura(Integer setEsercizio_procedura) {
		if (this.getIncarichi_procedura() != null)
			this.getIncarichi_procedura().setEsercizio(setEsercizio_procedura);
	}	
	public Long getPg_procedura() {
		if (this.getIncarichi_procedura() == null)
			return null;
		return this.getIncarichi_procedura().getPg_procedura();
	}
	public void setPg_procedura(Long pg_procedura) {
		if (this.getIncarichi_procedura() != null)
			this.getIncarichi_procedura().setPg_procedura(pg_procedura);
	}
	
// Tipo Rapporto
	public java.util.Collection getTipiRapporto() {
		return tipiRapporto;
	}
	public void setTipiRapporto(java.util.Collection newTipiRapporto) {
		tipiRapporto = newTipiRapporto;
	}
	public it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk getTipo_rapporto() {
		return tipo_rapporto;
	}
	public void setTipo_rapporto(it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk newTipo_rapporto) {
		tipo_rapporto = newTipo_rapporto;
	}
	public String getCd_tipo_rapporto() {
		it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk tipo_rapporto = this.getTipo_rapporto();
		if (tipo_rapporto == null)
			return null;
		return tipo_rapporto.getCd_tipo_rapporto();
	}
	public void setCd_tipo_rapporto(String cd_tipo_rapporto) {
		this.getTipo_rapporto().setCd_tipo_rapporto(cd_tipo_rapporto);
	}
	
	public it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk getTipo_trattamento() {
		return tipo_trattamento;
	}
	public void setTipo_trattamento(it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk newTipo_trattamento) {
		tipo_trattamento = newTipo_trattamento;
	}
	public String getCd_trattamento() {
		it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk tipo_trattamento = this.getTipo_trattamento();
		if (tipo_trattamento == null)
			return null;
		return tipo_trattamento.getCd_trattamento();
	}
	public void setCd_trattamento(String cd_trattamento) {
		this.getTipo_trattamento().setCd_trattamento(cd_trattamento);
	}

	/**
	 * Restituisce il valore della proprietà 'ds_terzo'
	 *
	 * @return Il valore della proprietà 'ds_terzo'
	 */
	public java.lang.String getDs_terzo() {
		if ( getTerzo() != null && getTerzo().getAnagrafico() != null &&
				getTerzo().getAnagrafico().getCognome()!=null )
			return getTerzo().getAnagrafico().getCognome() + " " + getTerzo().getAnagrafico().getNome();
		return "";	
	}	

	public BulkList getIncarichi_repertorio_annoColl() {
		return incarichi_repertorio_annoColl;
	}
	
	public void setIncarichi_repertorio_annoColl(
			BulkList incarichi_repertorio_annoColl) {
		this.incarichi_repertorio_annoColl = incarichi_repertorio_annoColl;
	}
	public BulkList getArchivioAllegati() {
		return archivioAllegati;
	}
	public void setArchivioAllegati(BulkList list) {
		archivioAllegati = list;
	}
	/**
	 * Restituisce un array di <code>BulkCollection</code> contenenti oggetti
	 * bulk da rendere persistenti insieme al ricevente.
	 * L'implementazione standard restituisce <code>null</code>.
	 */
	public BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] { 
				getIncarichi_repertorio_annoColl(), getArchivioAllegati(), 
				getIncarichi_repertorio_varColl(), getAssociazioneUO(),
				getAssociazioneUODisponibili(), getIncarichi_repertorio_rappColl(),
				getIncarichi_repertorio_rapp_detColl()};
	}
	public int addToIncarichi_repertorio_annoColl(Incarichi_repertorio_annoBulk dett) {
		dett.setIncarichi_repertorio(this);
		dett.setImporto_iniziale(Utility.ZERO);
		dett.setImporto_complessivo(Utility.ZERO);
		getIncarichi_repertorio_annoColl().add(dett);
		return getIncarichi_repertorio_annoColl().size()-1;
	}	
	public Incarichi_repertorio_annoBulk removeFromIncarichi_repertorio_annoColl(int index) {
		Incarichi_repertorio_annoBulk dett = (Incarichi_repertorio_annoBulk)getIncarichi_repertorio_annoColl().remove(index);
		return dett;
	}
	public int addToArchivioAllegati(Incarichi_repertorio_archivioBulk dett) {
		dett.setIncarichi_repertorio(this);
		dett.setStato(Incarichi_repertorio_archivioBulk.STATO_VALIDO);
		getArchivioAllegati().add(dett);
		return getArchivioAllegati().size()-1;
	}	
	public Incarichi_repertorio_archivioBulk removeFromArchivioAllegati(int index) {
		Incarichi_repertorio_archivioBulk dett = (Incarichi_repertorio_archivioBulk)getArchivioAllegati().remove(index);
		return dett;
	}
	public boolean isROCds() {
		return getUnita_organizzativa()!=null && 
		       getUnita_organizzativa().getCd_unita_organizzativa()!=null;
	}
	public boolean isRODataContratto() {
		return getCrudStatus()==NORMAL &&
			   isROIncarico();
	}
	public boolean isRODataProrogaOnView() {
		return !isDataProrogaEnableOnView();
	}
	public boolean isRODataProrogaPagamOnView() {
		return !isDataProrogaEnableOnView();
	}
	public boolean isRODataStipula() {
		return isRODataContratto() || getDt_inizio_validita() != null || 
		       getDt_invio_corte_conti() != null || 
		       isIncaricoInviatoCorteConti();
	}
	public boolean isRORapporto() {
		return isRODataContratto() || 
	   	       (getTerzo()==null || getTerzo().getCd_terzo()==null);
	}
	public boolean isROTrattamento() {
		return isRORapporto();
	}
	public boolean isROTerzo() {
		return isROIncarico()||
			   isIncaricoInviatoCorteConti()||
               (getTerzo() == null || getTerzo().getCrudStatus() == NORMAL);
	}
	public boolean isROSearchtoolTerzo() {
		return isROIncarico()||
			   isIncaricoInviatoCorteConti()||
	           getIncarichi_procedura().isProceduraScaduta();
	}
	public boolean isROIncarico() {
		return isIncaricoAnnullato()||isIncaricoDefinitivo()||isIncaricoChiuso()||
		       getIncarichi_procedura()==null || getIncarichi_procedura().isROProcedura();
	}
	public boolean isROIncaricoDefinitivo() {
		return isIncaricoDefinitivo();
	}
	/*
	 * Modifica inserita per consentire di cambiare l'importo lordo ad
	 * un utente di tipo Ente consentito fino a quando l'incarico non assume 
	 * stato "Definitivo"
	 */
	public boolean isROImportoLordo() {
		return getIncarichi_procedura()==null ||
			   getIncarichi_procedura().isROImportoLordo();
	}
	/**
	 * Ritorna una stringa che decodifica lo stato della richiesta
	 * 
	 * @return java.lang.String
	 */
	public String getStatoText(){
		if (this.isIncaricoProvvisorio())
			return ti_statoKeys.get(STATO_PROVVISORIO).toString();
		if (this.isIncaricoDefinitivo())
			return ti_statoKeys.get(STATO_DEFINITIVO).toString();
		if (this.isIncaricoInviatoCorteConti())
			return ti_statoKeys.get(STATO_INVIATO).toString();
		if (this.isIncaricoAnnullato())
			return ti_statoKeys.get(STATO_ANNULLATO).toString();
		if (this.isIncaricoChiuso()) {
			if (this.getEsito_corte_conti()!=null && 
				this.getEsito_corte_conti().equals(Incarichi_repertorioBulk.ESITO_ILLEGITTIMO))
				return ti_statoKeys.get(STATO_ANNULLATO).toString();
			return ti_statoKeys.get(STATO_CHIUSO).toString();
		}

		return null;
	}
	public java.util.Dictionary getTi_istituz_commercKeys() {
		return CompensoBulk.TIPI_COMPENSO;
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
		for (Iterator i=this.getIncarichi_repertorio_annoColl().iterator();i.hasNext();)
			totale = totale.add(((Incarichi_repertorio_annoBulk)i.next()).getImporto_complessivo());
		return totale;
	}
	/**
	 * Ritorna TRUE se la richiesta è in stato "Provvisorio"
	 * 
	 * @return boolean
	 */
	public boolean isIncaricoProvvisorio() {
    	return this.getStato()!=null&&this.getStato().equals(STATO_PROVVISORIO);
    }
	/**
	 * Ritorna TRUE se la richiesta è in stato "Definitiva"
	 * 
	 * @return boolean
	 */
    public boolean isIncaricoDefinitivo() {
    	return this.getStato()!=null&&this.getStato().equals(STATO_DEFINITIVO);
    }
	/**
	 * Ritorna TRUE se la richiesta è in stato "Chiusa"
	 * 
	 * @return boolean
	 */
    public boolean isIncaricoChiuso() {
    	return this.getStato()!=null&&this.getStato().equals(STATO_CHIUSO);
    }
	/**
	 * Ritorna TRUE se la richiesta è in stato "Annullato"
	 * 
	 * @return boolean
	 */
    public boolean isIncaricoAnnullato() {
    	return this.getStato()!=null&&this.getStato().equals(STATO_ANNULLATO);
    }
	/**
	 * Ritorna TRUE se la richiesta è in stato "Inviato Corte Conti"
	 * 
	 * @return boolean
	 */
    public boolean isIncaricoInviatoCorteConti() {
    	return this.getStato()!=null&&this.getStato().equals(STATO_INVIATO);
    }
	/**
	 * Ritorna l'indirizzo del terzo contraente ottenuto dalla concatenazione di:
	 *  - Via
	 *  - Numero civico
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getIndirizzoTerzo() {

		if (getTerzo()==null)
			return null;

		String indirizzo = "";
		if (getTerzo().getVia_sede()!=null)
			indirizzo = indirizzo + getTerzo().getVia_sede();
		if (getTerzo().getNumero_civico_sede()!=null)
			indirizzo = indirizzo + " " + getTerzo().getNumero_civico_sede();
			
		return indirizzo;
	}
	public Incarichi_repertorio_archivioBulk getContratto(){
		for ( Iterator i = getArchivioAllegati().iterator(); i.hasNext(); ) {
			Incarichi_repertorio_archivioBulk allegato = (Incarichi_repertorio_archivioBulk)i.next();
			if (allegato.isContratto()) {
				return allegato;
			}
		}
		return null;
	}
	public Incarichi_repertorio_archivioBulk getCurriculumVincitore(){
		for ( Iterator i = getArchivioAllegati().iterator(); i.hasNext(); ) {
			Incarichi_repertorio_archivioBulk allegato = (Incarichi_repertorio_archivioBulk)i.next();
			if (allegato.isCurriculumVincitore()) {
				return allegato;
			}
		}
		return null;
	}
	public List<Incarichi_repertorio_archivioBulk> getAggiornamentiCurriculumVincitore(){
		return Optional.ofNullable((BulkList<Incarichi_repertorio_archivioBulk>)getArchivioAllegati())
				.map(BulkList::stream)
				.orElse(Stream.empty())
				.filter(Incarichi_repertorio_archivioBulk.class::isInstance)
				.map(Incarichi_repertorio_archivioBulk.class::cast)
				.filter(el->el.isAggiornamentoCurriculumVincitore())
				.collect(Collectors.toList());
	}
	public Incarichi_repertorio_archivioBulk getDecretoDiNomina(){
		for ( Iterator i = getArchivioAllegati().iterator(); i.hasNext(); ) {
			Incarichi_repertorio_archivioBulk allegato = (Incarichi_repertorio_archivioBulk)i.next();
			if (allegato.isDecretoDiNomina()) {
				return allegato;
			}
		}
		return null;
	}
	public Incarichi_repertorio_archivioBulk getAttoEsitoControllo(){
		for ( Iterator i = getArchivioAllegati().iterator(); i.hasNext(); ) {
			Incarichi_repertorio_archivioBulk allegato = (Incarichi_repertorio_archivioBulk)i.next();
			if (allegato.isAttoEsitoControllo()) {
				return allegato;
			}
		}
		return null;
	}
	public Incarichi_repertorio_archivioBulk getConflittoInteressi(){
		for ( Iterator i = getArchivioAllegati().iterator(); i.hasNext(); ) {
			Incarichi_repertorio_archivioBulk allegato = (Incarichi_repertorio_archivioBulk)i.next();
			if (allegato.isConflittoInteressi()) {
				return allegato;
			}
		}
		return null;
	}
	public Incarichi_repertorio_archivioBulk getAttestazioneDirettore(){
		for ( Iterator i = getArchivioAllegati().iterator(); i.hasNext(); ) {
			Incarichi_repertorio_archivioBulk allegato = (Incarichi_repertorio_archivioBulk)i.next();
			if (allegato.isAttestazioneDirettore()) {
				return allegato;
			}
		}
		return null;
	}
	public void validaDateContratto() throws ValidationException {
//		if (this.getDt_stipula()!=null && this.getDt_provv()==null) 
//		    throw new ValidationException( "Non \350 possibile indicare la \"Data di stipula\" senza indicare la \"Data di protocollo\" del provvedimento di nomina.");
//		if (this.getDt_stipula()!=null && this.getDt_provv()!=null &&
//			this.getDt_provv().after(this.getDt_stipula()))
//			throw new ValidationException( "La \"Data di stipula\" del contratto deve essere uguale o superiore alla \"Data protocollo\" del provvedimento di nomina.");
		if (this.getDt_stipula()!=null && this.getIncarichi_procedura()!=null && this.getIncarichi_procedura().getDt_fine_pubblicazione()!=null && 
			!this.getDt_stipula().after(this.getIncarichi_procedura().getDt_fine_pubblicazione()))
			throw new ValidationException( "La \"Data di stipula\" del contratto deve essere superiore alla \"Data di fine pubblicazione\" della \"Procedura di conferimento incarico\".");
		if (this.getDt_stipula()!=null &&  this.getIncarichi_procedura()!=null &&  this.getIncarichi_procedura().getDt_scadenza()!=null && 
			this.getDt_stipula().after(this.getIncarichi_procedura().getDt_scadenza()))
			throw new ValidationException( "La \"Data di stipula\" del contratto deve essere inferiore o uguale alla \"Data di scadenza\" della \"Procedura di conferimento incarico\".");
		if (this.getFl_inviato_corte_conti()) {
			if (this.getDt_stipula()==null && this.getDt_invio_corte_conti()!=null)
			    throw new ValidationException( "Non \350 possibile indicare la \"Data di ricezione Corte dei Conti\" senza indicare la \"Data di stipula\".");
			if (this.getDt_invio_corte_conti()==null && this.getDt_efficacia()!=null) 
			    throw new ValidationException( "Non \350 possibile indicare la \"Data di efficacia\" senza indicare la \"Data di ricezione Corte dei Conti\".");
			if (this.getDt_efficacia()==null && this.getDt_inizio_validita()!=null) 
			    throw new ValidationException( "Non \350 possibile indicare la \"Data di inizio validit\340\" senza indicare la \"Data efficacia\".");
			if (this.getDt_stipula()!=null && this.getDt_invio_corte_conti()!=null && 
				this.getDt_stipula().after(this.getDt_invio_corte_conti())) 
				throw new ValidationException( "La \"Data di ricezione Corte dei Conti\" deve essere uguale o superiore alla \"Data di stipula\".");
			if (this.getDt_invio_corte_conti()!=null && this.getDt_efficacia()!=null && 
				this.getDt_invio_corte_conti().after(this.getDt_efficacia())) 
				throw new ValidationException( "La \"Data di efficacia\" del contratto deve essere uguale o superiore alla \"Data di ricezione Corte dei Conti\".");
			if (this.getDt_efficacia()!=null && this.getDt_inizio_validita()!=null && 
				this.getDt_efficacia().after(this.getDt_inizio_validita())) 
				throw new ValidationException( "La \"Data di inizio validit\340\" del contratto deve essere uguale o superiore alla \"Data di efficacia\".");
		}
		if (this.getDt_stipula()==null && this.getDt_inizio_validita()!=null)
		    throw new ValidationException( "Non \350 possibile indicare la \"Data di inizio validit\340\" senza indicare la \"Data di stipula\".");
		if (this.getDt_inizio_validita()==null && this.getDt_fine_validita()!=null)
		    throw new ValidationException( "Non \350 possibile indicare la \"Data di fine validit\340\" del contratto senza indicare la \"Data di inizio validit\340\".");
		if (this.getDt_fine_validita()==null && this.getDt_proroga()!=null)
		    throw new ValidationException( "Non \350 possibile indicare la \"Data di proroga\" del contratto senza indicare la \"Data di fine validit\340\".");
		if (this.getDt_stipula()!=null && this.getDt_inizio_validita()!=null &&
			this.getDt_stipula().after(this.getDt_inizio_validita()))
			throw new ValidationException( "La \"Data di inizio validit\340\" del contratto deve essere uguale o superiore alla \"Data stipula\".");
		if (this.getDt_inizio_validita()!=null && this.getDt_fine_validita()!=null &&
			this.getDt_inizio_validita().after(this.getDt_fine_validita()))
			throw new ValidationException( "La \"Data di fine validit\340\" del contratto deve essere uguale o superiore alla \"Data di inizio validit\340\".");
		if (this.getDt_fine_validita()!=null && this.getDt_proroga()!=null &&
			this.getDt_fine_validita().after(this.getDt_proroga()))
			throw new ValidationException( "La \"Data di proroga\" del contratto deve essere uguale o superiore alla \"Data di fine validit\340\".");
	}
	public void validate() throws ValidationException {
		super.validate();
		this.validaDateContratto();
	}
	public boolean isDataProrogaEnableOnView() {
		return dataProrogaEnableOnView;
	}
	public void setDataProrogaEnableOnView(boolean dataProrogaEnableOnView) {
		this.dataProrogaEnableOnView = dataProrogaEnableOnView;
	}
	public boolean isUtenteCollegatoUoEnte() {
		return utenteCollegatoUoEnte;
	}
	public void setUtenteCollegatoUoEnte(boolean utenteCollegatoUoEnte) {
		this.utenteCollegatoUoEnte = utenteCollegatoUoEnte;
	}
	public java.math.BigDecimal getImporto_utilizzato() {
		java.math.BigDecimal totale = new java.math.BigDecimal(0);
		for (Iterator i=this.getIncarichi_repertorio_annoColl().iterator();i.hasNext();)
			totale = totale.add( ((Incarichi_repertorio_annoBulk)i.next()).getImporto_utilizzato());
		return totale;
	}
	
	public BulkList getIncarichi_repertorio_varColl() {
		return incarichi_repertorio_varColl;
	}
	public void setIncarichi_repertorio_varColl(BulkList incarichi_repertorio_varColl) {
		this.incarichi_repertorio_varColl = incarichi_repertorio_varColl;
	}
	public int addToIncarichi_repertorio_varColl(Incarichi_repertorio_varBulk dett) {
		dett.setIncarichi_repertorio(this);
		dett.setImporto_lordo(Utility.ZERO);
		dett.setImporto_complessivo(Utility.ZERO);
		getIncarichi_repertorio_varColl().add(dett);
		return getIncarichi_repertorio_varColl().size()-1;
	}	
	public Incarichi_repertorio_varBulk removeFromIncarichi_repertorio_varColl(int index) {
		Incarichi_repertorio_varBulk dett = (Incarichi_repertorio_varBulk)getIncarichi_repertorio_varColl().remove(index);
		return dett;
	}
	public java.math.BigDecimal getImporto_complessivo_variazioni() {
		java.math.BigDecimal totale = BigDecimal.ZERO;
		for (Iterator<Incarichi_repertorio_varBulk> i=this.getIncarichi_repertorio_varColl().iterator();i.hasNext();){
			Incarichi_repertorio_varBulk variazione = i.next();			
			if (variazione.isAllegatoValido())
				totale = totale.add(variazione.getImporto_complessivo());
		}
		return totale;
	}
	public java.math.BigDecimal getImporto_lordo_variazioni() {
		java.math.BigDecimal totale = BigDecimal.ZERO;
		for (Iterator<Incarichi_repertorio_varBulk> i=this.getIncarichi_repertorio_varColl().iterator();i.hasNext();){
			Incarichi_repertorio_varBulk variazione = i.next();			
			if (variazione.isAllegatoValido())
				totale = totale.add(variazione.getImporto_lordo());
		}
		return totale;
	}
	public java.math.BigDecimal getImporto_da_ripartire() {
		return getImporto_complessivo_incarico().subtract(getIm_complessivo_ripartito());
	}
	public java.math.BigDecimal getImporto_complessivo_incarico() {
		return Optional.ofNullable(getImporto_complessivo()).map(map -> map).orElse(BigDecimal.ZERO).
				add(Optional.ofNullable(getImporto_complessivo_variazioni()).map(map -> map).orElse(BigDecimal.ZERO));
	}
	public boolean hasVariazioni() {
		if (getImporto_complessivo_variazioni().compareTo(BigDecimal.ZERO)!=0)
			return true;
		return false;
	}
	public TerzoBulk getTerzoForColumnMap() {
		return terzoForColumnMap;
	}
	public void setTerzoForColumnMap(TerzoBulk terzoForColumnMap) {
		this.terzoForColumnMap = terzoForColumnMap;
	}
	public java.lang.String getDsTerzoForColumnMap() {
		if ( getTerzoForColumnMap() != null && getTerzoForColumnMap().getAnagrafico() != null &&
				getTerzoForColumnMap().getAnagrafico().getCognome()!=null )
			return getTerzoForColumnMap().getAnagrafico().getCognome() + " " + getTerzoForColumnMap().getAnagrafico().getNome();
		return "";
	}
	public boolean isRODataInvioCorteConti() {
		return isROIncarico() || !getFl_inviato_corte_conti();
	}
	public boolean isROFlInvioCorteConti() {
		return getDt_stipula()!=null || getDt_invio_corte_conti()!=null || getEsito_corte_conti()!=null;
	}

	public BulkList getAssociazioneUO() {
		return associazioneUO;
	}
	public void setAssociazioneUO(BulkList list) {
		associazioneUO = list;
	}

	public BulkList getAssociazioneUODisponibili() {
		return associazioneUODisponibili;
	}
	public void setAssociazioneUODisponibili(BulkList list) {
		associazioneUODisponibili = list;
	}

	public int addToAssociazioneUO(Ass_incarico_uoBulk dett) {
		dett.setIncaricoRepertorio(this);
		dett.setEsercizio(getEsercizio());
		dett.setPg_repertorio(getPg_repertorio());
		getAssociazioneUO().add(dett);
		return getAssociazioneUO().size()-1;
	}
	public int addToAssociazioneUODisponibili(Unita_organizzativaBulk dett) {
		getAssociazioneUODisponibili().add(dett);
		return getAssociazioneUODisponibili().size()-1;
	}
	public Ass_incarico_uoBulk removeFromAssociazioneUO(int index) {
		Ass_incarico_uoBulk dett = (Ass_incarico_uoBulk)getAssociazioneUO().remove(index);
		return dett;
	}
	public Unita_organizzativaBulk removeFromAssociazioneUODisponubili(int index) {
		Unita_organizzativaBulk dett = (Unita_organizzativaBulk)getAssociazioneUODisponibili().remove(index);
		return dett;
	}
	public boolean isUoAbilitata(Unita_organizzativaBulk uo){
		if (getUnita_organizzativa().equalsByPrimaryKey(uo)) return true;
		if (getIncarichi_procedura().getUnita_organizzativa().equalsByPrimaryKey(uo)) return true;		
		for (Iterator iterator = getAssociazioneUO().iterator(); iterator.hasNext();) {
			Ass_incarico_uoBulk assUo = (Ass_incarico_uoBulk) iterator.next();
			if (assUo.getUnita_organizzativa().equalsByPrimaryKey(uo))
				return true;
		}
		return false;
	}
	public java.sql.Timestamp getDt_efficacia_orig() {
		return dt_efficacia_orig;
	}
	public void setDt_efficacia_orig(java.sql.Timestamp dt_efficacia_orig) {
		this.dt_efficacia_orig = dt_efficacia_orig;
	}
	public boolean isRODataEfficacia(){
		return getDt_efficacia_orig()!=null?isRODataStipula():false;
	}

	public BulkList getIncarichi_repertorio_rappColl() {
		return incarichi_repertorio_rappColl;
	}
	public void setIncarichi_repertorio_rappColl(BulkList incarichi_repertorio_rappColl) {
		this.incarichi_repertorio_rappColl = incarichi_repertorio_rappColl;
	}
	public int addToIncarichi_repertorio_rappColl(Incarichi_repertorio_rappBulk dett) {
		dett.setIncarichi_repertorio(this);
		if (this.getDt_stipula()!=null) {
			GregorianCalendar data_da = (GregorianCalendar) GregorianCalendar.getInstance();
			data_da.setTime(this.getDt_stipula());
			dett.setAnno_competenza(data_da.get(java.util.Calendar.YEAR));
		}
		dett.setTipo_archivio(Incarichi_archivioBulk.TIPO_GENERICO);
		dett.setStato(Incarichi_archivioBulk.STATO_VALIDO);
		getIncarichi_repertorio_rappColl().add(dett);
		return getIncarichi_repertorio_rappColl().size()-1;
	}	
	public Incarichi_repertorio_rappBulk removeFromIncarichi_repertorio_rappColl(int index) {
		Incarichi_repertorio_rappBulk dett = (Incarichi_repertorio_rappBulk)getIncarichi_repertorio_rappColl().remove(index);
		return dett;
	}

	public BulkList getIncarichi_repertorio_rapp_detColl() {
		return incarichi_repertorio_rapp_detColl;
	}
	public void setIncarichi_repertorio_rapp_detColl(BulkList incarichi_repertorio_rapp_detColl) {
		this.incarichi_repertorio_rapp_detColl = incarichi_repertorio_rapp_detColl;
	}
	public int addToIncarichi_repertorio_rapp_detColl(Incarichi_repertorio_rapp_detBulk dett) {
		dett.setIncarichi_repertorio(this);
		dett.setStato(Incarichi_archivioBulk.STATO_VALIDO);
		getIncarichi_repertorio_rapp_detColl().add(dett);
		return getIncarichi_repertorio_rapp_detColl().size()-1;
	}	
	public Incarichi_repertorio_rapp_detBulk removeFromIncarichi_repertorio_rapp_detColl(int index) {
		Incarichi_repertorio_rapp_detBulk dett = (Incarichi_repertorio_rapp_detBulk)getIncarichi_repertorio_rapp_detColl().remove(index);
		return dett;
	}

	public StorageFolderContrattiModel getCMISFolder() {
		if (Optional.ofNullable(getIncarichi_procedura()).map(Incarichi_proceduraBulk::isProceduraForBorseStudio).orElse(false)) 
			return new StorageFolderBorseStudio(this);
		else if (Optional.ofNullable(getIncarichi_procedura()).map(Incarichi_proceduraBulk::isProceduraForAssegniRicerca).orElse(false)) 
			return new StorageFolderAssegniRicerca(this);
		else
			return Optional.ofNullable(getIncarichi_procedura()).map(map -> new StorageFolderIncarico(this)).orElse(null) ;
	}

}
