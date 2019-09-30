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
public class L_ass_comp_doc_cont_nmpBulk extends L_ass_comp_doc_cont_nmpBase {
	public L_ass_comp_doc_cont_nmpBulk() {
		super();
	}
	public L_ass_comp_doc_cont_nmpBulk(java.math.BigDecimal pg_storico_, java.lang.String cd_cds_compenso, java.lang.Integer esercizio_compenso, java.lang.String cd_uo_compenso, java.lang.Long pg_compenso, java.lang.String cd_cds_doc, java.lang.Integer esercizio_doc, java.lang.Long pg_doc, java.lang.String cd_tipo_doc) {
		super(pg_storico_, cd_cds_compenso, esercizio_compenso, cd_uo_compenso, pg_compenso, cd_cds_doc, esercizio_doc, pg_doc, cd_tipo_doc);
	}
}