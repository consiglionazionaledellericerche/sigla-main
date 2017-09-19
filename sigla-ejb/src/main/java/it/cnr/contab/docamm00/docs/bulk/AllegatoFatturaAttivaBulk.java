package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.contab.docamm00.storage.StorageDocAmmAspect;
import it.cnr.contab.docamm00.fatturapa.bulk.AllegatoFatturaBulk;

public class AllegatoFatturaAttivaBulk extends AllegatoFatturaBulk {

	public AllegatoFatturaAttivaBulk() {
		super();
		setAspectName(StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_ALLEGATI_NON_INVIATI_SDI.value());
	}

	public AllegatoFatturaAttivaBulk(String storageKey) {
		super(storageKey);
		setAspectName(StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_ALLEGATI_NON_INVIATI_SDI.value());
	}
}