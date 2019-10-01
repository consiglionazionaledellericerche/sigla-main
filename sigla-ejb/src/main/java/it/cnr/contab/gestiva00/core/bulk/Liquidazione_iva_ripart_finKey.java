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

package it.cnr.contab.gestiva00.core.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Liquidazione_iva_ripart_finKey extends OggettoBulk implements KeyedPersistent {
	private static final long serialVersionUID = 1L;

	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_unita_organizzativa;

	// TIPO_LIQUIDAZIONE CHAR(1) NOT NULL (PK)
	private java.lang.String tipo_liquidazione;

	// DT_INIZIO TIMESTAMP NOT NULL (PK)
	private java.sql.Timestamp dt_inizio;

	// DT_FINE TIMESTAMP NOT NULL (PK)
	private java.sql.Timestamp dt_fine;

	// PG_DETTAGLIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Long pg_dettaglio;

	public Liquidazione_iva_ripart_finKey() {
		super();
	}
	
	public Liquidazione_iva_ripart_finKey(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.String cd_unita_organizzativa,java.lang.String tipo_liquidazione,java.sql.Timestamp dt_inizio,java.sql.Timestamp dt_fine, java.lang.Long pg_dettaglio) {
		super();
		this.cd_cds = cd_cds;
		this.esercizio = esercizio;
		this.cd_unita_organizzativa = cd_unita_organizzativa;
		this.tipo_liquidazione = tipo_liquidazione;
		this.dt_inizio = dt_inizio;
		this.dt_fine = dt_fine;
		this.pg_dettaglio = pg_dettaglio;
	}
	
	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof Liquidazione_iva_ripart_finKey)) return false;
		Liquidazione_iva_ripart_finKey k = (Liquidazione_iva_ripart_finKey)o;
		if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
		if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
		if(!compareKey(getCd_unita_organizzativa(),k.getCd_unita_organizzativa())) return false;
		if(!compareKey(getTipo_liquidazione(),k.getTipo_liquidazione())) return false;
		if(!compareKey(getDt_inizio(),k.getDt_inizio())) return false;
		if(!compareKey(getDt_fine(),k.getDt_fine())) return false;
		return true;
	}
	/* 
	 * Getter dell'attributo cd_cds
	 */
	public java.lang.String getCd_cds() {
		return cd_cds;
	}
	/* 
	 * Getter dell'attributo esercizio
	 */
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	/* 
	 * Getter dell'attributo cd_unita_organizzativa
	 */
	public java.lang.String getCd_unita_organizzativa() {
		return cd_unita_organizzativa;
	}
	/* 
	 * Getter dell'attributo tipo_liquidazione
	 */
	public java.lang.String getTipo_liquidazione() {
		return tipo_liquidazione;
	}
	/* 
	 * Getter dell'attributo dt_inizio
	 */
	public java.sql.Timestamp getDt_inizio() {
		return dt_inizio;
	}
	/* 
	 * Getter dell'attributo dt_fine
	 */
	public java.sql.Timestamp getDt_fine() {
		return dt_fine;
	}
	public int primaryKeyHashCode() {
		return
			calculateKeyHashCode(getCd_cds())+
			calculateKeyHashCode(getEsercizio())+
			calculateKeyHashCode(getCd_unita_organizzativa())+
			calculateKeyHashCode(getTipo_liquidazione())+
			calculateKeyHashCode(getDt_inizio())+
			calculateKeyHashCode(getDt_fine());
	}
	/* 
	 * Setter dell'attributo cd_cds
	 */
	public void setCd_cds(java.lang.String cd_cds) {
		this.cd_cds = cd_cds;
	}
	/* 
	 * Setter dell'attributo esercizio
	 */
	public void setEsercizio(java.lang.Integer esercizio) {
		this.esercizio = esercizio;
	}
	/* 
	 * Setter dell'attributo cd_unita_organizzativa
	 */
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
		this.cd_unita_organizzativa = cd_unita_organizzativa;
	}
	/* 
	 * Setter dell'attributo tipo_liquidazione
	 */
	public void setTipo_liquidazione(java.lang.String tipo_liquidazione) {
		this.tipo_liquidazione = tipo_liquidazione;
	}
	/* 
	 * Setter dell'attributo dt_inizio
	 */
	public void setDt_inizio(java.sql.Timestamp dt_inizio) {
		this.dt_inizio = dt_inizio;
	}
	/* 
	 * Setter dell'attributo dt_fine
	 */
	public void setDt_fine(java.sql.Timestamp dt_fine) {
		this.dt_fine = dt_fine;
	}
	
	public java.lang.Long getPg_dettaglio() {
		return pg_dettaglio;
	}
	
	public void setPg_dettaglio(java.lang.Long pg_dettaglio) {
		this.pg_dettaglio = pg_dettaglio;
	}
}