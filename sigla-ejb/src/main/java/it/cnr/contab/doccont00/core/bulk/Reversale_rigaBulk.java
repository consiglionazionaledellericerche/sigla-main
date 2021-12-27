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

import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk;
import it.cnr.contab.coepcoan00.core.bulk.Scrittura_partita_doppiaBulk;
import it.cnr.contab.config00.bulk.Codici_siopeBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class Reversale_rigaBulk extends Reversale_rigaBase {
    public final static String SIOPE_TOTALMENTE_ASSOCIATO = "T";
    public final static String SIOPE_PARZIALMENTE_ASSOCIATO = "P";
    public final static String SIOPE_NON_ASSOCIATO = "N";
    public final static String STATO_INIZIALE = "E";
    public final static String STATO_ANNULLATO = "A";
    public final static String STATO_PARZ_INCASSATO = "L";
    public final static String STATO_INCASSATO = "P";
    final static public java.util.Dictionary classeDiPagamentoKeys = it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk.TI_PAGAMENTO_KEYS;
    final static public java.util.Dictionary statoKeys;
    static private final java.util.Hashtable fatturaPassivaKeys;
    static private final java.util.Hashtable fatturaAttivaKeys;
    static private java.util.Hashtable fl_pgiro_Keys;

    static {
        fatturaPassivaKeys = new Hashtable();
        fatturaPassivaKeys.put(it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk.TIPO_FATTURA_PASSIVA, "Fattura Passiva");
        fatturaPassivaKeys.put(it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk.TIPO_NOTA_DI_CREDITO, "Nota di credito");
        fatturaPassivaKeys.put(it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk.TIPO_NOTA_DI_DEBITO, "Nota di debito");
    }

	static {
        fatturaAttivaKeys = new Hashtable();
        fatturaAttivaKeys.put(it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk.TIPO_FATTURA_ATTIVA, "Fattura Attiva");
        fatturaAttivaKeys.put(it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO, "Nota di credito");
        fatturaAttivaKeys.put(it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk.TIPO_NOTA_DI_DEBITO, "Nota di debito");
    }

	static {
        statoKeys = new it.cnr.jada.util.OrderedHashtable();
        statoKeys.put(STATO_ANNULLATO, "Annullata");
        statoKeys.put(STATO_INIZIALE, "Emessa");
        statoKeys.put(STATO_PARZ_INCASSATO, "Parz.Incassata");
        statoKeys.put(STATO_INCASSATO, "Incassata");
    }

    protected ReversaleBulk reversale;
    protected BancaBulk banca = new BancaBulk();
    protected Modalita_pagamentoBulk modalita_pagamento = new Modalita_pagamentoBulk();
    protected List modalita_pagamentoOptions;
    protected List bancaOptions;
    protected String ti_fattura;
    protected Elemento_voceBulk elemento_voce;
    protected BulkList reversale_siopeColl = new BulkList();
    protected BulkList codici_siopeColl = new BulkList();

	protected BulkList reversaleCupColl = new BulkList();
    private boolean fl_aggiorna_saldi_per_annullamento = false;
    private Long pg_ver_rec_doc_amm;

    public Reversale_rigaBulk() {
        super();
    }

    public Reversale_rigaBulk(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.Integer esercizio_accertamento, java.lang.Integer esercizio_ori_accertamento, java.lang.Long pg_accertamento, java.lang.Long pg_accertamento_scadenzario, java.lang.Long pg_reversale) {
        super(cd_cds, esercizio, esercizio_accertamento, esercizio_ori_accertamento, pg_accertamento, pg_accertamento_scadenzario, pg_reversale);
    }

    /**
     * @return java.util.Hashtable
     */
    public static java.util.Hashtable getFatturaAttivaKeys() {
        return fatturaAttivaKeys;
    }

    /**
     * @return java.util.Hashtable
     */
    public static java.util.Hashtable getFatturaPassivaKeys() {
        return fatturaPassivaKeys;
    }

    /**
     * Annulla il dettaglio della reversale.
     */
    public void annulla() {
        if (STATO_ANNULLATO.equals(getStato()))
            return;
        setStato(STATO_ANNULLATO);
        setToBeUpdated();
        setFl_aggiorna_saldi_per_annullamento(true);
//	setIm_reversale_riga(new java.math.BigDecimal(0));
        // setFl_aggiorna_saldi( true );
    }

    /**
     * @return it.cnr.contab.anagraf00.core.bulk.BancaBulk
     */
    public it.cnr.contab.anagraf00.core.bulk.BancaBulk getBanca() {
        return banca;
    }

    /**
     * @param newBanca it.cnr.contab.anagraf00.core.bulk.BancaBulk
     */
    public void setBanca(it.cnr.contab.anagraf00.core.bulk.BancaBulk newBanca) {
        banca = newBanca;
    }

    /**
     * @return java.util.List
     */
    public java.util.List getBancaOptions() {
        return bancaOptions;
    }

    /**
     * @param newBancaOptions java.util.List
     */
    public void setBancaOptions(java.util.List newBancaOptions) {
        bancaOptions = newBancaOptions;
    }

    public java.lang.String getCd_cds() {
        it.cnr.contab.doccont00.core.bulk.ReversaleBulk reversale = this.getReversale();
        if (reversale == null)
            return null;
        it.cnr.contab.config00.sto.bulk.CdsBulk cds = reversale.getCds();
        if (cds == null)
            return null;
        return cds.getCd_unita_organizzativa();
    }

    public void setCd_cds(java.lang.String cd_cds) {
        this.getReversale().getCds().setCd_unita_organizzativa(cd_cds);
    }

    public java.lang.String getCd_modalita_pag() {
        it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk modalita_pagamento = this.getModalita_pagamento();
        if (modalita_pagamento == null)
            return null;
        it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk rif_modalita_pagamento = modalita_pagamento.getRif_modalita_pagamento();
        if (rif_modalita_pagamento == null)
            return null;
        return rif_modalita_pagamento.getCd_modalita_pag();
    }

    public void setCd_modalita_pag(java.lang.String cd_modalita_pag) {
        this.getModalita_pagamento().getRif_modalita_pagamento().setCd_modalita_pag(cd_modalita_pag);
    }

    public java.lang.Integer getCd_terzo_uo() {
        it.cnr.contab.anagraf00.core.bulk.BancaBulk banca = this.getBanca();
        if (banca == null)
            return null;
        it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzo = banca.getTerzo();
        if (terzo == null)
            return null;
        return terzo.getCd_terzo();
    }

    public void setCd_terzo_uo(java.lang.Integer cd_terzo_uo) {
        this.getBanca().getTerzo().setCd_terzo(cd_terzo_uo);
    }

    /**
     * @return java.util.Dictionary
     */
    public java.util.Dictionary getClasseDiPagamentoKeys() {
        return classeDiPagamentoKeys;
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'ds_tipo_documento_amm'
     *
     * @return Il valore della proprietà 'ds_tipo_documento_amm'
     */
    public java.lang.String getDs_tipo_documento_amm() {
        if (Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA.equals(getCd_tipo_documento_amm()))
            return (String) getFatturaPassivaKeys().get(getTi_fattura());
        else if (Numerazione_doc_ammBulk.TIPO_FATTURA_ATTIVA.equals(getCd_tipo_documento_amm()))
            return (String) getFatturaAttivaKeys().get(getTi_fattura());
        return (String) getTipoDocumentoKeys().get(getCd_tipo_documento_amm());
    }

    public java.lang.Integer getEsercizio() {
        it.cnr.contab.doccont00.core.bulk.ReversaleBulk reversale = this.getReversale();
        if (reversale == null)
            return null;
        return reversale.getEsercizio();
    }

    public void setEsercizio(java.lang.Integer esercizio) {
        this.getReversale().setEsercizio(esercizio);
    }

    /**
     * Metodo con cui si ottiene il valore della variabile <code>fl_pgiro_Keys</code>
     * di tipo <code>Hashtable</code>.
     * In particolare, questo metodo carica in una Hashtable l'elenco dei possibili valori
     * che può assumere il flag <code>fl_pgiro</code>.
     *
     * @return java.util.Hashtable fl_pgiro_Keys I valori del flag <code>fl_pgiro</code>.
     */
    public java.util.Hashtable getFl_pgiro_Keys() {
        Hashtable fl_pgiro_Keys = new Hashtable();

        fl_pgiro_Keys.put(new Boolean(true), "Y");
        fl_pgiro_Keys.put(new Boolean(false), "N");
        return fl_pgiro_Keys;
    }

    /**
     * @return it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk
     */
    public it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk getModalita_pagamento() {
        return modalita_pagamento;
    }

    /**
     * @param newModalita_pagamento it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk
     */
    public void setModalita_pagamento(it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk newModalita_pagamento) {
        modalita_pagamento = newModalita_pagamento;
    }

    /**
     * @return java.util.List
     */
    public java.util.List getModalita_pagamentoOptions() {
        return modalita_pagamentoOptions;
    }

    /**
     * @param newModalita_pagamentoOptions java.util.List
     */
    public void setModalita_pagamentoOptions(java.util.List newModalita_pagamentoOptions) {
        modalita_pagamentoOptions = newModalita_pagamentoOptions;
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

    public java.lang.Long getPg_reversale() {
        it.cnr.contab.doccont00.core.bulk.ReversaleBulk reversale = this.getReversale();
        if (reversale == null)
            return null;
        return reversale.getPg_reversale();
    }

    public void setPg_reversale(java.lang.Long pg_reversale) {
        this.getReversale().setPg_reversale(pg_reversale);
    }

    /**
     * Insert the method's description here.
     * Creation date: (03/06/2003 21:03:24)
     *
     * @return java.lang.Long
     */
    public java.lang.Long getPg_ver_rec_doc_amm() {
        return pg_ver_rec_doc_amm;
    }

    /**
     * Insert the method's description here.
     * Creation date: (03/06/2003 21:03:24)
     *
     * @param newPg_ver_rec_doc_amm java.lang.Long
     */
    public void setPg_ver_rec_doc_amm(java.lang.Long newPg_ver_rec_doc_amm) {
        pg_ver_rec_doc_amm = newPg_ver_rec_doc_amm;
    }

    /**
     * @return it.cnr.contab.doccont00.core.bulk.ReversaleBulk
     */
    public ReversaleBulk getReversale() {
        return reversale;
    }

    /**
     * @param newReversale it.cnr.contab.doccont00.core.bulk.ReversaleBulk
     */
    public void setReversale(ReversaleBulk newReversale) {
        reversale = newReversale;
    }

    /**
     * @return java.util.Dictionary
     */
    public java.util.Dictionary getStatoKeys() {
        return statoKeys;
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'ti_fattura'
     *
     * @return Il valore della proprietà 'ti_fattura'
     */
    public java.lang.String getTi_fattura() {
        return ti_fattura;
    }

    /**
     * <!-- @TODO: da completare -->
     * Imposta il valore della proprietà 'ti_fattura'
     *
     * @param newTi_fattura Il valore da assegnare a 'ti_fattura'
     */
    public void setTi_fattura(java.lang.String newTi_fattura) {
        ti_fattura = newTi_fattura;
    }

    /**
     * Metodo con cui si ottiene il valore della variabile <code>tipoDocumentoKeys</code>
     * di tipo <code>Hashtable</code>.
     *
     * @return java.util.Hashtable tipoDocumentoKeys
     */
    public java.util.Dictionary getTipoDocumentoKeys() {
        if (getReversale() != null)
            return getReversale().getTipoDocumentoKeys();
        return null;
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'fl_aggiorna_saldi_per_annullamento'
     *
     * @return Il valore della proprietà 'fl_aggiorna_saldi_per_annullamento'
     */
    public boolean isFl_aggiorna_saldi_per_annullamento() {
        return fl_aggiorna_saldi_per_annullamento;
    }

    /**
     * <!-- @TODO: da completare -->
     * Imposta il valore della proprietà 'fl_aggiorna_saldi_per_annullamento'
     *
     * @param newFl_aggiorna_saldi_per_annullamento Il valore da assegnare a 'fl_aggiorna_saldi_per_annullamento'
     */
    public void setFl_aggiorna_saldi_per_annullamento(boolean newFl_aggiorna_saldi_per_annullamento) {
        fl_aggiorna_saldi_per_annullamento = newFl_aggiorna_saldi_per_annullamento;
    }

    /**
     * @return it.cnr.jada.bulk.BulkList
     */
    public it.cnr.jada.bulk.BulkList getReversale_siopeColl() {
        return reversale_siopeColl;
    }

    /**
     * @param newReversale_siopeColl it.cnr.jada.bulk.BulkList
     */
    public void setReversale_siopeColl(it.cnr.jada.bulk.BulkList newReversale_siopeColl) {
        reversale_siopeColl = newReversale_siopeColl;
    }

    /**
     * @return it.cnr.jada.bulk.BulkList
     */
    public it.cnr.jada.bulk.BulkList getCodici_siopeColl() {
        return codici_siopeColl;
    }

    /**
     * @param newCodici_siopeColl it.cnr.jada.bulk.BulkList
     */
    public void setCodici_siopeColl(it.cnr.jada.bulk.BulkList newCodici_siopeColl) {
        codici_siopeColl = newCodici_siopeColl;
    }

    /**
     * Restituisce un array di <code>BulkCollection</code> contenenti oggetti
     * bulk da rendere persistenti insieme al ricevente.
     * L'implementazione standard restituisce <code>null</code>.
     *
     * @see it.cnr.jada.comp.GenericComponent
     */
    public BulkCollection[] getBulkLists() {
        return new it.cnr.jada.bulk.BulkCollection[]{reversale_siopeColl, reversaleCupColl};

    }

    /**
     * Aggiunge un nuovo dettaglio (Reversale_rigaBulk) alla lista di dettagli definiti per la reversale
     * inizializzandone alcuni campi
     *
     * @param reversale_siope dettaglio da aggiungere alla lista
     * @return int
     */
    public int addToReversale_siopeColl(Reversale_siopeBulk reversale_siope) {
        reversale_siopeColl.add(reversale_siope);
        reversale_siope.setReversale_riga(this);
        if (reversale_siope.getImporto() == null)
            reversale_siope.setImporto(new java.math.BigDecimal(0));
        return reversale_siopeColl.size() - 1;
    }

    /**
     * Metodo per l'eliminazione di un elemento <code>Reversale_siopeBulk</code> dalla <code>Collection</code>
     * delle righe della reversale.
     *
     * @param index L'indice per scorrere la collezione dei codici siope legati alla riga della reversale.
     * @return Mandato_siopeBulk La riga da rimuovere.
     */
    public Reversale_siopeBulk removeFromReversale_siopeColl(int index) {
        Reversale_siopeBulk reversale_siope = (Reversale_siopeBulk) reversale_siopeColl.remove(index);
        addToCodici_siopeColl(reversale_siope.getCodice_siope());
        return reversale_siope;
    }

    /**
     * Aggiunge un nuovo dettaglio (Codici_siopeBulk) alla lista dei codici siope collegabili alla riga della eversale
     *
     * @param codice_siope dettaglio da aggiungere alla lista
     * @return int
     */
    public int addToCodici_siopeColl(Codici_siopeBulk codice_siope) {
        codici_siopeColl.add(codice_siope);
        return codici_siopeColl.size() - 1;
    }

    /**
     * Metodo per l'eliminazione di un elemento <code>Codici_siopeBulk</code> dalla <code>Collection</code>
     * dei codici siope collegabili alla riga della reversale.
     *
     * @param index L'indice per scorrere la collezione dei codici siope legati alla riga della reversale.
     * @return Codici_siopeBulk La riga da rimuovere.
     */
    public Codici_siopeBulk removeFromCodici_siopeColl(int index) {
        Codici_siopeBulk codice_siope = (Codici_siopeBulk) codici_siopeColl.remove(index);
        Reversale_siopeBulk reversale_siope = new Reversale_siopeIBulk();
        reversale_siope.setCodice_siope(codice_siope);
        reversale_siope.setToBeCreated();
        addToReversale_siopeColl(reversale_siope);
        return codice_siope;
    }

    public BigDecimal getIm_associato_siope() {
        BigDecimal totale = Utility.ZERO;
        for (Iterator i = getReversale_siopeColl().iterator(); i.hasNext(); )
            totale = totale.add(Utility.nvl(((Reversale_siopeBulk) i.next()).getImporto()));
        return Utility.nvl(totale);
    }

    public BigDecimal getIm_da_associare_siope() {
        return Utility.nvl(getIm_reversale_riga()).subtract(Utility.nvl(getIm_associato_siope()));
    }

    public Elemento_voceBulk getElemento_voce() {
        return elemento_voce;
    }

    public void setElemento_voce(Elemento_voceBulk elemento_voce) {
        this.elemento_voce = elemento_voce;
    }

    /*
     * Ritorna l'informazione circa la totale o parziale associazione della
     * riga a codici SIOPE.
     *
     * return
     * 	"T" = SIOPE_TOTALMENTE_ASSOCIATO
     *  "P" = SIOPE_PARZIALMENTE_ASSOCIATO
     *  "N" = SIOPE_NON_ASSOCIATO
     *
     */
    public String getTipoAssociazioneSiope() {
        BigDecimal totSiope = getIm_associato_siope();
        if (totSiope.compareTo(Utility.ZERO) == 0) return SIOPE_NON_ASSOCIATO;
        if (getIm_da_associare_siope().compareTo(Utility.ZERO) == 0) return SIOPE_TOTALMENTE_ASSOCIATO;
        return SIOPE_PARZIALMENTE_ASSOCIATO;
    }

    public void setToBeDeleted() {
        Iterator i;
        super.setToBeDeleted();
        for (i = reversale_siopeColl.iterator(); i.hasNext(); )
            ((Reversale_siopeBulk) i.next()).setToBeDeleted();
        for (i = reversale_siopeColl.deleteIterator(); i.hasNext(); )
            ((Reversale_siopeBulk) i.next()).setToBeDeleted();
    }

    public BulkList getReversaleCupColl() {
        return reversaleCupColl;
    }

    public void setReversaleCupColl(BulkList reversaleCupColl) {
        this.reversaleCupColl = reversaleCupColl;
    }

    /**
     * Aggiunge un nuovo dettaglio (ReversaleCupBulk) alla lista di dettagli definiti per la reversale
     * inizializzandone alcuni campi
     *
     * @param reversale_cup dettaglio da aggiungere alla lista
     * @return int
     */
    public int addToReversaleCupColl(ReversaleCupBulk reversale_cup) {
        reversaleCupColl.add(reversale_cup);
        reversale_cup.setReversale_riga(this);
        if (reversale_cup.getImporto() == null)
            reversale_cup.setImporto(this.getIm_reversale_riga());
        return reversaleCupColl.size() - 1;
    }

    /**
     * Metodo per l'eliminazione di un elemento <code>ReversaleCupBulk</code> dalla <code>Collection</code>
     * delle righe della reversale.
     *
     * @param index L'indice per scorrere la collezione dei codici cup legati alla riga della reversale.
     * @return ReversaleCupBulk La riga da rimuovere.
     */
    public ReversaleCupBulk removeFromReversaleCupColl(int index) {
        ReversaleCupBulk rev_cup = (ReversaleCupBulk) reversaleCupColl.remove(index);

        return rev_cup;
    }

    public BigDecimal getIm_associato_cup() {
        BigDecimal totale = Utility.ZERO;
        for (Iterator i = getReversaleCupColl().iterator(); i.hasNext(); )
            totale = totale.add(((ReversaleCupBulk) i.next()).getImporto());
        return Utility.nvl(totale);
    }

    public BigDecimal getIm_da_associare_cup() {
        return Utility.nvl(getIm_reversale_riga()).subtract(Utility.nvl(getIm_associato_cup()));
    }

    /*
     * Ritorna l'informazione circa la totale o parziale associazione della
     * riga a codici CUP.
     *
     * return
     * 	"T" = SIOPE_TOTALMENTE_ASSOCIATO
     *  "P" = SIOPE_PARZIALMENTE_ASSOCIATO
     *  "N" = SIOPE_NON_ASSOCIATO
     *
     */
    public String getTipoAssociazioneCup() {
        BigDecimal totCup = getIm_associato_cup();
        if (getIm_da_associare_cup().compareTo(Utility.ZERO) == 0) return SIOPE_TOTALMENTE_ASSOCIATO;
        if (totCup.compareTo(Utility.ZERO) == 0) return SIOPE_NON_ASSOCIATO;
        return SIOPE_PARZIALMENTE_ASSOCIATO;
    }

	public Scrittura_partita_doppiaBulk getScrittura_partita_doppia() {
		throw new IllegalStateException();
	}

	public void setScrittura_partita_doppia(Scrittura_partita_doppiaBulk scrittura_partita_doppia) {
		throw new IllegalStateException();
	}
}
