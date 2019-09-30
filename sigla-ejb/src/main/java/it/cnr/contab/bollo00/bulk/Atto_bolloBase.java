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

package it.cnr.contab.bollo00.bulk;

import it.cnr.jada.persistency.Keyed;

public class Atto_bolloBase extends Atto_bolloKey implements Keyed {
	private static final long serialVersionUID = 1L;

	// ESERCIZIO DECIMAL(4,0) NOT NULL 
	private java.lang.Integer esercizio;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cdUnitaOrganizzativa;
	
	// DESCRIZIONE_ATTO VARCHAR2(300 BYTE) NOT NULL
	private java.lang.String descrizioneAtto;

	// ID_TIPO_ATTO_BOLLO NUMBER NOT NULL
	private java.lang.Integer idTipoAttoBollo;

	// CD_PROVV VARCHAR(20)
	private java.lang.String cd_provv;

	// NUMERO_PROVV DECIMAL(10,0)
	private java.lang.Integer nr_provv;

	// DT_PROVV TIMESTAMP(7)
	private java.sql.Timestamp dt_provv;

	// ESERCIZIO_CONTRATTO DECIMAL(4,0) NULL
	private java.lang.Integer esercizio_contratto;

	// STATO_CONTRATTO CHAR(1) NULL
	private java.lang.String stato_contratto;

	// PG_CONTRATTO DECIMAL(10,0) NULL
	private java.lang.Long pg_contratto;

	// NUM_DETTAGLI NUMBER(6) NOT NULL
	private java.lang.Integer numDettagli;

	// NUM_RIGHE NUMBER(6)
	private java.lang.Integer numRighe;

	// NUM_REGISTRO NUMBER(6)
	private java.lang.Integer numRegistro;

	public Atto_bolloBase() {
		super();
	}

	public Atto_bolloBase(java.lang.Integer id) {
		super(id);
	}

	public java.lang.Integer getEsercizio() {
		return esercizio;
	}

	public void setEsercizio(java.lang.Integer esercizio) {
		this.esercizio = esercizio;
	}

	public java.lang.String getCdUnitaOrganizzativa() {
		return cdUnitaOrganizzativa;
	}

	public void setCdUnitaOrganizzativa(java.lang.String cdUnitaOrganizzativa) {
		this.cdUnitaOrganizzativa = cdUnitaOrganizzativa;
	}

	public java.lang.String getDescrizioneAtto() {
		return descrizioneAtto;
	}

	public void setDescrizioneAtto(java.lang.String descrizioneAtto) {
		this.descrizioneAtto = descrizioneAtto;
	}

	public java.lang.Integer getIdTipoAttoBollo() {
		return idTipoAttoBollo;
	}

	public void setIdTipoAttoBollo(java.lang.Integer idTipoAttoBollo) {
		this.idTipoAttoBollo = idTipoAttoBollo;
	}

	public java.lang.String getCd_provv() {
		return cd_provv;
	}
	
	public void setCd_provv(java.lang.String cdProvv) {
		cd_provv = cdProvv;
	}
	
	public java.lang.Integer getNr_provv() {
		return nr_provv;
	}
	
	public void setNr_provv(java.lang.Integer nrProvv) {
		nr_provv = nrProvv;
	}
	
	public java.sql.Timestamp getDt_provv() {
		return dt_provv;
	}
	
	public void setDt_provv(java.sql.Timestamp dtProvv) {
		dt_provv = dtProvv;
	}

	public java.lang.Integer getEsercizio_contratto() {
		return esercizio_contratto;
	}

	public void setEsercizio_contratto(java.lang.Integer integer) {
		esercizio_contratto = integer;
	}

	public java.lang.String getStato_contratto() {
		return stato_contratto;
	}

	public void setStato_contratto(java.lang.String string) {
		stato_contratto = string;
	}

	public java.lang.Long getPg_contratto() {
		return pg_contratto;
	}

	public void setPg_contratto(java.lang.Long long1) {
		pg_contratto = long1;
	}

	public java.lang.Integer getNumDettagli() {
		return numDettagli;
	}

	public void setNumDettagli(java.lang.Integer numDettagli) {
		this.numDettagli = numDettagli;
	}

	public java.lang.Integer getNumRighe() {
		return numRighe;
	}
	
	public void setNumRighe(java.lang.Integer numRighe) {
		this.numRighe = numRighe;
	}
	
	public java.lang.Integer getNumRegistro() {
		return numRegistro;
	}

	public void setNumRegistro(java.lang.Integer numRegistro) {
		this.numRegistro = numRegistro;
	}
}
