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
 * Date 09/03/2007
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
public class V_cons_scad_obblBulk extends OggettoBulk implements Persistent {
//    CDS VARCHAR(30)
	private java.lang.String cds;
 
//    UO VARCHAR(30) NOT NULL
	private java.lang.String uo;
 
//    ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;
 
//    ESERCIZIO_ORIGINALE DECIMAL(4,0)
	private java.lang.Integer esercizio_originale;
 
//    PG_OBBL DECIMAL(10,0)
	private java.lang.Long pg_obbl;
 
//    PG_OBBL_SCAD DECIMAL(10,0)
	private java.lang.Long pg_obbl_scad;
 
//    VOCE_BILANCIO VARCHAR(20) NOT NULL
	private java.lang.String voce_bilancio;
 
//    DATA_SCAD TIMESTAMP(7)
	private java.sql.Timestamp data_scad;
 
//    DS_SCAD VARCHAR(300)
	private java.lang.String ds_scad;
 
//    IM_SCAD DECIMAL(15,2)
	private java.math.BigDecimal im_scad;
 
//    IM_ASS_DOC_AMM DECIMAL(15,2)
	private java.math.BigDecimal im_ass_doc_amm;
 
//    IMP_ASS_DOC_CONT DECIMAL(15,2)
	private java.math.BigDecimal imp_ass_doc_cont;
 
//    CREDITORE VARCHAR(200) NOT NULL
	private java.lang.String creditore;
	
	private java.lang.String ds_obbl;
 
	public V_cons_scad_obblBulk() {
		super();
	}
	
	public java.lang.String getCds() {
		return cds;
	}
	public void setCds(java.lang.String cds)  {
		this.cds=cds;
	}
	public java.lang.String getUo() {
		return uo;
	}
	public void setUo(java.lang.String uo)  {
		this.uo=uo;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio_originale() {
		return esercizio_originale;
	}
	public void setEsercizio_originale(java.lang.Integer esercizio_originale)  {
		this.esercizio_originale=esercizio_originale;
	}
	public java.lang.Long getPg_obbl() {
		return pg_obbl;
	}
	public void setPg_obbl(java.lang.Long pg_obbl)  {
		this.pg_obbl=pg_obbl;
	}
	public java.lang.Long getPg_obbl_scad() {
		return pg_obbl_scad;
	}
	public void setPg_obbl_scad(java.lang.Long pg_obbl_scad)  {
		this.pg_obbl_scad=pg_obbl_scad;
	}
	public java.lang.String getVoce_bilancio() {
		return voce_bilancio;
	}
	public void setVoce_bilancio(java.lang.String voce_bilancio)  {
		this.voce_bilancio=voce_bilancio;
	}
	public java.sql.Timestamp getData_scad() {
		return data_scad;
	}
	public void setData_scad(java.sql.Timestamp data_scad)  {
		this.data_scad=data_scad;
	}
	public java.lang.String getDs_scad() {
		return ds_scad;
	}
	public void setDs_scad(java.lang.String ds_scad)  {
		this.ds_scad=ds_scad;
	}
	public java.math.BigDecimal getIm_scad() {
		return im_scad;
	}
	public void setIm_scad(java.math.BigDecimal im_scad)  {
		this.im_scad=im_scad;
	}
	public java.math.BigDecimal getIm_ass_doc_amm() {
		return im_ass_doc_amm;
	}
	public void setIm_ass_doc_amm(java.math.BigDecimal im_ass_doc_amm)  {
		this.im_ass_doc_amm=im_ass_doc_amm;
	}
	public java.math.BigDecimal getImp_ass_doc_cont() {
		return imp_ass_doc_cont;
	}
	public void setImp_ass_doc_cont(java.math.BigDecimal imp_ass_doc_cont)  {
		this.imp_ass_doc_cont=imp_ass_doc_cont;
	}
	public java.lang.String getCreditore() {
		return creditore;
	}
	public void setCreditore(java.lang.String creditore)  {
		this.creditore=creditore;
	}

	public java.lang.String getDs_obbl() {
		return ds_obbl;
	}

	public void setDs_obbl(java.lang.String ds_obbl) {
		this.ds_obbl = ds_obbl;
	}

	
}