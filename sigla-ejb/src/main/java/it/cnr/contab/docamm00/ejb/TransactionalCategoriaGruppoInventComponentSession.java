package it.cnr.contab.docamm00.ejb;
import java.rmi.*;
import java.util.List;

import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.ejb.*;

public class TransactionalCategoriaGruppoInventComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements CategoriaGruppoInventComponentSession {
public it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk completaElementoVoceOf(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk)invoke("completaElementoVoceOf",new Object[] {
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
public it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk findElementoVoce(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk)invoke("findElementoVoce",new Object[] {
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
public it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk findVoce_f(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException {
	try {
		return (it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk)invoke("findVoce_f",new Object[] {
			param0,
			param1 });
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(it.cnr.jada.persistency.PersistencyException ex) {
			throw ex;
		} catch(it.cnr.jada.persistency.IntrospectionException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public it.cnr.jada.util.RemoteIterator getChildren(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.jada.util.RemoteIterator)invoke("getChildren",new Object[] {
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
public it.cnr.jada.bulk.OggettoBulk getParent(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.jada.bulk.OggettoBulk)invoke("getParent",new Object[] {
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
public boolean isLeaf(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Boolean)invoke("isLeaf",new Object[] {
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
public List findAssVoceFList(UserContext param0, Categoria_gruppo_inventBulk param1)
		throws ComponentException, PersistencyException,
		IntrospectionException, RemoteException {
	try {
		return ((List)invoke("findAssVoceFList",new Object[] {
			param0,
			param1 }));
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
