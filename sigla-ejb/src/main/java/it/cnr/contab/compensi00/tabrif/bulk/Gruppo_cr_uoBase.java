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
 * Date 17/07/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import it.cnr.jada.persistency.Keyed;
public class Gruppo_cr_uoBase extends Gruppo_cr_uoKey implements Keyed {
//    FL_ACCENTRATO CHAR(1) NOT NULL
	private java.lang.Boolean fl_accentrato;
 
	public Gruppo_cr_uoBase() {
		super();
	}
	public Gruppo_cr_uoBase(java.lang.Integer esercizio, java.lang.String cd_gruppo_cr, java.lang.String cd_unita_organizzativa) {
		super(esercizio, cd_gruppo_cr, cd_unita_organizzativa);
	}
	public java.lang.Boolean getFl_accentrato() {
		return fl_accentrato;
	}
	public void setFl_accentrato(java.lang.Boolean fl_accentrato)  {
		this.fl_accentrato=fl_accentrato;
	}
}