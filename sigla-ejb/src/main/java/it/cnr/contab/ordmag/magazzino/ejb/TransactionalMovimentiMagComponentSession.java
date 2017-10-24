package it.cnr.contab.ordmag.magazzino.ejb;
import java.rmi.RemoteException;
import java.util.List;

import it.cnr.contab.ordmag.magazzino.bulk.BollaScaricoMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagBulk;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineBulk;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineRigaBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;

public class TransactionalMovimentiMagComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements MovimentiMagComponentSession {
public List<MovimentiMagBulk> caricoDaOrdine(UserContext userContext, EvasioneOrdineBulk evasioneOrdine,
		OrdineAcqConsegnaBulk consegna, OrdineAcqBulk ordine, EvasioneOrdineRigaBulk evasioneOrdineRiga, 
		List<MovimentiMagBulk> listaMovimentiScarico)throws ComponentException, PersistencyException, RemoteException, ApplicationException{
	try {
		return (List<MovimentiMagBulk>)invoke("caricoDaOrdine",new Object[] {
				userContext,
				evasioneOrdine,consegna, ordine, evasioneOrdineRiga, 
				listaMovimentiScarico});
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
public List<BollaScaricoMagBulk> generaBollaScarico(UserContext userContext, List<MovimentiMagBulk> listaMovimentiScarico)
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
}
