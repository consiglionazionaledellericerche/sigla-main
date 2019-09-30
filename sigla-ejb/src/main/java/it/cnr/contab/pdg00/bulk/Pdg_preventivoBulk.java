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

package it.cnr.contab.pdg00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

/**
 * Gestione dei dati relativi alla tabella Pdg_preventivo
 */

public class Pdg_preventivoBulk extends Pdg_preventivoBase {

	private it.cnr.contab.config00.sto.bulk.CdrBulk centro_responsabilita;

	private java.util.Dictionary statiKeys;
	private java.util.Collection elencoCdR;

	private it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_attivita_eliminazione;

	// COSTANTI

	public final static String ST_A_CREAZIONE = "A";  //Aperto in creazione
	public final static String ST_B_MODIFICA = "B";  //Aperto per modifica
	public final static String ST_C_CHIUSURA_II = "C";  //Chiuso dal CdR 2° Livello
	public final static String ST_C0_MODIFICA_AC = "C0"; //Adeguamenti d'ufficio AC
	public final static String ST_C1_MODIFC_CDRI = "C1"; //Adeguamenti d'ufficio CDR I
	public final static String ST_C2_MODIFIC_RUO = "C2"; //Adeguamenti d'ufficio RUO
	public final static String ST_CX_MODIFICA = "CX"; //Adeguamenti d'ufficio
	public final static String ST_D_CHIUSURA_I = "D";  //Chiuso dal CdR 1° Livello
	public final static String ST_E_CHIUSO = "E";  //Chiuso definitivamente
	public final static String ST_F_CHIUSO_DFNT = "F";  //Chiuso da ribaltare su area
	public final static String ST_G_APERTURA_PER_VARIAZIONI = "G";  // Riapertura per variazioni
	public final static String ST_H_PRECHIUSURA_PER_VARIAZIONI = "H";  // Prechiusura per variazioni
	public final static String ST_M_MODIFICATO_PER_VARIAZIONI = "M";  // Modificato per variazioni

	public final static java.util.Dictionary STATI;

	// Dizionario delle descrizione delle origini di un dettaglio;
	// Va qua per evitare la duplicazione in spe e etr
	final static java.util.Dictionary origineKeys;

	static {
		STATI = new it.cnr.jada.util.OrderedHashtable();
		STATI.put(ST_A_CREAZIONE,"Iniziale (A)");
		STATI.put(ST_B_MODIFICA,"Prechiusura (B)");
		STATI.put(ST_C_CHIUSURA_II,"Chiusura (C)");
		STATI.put(ST_CX_MODIFICA,"Riapertura d'ufficio per adeguamenti (CX)");
		STATI.put(ST_C0_MODIFICA_AC,"Riap. adeg. uff. Amm. Centr. (C0)");
		STATI.put(ST_C1_MODIFC_CDRI,"Riap. adeg. uff. CDRI livello (C1)");
		STATI.put(ST_C2_MODIFIC_RUO,"Riap. adeg. uff. RUO (C2)");
		STATI.put(ST_D_CHIUSURA_I,"Preriapertura (D)");
		STATI.put(ST_E_CHIUSO,"Riapertura per contrattazione (E)");
		STATI.put(ST_F_CHIUSO_DFNT,"Chiusura definitiva (F)");
		STATI.put(ST_G_APERTURA_PER_VARIAZIONI,"Riapertura per variazioni (G)");
		STATI.put(ST_H_PRECHIUSURA_PER_VARIAZIONI,"Prechiusura per variazioni (H)");
		STATI.put(ST_M_MODIFICATO_PER_VARIAZIONI,"Modificato per variazioni (M)");

		origineKeys = new it.cnr.jada.util.OrderedHashtable();
		origineKeys.put(Pdg_preventivo_detBulk.OR_UTENTE,"Definitivo");
		origineKeys.put(Pdg_preventivo_detBulk.OR_STIPENDI,"Stipendi");
		origineKeys.put(Pdg_preventivo_detBulk.OR_PROPOSTA_VARIAZIONE,"Proposta di variazione");
	}
public Pdg_preventivoBulk() {
	super();
}
public Pdg_preventivoBulk(java.lang.Integer esercizio,java.lang.String cd_centro_responsabilita) {
	super();
	setEsercizio(esercizio);
	setCentro_responsabilita(new it.cnr.contab.config00.sto.bulk.CdrBulk( cd_centro_responsabilita ));
}
	/* 
	 * Getter dell'attributo cd_centro_responsabilita
	 */
	public java.lang.String getCd_centro_responsabilita() {
		if(centro_responsabilita == null) return null;
		return centro_responsabilita.getCd_centro_responsabilita();
	}

/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'centro_responsabilita'
 *
 * @return Il valore della proprietà 'centro_responsabilita'
 */
public it.cnr.contab.config00.sto.bulk.CdrBulk getCentro_responsabilita() {
		return centro_responsabilita;
	}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'elencoCdR'
 *
 * @return Il valore della proprietà 'elencoCdR'
 */
public java.util.Collection getElencoCdR() {
		return elencoCdR;
	}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'linea_attivita_eliminazione'
 *
 * @return Il valore della proprietà 'linea_attivita_eliminazione'
 */
public it.cnr.contab.config00.latt.bulk.WorkpackageBulk getLinea_attivita_eliminazione() {
	return linea_attivita_eliminazione;
}
	/**
	 * Restituisce il <code>Dictionary</code> per la gestione dei tipi di stato.
	 *
	 * @return java.util.Dictionary
	 */

	public java.util.Dictionary getStatiKeys() {
		return statiKeys;
	}

public boolean isStatoVariazionePdG() {
	return 
		ST_G_APERTURA_PER_VARIAZIONI.equals(getStato()) ||
		ST_H_PRECHIUSURA_PER_VARIAZIONI.equals(getStato()) ||
		ST_M_MODIFICATO_PER_VARIAZIONI.equals(getStato());
}
	/* 
	 * Setter dell'attributo cd_centro_responsabilita
	 */
	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita) {
		centro_responsabilita.setCd_centro_responsabilita(cd_centro_responsabilita);
	}

/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'centro_responsabilita'
 *
 * @param newCentro_responsabilita	Il valore da assegnare a 'centro_responsabilita'
 */
public void setCentro_responsabilita(it.cnr.contab.config00.sto.bulk.CdrBulk newCentro_responsabilita) {
		centro_responsabilita = newCentro_responsabilita;
	}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'elencoCdR'
 *
 * @param newElencoCdR	Il valore da assegnare a 'elencoCdR'
 */
public void setElencoCdR(java.util.Collection newElencoCdR) {
		elencoCdR = newElencoCdR;
	}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'linea_attivita_eliminazione'
 *
 * @param newLinea_attivita_eliminazione	Il valore da assegnare a 'linea_attivita_eliminazione'
 */
public void setLinea_attivita_eliminazione(it.cnr.contab.config00.latt.bulk.WorkpackageBulk newLinea_attivita_eliminazione) {
	linea_attivita_eliminazione = newLinea_attivita_eliminazione;
}
	/**
	 * Imposta il <code>Dictionary</code> per la gestione dei tipi di stato.
	 *
	 * @param elenco java.util.Dictionary
	 */

	public void setStatiKeys(java.util.Dictionary elenco) {
		statiKeys = elenco;
	}

}
