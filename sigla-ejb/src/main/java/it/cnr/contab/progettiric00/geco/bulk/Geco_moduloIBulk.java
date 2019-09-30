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

package it.cnr.contab.progettiric00.geco.bulk;

import it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk;

public interface Geco_moduloIBulk {
	public abstract java.lang.Long getId_mod();
	public abstract java.lang.Long getEsercizio();
	public abstract java.lang.String getFase();
	public abstract java.lang.String getCod_mod();
	public abstract java.lang.String getDescr_mod();
	public abstract java.lang.Long getId_comm();
	//public abstract java.lang.Long getCod_tip();
	public abstract java.lang.String getSede_princ_cdsuo();
	public abstract java.lang.String getCod_3rzo_gest();
	public abstract java.sql.Timestamp getData_inizio_attivita();
	public abstract java.lang.Integer getEsito_negoz();
	public abstract java.lang.Long getStato_att_scie();
	public abstract java.lang.Long getStato_att_contab();
	public abstract java.lang.Long getStato();
	public abstract void aggiornaProgettoSIP(Progetto_sipBulk progetto_sip);

}
