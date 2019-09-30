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
* Date 19/10/2005
*/
package it.cnr.contab.prevent01.bulk;
import it.cnr.jada.persistency.Keyed;
public class Pdg_esercizioBase extends Pdg_esercizioKey implements Keyed {
//    STATO CHAR(2)
	private java.lang.String stato;
 
	public Pdg_esercizioBase() {
		super();
	}
	public Pdg_esercizioBase(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita) {
		super(esercizio, cd_centro_responsabilita);
	}
	public java.lang.String getStato () {
		return stato;
	}
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
}