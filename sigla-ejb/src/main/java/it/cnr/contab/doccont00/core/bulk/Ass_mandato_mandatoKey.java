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

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Ass_mandato_mandatoKey extends OggettoBulk implements KeyedPersistent {
	private Integer esercizio;

	private String cd_cds;

	private Long pg_mandato;

	private Integer esercizio_coll;

	private String cd_cds_coll;

	private Long pg_mandato_coll;


	public Ass_mandato_mandatoKey() {
		super();
	}

	public Ass_mandato_mandatoKey(String cd_cds, String cd_cds_coll, Integer esercizio, Integer esercizio_coll, Long pg_mandato, Long pg_mandato_coll) {
		super();
		this.cd_cds = cd_cds;
		this.cd_cds_coll = cd_cds_coll;
		this.esercizio = esercizio;
		this.esercizio_coll = esercizio_coll;
		this.pg_mandato = pg_mandato;
		this.pg_mandato_coll = pg_mandato_coll;
	}

	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof Ass_mandato_mandatoKey)) return false;
		Ass_mandato_mandatoKey k = (Ass_mandato_mandatoKey) o;
		if (!compareKey(getCd_cds(), k.getCd_cds())) return false;
		if (!compareKey(getCd_cds_coll(), k.getCd_cds_coll())) return false;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getEsercizio_coll(), k.getEsercizio_coll())) return false;
		if (!compareKey(getPg_mandato(), k.getPg_mandato())) return false;
		if (!compareKey(getPg_mandato_coll(), k.getPg_mandato_coll())) return false;
		return true;
	}

	public Integer getEsercizio() {
		return esercizio;
	}

	public void setEsercizio(Integer esercizio) {
		this.esercizio = esercizio;
	}

	public String getCd_cds() {
		return cd_cds;
	}

	public void setCd_cds(String cd_cds) {
		this.cd_cds = cd_cds;
	}

	public Long getPg_mandato() {
		return pg_mandato;
	}

	public void setPg_mandato(Long pg_mandato) {
		this.pg_mandato = pg_mandato;
	}

	public Integer getEsercizio_coll() {
		return esercizio_coll;
	}

	public void setEsercizio_coll(Integer esercizio_coll) {
		this.esercizio_coll = esercizio_coll;
	}

	public String getCd_cds_coll() {
		return cd_cds_coll;
	}

	public void setCd_cds_coll(String cd_cds_coll) {
		this.cd_cds_coll = cd_cds_coll;
	}

	public Long getPg_mandato_coll() {
		return pg_mandato_coll;
	}

	public void setPg_mandato_coll(Long pg_mandato_coll) {
		this.pg_mandato_coll = pg_mandato_coll;
	}
}