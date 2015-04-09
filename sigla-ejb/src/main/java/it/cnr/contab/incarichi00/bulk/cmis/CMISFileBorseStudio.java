package it.cnr.contab.incarichi00.bulk.cmis;

import it.cnr.contab.cmis.annotation.CMISPolicy;
import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.contab.incarichi00.bulk.Incarichi_archivioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_archivioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_rappBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_varBulk;
import it.cnr.contab.incarichi00.cmis.CMISContrattiProperty;

import java.io.File;
import java.io.IOException;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CMISFileBorseStudio extends CMISFileIncarichi {
	private static final long serialVersionUID = -1775673719677028944L;
	private transient static final Logger logger = LoggerFactory.getLogger(CMISFileBorseStudio.class);

	public CMISFileBorseStudio(Incarichi_repertorio_archivioBulk incaricoRepertorioArchivio) throws IOException{
		super(incaricoRepertorioArchivio);
	}

	public CMISFileBorseStudio(Incarichi_repertorio_rappBulk incaricoRepertorioRapp) throws IOException{
		super(incaricoRepertorioRapp);
	}

	public CMISFileBorseStudio(Incarichi_repertorio_varBulk incaricoRepertorioVar) throws IOException{
		super(incaricoRepertorioVar);
	}

	public CMISFileBorseStudio(File file, String originalName, Incarichi_repertorio_archivioBulk incaricoRepertorioArchivio) throws IOException {
		super(file, originalName, incaricoRepertorioArchivio);
	}

	public CMISFileBorseStudio(File file, String originalName, Incarichi_repertorio_rappBulk incaricoRepertorioRapp) throws IOException {
		super(file, originalName, incaricoRepertorioRapp);
	}

	public CMISFileBorseStudio(File file, String originalName, Incarichi_repertorio_varBulk incaricoRepertorioVar) throws IOException {
		super(file, originalName, incaricoRepertorioVar);
	}

	public CMISFileBorseStudio(Document node, Incarichi_repertorio_archivioBulk incaricoRepertorioArchivio) {
		super(node, incaricoRepertorioArchivio);
	}

	public CMISFileBorseStudio(Document node, Incarichi_repertorio_rappBulk incaricoRepertorioRapp) {
		super(node, incaricoRepertorioRapp);
	}

	public CMISFileBorseStudio(Document node, Incarichi_repertorio_varBulk incaricoRepertorioVar) {
		super(node, incaricoRepertorioVar);
	}

	@CMISPolicy(name="P:sigla_contratti_aspect:borse_studio", property=@CMISProperty(name="sigla_contratti_aspect_borse_studio:esercizio"))
    public Integer getEsercizioIncarico() {
		return super.getEsercizioIncarico();
    }

	@CMISPolicy(name="P:sigla_contratti_aspect:borse_studio", property=@CMISProperty(name="sigla_contratti_aspect_borse_studio:progressivo", converterBeanName="cmis.converter.longToIntegerConverter"))
    public Long getPgIncarico() {
		return super.getPgIncarico();
    }

	@SuppressWarnings("unused")
	private void initCMISField(Integer esercizio, Long progressivo){
	    this.setAuthor(getIncaricoArchivio().getUtcr());
		this.setTitle((String)Incarichi_archivioBulk.getTipo_archivioKeys().get(getIncaricoArchivio().getTipo_archivio()));
		this.setDescription((String)Incarichi_archivioBulk.getTipo_archivioKeys().get(getIncaricoArchivio().getTipo_archivio()).toString()+
					" - Borsa di Studio nr."+esercizio+"/"+progressivo);
	}
	public boolean isEqualsTo(CmisObject node){
		String initTesto = "Procedura "+this.getEsercizioProcedura().toString()+"/"+this.getPgProcedura().toString()+" - " +
						   "Incarico "+this.getEsercizioIncarico().toString()+"/"+this.getPgIncarico().toString()+" - Disallineamento dato ";
		boolean isEquals = true;
		String valueDB=null, valueCMIS=null; 

		valueDB=String.valueOf(this.getEsercizioProcedura());
		valueCMIS=String.valueOf(node.getPropertyValue(CMISContrattiProperty.SIGLA_CONTRATTI_PROCEDURA_ESERCIZIO.value()));
		if (!valueCMIS.equals(valueDB)) {
			logger.debug(initTesto+" - Esercizio Procedura - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getPgProcedura());
		valueCMIS=String.valueOf(node.getPropertyValue(CMISContrattiProperty.SIGLA_CONTRATTI_PROCEDURA_PROGRESSIVO.value()));
		if (!valueCMIS.equals(valueDB)) {
			logger.debug(initTesto+" - Pg_procedura - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getEsercizioIncarico());
		valueCMIS=String.valueOf(node.getPropertyValue("sigla_contratti_aspect_borse_studio:esercizio"));
		if (!valueCMIS.equals(valueDB)) {
			logger.debug(initTesto+" - Esercizio Incarico - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getPgIncarico());
		valueCMIS=String.valueOf(node.getPropertyValue("sigla_contratti_aspect_borse_studio:progressivo"));
		if (!valueCMIS.equals(valueDB)) {
			logger.debug(initTesto+" - Pg_repertorio - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getOriginalName());
		valueCMIS=String.valueOf(node.getPropertyValue("sigla_contratti_attachment:original_name"));
		if (!valueCMIS.equals(valueDB)) {
			logger.debug(initTesto+" - Nome Originale File - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getTypeName());
		valueCMIS=String.valueOf(node.getType().getId());
		if (!valueCMIS.equals(valueDB)) {
			logger.debug(initTesto+" - Type documento - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		return isEquals;
	}	
}
