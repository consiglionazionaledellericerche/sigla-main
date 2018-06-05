package it.cnr.contab.incarichi00.bulk.storage;

import it.cnr.si.spring.storage.bulk.StorageTypeName;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.contab.incarichi00.bulk.Incarichi_procedura_archivioBulk;
import it.cnr.contab.incarichi00.storage.StorageContrattiAttachment;
import it.cnr.si.spring.storage.StorageObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;

public class StorageFileProceduraBando extends StorageFileProcedura implements StorageTypeName {
	private static final long serialVersionUID = -1775673719677028944L;
	private transient static final Logger logger = LoggerFactory.getLogger(StorageFileProceduraBando.class);

	public StorageFileProceduraBando(File file, String originalName, Incarichi_procedura_archivioBulk incaricoProceduraArchivio) throws IOException {
		super(file, originalName, incaricoProceduraArchivio);
	}

	public StorageFileProceduraBando(Incarichi_procedura_archivioBulk incaricoProceduraArchivio) throws IOException {
		super(incaricoProceduraArchivio);
	}

	public StorageFileProceduraBando(StorageObject storageObject, Incarichi_procedura_archivioBulk incaricoProceduraArchivio) {
		super(storageObject, incaricoProceduraArchivio);
	}

	public String getTypeName() {
    	return StorageContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_BANDO.value();
	}

	@StorageProperty(name="sigla_contratti_attachment:data_inizio", converterBeanName="storage.converter.timestampToCalendarConverter")
	public java.sql.Timestamp getDataInizioPubblicazione() {
		if (this.getIncaricoProceduraArchivio()==null||getIncaricoProceduraArchivio().getIncarichi_procedura()==null)
			return null;
		return this.getIncaricoProceduraArchivio().getIncarichi_procedura().getDt_pubblicazione();
	}

	@StorageProperty(name="sigla_contratti_attachment:data_fine", converterBeanName="storage.converter.timestampToCalendarConverter")
	public java.sql.Timestamp getDataFinePubblicazione() {
		if (this.getIncaricoProceduraArchivio()==null||getIncaricoProceduraArchivio().getIncarichi_procedura()==null)
			return null;
		return this.getIncaricoProceduraArchivio().getIncarichi_procedura().getDt_fine_pubblicazione();
	}
	
	public boolean isEqualsTo(StorageObject storageObject, List<String> listError){
		String initTesto = "Procedura "+this.getEsercizioProcedura().toString()+"/"+this.getPgProcedura().toString()+" - Disallineamento dato ";
		boolean isEquals = super.isEqualsTo(storageObject, listError);
		String valueDB=null, valueCMIS=null; 

		GregorianCalendar gcDB = (GregorianCalendar)GregorianCalendar.getInstance(); 
		gcDB.setTime(this.getDataInizioPubblicazione());
		valueDB=Integer.toString(gcDB.get(GregorianCalendar.DAY_OF_MONTH)) + "/" + 
				Integer.toString(gcDB.get(GregorianCalendar.MONTH)) + "/" + 
				Integer.toString(gcDB.get(GregorianCalendar.YEAR));
		GregorianCalendar gcCMIS = (GregorianCalendar)storageObject.getPropertyValue("sigla_contratti_attachment:data_inizio");
		valueCMIS=Integer.toString(gcCMIS.get(GregorianCalendar.DAY_OF_MONTH)) + "/" + 
				  Integer.toString(gcCMIS.get(GregorianCalendar.MONTH)) + "/" + 
				  Integer.toString(gcCMIS.get(GregorianCalendar.YEAR));
		if (!valueCMIS.equals(valueDB)) {
			logger.debug(initTesto+" - Data Inizio Bando - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		gcDB = (GregorianCalendar)GregorianCalendar.getInstance(); 
		gcDB.setTime(this.getDataFinePubblicazione());
		valueDB=Integer.toString(gcDB.get(GregorianCalendar.DAY_OF_MONTH)) + "/" + 
				Integer.toString(gcDB.get(GregorianCalendar.MONTH)) + "/" + 
				Integer.toString(gcDB.get(GregorianCalendar.YEAR));
		gcCMIS = (GregorianCalendar)storageObject.getPropertyValue("sigla_contratti_attachment:data_fine");
		valueCMIS=Integer.toString(gcCMIS.get(GregorianCalendar.DAY_OF_MONTH)) + "/" + 
				  Integer.toString(gcCMIS.get(GregorianCalendar.MONTH)) + "/" + 
				  Integer.toString(gcCMIS.get(GregorianCalendar.YEAR));
		if (!valueCMIS.equals(valueDB)) {
			logger.debug(initTesto+" - Data Fine Bando - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		return isEquals;
	}	
}
