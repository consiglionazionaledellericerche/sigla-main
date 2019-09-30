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
 * Date 09/07/2007
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;
public class V_cons_siope_reversaliBulk extends OggettoBulk implements Persistent {
		
//  CD_CDS VARCHAR(30) NOT NULL
	private java.lang.String cd_cds;
	protected CdsBulk cds = new CdsBulk();
	
//    ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;
 
//    PG_REVERSALE DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_reversale;
 
//    ESERCIZIO_ACCERTAMENTO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_accertamento;
 
//    ESERCIZIO_ORI_ACCERTAMENTO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_ori_accertamento;
 
//    PG_ACCERTAMENTO DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_accertamento;
 
//    PG_ACCERTAMENTO_SCADENZARIO DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_accertamento_scadenzario;
 
//    CD_CDS_DOC_AMM VARCHAR(30) NOT NULL
	private java.lang.String cd_cds_doc_amm;
 
//    CD_UO_DOC_AMM VARCHAR(30) NOT NULL
	private java.lang.String cd_uo_doc_amm;
 
//    ESERCIZIO_DOC_AMM DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_doc_amm;
 
//    CD_TIPO_DOCUMENTO_AMM VARCHAR(10) NOT NULL
	private java.lang.String cd_tipo_documento_amm;

//  DS_TIPO_DOC_AMM VARCHAR(100) NOT NULL
	private java.lang.String ds_tipo_doc_amm;

//    PG_DOC_AMM DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_doc_amm;
 
//    ESERCIZIO_SIOPE DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_siope;
 
//    TI_GESTIONE CHAR(1) NOT NULL
	private java.lang.String ti_gestione;
 
//    CD_SIOPE VARCHAR(45) NOT NULL
	private java.lang.String cd_siope;

//  DS_SIOPE VARCHAR(200) NOT NULL
	private java.lang.String ds_siope;

//    IMPORTO DECIMAL(22,2) NOT NULL
	private java.math.BigDecimal importo;
	private java.math.BigDecimal tot_im_siope;
 
//    DT_EMISSIONE TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dt_emissione;
	private java.sql.Timestamp dt_emissione_da;
	private java.sql.Timestamp dt_emissione_a;
 
//    DT_INCASSO TIMESTAMP(7)
	private java.sql.Timestamp dt_incasso;
	private java.sql.Timestamp dt_incasso_da;
	private java.sql.Timestamp dt_incasso_a;
	
//    DT_TRASMISSIONE TIMESTAMP(7)
	private java.sql.Timestamp dt_trasmissione;
	private java.sql.Timestamp dt_trasmissione_da;
	private java.sql.Timestamp dt_trasmissione_a;
 
	
	private boolean roFindCds;
	
	public V_cons_siope_reversaliBulk() {
		super();
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
	public java.lang.Long getPg_reversale() {
		return pg_reversale;
	}
	public void setPg_reversale(java.lang.Long pg_reversale)  {
		this.pg_reversale=pg_reversale;
	}
	public java.lang.Integer getEsercizio_accertamento() {
		return esercizio_accertamento;
	}
	public void setEsercizio_accertamento(java.lang.Integer esercizio_accertamento)  {
		this.esercizio_accertamento=esercizio_accertamento;
	}
	public java.lang.Integer getEsercizio_ori_accertamento() {
		return esercizio_ori_accertamento;
	}
	public void setEsercizio_ori_accertamento(java.lang.Integer esercizio_ori_accertamento)  {
		this.esercizio_ori_accertamento=esercizio_ori_accertamento;
	}
	public java.lang.Long getPg_accertamento() {
		return pg_accertamento;
	}
	public void setPg_accertamento(java.lang.Long pg_accertamento)  {
		this.pg_accertamento=pg_accertamento;
	}
	public java.lang.Long getPg_accertamento_scadenzario() {
		return pg_accertamento_scadenzario;
	}
	public void setPg_accertamento_scadenzario(java.lang.Long pg_accertamento_scadenzario)  {
		this.pg_accertamento_scadenzario=pg_accertamento_scadenzario;
	}
	public java.lang.String getCd_cds_doc_amm() {
		return cd_cds_doc_amm;
	}
	public void setCd_cds_doc_amm(java.lang.String cd_cds_doc_amm)  {
		this.cd_cds_doc_amm=cd_cds_doc_amm;
	}
	public java.lang.String getCd_uo_doc_amm() {
		return cd_uo_doc_amm;
	}
	public void setCd_uo_doc_amm(java.lang.String cd_uo_doc_amm)  {
		this.cd_uo_doc_amm=cd_uo_doc_amm;
	}
	public java.lang.Integer getEsercizio_doc_amm() {
		return esercizio_doc_amm;
	}
	public void setEsercizio_doc_amm(java.lang.Integer esercizio_doc_amm)  {
		this.esercizio_doc_amm=esercizio_doc_amm;
	}
	public java.lang.String getCd_tipo_documento_amm() {
		return cd_tipo_documento_amm;
	}
	public void setCd_tipo_documento_amm(java.lang.String cd_tipo_documento_amm)  {
		this.cd_tipo_documento_amm=cd_tipo_documento_amm;
	}
	public java.lang.Long getPg_doc_amm() {
		return pg_doc_amm;
	}
	public void setPg_doc_amm(java.lang.Long pg_doc_amm)  {
		this.pg_doc_amm=pg_doc_amm;
	}
	public java.lang.Integer getEsercizio_siope() {
		return esercizio_siope;
	}
	public void setEsercizio_siope(java.lang.Integer esercizio_siope)  {
		this.esercizio_siope=esercizio_siope;
	}
	public java.lang.String getTi_gestione() {
		return ti_gestione;
	}
	public void setTi_gestione(java.lang.String ti_gestione)  {
		this.ti_gestione=ti_gestione;
	}
	public java.lang.String getCd_siope() {
		return cd_siope;
	}
	public void setCd_siope(java.lang.String cd_siope)  {
		this.cd_siope=cd_siope;
	}
	public java.math.BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(java.math.BigDecimal importo)  {
		this.importo=importo;
	}
	public java.sql.Timestamp getDt_emissione() {
		return dt_emissione;
	}
	public void setDt_emissione(java.sql.Timestamp dt_emissione)  {
		this.dt_emissione=dt_emissione;
	}
	public java.sql.Timestamp getDt_incasso() {
		return dt_incasso;
	}
	public void setDt_incasso(java.sql.Timestamp dt_incasso)  {
		this.dt_incasso=dt_incasso;
	}
	public java.sql.Timestamp getDt_trasmissione() {
		return dt_trasmissione;
	}
	public void setDt_trasmissione(java.sql.Timestamp dt_trasmissione)  {
		this.dt_trasmissione=dt_trasmissione;
	}
	

	public CdsBulk getCds() {
		return cds;
	}

	public void setCds(CdsBulk cds) {
		this.cds = cds;
	}

	public java.lang.String getDs_siope() {
		return ds_siope;
	}

	public void setDs_siope(java.lang.String ds_siope) {
		this.ds_siope = ds_siope;
	}

	public java.sql.Timestamp getDt_emissione_a() {
		return dt_emissione_a;
	}

	public void setDt_emissione_a(java.sql.Timestamp dt_emissione_a) {
		this.dt_emissione_a = dt_emissione_a;
	}

	public java.sql.Timestamp getDt_emissione_da() {
		return dt_emissione_da;
	}

	public void setDt_emissione_da(java.sql.Timestamp dt_emissione_da) {
		this.dt_emissione_da = dt_emissione_da;
	}

	public java.sql.Timestamp getDt_incasso_a() {
		return dt_incasso_a;
	}

	public void setDt_incasso_a(java.sql.Timestamp dt_incasso_a) {
		this.dt_incasso_a = dt_incasso_a;
	}

	public java.sql.Timestamp getDt_incasso_da() {
		return dt_incasso_da;
	}

	public void setDt_incasso_da(java.sql.Timestamp dt_incasso_da) {
		this.dt_incasso_da = dt_incasso_da;
	}

	public java.sql.Timestamp getDt_trasmissione_a() {
		return dt_trasmissione_a;
	}

	public void setDt_trasmissione_a(java.sql.Timestamp dt_trasmissione_a) {
		this.dt_trasmissione_a = dt_trasmissione_a;
	}

	public java.sql.Timestamp getDt_trasmissione_da() {
		return dt_trasmissione_da;
	}

	public void setDt_trasmissione_da(java.sql.Timestamp dt_trasmissione_da) {
		this.dt_trasmissione_da = dt_trasmissione_da;
	}

	public java.math.BigDecimal getTot_im_siope() {
		return tot_im_siope;
	}

	public void setTot_im_siope(java.math.BigDecimal tot_im_siope) {
		this.tot_im_siope = tot_im_siope;
	}
	
	public boolean isROFindCds() {
		return roFindCds;
	}

	public void setROFindCds(boolean b) {
		roFindCds = b;
	}

	public java.lang.String getDs_tipo_doc_amm() {
		return ds_tipo_doc_amm;
	}

	public void setDs_tipo_doc_amm(java.lang.String ds_tipo_doc_amm) {
		this.ds_tipo_doc_amm = ds_tipo_doc_amm;
	}
}