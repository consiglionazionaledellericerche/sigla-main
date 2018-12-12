package it.cnr.contab.chiusura00.ejb;
import java.rmi.RemoteException;

import javax.ejb.EJBException;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;

public class TransactionalRicercaDocContComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements RicercaDocContComponentSession {
public void callAnnullamentoDocCont(it.cnr.jada.UserContext param0,java.lang.Long param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("callAnnullamentoDocCont",new Object[] {
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
public void callRiportoNextEsDocCont(it.cnr.jada.UserContext param0,java.lang.Long param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("callRiportoNextEsDocCont",new Object[] {
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
public void callRiportoNextEsDocContVoce(it.cnr.jada.UserContext param0,java.lang.Long param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("callRiportoNextEsDocContVoce",new Object[] {
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
public void callRiportoPrevEsDocCont(it.cnr.jada.UserContext param0,java.lang.Long param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("callRiportoPrevEsDocCont",new Object[] {
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
public it.cnr.jada.util.RemoteIterator cercaPerAnnullamento(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.jada.util.RemoteIterator)invoke("cercaPerAnnullamento",new Object[] {
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
public it.cnr.jada.util.RemoteIterator cercaPerRiportaAvanti(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.jada.util.RemoteIterator)invoke("cercaPerRiportaAvanti",new Object[] {
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
public it.cnr.jada.util.RemoteIterator cercaPerRiportaAvantiEvoluto(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.jada.util.RemoteIterator)invoke("cercaPerRiportaAvantiEvoluto",new Object[] {
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
public it.cnr.jada.util.RemoteIterator cercaPerRiportaIndietro(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.jada.util.RemoteIterator)invoke("cercaPerRiportaIndietro",new Object[] {
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
public void clearSelectionPerAnnullamento(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("clearSelectionPerAnnullamento",new Object[] {
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
public void clearSelectionPerRiportaAvanti(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("clearSelectionPerRiportaAvanti",new Object[] {
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
public void clearSelectionPerRiportaIndietro(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("clearSelectionPerRiportaIndietro",new Object[] {
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
public it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk initializeSelectionPerAnnullamento(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk)invoke("initializeSelectionPerAnnullamento",new Object[] {
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
public it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk initializeSelectionPerRiportaAvanti(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk)invoke("initializeSelectionPerRiportaAvanti",new Object[] {
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
public it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk initializeSelectionPerRiportaIndietro(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk)invoke("initializeSelectionPerRiportaIndietro",new Object[] {
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
public void selectAllPerAnnullamento(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("selectAllPerAnnullamento",new Object[] {
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
public void selectAllPerRiportaAvanti(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("selectAllPerRiportaAvanti",new Object[] {
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
public void selectAllPerRiportaAvantiEvoluto(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("selectAllPerRiportaAvantiEvoluto",new Object[] {
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
public void selectAllPerRiportaIndietro(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("selectAllPerRiportaIndietro",new Object[] {
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
public void setSelectionPerAnnullamento(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1,it.cnr.jada.bulk.OggettoBulk[] param2,java.util.BitSet param3,java.util.BitSet param4) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("setSelectionPerAnnullamento",new Object[] {
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
public void setSelectionPerRiportaAvanti(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1,it.cnr.jada.bulk.OggettoBulk[] param2,java.util.BitSet param3,java.util.BitSet param4) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("setSelectionPerRiportaAvanti",new Object[] {
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
public void setSelectionPerRiportaAvantiEvoluto(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1,it.cnr.jada.bulk.OggettoBulk[] param2,java.util.BitSet param3,java.util.BitSet param4) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("setSelectionPerRiportaAvantiEvoluto",new Object[] {
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
public void setSelectionPerRiportaIndietro(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1,it.cnr.jada.bulk.OggettoBulk[] param2,java.util.BitSet param3,java.util.BitSet param4) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("setSelectionPerRiportaIndietro",new Object[] {
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
public boolean isRibaltato(it.cnr.jada.UserContext param0) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Boolean)invoke("isRibaltato",new Object[] {
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
public boolean isRibaltato(it.cnr.jada.UserContext param0, it.cnr.contab.config00.sto.bulk.CdrBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Boolean)invoke("isRibaltato",new Object[] {
			param0, param1})).booleanValue();
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
public boolean isRibaltato(it.cnr.jada.UserContext param0, String param1, Integer param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Boolean)invoke("isRibaltato",new Object[] {
			param0, param1, param2})).booleanValue();
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
public boolean isRibaltato(it.cnr.jada.UserContext param0, it.cnr.contab.config00.sto.bulk.CdsBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Boolean)invoke("isRibaltato",new Object[] {
			param0, param1})).booleanValue();
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
public boolean isRibaltato(it.cnr.jada.UserContext param0, it.cnr.contab.config00.sto.bulk.CdsBulk param1, Integer esercizio) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Boolean)invoke("isRibaltato",new Object[] {
			param0, param1, esercizio})).booleanValue();
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
public boolean isRibaltato(it.cnr.jada.UserContext param0, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk param1, Integer esercizio) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Boolean)invoke("isRibaltato",new Object[] {
			param0, param1, esercizio})).booleanValue();
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
public boolean isSfondataDispCdS(it.cnr.jada.UserContext param0) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Boolean)invoke("isSfondataDispCdS",new Object[] {
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
public boolean isRicosResiduiChiusa(it.cnr.jada.UserContext param0) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Boolean)invoke("isRicosResiduiChiusa",new Object[] {
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

public void callRibaltaDispImproprie(UserContext param0) throws ComponentException, RemoteException {
	try {
		invoke("callRibaltaDispImproprie",new Object[] {param0});
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
public OggettoBulk updateParametriCds (UserContext param0) throws EJBException, ComponentException, PersistencyException, RemoteException {
	try {
		return (it.cnr.jada.bulk.OggettoBulk)invoke("updateParametriCds",new Object[] {param0});
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
public boolean getCdsRibaltato(UserContext param0) throws ComponentException, RemoteException {
	try {
		return ((Boolean)invoke("getCdsRibaltato",new Object[] {
				param0 })).booleanValue();
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
public boolean isRiaccertamentoChiuso(it.cnr.jada.UserContext param0) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Boolean)invoke("isRiaccertamentoChiuso",new Object[] {
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
public boolean isRiobbligazioneChiusa(it.cnr.jada.UserContext param0) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Boolean)invoke("isRiobbligazioneChiusa",new Object[] {
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
public boolean isGaeCollegateProgetti(it.cnr.jada.UserContext param0) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Boolean)invoke("isGaeCollegateProgetti",new Object[] {
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
public boolean isProgettiCollegatiGaeApprovati(it.cnr.jada.UserContext param0) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Boolean)invoke("isProgettiCollegatiGaeApprovati",new Object[] {
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
public it.cnr.jada.util.RemoteIterator cercaResiduiForRiaccertamento(it.cnr.jada.UserContext param0) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.jada.util.RemoteIterator)invoke("cercaResiduiForRiaccertamento",new Object[] {
			param0 });
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
public it.cnr.jada.util.RemoteIterator cercaGaeSenzaProgettiForRibaltamento(it.cnr.jada.UserContext param0) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.jada.util.RemoteIterator)invoke("cercaGaeSenzaProgettiForRibaltamento",new Object[] {
			param0 });
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
public it.cnr.jada.util.RemoteIterator cercaProgettiCollegatiGaeNonApprovatiForRibaltamento(it.cnr.jada.UserContext param0) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.jada.util.RemoteIterator)invoke("cercaProgettiCollegatiGaeNonApprovatiForRibaltamento",new Object[] {
			param0 });
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