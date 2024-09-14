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

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;

/**
 * Insert the type's description here.
 * Creation date: (10/24/2001 2:26:43 PM)
 *
 * @author: Roberto Peli
 */
public class Nota_di_debitoBulk extends Fattura_passivaBulk {

    /**
     * Nota_di_creditoBulk constructor comment.
     */
    public Nota_di_debitoBulk() {
        super();

        setTi_fattura(TIPO_NOTA_DI_DEBITO);
    }

    /**
     * Nota_di_creditoBulk constructor comment.
     */
    public Nota_di_debitoBulk(
            Fattura_passiva_IBulk fattura_passiva,
            Integer esercizioScrivania) {
        this();

        copyFrom(fattura_passiva, esercizioScrivania);
    }

    /**
     * Nota_di_creditoBulk constructor comment.
     *
     * @param cd_cds                 java.lang.String
     * @param cd_unita_organizzativa java.lang.String
     * @param esercizio              java.lang.Integer
     * @param pg_fattura_passiva     java.lang.Long
     */
    public Nota_di_debitoBulk(String cd_cds, String cd_unita_organizzativa, Integer esercizio, Long pg_fattura_passiva) {
        super(cd_cds, cd_unita_organizzativa, esercizio, pg_fattura_passiva);

        setTi_fattura(TIPO_NOTA_DI_DEBITO);
    }

    public void copyFrom(
            Fattura_passiva_IBulk fattura_passiva,
            Integer esercizioScrivania) {

        if (fattura_passiva == null || esercizioScrivania == null) return;

        setStato_cofi(STATO_INIZIALE);
        setStato_coge(NON_REGISTRATO_IN_COGE);
        setStato_coan(NON_CONTABILIZZATO_IN_COAN);
        setStato_pagamento_fondo_eco(NO_FONDO_ECO);
        setTi_associato_manrev(NON_ASSOCIATO_A_MANDATO);

        setCd_cds(fattura_passiva.getCd_cds());
        setEsercizio(esercizioScrivania);
        setCd_unita_organizzativa(fattura_passiva.getCd_unita_organizzativa());
        setCd_cds_origine(fattura_passiva.getCd_cds_origine());
        setCd_uo_origine(fattura_passiva.getCd_uo_origine());
        // Gennaro Borriello/Farinella (09/11/2004 16.12.05)
        //	Aggiunta gestione dell'es di scrivania per il controllo sullo stato riportato/Anno di competenza
        setEsercizioInScrivania(fattura_passiva.getEsercizioInScrivania());
        setDataInizioFatturaElettronica(fattura_passiva.getDataInizioFatturaElettronica());
        setDataInizioSplitPayment(fattura_passiva.getDataInizioSplitPayment());

        try {
            java.sql.Timestamp date = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
            int annoSolare = getDateCalendar(date).get(java.util.Calendar.YEAR);
            if (annoSolare != getEsercizio().intValue())
                date = new java.sql.Timestamp(new java.text.SimpleDateFormat("dd/MM/yyyy").parse("31/12/" + fattura_passiva.getEsercizio()).getTime());
            setDt_registrazione(date);
            impostaDataScadenza();
        } catch (Throwable t) {
            throw new it.cnr.jada.DetailedRuntimeException(t);
        }
        setDt_da_competenza_coge(fattura_passiva.getDt_da_competenza_coge());
        setDt_a_competenza_coge(fattura_passiva.getDt_a_competenza_coge());

        //setNumero_protocollo(fattura_passiva.getNumero_protocollo());
        //setProtocollo_iva(fattura_passiva.getProtocollo_iva());
        //setProtocollo_iva_generale(fattura_passiva.getProtocollo_iva_generale());

        setTi_istituz_commerc(fattura_passiva.getTi_istituz_commerc());
        setSezionali(fattura_passiva.getSezionali());
        setTipo_sezionale(fattura_passiva.getTipo_sezionale());

        setFl_congelata(Boolean.FALSE);
        setFl_intra_ue(fattura_passiva.getFl_intra_ue());
        setFl_extra_ue(fattura_passiva.getFl_extra_ue());
        setFl_san_marino_con_iva(fattura_passiva.getFl_san_marino_con_iva());
        setFl_san_marino_senza_iva(fattura_passiva.getFl_san_marino_senza_iva());
        setFl_fattura_compenso(fattura_passiva.getFl_fattura_compenso());
        setFl_bolla_doganale(fattura_passiva.getFl_bolla_doganale());
        setFl_spedizioniere(fattura_passiva.getFl_spedizioniere());
        setFl_autofattura(fattura_passiva.getFl_autofattura());
        setTi_bene_servizio(fattura_passiva.getTi_bene_servizio());
        setFl_merce_extra_ue(fattura_passiva.getFl_merce_extra_ue());
        setFl_merce_intra_ue(fattura_passiva.getFl_merce_intra_ue());
        setDs_fattura_passiva(fattura_passiva.getDs_fattura_passiva());
        //setDt_fattura_fornitore(fattura_passiva.getDt_fattura_fornitore());
        //setDt_scadenza(fattura_passiva.getDt_scadenza());

        setValute(fattura_passiva.getValute());
        setValuta(fattura_passiva.getValuta());
        setCambio(fattura_passiva.getCambio());
        setDefaultValuta(fattura_passiva.isDefaultValuta());

        setTermini(fattura_passiva.getTermini());
        setTermini_pagamento(fattura_passiva.getTermini_pagamento());
        setModalita(fattura_passiva.getModalita());
        setModalita_pagamento(fattura_passiva.getModalita_pagamento());

        setFornitore(fattura_passiva.getFornitore());
        setRagione_sociale(fattura_passiva.getRagione_sociale());
        setNome(fattura_passiva.getNome());
        setCognome(fattura_passiva.getCognome());
        setCodice_fiscale(fattura_passiva.getCodice_fiscale());
        setPartita_iva(fattura_passiva.getPartita_iva());

        setCessionario(fattura_passiva.getCessionario());
        setBanca(fattura_passiva.getBanca());

        setIm_totale_fattura(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
        setIm_totale_imponibile_divisa(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
        setFl_liquidazione_differita(fattura_passiva.getFl_liquidazione_differita());
        setModalita_trasportoColl(fattura_passiva.getModalita_trasportoColl());
        setCondizione_consegnaColl(fattura_passiva.getCondizione_consegnaColl());
        setModalita_incassoColl(fattura_passiva.getModalita_incassoColl());
        setModalita_erogazioneColl(fattura_passiva.getModalita_erogazioneColl());
        setDt_termine_creazione_docamm(fattura_passiva.getDt_termine_creazione_docamm());
        setFlDaOrdini(fattura_passiva.getFlDaOrdini());
        setFl_split_payment(fattura_passiva.getFl_split_payment());
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/11/2002 3:14:44 PM)
     *
     * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
     */
    public java.lang.Class getChildClass() {
        return Nota_di_debito_rigaBulk.class;
    }

    /**
     * Insert the method's description here.
     * Creation date: (4/9/2002 12:05:13 PM)
     *
     * @return java.lang.String
     */
    public java.lang.String getDescrizioneEntita() {
        return "nota di debito";
    }

    /**
     * Insert the method's description here.
     * Creation date: (4/9/2002 12:05:13 PM)
     *
     * @return java.lang.String
     */
    public java.lang.String getDescrizioneEntitaPlurale() {
        return "note di debito";
    }

    public String getManagerName() {
        return "CRUDNotaDiDebitoBP";
    }

    /**
     * Insert the method's description here.
     * Creation date: (3/22/2002 2:36:21 PM)
     *
     * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
     */
    public java.lang.String getManagerOptions() {
        return "VTh";
    }

    public OggettoBulk initialize(CRUDBP bp, it.cnr.jada.action.ActionContext context) {

        super.initialize(bp, context);

        setTi_fattura(TIPO_NOTA_DI_DEBITO);
        return this;
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/4/2001 2:42:26 PM)
     *
     * @return boolean
     */
    public boolean isROfornitore() {

        return true;
    }

    public boolean isROfornitoreForSearch() {

        return getFornitore() == null ||
                getFornitore().getCrudStatus() == OggettoBulk.NORMAL;
    }
}
