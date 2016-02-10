package it.cnr.contab.doccont00.core.bulk;

import org.apache.chemistry.opencmis.client.api.Document;

import it.cnr.contab.cmis.annotation.CMISPolicy;
import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.contab.util00.bulk.cmis.AllegatoGenericoBulk;
import it.cnr.jada.util.OrderedHashtable;

public class AllegatoDocContBulk extends AllegatoGenericoBulk {
	private static final long serialVersionUID = 1L;
	private OrderedHashtable rifModalitaPagamentoKeys = new OrderedHashtable();
	@CMISPolicy(name="P:doccont:rif_modalita_pagamentoAspect", property=@CMISProperty(name="doccont:rif_modalita_pagamento"))
	private String rifModalitaPagamento;
		
	public AllegatoDocContBulk() {
		super();
	}

	public AllegatoDocContBulk(Document node) {
		super(node);
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
