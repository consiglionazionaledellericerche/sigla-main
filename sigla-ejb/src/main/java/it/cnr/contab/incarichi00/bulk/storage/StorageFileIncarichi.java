/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.incarichi00.bulk.storage;

import it.cnr.si.spring.storage.StorageDriver;
import it.cnr.si.spring.storage.bulk.StorageTypeName;
import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.si.spring.storage.bulk.StorageFile;
import it.cnr.contab.incarichi00.bulk.Incarichi_archivioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_archivioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_rappBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_varBulk;
import it.cnr.contab.incarichi00.storage.StorageContrattiAttachment;
import it.cnr.contab.incarichi00.storage.StorageContrattiProperty;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.config.StoragePropertyNames;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public class StorageFileIncarichi extends StorageFile implements StorageTypeName {
	private static final long serialVersionUID = -1775673719677028944L;

	private Incarichi_archivioBulk incaricoArchivio;
	
	private String originalName;

	public StorageFileIncarichi(Incarichi_repertorio_archivioBulk incaricoRepertorioArchivio) throws IOException{
		this(File.createTempFile(incaricoRepertorioArchivio.constructCMISNomeFile(),"txt"), 
			 incaricoRepertorioArchivio.constructCMISNomeFile(), 
			 incaricoRepertorioArchivio);
	}

	public StorageFileIncarichi(Incarichi_repertorio_rappBulk incaricoRepertorioRapp) throws IOException{
		this(File.createTempFile(incaricoRepertorioRapp.constructCMISNomeFile(),"txt"), 
			 incaricoRepertorioRapp.constructCMISNomeFile(), 
		     incaricoRepertorioRapp);
	}

	public StorageFileIncarichi(Incarichi_repertorio_varBulk incaricoRepertorioVar) throws IOException{
		this(File.createTempFile(incaricoRepertorioVar.constructCMISNomeFile(),"txt"), 
			 incaricoRepertorioVar.constructCMISNomeFile(), 
			 incaricoRepertorioVar);
	}

	public StorageFileIncarichi(File file, String originalName, Incarichi_repertorio_archivioBulk incaricoRepertorioArchivio) throws IOException{
    	super(file, incaricoRepertorioArchivio.getContentType(), incaricoRepertorioArchivio.constructCMISNomeFile());
    	setIncaricoArchivio(incaricoRepertorioArchivio);
    	initCMISField(incaricoRepertorioArchivio.getEsercizio(), incaricoRepertorioArchivio.getPg_repertorio(), originalName);
    }

	public StorageFileIncarichi(File file, String originalName, Incarichi_repertorio_rappBulk incaricoRepertorioRapp) throws IOException{
    	super(file, incaricoRepertorioRapp.getContentType(), incaricoRepertorioRapp.constructCMISNomeFile());
    	setIncaricoArchivio(incaricoRepertorioRapp);
		this.setOriginalName(originalName);
		initCMISField(incaricoRepertorioRapp.getEsercizio(), incaricoRepertorioRapp.getPg_repertorio(), originalName);
    }

    public StorageFileIncarichi(File file, String originalName, Incarichi_repertorio_varBulk incaricoRepertorioVar) throws IOException{
    	super(file,incaricoRepertorioVar.getContentType(), incaricoRepertorioVar.constructCMISNomeFile());
    	setIncaricoArchivio(incaricoRepertorioVar);
    	initCMISField(incaricoRepertorioVar.getEsercizio(), incaricoRepertorioVar.getPg_repertorio(), originalName);
    }

    public StorageFileIncarichi(StorageObject storageObject, Incarichi_repertorio_archivioBulk incaricoRepertorioArchivio) {
		super(storageObject);
    	setIncaricoArchivio(incaricoRepertorioArchivio);
    	if (storageObject.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_ATTACHMENT_ORIGINAL_NAME.value())!=null)
    		this.setOriginalName(storageObject.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_ATTACHMENT_ORIGINAL_NAME.value()).toString());
    	else
    		this.setOriginalName(incaricoRepertorioArchivio.getNome_file());
	}

    public StorageFileIncarichi(StorageObject storageObject, Incarichi_repertorio_rappBulk incaricoRepertorioRapp) {
		super(storageObject);
		setIncaricoArchivio(incaricoRepertorioRapp);
    	if (storageObject.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_ATTACHMENT_ORIGINAL_NAME.value())!=null)
    		this.setOriginalName(storageObject.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_ATTACHMENT_ORIGINAL_NAME.value()).toString());
    	else
    		this.setOriginalName(incaricoRepertorioRapp.getNome_file());
	}

    public StorageFileIncarichi(StorageObject storageObject, Incarichi_repertorio_varBulk incaricoRepertorioVar) {
		super(storageObject);
		setIncaricoArchivio(incaricoRepertorioVar);
    	if (storageObject.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_ATTACHMENT_ORIGINAL_NAME.value())!=null)
    		this.setOriginalName(storageObject.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_ATTACHMENT_ORIGINAL_NAME.value()).toString());
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

	@StoragePolicy(name="P:sigla_contratti_aspect:incarichi", property=@StorageProperty(name="sigla_contratti_aspect_incarichi:esercizio"))
    public Integer getEsercizioIncarico() {
    	if (getIncaricoArchivio()!=null)
    		if (getIncaricoArchivio() instanceof Incarichi_repertorio_archivioBulk)
    			return ((Incarichi_repertorio_archivioBulk)getIncaricoArchivio()).getEsercizio();
    		else if (getIncaricoArchivio() instanceof Incarichi_repertorio_rappBulk)
    			return ((Incarichi_repertorio_rappBulk)getIncaricoArchivio()).getEsercizio();
    		else if (getIncaricoArchivio() instanceof Incarichi_repertorio_varBulk)
    			return ((Incarichi_repertorio_varBulk)getIncaricoArchivio()).getEsercizio();
    	if (getStorageObject()!=null)
    		return ((BigInteger) getStorageObject().getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_INCARICHI_ESERCIZIO.value())).intValue();
    	return null;
    }

	@StoragePolicy(name="P:sigla_contratti_aspect:incarichi", property=@StorageProperty(name="sigla_contratti_aspect_incarichi:progressivo", converterBeanName="storage.converter.longToIntegerConverter"))
    public Long getPgIncarico() {
    	if (getIncaricoArchivio()!=null)
    		if (getIncaricoArchivio() instanceof Incarichi_repertorio_archivioBulk)
    			return ((Incarichi_repertorio_archivioBulk)getIncaricoArchivio()).getPg_repertorio();
    		else if (getIncaricoArchivio() instanceof Incarichi_repertorio_rappBulk)
    			return ((Incarichi_repertorio_rappBulk)getIncaricoArchivio()).getPg_repertorio();
    		else if (getIncaricoArchivio() instanceof Incarichi_repertorio_varBulk)
    			return ((Incarichi_repertorio_varBulk)getIncaricoArchivio()).getPg_repertorio();
    	if (getStorageObject()!=null)
    		return ((BigInteger) getStorageObject().getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_INCARICHI_PROGRESSIVO.value())).longValue();
    	return null;
    }

	@StoragePolicy(name="P:sigla_contratti_aspect:procedura", property=@StorageProperty(name="sigla_contratti_aspect_procedura:esercizio"))
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

	@StoragePolicy(name="P:sigla_contratti_aspect:procedura", property=@StorageProperty(name="sigla_contratti_aspect_procedura:progressivo", converterBeanName="storage.converter.longToIntegerConverter"))
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
    			return StorageContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_DICHIARAZIONE_ALTRI_RAPPORTI.value();
    		else if (getIncaricoArchivio() instanceof Incarichi_repertorio_varBulk)
    			return StorageContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_VARIAZIONE_CONTRATTO.value();
    		else
   	    		return getIncaricoArchivio().isBando()? StorageContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_BANDO.value():
	   	    			getIncaricoArchivio().isDecisioneAContrattare()? StorageContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_DECISIONE_A_CONTRATTARE.value():
	   	    				getIncaricoArchivio().isContratto()? StorageContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_CONTRATTO.value():
	   	    					getIncaricoArchivio().isCurriculumVincitore()? StorageContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_CURRICULUM_VINCITORE.value():
	   	    						getIncaricoArchivio().isDecretoDiNomina()? StorageContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_DECRETO_NOMINA.value():
	   	    							getIncaricoArchivio().isAttoEsitoControllo()? StorageContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_ATTO_ESITO_CONTROLLO.value():
	   	    								getIncaricoArchivio().isProgetto()? StorageContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_PROGETTO.value():
												getIncaricoArchivio().isConflittoInteressi()? StorageContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_CONFLITTO_INTERESSI.value():
	   	    		StorageContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_ALLEGATO_GENERICO.value();
    	return "cmis:document";
	}
	@Override
	public String getStorageParentPath() {
    	if (getIncaricoArchivio()!=null) {
    		if (getIncaricoArchivio() instanceof Incarichi_repertorio_archivioBulk &&
       			((Incarichi_repertorio_archivioBulk)getIncaricoArchivio()).getIncarichi_repertorio()!=null)
       			return ((Incarichi_repertorio_archivioBulk)getIncaricoArchivio()).getIncarichi_repertorio().getCMISFolder().getCMISPath();
       		else if (getIncaricoArchivio() instanceof Incarichi_repertorio_rappBulk &&
       			((Incarichi_repertorio_rappBulk)getIncaricoArchivio()).getIncarichi_repertorio()!=null)
       			return ((Incarichi_repertorio_rappBulk)getIncaricoArchivio()).getIncarichi_repertorio().getCMISFolder().getCMISPath();
       		else if (getIncaricoArchivio() instanceof Incarichi_repertorio_varBulk &&
       			((Incarichi_repertorio_varBulk)getIncaricoArchivio()).getIncarichi_repertorio()!=null)
       			return ((Incarichi_repertorio_varBulk)getIncaricoArchivio()).getIncarichi_repertorio().getCMISFolder().getCMISPath();
    	}
		return null;
	}
	@Override
	public String getStorageAlternativeParentPath() {
		String cmisPath = null;
    	if (getIncaricoArchivio()!=null) {
    		if (getIncaricoArchivio() instanceof Incarichi_repertorio_archivioBulk &&
       			((Incarichi_repertorio_archivioBulk)getIncaricoArchivio()).getIncarichi_repertorio()!=null &&
       			((Incarichi_repertorio_archivioBulk)getIncaricoArchivio()).getIncarichi_repertorio().getIncarichi_procedura()!=null)
    			cmisPath = ((Incarichi_repertorio_archivioBulk)getIncaricoArchivio()).getIncarichi_repertorio().getIncarichi_procedura().getCMISFolder().getCMISPrincipalPath();
       		else if (getIncaricoArchivio() instanceof Incarichi_repertorio_rappBulk &&
       			((Incarichi_repertorio_rappBulk)getIncaricoArchivio()).getIncarichi_repertorio()!=null)
    			cmisPath = ((Incarichi_repertorio_rappBulk)getIncaricoArchivio()).getIncarichi_repertorio().getIncarichi_procedura().getCMISFolder().getCMISPrincipalPath();
       		else if (getIncaricoArchivio() instanceof Incarichi_repertorio_varBulk &&
       			((Incarichi_repertorio_varBulk)getIncaricoArchivio()).getIncarichi_repertorio()!=null)
    			cmisPath = ((Incarichi_repertorio_varBulk)getIncaricoArchivio()).getIncarichi_repertorio().getIncarichi_procedura().getCMISFolder().getCMISPrincipalPath();
    		if (cmisPath!=null)
    			cmisPath = cmisPath.concat(StorageDriver.SUFFIX).concat((String)Incarichi_archivioBulk.getTipo_archivioKeys().get(getIncaricoArchivio().getTipo_archivio()));
    	}
		return cmisPath;
	}

	@StorageProperty(name="sigla_contratti_attachment:original_name")
	public String getOriginalName() {
		return originalName;
	}
	
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public boolean isEqualsTo(StorageObject storageObject, List<String> listError){
		String initTesto = "Procedura "+this.getEsercizioProcedura().toString()+"/"+this.getPgProcedura().toString()+" - " +
						   "Incarico "+this.getEsercizioIncarico().toString()+"/"+this.getPgIncarico().toString()+" - Disallineamento dato ";
		boolean isEquals = true;
		String valueDB=null, valueCMIS=null; 

		valueDB=String.valueOf(this.getEsercizioProcedura());
		valueCMIS=String.valueOf(storageObject.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_PROCEDURA_ESERCIZIO.value()));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Esercizio Procedura - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getPgProcedura());
		valueCMIS=String.valueOf(storageObject.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_PROCEDURA_PROGRESSIVO.value()));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Pg_procedura - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getEsercizioIncarico());
		valueCMIS=String.valueOf(storageObject.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_INCARICHI_ESERCIZIO.value()));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Esercizio Incarico - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getPgIncarico());
		valueCMIS=String.valueOf(storageObject.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_INCARICHI_PROGRESSIVO.value()));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Pg_repertorio - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getOriginalName());
		valueCMIS=String.valueOf(storageObject.getPropertyValue("sigla_contratti_attachment:original_name"));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Nome Originale File - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getTypeName());
		valueCMIS=String.valueOf(storageObject.getPropertyValue(StoragePropertyNames.OBJECT_TYPE_ID.value()));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Type documento - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		return isEquals;
	}	
}
