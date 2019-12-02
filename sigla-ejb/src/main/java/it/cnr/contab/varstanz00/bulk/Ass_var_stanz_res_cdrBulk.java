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
* Date 15/02/2006
*/
package it.cnr.contab.varstanz00.bulk;
import java.math.BigDecimal;

import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Ass_var_stanz_res_cdrBulk extends Ass_var_stanz_res_cdrBase {
	private Var_stanz_resBulk var_stanz_res;
	private CdrBulk centro_di_responsabilita;
	private BigDecimal spesa_ripartita;
	private BigDecimal spesa_diff;
	
	public Ass_var_stanz_res_cdrBulk() {
		super();
	}
	public Ass_var_stanz_res_cdrBulk(java.lang.Integer esercizio, java.lang.Long pg_variazione, java.lang.String cd_centro_responsabilita) {
		super(esercizio, pg_variazione, cd_centro_responsabilita);
		setVar_stanz_res(new Var_stanz_resBulk(esercizio, pg_variazione));
		setCentro_di_responsabilita(new CdrBulk(cd_centro_responsabilita));
	}
	/**
	 * @return
	 */
	public Var_stanz_resBulk getVar_stanz_res() {
		return var_stanz_res;
	}

	/**
	 * @param bulk
	 */
	public void setVar_stanz_res(Var_stanz_resBulk bulk) {
		var_stanz_res = bulk;
	}
    /* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Ass_var_stanz_res_cdrKey#setEsercizio(java.lang.Integer)
	 */
	public void setEsercizio(Integer esercizio) {
		getVar_stanz_res().setEsercizio(esercizio);
	}
    /* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Ass_var_stanz_res_cdrKey#getEsercizio()
	 */
	public Integer getEsercizio() {
		return getVar_stanz_res().getEsercizio();
	}
    /* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Ass_var_stanz_res_cdrKey#setPg_variazione(java.lang.Long)
	 */
	public void setPg_variazione(Long pg_variazione) {
		getVar_stanz_res().setPg_variazione(pg_variazione);
	}
    /* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Ass_var_stanz_res_cdrKey#getPg_variazione()
	 */
	public Long getPg_variazione() {
		return getVar_stanz_res().getPg_variazione();
	}
	/**
	 * @return
	 */
	public CdrBulk getCentro_di_responsabilita() {
		return centro_di_responsabilita;
	}

	/**
	 * @param bulk
	 */
	public void setCentro_di_responsabilita(CdrBulk bulk) {
		centro_di_responsabilita = bulk;
	}
    /* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Ass_var_stanz_res_cdrKey#setCd_centro_responsabilita(java.lang.String)
	 */
	public void setCd_centro_responsabilita(String cd_centro_responsabilita) {
		getCentro_di_responsabilita().setCd_centro_responsabilita(cd_centro_responsabilita);
	}
    /* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Ass_var_stanz_res_cdrKey#getCd_centro_responsabilita()
	 */
	public String getCd_centro_responsabilita() {
		return getCentro_di_responsabilita().getCd_centro_responsabilita();
	}
	/**
	 * @return
	 */
	public BigDecimal getSpesa_diff() {
		return Utility.nvl(getIm_spesa()).subtract(Utility.nvl(getSpesa_ripartita()));
	}

	/**
	 * @return
	 */
	public BigDecimal getSpesa_ripartita() {
		return spesa_ripartita;
	}
	/**
	 * @param decimal
	 */
	public void setSpesa_ripartita(BigDecimal decimal) {
		spesa_ripartita = decimal;
	}

}