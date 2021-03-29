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
public class Nota_di_debito_attivaBulk extends Fattura_attivaBulk {

    /**
     * Nota_di_creditoBulk constructor comment.
     */
    public Nota_di_debito_attivaBulk() {
        super();

        setTi_fattura(TIPO_NOTA_DI_DEBITO);
    }

    /**
     * Nota_di_creditoBulk constructor comment.
     */
    public Nota_di_debito_attivaBulk(
            Fattura_attiva_IBulk fattura_attiva,
            Integer esercizioScrivania) {
        this();

        copyFrom(fattura_attiva, esercizioScrivania);
    }

    /**
     * Nota_di_creditoBulk constructor comment.
     *
     * @param cd_cds                 java.lang.String
     * @param cd_unita_organizzativa java.lang.String
     * @param esercizio              java.lang.Integer
     * @param pg_fattura_passiva     java.lang.Long
     */
    public Nota_di_debito_attivaBulk(String cd_cds, String cd_unita_organizzativa, Integer esercizio, Long pg_fattura_passiva) {
        super(cd_cds, cd_unita_organizzativa, esercizio, pg_fattura_passiva);

        setTi_fattura(TIPO_NOTA_DI_DEBITO);
    }

    public void copyFrom(
            Fattura_attiva_IBulk fattura_attiva,
            Integer esercizioScrivania) {

        if (fattura_attiva == null || esercizioScrivania == null) return;

        setStato_cofi(STATO_INIZIALE);
        setStato_coge(NON_REGISTRATO_IN_COGE);
        setStato_coan(NON_CONTABILIZZATO_IN_COAN);
        setTi_associato_manrev(NON_ASSOCIATO_A_MANDATO);

        setCd_cds(fattura_attiva.getCd_cds());
        setEsercizio(esercizioScrivania);
        setCd_unita_organizzativa(fattura_attiva.getCd_unita_organizzativa());
        setCd_cds_origine(fattura_attiva.getCd_cds_origine());
        setCd_uo_origine(fattura_attiva.getCd_uo_origine());
        // Gennaro Borriello/Farinella (09/11/2004 16.12.05)
        //	Aggiunta gestione dell'es di scrivania per il controllo sullo stato riportato/Anno di competenza
        setEsercizioInScrivania(fattura_attiva.getEsercizioInScrivania());


        try {
            java.sql.Timestamp date = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
            int annoSolare = getDateCalendar(date).get(java.util.Calendar.YEAR);
            if (annoSolare != getEsercizio().intValue())
                date = new java.sql.Timestamp(new java.text.SimpleDateFormat("dd/MM/yyyy").parse("31/12/" + fattura_attiva.getEsercizio()).getTime());
            setDt_registrazione(date);
        } catch (Throwable t) {
            throw new it.cnr.jada.DetailedRuntimeException(t);
        }

        setDt_termine_creazione_docamm(fattura_attiva.getDt_termine_creazione_docamm());
        setDt_da_competenza_coge(fattura_attiva.getDt_da_competenza_coge());
        setDt_a_competenza_coge(fattura_attiva.getDt_a_competenza_coge());

        //setFl_cliente_occasionale(fattura_attiva.getFl_cliente_occasionale());
        setFl_liquidazione_differita(fattura_attiva.getFl_liquidazione_differita());
        setAttivoSplitPayment(fattura_attiva.isAttivoSplitPayment());
        //setProtocollo_iva(fattura_attiva.getProtocollo_iva());
        //setProtocollo_iva_generale(fattura_attiva.getProtocollo_iva_generale());

        setSezionali(fattura_attiva.getSezionali());
        setTipo_sezionale(fattura_attiva.getTipo_sezionale());

        setTi_causale_emissione(fattura_attiva.getTi_causale_emissione());
        setFl_intra_ue(fattura_attiva.getFl_intra_ue());
        setFl_extra_ue(fattura_attiva.getFl_extra_ue());
        setFl_san_marino(fattura_attiva.getFl_san_marino());
        setFl_congelata(Boolean.FALSE);

        setDs_fattura_attiva(fattura_attiva.getDs_fattura_attiva());
        setRiferimento_ordine(fattura_attiva.getRiferimento_ordine());
        //setDt_scadenza(fattura_attiva.getDt_scadenza());

        setValute(fattura_attiva.getValute());
        setValuta(fattura_attiva.getValuta());
        setCambio(fattura_attiva.getCambio());

        setTermini_uo(fattura_attiva.getTermini_uo());
        setTermini_pagamento_uo(fattura_attiva.getTermini_pagamento_uo());
        setModalita_uo(fattura_attiva.getModalita_uo());
        setModalita_pagamento_uo(fattura_attiva.getModalita_pagamento_uo());

        setCliente(fattura_attiva.getCliente());
        setTerzo_uo(fattura_attiva.getTerzo_uo());

        setRagione_sociale(fattura_attiva.getRagione_sociale());
        setNome(fattura_attiva.getNome());
        setCognome(fattura_attiva.getCognome());
        setCodice_fiscale(fattura_attiva.getCodice_fiscale());
        setPartita_iva(fattura_attiva.getPartita_iva());

        setBanca_uo(fattura_attiva.getBanca_uo());

        setIm_totale_fattura(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
        setFl_stampa(Boolean.FALSE);
        setModalita_trasportoColl(fattura_attiva.getModalita_trasportoColl());
        setCondizione_consegnaColl(fattura_attiva.getCondizione_consegnaColl());
        setModalita_incassoColl(fattura_attiva.getModalita_incassoColl());
        setModalita_erogazioneColl(fattura_attiva.getModalita_erogazioneColl());
        setTi_bene_servizio(fattura_attiva.getTi_bene_servizio());
        setFl_pagamento_anticipato(fattura_attiva.getFl_pagamento_anticipato());
        setAttivoSplitPayment(fattura_attiva.isAttivoSplitPayment());
    }

    public java.lang.Class getChildClass() {
        return Nota_di_debito_attiva_rigaBulk.class;
    }

    public String getManagerName() {
        return "CRUDNotaDiDebitoAttivaBP";
    }

    /**
     * Insert the method's description here.
     * Creation date: (3/22/2002 5:00:41 PM)
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
    public boolean isROcliente() {

        return true;
    }

    public boolean isROclienteForSearch() {

        return getCliente() == null ||
                getCliente().getCrudStatus() == OggettoBulk.NORMAL;
    }
}
