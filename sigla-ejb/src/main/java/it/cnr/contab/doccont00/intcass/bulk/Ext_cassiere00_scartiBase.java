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
 * Date 18/01/2008
 */
package it.cnr.contab.doccont00.intcass.bulk;
import it.cnr.jada.persistency.Keyed;
public class Ext_cassiere00_scartiBase extends Ext_cassiere00_scartiKey implements Keyed {
//    DT_GIORNALIERA TIMESTAMP(7)
	private java.sql.Timestamp dt_giornaliera;
 
//    DT_ELABORAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dt_elaborazione;
 
//    DT_ESECUZIONE TIMESTAMP(7)
	private java.sql.Timestamp dt_esecuzione;
 
//    CD_CDS VARCHAR(30)
	private java.lang.String cd_cds;
 
//    CD_CDS_SR VARCHAR(30)
	private java.lang.String cd_cds_sr;
 
//    ESERCIZIO_SR DECIMAL(4,0)
	private java.lang.Integer esercizio_sr;
 
//    TI_ENTRATA_SPESA_SR CHAR(1)
	private java.lang.String ti_entrata_spesa_sr;
 
//    TI_SOSPESO_RISCONTRO_SR CHAR(1)
	private java.lang.String ti_sospeso_riscontro_sr;
 
//    CD_SR VARCHAR(24)
	private java.lang.String cd_sr;
 
//    TIPO_MOV VARCHAR(1)
	private java.lang.String tipo_mov;
 
//    CD_CDS_MANREV VARCHAR(3)
	private java.lang.String cd_cds_manrev;
 
//    ESERCIZIO_MANREV DECIMAL(4,0)
	private java.lang.Integer esercizio_manrev;
 
//    PG_MANREV VARCHAR(50)
	private java.lang.String pg_manrev;
 
//    ANOMALIA VARCHAR(1000)
	private java.lang.String anomalia;
 
	public Ext_cassiere00_scartiBase() {
		super();
	}
	public Ext_cassiere00_scartiBase(java.lang.Integer esercizio, java.lang.String nome_file, java.lang.Long pg_esecuzione, java.lang.Long pg_rec) {
		super(esercizio, nome_file, pg_esecuzione, pg_rec);
	}
	public java.sql.Timestamp getDt_giornaliera() {
		return dt_giornaliera;
	}
	public void setDt_giornaliera(java.sql.Timestamp dt_giornaliera)  {
		this.dt_giornaliera=dt_giornaliera;
	}
	public java.sql.Timestamp getDt_elaborazione() {
		return dt_elaborazione;
	}
	public void setDt_elaborazione(java.sql.Timestamp dt_elaborazione)  {
		this.dt_elaborazione=dt_elaborazione;
	}
	public java.sql.Timestamp getDt_esecuzione() {
		return dt_esecuzione;
	}
	public void setDt_esecuzione(java.sql.Timestamp dt_esecuzione)  {
		this.dt_esecuzione=dt_esecuzione;
	}
	public java.lang.String getCd_cds() {
		return cd_cds;
	}
	public void setCd_cds(java.lang.String cd_cds)  {
		this.cd_cds=cd_cds;
	}
	public java.lang.String getCd_cds_sr() {
		return cd_cds_sr;
	}
	public void setCd_cds_sr(java.lang.String cd_cds_sr)  {
		this.cd_cds_sr=cd_cds_sr;
	}
	public java.lang.Integer getEsercizio_sr() {
		return esercizio_sr;
	}
	public void setEsercizio_sr(java.lang.Integer esercizio_sr)  {
		this.esercizio_sr=esercizio_sr;
	}
	public java.lang.String getTi_entrata_spesa_sr() {
		return ti_entrata_spesa_sr;
	}
	public void setTi_entrata_spesa_sr(java.lang.String ti_entrata_spesa_sr)  {
		this.ti_entrata_spesa_sr=ti_entrata_spesa_sr;
	}
	public java.lang.String getTi_sospeso_riscontro_sr() {
		return ti_sospeso_riscontro_sr;
	}
	public void setTi_sospeso_riscontro_sr(java.lang.String ti_sospeso_riscontro_sr)  {
		this.ti_sospeso_riscontro_sr=ti_sospeso_riscontro_sr;
	}
	public java.lang.String getCd_sr() {
		return cd_sr;
	}
	public void setCd_sr(java.lang.String cd_sr)  {
		this.cd_sr=cd_sr;
	}
	public java.lang.String getTipo_mov() {
		return tipo_mov;
	}
	public void setTipo_mov(java.lang.String tipo_mov)  {
		this.tipo_mov=tipo_mov;
	}
	public java.lang.String getCd_cds_manrev() {
		return cd_cds_manrev;
	}
	public void setCd_cds_manrev(java.lang.String cd_cds_manrev)  {
		this.cd_cds_manrev=cd_cds_manrev;
	}
	public java.lang.Integer getEsercizio_manrev() {
		return esercizio_manrev;
	}
	public void setEsercizio_manrev(java.lang.Integer esercizio_manrev)  {
		this.esercizio_manrev=esercizio_manrev;
	}
	public java.lang.String getPg_manrev() {
		return pg_manrev;
	}
	public void setPg_manrev(java.lang.String pg_manrev)  {
		this.pg_manrev=pg_manrev;
	}
	public java.lang.String getAnomalia() {
		return anomalia;
	}
	public void setAnomalia(java.lang.String anomalia)  {
		this.anomalia=anomalia;
	}
}