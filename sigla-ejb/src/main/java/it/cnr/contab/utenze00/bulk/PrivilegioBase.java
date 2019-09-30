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
 * Date 18/03/2008
 */
package it.cnr.contab.utenze00.bulk;
import it.cnr.jada.persistency.Keyed;
public class PrivilegioBase extends PrivilegioKey implements Keyed {
//    DS_PRIVILEGIO VARCHAR(50)
	private java.lang.String ds_privilegio;

//	  TI_ACCESSO CHAR(1)
	private java.lang.String ti_privilegio;

	public PrivilegioBase() {
		super();
	}
	public PrivilegioBase(java.lang.String cd_privilegio) {
		super(cd_privilegio);
	}
	public java.lang.String getDs_privilegio() {
		return ds_privilegio;
	}
	public void setDs_privilegio(java.lang.String ds_privilegio)  {
		this.ds_privilegio=ds_privilegio;
	}
	/* 
	 * Getter dell'attributo ti_privilegio
	 */
	public java.lang.String getTi_privilegio() {
		return ti_privilegio;
	}
	/* 
	 * Setter dell'attributo ti_privilegio
	 */
	public void setTi_privilegio(java.lang.String ti_privilegio) {
		this.ti_privilegio = ti_privilegio;
	}
}