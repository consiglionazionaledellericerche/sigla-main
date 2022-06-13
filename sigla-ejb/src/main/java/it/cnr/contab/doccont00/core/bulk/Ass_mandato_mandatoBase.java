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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.persistency.Keyed;

public class Ass_mandato_mandatoBase extends Ass_mandato_mandatoKey implements Keyed {
	public Ass_mandato_mandatoBase() {
	super();
}

	public Ass_mandato_mandatoBase(String cd_cds, String cd_cds_coll, Integer esercizio, Integer esercizio_coll, Long pg_mandato, Long pg_mandato_coll) {
		super(cd_cds, cd_cds_coll, esercizio, esercizio_coll, pg_mandato, pg_mandato_coll);
	}
}
