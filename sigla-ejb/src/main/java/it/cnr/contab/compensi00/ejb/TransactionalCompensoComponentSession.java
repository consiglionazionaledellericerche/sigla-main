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

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import it.cnr.contab.compensi00.docs.bulk.BonusBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;

public class TransactionalCompensoComponentSession extends
		it.cnr.jada.ejb.TransactionalCRUDComponentSession implements
		CompensoComponentSession {
	public void aggiornaMontanti(it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			invoke("aggiornaMontanti", new Object[] { param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk aggiornaObbligazione(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,
			it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.compensi00.docs.bulk.CompensoBulk) invoke(
					"aggiornaObbligazione", new Object[] { param0, param1,
							param2 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public java.lang.Long assegnaProgressivo(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (java.lang.Long) invoke("assegnaProgressivo",
					new Object[] { param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public java.lang.Long assegnaProgressivoTemporaneo(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (java.lang.Long) invoke("assegnaProgressivoTemporaneo",
					new Object[] { param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public it.cnr.jada.util.RemoteIterator cercaObbligazioni(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.jada.util.RemoteIterator) invoke(
					"cercaObbligazioni", new Object[] { param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk completaTerzo(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,
			it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk param2)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.compensi00.docs.bulk.CompensoBulk) invoke(
					"completaTerzo", new Object[] { param0, param1, param2 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk confermaModificaCORI(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,
			it.cnr.contab.compensi00.docs.bulk.Contributo_ritenutaBulk param2)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.compensi00.docs.bulk.CompensoBulk) invoke(
					"confermaModificaCORI", new Object[] { param0, param1,
							param2 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk conguaglioAssociatoACompenso(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk) invoke(
					"conguaglioAssociatoACompenso", new Object[] { param0,
							param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public it.cnr.jada.bulk.OggettoBulk creaConBulk(
			it.cnr.jada.UserContext param0,
			it.cnr.jada.bulk.OggettoBulk param1,
			it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.jada.bulk.OggettoBulk) invoke("creaConBulk",
					new Object[] { param0, param1, param2 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk doContabilizzaCompensoCofi(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.compensi00.docs.bulk.CompensoBulk) invoke(
					"doContabilizzaCompensoCofi",
					new Object[] { param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public it.cnr.contab.compensi00.docs.bulk.EstrazioneCUDVBulk doElaboraCUD(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.EstrazioneCUDVBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.compensi00.docs.bulk.EstrazioneCUDVBulk) invoke(
					"doElaboraCUD", new Object[] { param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public it.cnr.contab.compensi00.docs.bulk.EstrazioneINPSMensileBulk doElaboraINPSMensile(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.EstrazioneINPSMensileBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.compensi00.docs.bulk.EstrazioneINPSMensileBulk) invoke(
					"doElaboraINPSMensile", new Object[] { param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk elaboraScadenze(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,
			it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param2,
			it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param3)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.compensi00.docs.bulk.CompensoBulk) invoke(
					"elaboraScadenze", new Object[] { param0, param1, param2,
							param3 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public void eliminaCompensoTemporaneo(it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,
			java.lang.Long param2) throws RemoteException,
			it.cnr.jada.comp.ComponentException {
		try {
			invoke("eliminaCompensoTemporaneo", new Object[] { param0, param1,
					param2 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk eliminaObbligazione(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.compensi00.docs.bulk.CompensoBulk) invoke(
					"eliminaObbligazione", new Object[] { param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk eseguiCalcolo(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.compensi00.docs.bulk.CompensoBulk) invoke(
					"eseguiCalcolo", new Object[] { param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public java.util.List findListaBanche(it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (java.util.List) invoke("findListaBanche", new Object[] {
					param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public java.util.Collection findModalita(it.cnr.jada.UserContext param0,
			it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException,
			it.cnr.jada.comp.ComponentException {
		try {
			return (java.util.Collection) invoke("findModalita", new Object[] {
					param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public java.util.Collection findTermini(it.cnr.jada.UserContext param0,
			it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException,
			it.cnr.jada.comp.ComponentException {
		try {
			return (java.util.Collection) invoke("findTermini", new Object[] {
					param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public java.util.Collection findTipiRapporto(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (java.util.Collection) invoke("findTipiRapporto",
					new Object[] { param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public java.util.Collection findTipiTrattamento(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (java.util.Collection) invoke("findTipiTrattamento",
					new Object[] { param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}
	
	public java.util.Collection findTipiPrestazioneCompenso(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (java.util.Collection) invoke("findTipiPrestazioneCompenso",
					new Object[] { param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public java.sql.Timestamp getDataOdierna(it.cnr.jada.UserContext param0)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (java.sql.Timestamp) invoke("getDataOdierna",
					new Object[] { param0 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(
			it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.jada.bulk.OggettoBulk) invoke(
					"inizializzaBulkPerStampa", new Object[] { param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk inizializzaCompensoPerMinicarriera(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,
			it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk param2,
			java.util.List param3) throws RemoteException,
			it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.compensi00.docs.bulk.CompensoBulk) invoke(
					"inizializzaCompensoPerMinicarriera", new Object[] {
							param0, param1, param2, param3 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk inizializzaCompensoPerMissione(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,
			it.cnr.contab.missioni00.docs.bulk.MissioneBulk param2)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.compensi00.docs.bulk.CompensoBulk) invoke(
					"inizializzaCompensoPerMissione", new Object[] { param0,
							param1, param2 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk inserisciCompenso(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.compensi00.docs.bulk.CompensoBulk) invoke(
					"inserisciCompenso", new Object[] { param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public boolean isCompensoAnnullato(it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return ((Boolean) invoke("isCompensoAnnullato", new Object[] {
					param0, param1 })).booleanValue();
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public java.util.List loadDocContAssociati(it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (java.util.List) invoke("loadDocContAssociati",
					new Object[] { param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public it.cnr.jada.bulk.OggettoBulk modificaConBulk(
			it.cnr.jada.UserContext param0,
			it.cnr.jada.bulk.OggettoBulk param1,
			it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.jada.bulk.OggettoBulk) invoke("modificaConBulk",
					new Object[] { param0, param1, param2 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk onTipoTrattamentoChange(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.compensi00.docs.bulk.CompensoBulk) invoke(
					"onTipoTrattamentoChange", new Object[] { param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk onTipoRapportoInpsChange(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.compensi00.docs.bulk.CompensoBulk) invoke(
					"onTipoRapportoInpsChange", new Object[] { param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk reloadCompenso(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.compensi00.docs.bulk.CompensoBulk) invoke(
					"reloadCompenso", new Object[] { param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk resyncScadenza(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk) invoke(
					"resyncScadenza", new Object[] { param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk riportaAvanti(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk param1,
			it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk) invoke(
					"riportaAvanti", new Object[] { param0, param1, param2 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public void rollbackToSavePoint(it.cnr.jada.UserContext param0,
			java.lang.String param1) throws RemoteException,
			it.cnr.jada.comp.ComponentException {
		try {
			invoke("rollbackToSavePoint", new Object[] { param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public void setSavePoint(it.cnr.jada.UserContext param0,
			java.lang.String param1) throws RemoteException,
			it.cnr.jada.comp.ComponentException {
		try {
			invoke("setSavePoint", new Object[] { param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public it.cnr.jada.bulk.OggettoBulk stampaConBulk(
			it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.jada.bulk.OggettoBulk) invoke("stampaConBulk",
					new Object[] { param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk updateImportoAssociatoDocAmm(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk) invoke(
					"updateImportoAssociatoDocAmm", new Object[] { param0,
							param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public void validaObbligazione(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param1,
			it.cnr.jada.bulk.OggettoBulk param2) throws RemoteException,
			it.cnr.jada.comp.ComponentException {
		try {
			invoke("validaObbligazione",
					new Object[] { param0, param1, param2 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public void validaTerzo(it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			invoke("validaTerzo", new Object[] { param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public int validaTerzo(it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,
			boolean param2) throws RemoteException,
			it.cnr.jada.comp.ComponentException {
		try {
			return ((Integer) invoke("validaTerzo", new Object[] { param0,
					param1, new Boolean(param2) })).intValue();
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public it.cnr.contab.compensi00.docs.bulk.Estrazione770Bulk doElabora770(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.Estrazione770Bulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.compensi00.docs.bulk.Estrazione770Bulk) invoke(
					"doElabora770", new Object[] { param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	// aggiunto per testare l'esercizio della data competenza da/a
	public boolean isEsercizioChiusoPerDataCompetenza(
			it.cnr.jada.UserContext param0, Integer param1,
			java.lang.String param2) throws ComponentException,
			PersistencyException, RemoteException {
		try {
			return ((Boolean) invoke("isEsercizioChiusoPerDataCompetenza",
					new Object[] { param0, param1, param2 })).booleanValue();
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public boolean verificaEsistenzaCompenso(UserContext param0,
			CompensoBulk param1) throws ComponentException, RemoteException {
		try {
			return ((Boolean) invoke("verificaEsistenzaCompenso", new Object[] {
					param0, param1 })).booleanValue();
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public boolean isCompensoValido(UserContext param0, CompensoBulk param1)
			throws ComponentException, RemoteException {
		try {
			return ((Boolean) invoke("isCompensoValido", new Object[] { param0,
					param1 })).booleanValue();
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public boolean isTerzoCervellone(it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return ((Boolean) invoke("isTerzoCervellone", new Object[] {
					param0, param1 })).booleanValue();

		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public boolean isAccontoAddComOkPerContabil(it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return ((Boolean) invoke("isAccontoAddComOkPerContabil",
					new Object[] { param0, param1 })).booleanValue();

		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public boolean isGestiteDeduzioniIrpef(it.cnr.jada.UserContext param0)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return ((Boolean) invoke("isGestiteDeduzioniIrpef",
					new Object[] { param0 })).booleanValue();

		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk completaIncarico(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,
			it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_annoBulk param2)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.compensi00.docs.bulk.CompensoBulk) invoke(
					"completaIncarico", new Object[] { param0, param1, param2 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public BigDecimal prendiUtilizzato(it.cnr.jada.UserContext param0,
			it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_annoBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (BigDecimal) invoke("prendiUtilizzato", new Object[] {
					param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public it.cnr.contab.compensi00.tabrif.bulk.Acconto_classific_coriBulk doCalcolaAccontoAddCom(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.tabrif.bulk.Acconto_classific_coriBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.compensi00.tabrif.bulk.Acconto_classific_coriBulk) invoke(
					"doCalcolaAccontoAddCom", new Object[] { param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public List findListaCompensiSIP(UserContext userContext, String query,
			String dominio, String uo, String terzo, String voce, String cdr,
			String gae, String tipoRicerca, Timestamp data_inizio,
			Timestamp data_fine) throws ComponentException, RemoteException {
		try {
			return (List) invoke("findListaCompensiSIP", new Object[] {
					userContext, query, dominio, uo, terzo, voce, cdr, gae,
					tipoRicerca, data_inizio, data_fine });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public CompensoBulk inizializzaCompensoPerBonus(UserContext param0,
			CompensoBulk param1, BonusBulk param2) throws ComponentException,
			RemoteException {
		try {
			return (it.cnr.contab.compensi00.docs.bulk.CompensoBulk) invoke(
					"inizializzaCompensoPerBonus", new Object[] { param0,
							param1, param2 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public boolean isSospensioneIrpefOkPerContabil(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return ((Boolean) invoke("isSospensioneIrpefOkPerContabil",
					new Object[] { param0, param1 })).booleanValue();

		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}
	public void archiviaStampa(UserContext userContext, Date fromDate,
			Date untilDate, CompensoBulk compensoBulk, Integer... years) throws ComponentException,
			java.rmi.RemoteException {
		try {
			invoke("archiviaStampa", new Object[] { userContext, fromDate,
					untilDate, compensoBulk, years });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}
	
	public CompensoBulk ricercaCompensoTrovato(UserContext userContext,
			Long esercizio, String cd_cds, String cd_unita_organizzativa,
			Long pg_compenso) throws ComponentException, RemoteException,
			PersistencyException {
		try {
			return ((CompensoBulk) invoke("ricercaCompensoTrovato", new Object[] { userContext, esercizio, cd_cds, cd_unita_organizzativa, pg_compenso }));
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public CompensoBulk ricercaCompensoByKey(UserContext userContext,
			Long esercizio, String cd_cds, String cd_unita_organizzativa,
			Long pg_compenso) throws ComponentException, RemoteException,
			PersistencyException {
		try {
			return ((CompensoBulk) invoke("ricercaCompensoByKey", new Object[] { userContext, esercizio, cd_cds, cd_unita_organizzativa, pg_compenso }));
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}

	public List<CompensoBulk> ricercaCompensiTrovato(UserContext userContext,
			Long trovato) throws ComponentException, RemoteException,
			PersistencyException {
		try {
			return ((List<CompensoBulk>) invoke("ricercaCompensiTrovato", new Object[] { userContext, trovato }));
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}
	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk inizializzaCompensoPerFattura(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,
			it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk param2) 
			throws RemoteException,
			it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.compensi00.docs.bulk.CompensoBulk) invoke(
					"inizializzaCompensoPerFattura", new Object[] {
							param0, param1, param2 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}
	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk valorizzaInfoDocEle(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.compensi00.docs.bulk.CompensoBulk) invoke(
					"valorizzaInfoDocEle", new Object[] { param0, param1 });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}
}
