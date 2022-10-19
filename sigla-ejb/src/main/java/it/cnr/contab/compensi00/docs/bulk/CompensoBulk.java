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

package it.cnr.contab.compensi00.docs.bulk;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.*;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.coepcoan00.core.bulk.IDocumentoCogeBulk;
import it.cnr.contab.coepcoan00.core.bulk.Scrittura_partita_doppiaBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Tipo_prestazione_compensoBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Tipologia_rischioBulk;
import it.cnr.contab.config00.bulk.CigBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi;
import it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_annoBulk;
import it.cnr.contab.missioni00.docs.bulk.MissioneBulk;
import it.cnr.contab.util.Utility;
import it.cnr.contab.util.enumeration.TipoIVA;
import it.cnr.contab.util00.bulk.storage.AllegatoStorePath;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.si.spring.storage.annotation.StorageType;
import it.siopeplus.StMotivoEsclusioneCigSiope;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@StorageType(name = "D:emppay:compenso", parentName = "D:emppay:document")
@JsonInclude(value = Include.NON_NULL)
public class CompensoBulk extends CompensoBase implements IDefferUpdateSaldi, IDocumentoAmministrativoSpesaBulk, AllegatoStorePath {

    // Stato COAN
    public final static java.lang.String CONTABILIZZATO_COAN = "C";
    public final static java.lang.String NON_CONTABILIZZATO_COAN = "N";
    public final static java.lang.String DA_RICONTABILIZZARE_COAN = "R";
    // Stato COGE
    public final static java.lang.String CONTABILIZZATO_COGE = "C";
    public final static java.lang.String NON_CONTABILIZZATO_COGE = "N";
    public final static java.lang.String DA_RICONTABILIZZARE_COGE = "R";
    // Stato FONDO_ECONOMALE
    public final static java.lang.String ASSEGNATO_FONDO_ECO = "A";
    public final static java.lang.String REGISTRATO_FONDO_ECO = "R";
    public final static java.lang.String LIBERO_FONDO_ECO = "N";
    // Stato MANREV
    public final static java.lang.String NON_ASSOCIATO_MANREV = "N";
    public final static java.lang.String ASSOCIATO_MANREV = "T";
    // Stato COFI
    public final static java.lang.String STATO_INIZIALE = "I";
    public final static java.lang.String STATO_CONTABILIZZATO = "C";
    public final static java.lang.String STATO_PAGATO = "P";
    public final static java.lang.String STATO_ANNULLATO = "A";
    public final static java.util.Dictionary STATO_FONDO_ECO;
    public final static java.util.Dictionary TI_ASSOCIATO_MANREV;
    public final static java.util.Dictionary STATO_COFI;
    public final static Dictionary STATI_RIPORTO;
    // Tipo compenso
    public final static java.util.Dictionary TIPI_COMPENSO;
    public final static Dictionary STATO_LIQUIDAZIONE;
    public final static Dictionary CAUSALE;
    public final static Map<String, String> motivoEsclusioneCigSIOPEKeys = Arrays.asList(StMotivoEsclusioneCigSiope.values())
            .stream()
            .collect(Collectors.toMap(
                    StMotivoEsclusioneCigSiope::name,
                    StMotivoEsclusioneCigSiope::value,
                    (oldValue, newValue) -> oldValue,
                    Hashtable::new
            ));
    // Stato compenso - mi serve per gestire i bottoni di Esegui Calcolo,
    // Crea/Modifica obbligazione
    public final static int STATO_COMPENSO_NORMALE = 0;
    public final static int STATO_COMPENSO_ESEGUI_CALCOLO = 1;
    public final static int STATO_COMPENSO_SINCRONIZZA_OBBLIGAZIONE = 2;
    public final static int STATO_COMPENSO_OBBLIGAZIONE_SINCRONIZZATA = 3;
    public final static int STATO_COMPENSO_CONTABILIZZA_COFI = 4;
    // necessaria per i bulk
    // non persistenti
    public final static java.lang.String CODICE_IRAP = "IRAP";
    public final static java.lang.String CODICE_INAIL = "INAIL";
    public final static java.lang.String CODICE_IVA = "IVA";
    public final static int CANCELLAZIONE_FISICA = 2;
    public final static int CANCELLAZIONE_LOGICA = 1;

    static {
        STATO_FONDO_ECO = new it.cnr.jada.util.OrderedHashtable();
        STATO_FONDO_ECO.put(LIBERO_FONDO_ECO, "Non usare fondo economale");
        STATO_FONDO_ECO.put(ASSEGNATO_FONDO_ECO, "Usa fondo economale");
        STATO_FONDO_ECO.put(REGISTRATO_FONDO_ECO, "Registrato in fondo economale");

        TI_ASSOCIATO_MANREV = new it.cnr.jada.util.OrderedHashtable();
        TI_ASSOCIATO_MANREV.put(ASSOCIATO_MANREV, "Man/rev associato");
        TI_ASSOCIATO_MANREV.put(NON_ASSOCIATO_MANREV, "Man/rev non associato");

        STATO_COFI = new it.cnr.jada.util.OrderedHashtable();
        STATO_COFI.put(STATO_INIZIALE, "Iniziale");
        STATO_COFI.put(STATO_CONTABILIZZATO, "Contabilizzato");
        STATO_COFI.put(STATO_PAGATO, "Pagato");
        STATO_COFI.put(STATO_ANNULLATO, "Annullato");

        STATI_RIPORTO = new it.cnr.jada.util.OrderedHashtable();
        STATI_RIPORTO.put(NON_RIPORTATO, "Non riportata");
        STATI_RIPORTO.put(PARZIALMENTE_RIPORTATO, "Parzialmente riportata");
        STATI_RIPORTO.put(COMPLETAMENTE_RIPORTATO, "Completamente riportata");

        TIPI_COMPENSO = new it.cnr.jada.util.OrderedHashtable();
        for (TipoIVA tipoIVA : TipoIVA.values()) {
            TIPI_COMPENSO.put(tipoIVA.value(), tipoIVA.label());
        }

        /*
         * TIPI_PRESTAZIONE = new it.cnr.jada.util.OrderedHashtable();
         * TIPI_PRESTAZIONE .put(TIPO_PRESTAZIONE_SERVIZI,
         * "Prestazione di Servizi");
         * TIPI_PRESTAZIONE.put(TIPO_PRESTAZIONE_COLLABORAZIONE_IND,
         * "Incarico di collaborazione individuale");
         */
        STATO_LIQUIDAZIONE = new it.cnr.jada.util.OrderedHashtable();
        STATO_LIQUIDAZIONE.put(LIQ, "Liquidabile");
        STATO_LIQUIDAZIONE.put(NOLIQ, "Non Liquidabile");
        STATO_LIQUIDAZIONE.put(SOSP, "Liquidazione sospesa");

        CAUSALE = new it.cnr.jada.util.OrderedHashtable();
        CAUSALE.put(ATTLIQ, "In attesa di liquidazione");
        CAUSALE.put(CONT, "Contenzioso");
    }

    private BancaBulk banca;
    private Rif_termini_pagamentoBulk terminiPagamento;
    private Rif_modalita_pagamentoBulk modalitaPagamento;
    private java.util.Collection modalita;
    private java.util.Collection termini;
    private Tipo_rapportoBulk tipoRapporto;
    private Tipo_trattamentoBulk tipoTrattamento = new Tipo_trattamentoBulk();
    private V_terzo_per_compensoBulk v_terzo = new V_terzo_per_compensoBulk();
    private java.util.Collection tipiRapporto;
    private java.util.Collection tipiTrattamento;
    private Codici_rapporti_inpsBulk codici_rapporti_inps;
    private boolean visualizzaCodici_rapporti_inps = false;
    private Codici_attivita_inpsBulk codici_attivita_inps;
    private boolean visualizzaCodici_attivita_inps = false;
    private Codici_altra_forma_ass_inpsBulk codici_altra_forma_ass_inps;
    private boolean visualizzaCodici_altra_forma_ass_inps = false;
    private java.util.Collection<Contributo_ritenutaBulk> contributi;
    private it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obbligazioneScadenzario;
    // Unità Organizzativa
    private Unita_organizzativaBulk unitaOrganizzativa;
    private ComuneBulk comune_inps;
    private Incarichi_repertorio_annoBulk incarichi_repertorio_anno;

    // Tipo prestazione
    /*
     * public final static String TIPO_PRESTAZIONE_SERVIZI = "C"; public final
     * static String TIPO_PRESTAZIONE_COLLABORAZIONE_IND = "I"; public final
     * static Dictionary TIPI_PRESTAZIONE;
     */
    private java.lang.String incarichi_oggetto;
    private java.math.BigDecimal importo_iniziale;
    private java.math.BigDecimal importo_complessivo;
    private java.math.BigDecimal importo_utilizzato;
    private ContrattoBulk contratto;
    private java.lang.String oggetto_contratto;
    private PrimaryKeyHashMap deferredSaldi = new PrimaryKeyHashMap();
    private PrimaryKeyHashMap relationsDocContForSaldi = null;
    private TrovatoBulk trovato = new TrovatoBulk(); // inizializzazione
    private java.sql.Timestamp dataInizioFatturaElettronica;
    private it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk voceIvaFattura;
    private Fattura_passivaBulk fatturaPassiva;
    private int annoSolare;
    private int esercizioScrivania;
    private int statoCompenso = STATO_COMPENSO_NORMALE;
    private java.lang.Long pgCompensoPerClone;
    private it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk voceIva;
    private it.cnr.contab.anagraf00.tabter.bulk.RegioneBulk regioneIrap;
    private it.cnr.contab.compensi00.tabrif.bulk.Tipologia_rischioBulk tipologiaRischio;
    private boolean visualizzaVoceIva = false;
    private boolean visualizzaRegioneIrap = false;
    private boolean trattamentoSoloEnte = false;
    private boolean visualizzaTipologiaRischio = false;
    private boolean isCompensoSoloInailEnte = false;
    private it.cnr.contab.missioni00.docs.bulk.MissioneBulk missione;
    private MinicarrieraBulk minicarriera;
    private it.cnr.contab.config00.latt.bulk.WorkpackageBulk lineaAttivita;
    private ConguaglioBulk conguaglio;
    private java.util.Vector documentiContabiliCancellati = new java.util.Vector();
    private java.util.Vector dettagliCancellati = new java.util.Vector();
    private java.util.List docContAssociati;
    private java.util.List mandatiRigaAssociati;
    private V_doc_cont_compBulk docContPrincipale;
    private java.lang.Boolean aperturaDaMinicarriera = java.lang.Boolean.FALSE;
    private java.lang.String riportata = NON_RIPORTATO;
    private java.lang.String riportataInScrivania = NON_RIPORTATO;
    private java.lang.Boolean roQuota_esente_inps = java.lang.Boolean.FALSE;
    private BonusBulk bonus;
    private it.cnr.contab.anagraf00.core.bulk.TerzoBulk pignorato = new it.cnr.contab.anagraf00.core.bulk.TerzoBulk();
    private boolean visualizzaPignorato = false;
    private Tipo_prestazione_compensoBulk tipoPrestazioneCompenso;
    private java.util.Collection tipiPrestazioneCompenso;
    private java.sql.Timestamp dataInizioObbligoRegistroUnico;
    private boolean userAbilitatoSenzaCalcolo = false;
    private CigBulk cig;
    private Scrittura_partita_doppiaBulk scrittura_partita_doppia;

    public CompensoBulk() {
        super();
    }

    public CompensoBulk(java.lang.String cd_cds, java.lang.String cd_unita_organizzativa, java.lang.Integer esercizio,
                        java.lang.Long pg_compenso) {
        super(cd_cds, cd_unita_organizzativa, esercizio, pg_compenso);
    }

    public static java.sql.Timestamp decrementaData(java.sql.Timestamp data) {

        java.util.GregorianCalendar gc = (java.util.GregorianCalendar) java.util.GregorianCalendar.getInstance();
        gc.setTime(data);
        gc.add(java.util.Calendar.DATE, -1);
        return new java.sql.Timestamp(gc.getTime().getTime());
    }

    public static java.sql.Timestamp getDataOdierna() throws it.cnr.jada.action.BusinessProcessException {
        try {
            return getDataOdierna(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
        } catch (javax.ejb.EJBException e) {
            throw new it.cnr.jada.action.BusinessProcessException(e);
        }
    }

    public static java.sql.Timestamp getDataOdierna(java.sql.Timestamp dataOdierna) {

        java.util.Calendar gc = java.util.Calendar.getInstance();
        gc.setTime(dataOdierna);
        gc.set(java.util.Calendar.HOUR, 0);
        gc.set(java.util.Calendar.MINUTE, 0);
        gc.set(java.util.Calendar.SECOND, 0);
        gc.set(java.util.Calendar.MILLISECOND, 0);
        gc.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);
        return new java.sql.Timestamp(gc.getTime().getTime());
    }

    public static java.sql.Timestamp incrementaData(java.sql.Timestamp data) {

        java.util.GregorianCalendar gc = (java.util.GregorianCalendar) java.util.GregorianCalendar.getInstance();
        gc.setTime(data);
        gc.add(java.util.Calendar.DATE, 1);
        return new java.sql.Timestamp(gc.getTime().getTime());
    }

    public boolean hasCompetenzaCOGEInAnnoPrecedente() {

        return getDateCalendar(getDt_a_competenza_coge()).get(Calendar.YEAR) == getEsercizio().intValue() - 1;
    }

    /**
     * Insert the method's description here. Creation date: (24/05/2002
     * 13.01.57)
     *
     * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
     * @param values  java.util.Map
     */
    public void addToDefferredSaldi(IDocumentoContabileBulk docCont, java.util.Map values) {

        if (docCont != null) {
            if (deferredSaldi == null)
                deferredSaldi = new PrimaryKeyHashMap();
            if (!deferredSaldi.containsKey(docCont))
                deferredSaldi.put(docCont, values);
            else {
                java.util.Map firstValues = (java.util.Map) deferredSaldi.get(docCont);
                deferredSaldi.remove(docCont);
                deferredSaldi.put(docCont, firstValues);
            }
        }
    }

    /**
     * per interfaccia IDocumentoAmministrativoBulk
     */
    public void addToDettagliCancellati(it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk dettaglio) {

        if (dettaglio != null && ((OggettoBulk) dettaglio).getCrudStatus() == OggettoBulk.NORMAL) {
            getDettagliCancellati().addElement(dettaglio);
            addToDocumentiContabiliCancellati(dettaglio.getScadenzaDocumentoContabile());
        }
    }

    /**
     * per interfaccia IDocumentoAmministrativoSpesaBulk
     */
    public void addToDocumentiContabiliCancellati(
            it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk scadenza) {

        if (getDocumentiContabiliCancellati() == null)
            setDocumentiContabiliCancellati(new java.util.Vector());

        // documentiContabiliCancellati contiene le scadenze non piu' agganciate
        // al compenso
        // che pero' devo essere inserite op aggiornate in tabella
        if ((scadenza != null) && (((OggettoBulk) scadenza).getCrudStatus() == OggettoBulk.NORMAL)
                && (!BulkCollections.containsByPrimaryKey(getDocumentiContabiliCancellati(), (OggettoBulk) scadenza))) {
            // scadenza.setIm_associato_doc_amm(new java.math.BigDecimal(0));
            getDocumentiContabiliCancellati().addElement(scadenza);
        }
    }

    /**
     * Insert the method's description here. Creation date: (5/15/2002 10:50:29
     * AM)
     *
     * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
     */
    public void addToRelationsDocContForSaldi(it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk docCont,
                                              Long progTemporaneo) {

        if (docCont != null) {
            if (relationsDocContForSaldi == null)
                relationsDocContForSaldi = new PrimaryKeyHashMap();
            if (!relationsDocContForSaldi.containsKey(docCont))
                relationsDocContForSaldi.put(docCont, progTemporaneo);
        }
    }

    public void azzeraTipoTrattamento() {

        setTipiTrattamento(null);
        setTipoTrattamento(null);
        setTipoPrestazioneCompenso(null);
        resetDatiLiquidazione();
    }

    /**
     * Insert the method's description here. Creation date: (27/05/2002
     * 12.54.21)
     *
     * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
     */
    public it.cnr.contab.docamm00.docs.bulk.AccertamentiTable getAccertamentiHash() {
        return null;
    }

    /**
     * Insert the method's description here. Creation date: (14/07/2003
     * 11.14.16)
     *
     * @return int
     */
    public int getAnnoSolare() {
        return annoSolare;
    }

    /**
     * Insert the method's description here. Creation date: (14/07/2003
     * 11.14.16)
     *
     * @param newAnnoSolare int
     */
    public void setAnnoSolare(int newAnnoSolare) {
        annoSolare = newAnnoSolare;
    }

    /**
     * Insert the method's description here. Creation date: (12/12/2002
     * 15.47.14)
     *
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getAperturaDaMinicarriera() {
        return aperturaDaMinicarriera;
    }

    /**
     * Insert the method's description here. Creation date: (12/12/2002
     * 15.47.14)
     *
     * @param newAperturaDaMinicarriera java.lang.Boolean
     */
    public void setAperturaDaMinicarriera(java.lang.Boolean newAperturaDaMinicarriera) {
        aperturaDaMinicarriera = newAperturaDaMinicarriera;
    }

    /**
     * Insert the method's description here. Creation date: (19/02/2002
     * 14.22.21)
     *
     * @return it.cnr.contab.anagraf00.core.bulk.BancaBulk
     */
    public BancaBulk getBanca() {
        return banca;
    }

    /**
     * Insert the method's description here. Creation date: (19/02/2002
     * 14.22.21)
     *
     * @param newBanca it.cnr.contab.anagraf00.core.bulk.BancaBulk
     */
    public void setBanca(BancaBulk newBanca) {
        banca = newBanca;
    }

    /**
     * Insert the method's description here. Creation date: (19/02/2002
     * 14.22.21)
     *
     * @return it.cnr.contab.anagraf00.core.bulk.BancaBulk
     */
    public BulkCollection[] getBulkLists() {
        return new it.cnr.jada.bulk.BulkCollection[]{new BulkList(contributi)};
    }

    public java.lang.String getCd_cdr_genrc() {
        it.cnr.contab.config00.latt.bulk.WorkpackageBulk lineaAttivita = this.getLineaAttivita();
        if (lineaAttivita == null)
            return null;
        it.cnr.contab.config00.sto.bulk.CdrBulk centro_responsabilita = lineaAttivita.getCentro_responsabilita();
        if (centro_responsabilita == null)
            return null;
        return centro_responsabilita.getCd_centro_responsabilita();
    }

    public void setCd_cdr_genrc(java.lang.String cd_cdr_genrc) {
        this.getLineaAttivita().getCentro_responsabilita().setCd_centro_responsabilita(cd_cdr_genrc);
    }

    public java.lang.String getCd_cds_missione() {
        it.cnr.contab.missioni00.docs.bulk.MissioneBulk missione = this.getMissione();
        if (missione == null)
            return null;
        return missione.getCd_cds();
    }

    public void setCd_cds_missione(java.lang.String cd_cds_missione) {
        this.getMissione().setCd_cds(cd_cds_missione);
    }

    public java.lang.String getCd_cds_obbligazione() {
        it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obbligazioneScadenzario = this
                .getObbligazioneScadenzario();
        if (obbligazioneScadenzario == null)
            return null;
        it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = obbligazioneScadenzario.getObbligazione();
        if (obbligazione == null)
            return null;
        it.cnr.contab.config00.sto.bulk.CdsBulk cds = obbligazione.getCds();
        if (cds == null)
            return null;
        return cds.getCd_unita_organizzativa();
    }

    public void setCd_cds_obbligazione(java.lang.String cd_cds_obbligazione) {
        this.getObbligazioneScadenzario().getObbligazione().getCds().setCd_unita_organizzativa(cd_cds_obbligazione);
    }

    public java.lang.String getCd_linea_attivita_genrc() {
        it.cnr.contab.config00.latt.bulk.WorkpackageBulk lineaAttivita = this.getLineaAttivita();
        if (lineaAttivita == null)
            return null;
        return lineaAttivita.getCd_linea_attivita();
    }

    public void setCd_linea_attivita_genrc(java.lang.String cd_linea_attivita_genrc) {
        this.getLineaAttivita().setCd_linea_attivita(cd_linea_attivita_genrc);
    }

    public java.lang.String getCd_modalita_pag() {
        it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk modalitaPagamento = this.getModalitaPagamento();
        if (modalitaPagamento == null)
            return null;
        return modalitaPagamento.getCd_modalita_pag();
    }

    public void setCd_modalita_pag(java.lang.String cd_modalita_pag) {
        this.getModalitaPagamento().setCd_modalita_pag(cd_modalita_pag);
    }

    public java.lang.String getCd_regione_irap() {
        it.cnr.contab.anagraf00.tabter.bulk.RegioneBulk regioneIrap = this.getRegioneIrap();
        if (regioneIrap == null)
            return null;
        return regioneIrap.getCd_regione();
    }

    public void setCd_regione_irap(java.lang.String cd_regione_irap) {
        this.getRegioneIrap().setCd_regione(cd_regione_irap);
    }

    public java.lang.String getCd_termini_pag() {
        it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk terminiPagamento = this.getTerminiPagamento();
        if (terminiPagamento == null)
            return null;
        return terminiPagamento.getCd_termini_pag();
    }

    public void setCd_termini_pag(java.lang.String cd_termini_pag) {
        this.getTerminiPagamento().setCd_termini_pag(cd_termini_pag);
    }

    /**
     * Insert the method's description here. Creation date: (27/05/2002
     * 12.54.22)
     *
     * @return java.lang.Integer
     */
    public java.lang.String getCd_tipo_doc_amm() {

        return it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk.TIPO_COMPENSO;
    }

    /**
     * Insert the method's description here. Creation date: (27/05/2002
     * 12.54.22)
     *
     * @return java.lang.Integer
     */
    public void setCd_tipo_doc_amm(java.lang.String newCd_tipo_doc_amm) {
    }

    public java.lang.String getCd_tipo_rapporto() {
        it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk tipoRapporto = this.getTipoRapporto();
        if (tipoRapporto == null)
            return null;
        return tipoRapporto.getCd_tipo_rapporto();
    }

    public void setCd_tipo_rapporto(java.lang.String cd_tipo_rapporto) {
        this.getTipoRapporto().setCd_tipo_rapporto(cd_tipo_rapporto);
    }

    public java.lang.String getCd_trattamento() {
        it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk tipoTrattamento = this.getTipoTrattamento();
        if (tipoTrattamento == null)
            return null;
        return tipoTrattamento.getCd_trattamento();
    }

    public void setCd_trattamento(java.lang.String cd_trattamento) {
        this.getTipoTrattamento().setCd_trattamento(cd_trattamento);
    }

    /**
     * Insert the method's description here. Creation date: (27/05/2002
     * 12.54.21)
     *
     * @return java.lang.Integer
     */
    public java.lang.String getCd_uo() {

        return getCd_unita_organizzativa();
    }

    /**
     * Insert the method's description here. Creation date: (27/05/2002
     * 12.54.22)
     *
     * @return java.lang.Integer
     */
    public void setCd_uo(java.lang.String newCd_uo) {

        setCd_unita_organizzativa(newCd_uo);
    }

    public java.lang.String getCd_uo_missione() {
        it.cnr.contab.missioni00.docs.bulk.MissioneBulk missione = this.getMissione();
        if (missione == null)
            return null;
        return missione.getCd_unita_organizzativa();
    }

    public void setCd_uo_missione(java.lang.String cd_uo_missione) {
        this.getMissione().setCd_unita_organizzativa(cd_uo_missione);
    }

    public java.lang.String getCd_voce_iva() {
        it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk voceIva = this.getVoceIva();
        if (voceIva == null)
            return null;
        return voceIva.getCd_voce_iva();
    }

    public void setCd_voce_iva(java.lang.String cd_voce_iva) {
        this.getVoceIva().setCd_voce_iva(cd_voce_iva);
    }

    /**
     * Insert the method's description here. Creation date: (27/05/2002
     * 12.54.21)
     *
     * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
     */
    public java.lang.Class getChildClass() {
        return Contributo_ritenutaBulk.class;
    }

    /**
     * Insert the method's description here. Creation date: (27/05/2002
     * 12.54.21)
     *
     * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
     */
    public java.util.List<Contributo_ritenutaBulk> getChildren() {
        return (java.util.List<Contributo_ritenutaBulk>) contributi;
    }

    /**
     * Insert the method's description here. Creation date: (12/07/2002
     * 11.45.09)
     *
     * @return it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk
     */
    public ConguaglioBulk getConguaglio() {
        return conguaglio;
    }

    /**
     * Insert the method's description here. Creation date: (12/07/2002
     * 11.45.09)
     *
     * @param newConguaglio it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk
     */
    public void setConguaglio(ConguaglioBulk newConguaglio) {
        conguaglio = newConguaglio;
    }

    /**
     * Insert the method's description here. Creation date: (04/03/2002
     * 15.05.00)
     *
     * @return java.util.Collection
     */
    public java.util.Collection<Contributo_ritenutaBulk> getContributi() {
        return contributi;
    }

    /**
     * Insert the method's description here. Creation date: (04/03/2002
     * 15.05.00)
     *
     * @param newContributi java.util.Collection
     */
    public void setContributi(java.util.Collection<Contributo_ritenutaBulk> newContributi) {
        contributi = newContributi;
    }

    public java.util.Calendar getDateCalendar(java.sql.Timestamp date) {

        if (date == null)
            date = new java.sql.Timestamp(System.currentTimeMillis());

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(java.util.Calendar.HOUR, 0);
        calendar.set(java.util.Calendar.MINUTE, 0);
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MILLISECOND, 0);
        calendar.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);
        return calendar;
    }

    /**
     * Insert the method's description here. Creation date: (24/05/2002
     * 13.10.04)
     *
     * @return it.cnr.jada.bulk.PrimaryKeyHashMap
     */
    public PrimaryKeyHashMap getDefferredSaldi() {

        return deferredSaldi;
    }

    /**
     * getImportoSignForDelete method comment.
     */
    public void setDefferredSaldi(PrimaryKeyHashMap newDefferredSaldi) {
        deferredSaldi = newDefferredSaldi;
    }

    /**
     * Insert the method's description here. Creation date: (24/05/2002
     * 13.08.43)
     *
     * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
     * @return it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
     */
    public IDocumentoContabileBulk getDefferredSaldoFor(IDocumentoContabileBulk docCont) {

        if (docCont != null && deferredSaldi != null)
            for (java.util.Iterator i = deferredSaldi.keySet().iterator(); i.hasNext(); ) {
                IDocumentoContabileBulk key = (IDocumentoContabileBulk) i.next();
                if (((OggettoBulk) docCont).equalsByPrimaryKey(key))
                    return key;
            }
        return null;
    }

    /**
     * Insert the method's description here. Creation date: (27/05/2002
     * 12.54.22)
     *
     * @return java.lang.String
     */
    public java.lang.String getDescrizione_spesa() {
        return getDs_compenso();
    }

    /**
     * Insert the method's description here. Creation date: (27/08/2002
     * 17.27.47)
     *
     * @return java.util.Vector
     */
    public java.util.Vector getDettagliCancellati() {
        return dettagliCancellati;
    }

    /**
     * Insert the method's description here. Creation date: (27/08/2002
     * 17.27.47)
     *
     * @param newDettagliCancellati java.util.Vector
     */
    public void setDettagliCancellati(java.util.Vector newDettagliCancellati) {
        dettagliCancellati = newDettagliCancellati;
    }

    /**
     * Insert the method's description here. Creation date: (10/10/2002
     * 10.46.05)
     *
     * @return java.util.List
     */
    public java.util.List getDocContAssociati() {
        return docContAssociati;
    }

    /**
     * Insert the method's description here. Creation date: (10/10/2002
     * 10.46.05)
     *
     * @param newDocContAssociati java.util.List
     */
    public void setDocContAssociati(java.util.List newDocContAssociati) {

        if (newDocContAssociati != null) {

            docContAssociati = new java.util.LinkedList();
            for (java.util.Iterator i = newDocContAssociati.iterator(); i.hasNext(); ) {
                V_doc_cont_compBulk docCont = (V_doc_cont_compBulk) i.next();
                if (docCont.isDocumentoPrincipale())
                    setDocContPrincipale(docCont);
                else
                    docContAssociati.add(docCont);
            }
        }
    }

    /**
     * Insert the method's description here. Creation date: (23/10/2002
     * 13.00.41)
     *
     * @return it.cnr.contab.compensi00.docs.bulk.V_doc_cont_compBulk
     */
    public V_doc_cont_compBulk getDocContPrincipale() {
        return docContPrincipale;
    }

    /**
     * Insert the method's description here. Creation date: (23/10/2002
     * 13.00.41)
     *
     * @param newDocContPrincipale it.cnr.contab.compensi00.docs.bulk.V_doc_cont_compBulk
     */
    public void setDocContPrincipale(V_doc_cont_compBulk newDocContPrincipale) {
        docContPrincipale = newDocContPrincipale;
    }

    /**
     * Insert the method's description here. Creation date: (27/08/2002
     * 17.28.36)
     *
     * @return java.util.Vector
     */
    public java.util.Vector getDocumentiContabiliCancellati() {
        return documentiContabiliCancellati;
    }

    /**
     * Insert the method's description here. Creation date: (27/08/2002
     * 17.28.36)
     *
     * @param newDocumentiContabiliCancellati java.util.Vector
     */
    public void setDocumentiContabiliCancellati(java.util.Vector newDocumentiContabiliCancellati) {
        documentiContabiliCancellati = newDocumentiContabiliCancellati;
    }

    /**
     * getDocumentoAmministrativoClassForDelete method comment.
     */
    public java.lang.Class getDocumentoAmministrativoClassForDelete() {
        return null;
    }

    /**
     * getDocumentoContabileClassForDelete method comment.
     */
    public java.lang.Class getDocumentoContabileClassForDelete() {
        return null;
    }

    public String getDsDocContPrincipale() {

        if (getDocContPrincipale() == null)
            return null;
        return getDocContPrincipale().getDs_doc_cont();
    }

    public java.lang.Integer getEsercizio_missione() {
        it.cnr.contab.missioni00.docs.bulk.MissioneBulk missione = this.getMissione();
        if (missione == null)
            return null;
        return missione.getEsercizio();
    }

    public void setEsercizio_missione(java.lang.Integer esercizio_missione) {
        this.getMissione().setEsercizio(esercizio_missione);
    }

    public java.lang.Integer getEsercizio_obbligazione() {
        it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obbligazioneScadenzario = this
                .getObbligazioneScadenzario();
        if (obbligazioneScadenzario == null)
            return null;
        it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = obbligazioneScadenzario.getObbligazione();
        if (obbligazione == null)
            return null;
        return obbligazione.getEsercizio();
    }

    public void setEsercizio_obbligazione(java.lang.Integer esercizio_obbligazione) {
        this.getObbligazioneScadenzario().getObbligazione().setEsercizio(esercizio_obbligazione);
    }

    /**
     * Insert the method's description here. Creation date: (14/07/2003
     * 11.14.16)
     *
     * @return int
     */
    public int getEsercizioScrivania() {
        return esercizioScrivania;
    }

    /**
     * Insert the method's description here. Creation date: (14/07/2003
     * 11.14.16)
     *
     * @param newEsercizioScrivania int
     */
    public void setEsercizioScrivania(int newEsercizioScrivania) {
        esercizioScrivania = newEsercizioScrivania;
    }

    /**
     * Insert the method's description here. Creation date: (27/05/2002
     * 12.54.22)
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getImporto_netto_spesa() {

        java.math.BigDecimal importoNettoSpesa = getIm_netto_percipiente();

        if (isDaMissione() && getMissione().getAnticipo() != null) {
            java.math.BigDecimal imAnticipo = getMissione().getAnticipo().getIm_anticipo();
            importoNettoSpesa = importoNettoSpesa.add(imAnticipo.negate());
        }
        return importoNettoSpesa;
    }

    /**
     * Insert the method's description here. Creation date: (27/05/2002
     * 12.54.22)
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getImporto_spesa() {

        java.math.BigDecimal importoSpesa = getIm_totale_compenso();

        if (isDaMissione() && getMissione().getAnticipo() != null) {
            java.math.BigDecimal imAnticipo = getMissione().getAnticipo().getIm_anticipo();
            importoSpesa = importoSpesa.add(imAnticipo.negate());
        }
        return importoSpesa;
    }

    public java.math.BigDecimal getImportoObbligazione() {

        java.math.BigDecimal importoObbligazione = getIm_totale_compenso();

        if (isDaMissione() && getMissione().getAnticipo() != null) {
            java.math.BigDecimal imAnticipo = getMissione().getAnticipo().getIm_anticipo();
            if (imAnticipo.compareTo(getIm_netto_percipiente()) <= 0)
                importoObbligazione = importoObbligazione.add(imAnticipo.negate());
            else
                importoObbligazione = importoObbligazione.add(getIm_netto_percipiente().negate());
        }

        return importoObbligazione;
    }

    /**
     * getImportoSignForDelete method comment.
     */
    public java.math.BigDecimal getImportoSignForDelete(java.math.BigDecimal importo) {
        return getIm_totale_compenso();
    }

    /**
     * Insert the method's description here. Creation date: (22/02/2002
     * 18.34.06)
     *
     * @return java.lang.String
     */
    public java.lang.String getIndirizzoTerzo() {

        if (getTerzo() == null)
            return null;

        String indirizzo = "";
        if (getTerzo().getVia_sede() != null)
            indirizzo = indirizzo + getTerzo().getVia_sede();
        if (getTerzo().getNumero_civico_sede() != null)
            indirizzo = indirizzo + " " + getTerzo().getNumero_civico_sede();

        return indirizzo;
    }

    /**
     * Insert the method's description here. Creation date: (02/07/2002
     * 15.33.58)
     *
     * @return it.cnr.contab.doccont00.core.bulk.Linea_attivitaBulk
     */
    public it.cnr.contab.config00.latt.bulk.WorkpackageBulk getLineaAttivita() {
        return lineaAttivita;
    }

    /**
     * Insert the method's description here. Creation date: (02/07/2002
     * 15.33.58)
     *
     * @param newLineaAttivita it.cnr.contab.config00.latt.bulk.Linea_attivitaBulk
     */
    public void setLineaAttivita(it.cnr.contab.config00.latt.bulk.WorkpackageBulk newLineaAttivita) {
        lineaAttivita = newLineaAttivita;
    }

    /**
     * Insert the method's description here. Creation date: (27/05/2002
     * 12.54.21)
     *
     * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
     */
    public java.lang.String getManagerName() {
        return "CRUDCompensoBP";
    }

    /**
     * Insert the method's description here. Creation date: (27/05/2002
     * 12.54.21)
     *
     * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
     */
    public java.lang.String getManagerOptions() {
        return "VTh";
    }

    /**
     * Insert the method's description here. Creation date: (7/3/2002 12:47:28
     * PM)
     *
     * @return it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk
     */
    public MinicarrieraBulk getMinicarriera() {
        return minicarriera;
    }

    /**
     * Insert the method's description here. Creation date: (7/3/2002 12:47:28
     * PM)
     *
     * @param newMinicarriera it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk
     */
    public void setMinicarriera(MinicarrieraBulk newMinicarriera) {
        minicarriera = newMinicarriera;
    }

    /**
     * Insert the method's description here. Creation date: (01/07/2002
     * 13.01.53)
     *
     * @return it.cnr.contab.missioni00.docs.bulk.MissioneBulk
     */
    public it.cnr.contab.missioni00.docs.bulk.MissioneBulk getMissione() {
        return missione;
    }

    /**
     * Insert the method's description here. Creation date: (01/07/2002
     * 13.01.53)
     *
     * @param newMissione it.cnr.contab.missioni00.docs.bulk.MissioneBulk
     */
    public void setMissione(it.cnr.contab.missioni00.docs.bulk.MissioneBulk newMissione) {
        missione = newMissione;
    }

    /**
     * Insert the method's description here. Creation date: (19/02/2002
     * 14.24.54)
     *
     * @return java.util.Collection
     */
    public java.util.Collection getModalita() {
        return modalita;
    }

    /**
     * Insert the method's description here. Creation date: (19/02/2002
     * 14.24.54)
     *
     * @param newModalita java.util.Collection
     */
    public void setModalita(java.util.Collection newModalita) {
        modalita = newModalita;
    }

    /**
     * Insert the method's description here. Creation date: (19/02/2002
     * 14.23.02)
     *
     * @return it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk
     */
    public Rif_modalita_pagamentoBulk getModalitaPagamento() {
        return modalitaPagamento;
    }

    /**
     * Insert the method's description here. Creation date: (19/02/2002
     * 14.23.02)
     *
     * @param newModalitaPagamento it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk
     */
    public void setModalitaPagamento(Rif_modalita_pagamentoBulk newModalitaPagamento) {
        modalitaPagamento = newModalitaPagamento;
    }

    /**
     * Insert the method's description here. Creation date: (14/05/2002
     * 12.35.18)
     *
     * @return it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk
     */
    public it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk getObbligazioneScadenzario() {
        return obbligazioneScadenzario;
    }

    /**
     * Insert the method's description here. Creation date: (14/05/2002
     * 12.35.18)
     *
     * @param newObbligazioneScadenzario it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk
     */
    public void setObbligazioneScadenzario(
            it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk newObbligazioneScadenzario) {
        obbligazioneScadenzario = newObbligazioneScadenzario;
    }

    /**
     * Insert the method's description here. Creation date: (27/05/2002
     * 12.54.21)
     *
     * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
     */
    public it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable getObbligazioniHash() {

        it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable table = new it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable();
        if (getObbligazioneScadenzario() != null)
            table.put(getObbligazioneScadenzario(), new java.util.Vector());
        return table;
    }

    public java.lang.Long getPg_banca() {
        it.cnr.contab.anagraf00.core.bulk.BancaBulk banca = this.getBanca();
        if (banca == null)
            return null;
        return banca.getPg_banca();
    }

    public void setPg_banca(java.lang.Long pg_banca) {
        this.getBanca().setPg_banca(pg_banca);
    }

    /**
     * Insert the method's description here. Creation date: (27/05/2002
     * 12.54.22)
     *
     * @return java.lang.Integer
     */
    public java.lang.Long getPg_doc_amm() {

        return getPg_compenso();
    }

    /**
     * Insert the method's description here. Creation date: (27/05/2002
     * 12.54.21)
     *
     * @return java.lang.Integer
     */
    public void setPg_doc_amm(java.lang.Long newPg) {

        setPg_compenso(newPg);
    }

    public java.lang.Long getPg_missione() {
        it.cnr.contab.missioni00.docs.bulk.MissioneBulk missione = this.getMissione();
        if (missione == null)
            return null;
        return missione.getPg_missione();
    }

    public void setPg_missione(java.lang.Long pg_missione) {
        this.getMissione().setPg_missione(pg_missione);
    }

    public Integer getEsercizio_ori_obbligazione() {
        it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obbligazioneScadenzario = this
                .getObbligazioneScadenzario();
        if (obbligazioneScadenzario == null)
            return null;
        it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = obbligazioneScadenzario.getObbligazione();
        if (obbligazione == null)
            return null;
        return obbligazione.getEsercizio_originale();
    }

    public void setEsercizio_ori_obbligazione(Integer esercizio_ori_obbligazione) {
        this.getObbligazioneScadenzario().getObbligazione().setEsercizio_originale(esercizio_ori_obbligazione);
    }

    public java.lang.Long getPg_obbligazione() {
        it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obbligazioneScadenzario = this
                .getObbligazioneScadenzario();
        if (obbligazioneScadenzario == null)
            return null;
        it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = obbligazioneScadenzario.getObbligazione();
        if (obbligazione == null)
            return null;
        return obbligazione.getPg_obbligazione();
    }

    public void setPg_obbligazione(java.lang.Long pg_obbligazione) {
        this.getObbligazioneScadenzario().getObbligazione().setPg_obbligazione(pg_obbligazione);
    }

    public java.lang.Long getPg_obbligazione_scadenzario() {
        it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obbligazioneScadenzario = this
                .getObbligazioneScadenzario();
        if (obbligazioneScadenzario == null)
            return null;
        return obbligazioneScadenzario.getPg_obbligazione_scadenzario();
    }

    public void setPg_obbligazione_scadenzario(java.lang.Long pg_obbligazione_scadenzario) {
        this.getObbligazioneScadenzario().setPg_obbligazione_scadenzario(pg_obbligazione_scadenzario);
    }

    /**
     * Insert the method's description here. Creation date: (17/06/2002
     * 12.51.45)
     *
     * @return java.lang.Long
     */
    public java.lang.Long getPgCompensoPerClone() {
        return pgCompensoPerClone;
    }

    /**
     * Insert the method's description here. Creation date: (17/06/2002
     * 12.51.45)
     *
     * @param newPgCompensoPerClone java.lang.Long
     */
    public void setPgCompensoPerClone(java.lang.Long newPgCompensoPerClone) {
        pgCompensoPerClone = newPgCompensoPerClone;
    }

    /**
     * Insert the method's description here. Creation date: (23/04/2002
     * 12.40.29)
     *
     * @return java.lang.Long
     */
    public java.lang.Long getPgCompensoPos() {

        if (getPg_compenso() != null && getPg_compenso().intValue() < 0)
            return null;

        return getPg_compenso();
    }

    /**
     * Insert the method's description here. Creation date: (23/04/2002
     * 12.40.29)
     *
     * @param newPgCompensoPos java.lang.Long
     */
    public void setPgCompensoPos(java.lang.Long newPgCompensoPos) {
        setPg_compenso(newPgCompensoPos);
    }

    public Long getPgDocContPrincipale() {

        if (getDocContPrincipale() == null)
            return null;
        return getDocContPrincipale().getPg_doc_cont();
    }

    public java.lang.Long getPgObbligazionePos() {
        if (getPg_obbligazione() != null && getPg_obbligazione().longValue() < 0)
            return null;

        return getPg_obbligazione();
    }

    public void setPgObbligazionePos(Long newLong) {
        setPg_obbligazione(newLong);
    }

    /**
     * Insert the method's description here. Creation date: (24/06/2002
     * 16.28.24)
     *
     * @return it.cnr.contab.anagraf00.tabter.bulk.RegioneBulk
     */
    public it.cnr.contab.anagraf00.tabter.bulk.RegioneBulk getRegioneIrap() {
        return regioneIrap;
    }

    /**
     * Insert the method's description here. Creation date: (24/06/2002
     * 16.28.24)
     *
     * @param newRegioneIrap it.cnr.contab.anagraf00.tabter.bulk.RegioneBulk
     */
    public void setRegioneIrap(it.cnr.contab.anagraf00.tabter.bulk.RegioneBulk newRegioneIrap) {
        regioneIrap = newRegioneIrap;
    }

    /**
     * Insert the method's description here. Creation date: (7/22/2002 2:17:18
     * PM)
     *
     * @return it.cnr.jada.bulk.PrimaryKeyHashMap
     */
    public it.cnr.jada.bulk.PrimaryKeyHashMap getRelationsDocContForSaldi() {
        return relationsDocContForSaldi;
    }

    /**
     * Insert the method's description here. Creation date: (7/22/2002 2:17:18
     * PM)
     *
     * @param newRelationsDocContForSaldi it.cnr.jada.bulk.PrimaryKeyHashMap
     */
    public void setRelationsDocContForSaldi(it.cnr.jada.bulk.PrimaryKeyHashMap newRelationsDocContForSaldi) {
        relationsDocContForSaldi = newRelationsDocContForSaldi;
    }

    /**
     * Insert the method's description here. Creation date: (30/05/2003
     * 15.55.11)
     *
     * @return java.lang.String
     */
    public java.lang.String getRiportata() {
        return riportata;
    }

    /**
     * Insert the method's description here. Creation date: (02/11/2004
     * 14.28.41)
     *
     * @return java.lang.String
     */
    public java.lang.String getRiportataInScrivania() {
        return riportataInScrivania;
    }

    public java.util.Dictionary getStato_cofiKeys() {

        return STATO_COFI;
    }

    public java.util.Dictionary getStato_cofiKeysForSearch() {

        it.cnr.jada.util.OrderedHashtable oh = (it.cnr.jada.util.OrderedHashtable) ((it.cnr.jada.util.OrderedHashtable) STATO_COFI)
                .clone();
        oh.remove(STATO_INIZIALE);
        return oh;
    }

    public java.util.Dictionary getStato_pagamento_fondo_ecoKeys() {

        if (getStato_pagamento_fondo_eco() != null
                && REGISTRATO_FONDO_ECO.equalsIgnoreCase(getStato_pagamento_fondo_eco())) {
            return STATO_FONDO_ECO;
        }

        it.cnr.jada.util.OrderedHashtable oh = (it.cnr.jada.util.OrderedHashtable) ((it.cnr.jada.util.OrderedHashtable) STATO_FONDO_ECO)
                .clone();
        oh.remove(REGISTRATO_FONDO_ECO);
        return oh;
    }

    public java.util.Dictionary getStato_pagamento_fondo_ecoKeysForSearch() {

        return STATO_FONDO_ECO;
    }

    /**
     * Insert the method's description here. Creation date: (20/06/2002
     * 11.50.31)
     *
     * @return java.lang.String
     */
    public int getStatoCompenso() {
        return statoCompenso;
    }

    /**
     * Insert the method's description here. Creation date: (20/06/2002
     * 11.50.31)
     *
     * @param newStatoCompenso java.lang.String
     */
    private void setStatoCompenso(int newStatoCompenso) {
        statoCompenso = newStatoCompenso;
    }

    /**
     * Insert the method's description here. Creation date: (19/02/2002
     * 14.25.07)
     *
     * @return java.util.Collection
     */
    public java.util.Collection getTermini() {
        return termini;
    }

    /**
     * Insert the method's description here. Creation date: (19/02/2002
     * 14.25.07)
     *
     * @param newTermini java.util.Collection
     */
    public void setTermini(java.util.Collection newTermini) {
        termini = newTermini;
    }

    /**
     * Insert the method's description here. Creation date: (19/02/2002
     * 14.22.43)
     *
     * @return it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk
     */
    public Rif_termini_pagamentoBulk getTerminiPagamento() {
        return terminiPagamento;
    }

    /**
     * Insert the method's description here. Creation date: (19/02/2002
     * 14.22.43)
     *
     * @param newTerminiPagamento it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk
     */
    public void setTerminiPagamento(Rif_termini_pagamentoBulk newTerminiPagamento) {
        terminiPagamento = newTerminiPagamento;
    }

    /**
     * Insert the method's description here. Creation date: (19/02/2002
     * 14.25.23)
     *
     * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
     */
    public TerzoBulk getTerzo() {

        if (getV_terzo() == null)
            return null;
        return getV_terzo().getTerzo();
    }

    /**
     * Insert the method's description here. Creation date: (27/05/2002
     * 12.54.22)
     *
     * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
     */
    public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getTerzo_spesa() {

        return getTerzo();
    }

    /**
     * Insert the method's description here. Creation date: (12/02/2002
     * 11.49.26)
     *
     * @return java.util.Dictionary
     */
    public java.util.Dictionary getTi_anagraficoKeys() {
        return Tipo_rapportoBulk.DIPENDENTE_ALTRO;
    }

    public java.util.Dictionary getTi_associato_manrevKeys() {

        return TI_ASSOCIATO_MANREV;
    }

    /**
     * Restituisce il <code>Dictionary</code> per la gestione dei tipi di
     * compenso.
     *
     * @return java.util.Dictionary
     */

    public java.util.Dictionary getTi_istituz_commercKeys() {
        return TIPI_COMPENSO;
    }

    /**
     * Insert the method's description here. Creation date: (26/02/2002
     * 11.19.49)
     *
     * @return java.util.Collection
     */
    public java.util.Collection getTipiRapporto() {
        return tipiRapporto;
    }

    /**
     * Insert the method's description here. Creation date: (26/02/2002
     * 11.19.49)
     *
     * @param newTipiRapporto java.util.Collection
     */
    public void setTipiRapporto(java.util.Collection newTipiRapporto) {
        tipiRapporto = newTipiRapporto;
    }

    /**
     * Insert the method's description here. Creation date: (26/02/2002
     * 11.58.06)
     *
     * @return java.util.Collection
     */
    public java.util.Collection getTipiTrattamento() {
        return tipiTrattamento;
    }

    /**
     * Insert the method's description here. Creation date: (26/02/2002
     * 11.58.06)
     *
     * @param newTipiTrattamento java.util.Collection
     */
    public void setTipiTrattamento(java.util.Collection newTipiTrattamento) {
        tipiTrattamento = newTipiTrattamento;
    }

    /**
     * Insert the method's description here. Creation date: (24/06/2002
     * 16.28.54)
     *
     * @return it.cnr.contab.compensi00.tabrif.bulk.Tipologia_rischioBulk
     */
    public it.cnr.contab.compensi00.tabrif.bulk.Tipologia_rischioBulk getTipologiaRischio() {
        return tipologiaRischio;
    }

    /**
     * Insert the method's description here. Creation date: (24/06/2002
     * 16.28.54)
     *
     * @param newTipologiaRischio it.cnr.contab.compensi00.tabrif.bulk.Tipologia_rischioBulk
     */
    public void setTipologiaRischio(it.cnr.contab.compensi00.tabrif.bulk.Tipologia_rischioBulk newTipologiaRischio) {
        tipologiaRischio = newTipologiaRischio;
        setCd_tipologia_rischio(newTipologiaRischio.getCd_tipologia_rischio());
    }

    /**
     * Insert the method's description here. Creation date: (21/02/2002
     * 14.43.50)
     *
     * @return it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk
     */
    public Tipo_rapportoBulk getTipoRapporto() {
        return tipoRapporto;
    }

    /**
     * Insert the method's description here. Creation date: (21/02/2002
     * 14.43.50)
     *
     * @param newTipoRapporto it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk
     */
    public void setTipoRapporto(Tipo_rapportoBulk newTipoRapporto) {
        tipoRapporto = newTipoRapporto;
    }

    /**
     * Insert the method's description here. Creation date: (21/02/2002
     * 14.46.57)
     *
     * @return it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk
     */
    public Tipo_trattamentoBulk getTipoTrattamento() {
        return tipoTrattamento;
    }

    /**
     * Insert the method's description here. Creation date: (21/02/2002
     * 14.46.57)
     *
     * @param newTipoTrattamento it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk
     */
    public void setTipoTrattamento(Tipo_trattamentoBulk newTipoTrattamento) {
        tipoTrattamento = newTipoTrattamento;
    }

    /**
     * Insert the method's description here. Creation date: (25/02/2002
     * 14.36.31)
     *
     * @return it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk
     */
    public V_terzo_per_compensoBulk getV_terzo() {
        return v_terzo;
    }

    /**
     * Insert the method's description here. Creation date: (25/02/2002
     * 14.36.31)
     *
     * @param newV_terzo it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk
     */
    public void setV_terzo(V_terzo_per_compensoBulk newV_terzo) {
        v_terzo = newV_terzo;
    }

    /**
     * Insert the method's description here. Creation date: (24/06/2002
     * 16.27.56)
     *
     * @return it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk
     */
    public it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk getVoceIva() {
        return voceIva;
    }

    /**
     * Insert the method's description here. Creation date: (24/06/2002
     * 16.27.56)
     *
     * @param newVoceIva it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk
     */
    public void setVoceIva(it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk newVoceIva) {
        voceIva = newVoceIva;
    }

    /**
     * Ritorna TRUE se ho selezionato una linea di attività valida, FALSE
     * altrimenti
     */
    public boolean hasLineaAttivita() {

        return getCd_linea_attivita_genrc() != null;
    }

    /**
     * Insert the method's description here. Creation date: (21/02/2002
     * 14.43.50)
     *
     * @return it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk
     */
    public void impostaModalitaPagamento(Rif_modalita_pagamentoBulk newModPag) {

        for (java.util.Iterator i = getModalita().iterator(); i.hasNext(); ) {
            Rif_modalita_pagamentoBulk modPag = (Rif_modalita_pagamentoBulk) i.next();
            if (modPag.equalsByPrimaryKey(newModPag))
                setModalitaPagamento(modPag);
        }
    }

    /**
     * Insert the method's description here. Creation date: (21/02/2002
     * 14.43.50)
     *
     * @return it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk
     */
    public void impostaTerminiPagamento(Rif_termini_pagamentoBulk newTermPag) {

        for (java.util.Iterator i = getTermini().iterator(); i.hasNext(); ) {
            Rif_termini_pagamentoBulk termPag = (Rif_termini_pagamentoBulk) i.next();
            if (termPag.equalsByPrimaryKey(newTermPag))
                setTerminiPagamento(termPag);
        }
    }

    /**
     * Insert the method's description here. Creation date: (21/02/2002
     * 14.43.50)
     *
     * @return it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk
     */
    public void impostaTipoRapporto(Tipo_rapportoBulk newTipoRapporto) {

        for (java.util.Iterator i = getTipiRapporto().iterator(); i.hasNext(); ) {
            Tipo_rapportoBulk tipo = (Tipo_rapportoBulk) i.next();
            if (tipo.equalsByPrimaryKey(newTipoRapporto))
                setTipoRapporto(tipo);
        }
    }

    public void impostaTipoTratt(Tipo_trattamentoBulk newTipoTrattamento) {

        for (java.util.Iterator i = getTipiTrattamento().iterator(); i.hasNext(); ) {
            Tipo_trattamentoBulk tipo = (Tipo_trattamentoBulk) i.next();
            if (tipo.equalsByPrimaryKey(newTipoTrattamento))
                setTipoTrattamento(tipo);
        }
    }

    /**
     * Insert the method's description here. Creation date: (21/02/2002
     * 14.43.50)
     *
     * @return it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk
     */
    public void impostaTipoTrattamento(Tipo_trattamentoBulk newTipoTrattamento) {

        setTipiTrattamento(new java.util.Vector());
        getTipiTrattamento().add(newTipoTrattamento);
        setTipoTrattamento(newTipoTrattamento);
    }

    public void impostaTipoPrestazioneCompenso(Tipo_prestazione_compensoBulk newTipoPrestazioneCompenso) {

        setTipiPrestazioneCompenso(new java.util.Vector());
        getTipiPrestazioneCompenso().add(newTipoPrestazioneCompenso);
        setTipoPrestazioneCompenso(newTipoPrestazioneCompenso);
    }

    public OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) {

        it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo
                .getUnita_organizzativa(context);
        setCd_cds(unita_organizzativa.getCd_unita_padre());
        setCd_unita_organizzativa(unita_organizzativa.getCd_unita_organizzativa());
        setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
        setCd_uo_origine(getCd_unita_organizzativa());
        setCd_cds_origine(getCd_cds());

        return this;
    }

    /**
     * Insert the method's description here. Creation date: (18/01/2002
     * 14.52.26)
     */
    public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,
                                           it.cnr.jada.action.ActionContext context) {
        super.initializeForInsert(bp, context);
        return initialize();
    }

    public OggettoBulk initialize() {
        setNumero_giorni(new Integer(0));

        setTi_anagrafico(Tipo_rapportoBulk.DIPENDENTE);
        resetFlags();
        resetStati();
        resetImporti();
        resetDetrazioni();

        resetDatiLiquidazione();

        setFl_escludi_qvaria_deduzione(Boolean.FALSE);
        setFl_intera_qfissa_deduzione(Boolean.FALSE);
        setFl_recupero_rate(Boolean.FALSE);
        setFl_accantona_add_terr(Boolean.FALSE);
        setIm_detrazione_personale_anag(new java.math.BigDecimal(0));

        return this;
    }

    /**
     * Insert the method's description here. Creation date: (18/01/2002
     * 14.52.26)
     */
    public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp,
                                           it.cnr.jada.action.ActionContext context) {

        super.initializeForSearch(bp, context);

        if (((it.cnr.contab.compensi00.bp.CRUDCompensoBP) bp).isSpesaBP()) {
            setStato_pagamento_fondo_eco(ASSEGNATO_FONDO_ECO);
            setStato_cofi(STATO_CONTABILIZZATO);
            setStato_liquidazione(LIQ);
            it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo = it.cnr.contab.utenze00.bulk.CNRUserInfo
                    .getUnita_organizzativa(context);
            if (it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC
                    .equalsIgnoreCase(uo.getCd_tipo_unita())) {
                setCd_unita_organizzativa(null);
                setCd_uo_origine(null);
            }

        }

        return this;
    }

    /**
     * Insert the method's description here. Creation date: (21/02/2002
     * 17.01.17)
     *
     * @return boolean
     */
    public boolean isAbledToInsertBank() {

        return !(getTerzo() != null && getModalitaPagamento() != null && !isROModalitaPagamento());
    }

    public boolean isAnnullato() {
        return Optional.ofNullable(getStato_cofi()).map(x -> x.equals(STATO_ANNULLATO)).orElse(false);
    }

    public boolean isStatoCofiPagato() {
        return Optional.ofNullable(getStato_cofi()).map(x -> x.equals(STATO_PAGATO)).orElse(false);
    }

    public boolean isApertoDaMinicarriera() {
        return isDaMinicarriera() && getAperturaDaMinicarriera().booleanValue();
    }

    public boolean isAssegnatoAFondoEconomale() {
        return ASSEGNATO_FONDO_ECO.equals(getStato_pagamento_fondo_eco());
    }

    /**
     * Se il compenso è associato a Missione, Minicarriera o Conguaglio la Data
     * Competenza Coge non è modificabile
     *
     * @return boolean
     */

    public boolean isAssociatoADocumento() {

        return isDaMissione() || isDaMinicarriera() || isDaConguaglio() || isDaBonus()
                || (isDaFatturaPassiva() && getFatturaPassiva() == null);
    }

    public boolean isAssociatoAMandato() {

        return ASSOCIATO_MANREV.equals(getTi_associato_manrev());
    }

    /**
     * Insert the method's description here. Creation date: (25/02/2002
     * 11.24.00)
     *
     * @return boolean
     */
    public boolean isCancellabile() {
        return (Optional.ofNullable(getStato_cofi()).map(x -> x.equals(STATO_INIZIALE) || x.equals(STATO_CONTABILIZZATO)).orElse(true)
                && Optional.ofNullable(getTi_associato_manrev()).map(x -> x.equals(NON_ASSOCIATO_MANREV)).orElse(true));
    }

    public boolean isDaConguaglio() {

        return (getConguaglio() != null);
    }

    public boolean isDaMinicarriera() {

        return (getMinicarriera() != null);
    }

    public boolean isDaMissione() {

        return (getMissione() != null);
    }

    public boolean isDaBonus() {

        return (getBonus() != null);
    }

    public boolean isDaFatturaPassiva() {
        return (isGestione_doc_ele() && (getFl_generata_fattura() != null && getFl_generata_fattura()));
    }

    /**
     * isDeleting method comment.
     */
    public boolean isDeleting() {
        return false;
    }

    /**
     * isEditable method comment.
     */
    public boolean isEditable() {
        return (getEsercizioScrivania() == Optional.ofNullable(getEsercizio()).map(Integer::intValue).orElse(0)) && !isRiportata();
    }

    /**
     * Puo' capitare che il compenso risulti riportato ma in realtà non ha
     * obbligazione.
     */

    public boolean isLabelRiportoToShow() {
        return isObbligazioneObbligatoria();
    }

    /**
     * E' obbligatorio associare al compenso un'obbligazione solo se l'importo
     * lordo è strettamente positivo
     **/
    public boolean isObbligazioneObbligatoria() {
        return Optional.ofNullable(getImportoObbligazione()).map(x -> x.compareTo(BigDecimal.ZERO) > 0).orElse(false);
    }

    public boolean isPagato() {

        return (STATO_PAGATO.equals(getStato_cofi()) || (REGISTRATO_FONDO_ECO.equals(getStato_pagamento_fondo_eco())
                && STATO_CONTABILIZZATO.equals(getStato_cofi())));
    }

    public boolean isRiportata() {

        return !NON_RIPORTATO.equals(riportata);
    }

    /**
     * Insert the method's description here. Creation date: (30/05/2003
     * 15.55.11)
     *
     * @param newRiportata java.lang.String
     */
    public void setRiportata(java.lang.String newRiportata) {
        riportata = newRiportata;
    }

    /**
     * isRiportataInScrivania method comment.
     */
    public boolean isRiportataInScrivania() {

        return !NON_RIPORTATO.equals(riportataInScrivania);
    }

    /**
     * Insert the method's description here. Creation date: (02/11/2004
     * 14.28.41)
     *
     * @param newRiportataInScrivania java.lang.String
     */
    public void setRiportataInScrivania(java.lang.String newRiportataInScrivania) {
        riportataInScrivania = newRiportataInScrivania;
    }

    /**
     * Insert the method's description here. Creation date: (25/02/2002
     * 11.24.00)
     *
     * @return boolean
     */
    public boolean isROCdLineaAttivita() {

        return isROLineaAttivita()
                || (getLineaAttivita() == null || getLineaAttivita().getCrudStatus() == OggettoBulk.NORMAL);
    }

    /**
     * Insert the method's description here. Creation date: (25/02/2002
     * 11.24.00)
     *
     * @return boolean
     */
    public boolean isROCdTerzo() {

        return isROTerzo() || (getV_terzo() == null || getV_terzo().getCrudStatus() == OggettoBulk.NORMAL);
    }

    /**
     * Insert the method's description here. Creation date: (25/02/2002
     * 11.24.00)
     *
     * @return boolean
     */
    public boolean isRODatiFattura() {

        return isSenzaCalcoli() || isDaConguaglio() || isROPerChiusura() || isGestione_doc_ele();
    }

    /**
     * Insert the method's description here. Creation date: (25/02/2002
     * 11.24.00)
     *
     * @return boolean
     */
    public boolean isRODsCompenso() {
        return isROPerChiusura();
    }

    /**
     * Se il compenso è associato a Missione, Minicarriera o Conguaglio la Data
     * Competenza Coge non è modificabile
     *
     * @return boolean
     */
    public boolean isRODtACompetenzaCoge() {

        return isAssociatoADocumento() || isROPerChiusura() || getFatturaPassiva() != null;
    }

    /**
     * Se il compenso è associato a Missione, Minicarriera o Conguaglio la Data
     * Competenza Coge non è modificabile
     *
     * @return boolean
     */
    public boolean isRODtDaCompetenzaCoge() {

        return isAssociatoADocumento() || isROPerChiusura() || getFatturaPassiva() != null;
    }

    /**
     * Se il compenso è associato a Missione, Minicarriera o Conguaglio la Data
     * Registrazione non è modificabile
     *
     * @return boolean
     */
    public boolean isRODtRegistrazione() {

        return isAssociatoADocumento() || isROPerChiusura() || getFatturaPassiva() != null;
    }

    /**
     * Insert the method's description here. Creation date: (25/02/2002
     * 11.24.00)
     *
     * @return boolean
     */
    public boolean isROFindRegioneIrap() {
        return isROPerChiusura();
    }

    /**
     * Insert the method's description here. Creation date: (25/02/2002
     * 11.24.00)
     *
     * @return boolean
     */
    public boolean isROFindTipologiaRischio() {
        return isROPerChiusura();
    }

    /**
     * Insert the method's description here. Creation date: (25/02/2002
     * 11.24.00)
     *
     * @return boolean
     */
    public boolean isROFindVoceIva() {

        return false;
    }

    /**
     * Se il compenso è associato a Missione, Minicarriera o Conguaglio il flag
     * Senza Calcoli non è modificabile
     *
     * @return boolean
     */
    public boolean isROFlagSenzaCalcoli() {

        return isDaMissione() || isDaMinicarriera() || isDaConguaglio() || isDaBonus() || isROPerChiusura() || (isDaFatturaPassiva() && !isUserAbilitatoSenzaCalcolo());
    }

    /**
     * Insert the method's description here. Creation date: (25/02/2002
     * 11.24.00)
     *
     * @return boolean
     */
    public boolean isROImLordoPercipiente() {
        return isAssociatoADocumento() || isROPerChiusura();
    }

    public boolean isROImportoNoFiscale() {
        return isAssociatoADocumento() || isROPerChiusura();
    }

    /**
     * Insert the method's description here. Creation date: (25/02/2002
     * 11.24.00)
     *
     * @return boolean
     */
    public boolean isROLineaAttivita() {

        if (isStatoCompensoNormale() || isStatoCompensoEseguiCalcolo() || isROPerChiusura())
            return true;

        return getImportoObbligazione().compareTo(new java.math.BigDecimal(0)) >= 0;
    }

    /**
     * Insert the method's description here. Creation date: (25/02/2002
     * 11.24.00)
     *
     * @return boolean
     */
    public boolean isROModalitaPagamento() {
        return isROPerChiusura();// || isDaFatturaPassiva();
    }

    /**
     * Chiusura : se carico un compenso con esercizio precedente a quello solare
     * : - esercizio scrivania != anno solare e obbligazione riportata -->
     * nessuna modifica al documento - esercizio scrivania != anno solare e
     * obbligazione non riportata --> qualsiasi modifica consentita - esercizio
     * scrivania = anno solare e obbligazione riportata --> nessuna modifica
     * tranne CoFi e Annulla - esercizio scrivania = anno solare e obbligazione
     * non riportata --> nessuna modifica al documento
     */

    public boolean isROPerChiusura() {

        if (getEsercizio() == null)
            return false;

        // if(getEsercizio().intValue() == getAnnoSolare())
        // return false;

        // if(getEsercizioScrivania() != getAnnoSolare())
        // return isRiportata();

        // return true;

        // Gennaro Borriello/Farinella Luisa - (05/11/2004 12.23.28)
        // Modif. relativa alla nuova gestione di isRiportata()
        // MB - (06/11/2004 12.23.28)
        if (getEsercizio().intValue() == getEsercizioScrivania()) {
            return isRiportata();
        } else
            return true;
    }

    public boolean isROQuotaEsente() {
        return isAssociatoADocumento() || isROPerChiusura();
    }

    public boolean isROQuotaEsenteNoIva() {
        return isAssociatoADocumento() || isROPerChiusura();
    }

    /**
     * Insert the method's description here. Creation date: (25/02/2002
     * 11.24.00)
     *
     * @return boolean
     */
    public boolean isRORecuperoRate() {
        return Boolean.FALSE.equals(getFl_senza_calcoli()) || isROPerChiusura();
    }

    /**
     * Insert the method's description here. Creation date: (25/02/2002
     * 11.24.00)
     *
     * @return boolean
     */
    public boolean isRORegioneIrap() {

        return isROFindRegioneIrap()
                || (getRegioneIrap() == null || getRegioneIrap().getCrudStatus() == OggettoBulk.NORMAL);
    }

    public boolean isROStato_pagamento_fondo_eco() {

        return REGISTRATO_FONDO_ECO.equals(getStato_pagamento_fondo_eco()) || isROPerChiusura() || isElettronica();
    }

    /**
     * Insert the method's description here. Creation date: (25/02/2002
     * 11.24.00)
     *
     * @return boolean
     */
    public boolean isROTerminiPagamento() {
        return isROPerChiusura();
    }

    /**
     * Insert the method's description here. Creation date: (25/02/2002
     * 11.24.00)
     *
     * @return boolean
     */
    public boolean isROTerzo() {

        return isAssociatoADocumento() || isROPerChiusura() || getFatturaPassiva() != null;
    }

    /**
     * Se il compenso è associato a Missione, Minicarriera o Conguaglio il flag
     * Senza Calcoli non è modificabile
     *
     * @return boolean
     */
    public boolean isROTi_istituz_commerc() {

        return isAssociatoADocumento() || isROPerChiusura() || getFatturaPassiva() != null;
    }

    /**
     * Insert the method's description here. Creation date: (25/02/2002
     * 11.24.00)
     *
     * @return boolean
     */
    public boolean isROTipologiaRischio() {

        return isROFindTipologiaRischio()
                || (getTipologiaRischio() == null || getTipologiaRischio().getCrudStatus() == OggettoBulk.NORMAL);
    }

    /**
     * Insert the method's description here. Creation date: (25/02/2002
     * 11.24.00)
     *
     * @return boolean
     */
    public boolean isROTipoRapporto() {

        return isROTerzo();
    }

    /**
     * Insert the method's description here. Creation date: (25/02/2002
     * 11.24.00)
     *
     * @return boolean
     */
    public boolean isROTipoTrattamento() {

        return isROTerzo() && getFatturaPassiva() == null;
    }

    public boolean isROTipoPrestazioneCompenso() {

        return isROTerzo() && getFatturaPassiva() == null;
    }

    /**
     * Insert the method's description here. Creation date: (25/02/2002
     * 11.24.00)
     *
     * @return boolean
     */
    public boolean isROVoceIva() {

        return isROFindVoceIva() || (getVoceIva() == null || getVoceIva().getCrudStatus() == OggettoBulk.NORMAL);
    }

    /**
     * Inserito per recuperare i Parametri_cnr e gestire la
     * isROQuota_esente_inps
     *
     * @autor Marco Spasiano Creation date: (25/02/2002 11.24.00)
     * @see it.cnr.jada.bulk.OggettoBulk#fillFromActionContext(it.cnr.jada.action.ActionContext,
     * java.lang.String, int, it.cnr.jada.bulk.FieldValidationMap)
     */
    public boolean fillFromActionContext(ActionContext actioncontext, String s, int i,
                                         FieldValidationMap fieldvalidationmap) throws FillException {
        if (getTipoRapporto() != null && getTipoRapporto().getCd_tipo_rapporto()
                .equals(it.cnr.contab.utenze00.bulk.CNRUserInfo.getCd_tipo_rapporto(actioncontext))) {
            roQuota_esente_inps = java.lang.Boolean.FALSE;
        } else {
            roQuota_esente_inps = java.lang.Boolean.TRUE;
        }
        return super.fillFromActionContext(actioncontext, s, i, fieldvalidationmap);
    }

    /**
     * Insert the method's description here. Creation date: (25/02/2002
     * 11.24.00)
     *
     * @return boolean
     */
    public boolean isROQuota_esente_inps() {
        return (getTipoRapporto() == null || roQuota_esente_inps.booleanValue());
    }

    /**
     * Insert the method's description here. Creation date: (02/07/2002
     * 17.16.49)
     *
     * @return boolean
     */
    public boolean isSenzaCalcoli() {

        return Boolean.TRUE.equals(getFl_senza_calcoli());
    }

    /**
     * Insert the method's description here. Creation date: (25/02/2002
     * 11.24.00)
     *
     * @return boolean
     */
    public boolean isStatoCofiIniziale() {

        return STATO_INIZIALE.equals(getStato_cofi());
    }

    public boolean isStatoCompensoContabilizzaCofi() {

        return getStatoCompenso() == STATO_COMPENSO_CONTABILIZZA_COFI;
    }

    public boolean isStatoCompensoEseguiCalcolo() {

        return getStatoCompenso() == STATO_COMPENSO_ESEGUI_CALCOLO;
    }

    public boolean isStatoCompensoNormale() {

        return getStatoCompenso() == STATO_COMPENSO_NORMALE;
    }

    public boolean isStatoCompensoObbligazioneSincronizzata() {

        return getStatoCompenso() == STATO_COMPENSO_OBBLIGAZIONE_SINCRONIZZATA;
    }

    public boolean isStatoCompensoSincronizzaObbligazione() {

        return getStatoCompenso() == STATO_COMPENSO_SINCRONIZZA_OBBLIGAZIONE;
    }

    public boolean isTemporaneo() {

        if (getPg_compenso() == null)
            return false;
        return getPg_compenso().longValue() < 0;
    }

    /**
     * Insert the method's description here. Creation date: (24/06/2002
     * 17.52.30)
     *
     * @return boolean
     */
    public boolean isVisualizzaRegioneIrap() {
        return visualizzaRegioneIrap;
    }

    /**
     * Insert the method's description here. Creation date: (24/06/2002
     * 17.52.30)
     *
     * @param newVisualizzaRegioneIrap boolean
     */
    public void setVisualizzaRegioneIrap(boolean newVisualizzaRegioneIrap) {
        visualizzaRegioneIrap = newVisualizzaRegioneIrap;
    }

    /**
     * Insert the method's description here. Creation date: (24/06/2002
     * 17.52.50)
     *
     * @return boolean
     */
    public boolean isVisualizzaTipologiaRischio() {
        return visualizzaTipologiaRischio;
    }

    /**
     * Insert the method's description here. Creation date: (24/06/2002
     * 17.52.50)
     *
     * @param newVisualizzaTipologiaRischio boolean
     */
    public void setVisualizzaTipologiaRischio(boolean newVisualizzaTipologiaRischio) {
        visualizzaTipologiaRischio = newVisualizzaTipologiaRischio;
    }

    /**
     * Insert the method's description here. Creation date: (24/06/2002
     * 17.52.13)
     *
     * @return boolean
     */
    public boolean isVisualizzaVoceIva() {
        return visualizzaVoceIva;
    }

    /**
     * Insert the method's description here. Creation date: (24/06/2002
     * 17.52.13)
     *
     * @param newVisualizzaVoceIva boolean
     */
    public void setVisualizzaVoceIva(boolean newVisualizzaVoceIva) {
        visualizzaVoceIva = newVisualizzaVoceIva;
    }

    public boolean isVisualizzaCodici_rapporti_inps() {
        return visualizzaCodici_rapporti_inps;
    }

    public void setVisualizzaCodici_rapporti_inps(boolean newVisualizzaCodici_rapporti_inps) {
        visualizzaCodici_rapporti_inps = newVisualizzaCodici_rapporti_inps;
    }

    public boolean isVisualizzaCodici_attivita_inps() {
        return visualizzaCodici_attivita_inps;
    }

    public void setVisualizzaCodici_attivita_inps(boolean newVisualizzaCodici_attivita_inps) {
        visualizzaCodici_attivita_inps = newVisualizzaCodici_attivita_inps;
    }

    public boolean isVisualizzaCodici_altra_forma_ass_inps() {
        return visualizzaCodici_altra_forma_ass_inps;
    }

    public void setVisualizzaCodici_altra_forma_ass_inps(boolean newVisualizzaCodici_altra_forma_ass_inps) {
        visualizzaCodici_altra_forma_ass_inps = newVisualizzaCodici_altra_forma_ass_inps;
    }

    /**
     * Insert the method's description here. Creation date: (24/05/2002
     * 12.55.59)
     *
     * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
     */
    public void removeFromDefferredSaldi(it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk docCont) {

        if (docCont != null && deferredSaldi != null)
            deferredSaldi.remove(docCont);
    }

    /**
     * per interfaccia IDocumentoAmministrativoSpesaBulk
     */
    public int removeFromDettagliCancellati(
            it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk dettaglio) {

        if (BulkCollections.containsByPrimaryKey(getDettagliCancellati(), (OggettoBulk) dettaglio))
            getDettagliCancellati()
                    .remove(BulkCollections.indexOfByPrimaryKey(getDettagliCancellati(), (OggettoBulk) dettaglio));

        return getDettagliCancellati().size() - 1;
    }

    /**
     * per interfaccia IDocumentoAmministrativoSpesaBulk
     */
    public int removeFromDocumentiContabiliCancellati(
            it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk scadenza) {

        if (getDocumentiContabiliCancellati() == null)
            return -1;

        if (scadenza != null
                && BulkCollections.containsByPrimaryKey(getDocumentiContabiliCancellati(), (OggettoBulk) scadenza))
            getDocumentiContabiliCancellati().remove(
                    BulkCollections.indexOfByPrimaryKey(getDocumentiContabiliCancellati(), (OggettoBulk) scadenza));

        return getDocumentiContabiliCancellati().size() - 1;
    }

    /**
     * Insert the method's description here. Creation date: (5/15/2002 10:50:29
     * AM)
     *
     * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
     */
    public void removeFromRelationsDocContForSaldi(it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk docCont) {

        if (docCont != null && relationsDocContForSaldi != null)
            relationsDocContForSaldi.remove(docCont);
    }

    public void resetDatiFattura() {

        setEsercizio_fattura_fornitore(null);
        setDt_fattura_fornitore(null);
        setNr_fattura_fornitore(null);
        setFl_generata_fattura(Boolean.FALSE);
        setFl_liquidazione_differita(Boolean.FALSE);
        setData_protocollo(null);
        setNumero_protocollo(null);
        setDt_scadenza(null);
    }

    public void resetDatiLiquidazione() {

        setRegioneIrap(new it.cnr.contab.anagraf00.tabter.bulk.RegioneBulk());
        setVoceIva(new it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk());
        setTipologiaRischio(new Tipologia_rischioBulk());
        setImponibile_inail(new java.math.BigDecimal(0));

        setVisualizzaRegioneIrap(false);
        setVisualizzaTipologiaRischio(false);
        setCompensoSoloInailEnte(false);
        setVisualizzaVoceIva(false);
    }

    /**
     * Insert the method's description here. Creation date: (24/05/2002
     * 13.07.35)
     */
    public void resetDefferredSaldi() {

        deferredSaldi = null;
    }

    private void resetDetrazioni() {

        setDetrazione_altri(new java.math.BigDecimal(0));
        setDetrazione_coniuge(new java.math.BigDecimal(0));
        setDetrazione_figli(new java.math.BigDecimal(0));
        setDetrazioni_la(new java.math.BigDecimal(0));
        setDetrazioni_personali(new java.math.BigDecimal(0));

        setDetrazione_altri_netto(new java.math.BigDecimal(0));
        setDetrazione_coniuge_netto(new java.math.BigDecimal(0));
        setDetrazione_figli_netto(new java.math.BigDecimal(0));
        setDetrazioni_la_netto(new java.math.BigDecimal(0));
        setDetrazioni_personali_netto(new java.math.BigDecimal(0));
        setDetrazioneRiduzioneCuneo(new java.math.BigDecimal(0));
        setDetrazioneRidCuneoNetto(new java.math.BigDecimal(0));
    }

    private void resetFlags() {

        setFl_diaria(Boolean.FALSE);
        setFl_senza_calcoli(Boolean.FALSE);
        setFl_compenso_conguaglio(Boolean.FALSE);
        setFl_compenso_stipendi(Boolean.FALSE);
        setFl_compenso_minicarriera(Boolean.FALSE);
        setFl_compenso_mcarriera_tassep(Boolean.FALSE);
        setFl_generata_fattura(Boolean.FALSE);
        setFl_liquidazione_differita(Boolean.FALSE);
        setFl_documento_ele(Boolean.FALSE);
        setFl_split_payment(Boolean.FALSE);
    }

    private void resetImporti() {

        setIm_totale_compenso(new java.math.BigDecimal(0));
        setIm_lordo_percipiente(new java.math.BigDecimal(0));
        setIm_netto_percipiente(new java.math.BigDecimal(0));
        setIm_cr_percipiente(new java.math.BigDecimal(0));
        setQuota_esente_inps(new java.math.BigDecimal(0));
        setIm_cr_ente(new java.math.BigDecimal(0));
        setQuota_esente(new java.math.BigDecimal(0));
        setQuota_esente_no_iva(new java.math.BigDecimal(0));
        setIm_no_fiscale(new java.math.BigDecimal(0));
        setImponibile_fiscale(new java.math.BigDecimal(0));
        setImponibile_iva(new java.math.BigDecimal(0));
        setAliquota_irpef_da_missione(new java.math.BigDecimal(0));
        setAliquota_irpef_tassep(new java.math.BigDecimal(0));
        setIm_deduzione_irpef(new java.math.BigDecimal(0));
        setImponibile_fiscale_netto(new java.math.BigDecimal(0));
    }

    private void resetStati() {

        setStato_cofi(STATO_INIZIALE);
        setStato_coge(NON_CONTABILIZZATO_COGE);
        setStato_coan(NON_CONTABILIZZATO_COAN);
        setStato_pagamento_fondo_eco(LIBERO_FONDO_ECO);
        setTi_associato_manrev(NON_ASSOCIATO_MANREV);
        setTi_anagrafico(Tipo_rapportoBulk.ALTRO);
        setTi_istituz_commerc(TipoIVA.ISTITUZIONALE.value());
    }

    public void resetStatoCogeCoan() {

        if (CONTABILIZZATO_COAN.equals(getStato_coan()))
            setStato_coan(DA_RICONTABILIZZARE_COAN);

        if (CONTABILIZZATO_COGE.equals(getStato_coge()))
            setStato_coge(DA_RICONTABILIZZARE_COGE);
    }

    /**
     * setIsDeleting method comment.
     */
    public void setIsDeleting(boolean deletingStatus) {
    }

    @Override
    public Scrittura_partita_doppiaBulk getScrittura_partita_doppia() {
        return scrittura_partita_doppia;
    }

    @Override
    public void setScrittura_partita_doppia(Scrittura_partita_doppiaBulk scrittura_partita_doppia) {
        this.scrittura_partita_doppia = scrittura_partita_doppia;
    }

    public void setStatoCompensoToContabilizzaCofi() {

        setStatoCompenso(STATO_COMPENSO_CONTABILIZZA_COFI);
    }

    public void setStatoCompensoToEseguiCalcolo() {

        setStatoCompenso(STATO_COMPENSO_ESEGUI_CALCOLO);
    }

    public void setStatoCompensoToNormale() {

        setStatoCompenso(STATO_COMPENSO_NORMALE);
    }

    public void setStatoCompensoToObbligazioneSincronizzata() {

        setStatoCompenso(STATO_COMPENSO_OBBLIGAZIONE_SINCRONIZZATA);
    }

    public void setStatoCompensoToSincronizzaObbligazione() {

        setStatoCompenso(STATO_COMPENSO_SINCRONIZZA_OBBLIGAZIONE);
    }

    /**
     * Al rientro da ogni aggiornamento manuale della scadenza associata a
     * compenso devo sincronizzare anche le scadenze che ho messo tra le
     * cancellate perche' l'utente potrebbe averle modificate. Se non dovessi
     * trovare una delle mie scadenze cancellate tra quelle della obbligazione
     * significa che l'utente l'ha eliminata fisicamente.
     */
    public void sincronizzaScadenzeCancellate(Obbligazione_scadenzarioBulk newScadenza) {
        if (getDocumentiContabiliCancellati() == null || getDocumentiContabiliCancellati().isEmpty())
            return;

        if (getObbligazioneScadenzario() == null)
            return;

        if (!newScadenza.getObbligazione().equalsByPrimaryKey(getObbligazioneScadenzario().getObbligazione()))
            return;

        boolean trovata = false;
        BulkList coll = newScadenza.getObbligazione().getObbligazione_scadenzarioColl();
        if (coll == null)
            return;

        for (Iterator c = ((Vector) getDocumentiContabiliCancellati().clone()).iterator(); c.hasNext(); ) {
            Obbligazione_scadenzarioBulk aScadCanc = (Obbligazione_scadenzarioBulk) c.next();
            trovata = false;

            for (Iterator i = coll.iterator(); i.hasNext(); ) {
                Obbligazione_scadenzarioBulk aScadenza = (Obbligazione_scadenzarioBulk) i.next();

                if (aScadenza.equalsByPrimaryKey(aScadCanc)) {
                    getDocumentiContabiliCancellati().remove(aScadCanc);
                    getDocumentiContabiliCancellati().add(aScadenza);
                    trovata = true;
                }
            }
            if (!trovata && aScadCanc.getObbligazione().equalsByPrimaryKey(newScadenza.getObbligazione()))
                getDocumentiContabiliCancellati().remove(aScadCanc);
        }
    }

    public void validaDate() throws it.cnr.jada.comp.ApplicationException, it.cnr.jada.action.BusinessProcessException,
            java.text.ParseException, javax.ejb.EJBException {
        if (getDt_registrazione() == null)
            throw new it.cnr.jada.comp.ApplicationException("Inserire la data registrazione");

        Calendar calendar = getDateCalendar(getDt_registrazione());
        int annoDataRegistrazione = calendar.get(Calendar.YEAR);

        if (annoDataRegistrazione != getEsercizio().intValue())
            throw new it.cnr.jada.comp.ApplicationException(
                    "La Data registrazione deve essere nell'esercizio in corso");
        if (getDt_registrazione().compareTo(getDataOdierna()) > 0)
            throw new it.cnr.jada.comp.ApplicationException(
                    "La Data registrazione non può essere superiore alla data odierna");

        validaDateCompetenzaCoge();
    }

    public void validaDateCompetenzaCoge() throws ApplicationException {
        if (getDt_da_competenza_coge() == null)
            throw new ApplicationException("Inserire la Data Inizio Competenza COGE.");
        if (getDt_a_competenza_coge() == null)
            throw new ApplicationException("Inserire la Data Fine Competenza COGE.");

        if (getDt_a_competenza_coge().before(getDt_da_competenza_coge()))
            throw new ApplicationException("La Data Inizio Competenza deve essere inferiore alla Data Fine Competenza");

        Calendar competenzaDa = getDateCalendar(getDt_da_competenza_coge());
        Calendar competenzaA = getDateCalendar(getDt_a_competenza_coge());
        int annoCompetenzaDa = competenzaDa.get(Calendar.YEAR);
        int annoCompetenzaA = competenzaA.get(Calendar.YEAR);

        if (annoCompetenzaDa <= getEsercizio().intValue() - 2)
            throw new ApplicationException(
                    "La Data di Inizio Competenza deve appartenere all'esercizio di scrivania o al precedente!");
        if (annoCompetenzaA >= getEsercizio().intValue() + 2)
            throw new ApplicationException(
                    "La data di Fine Competenza deve appartenere all'esercizio di scrivania o al successivo!");
    }

    public void validaDatiFattura() throws it.cnr.jada.comp.ApplicationException {

        // Non ho inserito nessun dato oppure ho inserito tutti i dati relativi
        // alla fattura fornitore
        boolean tuttiNull = getEsercizio_fattura_fornitore() == null && getNr_fattura_fornitore() == null
                && getDt_fattura_fornitore() == null;
        boolean tuttiNotNull = getEsercizio_fattura_fornitore() != null && getNr_fattura_fornitore() != null
                && getDt_fattura_fornitore() != null;
        if (tuttiNull && Boolean.TRUE.equals(getFl_generata_fattura()))
            throw new it.cnr.jada.comp.ApplicationException(
                    "Inserire gli estremi identificativi della fattura fornitore");
        if (tuttiNotNull && Boolean.FALSE.equals(getFl_generata_fattura()))
            throw new it.cnr.jada.comp.ApplicationException(
                    "Indicare generare fattura o eliminare gli estremi identificativi della fattura");
        if (!(tuttiNull || tuttiNotNull))
            throw new it.cnr.jada.comp.ApplicationException(
                    "Completare gli estremi identificativi della fattura fornitore.");

        if (getDt_registrazione().after(dataInizioObbligoRegistroUnico)
                && Boolean.TRUE.equals(getFl_generata_fattura() && Boolean.FALSE.equals(isGestione_doc_ele()))) {
            if (getDt_scadenza() == null)
                throw new ApplicationException("Inserire la data di scadenza.");
            if (getData_protocollo() == null)
                throw new ApplicationException("Inserire la data di protocollo di entrata.");
            if (getNumero_protocollo() == null)
                throw new ApplicationException("Inserire il numero di protocollo di entrata!");
            if (getData_protocollo() != null && getData_protocollo().before(getDt_fattura_fornitore()))
                throw new it.cnr.jada.comp.ApplicationException(
                        "La data di protocollo non può essere precedente alla data di emissione del documento del fornitore!");
        }

        if (getData_protocollo() != null && getData_protocollo().after(getDt_registrazione()))
            throw new it.cnr.jada.comp.ApplicationException(
                    "La data protocollo di entrata non può essere superiore alla data registrazione del compenso");
        if (getDt_fattura_fornitore() != null && getDt_fattura_fornitore().compareTo(getDt_registrazione()) > 0)
            throw new it.cnr.jada.comp.ApplicationException(
                    "La data fattura fornitore non può essere superiore alla data registrazione del compenso");

        // E' stato richiesta la generazione di una fattura ma è stato
        // selezionato un tipo trattamento non compatibile
        if (getTipoTrattamento() != null)
            if (Boolean.TRUE.equals(getFl_generata_fattura())
                    && Boolean.FALSE.equals(getTipoTrattamento().getFl_registra_fattura()))
                throw new it.cnr.jada.comp.ApplicationException(
                        "Il Tipo Trattamento selezionato non prevede la gestione Genera Fattura");
    }

    public void validate() throws ValidationException {

        if (getDs_compenso() == null)
            throw new ValidationException("Inserire la Descrizione");

        if (getV_terzo() == null)
            throw new ValidationException("Selezionare un Terzo");

        if (Utility.nvl(getIm_netto_da_trattenere()).compareTo(new BigDecimal(0)) < 0) {
            throw new ValidationException("L'importo Netto da sospendere non può essere negativo.");
        }
        if (Utility.nvl(getIm_netto_da_trattenere()).compareTo(new BigDecimal(0)) > 0) {
            if (Utility.nvl(getIm_netto_percipiente()).compareTo(Utility.nvl(getIm_netto_da_trattenere())) < 0) {
                throw new ValidationException(
                        "L'importo Netto da sospendere non può superare l'importo netto da pagare.");
            }
        }
//        if (!(isEstera() ||isSanMarinoConIVA() || isSanMarinoSenzaIVA()) &&
//                (!Optional.ofNullable(getCd_cig()).isPresent() && !Optional.ofNullable(getMotivo_assenza_cig()).isPresent())) {
//            throw new ValidationException("Inserire il CIG o il motivo di assenza dello stesso!");
//        }
    }

    public void validaTerzo() throws it.cnr.jada.comp.ApplicationException {

        // Controllo se il terzo è valido
        if (getTerzo().getDt_fine_rapporto() != null)
            if (getTerzo().getDt_fine_rapporto().compareTo(getDt_registrazione()) < 0)
                throw new it.cnr.jada.comp.ApplicationException("Il terzo selezionato non è valido");

        // Controllo se ho inserito le modalità di pagamento
        if (getModalitaPagamento() == null)
            throw new it.cnr.jada.comp.ApplicationException("Inserire le modalità di pagamento");

        // Controllo se ho inserito il tipo rapporto
        if (getTipoRapporto() == null)
            throw new it.cnr.jada.comp.ApplicationException("Inserire il tipo rapporto");

        // Controllo se ho inserito il tipo trattamento
        if (getTipoTrattamento() == null)
            throw new it.cnr.jada.comp.ApplicationException("Inserire il tipo trattamento");

        // Controllo se ho inserito il tipo prestazione
        /*
         * if (getTipoPrestazioneCompenso() == null &&
         * isPrestazioneCompensoEnabled()) throw new
         * it.cnr.jada.comp.ApplicationException( "Inserire il tipo prestazione"
         * );
         */
    }

    public void validaTestata() throws it.cnr.jada.comp.ApplicationException,
            it.cnr.jada.action.BusinessProcessException, javax.ejb.EJBException, java.text.ParseException {
        // Validazione Date
        validaDate();
        if (getMissione() != null && getMissione().getDataInizioObbligoRegistroUnico() != null) {
            setDataInizioObbligoRegistroUnico(getMissione().getDataInizioObbligoRegistroUnico());
        }
        if (dataInizioObbligoRegistroUnico != null && (getDt_registrazione().after(dataInizioObbligoRegistroUnico))) {
            if (getStato_liquidazione() == null)
                throw new ApplicationException("Inserire lo stato della liquidazione!");
            if (getStato_liquidazione() != null && getStato_liquidazione().compareTo(this.LIQ) != 0
                    && getCausale() == null)
                throw new ApplicationException("Inserire la causale.");
        }

        // Validazione Descrizione
        if (getDs_compenso() == null)
            throw new it.cnr.jada.comp.ApplicationException("Inserire la Descrizione");
    }

    /**
     * @return
     */
    public Codici_rapporti_inpsBulk getCodici_rapporti_inps() {
        return this.codici_rapporti_inps;
    }

    /**
     * @param bulk
     */
    public void setCodici_rapporti_inps(Codici_rapporti_inpsBulk bulk) {
        codici_rapporti_inps = bulk;
    }

    /**
     * @return
     */

    public java.lang.String getCd_rapporto_inps() {
        if (getCodici_rapporti_inps() == null)
            return null;
        return this.getCodici_rapporti_inps().getCd_rapporto_inps();
    }

    /**
     * @param string
     */

    public void setCd_rapporto_inps(java.lang.String string) {
        this.getCodici_rapporti_inps().setCd_rapporto_inps(string);
    }

    /**
     * @return
     */
    public Codici_attivita_inpsBulk getCodici_attivita_inps() {
        return codici_attivita_inps;
    }

    /**
     * @param bulk
     */
    public void setCodici_attivita_inps(Codici_attivita_inpsBulk bulk) {
        codici_attivita_inps = bulk;
    }

    /**
     * @return
     */
    public java.lang.String getCd_attivita_inps() {
        if (getCodici_attivita_inps() == null)
            return null;
        return this.getCodici_attivita_inps().getCd_attivita_inps();
    }

    /**
     * @param string
     */
    public void setCd_attivita_inps(java.lang.String string) {
        this.getCodici_attivita_inps().setCd_attivita_inps(string);
    }

    /**
     * @return
     */
    public Codici_altra_forma_ass_inpsBulk getCodici_altra_forma_ass_inps() {
        return codici_altra_forma_ass_inps;
    }

    /**
     * @param bulk
     */
    public void setCodici_altra_forma_ass_inps(Codici_altra_forma_ass_inpsBulk bulk) {
        codici_altra_forma_ass_inps = bulk;
    }

    /**
     * @return
     */
    public java.lang.String getCd_altra_ass_inps() {
        if (getCodici_altra_forma_ass_inps() == null)
            return null;
        return this.getCodici_altra_forma_ass_inps().getAltra_ass_previd_inps();
    }

    /**
     * @param string
     */
    public void setCd_altra_ass_inps(java.lang.String string) {
        this.getCodici_altra_forma_ass_inps().setAltra_ass_previd_inps(string);
    }

    public it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk getComune_inps() {
        return comune_inps;
    }

    public void setComune_inps(it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk newComune_inps) {
        comune_inps = newComune_inps;
    }

    public java.lang.Long getPg_comune_inps() {
        it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk comune_inps = this.getComune_inps();
        if (comune_inps == null)
            return null;
        return comune_inps.getPg_comune();
    }

    public void setPg_comune_inps(java.lang.Long pg_comune_inps) {
        this.getComune_inps().setPg_comune(pg_comune_inps);
    }

    public boolean isROds_comune_inps() {
        return comune_inps == null || comune_inps.getCrudStatus() == OggettoBulk.NORMAL;
    }

    public Incarichi_repertorio_annoBulk getIncarichi_repertorio_anno() {
        return incarichi_repertorio_anno;
    }

    public void setIncarichi_repertorio_anno(Incarichi_repertorio_annoBulk incarichi_repertorio_anno) {
        this.incarichi_repertorio_anno = incarichi_repertorio_anno;
    }

    public java.lang.Integer getEsercizio_rep() {
        if (getIncarichi_repertorio_anno() == null)
            return null;
        return getIncarichi_repertorio_anno().getEsercizio();
    }

    public void setEsercizio_rep(java.lang.Integer esercizio_rep) {
        this.getIncarichi_repertorio_anno().setEsercizio(esercizio_rep);
    }

    public java.lang.Long getPg_repertorio() {
        if (getIncarichi_repertorio_anno() == null)
            return null;
        return getIncarichi_repertorio_anno().getPg_repertorio();
    }

    public void setPg_repertorio(java.lang.Long pg_repertorio) {
        this.getIncarichi_repertorio_anno().getIncarichi_repertorio().setPg_repertorio(pg_repertorio);
    }

    public java.lang.Integer getEsercizio_limite_rep() {
        if (getIncarichi_repertorio_anno() == null)
            return null;
        return this.getIncarichi_repertorio_anno().getEsercizio_limite();
    }

    public void setEsercizio_limite_rep(java.lang.Integer esercizio_limite) {
        this.getIncarichi_repertorio_anno().setEsercizio_limite(esercizio_limite);
    }

    public boolean isIncaricoEnabled() {
        return !this.isDaMissione() && !this.isSenzaCalcoli() && this.getTipoPrestazioneCompenso() != null
                /*
                 * || (this.getTipoTrattamento() != null &&
                 * this.getTipoTrattamento().getFl_incarico() != null && !this
                 * .getTipoTrattamento().getFl_incarico())
                 */
                /*
                 * || (this.getTerzo() != null && this.getTerzo().isStudioAssociato() &&
                 * (this .getTi_prestazione() == null || this
                 * .getTi_prestazione().equals( CompensoBulk.TIPO_PRESTAZIONE_SERVIZI)))
                 */
                && (!this.isPrestazioneCompensoEnabled() || this.getTipoPrestazioneCompenso() == null
                || this.getTipoPrestazioneCompenso().getFl_incarico() == null
                || this.getTipoPrestazioneCompenso().getFl_incarico());
    }

    public boolean isContrattoEnabled() {
        return !this.isDaMissione() && !this.isSenzaCalcoli() && this.getTipoPrestazioneCompenso() != null
                && (!this.isPrestazioneCompensoEnabled() || this.getTipoPrestazioneCompenso() == null
                || this.getTipoPrestazioneCompenso().getFl_contratto() == null
                || this.getTipoPrestazioneCompenso().getFl_contratto());
    }

    public boolean isPrestazioneCompensoEnabled() {
        return !this.isDaMissione() && !this.isSenzaCalcoli()
                && (this.getTipoTrattamento() == null || this.getTipoTrattamento().getFl_tipo_prestazione_obbl() == null
                || this.getTipoTrattamento().getFl_tipo_prestazione_obbl());
    }

    public java.lang.String getIncarichi_oggetto() {
        if (this.getIncarichi_repertorio_anno() == null
                || this.getIncarichi_repertorio_anno().getIncarichi_repertorio() == null
                || this.getIncarichi_repertorio_anno().getIncarichi_repertorio().getIncarichi_procedura() == null)
            return null;
        return this.getIncarichi_repertorio_anno().getIncarichi_repertorio().getIncarichi_procedura().getOggetto();
        // return incarichi_oggetto;
    }

    public void setIncarichi_oggetto(java.lang.String incarichi_oggetto) {
        this.getIncarichi_repertorio_anno().getIncarichi_repertorio().getIncarichi_procedura()
                .setOggetto(incarichi_oggetto);
        // this.incarichi_oggetto = incarichi_oggetto;
    }

    public java.math.BigDecimal getImporto_complessivo() {
        if (getIncarichi_repertorio_anno() == null)
            return null;
        return this.getIncarichi_repertorio_anno().getImporto_complessivo();
    }

    public void setImporto_complessivo(java.math.BigDecimal importo_complessivo) {
        this.getIncarichi_repertorio_anno().setImporto_complessivo(importo_complessivo);
    }

    public java.math.BigDecimal getImporto_iniziale() {
        if (getIncarichi_repertorio_anno() == null)
            return null;
        return this.getIncarichi_repertorio_anno().getImporto_iniziale();
    }

    public void setImporto_iniziale(java.math.BigDecimal importo_iniziale) {
        this.getIncarichi_repertorio_anno().setImporto_iniziale(importo_iniziale);
    }

    /*
     * public java.math.BigDecimal getImporto_utilizzato() { if
     * (getIncarichi_repertorio_anno() == null) return null; return
     * this.getIncarichi_repertorio_anno().getImporto_utilizzato(); } public
     * void setImporto_utilizzato(java.math.BigDecimal importo_utilizzato) {
     * this
     * .getIncarichi_repertorio_anno().setImporto_utilizzato(importo_utilizzato
     * ); }
     */
    public java.lang.Boolean getRoQuota_esente_inps() {
        return roQuota_esente_inps;
    }

    public void setRoQuota_esente_inps(java.lang.Boolean roQuota_esente_inps) {
        this.roQuota_esente_inps = roQuota_esente_inps;
    }

    public java.math.BigDecimal getImporto_utilizzato() {
        return importo_utilizzato;
    }

    public void setImporto_utilizzato(java.math.BigDecimal importo_utilizzato) {
        this.importo_utilizzato = importo_utilizzato;
    }

    public boolean isROIm_netto_da_trattenere() {
        return (isDaConguaglio() || isDaMissione());
    }

    /*
     * public java.util.Dictionary getTi_prestazioneKeys() { return
     * TIPI_PRESTAZIONE; }
     */
    public BonusBulk getBonus() {
        return bonus;
    }

    public void setBonus(BonusBulk bonus) {
        this.bonus = bonus;
    }

    public java.lang.Integer getEsercizio_bonus() {
        if (getBonus() == null)
            return null;
        return getBonus().getEsercizio();
    }

    public void setEsercizio_bonus(java.lang.Integer esercizio_bonus) {
        this.getBonus().setEsercizio(esercizio_bonus);
    }

    public java.lang.Long getPg_bonus() {
        if (getBonus() == null)
            return null;
        return getBonus().getPg_bonus();
    }

    public void setPg_bonus(java.lang.Long pg_bonus) {
        this.getBonus().setPg_bonus(pg_bonus);
    }

    public Unita_organizzativaBulk getUnitaOrganizzativa() {
        return unitaOrganizzativa;
    }

    public void setUnitaOrganizzativa(Unita_organizzativaBulk unitaOrganizzativa) {
        this.unitaOrganizzativa = unitaOrganizzativa;
    }

    @StoragePolicy(name = "P:strorg:uo", property = @StorageProperty(name = "strorg:descrizione"))
    public String getDsUnitaOrganizzativa() {
        if (getUnitaOrganizzativa() == null)
            return null;
        return getUnitaOrganizzativa().getDs_unita_organizzativa();
    }

    public Tipo_prestazione_compensoBulk getTipoPrestazioneCompenso() {
        return tipoPrestazioneCompenso;
    }

    public void setTipoPrestazioneCompenso(Tipo_prestazione_compensoBulk tipoPrestazioneCompenso) {
        this.tipoPrestazioneCompenso = tipoPrestazioneCompenso;
    }

    public java.lang.String getTi_prestazione() {
        Tipo_prestazione_compensoBulk tipoPrestazioneCompenso = this.getTipoPrestazioneCompenso();
        if (tipoPrestazioneCompenso == null)
            return null;
        return tipoPrestazioneCompenso.getCd_ti_prestazione();
    }

    public void setTi_prestazione(java.lang.String ti_prestazione) {
        this.getTipoPrestazioneCompenso().setCd_ti_prestazione(ti_prestazione);
    }

    public java.util.Collection getTipiPrestazioneCompenso() {
        return tipiPrestazioneCompenso;
    }

    public void setTipiPrestazioneCompenso(java.util.Collection tipiPrestazioneCompenso) {
        this.tipiPrestazioneCompenso = tipiPrestazioneCompenso;
    }

    public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getPignorato() {
        return pignorato;
    }

    public void setPignorato(it.cnr.contab.anagraf00.core.bulk.TerzoBulk pignorato) {
        this.pignorato = pignorato;
    }

    public java.lang.String getDs_pignorato() {
        if (pignorato != null)
            return pignorato.getDenominazione_sede();
        return "";
    }

    public java.lang.Integer getCd_terzo_pignorato() {
        it.cnr.contab.anagraf00.core.bulk.TerzoBulk pignorato = this.getPignorato();
        if (pignorato == null)
            return null;
        return pignorato.getCd_terzo();
    }

    public void setCd_terzo_pignorato(java.lang.Integer cd_terzo_pignorato) {
        this.getPignorato().setCd_terzo(cd_terzo_pignorato);
    }

    public boolean isROPignorato() {
        return pignorato == null || pignorato.getCrudStatus() == NORMAL;
    }

    public boolean isVisualizzaPignorato() {
        return visualizzaPignorato;
    }

    public void setVisualizzaPignorato(boolean visualizzaPignorato) {
        this.visualizzaPignorato = visualizzaPignorato;
    }

    public ContrattoBulk getContratto() {
        return contratto;
    }

    public void setContratto(ContrattoBulk contratto) {
        this.contratto = contratto;
    }

    public java.lang.Integer getEsercizio_contratto() {
        if (getContratto() == null)
            return null;
        return getContratto().getEsercizio();
    }

    public void setEsercizio_contratto(java.lang.Integer esercizio_contratto) {
        this.getContratto().setEsercizio(esercizio_contratto);
    }

    public java.lang.String getStato_contratto() {
        if (getContratto() == null)
            return null;
        return getContratto().getStato();
    }

    public void setStato_contratto(java.lang.String stato_contratto) {
        this.getContratto().setStato(stato_contratto);
    }

    public java.lang.Long getPg_contratto() {
        if (getContratto() == null)
            return null;
        return getContratto().getPg_contratto();
    }

    public void setPg_contratto(java.lang.Long pg_contratto) {
        this.getContratto().setPg_contratto(pg_contratto);
    }

    public java.lang.String getOggetto_contratto() {
        if (this.getContratto() == null)
            return null;
        return this.getContratto().getOggetto();
    }

    public void setOggetto_contratto(java.lang.String oggetto_contratto) {
        this.oggetto_contratto = oggetto_contratto;
        // this.getContratto().setOggetto(oggetto_contratto);
    }

    public TrovatoBulk getTrovato() {
        return trovato;
    }

    public void setTrovato(TrovatoBulk trovato) {
        this.trovato = trovato;
    }

    public java.lang.Long getPg_trovato() {
        if (this.getTrovato() == null)
            return null;
        return this.getTrovato().getPg_trovato();
    }

    public void setPg_trovato(java.lang.Long pg_trovato) {
        if (this.getTrovato() != null)
            this.getTrovato().setPg_trovato(pg_trovato);
    }

    public Boolean isCollegatoCapitoloPerTrovato() {
        // return collegatoCapitoloPerTrovato;
        if (getObbligazioneScadenzario() == null || getObbligazioneScadenzario().getObbligazione() == null)
            return false;
        return getObbligazioneScadenzario().getObbligazione().getElemento_voce().isVocePerTrovati();
    }

    public java.sql.Timestamp getDataInizioObbligoRegistroUnico() {
        return dataInizioObbligoRegistroUnico;
    }

    public void setDataInizioObbligoRegistroUnico(java.sql.Timestamp dataInizioObbligoRegistroUnico) {
        this.dataInizioObbligoRegistroUnico = dataInizioObbligoRegistroUnico;
    }

    public Dictionary getStato_liquidazioneKeys() {
        return STATO_LIQUIDAZIONE;
    }

    public Dictionary getCausaleKeys() {
        return CAUSALE;
    }

    public java.sql.Timestamp getDataInizioFatturaElettronica() {
        return dataInizioFatturaElettronica;
    }

    public void setDataInizioFatturaElettronica(java.sql.Timestamp dataInizioFatturaElettronica) {
        this.dataInizioFatturaElettronica = dataInizioFatturaElettronica;
    }

    public boolean isGestione_doc_ele() {
        if (this.getDt_registrazione() != null && this.getDataInizioFatturaElettronica() != null) {
            return this.getDt_registrazione().compareTo(this.getDataInizioFatturaElettronica()) >= 0;
        }
        return true; // non dovrebbe mai verificarsi
    }

    public it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk getVoceIvaFattura() {
        return voceIvaFattura;
    }

    public void setVoceIvaFattura(it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk voceIvaFattura) {
        this.voceIvaFattura = voceIvaFattura;
    }

    public Fattura_passivaBulk getFatturaPassiva() {
        return fatturaPassiva;
    }

    public void setFatturaPassiva(Fattura_passivaBulk fatturaPassiva) {
        this.fatturaPassiva = fatturaPassiva;
    }

    public boolean isElettronica() {
        return getFl_documento_ele() != null && getFl_documento_ele();
    }

    public boolean isTrattamentoSoloEnte() {
        return trattamentoSoloEnte;
    }

    public void setTrattamentoSoloEnte(boolean trattamentoSoloEnte) {
        this.trattamentoSoloEnte = trattamentoSoloEnte;
    }

    public void impostaVoceIva(Fattura_passiva_IBulk fp) {

        for (java.util.Iterator i = fp.getFattura_passiva_dettColl().iterator(); i.hasNext(); ) {
            Fattura_passiva_rigaIBulk riga = (Fattura_passiva_rigaIBulk) i.next();

            if (riga.getVoce_iva() != null && riga.getVoce_iva().getPercentuale().compareTo(new BigDecimal(0)) != 0) {
                setVoceIva(riga.getVoce_iva());
                setVoceIvaFattura(riga.getVoce_iva());
            }
        }

        if (getVoceIva() == null) {
            for (java.util.Iterator i = fp.getFattura_passiva_dettColl().iterator(); i.hasNext(); ) {
                Fattura_passiva_rigaIBulk riga = (Fattura_passiva_rigaIBulk) i.next();

                if (riga.getVoce_iva() != null) {
                    setVoceIva(riga.getVoce_iva());
                    setVoceIvaFattura(riga.getVoce_iva());
                }
            }
        }
    }

    public boolean isCompensoSoloInailEnte() {
        return isCompensoSoloInailEnte;
    }

    public void setCompensoSoloInailEnte(boolean isCompensoSoloInailEnte) {
        this.isCompensoSoloInailEnte = isCompensoSoloInailEnte;
    }

    public java.util.List getMandatiRigaAssociati() {
        return mandatiRigaAssociati;
    }

    public void setMandatiRigaAssociati(java.util.List mandatiRigaAssociati) {
        this.mandatiRigaAssociati = mandatiRigaAssociati;
    }

    public boolean isUserAbilitatoSenzaCalcolo() {
        return userAbilitatoSenzaCalcolo;
    }

    public void setUserAbilitatoSenzaCalcolo(boolean b) {
        this.userAbilitatoSenzaCalcolo = b;

    }

    public CigBulk getCig() {
        return cig;
    }

    public void setCig(CigBulk cig) {
        this.cig = cig;
    }

    @Override
    public List<String> getStorePath() {
        return Optional.ofNullable(missione)
                .map(MissioneBulk::getStorePath)
                .orElse(Collections.emptyList());
    }

    @Override
    public String getCd_tipo_doc() {
        return this.getCd_tipo_doc_amm();
    }

    @Override
    public Long getPg_doc() {
        return this.getPg_doc_amm();
    }

    public TipoDocumentoEnum getTipoDocumentoEnum() {
        return TipoDocumentoEnum.fromValue(this.getCd_tipo_doc_amm());
    }

    @Override
    public Timestamp getDt_contabilizzazione() {
        return this.getDt_registrazione();
    }

    public boolean isIstituzionale() {
        return TipoIVA.ISTITUZIONALE.value().equals(this.getTi_istituz_commerc());
    }

    public boolean isCommerciale() {
        return TipoIVA.COMMERCIALE.value().equals(this.getTi_istituz_commerc());
    }

    /**
     * Ritorna sempre valore null in quanto campo valido solo per liquidazioni
     */
    @Override
    public Timestamp getDtInizioLiquid() {
        return null;
    }

    /**
     * Ritorna sempre valore null in quanto campo valido solo per liquidazioni
     */
    @Override
    public Timestamp getDtFineLiquid() {
        return null;
    }

    /**
     * Ritorna sempre valore null in quanto campo valido solo per liquidazioni
     */
    @Override
    public String getTipoLiquid() {
        return null;
    }

    /**
     * Ritorna sempre valore null in quanto campo valido solo per liquidazioni
     */
    @Override
    public Long getReportIdLiquid() {
        return null;
    }
}
