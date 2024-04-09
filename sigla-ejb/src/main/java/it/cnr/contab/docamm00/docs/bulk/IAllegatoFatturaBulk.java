package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTestataBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoParentBulk;

import java.util.List;

public interface IAllegatoFatturaBulk extends AllegatoParentBulk {
    List<String> getStorePath();

    String getAllegatoLabel();

    default String getTitleAllegatoMultiplo() {
        StringBuffer buffer = new StringBuffer("Aggiungi allegato alle fatture");
        if (this instanceof DocumentoEleTestataBulk)
            buffer.append(" elettroniche");
        buffer.append(": ");
        return buffer.toString();
    }
}
