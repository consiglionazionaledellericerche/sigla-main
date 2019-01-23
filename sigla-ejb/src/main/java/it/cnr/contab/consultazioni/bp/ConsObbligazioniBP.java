/*
 * Created on Jan 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.consultazioni.bp;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.SearchProvider;

/**
 * @author mincarnato
 * <p>
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsObbligazioniBP extends ConsultazioniRestBP
        implements SearchProvider {

    private String componentSessioneName;
    private Class bulkClass;

    public ConsObbligazioniBP() {
        super();
    }

    public ConsObbligazioniBP(String function) {
        super(function);
    }

    protected void init(it.cnr.jada.action.Config config, it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            super.init(config, context);
            setBulkClassName(config.getInitParameter("bulkClassName"));
            setComponentSessioneName(config.getInitParameter("componentSessionName"));
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

    public it.cnr.jada.util.RemoteIterator findFreeSearch(
            ActionContext context,
            it.cnr.jada.persistency.sql.CompoundFindClause clauses,
            OggettoBulk model)
            throws it.cnr.jada.action.BusinessProcessException {

        try {
            clauses = CompoundFindClause.and(clauses, getBaseclause());
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
