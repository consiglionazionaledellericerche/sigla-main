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
public class Ass_tipo_ruolo_privilegioBase extends Ass_tipo_ruolo_privilegioKey implements Keyed {
	public Ass_tipo_ruolo_privilegioBase() {
		super();
	}
	public Ass_tipo_ruolo_privilegioBase(java.lang.String tipo, java.lang.String cd_privilegio) {
		super(tipo, cd_privilegio);
	}
}