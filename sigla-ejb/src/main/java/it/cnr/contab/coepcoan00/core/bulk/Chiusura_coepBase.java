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
* Date 17/05/2005
*/
package it.cnr.contab.coepcoan00.core.bulk;
import it.cnr.jada.persistency.Keyed;
public class Chiusura_coepBase extends Chiusura_coepKey implements Keyed {
//    STATO CHAR(1) NOT NULL
	private java.lang.String stato;
 
	public Chiusura_coepBase() {
		super();
	}
	public Chiusura_coepBase(java.lang.String cd_cds, java.lang.Integer esercizio) {
		super(cd_cds, esercizio);
	}
	public java.lang.String getStato () {
		return stato;
	}
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
}