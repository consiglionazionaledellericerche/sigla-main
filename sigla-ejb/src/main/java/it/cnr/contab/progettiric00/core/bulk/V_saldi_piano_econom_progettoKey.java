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
public class V_saldi_piano_econom_progettoKey extends OggettoBulk implements KeyedPersistent
{
	// PG_PROGETTO NUMBER (10) NOT NULL
	private java.lang.Integer pg_progetto;

	// ESERCIZIO NUMBER (4) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// CD_UNITA_PIANO VARCHAR(30) NOT NULL
	private java.lang.String cd_unita_piano;

	// CD_VOCE_PIANO VARCHAR(10) NOT NULL
	private java.lang.String cd_voce_piano;

	// TI_GESTIONE CHAR(1) NOT NULL
	private java.lang.String ti_gestione;
	
	public V_saldi_piano_econom_progettoKey() {
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

	public java.lang.String getCd_unita_piano() {
		return cd_unita_piano;
	}

	public void setCd_unita_piano(java.lang.String cd_unita_piano) {
		this.cd_unita_piano = cd_unita_piano;
	}

	public java.lang.String getCd_voce_piano() {
		return cd_voce_piano;
	}

	public void setCd_voce_piano(java.lang.String cd_voce_piano) {
		this.cd_voce_piano = cd_voce_piano;
	}

	public java.lang.String getTi_gestione() {
		return ti_gestione;
	}

	public void setTi_gestione(java.lang.String ti_gestione) {
		this.ti_gestione = ti_gestione;
	}
}