package it.cnr.contab.pdg00.bulk.cmis;


import java.io.File;
import java.util.StringTokenizer;

import it.cnr.contab.spring.config.StorageObject;

import it.cnr.contab.cmis.annotation.CMISPolicy;
import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.contab.cmis.annotation.CMISType;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.storage.StoreService;
import it.cnr.jada.bulk.OggettoBulk;
@CMISType(name="cmis:document")
public class AllegatoPdGVariazioneDocumentBulk extends OggettoBulk{
	private static final long serialVersionUID = 1L;
	private StorageObject node;
	private File file;
	private String contentType;
	private String nome;
	private String titolo;
	private String descrizione;
	
	public AllegatoPdGVariazioneDocumentBulk() {
		super();
	}

	public static AllegatoPdGVariazioneDocumentBulk construct(StorageObject node){
		return new AllegatoPdGVariazioneDocumentBulk(node);
	}
	
	public AllegatoPdGVariazioneDocumentBulk(StorageObject node) {
		super();
		this.node = node;
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
	
	public boolean isNodePresent(){
		return getDocument() != null;
	}

	public boolean isNodeNotPresent(){
		return !isNodePresent();
	}
	
	public StorageObject getDocument() {
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
}
