/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.ordmag.ordini.bp;

import it.cnr.contab.firma.bulk.FirmaOTPBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.ordmag.ordini.service.OrdineAcqCMISService;
import it.cnr.contab.util.SignP7M;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.config.StoragePropertyNames;

import java.util.Arrays;
import java.util.List;

public class CRUDFirmaOrdineAcqBP extends CRUDOrdineAcqBP {

    public CRUDFirmaOrdineAcqBP(String function) throws BusinessProcessException {
        super(function);
    }

    public CRUDFirmaOrdineAcqBP() throws BusinessProcessException {
        super();
    }

    public void firmaOTP(ActionContext actionContext, FirmaOTPBulk firmaOTPBulk) throws ApplicationException, BusinessProcessException {
        final Integer firmaFatture = firmaOrdine(actionContext, firmaOTPBulk, Arrays.asList((OrdineAcqBulk) this.getModel()));
        setMessage(INFO_MESSAGE, "L'Ordine è stato firmato correttamente");
    }

    public Integer firmaOrdine(ActionContext actionContext, FirmaOTPBulk firmaOTPBulk, List<OrdineAcqBulk> l)
            throws ApplicationException, BusinessProcessException {
        OrdineAcqCMISService ordineService = (OrdineAcqCMISService) this.getStoreService();
        StorageObject ordineAcqStampaNode = null;
        Integer ret = 0;
        try {
            for (OrdineAcqBulk ordineAcqBulk : l) {
                ordineAcqStampaNode = ordineService.getStorageObjectStampaOrdine(ordineAcqBulk);
                String nomeFileFirmato = ordineAcqStampaNode.<String>getPropertyValue(StoragePropertyNames.NAME.value())
                        .replace(".pdf", ".signed.pdf");
                SignP7M signP7M = new SignP7M(
                        ordineAcqStampaNode.getPropertyValue(StoragePropertyNames.ALFCMIS_NODEREF.value()),
                        firmaOTPBulk.getUserName(),
                        firmaOTPBulk.getPassword(),
                        firmaOTPBulk.getOtp(),
                        nomeFileFirmato);
                String key = ordineService.signOrdine(signP7M, ordineService.getStorePath(ordineAcqBulk));

                if (ordineAcqBulk.isStatoAllaFirma()) {
                    ordineAcqBulk.setStato(OrdineAcqBulk.STATO_DEFINITIVO);
                    java.sql.Timestamp dataReg = null;
                    try {
                        dataReg = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
                    } catch (javax.ejb.EJBException e) {
                        throw new it.cnr.jada.DetailedRuntimeException(e);
                    }

                    ordineAcqBulk.setDataOrdineDef(dataReg);
                }

                this.save(actionContext);
            }

        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return ret;
    }

    @Override
    protected it.cnr.jada.util.jsp.Button[] createToolbar() {
        it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[9];
        int i = 0;
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class), "CRUDToolbar.search");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class), "CRUDToolbar.startSearch");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class), "CRUDToolbar.freeSearch");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class), "CRUDToolbar.new");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class), "CRUDToolbar.save");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class), "CRUDToolbar.delete");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.sblocca");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.stampa");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.firmaOrdine");

        return toolbar;
    }

    public boolean isFirmaOrdineButtonHidden() {
        OrdineAcqBulk ordine = (OrdineAcqBulk) getModel();
        return (ordine == null || ordine.getNumero() == null || !ordine.isStatoAllaFirma());
    }

    public boolean isSbloccaButtonHidden() {
        OrdineAcqBulk ordine = (OrdineAcqBulk) getModel();
        return (ordine == null || ordine.getNumero() == null || !ordine.isStatoAllaFirma());
    }

    @Override
    public RemoteIterator find(ActionContext actioncontext, CompoundFindClause compoundfindclause,
                               OggettoBulk oggettobulk) throws BusinessProcessException {
        OrdineAcqBulk ordine = (OrdineAcqBulk) oggettobulk;
        ordine.setIsForFirma(true);
        return super.find(actioncontext, compoundfindclause, ordine);
    }

    @Override
    public void save(ActionContext actioncontext) throws ValidationException, BusinessProcessException {
        completeSearchTools(actioncontext, this);
        validate(actioncontext);
        saveChildren(actioncontext);
        update(actioncontext);
        if (getMessage() == null)
            setMessage("Salvataggio eseguito in modo corretto.");
        commitUserTransaction();
        try {
            basicEdit(actioncontext, getModel(), true);
        } catch (BusinessProcessException businessprocessexception) {
            setModel(actioncontext, null);
            setDirty(false);
            throw businessprocessexception;
        }
    }


    @Override
    public OggettoBulk initializeModelForSearch(ActionContext actioncontext, OggettoBulk oggettobulk)
            throws BusinessProcessException {
        // TODO Auto-generated method stub
        OrdineAcqBulk ordine = (OrdineAcqBulk) oggettobulk;
        ordine.setIsForFirma(true);
        return super.initializeModelForSearch(actioncontext, ordine);
    }
}
