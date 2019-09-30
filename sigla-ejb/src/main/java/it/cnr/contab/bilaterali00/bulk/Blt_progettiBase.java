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
* Date 19/10/2005
*/
package it.cnr.contab.bilaterali00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Blt_progettiBase extends Blt_progettiKey implements Keyed {
//	DS_PROGETTO_ITA              VARCHAR2(250 BYTE) NOT NULL,
	private java.lang.String ds_progetto_ita;

//	DS_PROGETTO_ENG              VARCHAR2(250 BYTE) NOT NULL,
	private java.lang.String ds_progetto_eng;

//	CD_RESPONS_ITA             NUMBER(8)          NOT NULL,
	private java.lang.Integer cd_respons_ita;

//	CD_CDR_RESPONS_ITA        VARCHAR(30) NOT NULL
	private java.lang.String cd_cdr_respons_ita;

//	EMAIL_RESPONS_ITA          VARCHAR2(100 BYTE),
	private java.lang.String email_respons_ita;

//	TELEF_RESPONS_ITA          VARCHAR2(30 BYTE),
	private java.lang.String telef_respons_ita;

//	FAX_RESPONS_ITA            VARCHAR2(30 BYTE),
	private java.lang.String fax_respons_ita;
	  
//	CD_RESPONS_STR             NUMBER(8)          NOT NULL,
	private java.lang.Integer cd_respons_str;

//	EMAIL_RESPONS_STR          VARCHAR2(100 BYTE),
	private java.lang.String email_respons_str;

//  TELEF_RESPONS_STR          VARCHAR2(30 BYTE),
	private java.lang.String telef_respons_str;

//	FAX_RESPONS_STR            VARCHAR2(30 BYTE),
	private java.lang.String fax_respons_str;
	  
//	NOME_ISTITUZIONE_RESPONS_STR   VARCHAR2(100 BYTE),
	private java.lang.String nome_istituzione_str;

//	SEDE_ISTITUZIONE_RESPONS_STR   VARCHAR2(100 BYTE),
	private java.lang.String sede_istituzione_str;

//  FL_ASSOCIATO_RESPONS_ITA CHAR(1) NOT NULL
	private java.lang.Boolean fl_associato_respons_ita;

//  ENTE__RESPONS_ITA CHAR(100) NOT NULL
	private java.lang.String ente_respons_ita;
	
//  INDIRIZZO_ENTE__RESPONS_ITA CHAR(100) NOT NULL
	private java.lang.String indirizzo_ente_respons_ita;

//  CAP_ENTE_RESPONS_ITA CHAR(20) NOT NULL
	private java.lang.String cap_ente_respons_ita;

//	PG_COMUNE_ENTE_RESPONS_ITA DECIMAL(10,0)
	private java.lang.Long pg_comune_ente_respons_ita;

	public Blt_progettiBase() {
		super();
	}
	public Blt_progettiBase(java.lang.String cd_accordo, java.lang.String cd_progetto) {
		super(cd_accordo, cd_progetto);
	}
	public java.lang.String getDs_progetto_ita() {
		return ds_progetto_ita;
	}
	public void setDs_progetto_ita(java.lang.String ds_progetto_ita) {
		this.ds_progetto_ita = ds_progetto_ita;
	}
	public java.lang.String getDs_progetto_eng() {
		return ds_progetto_eng;
	}
	public void setDs_progetto_eng(java.lang.String ds_progetto_eng) {
		this.ds_progetto_eng = ds_progetto_eng;
	}
	public java.lang.Integer getCd_respons_ita() {
		return cd_respons_ita;
	}
	public void setCd_respons_ita(java.lang.Integer cd_respons_ita) {
		this.cd_respons_ita = cd_respons_ita;
	}
	public java.lang.String getCd_cdr_respons_ita() {
		return cd_cdr_respons_ita;
	}
	public void setCd_cdr_respons_ita(java.lang.String cd_cdr_respons_ita) {
		this.cd_cdr_respons_ita = cd_cdr_respons_ita;
	}
	public java.lang.String getEmail_respons_ita() {
		return email_respons_ita;
	}
	public void setEmail_respons_ita(java.lang.String email_respons_ita) {
		this.email_respons_ita = email_respons_ita;
	}
	public java.lang.String getTelef_respons_ita() {
		return telef_respons_ita;
	}
	public void setTelef_respons_ita(java.lang.String telef_respons_ita) {
		this.telef_respons_ita = telef_respons_ita;
	}
	public java.lang.String getFax_respons_ita() {
		return fax_respons_ita;
	}
	public void setFax_respons_ita(java.lang.String fax_respons_ita) {
		this.fax_respons_ita = fax_respons_ita;
	}
	public java.lang.Integer getCd_respons_str() {
		return cd_respons_str;
	}
	public void setCd_respons_str(java.lang.Integer cd_respons_str) {
		this.cd_respons_str = cd_respons_str;
	}
	public java.lang.String getEmail_respons_str() {
		return email_respons_str;
	}
	public void setEmail_respons_str(java.lang.String email_respons_str) {
		this.email_respons_str = email_respons_str;
	}
	public java.lang.String getTelef_respons_str() {
		return telef_respons_str;
	}
	public void setTelef_respons_str(java.lang.String telef_respons_str) {
		this.telef_respons_str = telef_respons_str;
	}
	public java.lang.String getFax_respons_str() {
		return fax_respons_str;
	}
	public void setFax_respons_str(java.lang.String fax_respons_str) {
		this.fax_respons_str = fax_respons_str;
	}
	public java.lang.String getNome_istituzione_str() {
		return nome_istituzione_str;
	}
	public void setNome_istituzione_str(java.lang.String nome_istituzione_str) {
		this.nome_istituzione_str = nome_istituzione_str;
	}
	public java.lang.String getSede_istituzione_str() {
		return sede_istituzione_str;
	}
	public void setSede_istituzione_str(java.lang.String sede_istituzione_str) {
		this.sede_istituzione_str = sede_istituzione_str;
	}
	public java.lang.Boolean getFl_associato_respons_ita() {
		return fl_associato_respons_ita;
	}
	public void setFl_associato_respons_ita(
			java.lang.Boolean fl_associato_respons_ita) {
		this.fl_associato_respons_ita = fl_associato_respons_ita;
	}
	public java.lang.String getEnte_respons_ita() {
		return ente_respons_ita;
	}
	public void setEnte_respons_ita(java.lang.String ente_respons_ita) {
		this.ente_respons_ita = ente_respons_ita;
	}
	public java.lang.String getIndirizzo_ente_respons_ita() {
		return indirizzo_ente_respons_ita;
	}
	public void setIndirizzo_ente_respons_ita(java.lang.String indirizzo_ente_respons_ita) {
		this.indirizzo_ente_respons_ita = indirizzo_ente_respons_ita;
	}
	public java.lang.String getCap_ente_respons_ita() {
		return cap_ente_respons_ita;
	}
	public void setCap_ente_respons_ita(java.lang.String cap_ente_respons_ita) {
		this.cap_ente_respons_ita = cap_ente_respons_ita;
	}
	public java.lang.Long getPg_comune_ente_respons_ita() {
		return pg_comune_ente_respons_ita;
	}
	public void setPg_comune_ente_respons_ita(java.lang.Long pg_comune_ente_respons_ita) {
		this.pg_comune_ente_respons_ita = pg_comune_ente_respons_ita;
	}
}