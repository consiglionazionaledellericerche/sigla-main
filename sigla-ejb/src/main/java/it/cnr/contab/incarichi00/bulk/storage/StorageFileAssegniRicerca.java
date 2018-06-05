package it.cnr.contab.incarichi00.bulk.storage;

import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.contab.incarichi00.bulk.Incarichi_archivioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_archivioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_rappBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_varBulk;
import it.cnr.contab.incarichi00.storage.StorageContrattiProperty;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class StorageFileAssegniRicerca extends StorageFileIncarichi {
	private static final long serialVersionUID = -1775673719677028944L;
	private transient static final Logger logger = LoggerFactory.getLogger(StorageFileBorseStudio.class);

	public StorageFileAssegniRicerca(Incarichi_repertorio_archivioBulk incaricoRepertorioArchivio) throws IOException{
		super(incaricoRepertorioArchivio);
	}

	public StorageFileAssegniRicerca(Incarichi_repertorio_rappBulk incaricoRepertorioRapp) throws IOException{
		super(incaricoRepertorioRapp);
	}

	public StorageFileAssegniRicerca(Incarichi_repertorio_varBulk incaricoRepertorioVar) throws IOException{
		super(incaricoRepertorioVar);
	}

	public StorageFileAssegniRicerca(File file, String originalName, Incarichi_repertorio_archivioBulk incaricoRepertorioArchivio) throws IOException {
		super(file, originalName, incaricoRepertorioArchivio);
	}

	public StorageFileAssegniRicerca(File file, String originalName, Incarichi_repertorio_rappBulk incaricoRepertorioRapp) throws IOException {
		super(file, originalName, incaricoRepertorioRapp);
	}

	public StorageFileAssegniRicerca(File file, String originalName, Incarichi_repertorio_varBulk incaricoRepertorioVar) throws IOException {
		super(file, originalName, incaricoRepertorioVar);
	}

	public StorageFileAssegniRicerca(StorageObject storageObject, Incarichi_repertorio_archivioBulk incaricoRepertorioArchivio) {
		super(storageObject, incaricoRepertorioArchivio);
	}

	public StorageFileAssegniRicerca(StorageObject storageObject, Incarichi_repertorio_rappBulk incaricoRepertorioRapp) {
		super(storageObject, incaricoRepertorioRapp);
	}

	public StorageFileAssegniRicerca(StorageObject storageObject, Incarichi_repertorio_varBulk incaricoRepertorioVar) {
		super(storageObject, incaricoRepertorioVar);
	}

	@StoragePolicy(name="P:sigla_contratti_aspect:assegni_ricerca", property=@StorageProperty(name="sigla_contratti_aspect_assegni_ricerca:esercizio"))
    public Integer getEsercizioIncarico() {
		return super.getEsercizioIncarico();
    }

	@StoragePolicy(name="P:sigla_contratti_aspect:assegni_ricerca", property=@StorageProperty(name="sigla_contratti_aspect_assegni_ricerca:progressivo", converterBeanName="storage.converter.longToIntegerConverter"))
    public Long getPgIncarico() {
		return super.getPgIncarico();
    }

	@SuppressWarnings("unused")
	private void initCMISField(Integer esercizio, Long progressivo){
	    this.setAuthor(getIncaricoArchivio().getUtcr());
		this.setTitle((String)Incarichi_archivioBulk.getTipo_archivioKeys().get(getIncaricoArchivio().getTipo_archivio()));
		this.setDescription((String)Incarichi_archivioBulk.getTipo_archivioKeys().get(getIncaricoArchivio().getTipo_archivio()).toString()+
					" - Assegno di Ricerca nr."+esercizio+"/"+progressivo);
	}
	public boolean isEqualsTo(StorageObject storageObject){
		String initTesto = "Procedura "+this.getEsercizioProcedura().toString()+"/"+this.getPgProcedura().toString()+" - " +
						   "Incarico "+this.getEsercizioIncarico().toString()+"/"+this.getPgIncarico().toString()+" - Disallineamento dato ";
		boolean isEquals = true;
		String valueDB=null, valueCMIS=null; 

		valueDB=String.valueOf(this.getEsercizioProcedura());
		valueCMIS=String.valueOf(storageObject.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_PROCEDURA_ESERCIZIO.value()));
		if (!valueCMIS.equals(valueDB)) {
			logger.debug(initTesto+" - Esercizio Procedura - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getPgProcedura());
		valueCMIS=String.valueOf(storageObject.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_PROCEDURA_PROGRESSIVO.value()));
		if (!valueCMIS.equals(valueDB)) {
			logger.debug(initTesto+" - Pg_procedura - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getEsercizioIncarico());
		valueCMIS=String.valueOf(storageObject.getPropertyValue("sigla_contratti_aspect_assegni_ricerca:esercizio"));
		if (!valueCMIS.equals(valueDB)) {
			logger.debug(initTesto+" - Esercizio Incarico - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getPgIncarico());
		valueCMIS=String.valueOf(storageObject.getPropertyValue("sigla_contratti_aspect_assegni_ricerca:progressivo"));
		if (!valueCMIS.equals(valueDB)) {
			logger.debug(initTesto+" - Pg_repertorio - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getOriginalName());
		valueCMIS=String.valueOf(storageObject.getPropertyValue("sigla_contratti_attachment:original_name"));
		if (!valueCMIS.equals(valueDB)) {
			logger.debug(initTesto+" - Nome Originale File - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getTypeName());
		valueCMIS=String.valueOf(storageObject.getPropertyValue(StoragePropertyNames.OBJECT_TYPE_ID.value()));
		if (!valueCMIS.equals(valueDB)) {
			logger.debug(initTesto+" - Type documento - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		return isEquals;
	}	
	
}
