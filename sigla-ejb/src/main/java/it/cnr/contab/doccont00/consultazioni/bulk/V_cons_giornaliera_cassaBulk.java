/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 17/03/2009
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
public class V_cons_giornaliera_cassaBulk extends OggettoBulk implements Persistent { 
	public V_cons_giornaliera_cassaBulk() {
		super();
	}
//  ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;
 
//    NOME_FILE VARCHAR(20)
	private java.lang.String nome_file;
 
//    PG_REC DECIMAL(10,0)
	private java.lang.Long pg_rec;
 
//    TR VARCHAR(2)
	private java.lang.String tr;
 
//    DATA_GIORNALIERA TIMESTAMP(7)
	private java.sql.Timestamp data_giornaliera;
 
//    DATA_MOVIMENTO TIMESTAMP(7)
	private java.sql.Timestamp data_movimento;
 
//    CD_CDS VARCHAR(30)
	private java.lang.String cd_cds;
 
//    PG_MANDATO DECIMAL(22,0)
	private java.lang.String pg_mandato;
 
//    PG_REVERSALE DECIMAL(22,0)
	private java.lang.String pg_reversale;
 
//    CD_SOSPESO_E VARCHAR(54)
	private java.lang.String cd_sospeso_e;
 
//    CD_SOSPESO_S VARCHAR(54)
	private java.lang.String cd_sospeso_s;
 
//    IM_SOS_E_APERTI 
	 private java.math.BigDecimal im_sos_e_aperti;
 
//    IM_SOS_E_STORNI 
	 private java.math.BigDecimal im_sos_e_storni;
 
//    IM_REV_SOSPESI
	 private java.math.BigDecimal im_rev_sospesi;
 
//    IM_REVERSALI 
	 private java.math.BigDecimal im_reversali;
 
//    IM_REV_STORNI 
	 private java.math.BigDecimal im_rev_storni;
 
//    IM_SOS_S_APERTI
	 private java.math.BigDecimal im_sos_s_aperti;
 
//    IM_SOS_S_STORNI 
	 private java.math.BigDecimal im_sos_s_storni;
 
//    IM_MANDATI 
	 private java.math.BigDecimal im_mandati;
 
//    IM_MAN_STORNI 
	 private java.math.BigDecimal im_man_storni;
 
//   IM_MAN_SOSPESI 
	 private java.math.BigDecimal im_man_sospesi;
	 
	 //tot_sbilancio
	 private java.math.BigDecimal tot_sbilancio;
	 
	 private java.sql.Timestamp data_inizio_rif;
	 
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.String getNome_file() {
		return nome_file;
	}
	public void setNome_file(java.lang.String nome_file)  {
		this.nome_file=nome_file;
	}
	public java.lang.Long getPg_rec() {
		return pg_rec;
	}
	public void setPg_rec(java.lang.Long pg_rec)  {
		this.pg_rec=pg_rec;
	}
	public java.lang.String getTr() {
		return tr;
	}
	public void setTr(java.lang.String tr)  {
		this.tr=tr;
	}
	public java.sql.Timestamp getData_giornaliera() {
		return data_giornaliera;
	}
	public void setData_giornaliera(java.sql.Timestamp data_giornaliera)  {
		this.data_giornaliera=data_giornaliera;
	}
	public java.sql.Timestamp getData_movimento() {
		return data_movimento;
	}
	public void setData_movimento(java.sql.Timestamp data_movimento)  {
		this.data_movimento=data_movimento;
	}
	public java.lang.String getCd_cds() {
		return cd_cds;
	}
	public void setCd_cds(java.lang.String cd_cds)  {
		this.cd_cds=cd_cds;
	}
	public java.lang.String getPg_mandato() {
		return pg_mandato;
	}
	public void setPg_mandato(java.lang.String pg_mandato)  {
		this.pg_mandato=pg_mandato;
	}
	public java.lang.String getPg_reversale() {
		return pg_reversale;
	}
	public void setPg_reversale(java.lang.String pg_reversale)  {
		this.pg_reversale=pg_reversale;
	}
	public java.lang.String getCd_sospeso_e() {
		return cd_sospeso_e;
	}
	public void setCd_sospeso_e(java.lang.String cd_sospeso_e)  {
		this.cd_sospeso_e=cd_sospeso_e;
	}
	public java.lang.String getCd_sospeso_s() {
		return cd_sospeso_s;
	}
	public void setCd_sospeso_s(java.lang.String cd_sospeso_s)  {
		this.cd_sospeso_s=cd_sospeso_s;
	}
	public java.math.BigDecimal getIm_sos_e_aperti() {
		return im_sos_e_aperti;
	}
	public void setIm_sos_e_aperti(java.math.BigDecimal im_sos_e_aperti) {
		this.im_sos_e_aperti = im_sos_e_aperti;
	}
	public java.math.BigDecimal getIm_sos_e_storni() {
		return im_sos_e_storni;
	}
	public void setIm_sos_e_storni(java.math.BigDecimal im_sos_e_storni) {
		this.im_sos_e_storni = im_sos_e_storni;
	}
	public java.math.BigDecimal getIm_rev_sospesi() {
		return im_rev_sospesi;
	}
	public void setIm_rev_sospesi(java.math.BigDecimal im_rev_sospesi) {
		this.im_rev_sospesi = im_rev_sospesi;
	}
	public java.math.BigDecimal getIm_reversali() {
		return im_reversali;
	}
	public void setIm_reversali(java.math.BigDecimal im_reversali) {
		this.im_reversali = im_reversali;
	}
	public java.math.BigDecimal getIm_rev_storni() {
		return im_rev_storni;
	}
	public void setIm_rev_storni(java.math.BigDecimal im_rev_storni) {
		this.im_rev_storni = im_rev_storni;
	}
	public java.math.BigDecimal getIm_sos_s_aperti() {
		return im_sos_s_aperti;
	}
	public void setIm_sos_s_aperti(java.math.BigDecimal im_sos_s_aperti) {
		this.im_sos_s_aperti = im_sos_s_aperti;
	}
	public java.math.BigDecimal getIm_sos_s_storni() {
		return im_sos_s_storni;
	}
	public void setIm_sos_s_storni(java.math.BigDecimal im_sos_s_storni) {
		this.im_sos_s_storni = im_sos_s_storni;
	}
	public java.math.BigDecimal getIm_mandati() {
		return im_mandati;
	}
	public void setIm_mandati(java.math.BigDecimal im_mandati) {
		this.im_mandati = im_mandati;
	}
	public java.math.BigDecimal getIm_man_storni() {
		return im_man_storni;
	}
	public void setIm_man_storni(java.math.BigDecimal im_man_storni) {
		this.im_man_storni = im_man_storni;
	}
	public java.math.BigDecimal getIm_man_sospesi() {
		return im_man_sospesi;
	}
	public void setIm_man_sospesi(java.math.BigDecimal im_man_sospesi) {
		this.im_man_sospesi = im_man_sospesi;
	}
	public java.math.BigDecimal getTot_sbilancio() {
		return tot_sbilancio;
	}
	public void setTot_sbilancio(java.math.BigDecimal tot_sbilancio) {
		this.tot_sbilancio = tot_sbilancio;
	}
	public java.sql.Timestamp getData_inizio_rif() {
		return data_inizio_rif;
	}
	public void setData_inizio_rif(java.sql.Timestamp data_inizio_rif) {
		this.data_inizio_rif = data_inizio_rif;
	}
	
}