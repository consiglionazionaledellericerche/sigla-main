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

package it.cnr.contab.doccont00.intcass.bulk;

import it.cnr.jada.bulk.ValidationException;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Insert the type's description here.
 * Creation date: (21/03/2003 9.42.33)
 *
 * @author: Simonetta Costa
 */
public class SelezionaSospesiCNRBulk extends it.cnr.jada.bulk.OggettoBulk {
    public static final String TIPO_ENTRATA = "E";
    public static final String TIPO_SPESA = "S";
    public static final String TIPO_ENTRAMBI = "X";
    public final static java.util.Dictionary ti_entrata_spesaKeys;
    public final static String STATO_SOSP_NON_APPLICATA = "N";
    public final static String STATO_SOSP_INIZIALE = "I";
    public final static String STATO_SOSP_ASS_A_CDS = "A";
    public final static String STATO_SOSP_IN_SOSPESO = "S";
    public final static Dictionary stato_sospesoKeys;
    static private java.util.Hashtable ti_cc_biKeys;

	static {
        ti_entrata_spesaKeys = new it.cnr.jada.util.OrderedHashtable();
        ti_entrata_spesaKeys.put(TIPO_ENTRATA, "Entrata");
        ti_entrata_spesaKeys.put(TIPO_SPESA, "Spesa");
        ti_entrata_spesaKeys.put(TIPO_ENTRAMBI, "Entrambi");
    }

    static {
        stato_sospesoKeys = new it.cnr.jada.util.OrderedHashtable();
        stato_sospesoKeys.put(STATO_SOSP_NON_APPLICATA, "Non applicata");
        stato_sospesoKeys.put(STATO_SOSP_INIZIALE, "Iniziale");
        stato_sospesoKeys.put(STATO_SOSP_ASS_A_CDS, "Assegnato a CdS");
        stato_sospesoKeys.put(STATO_SOSP_IN_SOSPESO, "In sospeso");
    }

    protected Boolean flRicercaSospesiAssegnati = new Boolean(true);
    protected Boolean flRicercaSospesiInSospesoSelezionati = new Boolean(true);
    protected Boolean flRicercaSospesiInSospeso = new Boolean(true);
    protected Boolean flRicercaSospesiRiaccredito = new Boolean(false);

    protected String ti_entrata_spesa = TIPO_ENTRAMBI;


	/**
     * SelezionaSospesiCNRBulk constructor comment.
     */
    public SelezionaSospesiCNRBulk() {
        super();
    }

    /**
     * Insert the method's description here.
     * Creation date: (21/03/2003 9.48.34)
     *
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getRicercaSospesiAssegnati() {
        return flRicercaSospesiAssegnati;
    }

    /**
     * Insert the method's description here.
     * Creation date: (21/03/2003 9.48.34)
     *
     * @param newRicercaSospesiAssegnati java.lang.Boolean
     */
    public void setRicercaSospesiAssegnati(java.lang.Boolean newRicercaSospesiAssegnati) {
        flRicercaSospesiAssegnati = newRicercaSospesiAssegnati;
    }

    /**
     * Insert the method's description here.
     * Creation date: (21/03/2003 9.44.08)
     *
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getRicercaSospesiInSospeso() {
        return flRicercaSospesiInSospeso;
    }

    /**
     * Insert the method's description here.
     * Creation date: (21/03/2003 9.44.08)
     *
     * @param newRicercaSospesiInSospeso java.lang.Boolean
     */
    public void setRicercaSospesiInSospeso(java.lang.Boolean newRicercaSospesiInSospeso) {
        flRicercaSospesiInSospeso = newRicercaSospesiInSospeso;
    }

    /**
     * Insert the method's description here.
     * Creation date: (21/03/2003 9.48.34)
     *
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getRicercaSospesiInSospesoSelezionati() {
        return flRicercaSospesiInSospesoSelezionati;
    }

    /**
     * Insert the method's description here.
     * Creation date: (21/03/2003 9.48.34)
     *
     * @param newRicercaSospesiInSospesoSelezionati java.lang.Boolean
     */
    public void setRicercaSospesiInSospesoSelezionati(java.lang.Boolean newRicercaSospesiInSospesoSelezionati) {
        flRicercaSospesiInSospesoSelezionati = newRicercaSospesiInSospesoSelezionati;
    }

    public void setRicercaSospesiRiaccredito(java.lang.Boolean flRicercaSospesiRiaccredito) {
        this.flRicercaSospesiRiaccredito =flRicercaSospesiRiaccredito;
    }

    public Boolean getRicercaSospesiRiaccredito() {
        return this.flRicercaSospesiRiaccredito;
    }
    /**
     * Insert the method's description here.
     * Creation date: (21/03/2003 11.05.15)
     *
     * @return java.lang.String
     */
    public java.lang.String getTi_entrata_spesa() {
        return ti_entrata_spesa;
    }

    /**
     * Insert the method's description here.
     * Creation date: (21/03/2003 11.05.15)
     *
     * @param newTi_entrata_spesa java.lang.String
     */
    public void setTi_entrata_spesa(java.lang.String newTi_entrata_spesa) {
        ti_entrata_spesa = newTi_entrata_spesa;
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
        if (!getRicercaSospesiAssegnati().booleanValue() &&
                !getRicercaSospesiInSospeso().booleanValue() &&
                !getRicercaSospesiInSospesoSelezionati().booleanValue() &&
                !getRicercaSospesiRiaccredito().booleanValue())
            throw new it.cnr.jada.bulk.ValidationException("Deve essere selezionato almeno uno stato dei sospesi da ricercare");
    }

    /**
     * Metodo con cui si ottiene il valore della variabile <code>ti_cc_biKeys</code>
     * di tipo <code>Hashtable</code>.
     *
     * @return java.util.Hashtable ti_cc_biKeys
     */
    public java.util.Hashtable getTi_cc_biKeys() {
        Hashtable ti_cc_biKeys = new java.util.Hashtable();
        ti_cc_biKeys.put("C", "C/C");
        ti_cc_biKeys.put("B", "Banca d'Italia");
        return ti_cc_biKeys;
    }
}
