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

import it.cnr.contab.util.SIGLAStoragePropertyNames;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.config.StoragePropertyNames;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

public class PdgVariazioneDocument implements Serializable{
	private final StorageObject storageObject;

	public static PdgVariazioneDocument construct(StorageObject storageObject){
		return new PdgVariazioneDocument(storageObject);
	}
	
	public PdgVariazioneDocument(StorageObject storageObject) {
		super();
		this.storageObject = storageObject;
	}
	
	public StorageObject getStorageObject() {
		return storageObject;
	}

	public Integer getEsercizio(){
		return ((BigInteger) storageObject.getPropertyValue(SIGLAStoragePropertyNames.VARPIANOGEST_ESERCIZIO.value())).intValue();
	}
	
	public Integer getNumeroVariazione(){
		return ((BigInteger) storageObject.getPropertyValue(SIGLAStoragePropertyNames.VARPIANOGEST_NUMEROVARIAZIONE.value())).intValue();
	}
	
	public Boolean isSignedDocument(){
		return storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()).contains(SIGLAStoragePropertyNames.CNR_SIGNEDDOCUMENT.value());
	}
}
