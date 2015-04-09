package it.cnr.contab.docamm00.cmis;

import it.cnr.contab.cmis.CMISTypeName;
import it.cnr.contab.cmis.annotation.CMISPolicy;
import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.contab.cmis.bulk.CMISFile;
import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.cmis.service.SiglaCMISService;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_archivioBulk;
import it.cnr.jada.comp.ApplicationException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.chemistry.opencmis.client.api.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CMISFileFatturaAttiva extends CMISFile implements CMISTypeName{
	private static final long serialVersionUID = -1775673719677028944L;
	private transient static final Logger logger = LoggerFactory.getLogger(CMISFileFatturaAttiva.class);

	private Fattura_attivaBulk fattura_attivaBulk;

	private String originalName;
	private String typeName = "D:sigla_fatture_attachment:document";

	public CMISFileFatturaAttiva(InputStream inputStream, String contentType, String name, Fattura_attivaBulk fattura) throws IOException{
    	super(inputStream, contentType, name);
		impostaDatiBaseFattura(name, fattura);
    }
	
	public CMISFileFatturaAttiva(File file, Fattura_attivaBulk fattura, String prefissoNomeFile) throws IOException{
    	super(file, null, prefissoNomeFile + fattura.constructCMISNomeFile());
		impostaDatiBaseFattura(file.getName(), fattura);
    }
	
	public CMISFileFatturaAttiva(File file, Fattura_attivaBulk fattura, String contentType, String nomeFile) throws IOException{
    	super(file, contentType, nomeFile);
    	impostaDatiBaseFattura(file.getName(), fattura);
	}
	
    public CMISFileFatturaAttiva(Document node) {
		super(node);
    }
	
	public CMISPath getCMISParentPath(SiglaCMISService cmisService) throws ApplicationException{
    	if (getFattura_attivaBulk()!=null )
    		return getCMISFolder(this.getFattura_attivaBulk()).getCMISPath(cmisService);
		return null;
	}

	public CMISPath getCMISAlternativeParentPath(SiglaCMISService cmisService) throws ApplicationException{
		CMISPath cmisPath = getCMISFolder(this.getFattura_attivaBulk()).getCMISPrincipalPath(cmisService);
		if (cmisPath != null)
			cmisPath = cmisService.createFolderIfNotPresent(cmisPath, (String)Fattura_attivaBulk.getTipoFatturaKeys().get(this.getFattura_attivaBulk().getTi_fattura()), (String)Fattura_attivaBulk.getTipoFatturaKeys().get(this.getFattura_attivaBulk().getTi_fattura()), (String)Incarichi_archivioBulk.getTipo_archivioKeys().get(this.getFattura_attivaBulk().getTi_fattura()));
		return cmisPath;
	}

	public CMISFolderFatturaAttiva getCMISFolder(Fattura_attivaBulk fattura) {
		return new CMISFolderFatturaAttiva(fattura);
	}

	@CMISProperty(name="sigla_fatture_attachment:original_name")
	public String getOriginalName() {
		return originalName;
	}
	
	@CMISPolicy(name="P:sigla_commons_aspect:utente_applicativo_sigla", property=@CMISProperty(name="sigla_commons_aspect:utente_applicativo"))
	public String getUtenteSigla() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getUtuv();
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	
	public Fattura_attivaBulk getFattura_attivaBulk() {
		return fattura_attivaBulk;
	}

	public void setFattura_attivaBulk(Fattura_attivaBulk fattura_attivaBulk) {
		this.fattura_attivaBulk = fattura_attivaBulk;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public void impostaDatiBaseFattura(String originalName,
			Fattura_attivaBulk fattura) {
    	setFattura_attivaBulk(fattura);
		this.setAuthor(getFattura_attivaBulk().getUtcr());
		this.setTitle((String)Fattura_attivaBulk.getTipoFatturaKeys().get(getFattura_attivaBulk().getTi_fattura()));
		this.setDescription((String)this.getTitle().toString()+
					" - nr."+getFattura_attivaBulk().getEsercizio()+"/"+getFattura_attivaBulk().getPg_fattura_attiva());
		this.setOriginalName(originalName);
	}

}
