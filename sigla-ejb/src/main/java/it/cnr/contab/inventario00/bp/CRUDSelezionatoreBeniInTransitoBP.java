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

package it.cnr.contab.inventario00.bp;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.ejb.DocAmmFatturazioneElettronicaComponentSession;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.docamm00.service.DocumentiCollegatiDocAmmService;
import it.cnr.contab.docamm00.service.FatturaPassivaElettronicaService;
import it.cnr.contab.docamm00.storage.StorageDocAmmAspect;
import it.cnr.contab.docamm00.storage.StorageFileFatturaAttiva;
import it.cnr.contab.firma.bulk.FirmaOTPBulk;
import it.cnr.contab.inventario00.docs.bulk.Transito_beni_ordiniBulk;
import it.cnr.contab.ordmag.magazzino.ejb.TransitoBeniOrdiniComponentSession;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.util.SIGLAStoragePropertyNames;
import it.cnr.contab.util.StringEncrypter;
import it.cnr.contab.util.StringEncrypter.EncryptionException;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.CondizioneComplessaBulk;
import it.cnr.jada.util.action.SearchProvider;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;
import it.cnr.si.firmadigitale.firma.arss.ArubaSignServiceException;
import it.cnr.si.spring.storage.MimeTypes;
import it.cnr.si.spring.storage.StorageDriver;
import it.cnr.si.spring.storage.StorageException;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.bulk.StorageFile;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v1.FatturaElettronicaType;
import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.util.ByteArrayDataSource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import java.io.*;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CRUDSelezionatoreBeniInTransitoBP extends SelezionatoreListaBP implements SearchProvider {
    private transient final static Logger logger = LoggerFactory.getLogger(CRUDSelezionatoreBeniInTransitoBP.class);
    private static final long serialVersionUID = 1L;

    public CRUDSelezionatoreBeniInTransitoBP() {
        super();
    }


    /**
     * DocumentiAmministrativiProtocollabiliBP constructor comment.
     *
     * @param function java.lang.String
     */
    public CRUDSelezionatoreBeniInTransitoBP(String function) {
        super(function);
    }

    @Override
    public void setMultiSelection(boolean flag) {
        super.setMultiSelection(flag);
    }

    @Override
    protected void init(Config config, ActionContext context)
            throws BusinessProcessException {
        try {
            setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(getClass().getClassLoader().loadClass(config.getInitParameter("bulkClassName"))));
            setMultiSelection(true);
            OggettoBulk model = (OggettoBulk) getBulkInfo().getBulkClass().newInstance();
            UserContext userContext = context.getUserContext();
            setModel(context, model);
            super.init(config, context);
            openIterator(context);
        } catch (InstantiationException e) {
            throw handleException(e);
        } catch (IllegalAccessException e) {
            throw handleException(e);
        } catch (ClassNotFoundException e) {
            throw handleException(e);
        }
    }

    public void openIterator(ActionContext actioncontext)
            throws BusinessProcessException {
        try {
            setIterator(actioncontext, search(
                    actioncontext,
                    Optional.ofNullable(getCondizioneCorrente())
                            .map(CondizioneComplessaBulk::creaFindClause)
                            .filter(CompoundFindClause.class::isInstance)
                            .map(CompoundFindClause.class::cast)
                            .orElseGet(() -> new CompoundFindClause()),
                    getModel())
            );
        } catch (RemoteException e) {
            throw new BusinessProcessException(e);
        }
    }

    public RemoteIterator search(
            ActionContext actioncontext,
            CompoundFindClause compoundfindclause,
            OggettoBulk oggettobulk)
            throws BusinessProcessException {
        Transito_beni_ordiniBulk transito = (Transito_beni_ordiniBulk) oggettobulk;
        try {
            return getComponentSession().cerca(
                    actioncontext.getUserContext(),
                    compoundfindclause,
                    transito,
                    "selectBeniInTransitoDaInventariare");
        } catch (ComponentException | RemoteException e) {
            throw handleException(e);
        }
    }

    protected CRUDComponentSession getComponentSession() {
        return Utility.createTransitoBeniOrdiniComponentSession();
    }

    public Button[] createToolbar() {
        Button[] baseToolbar = super.createToolbar();
        Button[] toolbar = new Button[baseToolbar.length + 1];
        int i = 0;
        for (Button button : baseToolbar) {
            toolbar[i++] = button;
        }
        toolbar[i++] = new Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.predisponi");
        return toolbar;
    }

    public List<Transito_beni_ordiniBulk> getBeniSelezionati(ActionContext context) throws BusinessProcessException {
            UserContext userContext = context.getUserContext();
            List<OggettoBulk> lista = getSelectedElements(context);
            List<Transito_beni_ordiniBulk> listaTransito = new ArrayList<>();

            for (Iterator<OggettoBulk> i = lista.iterator(); i.hasNext(); ) {
                Transito_beni_ordiniBulk transito = (Transito_beni_ordiniBulk)i.next();
                listaTransito.add(transito);
            }
        return listaTransito;
    }
}
