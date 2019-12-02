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
 * Date 27/09/2006
 */
package it.cnr.contab.pdg00.cdip.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
public class Stipendi_cofi_cori_dettBase extends OggettoBulk implements Persistent {
//    ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;
 
//    MESE DECIMAL(2,0) NOT NULL
	private java.lang.Integer mese;
 
//    TIPO_FLUSSO VARCHAR(1) NOT NULL
	private java.lang.String tipo_flusso;
 
//    CD_CONTRIBUTO_RITENUTA VARCHAR(10) NOT NULL
	private java.lang.String cd_contributo_ritenuta;
 
//    TI_ENTE_PERCIPIENTE CHAR(1) NOT NULL
	private java.lang.String ti_ente_percipiente;
 
//    AMMONTARE DECIMAL(15,2)
	private java.math.BigDecimal ammontare;
 
//    DT_DA_COMPETENZA_COGE TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dt_da_competenza_coge;
 
//    DT_A_COMPETENZA_COGE TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dt_a_competenza_coge;
 
//  saldo DECIMAL(15,2)
	private java.math.BigDecimal saldo;
	
//  CD_CATASTALE VARCHAR(10) NOT NULL
	private java.lang.String cd_catastale;
	
	public Stipendi_cofi_cori_dettBase() {
		super();
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getMese() {
		return mese;
	}
	public void setMese(java.lang.Integer mese)  {
		this.mese=mese;
	}
	public java.lang.String getTipo_flusso() {
		return tipo_flusso;
	}
	public void setTipo_flusso(java.lang.String tipo_flusso)  {
		this.tipo_flusso=tipo_flusso;
	}
	public java.lang.String getCd_contributo_ritenuta() {
		return cd_contributo_ritenuta;
	}
	public void setCd_contributo_ritenuta(java.lang.String cd_contributo_ritenuta)  {
		this.cd_contributo_ritenuta=cd_contributo_ritenuta;
	}
	public java.lang.String getTi_ente_percipiente() {
		return ti_ente_percipiente;
	}
	public void setTi_ente_percipiente(java.lang.String ti_ente_percipiente)  {
		this.ti_ente_percipiente=ti_ente_percipiente;
	}
	public java.math.BigDecimal getAmmontare() {
		return ammontare;
	}
	public void setAmmontare(java.math.BigDecimal ammontare)  {
		this.ammontare=ammontare;
	}
	public java.sql.Timestamp getDt_da_competenza_coge() {
		return dt_da_competenza_coge;
	}
	public void setDt_da_competenza_coge(java.sql.Timestamp dt_da_competenza_coge)  {
		this.dt_da_competenza_coge=dt_da_competenza_coge;
	}
	public java.sql.Timestamp getDt_a_competenza_coge() {
		return dt_a_competenza_coge;
	}
	public void setDt_a_competenza_coge(java.sql.Timestamp dt_a_competenza_coge)  {
		this.dt_a_competenza_coge=dt_a_competenza_coge;
	}
	public java.lang.String getCd_catastale() {
		return cd_catastale;
	}
	public void setCd_catastale(java.lang.String cd_catastale) {
		this.cd_catastale = cd_catastale;
	}
	public java.math.BigDecimal getSaldo() {
		return saldo;
	}
	public void setSaldo(java.math.BigDecimal saldo) {
		this.saldo = saldo;
	}
}