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
* Created by Generator 1.0
* Date 22/09/2005
*/
package it.cnr.contab.logregistry00.logs.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class L_configurazione_cnrBulk extends L_configurazione_cnrBase {
	public L_configurazione_cnrBulk() {
		super();
	}
	public L_configurazione_cnrBulk(java.math.BigDecimal pg_storico_, java.lang.Integer esercizio, java.lang.String cd_unita_funzionale, java.lang.String cd_chiave_primaria, java.lang.String cd_chiave_secondaria) {
		super(pg_storico_, esercizio, cd_unita_funzionale, cd_chiave_primaria, cd_chiave_secondaria);
	}
}