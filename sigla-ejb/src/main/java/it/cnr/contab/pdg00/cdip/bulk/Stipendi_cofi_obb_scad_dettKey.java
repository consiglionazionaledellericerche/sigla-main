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
 * Date 28/09/2006
 */
package it.cnr.contab.pdg00.cdip.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Stipendi_cofi_obb_scad_dettKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.Integer mese;
	private java.lang.String tipo_flusso;
	private java.lang.String cd_cds_obbligazione;
	private java.lang.Integer esercizio_obbligazione;
	private java.lang.Long pg_obbligazione;
	private java.lang.Integer esercizio_ori_obbligazione;
	public Stipendi_cofi_obb_scad_dettKey() {
		super();
	}
	public Stipendi_cofi_obb_scad_dettKey(java.lang.Integer esercizio, java.lang.Integer mese, java.lang.String tipo_flusso, java.lang.String cd_cds_obbligazione, java.lang.Integer esercizio_obbligazione, java.lang.Long pg_obbligazione, java.lang.Integer esercizio_ori_obbligazione) {
		super();
		this.esercizio=esercizio;
		this.mese=mese;
		this.tipo_flusso=tipo_flusso;
		this.cd_cds_obbligazione=cd_cds_obbligazione;
		this.esercizio_obbligazione=esercizio_obbligazione;
		this.pg_obbligazione=pg_obbligazione;
		this.esercizio_ori_obbligazione=esercizio_ori_obbligazione;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Stipendi_cofi_obb_scad_dettKey)) return false;
		Stipendi_cofi_obb_scad_dettKey k = (Stipendi_cofi_obb_scad_dettKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getMese(), k.getMese())) return false;
		if (!compareKey(getTipo_flusso(), k.getTipo_flusso())) return false;
		if (!compareKey(getCd_cds_obbligazione(), k.getCd_cds_obbligazione())) return false;
		if (!compareKey(getEsercizio_obbligazione(), k.getEsercizio_obbligazione())) return false;
		if (!compareKey(getPg_obbligazione(), k.getPg_obbligazione())) return false;
		if (!compareKey(getEsercizio_ori_obbligazione(), k.getEsercizio_ori_obbligazione())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getMese());
		i = i + calculateKeyHashCode(getTipo_flusso());
		i = i + calculateKeyHashCode(getCd_cds_obbligazione());
		i = i + calculateKeyHashCode(getEsercizio_obbligazione());
		i = i + calculateKeyHashCode(getPg_obbligazione());
		i = i + calculateKeyHashCode(getEsercizio_ori_obbligazione());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setMese(java.lang.Integer mese)  {
		this.mese=mese;
	}
	public java.lang.Integer getMese() {
		return mese;
	}
	public void setTipo_flusso(java.lang.String tipo_flusso)  {
		this.tipo_flusso=tipo_flusso;
	}
	public java.lang.String getTipo_flusso() {
		return tipo_flusso;
	}
	public void setCd_cds_obbligazione(java.lang.String cd_cds_obbligazione)  {
		this.cd_cds_obbligazione=cd_cds_obbligazione;
	}
	public java.lang.String getCd_cds_obbligazione() {
		return cd_cds_obbligazione;
	}
	public void setEsercizio_obbligazione(java.lang.Integer esercizio_obbligazione)  {
		this.esercizio_obbligazione=esercizio_obbligazione;
	}
	public java.lang.Integer getEsercizio_obbligazione() {
		return esercizio_obbligazione;
	}
	public void setPg_obbligazione(java.lang.Long pg_obbligazione)  {
		this.pg_obbligazione=pg_obbligazione;
	}
	public java.lang.Long getPg_obbligazione() {
		return pg_obbligazione;
	}
	public void setEsercizio_ori_obbligazione(java.lang.Integer esercizio_ori_obbligazione)  {
		this.esercizio_ori_obbligazione=esercizio_ori_obbligazione;
	}
	public java.lang.Integer getEsercizio_ori_obbligazione() {
		return esercizio_ori_obbligazione;
	}
}