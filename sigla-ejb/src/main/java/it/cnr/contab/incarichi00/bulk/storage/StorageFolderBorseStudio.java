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

@StorageType(name="F:sigla_contratti:borse_studio")
public class StorageFolderBorseStudio extends StorageFolderContrattiModel {
	private static final long serialVersionUID = -8714200358707176681L;

	public StorageFolderBorseStudio(Incarichi_repertorioBulk incaricoRepertorio) {
    	super(incaricoRepertorio);
    }

	@StoragePolicy(name="P:sigla_contratti_aspect:borse_studio", property=@StorageProperty(name="sigla_contratti_aspect_borse_studio:esercizio"))
	public Integer getEsercizio() {
		return super.getEsercizio();
	}
	
	@StoragePolicy(name="P:sigla_contratti_aspect:borse_studio", property=@StorageProperty(name="sigla_contratti_aspect_borse_studio:progressivo", converterBeanName="storage.converter.longToIntegerConverter"))
	public Long getPg_repertorio() {
		return super.getPg_repertorio();
	}

	@StoragePolicy(name="P:sigla_contratti_aspect:incarichi", property=@StorageProperty(name="sigla_contratti_aspect_incarichi:esercizio"))
    public Integer getEsercizioIncarico() {
		return super.getEsercizio();
    }

	@StoragePolicy(name="P:sigla_contratti_aspect:incarichi", property=@StorageProperty(name="sigla_contratti_aspect_incarichi:progressivo", converterBeanName="storage.converter.longToIntegerConverter"))
    public Long getPgIncarico() {
		return super.getPg_repertorio();
    }
	
	public String getCMISPath() {
		return SpringUtil.getBean("storeService", StoreService.class)
				.createFolderIfNotPresent(
						getCMISParentPath(),
						"Borsa di Studio "+this.getEsercizio().toString()+Utility.lpad(this.getPg_repertorio().toString(),10,'0'),
						null, null, this);
	}

	public boolean isEqualsTo(StorageObject node, List<String> listError){
		boolean isEquals = super.isEqualsTo(node, listError);
		String initTesto = "Procedura "+this.getEsercizio_procedura().toString()+"/"+this.getPg_procedura().toString()+" - "+
						   "Incarico "+this.getEsercizio().toString()+"/"+this.getPg_repertorio().toString()+" - Disallineamento dato ";
		String valueDB=null, valueCMIS=null; 

		valueDB=String.valueOf(this.getEsercizio());
		valueCMIS=String.valueOf(node.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_BORSE_STUDIO_ESERCIZIO.value()));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Esercizio Borsa Studio - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getPg_repertorio());
		valueCMIS=String.valueOf(node.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_BORSE_STUDIO_PROGRESSIVO.value()));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Pg_repertorio Borsa Studio - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}
		return isEquals;
	}
}
