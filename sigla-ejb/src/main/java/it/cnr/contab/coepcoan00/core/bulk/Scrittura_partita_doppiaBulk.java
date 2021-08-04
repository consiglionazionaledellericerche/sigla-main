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

package it.cnr.contab.coepcoan00.core.bulk;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.util.enumeration.TipoIVA;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.OrderedHashtable;

import java.math.BigDecimal;
import java.util.Dictionary;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Scrittura_partita_doppiaBulk extends Scrittura_partita_doppiaBase {

    public final static String TIPO_COGE = "COGE";
    public final static String ORIGINE_CAUSALE = "CAUSALE";
    public final static String TIPO_PRIMA_SCRITTURA = "P";
    public final static String STATO_DEFINITIVO = "D";
    public final static java.util.Dictionary STATO_ATTIVA;
    public final static String ATTIVA_YES = "Y";
    public final static String ATTIVA_NO = "N";

    public final static Dictionary tipoKeys = TipoIVA.TipoIVAKeys;

    static {
        STATO_ATTIVA = new it.cnr.jada.util.OrderedHashtable();
        STATO_ATTIVA.put(ATTIVA_YES, "Y");
        STATO_ATTIVA.put(ATTIVA_NO, "N");
    }

    protected BulkList movimentiDareColl = new BulkList();
    protected BulkList movimentiAvereColl = new BulkList();
    protected CdsBulk cds = new CdsBulk();
    protected Unita_organizzativaBulk uo;
    protected TerzoBulk terzo;

    public Scrittura_partita_doppiaBulk() {
        super();
    }

    public Scrittura_partita_doppiaBulk(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.Long pg_scrittura) {
        super(cd_cds, esercizio, pg_scrittura);
        setCds(new it.cnr.contab.config00.sto.bulk.CdsBulk(cd_cds));
    }

    /**
     * Metodo per l'aggiunta di un elemento <code>Movimento_cogeBulk</code> di tipo Avere alla <code>Collection</code>
     * dei movimenti
     *
     * @param movimento Movimento_cogeBulk Il movimento
     * @return L'indice del movimento con aggiornati i dati relativi alla sezione, stato, scrittura
     */
    public int addToMovimentiAvereColl(Movimento_cogeBulk movimento) {
        this.movimentiAvereColl.add(movimento);
        movimento.setScrittura(this);
        movimento.setSezione(Movimento_cogeBulk.SEZIONE_AVERE);
        movimento.setStato(Movimento_cogeBulk.STATO_DEFINITIVO);
        return movimentiAvereColl.size() - 1;
    }

    /**
     * Metodo per l'aggiunta di un elemento <code>Movimento_cogeBulk</code> di tipo Dare alla <code>Collection</code>
     * dei movimenti
     *
     * @param movimento Movimento_cogeBulk Il movimento
     * @return L'indice del movimento con aggiornati i dati relativi alla sezione, stato, scrittura
     */
    public int addToMovimentiDareColl(Movimento_cogeBulk movimento) {
        this.movimentiDareColl.add(movimento);
        movimento.setScrittura(this);
        movimento.setSezione(Movimento_cogeBulk.SEZIONE_DARE);
        movimento.setStato(Movimento_cogeBulk.STATO_DEFINITIVO);
        return movimentiDareColl.size() - 1;
    }

    /**
     * Restituisce un array di <code>BulkCollection</code> contenenti oggetti
     * bulk da rendere persistenti insieme al ricevente.
     * L'implementazione standard restituisce <code>null</code>.
     */
    public BulkCollection[] getBulkLists() {
        return new it.cnr.jada.bulk.BulkCollection[]{
                movimentiAvereColl,
                movimentiDareColl};

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

    public java.lang.Integer getCd_terzo() {
        it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzo = this.getTerzo();
        if (terzo == null)
            return null;
        return terzo.getCd_terzo();
    }

    public void setCd_terzo(java.lang.Integer cd_terzo) {
        if (this.getTerzo() == null)
            this.setTerzo(new TerzoBulk());
        this.getTerzo().setCd_terzo(cd_terzo);
    }

    public java.lang.String getCd_unita_organizzativa() {
        it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo = this.getUo();
        if (uo == null)
            return null;
        return uo.getCd_unita_organizzativa();
    }

    public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
        if (this.getUo() == null)
            this.setUo(new Unita_organizzativaBulk());
        this.getUo().setCd_unita_organizzativa(cd_unita_organizzativa);
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

    public java.math.BigDecimal getImTotaleAvere() {
        Movimento_cogeBulk mov;
        java.math.BigDecimal tot = new java.math.BigDecimal(0);
        for (java.util.Iterator i = getMovimentiAvereColl().iterator(); i.hasNext(); ) {
            mov = (Movimento_cogeBulk) i.next();
            if (mov.getIm_movimento() != null)
                tot = tot.add(mov.getIm_movimento());
        }
        return tot;
    }

    public java.math.BigDecimal getImTotaleDare() {
        Movimento_cogeBulk mov;
        java.math.BigDecimal tot = new java.math.BigDecimal(0);
        for (java.util.Iterator i = getMovimentiDareColl().iterator(); i.hasNext(); ) {
            mov = (Movimento_cogeBulk) i.next();
            if (mov.getIm_movimento() != null)
                tot = tot.add(mov.getIm_movimento());
        }
        return tot;

    }

    /**
     * @return it.cnr.jada.bulk.BulkList
     */
    public it.cnr.jada.bulk.BulkList<Movimento_cogeBulk> getMovimentiAvereColl() {
        return movimentiAvereColl;
    }

    /**
     * @param newMovimentiAvereColl it.cnr.jada.bulk.BulkList
     */
    public void setMovimentiAvereColl(it.cnr.jada.bulk.BulkList newMovimentiAvereColl) {
        movimentiAvereColl = newMovimentiAvereColl;
    }

    /**
     * @return it.cnr.jada.bulk.BulkList
     */
    public it.cnr.jada.bulk.BulkList<Movimento_cogeBulk> getMovimentiDareColl() {
        return movimentiDareColl;
    }

    /**
     * @param newMovimentiDareColl it.cnr.jada.bulk.BulkList
     */
    public void setMovimentiDareColl(it.cnr.jada.bulk.BulkList newMovimentiDareColl) {
        movimentiDareColl = newMovimentiDareColl;
    }

    /**
     * Il metodo restituisce il dictionary per la gestione degli stati ATTIVA
     * e NON ATTIVA
     */
    public java.util.Dictionary getStato_attivaKeys() {
        return STATO_ATTIVA;
    }

    /**
     * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
     */
    public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getTerzo() {
        return terzo;
    }

    /**
     * @param newTerzo it.cnr.contab.anagraf00.core.bulk.TerzoBulk
     */
    public void setTerzo(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newTerzo) {
        terzo = newTerzo;
    }

    /**
     * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
     */
    public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUo() {
        return uo;
    }

    /**
     * @param newUo it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
     */
    public void setUo(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUo) {
        uo = newUo;
    }

    /**
     * Metodo per inizializzare un Oggetto Bulk in fase di inserimento.
     */
    public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) {
        setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
        setUo(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context));
        setCd_uo_documento(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_unita_organizzativa());
        setOrigine_scrittura(ORIGINE_CAUSALE);
        setAttiva(ATTIVA_YES);
        setTi_scrittura(TIPO_PRIMA_SCRITTURA);
        setStato(STATO_DEFINITIVO);
//	setCds(getUo().getUnita_padre());
        return this;
    }

    /**
     * Metodo per la gestione del campo <code>esercizio</code>.
     */
    public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) {
        setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
        setUo(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context));
//	setOrigine_scrittura( ORIGINE_CAUSALE );
        setAttiva(ATTIVA_YES);
//	setCds(getUo().getUnita_padre());
        return this;
    }

    public boolean isROTerzo() {
        return getTerzo() != null &&
                getTerzo().getCrudStatus() != UNDEFINED;

    }

    /**
     * Metodo per l'eliminazione di un elemento <code>Movimento_cogeBulk</code> di tipo Avere dalla <code>Collection</code>
     * dei movimenti della Scrittura in partita doppia
     *
     * @param index L'indice del movimento da eliminare
     * @return Movimento_cogeBulk Il movimento avere rimosso
     */
    public Movimento_cogeBulk removeFromMovimentiAvereColl(int index) {
        return (Movimento_cogeBulk) movimentiAvereColl.remove(index);
    }

    /**
     * Metodo per l'eliminazione di un elemento <code>Movimento_cogeBulk</code> di tipo Dare dalla <code>Collection</code>
     * dei movimenti della Scrittura in partita doppia
     *
     * @param index L'indice del movimento da eliminare
     * @return Movimento_cogeBulk Il movimento dare rimosso
     */
    public Movimento_cogeBulk removeFromMovimentiDareColl(int index) {
        return (Movimento_cogeBulk) movimentiDareColl.remove(index);
    }

    /**
     * Effettua una validazione formale del contenuto dello stato dell'oggetto
     * bulk. Viene invocato da <code>CRUDBP</code> in
     * seguito ad una richiesta di salvataggio.
     *
     * @throws it.cnr.jada.bulk.ValidationException Se la validazione fallisce.
     *                                              Contiene il messaggio da visualizzare all'utente per la notifica
     *                                              dell'errore di validazione.
     * @see it.cnr.jada.util.action.CRUDBP
     */
    public void validate() throws ValidationException {
        if (getDs_scrittura() == null)
            throw new ValidationException("E' necessario inserire la descrizione della scrittura in partita doppia");

        for (java.util.Iterator i = getMovimentiAvereColl().iterator(); i.hasNext(); )
            ((Movimento_cogeBulk) i.next()).validate();

        for (java.util.Iterator i = getMovimentiDareColl().iterator(); i.hasNext(); )
            ((Movimento_cogeBulk) i.next()).validate();

    }

    public BigDecimal getTotaleDare() {
        return this.getMovimentiDareColl().stream().map(el -> el.getIm_movimento()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotaleAvere() {
        return this.getMovimentiAvereColl().stream().map(el -> el.getIm_movimento()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Movimento_cogeBulk> getAllMovimentiColl() {
        BulkList<Movimento_cogeBulk> list = new BulkList<>();
        list.addAll(this.getMovimentiDareColl());
        list.addAll(this.getMovimentiAvereColl());
        return list;
    }

    public String getTi_istituz_commerc() {
        return getAllMovimentiColl()
                .stream()
                .filter(m -> Optional.ofNullable(m.getTi_istituz_commerc()).isPresent())
                .map(Movimento_cogeBase::getTi_istituz_commerc)
                .distinct()
                .findAny()
                .orElse(null);
    }
}
