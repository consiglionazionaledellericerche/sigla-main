/*
* Created by Generator 1.0
* Date 09/05/2005
*/
package it.cnr.contab.config00.contratto.bulk;

import it.cnr.jada.persistency.Keyed;

public class Procedure_amministrativeBase extends Procedure_amministrativeKey implements Keyed {
//    DS_PROC_AMM VARCHAR(200) NOT NULL
	private java.lang.String ds_proc_amm;
 
//    TI_PROC_AMM VARCHAR(2) NOT NULL
	private java.lang.String ti_proc_amm;

//    FL_RICERCA_INCARICO CHAR(1) NOT NULL
	private java.lang.Boolean fl_ricerca_incarico;

//    FL_CANCELLATO CHAR(1) NOT NULL
	private java.lang.Boolean fl_cancellato;
 
	private Integer incarico_ric_giorni_pubbl;

	private Integer incarico_ric_giorni_scad;

	private java.lang.String cd_gruppo_file;

//    FL_MERAMENTE_OCCASIONALE CHAR(1) NOT NULL
	private java.lang.Boolean fl_meramente_occasionale;

	public Procedure_amministrativeBase() {
		super();
	}
	public Procedure_amministrativeBase(java.lang.String cd_proc_amm) {
		super(cd_proc_amm);
	}
	public java.lang.String getDs_proc_amm () {
		return ds_proc_amm;
	}
	public void setDs_proc_amm(java.lang.String ds_proc_amm)  {
		this.ds_proc_amm=ds_proc_amm;
	}

	public java.lang.String getTi_proc_amm() {
		return ti_proc_amm;
	}
	public void setTi_proc_amm(java.lang.String ti_proc_amm) {
		this.ti_proc_amm= ti_proc_amm;
	}

	public java.lang.Boolean getFl_cancellato () {
		return fl_cancellato;
	}
	public void setFl_cancellato(java.lang.Boolean fl_cancellato)  {
		this.fl_cancellato=fl_cancellato;
	}
	public java.lang.Boolean getFl_ricerca_incarico() {
		return fl_ricerca_incarico;
	}
	public void setFl_ricerca_incarico(java.lang.Boolean fl_ricerca_incarico) {
		this.fl_ricerca_incarico = fl_ricerca_incarico;
	}
	public Integer getIncarico_ric_giorni_pubbl() {
		return incarico_ric_giorni_pubbl;
	}
	public void setIncarico_ric_giorni_pubbl(Integer incarico_ric_giorni_pubbl) {
		this.incarico_ric_giorni_pubbl = incarico_ric_giorni_pubbl;
	}
	public Integer getIncarico_ric_giorni_scad() {
		return incarico_ric_giorni_scad;
	}
	public void setIncarico_ric_giorni_scad(Integer incarico_ric_giorni_scad) {
		this.incarico_ric_giorni_scad = incarico_ric_giorni_scad;
	}
	public void setCd_gruppo_file(java.lang.String cd_gruppo_file)  {
		this.cd_gruppo_file=cd_gruppo_file;
	}
	public java.lang.String getCd_gruppo_file() {
		return cd_gruppo_file;
	}
	public void setFl_meramente_occasionale(java.lang.Boolean fl_meramente_occasionale)  {
		this.fl_meramente_occasionale=fl_meramente_occasionale;
	}
	public java.lang.Boolean getFl_meramente_occasionale() {
		return fl_meramente_occasionale;
	}
}