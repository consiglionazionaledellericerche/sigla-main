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

package it.cnr.contab.doccont00.core;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class DatiFinanziariScadenzeDTO implements Serializable, Cloneable{
	BigDecimal nuovoImportoScadenzaVecchia;
	Boolean mantieniImportoAssociatoScadenza = false;
	String nuovaDescrizione;
	Timestamp nuovaScadenza;
	String cdLineaAttivita;
	String cdVoce;
	String cdCentroResponsabilita;
	Long nuovoPgObbligazioneScadenzario;
	
	public BigDecimal getNuovoImportoScadenzaVecchia() {
		return nuovoImportoScadenzaVecchia;
	}
	public void setNuovoImportoScadenzaVecchia(BigDecimal nuovoImportoScadenzaVecchia) {
		this.nuovoImportoScadenzaVecchia = nuovoImportoScadenzaVecchia;
	}
	public String getNuovaDescrizione() {
		return nuovaDescrizione;
	}
	public void setNuovaDescrizione(String nuovaDescrizione) {
		this.nuovaDescrizione = nuovaDescrizione;
	}
	public Timestamp getNuovaScadenza() {
		return nuovaScadenza;
	}
	public void setNuovaScadenza(Timestamp nuovaScadenza) {
		this.nuovaScadenza = nuovaScadenza;
	}
	public String getCdLineaAttivita() {
		return cdLineaAttivita;
	}
	public void setCdLineaAttivita(String cdLineaAttivita) {
		this.cdLineaAttivita = cdLineaAttivita;
	}
	public String getCdVoce() {
		return cdVoce;
	}
	public void setCdVoce(String cdVoce) {
		this.cdVoce = cdVoce;
	}
	public String getCdCentroResponsabilita() {
		return cdCentroResponsabilita;
	}
	public void setCdCentroResponsabilita(String cdCentroResponsabilita) {
		this.cdCentroResponsabilita = cdCentroResponsabilita;
	}

	public Boolean getMantieniImportoAssociatoScadenza() {
		return mantieniImportoAssociatoScadenza;
	}

	public void setMantieniImportoAssociatoScadenza(Boolean mantieniImportoAssociatoScadenza) {
		this.mantieniImportoAssociatoScadenza = mantieniImportoAssociatoScadenza;
	}

	public Long getNuovoPgObbligazioneScadenzario() {
		return nuovoPgObbligazioneScadenzario;
	}

	public void setNuovoPgObbligazioneScadenzario(Long nuovoPgObbligazioneScadenzario) {
		this.nuovoPgObbligazioneScadenzario = nuovoPgObbligazioneScadenzario;
	}
}
