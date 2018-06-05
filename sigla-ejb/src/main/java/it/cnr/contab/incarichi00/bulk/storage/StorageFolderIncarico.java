package it.cnr.contab.incarichi00.bulk.storage;

import it.cnr.contab.service.SpringUtil;
import it.cnr.si.spring.storage.StorageService;
import it.cnr.si.spring.storage.StoreService;
import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.si.spring.storage.annotation.StorageType;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.storage.StorageContrattiProperty;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.contab.util.Utility;

import java.util.List;

@StorageType(name="F:sigla_contratti:incarichi")
public class StorageFolderIncarico extends StorageFolderContrattiModel {

	private static final long serialVersionUID = -4775726295197343186L;

	public StorageFolderIncarico(Incarichi_repertorioBulk incaricoRepertorio) {
    	super(incaricoRepertorio);
    }

	@StoragePolicy(name="P:sigla_contratti_aspect:incarichi", property=@StorageProperty(name="sigla_contratti_aspect_incarichi:esercizio"))
	public Integer getEsercizio() {
		return super.getEsercizio();
	}
	
	@StoragePolicy(name="P:sigla_contratti_aspect:incarichi", property=@StorageProperty(name="sigla_contratti_aspect_incarichi:progressivo", converterBeanName="storage.converter.longToIntegerConverter"))
	public Long getPg_repertorio() {
		return super.getPg_repertorio();
	}
	
	public String getCMISPath() {
		return SpringUtil.getBean("storeService", StoreService.class)
				.createFolderIfNotPresent(
						getCMISParentPath(),
						"Incarico "+this.getEsercizio().toString()+Utility.lpad(this.getPg_repertorio().toString(),10,'0'),
						null, null, this);
	}
	
	public boolean isEqualsTo(StorageObject node, List<String> listError){
		boolean isEquals = super.isEqualsTo(node, listError);
		String initTesto = "Procedura "+this.getEsercizio_procedura().toString()+"/"+this.getPg_procedura().toString()+" - "+
						   "Incarico "+this.getEsercizio().toString()+"/"+this.getPg_repertorio().toString()+" - Disallineamento dato ";
		String valueDB=null, valueCMIS=null; 

		valueDB=String.valueOf(this.getEsercizio());
		valueCMIS=String.valueOf(node.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_INCARICHI_ESERCIZIO.value()));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Esercizio Incarico - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getPg_repertorio());
		valueCMIS=String.valueOf(node.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_INCARICHI_PROGRESSIVO.value()));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Pg_repertorio - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}
		return isEquals;
	}
}
