package it.cnr.contab.gestiva00.ejb;
import it.cnr.jada.util.ejb.TransactionalSessionImpl;

import java.rmi.RemoteException;

public class TransactionalStampaRegistriIvaComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements StampaRegistriIvaComponentSession {
public it.cnr.jada.bulk.MTUWrapper callStampeIva(it.cnr.jada.UserContext param0,it.cnr.contab.gestiva00.core.bulk.Stampa_registri_ivaVBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.jada.bulk.MTUWrapper)invoke("callStampeIva",new Object[] {
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
public it.cnr.jada.bulk.BulkList findRegistriStampati(it.cnr.jada.UserContext param0,it.cnr.contab.gestiva00.core.bulk.Stampa_registri_ivaVBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.jada.bulk.BulkList)invoke("findRegistriStampati",new Object[] {
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
public it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_annualeVBulk riepilogoLiquidazioneIVA(it.cnr.jada.UserContext param0,it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_annualeVBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_annualeVBulk)invoke("riepilogoLiquidazioneIVA",new Object[] {
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
public java.util.Collection selectProspetti_stampatiByClause(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.gestiva00.core.bulk.Liquidazione_ivaBulk param2,it.cnr.jada.persistency.sql.CompoundFindClause param3) throws RemoteException,it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException {
	try {
		return (java.util.Collection)invoke("selectProspetti_stampatiByClause",new Object[] {
			param0,
			param1,
			param2,
			param3 });
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(it.cnr.jada.persistency.PersistencyException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public java.util.Collection selectTipi_sezionaliByClause(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk param2,it.cnr.jada.persistency.sql.CompoundFindClause param3) throws RemoteException,it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException {
	try {
		return (java.util.Collection)invoke("selectTipi_sezionaliByClause",new Object[] {
			param0,
			param1,
			param2,
			param3 });
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(it.cnr.jada.persistency.PersistencyException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_annualeVBulk tabCodIvaAcquisti(it.cnr.jada.UserContext param0,it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_annualeVBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_annualeVBulk)invoke("tabCodIvaAcquisti",new Object[] {
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
public it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_annualeVBulk tabCodIvaVendite(it.cnr.jada.UserContext param0,it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_annualeVBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_annualeVBulk)invoke("tabCodIvaVendite",new Object[] {
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
