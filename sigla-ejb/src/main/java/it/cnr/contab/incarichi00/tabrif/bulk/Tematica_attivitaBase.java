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
 * Date 14/09/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;
import it.cnr.jada.persistency.Keyed;
public class Tematica_attivitaBase extends Tematica_attivitaKey implements Keyed {
//    DS_TEMATICA_ATTIVITA VARCHAR(200) NOT NULL
	private java.lang.String ds_tematica_attivita;
 
	public Tematica_attivitaBase() {
		super();
	}
	public Tematica_attivitaBase(java.lang.String cd_tematica_attivita) {
		super(cd_tematica_attivita);
	}
	public java.lang.String getDs_tematica_attivita() {
		return ds_tematica_attivita;
	}
	public void setDs_tematica_attivita(java.lang.String ds_tematica_attivita)  {
		this.ds_tematica_attivita=ds_tematica_attivita;
	}
}