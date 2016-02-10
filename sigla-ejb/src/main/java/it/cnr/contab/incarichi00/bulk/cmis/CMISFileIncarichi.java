package it.cnr.contab.incarichi00.bulk.cmis;

import it.cnr.contab.cmis.CMISTypeName;
import it.cnr.contab.cmis.annotation.CMISPolicy;
import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.contab.cmis.bulk.CMISFile;
import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.cmis.service.SiglaCMISService;
import it.cnr.contab.incarichi00.bulk.Incarichi_archivioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_archivioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_rappBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_varBulk;
import it.cnr.contab.incarichi00.cmis.CMISContrattiAttachment;
import it.cnr.contab.incarichi00.cmis.CMISContrattiProperty;
import it.cnr.jada.comp.ApplicationException;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.Document;

public class CMISFileIncarichi extends CMISFile implements CMISTypeName{
	private static final long serialVersionUID = -1775673719677028944L;

	private Incarichi_archivioBulk incaricoArchivio;
	
	private String originalName;

	public CMISFileIncarichi(Incarichi_repertorio_archivioBulk incaricoRepertorioArchivio) throws IOException{
		this(File.createTempFile(incaricoRepertorioArchivio.constructCMISNomeFile(),"txt"), 
			 incaricoRepertorioArchivio.constructCMISNomeFile(), 
			 incaricoRepertorioArchivio);
	}

	public CMISFileIncarichi(Incarichi_repertorio_rappBulk incaricoRepertorioRapp) throws IOException{
		this(File.createTempFile(incaricoRepertorioRapp.constructCMISNomeFile(),"txt"), 
			 incaricoRepertorioRapp.constructCMISNomeFile(), 
		     incaricoRepertorioRapp);
	}

	public CMISFileIncarichi(Incarichi_repertorio_varBulk incaricoRepertorioVar) throws IOException{
		this(File.createTempFile(incaricoRepertorioVar.constructCMISNomeFile(),"txt"), 
			 incaricoRepertorioVar.constructCMISNomeFile(), 
			 incaricoRepertorioVar);
	}

	public CMISFileIncarichi(File file, String originalName, Incarichi_repertorio_archivioBulk incaricoRepertorioArchivio) throws IOException{
    	super(file, incaricoRepertorioArchivio.getContentType(), incaricoRepertorioArchivio.constructCMISNomeFile());
    	setIncaricoArchivio(incaricoRepertorioArchivio);
    	initCMISField(incaricoRepertorioArchivio.getEsercizio(), incaricoRepertorioArchivio.getPg_repertorio(), originalName);
    }

	public CMISFileIncarichi(File file, String originalName, Incarichi_repertorio_rappBulk incaricoRepertorioRapp) throws IOException{
    	super(file, incaricoRepertorioRapp.getContentType(), incaricoRepertorioRapp.constructCMISNomeFile());
    	setIncaricoArchivio(incaricoRepertorioRapp);
		this.setOriginalName(originalName);
		initCMISField(incaricoRepertorioRapp.getEsercizio(), incaricoRepertorioRapp.getPg_repertorio(), originalName);
    }

    public CMISFileIncarichi(File file, String originalName, Incarichi_repertorio_varBulk incaricoRepertorioVar) throws IOException{
    	super(file,incaricoRepertorioVar.getContentType(), incaricoRepertorioVar.constructCMISNomeFile());
    	setIncaricoArchivio(incaricoRepertorioVar);
    	initCMISField(incaricoRepertorioVar.getEsercizio(), incaricoRepertorioVar.getPg_repertorio(), originalName);
    }

    public CMISFileIncarichi(Document node, Incarichi_repertorio_archivioBulk incaricoRepertorioArchivio) {
		super(node);
    	setIncaricoArchivio(incaricoRepertorioArchivio);
    	if (node.getPropertyValue(CMISContrattiProperty.SIGLA_CONTRATTI_ATTACHMENT_ORIGINAL_NAME.value())!=null)
    		this.setOriginalName(node.getPropertyValue(CMISContrattiProperty.SIGLA_CONTRATTI_ATTACHMENT_ORIGINAL_NAME.value()).toString());
    	else
    		this.setOriginalName(incaricoRepertorioArchivio.getNome_file());
	}

    public CMISFileIncarichi(Document node, Incarichi_repertorio_rappBulk incaricoRepertorioRapp) {
		super(node);
		setIncaricoArchivio(incaricoRepertorioRapp);
    	if (node.getPropertyValue(CMISContrattiProperty.SIGLA_CONTRATTI_ATTACHMENT_ORIGINAL_NAME.value())!=null)
    		this.setOriginalName(node.getPropertyValue(CMISContrattiProperty.SIGLA_CONTRATTI_ATTACHMENT_ORIGINAL_NAME.value()).toString());
    	else
    		this.setOriginalName(incaricoRepertorioRapp.getNome_file());
	}

    public CMISFileIncarichi(Document node, Incarichi_repertorio_varBulk incaricoRepertorioVar) {
		super(node);
		setIncaricoArchivio(incaricoRepertorioVar);
    	if (node.getPropertyValue(CMISContrattiProperty.SIGLA_CONTRATTI_ATTACHMENT_ORIGINAL_NAME.value())!=null)
    		this.setOriginalName(node.getPropertyValue(CMISContrattiProperty.SIGLA_CONTRATTI_ATTACHMENT_ORIGINAL_NAME.value()).toString());
    	else
    		this.setOriginalName(incaricoRepertorioVar.getNome_file());
	}
    
	private void initCMISField(Integer esercizio, Long progressivo, String originalName){
	    this.setAuthor(getIncaricoArchivio().getUtcr());
		if (getIncaricoArchivio() instanceof Incarichi_repertorio_rappBulk) {
			this.setTitle("Dichiarazione Altri Rapporti");
			this.setDescription("Dichiarazione Altri Rapporti - Incarico nr."+esercizio+"/"+progressivo);
		} else {
			this.setTitle((String)Incarichi_archivioBulk.getTipo_archivioKeys().get(getIncaricoArchivio().getTipo_archivio()));
			this.setDescription((String)Incarichi_archivioBulk.getTipo_archivioKeys().get(getIncaricoArchivio().getTipo_archivio()).toString()+
						" - Incarico nr."+esercizio+"/"+progressivo);
		}
		this.setOriginalName(originalName);
	}

    private void setIncaricoArchivio(Incarichi_archivioBulk incaricoArchivio) {
		this.incaricoArchivio = incaricoArchivio;
	}
    
    protected Incarichi_archivioBulk getIncaricoArchivio() {
		return incaricoArchivio;
	}

	@CMISPolicy(name="P:sigla_contratti_aspect:incarichi", property=@CMISProperty(name="sigla_contratti_aspect_incarichi:esercizio"))
    public Integer getEsercizioIncarico() {
    	if (getIncaricoArchivio()!=null)
    		if (getIncaricoArchivio() instanceof Incarichi_repertorio_archivioBulk)
    			return ((Incarichi_repertorio_archivioBulk)getIncaricoArchivio()).getEsercizio();
    		else if (getIncaricoArchivio() instanceof Incarichi_repertorio_rappBulk)
    			return ((Incarichi_repertorio_rappBulk)getIncaricoArchivio()).getEsercizio();
    		else if (getIncaricoArchivio() instanceof Incarichi_repertorio_varBulk)
    			return ((Incarichi_repertorio_varBulk)getIncaricoArchivio()).getEsercizio();
    	if (getDocument()!=null)
    		return ((BigInteger) getDocument().getPropertyValue(CMISContrattiProperty.SIGLA_CONTRATTI_INCARICHI_ESERCIZIO.value())).intValue();
    	return null;
    }

	@CMISPolicy(name="P:sigla_contratti_aspect:incarichi", property=@CMISProperty(name="sigla_contratti_aspect_incarichi:progressivo", converterBeanName="cmis.converter.longToIntegerConverter"))
    public Long getPgIncarico() {
    	if (getIncaricoArchivio()!=null)
    		if (getIncaricoArchivio() instanceof Incarichi_repertorio_archivioBulk)
    			return ((Incarichi_repertorio_archivioBulk)getIncaricoArchivio()).getPg_repertorio();
    		else if (getIncaricoArchivio() instanceof Incarichi_repertorio_rappBulk)
    			return ((Incarichi_repertorio_rappBulk)getIncaricoArchivio()).getPg_repertorio();
    		else if (getIncaricoArchivio() instanceof Incarichi_repertorio_varBulk)
    			return ((Incarichi_repertorio_varBulk)getIncaricoArchivio()).getPg_repertorio();
    	if (getDocument()!=null)
    		return ((BigInteger) getDocument().getPropertyValue(CMISContrattiProperty.SIGLA_CONTRATTI_INCARICHI_PROGRESSIVO.value())).longValue();
    	return null;
    }

	@CMISPolicy(name="P:sigla_contratti_aspect:procedura", property=@CMISProperty(name="sigla_contratti_aspect_procedura:esercizio"))
    public Integer getEsercizioProcedura() {
    	if (getIncaricoArchivio()!=null)
    		if (getIncaricoArchivio() instanceof Incarichi_repertorio_archivioBulk &&
    			((Incarichi_repertorio_archivioBulk)getIncaricoArchivio()).getIncarichi_repertorio()!=null)
    			return ((Incarichi_repertorio_archivioBulk)getIncaricoArchivio()).getIncarichi_repertorio().getEsercizio_procedura();
    		else if (getIncaricoArchivio() instanceof Incarichi_repertorio_rappBulk &&
    			((Incarichi_repertorio_rappBulk)getIncaricoArchivio()).getIncarichi_repertorio()!=null)
    			return ((Incarichi_repertorio_rappBulk)getIncaricoArchivio()).getIncarichi_repertorio().getEsercizio_procedura();
    		else if (getIncaricoArchivio() instanceof Incarichi_repertorio_varBulk &&
    			((Incarichi_repertorio_varBulk)getIncaricoArchivio()).getIncarichi_repertorio()!=null)
    			return ((Incarichi_repertorio_varBulk)getIncaricoArchivio()).getIncarichi_repertorio().getEsercizio_procedura();
    	return null;
    }

	@CMISPolicy(name="P:sigla_contratti_aspect:procedura", property=@CMISProperty(name="sigla_contratti_aspect_procedura:progressivo", converterBeanName="cmis.converter.longToIntegerConverter"))
    public Long getPgProcedura() {
    	if (getIncaricoArchivio()!=null)
    		if (getIncaricoArchivio() instanceof Incarichi_repertorio_archivioBulk &&
    			((Incarichi_repertorio_archivioBulk)getIncaricoArchivio()).getIncarichi_repertorio()!=null)
    			return ((Incarichi_repertorio_archivioBulk)getIncaricoArchivio()).getIncarichi_repertorio().getPg_procedura();
    		else if (getIncaricoArchivio() instanceof Incarichi_repertorio_rappBulk &&
    			((Incarichi_repertorio_rappBulk)getIncaricoArchivio()).getIncarichi_repertorio()!=null)
    			return ((Incarichi_repertorio_rappBulk)getIncaricoArchivio()).getIncarichi_repertorio().getPg_procedura();
    		else if (getIncaricoArchivio() instanceof Incarichi_repertorio_varBulk &&
    			((Incarichi_repertorio_varBulk)getIncaricoArchivio()).getIncarichi_repertorio()!=null)
    			return ((Incarichi_repertorio_varBulk)getIncaricoArchivio()).getIncarichi_repertorio().getPg_procedura();
    	return null;
    }

	public String getTypeName() {
    	if (getIncaricoArchivio()!=null)
    		if (getIncaricoArchivio() instanceof Incarichi_repertorio_rappBulk)
    			return CMISContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_DICHIARAZIONE_ALTRI_RAPPORTI.value();
    		else if (getIncaricoArchivio() instanceof Incarichi_repertorio_varBulk)
    			return CMISContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_VARIAZIONE_CONTRATTO.value();
    		else
   	    		return getIncaricoArchivio().isBando()?CMISContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_BANDO.value():
	   	    			getIncaricoArchivio().isDecisioneAContrattare()?CMISContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_DECISIONE_A_CONTRATTARE.value():
	   	    				getIncaricoArchivio().isContratto()?CMISContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_CONTRATTO.value():
	   	    					getIncaricoArchivio().isCurriculumVincitore()?CMISContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_CURRICULUM_VINCITORE.value():
	   	    						getIncaricoArchivio().isDecretoDiNomina()?CMISContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_DECRETO_NOMINA.value():
	   	    							getIncaricoArchivio().isAttoEsitoControllo()?CMISContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_ATTO_ESITO_CONTROLLO.value():
	   	    								getIncaricoArchivio().isProgetto()?CMISContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_PROGETTO.value():
	   	    		CMISContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_ALLEGATO_GENERICO.value();
    	return "cmis:document";
	}

	public CMISPath getCMISParentPath(SiglaCMISService cmisService) throws ApplicationException{
    	if (getIncaricoArchivio()!=null) {
    		if (getIncaricoArchivio() instanceof Incarichi_repertorio_archivioBulk &&
       			((Incarichi_repertorio_archivioBulk)getIncaricoArchivio()).getIncarichi_repertorio()!=null)
       			return ((Incarichi_repertorio_archivioBulk)getIncaricoArchivio()).getIncarichi_repertorio().getCMISFolder().getCMISPath(cmisService);
       		else if (getIncaricoArchivio() instanceof Incarichi_repertorio_rappBulk &&
       			((Incarichi_repertorio_rappBulk)getIncaricoArchivio()).getIncarichi_repertorio()!=null)
       			return ((Incarichi_repertorio_rappBulk)getIncaricoArchivio()).getIncarichi_repertorio().getCMISFolder().getCMISPath(cmisService);
       		else if (getIncaricoArchivio() instanceof Incarichi_repertorio_varBulk &&
       			((Incarichi_repertorio_varBulk)getIncaricoArchivio()).getIncarichi_repertorio()!=null)
       			return ((Incarichi_repertorio_varBulk)getIncaricoArchivio()).getIncarichi_repertorio().getCMISFolder().getCMISPath(cmisService);
    	}
		return null;
	}
	
	public CMISPath getCMISAlternativeParentPath(SiglaCMISService cmisService) throws ApplicationException{
		CMISPath cmisPath = null;
    	if (getIncaricoArchivio()!=null) {
    		if (getIncaricoArchivio() instanceof Incarichi_repertorio_archivioBulk &&
       			((Incarichi_repertorio_archivioBulk)getIncaricoArchivio()).getIncarichi_repertorio()!=null &&
       			((Incarichi_repertorio_archivioBulk)getIncaricoArchivio()).getIncarichi_repertorio().getIncarichi_procedura()!=null)
    			cmisPath = ((Incarichi_repertorio_archivioBulk)getIncaricoArchivio()).getIncarichi_repertorio().getIncarichi_procedura().getCMISFolder().getCMISPrincipalPath(cmisService);
       		else if (getIncaricoArchivio() instanceof Incarichi_repertorio_rappBulk &&
       			((Incarichi_repertorio_rappBulk)getIncaricoArchivio()).getIncarichi_repertorio()!=null)
    			cmisPath = ((Incarichi_repertorio_rappBulk)getIncaricoArchivio()).getIncarichi_repertorio().getIncarichi_procedura().getCMISFolder().getCMISPrincipalPath(cmisService);
       		else if (getIncaricoArchivio() instanceof Incarichi_repertorio_varBulk &&
       			((Incarichi_repertorio_varBulk)getIncaricoArchivio()).getIncarichi_repertorio()!=null)
    			cmisPath = ((Incarichi_repertorio_varBulk)getIncaricoArchivio()).getIncarichi_repertorio().getIncarichi_procedura().getCMISFolder().getCMISPrincipalPath(cmisService);
    		if (cmisPath!=null)
    			cmisPath = cmisService.createFolderIfNotPresent(cmisPath, (String)Incarichi_archivioBulk.getTipo_archivioKeys().get(getIncaricoArchivio().getTipo_archivio()), (String)Incarichi_archivioBulk.getTipo_archivioKeys().get(getIncaricoArchivio().getTipo_archivio()), (String)Incarichi_archivioBulk.getTipo_archivioKeys().get(getIncaricoArchivio().getTipo_archivio()));
    	}
		return cmisPath;
	}


	@CMISProperty(name="sigla_contratti_attachment:original_name")
	public String getOriginalName() {
		return originalName;
	}
	
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public boolean isEqualsTo(Document node, List<String> listError){
		String initTesto = "Procedura "+this.getEsercizioProcedura().toString()+"/"+this.getPgProcedura().toString()+" - " +
						   "Incarico "+this.getEsercizioIncarico().toString()+"/"+this.getPgIncarico().toString()+" - Disallineamento dato ";
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

		valueDB=String.valueOf(this.getEsercizioIncarico());
		valueCMIS=String.valueOf(node.getPropertyValue(CMISContrattiProperty.SIGLA_CONTRATTI_INCARICHI_ESERCIZIO.value()));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Esercizio Incarico - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getPgIncarico());
		valueCMIS=String.valueOf(node.getPropertyValue(CMISContrattiProperty.SIGLA_CONTRATTI_INCARICHI_PROGRESSIVO.value()));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Pg_repertorio - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getOriginalName());
		valueCMIS=String.valueOf(node.getPropertyValue("sigla_contratti_attachment:original_name"));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Nome Originale File - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getTypeName());
		valueCMIS=String.valueOf(node.getType().getId());
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Type documento - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		return isEquals;
	}	
}
