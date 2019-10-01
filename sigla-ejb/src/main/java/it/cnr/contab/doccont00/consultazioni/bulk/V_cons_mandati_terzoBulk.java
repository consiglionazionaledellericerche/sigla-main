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
* Date 20/04/2006
*/
package it.cnr.contab.doccont00.consultazioni.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;

public class V_cons_mandati_terzoBulk extends OggettoBulk implements Persistent {
	//    ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;
 
//    CDS VARCHAR(30) NOT NULL
	private java.lang.String cds;

//  CDS_ORIGINE VARCHAR(30) NOT NULL
	private java.lang.String cds_origine;
	
//    DS_CDS_OROGINE VARCHAR(300) NOT NULL
	private java.lang.String ds_cds_origine;

//  UO VARCHAR(30) NOT NULL
	private java.lang.String uo;
 
//    PG_MANDATO DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_mandato;
 
//    DS_MANDATO VARCHAR(300)
	private java.lang.String ds_mandato;
	
//  DS_MANDATO VARCHAR(300)
	private java.lang.String natura_mandato;

//    TI_MANDATO CHAR(1) NOT NULL
	private java.lang.String ti_mandato;
 
//    STATO_MANDATO CHAR(1) NOT NULL
	private java.lang.String stato_mandato;

//  PG_DISTINTA DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_distinta;

//  UO_DISTINTA VARCHAR(30) NOT NULL
	private java.lang.String uo_distinta;

//    DT_EMISSIONE TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dt_emissione;
 
//    DT_TRASMISSIONE TIMESTAMP(7)
	private java.sql.Timestamp dt_trasmissione;
 
//    DT_PAGAMENTO TIMESTAMP(7)
	private java.sql.Timestamp dt_pagamento;
 
//    DT_ANNULLAMENTO TIMESTAMP(7)
	private java.sql.Timestamp dt_annullamento;
 
//    ESERCIZIO_OBBLIGAZIONE DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_obbligazione;
 
//  ESERCIZIO_OBBLIGAZIONE DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_ori_obbligazione;

//    PG_OBBLIGAZIONE DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_obbligazione;
 
//    DS_OBBLIGAZIONE VARCHAR(300) NOT NULL
	private java.lang.String ds_obbligazione;
 
//    CD_ELEMENTO_VOCE VARCHAR(20) NOT NULL
	private java.lang.String cd_elemento_voce;
 
//    DS_ELEMENTO_VOCE VARCHAR(100)
	private java.lang.String ds_elemento_voce;
 
//    CD_TERZO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_terzo;
 
//    DENOMINAZIONE_SEDE VARCHAR(200) NOT NULL
	private java.lang.String denominazione_sede;
 
//    PG_BANCA DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_banca;
 
//    ABI CHAR(5)
	private java.lang.String abi;
 
//    CAB CHAR(5)
	private java.lang.String cab;
 
//    NUMERO_CONTO VARCHAR(30)
	private java.lang.String numero_conto;
 
//    INTESTAZIONE VARCHAR(200)
	private java.lang.String intestazione;
 
//    TI_PAGAMENTO CHAR(1) NOT NULL
	private java.lang.String ti_pagamento;
 
//    DS_TI_PAGAMENTO VARCHAR(9)
	private java.lang.String ds_ti_pagamento;
 
//    IM_MANDATO_RIGA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_mandato_riga;
 
//    IM_RITENUTE_RIGA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_ritenute_riga;
 
//    NETTO_RIGA DECIMAL(15,2)
	private java.math.BigDecimal netto_riga;
 
//    STATO_COGE CHAR(1) NOT NULL
	private java.lang.String stato_coge;
	
	public V_cons_mandati_terzoBulk() {
		super();
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.String getCds () {
		return cds;
	}
	public void setCds(java.lang.String cds)  {
		this.cds=cds;
	}
	public java.lang.String getDs_cds_origine () {
		return ds_cds_origine;
	}
	public void setDs_cds_origine(java.lang.String ds_cds_origine)  {
		this.ds_cds_origine=ds_cds_origine;
	}
	public java.lang.Long getPg_mandato () {
		return pg_mandato;
	}
	public void setPg_mandato(java.lang.Long pg_mandato)  {
		this.pg_mandato=pg_mandato;
	}
	public java.lang.String getDs_mandato () {
		return ds_mandato;
	}
	public void setDs_mandato(java.lang.String ds_mandato)  {
		this.ds_mandato=ds_mandato;
	}
	public java.lang.String getTi_mandato () {
		return ti_mandato;
	}
	public void setTi_mandato(java.lang.String ti_mandato)  {
		this.ti_mandato=ti_mandato;
	}
	public java.lang.String getStato_mandato () {
		return stato_mandato;
	}
	public void setStato_mandato(java.lang.String stato_mandato)  {
		this.stato_mandato=stato_mandato;
	}
	public java.sql.Timestamp getDt_emissione () {
		return dt_emissione;
	}
	public void setDt_emissione(java.sql.Timestamp dt_emissione)  {
		this.dt_emissione=dt_emissione;
	}
	public java.sql.Timestamp getDt_trasmissione () {
		return dt_trasmissione;
	}
	public void setDt_trasmissione(java.sql.Timestamp dt_trasmissione)  {
		this.dt_trasmissione=dt_trasmissione;
	}
	public java.sql.Timestamp getDt_pagamento () {
		return dt_pagamento;
	}
	public void setDt_pagamento(java.sql.Timestamp dt_pagamento)  {
		this.dt_pagamento=dt_pagamento;
	}
	public java.sql.Timestamp getDt_annullamento () {
		return dt_annullamento;
	}
	public void setDt_annullamento(java.sql.Timestamp dt_annullamento)  {
		this.dt_annullamento=dt_annullamento;
	}
	public java.lang.Integer getEsercizio_obbligazione () {
		return esercizio_obbligazione;
	}
	public void setEsercizio_obbligazione(java.lang.Integer esercizio_obbligazione)  {
		this.esercizio_obbligazione=esercizio_obbligazione;
	}
	public java.lang.Integer getEsercizio_ori_obbligazione () {
		return esercizio_ori_obbligazione;
	}
	public void setEsercizio_ori_obbligazione(java.lang.Integer esercizio_ori_obbligazione)  {
		this.esercizio_ori_obbligazione=esercizio_ori_obbligazione;
	}
	public java.lang.Long getPg_obbligazione () {
		return pg_obbligazione;
	}
	public void setPg_obbligazione(java.lang.Long pg_obbligazione)  {
		this.pg_obbligazione=pg_obbligazione;
	}
	public java.lang.String getDs_obbligazione () {
		return ds_obbligazione;
	}
	public void setDs_obbligazione(java.lang.String ds_obbligazione)  {
		this.ds_obbligazione=ds_obbligazione;
	}
	public java.lang.String getCd_elemento_voce () {
		return cd_elemento_voce;
	}
	public void setCd_elemento_voce(java.lang.String cd_elemento_voce)  {
		this.cd_elemento_voce=cd_elemento_voce;
	}
	public java.lang.String getDs_elemento_voce () {
		return ds_elemento_voce;
	}
	public void setDs_elemento_voce(java.lang.String ds_elemento_voce)  {
		this.ds_elemento_voce=ds_elemento_voce;
	}
	public java.lang.Integer getCd_terzo () {
		return cd_terzo;
	}
	public void setCd_terzo(java.lang.Integer cd_terzo)  {
		this.cd_terzo=cd_terzo;
	}
	public java.lang.String getDenominazione_sede () {
		return denominazione_sede;
	}
	public void setDenominazione_sede(java.lang.String denominazione_sede)  {
		this.denominazione_sede=denominazione_sede;
	}
	public java.lang.Long getPg_banca () {
		return pg_banca;
	}
	public void setPg_banca(java.lang.Long pg_banca)  {
		this.pg_banca=pg_banca;
	}
	public java.lang.String getAbi () {
		return abi;
	}
	public void setAbi(java.lang.String abi)  {
		this.abi=abi;
	}
	public java.lang.String getCab () {
		return cab;
	}
	public void setCab(java.lang.String cab)  {
		this.cab=cab;
	}
	public java.lang.String getNumero_conto () {
		return numero_conto;
	}
	public void setNumero_conto(java.lang.String numero_conto)  {
		this.numero_conto=numero_conto;
	}
	public java.lang.String getIntestazione () {
		return intestazione;
	}
	public void setIntestazione(java.lang.String intestazione)  {
		this.intestazione=intestazione;
	}
	public java.lang.String getTi_pagamento () {
		return ti_pagamento;
	}
	public void setTi_pagamento(java.lang.String ti_pagamento)  {
		this.ti_pagamento=ti_pagamento;
	}
	public java.lang.String getDs_ti_pagamento () {
		return ds_ti_pagamento;
	}
	public void setDs_ti_pagamento(java.lang.String ds_ti_pagamento)  {
		this.ds_ti_pagamento=ds_ti_pagamento;
	}
	public java.math.BigDecimal getIm_mandato_riga () {
		return im_mandato_riga;
	}
	public void setIm_mandato_riga(java.math.BigDecimal im_mandato_riga)  {
		this.im_mandato_riga=im_mandato_riga;
	}
	public java.math.BigDecimal getIm_ritenute_riga () {
		return im_ritenute_riga;
	}
	public void setIm_ritenute_riga(java.math.BigDecimal im_ritenute_riga)  {
		this.im_ritenute_riga=im_ritenute_riga;
	}
	public java.math.BigDecimal getNetto_riga () {
		return netto_riga;
	}
	public void setNetto_riga(java.math.BigDecimal netto_riga)  {
		this.netto_riga=netto_riga;
	}
	public java.lang.String getStato_coge () {
		return stato_coge;
	}
	public void setStato_coge(java.lang.String stato_coge)  {
		this.stato_coge=stato_coge;
	}
	public java.lang.String getUo() {
		return uo;
	}
	public void setUo(java.lang.String uo) {
		this.uo = uo;
	}
	public java.lang.Long getPg_distinta() {
		return pg_distinta;
	}
	public void setPg_distinta(java.lang.Long pg_distinta) {
		this.pg_distinta = pg_distinta;
	}
	public java.lang.String getCds_origine() {
		return cds_origine;
	}
	public void setCds_origine(java.lang.String cds_origine) {
		this.cds_origine = cds_origine;
	}
	public java.lang.String getUo_distinta() {
		return uo_distinta;
	}
	public void setUo_distinta(java.lang.String uo_distinta) {
		this.uo_distinta = uo_distinta;
	}
	public java.lang.String getNatura_mandato() {
		return natura_mandato;
	}
	public void setNatura_mandato(java.lang.String natura_mandato) {
		this.natura_mandato = natura_mandato;
	}
}