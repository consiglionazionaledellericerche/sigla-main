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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.jada.util.OrderedHashtable;

public class AllegatoDocContBulk extends AllegatoGenericoBulk {
	private static final long serialVersionUID = 1L;
	private OrderedHashtable rifModalitaPagamentoKeys = new OrderedHashtable();
	@StoragePolicy(name="P:doccont:rif_modalita_pagamentoAspect", property=@StorageProperty(name="doccont:rif_modalita_pagamento"))
	private String rifModalitaPagamento;
	private boolean cancellabile;
	private boolean modificabile;

	public AllegatoDocContBulk() {
		super();
		this.cancellabile = Boolean.TRUE;
		this.modificabile = Boolean.TRUE;
	}

	public AllegatoDocContBulk(String storageKey) {
		super(storageKey);
		this.cancellabile = Boolean.TRUE;
		this.modificabile = Boolean.TRUE;
	}

	public String getRifModalitaPagamento() {
		return rifModalitaPagamento;
	}

	public void setRifModalitaPagamento(String rifModalitaPagamento) {
		this.rifModalitaPagamento = rifModalitaPagamento;
	}

	public OrderedHashtable getRifModalitaPagamentoKeys() {
		return rifModalitaPagamentoKeys;
	}

	public void setRifModalitaPagamentoKeys(
			OrderedHashtable rifModalitaPagamentoKeys) {
		this.rifModalitaPagamentoKeys = rifModalitaPagamentoKeys;
	}

	public boolean isCancellabile() {
		return cancellabile;
	}

	public void setCancellabile(boolean cancellabile) {
		this.cancellabile = cancellabile;
	}

	public boolean isModificabile() {
		return modificabile;
	}

	public void setModificabile(boolean modificabile) {
		this.modificabile = modificabile;
	}
}