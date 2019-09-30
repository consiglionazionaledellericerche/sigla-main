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
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.jada.persistency.Keyed;
public class Accertamento_modificaBase extends Accertamento_modificaKey implements Keyed {
//    DS_MODIFICA VARCHAR(300)
	private java.lang.String ds_modifica;
 
//    DT_MODIFICA TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dt_modifica;
 
//    PG_ACCERTAMENTO DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_accertamento;
 
//    PG_VARIAZIONE DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_variazione;
 
//    ESERCIZIO_ORIGINALE DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_originale;
 
	public Accertamento_modificaBase() {
		super();
	}
	public Accertamento_modificaBase(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.Long pg_modifica) {
		super(cd_cds, esercizio, pg_modifica);
	}
	public java.lang.String getDs_modifica() {
		return ds_modifica;
	}
	public void setDs_modifica(java.lang.String ds_modifica)  {
		this.ds_modifica=ds_modifica;
	}
	public java.sql.Timestamp getDt_modifica() {
		return dt_modifica;
	}
	public void setDt_modifica(java.sql.Timestamp dt_modifica)  {
		this.dt_modifica=dt_modifica;
	}
	public java.lang.Long getPg_accertamento() {
		return pg_accertamento;
	}
	public void setPg_accertamento(java.lang.Long pg_accertamento)  {
		this.pg_accertamento=pg_accertamento;
	}
	public java.lang.Long getPg_variazione() {
		return pg_variazione;
	}
	public void setPg_variazione(java.lang.Long pg_variazione)  {
		this.pg_variazione=pg_variazione;
	}
	public java.lang.Integer getEsercizio_originale() {
		return esercizio_originale;
	}
	public void setEsercizio_originale(java.lang.Integer esercizio_originale)  {
		this.esercizio_originale=esercizio_originale;
	}
}