package it.cnr.contab.incarichi00.storage;

public enum StorageContrattiProperty {
	
	SIGLA_CONTRATTI_PROCEDURA_ESERCIZIO("sigla_contratti_aspect_procedura:esercizio"),
	SIGLA_CONTRATTI_PROCEDURA_PROGRESSIVO("sigla_contratti_aspect_procedura:progressivo"),
	SIGLA_CONTRATTI_INCARICHI_ESERCIZIO("sigla_contratti_aspect_incarichi:esercizio"),
	SIGLA_CONTRATTI_INCARICHI_PROGRESSIVO("sigla_contratti_aspect_incarichi:progressivo"),
	SIGLA_CONTRATTI_BORSE_STUDIO_ESERCIZIO("sigla_contratti_aspect_borse_studio:esercizio"),
	SIGLA_CONTRATTI_BORSE_STUDIO_PROGRESSIVO("sigla_contratti_aspect_borse_studio:progressivo"),
	SIGLA_CONTRATTI_ASSEGNI_RICERCA_ESERCIZIO("sigla_contratti_aspect_assegni_ricerca:esercizio"),
	SIGLA_CONTRATTI_ASSEGNI_RICERCA_PROGRESSIVO("sigla_contratti_aspect_assegni_ricerca:progressivo"),
	SIGLA_CONTRATTI_APPALTI_ESERCIZIO("sigla_contratti_aspect_appalti:esercizio"),
	SIGLA_CONTRATTI_APPALTI_PROGRESSIVO("sigla_contratti_aspect_appalti:progressivo"),
	SIGLA_CONTRATTI_TIPO_NORMA_DESCRIZIONE("sigla_contratti_aspect_tipo_norma:descrizione"),
	SIGLA_CONTRATTI_TIPO_NORMA_NUMERO("sigla_contratti_aspect_tipo_norma:numero"),
	SIGLA_CONTRATTI_TIPO_NORMA_DATA("sigla_contratti_aspect_tipo_norma:data"),
	SIGLA_CONTRATTI_TIPO_NORMA_ARTICOLO("sigla_contratti_aspect_tipo_norma:articolo"),
	SIGLA_CONTRATTI_TIPO_NORMA_COMMA("sigla_contratti_aspect_tipo_norma:comma"),
	SIGLA_CONTRATTI_LINK_URL("sigla_contratti_aspect_link:url"),
	SIGLA_CONTRATTI_ATTACHMENT_ORIGINAL_NAME("sigla_contratti_attachment:original_name"),
	SIGLA_CONTRATTI_ATTACHMENT_DATA_INIZIO("sigla_contratti_attachment:data_inizio"),
	SIGLA_CONTRATTI_ATTACHMENT_DATA_FINE("sigla_contratti_attachment:data_fine");

	private final String value;

	private StorageContrattiProperty(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static StorageContrattiProperty fromValue(String v) {
		for (StorageContrattiProperty c : StorageContrattiProperty.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}
}
