package it.cnr.contab.util00.bulk.cmis;

import it.cnr.contab.cmis.annotation.CMISPolicy;
import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.contab.cmis.annotation.CMISType;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.storage.StoreService;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;

import java.io.File;
import java.util.StringTokenizer;

@CMISType(name="cmis:document")
public class AllegatoGenericoBulk extends OggettoBulk {
	private static final long serialVersionUID = 1L;
	private String storageKey;
	private File file;
	private String contentType;
	private String nome;
	private String titolo;
	private String descrizione;
	
	public AllegatoGenericoBulk() {
		super();
	}

	public static AllegatoGenericoBulk construct(String storageKey){
		return new AllegatoGenericoBulk(storageKey);
	}
	
	public AllegatoGenericoBulk(String storageKey) {
		super();
		this.storageKey = storageKey;
	}

	public String parseFilename(String file) {
		StringTokenizer fileName = new StringTokenizer(file,"\\",false);
		String newFileName = null;
		
		while (fileName.hasMoreTokens()){
			newFileName = fileName.nextToken();   	
		}

		if (newFileName != null){
			return SpringUtil.getBean("storeService", StoreService.class).sanitizeFilename(newFileName);
		}
		return SpringUtil.getBean("storeService", StoreService.class).sanitizeFilename(file);
	}

    public String getStorageKey() {
        return storageKey;
    }

    public boolean isNodePresent(){
		return storageKey != null;
	}
	
	public boolean isNodeNotPresent(){
		return !isNodePresent();
	}
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	@CMISProperty(name="cmis:name")
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	@CMISPolicy(name="P:cm:titled", property=@CMISProperty(name="cm:title"))
	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	@CMISPolicy(name="P:cm:titled", property=@CMISProperty(name="cm:description"))
	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	@Override
	public void validate() throws ValidationException {
		if (getNome() == null ) {
			if (file == null || file.getName().equals(""))
				throw new ValidationException("Attenzione: selezionare un File da caricare.");
		}
		super.validate();
	}
}
