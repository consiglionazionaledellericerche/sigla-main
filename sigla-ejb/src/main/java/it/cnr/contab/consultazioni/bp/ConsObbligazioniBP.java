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

/*
 * Created on Jan 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.consultazioni.bp;

import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.utenze00.bp.SelezionatoreCdrBP;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.RemoteOrderable;
import it.cnr.jada.util.action.CondizioneComplessaBulk;
import it.cnr.jada.util.action.RicercaLiberaBP;
import it.cnr.jada.util.action.SearchProvider;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.jsp.Button;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Optional;

/**
 * @author mincarnato
 * <p>
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsObbligazioniBP extends SelezionatoreListaBP
        implements SearchProvider {

    private String componentSessioneName;
    private Class bulkClass;
    private CompoundFindClause findclause;

    public CompoundFindClause getFindclause() {
        return findclause;
    }

    public void setFindclause(CompoundFindClause findclause) {
        this.findclause = findclause;
    }

    public CompoundFindClause getBaseclause() {
        return baseclause;
    }

    public void setBaseclause(CompoundFindClause baseclause) {
        this.baseclause = baseclause;
    }

    private CompoundFindClause baseclause;
    private int navPosition = 0;
    private boolean flNuovoPdg = false;

    private java.math.BigDecimal pg_stampa = null;
    private java.math.BigDecimal currentSequence = null;


    public ConsObbligazioniBP() {
        super();
    }

    public ConsObbligazioniBP(String function) {
        super(function);
    }

    public void openIterator(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            it.cnr.jada.util.RemoteIterator ri =
                    it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator
                            (context, createComponentSession().cerca(context.getUserContext(), addBaseClause(context), getModel()));
            this.setIterator(context, ri);
        } catch (Throwable e) {
            throw new BusinessProcessException(e);
        }
    }

    public void addToBaseclause(CompoundFindClause clause) {
        baseclause = CompoundFindClause.and(baseclause, clause);
    }

    private CompoundFindClause addBaseClause(ActionContext actioncontext) {
        if (getBaseclause() == null && getFunction() != null){
            CompoundFindClause compoundFindClause = new CompoundFindClause();
            compoundFindClause.addClause("AND", "esercizio", SQLBuilder.EQUALS, new Integer(3000));
            setBaseclause(compoundFindClause);
        }
        return baseclause;
    }


    protected void init(it.cnr.jada.action.Config config, it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {

            setMultiSelection(false);
            setPageSize(10);
            try {
                setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(
                        Class.forName(config.getInitParameter("bulkClassName"))
                ));
                OggettoBulk model = (OggettoBulk) getBulkInfo().getBulkClass().newInstance();
                setModel(context, model);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                throw handleException(e);
            }
            setColumns(getBulkInfo().getColumnFieldPropertyDictionary());
            super.init(config, context);
            setBulkClassName(config.getInitParameter("bulkClassName"));
            setComponentSessioneName(config.getInitParameter("componentSessionName"));
            if (getFunction() != null){
                openIterator(context);
            }
        } catch (Throwable e) {
            throw new BusinessProcessException(e);
        }
    }


    public RemoteIterator search(
            ActionContext actioncontext,
            CompoundFindClause compoundfindclause,
            OggettoBulk oggettobulk)
            throws BusinessProcessException {
        return findFreeSearch(actioncontext,
                compoundfindclause,
                oggettobulk);
    }
    public it.cnr.jada.ejb.CRUDComponentSession createComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException, BusinessProcessException {

        return (it.cnr.jada.ejb.CRUDComponentSession) createComponentSession("JADAEJB_CRUDComponentSession", it.cnr.jada.ejb.CRUDComponentSession.class);
    }



    public it.cnr.jada.util.RemoteIterator findFreeSearch(
            ActionContext context,
            it.cnr.jada.persistency.sql.CompoundFindClause clauses,
            OggettoBulk model)
            throws it.cnr.jada.action.BusinessProcessException {
        if (!clauses.getClauses().hasMoreElements()){
            throw handleException(new BusinessProcessException("E' necessario indicare almeno un criterio di selezione."));
        }
        try {
            it.cnr.jada.util.RemoteIterator ri =
                    it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator
                            (context, createComponentSession().cerca(context.getUserContext(), clauses, model));
            //this.setIterator(context,ri);
            return ri;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * @return
     */
    public String getComponentSessioneName() {
        return componentSessioneName;
    }

    /**
     * @param string
     */
    public void setComponentSessioneName(String string) {
        componentSessioneName = string;
    }

    /**
     * @return java.lang.Class
     */
    public java.lang.Class getBulkClass() {
        return bulkClass;
    }

    /**
     * @param newBulkClass java.lang.Class
     */
    public void setBulkClass(java.lang.Class newBulkClass) {
        bulkClass = newBulkClass;
    }

    /**
     * Imposta il valore della proprietà 'bulkClassName'
     *
     * @param bulkClassName Il valore da assegnare a 'bulkClassName'
     * @throws ClassNotFoundException
     */
    public void setBulkClassName(java.lang.String bulkClassName) throws ClassNotFoundException {
        bulkClass = getClass().getClassLoader().loadClass(bulkClassName);
        setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(bulkClass));
        setColumns(getBulkInfo().getColumnFieldPropertyDictionary());
    }

}
