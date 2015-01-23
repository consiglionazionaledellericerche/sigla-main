package it.cnr.contab.incarichi00.bulk.cmis;

import it.cnr.contab.cmis.annotation.CMISPolicy;
import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.contab.cmis.annotation.CMISType;
import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.cmis.service.SiglaCMISService;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.cmis.CMISContrattiProperty;
import it.cnr.contab.util.Utility;

import java.util.List;

import org.apache.chemistry.opencmis.client.api.CmisObject;

@CMISType(name="F:sigla_contratti:incarichi")
public class CMISFolderIncarico extends CMISFolderContrattiModel {

	private static final long serialVersionUID = -4775726295197343186L;

	public CMISFolderIncarico(Incarichi_repertorioBulk incaricoRepertorio) {
    	super(incaricoRepertorio);
    }

	@CMISPolicy(name="P:sigla_contratti_aspect:incarichi", property=@CMISProperty(name="sigla_contratti_aspect_incarichi:esercizio"))
	public Integer getEsercizio() {
		return super.getEsercizio();
	}
	
	@CMISPolicy(name="P:sigla_contratti_aspect:incarichi", property=@CMISProperty(name="sigla_contratti_aspect_incarichi:progressivo", converterBeanName="cmis.converter.longToIntegerConverter"))
	public Long getPg_repertorio() {
		return super.getPg_repertorio();
	}
	
	public CMISPath getCMISPath(SiglaCMISService cmisService){
		CMISPath cmisPath = this.getCMISParentPath(cmisService);
		if (cmisPath!=null) {
			cmisPath = cmisService.createFolderIfNotPresent(cmisPath, "Incarico "+this.getEsercizio().toString()+Utility.lpad(this.getPg_repertorio().toString(),10,'0'), "Incarico "+this.getEsercizio().toString()+"/"+this.getPg_repertorio().toString(), "Incarico "+this.getEsercizio().toString()+"/"+this.getPg_repertorio().toString(), this);
			cmisService.setInheritedPermission(cmisPath, Boolean.FALSE);
		}
		return cmisPath;
	}
	
	public boolean isEqualsTo(CmisObject node, List<String> listError){
		boolean isEquals = super.isEqualsTo(node, listError);
		String initTesto = "Procedura "+this.getEsercizio_procedura().toString()+"/"+this.getPg_procedura().toString()+" - "+
						   "Incarico "+this.getEsercizio().toString()+"/"+this.getPg_repertorio().toString()+" - Disallineamento dato ";
		String valueDB=null, valueCMIS=null; 

		valueDB=String.valueOf(this.getEsercizio());
		valueCMIS=String.valueOf(node.getPropertyValue(CMISContrattiProperty.SIGLA_CONTRATTI_INCARICHI_ESERCIZIO.value()));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Esercizio Incarico - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getPg_repertorio());
		valueCMIS=String.valueOf(node.getPropertyValue(CMISContrattiProperty.SIGLA_CONTRATTI_INCARICHI_PROGRESSIVO.value()));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Pg_repertorio - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}
		return isEquals;
	}
}
