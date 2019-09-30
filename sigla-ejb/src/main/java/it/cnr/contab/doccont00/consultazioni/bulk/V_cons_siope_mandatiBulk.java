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
 * Date 06/07/2007
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;
public class V_cons_siope_mandatiBulk extends OggettoBulk implements Persistent{
	
//  CD_CDS VARCHAR(30) NOT NULL
	private java.lang.String cd_cds;
	protected CdsBulk cds = new CdsBulk();
	
//    ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;
 
//    PG_MANDATO DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_mandato;
 
//    ESERCIZIO_OBBLIGAZIONE DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_obbligazione;
 
//    ESERCIZIO_ORI_OBBLIGAZIONE DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_ori_obbligazione;
 
//    PG_OBBLIGAZIONE DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_obbligazione;
 
//    PG_OBBLIGAZIONE_SCADENZARIO DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_obbligazione_scadenzario;
 
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
 
//    DT_PAGAMENTO TIMESTAMP(7)
	private java.sql.Timestamp dt_pagamento;
	private java.sql.Timestamp dt_pagamento_da;
	private java.sql.Timestamp dt_pagamento_a;
 
//    DT_TRASMISSIONE TIMESTAMP(7)
	private java.sql.Timestamp dt_trasmissione;
	private java.sql.Timestamp dt_trasmissione_da;
	private java.sql.Timestamp dt_trasmissione_a;
	
	
	private boolean roFindCds;
	
	public V_cons_siope_mandatiBulk() {
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
	public java.lang.Long getPg_mandato() {
		return pg_mandato;
	}
	public void setPg_mandato(java.lang.Long pg_mandato)  {
		this.pg_mandato=pg_mandato;
	}
	public java.lang.Integer getEsercizio_obbligazione() {
		return esercizio_obbligazione;
	}
	public void setEsercizio_obbligazione(java.lang.Integer esercizio_obbligazione)  {
		this.esercizio_obbligazione=esercizio_obbligazione;
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
	public java.lang.Long getPg_obbligazione_scadenzario() {
		return pg_obbligazione_scadenzario;
	}
	public void setPg_obbligazione_scadenzario(java.lang.Long pg_obbligazione_scadenzario)  {
		this.pg_obbligazione_scadenzario=pg_obbligazione_scadenzario;
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
	public java.sql.Timestamp getDt_pagamento() {
		return dt_pagamento;
	}
	public void setDt_pagamento(java.sql.Timestamp dt_pagamento)  {
		this.dt_pagamento=dt_pagamento;
	}
	public java.sql.Timestamp getDt_trasmissione() {
		return dt_trasmissione;
	}
	public void setDt_trasmissione(java.sql.Timestamp dt_trasmissione)  {
		this.dt_trasmissione=dt_trasmissione;
	}
	
	/*public java.util.Dictionary getTi_gestioneKeys() {
		return ti_gestioneKeys;
	}*/

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

	public java.sql.Timestamp getDt_pagamento_a() {
		return dt_pagamento_a;
	}

	public void setDt_pagamento_a(java.sql.Timestamp dt_pagamento_a) {
		this.dt_pagamento_a = dt_pagamento_a;
	}

	public java.sql.Timestamp getDt_pagamento_da() {
		return dt_pagamento_da;
	}

	public void setDt_pagamento_da(java.sql.Timestamp dt_pagamento_da) {
		this.dt_pagamento_da = dt_pagamento_da;
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