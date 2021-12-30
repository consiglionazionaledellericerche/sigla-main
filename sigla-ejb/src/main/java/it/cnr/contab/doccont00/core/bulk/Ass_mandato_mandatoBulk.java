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

import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;

import java.util.Optional;

public class Ass_mandato_mandatoBulk extends Ass_mandato_mandatoBase {
	protected MandatoBulk mandato;
	protected MandatoBulk mandatoColl;

	public Ass_mandato_mandatoBulk() {
	super();
}

	public Ass_mandato_mandatoBulk(MandatoBulk mandato, MandatoBulk mandatoColl)
	{
		setMandato( mandato );
		setMandatoColl( mandatoColl );
	}
	public MandatoBulk getMandato() {
	return mandato;
}

	public void setMandato(MandatoBulk newMandato) {
		mandato = newMandato;
	}

	public MandatoBulk getMandatoColl() {
		return mandatoColl;
	}

	public void setMandatoColl(MandatoBulk mandatoColl) {
		this.mandatoColl = mandatoColl;
	}

	@Override
	public Integer getEsercizio() {
		return Optional.ofNullable(this.getMandato())
				.map(MandatoBulk::getEsercizio)
				.orElse(null);
	}

	@Override
	public void setEsercizio(Integer esercizio) {
		this.getMandato().setEsercizio(esercizio);
	}

	@Override
	public String getCd_cds() {
		return Optional.ofNullable(this.getMandato())
				.map(MandatoBulk::getCd_cds)
				.orElse(null);
	}

	@Override
	public void setCd_cds(String cd_cds) {
		this.getMandato().setCd_cds(cd_cds);
	}

	@Override
	public Long getPg_mandato() {
		return Optional.ofNullable(this.getMandato())
				.map(MandatoBulk::getPg_mandato)
				.orElse(null);
	}

	@Override
	public void setPg_mandato(Long pg_mandato) {
		this.getMandato().setPg_mandato(pg_mandato);
	}

	@Override
	public Integer getEsercizio_coll() {
		return Optional.ofNullable(this.getMandatoColl())
				.map(MandatoBulk::getEsercizio)
				.orElse(null);
	}

	@Override
	public void setEsercizio_coll(Integer esercizio_coll) {
		this.getMandatoColl().setEsercizio(esercizio_coll);
	}

	@Override
	public String getCd_cds_coll() {
		return Optional.ofNullable(this.getMandatoColl())
				.map(MandatoBulk::getCd_cds)
				.orElse(null);
	}

	@Override
	public void setCd_cds_coll(String cd_cds_coll) {
		this.getMandatoColl().setCd_cds(cd_cds_coll);
	}

	@Override
	public Long getPg_mandato_coll() {
		return Optional.ofNullable(this.getMandatoColl())
				.map(MandatoBulk::getPg_mandato)
				.orElse(null);
	}

	@Override
	public void setPg_mandato_coll(Long pg_mandato_coll) {
		this.getMandatoColl().setPg_mandato(pg_mandato_coll);
	}
}
