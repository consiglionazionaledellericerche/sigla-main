/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.ordmag.magazzino.ejb;
import java.rmi.RemoteException;
import java.util.List;

import it.cnr.contab.ordmag.magazzino.bulk.AbilitazioneMagazzinoBulk;
import it.cnr.contab.ordmag.magazzino.bulk.BollaScaricoMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.CaricoMagazzinoBulk;
import it.cnr.contab.ordmag.magazzino.bulk.LottoMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagazzinoBulk;
import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagazzinoRigaBulk;
import it.cnr.contab.ordmag.magazzino.bulk.ParametriSelezioneMovimentiBulk;
import it.cnr.contab.ordmag.magazzino.bulk.ScaricoMagazzinoBulk;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineRigaBulk;
import it.cnr.contab.ordmag.ordini.bulk.FatturaOrdineBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.RemoteIterator;

public class TransactionalMovimentiMagComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements MovimentiMagComponentSession {
public MovimentiMagBulk caricoDaOrdine(UserContext userContext, OrdineAcqConsegnaBulk consegna, EvasioneOrdineRigaBulk evasioneOrdineRiga)throws ComponentException, PersistencyException, RemoteException, ApplicationException{
	try {
		return (MovimentiMagBulk)invoke("caricoDaOrdine",new Object[] {
				userContext,
				consegna, evasioneOrdineRiga});
	} catch(RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new RemoteException("Uncaugth exception",ex);
		}
	}
}
public java.util.Collection<LottoMagBulk> findLottiMagazzino(UserContext userContext, MovimentiMagazzinoRigaBulk movimentiMagazzinoRigaBulk) throws ComponentException, PersistencyException , RemoteException, ApplicationException {
	try {
		return (java.util.Collection<LottoMagBulk>)invoke("findLottiMagazzino",new Object[] {
				userContext, movimentiMagazzinoRigaBulk});
	} catch(RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new RemoteException("Uncaugth exception",ex);
		}
	}
}
public List<BollaScaricoMagBulk> generaBolleScarico(UserContext userContext, List<MovimentiMagBulk> listaMovimentiScarico)
		throws ComponentException, PersistencyException, RemoteException, ApplicationException{
	try {
		return (List<BollaScaricoMagBulk>)invoke("generaBollaScarico",new Object[] {
				userContext, listaMovimentiScarico});
	} catch(RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new RemoteException("Uncaugth exception",ex);
		}
	}
}

public ScaricoMagazzinoBulk scaricaMagazzino(UserContext userContext, ScaricoMagazzinoBulk scaricoMagazzino)
		throws ComponentException, PersistencyException, RemoteException, ApplicationException{
	try {
		return (ScaricoMagazzinoBulk)invoke("scaricaMagazzino",new Object[] {
				userContext, scaricoMagazzino});
	} catch(RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new RemoteException("Uncaugth exception",ex);
		}
	}
}

public CaricoMagazzinoBulk caricaMagazzino(UserContext userContext, CaricoMagazzinoBulk caricoMagazzino)
		throws ComponentException, PersistencyException, RemoteException, ApplicationException{
	try {
		return (CaricoMagazzinoBulk)invoke("caricaMagazzino",new Object[] {
				userContext, caricoMagazzino});
	} catch(RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new RemoteException("Uncaugth exception",ex);
		}
	}
}

public MovimentiMagazzinoBulk initializeMovimentiMagazzino(UserContext userContext, MovimentiMagazzinoBulk scaricoMagazzino)
		throws ComponentException, PersistencyException, RemoteException, ApplicationException{
	try {
		return (MovimentiMagazzinoBulk)invoke("initializeMovimentiMagazzino",new Object[] {
				userContext, scaricoMagazzino});
	} catch(RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new RemoteException("Uncaugth exception",ex);
		}
	}
}
public AbilitazioneMagazzinoBulk initializeAbilitazioneMovimentiMagazzino(UserContext userContext, AbilitazioneMagazzinoBulk abilitazioneMagazzinoBulk)
		throws ComponentException, PersistencyException, RemoteException, ApplicationException{
	try {
		return (AbilitazioneMagazzinoBulk)invoke("initializeAbilitazioneMovimentiMagazzino",new Object[] {
				userContext, abilitazioneMagazzinoBulk});
	} catch(RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new RemoteException("Uncaugth exception",ex);
		}
	}
}

public RemoteIterator preparaQueryBolleScaricoDaVisualizzare(UserContext userContext, List<BollaScaricoMagBulk> bolle)throws ComponentException, RemoteException{
	try {
		return (RemoteIterator)invoke("preparaQueryBolleScaricoDaVisualizzare",new Object[] {
				userContext,
				bolle});
	} catch(RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new RemoteException("Uncaugth exception",ex);
		}
	}
}
public RemoteIterator ricercaMovimenti(UserContext userContext, ParametriSelezioneMovimentiBulk parametri) throws ComponentException, RemoteException{
	try {
		return (RemoteIterator)invoke("ricercaMovimenti",new Object[] {
				userContext,
				parametri});
	} catch(RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new RemoteException("Uncaugth exception",ex);
		}
	}
}
public void annullaMovimento(UserContext userContext, MovimentiMagBulk movimentiMagBulk)
		throws ComponentException, PersistencyException, RemoteException, ApplicationException{
	try {
		invoke("annullaMovimento",new Object[] {
				userContext, movimentiMagBulk});
	} catch(RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new RemoteException("Uncaugth exception",ex);
		}
	}
}


	@Override
	public OggettoBulk inizializzaBulkPerStampa(UserContext userContext, OggettoBulk oggettoBulk) throws ComponentException, RemoteException {
		try {
			return (it.cnr.jada.bulk.OggettoBulk)invoke("stampaConBulk",new Object[] {
					userContext,
					oggettoBulk });
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
	public OggettoBulk stampaConBulk(UserContext userContext, OggettoBulk oggettoBulk) throws ComponentException, RemoteException {
		try {
			return (it.cnr.jada.bulk.OggettoBulk)invoke("stampaConBulk",new Object[] {
					userContext,
					oggettoBulk });
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

	public MovimentiMagBulk creaMovimentoRettificaValoreOrdine(UserContext userContext, FatturaOrdineBulk fatturaOrdineBulk) throws ComponentException, RemoteException{
		try {
			return (MovimentiMagBulk) invoke("creaMovimentoRettificaValoreOrdine",new Object[] {
					userContext,
					fatturaOrdineBulk});
		} catch(RemoteException e) {
			throw e;
		} catch(java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch(ComponentException ex) {
				throw ex;
			} catch(Throwable ex) {
				throw new RemoteException("Uncaugth exception",ex);
			}
		}
	}

}
