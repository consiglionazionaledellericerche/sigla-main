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

package it.cnr.contab.config00.pdcep.bulk;

import java.util.*;

import it.cnr.contab.coepcoan00.core.bulk.Movimento_cogeBulk;
import it.cnr.contab.config00.pdcep.cla.bulk.V_classificazione_voci_epBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Tipo_documento_genericoBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.*;

/**
 * Classe che eredita le caratteristiche della superclasse <code>OggettoBulk<code>.
 * Contiene tutte le variabili e i metodi che sono comuni alle sue sottoclassi.
 * Gestisce i dati relativi alla tabella Voce_ep.
 */
public class Voce_epBulk extends Voce_epBase {
	public final static String CONTO_ECONOMICO_PLURIENNALE_COSTO = "EPC";
	public final static String CONTO_ECONOMICO_PLURIENNALE_RICAVO = "EPR";
	public final static String CONTO_ECONOMICO_ESERCIZIO_COSTO = "EEC";
	public final static String CONTO_ECONOMICO_ESERCIZIO_RICAVO = "EER";
	public final static String CONTO_NUMERARIO_ATTIVITA = "NUA";
	public final static String CONTO_NUMERARIO_PASSIVITA = "NUP";
	public final static String CONTO_ORDINE = "CDO";
	public final static String CONTO_CAPITALE = "CDC";

	public final static String SEZIONE_BIFASE = "B";

	static private java.util.Hashtable riepiloga_a_Keys;
	final static public java.util.Hashtable natura_voce_Keys;
	static private java.util.Hashtable ti_sezione_Keys;
	static private java.util.Hashtable conto_speciale_Keys;
	private java.util.Hashtable ti_voce_ep_Keys;
	private it.cnr.jada.util.OrderedHashtable gruppiKeys;
	static private java.util.Hashtable fl_a_pareggio_Keys;
	static
	{
		natura_voce_Keys = new Hashtable();
		natura_voce_Keys.put(CONTO_ECONOMICO_PLURIENNALE_COSTO, "EPC - Economico Pluriennale Costo");
		natura_voce_Keys.put(CONTO_ECONOMICO_PLURIENNALE_RICAVO, "EPR - Economico Pluriennale Ricavo");
		natura_voce_Keys.put(CONTO_ECONOMICO_ESERCIZIO_COSTO, "EEC - Economico d'Esercizio Costo");
		natura_voce_Keys.put(CONTO_ECONOMICO_ESERCIZIO_RICAVO, "EER - Economico d'Esercizio Ricavo");
		natura_voce_Keys.put(CONTO_NUMERARIO_ATTIVITA, "NUA - Numerario Attività");
		natura_voce_Keys.put(CONTO_NUMERARIO_PASSIVITA, "NUP - Numerario Passività");
		natura_voce_Keys.put(CONTO_ORDINE, "CDO - Conto d'Ordine");
		natura_voce_Keys.put(CONTO_CAPITALE, "CDC - Conto di Capitale");
	}	
	private V_classificazione_voci_epBulk v_classificazione_voci_ep;

	public Voce_epBulk() {
		super();
	}
	public Voce_epBulk(java.lang.String cd_voce_ep,java.lang.Integer esercizio) {
		super(cd_voce_ep,esercizio);
	}
	/**
	 * Metodo con cui si ottiene il valore della variabile <code>conto_speciale_Keys</code>
	 * di tipo <code>Hashtable</code>.
	 * In particolare, questo metodo carica in una Hashtable l'elenco dei possibili valori
	 * che può assumere un conto speciale.
	 * @return java.util.Hashtable conto_speciale_Keys I valori del conto speciale.
	 */
	public java.util.Hashtable getConto_speciale_Keys() {
			Hashtable conto_speciale_Keys = new java.util.Hashtable();

			conto_speciale_Keys.put("RDE", "RDE - Risultato di Esercizio");
			conto_speciale_Keys.put("CSE", "CSE - Conto Sintetico Economico");
			conto_speciale_Keys.put("SPF", "SPF - Stato Patrimoniale Finale");
			conto_speciale_Keys.put("SPI", "SPI - Stato Patrimoniale Iniziale");
			conto_speciale_Keys.put("CAS", "CAS - Cassa");
			conto_speciale_Keys.put("BAN", "BAN - Banca");
			conto_speciale_Keys.put("ARR", "ARR - Arrotondamento");
			conto_speciale_Keys.put("PLU", "PLU - Plusvalenza");
			conto_speciale_Keys.put("MIN", "MIN - Minusvalenza");
			conto_speciale_Keys.put("IVA", "IVA - Per conti IVA a Credito o a Debito");
		return conto_speciale_Keys;
	}
	/**
	 * Metodo con cui si ottiene il valore della variabile <code>fl_a_pareggio_Keys</code>
	 * di tipo <code>Hashtable</code>.
	 * In particolare, questo metodo carica in una Hashtable l'elenco dei possibili valori
	 * che può assumere il flag <code>fl_a_pareggio</code>.
	 * @return java.util.Hashtable fl_a_pareggio_Keys I valori del flag <code>fl_a_pareggio</code>.
	 */
	public java.util.Hashtable getFl_a_pareggio_Keys() {
			Hashtable fl_a_pareggio_Keys = new Hashtable();

			fl_a_pareggio_Keys.put(new Boolean(true), "Y");
			fl_a_pareggio_Keys.put(new Boolean(false), "N");
		return fl_a_pareggio_Keys;
	}
	/**
	 * Metodo con cui si ottiene il valore della variabile <code>gruppiKeys</code>
	 * di tipo <code>Hashtable</code>.
	 * @return java.util.Hashtable gruppiKeys
	 */
	public OrderedHashtable getGruppiKeys() {
		return gruppiKeys;
	}
	/**
	 * Metodo con cui si ottiene il valore della variabile <code>natura_voce_Keys</code>
	 * di tipo <code>Hashtable</code>.
	 * In particolare, questo metodo carica in una Hashtable l'elenco dei possibili valori
	 * che può assumere la natura.
	 * @return java.util.Hashtable natura_voce_Keys I valori della natura.
	 */
	public java.util.Hashtable getNatura_voce_Keys()
	{
		return natura_voce_Keys;
	}
	/**
	 * Metodo con cui si ottiene il valore della variabile <code>riepiloga_a_Keys</code>
	 * di tipo <code>Hashtable</code>.
	 * In particolare, questo metodo carica in una Hashtable l'elenco dei possibili valori
	 * che può assumere il campo <code>riepiloga_a</code>.
	 * @return java.util.Hashtable riepiloga_a_Keys I valori del campo <code>riepiloga_a</code>.
	 */
	public java.util.Hashtable getRiepiloga_a_Keys() {
			Hashtable riepiloga_a_Keys = new java.util.Hashtable();

			riepiloga_a_Keys.put("CEC", "CEC - Conto Economico");
			riepiloga_a_Keys.put("SPA", "SPA - Stato Patrimoniale");
		return riepiloga_a_Keys;
	}
	/**
	 * Metodo con cui si ottiene il valore della variabile <code>ti_sezione_Keys</code>
	 * di tipo <code>Hashtable</code>.
	 * In particolare, questo metodo  carica in una Hashtable l'elenco delle tipologie
	 * di Sezione.
	 * @return java.util.Hashtable ti_sezione_Keys I valori della tipologia di Sezione.
	 */
	public java.util.Hashtable getTi_sezione_Keys() {
			Hashtable ti_sezione_Keys = new java.util.Hashtable();

			ti_sezione_Keys.put(Movimento_cogeBulk.SEZIONE_DARE, "Dare");
			ti_sezione_Keys.put(Movimento_cogeBulk.SEZIONE_AVERE, "Avere");
			ti_sezione_Keys.put(SEZIONE_BIFASE, "Bifase");
		return ti_sezione_Keys;
	}
	/**
	 * Metodo con cui si ottiene il valore della variabile <code>ti_voce_ep_Keys</code>
	 * di tipo <code>Hashtable</code>.
	 * @return java.util.Hashtable ti_voce_ep_Keys
	 */
	public java.util.Hashtable getTi_voce_ep_Keys() {
		return ti_voce_ep_Keys;
	}
	/**
	 * Metodo con cui si definisce il valore della variabile <code>gruppiKeys</code>
	 * di tipo <code>OrderedHashtable</code>.
	 * @param newGruppiKeys OrderedHashtable
	 */
	public void setGruppiKeys(OrderedHashtable newGruppiKeys) {
		gruppiKeys = newGruppiKeys;
	}
	/**
	 * Metodo con cui si definisce il valore della variabile <code>ti_voce_ep_Keys</code>
	 * di tipo <code>Hashtable</code>.
	 * @param newTi_voce_ep_Keys java.util.Hashtable
	 */
	public void setTi_voce_ep_Keys(java.util.Hashtable newTi_voce_ep_Keys) {
		ti_voce_ep_Keys = newTi_voce_ep_Keys;
	}
	/**
	 * Metodo con cui si verifica la validità di alcuni campi, mediante un
	 * controllo sintattico o contestuale.
	 */
	public void validate() throws ValidationException {
		super.validate();

		// controllo su campo ESERCIZIO vale per entrambi i tipi (Capoconto,Conto)
				if ( getEsercizio() == null )
					throw new ValidationException( "Il campo ESERCIZIO è obbligatorio." );
				if ( getEsercizio().toString().length() != 4 )
					throw new ValidationException( "Il campo ESERCIZIO deve essere di quattro cifre." );
	}
	public V_classificazione_voci_epBulk getV_classificazione_voci_ep() {
		return v_classificazione_voci_ep;
	}
	public void setV_classificazione_voci_ep(
			V_classificazione_voci_epBulk v_classificazione_voci_ep) {
		this.v_classificazione_voci_ep = v_classificazione_voci_ep;
	}
	public java.lang.Integer getId_classificazione() {
		if (getV_classificazione_voci_ep() == null)
			return null;
		return getV_classificazione_voci_ep().getId_classificazione();
	}

	public void setId_classificazione(java.lang.Integer v_id_classificazione) {
		getV_classificazione_voci_ep().setId_classificazione(v_id_classificazione);
	}

	public boolean isContoCostoEconomicoEsercizio() {
		return CONTO_ECONOMICO_ESERCIZIO_COSTO.equals(this.getNatura_voce());
	}

	public boolean isContoRicavoEconomicoEsercizio() {
		return CONTO_ECONOMICO_ESERCIZIO_RICAVO.equals(this.getNatura_voce());
	}

	public boolean isContoNumerarioAttivita() {
		return CONTO_NUMERARIO_ATTIVITA.equals(this.getNatura_voce());
	}

	public boolean isContoNumerarioPassivita() {
		return CONTO_NUMERARIO_PASSIVITA.equals(this.getNatura_voce());
	}

	public boolean isContoSezioneDare() {
		return Movimento_cogeBulk.SEZIONE_DARE.equals(this.getTi_sezione());
	}

	public boolean isContoSezioneAvere() {
		return Movimento_cogeBulk.SEZIONE_AVERE.equals(this.getTi_sezione());
	}

	public boolean isContoSezioneBifase() {
		return SEZIONE_BIFASE.equals(this.getTi_sezione());
	}
}