package it.cnr.contab.reports.ejb;

import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.jada.util.ejb.TransactionalSessionImpl;

import java.rmi.RemoteException;

public class TransactionalOfflineReportComponentSession extends
		it.cnr.jada.ejb.TransactionalCRUDComponentSession implements
		OfflineReportComponentSession {
	public void addJob(it.cnr.jada.UserContext param0,
			it.cnr.contab.reports.bulk.Print_spoolerBulk param1,
			it.cnr.jada.bulk.BulkList param2) throws RemoteException,
			it.cnr.jada.comp.ComponentException {
		try {
			invoke("addJob", new Object[] { param0, param1, param2 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public void cancellaSchedulazione(it.cnr.jada.UserContext param0,
			Long param1, String param2) throws RemoteException,
			it.cnr.jada.comp.ComponentException {
		try {
			invoke("cancellaSchedulazione", new Object[] { param0, param1,
					param2 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public void deleteJobs(it.cnr.jada.UserContext param0,
			it.cnr.contab.reports.bulk.Print_spoolerBulk[] param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			invoke("deleteJobs", new Object[] { param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public it.cnr.jada.util.RemoteIterator queryJobs(
			it.cnr.jada.UserContext param0, java.lang.String param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.jada.util.RemoteIterator) invoke("queryJobs",
					new Object[] { param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public Print_spoolerBulk findPrintSpooler(it.cnr.jada.UserContext param0,
			Long param1) throws RemoteException,
			it.cnr.jada.comp.ComponentException {
		try {
			return (Print_spoolerBulk) invoke("findPrintSpooler", new Object[] {
					param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}
	
	public String getLastServerActive(
			it.cnr.jada.UserContext param0)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (String) invoke("getLastServerActive",
					new Object[] { param0});
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}
	
}