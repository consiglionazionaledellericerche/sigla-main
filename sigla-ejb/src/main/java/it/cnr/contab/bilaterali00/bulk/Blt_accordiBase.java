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
public class Blt_accordiBase extends Blt_accordiKey implements Keyed {
//	NOME_ENTE_STR              VARCHAR2(100 BYTE) NOT NULL,
	private java.lang.String nome_ente_str;

//	ACRONIMO_ENTE_STR          VARCHAR2(10 BYTE)  NOT NULL,
	private java.lang.String acronimo_ente_str;

//	PG_NAZIONE_STR             NUMBER(10)         NOT NULL,
	private java.lang.Long pg_nazione_str;

//	DATA_FIRMA_ACCORDO         DATE               NOT NULL,
	private java.sql.Timestamp data_firma_accordo;

//	DATA_FIRMA_ADDENDUM        DATE               NOT NULL,
	private java.sql.Timestamp data_firma_addendum;

//	CD_RESPONS_ITA             NUMBER(8)          NOT NULL,
	private java.lang.Integer cd_respons_ita;

//	EMAIL_RESPONS_ITA          VARCHAR2(100 BYTE),
	private java.lang.String email_respons_ita;

//	TELEF_RESPONS_ITA          VARCHAR2(30 BYTE),
	private java.lang.String telef_respons_ita;

//	FAX_RESPONS_ITA            VARCHAR2(30 BYTE),
	private java.lang.String fax_respons_ita;
	  
//	COGNOME_RESPONS_STR        VARCHAR2(30 BYTE)  NOT NULL,
	private java.lang.String cognome_respons_str;

//	NOME_RESPONS_STR           VARCHAR2(30 BYTE)  NOT NULL,
	private java.lang.String nome_respons_str;

//	EMAIL_RESPONS_STR          VARCHAR2(100 BYTE),
	private java.lang.String email_respons_str;

//  TELEF_RESPONS_STR          VARCHAR2(30 BYTE),
	private java.lang.String telef_respons_str;

//	FAX_RESPONS_STR            VARCHAR2(30 BYTE),
	private java.lang.String fax_respons_str;
	  
//	IMPORTO_DIARIA_ITA         NUMBER(15,2)       NOT NULL,
	private java.math.BigDecimal importo_diaria_ita;

//	IMPORTO_MENSILE_ITA        NUMBER(15,2)       NOT NULL,
	private java.math.BigDecimal importo_mensile_ita;

//	IMPORTO_DIARIA_STR         NUMBER(15,2)       NOT NULL,
	private java.math.BigDecimal importo_diaria_str;

//	IMPORTO_MENSILE_STR        NUMBER(15,2)       NOT NULL,
	private java.math.BigDecimal importo_mensile_str;

//	SPESE_VIAGGIO_ITA          NUMBER(15,2)       NOT NULL,
	private java.math.BigDecimal spese_viaggio_ita;

//	FL_RIMBORSO_TRENO          CHAR(1 BYTE)       NOT NULL,
	private java.lang.Boolean fl_rimborso_treno;

//	FL_SPESE_VISTO             CHAR(1 BYTE)       NOT NULL,
	private java.lang.Boolean fl_spese_visto;

//	FL_CONV_FISCALE            CHAR(1 BYTE)       NOT NULL,
	private java.lang.Boolean fl_conv_fiscale;

//	FL_PAGAMENTO_ENTE          CHAR(1 BYTE)       NOT NULL,
	private java.lang.Boolean fl_pagamento_ente;

//	FL_SALTA_CONVENZIONE          CHAR(1 BYTE)       NOT NULL,
	private java.lang.Boolean fl_salta_convenzione;

//	CD_TERZO_ENTE              NUMBER(8 BYTE)         NULL,
	private java.lang.Integer cd_terzo_ente;

//	NUM_ARTICOLO_CONV_FISCALE  VARCHAR2(10 BYTE),
	private java.lang.String num_articolo_conv_fiscale;

//	NUM_ARTICOLO_PROP_INTEL    VARCHAR2(10 BYTE),
	private java.lang.String num_articolo_prop_intel;

//	ESERCIZIO                  NUMBER(4),
	private java.lang.Integer esercizio;

//   TI_APPARTENENZA CHAR(1) NOT NULL
	private java.lang.String ti_appartenenza;

//   TI_GESTIONE CHAR(1) NOT NULL
	private java.lang.String ti_gestione;

//   CD_ELEMENTO_VOCE VARCHAR(20) NOT NULL
	private java.lang.String cd_elemento_voce;

//	NUM_PROT_DEC_CONTR         NUMBER(10),
	private java.lang.Integer num_prot_dec_contr;

//	DATA_PROT_DEC_CONT         DATE,
	private java.sql.Timestamp data_prot_dec_contr;

//	PRC_ONERI_FISCALI   NUMBER(4,2)       NOT NULL,
	private java.math.BigDecimal prc_oneri_fiscali;

//	PRC_ONERI_CONTRIBUTIVI   NUMBER(4,2)       NOT NULL,
	private java.math.BigDecimal prc_oneri_contributivi;

//	PRC_ANTICIPO   NUMBER(4,2)       NOT NULL,
	private java.math.BigDecimal prc_anticipo;

//	IMPORTO_MAX_ANTICIPO        NUMBER(15,2)       NOT NULL,
	private java.math.BigDecimal importo_max_anticipo;

//  CD_DIVISA_ITA VARCHAR(10)
	private java.lang.String cd_divisa_ita;

//  NUM_PROT_PARERE_COMMISSIONE DECIMAL(10,0)
	private java.lang.Long num_prot_parere_commissione;

//  DT_PROT_PARERE_COMMISSIONE TIMESTAMP(7)
	private java.sql.Timestamp dt_prot_parere_commissione;

//  NUM_PROT_LISTA_CONGIUNTA DECIMAL(10,0)
	private java.lang.Long num_prot_lista_congiunta;

//  DT_PROT_LISTA_CONGIUNTA TIMESTAMP(7)
	private java.sql.Timestamp dt_prot_lista_congiunta;

//	FL_VIAGGI_INTERNI             CHAR(1 BYTE)       NOT NULL,
	private java.lang.Boolean fl_viaggi_interni;

//	FL_LETTERA_INVITO             CHAR(1 BYTE)       NOT NULL,
	private java.lang.Boolean fl_lettera_invito;
	
//	FL_ATTI_AMMINISTRATIVI        CHAR(1 BYTE)       NOT NULL,
	private java.lang.Boolean fl_atti_amministrativi;

//	DS_NAZIONE_ENG VARCHAR2(30 BYTE),
	private java.lang.String ds_nazione_eng;

//	DS_AGGETTIVO_NAZIONAL VARCHAR2(30 BYTE),
	private java.lang.String ds_aggettivo_nazional;

//	INDIRIZZO_ENTE_STR VARCHAR2(30 BYTE),
	private java.lang.String indirizzo_ente_str;

//	CITTA_ENTE_STR VARCHAR2(30 BYTE),
	private java.lang.String citta_ente_str;

//	EMAIL_TRASMISSIONE_PASSAPORTO VARCHAR2(30 BYTE),
	private java.lang.String email_trasmissione_passaporto;

//	IMPORTO_COMMISSIONE_BONIFICO NUMBER(15,2)       NOT NULL,
	private java.math.BigDecimal importo_commissione_bonifico;
	
// 	ANNO_INI DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer anno_ini;

// 	ANNO_FIN DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer anno_fin;
	
//	IM_DIARIA_CONTRIB_SOGG_ITA        NUMBER(15,2)       NOT NULL,
	private java.math.BigDecimal im_diaria_contrib_sogg_ita;

//	DIARIA_LUNGO_PERIODO        NUMBER(15,2)       NOT NULL,
	private java.math.BigDecimal diaria_lungo_periodo;

//	IM_MENSILE_CONTRIB_SOGG_ITA        NUMBER(15,2)       NOT NULL,
	private java.math.BigDecimal im_mensile_contrib_sogg_ita;

	public java.lang.Boolean getFl_atti_amministrativi() {
		return fl_atti_amministrativi;
	}
	public void setFl_atti_amministrativi(java.lang.Boolean fl_atti_amministrativi) {
		this.fl_atti_amministrativi = fl_atti_amministrativi;
	}
	public java.math.BigDecimal getDiaria_lungo_periodo() {
		return diaria_lungo_periodo;
	}
	public void setDiaria_lungo_periodo(java.math.BigDecimal diaria_lungo_periodo) {
		this.diaria_lungo_periodo = diaria_lungo_periodo;
	}
	public java.lang.Boolean getFl_salta_convenzione() {
		return fl_salta_convenzione;
	}
	public void setFl_salta_convenzione(java.lang.Boolean fl_salta_convenzione) {
		this.fl_salta_convenzione = fl_salta_convenzione;
	}
	public Blt_accordiBase() {
		super();
	}
	public Blt_accordiBase(java.lang.String cd_accordo) {
		super(cd_accordo);
	}
	public java.lang.String getNome_ente_str() {
		return nome_ente_str;
	}
	public void setNome_ente_str(java.lang.String nome_ente_str) {
		this.nome_ente_str = nome_ente_str;
	}
	public java.lang.String getAcronimo_ente_str() {
		return acronimo_ente_str;
	}
	public void setAcronimo_ente_str(java.lang.String acronimo_ente_str) {
		this.acronimo_ente_str = acronimo_ente_str;
	}
	public java.lang.Long getPg_nazione_str() {
		return pg_nazione_str;
	}
	public void setPg_nazione_str(java.lang.Long pg_nazione_str) {
		this.pg_nazione_str = pg_nazione_str;
	}
	public java.sql.Timestamp getData_firma_accordo() {
		return data_firma_accordo;
	}
	public void setData_firma_accordo(java.sql.Timestamp data_firma_accordo) {
		this.data_firma_accordo = data_firma_accordo;
	}
	public java.sql.Timestamp getData_firma_addendum() {
		return data_firma_addendum;
	}
	public void setData_firma_addendum(java.sql.Timestamp data_firma_addendum) {
		this.data_firma_addendum = data_firma_addendum;
	}
	public java.lang.Integer getCd_respons_ita() {
		return cd_respons_ita;
	}
	public void setCd_respons_ita(java.lang.Integer cd_respons_ita) {
		this.cd_respons_ita = cd_respons_ita;
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
	public java.lang.String getCognome_respons_str() {
		return cognome_respons_str;
	}
	public void setCognome_respons_str(java.lang.String cognome_respons_str) {
		this.cognome_respons_str = cognome_respons_str;
	}
	public java.lang.String getNome_respons_str() {
		return nome_respons_str;
	}
	public void setNome_respons_str(java.lang.String nome_respons_str) {
		this.nome_respons_str = nome_respons_str;
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
	public java.math.BigDecimal getImporto_diaria_ita() {
		return importo_diaria_ita;
	}
	public void setImporto_diaria_ita(java.math.BigDecimal importo_diaria_ita) {
		this.importo_diaria_ita = importo_diaria_ita;
	}
	public java.math.BigDecimal getImporto_mensile_ita() {
		return importo_mensile_ita;
	}
	public void setImporto_mensile_ita(java.math.BigDecimal importo_mensile_ita) {
		this.importo_mensile_ita = importo_mensile_ita;
	}
	public java.math.BigDecimal getImporto_diaria_str() {
		return importo_diaria_str;
	}
	public void setImporto_diaria_str(java.math.BigDecimal importo_diaria_str) {
		this.importo_diaria_str = importo_diaria_str;
	}
	public java.math.BigDecimal getImporto_mensile_str() {
		return importo_mensile_str;
	}
	public void setImporto_mensile_str(java.math.BigDecimal importo_mensile_str) {
		this.importo_mensile_str = importo_mensile_str;
	}
	public java.math.BigDecimal getSpese_viaggio_ita() {
		return spese_viaggio_ita;
	}
	public void setSpese_viaggio_ita(java.math.BigDecimal spese_viaggio_ita) {
		this.spese_viaggio_ita = spese_viaggio_ita;
	}
	public java.lang.Boolean getFl_rimborso_treno() {
		return fl_rimborso_treno;
	}
	public void setFl_rimborso_treno(java.lang.Boolean fl_rimborso_treno) {
		this.fl_rimborso_treno = fl_rimborso_treno;
	}
	public java.lang.Boolean getFl_spese_visto() {
		return fl_spese_visto;
	}
	public void setFl_spese_visto(java.lang.Boolean fl_spese_visto) {
	this.fl_spese_visto = fl_spese_visto;
	}
	public java.lang.Boolean getFl_conv_fiscale() {
		return fl_conv_fiscale;
	}
	public void setFl_conv_fiscale(java.lang.Boolean fl_conv_fiscale) {
		this.fl_conv_fiscale = fl_conv_fiscale;
	}
	public java.lang.Boolean getFl_pagamento_ente() {
		return fl_pagamento_ente;
	}
	public void setFl_pagamento_ente(java.lang.Boolean fl_pagamento_ente) {
		this.fl_pagamento_ente = fl_pagamento_ente;
	}
	public java.lang.Integer getCd_terzo_ente() {
		return cd_terzo_ente;
	}
	public void setCd_terzo_ente(java.lang.Integer cd_terzo_ente) {
		this.cd_terzo_ente = cd_terzo_ente;
	}
	public java.lang.String getNum_articolo_conv_fiscale() {
		return num_articolo_conv_fiscale;
	}
	public void setNum_articolo_conv_fiscale(
			java.lang.String num_articolo_conv_fiscale) {
		this.num_articolo_conv_fiscale = num_articolo_conv_fiscale;
	}
	public java.lang.String getNum_articolo_prop_intel() {
		return num_articolo_prop_intel;
	}
	public void setNum_articolo_prop_intel(java.lang.String num_articolo_prop_intel) {
		this.num_articolo_prop_intel = num_articolo_prop_intel;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio) {
		this.esercizio = esercizio;
	}
	public java.lang.String getTi_appartenenza() {
		return ti_appartenenza;
	}
	public void setTi_appartenenza(java.lang.String ti_appartenenza) {
		this.ti_appartenenza = ti_appartenenza;
	}
	public java.lang.String getTi_gestione() {
		return ti_gestione;
	}
	public void setTi_gestione(java.lang.String ti_gestione) {
		this.ti_gestione = ti_gestione;
	}
	public java.lang.String getCd_elemento_voce() {
		return cd_elemento_voce;
	}
	public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
		this.cd_elemento_voce = cd_elemento_voce;
	}
	public java.lang.Integer getNum_prot_dec_contr() {
		return num_prot_dec_contr;
	}
	public void setNum_prot_dec_contr(java.lang.Integer num_prot_dec_contr) {
		this.num_prot_dec_contr = num_prot_dec_contr;
	}
	public java.sql.Timestamp getData_prot_dec_contr() {
		return data_prot_dec_contr;
	}
	public void setData_prot_dec_contr(java.sql.Timestamp data_prot_dec_contr) {
		this.data_prot_dec_contr = data_prot_dec_contr;
	}
	public java.math.BigDecimal getPrc_oneri_fiscali() {
		return prc_oneri_fiscali;
	}
	public void setPrc_oneri_fiscali(java.math.BigDecimal prc_oneri_fiscali) {
		this.prc_oneri_fiscali = prc_oneri_fiscali;
	}
	public java.math.BigDecimal getPrc_oneri_contributivi() {
		return prc_oneri_contributivi;
	}
	public void setPrc_oneri_contributivi(java.math.BigDecimal prc_oneri_contributivi) {
		this.prc_oneri_contributivi = prc_oneri_contributivi;
	}
	public java.math.BigDecimal getImporto_max_anticipo() {
		return importo_max_anticipo;
	}
	public void setImporto_max_anticipo(java.math.BigDecimal importo_max_anticipo) {
		this.importo_max_anticipo = importo_max_anticipo;
	}
	public java.math.BigDecimal getPrc_anticipo() {
		return prc_anticipo;
	}
	public void setPrc_anticipo(java.math.BigDecimal prc_anticipo) {
		this.prc_anticipo = prc_anticipo;
	}
	public java.lang.String getCd_divisa_ita() {
		return cd_divisa_ita;
	}
	public void setCd_divisa_ita(java.lang.String cd_divisa_ita) {
		this.cd_divisa_ita = cd_divisa_ita;
	}
	public java.lang.Long getNum_prot_parere_commissione() {
		return num_prot_parere_commissione;
	}
	public void setNum_prot_parere_commissione(
			java.lang.Long num_prot_parere_commissione) {
		this.num_prot_parere_commissione = num_prot_parere_commissione;
	}
	public java.sql.Timestamp getDt_prot_parere_commissione() {
		return dt_prot_parere_commissione;
	}
	public void setDt_prot_parere_commissione(
			java.sql.Timestamp dt_prot_parere_commissione) {
		this.dt_prot_parere_commissione = dt_prot_parere_commissione;
	}
	public java.lang.Long getNum_prot_lista_congiunta() {
		return num_prot_lista_congiunta;
	}
	public void setNum_prot_lista_congiunta(java.lang.Long num_prot_lista_congiunta) {
		this.num_prot_lista_congiunta = num_prot_lista_congiunta;
	}
	public java.sql.Timestamp getDt_prot_lista_congiunta() {
		return dt_prot_lista_congiunta;
	}
	public void setDt_prot_lista_congiunta(java.sql.Timestamp dt_prot_lista_congiunta) {
		this.dt_prot_lista_congiunta = dt_prot_lista_congiunta;
	}
	public java.lang.Boolean getFl_viaggi_interni() {
		return fl_viaggi_interni;
	}
	public void setFl_viaggi_interni(java.lang.Boolean fl_viaggi_interni) {
		this.fl_viaggi_interni = fl_viaggi_interni;
	}
	public java.lang.Boolean getFl_lettera_invito() {
		return fl_lettera_invito;
	}
	public void setFl_lettera_invito(java.lang.Boolean fl_lettera_invito) {
		this.fl_lettera_invito = fl_lettera_invito;
	}
	public java.lang.String getDs_nazione_eng() {
		return ds_nazione_eng;
	}
	public void setDs_nazione_eng(java.lang.String ds_nazione_eng) {
		this.ds_nazione_eng = ds_nazione_eng;
	}
	public java.lang.String getDs_aggettivo_nazional() {
		return ds_aggettivo_nazional;
	}
	public void setDs_aggettivo_nazional(java.lang.String ds_aggettivo_nazional) {
		this.ds_aggettivo_nazional = ds_aggettivo_nazional;
	}
	public java.lang.String getIndirizzo_ente_str() {
		return indirizzo_ente_str;
	}
	public void setIndirizzo_ente_str(java.lang.String indirizzo_ente_str) {
		this.indirizzo_ente_str = indirizzo_ente_str;
	}
	public java.lang.String getCitta_ente_str() {
		return citta_ente_str;
	}
	public void setCitta_ente_str(java.lang.String citta_ente_str) {
		this.citta_ente_str = citta_ente_str;
	}
	public java.lang.String getEmail_trasmissione_passaporto() {
		return email_trasmissione_passaporto;
	}
	public void setEmail_trasmissione_passaporto(java.lang.String email_trasmissione_passaporto) {
		this.email_trasmissione_passaporto = email_trasmissione_passaporto;
	}
	public java.math.BigDecimal getImporto_commissione_bonifico() {
		return importo_commissione_bonifico;
	}
	public void setImporto_commissione_bonifico(java.math.BigDecimal importo_commissione_bonifico) {
		this.importo_commissione_bonifico = importo_commissione_bonifico;
	}
	public java.lang.Integer getAnno_ini() {
		return anno_ini;
	}
	public void setAnno_ini(java.lang.Integer anno_ini) {
		this.anno_ini = anno_ini;
	}
	public java.lang.Integer getAnno_fin() {
		return anno_fin;
	}
	public void setAnno_fin(java.lang.Integer anno_fin) {
		this.anno_fin = anno_fin;
	}
	public java.math.BigDecimal getIm_diaria_contrib_sogg_ita() {
		return im_diaria_contrib_sogg_ita;
	}
	public void setIm_diaria_contrib_sogg_ita(java.math.BigDecimal im_diaria_contrib_sogg_ita) {
		this.im_diaria_contrib_sogg_ita = im_diaria_contrib_sogg_ita;
	}
	public java.math.BigDecimal getIm_mensile_contrib_sogg_ita() {
		return im_mensile_contrib_sogg_ita;
	}
	public void setIm_mensile_contrib_sogg_ita(java.math.BigDecimal im_mensile_contrib_sogg_ita) {
		this.im_mensile_contrib_sogg_ita = im_mensile_contrib_sogg_ita;
	}
}