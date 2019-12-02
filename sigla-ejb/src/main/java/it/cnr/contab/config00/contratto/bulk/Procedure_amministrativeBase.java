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
	private java.lang.String codice_anac;

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
	public java.lang.String getCodice_anac() {
		return codice_anac;
	}
	public void setCodice_anac(java.lang.String codice_anac) {
		this.codice_anac = codice_anac;
	}
}