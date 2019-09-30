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

package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Contributo_ritenutaBase extends Contributo_ritenutaKey implements Keyed {
	// ALIQUOTA DECIMAL(10,6)
	private java.math.BigDecimal aliquota;

	// AMMONTARE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal ammontare;

	// AMMONTARE_LORDO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal ammontare_lordo;

	// BASE_CALCOLO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal base_calcolo;

	// CD_CDS_ACCERTAMENTO VARCHAR(30)
	private java.lang.String cd_cds_accertamento;

	// CD_CDS_OBBLIGAZIONE VARCHAR(30)
	private java.lang.String cd_cds_obbligazione;

	// DT_INI_VALIDITA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_ini_validita;

	// ESERCIZIO_ACCERTAMENTO DECIMAL(4,0)
	private java.lang.Integer esercizio_accertamento;

	// ESERCIZIO_OBBLIGAZIONE DECIMAL(4,0)
	private java.lang.Integer esercizio_obbligazione;

	// IMPONIBILE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imponibile;

	// IMPONIBILE_LORDO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imponibile_lordo;

	// IM_DEDUZIONE_IRPEF DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_deduzione_irpef;

	// IM_DEDUZIONE_FAMILY DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_deduzione_family;
	
	// MONTANTE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal montante;

	// ESERCIZIO_ORI_ACCERTAMENTO DECIMAL(4,0)
	private java.lang.Integer esercizio_ori_accertamento;

	// PG_ACCERTAMENTO DECIMAL(10,0)
	private java.lang.Long pg_accertamento;

	// PG_ACCERTAMENTO_SCADENZARIO DECIMAL(10,0)
	private java.lang.Long pg_accertamento_scadenzario;

	// ESERCIZIO_ORI_OBBLIGAZIONE DECIMAL(4,0)
	private java.lang.Integer esercizio_ori_obbligazione;

	// PG_OBBLIGAZIONE DECIMAL(10,0)
	private java.lang.Long pg_obbligazione;

	// PG_OBBLIGAZIONE_SCADENZARIO DECIMAL(10,0)
	private java.lang.Long pg_obbligazione_scadenzario;

	// STATO_COFI_CR CHAR(1) NOT NULL
	private java.lang.String stato_cofi_cr;
	
	// IM_CORI_SOSPESO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_cori_sospeso;	

public Contributo_ritenutaBase() {
	super();
}
public Contributo_ritenutaBase(java.lang.String cd_cds,java.lang.String cd_contributo_ritenuta,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_compenso,java.lang.String ti_ente_percipiente) {
	super(cd_cds,cd_contributo_ritenuta,cd_unita_organizzativa,esercizio,pg_compenso,ti_ente_percipiente);
}
/* 
 * Getter dell'attributo aliquota
 */
public java.math.BigDecimal getAliquota() {
	return aliquota;
}
/* 
 * Getter dell'attributo ammontare
 */
public java.math.BigDecimal getAmmontare() {
	return ammontare;
}
/* 
 * Getter dell'attributo ammontare_lordo
 */
public java.math.BigDecimal getAmmontare_lordo() {
	return ammontare_lordo;
}
/* 
 * Getter dell'attributo base_calcolo
 */
public java.math.BigDecimal getBase_calcolo() {
	return base_calcolo;
}
/* 
 * Getter dell'attributo cd_cds_accertamento
 */
public java.lang.String getCd_cds_accertamento() {
	return cd_cds_accertamento;
}
/* 
 * Getter dell'attributo cd_cds_obbligazione
 */
public java.lang.String getCd_cds_obbligazione() {
	return cd_cds_obbligazione;
}
/* 
 * Getter dell'attributo dt_ini_validita
 */
public java.sql.Timestamp getDt_ini_validita() {
	return dt_ini_validita;
}
/* 
 * Getter dell'attributo esercizio_accertamento
 */
public java.lang.Integer getEsercizio_accertamento() {
	return esercizio_accertamento;
}
/* 
 * Getter dell'attributo esercizio_obbligazione
 */
public java.lang.Integer getEsercizio_obbligazione() {
	return esercizio_obbligazione;
}
/* 
 * Getter dell'attributo im_deduzione_irpef
 */
public java.math.BigDecimal getIm_deduzione_irpef() {
	return im_deduzione_irpef;
}
/* 
 * Getter dell'attributo imponibile
 */
public java.math.BigDecimal getImponibile() {
	return imponibile;
}
/* 
 * Getter dell'attributo imponibile_lordo
 */
public java.math.BigDecimal getImponibile_lordo() {
	return imponibile_lordo;
}
/* 
 * Getter dell'attributo montante
 */
public java.math.BigDecimal getMontante() {
	return montante;
}
/* 
 * Getter dell'attributo esercizio_ori_accertamento
 */
public java.lang.Integer getEsercizio_ori_accertamento() {
	return esercizio_ori_accertamento;
}
/* 
 * Getter dell'attributo pg_accertamento
 */
public java.lang.Long getPg_accertamento() {
	return pg_accertamento;
}
/* 
 * Getter dell'attributo pg_accertamento_scadenzario
 */
public java.lang.Long getPg_accertamento_scadenzario() {
	return pg_accertamento_scadenzario;
}
/* 
 * Getter dell'attributo esercizio_ori_obbligazione
 */
public java.lang.Integer getEsercizio_ori_obbligazione() {
	return esercizio_ori_obbligazione;
}
/* 
 * Getter dell'attributo pg_obbligazione
 */
public java.lang.Long getPg_obbligazione() {
	return pg_obbligazione;
}
/* 
 * Getter dell'attributo pg_obbligazione_scadenzario
 */
public java.lang.Long getPg_obbligazione_scadenzario() {
	return pg_obbligazione_scadenzario;
}
/* 
 * Getter dell'attributo stato_cofi_cr
 */
public java.lang.String getStato_cofi_cr() {
	return stato_cofi_cr;
}
/* 
 * Setter dell'attributo aliquota
 */
public void setAliquota(java.math.BigDecimal aliquota) {
	this.aliquota = aliquota;
}
/* 
 * Setter dell'attributo ammontare
 */
public void setAmmontare(java.math.BigDecimal ammontare) {
	this.ammontare = ammontare;
}
/* 
 * Setter dell'attributo ammontare_lordo
 */
public void setAmmontare_lordo(java.math.BigDecimal ammontare_lordo) {
	this.ammontare_lordo = ammontare_lordo;
}
/* 
 * Setter dell'attributo base_calcolo
 */
public void setBase_calcolo(java.math.BigDecimal base_calcolo) {
	this.base_calcolo = base_calcolo;
}
/* 
 * Setter dell'attributo cd_cds_accertamento
 */
public void setCd_cds_accertamento(java.lang.String cd_cds_accertamento) {
	this.cd_cds_accertamento = cd_cds_accertamento;
}
/* 
 * Setter dell'attributo cd_cds_obbligazione
 */
public void setCd_cds_obbligazione(java.lang.String cd_cds_obbligazione) {
	this.cd_cds_obbligazione = cd_cds_obbligazione;
}
/* 
 * Setter dell'attributo dt_ini_validita
 */
public void setDt_ini_validita(java.sql.Timestamp dt_ini_validita) {
	this.dt_ini_validita = dt_ini_validita;
}
/* 
 * Setter dell'attributo esercizio_accertamento
 */
public void setEsercizio_accertamento(java.lang.Integer esercizio_accertamento) {
	this.esercizio_accertamento = esercizio_accertamento;
}
/* 
 * Setter dell'attributo esercizio_obbligazione
 */
public void setEsercizio_obbligazione(java.lang.Integer esercizio_obbligazione) {
	this.esercizio_obbligazione = esercizio_obbligazione;
}
/* 
 * Setter dell'attributo im_deduzione_irpef
 */
public void setIm_deduzione_irpef(java.math.BigDecimal im_deduzione_irpef) {
	this.im_deduzione_irpef = im_deduzione_irpef;
}
/* 
 * Setter dell'attributo imponibile
 */
public void setImponibile(java.math.BigDecimal imponibile) {
	this.imponibile = imponibile;
}
/* 
 * Setter dell'attributo imponibile_lordo
 */
public void setImponibile_lordo(java.math.BigDecimal imponibile_lordo) {
	this.imponibile_lordo = imponibile_lordo;
}
/* 
 * Setter dell'attributo montante
 */
public void setMontante(java.math.BigDecimal montante) {
	this.montante = montante;
}
/* 
 * Setter dell'attributo esercizio_ori_accertamento
 */
public void setEsercizio_ori_accertamento(java.lang.Integer esercizio_ori_accertamento) {
	this.esercizio_ori_accertamento = esercizio_ori_accertamento;
}
/* 
 * Setter dell'attributo pg_accertamento
 */
public void setPg_accertamento(java.lang.Long pg_accertamento) {
	this.pg_accertamento = pg_accertamento;
}
/* 
 * Setter dell'attributo pg_accertamento_scadenzario
 */
public void setPg_accertamento_scadenzario(java.lang.Long pg_accertamento_scadenzario) {
	this.pg_accertamento_scadenzario = pg_accertamento_scadenzario;
}
/* 
 * Setter dell'attributo esercizio_ori_obbligazione
 */
public void setEsercizio_ori_obbligazione(java.lang.Integer esercizio_ori_obbligazione) {
	this.esercizio_ori_obbligazione = esercizio_ori_obbligazione;
}
/* 
 * Setter dell'attributo pg_obbligazione
 */
public void setPg_obbligazione(java.lang.Long pg_obbligazione) {
	this.pg_obbligazione = pg_obbligazione;
}
/* 
 * Setter dell'attributo pg_obbligazione_scadenzario
 */
public void setPg_obbligazione_scadenzario(java.lang.Long pg_obbligazione_scadenzario) {
	this.pg_obbligazione_scadenzario = pg_obbligazione_scadenzario;
}
/* 
 * Setter dell'attributo stato_cofi_cr
 */
public void setStato_cofi_cr(java.lang.String stato_cofi_cr) {
	this.stato_cofi_cr = stato_cofi_cr;
}
	/**
	 * @return
	 */
	public java.math.BigDecimal getIm_deduzione_family() {
		return im_deduzione_family;
	}

	/**
	 * @param decimal
	 */
	public void setIm_deduzione_family(java.math.BigDecimal decimal) {
		im_deduzione_family = decimal;
	}
	public java.math.BigDecimal getIm_cori_sospeso() {
		return im_cori_sospeso;
	}
	public void setIm_cori_sospeso(java.math.BigDecimal im_cori_sospeso) {
		this.im_cori_sospeso = im_cori_sospeso;
	}

}
