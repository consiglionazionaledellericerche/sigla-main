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
* Date 19/09/2005
*/
package it.cnr.contab.prevent01.bulk;
import it.cnr.jada.persistency.Keyed;
public class Pdg_moduloBase extends Pdg_moduloKey implements Keyed {
//    STATO CHAR(1)
	private java.lang.String stato;
 
	public Pdg_moduloBase() {
		super();
	}
	public Pdg_moduloBase(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita, java.lang.Integer pg_progetto) {
		super(esercizio, cd_centro_responsabilita, pg_progetto);
	}
	public java.lang.String getStato () {
		return stato;
	}
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
}