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
 * Date 24/03/2009
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;

public class V_cons_gae_comp_res_sintesiBulk extends OggettoBulk implements Persistent {
	public V_cons_gae_comp_res_sintesiBulk() {
		super();
	}
//  ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;
 
//    CDS VARCHAR(4000)
	private java.lang.String cds;
 
//    CDR VARCHAR(30)
	private java.lang.String cdr;
 
//    DS_CDR VARCHAR(300) NOT NULL
	private java.lang.String ds_cdr;
 
//    LDA VARCHAR(10)
	private java.lang.String lda;
 
//    DS_LDA VARCHAR(300)
	private java.lang.String ds_lda;
 
//    CD_RESPONSABILE_TERZO DECIMAL(8,0)
	private java.lang.Integer cd_responsabile_terzo;
 
//    DENOMINAZIONE_SEDE VARCHAR(200) NOT NULL
	private java.lang.String denominazione_sede;
 
//    CD_ELEMENTO_VOCE VARCHAR(20)
	private java.lang.String cd_elemento_voce;
 
//    DS_ELEMENTO_VOCE VARCHAR(100)
	private java.lang.String ds_elemento_voce;
 
//    CD_CDS_OBB VARCHAR(30)
	private java.lang.String cd_cds_obb;
 
//    ESERCIZIO_OBBLIGAZIONE DECIMAL(4,0)
	private java.lang.Integer esercizio_obbligazione;
 
//    ESERCIZIO_ORIGINALE DECIMAL(4,0)
	private java.lang.Integer esercizio_originale;
 
//    PG_OBBLIGAZIONE DECIMAL(22,0)
	private java.lang.Long pg_obbligazione;
 
//    PG_OBBLIGAZIONE_SCADENZARIO DECIMAL(22,0)
	private java.lang.Long pg_obbligazione_scadenzario;
 
//    DS_SCADENZA VARCHAR(300)
	private java.lang.String ds_scadenza;
 
//    IM_IMPEGNI DECIMAL(22,0)
	private java.math.BigDecimal im_impegni;
 
//    CD_CDS_MAN VARCHAR(30)
	private java.lang.String cd_cds_man;
 
//    PG_MANDATO DECIMAL(22,0)
	private java.lang.Long pg_mandato;
 
//    DT_EMISSIONE TIMESTAMP(7)
	private java.sql.Timestamp dt_emissione;
 
//    DT_TRASMISSIONE TIMESTAMP(8)
	private java.sql.Timestamp dt_trasmissione;
 
//    DT_PAGAMENTO TIMESTAMP(7)
	private java.sql.Timestamp dt_pagamento;
 
//    DS_MANDATO VARCHAR(300)
	private java.lang.String ds_mandato;
 
//    IM_MANDATI DECIMAL(22,0)
	private java.math.BigDecimal im_mandati;
 
	
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.String getCds() {
		return cds;
	}
	public void setCds(java.lang.String cds)  {
		this.cds=cds;
	}
	public java.lang.String getCdr() {
		return cdr;
	}
	public void setCdr(java.lang.String cdr)  {
		this.cdr=cdr;
	}
	public java.lang.String getDs_cdr() {
		return ds_cdr;
	}
	public void setDs_cdr(java.lang.String ds_cdr)  {
		this.ds_cdr=ds_cdr;
	}
	public java.lang.String getLda() {
		return lda;
	}
	public void setLda(java.lang.String lda)  {
		this.lda=lda;
	}
	public java.lang.String getDs_lda() {
		return ds_lda;
	}
	public void setDs_lda(java.lang.String ds_lda)  {
		this.ds_lda=ds_lda;
	}
	public java.lang.Integer getCd_responsabile_terzo() {
		return cd_responsabile_terzo;
	}
	public void setCd_responsabile_terzo(java.lang.Integer cd_responsabile_terzo)  {
		this.cd_responsabile_terzo=cd_responsabile_terzo;
	}
	public java.lang.String getDenominazione_sede() {
		return denominazione_sede;
	}
	public void setDenominazione_sede(java.lang.String denominazione_sede)  {
		this.denominazione_sede=denominazione_sede;
	}
	public java.lang.String getCd_elemento_voce() {
		return cd_elemento_voce;
	}
	public void setCd_elemento_voce(java.lang.String cd_elemento_voce)  {
		this.cd_elemento_voce=cd_elemento_voce;
	}
	public java.lang.String getDs_elemento_voce() {
		return ds_elemento_voce;
	}
	public void setDs_elemento_voce(java.lang.String ds_elemento_voce)  {
		this.ds_elemento_voce=ds_elemento_voce;
	}
	public java.lang.String getCd_cds_obb() {
		return cd_cds_obb;
	}
	public void setCd_cds_obb(java.lang.String cd_cds_obb)  {
		this.cd_cds_obb=cd_cds_obb;
	}
	public java.lang.Integer getEsercizio_obbligazione() {
		return esercizio_obbligazione;
	}
	public void setEsercizio_obbligazione(java.lang.Integer esercizio_obbligazione)  {
		this.esercizio_obbligazione=esercizio_obbligazione;
	}
	public java.lang.Integer getEsercizio_originale() {
		return esercizio_originale;
	}
	public void setEsercizio_originale(java.lang.Integer esercizio_originale)  {
		this.esercizio_originale=esercizio_originale;
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
	public java.lang.String getDs_scadenza() {
		return ds_scadenza;
	}
	public void setDs_scadenza(java.lang.String ds_scadenza)  {
		this.ds_scadenza=ds_scadenza;
	}
	public java.math.BigDecimal getIm_impegni() {
		return im_impegni;
	}
	public void setIm_impegni(java.math.BigDecimal im_impegni)  {
		this.im_impegni=im_impegni;
	}
	public java.lang.String getCd_cds_man() {
		return cd_cds_man;
	}
	public void setCd_cds_man(java.lang.String cd_cds_man)  {
		this.cd_cds_man=cd_cds_man;
	}
	public java.lang.Long getPg_mandato() {
		return pg_mandato;
	}
	public void setPg_mandato(java.lang.Long pg_mandato)  {
		this.pg_mandato=pg_mandato;
	}
	public java.sql.Timestamp getDt_emissione() {
		return dt_emissione;
	}
	public void setDt_emissione(java.sql.Timestamp dt_emissione)  {
		this.dt_emissione=dt_emissione;
	}
	public java.sql.Timestamp getDt_trasmissione() {
		return dt_trasmissione;
	}
	public void setDt_trasmissione(java.sql.Timestamp dt_trasmissione)  {
		this.dt_trasmissione=dt_trasmissione;
	}
	public java.sql.Timestamp getDt_pagamento() {
		return dt_pagamento;
	}
	public void setDt_pagamento(java.sql.Timestamp dt_pagamento)  {
		this.dt_pagamento=dt_pagamento;
	}
	public java.lang.String getDs_mandato() {
		return ds_mandato;
	}
	public void setDs_mandato(java.lang.String ds_mandato)  {
		this.ds_mandato=ds_mandato;
	}
	public java.math.BigDecimal getIm_mandati() {
		return im_mandati;
	}
	public void setIm_mandati(java.math.BigDecimal im_mandati)  {
		this.im_mandati=im_mandati;
	}
}