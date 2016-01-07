package it.cnr.contab.doccont00.intcass.bulk;

import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.cmis.service.SiglaCMISService;
import it.cnr.contab.util00.cmis.bulk.AllegatoParentBulk;
import it.cnr.jada.comp.ApplicationException;

public interface StatoTrasmissione extends AllegatoParentBulk{
	public java.lang.String getStato_trasmissione();
	public void setStato_trasmissione(java.lang.String stato_trasmissione);
	public CMISPath getCMISPath(SiglaCMISService cmisService) throws ApplicationException;
	public Integer getEsercizio();
	public String getCd_cds();
	public String getCd_unita_organizzativa();
	public Long getPg_documento_cont();
	public String getCd_tipo_documento_cont();
}
