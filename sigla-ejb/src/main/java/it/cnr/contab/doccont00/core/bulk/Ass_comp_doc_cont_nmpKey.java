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
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Ass_comp_doc_cont_nmpKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio_compenso;

	private java.lang.String cd_cds_compenso;

	private java.lang.String cd_uo_compenso;

	private java.lang.Long pg_compenso;

	private java.lang.Integer esercizio_doc;

	private java.lang.String cd_cds_doc;

	private java.lang.String cd_tipo_doc;

	private java.lang.Long pg_doc;

	public Ass_comp_doc_cont_nmpKey() {
		super();
	}

	public Ass_comp_doc_cont_nmpKey(Integer esercizio_compenso, String cd_cds_compenso, String cd_uo_compenso, Long pg_compenso, Integer esercizio_doc, String cd_cds_doc, String cd_tipo_doc, Long pg_doc) {
		this.esercizio_compenso = esercizio_compenso;
		this.cd_cds_compenso = cd_cds_compenso;
		this.cd_uo_compenso = cd_uo_compenso;
		this.pg_compenso = pg_compenso;
		this.esercizio_doc = esercizio_doc;
		this.cd_cds_doc = cd_cds_doc;
		this.cd_tipo_doc = cd_tipo_doc;
		this.pg_doc = pg_doc;
	}

	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Ass_comp_doc_cont_nmpKey)) return false;
		Ass_comp_doc_cont_nmpKey k = (Ass_comp_doc_cont_nmpKey) o;
		if (!compareKey(getEsercizio_compenso(), k.getEsercizio_compenso())) return false;
		if (!compareKey(getCd_cds_compenso(), k.getCd_cds_compenso())) return false;
		if (!compareKey(getCd_uo_compenso(), k.getCd_uo_compenso())) return false;
		if (!compareKey(getPg_compenso(), k.getPg_compenso())) return false;
		if (!compareKey(getEsercizio_doc(), k.getEsercizio_doc())) return false;
		if (!compareKey(getCd_cds_doc(), k.getCd_cds_doc())) return false;
		if (!compareKey(getCd_tipo_doc(), k.getCd_tipo_doc())) return false;
		if (!compareKey(getPg_doc(), k.getPg_doc())) return false;
		return true;
	}

	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio_compenso());
		i = i + calculateKeyHashCode(getCd_cds_compenso());
		i = i + calculateKeyHashCode(getCd_uo_compenso());
		i = i + calculateKeyHashCode(getPg_compenso());
		i = i + calculateKeyHashCode(getEsercizio_doc());
		i = i + calculateKeyHashCode(getCd_cds_doc());
		i = i + calculateKeyHashCode(getCd_tipo_doc());
		i = i + calculateKeyHashCode(getPg_doc());
		return i;
	}

	public Integer getEsercizio_compenso() {
		return esercizio_compenso;
	}

	public void setEsercizio_compenso(Integer esercizio_compenso) {
		this.esercizio_compenso = esercizio_compenso;
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

	public Integer getEsercizio_doc() {
		return esercizio_doc;
	}

	public void setEsercizio_doc(Integer esercizio_doc) {
		this.esercizio_doc = esercizio_doc;
	}

	public String getCd_cds_doc() {
		return cd_cds_doc;
	}

	public void setCd_cds_doc(String cd_cds_doc) {
		this.cd_cds_doc = cd_cds_doc;
	}

	public String getCd_tipo_doc() {
		return cd_tipo_doc;
	}

	public void setCd_tipo_doc(String cd_tipo_doc) {
		this.cd_tipo_doc = cd_tipo_doc;
	}

	public Long getPg_doc() {
		return pg_doc;
	}

	public void setPg_doc(Long pg_doc) {
		this.pg_doc = pg_doc;
	}
}