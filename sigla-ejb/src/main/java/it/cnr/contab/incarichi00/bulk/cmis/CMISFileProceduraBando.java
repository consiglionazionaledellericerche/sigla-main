package it.cnr.contab.incarichi00.bulk.cmis;

import it.cnr.contab.cmis.CMISTypeName;
import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.contab.incarichi00.bulk.Incarichi_procedura_archivioBulk;
import it.cnr.contab.incarichi00.cmis.CMISContrattiAttachment;

import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;

public class CMISFileProceduraBando extends CMISFileProcedura implements CMISTypeName{
	private static final long serialVersionUID = -1775673719677028944L;
	private transient static final Logger logger = LoggerFactory.getLogger(CMISFileProceduraBando.class);

	public CMISFileProceduraBando(File file, String originalName, Incarichi_procedura_archivioBulk incaricoProceduraArchivio) throws IOException {
		super(file, originalName, incaricoProceduraArchivio);
	}

	public CMISFileProceduraBando(Incarichi_procedura_archivioBulk incaricoProceduraArchivio) throws IOException {
		super(incaricoProceduraArchivio);
	}

	public CMISFileProceduraBando(Document node, Incarichi_procedura_archivioBulk incaricoProceduraArchivio) {
		super(node, incaricoProceduraArchivio);
	}

	public String getTypeName() {
    	return CMISContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_BANDO.value();
	}

	@CMISProperty(name="sigla_contratti_attachment:data_inizio", converterBeanName="cmis.converter.timestampToCalendarConverter")
	public java.sql.Timestamp getDataInizioPubblicazione() {
		if (this.getIncaricoProceduraArchivio()==null||getIncaricoProceduraArchivio().getIncarichi_procedura()==null)
			return null;
		return this.getIncaricoProceduraArchivio().getIncarichi_procedura().getDt_pubblicazione();
	}

	@CMISProperty(name="sigla_contratti_attachment:data_fine", converterBeanName="cmis.converter.timestampToCalendarConverter")
	public java.sql.Timestamp getDataFinePubblicazione() {
		if (this.getIncaricoProceduraArchivio()==null||getIncaricoProceduraArchivio().getIncarichi_procedura()==null)
			return null;
		return this.getIncaricoProceduraArchivio().getIncarichi_procedura().getDt_fine_pubblicazione();
	}
	
	public boolean isEqualsTo(CmisObject node, List<String> listError){
		String initTesto = "Procedura "+this.getEsercizioProcedura().toString()+"/"+this.getPgProcedura().toString()+" - Disallineamento dato ";
		boolean isEquals = super.isEqualsTo(node, listError);
		String valueDB=null, valueCMIS=null; 

		GregorianCalendar gcDB = (GregorianCalendar)GregorianCalendar.getInstance(); 
		gcDB.setTime(this.getDataInizioPubblicazione());
		valueDB=Integer.toString(gcDB.get(GregorianCalendar.DAY_OF_MONTH)) + "/" + 
				Integer.toString(gcDB.get(GregorianCalendar.MONTH)) + "/" + 
				Integer.toString(gcDB.get(GregorianCalendar.YEAR));
		GregorianCalendar gcCMIS = (GregorianCalendar)node.getPropertyValue("sigla_contratti_attachment:data_inizio");
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
		gcCMIS = (GregorianCalendar)node.getPropertyValue("sigla_contratti_attachment:data_fine");
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
