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
* Created by Generator 1.0
* Date 15/02/2006
*/
package it.cnr.contab.varstanz00.bulk;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Optional;

import it.cnr.contab.config00.esercizio.bulk.Esercizio_baseBulk;
import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_modificaBulk;
import it.cnr.contab.pdg00.bp.PdGVariazioneBP;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.preventvar00.bulk.Var_bilancioBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazioneBulk;
import it.cnr.contab.util.ICancellatoLogicamente;
import it.cnr.contab.varstanz00.bp.CRUDVar_stanz_resBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.ejb.EJBCommonServices;
public class Var_stanz_resBulk extends Var_stanz_resBase implements ICancellatoLogicamente{
	private static final java.util.Dictionary ti_statoKeys = new it.cnr.jada.util.OrderedHashtable();
	/*
	 * variabile valorizzata dal BP per segnare l'anno da cui parte il piano economico ed è quindi possibile
	 * creare variazioni di tipo Rimodulazione
	 */
	private Integer annoFromPianoEconomico;

	final public static String STATO_PROPOSTA_PROVVISORIA = "PRP";
	final public static String STATO_PROPOSTA_DEFINITIVA = "PRD";
	final public static String STATO_APPROVATA = "APP";
	final public static String STATO_RESPINTA = "RES";
	final public static String STATO_ANNULLATA = "ANN";

	static {
		ti_statoKeys.put(STATO_PROPOSTA_PROVVISORIA,"Proposta Provvisoria");
		ti_statoKeys.put(STATO_PROPOSTA_DEFINITIVA,"Proposta Definitiva");
		ti_statoKeys.put(STATO_APPROVATA,"Approvata");
		ti_statoKeys.put(STATO_RESPINTA,"Respinta");
		ti_statoKeys.put(STATO_ANNULLATA,"Annullata");
	}

	private static final java.util.Dictionary ti_tipologiaKeys = new it.cnr.jada.util.OrderedHashtable();

	final public static String TIPOLOGIA_ECO = "ECO";
	final public static String TIPOLOGIA_STO_INT = "STO_INT";
	final public static String TIPOLOGIA_STO = "STO";

	static {
		ti_tipologiaKeys.put(TIPOLOGIA_ECO,"Depennati");
		ti_tipologiaKeys.put(TIPOLOGIA_STO_INT,"Storno interno al CDS");
		ti_tipologiaKeys.put(TIPOLOGIA_STO,"Storno tra CdS diversi");
	}
	
	private static final java.util.Dictionary ti_tipologia_finKeys = new it.cnr.jada.util.OrderedHashtable();
	static {
		ti_tipologia_finKeys.put(NaturaBulk.TIPO_NATURA_FONTI_INTERNE,"Fonti Interne");
		ti_tipologia_finKeys.put(NaturaBulk.TIPO_NATURA_FONTI_ESTERNE,"Fonti Esterne");
	}
	private static final java.util.Dictionary ds_causaleKeys = new it.cnr.jada.util.OrderedHashtable();
	final public static String FONDO = "Fondo Perequativo Stabilizzazioni";
	final public static String OVERHEAD = "Overhead/Spese Generali";

	static{
		ds_causaleKeys.put(FONDO,"Fondo Perequativo Stabilizzazioni");
		ds_causaleKeys.put(OVERHEAD,"Overhead/Spese Generali");
	}

	private CdsBulk centroDiSpesa;
	private CdrBulk centroDiResponsabilita;
	private BulkList associazioneCDR = new BulkList();
	private BulkList rigaVariazione = new BulkList();
	private CdrBulk cdr;	
	private Esercizio_baseBulk esercizio_res;
	protected java.util.Collection esercizi_res;
	private BigDecimal totale_da_ripartire;
	private BigDecimal totale_righe_variazione;

	private String cds_var_bil;
	private Integer es_var_bil;
	private Character ti_app_var_bil;
	private Integer pg_var_bil;
	
	private boolean isCdsAbilitatoAdApprovare = false;
	private boolean isEnteAbilitatoAdApprovare = false;
	private boolean erroreEsitaVariazioneBilancio = false;
	
	private Var_bilancioBulk var_bilancio;
	private ProgettoBulk progettoRimodulatoForSearch;
	private Progetto_rimodulazioneBulk progettoRimodulazione;
	
	private Accertamento_modificaBulk accMod;
	private boolean approvazioneControllata = false;
	
	private Long storageMatricola;
	
	// variabile utilizzata per gestire consentire l'inserimento di valori null in tiMotivazioneVariazione
	// ma rendere allo stesso tempo obbligatorio l'indicazione del campo da parte dell'utente
	private java.lang.String mapMotivazioneVariazione;

	public Var_stanz_resBulk() {
		super();
	}
	public Var_stanz_resBulk(java.lang.Integer esercizio, java.lang.Long pg_variazione) {
		super(esercizio, pg_variazione);
	}
	public boolean isPropostaProvvisoria(){
		return getStato()!=null && getStato().equals(STATO_PROPOSTA_PROVVISORIA);
	}
	public boolean isPropostaDefinitiva(){
		return getStato()!=null && getStato().equals(STATO_PROPOSTA_DEFINITIVA);
	}
	public boolean isApprovata(){
		return getStato()!=null && getStato().equals(STATO_APPROVATA);		
	}
	public boolean isAnnullata(){
		return getStato()!=null && getStato().equals(STATO_ANNULLATA);		
	}
	public boolean isRespinta(){
		return getStato()!=null && getStato().equals(STATO_RESPINTA);		
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (11/04/2005 12:34:48)
	 * @return java.util.Dictionary
	 */
	public final java.util.Dictionary getTi_statoKeys() {
		return ti_statoKeys;
	}	
	/**
	 * Insert the method's description here.
	 * Creation date: (11/04/2005 12:34:48)
	 * @return java.util.Dictionary
	 */
	public final java.util.Dictionary getTi_tipologiaKeys() {
		return ti_tipologiaKeys;
	}	
	public final java.util.Dictionary getTi_tipologia_finKeys() {
		return ti_tipologia_finKeys;
	}	
	
	/**
	 * @return
	 */
	public CdsBulk getCentroDiSpesa() {
		return centroDiSpesa;
	}

	/**
	 * @param bulk
	 */
	public void setCentroDiSpesa(CdsBulk bulk) {
		centroDiSpesa = bulk;
	}

	/**
	 * @return
	 */
	public CdrBulk getCentroDiResponsabilita() {
		return centroDiResponsabilita;
	}

	/**
	 * @param bulk
	 */
	public void setCentroDiResponsabilita(CdrBulk bulk) {
		centroDiResponsabilita = bulk;
	}
    /* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Var_stanz_resBase#getCd_cds()
	 */
	public String getCd_cds() {
		if (getCentroDiSpesa() == null)
		  return null;
		return getCentroDiSpesa().getCd_unita_organizzativa();
	}
    /* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Var_stanz_resBase#setCd_cds(java.lang.String)
	 */
	public void setCd_cds(String cd_cds) {
		getCentroDiSpesa().setCd_unita_organizzativa(cd_cds);
	}
    /* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Var_stanz_resBase#getCd_centro_responsabilita()
	 */
	public String getCd_centro_responsabilita() {
		if (getCentroDiResponsabilita() == null)
		  return null;
		return getCentroDiResponsabilita().getCd_centro_responsabilita();
	}
    /* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Var_stanz_resBase#setCd_centro_responsabilita(java.lang.String)
	 */
	public void setCd_centro_responsabilita(String cd_centro_responsabilita) {
		getCentroDiResponsabilita().setCd_centro_responsabilita(cd_centro_responsabilita);
	}
	/**
	 * @return
	 */
	public BulkList getAssociazioneCDR() {
		return associazioneCDR;
	}

	/**
	 * @param list
	 */
	public void setAssociazioneCDR(BulkList list) {
		associazioneCDR = list;
	}
	public int addToAssociazioneCDR(Ass_var_stanz_res_cdrBulk dett) {
		dett.setVar_stanz_res(this);
		getAssociazioneCDR().add(dett);
		return getAssociazioneCDR().size()-1;
	}	
	public it.cnr.jada.bulk.BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] {getAssociazioneCDR(),getRigaVariazione()};
	}
	public Ass_var_stanz_res_cdrBulk removeFromAssociazioneCDR(int index) {
		Ass_var_stanz_res_cdrBulk dett = (Ass_var_stanz_res_cdrBulk)getAssociazioneCDR().remove(index);
		return dett;
	}
	/* (non-Javadoc)
	 * @see it.cnr.contab.util.ICancellatoLogicamente#isCancellatoLogicamente()
	 */
	public boolean isCancellatoLogicamente() {
		return getStato() != null && isAnnullata();
	}
	/* (non-Javadoc)
	 * @see it.cnr.contab.util.ICancellatoLogicamente#cancellaLogicamente()
	 */
	public void cancellaLogicamente() {
		if (getEsercizio()!=null){
			
			java.sql.Timestamp tsOdierno = EJBCommonServices.getServerDate();
			GregorianCalendar tsOdiernoGregorian = (GregorianCalendar) GregorianCalendar.getInstance();
			tsOdiernoGregorian.setTime(tsOdierno);
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");	
//			Integer esercizio_scrivania = CNRUserContext.getEsercizio(userContext);
        	        
	        if(tsOdiernoGregorian.get(GregorianCalendar.YEAR) > getEsercizio())
				try {
					setDt_annullamento(new java.sql.Timestamp(sdf.parse("31/12/"+getEsercizio().intValue()).getTime()));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			else {
				setDt_annullamento(DateUtils.dataContabile(EJBCommonServices.getServerDate(), getEsercizio()));
	        }
			setDt_annullamento(DateUtils.dataContabile(EJBCommonServices.getServerDate(), getEsercizio()));
			}
		else
			setDt_annullamento(EJBCommonServices.getServerDate());
		setStato(STATO_ANNULLATA);
	}
	public int addToRigaVariazione(Var_stanz_res_rigaBulk dett) throws ValidationException {
		if (getEsercizio_res() == null)
		  throw new ValidationException("Valorizzare l'esercizio residuo!");
		dett.setVar_stanz_res(this);
		dett.setEsercizio_res(getEsercizio_res().getEsercizio());
		dett.setCentroTestata(getCdr());
		getRigaVariazione().add(dett);
		return getRigaVariazione().size()-1;
	}	
	public Var_stanz_res_rigaBulk removeFromRigaVariazione(int index) {
		Var_stanz_res_rigaBulk dett = (Var_stanz_res_rigaBulk)getRigaVariazione().remove(index);
		return dett;
	}
	/**
	 * @return
	 */
	public BulkList getRigaVariazione() {
		return rigaVariazione;
	}

	/**
	 * @param list
	 */
	public void setRigaVariazione(BulkList list) {
		rigaVariazione = list;
	}

	/**
	 * @return
	 */
	public CdrBulk getCdr() {
		return cdr;
	}

	/**
	 * @param bulk
	 */
	public void setCdr(CdrBulk bulk) {
		cdr = bulk;
	}
    public boolean isROEsercizio_res(){
    	return !getRigaVariazione().isEmpty();
    }
	public boolean isROTipologia(){
		return !getAssociazioneCDR().isEmpty();
	}    
	/**
	 * @return
	 */
	public java.util.Collection getEsercizi_res() {
		return esercizi_res;
	}

	/**
	 * @param collection
	 */
	public void setEsercizi_res(java.util.Collection collection) {
		esercizi_res = collection;
	}

	/**
	 * @return
	 */
	public Esercizio_baseBulk getEsercizio_res() {
		return esercizio_res;
	}

	/**
	 * @param bulk
	 */
	public void setEsercizio_res(Esercizio_baseBulk bulk) {
		esercizio_res = bulk;
	}

	/**
	 * @return
	 */
	public BigDecimal getTotale_da_ripartire() {
		return totale_da_ripartire;
	}

	/**
	 * @param decimal
	 */
	public void setTotale_da_ripartire(BigDecimal decimal) {
		totale_da_ripartire = decimal;
	}
	/**
	 * @return
	 */
	public boolean isCdsAbilitatoAdApprovare() {
		return isCdsAbilitatoAdApprovare;
	}

	/**
	 * @param b
	 */
	public void setCdsAbilitatoAdApprovare(boolean b) {
		isCdsAbilitatoAdApprovare = b;
	}
    /* (non-Javadoc)
	 * @see it.cnr.jada.bulk.OggettoBulk#validate(it.cnr.jada.bulk.OggettoBulk)
	 */
	public void validate() throws ValidationException {
        if (getTipologia() != null && !getTipologia().equalsIgnoreCase(Var_stanz_resBulk.TIPOLOGIA_ECO) && getTipologia_fin() == null)
            throw new ValidationException("Indicare l'origine delle Fonti!");
        if (getEsercizio_res()== null) 
        	throw new ValidationException("Valorizzare l'esercizio residuo!");
		super.validate();
		
		if (this.isMotivazioneVariazioneBandoPersonale() && getIdBando()==null) 
			throw new ValidationException("Occorre inserire i dettagli del bando per cui si effettua la variazione.");
		if (this.isMotivazioneVariazioneProrogaPersonale() && getIdMatricola()==null) 
			throw new ValidationException("Occorre inserire la matricola del dipendente per cui si effettua la variazione di proroga contratto.");
		if (this.isMotivazioneVariazioneAltreSpesePersonale() && getIdMatricola()==null) 
			throw new ValidationException("Occorre inserire la matricola del dipendente per cui si effettua la variazione per altre spese del personale.");
	}
	
    /* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Var_stanz_resBase#setEsericizio_residuo(java.lang.Integer)
	 */
	public void setEsercizio_residuo(Integer integer) {
		getEsercizio_res().setEsercizio(integer);
	}
    /* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Var_stanz_resBase#getEsercizio_residuo()
	 */
	public Integer getEsercizio_residuo() {
		return getEsercizio_res().getEsercizio();
	}

	/**
	 * @return
	 */
	public BigDecimal getTotale_righe_variazione() {
		return totale_righe_variazione;
	}

	/**
	 * @param decimal
	 */
	public void setTotale_righe_variazione(BigDecimal decimal) {
		totale_righe_variazione = decimal;
	}

	/**
	 * @return
	 */
	public boolean isEnteAbilitatoAdApprovare() {
		return isEnteAbilitatoAdApprovare;
	}

	/**
	 * @param b
	 */
	public void setEnteAbilitatoAdApprovare(boolean b) {
		isEnteAbilitatoAdApprovare = b;
	}
	public String getCds_var_bil() {
		return cds_var_bil;
	}
	
	public void setCds_var_bil(String cds_var_bil) {
		this.cds_var_bil = cds_var_bil;
	}
	
	public Integer getEs_var_bil() {
		return es_var_bil;
	}
	
	public void setEs_var_bil(Integer es_var_bil) {
		this.es_var_bil = es_var_bil;
	}
	
	public Integer getPg_var_bil() {
		return pg_var_bil;
	}
	
	public void setPg_var_bil(Integer pg_var_bil) {
		this.pg_var_bil = pg_var_bil;
	}
	
	public Character getTi_app_var_bil() {
		return ti_app_var_bil;
	}
	
	public void setTi_app_var_bil(Character ti_app_var_bil) {
		this.ti_app_var_bil = ti_app_var_bil;
	}
	public boolean isErroreEsitaVariazioneBilancio() {
		return erroreEsitaVariazioneBilancio;
	}
	
	public void setErroreEsitaVariazioneBilancio(
			boolean erroreEsitaVariazioneBilancio) {
		this.erroreEsitaVariazioneBilancio = erroreEsitaVariazioneBilancio;
	}
	public Var_bilancioBulk getVar_bilancio() {
		return var_bilancio;
	}
	
	public void setVar_bilancio(Var_bilancioBulk var_bilancio) {
		this.var_bilancio = var_bilancio;
	}
	public Accertamento_modificaBulk getAccMod() {
		return accMod;
	}
	public void setAccMod(Accertamento_modificaBulk accMod) {
		this.accMod = accMod;
	}
	public boolean isTemporaneo() {
		if (getPg_variazione() == null)
			return false;
		return getPg_variazione().longValue() < 0;
	}
	public boolean isApprovazioneControllata() {
		return approvazioneControllata;
	}
	public void setApprovazioneControllata(boolean approvazioneControllata) {
		this.approvazioneControllata = approvazioneControllata;
	}
	@Override
	public OggettoBulk initializeForInsert(CRUDBP crudbp, ActionContext actioncontext) {
		setFl_perenzione(Boolean.FALSE);
		return super.initializeForInsert(crudbp, actioncontext);
	}

	public boolean isMotivazioneVariazioneBandoPersonale() {
		return Pdg_variazioneBulk.MOTIVAZIONE_BANDO.equals(this.getTiMotivazioneVariazione());
	}

	public boolean isMotivazioneVariazioneProrogaPersonale() {
		return Pdg_variazioneBulk.MOTIVAZIONE_PROROGA.equals(this.getTiMotivazioneVariazione());
	}

	public boolean isMotivazioneCompensiIncentivanti() {
		return Pdg_variazioneBulk.MOTIVAZIONE_COMPENSI_INCENTIVANTI.equals(this.getTiMotivazioneVariazione());
	}

	public boolean isMotivazioneTrasferimentoRagioneria() {
		return Pdg_variazioneBulk.MOTIVAZIONE_TRASFERIMENTO_RAGIONERIA.equals(this.getTiMotivazioneVariazione());
	}

	public boolean isMotivazioneTrasferimentoFiscale() {
		return Pdg_variazioneBulk.MOTIVAZIONE_TRASFERIMENTO_FISCALE.equals(this.getTiMotivazioneVariazione());
	}

	public boolean isMotivazioneVariazioneAltreSpesePersonale() {
		return Pdg_variazioneBulk.MOTIVAZIONE_ALTRE_SPESE.equals(this.getTiMotivazioneVariazione());
	}

	public boolean isMotivazioneTrasferimentoArea() {
		return Pdg_variazioneBulk.MOTIVAZIONE_TRASFERIMENTO_AREA.equals(this.getTiMotivazioneVariazione());
	}

	public boolean isMotivazioneTrasferimentoAutorizzato() {
		return Pdg_variazioneBulk.MOTIVAZIONE_TRASFERIMENTO_AUTORIZZATO.equals(this.getTiMotivazioneVariazione());
	}

	public boolean isMotivazioneTrasferimentoEsigenzeFinanziarie() {
		return Pdg_variazioneBulk.MOTIVAZIONE_TRASFERIMENTO_ESIGENZE_FINANZIARIE.equals(this.getTiMotivazioneVariazione());
	}

	public boolean isMotivazioneGenerico() {
		return this.getTiMotivazioneVariazione()==null;
	}

	public java.lang.String getMapMotivazioneVariazione() {
		return mapMotivazioneVariazione;
	}

	// serve per consentire l'inserimento di valori null in tiMotivazioneVariazione
	// ma rendere allo stesso tempo obbligatorio l'indicazione del campo da parte dell'utente
	public void setMapMotivazioneVariazione(String mapMotivazioneVariazione) {
		this.mapMotivazioneVariazione = mapMotivazioneVariazione; 
	}
	
	public final java.util.Dictionary getTiMotivazioneVariazioneKeys() {
		java.util.Dictionary tiMotivazioneVariazioneKeys = new it.cnr.jada.util.OrderedHashtable();
		tiMotivazioneVariazioneKeys.put(Pdg_variazioneBulk.MOTIVAZIONE_GENERICO,"Variazione Generica");
		tiMotivazioneVariazioneKeys.put(Pdg_variazioneBulk.MOTIVAZIONE_BANDO,"Personale - Bando in corso");
		tiMotivazioneVariazioneKeys.put(Pdg_variazioneBulk.MOTIVAZIONE_PROROGA,"Personale - Proroga");
		tiMotivazioneVariazioneKeys.put(Pdg_variazioneBulk.MOTIVAZIONE_COMPENSI_INCENTIVANTI,"Personale - Compensi Incentivanti");
		tiMotivazioneVariazioneKeys.put(Pdg_variazioneBulk.MOTIVAZIONE_ALTRE_SPESE,"Personale - Altri Trasferimenti");

		tiMotivazioneVariazioneKeys.put(Pdg_variazioneBulk.MOTIVAZIONE_TRASFERIMENTO_RAGIONERIA, "Trasferimento alla Ragioneria");
		tiMotivazioneVariazioneKeys.put(Pdg_variazioneBulk.MOTIVAZIONE_TRASFERIMENTO_FISCALE, "Trasferimento Fiscale");
		tiMotivazioneVariazioneKeys.put(Pdg_variazioneBulk.MOTIVAZIONE_TRASFERIMENTO_ESIGENZE_FINANZIARIE, "Trasferimento Esigenze Finanziarie");

		if (!Optional.ofNullable(this.isVariazioneInternaIstituto()).orElse(Boolean.FALSE) || this.isMotivazioneTrasferimentoArea())
			tiMotivazioneVariazioneKeys.put(Pdg_variazioneBulk.MOTIVAZIONE_TRASFERIMENTO_AREA,"Trasferimento ad Aree di Ricerca");
		
		if (Optional.ofNullable(this.getCentroDiResponsabilita())
					.flatMap(el->Optional.ofNullable(el.getUnita_padre()))
					.map(Unita_organizzativaBulk::isUoEnte)
					.orElse(Boolean.FALSE) || this.isMotivazioneTrasferimentoAutorizzato())
			tiMotivazioneVariazioneKeys.put(Pdg_variazioneBulk.MOTIVAZIONE_TRASFERIMENTO_AUTORIZZATO,"Trasferimento In Deroga");
		
		return tiMotivazioneVariazioneKeys;
	}

	public final java.util.Dictionary getTiMotivazioneVariazioneForSearchKeys() {
		return Pdg_variazioneBulk.tiMotivazioneVariazioneForSearchKeys;
	}
	
	public Long getStorageMatricola() {
		return storageMatricola;
	}
	
	public void setStorageMatricola(Long storageMatricola) {
		this.storageMatricola = storageMatricola;
	}
	public final java.util.Dictionary getDs_causaleKeys() {
		return ds_causaleKeys;
	}

	public boolean isMotivazioneVariazionePersonale() {
		return this.isMotivazioneVariazioneBandoPersonale()||
				this.isMotivazioneVariazioneProrogaPersonale()||
				this.isMotivazioneVariazioneAltreSpesePersonale()||
				this.isMotivazioneCompensiIncentivanti();
	}
	
    public boolean isVariazioneStorno() {
    	return Var_stanz_resBulk.TIPOLOGIA_STO.equalsIgnoreCase(this.getTipologia())||
    			Var_stanz_resBulk.TIPOLOGIA_STO_INT.equalsIgnoreCase(this.getTipologia());
    }

    public boolean isVariazioneInternaIstituto() {
    	return Var_stanz_resBulk.TIPOLOGIA_STO_INT.equalsIgnoreCase(this.getTipologia());
    }
    
	public ProgettoBulk getProgettoRimodulatoForSearch() {
		return progettoRimodulatoForSearch;
	}
	
	public void setProgettoRimodulatoForSearch(ProgettoBulk progettoRimodulatoForSearch) {
		this.progettoRimodulatoForSearch = progettoRimodulatoForSearch;
	}

	public Progetto_rimodulazioneBulk getProgettoRimodulazione() {
		return progettoRimodulazione;
	}
	
	public void setProgettoRimodulazione(Progetto_rimodulazioneBulk progettoRimodulazione) {
		this.progettoRimodulazione = progettoRimodulazione;
	}
	
	@Override
	public Integer getPg_progetto_rimodulazione() {
		return Optional.ofNullable(this.getProgettoRimodulazione()).map(Progetto_rimodulazioneBulk::getPg_progetto).orElse(null);
	}
	
	@Override
	public void setPg_progetto_rimodulazione(Integer pg_progetto_rimodulazione) {
		Optional.ofNullable(this.getProgettoRimodulazione()).ifPresent(el->{
			el.setPg_progetto(pg_progetto_rimodulazione);	
		});
	}

	@Override
	public Integer getPg_rimodulazione() {
		return Optional.ofNullable(this.getProgettoRimodulazione()).map(Progetto_rimodulazioneBulk::getPg_rimodulazione).orElse(null);
	}
	
	@Override
	public void setPg_rimodulazione(Integer pg_rimodulazione) {
		Optional.ofNullable(this.getProgettoRimodulazione()).ifPresent(el->{
			el.setPg_rimodulazione(pg_rimodulazione);	
		});
	}    
	
	public void setAnnoFromPianoEconomico(Integer annoFromPianoEconomico) {
		this.annoFromPianoEconomico = annoFromPianoEconomico;
	}
	
	public Integer getAnnoFromPianoEconomico() {
		return annoFromPianoEconomico;
	}
	
	public boolean isVariazioneRimodulazioneProgetto() {
		return Optional.ofNullable(this.getProgettoRimodulazione())
					   .flatMap(el->Optional.ofNullable(el.getPg_gen_rimodulazione()))
					   .isPresent();
	}	
}