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

package it.cnr.contab.util00.bulk.storage;

import it.cnr.jada.UserContext;
import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.si.spring.storage.annotation.StorageType;
import it.cnr.contab.service.SpringUtil;
import it.cnr.si.spring.storage.StoreService;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.StringTokenizer;

@StorageType(name="cmis:document")
public class AllegatoGenericoBulk extends OggettoBulk {
	private static final long serialVersionUID = 1L;
	private String storageKey;
	private File file;
	private String contentType;
	private String nome;
	private String titolo;
	private String descrizione;
	private String relativePath;
	private Date lastModificationDate;
	private Boolean daNonEliminare = false;
	
	public AllegatoGenericoBulk() {
		super();
	}

	public static AllegatoGenericoBulk construct(String storageKey){
		return new AllegatoGenericoBulk(storageKey);
	}
	public static AllegatoGenericoBulk name(String name){
		AllegatoGenericoBulk allegatoGenericoBulk = new AllegatoGenericoBulk();
		allegatoGenericoBulk.setNome(name);
		return allegatoGenericoBulk;
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

	public Date getLastModificationDate() {
		return lastModificationDate;
	}

	public void setLastModificationDate(Date lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}

	@Override
	public void validate() throws ValidationException {
		if (getNome() == null ) {
			if (file == null || file.getName().equals(""))
				throw new ValidationException("Attenzione: selezionare un File da caricare.");
		}
		super.validate();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AllegatoGenericoBulk that = (AllegatoGenericoBulk) o;
		return Objects.equals(storageKey, that.storageKey);
	}

	@Override
	public int hashCode() {
		return Objects.hash(storageKey);
	}

	public Boolean getDaNonEliminare() {
		return daNonEliminare;
	}

	public void setDaNonEliminare(Boolean daNonEliminare) {
		this.daNonEliminare = daNonEliminare;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

    public void complete(UserContext userContext) {
    }

	public void setStorageKey(String storageKey) {
		this.storageKey = storageKey;
	}
}
