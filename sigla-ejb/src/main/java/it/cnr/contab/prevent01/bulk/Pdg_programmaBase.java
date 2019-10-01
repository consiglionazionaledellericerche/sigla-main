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

public class Pdg_programmaBase extends Pdg_programmaKey implements Keyed {
	private static final long serialVersionUID = 1L;

//    DS_PROGRAMMA VARCHAR(300) NOT NULL
	private java.lang.String ds_programma;
 
	public Pdg_programmaBase() {
		super();
	}
	
	public Pdg_programmaBase(java.lang.String cd_programma) {
		super(cd_programma);
	}
	
	public java.lang.String getDs_programma () {
		return ds_programma;
	}
	
	public void setDs_programma(java.lang.String ds_programma)  {
		this.ds_programma=ds_programma;
	}
}