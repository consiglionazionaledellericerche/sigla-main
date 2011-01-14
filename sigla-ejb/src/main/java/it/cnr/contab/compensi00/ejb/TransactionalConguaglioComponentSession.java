package it.cnr.contab.compensi00.ejb;
import java.rmi.*;

import it.cnr.jada.util.ejb.*;

public class TransactionalConguaglioComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements ConguaglioComponentSession {
public it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk completaTerzo(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1,it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk)invoke("completaTerzo",new Object[] {
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
public it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk doAbilitaConguaglio(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk)invoke("doAbilitaConguaglio",new Object[] {
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
public it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk doCreaCompensoConguaglio(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk)invoke("doCreaCompensoConguaglio",new Object[] {
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
public java.util.List findListaBanche(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (java.util.List)invoke("findListaBanche",new Object[] {
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
public java.util.Collection findModalita(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (java.util.Collection)invoke("findModalita",new Object[] {
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
public java.util.Collection findTermini(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (java.util.Collection)invoke("findTermini",new Object[] {
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
public java.util.Collection findTipiRapporto(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (java.util.Collection)invoke("findTipiRapporto",new Object[] {
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
public java.util.Collection findTipiTrattamento(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (java.util.Collection)invoke("findTipiTrattamento",new Object[] {
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
public boolean isConguaglioAnnullato(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Boolean)invoke("isConguaglioAnnullato",new Object[] {
			param0,
			param1 })).booleanValue();
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
public it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk reloadConguaglio(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk)invoke("reloadConguaglio",new Object[] {
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
public void validaTerzo(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("validaTerzo",new Object[] {
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
public int validaTerzo(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1,boolean param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Integer)invoke("validaTerzo",new Object[] {
			param0,
			param1,
			new Boolean(param2) })).intValue();
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
public boolean isGestiteDeduzioniIrpef(it.cnr.jada.UserContext param0) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Boolean)invoke("isGestiteDeduzioniIrpef",new Object[] {
			param0})).booleanValue();

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
public boolean isGestiteDetrazioniFamily(it.cnr.jada.UserContext param0) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Boolean)invoke("isGestiteDetrazioniFamily",new Object[] {
			param0})).booleanValue();

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
public void validaAltriDatiEsterni(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("validaAltriDatiEsterni",new Object[] {
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
public String verificaIncoerenzaCarichiFam(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (String)invoke("verificaIncoerenzaCarichiFam",new Object[] {
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
