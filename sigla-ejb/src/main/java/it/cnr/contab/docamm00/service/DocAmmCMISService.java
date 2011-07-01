package it.cnr.contab.docamm00.service;

import it.cnr.cmisdl.model.Node;
import it.cnr.contab.cmis.acl.Permission;
import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.cmis.service.CMISService;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoSpesaBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.service.SpringUtil;

public class DocAmmCMISService extends CMISService{
	
	public void addAnotherParentToNode(MandatoBulk mandato, IDocumentoAmministrativoSpesaBulk docAmm, String user, Node node, String beanNamePath){
		CMISPath cmisPathCDS = SpringUtil.getBean(beanNamePath,
				CMISPath.class);
		cmisPathCDS = createFolderIfNotPresent(cmisPathCDS, String.valueOf(mandato.getEsercizio()), 
			"Esercizio "+mandato.getEsercizio(), "Esercizio "+mandato.getEsercizio());
		cmisPathCDS = createFolderIfNotPresent(cmisPathCDS, "CDS "+docAmm.getCd_cds(), 
			"CDS "+docAmm.getCd_cds(), "CDS "+docAmm.getCd_cds());
		cmisPathCDS = createFolderIfNotPresent(cmisPathCDS, "UO "+docAmm.getCd_uo(), 
				"UO "+docAmm.getCd_uo(), "UO "+docAmm.getCd_uo());
		copyTo(node, cmisPathCDS);
	
		CMISPath cmisPathUserHomes = SpringUtil.getBean("cmisPathUserHomes",CMISPath.class);
		cmisPathUserHomes = createFolderIfNotPresent(cmisPathUserHomes,user,
			user, user, Permission.construct(user, getRoleCoordinator())); 
		CMISPath cmisPathConcFormazReddito = SpringUtil.getBean(beanNamePath,CMISPath.class);
		for (String names : cmisPathConcFormazReddito.getNames()) {
			cmisPathUserHomes = createFolderIfNotPresent(cmisPathUserHomes,
				names,null,null, Permission.construct(user, getRoleConsumer()));
		}
		cmisPathUserHomes = createFolderIfNotPresent(cmisPathUserHomes, String.valueOf(mandato.getEsercizio()), 
				"Esercizio "+mandato.getEsercizio(), "Esercizio "+mandato.getEsercizio());
		copyTo(node, cmisPathUserHomes);		
	}
}
