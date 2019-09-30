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
public class Tipo_cr_baseBase extends Tipo_cr_baseKey implements Keyed {
//    CD_GRUPPO_CR VARCHAR(10)
	private java.lang.String cd_gruppo_cr;
 
	public Tipo_cr_baseBase() {
		super();
	}
	public Tipo_cr_baseBase(java.lang.Integer esercizio, java.lang.String cd_contributo_ritenuta) {
		super(esercizio, cd_contributo_ritenuta);
	}
	public java.lang.String getCd_gruppo_cr() {
		return cd_gruppo_cr;
	}
	public void setCd_gruppo_cr(java.lang.String cd_gruppo_cr)  {
		this.cd_gruppo_cr=cd_gruppo_cr;
	}
}