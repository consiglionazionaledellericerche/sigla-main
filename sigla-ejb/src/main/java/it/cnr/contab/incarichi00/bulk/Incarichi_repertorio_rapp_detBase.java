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

package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Incarichi_repertorio_rapp_detBase extends Incarichi_repertorio_rapp_detKey implements Keyed {
 
	//  CONFERENTE_RAPPORTO VARCHAR2(200 BYTE) NOT NULL
	private java.lang.String conferente_rapporto;

	//	NATURA_RAPPORTO CHAR(3 BYTE)  NOT NULL
	private java.lang.String natura_rapporto;

	//  DT_INI_RAPPORTO TIMESTAMP(7)  NOT NULL
	private java.sql.Timestamp dt_ini_rapporto;

	//  IMPORTO_RAPPORTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal importo_rapporto;

	//  STATO VARCHAR(1) NOT NULL
	private java.lang.String stato;

	//  DT_ANNULLAMENTO TIMESTAMP(7)  NOT NULL
	private java.sql.Timestamp dt_annullamento;

	public Incarichi_repertorio_rapp_detBase() {
		super();
	}
	
	public Incarichi_repertorio_rapp_detBase(java.lang.Integer esercizio, java.lang.Long pg_repertorio, java.lang.Long progressivo_riga) {
		super(esercizio, pg_repertorio, progressivo_riga);
	}
	
	public java.lang.String getConferente_rapporto() {
		return conferente_rapporto;
	}
	public void setConferente_rapporto(java.lang.String conferente_rapporto) {
		this.conferente_rapporto = conferente_rapporto;
	}
	
	public java.lang.String getNatura_rapporto() {
		return natura_rapporto;
	}
	public void setNatura_rapporto(java.lang.String natura_rapporto) {
		this.natura_rapporto = natura_rapporto;
	}
	
	public java.sql.Timestamp getDt_ini_rapporto() {
		return dt_ini_rapporto;
	}
	public void setDt_ini_rapporto(java.sql.Timestamp dt_ini_rapporto) {
		this.dt_ini_rapporto = dt_ini_rapporto;
	}
	
	public java.math.BigDecimal getImporto_rapporto() {
		return importo_rapporto;
	}
	public void setImporto_rapporto(java.math.BigDecimal importo_rapporto) {
		this.importo_rapporto = importo_rapporto;
	}

	public java.lang.String getStato() {
		return stato;
	}
	public void setStato(java.lang.String stato) {
		this.stato = stato;
	}
	
	public java.sql.Timestamp getDt_annullamento() {
		return dt_annullamento;
	}
	public void setDt_annullamento(java.sql.Timestamp dt_annullamento) {
		this.dt_annullamento = dt_annullamento;
	}
}