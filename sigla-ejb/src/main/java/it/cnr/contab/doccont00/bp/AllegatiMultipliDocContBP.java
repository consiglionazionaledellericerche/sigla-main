/*
 * Copyright (C) 2021  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.doccont00.bp;

import it.cnr.contab.doccont00.intcass.bulk.StatoTrasmissione;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.FormBP;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.upload.UploadedFile;
import it.cnr.si.spring.storage.StorageException;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.StoreService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AllegatiMultipliDocContBP extends SimpleCRUDBP {
    private final List<StatoTrasmissione> documents;
    protected StoreService storeService;

    public AllegatiMultipliDocContBP(List<StatoTrasmissione> documents) {
        this.documents = documents;
    }

    public AllegatiMultipliDocContBP(String s, List<StatoTrasmissione> documents) {
        super(s);
        this.documents = documents;
    }

    @Override
    protected void initialize(ActionContext actioncontext)
            throws BusinessProcessException {
        storeService = SpringUtil.getBean("storeService", StoreService.class);
        super.initialize(actioncontext);
    }

    @Override
    public RemoteIterator find(ActionContext actionContext, CompoundFindClause compoundFindClause, OggettoBulk oggettoBulk, OggettoBulk oggettoBulk1, String s) throws BusinessProcessException {
        return null;
    }

    public String getLabel() {
        return "Aggiungi allegato ai documenti contabili: " + documentsLabel(documents);
    }

    private String documentsLabel(List<StatoTrasmissione> list) {
        return list
                .stream()
                .map(statoTrasmissione ->
                        statoTrasmissione.getEsercizio() + "/" +
                                statoTrasmissione.getPg_documento_cont()
                )
                .collect(Collectors.joining(" "));
    }

    @Override
    public void save(ActionContext actioncontext) throws ValidationException, BusinessProcessException {
        AllegatoGenericoBulk allegato = (AllegatoGenericoBulk) getModel();
        UploadedFile uploadedFile = ((it.cnr.jada.action.HttpActionContext) actioncontext)
                .getMultipartParameter("main.file");
        allegato.setContentType(uploadedFile.getContentType());
        allegato.setFile(uploadedFile.getFile());
        allegato.setNome(uploadedFile.getName());
        final File file = Optional.ofNullable(allegato.getFile())
                .orElseThrow(() -> handleException(new ApplicationException("File non presente")));
        List<StatoTrasmissione> result = new ArrayList<StatoTrasmissione>();
        try {
            for (StatoTrasmissione statoTrasmissione : documents) {
                final Optional<StorageObject> parentFolder =
                        Optional.ofNullable(storeService.getStorageObjectByPath(statoTrasmissione.getStorePath()));
                if (parentFolder.isPresent()) {
                    storeService.storeSimpleDocument(
                            allegato,
                            new FileInputStream(file),
                            allegato.getContentType(),
                            allegato.getNome(),
                            statoTrasmissione.getStorePath()
                    );
                    result.add(statoTrasmissione);
                }
            }
        } catch (FileNotFoundException e) {
            throw handleException(e);
        } catch (StorageException e) {
            if (e.getType().equals(StorageException.Type.CONSTRAINT_VIOLATED))
                throw handleException(new ApplicationException("File [" + allegato.getNome() + "] gia' presente. Inserimento non possibile!"));
            throw handleException(e);
        }
        setMessage(FormBP.INFO_MESSAGE, "Allegato inserito correttamente ai documenti: " +documentsLabel(result));
    }

    @Override
    public boolean isSearchButtonHidden() {
        return Boolean.TRUE;
    }

    @Override
    public boolean isDeleteButtonHidden() {
        return Boolean.TRUE;
    }

    @Override
    public boolean isNewButtonHidden() {
        return Boolean.TRUE;
    }

    @Override
    public boolean isFreeSearchButtonHidden() {
        return Boolean.TRUE;
    }
}
