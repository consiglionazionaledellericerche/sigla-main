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

package it.cnr.contab.pdg00.bp;

import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;

/**
 * Business Process per la gestione dei dettagli di spesa del PDG
 */

public class CRUDSpeDetPdGBP extends it.cnr.jada.util.action.SimpleCRUDBP {

	private it.cnr.contab.config00.sto.bulk.CdrBulk centro_responsabilita;
	private Pdg_variazioneBulk pdg_variazione;

public CRUDSpeDetPdGBP() {
	super();
}

public CRUDSpeDetPdGBP(String function) {
	super(function);
}

/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param function	La funzione con cui è stato creato il BusinessProcess
 * @param cdr	
 */
public CRUDSpeDetPdGBP(String function, it.cnr.contab.config00.sto.bulk.CdrBulk cdr) {
	super(function);
	setCentro_responsabilita(cdr);
}
public CRUDSpeDetPdGBP(String function, it.cnr.contab.config00.sto.bulk.CdrBulk cdr, Pdg_variazioneBulk pdg_variazione) {
	super(function);
	setCentro_responsabilita(cdr);
	setPdg_variazione(pdg_variazione);
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

	protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		super.init(config,context);
		resetTabs();
	}

public boolean isInputReadonly() {
	if (getModel() == null) return super.isInputReadonly();
	
	Boolean fl_sola_lettura = ((it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk)getModel()).getFl_sola_lettura();
	return fl_sola_lettura != null && fl_sola_lettura.booleanValue();
}

	/**
	 * Permette un'assegnazione di default dei tab in uso.
	 * Questa operazione è particolarmente utile in startpu dove viene utilizzata per
	 * permettere la visualizzazione del primo tab senza che l'utente lo selezioni.
	 *
	 * @param context <code>ActionContext</code> in uso.
	 */

	public void resetTabs() {
		setTab("tabPreventivoSpe","tabEsercizio");
		setTab("tabCostiSpese","tabCostiConSpese");
		setTab("tabCostiSpese2","tabCosti2");
		setTab("tabCostiSpese3","tabCosti3");
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
 * @return
 */
public Pdg_variazioneBulk getPdg_variazione() {
	return pdg_variazione;
}

/**
 * @param bulk
 */
public void setPdg_variazione(Pdg_variazioneBulk bulk) {
	pdg_variazione = bulk;
}

}