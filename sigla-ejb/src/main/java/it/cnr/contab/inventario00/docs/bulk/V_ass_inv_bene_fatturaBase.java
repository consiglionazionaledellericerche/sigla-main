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
* Date 30/08/2005
*/
package it.cnr.contab.inventario00.docs.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
public class V_ass_inv_bene_fatturaBase extends OggettoBulk implements Persistent {
 
//    CD_CDS_FATT_PASS VARCHAR(30)
	private java.lang.String cd_cds_fatt_pass;
 
//    CD_UO_FATT_PASS VARCHAR(30)
	private java.lang.String cd_uo_fatt_pass;
 
//    ESERCIZIO_FATT_PASS DECIMAL(4,0)
	private java.lang.Integer esercizio_fatt_pass;
 
//    PG_FATTURA_PASSIVA DECIMAL(10,0)
	private java.lang.Long pg_fattura_passiva;
 
//    PROGRESSIVO_RIGA_FATT_PASS DECIMAL(10,0)
	private java.lang.Long progressivo_riga_fatt_pass;
 
//    PG_INVENTARIO DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_inventario;
 
//    NR_INVENTARIO DECIMAL(10,0) NOT NULL
	private java.lang.Long nr_inventario;
 
//    PROGRESSIVO DECIMAL(10,0) NOT NULL
	private java.lang.Long progressivo;
 
//    DS_BENE VARCHAR(100)
	private java.lang.String ds_bene;
 
//    DS_FATTURA VARCHAR(200)
	private java.lang.String ds_fattura;
 
//    CD_TERZO VARCHAR(8)
	private java.lang.String cd_terzo;
 
//    DS_TERZO VARCHAR(100)
	private java.lang.String ds_terzo;

//	ESERCIZIO DECIMAL(4,0) 
	private java.lang.Integer esercizio;

// TI_DOCUMENTO CHAR(1) 
 	private java.lang.String ti_documento;
 
// PG_BUONO_C_S DECIMAL(10,0)
	private java.lang.Long pg_buono_c_s;
	
	private String cd_tipo_documento_amm;
	
	private String tipo;
	
		
	public V_ass_inv_bene_fatturaBase() {
		super();
	}
	public java.lang.String getCd_cds_fatt_pass () {
		return cd_cds_fatt_pass;
	}
	public void setCd_cds_fatt_pass(java.lang.String cd_cds_fatt_pass)  {
		this.cd_cds_fatt_pass=cd_cds_fatt_pass;
	}
	public java.lang.String getCd_uo_fatt_pass () {
		return cd_uo_fatt_pass;
	}
	public void setCd_uo_fatt_pass(java.lang.String cd_uo_fatt_pass)  {
		this.cd_uo_fatt_pass=cd_uo_fatt_pass;
	}
	public java.lang.Integer getEsercizio_fatt_pass () {
		return esercizio_fatt_pass;
	}
	public void setEsercizio_fatt_pass(java.lang.Integer esercizio_fatt_pass)  {
		this.esercizio_fatt_pass=esercizio_fatt_pass;
	}
	public java.lang.Long getPg_fattura_passiva () {
		return pg_fattura_passiva;
	}
	public void setPg_fattura_passiva(java.lang.Long pg_fattura_passiva)  {
		this.pg_fattura_passiva=pg_fattura_passiva;
	}
	public java.lang.Long getProgressivo_riga_fatt_pass () {
		return progressivo_riga_fatt_pass;
	}
	public void setProgressivo_riga_fatt_pass(java.lang.Long progressivo_riga_fatt_pass)  {
		this.progressivo_riga_fatt_pass=progressivo_riga_fatt_pass;
	}
	public java.lang.Long getPg_inventario () {
		return pg_inventario;
	}
	public void setPg_inventario(java.lang.Long pg_inventario)  {
		this.pg_inventario=pg_inventario;
	}
	public java.lang.Long getNr_inventario () {
		return nr_inventario;
	}
	public void setNr_inventario(java.lang.Long nr_inventario)  {
		this.nr_inventario=nr_inventario;
	}
	public java.lang.Long getProgressivo () {
		return progressivo;
	}
	public void setProgressivo(java.lang.Long progressivo)  {
		this.progressivo=progressivo;
	}
	public java.lang.String getDs_bene () {
		return ds_bene;
	}
	public void setDs_bene(java.lang.String ds_bene)  {
		this.ds_bene=ds_bene;
	}
	public java.lang.String getDs_fattura () {
		return ds_fattura;
	}
	public void setDs_fattura(java.lang.String ds_fattura)  {
		this.ds_fattura=ds_fattura;
	}
	public java.lang.String getCd_terzo () {
		return cd_terzo;
	}
	public void setCd_terzo(java.lang.String cd_terzo)  {
		this.cd_terzo=cd_terzo;
	}
	public java.lang.String getDs_terzo () {
		return ds_terzo;
	}
	public void setDs_terzo(java.lang.String ds_terzo)  {
		this.ds_terzo=ds_terzo;
	}	
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public java.lang.Long getPg_buono_c_s() {
		return pg_buono_c_s;
	}
	public java.lang.String getTi_documento() {
		return ti_documento;
	}
	public void setEsercizio(java.lang.Integer integer) {
		esercizio = integer;
	}
	public void setPg_buono_c_s(java.lang.Long long1) {
		pg_buono_c_s = long1;
	}
	public void setTi_documento(java.lang.String string) {
		ti_documento = string;
	}
	public String getCd_tipo_documento_amm() {
		return cd_tipo_documento_amm;
	}
	public void setCd_tipo_documento_amm(String cd_tipo_documento_amm) {
		this.cd_tipo_documento_amm = cd_tipo_documento_amm;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}