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

package it.cnr.contab.config00.bulk;

import java.util.Date;

import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.action.CRUDBP;

/**
 * Creation date: (09/11/2004)
 * @author Aurelio D'Amico
 * @version 1.0
 */
public class Parametri_cnrBulk extends Parametri_cnrBase {

	private Tipo_rapportoBulk tipo_rapporto = new Tipo_rapportoBulk();
	private Tipo_rapportoBulk tipo_rapporto_prof = new Tipo_rapportoBulk();

	public Parametri_cnrBulk() {
		super();
	}
	public Parametri_cnrBulk(java.lang.Integer esercizio) {
		super(esercizio);
	}
	public OggettoBulk initialize(CRUDBP bp, ActionContext context){
		setEsercizio(CNRUserInfo.getEsercizio(context) );
	  return this;
	}
	
	public void validate() throws ValidationException {

		super.validate();
		if (getLivello_contratt_pdg_spe() != null && getLivello_contratt_pdg_spe().compareTo(getLivello_pdg_decis_spe())>0)
			throw new ValidationException("Il livello di contrattazione Piano di Gestione Spese non può essere\n"+
				"maggiore del livello di classificazione Spesa PDG Decisionale");
	}

	/**
	 * Restituisce il <code>Tipo_rapportoBulk</code> a cui l'oggetto è correlato.
	 *
	 * @return it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk
	 */
	public Tipo_rapportoBulk getTipo_rapporto() {
		return tipo_rapporto;
	}
	/**
	 * Imposta il <code>Tipo_rapportoBulk</code> a cui l'oggetto è correlato.
	 *
	 * @param newTipo_rapporto il tipo di rapporto.
	 */

	public void setTipo_rapporto(Tipo_rapportoBulk newTipo_rapporto) {
		tipo_rapporto = newTipo_rapporto;
	}
	public String getCd_tipo_rapporto() {
		if(getTipo_rapporto() == null)
		  return null;
		return getTipo_rapporto().getCd_tipo_rapporto();
	}

	public void setCd_tipo_rapporto(String string) {
		getTipo_rapporto().setCd_tipo_rapporto(string);
	}	

	public Tipo_rapportoBulk getTipo_rapporto_prof() {
		return tipo_rapporto_prof;
	}
	/**
	 * Imposta il <code>Tipo_rapportoBulk</code> a cui l'oggetto è correlato.
	 *
	 * @param newTipo_rapporto il tipo di rapporto.
	 */

	public void setTipo_rapporto_prof(Tipo_rapportoBulk newTipo_rapporto) {
		tipo_rapporto_prof = newTipo_rapporto;
	}
	public String getCd_tipo_rapporto_prof() {
		if(getTipo_rapporto_prof() == null)
		  return null;
		return getTipo_rapporto_prof().getCd_tipo_rapporto();
	}

	public void setCd_tipo_rapporto_prof(String string) {
		getTipo_rapporto_prof().setCd_tipo_rapporto(string);
	}
	
	public boolean isCofogObbligatorio() {	
		return this.getLivello_pdg_cofog()!=null && this.getLivello_pdg_cofog()!=0;
	}
	
	public Integer getLivelloProgetto() {
		if (this.getFl_nuovo_pdg()) 
			return ProgettoBulk.LIVELLO_PROGETTO_SECONDO;
		return ProgettoBulk.LIVELLO_PROGETTO_TERZO;
	}
	
	public boolean isEnableVoceNext() {
		return this.getData_attivazione_new_voce()!=null && 
				DateUtils.truncate(this.getData_attivazione_new_voce()).compareTo(DateUtils.truncate(new Date()))!=1;
	}
}