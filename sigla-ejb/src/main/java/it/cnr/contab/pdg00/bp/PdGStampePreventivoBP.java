package it.cnr.contab.pdg00.bp;
/**
 * Business Process per la gestione delle stampe su PDG
 */
import it.cnr.contab.utenze00.bp.*;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.pdg00.bulk.*;
import it.cnr.jada.*;
import it.cnr.jada.comp.*;

public class PdGStampePreventivoBP extends it.cnr.jada.util.action.BulkBP {
public PdGStampePreventivoBP() {
	super();
}

public it.cnr.jada.util.RemoteIterator find(it.cnr.jada.action.ActionContext actionContext, it.cnr.jada.persistency.sql.CompoundFindClause clauses, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.jada.bulk.OggettoBulk context, java.lang.String property) throws it.cnr.jada.action.BusinessProcessException {
	return null;
}

public boolean isUoPrincipale(CNRUserContext userContext) throws javax.ejb.EJBException,java.rmi.RemoteException, ComponentException {
	it.cnr.contab.pdg00.ejb.PdGPreventivoComponentSession comp = (it.cnr.contab.pdg00.ejb.PdGPreventivoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPDG00_EJB_PdGPreventivoComponentSession", it.cnr.contab.pdg00.ejb.PdGPreventivoComponentSession.class);
	return comp.isUoPrincipale(userContext);
	}
}