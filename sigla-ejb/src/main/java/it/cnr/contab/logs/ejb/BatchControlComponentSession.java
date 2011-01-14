package it.cnr.contab.logs.ejb;

import it.cnr.contab.logs.bulk.*;
import it.cnr.jada.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.util.*;

import javax.ejb.EJBObject;
import javax.ejb.Remote;


import java.rmi.RemoteException;
@Remote
public interface BatchControlComponentSession extends CRUDComponentSession{
	
	public Batch_controlBulk attivaBatch(UserContext usercontext, Batch_controlBulk batch_controlbulk) throws RemoteException, ComponentException;

	public Batch_controlBulk disattivaBatch(UserContext usercontext, Batch_controlBulk batch_controlbulk) throws RemoteException, ComponentException;
	
    public RemoteIterator listaBatch_control_jobs(UserContext usercontext) throws RemoteException, ComponentException;
    
    public Batch_controlBulk inizializzaParametri(UserContext usercontext, Batch_controlBulk batch_controlbulk) throws RemoteException, ComponentException;
    
}