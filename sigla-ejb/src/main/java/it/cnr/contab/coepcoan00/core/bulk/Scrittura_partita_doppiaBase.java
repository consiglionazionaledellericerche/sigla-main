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

package it.cnr.contab.coepcoan00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.sql.Timestamp;

public class Scrittura_partita_doppiaBase extends Scrittura_partita_doppiaKey implements Keyed {
	// ATTIVA CHAR(1)
	private java.lang.String attiva;

	// CD_CAUSALE_COGE VARCHAR(50)
	private java.lang.String cd_causale_coge;

	// CD_CDS_DOCUMENTO VARCHAR(30)
	private java.lang.String cd_cds_documento;

	// CD_COMP_DOCUMENTO VARCHAR(50)
	private java.lang.String cd_comp_documento;

	// CD_DIVISA VARCHAR(20)
	private java.lang.String cd_divisa;

	// CD_TERZO DECIMAL(8,0)
	private java.lang.Integer cd_terzo;

	// CD_TIPO_DOCUMENTO VARCHAR(10)
	private java.lang.String cd_tipo_documento;

	// CD_UO_DOCUMENTO VARCHAR(30)
	private java.lang.String cd_uo_documento;

	// COSTO_PLURIENNALE VARCHAR(45)
	private java.lang.String costo_pluriennale;

	// DS_SCRITTURA VARCHAR(250)
	private java.lang.String ds_scrittura;

	// DT_CANCELLAZIONE TIMESTAMP
	private java.sql.Timestamp dt_cancellazione;

	// DT_CONTABILIZZAZIONE TIMESTAMP
	private java.sql.Timestamp dt_contabilizzazione;

	// DT_PAGAMENTO TIMESTAMP
	private java.sql.Timestamp dt_pagamento;

	// ESERCIZIO_DOCUMENTO_AMM DECIMAL(4,0)
	private java.lang.Integer esercizio_documento_amm;

	// IM_SCRITTURA DECIMAL(15,2)
	private java.math.BigDecimal im_scrittura;

	// ORIGINE_SCRITTURA VARCHAR(20)
	private java.lang.String origine_scrittura;

	// PG_ENTE DECIMAL(10,0)
	private java.lang.Long pg_ente;

	// PG_NUMERO_DOCUMENTO DECIMAL(10,0)
	private java.lang.Long pg_numero_documento;

	// PG_SCRITTURA_ANNULLATA DECIMAL(10,0)
	private java.lang.Long pg_scrittura_annullata;

	// STATO VARCHAR(1)
	private java.lang.String stato;

	// TI_SCRITTURA CHAR(1)
	private java.lang.String ti_scrittura;

	private java.lang.String tipo_liquidazione;

	private java.sql.Timestamp dt_inizio_liquid;

	private java.sql.Timestamp dt_fine_liquid;

	private java.lang.Long report_id_liquid;

	// DT_INIZIO TIMESTAMP NOT NULL (PK)

	public Scrittura_partita_doppiaBase() {
		super();
	}
	public Scrittura_partita_doppiaBase(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.Long pg_scrittura) {
		super(cd_cds,esercizio,pg_scrittura);
	}
	public Scrittura_partita_doppiaBase(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_scrittura) {
		super(cd_cds,cd_unita_organizzativa,esercizio,pg_scrittura);
	}
	/*
	 * Getter dell'attributo attiva
	 */
	public java.lang.String getAttiva() {
		return attiva;
	}
	/*
	 * Getter dell'attributo cd_causale_coge
	 */
	public java.lang.String getCd_causale_coge() {
		return cd_causale_coge;
	}
	/*
	 * Getter dell'attributo cd_cds_documento
	 */
	public java.lang.String getCd_cds_documento() {
		return cd_cds_documento;
	}
	/*
	 * Getter dell'attributo cd_comp_documento
	 */
	public java.lang.String getCd_comp_documento() {
		return cd_comp_documento;
	}
	/*
	 * Getter dell'attributo cd_divisa
	 */
	public java.lang.String getCd_divisa() {
		return cd_divisa;
	}
	/*
	 * Getter dell'attributo cd_terzo
	 */
	public java.lang.Integer getCd_terzo() {
		return cd_terzo;
	}
	/*
	 * Getter dell'attributo cd_tipo_documento
	 */
	public java.lang.String getCd_tipo_documento() {
		return cd_tipo_documento;
	}
	/*
	 * Getter dell'attributo cd_uo_documento
	 */
	public java.lang.String getCd_uo_documento() {
		return cd_uo_documento;
	}
	/*
	 * Getter dell'attributo costo_pluriennale
	 */
	public java.lang.String getCosto_pluriennale() {
		return costo_pluriennale;
	}
	/*
	 * Getter dell'attributo ds_scrittura
	 */
	public java.lang.String getDs_scrittura() {
		return ds_scrittura;
	}
	/*
	 * Getter dell'attributo dt_cancellazione
	 */
	public java.sql.Timestamp getDt_cancellazione() {
		return dt_cancellazione;
	}
	/*
	 * Getter dell'attributo dt_contabilizzazione
	 */
	public java.sql.Timestamp getDt_contabilizzazione() {
		return dt_contabilizzazione;
	}
	/*
	 * Getter dell'attributo dt_pagamento
	 */
	public java.sql.Timestamp getDt_pagamento() {
		return dt_pagamento;
	}
	/*
	 * Getter dell'attributo esercizio_documento_amm
	 */
	public java.lang.Integer getEsercizio_documento_amm() {
		return esercizio_documento_amm;
	}
	/*
	 * Getter dell'attributo im_scrittura
	 */
	public java.math.BigDecimal getIm_scrittura() {
		return im_scrittura;
	}
	/*
	 * Getter dell'attributo origine_scrittura
	 */
	public java.lang.String getOrigine_scrittura() {
		return origine_scrittura;
	}
	/*
	 * Getter dell'attributo pg_ente
	 */
	public java.lang.Long getPg_ente() {
		return pg_ente;
	}
	/*
	 * Getter dell'attributo pg_numero_documento
	 */
	public java.lang.Long getPg_numero_documento() {
		return pg_numero_documento;
	}
	/*
	 * Getter dell'attributo pg_scrittura_annullata
	 */
	public java.lang.Long getPg_scrittura_annullata() {
		return pg_scrittura_annullata;
	}
	/*
	 * Getter dell'attributo stato
	 */
	public java.lang.String getStato() {
		return stato;
	}
	/*
	 * Getter dell'attributo ti_scrittura
	 */
	public java.lang.String getTi_scrittura() {
		return ti_scrittura;
	}
	/*
	 * Setter dell'attributo attiva
	 */
	public void setAttiva(java.lang.String attiva) {
		this.attiva = attiva;
	}
	/*
	 * Setter dell'attributo cd_causale_coge
	 */
	public void setCd_causale_coge(java.lang.String cd_causale_coge) {
		this.cd_causale_coge = cd_causale_coge;
	}
	/*
	 * Setter dell'attributo cd_cds_documento
	 */
	public void setCd_cds_documento(java.lang.String cd_cds_documento) {
		this.cd_cds_documento = cd_cds_documento;
	}
	/*
	 * Setter dell'attributo cd_comp_documento
	 */
	public void setCd_comp_documento(java.lang.String cd_comp_documento) {
		this.cd_comp_documento = cd_comp_documento;
	}
	/*
	 * Setter dell'attributo cd_divisa
	 */
	public void setCd_divisa(java.lang.String cd_divisa) {
		this.cd_divisa = cd_divisa;
	}
	/*
	 * Setter dell'attributo cd_terzo
	 */
	public void setCd_terzo(java.lang.Integer cd_terzo) {
		this.cd_terzo = cd_terzo;
	}
	/*
	 * Setter dell'attributo cd_tipo_documento
	 */
	public void setCd_tipo_documento(java.lang.String cd_tipo_documento) {
		this.cd_tipo_documento = cd_tipo_documento;
	}
	/*
	 * Setter dell'attributo cd_uo_documento
	 */
	public void setCd_uo_documento(java.lang.String cd_uo_documento) {
		this.cd_uo_documento = cd_uo_documento;
	}
	/*
	 * Setter dell'attributo costo_pluriennale
	 */
	public void setCosto_pluriennale(java.lang.String costo_pluriennale) {
		this.costo_pluriennale = costo_pluriennale;
	}
	/*
	 * Setter dell'attributo ds_scrittura
	 */
	public void setDs_scrittura(java.lang.String ds_scrittura) {
		this.ds_scrittura = ds_scrittura;
	}
	/*
	 * Setter dell'attributo dt_cancellazione
	 */
	public void setDt_cancellazione(java.sql.Timestamp dt_cancellazione) {
		this.dt_cancellazione = dt_cancellazione;
	}
	/*
	 * Setter dell'attributo dt_contabilizzazione
	 */
	public void setDt_contabilizzazione(java.sql.Timestamp dt_contabilizzazione) {
		this.dt_contabilizzazione = dt_contabilizzazione;
	}
	/*
	 * Setter dell'attributo dt_pagamento
	 */
	public void setDt_pagamento(java.sql.Timestamp dt_pagamento) {
		this.dt_pagamento = dt_pagamento;
	}
	/*
	 * Setter dell'attributo esercizio_documento_amm
	 */
	public void setEsercizio_documento_amm(java.lang.Integer esercizio_documento_amm) {
		this.esercizio_documento_amm = esercizio_documento_amm;
	}
	/*
	 * Setter dell'attributo im_scrittura
	 */
	public void setIm_scrittura(java.math.BigDecimal im_scrittura) {
		this.im_scrittura = im_scrittura;
	}
	/*
	 * Setter dell'attributo origine_scrittura
	 */
	public void setOrigine_scrittura(java.lang.String origine_scrittura) {
		this.origine_scrittura = origine_scrittura;
	}
	/*
	 * Setter dell'attributo pg_ente
	 */
	public void setPg_ente(java.lang.Long pg_ente) {
		this.pg_ente = pg_ente;
	}
	/*
	 * Setter dell'attributo pg_numero_documento
	 */
	public void setPg_numero_documento(java.lang.Long pg_numero_documento) {
		this.pg_numero_documento = pg_numero_documento;
	}
	/*
	 * Setter dell'attributo pg_scrittura_annullata
	 */
	public void setPg_scrittura_annullata(java.lang.Long pg_scrittura_annullata) {
		this.pg_scrittura_annullata = pg_scrittura_annullata;
	}
	/*
	 * Setter dell'attributo stato
	 */
	public void setStato(java.lang.String stato) {
		this.stato = stato;
	}
	/*
	 * Setter dell'attributo ti_scrittura
	 */
	public void setTi_scrittura(java.lang.String ti_scrittura) {
		this.ti_scrittura = ti_scrittura;
	}

	public String getTipo_liquidazione() {
		return tipo_liquidazione;
	}

	public void setTipo_liquidazione(String tipo_liquidazione) {
		this.tipo_liquidazione = tipo_liquidazione;
	}

	public Timestamp getDt_inizio_liquid() {
		return dt_inizio_liquid;
	}

	public void setDt_inizio_liquid(Timestamp dt_inizio_liquid) {
		this.dt_inizio_liquid = dt_inizio_liquid;
	}

	public Timestamp getDt_fine_liquid() {
		return dt_fine_liquid;
	}

	public void setDt_fine_liquid(Timestamp dt_fine_liquid) {
		this.dt_fine_liquid = dt_fine_liquid;
	}

	public Long getReport_id_liquid() {
		return report_id_liquid;
	}

	public void setReport_id_liquid(Long report_id_liquid) {
		this.report_id_liquid = report_id_liquid;
	}
}
