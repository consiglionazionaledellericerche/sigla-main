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
import it.cnr.si.spring.storage.bulk.StorageFile;
import it.cnr.si.spring.storage.bulk.StorageTypeName;
import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.contab.incarichi00.bulk.Incarichi_archivioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_procedura_archivioBulk;
import it.cnr.contab.incarichi00.storage.StorageContrattiAttachment;
import it.cnr.contab.incarichi00.storage.StorageContrattiProperty;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.config.StoragePropertyNames;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public class StorageFileProcedura extends StorageFile implements StorageTypeName {
	private static final long serialVersionUID = -1775673719677028944L;

	private Incarichi_procedura_archivioBulk incaricoProceduraArchivio;

	private String originalName;
	
	public StorageFileProcedura() {
		super();
	}

	public StorageFileProcedura(Incarichi_procedura_archivioBulk incaricoProceduraArchivio) throws IOException{
		this(File.createTempFile(incaricoProceduraArchivio.constructCMISNomeFile(),"txt"), 
			 incaricoProceduraArchivio.constructCMISNomeFile(), 
			 incaricoProceduraArchivio);
	}

	public StorageFileProcedura(File file, String originalName, Incarichi_procedura_archivioBulk incaricoProceduraArchivio) throws IOException{
    	super(file, incaricoProceduraArchivio.getContentType(), incaricoProceduraArchivio.constructCMISNomeFile());
    	setIncaricoProceduraArchivio(incaricoProceduraArchivio);
		this.setAuthor(getIncaricoProceduraArchivio().getUtcr());
		this.setTitle((String)Incarichi_archivioBulk.getTipo_archivioKeys().get(getIncaricoProceduraArchivio().getTipo_archivio()));
		this.setDescription((String)Incarichi_archivioBulk.getTipo_archivioKeys().get(getIncaricoProceduraArchivio().getTipo_archivio()).toString()+
					" - Procedura di conferimento incarico nr."+getIncaricoProceduraArchivio().getEsercizio()+"/"+getIncaricoProceduraArchivio().getPg_procedura());
		this.setOriginalName(originalName);
    }

    public StorageFileProcedura(StorageObject storageObject, Incarichi_procedura_archivioBulk incaricoProceduraArchivio) {
		super(storageObject);
    	setIncaricoProceduraArchivio(incaricoProceduraArchivio);
    	if (storageObject.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_ATTACHMENT_ORIGINAL_NAME.value())!=null)
    		this.setOriginalName(storageObject.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_ATTACHMENT_ORIGINAL_NAME.value()).toString());
    	else
    		this.setOriginalName(incaricoProceduraArchivio.getNome_file());
	}
	
    private void setIncaricoProceduraArchivio(Incarichi_procedura_archivioBulk incaricoProceduraArchivio) {
		this.incaricoProceduraArchivio = incaricoProceduraArchivio;
	}
    
    protected Incarichi_procedura_archivioBulk getIncaricoProceduraArchivio() {
		return this.incaricoProceduraArchivio;
	}

	@StoragePolicy(name="P:sigla_contratti_aspect:procedura", property=@StorageProperty(name="sigla_contratti_aspect_procedura:esercizio"))
    public Integer getEsercizioProcedura() {
    	if (getIncaricoProceduraArchivio()!=null)
    		return getIncaricoProceduraArchivio().getEsercizio();
    	if (getStorageObject()!=null)
    		return ((BigInteger) getStorageObject().getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_PROCEDURA_ESERCIZIO.value())).intValue();
    	return null;
    }

	@StoragePolicy(name="P:sigla_contratti_aspect:procedura", property=@StorageProperty(name="sigla_contratti_aspect_procedura:progressivo", converterBeanName="storage.converter.longToIntegerConverter"))
    public Long getPgProcedura() {
    	if (getIncaricoProceduraArchivio()!=null)
    		return getIncaricoProceduraArchivio().getPg_procedura();
    	if (getStorageObject()!=null)
    		return ((BigInteger) getStorageObject().getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_PROCEDURA_PROGRESSIVO.value())).longValue();
    	return null;
    }
	
	public String getTypeName() {
		return getTypeName(getIncaricoProceduraArchivio());
	}

	protected String getTypeName(Incarichi_archivioBulk archivio) {
    	if (archivio!=null)
    		return archivio.isBando()? StorageContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_BANDO.value():
    				archivio.isDecisioneAContrattare()? StorageContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_DECISIONE_A_CONTRATTARE.value():
    					archivio.isContratto()? StorageContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_CONTRATTO.value():
    						archivio.isCurriculumVincitore()? StorageContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_CURRICULUM_VINCITORE.value():
    							archivio.isDecretoDiNomina()? StorageContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_DECRETO_NOMINA.value():
    								archivio.isAttoEsitoControllo()? StorageContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_ATTO_ESITO_CONTROLLO.value():
    									archivio.isProgetto()? StorageContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_PROGETTO.value():
											archivio.isConflittoInteressi()? StorageContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_CONFLITTO_INTERESSI.value():
												archivio.isAttestazioneDirettore()? StorageContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_ATTESTAZIONE_DIRETTORE.value():
    				StorageContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_ALLEGATO_GENERICO.value();
    	return "cmis:document";
	}
	@Override
	public String getStorageParentPath() {
    	if (getIncaricoProceduraArchivio()!=null && getIncaricoProceduraArchivio().getIncarichi_procedura()!=null)
    		return getIncaricoProceduraArchivio().getIncarichi_procedura().getCMISFolder().getCMISPath();
		return null;
	}
	@Override
	public String getStorageAlternativeParentPath() {
		String cmisPath = this.getIncaricoProceduraArchivio().getIncarichi_procedura().getCMISFolder().getCMISPrincipalPath();
		if (cmisPath != null)
			cmisPath = cmisPath.concat(StorageDriver.SUFFIX).concat((String)Incarichi_archivioBulk.getTipo_archivioKeys().get(this.getIncaricoProceduraArchivio().getTipo_archivio()));
		return cmisPath;
	}

	@StorageProperty(name="sigla_contratti_attachment:original_name")
	public String getOriginalName() {
		return originalName;
	}
	
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	
	@StoragePolicy(name="P:sigla_contratti_aspect:link", property=@StorageProperty(name="sigla_contratti_aspect_link:url"))
	public String getLinkUrl() {
		if (this.getIncaricoProceduraArchivio()!=null)
			return this.getIncaricoProceduraArchivio().getUrl_file();
		return null;
	}
	
	public boolean isEqualsTo(StorageObject storageObject, List<String> listError){
		String initTesto = "Procedura "+this.getEsercizioProcedura().toString()+"/"+this.getPgProcedura().toString()+" - Disallineamento dato ";
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

		valueDB=String.valueOf(this.getOriginalName());
		valueCMIS=String.valueOf(storageObject.getPropertyValue("sigla_contratti_attachment:original_name"));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Nome Originale File - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getLinkUrl());
		valueCMIS=String.valueOf(storageObject.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_LINK_URL.value()));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Link URL - DB:"+valueDB+" - CMIS:"+valueCMIS);
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
