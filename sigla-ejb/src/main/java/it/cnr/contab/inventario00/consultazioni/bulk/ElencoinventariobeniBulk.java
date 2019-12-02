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
* Creted by Generator 1.0
* Date 03/03/2005
*/
package it.cnr.contab.inventario00.consultazioni.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class ElencoinventariobeniBulk extends ElencoinventariobeniBase {
	public ElencoinventariobeniBulk() {
		super();
	}
	public ElencoinventariobeniBulk(java.lang.String cd_unita_organizzativa, java.lang.String cd_categoria_gruppo, java.lang.Long nr_inventario, java.lang.Long progressivo, java.lang.String ds_bene, java.sql.Timestamp data_registrazione) {
		super(cd_unita_organizzativa, cd_categoria_gruppo, nr_inventario, progressivo, ds_bene, data_registrazione);
	}
}