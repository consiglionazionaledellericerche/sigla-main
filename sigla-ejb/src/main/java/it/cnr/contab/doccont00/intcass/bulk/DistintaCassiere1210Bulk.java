/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 24/12/2015
 */
package it.cnr.contab.doccont00.intcass.bulk;

import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.contab.cmis.annotation.CMISType;
import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.cmis.service.SiglaCMISService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.jada.comp.ApplicationException;
@CMISType(name="D:doccont:document")
public class DistintaCassiere1210Bulk extends DistintaCassiere1210Base {
	private static final long serialVersionUID = 1L;

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: DISTINTA_CASSIERE_1210
	 **/
	public DistintaCassiere1210Bulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: DISTINTA_CASSIERE_1210
	 **/
	public DistintaCassiere1210Bulk(java.lang.Integer esercizio, java.lang.Long pgDistinta) {
		super(esercizio, pgDistinta);
	}
	public String getCMISFolderName() {
		String suffix = "Distinta 1210 n.";
		suffix = suffix.concat(String.valueOf(getPgDistinta()));
		return suffix;
	}
	
	@CMISProperty(name="doccont:tipo")	
	public String getTipo() {
		return "DIST1210";
	}
	
	public CMISPath getCMISPath(SiglaCMISService cmisService) throws ApplicationException {
		CMISPath cmisPath = SpringUtil.getBean("cmisPathComunicazioniDalCNR",CMISPath.class);
		cmisPath = cmisService.createFolderIfNotPresent(cmisPath,"Distinte 1210" ,null, null);
		cmisPath = cmisService.createFolderIfNotPresent(cmisPath, getEsercizio().toString(), null, null);		
		cmisPath = cmisService.createFolderIfNotPresent(cmisPath, getCMISFolderName(), 
				null, 
				null);
		return cmisPath;		
	}
}