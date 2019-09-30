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

/*
 * Created on Apr 26, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg00.bulk;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Stampa_pdg_etr_spe_istVBulk extends Stampa_pdg_etr_speVBulk {

	/**
	 * 
	 */
	public Stampa_pdg_etr_spe_istVBulk() {
		super();
	}
	/**
	 * Inizializza gli attributi di ragruppamento
	 * @param bp business process corrente
	 * @param context contesto dell'Action che e' stata generata
	 * @return OggettoBulk Stampa_pdg_etr_speVBulk con i falg inizializzati
	 */
	public void inizializzaRagruppamenti() {
		super.inizializzaRagruppamenti();
		setRagrr_istituto(new Boolean(true));
		setRagrr_dipartimento(new Boolean(false));	
	}	
	public void selezionaRagruppamenti(){
		super.selezionaRagruppamenti();		
		setRagrr_istituto(new Boolean(true));
		setRagrr_dipartimento(new Boolean(!getRagrr_dipartimento().booleanValue()));
	}
}
