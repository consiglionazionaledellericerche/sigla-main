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

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.coepcoan00.core.bulk.Scrittura_partita_doppiaBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.docamm00.docs.bulk.TipoDocumentoEnum;
import it.cnr.contab.util.RemoveAccent;
import it.cnr.contab.util.Utility;
import it.cnr.contab.util.enumeration.EsitoOperazione;
import it.cnr.contab.util.enumeration.StatoVariazioneSostituzione;
import it.cnr.jada.bulk.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

public class MandatoBulk extends MandatoBase implements IManRevBulk, IDefferUpdateSaldi {
    public final static String STATO_MANDATO_ANNULLATO = "A";
    public final static String STATO_MANDATO_EMESSO = "E";
    public final static String STATO_MANDATO_PAGATO = "P";
    public final static String STATO_COGE_N = "N";
    public final static String STATO_COGE_C = "C";
    public final static String STATO_COGE_R = "R";
    public final static String STATO_COGE_X = "X";
    public final static Dictionary statoKeys;
    public final static String STATO_TRASMISSIONE_NON_INSERITO = "N";
    public final static String STATO_TRASMISSIONE_INSERITO = "I";
    public final static String STATO_TRASMISSIONE_TRASMESSO = "T";
    //public final static String STATO_TRASMISSIONE_DA_FIRMARE	= "D";
    public final static String STATO_TRASMISSIONE_PREDISPOSTO = "P";
    public final static String STATO_TRASMISSIONE_PRIMA_FIRMA = "F";
    public final static Dictionary stato_trasmissioneKeys;
    public final static String TIPO_COMPETENZA = "C";
    public final static String TIPO_RESIDUO = "R";
    public final static Dictionary competenzaResiduoKeys;

    public final static String TIPO_ACCREDITAMENTO = "A";
    public final static String TIPO_REGOLARIZZAZIONE = "R";
    public final static String TIPO_REGOLAM_SOSPESO = "S";
    public final static String TIPO_PAGAMENTO = "P";
    public final static java.util.Dictionary tipoMandatoCNRKeys;
    public final static java.util.Dictionary tipoMandatoCdSKeys;

    public final static  Map<String,String> esito_OperazioneKeys = EsitoOperazione.KEYS;
    public final static  Map<String,String> statoVariazioneSostituzioneKeys = StatoVariazioneSostituzione.KEYS;

    protected final static java.util.Dictionary classeDiPagamentoKeys = it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk.TI_PAGAMENTO_KEYS;
    public static final String BOEST_MAX_LENGTH_DESCRIPTION = "boest.max.length.description";
    private it.cnr.jada.bulk.PrimaryKeyHashMap deferredSaldi = new PrimaryKeyHashMap();

    private String cdUoScrivania;

    static {
        statoKeys = new it.cnr.jada.util.OrderedHashtable();
        statoKeys.put(STATO_MANDATO_ANNULLATO, "Annullato");
        statoKeys.put(STATO_MANDATO_EMESSO, "Emesso");
        statoKeys.put(STATO_MANDATO_PAGATO, "Pagato");
    }

    static {
        stato_trasmissioneKeys = new it.cnr.jada.util.OrderedHashtable();
        stato_trasmissioneKeys.put(STATO_TRASMISSIONE_NON_INSERITO, "Non inserito in distinta");
        stato_trasmissioneKeys.put(STATO_TRASMISSIONE_INSERITO, "Inserito in distinta");
        stato_trasmissioneKeys.put(STATO_TRASMISSIONE_PREDISPOSTO, "Predisposto alla Firma");
        stato_trasmissioneKeys.put(STATO_TRASMISSIONE_PRIMA_FIRMA, "Prima Firma");
        stato_trasmissioneKeys.put(STATO_TRASMISSIONE_TRASMESSO, "Trasmesso");

    }

    static {
        competenzaResiduoKeys = new it.cnr.jada.util.OrderedHashtable();
        competenzaResiduoKeys.put(TIPO_COMPETENZA, "Competenza");
        competenzaResiduoKeys.put(TIPO_RESIDUO, "Residuo");
    }

    ;

    static {
        tipoMandatoCNRKeys = new it.cnr.jada.util.OrderedHashtable();
        tipoMandatoCNRKeys.put(TIPO_PAGAMENTO, "Pagamento");
        tipoMandatoCNRKeys.put(TIPO_REGOLAM_SOSPESO, "Regolamento sospeso");
//		tipoMandatoCNRKeys.put(TIPO_PAGAMENTO_ESTERO, 	"Pagamento estero");
//		tipoMandatoCNRKeys.put(TIPO_REGOLARIZZAZIONE, 	"Regolarizzazione");
    }

    static {
        tipoMandatoCdSKeys = new it.cnr.jada.util.OrderedHashtable();
        tipoMandatoCdSKeys.put(TIPO_PAGAMENTO, "Pagamento");
        tipoMandatoCdSKeys.put(TIPO_REGOLAM_SOSPESO, "Regolamento sospeso");
//		tipoMandatoCdSKeys.put(TIPO_PAGAMENTO_ESTERO, 	"Pagamento estero");
    }

    protected BulkList mandato_rigaColl = new BulkList();
    protected Mandato_terzoBulk mandato_terzo;
    protected BulkList sospeso_det_uscColl = new BulkList();
    protected BulkList reversaliColl = new BulkList(); //utilizzato solo da man.regolarizzazione e accreditamento
    protected Collection doc_contabili_collColl; //documenti contabili collegati
    protected it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa;
    protected it.cnr.contab.config00.sto.bulk.CdsBulk cds = new it.cnr.contab.config00.sto.bulk.CdsBulk();
    protected String cd_uo_ente;
    protected List reversaliAssociate = new it.cnr.jada.util.Collect(reversaliColl, "reversale");
    protected List reversaliDisponibili = new Vector();
    protected it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk v_man_rev = new it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk();
    private List unita_organizzativaOptions;
    private java.util.Dictionary tipoDocumentoKeys;
    private java.util.Dictionary tipoDocumentoPerRicercaKeys;
    private java.math.BigDecimal im_disp_cassa_cds;
    private java.math.BigDecimal im_disp_cassa_CNR;

    private Scrittura_partita_doppiaBulk scrittura_partita_doppia;

    public MandatoBulk() {
        super();
    }

    public MandatoBulk(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.Long pg_mandato) {
        super(cd_cds, esercizio, pg_mandato);
        setCds(new it.cnr.contab.config00.sto.bulk.CdsBulk(cd_cds));

    }

    /**
     * Metodo per l'aggiunta di un elemento <code>Sospeso_det_uscBulk</code> alla <code>Collection</code>
     * dei sospesi del mandato.
     *
     * @param sospeso Il sospeso.
     * @return sdu Il sospeso da aggiungere.
     */
    public Sospeso_det_uscBulk addToSospeso_det_uscColl(SospesoBulk sospeso) {
        Sospeso_det_uscBulk sdu = null, tmp;
        //verifico che il sospeso esiste già con stato annullato
        for (Iterator i = getSospeso_det_uscColl().deleteIterator(); i.hasNext(); ) {
            tmp = (Sospeso_det_uscBulk) i.next();
            if (tmp.getCd_sospeso().equals(sospeso.getCd_sospeso())) {
                sdu = tmp;
                break;
            }
        }
        if (sdu == null) //il sospeso non esiste
            sdu = new Sospeso_det_uscBulk();
        else
            sdu.setToBeUpdated();    // il sospeso esiste già
        sdu.setMandato(this);
        sdu.setSospeso(sospeso);
        BigDecimal im_mandato = BigDecimal.ZERO;
        if (getIm_mandato() != null && getIm_ritenute() != null)
            im_mandato = getIm_mandato().subtract(getIm_ritenute());
        if (im_mandato.compareTo(sospeso.getIm_disponibile()) < 0)
            sdu.setIm_associato(im_mandato);
        else
            sdu.setIm_associato(sospeso.getIm_disponibile());
        sdu.setStato(sdu.STATO_DEFAULT);
        this.sospeso_det_uscColl.add(sdu);
        return sdu;
    }

    /**
     * Aggiunge un nuovo dettaglio (Mandato_rigaBulk) alla lista di dettagli definiti per il mandato
     * inizializzandone alcuni campi
     *
     * @param riga dettaglio da aggiungere alla lista
     * @return int
     */
    public int addToMandato_rigaColl(Mandato_rigaBulk riga) {
        mandato_rigaColl.add(riga);
        return mandato_rigaColl.size() - 1;
    }

    /**
     * Annulla il mandato e cancella eventuali sospesi associati al mandato.
     */
    public void annulla() {
        Iterator i;
        Sospeso_det_uscBulk sospeso;

        setStato(STATO_MANDATO_ANNULLATO);
//	setIm_mandato(new java.math.BigDecimal(0));
        setToBeUpdated();
        //annulla i sospesi
        for (i = getSospeso_det_uscColl().iterator(); i.hasNext(); ) {
            // ((Sospeso_det_uscBulk) i.next()).setToBeDeleted();
            sospeso = ((Sospeso_det_uscBulk) i.next());
            sospeso.setStato(Sospeso_det_uscBulk.STATO_ANNULLATO);
            sospeso.setToBeUpdated();
        }
	/*
	for (i = getSospeso_det_uscColl().deleteIterator(); i.hasNext(); )
	{
		// ((Sospeso_det_uscBulk) i.next()).setToBeDeleted();
		sospeso = ((Sospeso_det_uscBulk) i.next());
		sospeso.setStato( Sospeso_det_uscBulk.STATO_ANNULLATO );
		sospeso.setToBeUpdated();
	}
	*/
    }

    /**
     * Restituisce un array di <code>BulkCollection</code> contenenti oggetti
     * bulk da rendere persistenti insieme al ricevente.
     * L'implementazione standard restituisce <code>null</code>.
     *
     */
    public BulkCollection[] getBulkLists() {
        return new it.cnr.jada.bulk.BulkCollection[]{
                mandato_rigaColl, sospeso_det_uscColl};

    }

    /**
     * Restituisce un array di <code>OggettoBulk</code> da rendere persistenti
     * insieme al ricevente.
     * L'implementazione standard restituisce <code>null</code>.
     *
     */
    public OggettoBulk[] getBulksForPersistentcy() {
        return new OggettoBulk[]{
                mandato_terzo};

    }

    public java.lang.String getCd_cds() {
        it.cnr.contab.config00.sto.bulk.CdsBulk cds = this.getCds();
        if (cds == null)
            return null;
        return cds.getCd_unita_organizzativa();
    }

    public void setCd_cds(java.lang.String cd_cds) {
        this.getCds().setCd_unita_organizzativa(cd_cds);
    }

    public java.lang.String getCd_unita_organizzativa() {
        it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = this.getUnita_organizzativa();
        if (unita_organizzativa == null)
            return null;
        return unita_organizzativa.getCd_unita_organizzativa();
    }

    public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
        this.getUnita_organizzativa().setCd_unita_organizzativa(cd_unita_organizzativa);
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'cd_uo_ente'
     *
     * @return Il valore della proprietà 'cd_uo_ente'
     */
    public java.lang.String getCd_uo_ente() {
        return cd_uo_ente;
    }

    /**
     * <!-- @TODO: da completare -->
     * Imposta il valore della proprietà 'cd_uo_ente'
     *
     * @param newCd_uo_ente Il valore da assegnare a 'cd_uo_ente'
     */
    public void setCd_uo_ente(java.lang.String newCd_uo_ente) {
        cd_uo_ente = newCd_uo_ente;
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'cds'
     *
     * @return Il valore della proprietà 'cds'
     */
    public it.cnr.contab.config00.sto.bulk.CdsBulk getCds() {
        return cds;
    }

    /**
     * @param newCds it.cnr.contab.config00.sto.bulk.CdsBulk
     */
    public void setCds(it.cnr.contab.config00.sto.bulk.CdsBulk newCds) {
        cds = newCds;
    }

    /**
     * Metodo con cui si ottiene il valore della variabile <code>classeDiPagamentoKeys</code>
     * di tipo <code>Hashtable</code>.
     * In particolare, questo metodo carica in una Hashtable l'elenco dei possibili valori
     * che puè assumere il campo <code>ti_pagamento</code>.
     *
     * @return java.util.Hashtable classeDiPagamentoKeys I valori del campo <code>ti_pagamento</code>.
     */
    public java.util.Dictionary getClasseDiPagamentoKeys() {
        return classeDiPagamentoKeys;
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'competenzaResiduoKeys'
     *
     * @return Il valore della proprietà 'competenzaResiduoKeys'
     */
    public java.util.Dictionary getCompetenzaResiduoKeys() {
        return competenzaResiduoKeys;
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'doc_contabili_collColl'
     *
     * @return Il valore della proprietà 'doc_contabili_collColl'
     */
    public java.util.Collection getDoc_contabili_collColl() {
        return doc_contabili_collColl;
    }

    /**
     * <!-- @TODO: da completare -->
     * Imposta il valore della proprietà 'doc_contabili_collColl'
     *
     * @param newDoc_contabili_collColl Il valore da assegnare a 'doc_contabili_collColl'
     */
    public void setDoc_contabili_collColl(java.util.Collection newDoc_contabili_collColl) {
        doc_contabili_collColl = newDoc_contabili_collColl;
    }

    /**
     * Insert the method's description here.
     * Creation date: (27/11/2002 15.32.36)
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getIm_disp_cassa() {
        if (getCd_uo_ente() != null && getCd_unita_organizzativa() != null)
            if (getCd_uo_ente().equals(getCd_unita_organizzativa()))
                return im_disp_cassa_CNR;
            else
                return im_disp_cassa_cds;
        return null;
    }

    /**
     * Insert the method's description here.
     * Creation date: (27/11/2002 15.32.36)
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getIm_disp_cassa_cds() {
        return im_disp_cassa_cds;
    }

    /**
     * Insert the method's description here.
     * Creation date: (27/11/2002 15.32.36)
     *
     * @param newIm_disp_cassa_cds java.math.BigDecimal
     */
    public void setIm_disp_cassa_cds(java.math.BigDecimal newIm_disp_cassa_cds) {
        im_disp_cassa_cds = newIm_disp_cassa_cds;
    }

    /**
     * Insert the method's description here.
     * Creation date: (27/11/2002 15.32.36)
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getIm_disp_cassa_CNR() {
        return im_disp_cassa_CNR;
    }

    /**
     * Insert the method's description here.
     * Creation date: (27/11/2002 15.32.36)
     *
     * @param newIm_disp_cassa_CNR java.math.BigDecimal
     */
    public void setIm_disp_cassa_CNR(java.math.BigDecimal newIm_disp_cassa_CNR) {
        im_disp_cassa_CNR = newIm_disp_cassa_CNR;
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'im_netto'
     *
     * @return Il valore della proprietà 'im_netto'
     */
    public java.math.BigDecimal getIm_netto() {
        if (getIm_mandato() == null)
            return new BigDecimal(0);
        else if (getIm_ritenute() == null)
            return new BigDecimal(0);
        else
            return getIm_mandato().subtract(getIm_ritenute());
    }

    /**
     * Metodo per calcolare l'importo totale dei sospesi (di spesa) associati
     * al mandato.
     *
     * @return totSospesi <code>BigDecimal</code> l'importo totale calcolato dei sospesi
     */
    public java.math.BigDecimal getImTotaleSospesi() {
        java.math.BigDecimal totSospesi = new java.math.BigDecimal(0);
        Sospeso_det_uscBulk sospeso;
        for (Iterator i = getSospeso_det_uscColl().iterator(); i.hasNext(); ) {
            sospeso = ((Sospeso_det_uscBulk) i.next());
            totSospesi = totSospesi.add(sospeso.getIm_associato());
        }
        return totSospesi;

    }

    /**
     * @return it.cnr.jada.bulk.BulkList
     */
    public it.cnr.jada.bulk.BulkList<Mandato_rigaBulk> getMandato_rigaColl() {
        return mandato_rigaColl;
    }

    /**
     * @param newMandato_rigaColl it.cnr.jada.bulk.BulkList
     */
    public void setMandato_rigaColl(it.cnr.jada.bulk.BulkList newMandato_rigaColl) {
        mandato_rigaColl = newMandato_rigaColl;
    }

    /**
     * @return it.cnr.contab.doccont00.core.bulk.Mandato_terzoBulk
     */
    public Mandato_terzoBulk getMandato_terzo() {
        return mandato_terzo;
    }

    /**
     * @param newMandato_terzo it.cnr.contab.doccont00.core.bulk.Mandato_terzoBulk
     */
    public void setMandato_terzo(Mandato_terzoBulk newMandato_terzo) {
        mandato_terzo = newMandato_terzo;
    }

    public Long getPg_documento_cont() {
        return getPg_mandato();
    }

    /**
     * Insert the method's description here.
     * Creation date: (21/11/2002 10.23.06)
     *
     * @return it.cnr.jada.bulk.BulkList
     */
    public List getReversaliAssociate() {
        return reversaliAssociate;
    }

    /**
     * Insert the method's description here.
     * Creation date: (21/11/2002 10.23.06)
     *
     * @param newReversaliAssociate it.cnr.jada.bulk.BulkList
     */
    public void setReversaliAssociate(List newReversaliAssociate) {
        reversaliAssociate = newReversaliAssociate;
    }

    /**
     * @return it.cnr.jada.bulk.BulkList
     */
    public it.cnr.jada.bulk.BulkList getReversaliColl() {
        return reversaliColl;
    }

    /**
     * @param newReversaliColl it.cnr.jada.bulk.BulkList
     */
    public void setReversaliColl(it.cnr.jada.bulk.BulkList newReversaliColl) {
        ((it.cnr.jada.util.Collect) reversaliAssociate).setList(reversaliColl = newReversaliColl);
    }

    /**
     * Insert the method's description here.
     * Creation date: (21/11/2002 10.23.06)
     *
     * @return java.util.List
     */
    public java.util.List getReversaliDisponibili() {
        return reversaliDisponibili;
    }

    /**
     * Insert the method's description here.
     * Creation date: (21/11/2002 10.23.06)
     *
     * @param newReversaliDisponibili java.util.List
     */
    public void setReversaliDisponibili(java.util.List newReversaliDisponibili) {
        reversaliDisponibili = newReversaliDisponibili;
    }

    /**
     * @return it.cnr.jada.bulk.BulkList
     */
    public it.cnr.jada.bulk.BulkList getSospeso_det_uscColl() {
        return sospeso_det_uscColl;
    }

    /**
     * @param newSospeso_det_uscColl it.cnr.jada.bulk.BulkList
     */
    public void setSospeso_det_uscColl(it.cnr.jada.bulk.BulkList newSospeso_det_uscColl) {
        sospeso_det_uscColl = newSospeso_det_uscColl;
    }

    /**
     * @return java.util.Dictionary
     */
    public java.util.Dictionary getStatoKeys() {
        return statoKeys;
    }

    public TerzoBulk getTerzo_cedente() {
        if (getMandato_rigaColl() != null && getMandato_rigaColl().size() > 0)
            return ((Mandato_rigaBulk) getMandato_rigaColl().get(0)).getTerzo_cedente();
        return null;

    }

    /**
     * Metodo con cui si ottiene il valore della variabile <code>tipoDocumentoKeys</code>
     * di tipo <code>Hashtable</code>.
     * In particolare, questo metodo carica in una Hashtable l'elenco dei possibili valori
     * che puè assumere il campo <code>cd_tipo_documento_amm</code>.
     *
     * @return java.util.Hashtable tipoDocumentoKeys I valori del campo <code>cd_tipo_documento_amm</code>.
     */
    public java.util.Dictionary getTipoDocumentoKeys() {
        return tipoDocumentoKeys;

    }

    /**
     * @param newTipoDocumentoKeys java.util.Dictionary
     */
    public void setTipoDocumentoKeys(java.util.Dictionary newTipoDocumentoKeys) {
        tipoDocumentoKeys = newTipoDocumentoKeys;
    }

    /**
     * @return java.util.Dictionary
     */
    public java.util.Dictionary getTipoDocumentoPerRicercaKeys() {
        return tipoDocumentoPerRicercaKeys;
    }

    /**
     * @param newTipoDocumentoPerRicercaKeys java.util.Dictionary
     */
    public void setTipoDocumentoPerRicercaKeys(java.util.Dictionary newTipoDocumentoPerRicercaKeys) {
        tipoDocumentoPerRicercaKeys = newTipoDocumentoPerRicercaKeys;
    }

    /**
     * Metodo con cui si ottiene il valore della variabile <code>tipoMandatoKeys</code>
     * di tipo <code>Hashtable</code>.
     * In particolare, questo metodo carica in una Hashtable l'elenco dei possibili valori
     * che puè assumere il campo <code>ti_mandato</code>.
     *
     * @return java.util.Dictionary tipoMandatoKeys I valori del campo <code>ti_mandato</code>.
     */
    public java.util.Dictionary getTipoMandatoKeys() {
        if (TIPO_REGOLARIZZAZIONE.equals(getTi_mandato())) {
            Hashtable ht = new Hashtable();
            ht.put(TIPO_REGOLARIZZAZIONE, "Regolarizzazione");
            return ht;
        }
        if (getUnita_organizzativa() == null || getUnita_organizzativa().getCd_unita_organizzativa() == null)
            return tipoMandatoCdSKeys;
        else if (getUnita_organizzativa().getCd_unita_organizzativa().equals(cd_uo_ente))
            return tipoMandatoCNRKeys;
        return tipoMandatoCdSKeys;

    }

    /**
     * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
     */
    public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUnita_organizzativa() {
        return unita_organizzativa;
    }

    /**
     * @param newUnita_organizzativa it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
     */
    public void setUnita_organizzativa(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUnita_organizzativa) {
        unita_organizzativa = newUnita_organizzativa;
    }

    /**
     * @return java.util.List
     */
    public java.util.List getUnita_organizzativaOptions() {
        return unita_organizzativaOptions;
    }

    /**
     * @param newUnita_organizzativaOptions java.util.List
     */
    public void setUnita_organizzativaOptions(java.util.List newUnita_organizzativaOptions) {
        unita_organizzativaOptions = newUnita_organizzativaOptions;
    }

    /**
     * Inizializza l'Oggetto Bulk per la ricerca libera.
     *
     * @param bp      Il Business Process in uso
     * @param context Il contesto dell'azione
     * @return OggettoBulk L'oggetto bulk inizializzato
     */
    public OggettoBulk initializeForFreeSearch(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) {
        super.initializeForSearch(bp, context);

        setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
        // setCd_uo_origine( it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_unita_organizzativa());

        if (bp.getName().equals("CRUDMandatoRegolarizzazioneBP"))
            setTi_mandato(TIPO_REGOLARIZZAZIONE);
        return this;
    }

    /**
     * Inizializza l'Oggetto Bulk per l'inserimento.
     *
     * @param bp      Il Business Process in uso
     * @param context Il contesto dell'azione
     * @return OggettoBulk L'oggetto bulk inizializzato
     */
    public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) {
        super.initializeForInsert(bp, context);
        setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
        unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
        setCd_cds(unita_organizzativa.getUnita_padre().getCd_unita_organizzativa());
        setCd_cds_origine(unita_organizzativa.getUnita_padre().getCd_unita_organizzativa());
        setCd_uo_origine(unita_organizzativa.getCd_unita_organizzativa());

        setStato(STATO_MANDATO_EMESSO);
        setStato_trasmissione(STATO_TRASMISSIONE_NON_INSERITO);
        setTi_competenza_residuo(TIPO_COMPETENZA);
        if (bp.getName().equals("CRUDMandatoRegolarizzazioneBP"))
            setTi_mandato(TIPO_REGOLARIZZAZIONE);
        else
            setTi_mandato(TIPO_PAGAMENTO);
        setIm_pagato(new java.math.BigDecimal(0));
        setIm_mandato(new java.math.BigDecimal(0));
        setCd_tipo_documento_cont(Numerazione_doc_contBulk.TIPO_MAN);
        setStato_coge(STATO_COGE_N);
        setIm_ritenute(new java.math.BigDecimal(0));

        return this;
    }

    /**
     * Inizializza l'Oggetto Bulk per la ricerca.
     *
     * @param bp      Il Business Process in uso
     * @param context Il contesto dell'azione
     * @return OggettoBulk L'oggetto bulk inizializzato
     */
    public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) {
        super.initializeForSearch(bp, context);
        setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
        // setCd_uo_origine( it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_unita_organizzativa());
        unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
        if (bp.getName().equals("CRUDMandatoRegolarizzazioneBP"))
            setTi_mandato(TIPO_REGOLARIZZAZIONE);
        return this;
    }

    /**
     * Verifica se il mandato ha lo stato "annullato".
     *
     * @return Lo stato del mandato
     * TRUE 	Il mandato è annullato
     * FALSE 	Il mandato non è annullato
     */
    public boolean isAnnullato() {
        return STATO_MANDATO_ANNULLATO.equals(getStato());
    }

    /**
     * Verifica se il mandato e' stato originato da un altro doc. contabile
     *
     * @return TRUE    se il mandato non dipende da un altro doc. contabile
     * FALSE 	altrimenti
     */
    public boolean isDipendenteDaAltroDocContabile() {
        V_ass_doc_contabiliBulk associazione;
        for (Iterator i = getDoc_contabili_collColl().iterator(); i.hasNext(); ) {
            associazione = (V_ass_doc_contabiliBulk) i.next();
            if (associazione.getFl_con_man_prc().booleanValue() &&
                    associazione.getCd_tipo_documento_cont_coll().equals(getCd_tipo_documento_cont()) &&
                    associazione.getCd_cds_coll().equals(getCd_cds()) &&
                    associazione.getEsercizio_coll().equals(getEsercizio()) &&
                    associazione.getPg_documento_cont_coll().equals(getPg_mandato()))
                return true;
        }
        return false;
    }

    /**
     * Verifica se il mandato è di tipo accreditamento.
     *
     * @return FALSE    Il mandato non è di tipo accreditamento
     */
    public boolean isMandatoAccreditamentoBulk() {
        return false;
    }

    /**
     * Verifica se il mandato ha lo stato "pagato".
     *
     * @return Lo stato del mandato
     * TRUE 	Il mandato è pagato
     * FALSE 	Il mandato non è pagato
     */
    public boolean isPagato() {
        return STATO_MANDATO_PAGATO.equals(getStato());
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'rOTi_mandato'
     *
     * @return Il valore della proprietà 'rOTi_mandato'
     */
    public boolean isROTi_mandato() {
        return !this.mandato_rigaColl.isEmpty();
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'rOUnitaOrganizzativa'
     *
     * @return Il valore della proprietà 'rOUnitaOrganizzativa'
     */
    public boolean isROUnitaOrganizzativa() {
        if (TIPO_REGOLARIZZAZIONE.equals(getTi_mandato()))
            return true;
        else
            return !this.mandato_rigaColl.isEmpty() || !this.sospeso_det_uscColl.isEmpty();
//	return true;
    }

    /**
     * <!-- @TODO: da completare -->
     */
    public void refreshImporto() {
        setIm_mandato(new java.math.BigDecimal(0));
        setIm_ritenute(new java.math.BigDecimal(0));

        for (Iterator i = mandato_rigaColl.iterator(); i.hasNext(); ) {
            Mandato_rigaBulk mr = (Mandato_rigaBulk) i.next();
            if (!mr.getStato().equals(mr.STATO_ANNULLATO)) {
                setIm_mandato(getIm_mandato().add(mr.getIm_mandato_riga()));
                setIm_ritenute(getIm_ritenute().add(mr.getIm_ritenute_riga()));
            }
        }

    }

    /**
     * Metodo per l'eliminazione di un elemento <code>Mandato_rigaBulk</code> dalla <code>Collection</code>
     * delle righe del mandato.
     *
     * @param index L'indice per scorrere la collezione di righe del mandato.
     * @return Mandato_rigaBulk La riga da rimuovere.
     */
    public Mandato_rigaBulk removeFromMandato_rigaColl(int index) {
        Mandato_rigaBulk riga = (Mandato_rigaBulk) mandato_rigaColl.remove(index);
//	riga.setToBeDeleted();
//	refreshImporto();
        return riga;
    }

    public ReversaleBulk removeFromReversaliAssociate(int index) {

        Ass_mandato_reversaleBulk assManRev = (Ass_mandato_reversaleBulk) this.reversaliColl.remove(index);
        assManRev.setToBeDeleted();
        this.reversaliDisponibili.add(assManRev.getReversale());
        return assManRev.getReversale();
    }

    public ReversaleBulk removeFromReversaliDisponibili(int index) {

        ReversaleBulk reversale = (ReversaleBulk) this.reversaliDisponibili.remove(index);
        Ass_mandato_reversaleBulk assManRev = new Ass_mandato_reversaleBulk();
        assManRev.setToBeCreated();
        assManRev.setMandato(this);
        assManRev.setReversale(reversale);
        assManRev.setTi_origine(assManRev.TIPO_ORIGINE_SPESA);
        this.reversaliColl.add(assManRev);
        return reversale;
    }

    /**
     * Metodo per l'eliminazione di un elemento <code>Sospeso_det_uscBulk</code> dalla <code>Collection</code>
     * dei sospesi del mandato.
     *
     * @param index L'indice per scorrere la collezione di sospesi del mandato.
     * @return Sospeso_det_uscBulk Il sospeso da rimuovere.
     */
    public Sospeso_det_uscBulk removeFromSospeso_det_uscColl(int index) {
        Sospeso_det_uscBulk sospeso = (Sospeso_det_uscBulk) sospeso_det_uscColl.get(index);
        sospeso.setStato(sospeso.STATO_ANNULLATO);
        if (!sospeso.isToBeCreated())
            sospeso.setCrudStatus(sospeso.TO_BE_UPDATED);
        else
            sospeso.setToBeDeleted();
        sospeso = (Sospeso_det_uscBulk) sospeso_det_uscColl.remove(index);
        return sospeso;
    }

    /**
     * Metodo per validare i sospesi di spesa da associare al mandato.
     *
     * @param sospesi <code>List</code> la lista dei sospesi da validare prima di essere associati al mandato
     */
    public void validaSospesi(List sospesi) throws ValidationException {
        if (sospesi.size() == 0)
            throw new ValidationException("Non sono stati selezionati sospesi da associare al mandato");

        // verifica sulla provenienza dei sospesi (Banca d'Italia o C/C)
        String ti_cc_bi;

        if (getSospeso_det_uscColl().size() > 0)
            ti_cc_bi = ((Sospeso_det_uscBulk) getSospeso_det_uscColl().get(0)).getSospeso().getTi_cc_bi();
        else
            ti_cc_bi = ((SospesoBulk) sospesi.get(0)).getTi_cc_bi();

        for (Iterator i = sospesi.iterator(); i.hasNext(); )
            if (!((SospesoBulk) i.next()).getTi_cc_bi().equals(ti_cc_bi))
                throw new ValidationException("I sospesi associabili al mandato devono avere la stessa provenienza (Banca d'Italia o C/C).");

    }

    /**
     * Metodo con cui si verifica la validità di alcuni campi, mediante un
     * controllo sintattico o contestuale.
     */
    public void validate() throws ValidationException {
        super.validate();
        if (!Optional.ofNullable(getIm_netto())
                .filter(imnetto -> imnetto.compareTo(BigDecimal.ZERO) >= 0)
                .isPresent()) {
            throw new ValidationException("L'importo Netto del Manndato è oggligatorio e non può essere negativo.");
        }
        if (!Optional.ofNullable(getIm_ritenute())
                .filter(imnetto -> imnetto.compareTo(BigDecimal.ZERO) >= 0)
                .isPresent()) {
            throw new ValidationException("L'importo delle rtitenute non può essere negativo.");
        }

        // controllo su campo DATA EMISSIONE
        if (getDt_emissione() == null)
            throw new ValidationException("Il campo DATA CONTABILITA' è obbligatorio.");

        if (getDs_mandato() != null && getDs_mandato().length() != 0) {
            for (int i = 0; i < getDs_mandato().length(); i++) {
                if ((((int) RemoveAccent.convert(getDs_mandato()).charAt(i)) < 31 ||
                        ((int) RemoveAccent.convert(getDs_mandato()).charAt(i)) > 126) &&
                        (int) RemoveAccent.convert(getDs_mandato()).charAt(i) != 13 &&
                        (int) RemoveAccent.convert(getDs_mandato()).charAt(i) != 10)
                    throw new ValidationException("La descrizione contienere caratteri speciali non supportati.");
            }
        }
        final Integer maxLengthDescription = Optional.ofNullable(System.getProperty(BOEST_MAX_LENGTH_DESCRIPTION))
                .map(Integer::valueOf)
                .orElse(140);
        if (getMandato_rigaColl()
                .stream()
                .anyMatch(mandato_rigaBulk -> Optional.ofNullable(mandato_rigaBulk.getModalita_pagamento())
                        .flatMap(modalita_pagamentoBulk -> Optional.ofNullable(modalita_pagamentoBulk.getRif_modalita_pagamento()))
                        .filter(rif_modalita_pagamentoBulk ->
                                Optional.ofNullable(rif_modalita_pagamentoBulk.getTi_pagamento())
                                        .map(s -> s.equalsIgnoreCase(Rif_modalita_pagamentoBulk.IBAN))
                                        .orElse(Boolean.FALSE) &&
                                Optional.ofNullable(rif_modalita_pagamentoBulk.getTipo_pagamento_siope())
                                        .map(s -> s.equalsIgnoreCase(
                                                Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.DISPOSIZIONEDOCUMENTOESTERNO.value())
                                        ).orElse(Boolean.FALSE)
                        ).isPresent()
                ) && getDs_mandato().length() > maxLengthDescription) {
            throw new ValidationException("La descrizione del Mandato non può superare i " + maxLengthDescription + " caratteri!");
        }

        //verifica sui sospesi
        for (Iterator i = getSospeso_det_uscColl().iterator(); i.hasNext(); )
            ((Sospeso_det_uscBulk) i.next()).validate();

    }

    /**
     * Verifica se il mandato è di tipo "accreditamento".
     *
     * @return Il tipo di mandato
     * TRUE 	Il mandato è di tipo "accreditamento".
     * FALSE 	Il mandato è di tipo "accreditamento".
     */
    public boolean isMandatoAccreditamento() {
        return getTi_mandato() != null && getTi_mandato().equals(MandatoBulk.TIPO_ACCREDITAMENTO);
    }

    /**
     * Verifica se il mandato è di tipo "regolarizzazione".
     *
     * @return Il tipo di mandato
     * TRUE 	Il mandato è di tipo "regolarizzazione"
     * FALSE 	Il mandato non è di tipo "regolarizzazione"
     */
    public boolean isMandatoRegolarizzazione() {
        return getTi_mandato() != null && getTi_mandato().equals(MandatoBulk.TIPO_REGOLARIZZAZIONE);
    }

    /**
     * Verifica se il mandato è di tipo "regolamento sospeso".
     *
     * @return Il tipo di mandato
     * TRUE 	Il mandato è di tipo "regolamento sospeso"
     * FALSE 	Il mandato non è di tipo "regolamento sospeso"
     */
    public boolean isRegolamentoSospeso() {
        return getTi_mandato() != null && getTi_mandato().equals(MandatoBulk.TIPO_REGOLAM_SOSPESO);
    }

    /**
     * Verifica se il mandato è di tipo "pagamento".
     *
     * @return Il tipo di mandato
     * TRUE 	Il mandato è di tipo "pagamento"
     * FALSE 	Il mandato non è di tipo "pagamento"
     */
    public boolean isPagamento() {
        return getTi_mandato() != null && getTi_mandato().equals(MandatoBulk.TIPO_PAGAMENTO);
    }

    /**
     * Verifica se il mandato richiede la gestione SIOPE
     *
     * @return l'obbligatorietè della gestione SIOPE
     * TRUE 	Sulle righe di mandato è richiesta la gestione SIOPE
     * FALSE 	Sulle righe di mandato non è richiesta la gestione SIOPE
     * <p>
     * Sui mandati non è obbligatorio caricare i codici SIOPE solo se trattasi di mandati di tipo interno
     * e quindi di Accreditamento emessi dalla UO Ente e di Regolarizzazione
     */
    public boolean isRequiredSiope() {
        return !(this.isMandatoRegolarizzazione());
    }

    /**
     * Verifica se il mandato ha tutte le righe completamente associate a codici SIOPE
     *
     * @return TRUE    Tutte le righe di mandato sono associate a codici SIOPE
     * FALSE 	Alcune righe di mandato non sono associate a codici SIOPE
     */
    public boolean isSiopeTotalmenteAssociato() {
        for (Iterator i = this.getMandato_rigaColl().iterator(); i.hasNext(); ) {
            Mandato_rigaBulk riga = (Mandato_rigaBulk) i.next();
            if (!riga.getTipoAssociazioneSiope().equals(Mandato_rigaBulk.SIOPE_TOTALMENTE_ASSOCIATO))
                return false;
        }
        return true;
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'im_associato_siope'
     *
     * @return Il valore della proprietà 'im_associato_siope'
     */
    public java.math.BigDecimal getIm_associato_siope() {
        BigDecimal totale = Utility.ZERO;
        for (Iterator i = this.getMandato_rigaColl().iterator(); i.hasNext(); )
            totale = totale.add(((Mandato_rigaBulk) i.next()).getIm_associato_siope());
        return totale;
    }

    public BigDecimal getIm_da_associare_siope() {
        return Utility.nvl(getIm_mandato()).subtract(Utility.nvl(getIm_associato_siope()));
    }

    public boolean isDipendenteDaConguaglio() {
        for (Iterator i = this.getMandato_rigaColl().iterator(); i.hasNext(); ) {
            Mandato_rigaBulk riga = (Mandato_rigaBulk) i.next();
            if (riga.getCd_tipo_documento_amm().equals(Numerazione_doc_ammBulk.TIPO_COMPENSO)) {
                CompensoBulk compenso;

            }


            return false;
        }

	/*

	V_ass_doc_contabiliBulk associazione;
	for ( Iterator i = getDoc_contabili_collColl().iterator() ;i.hasNext() ;)
	{
		associazione = (V_ass_doc_contabiliBulk) i.next();
		if ( associazione.getFl_con_man_prc().booleanValue() &&
			  associazione.getCd_tipo_documento_cont_coll().equals( getCd_tipo_documento_cont() ) &&
   		  associazione.getCd_cds_coll().equals( getCd_cds()) &&
			  associazione.getEsercizio_coll().equals( getEsercizio()) &&
			  associazione.getPg_documento_cont_coll().equals( getPg_mandato()))
				 return true;
	}
	return false;
	*/

        return false;
    }

    public boolean isRequiredSospeso() {
        return (this.isRegolamentoSospeso());
    }

    public boolean isSospesoTotalmenteAssociato() {

        BigDecimal imTotSospesi;
        if (((MandatoIBulk) this).isFaiReversale())
            imTotSospesi = this.getIm_mandato().subtract(
                    ((MandatoIBulk) this).getImReversaleDiIncassoIVA());
        else
            imTotSospesi = this.getIm_mandato();
        if (getImTotaleSospesi().compareTo(imTotSospesi) == 0)
            return true;
        else
            return false;
    }

    public it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk getV_man_rev() {
        return v_man_rev;
    }

    public void setV_man_rev(
            it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk v_man_rev) {
        this.v_man_rev = v_man_rev;
    }

    public Long getPg_mandato_riemissione() {
        if (v_man_rev == null)
            return null;
        return v_man_rev.getPg_documento_cont();
    }

    @Override
    public void setPg_mandato_riemissione(Long pg_mandato_riemissione) {
        if (getV_man_rev() != null)
            getV_man_rev().setPg_documento_cont(pg_mandato_riemissione);
    }

    @Override
    public void addToDefferredSaldi(IDocumentoContabileBulk docCont, Map values) {
        Optional.ofNullable(docCont)
                .ifPresent(iDocumentoContabileBulk -> {
                    final PrimaryKeyHashMap primaryKeyHashMap = Optional.ofNullable(deferredSaldi)
                            .orElse(new PrimaryKeyHashMap());
                    if(primaryKeyHashMap.containsKey(iDocumentoContabileBulk)) {
                        primaryKeyHashMap.put(docCont, values);
                    } else {
                        Map firstValues = (Map)primaryKeyHashMap.get(iDocumentoContabileBulk);
                        primaryKeyHashMap.remove(iDocumentoContabileBulk);
                        primaryKeyHashMap.put(iDocumentoContabileBulk, firstValues);
                    }
                });
    }

    @Override
    public PrimaryKeyHashMap getDefferredSaldi() {
        return deferredSaldi;
    }

    @Override
    public IDocumentoContabileBulk getDefferredSaldoFor(IDocumentoContabileBulk docCont) {
        return Optional.ofNullable(docCont)
                .flatMap(iDocumentoContabileBulk -> Optional.ofNullable(deferredSaldi))
                .map(primaryKeyHashMap -> primaryKeyHashMap.get(docCont))
                .filter(IDocumentoContabileBulk.class::isInstance)
                .map(IDocumentoContabileBulk.class::cast)
                .orElse(null);
    }

    @Override
    public void removeFromDefferredSaldi(IDocumentoContabileBulk docCont) {
        Optional.ofNullable(docCont)
                .flatMap(iDocumentoContabileBulk -> Optional.ofNullable(deferredSaldi))
                .filter(primaryKeyHashMap -> primaryKeyHashMap.containsKey(docCont))
                .ifPresent(primaryKeyHashMap -> primaryKeyHashMap.remove(docCont));
    }

    @Override
    public void resetDefferredSaldi() {
        deferredSaldi = null;
    }

    private String getCdUoScrivania() {
        return cdUoScrivania;
    }

    public void setCdUoScrivania(String cdUoScrivania) {
        this.cdUoScrivania = cdUoScrivania;
    }

    public boolean isRODtPagamentoRichiesta() {
        return this.isAnnullato() || this.isPagato() ||
                Optional.ofNullable(this.getStato_trasmissione())
                  .map(stato->stato.equals(MandatoBulk.STATO_TRASMISSIONE_TRASMESSO))
                  .orElse(Boolean.FALSE) ||
                Optional.of(this)
                  .filter(model->Optional.ofNullable(model.getStato_trasmissione()).filter(stato->!stato.equals(MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO)).isPresent())
                  .map(model->!model.getMandato_rigaColl().stream().map(Mandato_rigaBulk::getCd_modalita_pag).filter(modpag->modpag.equals("F24EP")).findAny().isPresent())
                  .orElse(Boolean.FALSE) ||
                Optional.ofNullable(this.getCdUoScrivania())
                  .map(uo->!uo.equals(this.getCd_uo_origine()))
                  .orElse(Boolean.FALSE);
    }

    @Override
    public String getCd_tipo_doc() {
        return this.getCd_tipo_documento_cont();
    }

    @Override
    public String getCd_uo() {
        return this.getCd_unita_organizzativa();
    }

    @Override
    public Long getPg_doc() {
        return this.getPg_documento_cont();
    }

    @Override
    public TipoDocumentoEnum getTipoDocumentoEnum() {
        return TipoDocumentoEnum.fromValue(this.getCd_tipo_doc());
    }

    @Override
    public TerzoBulk getTerzo() {
        return Optional.ofNullable(this.getMandato_terzo()).flatMap(el->Optional.ofNullable(el.getTerzo())).orElse(null);
    }

    @Override
    public java.lang.Long getPg_manrev() {
        return this.getPg_mandato();
    }

    public Scrittura_partita_doppiaBulk getScrittura_partita_doppia() {
        return scrittura_partita_doppia;
    }

    public void setScrittura_partita_doppia(Scrittura_partita_doppiaBulk scrittura_partita_doppia) {
        this.scrittura_partita_doppia = scrittura_partita_doppia;
    }

    @Override
    public Timestamp getDt_contabilizzazione() {
        return this.getDt_pagamento();
    }
}