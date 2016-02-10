package it.cnr.contab.incarichi00.ejb;

import it.cnr.contab.incarichi00.bulk.VIncarichiAssRicBorseStBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.RemoteIterator;

import java.rmi.RemoteException;

import javax.ejb.Remote;
@Remote
public interface ConsIncarAssRicBorseStComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	RemoteIterator findIncarichi(UserContext param0, VIncarichiAssRicBorseStBulk param1) throws PersistencyException, IntrospectionException, ComponentException, RemoteException;
	VIncarichiAssRicBorseStBulk impostaDatiIniziali(UserContext userContext, VIncarichiAssRicBorseStBulk incarichi) throws ComponentException;
}
