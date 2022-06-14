/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.ordmag.ordini.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import it.cnr.contab.util.Utility;

public class ImportoOrdine implements Serializable {
    BigDecimal imponibile;
	BigDecimal importoIva;
	BigDecimal importoIvaDetraibile;
	BigDecimal importoIvaInd;
	BigDecimal imponibilePerNotaCredito;
	BigDecimal importoIvaPerNotaCredito;
	BigDecimal importoIvaDetraibilePerNotaCredito;
	BigDecimal importoIvaIndPerNotaCredito;

	public BigDecimal getImponibilePerNotaCredito() {
		return imponibilePerNotaCredito;
	}

	public void setImponibilePerNotaCredito(BigDecimal imponibilePerNotaCredito) {
		this.imponibilePerNotaCredito = imponibilePerNotaCredito;
	}

	public BigDecimal getImportoIvaPerNotaCredito() {
		return importoIvaPerNotaCredito;
	}

	public void setImportoIvaPerNotaCredito(BigDecimal importoIvaPerNotaCredito) {
		this.importoIvaPerNotaCredito = importoIvaPerNotaCredito;
	}

	public BigDecimal getImportoIvaDetraibilePerNotaCredito() {
		return importoIvaDetraibilePerNotaCredito;
	}

	public void setImportoIvaDetraibilePerNotaCredito(BigDecimal importoIvaDetraibilePerNotaCredito) {
		this.importoIvaDetraibilePerNotaCredito = importoIvaDetraibilePerNotaCredito;
	}

	public BigDecimal getImportoIvaIndPerNotaCredito() {
		return importoIvaIndPerNotaCredito;
	}

	public void setImportoIvaIndPerNotaCredito(BigDecimal importoIvaIndPerNotaCredito) {
		this.importoIvaIndPerNotaCredito = importoIvaIndPerNotaCredito;
	}

	BigDecimal arrAliIva;
	public BigDecimal getImponibile() {
		return imponibile;
	}
	public void setImponibile(BigDecimal imponibile) {
		this.imponibile = imponibile;
	}
	public BigDecimal getImportoIva() {
		return importoIva;
	}
	public void setImportoIva(BigDecimal importoIva) {
		this.importoIva = importoIva;
	}
	public BigDecimal getImportoIvaInd() {
		return importoIvaInd;
	}
	public void setImportoIvaInd(BigDecimal importoIvaInd) {
		this.importoIvaInd = importoIvaInd;
	}
	public BigDecimal getTotale() {
		return Utility.nvl(getImponibile()).add(Utility.nvl(getImportoIva())).add(Utility.nvl(getArrAliIva()));
	}
	public BigDecimal getTotalePerNotaCredito() {
		return Utility.nvl(getImponibilePerNotaCredito()).add(Utility.nvl(getImportoIvaPerNotaCredito())).add(Utility.nvl(getArrAliIva()));
	}
	public BigDecimal getArrAliIva() {
		return arrAliIva;
	}
	public void setArrAliIva(BigDecimal arrAliIva) {
		this.arrAliIva = arrAliIva;
	}
	public BigDecimal getImportoIvaDetraibile() {
		return importoIvaDetraibile;
	}
	public void setImportoIvaDetraibile(BigDecimal importoIvaDetraibile) {
		this.importoIvaDetraibile = importoIvaDetraibile;
	}
}
