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
import it.cnr.jada.persistency.KeyedPersistent;

import java.util.Objects;

public class Ass_compenso_conguaglioKey extends OggettoBulk implements KeyedPersistent {
	// CD_CDS_CONGUAGLIO VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds_conguaglio;

	// CD_UO_CONGUAGLIO VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_uo_conguaglio;

	// PG_CONGUAGLIO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_conguaglio;

	// ESERCIZIO_CONGUAGLIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio_conguaglio;

	// CD_CDS_COMPENSO VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds_compenso;

	// CD_UO_COMPENSO VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_uo_compenso;

	// PG_COMPENSO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_compenso;

	// ESERCIZIO_COMPENSO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio_compenso;

	public Ass_compenso_conguaglioKey() {
			super();
		}

	public Ass_compenso_conguaglioKey(String cd_cds_conguaglio, String cd_uo_conguaglio, Long pg_conguaglio, Integer esercizio_conguaglio, String cd_cds_compenso, String cd_uo_compenso, Long pg_compenso, Integer esercizio_compenso) {
		this.cd_cds_conguaglio = cd_cds_conguaglio;
		this.cd_uo_conguaglio = cd_uo_conguaglio;
		this.pg_conguaglio = pg_conguaglio;
		this.esercizio_conguaglio = esercizio_conguaglio;
		this.cd_cds_compenso = cd_cds_compenso;
		this.cd_uo_compenso = cd_uo_compenso;
		this.pg_compenso = pg_compenso;
		this.esercizio_compenso = esercizio_compenso;
	}

	public String getCd_cds_conguaglio() {
		return cd_cds_conguaglio;
	}

	public void setCd_cds_conguaglio(String cd_cds_conguaglio) {
		this.cd_cds_conguaglio = cd_cds_conguaglio;
	}

	public String getCd_uo_conguaglio() {
		return cd_uo_conguaglio;
	}

	public void setCd_uo_conguaglio(String cd_uo_conguaglio) {
		this.cd_uo_conguaglio = cd_uo_conguaglio;
	}

	public Long getPg_conguaglio() {
		return pg_conguaglio;
	}

	public void setPg_conguaglio(Long pg_conguaglio) {
		this.pg_conguaglio = pg_conguaglio;
	}

	public Integer getEsercizio_conguaglio() {
		return esercizio_conguaglio;
	}

	public void setEsercizio_conguaglio(Integer esercizio_conguaglio) {
		this.esercizio_conguaglio = esercizio_conguaglio;
	}

	public String getCd_cds_compenso() {
		return cd_cds_compenso;
	}

	public void setCd_cds_compenso(String cd_cds_compenso) {
		this.cd_cds_compenso = cd_cds_compenso;
	}

	public String getCd_uo_compenso() {
		return cd_uo_compenso;
	}

	public void setCd_uo_compenso(String cd_uo_compenso) {
		this.cd_uo_compenso = cd_uo_compenso;
	}

	public Long getPg_compenso() {
		return pg_compenso;
	}

	public void setPg_compenso(Long pg_compenso) {
		this.pg_compenso = pg_compenso;
	}

	public Integer getEsercizio_compenso() {
		return esercizio_compenso;
	}

	public void setEsercizio_compenso(Integer esercizio_compenso) {
		this.esercizio_compenso = esercizio_compenso;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Ass_compenso_conguaglioKey that = (Ass_compenso_conguaglioKey) o;
		return Objects.equals(cd_cds_conguaglio, that.cd_cds_conguaglio) && Objects.equals(cd_uo_conguaglio, that.cd_uo_conguaglio) && Objects.equals(pg_conguaglio, that.pg_conguaglio) && Objects.equals(esercizio_conguaglio, that.esercizio_conguaglio) && Objects.equals(cd_cds_compenso, that.cd_cds_compenso) && Objects.equals(cd_uo_compenso, that.cd_uo_compenso) && Objects.equals(pg_compenso, that.pg_compenso) && Objects.equals(esercizio_compenso, that.esercizio_compenso);
	}

	@Override
	public int hashCode() {
		return Objects.hash(cd_cds_conguaglio, cd_uo_conguaglio, pg_conguaglio, esercizio_conguaglio, cd_cds_compenso, cd_uo_compenso, pg_compenso, esercizio_compenso);
	}
}
