package it.cnr.contab.incarichi00.bulk.cmis;

import it.cnr.cmisdl.model.Node;
import it.cnr.contab.cmis.CMISTypeName;
import it.cnr.contab.cmis.annotation.CMISPolicy;
import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.contab.cmis.bulk.CMISFile;
import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.cmis.service.CMISService;
import it.cnr.contab.incarichi00.bulk.Incarichi_archivioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_procedura_archivioBulk;
import it.cnr.contab.incarichi00.cmis.CMISContrattiAttachment;
import it.cnr.contab.incarichi00.cmis.CMISContrattiProperty;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public class CMISFileProcedura extends CMISFile implements CMISTypeName{
	private static final long serialVersionUID = -1775673719677028944L;

	private Incarichi_procedura_archivioBulk incaricoProceduraArchivio;

	private String originalName;
	
	public CMISFileProcedura() {
		super();
	}

	public CMISFileProcedura(Incarichi_procedura_archivioBulk incaricoProceduraArchivio) throws IOException{
		this(File.createTempFile(incaricoProceduraArchivio.constructCMISNomeFile(),"txt"), 
			 incaricoProceduraArchivio.constructCMISNomeFile(), 
			 incaricoProceduraArchivio);
	}

	public CMISFileProcedura(File file, String originalName, Incarichi_procedura_archivioBulk incaricoProceduraArchivio) throws IOException{
    	super(file, incaricoProceduraArchivio.getContentType(), incaricoProceduraArchivio.constructCMISNomeFile());
    	setIncaricoProceduraArchivio(incaricoProceduraArchivio);
		this.setAuthor(getIncaricoProceduraArchivio().getUtcr());
		this.setTitle((String)Incarichi_archivioBulk.getTipo_archivioKeys().get(getIncaricoProceduraArchivio().getTipo_archivio()));
		this.setDescription((String)Incarichi_archivioBulk.getTipo_archivioKeys().get(getIncaricoProceduraArchivio().getTipo_archivio()).toString()+
					" - Procedura di conferimento incarico nr."+getIncaricoProceduraArchivio().getEsercizio()+"/"+getIncaricoProceduraArchivio().getPg_procedura());
		this.setOriginalName(originalName);
    }

    public CMISFileProcedura(Node node, Incarichi_procedura_archivioBulk incaricoProceduraArchivio) {
		super(node);
    	setIncaricoProceduraArchivio(incaricoProceduraArchivio);
		this.setOriginalName((String)node.getPropertyValue(CMISContrattiProperty.SIGLA_CONTRATTI_ATTACHMENT_ORIGINAL_NAME.value()));
	}
	
    private void setIncaricoProceduraArchivio(Incarichi_procedura_archivioBulk incaricoProceduraArchivio) {
		this.incaricoProceduraArchivio = incaricoProceduraArchivio;
	}
    
    protected Incarichi_procedura_archivioBulk getIncaricoProceduraArchivio() {
		return this.incaricoProceduraArchivio;
	}

	@CMISPolicy(name="P:sigla_contratti_aspect:procedura", property=@CMISProperty(name="sigla_contratti_aspect_procedura:esercizio"))
    public Integer getEsercizioProcedura() {
    	if (getIncaricoProceduraArchivio()!=null)
    		return getIncaricoProceduraArchivio().getEsercizio();
    	if (getNode()!=null)
    		return ((BigInteger) getNode().getPropertyValue(CMISContrattiProperty.SIGLA_CONTRATTI_PROCEDURA_ESERCIZIO.value())).intValue();
    	return null;
    }

	@CMISPolicy(name="P:sigla_contratti_aspect:procedura", property=@CMISProperty(name="sigla_contratti_aspect_procedura:progressivo", converterBeanName="cmis.converter.longToIntegerConverter"))
    public Long getPgProcedura() {
    	if (getIncaricoProceduraArchivio()!=null)
    		return getIncaricoProceduraArchivio().getPg_procedura();
    	if (getNode()!=null)
    		return ((BigInteger) getNode().getPropertyValue(CMISContrattiProperty.SIGLA_CONTRATTI_PROCEDURA_PROGRESSIVO.value())).longValue();
    	return null;
    }
	
	public String getTypeName() {
		return getTypeName(getIncaricoProceduraArchivio());
	}

	protected String getTypeName(Incarichi_archivioBulk archivio) {
    	if (archivio!=null)
    		return archivio.isBando()?CMISContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_BANDO.value():
    				archivio.isDecisioneAContrattare()?CMISContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_DECISIONE_A_CONTRATTARE.value():
    					archivio.isContratto()?CMISContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_CONTRATTO.value():
    						archivio.isCurriculumVincitore()?CMISContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_CURRICULUM_VINCITORE.value():
    							archivio.isDecretoDiNomina()?CMISContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_DECRETO_NOMINA.value():
    								archivio.isAttoEsitoControllo()?CMISContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_ATTO_ESITO_CONTROLLO.value():
    									archivio.isProgetto()?CMISContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_PROGETTO.value():
    				CMISContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_ALLEGATO_GENERICO.value();
    	return "cmis:document";
	}

	public CMISPath getCMISParentPath(CMISService cmisService){
    	if (getIncaricoProceduraArchivio()!=null && getIncaricoProceduraArchivio().getIncarichi_procedura()!=null)
    		return getIncaricoProceduraArchivio().getIncarichi_procedura().getCMISFolder().getCMISPath(cmisService);
		return null;
	}

	public CMISPath getCMISAlternativeParentPath(CMISService cmisService){
		CMISPath cmisPath = this.getIncaricoProceduraArchivio().getIncarichi_procedura().getCMISFolder().getCMISPrincipalPath(cmisService);
		if (cmisPath != null)
			cmisPath = cmisService.createFolderIfNotPresent(cmisPath, (String)Incarichi_archivioBulk.getTipo_archivioKeys().get(this.getIncaricoProceduraArchivio().getTipo_archivio()), (String)Incarichi_archivioBulk.getTipo_archivioKeys().get(this.getIncaricoProceduraArchivio().getTipo_archivio()), (String)Incarichi_archivioBulk.getTipo_archivioKeys().get(this.getIncaricoProceduraArchivio().getTipo_archivio()));
		return cmisPath;
	}

	@CMISProperty(name="sigla_contratti_attachment:original_name")
	public String getOriginalName() {
		return originalName;
	}
	
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	
	@CMISPolicy(name="P:sigla_contratti_aspect:link", property=@CMISProperty(name="sigla_contratti_aspect_link:url"))
	public String getLinkUrl() {
		if (this.getIncaricoProceduraArchivio()!=null)
			return this.getIncaricoProceduraArchivio().getUrl_file();
		return null;
	}
	
	public boolean isEqualsTo(Node node, List<String> listError){
		String initTesto = "Procedura "+this.getEsercizioProcedura().toString()+"/"+this.getPgProcedura().toString()+" - Disallineamento dato ";
		boolean isEquals = true;
		String valueDB=null, valueCMIS=null; 

		valueDB=String.valueOf(this.getEsercizioProcedura());
		valueCMIS=String.valueOf(node.getPropertyValue(CMISContrattiProperty.SIGLA_CONTRATTI_PROCEDURA_ESERCIZIO.value()));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Esercizio Procedura - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getPgProcedura());
		valueCMIS=String.valueOf(node.getPropertyValue(CMISContrattiProperty.SIGLA_CONTRATTI_PROCEDURA_PROGRESSIVO.value()));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Pg_procedura - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getOriginalName());
		valueCMIS=String.valueOf(node.getPropertyValue("sigla_contratti_attachment:original_name"));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Nome Originale File - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getLinkUrl());
		valueCMIS=String.valueOf(node.getPropertyValue(CMISContrattiProperty.SIGLA_CONTRATTI_LINK_URL.value()));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Link URL - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getTypeName());
		valueCMIS=String.valueOf(node.getTypeId());
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Type documento - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		return isEquals;
	}	
}
