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
* Date 12/05/2005
*/
package it.cnr.contab.anagraf00.tabrif.bulk;
import it.cnr.jada.persistency.Keyed;
public class Codici_rapporti_inpsBase extends Codici_rapporti_inpsKey implements Keyed {
//    DS_RAPPORTO_INPS VARCHAR(200) NOT NULL
	private java.lang.String ds_rapporto_inps;
 
//    FL_ATTIVITA_OBBL CHAR(1) NOT NULL
	private java.lang.Boolean fl_attivita_obbl;
 
//    FL_CANCELLATO CHAR(1) NOT NULL
	private java.lang.Boolean fl_cancellato;
 
	public Codici_rapporti_inpsBase() {
		super();
	}
	public Codici_rapporti_inpsBase(java.lang.String cd_rapporto_inps) {
		super(cd_rapporto_inps);
	}
	public java.lang.String getDs_rapporto_inps () {
		return ds_rapporto_inps;
	}
	public void setDs_rapporto_inps(java.lang.String ds_rapporto_inps)  {
		this.ds_rapporto_inps=ds_rapporto_inps;
	}
	public java.lang.Boolean getFl_attivita_obbl () {
		return fl_attivita_obbl;
	}
	public void setFl_attivita_obbl(java.lang.Boolean fl_attivita_obbl)  {
		this.fl_attivita_obbl=fl_attivita_obbl;
	}
	public java.lang.Boolean getFl_cancellato () {
		return fl_cancellato;
	}
	public void setFl_cancellato(java.lang.Boolean fl_cancellato)  {
		this.fl_cancellato=fl_cancellato;
	}
}