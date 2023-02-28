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
import it.cnr.contab.docamm00.docs.bulk.TipoDocumentoEnum;
import it.cnr.contab.util.RemoveAccent;
import it.cnr.contab.util.Utility;
import it.cnr.contab.util.enumeration.EsitoOperazione;
import it.cnr.contab.util.enumeration.StatoVariazioneSostituzione;
import it.cnr.jada.bulk.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public class ReversaleBulk extends ReversaleBase implements IManRevBulk, IDefferUpdateSaldi {

    public final static String STATO_REVERSALE_ANNULLATO = "A";
    public final static String STATO_REVERSALE_EMESSO = "E";
    public final static String STATO_REVERSALE_INCASSATO = "P";
    public final static Dictionary statoKeys;
    public final static String STATO_TRASMISSIONE_NON_INSERITO = "N";
    public final static String STATO_TRASMISSIONE_INSERITO = "I";
    public final static String STATO_TRASMISSIONE_TRASMESSO = "T";

    public final static String STATO_TRASMISSIONE_PREDISPOSTO = "P";
    public final static String STATO_TRASMISSIONE_PRIMA_FIRMA = "F";
    public final static Dictionary stato_trasmissioneKeys;
    public final static String TIPO_COMPETENZA = "C";
    public final static String TIPO_RESIDUO = "R";
    public final static Dictionary competenzaResiduoKeys;
    public final static String TIPO_TRASFERIMENTO = "A";

    public final static String TIPO_REGOLARIZZAZIONE = "R";
    public final static String TIPO_REGOLAM_SOSPESO = "S";
    public final static String TIPO_INCASSO = "I";
    public final static java.util.Dictionary tipoReversaleCNRKeys;
    public final static java.util.Dictionary tipoReversaleCdSKeys;
    public final static  Map<String,String> statoVariazioneSostituzioneKeys = StatoVariazioneSostituzione.KEYS;
    protected final static java.util.Dictionary classeDiPagamentoKeys = it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk.TI_PAGAMENTO_KEYS;
    public final static Map<String,String> esito_OperazioneKeys = Arrays.asList(EsitoOperazione.values())
            .stream()
            .collect(Collectors.toMap(
                    EsitoOperazione::value,
                    EsitoOperazione::label,
                    (oldValue, newValue) -> oldValue,
                    Hashtable::new
            ));

    static {
        statoKeys = new it.cnr.jada.util.OrderedHashtable();
        statoKeys.put(STATO_REVERSALE_ANNULLATO, "Annullata");
        statoKeys.put(STATO_REVERSALE_EMESSO, "Emessa");
        statoKeys.put(STATO_REVERSALE_INCASSATO, "Incassata");
    }

    static {
        stato_trasmissioneKeys = new it.cnr.jada.util.OrderedHashtable();
        stato_trasmissioneKeys.put(STATO_TRASMISSIONE_NON_INSERITO, "Non inserita in distinta");
        stato_trasmissioneKeys.put(STATO_TRASMISSIONE_INSERITO, "Inserita in distinta");
        stato_trasmissioneKeys.put(STATO_TRASMISSIONE_PREDISPOSTO, "Predisposto alla Firma");
        stato_trasmissioneKeys.put(STATO_TRASMISSIONE_PRIMA_FIRMA, "Prima Firma");
        stato_trasmissioneKeys.put(STATO_TRASMISSIONE_TRASMESSO, "Trasmessa");
    }

    static {
        competenzaResiduoKeys = new it.cnr.jada.util.OrderedHashtable();
        competenzaResiduoKeys.put(TIPO_COMPETENZA, "Competenza");
        competenzaResiduoKeys.put(TIPO_RESIDUO, "Residuo");
    }

    static {
        tipoReversaleCNRKeys = new it.cnr.jada.util.OrderedHashtable();
        tipoReversaleCNRKeys.put(TIPO_REGOLAM_SOSPESO, "Regolamento Sospeso");
        tipoReversaleCNRKeys.put(TIPO_INCASSO, "Incasso");
//		tipoReversaleCNRKeys.put(TIPO_TRASFERIMENTO, 	"Trasferimento");
//		tipoReversaleCNRKeys.put(TIPO_INCASSO_ESTERO,	"Incasso estero");
        tipoReversaleCNRKeys.put(TIPO_REGOLARIZZAZIONE, "Regolarizzazione");
    }

    static {
        tipoReversaleCdSKeys = new it.cnr.jada.util.OrderedHashtable();
        tipoReversaleCdSKeys.put(TIPO_REGOLAM_SOSPESO, "Regolamento Sospeso");
        tipoReversaleCdSKeys.put(TIPO_INCASSO, "Incasso");
        tipoReversaleCdSKeys.put(TIPO_TRASFERIMENTO, "Trasferimento");
//		tipoReversaleCdSKeys.put(TIPO_INCASSO_ESTERO	,	"Incasso estero");
        tipoReversaleCdSKeys.put(TIPO_REGOLARIZZAZIONE, "Regolarizzazione");
    }

    protected it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = new it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk();
    protected it.cnr.contab.config00.sto.bulk.CdsBulk cds = new it.cnr.contab.config00.sto.bulk.CdsBulk();
    protected BulkList reversale_rigaColl = new BulkList();
    protected Reversale_terzoBulk reversale_terzo;
    protected BulkList sospeso_det_etrColl = new BulkList();
    protected String cd_uo_ente;
    protected BulkList mandatiColl = new BulkList();
    protected Collection doc_contabili_collColl; //documenti contabili collegati
    protected it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk v_man_rev = new it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk();
    private List unita_organizzativaOptions;
    private java.util.Dictionary tipoDocumentoKeys;
    private java.util.Dictionary tipoDocumentoPerRicercaKeys;
    private java.math.BigDecimal im_residuo_reversale;
    private boolean siopeDaCompletare = false;
    private Scrittura_partita_doppiaBulk scrittura_partita_doppia;
    private it.cnr.jada.bulk.PrimaryKeyHashMap deferredSaldi = new PrimaryKeyHashMap();

    public ReversaleBulk() {
        super();
    }

    public ReversaleBulk(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.Long pg_reversale) {
        super(cd_cds, esercizio, pg_reversale);
        setCds(new it.cnr.contab.config00.sto.bulk.CdsBulk(cd_cds));
    }

    /**
     * Metodo per l'aggiunta di un elemento <code>Sospeso_det_etrBulk</code> alla <code>Collection</code>
     * dei sospesi della reversale.
     *
     * @param sospeso Il sospeso.
     * @return sde Il sospeso da aggiungere.
     */
    public Sospeso_det_etrBulk addToSospeso_det_etrColl(SospesoBulk sospeso) {
        Sospeso_det_etrBulk sde = null, tmp;
        // verifico che il sospeso esiste già con stato annullato
        for (Iterator i = getSospeso_det_etrColl().deleteIterator(); i.hasNext(); ) {
            tmp = (Sospeso_det_etrBulk) i.next();
            if (tmp.getCd_sospeso().equals(sospeso.getCd_sospeso())) {
                sde = tmp;
                break;
            }
        }
        if (sde == null) // il sospeso non esiste
            sde = new Sospeso_det_etrBulk();
        else {
            sde.setToBeUpdated();    // il sospeso esiste già
            sde.setIm_associato(null);
        }
        sde.setReversale(this);
        sde.setSospeso(sospeso);
        sde.setStato(Sospeso_det_etrBulk.STATO_DEFAULT);
        //sde.setIm_associato( getIm_reversale() );
        this.sospeso_det_etrColl.add(sde);
        return sde;
    }

    /**
     * Annulla la reversale e cancella eventuali sospesi associati alla reversale.
     */
    public void annulla() {
        Iterator i;
        Sospeso_det_etrBulk sospeso;

        setStato(STATO_REVERSALE_ANNULLATO);
//	setIm_reversale(new java.math.BigDecimal(0));	
        setToBeUpdated();
        //annulla i sospesi
        for (i = getSospeso_det_etrColl().iterator(); i.hasNext(); ) {
            // ((Sospeso_det_etrBulk) i.next()).setToBeDeleted();
            sospeso = ((Sospeso_det_etrBulk) i.next());
            sospeso.setStato(Sospeso_det_etrBulk.STATO_ANNULLATO);
            sospeso.setToBeUpdated();
        }
	/*
	la delete Iterator contiene già tutti i record con stato = ANNULLATO
	for (i = getSospeso_det_etrColl().deleteIterator(); i.hasNext(); )
	{
		// ((Sospeso_det_etrBulk) i.next()).setToBeDeleted();
		sospeso = ((Sospeso_det_etrBulk) i.next());
		sospeso.setStato( Sospeso_det_etrBulk.STATO_ANNULLATO );
		sospeso.setToBeUpdated();
	}
	*/


    }

    /**
     * Restituisce un array di <code>BulkCollection</code> contenenti oggetti
     * bulk da rendere persistenti insieme al ricevente.
     * L'implementazione standard restituisce <code>null</code>.
     */
    public BulkCollection[] getBulkLists() {
        return new it.cnr.jada.bulk.BulkCollection[]{
                reversale_rigaColl, sospeso_det_etrColl};
    }

    /**
     * Restituisce un array di <code>OggettoBulk</code> da rendere persistenti
     * insieme al ricevente.
     * L'implementazione standard restituisce <code>null</code>.
     */
    public OggettoBulk[] getBulksForPersistentcy() {
        return new OggettoBulk[]{
                reversale_terzo};

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
     * @return it.cnr.contab.config00.sto.bulk.CdsBulk
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
     * di tipo <code>Dictionary</code>.
     * In particolare, questo metodo carica in un Dictionary l'elenco dei possibili valori
     * che può assumere il campo <code>ti_pagamento</code>.
     *
     * @return java.util.Hashtable classeDiPagamentoKeys I valori del campo <code>ti_pagamento</code>.
     */
    public java.util.Dictionary getClasseDiPagamentoKeys() {
        return classeDiPagamentoKeys;
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
     * Calcola l'importo residuo della reversale, che è dato dalla differenza tra l'importo totale
     * della reversale e la somma degli importi ripartiti sui singoli sospesi associati.
     *
     * @return im_residuo_reversale L'importo residuo della reversale
     */
    public java.math.BigDecimal getIm_residuo_reversale() {

        Sospeso_det_etrBulk sospeso;

        im_residuo_reversale = Utility.nvl(this.getIm_reversale());

        for (Iterator i = getSospeso_det_etrColl().iterator(); i.hasNext(); ) {
            sospeso = (Sospeso_det_etrBulk) i.next();
            if (sospeso.getIm_associato() == null)
                sospeso.setIm_associato(new java.math.BigDecimal(0));
            im_residuo_reversale = im_residuo_reversale.subtract(sospeso.getIm_associato());
        }

	/*
	 ** Controlli complicati per importo residuo "sballato":

	// la reversale non ha ancora sospesi associati
	if( getSospeso_det_etrColl().size() == 0 )
		im_residuo_reversale = this.getIm_reversale();
	else
	{
	// la reversale ha almeno un sospeso associato
		for ( Iterator i = getSospeso_det_etrColl().iterator(); i.hasNext(); )
		{
			sospeso = (Sospeso_det_etrBulk)i.next();
			if ( sospeso.getIm_associato() == null ||
				 sospeso.getIm_associato().compareTo( new java.math.BigDecimal(0)) < 0 )
				 sospeso.setIm_associato( new java.math.BigDecimal(0) );
			else if ( sospeso.getIm_associato().compareTo( this.getIm_reversale()) > 0  && im_residuo_reversale != null )
			{
				if ( sospeso.getSospeso().getIm_disponibile().compareTo( im_residuo_reversale ) > 0 )
					sospeso.setIm_associato(im_residuo_reversale );
				else
					sospeso.setIm_associato( sospeso.getSospeso().getIm_disponibile() );
			}
		}
		im_residuo_reversale = this.getIm_reversale().subtract( this.getImTotaleSospesi() );
	}
	*/
        return im_residuo_reversale;
    }

    /**
     * Metodo per calcolare l'importo totale dei sospesi (d'entrata) associati
     * alla reversale.
     *
     * @return totSospesi <code>BigDecimal</code> l'importo totale calcolato dei sospesi
     */
    public java.math.BigDecimal getImTotaleSospesi() {
        java.math.BigDecimal totSospesi = new java.math.BigDecimal(0);
        Sospeso_det_etrBulk sospeso;
        for (Iterator i = getSospeso_det_etrColl().iterator(); i.hasNext(); ) {
            sospeso = ((Sospeso_det_etrBulk) i.next());
            totSospesi = totSospesi.add(sospeso.getIm_associato());
        }
        return totSospesi;

    }

    /**
     * @return it.cnr.jada.bulk.BulkList
     */
    public it.cnr.jada.bulk.BulkList getMandatiColl() {
        return mandatiColl;
    }

    /**
     * @param newMandatiColl it.cnr.jada.bulk.BulkList
     */
    public void setMandatiColl(it.cnr.jada.bulk.BulkList newMandatiColl) {
        mandatiColl = newMandatiColl;
    }

    public Long getPg_documento_cont() {
        return getPg_reversale();
    }

    /**
     * @return it.cnr.jada.bulk.BulkList
     */
    public it.cnr.jada.bulk.BulkList<Reversale_rigaBulk> getReversale_rigaColl() {
        return reversale_rigaColl;
    }

    /**
     * @param newReversale_rigaColl it.cnr.jada.bulk.BulkList
     */
    public void setReversale_rigaColl(it.cnr.jada.bulk.BulkList newReversale_rigaColl) {
        reversale_rigaColl = newReversale_rigaColl;
    }

    /**
     * @return it.cnr.contab.doccont00.core.bulk.Reversale_terzoBulk
     */
    public Reversale_terzoBulk getReversale_terzo() {
        return reversale_terzo;
    }

    /**
     * @param newReversale_terzo it.cnr.contab.doccont00.core.bulk.Reversale_terzoBulk
     */
    public void setReversale_terzo(Reversale_terzoBulk newReversale_terzo) {
        reversale_terzo = newReversale_terzo;
    }

    /**
     * @return it.cnr.jada.bulk.BulkList
     */
    public it.cnr.jada.bulk.BulkList getSospeso_det_etrColl() {
        return sospeso_det_etrColl;
    }

    /**
     * @param newSospeso_det_etrColl it.cnr.jada.bulk.BulkList
     */
    public void setSospeso_det_etrColl(it.cnr.jada.bulk.BulkList newSospeso_det_etrColl) {
        sospeso_det_etrColl = newSospeso_det_etrColl;
    }

    /**
     * @return java.util.Dictionary
     */
    public java.util.Dictionary getStatoKeys() {
        return statoKeys;
    }

    /**
     * @return java.util.Dictionary
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
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'tipoReversaleKeys'
     *
     * @return Il valore della proprietà 'tipoReversaleKeys'
     */
    public java.util.Dictionary getTipoReversaleKeys() {
        if (getUnita_organizzativa() == null || getUnita_organizzativa().getCd_unita_organizzativa() == null)
            return tipoReversaleCdSKeys;
        else if (getUnita_organizzativa().getCd_unita_organizzativa().equals(cd_uo_ente))
            return tipoReversaleCNRKeys;
        return tipoReversaleCdSKeys;

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
//	setCd_uo_origine( it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_unita_organizzativa());
        unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);

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
//	unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
//	setCd_cds( unita_organizzativa.getUnita_padre().getCd_unita_organizzativa());
        setCd_cds_origine(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_unita_padre());
        setCd_uo_origine(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_unita_organizzativa());

        setStato(STATO_REVERSALE_EMESSO);
        setStato_trasmissione(STATO_TRASMISSIONE_NON_INSERITO);
        setTi_reversale(TIPO_REGOLAM_SOSPESO);
        setIm_incassato(new java.math.BigDecimal(0));
        setIm_reversale(new java.math.BigDecimal(0));
        setCd_tipo_documento_cont(Numerazione_doc_contBulk.TIPO_REV);
        setStato_coge(MandatoBulk.STATO_COGE_N);

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
//	setCd_uo_origine( it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_unita_organizzativa());
        unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
        return this;
    }

    /**
     * Verifica se la reversale ha lo stato "annullato".
     *
     * @return Lo stato della reversale
     * TRUE 	La reversale è annullata
     * FALSE 	La reversale non è annullata
     */
    public boolean isAnnullato() {
        return STATO_REVERSALE_ANNULLATO.equals(getStato());
    }

    public boolean isBanca_italia() {
        if (reversale_rigaColl != null && reversale_rigaColl.size() > 0)
            return ((Reversale_rigaBulk) reversale_rigaColl.get(0)).getBanca().getTi_pagamento().equals(Rif_modalita_pagamentoBulk.BANCA_ITALIA);
        return false;
    }

    /**
     * Verifica se la reversale ha lo stato "incassato".
     *
     * @return Lo stato della reversale
     * TRUE 	La reversale è incassata
     * FALSE 	La reversale non è incassata
     */
    public boolean isIncassato() {
        return STATO_REVERSALE_INCASSATO.equals(getStato());
    }

    /**
     * Verifica se la reversale è provvisoria.
     *
     * @return Il tipo di reversale
     * TRUE 	La reversale è provvisoria
     * FALSE 	La reversale non è provvisoria
     */
    public boolean isProvvisoria() {
        return Numerazione_doc_contBulk.TIPO_REV_PROVV.equals(getCd_tipo_documento_cont());
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'rOUnitaOrganizzativa'
     *
     * @return Il valore della proprietà 'rOUnitaOrganizzativa'
     */
    public boolean isROUnitaOrganizzativa() {
        // return unita_organizzativa == null || unita_organizzativa.getCrudStatus() == NORMAL;
        return !this.reversale_rigaColl.isEmpty() || !this.sospeso_det_etrColl.isEmpty();
    }

    /**
     * <!-- @TODO: da completare -->
     */
    public void refreshImporto() {
        setIm_reversale(new java.math.BigDecimal(0));
        for (Iterator i = reversale_rigaColl.iterator(); i.hasNext(); ) {
            Reversale_rigaBulk rr = (Reversale_rigaBulk) i.next();
            if (!rr.getStato().equals(Reversale_rigaBulk.STATO_ANNULLATO))
                setIm_reversale(getIm_reversale().add(rr.getIm_reversale_riga()));
        }

    }

    /**
     * Metodo per l'eliminazione di un elemento <code>Reversale_rigaBulk</code> dalla <code>Collection</code>
     * delle righe della reversale.
     *
     * @param index L'indice per scorrere la collezione di righe della reversale.
     * @return Reversale_rigaBulk La riga da rimuovere.
     */
    public Reversale_rigaBulk removeFromReversale_rigaColl(int index) {
        Reversale_rigaBulk riga = (Reversale_rigaBulk) reversale_rigaColl.remove(index);
//	riga.setToBeDeleted();
//	refreshImporto();
        return riga;
    }

    /**
     * Metodo per l'eliminazione di un elemento <code>Sospeso_det_etrBulk</code> dalla <code>Collection</code>
     * dei sospesi della reversale.
     *
     * @param index L'indice per scorrere la collezione di sospesi della reversale.
     * @return Sospeso_det_etrBulk Il sospeso da rimuovere.
     */
    public Sospeso_det_etrBulk removeFromSospeso_det_etrColl(int index) {
        Sospeso_det_etrBulk sospeso = (Sospeso_det_etrBulk) sospeso_det_etrColl.get(index);
        sospeso.setStato(Sospeso_det_etrBulk.STATO_ANNULLATO);
	/*
	if ( sospeso.getIm_associato().compareTo( this.getIm_reversale()) >= 0  && im_residuo_reversale != null )
	{
		if ( sospeso.getSospeso().getIm_disponibile().compareTo( im_residuo_reversale ) > 0 )
			sospeso.setIm_associato(im_residuo_reversale );
		else
			sospeso.setIm_associato( sospeso.getSospeso().getIm_disponibile() );
	}
	*/
        if (!sospeso.isToBeCreated())
            sospeso.setCrudStatus(TO_BE_UPDATED);
        else
            sospeso.setToBeDeleted();
        sospeso = (Sospeso_det_etrBulk) sospeso_det_etrColl.remove(index);
        return sospeso;
    }

    public void setToBeDeleted() {
        Iterator i, x;
        super.setToBeDeleted();
        for (i = reversale_rigaColl.iterator(); i.hasNext(); ) {
            Reversale_rigaBulk riga = (Reversale_rigaBulk) i.next();
            riga.setToBeDeleted();
            for (x = riga.reversale_siopeColl.iterator(); x.hasNext(); )
                ((Reversale_siopeBulk) x.next()).setToBeDeleted();
            for (x = riga.reversale_siopeColl.deleteIterator(); x.hasNext(); )
                ((Reversale_siopeBulk) x.next()).setToBeDeleted();
        }
        for (i = reversale_rigaColl.deleteIterator(); i.hasNext(); ) {
            Reversale_rigaBulk riga = (Reversale_rigaBulk) i.next();
            riga.setToBeDeleted();
            for (x = riga.reversale_siopeColl.iterator(); x.hasNext(); )
                ((Reversale_siopeBulk) x.next()).setToBeDeleted();
            for (x = riga.reversale_siopeColl.deleteIterator(); x.hasNext(); )
                ((Reversale_siopeBulk) x.next()).setToBeDeleted();
        }
        reversale_terzo.setToBeDeleted();
        for (i = sospeso_det_etrColl.deleteIterator(); i.hasNext(); )
            ((Sospeso_det_etrBulk) i.next()).setToBeDeleted();
        for (i = sospeso_det_etrColl.iterator(); i.hasNext(); )
            ((Sospeso_det_etrBulk) i.next()).setToBeDeleted();
    }

    /**
     * Metodo per validare i sospesi d'entrata da associare alla reversale.
     *
     * @param sospesi <code>List</code> la lista dei sospesi da validare prima di essere associati alla reversale
     */
    public void validaSospesi(List sospesi) throws ValidationException {
        if (sospesi.size() == 0)
            throw new ValidationException("Non sono stati selezionati sospesi da associare alla reversale");

        String ti_cc_bi;

        if (getSospeso_det_etrColl().size() > 0)
            ti_cc_bi = ((Sospeso_det_etrBulk) getSospeso_det_etrColl().get(0)).getSospeso().getTi_cc_bi();
        else
            ti_cc_bi = ((SospesoBulk) sospesi.get(0)).getTi_cc_bi();

        for (Iterator i = sospesi.iterator(); i.hasNext(); )
            if (!((SospesoBulk) i.next()).getTi_cc_bi().equals(ti_cc_bi))
                throw new ValidationException("I sospesi associabili alla reversale devono avere la stessa provenienza (Banca d'Italia o C/C).");
    }

    /**
     * Metodo con cui si verifica la validità di alcuni campi, mediante un
     * controllo sintattico o contestuale.
     */
    public void validate() throws ValidationException {
        super.validate();

        // controllo su campo DATA EMISSIONE
        if (getDt_emissione() == null)
            throw new ValidationException("Il campo DATA CONTABILITA' è obbligatorio.");
        if (getDs_reversale() != null && getDs_reversale().length() != 0) {
            for (int i = 0; i < getDs_reversale().length(); i++) {
                if ((((int) RemoveAccent.convert(getDs_reversale()).charAt(i)) < 31 ||
                        ((int) RemoveAccent.convert(getDs_reversale()).charAt(i)) > 126) &&
                        (int) RemoveAccent.convert(getDs_reversale()).charAt(i) != 13 &&
                        (int) RemoveAccent.convert(getDs_reversale()).charAt(i) != 10)
                    throw new ValidationException("La descrizione contienere caratteri speciali non supportati.");
            }
        }
        for (Iterator i = getSospeso_det_etrColl().iterator(); i.hasNext(); )
            ((Sospeso_det_etrBulk) i.next()).validate();

    }

    /**
     * Verifica se la reversale è di tipo "trasferimento".
     *
     * @return Il tipo di reversale
     * TRUE 	La reversale è di tipo "trasferimento"
     * FALSE 	La reversale non è di tipo "trasferimento"
     */
    public boolean isTrasferimento() {
        return getTi_reversale() != null && getTi_reversale().equals(ReversaleBulk.TIPO_TRASFERIMENTO);
    }

    /**
     * Verifica se la reversale è di tipo "regolarizzazione".
     *
     * @return Il tipo di reversale
     * TRUE 	La reversale è di tipo "regolarizzazione"
     * FALSE 	La reversale non è di tipo "regolarizzazione"
     */
    public boolean isReversaleRegolarizzazione() {
        return getTi_reversale() != null && getTi_reversale().equals(ReversaleBulk.TIPO_REGOLARIZZAZIONE);
    }

    /**
     * Verifica se la reversale è di tipo "regolamento sospeso".
     *
     * @return Il tipo di reversale
     * TRUE 	La reversale è di tipo "regolamento sospeso"
     * FALSE 	La reversale non è di tipo "regolamento sospeso"
     */
    public boolean isRegolamentoSospeso() {
        return getTi_reversale() != null && getTi_reversale().equals(ReversaleBulk.TIPO_REGOLAM_SOSPESO);
    }

    /**
     * Verifica se la reversale è di tipo "incasso".
     *
     * @return Il tipo di reversale
     * TRUE 	La reversale è di tipo "incasso"
     * FALSE 	La reversale non è di tipo "incasso"
     */
    public boolean isIncasso() {
        return getTi_reversale() != null && getTi_reversale().equals(ReversaleBulk.TIPO_INCASSO);
    }

    /**
     * Verifica se la reversale richiede la gestione SIOPE
     *
     * @return l'obbligatorietà della gestione SIOPE
     * TRUE 	Sulle righe di reversale è richiesta la gestione SIOPE
     * FALSE 	Sulle righe di reversale non è richiesta la gestione SIOPE
     * <p>
     * Sulle reversali non è obbligatorio caricare i codici SIOPE solo se trattasi di reversali di tipo interno
     * e quindi di Trasferimento generate sui CdR e di Regolarizzazione
     */
    public boolean isRequiredSiope() {
        return !(this.isReversaleRegolarizzazione());
    }

    /**
     * Verifica se la reversale ha tutte le righe completamente associate a codici SIOPE
     *
     * @return TRUE    Tutte le righe di reversale sono associate a codici SIOPE
     * FALSE 	Alcune righe di reversale non sono associate a codici SIOPE
     */
    public boolean isSiopeTotalmenteAssociato() {
        for (Iterator i = this.getReversale_rigaColl().iterator(); i.hasNext(); ) {
            Reversale_rigaBulk riga = (Reversale_rigaBulk) i.next();
            if (!riga.getTipoAssociazioneSiope().equals(Reversale_rigaBulk.SIOPE_TOTALMENTE_ASSOCIATO))
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
        for (Iterator i = this.getReversale_rigaColl().iterator(); i.hasNext(); )
            totale = totale.add(((Reversale_rigaBulk) i.next()).getIm_associato_siope());
        return totale;
    }

    public BigDecimal getIm_da_associare_siope() {
        return Utility.nvl(getIm_reversale()).subtract(Utility.nvl(getIm_associato_siope()));
    }

    public boolean isSiopeDaCompletare() {
        return siopeDaCompletare;
    }

    public void setSiopeDaCompletare(boolean siopeDaCompletare) {
        this.siopeDaCompletare = siopeDaCompletare;
    }

    public it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk getV_man_rev() {
        return v_man_rev;
    }

    public void setV_man_rev(
            it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk v_man_rev) {
        this.v_man_rev = v_man_rev;
    }

    public Long getPg_reversale_riemissione() {
        if (v_man_rev == null)
            return null;
        return v_man_rev.getPg_documento_cont();
    }

    @Override
    public void setPg_reversale_riemissione(Long pg_reversale_riemissione) {
        if (getV_man_rev() != null)
            getV_man_rev().setPg_documento_cont(pg_reversale_riemissione);
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
        return Optional.ofNullable(this.getReversale_terzo()).flatMap(el->Optional.ofNullable(el.getTerzo())).orElse(null);
    }

    @Override
    public java.lang.Long getPg_manrev() {
        return this.getPg_reversale();
    }

    public Scrittura_partita_doppiaBulk getScrittura_partita_doppia() {
        return scrittura_partita_doppia;
    }

    public void setScrittura_partita_doppia(Scrittura_partita_doppiaBulk scrittura_partita_doppia) {
        this.scrittura_partita_doppia = scrittura_partita_doppia;
    }

    @Override
    public Timestamp getDt_contabilizzazione() {
        return this.getDt_incasso();
    }
    public String getIdReversaleAsString(){
        return getCd_cds()+"-"+getEsercizio()+"-"+getPg_reversale();
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


    /**
     * Aggiunge un nuovo dettaglio (Mandato_rigaBulk) alla lista di dettagli definiti per il mandato
     * inizializzandone alcuni campi
     *
     * @param riga dettaglio da aggiungere alla lista
     * @return int
     */
    public int addToReversale_rigaColl(Reversale_rigaBulk riga) {
        reversale_rigaColl.add(riga);
        return reversale_rigaColl.size() - 1;
    }

}
