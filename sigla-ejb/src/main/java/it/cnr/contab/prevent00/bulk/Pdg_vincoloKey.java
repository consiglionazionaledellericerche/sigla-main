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
 * Created on Mar 17, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent00.bulk;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Pdg_vincoloKey extends OggettoBulk implements KeyedPersistent {
	private static final long serialVersionUID = 1L;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// ESERCIZIO_RES DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio_res;
	
	// CD_CENTRO_RESPONSABILITA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_centro_responsabilita;
	
	// CD_LINEA_ATTIVITA VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_linea_attivita;

	// TI_APPARTENENZA CHAR(1) NOT NULL (PK)
	private java.lang.String ti_appartenenza;

	// TI_GESTIONE CHAR(1) NOT NULL (PK)
	private java.lang.String ti_gestione;

	// CD_ELEMENTO_VOCE VARCHAR(50) NOT NULL (PK)
	private java.lang.String cd_elemento_voce;

	// PG_VINCOLO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_vincolo;

	public Pdg_vincoloKey() {
		super();
	}
	
	public Pdg_vincoloKey(java.lang.Integer esercizio,java.lang.Integer esercizio_res,java.lang.String cd_centro_responsabilita,java.lang.String cd_linea_attivita,java.lang.String ti_appartenenza,java.lang.String ti_gestione,java.lang.String cd_elemento_voce,java.lang.Long pg_vincolo) {
		super();
		this.esercizio = esercizio;
		this.esercizio_res = esercizio_res;
		this.cd_centro_responsabilita = cd_centro_responsabilita;	
		this.cd_linea_attivita = cd_linea_attivita;
		this.ti_appartenenza = ti_appartenenza;
		this.ti_gestione = ti_gestione;
		this.cd_elemento_voce = cd_elemento_voce;
		this.pg_vincolo = pg_vincolo;
	}
	
	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof Pdg_vincoloKey)) return false;
		Pdg_vincoloKey k = (Pdg_vincoloKey)o;
		if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
		if(!compareKey(getEsercizio_res(),k.getEsercizio_res())) return false;			
		if(!compareKey(getCd_centro_responsabilita(),k.getCd_centro_responsabilita())) return false;
		if(!compareKey(getCd_linea_attivita(),k.getCd_linea_attivita())) return false;
		if(!compareKey(getTi_appartenenza(),k.getTi_appartenenza())) return false;
		if(!compareKey(getTi_gestione(),k.getTi_gestione())) return false;
		if(!compareKey(getCd_elemento_voce(),k.getCd_elemento_voce())) return false;
		if(!compareKey(getPg_vincolo(),k.getPg_vincolo())) return false;
		return true;
	}

	public int primaryKeyHashCode() {
		return
			calculateKeyHashCode(getEsercizio())+
			calculateKeyHashCode(getEsercizio_res())+
			calculateKeyHashCode(getCd_centro_responsabilita())+
			calculateKeyHashCode(getCd_linea_attivita())+
			calculateKeyHashCode(getTi_appartenenza())+
			calculateKeyHashCode(getTi_gestione()) +
		    calculateKeyHashCode(getCd_elemento_voce()) +
		    calculateKeyHashCode(getPg_vincolo());
	}

	/**
	 * @return esercizio
	 */
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	
	/**
	 * @return esercizio_res
	 */ 
	public java.lang.Integer getEsercizio_res() {
		return esercizio_res;
	}

	/**
	 * @return cd_centro_responsabilita
	 */
	public java.lang.String getCd_centro_responsabilita() {
		return cd_centro_responsabilita;
	}

	/**
	 * @return cd_linea_attivita
	 */
	public java.lang.String getCd_linea_attivita() {
		return cd_linea_attivita;
	}

	/**
	 * @return ti_appartenenza
	 */
	public java.lang.String getTi_appartenenza() {
		return ti_appartenenza;
	}

	/**
	 * @return ti_gestione
	 */
	public java.lang.String getTi_gestione() {
		return ti_gestione;
	}

	/**
	 * @return cd_elemento_voce
	 */
	public java.lang.String getCd_elemento_voce() {
		return cd_elemento_voce;
	}

	/**
	 * @return pg_vincolo
	 */
	public java.lang.Long getPg_vincolo() {
		return pg_vincolo;
	}

	/**
	 * @param esercizio
	 */
	public void setEsercizio(java.lang.Integer esercizio) {
		this.esercizio = esercizio;
	}

	/**
	 * @param esercizio_res
	 */
	public void setEsercizio_res(java.lang.Integer esercizio_res) {
		this.esercizio_res = esercizio_res;
	}

	/**
	 * @param cd_centro_responsabilita
	 */
	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita) {
		this.cd_centro_responsabilita = cd_centro_responsabilita;
	}

	/**
	 * @param cd_linea_attivita
	 */
	public void setCd_linea_attivita(java.lang.String cd_linea_attivita) {
		this.cd_linea_attivita = cd_linea_attivita;
	}

	/**
	 * @param ti_appartenenza
	 */
	public void setTi_appartenenza(java.lang.String ti_appartenenza) {
		this.ti_appartenenza = ti_appartenenza;
	}

	/**
	 * @param ti_gestione
	 */
	public void setTi_gestione(java.lang.String ti_gestione) {
		this.ti_gestione = ti_gestione;
	}

	/**
	 * @param cd_elemento_voce
	 */
	public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
		this.cd_elemento_voce = cd_elemento_voce;
	}

	/**
	 * @param pg_vincolo
	 */
	public void setPg_vincolo(java.lang.Long pg_vincolo) {
		this.pg_vincolo = pg_vincolo;
	}
}
