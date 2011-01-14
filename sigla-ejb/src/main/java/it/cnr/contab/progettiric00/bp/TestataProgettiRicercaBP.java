package it.cnr.contab.progettiric00.bp;

import java.rmi.RemoteException;

import it.cnr.contab.progettiric00.core.bulk.*;
import it.cnr.contab.config00.blob.bulk.*;
import it.cnr.contab.progettiric00.ejb.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.*;
import it.cnr.jada.util.action.SimpleDetailCRUDController;


public class TestataProgettiRicercaBP extends it.cnr.jada.util.action.SimpleCRUDBP implements IProgettoBP{

	private SimpleDetailCRUDController crudDettagli = new SimpleDetailCRUDController( "Dettagli", Progetto_uoBulk.class, "dettagli", this) {
		public void validateForDelete(ActionContext context, OggettoBulk detail)
			throws ValidationException
		{
			validaUO(context, detail);
		}
	};
	private SimpleDetailCRUDController crudDettagliFinanziatori = new SimpleDetailCRUDController( "DettagliFinanziatori", Progetto_finanziatoreBulk.class, "dettagliFinanziatori", this);
	private SimpleDetailCRUDController crudDettagliPartner_esterni = new SimpleDetailCRUDController( "DettagliPartner_esterni", Progetto_partner_esternoBulk.class, "dettagliPartner_esterni", this);
	private SimpleDetailCRUDController crudDettagliPostIt = new SimpleDetailCRUDController( "DettagliPostIt", PostItBulk.class, "dettagliPostIt", this);


/**
 * TestataProgettiRicercaBP constructor comment.
 */
public TestataProgettiRicercaBP() {
	super();
}
/**
 * TestataProgettiRicercaBP constructor comment.
 * @param function java.lang.String
 */
public TestataProgettiRicercaBP(String function) {
	super(function);
}
public BulkInfo getBulkInfo()
{
	BulkInfo infoBulk = super.getBulkInfo();
	infoBulk.setShortDescription("Commesse");
	return infoBulk;
}
	public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudDettagli() {
		return crudDettagli;
	}
	
	public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudDettagliFinanziatori() {
		return crudDettagliFinanziatori;
	}
/**
	 * Returns the crudDettagliPartner_esterni.
	 * @return SimpleDetailCRUDController
	 */
	public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudDettagliPartner_esterni() {
		return crudDettagliPartner_esterni;
	}
	
	 /**
	 * Returns the crudDettagliPostIt.
	 * @return SimpleDetailCRUDController
	 */
	public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudDettagliPostIt() {
		return crudDettagliPostIt;
	}		
protected void resetTabs(it.cnr.jada.action.ActionContext context) {
	setTab("tab","tabTestata");
}
/**
 * E' stata generata la richiesta di cercare il Progetto che sarà nodo padre del Progetto
 *	che si sta creando.
 *  Il metodo restituisce un Iteratore che permette di navigare tra i Progetti passando
 *	da un livello ai suoi nodi figli e viceversa. Il metodo isLeaf, permette di definire un 
 *	"livello foglia", il livello, cioè, che non ha nodi sotto di esso.
 *
 * @param context la <code>ActionContext</code> che ha generato la richiesta
 *
 * @return <code>RemoteBulkTree</code> l'albero richiesto
**/
public RemoteBulkTree getProgettiTree(ActionContext context) throws it.cnr.jada.comp.ComponentException{
  return
	new RemoteBulkTree() {
	  public RemoteIterator getChildren(ActionContext context, OggettoBulk bulk) throws java.rmi.RemoteException {
		try{
		  return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,((ProgettoRicercaComponentSession)createComponentSession()).getChildren(context.getUserContext(),bulk));
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw new java.rmi.RemoteException("Component Exception",ex);
		}catch(it.cnr.jada.action.BusinessProcessException ex){
			throw new java.rmi.RemoteException("BusinessProcess Exception",ex);
		}

	  }
	  public OggettoBulk getParent(ActionContext context, OggettoBulk bulk) throws java.rmi.RemoteException {
		try{
		  return ((ProgettoRicercaComponentSession)createComponentSession()).getParent(context.getUserContext(),bulk);
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw new java.rmi.RemoteException("Component Exception",ex);
		}catch(it.cnr.jada.action.BusinessProcessException ex){
			throw new java.rmi.RemoteException("BusinessProcess Exception",ex);
		}
	  }
	  

	  public boolean isLeaf(ActionContext context, OggettoBulk bulk) throws java.rmi.RemoteException {
		try{
		  return ((ProgettoRicercaComponentSession)createComponentSession()).isLeaf(context.getUserContext(),bulk);
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw new java.rmi.RemoteException("Component Exception",ex);
		}catch(it.cnr.jada.action.BusinessProcessException ex){
			throw new java.rmi.RemoteException("BusinessProcess Exception",ex);
		}
	  }
	};
}
/**
 * E' stata generata la richiesta di cercare il Progetto che sarà nodo padre del Progetto
 *	che si sta creando.
 *  Il metodo restituisce un Iteratore che permette di navigare tra i Progetti passando
 *	da un livello ai suoi nodi figli e viceversa. Il metodo isLeaf, permette di definire un 
 *	"livello foglia", il livello, cioè, che non ha nodi sotto di esso.
 *
 * @param context la <code>ActionContext</code> che ha generato la richiesta
 *
 * @return <code>RemoteBulkTree</code> l'albero richiesto
**/
public RemoteBulkTree getProgetti_sipTree(ActionContext context) throws it.cnr.jada.comp.ComponentException{
  return
	new RemoteBulkTree() {
	  public RemoteIterator getChildren(ActionContext context, OggettoBulk bulk) throws java.rmi.RemoteException {
		try{
		  return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,((ProgettoRicercaComponentSession)createComponentSession()).getChildrenForSip(context.getUserContext(),bulk));
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw new java.rmi.RemoteException("Component Exception",ex);
		}catch(it.cnr.jada.action.BusinessProcessException ex){
			throw new java.rmi.RemoteException("BusinessProcess Exception",ex);
		}

	  }
	  public OggettoBulk getParent(ActionContext context, OggettoBulk bulk) throws java.rmi.RemoteException {
		try{
		  return ((ProgettoRicercaComponentSession)createComponentSession()).getParentForSip(context.getUserContext(),bulk);
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw new java.rmi.RemoteException("Component Exception",ex);
		}catch(it.cnr.jada.action.BusinessProcessException ex){
			throw new java.rmi.RemoteException("BusinessProcess Exception",ex);
		}
	  }
	  

	  public boolean isLeaf(ActionContext context, OggettoBulk bulk) throws java.rmi.RemoteException {
		try{
		  return ((ProgettoRicercaComponentSession)createComponentSession()).isLeafForSip(context.getUserContext(),bulk);
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw new java.rmi.RemoteException("Component Exception",ex);
		}catch(it.cnr.jada.action.BusinessProcessException ex){
			throw new java.rmi.RemoteException("BusinessProcess Exception",ex);
		}
	  }
	};
}

/**
 * E' stata generata la richiesta di cercare il Progetto che sarà nodo padre del Progetto
 *	che si sta creando.
 *  Il metodo restituisce un Iteratore che permette di navigare tra i Progetti passando
 *	da un livello ai suoi nodi figli e viceversa. Il metodo isLeaf, permette di definire un 
 *	"livello foglia", il livello, cioè, che non ha nodi sotto di esso.
 *
 * @param context la <code>ActionContext</code> che ha generato la richiesta
 *
 * @return <code>RemoteBulkTree</code> l'albero richiesto
**/
public RemoteBulkTree getProgettiTreeWorkpackage(ActionContext context) throws it.cnr.jada.comp.ComponentException{
  return
	new RemoteBulkTree() {
	  public RemoteIterator getChildren(ActionContext context, OggettoBulk bulk) throws java.rmi.RemoteException {
		try{
		  return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,((ProgettoRicercaComponentSession)createComponentSession()).getChildrenWorkpackage(context.getUserContext(),bulk));
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw new java.rmi.RemoteException("Component Exception",ex);
		}catch(it.cnr.jada.action.BusinessProcessException ex){
			throw new java.rmi.RemoteException("BusinessProcess Exception",ex);
		}

	  }
	  public OggettoBulk getParent(ActionContext context, OggettoBulk bulk) throws java.rmi.RemoteException {
		try{
		  return ((ProgettoRicercaComponentSession)createComponentSession()).getParent(context.getUserContext(),bulk);
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw new java.rmi.RemoteException("Component Exception",ex);
		}catch(it.cnr.jada.action.BusinessProcessException ex){
			throw new java.rmi.RemoteException("BusinessProcess Exception",ex);
		}
	  }
	  

	  public boolean isLeaf(ActionContext context, OggettoBulk bulk) throws java.rmi.RemoteException {
		try{
		  return ((ProgettoRicercaComponentSession)createComponentSession()).isLeaf(context.getUserContext(),bulk);
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw new java.rmi.RemoteException("Component Exception",ex);
		}catch(it.cnr.jada.action.BusinessProcessException ex){
			throw new java.rmi.RemoteException("BusinessProcess Exception",ex);
		}
	  }
	};
}
/* 
 * Necessario per la creazione di una form con enctype di tipo "multipart/form-data"
 * Sovrascrive quello presente nelle superclassi
 * 
*/

public void openForm(javax.servlet.jsp.PageContext context,String action,String target) throws java.io.IOException,javax.servlet.ServletException {

		openForm(context,action,target,"multipart/form-data");
	
}

/*
 * Utilizzato per la gestione del bottone di attivazione del PostIt
 * Sovrascrive il metodo si SimpleCRUDBP
 * */
public boolean isActive(OggettoBulk bulk,int sel) {
  if (bulk instanceof ProgettoBulk)	
    try
    {
        return ((PostItBulk) ((ProgettoBulk)bulk).getDettagliPostIt().get(sel)).isROpostit();
    }
    catch (RuntimeException e)
    {
    	if (sel==0)
		return false;
        else
        e.printStackTrace();
    }
	return false;
}
/*
 * Utilizzato per la gestione del titolo
 * Sovrascrive il metodo si CRUDBP
 * */
public String getFormTitle()
{
	StringBuffer stringbuffer = new StringBuffer("Commesse");
	stringbuffer.append(" - ");
	switch(getStatus())
	{
	case 1: // '\001'
		stringbuffer.append("Inserimento");
		break;

	case 2: // '\002'
		stringbuffer.append("Modifica");
		break;

	case 0: // '\0'
		stringbuffer.append("Ricerca");
		break;

	case 5: // '\005'
		stringbuffer.append("Visualizza");
		break;
	}
	return stringbuffer.toString();
}
public String getSearchResultColumnSet() {

	return "filtro_ricerca_commesse";
}

public int getLivelloProgetto() {
	return ProgettoBulk.LIVELLO_PROGETTO_SECONDO.intValue();
}

public void validaUO(ActionContext context, it.cnr.jada.bulk.OggettoBulk detail) throws ValidationException {
	try {
		// controllo viene effettuato solo per i moduli attività
		if (getLivelloProgetto()==ProgettoBulk.LIVELLO_PROGETTO_TERZO.intValue())
			((ProgettoRicercaComponentSession)createComponentSession()).validaCancellazioneUoAssociata(
				context.getUserContext(),
				(ProgettoBulk)getModel(),
				detail);
	} catch (ComponentException e) {
		throw new ValidationException(e.getMessage());
	} catch (RemoteException e) {
		throw new ValidationException(e.getMessage());
	} catch (BusinessProcessException e) {
		throw new ValidationException(e.getMessage());
	}
}
}
