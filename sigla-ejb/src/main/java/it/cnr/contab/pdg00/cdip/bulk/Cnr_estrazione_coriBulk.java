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
 * Date 17/03/2009
 */
package it.cnr.contab.pdg00.cdip.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Cnr_estrazione_coriBulk extends Cnr_estrazione_coriBase {
	public Cnr_estrazione_coriBulk() {
		super();
	}
	public Cnr_estrazione_coriBulk(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.String cd_unita_organizzativa, java.lang.Integer pg_liquidazione, java.lang.Integer matricola, java.lang.String codice_fiscale, java.lang.String ti_pagamento, java.lang.Integer esercizio_compenso, java.lang.String cd_imponibile, java.lang.String ti_ente_percipiente, java.lang.String cd_contributo_ritenuta) {
		super(cd_cds, esercizio, cd_unita_organizzativa, pg_liquidazione, matricola, codice_fiscale, ti_pagamento, esercizio_compenso, cd_imponibile, ti_ente_percipiente, cd_contributo_ritenuta);
	}
}