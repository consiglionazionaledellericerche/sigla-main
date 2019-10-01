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

package it.cnr.contab.bollo00.tabrif.bulk;

import it.cnr.jada.persistency.Keyed;

public class Tipo_atto_bolloBase extends Tipo_atto_bolloKey implements Keyed {
	private static final long serialVersionUID = 1L;

	// CODICE VARCHAR2(3 BYTE) NOT NULL
	private java.lang.String codice;

	// DESCRIZIONE VARCHAR2(1000 BYTE) NOT NULL
	private java.lang.String descrizione;

	// DESC_NORMA VARCHAR2(1000 BYTE) NOT NULL
	private java.lang.String descNorma;

	// IM_BOLLO NUMBER(15,2) NOT NULL
	private java.math.BigDecimal imBollo;

	// TIPO_CALCOLO CHAR(1) NOT NULL
	private java.lang.String tipoCalcolo;

	// LIMITE_CALCOLO NUMBER(15,2) NOT NULL
	private java.math.BigDecimal limiteCalcolo;

	// RIGHE_FOGLIO NUMBER(10) NULL
	private java.lang.Integer righeFoglio;

	// DT_INI_VALIDITA TIMESTAMP NOT NULL
	private java.sql.Timestamp dtIniValidita;

	// DT_FIN_VALIDITA TIMESTAMP NULL
	private java.sql.Timestamp dtFinValidita;

	public Tipo_atto_bolloBase() {
		super();
	}

	public Tipo_atto_bolloBase(java.lang.Integer id) {
		super(id);
	}

	public java.lang.String getCodice() {
		return codice;
	}

	public void setCodice(java.lang.String codice) {
		this.codice = codice;
	}

	public java.lang.String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(java.lang.String descrizione) {
		this.descrizione = descrizione;
	}

	public java.lang.String getDescNorma() {
		return descNorma;
	}

	public void setDescNorma(java.lang.String descNorma) {
		this.descNorma = descNorma;
	}

	public java.math.BigDecimal getImBollo() {
		return imBollo;
	}

	public void setImBollo(java.math.BigDecimal imBollo) {
		this.imBollo = imBollo;
	}

	public java.lang.String getTipoCalcolo() {
		return tipoCalcolo;
	}

	public void setTipoCalcolo(java.lang.String tipoCalcolo) {
		this.tipoCalcolo = tipoCalcolo;
	}

	public java.math.BigDecimal getLimiteCalcolo() {
		return limiteCalcolo;
	}

	public void setLimiteCalcolo(java.math.BigDecimal limiteCalcolo) {
		this.limiteCalcolo = limiteCalcolo;
	}

	public java.lang.Integer getRigheFoglio() {
		return righeFoglio;
	}

	public void setRigheFoglio(java.lang.Integer righeFoglio) {
		this.righeFoglio = righeFoglio;
	}

	public java.sql.Timestamp getDtIniValidita() {
		return dtIniValidita;
	}
	
	public void setDtIniValidita(java.sql.Timestamp dtIniValidita) {
		this.dtIniValidita = dtIniValidita;
	}
	
	public java.sql.Timestamp getDtFinValidita() {
		return dtFinValidita;
	}
	
	public void setDtFinValidita(java.sql.Timestamp dtFinValidita) {
		this.dtFinValidita = dtFinValidita;
	}
}
