package it.cnr.contab.ordmag.magazzino.ejb;
import java.rmi.RemoteException;
import java.util.List;

import it.cnr.contab.ordmag.magazzino.bulk.BollaScaricoMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.ScaricoMagazzinoBulk;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineRigaBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.RemoteIterator;

public class TransactionalTipoMovimentiMagComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements TipoMovimentoMagComponentSession {
public MovimentiMagBulk caricoDaOrdine(UserContext userContext, OrdineAcqConsegnaBulk consegna, EvasioneOrdineRigaBulk evasioneOrdineRiga)throws ComponentException, PersistencyException, RemoteException, ApplicationException{
	try {
		return (MovimentiMagBulk)invoke("caricoDaOrdine",new Object[] {
				userContext,
				consegna, evasioneOrdineRiga});
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public List<BollaScaricoMagBulk> generaBolleScarico(UserContext userContext, List<MovimentiMagBulk> listaMovimentiScarico)
		throws ComponentException, PersistencyException, RemoteException, ApplicationException{
	try {
		return (List<BollaScaricoMagBulk>)invoke("generaBollaScarico",new Object[] {
				userContext, listaMovimentiScarico});
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}

public ScaricoMagazzinoBulk scaricaMagazzino(UserContext userContext, ScaricoMagazzinoBulk scaricoMagazzino)
		throws ComponentException, PersistencyException, RemoteException, ApplicationException{
	try {
		return (ScaricoMagazzinoBulk)invoke("scaricaMagazzino",new Object[] {
				userContext, scaricoMagazzino});
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}

public ScaricoMagazzinoBulk initializeScaricoMagazzino(UserContext userContext, ScaricoMagazzinoBulk scaricoMagazzino)
		throws ComponentException, PersistencyException, RemoteException, ApplicationException{
	try {
		return (ScaricoMagazzinoBulk)invoke("initializeScaricoMagazzino",new Object[] {
				userContext, scaricoMagazzino});
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public RemoteIterator preparaQueryBolleScaricoDaVisualizzare(UserContext userContext, List<BollaScaricoMagBulk> bolle)throws ComponentException, RemoteException{
	try {
		return (RemoteIterator)invoke("preparaQueryBolleScaricoDaVisualizzare",new Object[] {
				userContext,
				bolle});
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
}
