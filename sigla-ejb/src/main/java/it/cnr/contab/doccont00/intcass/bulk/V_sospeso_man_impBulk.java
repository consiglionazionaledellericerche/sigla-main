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
 * Date 14/07/2009
 */
package it.cnr.contab.doccont00.intcass.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;
public class V_sospeso_man_impBulk   extends OggettoBulk implements Persistent { 
	public V_sospeso_man_impBulk() {
		super();
	}
//  CD_CDS VARCHAR(30) NOT NULL
	private java.lang.String cd_cds;
 
//    ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;
 
//    TI_ENTRATA_SPESA CHAR(1) NOT NULL
	private java.lang.String ti_entrata_spesa;
 
//    TI_SOSPESO_RISCONTRO CHAR(1) NOT NULL
	private java.lang.String ti_sospeso_riscontro;
 
//    CD_SOSPESO VARCHAR(24) NOT NULL
	private java.lang.String cd_sospeso;
 
//    CD_SOSPESO_PADRE VARCHAR(24)
	private java.lang.String cd_sospeso_padre;
 
//    IM_SOSPESO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_sospeso;
 
//    PG_MANDATO DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_mandato;
 
//    CD_UO_ORIGINE VARCHAR(30) NOT NULL
	private java.lang.String cd_uo_origine;
 
//    IM_ASSOCIATO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_associato;
 
//    ESERCIZIO_ORI_OBBLIGAZIONE DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_ori_obbligazione;
 
//    PG_OBBLIGAZIONE DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_obbligazione;
 
//    CD_VOCE VARCHAR(20) NOT NULL
	private java.lang.String cd_voce;
 
//    IM_OBBLIGAZIONE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_obbligazione;
	
	private java.math.BigDecimal im_mandato;
 
	public java.math.BigDecimal getIm_mandato() {
		return im_mandato;
	}
	public void setIm_mandato(java.math.BigDecimal im_mandato) {
		this.im_mandato = im_mandato;
	}
	public java.lang.String getCd_cds() {
		return cd_cds;
	}
	public void setCd_cds(java.lang.String cd_cds)  {
		this.cd_cds=cd_cds;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.String getTi_entrata_spesa() {
		return ti_entrata_spesa;
	}
	public void setTi_entrata_spesa(java.lang.String ti_entrata_spesa)  {
		this.ti_entrata_spesa=ti_entrata_spesa;
	}
	public java.lang.String getTi_sospeso_riscontro() {
		return ti_sospeso_riscontro;
	}
	public void setTi_sospeso_riscontro(java.lang.String ti_sospeso_riscontro)  {
		this.ti_sospeso_riscontro=ti_sospeso_riscontro;
	}
	public java.lang.String getCd_sospeso() {
		return cd_sospeso;
	}
	public void setCd_sospeso(java.lang.String cd_sospeso)  {
		this.cd_sospeso=cd_sospeso;
	}
	public java.lang.String getCd_sospeso_padre() {
		return cd_sospeso_padre;
	}
	public void setCd_sospeso_padre(java.lang.String cd_sospeso_padre)  {
		this.cd_sospeso_padre=cd_sospeso_padre;
	}
	public java.math.BigDecimal getIm_sospeso() {
		return im_sospeso;
	}
	public void setIm_sospeso(java.math.BigDecimal im_sospeso)  {
		this.im_sospeso=im_sospeso;
	}
	public java.lang.Long getPg_mandato() {
		return pg_mandato;
	}
	public void setPg_mandato(java.lang.Long pg_mandato)  {
		this.pg_mandato=pg_mandato;
	}
	public java.lang.String getCd_uo_origine() {
		return cd_uo_origine;
	}
	public void setCd_uo_origine(java.lang.String cd_uo_origine)  {
		this.cd_uo_origine=cd_uo_origine;
	}
	public java.math.BigDecimal getIm_associato() {
		return im_associato;
	}
	public void setIm_associato(java.math.BigDecimal im_associato)  {
		this.im_associato=im_associato;
	}
	public java.lang.Integer getEsercizio_ori_obbligazione() {
		return esercizio_ori_obbligazione;
	}
	public void setEsercizio_ori_obbligazione(java.lang.Integer esercizio_ori_obbligazione)  {
		this.esercizio_ori_obbligazione=esercizio_ori_obbligazione;
	}
	public java.lang.Long getPg_obbligazione() {
		return pg_obbligazione;
	}
	public void setPg_obbligazione(java.lang.Long pg_obbligazione)  {
		this.pg_obbligazione=pg_obbligazione;
	}
	public java.lang.String getCd_voce() {
		return cd_voce;
	}
	public void setCd_voce(java.lang.String cd_voce)  {
		this.cd_voce=cd_voce;
	}
	public java.math.BigDecimal getIm_obbligazione() {
		return im_obbligazione;
	}
	public void setIm_obbligazione(java.math.BigDecimal im_obbligazione)  {
		this.im_obbligazione=im_obbligazione;
	}
}