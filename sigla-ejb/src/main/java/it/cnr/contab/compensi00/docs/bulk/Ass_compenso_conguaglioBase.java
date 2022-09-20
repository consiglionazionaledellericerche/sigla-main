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

package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.jada.persistency.Keyed;

public class Ass_compenso_conguaglioBase extends Ass_compenso_conguaglioKey implements Keyed {
	public Ass_compenso_conguaglioBase() {
	super();
}

	public Ass_compenso_conguaglioBase(String cd_cds_conguaglio, String cd_uo_conguaglio, Long pg_conguaglio, Integer esercizio_conguaglio, String cd_cds_compenso, String cd_uo_compenso, Long pg_compenso, Integer esercizio_compenso) {
		super(cd_cds_conguaglio, cd_uo_conguaglio, pg_conguaglio, esercizio_conguaglio, cd_cds_compenso, cd_uo_compenso, pg_compenso, esercizio_compenso);
	}
}
