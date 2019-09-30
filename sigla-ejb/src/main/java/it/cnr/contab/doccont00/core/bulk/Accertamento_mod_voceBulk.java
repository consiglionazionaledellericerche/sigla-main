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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 27/09/2006
 */
package it.cnr.contab.doccont00.core.bulk;

import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk;

public class Accertamento_mod_voceBulk extends Accertamento_mod_voceBase {

	private Accertamento_modificaBulk accertamento_modifica = new Accertamento_modificaBulk();
	private IVoceBilancioBulk voce;
	private WorkpackageBulk linea_attivita = new WorkpackageBulk();

	public Accertamento_mod_voceBulk() {
		super();
	}
	public Accertamento_mod_voceBulk(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.Long pg_modifica, java.lang.String ti_appartenenza, java.lang.String ti_gestione, java.lang.String cd_voce, java.lang.String cd_centro_responsabilita, java.lang.String cd_linea_attivita) {
		super(cd_cds, esercizio, pg_modifica, ti_appartenenza, ti_gestione, cd_voce, cd_centro_responsabilita, cd_linea_attivita);
	}

	public Accertamento_modificaBulk getAccertamento_modifica() {
		return accertamento_modifica;
	}
	
	public void setAccertamento_modifica(Accertamento_modificaBulk accertamento_modifica) {
		this.accertamento_modifica = accertamento_modifica;
	}
	
	public IVoceBilancioBulk getVoce() {
		return voce;
	}

	public void setVoce(IVoceBilancioBulk voce) {
		this.voce = voce;
	}

	public WorkpackageBulk getLinea_attivita() {
		return linea_attivita;
	}
	
	public void setLinea_attivita(WorkpackageBulk linea_attivita) {
		this.linea_attivita = linea_attivita;
	}

	public java.lang.String getCd_cds() {
		Accertamento_modificaBulk accertamento_modifica = this.getAccertamento_modifica();
		if (accertamento_modifica == null)
			return null;
		it.cnr.contab.config00.sto.bulk.CdsBulk cds = accertamento_modifica.getCds();
		if (cds == null)
			return null;
		return cds.getCd_unita_organizzativa();
	}

	public void setCd_cds(String cd_cds) {
		getAccertamento_modifica().setCd_cds(cd_cds);
	}

	public java.lang.Integer getEsercizio() {
		Accertamento_modificaBulk accertamento_modifica = this.getAccertamento_modifica();
		if (accertamento_modifica == null)
			return null;
		return accertamento_modifica.getEsercizio();
	}

	public void setEsercizio(Integer esercizio) {
		getAccertamento_modifica().setEsercizio(esercizio);
	}

	public Long getPg_modifica() {
		Accertamento_modificaBulk accertamento_modifica = this.getAccertamento_modifica();
		if (accertamento_modifica == null)
			return null;
		return accertamento_modifica.getPg_modifica();
	}

	public void setPg_modifica(Long pg_modifica) {
		getAccertamento_modifica().setPg_modifica(pg_modifica);
	}

	public String getCd_centro_responsabilita() {
		WorkpackageBulk linea_attivita = this.getLinea_attivita();
		if (linea_attivita == null)
			return null;
		return linea_attivita.getCd_centro_responsabilita();
	}	

	public void setCd_centro_responsabilita(String cd_centro_responsabilita) {
		getLinea_attivita().setCd_centro_responsabilita(cd_centro_responsabilita);
	}	

	public String getCd_linea_attivita() {
		WorkpackageBulk linea_attivita = this.getLinea_attivita();
		if (linea_attivita == null)
			return null;
		return linea_attivita.getCd_linea_attivita();
	}
	
	public void setCd_linea_attivita(String cd_linea_attivita) {
		getLinea_attivita().setCd_linea_attivita(cd_linea_attivita);
	}
}