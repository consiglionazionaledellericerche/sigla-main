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
* Date 13/04/2005
*/
package it.cnr.contab.config00.contratto.bulk;
import it.cnr.jada.persistency.Keyed;
public class Ass_contratto_uoBase extends Ass_contratto_uoKey implements Keyed {
	public Ass_contratto_uoBase() {
		super();
	}
	public Ass_contratto_uoBase(java.lang.Integer esercizio, java.lang.String stato_contratto, java.lang.Long pg_contratto, java.lang.String cd_unita_organizzativa) {
		super(esercizio, stato_contratto, pg_contratto, cd_unita_organizzativa);
	}
}