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

package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Ass_evold_evnewKey extends OggettoBulk implements KeyedPersistent {
	private static final long serialVersionUID = 1L;

	// ESERCIZIO_OLD DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio_old;

	// TI_APPARTENENZA_OLD CHAR(1) NOT NULL (PK)
	private java.lang.String ti_appartenenza_old;

	// TI_GESTIONE_OLD CHAR(1) NOT NULL (PK)
	private java.lang.String ti_gestione_old;

	// CD_ELEMENTO_VOCE_OLD VARCHAR(20) NOT NULL (PK)
	private java.lang.String cd_elemento_voce_old;

	// ESERCIZIO_NEW DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio_new;

	// TI_APPARTENENZA_NEW CHAR(1) NOT NULL (PK)
	private java.lang.String ti_appartenenza_new;

	// TI_GESTIONE_NEW CHAR(1) NOT NULL (PK)
	private java.lang.String ti_gestione_new;

	// CD_ELEMENTO_VOCE_NEW VARCHAR(20) NOT NULL (PK)
	private java.lang.String cd_elemento_voce_new;

	public Ass_evold_evnewKey() {
		super();
	}

	public java.lang.Integer getEsercizio_old() {
		return esercizio_old;
	}

	public void setEsercizio_old(java.lang.Integer esercizio_old) {
		this.esercizio_old = esercizio_old;
	}

	public java.lang.String getTi_appartenenza_old() {
		return ti_appartenenza_old;
	}

	public void setTi_appartenenza_old(java.lang.String ti_appartenenza_old) {
		this.ti_appartenenza_old = ti_appartenenza_old;
	}

	public java.lang.String getTi_gestione_old() {
		return ti_gestione_old;
	}

	public void setTi_gestione_old(java.lang.String ti_gestione_old) {
		this.ti_gestione_old = ti_gestione_old;
	}

	public java.lang.String getCd_elemento_voce_old() {
		return cd_elemento_voce_old;
	}

	public void setCd_elemento_voce_old(java.lang.String cd_elemento_voce_old) {
		this.cd_elemento_voce_old = cd_elemento_voce_old;
	}

	public java.lang.Integer getEsercizio_new() {
		return esercizio_new;
	}

	public void setEsercizio_new(java.lang.Integer esercizio_new) {
		this.esercizio_new = esercizio_new;
	}

	public java.lang.String getTi_appartenenza_new() {
		return ti_appartenenza_new;
	}

	public void setTi_appartenenza_new(java.lang.String ti_appartenenza_new) {
		this.ti_appartenenza_new = ti_appartenenza_new;
	}

	public java.lang.String getTi_gestione_new() {
		return ti_gestione_new;
	}

	public void setTi_gestione_new(java.lang.String ti_gestione_new) {
		this.ti_gestione_new = ti_gestione_new;
	}

	public java.lang.String getCd_elemento_voce_new() {
		return cd_elemento_voce_new;
	}

	public void setCd_elemento_voce_new(java.lang.String cd_elemento_voce_new) {
		this.cd_elemento_voce_new = cd_elemento_voce_new;
	}
}
