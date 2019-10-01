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

package it.cnr.contab.pdg00.cdip.bulk;

import java.math.BigDecimal;
import java.util.Iterator;

import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.core.bulk.Linea_attivitaBulk;
import it.cnr.jada.bulk.BulkList;

/*
 Oggetto di servizio per il mantenimento della lista di tutte le matricole su cui l'utente
 ha accesso per la gestione dei CDP.
*/

public class Costi_dipendenteVBulk extends it.cnr.jada.bulk.OggettoBulk {
	private BulkList costi_dipendenti;
	private java.math.BigDecimal giorni_lavorativi;
	private int mese;
	private int esercizio;
	private Unita_organizzativaBulk unita_organizzativa_filter; 
	private Unita_organizzativaBulk unita_organizzativa_scarico;
	private WorkpackageBulk linea_attivita_scarico; 
	
	
	public Costi_dipendenteVBulk() {
		super();
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Restituisce il valore della proprietà 'costi_dipendenti'
	 *
	 * @return Il valore della proprietà 'costi_dipendenti'
	 */
	public it.cnr.jada.bulk.BulkList getCosti_dipendenti() {
		return costi_dipendenti;
	}
	public java.math.BigDecimal getGiorni_lavorativi() {
		return giorni_lavorativi;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (19/09/2002 14:52:18)
	 * @return int
	 */
	public int getMese() {
		return mese;
	}
	public boolean isMensile() {
		return mese > 0;
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Imposta il valore della proprietà 'costi_dipendenti'
	 *
	 * @param newCosti_dipendenti	Il valore da assegnare a 'costi_dipendenti'
	 */
	public void setCosti_dipendenti(BulkList newCosti_dipendenti) {
		costi_dipendenti = newCosti_dipendenti;
	}
	/**
	 * Imposta i giorni lavorativi per l'anno i
	 *
	 * @param i	indice dell'anno (1,2,3)
	 * @param giorni_lavorativi	giorni da impostare
	 */
	public void setGiorni_lavorativi(java.math.BigDecimal giorni_lavorativi) {
		this.giorni_lavorativi = giorni_lavorativi;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (19/09/2002 14:52:18)
	 * @param newMese int
	 */
	public void setMese(int newMese) {
		mese = newMese;
	}
	/**
	 * @return
	 */
	public Unita_organizzativaBulk getUnita_organizzativa_filter() {
		return unita_organizzativa_filter;
	}

	/**
	 * @param bulk
	 */
	public void setUnita_organizzativa_filter(Unita_organizzativaBulk bulk) {
		unita_organizzativa_filter = bulk;
	}

	/**
	 * Aggiunge un nuovo dettaglio alla lista
	 * @param cdp V_cdp_matricolaBulk da aggiungere alla lista
	 * @return int
	 */

	public int addToCosti_dipendenti( V_cdp_matricolaBulk cdp ) 
	{
		costi_dipendenti.add(cdp);
		return costi_dipendenti.size()-1;
	}
	/**
	 * Rimuove una dettaglio alla lista
	 * @param index L'indice per scorrere la lista
	 * @return V_cdp_matricolaBulk Il dettaglio da rimuovere dalla lista
	 */
	public V_cdp_matricolaBulk removeFromCosti_dipendenti(int index) 
	{
		return (V_cdp_matricolaBulk)costi_dipendenti.remove(index);
	}
	public int getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(int esercizio) {
		this.esercizio = esercizio;
	}
	public Unita_organizzativaBulk getUnita_organizzativa_scarico() {
		return unita_organizzativa_scarico;
	}
	public void setUnita_organizzativa_scarico(
			Unita_organizzativaBulk unita_organizzativa_scarico) {
		this.unita_organizzativa_scarico = unita_organizzativa_scarico;
	}
	public WorkpackageBulk getLinea_attivita_scarico() {
		return linea_attivita_scarico;
	}
	public void setLinea_attivita_scarico(WorkpackageBulk linea_attivita_scarico) {
		this.linea_attivita_scarico = linea_attivita_scarico;
	}
}
