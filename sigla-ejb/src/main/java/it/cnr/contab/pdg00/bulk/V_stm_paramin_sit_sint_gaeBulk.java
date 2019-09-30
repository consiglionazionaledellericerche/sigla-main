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
 * Date 25/03/2008
 */
package it.cnr.contab.pdg00.bulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class V_stm_paramin_sit_sint_gaeBulk extends V_stm_paramin_sit_sint_gaeBase {
	
	public V_stm_paramin_sit_sint_gaeBulk() {
		super();
	}
	public V_stm_paramin_sit_sint_gaeBulk(WorkpackageBulk gae) {
		
		this();
		completeFrom(gae);
	}
	
	private void completeFrom(WorkpackageBulk gae) {

		if (gae == null) return;

		setCdr(gae.getCd_centro_responsabilita());
		setGae(gae.getCd_linea_attivita());
	}
}