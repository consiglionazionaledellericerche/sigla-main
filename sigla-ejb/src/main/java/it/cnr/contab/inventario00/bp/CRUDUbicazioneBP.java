package it.cnr.contab.inventario00.bp;

/** 
 * Un BusinessProcess controller che permette di effettuare le operazioni di CRUD su istanze 
 *	di Ubicazione_beneBulk, per la gestione delle Ubicazioni di un bene.
**/ 
import it.cnr.contab.inventario00.tabrif.bulk.*;
import it.cnr.contab.inventario00.ejb.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.*;

public class CRUDUbicazioneBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	
public CRUDUbicazioneBP() {
	super();
}
public CRUDUbicazioneBP(String function) {
	super(function);
}
/**
 * E' stata generata la richiesta di cercare l'Ubicazione che sarà nodo padre dell'Ubicazione
 *	che si sta creando.
 *  Il metodo restituisce un Iteratore che permette di navigare tra le ubicazioni passando
 *	da un livello ai suoi nodi figli e viceversa. Il metodo isLeaf, permette di definire un 
 *	"livello foglia", il livello, cioè, che non può avere nodi sotto di esso.
 *
 * @param context la <code>ActionContext</code> che ha generato la richiesta
 *
 * @return <code>RemoteBulkTree</code> l'albero richiesto
**/
public RemoteBulkTree getUbicazioniTree(ActionContext context) throws it.cnr.jada.comp.ComponentException{
  return
	new RemoteBulkTree() {
	  public RemoteIterator getChildren(ActionContext context, OggettoBulk bulk) throws java.rmi.RemoteException {
		try{
		  return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,((UbicazioneBeneComponentSession)createComponentSession()).getChildren(context.getUserContext(),bulk));
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw new java.rmi.RemoteException("Component Exception",ex);
		}catch(it.cnr.jada.action.BusinessProcessException ex){
			throw new java.rmi.RemoteException("BusinessProcess Exception",ex);
		}

	  }

	  public OggettoBulk getParent(ActionContext context, OggettoBulk bulk) throws java.rmi.RemoteException {
		try{
		  return ((UbicazioneBeneComponentSession)createComponentSession()).getParent(context.getUserContext(),bulk);
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw new java.rmi.RemoteException("Component Exception",ex);
		}catch(it.cnr.jada.action.BusinessProcessException ex){
			throw new java.rmi.RemoteException("BusinessProcess Exception",ex);
		}
	  }

	  public boolean isLeaf(ActionContext context, OggettoBulk bulk) throws java.rmi.RemoteException {
		try{
		  return ((UbicazioneBeneComponentSession)createComponentSession()).isLeaf(context.getUserContext(),bulk);
  		}catch(it.cnr.jada.comp.ComponentException ex){
			throw new java.rmi.RemoteException("Component Exception",ex);
		}catch(it.cnr.jada.action.BusinessProcessException ex){
			throw new java.rmi.RemoteException("BusinessProcess Exception",ex);
		}
	  }
	};
}
}
