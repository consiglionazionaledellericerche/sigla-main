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

package it.cnr.contab.anagraf00.core.bulk;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.jada.bulk.BulkList;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Gestione dei dati relativi alla tabella Terzo
 */
@JsonInclude(value = Include.NON_NULL)
public class TerzoBulk extends TerzoBase {
    public final static java.util.Dictionary DEBITORE_CREDITORE;
    public final static String DEBITORE = "D";
    public final static String CREDITORE = "C";
    public final static String ENTRAMBI = "E";
    public final static Integer TERZO_NULLO = new Integer(0);
    public static String ACCESSO_PER_CANCELLAZIONE_BANCA = "CFGANAGCFCOREDELBANM";

    static {
        DEBITORE_CREDITORE = new it.cnr.jada.util.OrderedHashtable();
        DEBITORE_CREDITORE.put(DEBITORE, "Debitore");
        DEBITORE_CREDITORE.put(CREDITORE, "Creditore");
        DEBITORE_CREDITORE.put(ENTRAMBI, "Entrambi");
    }

    private BulkList telefoni = new BulkList();
    private BulkList fax = new BulkList();
    //private List modalita_pagamento_disponibili = new java.util.Vector();
    private BulkList email = new BulkList();
    private BulkList pec = new BulkList();
    //private List rif_termini_pagamento_disponibili  = new it.cnr.jada.util.Collect(termini_pagamento_disponibili,"rif_termini_pagamento");
    private BulkList contatti = new BulkList();
    private BulkList banche = new BulkList();
    private BulkList modalita_pagamento = new BulkList();
    private BulkList termini_pagamento = new BulkList();
    private List rif_termini_pagamento_disponibili = new java.util.Vector();
    private final List rif_modalita_pagamento = new it.cnr.jada.util.Collect(modalita_pagamento, "rif_modalita_pagamento");
    //private List rif_modalita_pagamento_disponibili = new it.cnr.jada.util.Collect(modalita_pagamento_disponibili,"rif_modalita_pagamento");
    private final List rif_termini_pagamento = new it.cnr.jada.util.Collect(termini_pagamento, "rif_termini_pagamento");
    private Map banchePerTipoPagamento = new java.util.Hashtable();
    private AnagraficoBulk anagrafico;
    private ComuneBulk comune_sede;
    private Rappresentante_legaleBulk rappresentante_legale;
    private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa;
    private java.util.Collection caps_comune;
    private boolean find_uoRO = false;
    private boolean terzo_speciale;
    private boolean dipendente;
    private boolean notGestoreIstat;
    // CODICE_FISCALE_ANAGRAFICO VARCHAR(20)
    private java.lang.String codice_fiscale_anagrafico;

    // PARTITA_IVA_ANAGRAFICO VARCHAR(20)
    private java.lang.String partita_iva_anagrafico;

    private java.lang.String pecForRest;

    public TerzoBulk() {
        super();
    }

    public TerzoBulk(java.lang.Integer cd_terzo) {
        super(cd_terzo);
    }

    /**
     * Metodo per l'aggiunta di un elemento <code>BancaBulk</code> alla <code>BulkList</code>
     * delle banche relative al terzo.
     *
     * @param banca Banca da aggiungere.
     * @return la posizione nella lista
     * @see removeFromBanche
     */

    public int addToBanche(BancaBulk banca) {

        //java.util.List coll = (java.util.List)banchePerTipoPagamento.get(banca.getChiave());
        //if (coll == null){
        //banchePerTipoPagamento.put(banca.getChiave(),coll = new java.util.Vector());
        //} else if (banca.isPerCessione() && existBancaInCollection(coll, banca)){
        //return coll.size()-1;
        ////for (java.util.Iterator i = coll.iterator(); i.hasNext();){
        ////BancaBulk existBanca = (BancaBulk)i.next();
        ////if (banca.equalsByPrimaryKey(existBanca))
        ////return coll.size()-1;
        ////}
        //}

        //banca.setPg_banca(null);
        //banche.add(banca);
        //banca.setTerzo(this);

        //coll.add(banca);
        //return coll.size()-1;


        banca.setPg_banca(null);
        banche.add(banca);
        banca.setTerzo(this);

        java.util.List coll = (java.util.List) banchePerTipoPagamento.get(banca.getChiave());
        if (coll == null)
            banchePerTipoPagamento.put(banca.getChiave(), coll = new java.util.Vector());
        coll.add(banca);
        return coll.size() - 1;

    }

    /**
     * Metodo per l'aggiunta di un elemento <code>ContattoBulk</code> alla <code>BulkList</code>
     * dei contatti relativi al terzo.
     *
     * @param contatto Contatto da aggiungere.
     * @return la posizione nella lista
     * @see removeFromContatti
     */

    public int addToContatti(ContattoBulk contatto) {
        contatto.setTerzo(this);
        contatti.add(contatto);
        return contatti.size() - 1;
    }

    /**
     * Metodo per l'aggiunta di un elemento <code>TelefonoBulk</code> alla <code>BulkList</code>
     * dei telefoni relativi al terzo.
     *
     * @param telefono Telefono da aggiungere.
     * @return la posizione nella lista
     * @see removeFromEmail
     */

    public int addToEmail(TelefonoBulk telefono) {
        telefono.setTi_riferimento(TelefonoBulk.EMAIL);
        telefono.setFattElettronica(false);
        telefono.setTerzo(this);
        email.add(telefono);
        return email.size() - 1;
    }

    public int addToPec(TelefonoBulk telefono) {
        telefono.setTi_riferimento(TelefonoBulk.PEC);
        telefono.setFattElettronica(Optional.ofNullable(telefono.getFattElettronica()).orElse(false));
        telefono.setTerzo(this);
        pec.add(telefono);
        return pec.size() - 1;
    }

    public Boolean esistePecFatturazioneElettronica() {
		return getPecFatturazioneElettronica() != null;
	}

    public Boolean inseriteDiversePecFatturazioneElettronica() {
        Boolean esistePec = false;
        for (java.util.Iterator i = pec.iterator(); i.hasNext(); ) {
            TelefonoBulk emailPec = (TelefonoBulk) i.next();
            if (emailPec.getFattElettronica()) {
                if (!esistePec) {
                    esistePec = true;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean inseriteDiverseMailFatturazioneElettronica() {
        Boolean esisteMail = false;
        for (java.util.Iterator i = email.iterator(); i.hasNext(); ) {
            TelefonoBulk email = (TelefonoBulk) i.next();
            if (email.getFattElettronica()) {
                if (!esisteMail) {
                    esisteMail = true;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public TelefonoBulk getPecFatturazioneElettronica() {
        for (java.util.Iterator i = pec.iterator(); i.hasNext(); ) {
            TelefonoBulk emailPec = (TelefonoBulk) i.next();
            if (emailPec.getFattElettronica())
                return emailPec;
        }

        return null;
    }

    public TelefonoBulk getEmailFatturazioneElettronica() {
        for (java.util.Iterator i = email.iterator(); i.hasNext(); ) {
            TelefonoBulk emailPec = (TelefonoBulk) i.next();
            if (emailPec.getFattElettronica())
                return emailPec;
        }

        return null;
    }

    /**
     * Metodo per l'aggiunta di un elemento <code>TelefonoBulk</code> alla <code>BulkList</code>
     * dei telefoni relativi al terzo.
     *
     * @param telefono Fax da aggiungere.
     * @return la posizione nella lista
     * @see removeFromFax
     */

    public int addToFax(TelefonoBulk telefono) {
        telefono.setTi_riferimento(TelefonoBulk.FAX);
        telefono.setFattElettronica(false);
        telefono.setTerzo(this);
        fax.add(telefono);
        return fax.size() - 1;
    }

    /**
     * Metodo per l'aggiunta di un elemento <code>Modalita_pagamentoBulk</code> alla <code>BulkList</code>
     * delle modalita di pagamento relative al terzo.
     *
     * @param telefono Modalita pagamento da aggiungere.
     * @return la posizione nella lista
     * @see removeFromModalita_pagamento
     */

    public int addToModalita_pagamento(Modalita_pagamentoBulk modalita_pagamento) {
        this.modalita_pagamento.add(modalita_pagamento);
        modalita_pagamento.setTerzo(this);
        return this.modalita_pagamento.size() - 1;
    }

    /**
     * Metodo per l'aggiunta di un elemento <code>TelefonoBulk</code> alla <code>BulkList</code>
     * dei telefoni relativi al terzo.
     *
     * @param telefono Telefono da aggiungere.
     * @return la posizione nella lista
     * @see removeFromTelefoni
     */

    public int addToTelefoni(TelefonoBulk telefono) {
        telefono.setTi_riferimento(TelefonoBulk.TEL);
        telefono.setFattElettronica(false);
        telefono.setTerzo(this);
        telefoni.add(telefono);
        return telefoni.size() - 1;
    }

    /**
     * Restituisce l'<code>AnagraficoBulk</code> a cui l'oggetto è correlato.
     *
     * @return it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk
     * @see setAnagrafico
     */

    public AnagraficoBulk getAnagrafico() {
        return anagrafico;
    }

    /**
     * Imposta l'<code>AnagraficoBulk</code> a cui l'oggetto è correlato.
     *
     * @param newAnagrafico Anagrafica di riferimento.
     * @see getAnagrafico
     */

    public void setAnagrafico(AnagraficoBulk newAnagrafico) {
        anagrafico = newAnagrafico;
    }

    /**
     * Restituisce la <code>BulkList</code> contenente l'elenco di <code>BancaBulk</code> in
     * relazione con l'oggetto.
     *
     * @return it.cnr.jada.bulk.BulkList
     * @see setBanche
     */

    public it.cnr.jada.bulk.BulkList getBanche() {
        return banche;
    }

    public void setBanche(it.cnr.jada.bulk.BulkList banche) {
        this.banche = banche;
        banchePerTipoPagamento = new java.util.Hashtable();
        for (java.util.Iterator i = banche.iterator(); i.hasNext(); ) {
            BancaBulk banca = (BancaBulk) i.next();
            //java.util.List coll = (java.util.List)banchePerTipoPagamento.get(banca.getTi_pagamento());
            java.util.List coll = (java.util.List) banchePerTipoPagamento.get(banca.getChiave());
            if (coll == null)
                //banchePerTipoPagamento.put(banca.getTi_pagamento(),coll = new java.util.Vector());
                banchePerTipoPagamento.put(banca.getChiave(), coll = new java.util.Vector());
            coll.add(banca);
        }
    }

    /**
     * Restituisce l'elenco di <code>BancaBulk</code> con la modalita pagamento specifinata
     * dal parametro in ingresso e in relazione con l'oggetto.
     *
     * @param modalita_pagamento <code>Modalita_pagamentoBulk</code> che specifica la modalita richiesta.
     * @return java.util.List
     * @see setBanche
     */

    public java.util.List getBanche(it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk modalita_pagamento) {

        if (modalita_pagamento == null)
            return java.util.Collections.EMPTY_LIST;


        java.util.List coll = (java.util.List) banchePerTipoPagamento.get(modalita_pagamento.getChiavePerBanca());

        //java.util.List coll = (java.util.List)banchePerTipoPagamento.get(modalita_pagamento.getRif_modalita_pagamento().getTi_pagamento());

        if (coll == null)
            return java.util.Collections.EMPTY_LIST;

        return coll;
    }

    /**
     * Restituisce l'elenco di <code>BancaBulk</code> con il tipo di modalita pagamento specifinata
     * dal parametro in ingresso e in relazione con l'oggetto.
     *
     * @param modalita_pagamento <code>Rif_modalita_pagamentoBulk</code> che specifica la modalita richiesta.
     * @return java.util.List
     * @see setBanche
     */

    public java.util.List getBanche(it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk modalita_pagamento) {
        if (modalita_pagamento == null) return java.util.Collections.EMPTY_LIST;
        java.util.List coll = (java.util.List) banchePerTipoPagamento.get(modalita_pagamento.getTi_pagamento());
        if (coll == null) return java.util.Collections.EMPTY_LIST;
        return coll;
    }

    /**
     * Insert the method's description here.
     * Creation date: (11/11/2002 15.47.35)
     *
     * @return java.util.Map
     */
    public java.util.Map getBanchePerTipoPagamento() {
        return banchePerTipoPagamento;
    }

    /**
     * Restituisce le liste di componenti correlati con l'oggetto terzo.
     *
     * @return it.cnr.jada.bulk.BulkCollection[]
     */

    public it.cnr.jada.bulk.BulkCollection[] getBulkLists() {
        return new it.cnr.jada.bulk.BulkCollection[]{
                telefoni,
                fax,
                email,
                pec,
                contatti,
                banche,
                termini_pagamento,
                modalita_pagamento
        };
    }

    /**
     * Restituisce l'elenco dei cap relativi al comune della sede del terzo.
     *
     * @return java.util.Collection
     * @see setCaps_comune
     */

    public java.util.Collection getCaps_comune() {
        return caps_comune;
    }

    /**
     * Imposta l'elenco dei cap relativi al comune della sede del terzo.
     *
     * @param newCaps_comune l'elenco.
     * @see getCaps_comune
     */

    public void setCaps_comune(java.util.Collection newCaps_comune) {
        caps_comune = newCaps_comune;
    }

    public java.lang.Integer getCd_anag() {
        it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk anagrafico = this.getAnagrafico();
        if (anagrafico == null)
            return null;
        return anagrafico.getCd_anag();
    }

    public void setCd_anag(java.lang.Integer cd_anag) {
        this.getAnagrafico().setCd_anag(cd_anag);
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
     * Restituisce il <code>ComuneBulk</code> descrittivo del comune della sede.
     *
     * @return it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk
     * @see setComune_sede
     */

    public it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk getComune_sede() {
        return comune_sede;
    }

    /**
     * Imposta il <code>ComuneBulk</code> descrittivo del comune della sede.
     *
     * @param newComune_sede il comune.
     * @see getComune_sede
     */

    public void setComune_sede(it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk newComune_sede) {
        comune_sede = newComune_sede;
    }

    /**
     * Restituisce la <code>BulkList</code> contenente l'elenco di <code>ContattoBulk</code> in
     * relazione con l'oggetto.
     *
     * @return it.cnr.jada.bulk.BulkList
     * @see setContatti
     */

    public it.cnr.jada.bulk.BulkList getContatti() {
        return contatti;
    }

    /**
     * Imposta la <code>BulkList</code> contenente l'elenco di <code>ContattoBulk</code> in
     * relazione con l'oggetto.
     *
     * @param newContatti la lista.
     * @see getContatti
     */

    public void setContatti(it.cnr.jada.bulk.BulkList newContatti) {
        contatti = newContatti;
    }

    /**
     * Restituisce una <code>BulkList</code> contenente l'elenco delle E-mail in
     * relazione con l'oggetto.
     *
     * @return it.cnr.jada.bulk.BulkList
     * @see setEmail
     */

    public it.cnr.jada.bulk.BulkList getEmail() {
        return email;
    }

    /**
     * Imposta una <code>BulkList</code> contenente l'elenco delle E-mail in
     * relazione con l'oggetto.
     *
     * @param newEmail la lista.
     * @see getEmail
     */

    public void setEmail(it.cnr.jada.bulk.BulkList newEmail) {
        email = newEmail;
    }

    public it.cnr.jada.bulk.BulkList getPec() {
        return pec;
    }

    public void setPec(it.cnr.jada.bulk.BulkList newPec) {
        pec = newPec;
    }

    /**
     * Restituisce una <code>BulkList</code> contenente l'elenco dei Fax in
     * relazione con l'oggetto.
     *
     * @return it.cnr.jada.bulk.BulkList
     * @see setFax
     */

    public it.cnr.jada.bulk.BulkList getFax() {
        return fax;
    }

    /**
     * Imposta una <code>BulkList</code> contenente l'elenco dei Fax in
     * relazione con l'oggetto.
     *
     * @param newFax la lista.
     * @see getFax
     */

    public void setFax(it.cnr.jada.bulk.BulkList newFax) {
        fax = newFax;
    }

    /**
     * Insert the method's description here.
     * Creation date: (29/07/2002 17:13:50)
     *
     * @return it.cnr.jada.bulk.BulkList
     */
    public it.cnr.jada.bulk.BulkList<Modalita_pagamentoBulk> getModalita_pagamento() {
        return modalita_pagamento;
    }

    /**
     * Insert the method's description here.
     * Creation date: (29/07/2002 17:13:50)
     *
     * @param newModalita_pagamento it.cnr.jada.bulk.BulkList
     */
    public void setModalita_pagamento(it.cnr.jada.bulk.BulkList newModalita_pagamento) {
        modalita_pagamento = newModalita_pagamento;
    }

    public java.lang.Long getPg_comune_sede() {
        it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk comune_sede = this.getComune_sede();
        if (comune_sede == null)
            return null;
        return comune_sede.getPg_comune();
    }

    public void setPg_comune_sede(java.lang.Long pg_comune_sede) {
        this.getComune_sede().setPg_comune(pg_comune_sede);
    }

    public java.lang.Long getPg_rapp_legale() {
        it.cnr.contab.anagraf00.core.bulk.Rappresentante_legaleBulk rappresentante_legale = this.getRappresentante_legale();
        if (rappresentante_legale == null)
            return null;
        return rappresentante_legale.getPg_rapp_legale();
    }

    public void setPg_rapp_legale(java.lang.Long pg_rapp_legale) {
        this.getRappresentante_legale().setPg_rapp_legale(pg_rapp_legale);
    }

    /**
     * Restituisce il <code>Rappresentante_legaleBulk</code> contenente la descrizione del rappresentante legale
     * in relazione con l'oggetto.
     *
     * @return Rappresentante_legaleBulk
     * @see setModalita_pagamento
     */

    public Rappresentante_legaleBulk getRappresentante_legale() {
        return rappresentante_legale;
    }

    /**
     * Imposta il <code>Rappresentante_legaleBulk</code> contenente la descrizione del rappresentante legale
     * in relazione con l'oggetto.
     *
     * @param newRappresentante_legale il rappresentante legale.
     * @see getModalita_pagamento
     */

    public void setRappresentante_legale(Rappresentante_legaleBulk newRappresentante_legale) {
        rappresentante_legale = newRappresentante_legale;
    }

    /**
     * Restituisce la lista contenente l'elenco di <code>Rif_modalita_pagamentoBulk</code> esistenti
     * per l'oggetto.
     *
     * @return java.util.List
     * @see setRif_modalita_pagamento_disponibili
     */

    public java.util.List getRif_modalita_pagamento() {
        return rif_modalita_pagamento;
    }

    /**
     * Restituisce la lista contenente l'elenco di <code>Rif_termini_pagamentoBulk</code> esistenti
     * per l'oggetto.
     *
     * @return java.util.List
     * @see setRif_termini_pagamento_disponibili
     */

    public java.util.List getRif_termini_pagamento() {
        return rif_termini_pagamento;
    }

    /**
     * Insert the method's description here.
     * Creation date: (30/07/2002 17:38:10)
     *
     * @return java.util.List
     */
    public java.util.List getRif_termini_pagamento_disponibili() {
        return rif_termini_pagamento_disponibili;
    }

    /**
     * Insert the method's description here.
     * Creation date: (30/07/2002 17:38:10)
     *
     * @param newRif_termini_pagamento_disponibili java.util.List
     */
    public void setRif_termini_pagamento_disponibili(java.util.List newRif_termini_pagamento_disponibili) {
        rif_termini_pagamento_disponibili = newRif_termini_pagamento_disponibili;
    }

    /**
     * Restituisce una <code>BulkList</code> contenente l'elenco di Telefoni in
     * relazione con l'oggetto.
     *
     * @return it.cnr.jada.bulk.BulkList
     * @see setTelefoni
     */

    public it.cnr.jada.bulk.BulkList getTelefoni() {
        return telefoni;
    }

    /**
     * Imposta una <code>BulkList</code> contenente l'elenco di Telefoni in
     * relazione con l'oggetto.
     *
     * @param newTelefoni la lista
     * @see getTelefoni
     */

    public void setTelefoni(it.cnr.jada.bulk.BulkList newTelefoni) {
        telefoni = newTelefoni;
    }

    /**
     * Insert the method's description here.
     * Creation date: (29/07/2002 17:13:36)
     *
     * @return it.cnr.jada.bulk.BulkList
     */
    public it.cnr.jada.bulk.BulkList getTermini_pagamento() {
        return termini_pagamento;
    }

    /**
     * Insert the method's description here.
     * Creation date: (29/07/2002 17:13:36)
     *
     * @param newTermini_pagamento it.cnr.jada.bulk.BulkList
     */
    public void setTermini_pagamento(it.cnr.jada.bulk.BulkList newTermini_pagamento) {
        ((it.cnr.jada.util.Collect) rif_termini_pagamento).setList(termini_pagamento = newTermini_pagamento);
    }

    /**
     * Restituisce il <code>Dictionary</code> per la gestione del tipo di terzo.
     *
     * @return java.util.Dictionary
     */

    public java.util.Dictionary getTi_terzoKeys() {
        return DEBITORE_CREDITORE;
    }

    /**
     * Restituisce l'<code>Unita_organizzativaBulk</code> contenente la descrizione dell'unità organizzativa
     * relazionata con l'oggetto.
     *
     * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
     * @see setUnita_organizzativa
     */

    public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUnita_organizzativa() {
        return unita_organizzativa;
    }

    /**
     * Imposta l'<code>Unita_organizzativaBulk</code> contenente la descrizione dell'unità organizzativa
     * relazionata con l'oggetto.
     *
     * @param newUnita_organizzativa l'unità organizzativa.
     * @see getUnita_organizzativa
     */

    public void setUnita_organizzativa(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUnita_organizzativa) {
        unita_organizzativa = newUnita_organizzativa;
    }

    public boolean isAnagraficoScaduto() {
        return anagrafico != null && anagrafico.getDt_fine_rapporto() != null;
    }

    /**
     * Insert the method's description here.
     * Creation date: (30/10/2002 17.31.26)
     *
     * @return boolean
     */
    public boolean isDipendente() {
        return dipendente;
    }

    /**
     * Insert the method's description here.
     * Creation date: (30/10/2002 17.31.26)
     *
     * @param newDipendente boolean
     */
    public void setDipendente(boolean newDipendente) {
        dipendente = newDipendente;
    }

    /**
     * Indica quando unita_organizzativa deve essere read only.
     *
     * @return boolean
     */

    public boolean isFind_uoRO() {
        return find_uoRO;
    }

    /**
     *
     */

    public void setFind_uoRO(boolean newFind_uoRO) {
        find_uoRO = newFind_uoRO;
    }

    /**
     * Indica quando comune_sede deve essere read only.
     *
     * @return boolean
     */

    public boolean isROcomune_sede() {
        return comune_sede == null || comune_sede.getCrudStatus() == NORMAL;
    }

    /**
     * Indica quando rappresentante_legale deve essere read only.
     *
     * @return boolean
     */

    public boolean isROrappresentante_legale() {
        return rappresentante_legale == null || rappresentante_legale.getCrudStatus() == NORMAL;
    }

    /**
     * Indica quando unita_organizzativa deve essere read only.
     *
     * @return boolean
     */

    public boolean isROunita_organizzativa() {
        return isFind_uoRO() || unita_organizzativa == null || unita_organizzativa.getCrudStatus() == NORMAL;
    }

    /**
     * Insert the method's description here.
     * Creation date: (31/07/2002 14:46:30)
     *
     * @return boolean
     */
    public boolean isTerzo_speciale() {
        return terzo_speciale;
    }

    /**
     * Insert the method's description here.
     * Creation date: (31/07/2002 14:46:30)
     *
     * @param newTerzo_speciale boolean
     */
    public void setTerzo_speciale(boolean newTerzo_speciale) {
        terzo_speciale = newTerzo_speciale;
    }

    /**
     * Elimina il <code>BancaBulk</code> alla posizione index dalla lista
     * banche.
     *
     * @param index Indice dell'elemento da cancellare.
     * @return BancaBulk
     * @see addToBanche
     */

    public BancaBulk removeFromBanche(int index) {
        //return null;

        BancaBulk banca = (BancaBulk) getBanche().get(index);

        return (BancaBulk) getBanche().remove(index);
    }

    /**
     * Elimina il <code>BancaBulk</code> alla posizione index dalla lista
     * banche.
     *
     * @param index Indice dell'elemento da cancellare.
     * @return BancaBulk
     * @see addToBanche
     */

    public BancaBulk removeFromBanche(BancaBulk banca, int index) {
        banche.remove(banca);
        if (banca != null) {
            //java.util.List coll = (java.util.List)banchePerTipoPagamento.get(banca.getTi_pagamento());
            java.util.List coll = (java.util.List) banchePerTipoPagamento.get(banca.getChiave());
            if (coll != null)
                coll.remove(index);
        }
        return banca;
    }

    /**
     * Elimina il <code>ContattoBulk</code> alla posizione index dalla lista
     * contatti.
     *
     * @param index Indice dell'elemento da cancellare.
     * @return ContattoBulk
     * @see addToContatti
     */

    public ContattoBulk removeFromContatti(int index) {
        return (ContattoBulk) contatti.remove(index);
    }

    /**
     * Elimina il <code>TelefonoBulk</code> alla posizione index dalla lista
     * email.
     *
     * @param index Indice dell'elemento da cancellare.
     * @return TelefonoBulk
     * @see addToEmail
     */

    public TelefonoBulk removeFromEmail(int index) {
        return (TelefonoBulk) email.remove(index);
    }

    public TelefonoBulk removeFromPec(int index) {
        return (TelefonoBulk) pec.remove(index);
    }

    /**
     * Elimina il <code>TelefonoBulk</code> alla posizione index dalla lista
     * fax.
     *
     * @param index Indice dell'elemento da cancellare.
     * @return TelefonoBulk
     * @see addToFax
     */

    public TelefonoBulk removeFromFax(int index) {
        return (TelefonoBulk) fax.remove(index);
    }

    /**
     * Elimina il <code>Modalita_pagamentoBulk</code> alla posizione index dalla lista
     * modalita_pagamento e lo aggiunge alla lista modalita_pagamento_disponibili.
     *
     * @param index Indice dell'elemento da cancellare.
     * @return Modalita_pagamentoBulk
     * @see addToModalita_pagamento
     */

    public Modalita_pagamentoBulk removeFromModalita_pagamento(int index) {
        Modalita_pagamentoBulk modalita_pagamento = (Modalita_pagamentoBulk) this.modalita_pagamento.remove(index);
        //this.modalita_pagamento_disponibili.add(modalita_pagamento);
        String ti_pagamento = modalita_pagamento.getRif_modalita_pagamento().getTi_pagamento();
        for (java.util.Iterator i = this.modalita_pagamento.iterator(); i.hasNext(); ) {
            Modalita_pagamentoBulk m = (Modalita_pagamentoBulk) i.next();
            if (ti_pagamento.equals(m.getRif_modalita_pagamento().getTi_pagamento()))
                return modalita_pagamento;
        }
        //banchePerTipoPagamento.remove(ti_pagamento);
        banchePerTipoPagamento.remove(modalita_pagamento.getChiavePerBanca());

        for (java.util.Iterator i = banche.iterator(); i.hasNext(); ) {
            BancaBulk banca = (BancaBulk) i.next();
            //if (ti_pagamento.equals(banca.getTi_pagamento())) {
            //i.remove();
            //banca.setToBeDeleted();
            //}
            if (modalita_pagamento.getChiavePerBanca().equals(banca.getChiave())) {
                i.remove();
                banca.setToBeDeleted();
            }
        }
        return modalita_pagamento;
    }

    public Rif_termini_pagamentoBulk removeFromRif_termini_pagamento(int index) {
        Termini_pagamentoBulk termini_pagamento = (Termini_pagamentoBulk) this.termini_pagamento.remove(index);
        termini_pagamento.setToBeDeleted();
        this.rif_termini_pagamento_disponibili.add(termini_pagamento.getRif_termini_pagamento());
        return termini_pagamento.getRif_termini_pagamento();
    }

    public Rif_termini_pagamentoBulk removeFromRif_termini_pagamento_disponibili(int index) {
        Rif_termini_pagamentoBulk rif_termini_pagamento = (Rif_termini_pagamentoBulk) this.rif_termini_pagamento_disponibili.remove(index);
        Termini_pagamentoBulk termini_pagamento = new Termini_pagamentoBulk();
        termini_pagamento.setToBeCreated();
        termini_pagamento.setTerzo(this);
        termini_pagamento.setRif_termini_pagamento(rif_termini_pagamento);
        this.termini_pagamento.add(termini_pagamento);
        return rif_termini_pagamento;
    }

    /**
     * Elimina il <code>TelefonoBulk</code> alla posizione index dalla lista telefoni.
     *
     * @param index Indice dell'elemento da cancellare.
     * @return TelefonoBulk
     * @see addToTelefoni
     */

    public TelefonoBulk removeFromTelefoni(int index) {
        return (TelefonoBulk) telefoni.remove(index);
    }

    /**
     * Elimina il <code>Termini_pagamentoBulk</code> alla posizione index dalla lista
     * termini_pagamento e lo aggiunge alla lista termini_pagamento_disponibili.
     *
     * @param index Indice dell'elemento da cancellare.
     * @return Termini_pagamentoBulk
     * @see addToTermini_pagamento
     */

    public Termini_pagamentoBulk removeFromTermini_pagamento(int index) {
        Termini_pagamentoBulk termini_pagamento = (Termini_pagamentoBulk) this.termini_pagamento.remove(index);
        this.rif_termini_pagamento_disponibili.add(termini_pagamento.getRif_termini_pagamento());
        return termini_pagamento;
    }

    public boolean isStudioAssociato() {
        return Optional.ofNullable(getAnagrafico()).map(AnagraficoBulk::getFl_studio_associato).orElse(false);
    }

    public java.lang.String getCodice_fiscale_anagrafico() {
        it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk anagrafico = this.getAnagrafico();
        if (anagrafico == null)
            return null;
        return anagrafico.getCodice_fiscale();
    }

    public void setCodice_fiscale_anagrafico(java.lang.String codiceFiscaleAnagrafico) {
        this.getAnagrafico().setCodice_fiscale(codiceFiscaleAnagrafico);
    }

    public java.lang.String getPartita_iva_anagrafico() {
        it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk anagrafico = this.getAnagrafico();
        if (anagrafico == null)
            return null;
        return anagrafico.getPartita_iva();
    }

    public void setPartita_iva_anagrafico(java.lang.String partitaIvaAnagrafico) {
        this.getAnagrafico().setPartita_iva(partitaIvaAnagrafico);
    }

    public boolean isNotGestoreIstat() {
        return notGestoreIstat;
    }

    public void setNotGestoreIstat(boolean notGestoreIstat) {
        this.notGestoreIstat = notGestoreIstat;
    }

    public java.lang.String getPecForRest() {
        return pecForRest;
    }

    public void setPecForRest(java.lang.String pecForRest) {
        this.pecForRest = pecForRest;
    }
}
