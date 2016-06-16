package it.cnr.contab.docamm00.docs.bulk;

import org.apache.chemistry.opencmis.client.api.Document;

import it.cnr.contab.docamm00.cmis.CMISDocAmmAspect;
import it.cnr.contab.docamm00.fatturapa.bulk.AllegatoFatturaBulk;

public class AllegatoFatturaAttivaBulk extends AllegatoFatturaBulk {

	public AllegatoFatturaAttivaBulk() {
		super();
		setAspectName(CMISDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_ALLEGATI_NON_INVIATI_SDI.value());
	}

	public AllegatoFatturaAttivaBulk(Document node) {
		super(node);
		setAspectName(CMISDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_ALLEGATI_NON_INVIATI_SDI.value());
	}
}
