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
 * Date 14/05/2007
 */
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Reversale_siopeKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_cds;
	private java.lang.Integer esercizio;
	private java.lang.Long pg_reversale;
	private java.lang.Integer esercizio_accertamento;
	private java.lang.Integer esercizio_ori_accertamento;
	private java.lang.Long pg_accertamento;
	private java.lang.Long pg_accertamento_scadenzario;
	private java.lang.String cd_cds_doc_amm;
	private java.lang.String cd_uo_doc_amm;
	private java.lang.Integer esercizio_doc_amm;
	private java.lang.String cd_tipo_documento_amm;
	private java.lang.Long pg_doc_amm;
	private java.lang.Integer esercizio_siope;
	private java.lang.String ti_gestione;
	private java.lang.String cd_siope;
	public Reversale_siopeKey() {
		super();
	}
	public Reversale_siopeKey(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.Long pg_reversale, java.lang.Integer esercizio_accertamento, java.lang.Integer esercizio_ori_accertamento, java.lang.Long pg_accertamento, java.lang.Long pg_accertamento_scadenzario, java.lang.String cd_cds_doc_amm, java.lang.String cd_uo_doc_amm, java.lang.Integer esercizio_doc_amm, java.lang.String cd_tipo_documento_amm, java.lang.Long pg_doc_amm, java.lang.Integer esercizio_siope, java.lang.String ti_gestione, java.lang.String cd_siope) {
		super();
		this.cd_cds=cd_cds;
		this.esercizio=esercizio;
		this.pg_reversale=pg_reversale;
		this.esercizio_accertamento=esercizio_accertamento;
		this.esercizio_ori_accertamento=esercizio_ori_accertamento;
		this.pg_accertamento=pg_accertamento;
		this.pg_accertamento_scadenzario=pg_accertamento_scadenzario;
		this.cd_cds_doc_amm=cd_cds_doc_amm;
		this.cd_uo_doc_amm=cd_uo_doc_amm;
		this.esercizio_doc_amm=esercizio_doc_amm;
		this.cd_tipo_documento_amm=cd_tipo_documento_amm;
		this.pg_doc_amm=pg_doc_amm;
		this.esercizio_siope=esercizio_siope;
		this.ti_gestione=ti_gestione;
		this.cd_siope=cd_siope;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Reversale_siopeKey)) return false;
		Reversale_siopeKey k = (Reversale_siopeKey) o;
		if (!compareKey(getCd_cds(), k.getCd_cds())) return false;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getPg_reversale(), k.getPg_reversale())) return false;
		if (!compareKey(getEsercizio_accertamento(), k.getEsercizio_accertamento())) return false;
		if (!compareKey(getEsercizio_ori_accertamento(), k.getEsercizio_ori_accertamento())) return false;
		if (!compareKey(getPg_accertamento(), k.getPg_accertamento())) return false;
		if (!compareKey(getPg_accertamento_scadenzario(), k.getPg_accertamento_scadenzario())) return false;
		if (!compareKey(getCd_cds_doc_amm(), k.getCd_cds_doc_amm())) return false;
		if (!compareKey(getCd_uo_doc_amm(), k.getCd_uo_doc_amm())) return false;
		if (!compareKey(getEsercizio_doc_amm(), k.getEsercizio_doc_amm())) return false;
		if (!compareKey(getCd_tipo_documento_amm(), k.getCd_tipo_documento_amm())) return false;
		if (!compareKey(getPg_doc_amm(), k.getPg_doc_amm())) return false;
		if (!compareKey(getEsercizio_siope(), k.getEsercizio_siope())) return false;
		if (!compareKey(getTi_gestione(), k.getTi_gestione())) return false;
		if (!compareKey(getCd_siope(), k.getCd_siope())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_cds());
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getPg_reversale());
		i = i + calculateKeyHashCode(getEsercizio_accertamento());
		i = i + calculateKeyHashCode(getEsercizio_ori_accertamento());
		i = i + calculateKeyHashCode(getPg_accertamento());
		i = i + calculateKeyHashCode(getPg_accertamento_scadenzario());
		i = i + calculateKeyHashCode(getCd_cds_doc_amm());
		i = i + calculateKeyHashCode(getCd_uo_doc_amm());
		i = i + calculateKeyHashCode(getEsercizio_doc_amm());
		i = i + calculateKeyHashCode(getCd_tipo_documento_amm());
		i = i + calculateKeyHashCode(getPg_doc_amm());
		i = i + calculateKeyHashCode(getEsercizio_siope());
		i = i + calculateKeyHashCode(getTi_gestione());
		i = i + calculateKeyHashCode(getCd_siope());
		return i;
	}
	public void setCd_cds(java.lang.String cd_cds)  {
		this.cd_cds=cd_cds;
	}
	public java.lang.String getCd_cds() {
		return cd_cds;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setPg_reversale(java.lang.Long pg_reversale)  {
		this.pg_reversale=pg_reversale;
	}
	public java.lang.Long getPg_reversale() {
		return pg_reversale;
	}
	public void setEsercizio_accertamento(java.lang.Integer esercizio_accertamento)  {
		this.esercizio_accertamento=esercizio_accertamento;
	}
	public java.lang.Integer getEsercizio_accertamento() {
		return esercizio_accertamento;
	}
	public void setEsercizio_ori_accertamento(java.lang.Integer esercizio_ori_accertamento)  {
		this.esercizio_ori_accertamento=esercizio_ori_accertamento;
	}
	public java.lang.Integer getEsercizio_ori_accertamento() {
		return esercizio_ori_accertamento;
	}
	public void setPg_accertamento(java.lang.Long pg_accertamento)  {
		this.pg_accertamento=pg_accertamento;
	}
	public java.lang.Long getPg_accertamento() {
		return pg_accertamento;
	}
	public void setPg_accertamento_scadenzario(java.lang.Long pg_accertamento_scadenzario)  {
		this.pg_accertamento_scadenzario=pg_accertamento_scadenzario;
	}
	public java.lang.Long getPg_accertamento_scadenzario() {
		return pg_accertamento_scadenzario;
	}
	public void setCd_cds_doc_amm(java.lang.String cd_cds_doc_amm)  {
		this.cd_cds_doc_amm=cd_cds_doc_amm;
	}
	public java.lang.String getCd_cds_doc_amm() {
		return cd_cds_doc_amm;
	}
	public void setCd_uo_doc_amm(java.lang.String cd_uo_doc_amm)  {
		this.cd_uo_doc_amm=cd_uo_doc_amm;
	}
	public java.lang.String getCd_uo_doc_amm() {
		return cd_uo_doc_amm;
	}
	public void setEsercizio_doc_amm(java.lang.Integer esercizio_doc_amm)  {
		this.esercizio_doc_amm=esercizio_doc_amm;
	}
	public java.lang.Integer getEsercizio_doc_amm() {
		return esercizio_doc_amm;
	}
	public void setCd_tipo_documento_amm(java.lang.String cd_tipo_documento_amm)  {
		this.cd_tipo_documento_amm=cd_tipo_documento_amm;
	}
	public java.lang.String getCd_tipo_documento_amm() {
		return cd_tipo_documento_amm;
	}
	public void setPg_doc_amm(java.lang.Long pg_doc_amm)  {
		this.pg_doc_amm=pg_doc_amm;
	}
	public java.lang.Long getPg_doc_amm() {
		return pg_doc_amm;
	}
	public void setEsercizio_siope(java.lang.Integer esercizio_siope)  {
		this.esercizio_siope=esercizio_siope;
	}
	public java.lang.Integer getEsercizio_siope() {
		return esercizio_siope;
	}
	public void setTi_gestione(java.lang.String ti_gestione)  {
		this.ti_gestione=ti_gestione;
	}
	public java.lang.String getTi_gestione() {
		return ti_gestione;
	}
	public void setCd_siope(java.lang.String cd_siope)  {
		this.cd_siope=cd_siope;
	}
	public java.lang.String getCd_siope() {
		return cd_siope;
	}
}