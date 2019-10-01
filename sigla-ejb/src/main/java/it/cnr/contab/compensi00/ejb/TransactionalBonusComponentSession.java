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

package it.cnr.contab.compensi00.ejb;

import java.rmi.*;
import java.sql.SQLException;
import java.util.List;

import it.cnr.contab.compensi00.docs.bulk.BonusBulk;
import it.cnr.contab.compensi00.docs.bulk.Bonus_nucleo_famBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

public class TransactionalBonusComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements BonusComponentSession {

	public void checkCodiceFiscale(UserContext param0, BonusBulk param1)
			throws ComponentException, RemoteException, SQLException {
		try {
			invoke("checkCodiceFiscale",new Object[] {
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

	public void checkCodiceFiscaleComponente(UserContext param0,
			Bonus_nucleo_famBulk param1) throws ComponentException,
			RemoteException, SQLException {
		try {
			invoke("checkCodiceFiscaleComponente",new Object[] {
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

	public BonusBulk completaBonus(UserContext param0, BonusBulk param1)
			throws ComponentException, RemoteException {
		try {
			return (it.cnr.contab.compensi00.docs.bulk.BonusBulk)invoke("completaBonus",new Object[] {
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

	public BonusBulk recuperoDati(UserContext param0, BonusBulk param1)
			throws ComponentException, RemoteException, SQLException {
		try {
			return (it.cnr.contab.compensi00.docs.bulk.BonusBulk)invoke("recuperoDati",new Object[] {
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

	public Boolean verificaLimiteFamiliareCarico(UserContext param0,
			Bonus_nucleo_famBulk param1) throws ComponentException,
			RemoteException {
		try {
			return ((Boolean)invoke("verificaLimiteFamiliareCarico",new Object[] {
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

	public CompensoBulk cercaCompensoPerBonus(UserContext param0,
			BonusBulk param1) throws ComponentException, RemoteException {
		try {
			return (it.cnr.contab.compensi00.docs.bulk.CompensoBulk)invoke("cercaCompensoPerBonus",new Object[] {
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

	public List estraiDettagli(UserContext param0, BonusBulk param1)
		 throws ComponentException, RemoteException {
			try {
				return (java.util.List)invoke("estraiDettagli",new Object[] {
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

	public List estraiLista(UserContext param0) throws ComponentException, RemoteException {
			try {
				return (java.util.List)invoke("estraiLista",new Object[] {
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

	public String recuperaCodiceFiscaleInvio(UserContext param0)
			throws ComponentException, RemoteException {
		try {
			return (String)invoke("recuperaCodiceFiscaleInvio",new Object[] {
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

	public void aggiornaInvio(UserContext param0)
			throws ComponentException, RemoteException {
		try {
			invoke("aggiornaInvio",new Object[] {
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

