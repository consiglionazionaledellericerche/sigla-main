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

import it.cnr.contab.docamm00.bp.CRUDFatturaPassivaIBP;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;

/**
 * Insert the type's description here.
 * Creation date: (10/24/2001 2:26:02 PM)
 *
 * @author: Roberto Peli
 */
public class Fattura_passiva_IBulk
        extends Fattura_passivaBulk
        implements IDocumentoAmministrativoSpesaBulk {

    private java.util.HashMap storniHashMap = new java.util.HashMap();
    private java.util.HashMap addebitiHashMap = new java.util.HashMap();
    private Fattura_passiva_IBulk fattura_estera = null;

    /**
     * Fattura_passiva_IBulk constructor comment.
     */
    public Fattura_passiva_IBulk() {
        super();
    }

    /**
     * Fattura_passiva_IBulk constructor comment.
     *
     * @param cd_cds                 java.lang.String
     * @param cd_unita_organizzativa java.lang.String
     * @param esercizio              java.lang.Integer
     * @param pg_fattura_passiva     java.lang.Long
     */
    public Fattura_passiva_IBulk(String cd_cds, String cd_unita_organizzativa, Integer esercizio, Long pg_fattura_passiva) {
        super(cd_cds, cd_unita_organizzativa, esercizio, pg_fattura_passiva);

        setTi_fattura(TIPO_FATTURA_PASSIVA);
    }

    /**
     * Insert the method's description here.
     * Creation date: (12/17/2001 2:18:37 PM)
     *
     * @return java.util.HashMap
     */
    public java.util.HashMap getAddebitiHashMap() {
        return addebitiHashMap;
    }

    /**
     * Insert the method's description here.
     * Creation date: (12/17/2001 2:18:38 PM)
     *
     * @param newAddebitiHashMap java.util.HashMap
     */
    public void setAddebitiHashMap(java.util.HashMap newAddebitiHashMap) {
        addebitiHashMap = newAddebitiHashMap;
    }

    /*
     * Getter dell'attributo cd_cds_fat_clgs
     */
    public java.lang.String getCd_cds_fat_clgs() {

        it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk fattura_estera = this.getFattura_estera();
        if (fattura_estera == null)
            return null;
        return fattura_estera.getCd_cds();
    }

    /*
     * Setter dell'attributo cd_cds_fat_clgs
     */
    public void setCd_cds_fat_clgs(java.lang.String cd_cds_fat_clgs) {

        this.getFattura_estera().setCd_cds(cd_cds_fat_clgs);
    }

    /*
     * Getter dell'attributo cd_uo_fat_clgs
     */
    public java.lang.String getCd_uo_fat_clgs() {

        it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk fattura_estera = this.getFattura_estera();
        if (fattura_estera == null)
            return null;
        return fattura_estera.getCd_unita_organizzativa();
    }

    /*
     * Setter dell'attributo cd_uo_fat_clgs
     */
    public void setCd_uo_fat_clgs(java.lang.String cd_uo_fat_clgs) {

        this.getFattura_estera().setCd_unita_organizzativa(cd_uo_fat_clgs);
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/11/2002 3:13:39 PM)
     *
     * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
     */
    public java.lang.Class getChildClass() {
        return Fattura_passiva_rigaIBulk.class;
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/8/2002 10:45:38 AM)
     *
     * @return java.lang.String
     */
    public java.lang.String getDescrizione_spesa() {
        return getDs_fattura_passiva();
    }

    /**
     * Insert the method's description here.
     * Creation date: (4/9/2002 12:03:54 PM)
     *
     * @return java.lang.String
     */
    public java.lang.String getDescrizioneEntita() {
        return "fattura passiva";
    }

    /**
     * Insert the method's description here.
     * Creation date: (4/9/2002 12:03:54 PM)
     *
     * @return java.lang.String
     */
    public java.lang.String getDescrizioneEntitaPlurale() {
        return "fatture passive";
    }

    /*
     * Getter dell'attributo esercizio_fat_clgs
     */
    public java.lang.Integer getEsercizio_fat_clgs() {

        it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk fattura_estera = this.getFattura_estera();
        if (fattura_estera == null)
            return null;
        return fattura_estera.getEsercizio_fat_clgs();
    }

    /*
     * Setter dell'attributo esercizio_fat_clgs
     */
    public void setEsercizio_fat_clgs(java.lang.Integer esercizio_fat_clgs) {

        this.getFattura_estera().setEsercizio(esercizio_fat_clgs);
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/14/2002 10:04:36 AM)
     *
     * @return it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk
     */
    public Fattura_passiva_IBulk getFattura_estera() {
        return fattura_estera;
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/14/2002 10:04:36 AM)
     *
     * @param newFattura_estera it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk
     */
    public void setFattura_estera(Fattura_passiva_IBulk newFattura_estera) {
        fattura_estera = newFattura_estera;
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/8/2002 10:43:10 AM)
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getImporto_netto_spesa() {
        return getImporto_spesa();
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/8/2002 10:43:10 AM)
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getImporto_spesa() {

        return getIm_importo_totale_fattura_fornitore_euro();
    }

    public String getManagerName() {
        return "CRUDFatturaPassivaBP";
    }

    /**
     * Insert the method's description here.
     * Creation date: (3/22/2002 2:36:40 PM)
     *
     * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
     */
    public java.lang.String getManagerOptions() {

        // NON CANCELLARE QUESTO COMMENTO: E' DA ABILITARE AL POSTO DEL RESTO NEL CASO DI APERTURA
        // IN MODIFICA
        //return "MTh";

        return "VTh";
    }

    /*
     * Getter dell'attributo pg_fattura_passiva_fat_clgs
     */
    public java.lang.Long getPg_fattura_passiva_fat_clgs() {

        it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk fattura_estera = this.getFattura_estera();
        if (fattura_estera == null)
            return null;
        return fattura_estera.getPg_fattura_passiva();
    }

    /*
     * Setter dell'attributo pg_fattura_passiva_fat_clgs
     */
    public void setPg_fattura_passiva_fat_clgs(java.lang.Long pg_fattura_passiva_fat_clgs) {

        this.getFattura_estera().setPg_fattura_passiva_fat_clgs(pg_fattura_passiva_fat_clgs);
    }

    /**
     * Insert the method's description here.
     * Creation date: (11/6/2001 2:59:50 PM)
     *
     * @return java.util.HashMap
     */
    public java.util.HashMap getStorniHashMap() {
        return storniHashMap;
    }

    /**
     * Insert the method's description here.
     * Creation date: (11/6/2001 2:59:50 PM)
     *
     * @param newStorniHashMap java.util.HashMap
     */
    public void setStorniHashMap(java.util.HashMap newStorniHashMap) {
        storniHashMap = newStorniHashMap;
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/8/2002 10:43:10 AM)
     *
     * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
     */
    public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getTerzo_spesa() {

        return getFornitore();
    }

    /**
     * Insert the method's description here.
     * Creation date: (11/22/2001 11:59:14 AM)
     *
     * @return boolean
     */
    public boolean hasAddebiti() {

        if (addebitiHashMap != null) {
            for (java.util.Iterator i = addebitiHashMap.keySet().iterator(); i.hasNext(); ) {
                Fattura_passiva_rigaIBulk fpr = (Fattura_passiva_rigaIBulk) i.next();
                java.util.Vector righeNDD = (java.util.Vector) addebitiHashMap.get(fpr);
                if (righeNDD != null) {
                    for (java.util.Iterator it = righeNDD.iterator(); it.hasNext(); ) {
                        Nota_di_debito_rigaBulk nddrow = (Nota_di_debito_rigaBulk) it.next();
                        if (!nddrow.isAnnullato()) return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Insert the method's description here.
     * Creation date: (11/22/2001 11:59:14 AM)
     *
     * @return boolean
     */
    public boolean hasStorni() {

        if (storniHashMap != null) {
            for (java.util.Iterator i = storniHashMap.keySet().iterator(); i.hasNext(); ) {
                Fattura_passiva_rigaIBulk fpr = (Fattura_passiva_rigaIBulk) i.next();
                java.util.Vector righeNDC = (java.util.Vector) storniHashMap.get(fpr);
                if (righeNDC != null) {
                    for (java.util.Iterator it = righeNDC.iterator(); it.hasNext(); ) {
                        Nota_di_credito_rigaBulk ndcrow = (Nota_di_credito_rigaBulk) it.next();
                        if (!ndcrow.isAnnullato()) return true;
                    }
                }
            }
        }
        return false;
    }

    public OggettoBulk initialize(CRUDBP bp, it.cnr.jada.action.ActionContext context) {

        super.initialize(bp, context);

        setTi_fattura(TIPO_FATTURA_PASSIVA);

        return this;
    }

    /**
     * Inizializza il ricevente per la visualizzazione in un <code>FormController</code>
     * in stato <code>SEARCH</code>.
     * Questo metodo viene invocato automaticamente da un
     * <code>it.cnr.jada.util.action.CRUDBP</code> quando viene inizializzato
     * per la ricerca di un OggettoBulk.
     */
    public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) {

        super.initializeForSearch(bp, context);

        if (bp instanceof CRUDFatturaPassivaIBP && ((CRUDFatturaPassivaIBP) bp).isSpesaBP()) {
            setStato_cofi(STATO_CONTABILIZZATO);
            setStato_pagamento_fondo_eco(FONDO_ECO);
            setFl_congelata(Boolean.FALSE);
            setStato_liquidazione(LIQ);
            it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
            if (it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC.equalsIgnoreCase(uo.getCd_tipo_unita())) {
                setCd_unita_organizzativa(null);
                setCd_uo_origine(null);
            }
        }
        return this;
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/4/2001 2:42:26 PM)
     *
     * @return boolean
     */
    public boolean isAbledToInsertLettera() {

        return super.isAbledToInsertLettera();//|| hasStorni() || hasAddebiti();
    }

    public boolean isAbledToModifyFlagsTipoFattura() {

        return super.isAbledToModifyFlagsTipoFattura() || getLettera_pagamento_estero() != null;
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/4/2001 2:42:26 PM)
     *
     * @return boolean
     */
    public boolean isByFondoEconomale() {

        return !NO_FONDO_ECO.equalsIgnoreCase(getStato_pagamento_fondo_eco());
    }

    public boolean isDoc1210Associato() {

        return getLettera_pagamento_estero() != null &&
                getLettera_pagamento_estero().getIm_pagamento() != null &&
                getLettera_pagamento_estero().getIm_pagamento().compareTo(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP)) != 0;
    }

    public boolean isEditable() {
        try {
            return !(isPagata() ||
                    //(isStampataSuRegistroIVA() && // Tolto per richiesta 423
                    //!isDoc1210Associato() && !isDeleting()) ||
                    isAnnullato() ||
                    isCongelata() ||
                    (!((getEsercizio().intValue() == getEsercizioInScrivania().intValue()) && !isRiportata()) &&
                            !isDeleting()));
        } catch (NullPointerException e) {
            return false;
        }

    }

    /**
     * Insert the method's description here.
     * Creation date: (10/4/2001 2:42:26 PM)
     *
     * @return boolean
     */
    public boolean isROFattura_estera() {

        return getFattura_estera() == null ||
                getFattura_estera().getCrudStatus() == OggettoBulk.NORMAL;
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/4/2001 2:42:26 PM)
     *
     * @return boolean
     */
    public boolean isROFattura_esteraSearchTool() {

        return !(isBollaDoganale() || isSpedizioniere());
    }

    public boolean isROModalita_pagamento() {

        return isEstera() && isDoc1210Associato();
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/4/2001 2:42:26 PM)
     *
     * @return boolean
     */
    public boolean isROStato_pagamento_fondo_eco() {

        return isPagata() ||
                isPagataParzialmente() ||
                hasStorni() ||
                hasAddebiti() ||
                isEstera() ||
                isElettronica() ||
                (getFl_san_marino_con_iva() != null && getFl_san_marino_con_iva().booleanValue()) ||
                (getFl_san_marino_senza_iva() != null && getFl_san_marino_senza_iva().booleanValue());// ||
        //(getFl_spedizioniere() != null && getFl_spedizioniere().booleanValue()) ||
        //(getFl_bolla_doganale() != null && getFl_bolla_doganale().booleanValue());
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/6/2002 1:19:11 PM)
     *
     * @return java.lang.Integer
     */
    public void setCd_tipo_doc_amm(java.lang.String newCd_tipo_doc_amm) {
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/6/2002 1:19:11 PM)
     *
     * @return java.lang.Integer
     */
    public void setCd_uo(java.lang.String newCd_uo) {

        setCd_unita_organizzativa(newCd_uo);
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/6/2002 1:19:11 PM)
     *
     * @return java.lang.Integer
     */
    public void setPg_doc_amm(java.lang.Long newPg) {

        setPg_fattura_passiva(newPg);
    }

}

