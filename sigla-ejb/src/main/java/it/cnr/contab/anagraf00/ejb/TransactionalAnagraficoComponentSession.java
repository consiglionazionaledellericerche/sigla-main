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

package it.cnr.contab.anagraf00.ejb;
import java.rmi.*;
import java.sql.Timestamp;

import it.cnr.contab.anagraf00.comp.AnagraficoComponent;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.Carico_familiare_anagBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.ejb.*;

public class TransactionalAnagraficoComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements AnagraficoComponentSession {
public java.lang.String calcolaCodiceFiscale(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (java.lang.String)invoke("calcolaCodiceFiscale",new Object[] {
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
public it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk getAnagraficoEnte(it.cnr.jada.UserContext param0) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk)invoke("getAnagraficoEnte",new Object[] {
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
public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getDefaultTerzo(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.anagraf00.core.bulk.TerzoBulk)invoke("getDefaultTerzo",new Object[] {
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
public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.jada.bulk.OggettoBulk)invoke("inizializzaBulkPerStampa",new Object[] {
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
public it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk setComune_fiscale(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1,it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk)invoke("setComune_fiscale",new Object[] {
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
public it.cnr.jada.bulk.OggettoBulk stampaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.jada.bulk.OggettoBulk)invoke("stampaConBulk",new Object[] {
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
public java.util.List bulkForSIP(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (java.util.List)invoke("bulkForSIP",new Object[] {
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
public void eliminaBulkForSIP(it.cnr.jada.UserContext param0,String param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("eliminaBulkForSIP",new Object[] {
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
public boolean isItalianoEsteroModificabile(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Boolean)invoke("isItalianoEsteroModificabile",new Object[] {
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
public void checkConiugeAlreadyExistFor(it.cnr.jada.UserContext param0, it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1, it.cnr.contab.anagraf00.core.bulk.Carico_familiare_anagBulk param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("checkConiugeAlreadyExistFor",new Object[] {
			param0,
			param1,
			param2});
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
public boolean isGestiteDeduzioniFamily(it.cnr.jada.UserContext param0) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Boolean)invoke("isGestiteDeduzioniFamily",new Object[] {
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
public boolean isGestiteDetrazioniAltre(it.cnr.jada.UserContext param0) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Boolean)invoke("isGestiteDetrazioniAltre",new Object[] {
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
public boolean esisteConiugeValido(it.cnr.jada.UserContext param0, it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1, it.cnr.contab.anagraf00.core.bulk.Carico_familiare_anagBulk param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Boolean)invoke("esisteConiugeValido",new Object[] {
			param0,
			param1,
			param2})).booleanValue();

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
	public java.util.List EstraiLista(it.cnr.jada.UserContext param0,Long param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (java.util.List)invoke("EstraiLista",new Object[] {
				param0,param1});
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
	public Long Max_prog_estrazione(it.cnr.jada.UserContext param0) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (Long)invoke("Max_prog_estrazione",new Object[] {
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
	public void Popola_ecf(it.cnr.jada.UserContext param0,Long param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			invoke("Popola_ecf",new Object[] {
				param0,param1 });
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
public boolean esisteFiglioValido(it.cnr.jada.UserContext param0, it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Boolean)invoke("esisteFiglioValido",new Object[] {
			param0,
			param1})).booleanValue();

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
public boolean esisteAltroFamValido(it.cnr.jada.UserContext param0, it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Boolean)invoke("esisteAltroFamValido",new Object[] {
			param0,
			param1})).booleanValue();

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
public Timestamp findMaxDataCompValida(it.cnr.jada.UserContext param0,AnagraficoBulk param1) throws PersistencyException,RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (Timestamp)invoke("findMaxDataCompValida",new Object[] {
			param0,
			param1});
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
public boolean verificaStrutturaPiva(UserContext param0,AnagraficoBulk param1) throws ComponentException,ValidationException, RemoteException {
	try {
		return ((Boolean)invoke("verificaStrutturaPiva",new Object[] {
			param0,
			param1})).booleanValue();

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
public void checkCaricoAlreadyExistFor(UserContext param0,
		AnagraficoBulk param1, Carico_familiare_anagBulk param2)
		throws ComponentException, RemoteException {
	try {
		invoke("checkCaricoAlreadyExistFor",new Object[] {
			param0,
			param1,
			param2});
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
public void controllaUnicitaCaricoInAnnoImposta(it.cnr.jada.UserContext param0, it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1, it.cnr.contab.anagraf00.core.bulk.Carico_familiare_anagBulk param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("controllaUnicitaCaricoInAnnoImposta",new Object[] {
			param0,
			param1,
			param2});
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
public boolean isGestitoCreditoIrpef(UserContext param0)throws ComponentException, RemoteException {
	try {
		return ((Boolean)invoke("isGestitoCreditoIrpef",new Object[] {
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
	public void aggiornaDatiAce(it.cnr.jada.UserContext param0, it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			invoke("aggiornaDatiAce",new Object[] {
					param0,
					param1});
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

