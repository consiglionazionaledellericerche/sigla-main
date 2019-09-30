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
* Creted by Generator 1.0
* Date 18/02/2005
*/
package it.cnr.contab.prevent01.bulk;

import it.cnr.jada.persistency.Keyed;

public class Pdg_missioneBase extends Pdg_missioneKey implements Keyed {
	private static final long serialVersionUID = 1L;

//    DS_MISSIONE VARCHAR(300) NOT NULL
	private java.lang.String ds_missione;
 
	public Pdg_missioneBase() {
		super();
	}
	
	public Pdg_missioneBase(java.lang.String cd_missione) {
		super(cd_missione);
	}
	
	public java.lang.String getDs_missione () {
		return ds_missione;
	}
	
	public void setDs_missione(java.lang.String ds_missione)  {
		this.ds_missione=ds_missione;
	}
}