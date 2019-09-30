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
 * Date 03/04/2008
 */
package it.cnr.contab.incarichi00.tabrif.bulk;

import it.cnr.jada.persistency.Keyed;

public class Incarichi_parametri_configBase extends Incarichi_parametri_configKey implements Keyed {
//    CD_PROC_AMM VARCHAR(5) 
	private java.lang.String cd_proc_amm;
 
//    CD_TIPO_INCARICO VARCHAR(5)
	private java.lang.String cd_tipo_incarico;
 
//    CD_TIPO_ATTIVITA VARCHAR(5)
	private java.lang.String cd_tipo_attivita;
 
//    TIPO_NATURA VARCHAR(3)
	private java.lang.String tipo_natura;
 
//    MERAMENTE_OCCASIONALE CHAR(1)
	private java.lang.String meramente_occasionale;

//    ART51 CHAR(1)
	private java.lang.String art51;

//    CD_PARAMETRI VARCHAR(5)
	private java.lang.String cd_parametri;

	public Incarichi_parametri_configBase() {
		super();
	}
	public Incarichi_parametri_configBase(java.lang.String cd_config) {
		super(cd_config);
	}

	public java.lang.String getCd_proc_amm() {
		return cd_proc_amm;
	}
	public void setCd_proc_amm(java.lang.String cd_proc_amm)  {
		this.cd_proc_amm=cd_proc_amm;
	}
	
	public java.lang.String getCd_tipo_incarico() {
		return cd_tipo_incarico;
	}
	public void setCd_tipo_incarico(java.lang.String cd_tipo_incarico)  {
		this.cd_tipo_incarico=cd_tipo_incarico;
	}
	
	public java.lang.String getCd_tipo_attivita() {
		return cd_tipo_attivita;
	}
	public void setCd_tipo_attivita(java.lang.String cd_tipo_attivita)  {
		this.cd_tipo_attivita=cd_tipo_attivita;
	}
	
	public java.lang.String getTipo_natura() {
		return tipo_natura;
	}
	public void setTipo_natura(java.lang.String tipo_natura)  {
		this.tipo_natura=tipo_natura;
	}

	public java.lang.String getMeramente_occasionale() {
		return meramente_occasionale;
	}
	public void setMeramente_occasionale(java.lang.String meramente_occasionale) {
		this.meramente_occasionale = meramente_occasionale;
	}

	public java.lang.String getArt51() {
		return art51;
	}
	public void setArt51(java.lang.String art51) {
		this.art51 = art51;
	}

	public java.lang.String getCd_parametri() {
		return cd_parametri;
	}
	public void setCd_parametri(java.lang.String cd_parametri) {
		this.cd_parametri = cd_parametri;
	}
}