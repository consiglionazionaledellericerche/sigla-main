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

package it.cnr.contab.docamm00.storage;

import it.cnr.contab.docamm00.docs.bulk.AutofatturaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.si.spring.storage.StorageDriver;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.si.spring.storage.bulk.StorageFile;
import it.cnr.si.spring.storage.bulk.StorageTypeName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class StorageFileAutofattura extends StorageFile implements StorageTypeName {
	private static final long serialVersionUID = -1775673719677028944L;
	private transient static final Logger logger = LoggerFactory.getLogger(StorageFileAutofattura.class);

	private AutofatturaBulk autofatturaBulk;

	private String originalName;
	private String typeName = "D:sigla_fatture_attachment:document";

	public StorageFileAutofattura(InputStream inputStream, String contentType, String name, AutofatturaBulk autofattura) throws IOException{
    	super(inputStream, contentType, name);
		impostaDatiBaseAutofattura(name, autofattura);
    }

	public StorageFileAutofattura(File file, AutofatturaBulk autofattura, String prefissoNomeFile) throws IOException{
    	super(file, null, prefissoNomeFile + autofattura.constructCMISNomeFile());
		impostaDatiBaseAutofattura(file.getName(), autofattura);
    }

	public StorageFileAutofattura(File file, AutofatturaBulk autofattura, String contentType, String nomeFile) throws IOException{
    	super(file, contentType, nomeFile);
		impostaDatiBaseAutofattura(file.getName(), autofattura);
	}

    public StorageFileAutofattura(StorageObject storageObject) {
		super(storageObject);
    }
	@Override
	public String getStorageParentPath() {
		return Optional.ofNullable(this.getAutofatturaBulk())
                .map(autofatturaBulk -> getCMISFolder(autofatturaBulk))
				.map(storageFolderAutofattura -> storageFolderAutofattura.getCMISPath())
				.orElse(null);
	}
    @Override
	public String getStorageAlternativeParentPath() {
        return Optional.ofNullable(this.getAutofatturaBulk())
                .map(autofatturaBulk -> getCMISFolder(autofatturaBulk))
                .map(storageFolderAutofattura -> storageFolderAutofattura.getCMISPrincipalPath())
                .map(path -> path.concat(StorageDriver.SUFFIX))
                .map(path -> path.concat((String)Fattura_attivaBulk.getTipoFatturaKeys().get(this.getAutofatturaBulk().getTi_fattura())))
                .orElse(null);
    }

	public StorageFolderAutofattura getCMISFolder(AutofatturaBulk autofattura) {
		return new StorageFolderAutofattura(autofattura);
	}

	@StorageProperty(name="sigla_fatture_attachment:original_name")
	public String getOriginalName() {
		return originalName;
	}
	
	@StoragePolicy(name="P:sigla_commons_aspect:utente_applicativo_sigla", property=@StorageProperty(name="sigla_commons_aspect:utente_applicativo"))
	public String getUtenteSigla() {
		return Optional.ofNullable(this.getAutofatturaBulk())
				.map(AutofatturaBulk::getUtuv)
				.orElse(null);
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public AutofatturaBulk getAutofatturaBulk() {
		return autofatturaBulk;
	}

	public void setAutofatturaBulk(AutofatturaBulk autofatturaBulk) {
		this.autofatturaBulk = autofatturaBulk;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public void impostaDatiBaseAutofattura(String originalName, AutofatturaBulk autofattura) {
    	setAutofatturaBulk(autofattura);
		this.setAuthor(getAutofatturaBulk().getUtcr());
		this.setTitle((String)Fattura_attivaBulk.getTipoFatturaKeys().get(getAutofatturaBulk().getTi_fattura()));
		this.setDescription((String)this.getTitle().toString()+
					" - nr."+getAutofatturaBulk().getEsercizio()+"/"+getAutofatturaBulk().getPg_autofattura());
		this.setOriginalName(originalName);
	}

}
