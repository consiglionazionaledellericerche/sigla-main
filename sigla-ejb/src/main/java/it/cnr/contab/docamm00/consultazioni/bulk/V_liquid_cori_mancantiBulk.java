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
* Date 16/06/2005
*/
package it.cnr.contab.docamm00.consultazioni.bulk;
import java.math.BigDecimal;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;
public class V_liquid_cori_mancantiBulk extends OggettoBulk implements Persistent {
//	CD_CDS VARCHAR(30) NOT NULL
  private java.lang.String cd_cds;

//	CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
  private java.lang.String cd_unita_organizzativa;

//	CD_GRUPPO_CR VARCHAR(10)
	private java.lang.String cd_gruppo_cr;
 
//	CD_REGIONE VARCHAR(10)
	private java.lang.String cd_regione;
 
//	PG_COMUNE DECIMAL(10,0)
	private java.lang.Integer pg_comune;

//	ESERCIZIO_COMPENSO DECIMAL(4,0)
	private java.lang.Integer esercizio_compenso;
	
//	PG_COMPENSO DECIMAL(10,0)
	private java.lang.Integer pg_compenso;		

//	CD_CONTRIBUTO_RITENUTA VARCHAR(10)
	private java.lang.String cd_contributo_ritenuta;
	
//  DT_EMISSIONE_MANDATO	
	private java.sql.Timestamp dt_mandato; 

//	DT_DA	
	private java.sql.Timestamp dt_da;
	
//	DT_A	
	private java.sql.Timestamp dt_a;	
	
	public V_liquid_cori_mancantiBulk() {
		super();
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
	public java.lang.String getCd_gruppo_cr() {
		return cd_gruppo_cr;
	}
	public void setCd_gruppo_cr(java.lang.String string) {
		cd_gruppo_cr = string;
	}
	public java.lang.String getCd_regione() {
		return cd_regione;
	}
	public void setCd_regione(java.lang.String string) {
		cd_regione = string;
	}
	public java.lang.Integer getPg_comune() {
		return pg_comune;
	}
	public void setPg_comune(java.lang.Integer int1) {
		pg_comune = int1;
	}
	public java.sql.Timestamp getDt_mandato() {
		return dt_mandato;
	}
	public void setDt_mandato(java.sql.Timestamp timestamp) {
		dt_mandato = timestamp;
	}
	public java.lang.String getCd_contributo_ritenuta() {
		return cd_contributo_ritenuta;
	}
	public java.lang.Integer getEsercizio_compenso() {
		return esercizio_compenso;
	}
	public java.lang.Integer getPg_compenso() {
		return pg_compenso;
	}
	public void setCd_contributo_ritenuta(java.lang.String string) {
		cd_contributo_ritenuta = string;
	}
	public void setEsercizio_compenso(java.lang.Integer int1) {
		esercizio_compenso = int1;
	}
	public void setPg_compenso(java.lang.Integer int1) {
		pg_compenso = int1;
	}
	public java.sql.Timestamp getDt_a() {
		return dt_a;
	}
	public java.sql.Timestamp getDt_da() {
		return dt_da;
	}
	public void setDt_a(java.sql.Timestamp timestamp) {
		dt_a = timestamp;
	}
	public void setDt_da(java.sql.Timestamp timestamp) {
		dt_da = timestamp;
	}
}