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
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.ordmag.anag00.NumerazioneMagBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.CRUDBP;

import java.util.Optional;

public class Ass_comp_doc_cont_nmpBulk extends Ass_comp_doc_cont_nmpBase {

	private CompensoBulk compenso = new CompensoBulk();

	public Ass_comp_doc_cont_nmpBulk() {
		super();
	}

	public Ass_comp_doc_cont_nmpBulk(Integer esercizio_compenso, String cd_cds_compenso, String cd_uo_compenso, Long pg_compenso, Integer esercizio_doc, String cd_cds_doc, String cd_tipo_doc, Long pg_doc) {
		super(esercizio_compenso, cd_cds_compenso, cd_uo_compenso, pg_compenso, esercizio_doc, cd_cds_doc, cd_tipo_doc, pg_doc);
	}

	public CompensoBulk getCompenso() {
		return compenso;
	}

	public void setCompenso(CompensoBulk compenso) {
		this.compenso = compenso;
	}

	@Override
	public Integer getEsercizio_compenso() {
		return Optional.ofNullable(this.getCompenso())
				.map(CompensoBulk::getEsercizio)
				.orElse(null);
	}

	@Override
	public void setEsercizio_compenso(Integer esercizio_compenso) {
		this.getCompenso().setEsercizio(esercizio_compenso);
	}

	@Override
	public String getCd_cds_compenso() {
		return Optional.ofNullable(this.getCompenso())
				.map(CompensoBulk::getCd_cds)
				.orElse(null);
	}

	@Override
	public void setCd_cds_compenso(String cd_cds_compenso) {
		this.getCompenso().setCd_cds(cd_cds_compenso);
	}

	@Override
	public String getCd_uo_compenso() {
		return Optional.ofNullable(this.getCompenso())
				.map(CompensoBulk::getCd_uo)
				.orElse(null);
	}

	@Override
	public void setCd_uo_compenso(String cd_uo_compenso) {
		this.getCompenso().setCd_uo(cd_uo_compenso);
	}

	@Override
	public Long getPg_compenso() {
		return Optional.ofNullable(this.getCompenso())
				.map(CompensoBulk::getPg_compenso)
				.orElse(null);
	}

	@Override
	public void setPg_compenso(Long pg_compenso) {
		this.getCompenso().setPg_compenso(pg_compenso);
	}


}