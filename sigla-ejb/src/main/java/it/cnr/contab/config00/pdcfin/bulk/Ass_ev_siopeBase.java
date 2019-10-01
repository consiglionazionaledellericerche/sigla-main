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
 * Date 07/05/2007
 */
package it.cnr.contab.config00.pdcfin.bulk;
import it.cnr.jada.persistency.Keyed;
public class Ass_ev_siopeBase extends Ass_ev_siopeKey implements Keyed {
	public Ass_ev_siopeBase() {
		super();
	}
	public Ass_ev_siopeBase(java.lang.Integer esercizio, java.lang.String ti_appartenenza, java.lang.String ti_gestione, java.lang.String cd_elemento_voce, java.lang.String cd_siope) {
		super(esercizio, ti_appartenenza, ti_gestione, cd_elemento_voce, cd_siope);
	}
}