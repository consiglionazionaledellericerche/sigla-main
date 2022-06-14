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

package it.cnr.contab.pagopa.bulk;

import it.cnr.jada.persistency.Keyed;

public class TipoPendenzaPagopaBase extends TipoPendenzaPagopaKey implements Keyed {
	private static final long serialVersionUID = 1L;

	private String descrizione;
	private Integer auxDigit;

	private Integer divisoreCheckDigit;
	private Integer lunghezzaIuv;
	private Integer lunghezzaIuvBase;
	private Boolean applicationCode;
	private Boolean codiceSegregazione;

	public Integer getDivisoreCheckDigit() {
		return divisoreCheckDigit;
	}

	public void setDivisoreCheckDigit(Integer divisoreCheckDigit) {
		this.divisoreCheckDigit = divisoreCheckDigit;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	private Boolean iuvCheckDigit;

	private String applicationCodeDefault;

	public TipoPendenzaPagopaBase() {
		super();
	}

	public TipoPendenzaPagopaBase(Integer id) {
		super(id);
	}

	public Integer getAuxDigit() {
		return auxDigit;
	}

	public void setAuxDigit(Integer auxDigit) {
		this.auxDigit = auxDigit;
	}

	public Integer getLunghezzaIuv() {
		return lunghezzaIuv;
	}

	public void setLunghezzaIuv(Integer lunghezzaIuv) {
		this.lunghezzaIuv = lunghezzaIuv;
	}

	public Integer getLunghezzaIuvBase() {
		return lunghezzaIuvBase;
	}

	public void setLunghezzaIuvBase(Integer lunghezzaIuvBase) {
		this.lunghezzaIuvBase = lunghezzaIuvBase;
	}

	public Boolean getApplicationCode() {
		return applicationCode;
	}

	public void setApplicationCode(Boolean applicationCode) {
		this.applicationCode = applicationCode;
	}

	public Boolean getCodiceSegregazione() {
		return codiceSegregazione;
	}

	public void setCodiceSegregazione(Boolean codiceSegregazione) {
		this.codiceSegregazione = codiceSegregazione;
	}

	public Boolean getIuvCheckDigit() {
		return iuvCheckDigit;
	}

	public void setIuvCheckDigit(Boolean iuvCheckDigit) {
		this.iuvCheckDigit = iuvCheckDigit;
	}

	public String getApplicationCodeDefault() {
		return applicationCodeDefault;
	}

	public void setApplicationCodeDefault(String applicationCodeDefault) {
		this.applicationCodeDefault = applicationCodeDefault;
	}
}
