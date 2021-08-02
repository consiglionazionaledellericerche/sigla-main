/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.reports.ejb;

import it.cnr.contab.reports.bulk.Print_priorityBulk;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.service.dataSource.PrintDataSourceOffline;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
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
	
	
	public boolean controllaStampeInCoda(it.cnr.jada.UserContext param0,
			it.cnr.contab.reports.bulk.Print_spoolerBulk param1) throws 
			it.cnr.jada.comp.ComponentException,RemoteException {
		try {
			return ((Boolean)invoke("controllaStampeInCoda",new Object[] {
					param0, param1 })).booleanValue();
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

	@Override
	public Print_spoolerBulk getJobWaitToJsoDS(UserContext param0) throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (Print_spoolerBulk) invoke("getJobWaitToJsoDS",
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

	@Override
	public Print_priorityBulk findPrintPriority(UserContext userContext, String reportName) throws RemoteException, ComponentException {
		try {
			return (Print_priorityBulk) invoke("findPrintPriority", new Object[] {
					userContext, reportName });
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

	@Override
	public Print_spoolerBulk getPrintSpoolerDsOffLine(UserContext userContext, Print_spoolerBulk printSpoller, PrintDataSourceOffline printDsOffLine) throws ComponentException, RemoteException {
		try {
			return (Print_spoolerBulk) invoke("getPrintSpoolerDsOffLine", new Object[] {
					userContext, printSpoller,printDsOffLine });
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