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
* Date 14/09/2005
*/
package it.cnr.contab.prevent00.bulk;
import java.math.BigDecimal;

import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.HomeCache;
import it.cnr.jada.util.action.CRUDBP;
public class Pdg_piano_ripartoBulk extends Pdg_piano_ripartoBase {
	protected V_classificazione_vociBulk v_classificazione_voci;
	protected CdrBulk centro_responsabilita;

	public Pdg_piano_ripartoBulk() {
		super();
	}
	public Pdg_piano_ripartoBulk(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita, java.lang.Integer id_classificazione) {
		super(esercizio, cd_centro_responsabilita, id_classificazione);
	}

	public CdrBulk getCentro_responsabilita() {
		return centro_responsabilita;
	}

	public V_classificazione_vociBulk getV_classificazione_voci() {
		return v_classificazione_voci;
	}

	public void setCentro_responsabilita(CdrBulk bulk) {
		centro_responsabilita = bulk;
	}

	public void setV_classificazione_voci(V_classificazione_vociBulk bulk) {
		v_classificazione_voci = bulk;
	}

	public Integer getId_classificazione() {
		if (getV_classificazione_voci()==null)
			return null;
		return getV_classificazione_voci().getId_classificazione();
	}
	
	public void setId_classificazione(Integer id_classificazione) {
		getV_classificazione_voci().setId_classificazione(id_classificazione);
	}

	public String getCd_centro_responsabilita() {
		if (getCentro_responsabilita()==null)
			return null;
		return getCentro_responsabilita().getCd_centro_responsabilita();
	}

	public void setCd_centro_responsabilita(String cd_centro_responsabilita) {
		getCentro_responsabilita().setCd_centro_responsabilita(cd_centro_responsabilita);
	}

	public OggettoBulk initialize(CRUDBP crudbp, ActionContext actioncontext) {
		setEsercizio(CNRUserContext.getEsercizio(actioncontext.getUserContext()));
		setStato(Pdg_piano_ripartoHome.STATO_PROVVISORIO);
		return super.initialize(crudbp, actioncontext);
	}

	public boolean isROCentroResponsabilita() {
	   return false;
	}
}