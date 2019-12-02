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
public class ConsAccertamentiBase extends  OggettoBulk implements Persistent {
//	 CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cds;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// ESERCIZIO_ORIGINALE DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio_originale;

	// pg_accertamento DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_accertamento;

	private java.lang.Long pg_accertamento_scadenzario;

	//	PG_DOC_AMM VARCHAR(15)
  	private java.lang.String pg_doc_amm;
 
	//	PG_reversale VARCHAR(15)
  	private java.lang.String pg_reversale;
  	
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
	
	// ESERCIZIO_DOCAMM
	private java.lang.Integer esercizio_docamm;
  
//  DT_PAGAMENTO TIMESTAMP(7)
	private java.sql.Timestamp data_incasso;

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
	
	public java.lang.String getTipo_doc_amm() {
		return tipo_doc_amm;
	}
	public void setTipo_doc_amm(java.lang.String tipo_doc_amm) {
		this.tipo_doc_amm = tipo_doc_amm;
	}
	public java.lang.Long getPg_accertamento() {
		return pg_accertamento;
	}
	public void setPg_accertamento(java.lang.Long pg_accertamento) {
		this.pg_accertamento = pg_accertamento;
	}
	public java.lang.Long getPg_accertamento_scadenzario() {
		return pg_accertamento_scadenzario;
	}
	public void setPg_accertamento_scadenzario(
			java.lang.Long pg_accertamento_scadenzario) {
		this.pg_accertamento_scadenzario = pg_accertamento_scadenzario;
	}
	public java.lang.String getPg_reversale() {
		return pg_reversale;
	}
	public void setPg_reversale(java.lang.String pg_reversale) {
		this.pg_reversale = pg_reversale;
	}
	public java.lang.Integer getEsercizio_docamm() {
		return esercizio_docamm;
	}
	public void setEsercizio_docamm(java.lang.Integer esercizio_docamm) {
		this.esercizio_docamm = esercizio_docamm;
	}
	public java.sql.Timestamp getData_incasso() {
		return data_incasso;
	}
	public void setData_incasso(java.sql.Timestamp data_incasso) {
		this.data_incasso = data_incasso;
	}
	public java.sql.Timestamp getData_docamm() {
		return data_docamm;
	}
	public void setData_docamm(java.sql.Timestamp data_docamm) {
		this.data_docamm = data_docamm;
	}
}