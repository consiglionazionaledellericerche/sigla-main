/*
* Created by Generator 1.0
* Date 17/03/2006
*/
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.jada.util.action.CRUDBP;

import java.sql.Timestamp;
import java.util.Dictionary;

public class V_cons_stato_invio_mandatiBulk extends OggettoBulk implements Persistent, V_cons_stato_invio{
//    CD_CDS VARCHAR(30)
	private java.lang.String cd_cds;
 
//    ESERCIZIO DECIMAL(22,0)
	private java.lang.Long esercizio;
 
//    PG_MANDATO DECIMAL(22,0)
	private java.lang.Long pg_mandato;
 
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30)
	private java.lang.String cd_unita_organizzativa;
 
//    TI_MANDATO CHAR(1)
	private java.lang.String ti_mandato;
 
//    DS_MANDATO VARCHAR(300)
	private java.lang.String ds_mandato;
 
//    STATO CHAR(1)
	private java.lang.String stato;
 
//    IM_MANDATO DECIMAL(22,0)
	private java.math.BigDecimal im_mandato;
 
//    IM_RITENUTE DECIMAL(22,0)
	private java.math.BigDecimal im_ritenute;
 
//    IM_PAGATO DECIMAL(22,0)
	private java.math.BigDecimal im_pagato;
 
//  IM_NETTO DECIMAL(22,0)
	private java.math.BigDecimal im_netto;
	
//    DT_EMIS_MAN TIMESTAMP(7)
	private java.sql.Timestamp dt_emis_man;
 
//    DT_ANNULLAMENTO TIMESTAMP(7)
	private java.sql.Timestamp dt_annullamento;

//  DT_PAGAMENTO TIMESTAMP(7)
	private java.sql.Timestamp dt_pagamento;
 
//    PG_DISTINTA DECIMAL(22,0)
	private java.lang.Long pg_distinta;
	
//  PG_DISTINTA_DEF DECIMAL(22,0)
	private java.lang.Long pg_distinta_def;
 
//    DT_EMIS_DIS TIMESTAMP(7)
	private java.sql.Timestamp dt_emis_dis;
 
//    DT_INVIO_DIS TIMESTAMP(7)
	private java.sql.Timestamp dt_invio_dis;
 
	private String contabile;
	
	private java.lang.String cd_modalita_pag;

	// ESITO_OPERAZIONE VARCHAR2(30)
	private java.lang.String esitoOperazione;

	// DT_ORA_ESITO_OPERAZIONE TIMESTAMP
	private java.sql.Timestamp dtOraEsitoOperazione;

	// ERRORE_SIOPE_PLUS VARCHAR2(2000)
	private java.lang.String erroreSiopePlus;

	public final static Dictionary esito_OperazioneKeys = new OrderedHashtable();
	static {
		for (MandatoBulk.EsitoOperazione esito : MandatoBulk.EsitoOperazione.values()) {
			esito_OperazioneKeys.put(esito.value(), esito.label());
		}
	}

	public V_cons_stato_invio_mandatiBulk() {
		super();
	}
	public java.lang.String getCd_cds () {
		return cd_cds;
	}
	public void setCd_cds(java.lang.String cd_cds)  {
		this.cd_cds=cd_cds;
	}
	public java.lang.Long getEsercizio () {
		return esercizio;
	}
	public void setEsercizio(java.lang.Long esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Long getPg_mandato () {
		return pg_mandato;
	}
	public void setPg_mandato(java.lang.Long pg_mandato)  {
		this.pg_mandato=pg_mandato;
	}
	public java.lang.String getCd_unita_organizzativa () {
		return cd_unita_organizzativa;
	}
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa)  {
		this.cd_unita_organizzativa=cd_unita_organizzativa;
	}
	public java.lang.String getTi_mandato () {
		return ti_mandato;
	}
	public void setTi_mandato(java.lang.String ti_mandato)  {
		this.ti_mandato=ti_mandato;
	}
	public java.lang.String getDs_mandato () {
		return ds_mandato;
	}
	public void setDs_mandato(java.lang.String ds_mandato)  {
		this.ds_mandato=ds_mandato;
	}
	public java.lang.String getStato () {
		return stato;
	}
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
	public java.math.BigDecimal getIm_mandato () {
		return im_mandato;
	}
	public void setIm_mandato(java.math.BigDecimal im_mandato)  {
		this.im_mandato=im_mandato;
	}
	public java.math.BigDecimal getIm_ritenute () {
		return im_ritenute;
	}
	public void setIm_ritenute(java.math.BigDecimal im_ritenute)  {
		this.im_ritenute=im_ritenute;
	}
	public java.math.BigDecimal getIm_pagato () {
		return im_pagato;
	}
	public void setIm_pagato(java.math.BigDecimal im_pagato)  {
		this.im_pagato=im_pagato;
	}
	public java.math.BigDecimal getIm_netto () {
		return im_netto;
	}
	public void setIm_netto(java.math.BigDecimal im_netto)  {
		this.im_netto=im_netto;
	}
	public java.sql.Timestamp getDt_emis_man () {
		return dt_emis_man;
	}
	public void setDt_emis_man(java.sql.Timestamp dt_emis_man)  {
		this.dt_emis_man=dt_emis_man;
	}
	public java.sql.Timestamp getDt_annullamento () {
		return dt_annullamento;
	}
	public void setDt_annullamento(java.sql.Timestamp dt_annullamento)  {
		this.dt_annullamento=dt_annullamento;
	}
	public java.lang.Long getPg_distinta () {
		return pg_distinta;
	}
	public void setPg_distinta(java.lang.Long pg_distinta)  {
		this.pg_distinta=pg_distinta;
	}
	public java.lang.Long getPg_distinta_def () {
		return pg_distinta_def;
	}
	public void setPg_distinta_def(java.lang.Long pg_distinta_def)  {
		this.pg_distinta_def=pg_distinta_def;
	}
	public java.sql.Timestamp getDt_emis_dis () {
		return dt_emis_dis;
	}
	public void setDt_emis_dis(java.sql.Timestamp dt_emis_dis)  {
		this.dt_emis_dis=dt_emis_dis;
	}
	public java.sql.Timestamp getDt_invio_dis () {
		return dt_invio_dis;
	}
	public void setDt_invio_dis(java.sql.Timestamp dt_invio_dis)  {
		this.dt_invio_dis=dt_invio_dis;
	}
	public java.sql.Timestamp getDt_pagamento() {
		return dt_pagamento;
	}
	public void setDt_pagamento(java.sql.Timestamp dt_pagamento) {
		this.dt_pagamento = dt_pagamento;
	}
	public String getContabile() {
		return contabile;
	}
	public void setContabile(String contabile) {
		this.contabile = contabile;
	}
	public Long getProgressivo() {
		return getPg_mandato();
	}
	public String getTipo() {
		return Numerazione_doc_contBulk.TIPO_MAN;
	}
	public java.lang.String getCd_modalita_pag() {
		return cd_modalita_pag; 
	}
	public void setCd_modalita_pag(java.lang.String cd_modalita_pag) {
		this.cd_modalita_pag = cd_modalita_pag;
	}

	public String getEsitoOperazione() {
		return esitoOperazione;
	}

	public void setEsitoOperazione(String esitoOperazione) {
		this.esitoOperazione = esitoOperazione;
	}

	public Timestamp getDtOraEsitoOperazione() {
		return dtOraEsitoOperazione;
	}

	public void setDtOraEsitoOperazione(Timestamp dtOraEsitoOperazione) {
		this.dtOraEsitoOperazione = dtOraEsitoOperazione;
	}

	public String getErroreSiopePlus() {
		return erroreSiopePlus;
	}

	public void setErroreSiopePlus(String erroreSiopePlus) {
		this.erroreSiopePlus = erroreSiopePlus;
	}
}