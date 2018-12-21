package it.cnr.contab.anagraf00.core.bulk;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.jada.bulk.OggettoBulk;

import java.util.Arrays;
import java.util.Optional;

/**
 * Gestione dei dati relativi alla tabella Modalita_pagamento
 */
@JsonInclude(value = Include.NON_NULL)
public class Modalita_pagamentoBulk extends Modalita_pagamentoBase {

    protected TerzoBulk terzo = new TerzoBulk();
    protected TerzoBulk terzo_delegato;
    protected Rif_modalita_pagamentoBulk rif_modalita_pagamento = new Rif_modalita_pagamentoBulk();

    public Modalita_pagamentoBulk() {
        super();
    }

    public Modalita_pagamentoBulk(java.lang.String cd_modalita_pag, java.lang.Integer cd_terzo) {
        super(cd_modalita_pag, cd_terzo);
        setTerzo(new it.cnr.contab.anagraf00.core.bulk.TerzoBulk(cd_terzo));
        setRif_modalita_pagamento(new it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk(cd_modalita_pag));
    }

    public java.lang.String getCd_modalita_pag() {
        it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk rif_modalita_pagamento = this.getRif_modalita_pagamento();
        if (rif_modalita_pagamento == null)
            return null;
        return rif_modalita_pagamento.getCd_modalita_pag();
    }

    public void setCd_modalita_pag(java.lang.String cd_modalita_pag) {
        this.getRif_modalita_pagamento().setCd_modalita_pag(cd_modalita_pag);
    }

    public java.lang.Integer getCd_terzo() {
        it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzo = this.getTerzo();
        if (terzo == null)
            return null;
        return terzo.getCd_terzo();
    }

    public void setCd_terzo(java.lang.Integer cd_terzo) {
        this.getTerzo().setCd_terzo(cd_terzo);
    }

    public java.lang.Integer getCd_terzo_delegato() {
        it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzo_delegato = this.getTerzo_delegato();
        if (terzo_delegato == null)
            return null;
        return terzo_delegato.getCd_terzo();
    }

    public void setCd_terzo_delegato(java.lang.Integer cd_terzo_delegato) {
        this.getTerzo_delegato().setCd_terzo(cd_terzo_delegato);
    }

    /**
     * Costruisce la chiave utilizzata per le associazioni tra modalità di pagamento e banche.
     */
    public java.lang.String getChiavePerBanca() {

        String perCessione = "N";
        String cdTerzoDelegato = "XXX";

        if (isPerCessione()) {
            perCessione = "Y";
            //if (getTerzo_delegato() != null && getTerzo_delegato().getCd_terzo() != null)
            //cdTerzoDelegato = Integer.toString(getTerzo_delegato().getCd_terzo().intValue());
        }

        String chiave = getRif_modalita_pagamento().getTi_pagamento() +
                "-" +
                perCessione + "-" +
                cdTerzoDelegato;

        return chiave;
    }

    /**
     * Restituisce il <code>Dictionary</code> per la gestione della descrizione dei tipi banche.
     *
     * @return java.util.Dictionary
     */

    public String getLabelFrame() {

        if (getRif_modalita_pagamento() != null) {
            return (String) getRif_modalita_pagamento().getDs_lista_bancheKeys().get(getRif_modalita_pagamento().getTi_pagamento());
        }

        return null;
    }

    /**
     * Restituisce la <code>Rif_modalita_pagamentoBulk</code> a cui è associato l'oggetto.
     *
     * @return it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk
     * @see setRif_modalita_pagamento
     */

    public Rif_modalita_pagamentoBulk getRif_modalita_pagamento() {
        return rif_modalita_pagamento;
    }

    /**
     * Imposta la <code>Rif_modalita_pagamentoBulk</code> a cui è associato l'oggetto.
     *
     * @param newRif_modalita_pagamento Il tipo modalita pagamento da associare.
     * @see getRif_modalita_pagamento
     */

    public void setRif_modalita_pagamento(Rif_modalita_pagamentoBulk newRif_modalita_pagamento) {
        rif_modalita_pagamento = newRif_modalita_pagamento;
    }

    /**
     * Restituisce il <code>TerzoBulk</code> a cui è associato l'oggetto.
     *
     * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
     * @see setTerzo
     */

    public TerzoBulk getTerzo() {
        return terzo;
    }

    /**
     * Imposta il <code>TerzoBulk</code> a cui è associato l'oggetto.
     *
     * @param newTerzo Il terzo da associare.
     * @see getTerzo
     */

    public void setTerzo(TerzoBulk newTerzo) {
        terzo = newTerzo;
    }

    /**
     * Restituisce il <code>TerzoBulk</code> descrittivo del terzo delegato.
     *
     * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
     * @see setTerzo_delegato
     */

    public TerzoBulk getTerzo_delegato() {
        return terzo_delegato;
    }

    /**
     * Imposta il <code>TerzoBulk</code> descrittivo del terzo delegato.
     *
     * @param newTerzo_delegato Il terzo da delegare.
     * @see getTerzo_delegato
     */

    public void setTerzo_delegato(TerzoBulk newTerzo_delegato) {
        terzo_delegato = newTerzo_delegato;
    }

    /**
     * Restituisce TRUE se la rif_modalita_di_pagamento è di tipo <codea>per cessione</codea>
     *
     * @return boolean
     */

    public boolean isPerCessione() {

        if (getRif_modalita_pagamento() != null && getRif_modalita_pagamento().getFl_per_cessione() != null)
            return getRif_modalita_pagamento().getFl_per_cessione().booleanValue();

        return false;
    }

    public boolean isAnnullata() {

        if (getRif_modalita_pagamento() != null && getRif_modalita_pagamento().getFl_cancellato() != null)
            return getRif_modalita_pagamento().getFl_cancellato().booleanValue();

        return false;
    }

    /**
     * Indica quando terzo_delegato deve essere read only.
     *
     * @return boolean
     */

    public boolean isROterzo_delegato() {
        return terzo_delegato == null || terzo_delegato.getCrudStatus() == OggettoBulk.NORMAL;
    }


    public String getTiPagamentoColumnSet() {
        return Optional.ofNullable(getRif_modalita_pagamento())
                .map(Rif_modalita_pagamentoBulk::getTiPagamentoColumnSet)
                .orElse(Rif_modalita_pagamentoBulk.ALTRO);
    }
}
