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

package it.cnr.contab.utenze00.bp;

import it.cnr.contab.utenze00.bulk.PreferitiBulk;
import it.cnr.contab.utenze00.ejb.AssBpAccessoComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.FormController;
import it.cnr.jada.util.action.SimpleCRUDBP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Optional;

public class CRUDAggiungiPreferitiBP extends SimpleCRUDBP {
    private transient final static Logger logger = LoggerFactory.getLogger(CRUDAggiungiPreferitiBP.class);
    private String bpName;
    private Character funzione;
    private String descrizione;

    public CRUDAggiungiPreferitiBP() {
        super();
    }

    public CRUDAggiungiPreferitiBP(String s) {
        super(s);
    }

    public CRUDAggiungiPreferitiBP(String bpName, Character funzione, String descrizione) {
        super("M");
        this.bpName = bpName;
        this.funzione = funzione;
        this.descrizione = descrizione;
    }

    public static AssBpAccessoComponentSession assBpAccessoComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException {
        return (AssBpAccessoComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRUTENZE00_EJB_AssBpAccessoComponentSession");
    }

    @Override
    protected void init(Config config, ActionContext actioncontext) throws BusinessProcessException {
        super.init(config, actioncontext);
        if (bpName != null && funzione != null) {
            setStatus(FormController.INSERT);
            PreferitiBulk preferiti = (PreferitiBulk) getModel();
            preferiti.setCd_utente(CNRUserContext.getUser(actioncontext.getUserContext()));
            try {
                preferiti.setAssBpAccessoBulk(
                        assBpAccessoComponentSession().finAssBpAccesso(
                                actioncontext.getUserContext(),
                                bpName,
                                String.valueOf(funzione)
                        )
                );
            } catch (ComponentException|RemoteException e) {
                logger.error("Cannot find AssBpAccesso for BusinessProcess {} and Funzione {}", bpName, funzione);
            }
            preferiti.setBusiness_process(bpName);
            preferiti.setTi_funzione(String.valueOf(funzione));
            preferiti.setDescrizione(descrizione);
            preferiti.setUrl_icona(PreferitiBulk.LINK1);
            preferiti.setToBeCreated();
            initializeModelForInsert(actioncontext, preferiti);
        }
    }

    @Override
    public OggettoBulk initializeModelForEdit(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
        return Optional.ofNullable(super.initializeModelForEdit(actioncontext, oggettobulk))
                .filter(PreferitiBulk.class::isInstance)
                .map(PreferitiBulk.class::cast)
                .map(preferitiBulk -> {
                    try {
                        preferitiBulk.setAssBpAccessoBulk(
                                assBpAccessoComponentSession().finAssBpAccesso(
                                        actioncontext.getUserContext(),
                                        preferitiBulk.getBusiness_process(),
                                        preferitiBulk.getTi_funzione()
                                )
                        );
                        return preferitiBulk;
                    } catch (ComponentException|RemoteException e) {
                        return oggettobulk;
                    }
                }).orElse(oggettobulk);
    }

    @Override
    public OggettoBulk initializeModelForInsert(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
        Optional.ofNullable(oggettobulk)
                .filter(PreferitiBulk.class::isInstance)
                .map(PreferitiBulk.class::cast)
                .ifPresent(preferitiBulk -> {
                    preferitiBulk.setCd_utente(CNRUserContext.getUser(actioncontext.getUserContext()));
                    preferitiBulk.setUrl_icona(PreferitiBulk.LINK1);
                });
        return super.initializeModelForInsert(actioncontext, oggettobulk);
    }

    @Override
    public void writeForm(JspWriter jspwriter) throws IOException {
        if (this.getParentRoot().isBootstrap())
            super.writeForm(jspwriter, "bootstrap");
        else
            super.writeForm(jspwriter);
    }

    @Override
    public String getSearchResultColumnSet() {
        if (this.getParentRoot().isBootstrap())
            return "bootstrap";
        else
            return super.getSearchResultColumnSet();
    }

    @Override
    public String getFreeSearchSet() {
        if (this.getParentRoot().isBootstrap())
            return "bootstrap";
        else
            return super.getFreeSearchSet();
    }
}
