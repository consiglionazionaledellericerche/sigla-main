package it.cnr.contab.docamm00.cmis;

import it.cnr.contab.cmis.CMISTypeName;
import it.cnr.contab.cmis.annotation.CMISPolicy;
import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.contab.cmis.bulk.CMISFile;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.spring.config.StorageObject;
import it.cnr.contab.spring.storage.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

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
	
    public CMISFileFatturaAttiva(StorageObject storageObject) {
		super(storageObject);
    }
	@Override
	public String getCMISParentPath() {
		return Optional.ofNullable(this.getFattura_attivaBulk())
                .map(fattura_attivaBulk1 -> getCMISFolder(fattura_attivaBulk1))
				.map(cmisFolderFatturaAttiva -> cmisFolderFatturaAttiva.getCMISPath())
				.orElse(null);
	}
    @Override
	public String getCMISAlternativeParentPath() {
        return Optional.ofNullable(this.getFattura_attivaBulk())
                .map(fattura_attivaBulk1 -> getCMISFolder(fattura_attivaBulk1))
                .map(cmisFolderFatturaAttiva -> cmisFolderFatturaAttiva.getCMISPrincipalPath())
                .map(path -> path.concat(StoreService.BACKSLASH))
                .map(path -> path.concat((String)Fattura_attivaBulk.getTipoFatturaKeys().get(this.getFattura_attivaBulk().getTi_fattura())))
                .orElse(null);
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
