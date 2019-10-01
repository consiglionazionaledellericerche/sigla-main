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
* Date 12/09/2005
*/
package it.cnr.contab.config00.sto.bulk;

import it.cnr.jada.persistency.Keyed;
public class Ass_uo_areaBase extends Ass_uo_areaKey implements Keyed {
//    FL_PRESIDENTE_AREA CHAR(1) NOT NULL
	private java.lang.Boolean fl_presidente_area;
 
	public Ass_uo_areaBase() {
		super();
	}
	public Ass_uo_areaBase(java.lang.Integer esercizio, java.lang.String cd_unita_area, java.lang.String cd_unita_organizzativa) {
		super(esercizio, cd_unita_area, cd_unita_organizzativa);
	}
	public java.lang.Boolean getFl_presidente_area () {
		return fl_presidente_area;
	}
	public void setFl_presidente_area(java.lang.Boolean fl_presidente_area)  {
		this.fl_presidente_area=fl_presidente_area;
	}
}