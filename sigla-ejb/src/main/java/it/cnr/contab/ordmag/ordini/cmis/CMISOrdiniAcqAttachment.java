package it.cnr.contab.ordmag.ordini.cmis;

public enum CMISOrdiniAcqAttachment {
	
	SIGLA_ORDINI_ACQ_ATTACHMENT_STAMPA("D:ordini_acq_attachment:stampa"),
	SIGLA_ORDINI_ACQ_ATTACHMENT_BANDO("D:ordini_acq_attachment:bando"),
	SIGLA_ORDINI_ACQ_ATTACHMENT_DECISIONE_A_CONTRATTARE("D:ordini_acq_attachment:decisione_a_contrattare"),
	SIGLA_ORDINI_ACQ_ATTACHMENT_DECRETO_NOMINA("D:ordini_acq_attachment:decreto_nomina"),
	SIGLA_ORDINI_ACQ_ATTACHMENT_ATTO_ESITO_CONTROLLO("D:ordini_acq_attachment:atto_esito_controllo"),
	SIGLA_ORDINI_ACQ_ATTACHMENT_ALLEGATO_GENERICO("D:ordini_acq_attachment:allegato_generico"),
	SIGLA_ORDINI_ACQ_ATTACHMENT_CAPITOLATO("D:ordini_acq_attachment:capitolato"),
	SIGLA_ORDINI_ACQ_ATTACHMENT_DICHIARAZIONE_ALTRI_RAPPORTI("D:ordini_acq_attachment:dichiarazione_altri_rapporti");
	
	private final String value;

	private CMISOrdiniAcqAttachment(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static CMISOrdiniAcqAttachment fromValue(String v) {
		for (CMISOrdiniAcqAttachment c : CMISOrdiniAcqAttachment.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}	
}
