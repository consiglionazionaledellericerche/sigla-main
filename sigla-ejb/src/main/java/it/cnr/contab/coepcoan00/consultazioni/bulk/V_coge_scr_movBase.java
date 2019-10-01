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
* Created by Generator 1.0
* Date 11/07/2005
*/
package it.cnr.contab.coepcoan00.consultazioni.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Keyed;
import it.cnr.jada.persistency.Persistent;
public class V_coge_scr_movBase extends OggettoBulk implements Persistent {
//    CD_CDS VARCHAR(30) NOT NULL
	private java.lang.String cd_cds;
 
//    ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;
 
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cd_unita_organizzativa;
 
//    PG_SCRITTURA DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_scrittura;
 
//    CD_TERZO DECIMAL(8,0)
	private java.lang.Integer cd_terzo;
 
//    ORIGINE_SCRITTURA VARCHAR(20)
	private java.lang.String origine_scrittura;
 
//    CD_CAUSALE_COGE VARCHAR(50)
	private java.lang.String cd_causale_coge;
 
//    TI_SCRITTURA CHAR(1) NOT NULL
	private java.lang.String ti_scrittura;
 
//    CD_TIPO_DOCUMENTO VARCHAR(10)
	private java.lang.String cd_tipo_documento;
 
//    CD_CDS_DOCUMENTO VARCHAR(30)
	private java.lang.String cd_cds_documento;
 
//    CD_UO_DOCUMENTO VARCHAR(30)
	private java.lang.String cd_uo_documento;
 
//    PG_NUMERO_DOCUMENTO DECIMAL(10,0)
	private java.lang.Long pg_numero_documento;
 
//    ESERCIZIO_DOCUMENTO_AMM DECIMAL(4,0)
	private java.lang.Integer esercizio_documento_amm;
 
//    CD_COMP_DOCUMENTO VARCHAR(50)
	private java.lang.String cd_comp_documento;
 
//    IM_SCRITTURA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_scrittura;
 
//    STATO VARCHAR(1) NOT NULL
	private java.lang.String stato;
 
//    ATTIVA CHAR(1)
	private java.lang.String attiva;
 
//    CD_VOCE_EP VARCHAR(45)
	private java.lang.String cd_voce_ep;
 
//    DS_VOCE_EP VARCHAR(100)
	private java.lang.String ds_voce_ep;
 
//    SEZIONE VARCHAR(1) NOT NULL
	private java.lang.String sezione;
 
//    IM_MOVIMENTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_movimento;
 
//    DT_DA_COMPETENZA_COGE TIMESTAMP(7)
	private java.sql.Timestamp dt_da_competenza_coge;
 
//    DT_A_COMPETENZA_COGE TIMESTAMP(7)
	private java.sql.Timestamp dt_a_competenza_coge;
 
//    TI_ISTITUZ_COMMERC CHAR(1) NOT NULL
	private java.lang.String ti_istituz_commerc;
 
//    FL_MOV_TERZO CHAR(1)
	private java.lang.Boolean fl_mov_terzo;
 
	public V_coge_scr_movBase() {
		super();
	}
	public java.lang.String getCd_cds () {
		return cd_cds;
	}
	public void setCd_cds(java.lang.String cd_cds)  {
		this.cd_cds=cd_cds;
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.String getCd_unita_organizzativa () {
		return cd_unita_organizzativa;
	}
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa)  {
		this.cd_unita_organizzativa=cd_unita_organizzativa;
	}
	public java.lang.Long getPg_scrittura () {
		return pg_scrittura;
	}
	public void setPg_scrittura(java.lang.Long pg_scrittura)  {
		this.pg_scrittura=pg_scrittura;
	}
	public java.lang.Integer getCd_terzo () {
		return cd_terzo;
	}
	public void setCd_terzo(java.lang.Integer cd_terzo)  {
		this.cd_terzo=cd_terzo;
	}
	public java.lang.String getOrigine_scrittura () {
		return origine_scrittura;
	}
	public void setOrigine_scrittura(java.lang.String origine_scrittura)  {
		this.origine_scrittura=origine_scrittura;
	}
	public java.lang.String getCd_causale_coge () {
		return cd_causale_coge;
	}
	public void setCd_causale_coge(java.lang.String cd_causale_coge)  {
		this.cd_causale_coge=cd_causale_coge;
	}
	public java.lang.String getTi_scrittura () {
		return ti_scrittura;
	}
	public void setTi_scrittura(java.lang.String ti_scrittura)  {
		this.ti_scrittura=ti_scrittura;
	}
	public java.lang.String getCd_tipo_documento () {
		return cd_tipo_documento;
	}
	public void setCd_tipo_documento(java.lang.String cd_tipo_documento)  {
		this.cd_tipo_documento=cd_tipo_documento;
	}
	public java.lang.String getCd_cds_documento () {
		return cd_cds_documento;
	}
	public void setCd_cds_documento(java.lang.String cd_cds_documento)  {
		this.cd_cds_documento=cd_cds_documento;
	}
	public java.lang.String getCd_uo_documento () {
		return cd_uo_documento;
	}
	public void setCd_uo_documento(java.lang.String cd_uo_documento)  {
		this.cd_uo_documento=cd_uo_documento;
	}
	public java.lang.Long getPg_numero_documento () {
		return pg_numero_documento;
	}
	public void setPg_numero_documento(java.lang.Long pg_numero_documento)  {
		this.pg_numero_documento=pg_numero_documento;
	}
	public java.lang.Integer getEsercizio_documento_amm () {
		return esercizio_documento_amm;
	}
	public void setEsercizio_documento_amm(java.lang.Integer esercizio_documento_amm)  {
		this.esercizio_documento_amm=esercizio_documento_amm;
	}
	public java.lang.String getCd_comp_documento () {
		return cd_comp_documento;
	}
	public void setCd_comp_documento(java.lang.String cd_comp_documento)  {
		this.cd_comp_documento=cd_comp_documento;
	}
	public java.math.BigDecimal getIm_scrittura () {
		return im_scrittura;
	}
	public void setIm_scrittura(java.math.BigDecimal im_scrittura)  {
		this.im_scrittura=im_scrittura;
	}
	public java.lang.String getStato () {
		return stato;
	}
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
	public java.lang.String getAttiva () {
		return attiva;
	}
	public void setAttiva(java.lang.String attiva)  {
		this.attiva=attiva;
	}
	public java.lang.String getCd_voce_ep () {
		return cd_voce_ep;
	}
	public void setCd_voce_ep(java.lang.String cd_voce_ep)  {
		this.cd_voce_ep=cd_voce_ep;
	}
	public java.lang.String getDs_voce_ep () {
		return ds_voce_ep;
	}
	public void setDs_voce_ep(java.lang.String ds_voce_ep)  {
		this.ds_voce_ep=ds_voce_ep;
	}
	public java.lang.String getSezione () {
		return sezione;
	}
	public void setSezione(java.lang.String sezione)  {
		this.sezione=sezione;
	}
	public java.math.BigDecimal getIm_movimento () {
		return im_movimento;
	}
	public void setIm_movimento(java.math.BigDecimal im_movimento)  {
		this.im_movimento=im_movimento;
	}
	public java.sql.Timestamp getDt_da_competenza_coge () {
		return dt_da_competenza_coge;
	}
	public void setDt_da_competenza_coge(java.sql.Timestamp dt_da_competenza_coge)  {
		this.dt_da_competenza_coge=dt_da_competenza_coge;
	}
	public java.sql.Timestamp getDt_a_competenza_coge () {
		return dt_a_competenza_coge;
	}
	public void setDt_a_competenza_coge(java.sql.Timestamp dt_a_competenza_coge)  {
		this.dt_a_competenza_coge=dt_a_competenza_coge;
	}
	public java.lang.String getTi_istituz_commerc () {
		return ti_istituz_commerc;
	}
	public void setTi_istituz_commerc(java.lang.String ti_istituz_commerc)  {
		this.ti_istituz_commerc=ti_istituz_commerc;
	}
	public java.lang.Boolean getFl_mov_terzo () {
		return fl_mov_terzo;
	}
	public void setFl_mov_terzo(java.lang.Boolean fl_mov_terzo)  {
		this.fl_mov_terzo=fl_mov_terzo;
	}
}