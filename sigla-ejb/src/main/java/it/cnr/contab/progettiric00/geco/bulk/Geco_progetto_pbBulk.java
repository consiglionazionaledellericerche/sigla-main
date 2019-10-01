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
 * Date 27/11/2006
 */
package it.cnr.contab.progettiric00.geco.bulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Geco_progetto_pbBulk extends Geco_progetto_pbBase implements Geco_progettoIBulk{
	public Geco_progetto_pbBulk() {
		super();
	}
	public Geco_progetto_pbBulk(java.lang.Long id_prog, java.lang.Long esercizio, java.lang.String fase) {
		super(id_prog, esercizio, fase);
	}
	public void aggiornaProgettoSIP(Progetto_sipBulk progetto_sip){
		if (!getCod_prog().equals(progetto_sip.getCd_progetto())){
			progetto_sip.setCd_progetto(getCod_prog());
			progetto_sip.setToBeUpdated();
		}
		if (!getDescr_prog().equals(progetto_sip.getDs_progetto())){
			progetto_sip.setDs_progetto(getDescr_prog());
			progetto_sip.setToBeUpdated();
		}
		if (!getData_istituzione_prog().equals(progetto_sip.getDt_inizio())){
			progetto_sip.setDt_inizio(getData_istituzione_prog());
			progetto_sip.setToBeUpdated();
		}
	}
}