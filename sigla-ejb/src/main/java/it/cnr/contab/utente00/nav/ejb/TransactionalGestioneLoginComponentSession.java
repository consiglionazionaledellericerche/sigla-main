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

package it.cnr.contab.utente00.nav.ejb;
import java.rmi.*;
import java.util.Hashtable;
import java.util.List;

import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.utenze00.bulk.PreferitiBulk;
import it.cnr.contab.utenze00.bulk.SessionTraceBulk;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.ejb.*;

public class TransactionalGestioneLoginComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements GestioneLoginComponentSession {
	public it.cnr.contab.utenze00.bulk.UtenteBulk cambiaPassword(it.cnr.jada.UserContext param0,it.cnr.contab.utenze00.bulk.UtenteBulk param1,java.lang.String param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.utenze00.bulk.UtenteBulk)invoke("cambiaPassword",new Object[] {
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
	public boolean controllaAccesso(it.cnr.jada.UserContext param0,java.lang.String param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return ((Boolean)invoke("controllaAccesso",new Object[] {
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
	public it.cnr.contab.utenze00.bulk.Albero_mainBulk generaAlberoPerUtente(it.cnr.jada.UserContext param0,it.cnr.contab.utenze00.bulk.UtenteBulk param1,java.lang.String param2,java.lang.String param3,short param4) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.utenze00.bulk.Albero_mainBulk)invoke("generaAlberoPerUtente",new Object[] {
				param0,
				param1,
				param2,
				param3,
				new Short(param4) });
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
	public void leggiMessaggi(it.cnr.jada.UserContext param0,it.cnr.contab.messaggio00.bulk.MessaggioBulk[] param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			invoke("leggiMessaggi",new Object[] {
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
	public java.lang.Integer[] listaEserciziPerUtente(it.cnr.jada.UserContext param0,it.cnr.contab.utenze00.bulk.UtenteBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (java.lang.Integer[])invoke("listaEserciziPerUtente",new Object[] {
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
	public it.cnr.jada.util.RemoteIterator listaMessaggi(it.cnr.jada.UserContext param0,java.lang.String param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.jada.util.RemoteIterator)invoke("listaMessaggi",new Object[] {
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
	public it.cnr.jada.util.RemoteIterator listaUOPerUtente(it.cnr.jada.UserContext param0,it.cnr.contab.utenze00.bulk.UtenteBulk param1,java.lang.Integer param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.jada.util.RemoteIterator)invoke("listaUOPerUtente",new Object[] {
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
	public it.cnr.jada.util.RemoteIterator listaCdrPerUtente(it.cnr.jada.UserContext param0,it.cnr.contab.utenze00.bulk.UtenteBulk param1,java.lang.Integer param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.jada.util.RemoteIterator)invoke("listaCdrPerUtente",new Object[] {
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
	public void notificaMessaggi(it.cnr.jada.UserContext param0,java.lang.String param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			invoke("notificaMessaggi",new Object[] {
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
	public void registerUser(it.cnr.jada.UserContext param0,java.lang.String param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			invoke("registerUser",new Object[] {
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
	public void unregisterUser(it.cnr.jada.UserContext param0) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			invoke("unregisterUser",new Object[] {
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
	public void unregisterUsers(java.lang.String param0) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			invoke("unregisterUsers",new Object[] {
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
	public java.lang.String validaBPPerUtente(it.cnr.jada.UserContext param0,it.cnr.contab.utenze00.bulk.UtenteBulk param1,java.lang.String param2,java.lang.String param3) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (java.lang.String)invoke("validaBPPerUtente",new Object[] {
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
			} catch(Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception",ex);
			}
		}
	}
	public java.lang.Boolean isBPEnableForUser(it.cnr.jada.UserContext param0,it.cnr.contab.utenze00.bulk.UtenteBulk param1,java.lang.String param2,java.lang.String param3) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (java.lang.Boolean)invoke("isBPEnableForUser",new Object[] {
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
			} catch(Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception",ex);
			}
		}
	}	
	public it.cnr.contab.utenze00.bulk.Albero_mainBulk validaNodoPerUtente(it.cnr.jada.UserContext param0,it.cnr.contab.utenze00.bulk.UtenteBulk param1,java.lang.String param2,java.lang.String param3) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.utenze00.bulk.Albero_mainBulk)invoke("validaNodoPerUtente",new Object[] {
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
			} catch(Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception",ex);
			}
		}
	}
	public it.cnr.contab.utenze00.bulk.UtenteBulk validaUtente(it.cnr.jada.UserContext param0,it.cnr.contab.utenze00.bulk.UtenteBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.utenze00.bulk.UtenteBulk)invoke("validaUtente",new Object[] {
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
	public it.cnr.contab.utenze00.bulk.UtenteBulk validaUtente(it.cnr.jada.UserContext param0,it.cnr.contab.utenze00.bulk.UtenteBulk param1, int param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.utenze00.bulk.UtenteBulk)invoke("validaUtente",new Object[] {
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

	public java.util.List utentiMultipli(it.cnr.jada.UserContext param0,it.cnr.contab.utenze00.bulk.UtenteBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (java.util.List)invoke("utentiMultipli",new Object[] {
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
	public boolean isUtenteAbilitatoLdap(it.cnr.jada.UserContext param0,String param1,boolean param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (Boolean)invoke("isUtenteAbilitatoLdap",new Object[] {
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
	public void cambiaAbilitazioneUtente(it.cnr.jada.UserContext param0,String param1,boolean param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			invoke("cambiaAbilitazioneUtente",new Object[] {
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
	
	public List<SessionTraceBulk> sessionList(UserContext param0, String param1) throws ComponentException, RemoteException {
		try {
			return (List<SessionTraceBulk>)invoke("sessionList",new Object[] {
				param0, param1 });
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
	public CdrBulk cdrDaUo(it.cnr.jada.UserContext param0,Unita_organizzativaBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (CdrBulk)invoke("cdrDaUo",new Object[] {
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
	public List<PreferitiBulk> preferitiList(UserContext param0) throws ComponentException, RemoteException {
		try {
			return (List<PreferitiBulk>)invoke("preferitiList",new Object[] {
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

	public List getUnitaRuolo(UserContext param0, UtenteBulk utente) throws it.cnr.jada.comp.ComponentException, RemoteException{
		try {
			return (java.util.List)invoke("getUnitaRuolo",new Object[] {
				param0,
				utente });
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
	public List getRuoli(UserContext param0, UtenteBulk utente) throws it.cnr.jada.comp.ComponentException , RemoteException{
		try {
			return (java.util.List)invoke("getRuoli",new Object[] {
				param0,
				utente });
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
	public java.lang.Boolean isUserAccessoAllowed(it.cnr.jada.UserContext param0, String...param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (java.lang.Boolean)invoke("isUserAccessoAllowed",new Object[] {
				param0,
				param1 
			});
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
