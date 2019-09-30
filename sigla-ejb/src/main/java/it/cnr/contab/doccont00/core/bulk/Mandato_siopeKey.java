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
 * Date 07/05/2007
 */
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Mandato_siopeKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_cds;
	private java.lang.Integer esercizio;
	private java.lang.Long pg_mandato;
	private java.lang.Integer esercizio_obbligazione;
	private java.lang.Integer esercizio_ori_obbligazione;
	private java.lang.Long pg_obbligazione;
	private java.lang.Long pg_obbligazione_scadenzario;
	private java.lang.String cd_cds_doc_amm;
	private java.lang.String cd_uo_doc_amm;
	private java.lang.Integer esercizio_doc_amm;
	private java.lang.String cd_tipo_documento_amm;
	private java.lang.Long pg_doc_amm;
	private java.lang.Integer esercizio_siope;
	private java.lang.String ti_gestione;
	private java.lang.String cd_siope;
	public Mandato_siopeKey() {
		super();
	}
	public Mandato_siopeKey(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.Long pg_mandato, java.lang.Integer esercizio_obbligazione, java.lang.Integer esercizio_ori_obbligazione, java.lang.Long pg_obbligazione, java.lang.Long pg_obbligazione_scadenzario, java.lang.String cd_cds_doc_amm, java.lang.String cd_uo_doc_amm, java.lang.Integer esercizio_doc_amm, java.lang.String cd_tipo_documento_amm, java.lang.Long pg_doc_amm, java.lang.Integer esercizio_siope, java.lang.String ti_gestione, java.lang.String cd_siope) {
		super();
		this.cd_cds=cd_cds;
		this.esercizio=esercizio;
		this.pg_mandato=pg_mandato;
		this.esercizio_obbligazione=esercizio_obbligazione;
		this.esercizio_ori_obbligazione=esercizio_ori_obbligazione;
		this.pg_obbligazione=pg_obbligazione;
		this.pg_obbligazione_scadenzario=pg_obbligazione_scadenzario;
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
		if (!(o instanceof Mandato_siopeKey)) return false;
		Mandato_siopeKey k = (Mandato_siopeKey) o;
		if (!compareKey(getCd_cds(), k.getCd_cds())) return false;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getPg_mandato(), k.getPg_mandato())) return false;
		if (!compareKey(getEsercizio_obbligazione(), k.getEsercizio_obbligazione())) return false;
		if (!compareKey(getEsercizio_ori_obbligazione(), k.getEsercizio_ori_obbligazione())) return false;
		if (!compareKey(getPg_obbligazione(), k.getPg_obbligazione())) return false;
		if (!compareKey(getPg_obbligazione_scadenzario(), k.getPg_obbligazione_scadenzario())) return false;
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
		i = i + calculateKeyHashCode(getPg_mandato());
		i = i + calculateKeyHashCode(getEsercizio_obbligazione());
		i = i + calculateKeyHashCode(getEsercizio_ori_obbligazione());
		i = i + calculateKeyHashCode(getPg_obbligazione());
		i = i + calculateKeyHashCode(getPg_obbligazione_scadenzario());
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
	public void setPg_mandato(java.lang.Long pg_mandato)  {
		this.pg_mandato=pg_mandato;
	}
	public java.lang.Long getPg_mandato() {
		return pg_mandato;
	}
	public void setEsercizio_obbligazione(java.lang.Integer esercizio_obbligazione)  {
		this.esercizio_obbligazione=esercizio_obbligazione;
	}
	public java.lang.Integer getEsercizio_obbligazione() {
		return esercizio_obbligazione;
	}
	public void setEsercizio_ori_obbligazione(java.lang.Integer esercizio_ori_obbligazione)  {
		this.esercizio_ori_obbligazione=esercizio_ori_obbligazione;
	}
	public java.lang.Integer getEsercizio_ori_obbligazione() {
		return esercizio_ori_obbligazione;
	}
	public void setPg_obbligazione(java.lang.Long pg_obbligazione)  {
		this.pg_obbligazione=pg_obbligazione;
	}
	public java.lang.Long getPg_obbligazione() {
		return pg_obbligazione;
	}
	public void setPg_obbligazione_scadenzario(java.lang.Long pg_obbligazione_scadenzario)  {
		this.pg_obbligazione_scadenzario=pg_obbligazione_scadenzario;
	}
	public java.lang.Long getPg_obbligazione_scadenzario() {
		return pg_obbligazione_scadenzario;
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