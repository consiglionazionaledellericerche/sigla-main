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

package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Progetto_piano_economicoKey extends OggettoBulk implements KeyedPersistent {
	// PG_PROGETTO NUMBER (10) NOT NULL (PK)
	private java.lang.Integer pg_progetto;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cd_unita_organizzativa;

	// CD_VOCE_PIANO VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_voce_piano;

	// ESERCIZIO_PIANO NUMBER (4) NOT NULL (PK)
	private java.lang.Integer esercizio_piano;

	public Progetto_piano_economicoKey() {
		super();
	}
	
	public Progetto_piano_economicoKey(java.lang.Integer pg_progetto, java.lang.String cd_unita_organizzativa, java.lang.String cd_voce_piano, java.lang.Integer esercizio_piano) {
		super();
		this.pg_progetto = pg_progetto;
		this.cd_unita_organizzativa = cd_unita_organizzativa;
		this.cd_voce_piano = cd_voce_piano;
		this.esercizio_piano = esercizio_piano;
	}
	
	public java.lang.Integer getPg_progetto() {
		return pg_progetto;
	}
	
	public void setPg_progetto(java.lang.Integer pg_progetto) {
		this.pg_progetto = pg_progetto;
	}
	
	public java.lang.String getCd_unita_organizzativa() {
		return cd_unita_organizzativa;
	}

	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
		this.cd_unita_organizzativa = cd_unita_organizzativa;
	}

	public java.lang.String getCd_voce_piano() {
		return cd_voce_piano;
	}
	
	public void setCd_voce_piano(java.lang.String cd_voce_piano) {
		this.cd_voce_piano = cd_voce_piano;
	}
	
	public java.lang.Integer getEsercizio_piano() {
		return esercizio_piano;
	}
	
	public void setEsercizio_piano(java.lang.Integer esercizio_piano) {
		this.esercizio_piano = esercizio_piano;
	}
	
	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof Progetto_piano_economicoKey)) return false;
		Progetto_piano_economicoKey k = (Progetto_piano_economicoKey)o;
		if(!compareKey(getPg_progetto(),k.getPg_progetto())) return false;
		if(!compareKey(getCd_unita_organizzativa(),k.getCd_unita_organizzativa())) return false;
		if(!compareKey(getCd_voce_piano(),k.getCd_voce_piano())) return false;
		if(!compareKey(getEsercizio_piano(),k.getEsercizio_piano())) return false;
		return true;
	}
	
	public int primaryKeyHashCode() {
		return
			calculateKeyHashCode(getPg_progetto())+
			calculateKeyHashCode(getCd_unita_organizzativa())+
			calculateKeyHashCode(getCd_voce_piano())+
			calculateKeyHashCode(getEsercizio_piano());
	}
}
