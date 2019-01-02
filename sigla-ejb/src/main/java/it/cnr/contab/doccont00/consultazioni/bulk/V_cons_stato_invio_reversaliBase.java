/*
* Created by Generator 1.0
* Date 27/04/2006
*/
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Keyed;
import it.cnr.jada.persistency.Persistent;

import java.sql.Timestamp;

public class V_cons_stato_invio_reversaliBase extends OggettoBulk implements Persistent {
//    CD_CDS VARCHAR(30)
	private java.lang.String cd_cds;
 
//    ESERCIZIO DECIMAL(22,0)
	private java.lang.Long esercizio;
 
//    PG_REVERSALE DECIMAL(22,0)
	private java.lang.Long pg_reversale;
 
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30)
	private java.lang.String cd_unita_organizzativa;
 
//    TI_REVERSALE CHAR(1)
	private java.lang.String ti_reversale;
 
//    DS_REVERSALE VARCHAR(300)
	private java.lang.String ds_reversale;
 
//    STATO CHAR(1)
	private java.lang.String stato;
 
//    IM_REVERSALE DECIMAL(22,0)
	private java.math.BigDecimal im_reversale;
  
//    IM_PAGATO DECIMAL(22,0)
	private java.math.BigDecimal im_incassato;
 
//    DT_EMIS_REV TIMESTAMP(7)
	private java.sql.Timestamp dt_emis_rev;
 
//    DT_ANNULLAMENTO TIMESTAMP(7)
	private java.sql.Timestamp dt_annullamento;
	
	private java.sql.Timestamp dt_incasso;
 
	//    PG_DISTINTA DECIMAL(22,0)
	private java.lang.Long pg_distinta;

//  PG_DISTINTA_DEF DECIMAL(22,0)
	private java.lang.Long pg_distinta_def;
 
//    DT_EMIS_DIS TIMESTAMP(7)
	private java.sql.Timestamp dt_emis_dis;
 
//    DT_INVIO_DIS TIMESTAMP(7)
	private java.sql.Timestamp dt_invio_dis;

	// ESITO_OPERAZIONE VARCHAR2(30)
	private java.lang.String esitoOperazione;

	// DT_ORA_ESITO_OPERAZIONE TIMESTAMP
	private java.sql.Timestamp dtOraEsitoOperazione;

	// ERRORE_SIOPE_PLUS VARCHAR2(2000)
	private java.lang.String erroreSiopePlus;
 
	public V_cons_stato_invio_reversaliBase() {
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
	public java.lang.Long getPg_reversale () {
		return pg_reversale;
	}
	public void setPg_reversale(java.lang.Long pg_reversale)  {
		this.pg_reversale=pg_reversale;
	}
	public java.lang.String getCd_unita_organizzativa () {
		return cd_unita_organizzativa;
	}
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa)  {
		this.cd_unita_organizzativa=cd_unita_organizzativa;
	}
	public java.lang.String getTi_reversale () {
		return ti_reversale;
	}
	public void setTi_reversale(java.lang.String ti_reversale)  {
		this.ti_reversale=ti_reversale;
	}
	public java.lang.String getDs_reversale () {
		return ds_reversale;
	}
	public void setDs_reversale(java.lang.String ds_reversale)  {
		this.ds_reversale=ds_reversale;
	}
	public java.lang.String getStato () {
		return stato;
	}
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
	public java.math.BigDecimal getIm_reversale () {
		return im_reversale;
	}
	public void setIm_reversale(java.math.BigDecimal im_reversale)  {
		this.im_reversale=im_reversale;
	}
	public java.math.BigDecimal getIm_incassato () {
		return im_incassato;
	}
	public void setIm_incassato(java.math.BigDecimal im_incassato)  {
		this.im_incassato=im_incassato;
	}
	public java.sql.Timestamp getDt_emis_rev () {
		return dt_emis_rev;
	}
	public void setDt_emis_rev(java.sql.Timestamp dt_emis_rev)  {
		this.dt_emis_rev=dt_emis_rev;
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
	public java.sql.Timestamp getDt_incasso() {
		return dt_incasso;
	}
	public void setDt_incasso(java.sql.Timestamp dt_incasso) {
		this.dt_incasso = dt_incasso;
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