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

package it.cnr.contab.docamm00.docs.bulk;

import java.util.Calendar;
import java.util.Dictionary;
import java.util.Optional;

import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk;
import it.cnr.contab.config00.bulk.CigBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk;
import it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.ordmag.ordini.bulk.FatturaOrdineBulk;
import it.cnr.contab.util.enumeration.TipoIVA;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;

public abstract class Fattura_passiva_rigaBulk
        extends Fattura_passiva_rigaBase
        implements IDocumentoAmministrativoRigaBulk,
        Voidable {

    public final static String STATO_INIZIALE = "I";
    public final static String STATO_CONTABILIZZATO = "C";
    public final static String STATO_PAGATO = "P";
    public final static String STATO_ANNULLATO = "A";
    public final static String NON_ASSOCIATO_A_MANDATO = "N";
    public final static String ASSOCIATO_A_MANDATO = "T";
    public final static Dictionary TIPO;
    public final static Dictionary STATO;
    public final static Dictionary STATO_MANDATO;
    public final static Dictionary STATI_RIPORTO;

    static {
        TIPO = new it.cnr.jada.util.OrderedHashtable();
        for (TipoIVA tipoIVA : TipoIVA.values()) {
            TIPO.put(tipoIVA.value(), tipoIVA.label());
        }

        STATO = new it.cnr.jada.util.OrderedHashtable();
        STATO.put(STATO_INIZIALE, "Iniziale");
        STATO.put(STATO_CONTABILIZZATO, "Contabilizzato");
        STATO.put(STATO_PAGATO, "Pagato");
        STATO.put(STATO_ANNULLATO, "Annullato");

        STATO_MANDATO = new it.cnr.jada.util.OrderedHashtable();
        STATO_MANDATO.put(NON_ASSOCIATO_A_MANDATO, "Man/rev non associato");
        STATO_MANDATO.put(ASSOCIATO_A_MANDATO, "Man/rev associato");

        STATI_RIPORTO = new it.cnr.jada.util.OrderedHashtable();
        STATI_RIPORTO.put(NON_RIPORTATO, "Non riportata");
        STATI_RIPORTO.put(RIPORTATO, "Riportata");
    }

    // TI_PROMISCUO CHAR(1) NOT NULL
    protected java.lang.String ti_promiscuo;
    protected TerzoBulk fornitore;
    protected TerzoBulk cessionario;
    protected BancaBulk banca;
    protected Rif_modalita_pagamentoBulk modalita_pagamento;
    protected Rif_termini_pagamentoBulk termini_pagamento;
    private Bene_servizioBulk bene_servizio;
    private Voce_ivaBulk voce_iva;
    private Obbligazione_scadenzarioBulk obbligazione_scadenziario;
    private boolean inventariato = false;
    private java.math.BigDecimal im_totale_inventario = null;
    private java.lang.String riportata = NON_RIPORTATO;
    private java.util.Collection banche;
    private java.util.Collection modalita;
    private java.util.Collection termini;
    private BulkList<FatturaOrdineBulk> fatturaOrdineColl = new BulkList();
	private CigBulk cig;

    private TrovatoBulk trovato = new TrovatoBulk(); // inizializzazione necessaria per i bulk non persistenti
    private Boolean collegatoCapitoloPerTrovato = false;

    public Fattura_passiva_rigaBulk() {
        super();
    }

    public Fattura_passiva_rigaBulk(java.lang.String cd_cds, java.lang.String cd_unita_organizzativa, java.lang.Integer esercizio, java.lang.Long pg_fattura_passiva, java.lang.Long progressivo_riga) {
        super(cd_cds, cd_unita_organizzativa, esercizio, pg_fattura_passiva, progressivo_riga);
    }

    public void calcolaCampiDiRiga() {

        if (getQuantita() == null) setQuantita(new java.math.BigDecimal(1));
        if (getPrezzo_unitario() == null) setPrezzo_unitario(new java.math.BigDecimal(0));

        setIm_totale_divisa(getQuantita().multiply(getPrezzo_unitario()).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
        java.math.BigDecimal imp_divisa = new java.math.BigDecimal(0);
        java.math.BigDecimal change = getFattura_passiva().getCambio();
        if (change == null) {
            change = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
            getFattura_passiva().setCambio(change);
        }
        imp_divisa = (getFattura_passiva().getChangeOperation() == Fattura_passivaBulk.MOLTIPLICA) ?
                getIm_totale_divisa().multiply(change) :
                getIm_totale_divisa().divide(change, java.math.BigDecimal.ROUND_HALF_UP);
        setIm_imponibile(imp_divisa.setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
        if (!getFl_iva_forzata().booleanValue()) {
            if (voce_iva != null && voce_iva.getPercentuale() != null)
                setIm_iva(imp_divisa.multiply(voce_iva.getPercentuale()).divide(new java.math.BigDecimal(100), 2, java.math.BigDecimal.ROUND_HALF_UP));
            else
                setIm_iva(new java.math.BigDecimal(0));
        }
    }

    public boolean checkIfRiportata() {

        return !isPagata() &&
                (getObbligazione_scadenziario() != null &&
                        !getObbligazione_scadenziario().getEsercizio().equals(getEsercizio()));
    }

    public IDocumentoAmministrativoRigaBulk getAssociatedDetail() {

        return null;
    }

    /**
     * Insert the method's description here.
     * Creation date: (9/7/2001 3:39:39 PM)
     *
     * @return it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk
     */
    public Bene_servizioBulk getBene_servizio() {
        return bene_servizio;
    }

    /**
     * Insert the method's description here.
     * Creation date: (9/7/2001 3:39:39 PM)
     *
     * @param newBene_servizio it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk
     */
    public void setBene_servizio(Bene_servizioBulk newBene_servizio) {
        bene_servizio = newBene_servizio;
    }

    public java.lang.String getCd_cds() {
        it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk fattura_passiva = this.getFattura_passiva();
        if (fattura_passiva == null)
            return null;
        return fattura_passiva.getCd_cds();
    }

    public void setCd_cds(java.lang.String cd_cds) {
        this.getFattura_passiva().setCd_cds(cd_cds);
    }

    public java.lang.String getCd_unita_organizzativa() {
        it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk fattura_passiva = this.getFattura_passiva();
        if (fattura_passiva == null)
            return null;
        return fattura_passiva.getCd_unita_organizzativa();
    }

    public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
        this.getFattura_passiva().setCd_unita_organizzativa(cd_unita_organizzativa);
    }

    public java.lang.String getCd_voce_iva() {
        it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk voce_iva = this.getVoce_iva();
        if (voce_iva == null)
            return null;
        return voce_iva.getCd_voce_iva();
    }

    public void setCd_voce_iva(java.lang.String cd_voce_iva) {
        this.getVoce_iva().setCd_voce_iva(cd_voce_iva);
    }

    public java.lang.Integer getEsercizio() {
        it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk fattura_passiva = this.getFattura_passiva();
        if (fattura_passiva == null)
            return null;
        return fattura_passiva.getEsercizio();
    }

    public void setEsercizio(java.lang.Integer esercizio) {
        this.getFattura_passiva().setEsercizio(esercizio);
    }

    /**
     * Insert the method's description here.
     * Creation date: (12/14/2001 2:38:43 PM)
     *
     * @return it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk
     */
    public IDocumentoAmministrativoBulk getFather() {

        return getFattura_passiva();
    }

    /**
     * Insert the method's description here.
     * Creation date: (9/10/2001 5:51:50 PM)
     *
     * @return it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk
     */
    public abstract Fattura_passivaBulk getFattura_passiva();

    /**
     * Insert the method's description here.
     * Creation date: (9/10/2001 5:51:50 PM)
     *
     * @return it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk
     */
    public abstract void setFattura_passiva(Fattura_passivaBulk fattura_passiva);

    /**
     * Insert the method's description here.
     * Creation date: (2/14/2002 5:58:01 PM)
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getIm_totale_inventario() {

        if (TipoIVA.ISTITUZIONALE.value().equalsIgnoreCase(getTi_istituz_commerc()))
            return getIm_imponibile().add(getIm_iva());
        return getIm_imponibile();
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/14/2002 5:58:01 PM)
     *
     * @param newIm_totale_inventario java.math.BigDecimal
     */
    public void setIm_totale_inventario(java.math.BigDecimal newIm_totale_inventario) {
        im_totale_inventario = newIm_totale_inventario;
    }

    public java.math.BigDecimal getImportoSignForDelete(java.math.BigDecimal importo) {

        if (importo == null) return null;
        return importo.negate();
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/16/2001 12:06:24 PM)
     *
     * @return it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk
     */
    public Obbligazione_scadenzarioBulk getObbligazione_scadenziario() {
        return obbligazione_scadenziario;
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/16/2001 12:06:24 PM)
     *
     * @param newObbligazione it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk
     */
    public void setObbligazione_scadenziario(Obbligazione_scadenzarioBulk newObbligazione) {
        obbligazione_scadenziario = newObbligazione;
    }

    public java.lang.Long getPg_fattura_passiva() {
        it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk fattura_passiva = this.getFattura_passiva();
        if (fattura_passiva == null)
            return null;
        return fattura_passiva.getPg_fattura_passiva();
    }

    public void setPg_fattura_passiva(java.lang.Long pg_fattura_passiva) {
        this.getFattura_passiva().setPg_fattura_passiva(pg_fattura_passiva);
    }

    /**
     * Insert the method's description here.
     * Creation date: (30/05/2003 16.17.42)
     *
     * @return java.lang.String
     */
    public java.lang.String getRiportata() {
        return riportata;
    }

    /**
     * Insert the method's description here.
     * Creation date: (12/14/2001 2:38:43 PM)
     *
     * @return it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk
     */
    public IScadenzaDocumentoContabileBulk getScadenzaDocumentoContabile() {

        return (IScadenzaDocumentoContabileBulk) getObbligazione_scadenziario();
    }

    /**
     * Restituisce il <code>Dictionary</code> per la gestione dei tipi di fattura.
     *
     * @return java.util.Dictionary
     */

    public Dictionary getStato_cofiKeys() {
        return STATO;
    }

    /*
     * Getter dell'attributo ti_associato_manrev
     */
    public Dictionary getTi_associato_manrevKeys() {
        return STATO_MANDATO;
    }

    /**
     * Restituisce il <code>Dictionary</code> per la gestione dei tipi di fattura.
     *
     * @return java.util.Dictionary
     */

    public Dictionary getTi_istituz_commercKeys() {
        return TIPO;
    }

    /**
     * Insert the method's description here.
     * Creation date: (9/13/2001 10:33:00 AM)
     *
     * @return java.lang.String
     */
    public java.lang.String getTi_promiscuo() {
        return ti_promiscuo;
    }

    /**
     * Insert the method's description here.
     * Creation date: (9/13/2001 10:33:00 AM)
     *
     * @param newTi_promiscuo java.lang.String
     */
    public void setTi_promiscuo(java.lang.String newTi_promiscuo) {
        ti_promiscuo = newTi_promiscuo;
    }

    /**
     * Restituisce il <code>Dictionary</code> per la gestione dei tipi di fattura.
     *
     * @return java.util.Dictionary
     */

    public Dictionary getTi_promiscuoKeys() {
        return TIPO;
    }

    /**
     * Insert the method's description here.
     * Creation date: (9/10/2001 5:51:50 PM)
     *
     * @return it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk
     */
    public it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk getVoce_iva() {
        return voce_iva;
    }

    /**
     * Insert the method's description here.
     * Creation date: (9/10/2001 5:51:50 PM)
     *
     * @param newVoce_iva it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk
     */
    public void setVoce_iva(it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk newVoce_iva) {
        voce_iva = newVoce_iva;
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/15/2002 2:28:51 PM)
     *
     * @return java.util.Vector
     */
    public boolean isAnnullato() {

        return STATO_ANNULLATO.equalsIgnoreCase(getStato_cofi());
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/15/2002 2:28:51 PM)
     *
     * @return java.util.Vector
     */
    public void setAnnullato(java.sql.Timestamp date) {

        setStato_cofi(STATO_ANNULLATO);
        setDt_cancellazione(date);
    }

    public boolean isDirectlyLinkedToDC() {

        return false;
    }

    /**
     * Insert the method's description here.
     * Creation date: (11/29/2001 3:49:21 PM)
     *
     * @return boolean
     */
    public boolean isInventariato() {
        return inventariato;
    }

    /**
     * Insert the method's description here.
     * Creation date: (11/29/2001 3:49:21 PM)
     *
     * @param newInventariato boolean
     */
    public void setInventariato(boolean newInventariato) {
        inventariato = newInventariato;
    }

    public boolean isPagata() {

        //Se si necessita di modifica, verificare che la condizione in 'checkIfRiportata' è ancora valida!

        return STATO_PAGATO.equals(getStato_cofi());
    }

    public boolean isRiportata() {

        return getRiportata().equals(RIPORTATO);
    }

    /**
     * Insert the method's description here.
     * Creation date: (30/05/2003 16.17.42)
     *
     * @param newRiportata java.lang.String
     */
    public void setRiportata(java.lang.String newRiportata) {
        riportata = newRiportata;
    }

    /**
     * Insert the method's description here.
     * Creation date: (9/13/2001 10:33:00 AM)
     *
     * @return java.lang.String
     */
    public boolean isRObeneservizio() {

        return getBene_servizio() == null ||
                getBene_servizio().getCrudStatus() == OggettoBulk.NORMAL;
    }

    /**
     * Insert the method's description here.
     * Creation date: (9/13/2001 10:33:00 AM)
     *
     * @return java.lang.String
     */
    public boolean isROBeneServizioSearchTool() {

        return isInventariato() ||
                !STATO_INIZIALE.equals(getStato_cofi());
    }

    public boolean isStatoIniziale() {
        return Optional.ofNullable(getStato_cofi())
                .map(statoCofi -> statoCofi.equals(STATO_INIZIALE))
                .orElse(false);
    }
    /**
     * Insert the method's description here.
     * Creation date: (9/13/2001 10:33:00 AM)
     *
     * @return java.lang.String
     */
    public boolean isROdetail() {

        return getBene_servizio() == null ||
                getVoce_iva() == null;
    }

    /**
     * Insert the method's description here.
     * Creation date: (9/13/2001 10:33:00 AM)
     *
     * @return java.lang.String
     */
    public boolean isROvoceiva() {

        return getBene_servizio() == null ||
                getBene_servizio().getCrudStatus() == OggettoBulk.UNDEFINED ||
                getVoce_iva() == null ||
                getVoce_iva().getCrudStatus() == OggettoBulk.NORMAL;
    }

    // Richiesta 658 del 29/01/2004
    public boolean isVoceIVAOnlyIntraUE() {

        Fattura_passivaBulk fp = getFattura_passiva();
        if (fp == null) return false;

        return ((fp.isIstituzionale() &&
                fp.getTi_bene_servizio() != null && Bene_servizioBulk.SERVIZIO.equalsIgnoreCase(fp.getTi_bene_servizio()) &&
                fp.getFl_extra_ue() != null && fp.getFl_extra_ue().booleanValue()) ||
                (fp.isIstituzionale() && fp.getFl_intra_ue() != null && fp.getFl_intra_ue().booleanValue()) ||
                // ??? Rospuc da chiedere
                (fp.isIstituzionale() && fp.getFl_merce_intra_ue() != null && fp.getFl_merce_intra_ue().booleanValue()) ||
                // ??? Rospuc da chiedere
                (fp.isIstituzionale() && (fp.getFl_san_marino_senza_iva() != null && fp.getFl_san_marino_senza_iva().booleanValue())));
    }

    public boolean isVoidable() {

        return isRiportata() ||
                (STATO_CONTABILIZZATO.equals(getStato_cofi()) &&
                        ASSOCIATO_A_MANDATO.equals(getTi_associato_manrev())) ||
                (Fattura_passivaBulk.STATO_CONTABILIZZATO.equals(getStato_cofi()) &&
                        Fattura_passivaBulk.PARZIALMENTE_ASSOCIATO_A_MANDATO.equals(getFattura_passiva().getTi_associato_manrev())) ||
                (!Fattura_passivaBulk.NON_REGISTRATO_IN_COGE.equalsIgnoreCase(getFattura_passiva().getStato_coge()) &&
                        !Fattura_passivaBulk.NON_PROCESSARE_IN_COGE.equalsIgnoreCase(getFattura_passiva().getStato_coge())) ||
                (!Fattura_passivaBulk.NON_CONTABILIZZATO_IN_COAN.equalsIgnoreCase(getFattura_passiva().getStato_coan()) &&
                        !Fattura_passivaBulk.NON_PROCESSARE_IN_COAN.equalsIgnoreCase(getFattura_passiva().getStato_coan()));
    }

    public void validaDateCompetenza()
            throws ValidationException {

        String dsRiga = (getBene_servizio() != null) ?
                "per il dettaglio \"" + getBene_servizio().getCd_bene_servizio() + "\"" :
                "per il/i dettagli selezionato/i";
        if (getDt_da_competenza_coge() == null)
            throw new ValidationException("Inserire la data di \"competenza da\" " + dsRiga + ".");
        if (getDt_a_competenza_coge() == null)
            throw new ValidationException("Inserire la data di \"competenza a\" " + dsRiga + ".");

        Calendar competenzaDa = getFattura_passiva().getDateCalendar(getDt_da_competenza_coge());
        Calendar competenzaA = getFattura_passiva().getDateCalendar(getDt_a_competenza_coge());
        Calendar competenzaDaTestata = getFattura_passiva().getDateCalendar(getFattura_passiva().getDt_da_competenza_coge());
        Calendar competenzaATestata = getFattura_passiva().getDateCalendar(getFattura_passiva().getDt_a_competenza_coge());

        if (competenzaA.before(competenzaDa))
            throw new ValidationException("Inserire correttamente le date di competenza " + dsRiga + ".");

        if (competenzaDa.before(competenzaDaTestata))
            throw new ValidationException("La data di \"competenza Da\" deve essere successiva o uguale alla data di \"competenza da\" della testata " + dsRiga + ".");
        if (competenzaA.after(competenzaATestata))
            throw new ValidationException("La data di \"competenza A\" deve essere inferiore o uguale alla data di \"competenza a\" della testata " + dsRiga + ".");
    }

    public TerzoBulk getFornitore() {
        return fornitore;
    }

    public void setFornitore(TerzoBulk fornitore) {
        this.fornitore = fornitore;
    }

    public TerzoBulk getCessionario() {
        return cessionario;
    }

    public void setCessionario(TerzoBulk cessionario) {
        this.cessionario = cessionario;
    }

    public BancaBulk getBanca() {

        return banca;
    }

    public void setBanca(BancaBulk banca) {
        this.banca = banca;
    }

    public it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk getModalita_pagamento() {
        return modalita_pagamento;
    }

    public void setModalita_pagamento(Rif_modalita_pagamentoBulk modalita_pagamento) {
        this.modalita_pagamento = modalita_pagamento;
    }

    public java.util.Collection getModalita() {
        return modalita;
    }

    public void setModalita(java.util.Collection newModalita) {
        modalita = newModalita;
    }

    public Rif_termini_pagamentoBulk getTermini_pagamento() {
        return termini_pagamento;
    }

    public void setTermini_pagamento(Rif_termini_pagamentoBulk termini_pagamento) {
        this.termini_pagamento = termini_pagamento;
    }

    public java.util.Collection getTermini() {
        return termini;
    }

    public void setTermini(java.util.Collection newTermini) {
        termini = newTermini;
    }

    public java.util.Collection getBanche() {
        return banche;
    }

    public void setBanche(java.util.Collection newBanche) {
        banche = newBanche;
    }

    public java.lang.String getCd_modalita_pag() {
        it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk modalita_pagamento = this.getModalita_pagamento();
        if (modalita_pagamento == null)
            return null;
        return modalita_pagamento.getCd_modalita_pag();
    }

    public void setCd_modalita_pag(java.lang.String cd_modalita_pag) {
        this.getModalita_pagamento().setCd_modalita_pag(cd_modalita_pag);
    }

    public java.lang.String getCd_termini_pag() {
        it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk termini_pagamento = this.getTermini_pagamento();
        if (termini_pagamento == null)
            return null;
        return termini_pagamento.getCd_termini_pag();
    }

    public void setCd_termini_pag(java.lang.String cd_termini_pag) {
        this.getTermini_pagamento().setCd_termini_pag(cd_termini_pag);
    }

    public java.lang.Long getPg_banca() {
        it.cnr.contab.anagraf00.core.bulk.BancaBulk banca = this.getBanca();
        if (banca == null)
            return null;
        return banca.getPg_banca();
    }

    public java.lang.Integer getCd_terzo() {
        it.cnr.contab.anagraf00.core.bulk.TerzoBulk fornitore = this.getFornitore();
        if (fornitore == null)
            return null;
        return fornitore.getCd_terzo();
    }

    public void setCd_terzo(java.lang.Integer cd_terzo) {
        this.getFornitore().setCd_terzo(cd_terzo);
    }

    public java.lang.Integer getCd_terzo_cessionario() {
        it.cnr.contab.anagraf00.core.bulk.TerzoBulk cessionario = this.getCessionario();
        if (cessionario == null)
            return null;
        return cessionario.getCd_terzo();
    }

    public void setCd_terzo_cessionario(java.lang.Integer cd_terzo_cessionario) {
        this.getCessionario().setCd_terzo(cd_terzo_cessionario);
    }

    public boolean isROModalita_pagamento_dett() {

        return (this.getStato_cofi().equals(this.STATO_PAGATO)
                || !this.getFattura_passiva().getTi_fattura().equals(this.getFattura_passiva().TIPO_FATTURA_PASSIVA));
    }

    public boolean isAbledToInsertBank() {

        return !(getFornitore() != null &&
                getFornitore().getCrudStatus() == OggettoBulk.NORMAL &&
                getModalita_pagamento() != null &&
                !isROModalita_pagamento_dett());
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

    public Boolean getCollegatoCapitoloPerTrovato() {
        return collegatoCapitoloPerTrovato;
    }

    public void setCollegatoCapitoloPerTrovato(
            Boolean collegatoCapitoloPerTrovato) {
        this.collegatoCapitoloPerTrovato = collegatoCapitoloPerTrovato;
    }

    public boolean isCommerciale() {
        return TipoIVA.COMMERCIALE.value().equals(getTi_istituz_commerc());
    }

    public BulkList<FatturaOrdineBulk> getFatturaOrdineColl() {
        return fatturaOrdineColl;
    }

    public void setFatturaOrdineColl(BulkList<FatturaOrdineBulk> fatturaOrdineColl) {
        this.fatturaOrdineColl = fatturaOrdineColl;
    }

    public int addToFatturaOrdineColl(FatturaOrdineBulk fatturaOrdineBulk) {
        fatturaOrdineColl.add(fatturaOrdineBulk);
        fatturaOrdineBulk.setFatturaPassivaRiga(this);
        return fatturaOrdineColl.size() - 1;
    }

    public void removeFromFatturaOrdineColl(FatturaOrdineBulk fatturaOrdineBulk) {
        fatturaOrdineColl.removeByPrimaryKey(fatturaOrdineBulk);
    }

    public BulkCollection[] getBulkLists() {
        // Metti solo le liste di oggetti che devono essere resi persistenti
        return new it.cnr.jada.bulk.BulkCollection[]{
                fatturaOrdineColl
        };
    }

	public CigBulk getCig() {
		return cig;
	}

	public void setCig(CigBulk cig) {
		this.cig = cig;
	}

    @Override
    public TerzoBulk getTerzo() {
        return this.getFornitore();
    }
}