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
 * Date 23/04/2007
 */
package it.cnr.contab.anagraf00.tabrif.bulk ;
import it.cnr.jada.persistency.Keyed;
public class Tipologie_istatBase extends Tipologie_istatKey implements Keyed {
//    DS_TIPOLOGIA VARCHAR(200) NOT NULL
	private java.lang.String ds_tipologia;
 

	public Tipologie_istatBase() {
		super();
	}
	public Tipologie_istatBase(java.lang.Integer pg_tipologia) {
		super(pg_tipologia);
	}
	public java.lang.String getDs_tipologia() {
		return ds_tipologia;
	}
	public void setDs_tipologia(java.lang.String ds_tipologia)  {
		this.ds_tipologia=ds_tipologia;
	}

}