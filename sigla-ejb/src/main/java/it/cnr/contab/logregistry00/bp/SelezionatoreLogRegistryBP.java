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

package it.cnr.contab.logregistry00.bp;


import it.cnr.contab.logregistry00.ejb.LogRegistryComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;


public class SelezionatoreLogRegistryBP
        extends it.cnr.jada.util.action.SelezionatoreListaBP
        implements
        it.cnr.jada.util.action.SearchProvider,
        ILogRegistryBP {

    private String componentSessioneName;
    private Class bulkClass;

    /**
     * SelezionatoreLogRegistryBP constructor comment.
     */
    public SelezionatoreLogRegistryBP() {
        super();
    }

    /**
     * SelezionatoreLogRegistryBP constructor comment.
     *
     * @param function java.lang.String
     */
    public SelezionatoreLogRegistryBP(String function) {
        super(function);
    }

    /**
     * Crea la CRUDComponentSession da usare per effettuare le operazioni di CRUD
	 * @return LogRegistryComponentSession
	 * @throws javax.ejb.EJBException, java.rmi.RemoteException
     */
    public LogRegistryComponentSession createComponentSession()
            throws javax.ejb.EJBException, java.rmi.RemoteException {

        return (LogRegistryComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
                componentSessioneName,
                LogRegistryComponentSession.class);
    }

    public OggettoBulk createEmptyModelForFreeSearch(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            return createNewBulk(context);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public OggettoBulk createEmptyModelForSearch(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            return createNewBulk(context);
// TODO: initializaForFreeSearch con CRUDListaBP
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public OggettoBulk createNewBulk(ActionContext context) throws BusinessProcessException {
        try {
            OggettoBulk bulk = (OggettoBulk) bulkClass.newInstance();
            bulk.setUser(context.getUserInfo().getUserid());
            return bulk.initializeForSearch(null, context);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public it.cnr.jada.util.RemoteIterator find(
            ActionContext context,
            it.cnr.jada.persistency.sql.CompoundFindClause clauses,
            OggettoBulk model)
            throws it.cnr.jada.action.BusinessProcessException {

        try {
            return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(
                    context,
                    createComponentSession().cercaTabelleDiLog(
                            context.getUserContext(),
                            clauses,
                            model));
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public it.cnr.jada.util.RemoteIterator findFreeSearch(
            ActionContext context,
            it.cnr.jada.persistency.sql.CompoundFindClause clauses,
            OggettoBulk model)
            throws it.cnr.jada.action.BusinessProcessException {

        try {
            return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(
                    context,
                    createComponentSession().cerca(
                            context.getUserContext(),
                            clauses,
                            model));
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (01/10/2003 13.00.09)
     *
     * @return java.lang.String
     */
    public java.lang.String getComponentSessioneName() {
        return componentSessioneName;
    }

    /**
     * Insert the method's description here.
     * Creation date: (01/10/2003 13.00.09)
     *
     * @param newComponentSessioneName java.lang.String
     */
    public void setComponentSessioneName(java.lang.String newComponentSessioneName) {
        componentSessioneName = newComponentSessioneName;
    }

    protected void init(it.cnr.jada.action.Config config, it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        super.init(config, context);
        try {
            setBulkClassName(config.getInitParameter("bulkClassName"));
            setComponentSessioneName(config.getInitParameter("componentSessionName"));
            setIterator(context, find(context, null, createEmptyModelForSearch(context)));
            setMultiSelection(false);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Non trovata la classe bulk");
        } catch (Throwable e) {
            throw new BusinessProcessException(e);
        }
    }

    public it.cnr.jada.util.RemoteIterator search(
            it.cnr.jada.action.ActionContext context,
            it.cnr.jada.persistency.sql.CompoundFindClause clauses,
            it.cnr.jada.bulk.OggettoBulk prototype)
            throws it.cnr.jada.action.BusinessProcessException {

        return findFreeSearch(
                context,
                clauses,
                prototype);
    }

    public void setBulkClassName(java.lang.String bulkClassName) throws ClassNotFoundException {
        bulkClass = getClass().getClassLoader().loadClass(bulkClassName);
        setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(bulkClass));
    }
}
