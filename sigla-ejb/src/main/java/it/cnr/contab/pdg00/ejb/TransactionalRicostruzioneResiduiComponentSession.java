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

/*
 * Created on Jun 20, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg00.ejb;

import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.pdg00.bulk.Pdg_residuoBulk;
import it.cnr.jada.util.ejb.TransactionalSessionImpl;

import java.rmi.RemoteException;

/**
 * @author mincarnato
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TransactionalRicostruzioneResiduiComponentSession
	extends it.cnr.jada.ejb.TransactionalCRUDComponentSession
	implements RicostruzioneResiduiComponentSession {

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
		public CdrBulk findCdrUo(it.cnr.jada.UserContext param0) throws RemoteException,it.cnr.jada.comp.ComponentException {
			try {
				return (CdrBulk)invoke("findCdrUo",new Object[] {
					param0});
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
		public CdrBulk findCdr(it.cnr.jada.UserContext param0) throws RemoteException,it.cnr.jada.comp.ComponentException {
			try {
				return (CdrBulk)invoke("findCdr",new Object[] {
					param0});
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
		public boolean isUOScrivaniaEnte(it.cnr.jada.UserContext param0) throws RemoteException,it.cnr.jada.comp.ComponentException {
			try {
				return ((Boolean)invoke("isUOScrivaniaEnte",new Object[] {
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
		public Pdg_residuoBulk calcolaDispCassaPerCds(it.cnr.jada.UserContext param0,Pdg_residuoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
			try {
				return (Pdg_residuoBulk)invoke("calcolaDispCassaPerCds",new Object[] {
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
		public boolean isCdrSAC(it.cnr.jada.UserContext param0,CdrBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
			try {
				return ((Boolean)invoke("isCdrSAC",new Object[] {
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
		public it.cnr.contab.pdg00.bulk.Pdg_residuoBulk caricaDettagliFiltrati(it.cnr.jada.UserContext param0, it.cnr.jada.persistency.sql.CompoundFindClause param1, it.cnr.contab.pdg00.bulk.Pdg_residuoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException {
			try {
				return (Pdg_residuoBulk)invoke("caricaDettagliFiltrati",new Object[] {
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
		
}
