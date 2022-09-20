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

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;

import java.util.Optional;

public class Ass_compenso_conguaglioBulk extends Ass_compenso_conguaglioBase {
	private ConguaglioBulk conguaglio = new ConguaglioBulk();

	private CompensoBulk compenso = new CompensoBulk();

	public Ass_compenso_conguaglioBulk() {
	super();
}

	public Ass_compenso_conguaglioBulk(String cd_cds_conguaglio, String cd_uo_conguaglio, Long pg_conguaglio, Integer esercizio_conguaglio, String cd_cds_compenso, String cd_uo_compenso, Long pg_compenso, Integer esercizio_compenso) {
		super(cd_cds_conguaglio, cd_uo_conguaglio, pg_conguaglio, esercizio_conguaglio, cd_cds_compenso, cd_uo_compenso, pg_compenso, esercizio_compenso);
	}

	public ConguaglioBulk getConguaglio() {
		return conguaglio;
	}

	public void setConguaglio(ConguaglioBulk conguaglio) {
		this.conguaglio = conguaglio;
	}

	public CompensoBulk getCompenso() {
		return compenso;
	}

	public void setCompenso(CompensoBulk compenso) {
		this.compenso = compenso;
	}

	@Override
	public String getCd_cds_conguaglio() {
		return Optional.ofNullable(this.getConguaglio()).map(ConguaglioBulk::getCd_cds).orElse(null);
	}

	@Override
	public void setCd_cds_conguaglio(String cd_cds_conguaglio) {
		Optional.ofNullable(this.getConguaglio()).ifPresent(el->el.setCd_cds(cd_cds_conguaglio));
	}

	@Override
	public String getCd_uo_conguaglio() {
		return Optional.ofNullable(this.getConguaglio()).map(ConguaglioBulk::getCd_unita_organizzativa).orElse(null);
	}

	@Override
	public void setCd_uo_conguaglio(String cd_uo_conguaglio) {
		Optional.ofNullable(this.getConguaglio()).ifPresent(el->el.setCd_unita_organizzativa(cd_uo_conguaglio));
	}

	@Override
	public Long getPg_conguaglio() {
		return Optional.ofNullable(this.getConguaglio()).map(ConguaglioBulk::getPg_conguaglio).orElse(null);
	}

	@Override
	public void setPg_conguaglio(Long pg_conguaglio) {
		Optional.ofNullable(this.getConguaglio()).ifPresent(el->el.setPg_conguaglio(pg_conguaglio));
	}

	@Override
	public Integer getEsercizio_conguaglio() {
		return Optional.ofNullable(this.getConguaglio()).map(ConguaglioBulk::getEsercizio).orElse(null);
	}

	@Override
	public void setEsercizio_conguaglio(Integer esercizio_conguaglio) {
		Optional.ofNullable(this.getConguaglio()).ifPresent(el->el.setEsercizio(esercizio_conguaglio));
	}

	@Override
	public String getCd_cds_compenso() {
		return Optional.ofNullable(this.getCompenso()).map(CompensoBulk::getCd_cds).orElse(null);
	}

	@Override
	public void setCd_cds_compenso(String cd_cds_compenso) {
		Optional.ofNullable(this.getCompenso()).ifPresent(el->el.setCd_cds(cd_cds_compenso));
	}

	@Override
	public String getCd_uo_compenso() {
		return Optional.ofNullable(this.getCompenso()).map(CompensoBulk::getCd_unita_organizzativa).orElse(null);
	}

	@Override
	public void setCd_uo_compenso(String cd_uo_compenso) {
		Optional.ofNullable(this.getCompenso()).ifPresent(el->el.setCd_unita_organizzativa(cd_uo_compenso));
	}

	@Override
	public Long getPg_compenso() {
		return Optional.ofNullable(this.getCompenso()).map(CompensoBulk::getPg_compenso).orElse(null);
	}

	@Override
	public void setPg_compenso(Long pg_compenso) {
		Optional.ofNullable(this.getCompenso()).ifPresent(el->el.setPg_compenso(pg_compenso));
	}

	@Override
	public Integer getEsercizio_compenso() {
		return Optional.ofNullable(this.getCompenso()).map(CompensoBulk::getEsercizio).orElse(null);
	}

	@Override
	public void setEsercizio_compenso(Integer esercizio_compenso) {
		Optional.ofNullable(this.getCompenso()).ifPresent(el->el.setEsercizio(esercizio_compenso));
	}

}
