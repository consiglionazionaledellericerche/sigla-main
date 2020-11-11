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
* Creted by Generator 1.0
* Date 28/01/2005
*/
package it.cnr.contab.consultazioni.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
public class ConsObbligazioniBase extends  OggettoBulk implements Persistent {
//	 CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cds;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// ESERCIZIO_ORIGINALE DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio_originale;

	// PG_OBBLIGAZIONE DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_obbligazione;

	private java.lang.Long pg_obbligazione_scadenzario;

	//	PG_DOC_AMM VARCHAR(40)
  	private java.lang.String pg_doc_amm;
 
	//	PG_MANDATO VARCHAR(40)
  	private java.lang.String pg_mandato;
  	
  	private java.lang.String tipo_doc_amm;
//    UO VARCHAR(30)
	private java.lang.String uo;
 
//    CD_CENTRO_RESPONSABILITA VARCHAR(30)
	private java.lang.String cd_centro_responsabilita;
 
//    CD_LINEA_ATTIVITA VARCHAR(10)
	private java.lang.String cd_linea_attivita;
 
//    DS_LINEA_ATTIVITA VARCHAR(300)
	private java.lang.String ds_linea_attivita;
 
//    CD_ELEMENTO_VOCE VARCHAR(20)
	private java.lang.String cd_elemento_voce;
 
//    DT_REGISTRAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dt_registrazione;

//    ESERCIZIO_ORI_ORI_RIPORTO DECIMAL(4,0)
	private java.lang.Integer esercizio_ori_ori_riporto;

//    PG_OBBLIGAZIONE_ORI_RIPORTO DECIMAL(10,0)
	private java.lang.Long pg_obbligazione_ori_riporto;
 
//    ESERCIZIO_ORI_RIPORTO DECIMAL(4,0)
	private java.lang.Integer esercizio_ori_riporto;
 
//    DS_SCADENZA VARCHAR(300)
	private java.lang.String ds_scadenza;
 
//    IM_SCADENZA DECIMAL(15,2)
	private java.math.BigDecimal im_scadenza;
 
//    IM_VOCE DECIMAL(15,2)
	private java.math.BigDecimal im_voce;
 
//    CD_TERZO VARCHAR(40)
	private java.lang.String cd_terzo;
 
//    DENOMINAZIONE_SEDE VARCHAR(200)
	private java.lang.String denominazione_sede;
 
//    TI_FATTURA VARCHAR(1)
	private java.lang.String ti_fattura;
	
	private java.lang.Integer esercizio_docamm; 
	
	private java.lang.Integer esercizio_contratto;

	public Integer getCd_terzo_resp_gae() {
		return cd_terzo_resp_gae;
	}

	public void setCd_terzo_resp_gae(Integer cd_terzo_resp_gae) {
		this.cd_terzo_resp_gae = cd_terzo_resp_gae;
	}

	private java.lang.Long pg_contratto;

	private java.lang.Integer cd_terzo_resp_gae;

	private java.lang.String cd_progetto;

	public String getCd_progetto() {
		return cd_progetto;
	}

	public void setCd_progetto(String cd_progetto) {
		this.cd_progetto = cd_progetto;
	}

	public String getCd_progetto_padre() {
		return cd_progetto_padre;
	}

	public void setCd_progetto_padre(String cd_progetto_padre) {
		this.cd_progetto_padre = cd_progetto_padre;
	}

	private java.lang.String cd_progetto_padre;

	//  DT_PAGAMENTO TIMESTAMP(7)
	private java.sql.Timestamp data_pagamento;

//  DT_DOCAMM TIMESTAMP(7)
	private java.sql.Timestamp data_docamm;

	public java.lang.String getUo () {
		return uo;
	}
	public void setUo(java.lang.String uo)  {
		this.uo=uo;
	}
	public java.lang.String getCd_centro_responsabilita () {
		return cd_centro_responsabilita;
	}
	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita)  {
		this.cd_centro_responsabilita=cd_centro_responsabilita;
	}
	public java.lang.String getCd_linea_attivita () {
		return cd_linea_attivita;
	}
	public void setCd_linea_attivita(java.lang.String cd_linea_attivita)  {
		this.cd_linea_attivita=cd_linea_attivita;
	}
	public java.lang.String getDs_linea_attivita () {
		return ds_linea_attivita;
	}
	public void setDs_linea_attivita(java.lang.String ds_linea_attivita)  {
		this.ds_linea_attivita=ds_linea_attivita;
	}
	public java.lang.String getCd_elemento_voce () {
		return cd_elemento_voce;
	}
	public void setCd_elemento_voce(java.lang.String cd_elemento_voce)  {
		this.cd_elemento_voce=cd_elemento_voce;
	}
	public java.sql.Timestamp getDt_registrazione () {
		return dt_registrazione;
	}
	public void setDt_registrazione(java.sql.Timestamp dt_registrazione)  {
		this.dt_registrazione=dt_registrazione;
	}
	public java.lang.Integer getEsercizio_ori_ori_riporto () {
		return esercizio_ori_ori_riporto;
	}
	public void setEsercizio_ori_ori_riporto(java.lang.Integer esercizio_ori_ori_riporto)  {
		this.esercizio_ori_ori_riporto=esercizio_ori_ori_riporto;
	}
	public java.lang.Long getPg_obbligazione_ori_riporto () {
		return pg_obbligazione_ori_riporto;
	}
	public void setPg_obbligazione_ori_riporto(java.lang.Long pg_obbligazione_ori_riporto)  {
		this.pg_obbligazione_ori_riporto=pg_obbligazione_ori_riporto;
	}
	public java.lang.Integer getEsercizio_ori_riporto () {
		return esercizio_ori_riporto;
	}
	public void setEsercizio_ori_riporto(java.lang.Integer esercizio_ori_riporto)  {
		this.esercizio_ori_riporto=esercizio_ori_riporto;
	}
	public java.lang.String getDs_scadenza () {
		return ds_scadenza;
	}
	public void setDs_scadenza(java.lang.String ds_scadenza)  {
		this.ds_scadenza=ds_scadenza;
	}
	public java.math.BigDecimal getIm_scadenza () {
		return im_scadenza;
	}
	public void setIm_scadenza(java.math.BigDecimal im_scadenza)  {
		this.im_scadenza=im_scadenza;
	}
	public java.math.BigDecimal getIm_voce () {
		return im_voce;
	}
	public void setIm_voce(java.math.BigDecimal im_voce)  {
		this.im_voce=im_voce;
	}
	public java.lang.String getCd_terzo () {
		return cd_terzo;
	}
	public void setCd_terzo(java.lang.String cd_terzo)  {
		this.cd_terzo=cd_terzo;
	}
	public java.lang.String getDenominazione_sede () {
		return denominazione_sede;
	}
	public void setDenominazione_sede(java.lang.String denominazione_sede)  {
		this.denominazione_sede=denominazione_sede;
	}
	public java.lang.String getTi_fattura () {
		return ti_fattura;
	}
	public void setTi_fattura(java.lang.String ti_fattura)  {
		this.ti_fattura=ti_fattura;
	}
	public java.lang.String getCds() {
		return cds;
	}
	public void setCds(java.lang.String cds) {
		this.cds = cds;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio) {
		this.esercizio = esercizio;
	}
	public java.lang.Integer getEsercizio_originale() {
		return esercizio_originale;
	}
	public void setEsercizio_originale(java.lang.Integer esercizio_originale) {
		this.esercizio_originale = esercizio_originale;
	}
	public java.lang.String getPg_doc_amm() {
		return pg_doc_amm;
	}
	public void setPg_doc_amm(java.lang.String pg_doc_amm) {
		this.pg_doc_amm = pg_doc_amm;
	}
	public java.lang.String getPg_mandato() {
		return pg_mandato;
	}
	public void setPg_mandato(java.lang.String pg_mandato) {
		this.pg_mandato = pg_mandato;
	}
	public java.lang.Long getPg_obbligazione() {
		return pg_obbligazione;
	}
	public void setPg_obbligazione(java.lang.Long pg_obbligazione) {
		this.pg_obbligazione = pg_obbligazione;
	}
	public java.lang.Long getPg_obbligazione_scadenzario() {
		return pg_obbligazione_scadenzario;
	}
	public void setPg_obbligazione_scadenzario(
			java.lang.Long pg_obbligazione_scadenzario) {
		this.pg_obbligazione_scadenzario = pg_obbligazione_scadenzario;
	}
	public java.lang.String getTipo_doc_amm() {
		return tipo_doc_amm;
	}
	public void setTipo_doc_amm(java.lang.String tipo_doc_amm) {
		this.tipo_doc_amm = tipo_doc_amm;
	}
	public java.lang.Integer getEsercizio_docamm() {
		return esercizio_docamm;
	}
	public void setEsercizio_docamm(java.lang.Integer esercizio_docamm) {
		this.esercizio_docamm = esercizio_docamm;
	}
	public java.lang.Integer getEsercizio_contratto() {
		return esercizio_contratto;
	}
	public void setEsercizio_contratto(java.lang.Integer esercizio_contratto) {
		this.esercizio_contratto = esercizio_contratto;
	}
	public java.lang.Long getPg_contratto() {
		return pg_contratto;
	}
	public void setPg_contratto(java.lang.Long pg_contratto) {
		this.pg_contratto = pg_contratto;
	}
	public java.sql.Timestamp getData_pagamento() {
		return data_pagamento;
	}
	public void setData_pagamento(java.sql.Timestamp data_pagamento) {
		this.data_pagamento = data_pagamento;
	}
	public java.sql.Timestamp getData_docamm() {
		return data_docamm;
	}
	public void setData_docamm(java.sql.Timestamp data_docamm) {
		this.data_docamm = data_docamm;
	}
}