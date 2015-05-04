package it.cnr.contab.util00.bulk.cmis;

import it.cnr.contab.cmis.annotation.CMISPolicy;
import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.contab.cmis.annotation.CMISType;
import it.cnr.contab.cmis.service.SiglaCMISService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;

import java.io.File;
import java.util.StringTokenizer;

import org.apache.chemistry.opencmis.client.api.Document;
@CMISType(name="cmis:document")
public class AllegatoGenericoBulk extends OggettoBulk {
	private static final long serialVersionUID = 1L;
	private Document node;
	private File file;
	private String contentType;
	private String nome;
	private String titolo;
	private String descrizione;
	
	public AllegatoGenericoBulk() {
		super();
	}

	public static AllegatoGenericoBulk construct(Document node){
		return new AllegatoGenericoBulk(node);
	}
	
	public AllegatoGenericoBulk(Document node) {
		super();
		this.node = node;
	}

	public String parseFilename(String file) {
		StringTokenizer fileName = new StringTokenizer(file,"\\",false);
		String newFileName = null;
		
		while (fileName.hasMoreTokens()){
			newFileName = fileName.nextToken();   	
		}
		SiglaCMISService cmisService = SpringUtil.getBean("cmisService",
				SiglaCMISService.class);		

		if (newFileName != null){
			return cmisService.sanitizeFilename(newFileName);
		}
		return cmisService.sanitizeFilename(file);
	}
	
	public boolean isNodePresent(){
		return getDocument() == null;
	}
	
	public Document getDocument() {
		return node;
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
