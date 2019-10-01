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
package it.cnr.contab.config00.pdcfin.cla.bulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.action.CRUDBP;
public class Parametri_livelliBulk extends Parametri_livelliBase {

	public Parametri_livelliBulk() {
		super();
	}
	public Parametri_livelliBulk(java.lang.Integer esercizio) {
		super(esercizio);
	}
	public String getDs_livello_etr(int livello) {
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
		else  
			return(getDs_livello7e());
	}
	public String getDs_livello_spe(int livello) {
		if (livello==1)  
			return(getDs_livello1s());
		else if (livello==2)  
			return(getDs_livello2s());
		else if (livello==3)  
			return(getDs_livello3s());
		else if (livello==4)  
			return(getDs_livello4s());
		else if (livello==5)  
			return(getDs_livello5s());
		else if (livello==6)  
			return(getDs_livello6s());
		else  
			return(getDs_livello7s());
	}
	public Integer getLung_livello_etr(int livello) {
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
		else  
			return(getLung_livello7e());
	}
	public Integer getLung_livello_spe(int livello) {
		if (livello==1)  
			return(getLung_livello1s());
		else if (livello==2)  
			return(getLung_livello2s());
		else if (livello==3)  
			return(getLung_livello3s());
		else if (livello==4)  
			return(getLung_livello4s());
		else if (livello==5)  
			return(getLung_livello5s());
		else if (livello==6)  
			return(getLung_livello6s());
		else  
			return(getLung_livello7s());
	}

	/*
	 * Verifica che i campi dei livelli da non gestire non sia siano valorizzati
	 */
	public void validaLivelliValorizzati() throws ValidationException {
		for (int i=1;i<=7;i++) {
			/*Controllo Livelli SPESA valorizzati*/
			if ((this.getDs_livello_spe(i)!=null || this.getLung_livello_spe(i)!=null) &&
				(this.getLivelli_spesa() == null || this.getLivelli_spesa().intValue()<i))
				throw new ValidationException("Attenzione! I campi relativi al livello " + i + " di spesa non devono essere valorizzati.");
	
			/*Controllo Livelli ENTRATA valorizzati*/
			if ((this.getDs_livello_etr(i)!=null || this.getLung_livello_etr(i)!=null) &&
				(this.getLivelli_entrata() == null || this.getLivelli_entrata().intValue()<i))
				throw new ValidationException("Attenzione! I campi relativi al livello " + i + " di entrata non devono essere valorizzati.");
		}
	}

	/*
	 * Verifica che tutti i campi dei livelli da gestire per costruire la voce sia siano valorizzati
	 */
	public void validaLivelliNonValorizzati() throws ValidationException {
		for (int i=1;i<=7;i++) {
			/*Controllo Livelli SPESA non valorizzati*/
			if ((this.getDs_livello_spe(i)==null || this.getLung_livello_spe(i)==null) &&
				(this.getLivelli_spesa() != null && this.getLivelli_spesa().intValue()>=i))
				throw new ValidationException("Attenzione! I campi relativi al livello " + i + " di spesa devono essere valorizzati.");
	
			/*Controllo Livelli ENTRATA non valorizzati*/
			if ((this.getDs_livello_etr(i)==null || this.getLung_livello_etr(i)==null) &&
				(this.getLivelli_entrata() != null && this.getLivelli_entrata().intValue()>=i))
				throw new ValidationException("Attenzione! I campi relativi al livello " + i + " di entrata devono essere valorizzati.");
		}
	}

	/*
	 * Ritorna ValidationException se:
	 * - Il numero dei livelli ENTRATA o SPESA inseriti è maggiore di 7
	 * - Non risultano valorizzati campi di un livello da gestire
	 * - Risultano valorizzati campi di un livello da non gestire
	 */
	public void validate() throws ValidationException {
		super.validate();

		/*Controllo Livelli SPESA*/
		if (this.getLivelli_spesa() != null && this.getLivelli_spesa().intValue()>7)
			throw new ValidationException("Attenzione! Il livello massimo consentito è 7.");

		if (this.getLivelli_entrata() != null && this.getLivelli_entrata().intValue()>7)
			throw new ValidationException("Attenzione! Il livello massimo consentito è 7.");

		validaLivelliValorizzati();
		validaLivelliNonValorizzati();
	}
}