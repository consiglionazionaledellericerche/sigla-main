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

package it.cnr.contab.pdg00.bulk.storage;


import java.io.File;
import java.util.StringTokenizer;

import it.cnr.si.spring.storage.StorageObject;

import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.si.spring.storage.annotation.StorageType;
import it.cnr.contab.service.SpringUtil;
import it.cnr.si.spring.storage.StoreService;
import it.cnr.jada.bulk.OggettoBulk;
@StorageType(name="cmis:document")
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
	@StorageProperty(name="cmis:name")
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	@StoragePolicy(name="P:cm:titled", property=@StorageProperty(name="cm:title"))
	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	@StoragePolicy(name="P:cm:titled", property=@StorageProperty(name="cm:description"))
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
