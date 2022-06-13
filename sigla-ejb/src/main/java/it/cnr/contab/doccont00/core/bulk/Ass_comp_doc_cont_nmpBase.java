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
 * Date 27/09/2006
 */
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.jada.persistency.Keyed;

public class Ass_comp_doc_cont_nmpBase extends Ass_comp_doc_cont_nmpKey implements Keyed {

	public Ass_comp_doc_cont_nmpBase() {
		super();
	}

	public Ass_comp_doc_cont_nmpBase(Integer esercizio_compenso, String cd_cds_compenso, String cd_uo_compenso, Long pg_compenso, Integer esercizio_doc, String cd_cds_doc, String cd_tipo_doc, Long pg_doc) {
		super(esercizio_compenso, cd_cds_compenso, cd_uo_compenso, pg_compenso, esercizio_doc, cd_cds_doc, cd_tipo_doc, pg_doc);
	}
}