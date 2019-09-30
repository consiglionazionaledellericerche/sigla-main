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
* Date 07/08/2006
*/
package it.cnr.contab.pdg00.cdip.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
public class V_cnr_estrazione_coriBase extends OggettoBulk implements Persistent {
//    ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;
 
//    MESE DECIMAL(2,0)
	private java.lang.Integer mese;
 
//    DT_ESTRAZIONE TIMESTAMP
	private java.sql.Timestamp dt_estrazione;

//    ESERCIZIO_COMPENSO DECIMAL(4,0)
	private java.lang.Integer esercizio_compenso;	
	
//    CD_CONTRIBUTO_RITENUTA CHAR(10)
	private java.lang.String cd_contributo_ritenuta;

//    TOTALE_IMPONIBILE DECIMAL(15,2)
	private java.math.BigDecimal totale_imponibile;	

//    TOTALE_RITENUTA DECIMAL(15,2)
	private java.math.BigDecimal totale_ritenuta;	
	

//  TOTALE_RITENUTA_SOSPESA DECIMAL(15,2)
	private java.math.BigDecimal totale_ritenuta_sospesa;
	
	public V_cnr_estrazione_coriBase() {
		super();
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getMese () {
		return mese;
	}
	public void setMese(java.lang.Integer mese)  {
		this.mese=mese;
	}
	public java.sql.Timestamp getDt_estrazione() {
		return dt_estrazione;
	}
	public void setDt_estrazione(java.sql.Timestamp dt_estrazione) {
		this.dt_estrazione = dt_estrazione;
	}
	public java.lang.Integer getEsercizio_compenso() {
		return esercizio_compenso;
	}
	public void setEsercizio_compenso(java.lang.Integer esercizio_compenso) {
		this.esercizio_compenso = esercizio_compenso;
	}
	public java.lang.String getCd_contributo_ritenuta() {
		return cd_contributo_ritenuta;
	}
	public void setCd_contributo_ritenuta(java.lang.String cd_contributo_ritenuta) {
		this.cd_contributo_ritenuta = cd_contributo_ritenuta;
	}
	public java.math.BigDecimal getTotale_imponibile() {
		return totale_imponibile;
	}
	public void setTotale_imponibile(java.math.BigDecimal totale_imponibile) {
		this.totale_imponibile = totale_imponibile;
	}
	public java.math.BigDecimal getTotale_ritenuta() {
		return totale_ritenuta;
	}
	public void setTotale_ritenuta(java.math.BigDecimal totale_ritenuta) {
		this.totale_ritenuta = totale_ritenuta;
	}
	public java.math.BigDecimal getTotale_ritenuta_sospesa() {
		return totale_ritenuta_sospesa;
	}
	public void setTotale_ritenuta_sospesa(
			java.math.BigDecimal totale_ritenuta_sospesa) {
		this.totale_ritenuta_sospesa = totale_ritenuta_sospesa;
	}
}