package it.cnr.contab.compensi00.ejb;
import java.rmi.*;

import it.cnr.jada.util.ejb.*;

public class TransactionalLiquidazioneRateMinicarrieraComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements LiquidazioneRateMinicarrieraComponentSession {
public it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk findVoceF(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.Liquidazione_rate_minicarrieraBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk)invoke("findVoceF",new Object[] {
			param0,
			param1 });
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
public void liquidaRate(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.Liquidazione_rate_minicarrieraBulk param1,java.util.List param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("liquidaRate",new Object[] {
			param0,
			param1,
			param2 });
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
