package it.cnr.contab.incarichi00.bulk.cmis;

import it.cnr.contab.cmis.annotation.CMISPolicy;
import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.contab.cmis.annotation.CMISType;
import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.cmis.service.CMISService;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.util.Utility;

@CMISType(name="F:sigla_contratti:assegni_ricerca")
public class CMISFolderAssegniRicerca extends CMISFolderContrattiModel {
	private static final long serialVersionUID = 1L;

	public CMISFolderAssegniRicerca(Incarichi_repertorioBulk incaricoRepertorio) {
    	super(incaricoRepertorio);
    }

	@CMISPolicy(name="P:sigla_contratti_aspect:assegni_ricerca", property=@CMISProperty(name="sigla_contratti_aspect_assegni_ricerca:esercizio"))
	public Integer getEsercizio() {
		return super.getEsercizio();
	}
	
	@CMISPolicy(name="P:sigla_contratti_aspect:assegni_ricerca", property=@CMISProperty(name="sigla_contratti_aspect_assegni_ricerca:progressivo", converterBeanName="cmis.converter.longToIntegerConverter"))
	public Long getPg_repertorio() {
		return super.getPg_repertorio();
	}

	public CMISPath getCMISPath(CMISService cmisService){
		CMISPath cmisPath = this.getCMISParentPath(cmisService);
		if (cmisPath!=null) {
			cmisPath = cmisService.createFolderIfNotPresent(cmisPath, "Assegno di Ricerca "+this.getEsercizio().toString()+Utility.lpad(this.getPg_repertorio().toString(),10,'0'), "Assegno di Ricerca "+this.getEsercizio().toString()+"/"+this.getPg_repertorio().toString(), "Assegno di Ricerca "+this.getEsercizio().toString()+"/"+this.getPg_repertorio().toString(), this);
			cmisService.setInheritedPermission(cmisPath, Boolean.FALSE);
		}
		return cmisPath;
	}
}
