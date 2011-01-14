/*
* Created by Generator 1.0
* Date 04/11/2005
*/
package it.cnr.contab.config00.sto.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Keyed;
import it.cnr.jada.persistency.Persistent;
public class V_struttura_organizzativaBulk extends OggettoBulk implements Persistent {

//    ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;
 
//    CD_ROOT VARCHAR(30)
	private java.lang.String cd_root;
 
//    CD_CDS VARCHAR(30)
	private java.lang.String cd_cds;
 
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30)
	private java.lang.String cd_unita_organizzativa;
 
//    CD_CENTRO_RESPONSABILITA VARCHAR(30)
	private java.lang.String cd_centro_responsabilita;
 
//    CD_CDR_AFFERENZA VARCHAR(30)
	private java.lang.String cd_cdr_afferenza;
 
//    CD_TIPO_UNITA VARCHAR(20)
	private java.lang.String cd_tipo_unita;
 
//    CD_TIPO_LIVELLO VARCHAR(3)
	private java.lang.String cd_tipo_livello;
 
//    FL_UO_CDS CHAR(1)
	private java.lang.Boolean fl_uo_cds;
 
//    FL_CDR_UO VARCHAR(1)
	private java.lang.String fl_cdr_uo;
 
	public V_struttura_organizzativaBulk() {
		super();
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.String getCd_root () {
		return cd_root;
	}
	public void setCd_root(java.lang.String cd_root)  {
		this.cd_root=cd_root;
	}
	public java.lang.String getCd_cds () {
		return cd_cds;
	}
	public void setCd_cds(java.lang.String cd_cds)  {
		this.cd_cds=cd_cds;
	}
	public java.lang.String getCd_unita_organizzativa () {
		return cd_unita_organizzativa;
	}
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa)  {
		this.cd_unita_organizzativa=cd_unita_organizzativa;
	}
	public java.lang.String getCd_centro_responsabilita () {
		return cd_centro_responsabilita;
	}
	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita)  {
		this.cd_centro_responsabilita=cd_centro_responsabilita;
	}
	public java.lang.String getCd_cdr_afferenza () {
		return cd_cdr_afferenza;
	}
	public void setCd_cdr_afferenza(java.lang.String cd_cdr_afferenza)  {
		this.cd_cdr_afferenza=cd_cdr_afferenza;
	}
	public java.lang.String getCd_tipo_unita () {
		return cd_tipo_unita;
	}
	public void setCd_tipo_unita(java.lang.String cd_tipo_unita)  {
		this.cd_tipo_unita=cd_tipo_unita;
	}
	public java.lang.String getCd_tipo_livello () {
		return cd_tipo_livello;
	}
	public void setCd_tipo_livello(java.lang.String cd_tipo_livello)  {
		this.cd_tipo_livello=cd_tipo_livello;
	}
	public java.lang.Boolean getFl_uo_cds () {
		return fl_uo_cds;
	}
	public void setFl_uo_cds(java.lang.Boolean fl_uo_cds)  {
		this.fl_uo_cds=fl_uo_cds;
	}
	public java.lang.String getFl_cdr_uo () {
		return fl_cdr_uo;
	}
	public void setFl_cdr_uo(java.lang.String fl_cdr_uo)  {
		this.fl_cdr_uo=fl_cdr_uo;
	}
}