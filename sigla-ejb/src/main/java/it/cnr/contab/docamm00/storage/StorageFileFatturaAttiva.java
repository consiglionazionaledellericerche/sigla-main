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

import it.cnr.si.spring.storage.StorageDriver;
import it.cnr.si.spring.storage.bulk.StorageTypeName;
import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.si.spring.storage.bulk.StorageFile;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.si.spring.storage.StorageObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class StorageFileFatturaAttiva extends StorageFile implements StorageTypeName {
	private static final long serialVersionUID = -1775673719677028944L;
	private transient static final Logger logger = LoggerFactory.getLogger(StorageFileFatturaAttiva.class);

	private Fattura_attivaBulk fattura_attivaBulk;

	private String originalName;
	private String typeName = "D:sigla_fatture_attachment:document";

	public StorageFileFatturaAttiva(InputStream inputStream, String contentType, String name, Fattura_attivaBulk fattura) throws IOException{
    	super(inputStream, contentType, name);
		impostaDatiBaseFattura(name, fattura);
    }
	
	public StorageFileFatturaAttiva(File file, Fattura_attivaBulk fattura, String prefissoNomeFile) throws IOException{
    	super(file, null, prefissoNomeFile + fattura.constructCMISNomeFile());
		impostaDatiBaseFattura(file.getName(), fattura);
    }
	
	public StorageFileFatturaAttiva(File file, Fattura_attivaBulk fattura, String contentType, String nomeFile) throws IOException{
    	super(file, contentType, nomeFile);
    	impostaDatiBaseFattura(file.getName(), fattura);
	}
	
    public StorageFileFatturaAttiva(StorageObject storageObject) {
		super(storageObject);
    }
	@Override
	public String getStorageParentPath() {
		return Optional.ofNullable(this.getFattura_attivaBulk())
                .map(fattura_attivaBulk1 -> getCMISFolder(fattura_attivaBulk1))
				.map(storageFolderFatturaAttiva -> storageFolderFatturaAttiva.getCMISPath())
				.orElse(null);
	}
    @Override
	public String getStorageAlternativeParentPath() {
        return Optional.ofNullable(this.getFattura_attivaBulk())
                .map(fattura_attivaBulk1 -> getCMISFolder(fattura_attivaBulk1))
                .map(storageFolderFatturaAttiva -> storageFolderFatturaAttiva.getCMISPrincipalPath())
                .map(path -> path.concat(StorageDriver.SUFFIX))
                .map(path -> path.concat((String)Fattura_attivaBulk.getTipoFatturaKeys().get(this.getFattura_attivaBulk().getTi_fattura())))
                .orElse(null);
    }

	public StorageFolderFatturaAttiva getCMISFolder(Fattura_attivaBulk fattura) {
		return new StorageFolderFatturaAttiva(fattura);
	}

	@StorageProperty(name="sigla_fatture_attachment:original_name")
	public String getOriginalName() {
		return originalName;
	}
	
	@StoragePolicy(name="P:sigla_commons_aspect:utente_applicativo_sigla", property=@StorageProperty(name="sigla_commons_aspect:utente_applicativo"))
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
