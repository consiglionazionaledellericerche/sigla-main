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

package it.cnr.contab.missioni00.docs.bulk;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk;
import it.cnr.contab.coepcoan00.core.bulk.Scrittura_partita_doppiaBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk;
import it.cnr.contab.docamm00.bp.IDocumentoAmministrativoSpesaBP;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoSpesaBulk;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi;
import it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.jada.util.action.CRUDBP;

import java.util.Dictionary;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Vector;

@JsonInclude(value = Include.NON_NULL)
public class AnticipoBulk extends AnticipoBase implements IDefferUpdateSaldi, IDocumentoAmministrativoSpesaBulk {
    /************* TERZO *********************************/
    public final static String ANAG_ALTRO = "A";
    public final static String ANAG_DIPENDENTE = "D";
    public final static java.util.Dictionary ti_anagraficoKeys;
    // Fondo Economale
    public final static java.util.Dictionary STATO_FONDO_ECO;
    public final static java.lang.String STATO_ASSEGNATO_FONDO_ECO = "A";
    public final static java.lang.String STATO_REGISTRATO_FONDO_ECO = "R";
    public final static java.lang.String STATO_LIBERO_FONDO_ECO = "N";
    // Stati Cofi
    public final static java.util.Dictionary STATO_COFI;
    public final static String STATO_INIZIALE_COFI = "I";
    public final static String STATO_CONTABILIZZATO = "C";
    public final static String STATO_PAGATO = "P";
    public final static String STATO_ANNULLATO = "A";
    // Stati Coge
    public final static String STATO_ESCLUSO_COGE = "X";
    public final static String STATO_INIZIALE_COGE = "N";
    public final static String STATO_CONTABILIZZATO_COGE = "C";
    public final static String STATO_RICONTABILIZZARE_COGE = "R";
    // Stati Coan
    public final static String STATO_INIZIALE_COAN = "N";
    public final static String STATO_CONTABILIZZATO_COAN = "C";
    public final static String STATO_RICONTABILIZZARE_COAN = "R";
    // Stati Manrev
    public final static java.util.Dictionary TI_ASSOCIATO_MANREV;
    public final static String STATO_INIZIALE_MANREV = "N";
    public final static java.lang.String ASSOCIATO_MANREV = "T";
    // Stati documento riportato
    public final static Dictionary STATI_RIPORTO;

    static {
        ti_anagraficoKeys = new OrderedHashtable();
        ti_anagraficoKeys.put("D", "Dipendente");
        ti_anagraficoKeys.put("A", "Altro");
    }

    static {
        STATO_FONDO_ECO = new it.cnr.jada.util.OrderedHashtable();
        STATO_FONDO_ECO.put(STATO_LIBERO_FONDO_ECO, "Non usare fondo economale");
        STATO_FONDO_ECO.put(STATO_ASSEGNATO_FONDO_ECO, "Usa fondo economale");
        STATO_FONDO_ECO.put(STATO_REGISTRATO_FONDO_ECO, "Registrato in fondo economale");

        TI_ASSOCIATO_MANREV = new it.cnr.jada.util.OrderedHashtable();
        TI_ASSOCIATO_MANREV.put(ASSOCIATO_MANREV, "Man/rev associato");
        TI_ASSOCIATO_MANREV.put(STATO_INIZIALE_MANREV, "Man/rev non associato");

        STATO_COFI = new it.cnr.jada.util.OrderedHashtable();
        STATO_COFI.put(STATO_INIZIALE_COFI, "Iniziale");
        STATO_COFI.put(STATO_CONTABILIZZATO, "Contabilizzato");
        STATO_COFI.put(STATO_PAGATO, "Pagato");
        STATO_COFI.put(STATO_ANNULLATO, "Annullato");

        STATI_RIPORTO = new it.cnr.jada.util.OrderedHashtable();
        STATI_RIPORTO.put(NON_RIPORTATO, "Non riportata");
        STATI_RIPORTO.put(PARZIALMENTE_RIPORTATO, "Parzialmente riportata");
        STATI_RIPORTO.put(COMPLETAMENTE_RIPORTATO, "Completamente riportata");
    }

    protected BancaBulk banca;
    protected Rif_modalita_pagamentoBulk modalita_pagamento;
    protected Rif_termini_pagamentoBulk termini_pagamento;
    private DivisaBulk divisa = new DivisaBulk();
    private RimborsoBulk rimborso;
    private MissioneBulk missione;
    private it.cnr.contab.config00.latt.bulk.WorkpackageBulk lattPerRimborso;
    private int annoSolare;
    private int esercizioScrivania;
    private V_terzo_per_compensoBulk v_terzo;
    private java.util.Collection modalita;
    private java.util.Collection termini;
    // Obbligazione
    private Vector documentiContabiliCancellati = null;
    private Vector dettagliCancellati = null;
    private Obbligazione_scadenzarioBulk scadenza_obbligazione = new Obbligazione_scadenzarioBulk();
    private Obbligazione_scadenzarioBulk scadenza_obbligazioneClone = new Obbligazione_scadenzarioBulk();
    private it.cnr.jada.bulk.PrimaryKeyHashMap deferredSaldi = new PrimaryKeyHashMap();
    private java.lang.String riportata = NON_RIPORTATO;
    private java.lang.String riportataInScrivania = NON_RIPORTATO;
    private Scrittura_partita_doppiaBulk scrittura_partita_doppia;

    public AnticipoBulk() {
        super();
    }

    public AnticipoBulk(java.lang.String cd_cds, java.lang.String cd_unita_organizzativa, java.lang.Integer esercizio, java.lang.Long pg_anticipo) {
        super(cd_cds, cd_unita_organizzativa, esercizio, pg_anticipo);
    }

    /**
     * Il metodo ritorna il valore dell'attributo 'ti_anagraficoKeys'
     */
    public final static java.util.Dictionary getTi_anagraficoKeys() {
        return ti_anagraficoKeys;
    }

    /**
     * Metodo richiesto dall' interfaccia IDefferUpdateSaldi.
     * Il metodo aggiunge alla PrimaryKeyHashMap dei saldi il documento contabile (in chiave)
     * e i relativi valori di business (in valore). Se la mappa non esiste viene creata
     */

    public void addToDefferredSaldi(IDocumentoContabileBulk docCont, java.util.Map values) {
        if (docCont != null) {
            if (deferredSaldi == null)
                deferredSaldi = new PrimaryKeyHashMap();
            if (!deferredSaldi.containsKey(docCont))
                deferredSaldi.put(docCont, values);
            else {
                java.util.Map firstValues = (java.util.Map) deferredSaldi.get(docCont);
                deferredSaldi.remove(docCont);
                deferredSaldi.put(docCont, firstValues);
            }
        }
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBulk.
     */

    public void addToDettagliCancellati(it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk dettaglio) {
        if (dettaglio != null && ((OggettoBulk) dettaglio).getCrudStatus() == OggettoBulk.NORMAL) {
            getDettagliCancellati().addElement(dettaglio);
            addToDocumentiContabiliCancellati(dettaglio.getScadenzaDocumentoContabile());
        }
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoSpesaBulk.
     * La collection 'documentiContabiliCancellati' contiene le scadenze elaborate dall'anticipo
     * che devono essere cancellate/inserite/modificate a db
     */
    public void addToDocumentiContabiliCancellati(it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk scadenza) {
        if (getDocumentiContabiliCancellati() == null)
            setDocumentiContabiliCancellati(new Vector());

        // documentiContabiliCancellati contiene le scadenze non piu' agganciate all'anticipo
        // che pero' devo essere inserite op aggiornate in tabella
        if ((scadenza != null) && (((OggettoBulk) scadenza).getCrudStatus() == OggettoBulk.NORMAL) &&
                (!BulkCollections.containsByPrimaryKey(getDocumentiContabiliCancellati(), (OggettoBulk) scadenza))) {
            scadenza.setIm_associato_doc_amm(new java.math.BigDecimal(0));
            getDocumentiContabiliCancellati().addElement(scadenza);
        }
    }

    /**
     * Quando l'utente seleziona una scadenza, l'applicazione deve sincronizzare le scadenze
     * gia' elaborate dall'anticipo.
     * Per quanto riguarda la scadenza precedentemente selezionata :
     * - se creata da anticipo ma mai salvata dovra' comunque essere inserita in tabella
     * con "im_associato_doc_amm" = 0
     * - se precedentemente associata ad anticipo salvato devo aggiornarne "im_associato_doc_amm" = 0
     * - se sganciata perche' cancellata non devo aggiornrne l'"im_associato_doc_amm"
     */

    public void gestisciCambioSelezioneScadenza(Obbligazione_scadenzarioBulk newScadenza) {
        //	Sincronizzo le scadenze che ho tra le cancellate perche' l'utente
        //	potrebbe averle modificate
        sincronizzaScadenzeCancellate(newScadenza);

        // L'anticipo non aveva associato alcuna scadenza
        if ((getPg_obbligazione_scadenzario() == null) || (getPg_obbligazione() == null) || (getEsercizio_ori_obbligazione() == null))
            return;

        // La scadenza associata all'anticipo e' sempre la stessa
        if (newScadenza.equalsByPrimaryKey(getScadenza_obbligazione()))
            return;

        // 	Se scopro, che la scadenza da sostituire appartiene alla stessa obbligazione
        //	della nuova scadenza, devo sincronizzarla perchè l'utente prima di riportare
        //	quella nuova potrebbe avermi modificato quella da sostituire (cioe' quella attulamente
        //	associata).
        if (newScadenza.getObbligazione().equalsByPrimaryKey(getScadenza_obbligazione().getObbligazione())) {
            boolean trovata = false;
            BulkList coll = newScadenza.getObbligazione().getObbligazione_scadenzarioColl();
            if (coll != null && !coll.isEmpty())    // 	Se coll e' vuota significa che non arrivo da obbligazione
            {                                    //	ma dal filtro di ricerca delle obbligazioni
                for (Iterator i = coll.iterator(); i.hasNext(); ) {
                    Obbligazione_scadenzarioBulk aScadenza = (Obbligazione_scadenzarioBulk) i.next();
                    if (aScadenza.equalsByPrimaryKey(getScadenza_obbligazione())) {
                        setScadenza_obbligazione(aScadenza);
                        trovata = true;
                    }
                }
                if (!trovata)
                    return;        //	significa che la scadenza che sto sostituendo
                //	e' stata cancellata
            }
        }

        addToDocumentiContabiliCancellati(getScadenza_obbligazione());

        if (getDocumentiContabiliCancellati() == null || getDocumentiContabiliCancellati().isEmpty())
            return;

        //	Se la scadenza appena associata all'anticipo l'avevo precedente sganciata dalla
        //	anticipo e quindi inserita nella collection dei Cancellati, devo rimuoverla da
        //	tale collection.
        for (Iterator i = ((Vector) getDocumentiContabiliCancellati().clone()).iterator(); i.hasNext(); ) {
            Obbligazione_scadenzarioBulk aScadDaSganciare = (Obbligazione_scadenzarioBulk) i.next();
            if (aScadDaSganciare.equalsByPrimaryKey(newScadenza)) {
                getDocumentiContabiliCancellati().remove(aScadDaSganciare);
                break;
            }
        }
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBulk.
     */
    public it.cnr.contab.docamm00.docs.bulk.AccertamentiTable getAccertamentiHash() {
        return null;
    }

    /**
     * Il metodo ritorna il valore dell'attributo 'annoSolare'
     */
    public int getAnnoSolare() {
        return annoSolare;
    }

    /**
     * il  metodo imposta il valore della proprietà 'annoSolare'
     */
    public void setAnnoSolare(int newAnnoSolare) {
        annoSolare = newAnnoSolare;
    }

    /**
     * Il metodo ritorna il valore dell'attributo 'banca'
     */
    public it.cnr.contab.anagraf00.core.bulk.BancaBulk getBanca() {
        return banca;
    }

    /**
     * Il metodo imposta il valore dell'attributo 'banca'
     */
    public void setBanca(it.cnr.contab.anagraf00.core.bulk.BancaBulk newBanca) {
        banca = newBanca;
    }

    /**
     * Il metodo ritorna il valore dell'attributo 'cd_cds_obbligazione'
     */
    public java.lang.String getCd_cds_obbligazione() {
        it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk scadenza_obbligazione = this.getScadenza_obbligazione();
        if (scadenza_obbligazione == null)
            return null;
        it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = scadenza_obbligazione.getObbligazione();
        if (obbligazione == null)
            return null;
        it.cnr.contab.config00.sto.bulk.CdsBulk cds = obbligazione.getCds();
        if (cds == null)
            return null;
        return cds.getCd_unita_organizzativa();
    }

    /**
     * Il  metodo imposta il valore dell'attributo 'cd_cds_obbligazione'
     */
    public void setCd_cds_obbligazione(java.lang.String cd_cds_obbligazione) {
        this.getScadenza_obbligazione().getObbligazione().getCds().setCd_unita_organizzativa(cd_cds_obbligazione);
    }

    /**
     * Il metodo ritorna il valore dell'attributo 'cd_centro_responsabilita'
     */
    public java.lang.String getCd_centro_responsabilita() {
        it.cnr.contab.config00.latt.bulk.WorkpackageBulk lattPerRimborso = this.getLattPerRimborso();
        if (lattPerRimborso == null)
            return null;
        it.cnr.contab.config00.sto.bulk.CdrBulk centro_responsabilita = lattPerRimborso.getCentro_responsabilita();
        if (centro_responsabilita == null)
            return null;
        return centro_responsabilita.getCd_centro_responsabilita();
    }

    /**
     * il metodo imposta il valore dell'attributo 'cd_centro_responsabilita' della linea di attività
     */
    public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita) {
        this.getLattPerRimborso().getCentro_responsabilita().setCd_centro_responsabilita(cd_centro_responsabilita);
    }

    /**
     * Il metodo ritorna il valore dell'attributo 'cd_divisa'
     */
    public java.lang.String getCd_divisa() {
        it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk divisa = this.getDivisa();
        if (divisa == null)
            return null;
        return divisa.getCd_divisa();
    }

    /**
     * il  metodo imposta il valore dell'attributo 'cd_divisa'
     */
    public void setCd_divisa(java.lang.String cd_divisa) {
        this.getDivisa().setCd_divisa(cd_divisa);
    }

    /**
     * Il metodo ritorna il valore dell'attributo 'cd_linea_attività'
     */
    public java.lang.String getCd_linea_attivita() {
        it.cnr.contab.config00.latt.bulk.WorkpackageBulk lattPerRimborso = this.getLattPerRimborso();
        if (lattPerRimborso == null)
            return null;
        return lattPerRimborso.getCd_linea_attivita();
    }

    /**
     * il  metodo imposta il valore dell'attributo 'cd_linea_attività'
     */
    public void setCd_linea_attivita(java.lang.String cd_linea_attivita) {
        this.getLattPerRimborso().setCd_linea_attivita(cd_linea_attivita);
    }

    /**
     * Il metodo ritorna il valore dell'attributo 'cd_modalita_pag'
     */
    public java.lang.String getCd_modalita_pag() {
        it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk modalita_pagamento = this.getModalita_pagamento();
        if (modalita_pagamento == null)
            return null;
        return modalita_pagamento.getCd_modalita_pag();
    }

    /**
     * il  metodo imposta il valore dell'attributo 'cd_modalita_pagamento'
     */
    public void setCd_modalita_pag(java.lang.String cd_modalita_pag) {
        this.getModalita_pagamento().setCd_modalita_pag(cd_modalita_pag);
    }

    /**
     * Il metodo ritorna il valore dell'attributo 'cd_termini_pag'
     */
    public java.lang.String getCd_termini_pag() {
        it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk termini_pagamento = this.getTermini_pagamento();
        if (termini_pagamento == null)
            return null;
        return termini_pagamento.getCd_termini_pag();
    }

    /**
     * il  metodo imposta il valore dell'attributo 'cd_termini_pag'
     */
    public void setCd_termini_pag(java.lang.String cd_termini_pag) {
        this.getTermini_pagamento().setCd_termini_pag(cd_termini_pag);
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoSpesaBulk.
     * Ritorna il tipo di documento = ANTICIPO
     */
    public java.lang.String getCd_tipo_doc_amm() {
        return it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk.TIPO_ANTICIPO;
    }

    /**
     * Metodo richiesto dall'interfaccia IDocumentoAmministrativoSpesaBulk
     */

    public void setCd_tipo_doc_amm(java.lang.String newCd_tipo_doc_amm) {
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoSpesaBulk
     * Ritorna il valore dell'attributo 'cd_unita_organizzativa'
     */

    public java.lang.String getCd_uo() {
        return getCd_unita_organizzativa();
    }

    /**
     * Metodo richiesto dall'interfaccia IDocumentoAmministrativoSpesaBulk
     * Imposta il valore dell'attributo 'cd_unita_organizzativa'
     */
    public void setCd_uo(java.lang.String newCd_uo) {
        setCd_unita_organizzativa(newCd_uo);
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBulk
     */

    public java.lang.Class getChildClass() {
        return null;
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBulk
     */
    public java.util.List getChildren() {
        return null;
    }

    /**
     * Metodo richiesto dall' interfaccia IDefferUpdateSaldi
     */

    public it.cnr.jada.bulk.PrimaryKeyHashMap getDefferredSaldi() {
        return deferredSaldi;
    }

    /**
     * Metodo richiesto dall' interfaccia IDefferUpdateSaldi
     */

    public IDocumentoContabileBulk getDefferredSaldoFor(IDocumentoContabileBulk docCont) {
        if (docCont != null && deferredSaldi != null) {
            for (java.util.Iterator i = deferredSaldi.keySet().iterator(); i.hasNext(); ) {
                IDocumentoContabileBulk key = (IDocumentoContabileBulk) i.next();
                if (((OggettoBulk) docCont).equalsByPrimaryKey(key))
                    return key;
            }
        }
        return null;
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoSpesaBulk
     * Il metodo ritorna la descrizione dell'anticipo
     */
    public java.lang.String getDescrizione_spesa() {
        return getDs_anticipo();
    }

    /**
     * Il  metodo ritorna il valore dell'attributo 'dettagliCancellati'
     */
    public java.util.Vector getDettagliCancellati() {
        return dettagliCancellati;
    }

    /**
     * Il metodo imposta il valore dell'attributo 'dettagliCancellati'
     */
    public void setDettagliCancellati(java.util.Vector newDettagliCancellati) {
        dettagliCancellati = newDettagliCancellati;
    }

    /**
     * Il  metodo ritorna il valore dell'attributo 'divisa'
     */
    public DivisaBulk getDivisa() {
        return divisa;
    }

    /**
     * Il metodo imposta il valore dell'attributo 'divisa'
     */
    public void setDivisa(DivisaBulk newDivisa) {
        divisa = newDivisa;
        if ((newDivisa == null) || (newDivisa.getCd_divisa() == null))
            setCambio(null);
    }

    /**
     * Il  metodo ritorna il valore dell'attributo 'documentiContabiliCancellati'
     */
    public java.util.Vector getDocumentiContabiliCancellati() {
        return documentiContabiliCancellati;
    }

    /**
     * Il metodo imposta il valore dell'attributo 'documentiContabiliCancellati'
     */
    public void setDocumentiContabiliCancellati(java.util.Vector newDocumentiContabiliCancellati) {
        documentiContabiliCancellati = newDocumentiContabiliCancellati;
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBulk
     */

    public java.lang.Class getDocumentoAmministrativoClassForDelete() {
        return AnticipoBulk.class;
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBulk
     */

    public java.lang.Class getDocumentoContabileClassForDelete() {
        return it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk.class;
    }

    /**
     * Il metodo ritorna l'esercizio dell'obbligazione
     */
    public java.lang.Integer getEsercizio_obbligazione() {
        it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk scadenza_obbligazione = this.getScadenza_obbligazione();
        if (scadenza_obbligazione == null)
            return null;
        it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = scadenza_obbligazione.getObbligazione();
        if (obbligazione == null)
            return null;
        return obbligazione.getEsercizio();
    }

    /**
     * Il metodo imposta il valore dell'attributo 'esercizio_obbligazione'
     */
    public void setEsercizio_obbligazione(java.lang.Integer esercizio_obbligazione) {
        this.getScadenza_obbligazione().getObbligazione().setEsercizio(esercizio_obbligazione);
    }

    /**
     * Il metodo ritorna l'esercizio di scrivania
     */
    public int getEsercizioScrivania() {
        return esercizioScrivania;
    }

    /**
     * Il metodo imposta il valore dell'attributo 'esercizioScrivania'
     */
    public void setEsercizioScrivania(int newEsercizioScrivania) {
        esercizioScrivania = newEsercizioScrivania;
    }

    /**
     * Dato un Timestamp, tale metodo ritorna la relativa istanza di GregorianCalendar
     */

    public GregorianCalendar getGregorianCalendar(java.sql.Timestamp ts) {
        if (ts == null)
            return null;
        GregorianCalendar gc = (GregorianCalendar) GregorianCalendar.getInstance();
        gc.setTime(ts);

        return (gc);
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBulk
     * Il metodo ritorna l'importo dell'anticipo
     */
    public java.math.BigDecimal getImporto_netto_spesa() {
        return getImporto_spesa();
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBulk
     * Il metodo ritorna l'importo dell'anticipo
     */
    public java.math.BigDecimal getImporto_spesa() {
        return getIm_anticipo_divisa();
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBulk
     * Il metodo ritorna l'importo dell'anticipo
     */

    public java.math.BigDecimal getImportoSignForDelete(java.math.BigDecimal importo) {
        return getIm_anticipo_divisa();
    }

    /**
     * Il metodo ritorna la linea di attivita per il Rimborso
     */
    public it.cnr.contab.config00.latt.bulk.WorkpackageBulk getLattPerRimborso() {
        return lattPerRimborso;
    }

    /**
     * Il metodo imposta il valore dell'attributo 'lattPerRimborso'
     */
    public void setLattPerRimborso(it.cnr.contab.config00.latt.bulk.WorkpackageBulk newLattPerRimborso) {
        lattPerRimborso = newLattPerRimborso;
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBulk
     */
    public java.lang.String getManagerName() {
        return "CRUDAnticipoBP";
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBulk
     */

    public java.lang.String getManagerOptions() {
        // NON CANCELLARE QUESTO COMMENTO: E' DA ABILITARE AL POSTO DEL RESTO NEL CASO DI APERTURA
        // IN MODIFICA
        //return "MTh";

        return "VTh";
    }

    /**
     * Il metodo ritorna la missione alla quale e' associato l'anticipo
     */
    public MissioneBulk getMissione() {
        return missione;
    }

    /**
     * Il metodo imposta il valore dell'attributo 'missione'
     */
    public void setMissione(MissioneBulk newMissione) {
        missione = newMissione;
    }

    /**
     * Il metodo ritorna le modalità di pagamento dell'anticipo
     */
    public java.util.Collection getModalita() {
        return modalita;
    }

    /**
     * Il metodo imposta il valore dell'attributo 'modalita'
     */
    public void setModalita(java.util.Collection newModalita) {
        modalita = newModalita;
    }

    /**
     * Il  metodo ritorna la modalità di pagamento selezionata per l'anticipo
     */
    public it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk getModalita_pagamento() {
        return modalita_pagamento;
    }

    /**
     * Il metodo imposta il valore dell'attributo 'modalita_pagamento'
     */
    public void setModalita_pagamento(it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk newModalita_pagamento) {
        modalita_pagamento = newModalita_pagamento;
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBulk
     */

    public it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable getObbligazioniHash() {
        it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable table = new it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable();
        table.put(getScadenza_obbligazione(), new java.util.Vector());
        return table;
    }

    /**
     * Il metodo ritorna il progressivo della banca dell'anticipo
     */
    public java.lang.Long getPg_banca() {
        it.cnr.contab.anagraf00.core.bulk.BancaBulk banca = this.getBanca();
        if (banca == null)
            return null;
        return banca.getPg_banca();
    }

    /**
     * Il metodo imposta il valore dell'attributo 'pg_banca'
     */
    public void setPg_banca(java.lang.Long pg_banca) {
        this.getBanca().setPg_banca(pg_banca);
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoSpesaBulk
     * Ritorna il progressivo dell'anticipo
     */

    public java.lang.Long getPg_doc_amm() {
        return getPg_anticipo();
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBulk
     * il metodo ritorna il progressivo dell'anticipo
     */

    public void setPg_doc_amm(java.lang.Long newPg) {
        setPg_anticipo(newPg);
    }

    /**
     * Il metodo ritorna l'esercizio originale dell'obbligazione la cui scadenza e' associata all'anticipo
     */
    public Integer getEsercizio_ori_obbligazione() {
        it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk scadenza_obbligazione = this.getScadenza_obbligazione();
        if (scadenza_obbligazione == null)
            return null;
        it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = scadenza_obbligazione.getObbligazione();
        if (obbligazione == null)
            return null;
        return obbligazione.getEsercizio_originale();
    }

    /**
     * Il metodo imposta il valore dell'attributo 'esercizio_ori_obbligazione'
     */
    public void setEsercizio_ori_obbligazione(Integer esercizio_ori_obbligazione) {
        this.getScadenza_obbligazione().getObbligazione().setEsercizio_originale(esercizio_ori_obbligazione);
    }

    /**
     * Il metodo ritorna il progressivo dell'obbligazione la cui scadenza e' associata all'anticipo
     */
    public java.lang.Long getPg_obbligazione() {
        it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk scadenza_obbligazione = this.getScadenza_obbligazione();
        if (scadenza_obbligazione == null)
            return null;
        it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = scadenza_obbligazione.getObbligazione();
        if (obbligazione == null)
            return null;
        return obbligazione.getPg_obbligazione();
    }

    /**
     * Il metodo imposta il valore dell'attributo 'pg_obbligazione'
     */
    public void setPg_obbligazione(java.lang.Long pg_obbligazione) {
        this.getScadenza_obbligazione().getObbligazione().setPg_obbligazione(pg_obbligazione);
    }

    /**
     * Il metodo ritorna il progressivo della scadenza e' associata all'anticipo
     */
    public java.lang.Long getPg_obbligazione_scadenzario() {
        it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk scadenza_obbligazione = this.getScadenza_obbligazione();
        if (scadenza_obbligazione == null)
            return null;
        return scadenza_obbligazione.getPg_obbligazione_scadenzario();
    }

    /**
     * Il metodo imposta il valore dell'attributo 'pg_obbligazione_scadenzario'
     */
    public void setPg_obbligazione_scadenzario(java.lang.Long pg_obbligazione_scadenzario) {
        this.getScadenza_obbligazione().setPg_obbligazione_scadenzario(pg_obbligazione_scadenzario);
    }

    /**
     * Il  metodo ritorna il Rimborso dell'anticipo
     */
    public RimborsoBulk getRimborso() {
        return rimborso;
    }

    /**
     * Il metodo imposta il valore dell'attributo 'rimborso'
     */
    public void setRimborso(RimborsoBulk newRimborso) {
        rimborso = newRimborso;
    }

    /**
     * Il metodo ritorna il valore dell'attributo 'riportata'
     */
    public java.lang.String getRiportata() {
        return riportata;
    }

    /**
     * Insert the method's description here.
     * Creation date: (02/11/2004 14.30.02)
     *
     * @return java.lang.String
     */
    public java.lang.String getRiportataInScrivania() {
        return riportataInScrivania;
    }

    /**
     * il metodo ritorna la scadenza associata all'anticipo
     */
    public it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk getScadenza_obbligazione() {
        return scadenza_obbligazione;
    }

    /**
     * Il metodo imposta il valore dell'attributo 'scadenza_obbligazione'
     */
    public void setScadenza_obbligazione(it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk newScadenza_obbligazione) {
        scadenza_obbligazione = newScadenza_obbligazione;
    }

    /**
     * il metodo ritorna una copia della scadenza associata all'anticipo
     */
    public it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk getScadenza_obbligazioneClone() {
        return scadenza_obbligazioneClone;
    }

    /**
     * Il metodo imposta il valore dell'attributo 'scadenza_obbligazione_clone'
     */
    public void setScadenza_obbligazioneClone(it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk newScadenza_obbligazioneClone) {
        scadenza_obbligazioneClone = newScadenza_obbligazioneClone;
    }

    /**
     * Il metodo restituisce il dictionary per la gestione degli stati Cofi dell'anticipo
     */

    public java.util.Dictionary getStato_cofiKeys() {
        return STATO_COFI;
    }

    /**
     * Il metodo restituisce il dictionary per la gestione degli stati Cofi dell'anticipo
     */

    public Dictionary getStato_cofiKeysForSearch() {
        it.cnr.jada.util.OrderedHashtable d = (it.cnr.jada.util.OrderedHashtable) getStato_cofiKeys();
        if (d == null) return null;

        it.cnr.jada.util.OrderedHashtable clone = (it.cnr.jada.util.OrderedHashtable) d.clone();
        clone.remove(STATO_INIZIALE_COFI);
        return clone;
    }

    /**
     * Il metodo restituisce il dictionary per la gestione degli stati della Registrazione nel Fondo
     * Economale dell'anticipo
     */
    public java.util.Dictionary getStato_pagamento_fondo_ecoKeys() {
        if (getStato_pagamento_fondo_eco() != null && STATO_REGISTRATO_FONDO_ECO.equalsIgnoreCase(getStato_pagamento_fondo_eco())) {
            return STATO_FONDO_ECO;
        }

        it.cnr.jada.util.OrderedHashtable oh = (it.cnr.jada.util.OrderedHashtable) ((it.cnr.jada.util.OrderedHashtable) STATO_FONDO_ECO).clone();
        oh.remove(STATO_REGISTRATO_FONDO_ECO);
        return oh;
    }

    /**
     * Il metodo restituisce il dictionary per la gestione degli stati della Registrazione nel Fondo
     * Economale dell'anticipo
     */
    public java.util.Dictionary getStato_pagamento_fondo_ecoKeysForSearch() {
        return STATO_FONDO_ECO;
    }

    /**
     * Il metodo ritorna i termini di pagamento dell'anticipo
     */
    public java.util.Collection getTermini() {
        return termini;
    }

    /**
     * Il metodo imposta il valore dell'attributo 'termini'
     */
    public void setTermini(java.util.Collection newTermini) {
        termini = newTermini;
    }

    /**
     * Il metodo ritorna il termine di pagamento selezionato per l'anticipo
     */
    public it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk getTermini_pagamento() {
        return termini_pagamento;
    }

    /**
     * Il metodo imposta il valore dell'attributo 'termini_pagamento'
     */
    public void setTermini_pagamento(it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk newTermini_pagamento) {
        termini_pagamento = newTermini_pagamento;
    }

    /**
     * Il metodo ritorna il terzo dell'anticipo, come istanza della tabella 'TerzoBulk'
     */
    public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getTerzo() {
        if (getV_terzo() == null)
            return null;
        return getV_terzo().getTerzo();
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoSpesaBulk
     * Il  metodo ritorna il terzo dell'anticipo
     */

    public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getTerzo_spesa() {
        return getTerzo();
    }

    /**
     * Il metodo restituisce il dictionary per la gestione di ti_associato_manrev
     */

    public java.util.Dictionary getTi_associato_manrevKeys() {
        return TI_ASSOCIATO_MANREV;
    }

    /**
     * Il metodo ritorna il terzo dell'anticipo, come istanza della vista 'V_terzo_per_compensoBulk'
     */
    public it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk getV_terzo() {
        return v_terzo;
    }

    /**
     * Il metodo imposta il valore dell'attributo 'v_terzo'
     */
    public void setV_terzo(it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk newV_terzo) {
        v_terzo = newV_terzo;
    }

    /**
     * Il metodo ritorna TRUE se l'anticipo e' stato rimborsato
     */
    public boolean hasRimborso() {
        return getRimborso() != null;
    }

    /**
     * Il metodo inzializza l'anticipo da modificare
     */
    public OggettoBulk initializeForInsert(CRUDBP bp, ActionContext context) {
        it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = null;
        unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
        setCd_cds(unita_organizzativa.getUnita_padre().getCd_unita_organizzativa());
        setCd_cds_origine(getCd_cds());
        setCd_unita_organizzativa(unita_organizzativa.getCd_unita_organizzativa());
        setCd_uo_origine(getCd_unita_organizzativa());
        setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));

        setV_terzo(new V_terzo_per_compensoBulk());
        setTi_anagrafico(ANAG_DIPENDENTE);

        setStato_cofi(STATO_INIZIALE_COFI);
        setStato_coge(STATO_INIZIALE_COGE);
        setStato_coan(STATO_ESCLUSO_COGE);
        setTi_associato_manrev(STATO_INIZIALE_MANREV);
        setStato_pagamento_fondo_eco(STATO_LIBERO_FONDO_ECO);
        setFl_associato_missione(new Boolean(false));

        //	La data di registrazione, la data competenza coge da/a
        //	sono inizializzate dalla Component

        return this;
    }

    /**
     * Il metodo inzializza l'anticipo da ricercare
     */
    public OggettoBulk initializeForSearch(CRUDBP bp, ActionContext context) {
        it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = null;
        unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
        setCd_cds(unita_organizzativa.getUnita_padre().getCd_unita_organizzativa());
        setCd_unita_organizzativa(unita_organizzativa.getCd_unita_organizzativa());
        setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));

        setV_terzo(new V_terzo_per_compensoBulk());
        setTi_anagrafico(null);

        if (bp instanceof IDocumentoAmministrativoSpesaBP && ((IDocumentoAmministrativoSpesaBP) bp).isSpesaBP()) {
            setStato_cofi(STATO_CONTABILIZZATO);
            setStato_pagamento_fondo_eco(STATO_ASSEGNATO_FONDO_ECO);

            if (it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC.equalsIgnoreCase(unita_organizzativa.getCd_tipo_unita()))
                setCd_unita_organizzativa(null);
        }

        return this;
    }

    /**
     * Il metodo inzializza il terzo dell'anticipo con valori NULL
     */
    public void inizializzaTerzo() {
        V_terzo_per_compensoBulk vTerzo = new V_terzo_per_compensoBulk();
        vTerzo.setTi_dipendente_altro(getTi_anagrafico());

        setV_terzo(vTerzo);
        setCd_terzo(null);
        setNome(null);
        setCognome(null);
        setRagione_sociale(null);
        setCodice_fiscale(null);
        setPartita_iva(null);
        setTermini(null);
        setTermini_pagamento(null);
        setModalita(null);
        setModalita_pagamento(null);
        setBanca(null);
    }

    /**
     * Il metodo stabilisce se i dati relativi alla banca sono abilitati.
     * Abilitati :
     * - se la modalità di pagamento e' stata selezionata
     * - se l'anticipo non e' stato pagato
     * - se l'esercizio di scrivania e quello del documento sono diversi da quello solare
     * l' obbligazione non deve essere stata riportata (isROPerChiusura)
     */
    public boolean isAbledToInsertBank() {
        return !(getV_terzo() != null &&
                getV_terzo().getCrudStatus() == OggettoBulk.NORMAL &&
                getModalita_pagamento() != null &&
                !isPagata() &&
                !isROPerChiusura());
    }

    /**
     * Il metodo ritorna TRUE se l'anticipo e' stato cancellato logicamente
     */
    public boolean isAnnullato() {
        return STATO_ANNULLATO.equalsIgnoreCase(getStato_cofi());
    }

    /**
     * Il metodo ritorna TRUE se l'anticipo e' associato ad una missione
     */
    public boolean isAnticipoConMissione() {
        return (getFl_associato_missione() != null && getFl_associato_missione().booleanValue());
    }

    /**
     * Il metodo ritorna TRUE se l'anticipo non e' pagato via fondo economale
     */
    public boolean isByFondoEconomale() {

        return !STATO_LIBERO_FONDO_ECO.equalsIgnoreCase(getStato_pagamento_fondo_eco());
    }

    /**
     * il metodo ritorna TRUE se l'anticipo e' stato cancellato logicamente
     */
    public boolean isCancellatoLogicamente() {
        return getDt_cancellazione() != null;
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoBulk
     */

    public boolean isDeleting() {
        return false;
    }

    /**
     * Il metodo ritorna TRUE se l'anticipo non e' pagato, non e' associato a missione non e'
     * stato rimborsato
     */

    public boolean isEditable() {
        return (!isPagata() && !isAnticipoConMissione() && !hasRimborso());
    }

    /**
     * Il  metodo riotrna TRUE se l'anticipo e' stato pagato
     */
    public boolean isPagata() {
        return (STATO_PAGATO.equalsIgnoreCase(getStato_cofi())) ||
                (STATO_REGISTRATO_FONDO_ECO.equalsIgnoreCase(getStato_pagamento_fondo_eco()));
    }

    /**
     * Il metodo ritorna TRUE se l'anticipo e' stato riportato
     */
    public boolean isRiportata() {
        return !NON_RIPORTATO.equals(riportata);
    }

    /**
     * Il metodo imposta il valore dell'attributo 'riportata'
     */
    public void setRiportata(java.lang.String newRiportata) {
        riportata = newRiportata;
    }

    /**
     * isRiportataInScrivania method comment.
     */
    public boolean isRiportataInScrivania() {

        return !NON_RIPORTATO.equals(riportataInScrivania);
    }

    /**
     * Insert the method's description here.
     * Creation date: (02/11/2004 14.30.02)
     *
     * @param newRiportataInScrivania java.lang.String
     */
    public void setRiportataInScrivania(java.lang.String newRiportataInScrivania) {
        riportataInScrivania = newRiportataInScrivania;
    }

    /**
     * Il metodo stabilisce se il campo cd_centro_responsabilita e' abilitato.
     * Disabilitato :
     * - se l'anticipo è rimborsato
     * - se l'esercizio di scrivania e' diverso da quello solare e l' obbligazione e' stata riportata
     * (isROPerChiusura)
     */
    public boolean isROCd_centro_responsabilita() {
        return isROCd_linea_attivita();
    }

    /**
     * Il metodo stabilisce se il campo del codice della linea di attivita' e' abilitato.
     * Disabilitato :
     * - se l'anticipo è rimborsato
     * - se l'esercizio di scrivania e quello del documento sono diversi da quello solare
     * e l' obbligazione e' stata riportata (isROPerChiusura)
     */
    public boolean isROCd_linea_attivita() {
        return isROFind_latt() ||
                (getLattPerRimborso() == null ||
                        (getLattPerRimborso() != null && getLattPerRimborso().getCrudStatus() == OggettoBulk.NORMAL));
    }

    public boolean isRODivisa() {
        return divisa == null || divisa.getCrudStatus() == NORMAL;
    }

    /**
     * Il metodo stabilisce se il search tool della linea di attivita e' abilitato o meno
     * Disabilitato :
     * - se l'anticipo e' stato rimborsato
     * - se l'esercizio dell'anticipo e quello di scrivania sono diverse da quello solare
     * e l'obbligazione e' riportata
     */
    public boolean isROFind_latt() {
        if (hasRimborso() || !isPagata())
            return true;

        if (getEsercizio() == null)
            return true;

        if (getEsercizio().intValue() == getEsercizioScrivania())
            return isRiportata();
            //Gennaro Borriello/Luisa Farinella - (03/11/2004 19.04.48)
            // Fix sul controllo dello "Stato Riportato"
        else if (getEsercizio().intValue() != getEsercizioScrivania())
            return !isRiportataInScrivania();


        return false;
    }

    /**
     * Il  metodo stabilisce se il search tool del terzo e' abilitato o meno.
     * Disabilitato :
     * - se l'esercizio di scrivania e quello del documento sono diversi da quello solare
     * e l' obbligazione e' stata riportata (isROPerChiusura)
     * - se l'anticipo e' pagato o associato a missione o rimborsato (isEditable)
     */
    public boolean isROFindTerzo() {
        return (!isEditable() || isROPerChiusura());
    }

    /**
     * Il metodo definisce le regole base per l'abilitazione dei campi in caso di obbligazione riportata
     * Se carico un anticipo con esercizio precedente a quello solare :
     * - esercizio scrivania != anno solare e obbligazione riportata --> nessuna modifica al documento
     * - esercizio scrivania != anno solare e obbligazione non riportata --> qualsiasi modifica consentita
     * - esercizio scrivania = anno solare e obbligazione riportata --> nessuna modifica tranne CoFi e Annulla
     * - esercizio scrivania = anno solare e obbligazione non riportata --> nessuna modifica al documento
     */

    public boolean isROPerChiusura() {
        if (getEsercizio() == null)
            return false;

        // Gennaro Borriello/Farinella Luisa - (05/11/2004 12.23.28)
        // Modif. relativa alla nuova gestione di isRiportata()
        // MB - (06/11/2004 12.23.28)
        if (getEsercizio().intValue() == getEsercizioScrivania()) {
            return isRiportata();
        } else
            return true;

    }

    /**
     * Il  metodo stabilisce se il campo 'select' relativo al Fondo Economale e' abilitato o meno.
     * Disabilitato :
     * - se l'esercizio di scrivania e quello del documento sono diversi da quello solare
     * e l' obbligazione e' stata riportata (isROPerChiusura)
     * - se l'anticipo e' pagato o associato a missione o rimborsato (isEditable)
     */
    public boolean isROStato_pagamento_fondo_eco() {
        return !isEditable() || isROPerChiusura();
    }

    /**
     * Il  metodo stabilisce se il terzo e' abilitato o meno.
     * Disabilitato :
     * - se l'esercizio di scrivania e quello del documento sono diversi da quello solare
     * e l' obbligazione e' stata riportata (isROPerChiusura)
     * - se l'anticipo e' pagato o associato a missione o rimborsato (isEditable)
     */
    public boolean isROTerzo() {
        return !isEditable() || isROPerChiusura() || v_terzo == null || v_terzo.getCrudStatus() == NORMAL;

    }

    /**
     * Il metodo verifico se la scadenza appena associata era stata messa precedentemente tra quelle da
     * da cancellare.
     * Cio' puo' succedere quando scollego e ricollego una stessa scadenza o la scadenza che gia'
     * avevo salvato con l'anticipo (clone) o una scadenza appena creata ma non ancora
     * salvata con l'anticipo (numerazione negativa)
     */

    public boolean isScadenzaDaRimuovereDaiCancellati() {
        if (getDocumentiContabiliCancellati() == null || getDocumentiContabiliCancellati().isEmpty())
            return false;

        if (getScadenza_obbligazioneClone() != null && getScadenza_obbligazione().equalsByPrimaryKey(getScadenza_obbligazioneClone()))
            return true;

        Obbligazione_scadenzarioBulk scadenza = null;
        for (Iterator i = getDocumentiContabiliCancellati().iterator(); i.hasNext(); ) {
            scadenza = (Obbligazione_scadenzarioBulk) i.next();
            if (scadenza.equalsByPrimaryKey(getScadenza_obbligazione()))
                return true;
        }
        return false;
    }

    /**
     * Il metodo verifico se il terzo e' valido rispetto alla data di resistrazione dell'anticipo.
     */

    public boolean isTerzoValido() {
        if ((getV_terzo() == null) || (getV_terzo().getDt_fine_validita_terzo() == null))
            return true;

        if (getDt_registrazione() == null)
            return false;

        return ((getV_terzo().getDt_fine_validita_terzo().compareTo(getDt_registrazione()) >= 0));
    }

    /**
     * Metodo richiesto dall' interfaccia IDefferUpdateSaldi
     * Il metodo rimuove il documento contabile dalla mappa dei saldi in differita
     */

    public void removeFromDefferredSaldi(it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk docCont) {
        if (docCont != null && deferredSaldi != null)
            deferredSaldi.remove(docCont);
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoSpesaBulk
     */

    public int removeFromDettagliCancellati(it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk dettaglio) {
        if (BulkCollections.containsByPrimaryKey(getDettagliCancellati(), (OggettoBulk) dettaglio))
            getDettagliCancellati().remove(BulkCollections.indexOfByPrimaryKey(getDettagliCancellati(), (OggettoBulk) dettaglio));
        return getDettagliCancellati().size() - 1;
    }

    /**
     * Metodo richiesto dall' interfaccia IDocumentoAmministrativoSpesaBulk
     */
    public int removeFromDocumentiContabiliCancellati(it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk dettaglio) {
        if (getDocumentiContabiliCancellati() == null)
            return -1;

        if (BulkCollections.containsByPrimaryKey(getDocumentiContabiliCancellati(), (OggettoBulk) dettaglio))
            getDocumentiContabiliCancellati().remove(BulkCollections.indexOfByPrimaryKey(getDocumentiContabiliCancellati(), (OggettoBulk) dettaglio));
        return getDocumentiContabiliCancellati().size() - 1;
    }

    /**
     * Metodo richiesto dall' interfaccia IDefferUpdateSaldi
     * Imposta la mappa dei saldi in differita al valore di default
     */

    public void resetDefferredSaldi() {
        deferredSaldi = null;
    }

    /**
     * Il metodo imposta il valore dell'attributo 'isDeleting'
     */
    public void setIsDeleting(boolean deletingStatus) {
    }

    @Override
    public Scrittura_partita_doppiaBulk getScrittura_partita_doppia() {
        return scrittura_partita_doppia;
    }

    /**
     * Il metodo sincronizza le scadenze elaborate dall'anticipo ad ogni rientro da ogni aggiornamento
     * manuale della scadenza associata.
     * Se la scadenza appena aggiornata appartiene alla stessa obbligazione di una delle scadenze elaborate,
     * l'utente avrebbe potute modificare una di queste.
     * Se non dovessi trovare una delle scadenze elaborate dall'anticipo e appartenente alla stessa obbligazione
     * di quella appena aggiornata significa che l'utente l'ha eliminata fisicamente.
     */
    public void sincronizzaScadenzeCancellate(Obbligazione_scadenzarioBulk newScadenza) {
        if (getDocumentiContabiliCancellati() == null || getDocumentiContabiliCancellati().isEmpty())
            return;

        if (!newScadenza.getObbligazione().equalsByPrimaryKey(getScadenza_obbligazione().getObbligazione()))
            return;

        boolean trovata = false;
        BulkList coll = newScadenza.getObbligazione().getObbligazione_scadenzarioColl();
        if (coll == null)
            return;

        for (Iterator c = ((Vector) getDocumentiContabiliCancellati().clone()).iterator(); c.hasNext(); ) {
            Obbligazione_scadenzarioBulk aScadCanc = (Obbligazione_scadenzarioBulk) c.next();
            trovata = false;

            for (Iterator i = coll.iterator(); i.hasNext(); ) {
                Obbligazione_scadenzarioBulk aScadenza = (Obbligazione_scadenzarioBulk) i.next();

                if (aScadenza.equalsByPrimaryKey(aScadCanc)) {
                    getDocumentiContabiliCancellati().remove(aScadCanc);
                    getDocumentiContabiliCancellati().add(aScadenza);
                    trovata = true;
                }
            }
            if (!trovata && aScadCanc.getObbligazione().equalsByPrimaryKey(newScadenza.getObbligazione()))
                getDocumentiContabiliCancellati().remove(aScadCanc);
        }
    }

    /**
     * Il metodo valida la data di registrazione. Essa deve essere nell'esercizio corrente e non può essere futura
     */

    public void validaDataRegistrazione(ActionContext context) throws it.cnr.jada.comp.ApplicationException, javax.ejb.EJBException {
        if (getDt_registrazione() == null)
            return;

        if (getDt_registrazione().after(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()))
            throw new it.cnr.jada.comp.ApplicationException("La data di registrazione non puo' essere futura !");

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(getDt_registrazione());
        int annoDataRegistrazione = calendar.get(java.util.Calendar.YEAR);

        if (annoDataRegistrazione != getEsercizio().intValue())
            throw new it.cnr.jada.comp.ApplicationException("La Data registrazione deve essere nell'esercizio in corso");
    }

    /**
     * Il metodo valida i dati del Tab Anagrfico
     */
    public void validaTabAnagrafico() throws ValidationException {
        if (getDt_registrazione() == null)
            throw new ValidationException("Valorizzare la Data di Registrazione !");

        if (getCd_terzo() == null || getV_terzo().getCd_terzo() == null)
            throw new ValidationException("Selezionare un Terzo !");

        if (getCd_modalita_pag() == null)
            throw new it.cnr.jada.action.MessageToUser("Selezionare la Modalita' di Pagamento");

        if (getPg_banca() == null)
            throw new it.cnr.jada.action.MessageToUser("Selezionare un'altra Modalita' di Pagamento");
    }

    /**
     * Il metodo valida l'anticipo che si sta creando/modificando
     */
    public void validate() throws ValidationException {
        if ((getCd_terzo() == null) || getV_terzo() == null || getV_terzo().getCd_terzo() == null)
            throw new ValidationException("Selezionare il terzo !");

        if ((getIm_anticipo_divisa() == null) || (getIm_anticipo_divisa().compareTo(new java.math.BigDecimal(0)) == 0))
            throw new ValidationException("Valorizzare l'importo dell'anticipo !");

        setIm_anticipo(getIm_anticipo_divisa());

        super.validate();

        if (getCd_terzo() == null)
            throw new ValidationException("Inserire un terzo !");

        if (getDt_registrazione() == null)
            throw new ValidationException("Valorizzare la data di registrazione!");

        // 	La validazione della scadenza di obbligazione legata all'anticipo viene fatta nel modificaConBulk
        //	e nell creaConBulk
    }

}