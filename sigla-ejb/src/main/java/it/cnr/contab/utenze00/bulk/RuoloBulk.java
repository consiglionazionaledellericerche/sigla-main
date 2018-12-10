package it.cnr.contab.utenze00.bulk;

/**
 * Rappresenta una aggregazione logica di Accessi
 */


import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.ValidationException;

import java.util.List;
import java.util.Vector;

public class RuoloBulk extends RuoloBase {
    private Tipo_ruoloBulk tipo_ruolo = new Tipo_ruoloBulk();
    private it.cnr.jada.bulk.BulkList ruolo_accessi = new it.cnr.jada.bulk.BulkList();
    private List ruolo_accessi_disponibili = new Vector();

    private List accessi = new it.cnr.jada.util.Collect(ruolo_accessi, "accesso");
    private List accessi_disponibili = new it.cnr.jada.util.Collect(ruolo_accessi_disponibili, "accesso");

    private CdsBulk cds = new CdsBulk();
    private UtenteBulk gestore;

    public RuoloBulk() {
        super();
    }

    public RuoloBulk(java.lang.String cd_ruolo) {
        super(cd_ruolo);
    }

    /**
     * Aggiunge una nuova associazione ruolo-accesso (Ruolo_accessoBulk) alla lista di accessi definiti per il ruolo
     * inizializzandone alcuni campi e rimuove tale associazione dalla lista di quelle ancora disponibili per il ruolo
     * @param index indice della collezione di ruoli-accessi disponibili da cui rimuovere l'associazione
     * @return Ruolo_accessoBulk l'associazione ruolo-accesso aggiunta
     */

    public Ruolo_accessoBulk addToRuolo_accessi(int index) {
        Ruolo_accessoBulk ra = (Ruolo_accessoBulk) this.ruolo_accessi_disponibili.remove(index);
        ra.setCd_ruolo(getCd_ruolo());
        this.ruolo_accessi.add(ra);
        return ra;
    }

    /**
     * Aggiunge una nuova associazione ruolo-accesso (Ruolo_accessoBulk) alla lista di accessi definiti per il ruolo
     * @param ra Ruolo_accessoBulk da aggiungere
     * @return int numero di associazioni ruolo-accessi
     */

    public int addToRuolo_accessi(Ruolo_accessoBulk ra) {
        this.ruolo_accessi.add(ra);
        return this.ruolo_accessi.size() - 1;
    }

    /**
     * @return java.util.List
     */
    public java.util.List getAccessi() {
        return accessi;
    }

    /**
     * @return java.util.List
     */
    public java.util.List getAccessi_disponibili() {
        return accessi_disponibili;
    }

    /**
     * Imposta la lista di associazioni ruolo-accesso (Ruolo_accessoBulk) disponibili ad un ruolo a partire
     * da una lista di accessi (AccessoBulk)
     * @param newAccessi_disponibili lista di AccessoBulk
     */

    public void setAccessi_disponibili(java.util.List newAccessi_disponibili) {
        for (java.util.Iterator i = newAccessi_disponibili.iterator(); i.hasNext(); ) {
            AccessoBulk accesso = (AccessoBulk) i.next();
            if (this.accessi.contains(accesso)) continue;
            Ruolo_accessoBulk ra = new Ruolo_accessoBulk();
            ra.setAccesso(accesso);
            ruolo_accessi_disponibili.add(ra);
        }
    }

    public void resetAccessi()
    {
        accessi_disponibili.clear();
    }
    /**
     * Restituisce la collezione di Ruolo_accessoBulk associata al ruolo per
     * renderla persistente contestualmente alla gestione della persistenza del ruolo stesso
     */

    public BulkCollection[] getBulkLists() {
        return new it.cnr.jada.bulk.BulkCollection[]{
                ruolo_accessi
        };
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'cds'
     *
     * @return Il valore della proprietà 'cds'
     */
    public it.cnr.contab.config00.sto.bulk.CdsBulk getCds() {
        return cds;
    }

    /**
     * <!-- @TODO: da completare -->
     * Imposta il valore della proprietà 'cds'
     *
     * @param newCds    Il valore da assegnare a 'cds'
     */
    public void setCds(it.cnr.contab.config00.sto.bulk.CdsBulk newCds) {
        cds = newCds;
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'gestore'
     *
     * @return Il valore della proprietà 'gestore'
     */
    public UtenteBulk getGestore() {
        return gestore;
    }

    /**
     * <!-- @TODO: da completare -->
     * Imposta il valore della proprietà 'gestore'
     *
     * @param newGestore    Il valore da assegnare a 'gestore'
     */
    public void setGestore(UtenteBulk newGestore) {
        gestore = newGestore;
    }

    /**
     * @return it.cnr.jada.bulk.BulkList
     */
    public it.cnr.jada.bulk.BulkList getRuolo_accessi() {
        return ruolo_accessi;
    }

    /**
     * Rimuove una associazione ruolo-accesso (Ruolo_accessoBulk) alla lista di accessi definiti per il ruolo
     * e aggiunge tale associazione alla lista di quelle ancora disponibili per il ruolo
     * @param index indice della collezione di ruoli-accessi assegnati al ruolo da cui rimuovere l'associazione
     * @return Ruolo_accessoBulk l'associazione rimossa
     */

    public Ruolo_accessoBulk removeFromRuolo_accessi(int index) {
        Ruolo_accessoBulk ra = (Ruolo_accessoBulk) this.ruolo_accessi.remove(index);
        this.ruolo_accessi_disponibili.add(ra);
        return ra;
    }

    /**
     * Esegue la validazione formale dei campi di input
     */

    public void validate() throws ValidationException {
        if (getCd_ruolo() == null)
            throw new ValidationException("Il campo CODICE RUOLO e' obbligatorio");
        if (getDs_ruolo() == null)
            throw new ValidationException("Il campo DESCRIZIONE e' obbligatorio");
    }


    public Tipo_ruoloBulk getTipo_ruolo() {
        return tipo_ruolo;
    }

    public void setTipo_ruolo(Tipo_ruoloBulk bulk) {
        tipo_ruolo = bulk;
    }

    public java.lang.String getTipo() {
        it.cnr.contab.utenze00.bulk.Tipo_ruoloBulk tipo_ruolo = this.getTipo_ruolo();
        if (tipo_ruolo == null)
            return null;
        return tipo_ruolo.getTipo();

    }

    public void setTipo(java.lang.String tipo) {
        this.getTipo_ruolo().setTipo(tipo);
    }

}
