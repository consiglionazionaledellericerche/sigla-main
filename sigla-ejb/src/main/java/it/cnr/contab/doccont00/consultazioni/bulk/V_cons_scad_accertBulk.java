/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 09/03/2007
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
public class V_cons_scad_accertBulk extends OggettoBulk implements Persistent {
//    CDS VARCHAR(30)
	private java.lang.String cds;
 
//    UO VARCHAR(30) NOT NULL
	private java.lang.String uo;
 
//    ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;
 
//    ESERCIZIO_ORIGINALE DECIMAL(4,0)
	private java.lang.Integer esercizio_originale;
 
//    PG_acc DECIMAL(10,0)
	private java.lang.Long pg_acc;
 
//    PG_OBBL_SCAD DECIMAL(10,0)
	private java.lang.Long pg_acc_scad;
 
//    VOCE_BILANCIO VARCHAR(20) NOT NULL
	private java.lang.String voce_bilancio;
 
//    DATA_SCAD TIMESTAMP(7)
	private java.sql.Timestamp data_scad;
 
//    DS_SCAD VARCHAR(300)
	private java.lang.String ds_scad;
 
//    IM_SCAD DECIMAL(15,2)
	private java.math.BigDecimal im_scad;
 
//    IM_ASS_DOC_AMM DECIMAL(15,2)
	private java.math.BigDecimal im_ass_doc_amm;
 
//    IMP_ASS_DOC_CONT DECIMAL(15,2)
	private java.math.BigDecimal imp_ass_doc_cont;
 
//    debitore VARCHAR(200) NOT NULL
	private java.lang.String debitore;
 
	public V_cons_scad_accertBulk() {
		super();
	}
	
	public java.lang.String getCds() {
		return cds;
	}
	public void setCds(java.lang.String cds)  {
		this.cds=cds;
	}
	public java.lang.String getUo() {
		return uo;
	}
	public void setUo(java.lang.String uo)  {
		this.uo=uo;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio_originale() {
		return esercizio_originale;
	}
	public void setEsercizio_originale(java.lang.Integer esercizio_originale)  {
		this.esercizio_originale=esercizio_originale;
	}
	public java.lang.Long getPg_acc() {
		return pg_acc;
	}
	public void setPg_acc(java.lang.Long pg_acc)  {
		this.pg_acc=pg_acc;
	}
	public java.lang.Long getPg_acc_scad() {
		return pg_acc_scad;
	}
	public void setPg_acc_scad(java.lang.Long pg_acc_scad)  {
		this.pg_acc_scad=pg_acc_scad;
	}
	public java.lang.String getVoce_bilancio() {
		return voce_bilancio;
	}
	public void setVoce_bilancio(java.lang.String voce_bilancio)  {
		this.voce_bilancio=voce_bilancio;
	}
	public java.sql.Timestamp getData_scad() {
		return data_scad;
	}
	public void setData_scad(java.sql.Timestamp data_scad)  {
		this.data_scad=data_scad;
	}
	public java.lang.String getDs_scad() {
		return ds_scad;
	}
	public void setDs_scad(java.lang.String ds_scad)  {
		this.ds_scad=ds_scad;
	}
	public java.math.BigDecimal getIm_scad() {
		return im_scad;
	}
	public void setIm_scad(java.math.BigDecimal im_scad)  {
		this.im_scad=im_scad;
	}
	public java.math.BigDecimal getIm_ass_doc_amm() {
		return im_ass_doc_amm;
	}
	public void setIm_ass_doc_amm(java.math.BigDecimal im_ass_doc_amm)  {
		this.im_ass_doc_amm=im_ass_doc_amm;
	}
	public java.math.BigDecimal getImp_ass_doc_cont() {
		return imp_ass_doc_cont;
	}
	public void setImp_ass_doc_cont(java.math.BigDecimal imp_ass_doc_cont)  {
		this.imp_ass_doc_cont=imp_ass_doc_cont;
	}
	public java.lang.String getDebitore() {
		return debitore;
	}
	public void setDebitore(java.lang.String debitore)  {
		this.debitore=debitore;
	}

	
}