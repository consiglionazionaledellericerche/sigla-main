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
package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class V_saldi_voce_progettoKey extends OggettoBulk implements KeyedPersistent
{
	// PG_PROGETTO NUMBER (10) NOT NULL (PK)
	private java.lang.Integer pg_progetto;

	// ESERCIZIO NUMBER (4) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// ESERCIZIO_VOCE NUMBER (4) NOT NULL (PK)
	private java.lang.Integer esercizio_voce;

	// TI_APPARTENENZA CHAR(1) NOT NULL (PK)
	private java.lang.String ti_appartenenza;

	// TI_GESTIONE CHAR(1) NOT NULL (PK)
	private java.lang.String ti_gestione;
	
	// CD_ELEMENTO_VOCE VARCHAR2(20) NOT NULL (PK)
	private java.lang.String cd_elemento_voce;
	
	public V_saldi_voce_progettoKey() {
		super();
	}

	public java.lang.Integer getPg_progetto() {
		return pg_progetto;
	}

	public void setPg_progetto(java.lang.Integer pg_progetto) {
		this.pg_progetto = pg_progetto;
	}

	public java.lang.Integer getEsercizio() {
		return esercizio;
	}

	public void setEsercizio(java.lang.Integer esercizio) {
		this.esercizio = esercizio;
	}

	public java.lang.Integer getEsercizio_voce() {
		return esercizio_voce;
	}

	public void setEsercizio_voce(java.lang.Integer esercizio_voce) {
		this.esercizio_voce = esercizio_voce;
	}

	public java.lang.String getTi_appartenenza() {
		return ti_appartenenza;
	}

	public void setTi_appartenenza(java.lang.String ti_appartenenza) {
		this.ti_appartenenza = ti_appartenenza;
	}

	public java.lang.String getTi_gestione() {
		return ti_gestione;
	}

	public void setTi_gestione(java.lang.String ti_gestione) {
		this.ti_gestione = ti_gestione;
	}

	public java.lang.String getCd_elemento_voce() {
		return cd_elemento_voce;
	}

	public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
		this.cd_elemento_voce = cd_elemento_voce;
	}
}