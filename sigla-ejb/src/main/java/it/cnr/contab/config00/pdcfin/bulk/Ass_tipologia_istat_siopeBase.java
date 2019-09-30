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
 * Date 18/06/2007
 */
package it.cnr.contab.config00.pdcfin.bulk;
import it.cnr.jada.persistency.Keyed;
public class Ass_tipologia_istat_siopeBase extends Ass_tipologia_istat_siopeKey implements Keyed {
	public Ass_tipologia_istat_siopeBase() {
		super();
	}
	public Ass_tipologia_istat_siopeBase(java.lang.Integer pg_tipologia, java.lang.Integer esercizio_siope, java.lang.String ti_gestione_siope, java.lang.String cd_siope) {
		super(pg_tipologia, esercizio_siope, ti_gestione_siope, cd_siope);
	}
}