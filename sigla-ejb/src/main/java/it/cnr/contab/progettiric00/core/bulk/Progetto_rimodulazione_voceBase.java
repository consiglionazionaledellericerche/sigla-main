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

import it.cnr.jada.persistency.Keyed;

public class Progetto_rimodulazione_voceBase extends Progetto_rimodulazione_voceKey implements Keyed {
	// ESERCIZIO_VOCE DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_voce;

	// TI_APPARTENENZA CHAR(1) NOT NULL
	private java.lang.String ti_appartenenza;

	// TI_GESTIONE CHAR(1) NOT NULL
	private java.lang.String ti_gestione;

	// CD_ELEMENTO_VOCE VARCHAR(20) NOT NULL
	private java.lang.String cd_elemento_voce;

	// TI_OPERAZIONE CHAR(1) NOT NULL
	private java.lang.String ti_operazione;

	// IM_VAR_SPESA_FINANZIATO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imVarSpesaFinanziato;

	// IM_VAR_SPESA_COFINANZIATO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imVarSpesaCofinanziato;

	public Progetto_rimodulazione_voceBase() {
		super();
	}
	
	public Progetto_rimodulazione_voceBase(java.lang.Integer pg_progetto, java.lang.Integer pg_rimodulazione, java.lang.String cd_unita_organizzativa, java.lang.String cd_voce_piano, java.lang.Integer esercizio_piano, java.lang.Integer pg_variazione) {
		super(pg_progetto, pg_rimodulazione, cd_unita_organizzativa, cd_voce_piano, esercizio_piano, pg_variazione);
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
	
	public java.lang.String getTi_operazione() {
		return ti_operazione;
	}
	
	public void setTi_operazione(java.lang.String ti_operazione) {
		this.ti_operazione = ti_operazione;
	}
	
	public java.math.BigDecimal getImVarSpesaFinanziato() {
		return imVarSpesaFinanziato;
	}

	public void setImVarSpesaFinanziato(java.math.BigDecimal imVarSpesaFinanziato) {
		this.imVarSpesaFinanziato = imVarSpesaFinanziato;
	}

	public java.math.BigDecimal getImVarSpesaCofinanziato() {
		return imVarSpesaCofinanziato;
	}

	public void setImVarSpesaCofinanziato(java.math.BigDecimal imVarSpesaCofinanziato) {
		this.imVarSpesaCofinanziato = imVarSpesaCofinanziato;
	}
}
