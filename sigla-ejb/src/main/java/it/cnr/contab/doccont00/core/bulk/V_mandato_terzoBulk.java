package it.cnr.contab.doccont00.core.bulk;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;

import java.util.Optional;

/**
 * Insert the type's description here.
 * Creation date: (10/12/2002 14.29.27)
 *
 * @author: Ilaria Gorla
 */
public class V_mandato_terzoBulk extends MandatoIBulk {

    private java.lang.Integer cd_terzo;
    private java.lang.Integer cd_anag;
    private java.lang.String denominazione_sede;


    /**
     * V_mandato_terzoBulk constructor comment.
     */
    public V_mandato_terzoBulk() {
        super();
    }

    /**
     * V_mandato_terzoBulk constructor comment.
     *
     * @param cd_cds     java.lang.String
     * @param esercizio  java.lang.Integer
     * @param pg_mandato java.lang.Long
     */
    public V_mandato_terzoBulk(String cd_cds, Integer esercizio, Long pg_mandato) {
        super(cd_cds, esercizio, pg_mandato);
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/12/2002 14.32.57)
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getCd_anag() {
        return cd_anag;
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/12/2002 14.32.57)
     *
     * @param newCd_anag java.lang.Integer
     */
    public void setCd_anag(java.lang.Integer newCd_anag) {
        cd_anag = newCd_anag;
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/12/2002 14.32.57)
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getCd_terzo() {
        return cd_terzo;
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/12/2002 14.32.57)
     *
     * @param newCd_terzo java.lang.Integer
     */
    public void setCd_terzo(java.lang.Integer newCd_terzo) {
        cd_terzo = newCd_terzo;
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/12/2002 16.23.51)
     *
     * @return java.lang.String
     */
    public java.lang.String getDenominazione_sede() {
        return denominazione_sede;
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/12/2002 16.23.51)
     *
     * @param newDenominazione_sede java.lang.String
     */
    public void setDenominazione_sede(java.lang.String newDenominazione_sede) {
        denominazione_sede = newDenominazione_sede;
    }

    /**
     * Inizializza il ricevente per la visualizzazione in un <code>FormController</code>
     * in stato <code>EDIT</code>.
     * Questo metodo viene invocato automaticamente da un
     * <code>it.cnr.jada.util.action.CRUDBP</code> quando viene inizializzato
     * per la modifica di un OggettoBulk.
     */
    public OggettoBulk initializeForEdit(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) {
        return new MandatoIBulk(getCd_cds(), getEsercizio(), getPg_mandato());
    }

    @Override
    public OggettoBulk initializeForSearch(CRUDBP bp, ActionContext context) {
        setMandato_terzo(new Mandato_terzoIBulk());
        getMandato_terzo().setTerzo(new TerzoBulk());
        return super.initializeForSearch(bp, context);
    }
}
