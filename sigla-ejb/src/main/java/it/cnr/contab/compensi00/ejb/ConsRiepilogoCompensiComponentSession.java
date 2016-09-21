package it.cnr.contab.compensi00.ejb;

import it.cnr.contab.docamm00.consultazioni.bulk.VConsRiepCompensiBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.RemoteIterator;

import java.rmi.RemoteException;

import javax.ejb.Remote;
@Remote
public interface ConsRiepilogoCompensiComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	RemoteIterator findRiepilogoCompensi(UserContext param0, VConsRiepCompensiBulk param1) throws PersistencyException, IntrospectionException, ComponentException, RemoteException;
	VConsRiepCompensiBulk impostaDatiIniziali(UserContext userContext, VConsRiepCompensiBulk incarichi) throws ComponentException;
}
