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
* Created by Generator 1.0
* Date 30/08/2005
*/
package it.cnr.contab.config00.pdcep.cla.bulk;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.action.CRUDBP;
public class Parametri_livelli_epBulk extends Parametri_livelli_epBase {

	public Parametri_livelli_epBulk() {
		super();
	}
	public Parametri_livelli_epBulk(java.lang.Integer esercizio) {
		super(esercizio);
	}
	public String getDs_livello_eco(int livello) {
		if (livello==1)  
			return(getDs_livello1e());
		else if (livello==2)  
			return(getDs_livello2e());
		else if (livello==3)  
			return(getDs_livello3e());
		else if (livello==4)  
			return(getDs_livello4e());
		else if (livello==5)  
			return(getDs_livello5e());
		else if (livello==6)  
			return(getDs_livello6e());
		else if (livello==7)  
			return(getDs_livello7e());
		else  
			return(getDs_livello8e());
	}
	public String getDs_livello_pat(int livello) {
		if (livello==1)  
			return(getDs_livello1p());
		else if (livello==2)  
			return(getDs_livello2p());
		else if (livello==3)  
			return(getDs_livello3p());
		else if (livello==4)  
			return(getDs_livello4p());
		else if (livello==5)  
			return(getDs_livello5p());
		else if (livello==6)  
			return(getDs_livello6p());
		else if (livello==7)  
			return(getDs_livello7p());
		else  
			return(getDs_livello8p());
	}
	public Integer getLung_livello_eco(int livello) {
		if (livello==1)  
			return(getLung_livello1e());
		else if (livello==2)  
			return(getLung_livello2e());
		else if (livello==3)  
			return(getLung_livello3e());
		else if (livello==4)  
			return(getLung_livello4e());
		else if (livello==5)  
			return(getLung_livello5e());
		else if (livello==6)  
			return(getLung_livello6e());
		else if (livello==7)  
			return(getLung_livello7e());
		else  
			return(getLung_livello8e());
	}
	public Integer getLung_livello_pat(int livello) {
		if (livello==1)  
			return(getLung_livello1p());
		else if (livello==2)  
			return(getLung_livello2p());
		else if (livello==3)  
			return(getLung_livello3p());
		else if (livello==4)  
			return(getLung_livello4p());
		else if (livello==5)  
			return(getLung_livello5p());
		else if (livello==6)  
			return(getLung_livello6p());
		else if (livello==7)  
			return(getLung_livello7p());
		else  
			return(getLung_livello8p());
	}

	/*
	 * Verifica che i campi dei livelli da non gestire non sia siano valorizzati
	 */
	public void validaLivelliValorizzati() throws ValidationException {
		for (int i=1;i<=8;i++) {
			/*Controllo Livelli patrimoniale valorizzati*/
			if ((this.getDs_livello_pat(i)!=null || this.getLung_livello_pat(i)!=null) &&
				(this.getLivelli_pat() == null || this.getLivelli_pat().intValue()<i))
				throw new ValidationException("Attenzione! I campi relativi al livello " + i + " di patrimoniale non devono essere valorizzati.");
	
			/*Controllo Livelli economica valorizzati*/
			if ((this.getDs_livello_eco(i)!=null || this.getLung_livello_eco(i)!=null) &&
				(this.getLivelli_eco() == null || this.getLivelli_eco().intValue()<i))
				throw new ValidationException("Attenzione! I campi relativi al livello " + i + " di economica non devono essere valorizzati.");
		}
	}

	/*
	 * Verifica che tutti i campi dei livelli da gestire per costruire la voce sia siano valorizzati
	 */
	public void validaLivelliNonValorizzati() throws ValidationException {
		for (int i=1;i<=8;i++) {
			/*Controllo Livelli patrimoniale non valorizzati*/
			if ((this.getDs_livello_pat(i)==null || this.getLung_livello_pat(i)==null) &&
				(this.getLivelli_pat() != null && this.getLivelli_pat().intValue()>=i))
				throw new ValidationException("Attenzione! I campi relativi al livello " + i + " di patrimoniale devono essere valorizzati.");
	
			/*Controllo Livelli economica non valorizzati*/
			if ((this.getDs_livello_eco(i)==null || this.getLung_livello_eco(i)==null) &&
				(this.getLivelli_eco() != null && this.getLivelli_eco().intValue()>=i))
				throw new ValidationException("Attenzione! I campi relativi al livello " + i + " di economica devono essere valorizzati.");
		}
	}

	/*
	 * Ritorna ValidationException se:
	 * - Il numero dei livelli economica/patrimoniale inseriti è maggiore di 8
	 * - Non risultano valorizzati campi di un livello da gestire
	 * - Risultano valorizzati campi di un livello da non gestire
	 */
	public void validate() throws ValidationException {
		super.validate();

		/*Controllo Livelli Patrimoniale*/
		if (this.getLivelli_pat() != null && this.getLivelli_pat().intValue()>8)
			throw new ValidationException("Attenzione! Il livello massimo consentito è 8.");

		if (this.getLivelli_eco() != null && this.getLivelli_eco().intValue()>8)
			throw new ValidationException("Attenzione! Il livello massimo consentito è 8.");

		validaLivelliValorizzati();
		validaLivelliNonValorizzati();
	}
}