package it.cnr.contab.inventario01.ejb;

import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.ejb.TransactionalSessionImpl;

import java.rmi.RemoteException;

public class TransactionalNumerazioneTempBuonoComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements NumerazioneTempBuonoComponentSession {
	public it.cnr.jada.util.RemoteIterator cerca(it.cnr.jada.UserContext param0,it.cnr.jada.persistency.sql.CompoundFindClause param1,it.cnr.jada.bulk.OggettoBulk param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.jada.util.RemoteIterator)invoke("cerca",new Object[] {
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
	public it.cnr.jada.util.RemoteIterator cerca(it.cnr.jada.UserContext param0,it.cnr.jada.persistency.sql.CompoundFindClause param1,it.cnr.jada.bulk.OggettoBulk param2,it.cnr.jada.bulk.OggettoBulk param3,java.lang.String param4) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.jada.util.RemoteIterator)invoke("cerca",new Object[] {
				param0,
				param1,
				param2,
				param3,
				param4 });
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
	public it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.jada.bulk.OggettoBulk)invoke("creaConBulk",new Object[] {
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
	public it.cnr.jada.bulk.OggettoBulk[] creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk[] param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.jada.bulk.OggettoBulk[])invoke("creaConBulk",new Object[] {
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
	public void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			invoke("eliminaConBulk",new Object[] {
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
	public void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk[] param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			invoke("eliminaConBulk",new Object[] {
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
	
	public Long getNextTempPG(UserContext param0, Buono_carico_scaricoBulk param1) throws ComponentException, RemoteException {
		try {
			return (java.lang.Long)invoke("getNextTempPG",new Object[] {
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
	public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.jada.bulk.OggettoBulk)invoke("inizializzaBulkPerInserimento",new Object[] {
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
	public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.jada.bulk.OggettoBulk)invoke("inizializzaBulkPerModifica",new Object[] {
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
	public it.cnr.jada.bulk.OggettoBulk[] inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk[] param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.jada.bulk.OggettoBulk[])invoke("inizializzaBulkPerModifica",new Object[] {
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
	public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicerca(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.jada.bulk.OggettoBulk)invoke("inizializzaBulkPerRicerca",new Object[] {
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
	public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicercaLibera(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.jada.bulk.OggettoBulk)invoke("inizializzaBulkPerRicercaLibera",new Object[] {
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
	public it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.jada.bulk.OggettoBulk)invoke("modificaConBulk",new Object[] {
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
	public it.cnr.jada.bulk.OggettoBulk[] modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk[] param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.jada.bulk.OggettoBulk[])invoke("modificaConBulk",new Object[] {
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
	
}
