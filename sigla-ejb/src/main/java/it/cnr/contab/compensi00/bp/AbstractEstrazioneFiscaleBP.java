package it.cnr.contab.compensi00.bp;

import it.cnr.contab.compensi00.ejb.*;
import it.cnr.jada.action.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.util.action.*;
/**
 * Insert the type's description here.
 * Creation date: (16/03/2004 18.03.53)
 * @author: Gennaro Borriello
 */
public class AbstractEstrazioneFiscaleBP extends it.cnr.jada.util.action.BulkBP {
/**
 * AbstractEstrazioneFiscaleBP constructor comment.
 */
public AbstractEstrazioneFiscaleBP() {
	super();
}
/**
 * AbstractEstrazioneFiscaleBP constructor comment.
 * @param function java.lang.String
 */
public AbstractEstrazioneFiscaleBP(String function) {
	super(function);
}
/**
 * Crea una componente di sessione
 * 
 *
 * @return 
 * @throws EJBException	Se si verifica qualche eccezione applicativa per cui non è possibile effettuare l'operazione
 * @throws RemoteException	Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
 */
public CompensoComponentSession createComponentSession()
	throws javax.ejb.EJBException,
			java.rmi.RemoteException,
			BusinessProcessException {
	return (CompensoComponentSession)createComponentSession("CNRCOMPENSI00_EJB_CompensoComponentSession",CompensoComponentSession.class);
}
/**
 * Effettua una operazione di ricerca per un attributo di un modello.
 * @param actionContext contesto dell'azione in corso
 * @param clauses Albero di clausole da utilizzare per la ricerca
 * @param bulk prototipo del modello di cui si effettua la ricerca
 * @param context modello che fa da contesto alla ricerca (il modello del FormController padre del
 * 			controller che ha scatenato la ricerca)
 * @return un RemoteIterator sul risultato della ricerca o null se la ricerca non ha ottenuto nessun risultato
 */
public it.cnr.jada.util.RemoteIterator find(it.cnr.jada.action.ActionContext actionContext, it.cnr.jada.persistency.sql.CompoundFindClause clauses, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.jada.bulk.OggettoBulk context, String property) throws it.cnr.jada.action.BusinessProcessException {
	try {
		return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(actionContext,createComponentSession().cerca(actionContext.getUserContext(),clauses,bulk,context,property));
	} catch(Exception e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}
}
