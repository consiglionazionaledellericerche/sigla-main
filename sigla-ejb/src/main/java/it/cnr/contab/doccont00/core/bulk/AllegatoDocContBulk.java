package it.cnr.contab.doccont00.core.bulk;

import it.cnr.contab.spring.storage.annotation.StoragePolicy;
import it.cnr.contab.spring.storage.annotation.StorageProperty;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.jada.util.OrderedHashtable;

public class AllegatoDocContBulk extends AllegatoGenericoBulk {
	private static final long serialVersionUID = 1L;
	private OrderedHashtable rifModalitaPagamentoKeys = new OrderedHashtable();
	@StoragePolicy(name="P:doccont:rif_modalita_pagamentoAspect", property=@StorageProperty(name="doccont:rif_modalita_pagamento"))
	private String rifModalitaPagamento;

	public AllegatoDocContBulk() {
		super();
	}

	public AllegatoDocContBulk(String storageKey) {
		super(storageKey);
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
}