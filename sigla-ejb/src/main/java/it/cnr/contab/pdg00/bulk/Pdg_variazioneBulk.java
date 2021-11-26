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
 * Date 25/05/2005
 */
package it.cnr.contab.pdg00.bulk;

import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.pdg00.bp.PdGVariazioneBP;
import it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrBulk;
import it.cnr.contab.pdg01.bulk.Tipo_variazioneBulk;
import it.cnr.contab.preventvar00.bulk.Var_bilancioBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazioneBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.ICancellatoLogicamente;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.math.BigDecimal;
import java.util.Optional;

public class Pdg_variazioneBulk extends Pdg_variazioneBase implements ICancellatoLogicamente {
    //Elenco completo delle Finalità della Variazioni utilizzato dalle mappe in modalità ricerca
    public static final java.util.Dictionary tiMotivazioneVariazioneForSearchKeys = new it.cnr.jada.util.OrderedHashtable();
    final public static String STATO_PROPOSTA_PROVVISORIA = "PRP";
    final public static String STATO_PROPOSTA_DEFINITIVA = "PRD";
    final public static String STATO_APPROVATA = "APP";
    final public static String STATO_APPROVAZIONE_FORMALE = "APF";
    final public static String STATO_RESPINTA = "RES";
    final public static String STATO_ANNULLATA = "ANN";
    final public static String STATO_DA_INVIARE = "DAI";
    final public static String STATO_INVIATA = "INV";
    final public static String MOTIVAZIONE_GENERICO = "GEN";
    final public static String MOTIVAZIONE_BANDO = "BAN";
    final public static String MOTIVAZIONE_PROROGA = "PRG";
    final public static String MOTIVAZIONE_COMPENSI_INCENTIVANTI = "INC";
    final public static String MOTIVAZIONE_TRASFERIMENTO_RAGIONERIA = "RAG";
    final public static String MOTIVAZIONE_TRASFERIMENTO_AREA = "TAE";
    final public static String MOTIVAZIONE_TRASFERIMENTO_AUTORIZZATO = "TAU";
    final public static String MOTIVAZIONE_VARIAZIONE_AUTOMATICA = "SAU";
    final public static String MOTIVAZIONE_ALTRE_SPESE = "ALT";
    final public static String FONDO = "Fondo Perequativo Stabilizzazioni";
    final public static String OVERHEAD = "Overhead/Spese Generali";
    private static final java.util.Dictionary ti_statoKeys = new it.cnr.jada.util.OrderedHashtable();
    private static final java.util.Dictionary ti_tipologia_finKeys = new it.cnr.jada.util.OrderedHashtable();
    private static final java.util.Dictionary stato_invioKeys = new it.cnr.jada.util.OrderedHashtable();
    private static final java.util.Dictionary ds_causaleKeys = new it.cnr.jada.util.OrderedHashtable();

    static {
        ti_statoKeys.put(STATO_PROPOSTA_PROVVISORIA, "Proposta Provvisoria");
        ti_statoKeys.put(STATO_PROPOSTA_DEFINITIVA, "Proposta Definitiva");
        ti_statoKeys.put(STATO_APPROVATA, "Approvata");
        ti_statoKeys.put(STATO_APPROVAZIONE_FORMALE, "Approvazione Formale");
        ti_statoKeys.put(STATO_RESPINTA, "Respinta");
        ti_statoKeys.put(STATO_ANNULLATA, "Annullata");

        ti_tipologia_finKeys.put(NaturaBulk.TIPO_NATURA_FONTI_INTERNE, "Fonti Interne");
        ti_tipologia_finKeys.put(NaturaBulk.TIPO_NATURA_FONTI_ESTERNE, "Fonti Esterne");

        stato_invioKeys.put(STATO_DA_INVIARE, "Da inviare");
        stato_invioKeys.put(STATO_INVIATA, "Inviata");

        tiMotivazioneVariazioneForSearchKeys.put(MOTIVAZIONE_BANDO, "Personale - Bando da pubblicare");
        tiMotivazioneVariazioneForSearchKeys.put(MOTIVAZIONE_PROROGA, "Personale - Proroga");
        tiMotivazioneVariazioneForSearchKeys.put(MOTIVAZIONE_ALTRE_SPESE, "Personale - Altre Spese");
        tiMotivazioneVariazioneForSearchKeys.put(MOTIVAZIONE_COMPENSI_INCENTIVANTI, "Personale - Compensi Incentivanti");
        tiMotivazioneVariazioneForSearchKeys.put(MOTIVAZIONE_TRASFERIMENTO_RAGIONERIA, "Trasferimento a Ragioneria");
        tiMotivazioneVariazioneForSearchKeys.put(MOTIVAZIONE_TRASFERIMENTO_AREA, "Trasferimento ad Aree di Ricerca");
        tiMotivazioneVariazioneForSearchKeys.put(MOTIVAZIONE_TRASFERIMENTO_AUTORIZZATO, "Trasferimento In Deroga");

        ds_causaleKeys.put(FONDO, "Fondo Perequativo Stabilizzazioni");
        ds_causaleKeys.put(OVERHEAD, "Overhead/Spese Generali");
    }

    protected java.util.Collection tipologie_variazione;
    private Long storageMatricola;
    private BulkList associazioneCDR = new BulkList();
    private BulkList archivioConsultazioni = new BulkList();
    private CdrBulk centro_responsabilita;
    private Elemento_voceBulk elemento_voce;
    private Tipo_variazioneBulk tipo_variazione;
    private java.util.Dictionary ti_causale_respintaKeys = new it.cnr.jada.util.OrderedHashtable();
    private ProgettoBulk progettoRimodulatoForSearch;
    private Progetto_rimodulazioneBulk progettoRimodulazione;

    private boolean isBulkforSearch = false;
    private BigDecimal somma_spesa_var_piu;
    private BigDecimal somma_spesa_var_meno;
    private BigDecimal somma_spesa_diff;
    private BigDecimal somma_costi_var_piu;
    private BigDecimal somma_costi_var_meno;
    private BigDecimal somma_costi_diff;
    private BigDecimal somma_entrata_var_piu;
    private BigDecimal somma_entrata_var_meno;
    private BigDecimal somma_entrata_diff;
    private BigDecimal somma_ricavi_var_piu;
    private BigDecimal somma_ricavi_var_meno;
    private BigDecimal somma_ricavi_diff;
    private boolean isCdsAbilitatoAdApprovare = false;
    private boolean erroreEsitaVariazioneBilancio = false;
    private boolean checkDispAssestatoCdrGAEVoceEseguito = false;

    // variabile utilizzata per gestire consentire l'inserimento di valori null in tiMotivazioneVariazione
    // ma rendere allo stesso tempo obbligatorio l'indicazione del campo da parte dell'utente
    private java.lang.String mapMotivazioneVariazione;

    private String cds_var_bil;
    private Integer es_var_bil;
    private Character ti_app_var_bil;
    private Integer pg_var_bil;

    private Var_bilancioBulk var_bilancio;

    private String desTipoVariazione;
    private String ds_cdr;
    private BulkList riepilogoSpese = new BulkList();
    private BulkList riepilogoEntrate = new BulkList();

    public Pdg_variazioneBulk() {
        super();
    }

    public Pdg_variazioneBulk(java.lang.Integer esercizio, java.lang.Long pg_variazione_pdg) {
        super(esercizio, pg_variazione_pdg);
    }

    public boolean isPropostaProvvisoria() {
        return getStato() != null && getStato().equals(STATO_PROPOSTA_PROVVISORIA);
    }

    public boolean isPropostaDefinitiva() {
        return getStato() != null && getStato().equals(STATO_PROPOSTA_DEFINITIVA);
    }

    public boolean isApprovazioneFormale() {
        return getStato() != null && getStato().equals(STATO_APPROVAZIONE_FORMALE);
    }

    public boolean isApprovata() {
        return getStato() != null && getStato().equals(STATO_APPROVATA);
    }

    public boolean isAnnullata() {
        return getStato() != null && getStato().equals(STATO_ANNULLATA);
    }

    public boolean isRespinta() {
        return getStato() != null && getStato().equals(STATO_RESPINTA);
    }

    /**
     * Serve per gestire la disabilitazione dei tasti di consultazione assestato entrate/ricavi/spese/costi
     * Ritorna TRUE se la variazione al PDG non è caricata (numero variazione pdg non assegnato)
     *
     * @return Il valore della proprietà 'consultazioneAssestatoDisabled'
     */
    public boolean isConsultazioneAssestatoDisabled() {
        return getPg_variazione_pdg() == null;
    }

    // metodo per inizializzare l'oggetto bulk
    private void initialize() {
        setFl_visto_dip_variazioni(new Boolean(false));
    }

    /**
     * Insert the method's description here.
     * Creation date: (11/04/2005 12:34:48)
     *
     * @return java.util.Dictionary
     */
    public final java.util.Dictionary getTi_statoKeys() {
        return ti_statoKeys;
    }

    public final java.util.Dictionary getStato_invioKeys() {
        return stato_invioKeys;
    }

    public final java.util.Dictionary getDs_causaleKeys() {
        return ds_causaleKeys;
    }

    /**
     * @return
     */
    public final java.util.Dictionary getTi_causale_respintaKeys() {
        return ti_causale_respintaKeys;
    }

    /**
     * @param dictionary
     */
    public void setTi_causale_respintaKeys(java.util.Dictionary dictionary) {
        ti_causale_respintaKeys = dictionary;
    }

    public final java.util.Dictionary getTiMotivazioneVariazioneKeys() {
        java.util.Dictionary tiMotivazioneVariazioneKeys = new it.cnr.jada.util.OrderedHashtable();
        tiMotivazioneVariazioneKeys.put(MOTIVAZIONE_GENERICO, "Variazione Generica");
        tiMotivazioneVariazioneKeys.put(MOTIVAZIONE_BANDO, "Personale - Bando in corso");
        tiMotivazioneVariazioneKeys.put(MOTIVAZIONE_PROROGA, "Personale - Proroga");
        tiMotivazioneVariazioneKeys.put(MOTIVAZIONE_COMPENSI_INCENTIVANTI, "Personale - Compensi Incentivanti");
        tiMotivazioneVariazioneKeys.put(MOTIVAZIONE_ALTRE_SPESE, "Personale - Altri Trasferimenti");

        tiMotivazioneVariazioneKeys.put(Pdg_variazioneBulk.MOTIVAZIONE_TRASFERIMENTO_RAGIONERIA, "Trasferimento Ragioneria");

        if (Optional.ofNullable(this.getCentro_responsabilita())
                .flatMap(el -> Optional.ofNullable(el.getUnita_padre()))
                .filter(el -> !el.isUoArea() && !el.isUoEnte())
                .isPresent()) {
            if (!Optional.ofNullable(this.isVariazioneInternaIstituto()).orElse(Boolean.FALSE) || this.isMotivazioneTrasferimentoArea()) {
                if (Optional.ofNullable(this.getCentro_responsabilita())
                        .flatMap(el -> Optional.ofNullable(el.getUnita_padre()))
                        .map(Unita_organizzativaBulk::isUoArea)
                        .orElse(Boolean.FALSE))
                    tiMotivazioneVariazioneKeys.put(MOTIVAZIONE_TRASFERIMENTO_AREA, "Trasferimento da Aree di Ricerca");
                else
                    tiMotivazioneVariazioneKeys.put(MOTIVAZIONE_TRASFERIMENTO_AREA, "Trasferimento ad Aree di Ricerca");
            }
        }
        if (Optional.ofNullable(this.getCentro_responsabilita())
                .flatMap(el -> Optional.ofNullable(el.getUnita_padre()))
                .map(Unita_organizzativaBulk::isUoEnte)
                .orElse(Boolean.FALSE) || this.isMotivazioneTrasferimentoAutorizzato())
            tiMotivazioneVariazioneKeys.put(MOTIVAZIONE_TRASFERIMENTO_AUTORIZZATO, "Trasferimento In Deroga");

        return tiMotivazioneVariazioneKeys;
    }

    public final java.util.Dictionary getTiMotivazioneVariazioneForSearchKeys() {
        return tiMotivazioneVariazioneForSearchKeys;
    }

    /**
     * @return
     */
    public BulkList<Ass_pdg_variazione_cdrBulk> getAssociazioneCDR() {
        return associazioneCDR;
    }

    /**
     * @param list
     */
    public void setAssociazioneCDR(BulkList list) {
        associazioneCDR = list;
    }

    /**
     * @return
     */
    public BulkList getArchivioConsultazioni() {
        return archivioConsultazioni;
    }

    /**
     * @param list
     */
    public void setArchivioConsultazioni(BulkList list) {
        archivioConsultazioni = list;
    }

    public int addToAssociazioneCDR(Ass_pdg_variazione_cdrBulk dett) {
        dett.setPdg_variazione(this);
        getAssociazioneCDR().add(dett);
        return getAssociazioneCDR().size() - 1;
    }

    public int addToArchivioConsultazioni(Pdg_variazione_archivioBulk dett) {
        dett.setPdg_variazione(this);
        getArchivioConsultazioni().add(dett);
        return getArchivioConsultazioni().size() - 1;
    }

    public it.cnr.jada.bulk.BulkCollection[] getBulkLists() {
        return new it.cnr.jada.bulk.BulkCollection[]{getAssociazioneCDR(), getArchivioConsultazioni(), getRiepilogoSpese(), getRiepilogoEntrate()};
    }

    public Ass_pdg_variazione_cdrBulk removeFromAssociazioneCDR(int index) {
        Ass_pdg_variazione_cdrBulk dett = (Ass_pdg_variazione_cdrBulk) getAssociazioneCDR().remove(index);
        return dett;
    }

    public Pdg_variazione_archivioBulk removeFromArchivioConsultazioni(int index) {
        Pdg_variazione_archivioBulk dett = (Pdg_variazione_archivioBulk) getArchivioConsultazioni().remove(index);
        return dett;
    }

    /**
     * Inizializza per l'inserimento i flag
     */
    public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) {
        if (Optional.ofNullable(bp).filter(PdGVariazioneBP.class::isInstance).isPresent()) {
            PdGVariazioneBP myBp = (PdGVariazioneBP) bp;
            setCentro_responsabilita(myBp.getCentro_responsabilita_scrivania());
        }
        setEsercizio(CNRUserContext.getEsercizio(context.getUserContext()));
        return super.initializeForInsert(bp, context);
    }

    /**
     * @return
     */
    public CdrBulk getCentro_responsabilita() {
        return centro_responsabilita;
    }

    /**
     * @param bulk
     */
    public void setCentro_responsabilita(CdrBulk bulk) {
        centro_responsabilita = bulk;
    }

    /*
     *  (non-Javadoc)
     * @see it.cnr.contab.pdg00.bulk.Pdg_variazioneBase#getCd_centro_responsabilita()
     */
    public java.lang.String getCd_centro_responsabilita() {
        return getCentro_responsabilita().getCd_centro_responsabilita();
    }

    /*
     *  (non-Javadoc)
     * @see it.cnr.contab.pdg00.bulk.Pdg_variazioneBase#setCd_centro_responsabilita(java.lang.String)
     */
    public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita) {
        getCentro_responsabilita().setCd_centro_responsabilita(cd_centro_responsabilita);
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
        if (getEsercizio() != null)
            setDt_annullamento(DateUtils.dataContabile(EJBCommonServices.getServerDate(), getEsercizio()));
        else
            setDt_annullamento(EJBCommonServices.getServerDate());
        setStato(STATO_ANNULLATA);
    }

    /**
     * @return
     */
    public BigDecimal getSomma_costi_diff() {
        return somma_costi_diff;
    }

    /**
     * @param decimal
     */
    public void setSomma_costi_diff(BigDecimal decimal) {
        somma_costi_diff = decimal;
    }

    /**
     * @return
     */
    public BigDecimal getSomma_costi_var_meno() {
        return somma_costi_var_meno;
    }

    /**
     * @param decimal
     */
    public void setSomma_costi_var_meno(BigDecimal decimal) {
        somma_costi_var_meno = decimal;
    }

    /**
     * @return
     */
    public BigDecimal getSomma_costi_var_piu() {
        return somma_costi_var_piu;
    }

    /**
     * @param decimal
     */
    public void setSomma_costi_var_piu(BigDecimal decimal) {
        somma_costi_var_piu = decimal;
    }

    /**
     * @return
     */
    public BigDecimal getSomma_entrata_diff() {
        return somma_entrata_diff;
    }

    /**
     * @param decimal
     */
    public void setSomma_entrata_diff(BigDecimal decimal) {
        somma_entrata_diff = decimal;
    }

    /**
     * @return
     */
    public BigDecimal getSomma_entrata_var_meno() {
        return somma_entrata_var_meno;
    }

    /**
     * @param decimal
     */
    public void setSomma_entrata_var_meno(BigDecimal decimal) {
        somma_entrata_var_meno = decimal;
    }

    /**
     * @return
     */
    public BigDecimal getSomma_entrata_var_piu() {
        return somma_entrata_var_piu;
    }

    /**
     * @param decimal
     */
    public void setSomma_entrata_var_piu(BigDecimal decimal) {
        somma_entrata_var_piu = decimal;
    }

    /**
     * @return
     */
    public BigDecimal getSomma_ricavi_diff() {
        return somma_ricavi_diff;
    }

    /**
     * @param decimal
     */
    public void setSomma_ricavi_diff(BigDecimal decimal) {
        somma_ricavi_diff = decimal;
    }

    /**
     * @return
     */
    public BigDecimal getSomma_ricavi_var_meno() {
        return somma_ricavi_var_meno;
    }

    /**
     * @param decimal
     */
    public void setSomma_ricavi_var_meno(BigDecimal decimal) {
        somma_ricavi_var_meno = decimal;
    }

    /**
     * @return
     */
    public BigDecimal getSomma_ricavi_var_piu() {
        return somma_ricavi_var_piu;
    }

    /**
     * @param decimal
     */
    public void setSomma_ricavi_var_piu(BigDecimal decimal) {
        somma_ricavi_var_piu = decimal;
    }

    /**
     * @return
     */
    public BigDecimal getSomma_spesa_diff() {
        return somma_spesa_diff;
    }

    /**
     * @param decimal
     */
    public void setSomma_spesa_diff(BigDecimal decimal) {
        somma_spesa_diff = decimal;
    }

    /**
     * @return
     */
    public BigDecimal getSomma_spesa_var_meno() {
        return somma_spesa_var_meno;
    }

    /**
     * @param decimal
     */
    public void setSomma_spesa_var_meno(BigDecimal decimal) {
        somma_spesa_var_meno = decimal;
    }

    /**
     * @return
     */
    public BigDecimal getSomma_spesa_var_piu() {
        return somma_spesa_var_piu;
    }

    /**
     * @param decimal
     */
    public void setSomma_spesa_var_piu(BigDecimal decimal) {
        somma_spesa_var_piu = decimal;
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

    /**
     * Ritorna la descrizione della variazione
     *
     * @return String
     */
    public String getDesTipoVariazione() {
        return desTipoVariazione;
    }

    /**
     * Setta la descrizione della variazione
     *
     * @return String
     */
    public void setDesTipoVariazione(String string) {
        desTipoVariazione = string;
    }

    public final java.util.Dictionary getTi_tipologia_finKeys() {
        return ti_tipologia_finKeys;
    }

    public boolean isROTipologia() {
        return !getAssociazioneCDR().isEmpty();
    }

    public boolean isROFondo_spesa() {
        return !isPropostaProvvisoria();
    }

    /*
     * Serve per sapere se la variazione è di tipo Interna all'Istituto
     * Ritorna un boolean con valore true se la tipologia della variazione è:
     * 		TIPO_STORNO_SPESA_STESSO_ISTITUTO
     * 	    TIPO_STORNO_ENTRATA_STESSO_ISTITUTO
     *		TIPO_VARIAZIONE_POSITIVA_STESSO_ISTITUTO
     *      TIPO_VARIAZIONE_NEGATIVA_STESSO_ISTITUTO
     */
    public boolean isVariazioneInternaIstituto() {
        return getTipo_variazione() != null &&
                getTipo_variazione().isVariazioneInternaIstituto();
    }

    /*
     * Serve per sapere se la variazione è di tipo Storno
     * Ritorna un boolean con valore true se la tipologia della variazione è:
     * 		TIPO_STORNO_SPESA_STESSO_ISTITUTO
     * 	    TIPO_STORNO_ENTRATA_STESSO_ISTITUTO
     * 		TIPO_STORNO_SPESA_ISTITUTI_DIVERSI
     * 	    TIPO_STORNO_ENTRATA_ISTITUTI_DIVERSI
     */
    public boolean isStorno() {
        return getTipo_variazione() != null &&
                getTipo_variazione().isStorno();
    }

    /*
     * Serve per sapere se la variazione consente di effettuare interventi sulle voci di entrata
     * Ritorna un boolean con valore true se la tipologia della variazione è:
     * 	    TIPO_STORNO_ENTRATA_STESSO_ISTITUTO
     * 	    TIPO_STORNO_ENTRATA_ISTITUTI_DIVERSI
     *		TIPO_VARIAZIONE_POSITIVA_STESSO_ISTITUTO
     *		TIPO_VARIAZIONE_NEGATIVA_STESSO_ISTITUTO
     *		TIPO_VARIAZIONE_POSITIVA_ISTITUTI_DIVERSI
     *		TIPO_VARIAZIONE_NEGATIVA_ISTITUTI_DIVERSI
     */
    public boolean isGestioneEntrateEnable() {
        return getTipo_variazione() != null &&
                getTipo_variazione().isTipoVariazioneEntrata();
    }

    /*
     * Serve per sapere se la variazione consente di effettuare interventi sulle voci di spesa
     * Ritorna un boolean con valore true se la tipologia della variazione è:
     * 	    TIPO_STORNO_SPESA_STESSO_ISTITUTO
     * 	    TIPO_STORNO_SPESA_ISTITUTI_DIVERSI
     *		TIPO_VARIAZIONE_POSITIVA_STESSO_ISTITUTO
     *		TIPO_VARIAZIONE_NEGATIVA_STESSO_ISTITUTO
     *		TIPO_VARIAZIONE_POSITIVA_ISTITUTI_DIVERSI
     *		TIPO_VARIAZIONE_NEGATIVA_ISTITUTI_DIVERSI
     *		TIPO_PRELIEVO_FONDI
     */
    public boolean isGestioneSpeseEnable() {
        return getTipo_variazione() != null &&
                getTipo_variazione().isTipoVariazioneSpesa();
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

    public void setErroreEsitaVariazioneBilancio(boolean erroreEsitaVariazioneBilancio) {
        this.erroreEsitaVariazioneBilancio = erroreEsitaVariazioneBilancio;
    }

    public boolean isCheckDispAssestatoCdrGAEVoceEseguito() {
        return checkDispAssestatoCdrGAEVoceEseguito;
    }

    public void setCheckDispAssestatoCdrGAEVoceEseguito(
            boolean checkDispAssestatoCdrGAEVoceEseguito) {
        this.checkDispAssestatoCdrGAEVoceEseguito = checkDispAssestatoCdrGAEVoceEseguito;
    }

    public Var_bilancioBulk getVar_bilancio() {
        return var_bilancio;
    }

    public void setVar_bilancio(Var_bilancioBulk var_bilancio) {
        this.var_bilancio = var_bilancio;
    }

    public Elemento_voceBulk getElemento_voce() {
        return elemento_voce;
    }

    public void setElemento_voce(Elemento_voceBulk elemento_voce) {
        this.elemento_voce = elemento_voce;
    }

    public String getTi_appartenenza() {
        if (getElemento_voce() == null) return null;
        return getElemento_voce().getTi_appartenenza();
    }

    public void setTi_appartenenza(String ti_appartenenza) {
        getElemento_voce().setTi_appartenenza(ti_appartenenza);
    }

    public String getTi_gestione() {
        if (getElemento_voce() == null) return null;
        return getElemento_voce().getTi_gestione();
    }

    public void setTi_gestione(String ti_gestione) {
        getElemento_voce().setTi_gestione(ti_gestione);
    }

    public String getCd_elemento_voce() {
        if (getElemento_voce() == null) return null;
        return getElemento_voce().getCd_elemento_voce();
    }

    public void setCd_elemento_voce(String cd_elemento_voce) {
        getElemento_voce().setCd_elemento_voce(cd_elemento_voce);
    }

    public void validate() throws ValidationException {
        super.validate();
        if (getTipo_variazione() != null) {
            if (getTipo_variazione().isMovimentoSuFondi() &&
                    (getElemento_voce() == null || getElemento_voce().getCd_elemento_voce() == null))
                throw new ValidationException("Occorre valorizzare la voce di tipo 'Fondo' da utilizzare per il prelievo.");
            if (getTipo_variazione().getFl_variazione_trasferimento() && getMapMotivazioneVariazione() == null)
                throw new ValidationException("Occorre indicare la motivazione per cui viene effettuata la variazione.");
        }
        if (this.isMotivazioneVariazioneBandoPersonale() && getIdBando() == null)
            throw new ValidationException("Occorre inserire i dettagli del bando per cui si effettua la variazione.");
        if (this.isMotivazioneVariazioneProrogaPersonale() && getIdMatricola() == null)
            throw new ValidationException("Occorre inserire la matricola del dipendente per cui si effettua la variazione di proroga contratto.");
        if (this.isMotivazioneVariazioneAltreSpesePersonale() && getIdMatricola() == null)
            throw new ValidationException("Occorre inserire la matricola del dipendente per cui si effettua la variazione per altre spese del personale.");
    }

    public Tipo_variazioneBulk getTipo_variazione() {
        return tipo_variazione;
    }

    public void setTipo_variazione(Tipo_variazioneBulk tipo_variazione) {
        this.tipo_variazione = tipo_variazione;
    }

    public String getTipologia() {
        if (getTipo_variazione() == null) return null;
        return getTipo_variazione().getCd_tipo_variazione();
    }

    public void setTipologia(String tipologia) {
        getTipo_variazione().setCd_tipo_variazione(tipologia);
    }

    public java.util.Collection getTipologie_variazione() {
        return tipologie_variazione;
    }

    public void setTipologie_variazione(java.util.Collection tipologie_variazione) {
        this.tipologie_variazione = tipologie_variazione;
    }

    /*
     * Metodo che serve per conservare l'informazione, utile al component,
     * se la mappa è in modalità ricerca
     */
    public boolean isBulkforSearch() {
        return isBulkforSearch;
    }

    public void setBulkforSearch(boolean isBulkforSearch) {
        this.isBulkforSearch = isBulkforSearch;
    }

    public String getDs_cdr() {
        return ds_cdr;
    }

    public void setDs_cdr(String ds_centro_responsabilita) {
        this.ds_cdr = ds_centro_responsabilita;
    }

    public it.cnr.jada.bulk.BulkList getRiepilogoSpese() {
        return riepilogoSpese;
    }

    public void setRiepilogoSpese(it.cnr.jada.bulk.BulkList newSpeseRipartite) {
        riepilogoSpese = newSpeseRipartite;
    }

    public int addToSpeseRipartite(V_pdg_variazione_riepilogoBulk dett) {
        riepilogoSpese.add(dett);
        return riepilogoSpese.size() - 1;
    }

    public V_pdg_variazione_riepilogoBulk removeFromSpeseRipartite(int index) {
        V_pdg_variazione_riepilogoBulk dett = (V_pdg_variazione_riepilogoBulk) riepilogoSpese.remove(index);
        return dett;
    }

    public it.cnr.jada.bulk.BulkList getRiepilogoEntrate() {
        return riepilogoEntrate;
    }

    public void setRiepilogoEntrate(it.cnr.jada.bulk.BulkList newEntrateRipartite) {
        riepilogoEntrate = newEntrateRipartite;
    }

    public int addToEntrateRipartite(V_pdg_variazione_riepilogoBulk dett) {
        riepilogoEntrate.add(dett);
        return riepilogoEntrate.size() - 1;
    }

    public V_pdg_variazione_riepilogoBulk removeFromEntrateRipartite(int index) {
        V_pdg_variazione_riepilogoBulk dett = (V_pdg_variazione_riepilogoBulk) riepilogoEntrate.remove(index);
        return dett;
    }

    public boolean isMotivazioneVariazionePersonale() {
        return this.isMotivazioneVariazioneBandoPersonale() ||
                this.isMotivazioneVariazioneProrogaPersonale() ||
                this.isMotivazioneVariazioneAltreSpesePersonale() ||
                this.isMotivazioneCompensiIncentivanti();
    }

    public boolean isMotivazioneVariazioneBandoPersonale() {
        return MOTIVAZIONE_BANDO.equals(this.getTiMotivazioneVariazione());
    }

    public boolean isMotivazioneVariazioneProrogaPersonale() {
        return MOTIVAZIONE_PROROGA.equals(this.getTiMotivazioneVariazione());
    }

    public boolean isMotivazioneCompensiIncentivanti() {
        return MOTIVAZIONE_COMPENSI_INCENTIVANTI.equals(this.getTiMotivazioneVariazione());
    }

    public boolean isMotivazioneVariazioneAltreSpesePersonale() {
        return MOTIVAZIONE_ALTRE_SPESE.equals(this.getTiMotivazioneVariazione());
    }

    public boolean isMotivazioneTrasferimentoRagioneria() {
        return MOTIVAZIONE_TRASFERIMENTO_RAGIONERIA.equals(this.getTiMotivazioneVariazione());
    }

    public boolean isMotivazioneTrasferimentoArea() {
        return MOTIVAZIONE_TRASFERIMENTO_AREA.equals(this.getTiMotivazioneVariazione());
    }

    public boolean isMotivazioneTrasferimentoAutorizzato() {
        return MOTIVAZIONE_TRASFERIMENTO_AUTORIZZATO.equals(this.getTiMotivazioneVariazione());
    }

    public boolean isMotivazioneGenerico() {
        return this.getTiMotivazioneVariazione() == null;
    }

    public java.lang.String getMapMotivazioneVariazione() {
        return mapMotivazioneVariazione;
    }

    // serve per consentire l'inserimento di valori null in tiMotivazioneVariazione
    // ma rendere allo stesso tempo obbligatorio l'indicazione del campo da parte dell'utente
    public void setMapMotivazioneVariazione(String mapMotivazioneVariazione) {
        this.mapMotivazioneVariazione = mapMotivazioneVariazione;
    }

    public Long getStorageMatricola() {
        return storageMatricola;
    }

    public void setStorageMatricola(Long storageMatricola) {
        this.storageMatricola = storageMatricola;
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
        Optional.ofNullable(this.getProgettoRimodulazione()).ifPresent(el -> {
            el.setPg_progetto(pg_progetto_rimodulazione);
        });
    }

    @Override
    public Integer getPg_rimodulazione() {
        return Optional.ofNullable(this.getProgettoRimodulazione()).map(Progetto_rimodulazioneBulk::getPg_rimodulazione).orElse(null);
    }

    @Override
    public void setPg_rimodulazione(Integer pg_rimodulazione) {
        Optional.ofNullable(this.getProgettoRimodulazione()).ifPresent(el -> {
            el.setPg_rimodulazione(pg_rimodulazione);
        });
    }

    public boolean isVariazioneRimodulazioneProgetto() {
        return Optional.ofNullable(this.getProgettoRimodulazione())
                .flatMap(el -> Optional.ofNullable(el.getPg_rimodulazione()))
                .isPresent();
    }

    public boolean isVariazioneFirmata() {
        return Optional.ofNullable(getDt_firma()).isPresent();
    }
}
