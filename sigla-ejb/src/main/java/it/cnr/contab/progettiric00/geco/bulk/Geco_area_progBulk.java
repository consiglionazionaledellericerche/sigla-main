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

import java.sql.Timestamp;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk;
import it.cnr.contab.util.Utility;

public class Geco_area_progBulk extends Geco_area_progBase implements Geco_progettoIBulk{
	private static final long serialVersionUID = 1L;

	public Geco_area_progBulk() {
		super();
	}
	public Geco_area_progBulk(java.lang.Long id_area, java.lang.Long esercizio, java.lang.String fase) {
		super(id_area, esercizio, fase);
	}
	public void aggiornaProgettoSIP(Progetto_sipBulk progetto_sip){
		if (!Utility.equalsNull(getCod_prog(), progetto_sip.getCd_progetto())){
			progetto_sip.setCd_progetto(getCod_prog());
			progetto_sip.setToBeUpdated();
		}
		if (!Utility.equalsNull(getDescr_prog(),progetto_sip.getDs_progetto())){
			progetto_sip.setDs_progetto(getDescr_prog());
			progetto_sip.setToBeUpdated();
		}
		if (!Utility.equalsNull(getData_istituzione_prog(), progetto_sip.getDt_inizio())){
			progetto_sip.setDt_inizio(getData_istituzione_prog());
			progetto_sip.setToBeUpdated();
		}
	}
	public Long getId_prog() {
		return super.getId_area();
	}
	public String getCod_prog() {
		return super.getCod_area();
	}
	public String getDescr_prog() {
		return super.getDenom_area();
	}
	public Timestamp getData_istituzione_prog() {
		return super.getData_istituzione_area();
	}
}