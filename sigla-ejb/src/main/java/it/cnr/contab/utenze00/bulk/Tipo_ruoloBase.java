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
* Date 09/01/2006
*/
package it.cnr.contab.utenze00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Tipo_ruoloBase extends Tipo_ruoloKey implements Keyed {
//    DS_TIPO VARCHAR(50)
	private java.lang.String ds_tipo;

	public Tipo_ruoloBase() {
		super();
	}
	public Tipo_ruoloBase(java.lang.String tipo) {
		super(tipo);
	}
	public java.lang.String getDs_tipo () {
		return ds_tipo;
	}
	public void setDs_tipo(java.lang.String ds_tipo)  {
		this.ds_tipo=ds_tipo;
	}
}
