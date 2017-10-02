package it.cnr.contab.docamm00.storage;

public enum StorageDocAmmAttachment {
	
	SIGLA_CONTRATTI_ATTACHMENT_BANDO("D:sigla_contratti_attachment:bando"),
	SIGLA_CONTRATTI_ATTACHMENT_DECISIONE_A_CONTRATTARE("D:sigla_contratti_attachment:decisione_a_contrattare"),
	SIGLA_CONTRATTI_ATTACHMENT_CONTRATTO("D:sigla_contratti_attachment:contratto"),
	SIGLA_CONTRATTI_ATTACHMENT_VARIAZIONE_CONTRATTO("D:sigla_contratti_attachment:variazione_contratto"),
	SIGLA_CONTRATTI_ATTACHMENT_CURRICULUM_VINCITORE("D:sigla_contratti_attachment:curriculum_vincitore"),
	SIGLA_CONTRATTI_ATTACHMENT_DECRETO_NOMINA("D:sigla_contratti_attachment:decreto_nomina"),
	SIGLA_CONTRATTI_ATTACHMENT_ATTO_ESITO_CONTROLLO("D:sigla_contratti_attachment:atto_esito_controllo"),
	SIGLA_CONTRATTI_ATTACHMENT_PROGETTO("D:sigla_contratti_attachment:progetto"),
	SIGLA_CONTRATTI_ATTACHMENT_ALLEGATO_GENERICO("D:sigla_contratti_attachment:allegato_generico"),
	SIGLA_CONTRATTI_ATTACHMENT_CAPITOLATO("D:sigla_contratti_attachment:capitolato"),
	SIGLA_CONTRATTI_ATTACHMENT_DICHIARAZIONE_ALTRI_RAPPORTI("D:sigla_contratti_attachment:dichiarazione_altri_rapporti");
	
	private final String value;

	private StorageDocAmmAttachment(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static StorageDocAmmAttachment fromValue(String v) {
		for (StorageDocAmmAttachment c : StorageDocAmmAttachment.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}	
}
