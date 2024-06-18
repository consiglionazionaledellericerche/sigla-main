package it.cnr.contab.util00.bulk.storage;

import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.si.spring.storage.StorageDriver;

import java.util.Arrays;
import java.util.stream.Collectors;

public class AllegatoParentIBulk extends OggettoBulk implements AllegatoParentBulk{
    private BulkList<AllegatoGenericoBulk> archivioAllegati = new BulkList<AllegatoGenericoBulk>();

    public AllegatoParentIBulk(BulkList<AllegatoGenericoBulk> archivioAllegati) {
        this.archivioAllegati = archivioAllegati;
    }

    public AllegatoParentIBulk() {
    }

    public AllegatoGenericoBulk removeFromArchivioAllegati(int index) {
        return getArchivioAllegati().remove(index);
    }

    public int addToArchivioAllegati(AllegatoGenericoBulk allegato) {
        archivioAllegati.add(allegato);
        return archivioAllegati.size()-1;
    }

    public BulkList<AllegatoGenericoBulk> getArchivioAllegati() {
        return archivioAllegati;
    }

    public void setArchivioAllegati(
            BulkList<AllegatoGenericoBulk> archivioAllegati) {
        this.archivioAllegati = archivioAllegati;
    }

    public static String getStorePath(String suffix, Integer esercizio) {
        return Arrays.asList(
                SpringUtil.getBean(StorePath.class).getPathComunicazioniDal(),
                suffix,
                String.valueOf(esercizio)
        ).stream().collect(
                Collectors.joining(StorageDriver.SUFFIX)
        );
    }


}
