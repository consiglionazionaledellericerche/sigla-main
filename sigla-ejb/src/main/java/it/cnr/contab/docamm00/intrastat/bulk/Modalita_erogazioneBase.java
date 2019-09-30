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
 * Date 18/02/2010
 */
package it.cnr.contab.docamm00.intrastat.bulk;

import it.cnr.jada.persistency.Keyed;

public class Modalita_erogazioneBase extends Modalita_erogazioneKey implements Keyed {
//    DS_MODALITA_EROGAZIONE VARCHAR(100) NOT NULL
	private java.lang.String ds_modalita_erogazione;
 
	public Modalita_erogazioneBase() {
		super();
	}
	public Modalita_erogazioneBase(java.lang.Integer esercizio, java.lang.String cd_modalita_erogazione) {
		super(esercizio, cd_modalita_erogazione);
	}
	public java.lang.String getDs_modalita_erogazione() {
		return ds_modalita_erogazione;
	}
	public void setDs_modalita_erogazione(java.lang.String ds_modalita_erogazione)  {
		this.ds_modalita_erogazione=ds_modalita_erogazione;
	}
}