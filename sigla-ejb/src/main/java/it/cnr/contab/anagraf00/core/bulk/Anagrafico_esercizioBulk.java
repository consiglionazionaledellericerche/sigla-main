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

package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.contab.config00.pdcep.bulk.ContoBulk;
import it.cnr.contab.config00.pdcep.bulk.Voce_epBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.util.Optional;

public class Anagrafico_esercizioBulk extends Anagrafico_esercizioBase {

public Anagrafico_esercizioBulk() {
	super();
}
	private ContoBulk contoCredito = new ContoBulk();
	private ContoBulk contoDebito = new ContoBulk();

	public Anagrafico_esercizioBulk(java.lang.Integer cd_anag,java.lang.Integer esercizio) {
	super(cd_anag,esercizio);
}

	public ContoBulk getContoCredito() {
		return contoCredito;
	}

	public void setContoCredito(ContoBulk contoCredito) {
		this.contoCredito = contoCredito;
	}

	public ContoBulk getContoDebito() {
		return contoDebito;
	}

	public void setContoDebito(ContoBulk contoDebito) {
		this.contoDebito = contoDebito;
	}

	@Override
	public Integer getEsercizio_voce_credito_ep() {
		return Optional.ofNullable(this.getContoCredito()).map(Voce_epBulk::getEsercizio).orElse(null);
	}

	@Override
	public void setEsercizio_voce_credito_ep(Integer esercizio_voce_credito_ep) {
		if (!Optional.ofNullable(this.getContoCredito()).isPresent())
			this.setContoCredito(new ContoBulk());
		this.getContoCredito().setEsercizio(esercizio_voce_credito_ep);
	}

	@Override
	public String getCd_voce_credito_ep() {
		return Optional.ofNullable(this.getContoCredito()).map(Voce_epBulk::getCd_voce_ep).orElse(null);
	}

	@Override
	public void setCd_voce_credito_ep(String cd_voce_credito_ep) {
		if (!Optional.ofNullable(this.getContoCredito()).isPresent())
			this.setContoCredito(new ContoBulk());
		this.getContoCredito().setCd_voce_ep(cd_voce_credito_ep);
	}

	@Override
	public Integer getEsercizio_voce_debito_ep() {
		return Optional.ofNullable(this.getContoDebito()).map(Voce_epBulk::getEsercizio).orElse(null);
	}

	@Override
	public void setEsercizio_voce_debito_ep(Integer esercizio_voce_debito_ep) {
		if (!Optional.ofNullable(this.getContoDebito()).isPresent())
			this.setContoDebito(new ContoBulk());
		this.getContoDebito().setEsercizio(esercizio_voce_debito_ep);
	}

	@Override
	public String getCd_voce_debito_ep() {
		return Optional.ofNullable(this.getContoDebito()).map(Voce_epBulk::getCd_voce_ep).orElse(null);
	}

	@Override
	public void setCd_voce_debito_ep(String cd_voce_debito_ep) {
		if (!Optional.ofNullable(this.getContoDebito()).isPresent())
			this.setContoDebito(new ContoBulk());
		this.getContoDebito().setCd_voce_ep(cd_voce_debito_ep);
	}
}
