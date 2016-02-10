package it.cnr.contab.util00.cmis.bulk;

import it.cnr.contab.util00.bulk.cmis.AllegatoGenericoBulk;
import it.cnr.jada.bulk.BulkList;

public interface AllegatoParentBulk {
	int addToArchivioAllegati(AllegatoGenericoBulk allegato);
	AllegatoGenericoBulk removeFromArchivioAllegati(int index);
	BulkList<AllegatoGenericoBulk> getArchivioAllegati();
	void setArchivioAllegati(BulkList<AllegatoGenericoBulk> archivioAllegati);
}
