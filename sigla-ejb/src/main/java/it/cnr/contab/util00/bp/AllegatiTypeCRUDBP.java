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

package it.cnr.contab.util00.bp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;

import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoTypeBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoParentBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.si.spring.storage.StorageException;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.config.StoragePropertyNames;

public abstract class AllegatiTypeCRUDBP<T extends AllegatoGenericoTypeBulk, K extends AllegatoParentBulk> extends AllegatiCRUDBP<T,K> {
    private static final long serialVersionUID = 1L;

    public AllegatiTypeCRUDBP() {
        super();
    }

    public AllegatiTypeCRUDBP(String s) {
        super(s);
    }

    @Override
    protected void completeAllegato(T allegato, StorageObject storageObject) throws ApplicationException {
        super.completeAllegato(allegato, storageObject);
        completeAllegato(allegato);
    }

    protected void completeAllegato(T allegato) throws ApplicationException {
		StorageObject storageObject = storeService.getStorageObjectBykey(allegato.getStorageKey());
		allegato.setObjectType(storageObject.getPropertyValue(StoragePropertyNames.OBJECT_TYPE_ID.value()));
    }
 
    @SuppressWarnings("unchecked")
    public void archiviaAllegati(ActionContext actioncontext) throws BusinessProcessException, ApplicationException {
        AllegatoParentBulk allegatoParentBulk = (AllegatoParentBulk) getModel();
        for (AllegatoGenericoBulk model : allegatoParentBulk.getArchivioAllegati()) {
            AllegatoGenericoTypeBulk allegato = (AllegatoGenericoTypeBulk)model;
        	if (allegato.isToBeCreated()) {
                final File file = Optional.ofNullable(allegato.getFile())
                        .orElseThrow(() -> new ApplicationException("File non presente"));
                try {
                    storeService.storeSimpleDocument(allegato,
                            new FileInputStream(file),
                            allegato.getContentType(),
                            allegato.getNome(),
                            getStorePath((K) allegatoParentBulk,
                                    true),
                            allegato.getObjectType(),
                            false
                    		);
                    allegato.setCrudStatus(OggettoBulk.NORMAL);
                } catch (FileNotFoundException e) {
                    throw handleException(e);
                } catch (StorageException e) {
                    if (e.getType().equals(StorageException.Type.CONSTRAINT_VIOLATED))
                        throw new ApplicationException("File [" + allegato.getNome() + "] gia' presente. Inserimento non possibile!");
                    throw handleException(e);
                }
            } else if (allegato.isToBeUpdated()) {
                if (isPossibileModifica(allegato)) {
                    try {
                        if (allegato.getFile() != null) {
                            storeService.updateStream(allegato.getStorageKey(),
                                    new FileInputStream(allegato.getFile()),
                                    allegato.getContentType());
                        }
                        storeService.updateProperties(allegato, storeService.getStorageObjectBykey(allegato.getStorageKey()));
                        allegato.setCrudStatus(OggettoBulk.NORMAL);
                    } catch (FileNotFoundException e) {
                        throw handleException(e);
                    } catch (StorageException e) {
                        if (e.getType().equals(StorageException.Type.CONSTRAINT_VIOLATED))
                            throw new ApplicationException("File [" + allegato.getNome() + "] gia' presente. Inserimento non possibile!");
                        throw handleException(e);
                    }
                }
            }
        }
        gestioneCancellazioneAllegati(allegatoParentBulk);
    }
}
