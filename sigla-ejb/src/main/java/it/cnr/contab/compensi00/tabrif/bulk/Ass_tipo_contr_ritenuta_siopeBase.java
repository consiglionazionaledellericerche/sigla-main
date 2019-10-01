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
 * Date 27/09/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import it.cnr.jada.persistency.Keyed;
public class Ass_tipo_contr_ritenuta_siopeBase extends Ass_tipo_contr_ritenuta_siopeKey implements Keyed {
//    ESERCIZIO_SIOPE_S DECIMAL(4,0)
	private java.lang.Integer esercizio_siope_s;
 
//    TI_GESTIONE_SIOPE_S CHAR(1)
	private java.lang.String ti_gestione_siope_s;
 
//    CD_SIOPE_S VARCHAR(45)
	private java.lang.String cd_siope_s;
 
//    ESERCIZIO_SIOPE_E DECIMAL(4,0)
	private java.lang.Integer esercizio_siope_e;
 
//    TI_GESTIONE_SIOPE_E CHAR(1)
	private java.lang.String ti_gestione_siope_e;
 
//    CD_SIOPE_E VARCHAR(45)
	private java.lang.String cd_siope_e;
 
	public Ass_tipo_contr_ritenuta_siopeBase() {
		super();
	}
	public Ass_tipo_contr_ritenuta_siopeBase(java.lang.Integer esercizio, java.lang.String cd_contributo_ritenuta, java.sql.Timestamp dt_ini_validita) {
		super(esercizio, cd_contributo_ritenuta, dt_ini_validita);
	}
	public java.lang.Integer getEsercizio_siope_s() {
		return esercizio_siope_s;
	}
	public void setEsercizio_siope_s(java.lang.Integer esercizio_siope_s)  {
		this.esercizio_siope_s=esercizio_siope_s;
	}
	public java.lang.String getTi_gestione_siope_s() {
		return ti_gestione_siope_s;
	}
	public void setTi_gestione_siope_s(java.lang.String ti_gestione_siope_s)  {
		this.ti_gestione_siope_s=ti_gestione_siope_s;
	}
	public java.lang.String getCd_siope_s() {
		return cd_siope_s;
	}
	public void setCd_siope_s(java.lang.String cd_siope_s)  {
		this.cd_siope_s=cd_siope_s;
	}
	public java.lang.Integer getEsercizio_siope_e() {
		return esercizio_siope_e;
	}
	public void setEsercizio_siope_e(java.lang.Integer esercizio_siope_e)  {
		this.esercizio_siope_e=esercizio_siope_e;
	}
	public java.lang.String getTi_gestione_siope_e() {
		return ti_gestione_siope_e;
	}
	public void setTi_gestione_siope_e(java.lang.String ti_gestione_siope_e)  {
		this.ti_gestione_siope_e=ti_gestione_siope_e;
	}
	public java.lang.String getCd_siope_e() {
		return cd_siope_e;
	}
	public void setCd_siope_e(java.lang.String cd_siope_e)  {
		this.cd_siope_e=cd_siope_e;
	}
}