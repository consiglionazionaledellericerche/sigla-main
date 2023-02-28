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

import it.cnr.jada.bulk.OggettoBulk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ReversaleIBulk extends ReversaleBulk {
    protected Collection docAttiviColl = new ArrayList();
    protected V_doc_attivo_accertamentoBulk find_doc_attivi = new V_doc_attivo_accertamentoBulk();
    protected List tipoBolloOptions;

    public ReversaleIBulk() {
        super();
    }

    public ReversaleIBulk(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.Long pg_reversale) {
        super(cd_cds, esercizio, pg_reversale);
    }

    /**
     * Aggiunge un nuovo dettaglio (Reversale_rigaBulk) alla lista di dettagli definiti per la reversale
     * inizializzandone alcuni campi
     *
     * @param rr dettaglio da aggiungere alla lista
     * @return int
     */

    public int addToReversale_rigaColl(Reversale_rigaBulk riga) {
        reversale_rigaColl.add(riga);
        return reversale_rigaColl.size() - 1;
    }

    /**
     * Aggiunge un nuovo dettaglio (Reversale_rigaBulk) alla lista di dettagli definiti per la reversale
     * inizializzandone alcuni campi
     *
     * @param rr dettaglio da aggiungere alla lista
     * @return int
     */

    public int addToReversale_rigaColl(Reversale_rigaBulk riga, V_doc_attivo_accertamentoBulk docAttivo) {
        reversale_rigaColl.add(riga);
        docAttiviColl.remove(docAttivo);
//	reversale_rigaTable. ???
        return reversale_rigaColl.size() - 1;
    }

    /**
     * @return java.util.Collection
     */
    public java.util.Collection getDocAttiviColl() {
        return docAttiviColl;
    }

    /**
     * @param newDocAttiviColl java.util.Collection
     */
    public void setDocAttiviColl(java.util.Collection newDocAttiviColl) {
        docAttiviColl = newDocAttiviColl;
    }

    /**
     * @return it.cnr.contab.doccont00.core.bulk.V_doc_attivo_accertamentoBulk
     */
    public V_doc_attivo_accertamentoBulk getFind_doc_attivi() {
        return find_doc_attivi;
    }

    /**
     * @param newFind_doc_attivi it.cnr.contab.doccont00.core.bulk.V_doc_attivo_accertamentoBulk
     */
    public void setFind_doc_attivi(V_doc_attivo_accertamentoBulk newFind_doc_attivi) {
        find_doc_attivi = newFind_doc_attivi;
    }

    /**
     * @return java.util.List
     */
    public java.util.List getTipoBolloOptions() {
        return tipoBolloOptions;
    }

    /**
     * @param newTipoBolloOptions java.util.List
     */
    public void setTipoBolloOptions(java.util.List newTipoBolloOptions) {
        tipoBolloOptions = newTipoBolloOptions;
    }

    /**
     * Verifica se la reversale e' stata originata da un altro doc. contabile
     *
     * @return TRUE    se la reversale non dipende da un altro doc. contabile
     * FALSE 	altrimenti
     */
    public boolean isDipendenteDaAltroDocContabile() {
        V_ass_doc_contabiliBulk associazione;
        for (Iterator i = getDoc_contabili_collColl().iterator(); i.hasNext(); ) {
            associazione = (V_ass_doc_contabiliBulk) i.next();
            if (associazione.getFl_con_man_prc().booleanValue() &&
                    associazione.getCd_tipo_documento_cont_coll().equals(Numerazione_doc_contBulk.TIPO_REV) &&
                    associazione.getCd_cds_coll().equals(getCd_cds()) &&
                    associazione.getEsercizio_coll().equals(getEsercizio()) &&
                    associazione.getPg_documento_cont_coll().equals(getPg_reversale()))
                return true;
        }
        return false;
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'rOFind_terzoAnag'
     *
     * @return Il valore della proprietà 'rOFind_terzoAnag'
     */
    public boolean isROFind_terzoAnag() {
        return getFind_doc_attivi().getTerzoAnag().getCrudStatus() == NORMAL;
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
        if (riga.getReversaleCupColl() != null && !riga.getReversaleCupColl().isEmpty())
            riga.setReversaleCupColl(null);
        return riga;
    }
}
