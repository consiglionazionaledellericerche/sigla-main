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

	package it.cnr.contab.ordmag.ordini.ejb;

	import it.cnr.contab.config00.contratto.bulk.Dettaglio_contrattoBulk;
	import it.cnr.contab.config00.pdcep.bulk.ContoBulk;
	import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
	import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
	import it.cnr.contab.ordmag.ordini.bulk.*;
	import it.cnr.contab.ordmag.ordini.dto.ImportoOrdine;
	import it.cnr.contab.ordmag.ordini.dto.ParametriCalcoloImportoOrdine;
	import it.cnr.jada.UserContext;
	import it.cnr.jada.comp.ApplicationException;
	import it.cnr.jada.comp.ComponentException;
	import it.cnr.jada.persistency.PersistencyException;
	import it.cnr.jada.util.RemoteIterator;

	import java.rmi.RemoteException;

	public class TransactionalOrdineAcqComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements OrdineAcqComponentSession {
	public void gestioneStampaOrdine(UserContext userContext, OrdineAcqBulk ordine) throws RemoteException,it.cnr.jada.comp.ComponentException{
		try {
			invoke("gestioneStampaOrdine",new Object[] {
				userContext,
				ordine });
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

		public void controllaQuadraturaObbligazioni(UserContext userContext,OrdineAcqBulk ordine) throws RemoteException,ComponentException, PersistencyException{
			try {
				invoke("controllaQuadraturaObbligazioni",new Object[] {
						userContext,
						ordine });
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

	public void completaOrdine(UserContext userContext, OrdineAcqBulk ordine) throws RemoteException,ComponentException, PersistencyException{
		try {
			invoke("completaOrdine",new Object[] {
				userContext,
				ordine });
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
	public Boolean isUtenteAbilitatoOrdine(UserContext usercontext, OrdineAcqBulk ordine) throws ComponentException, PersistencyException,javax.ejb.EJBException, RemoteException{
		try {
			return (Boolean)invoke("isUtenteAbilitatoOrdine",new Object[] {
					usercontext,
					ordine });
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
	public Boolean isUtenteAbilitatoValidazioneOrdine(UserContext usercontext, OrdineAcqBulk ordine) throws ComponentException, PersistencyException,javax.ejb.EJBException, RemoteException{
		try {
			return (Boolean)invoke("isUtenteAbilitatoValidazioneOrdine",new Object[] {
					usercontext,
					ordine });
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
	public it.cnr.jada.util.RemoteIterator cercaObbligazioni(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.jada.util.RemoteIterator)invoke("cercaObbligazioni",new Object[] {
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
	public OrdineAcqBulk contabilizzaDettagliSelezionati(it.cnr.jada.UserContext param0,OrdineAcqBulk param1,java.util.Collection param2,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param3) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (OrdineAcqBulk)invoke("contabilizzaDettagliSelezionati",new Object[] {
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
	public OrdineAcqBulk calcolaImportoOrdine(it.cnr.jada.UserContext userContext, OrdineAcqBulk ordine) throws RemoteException,ComponentException, PersistencyException{
		try {
			return (OrdineAcqBulk)invoke("calcolaImportoOrdine",new Object[] {
					userContext,
					ordine });
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

	public OrdineAcqBulk cancellaOrdine(it.cnr.jada.UserContext userContext, OrdineAcqBulk ordine) throws RemoteException,ComponentException, PersistencyException{
		try {
			return (OrdineAcqBulk)invoke("cancellaOrdine",new Object[] {
					userContext,
					ordine });
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

	public Unita_organizzativaBulk recuperoUoPerImpegno(it.cnr.jada.UserContext userContext, OrdineAcqConsegnaBulk consegna) throws RemoteException,ComponentException, PersistencyException{
		try {
			return (Unita_organizzativaBulk)invoke("recuperoUoPerImpegno",new Object[] {
					userContext,
					consegna});
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

	public OrdineAcqBulk contabilizzaConsegneSelezionate(it.cnr.jada.UserContext param0,OrdineAcqBulk param1,java.util.Collection param2,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param3) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (OrdineAcqBulk)invoke("contabilizzaConsegneSelezionate",new Object[] {
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
	public ImportoOrdine calcoloImportoOrdine(UserContext userContext, ParametriCalcoloImportoOrdine parametri) throws RemoteException,ComponentException{
		try {
			return (ImportoOrdine)invoke("calcoloImportoOrdine",new Object[] {
				userContext,
				parametri });
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
	public ImportoOrdine calcoloImportoOrdinePerMagazzino(UserContext userContext, ParametriCalcoloImportoOrdine parametri) throws RemoteException,ComponentException{
		try {
			return (ImportoOrdine)invoke("calcoloImportoOrdinePerMagazzino",new Object[] {
				userContext,
				parametri });
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
		public AbilitazioneOrdiniAcqBulk initializeAbilitazioneOrdiniAcq(UserContext userContext, AbilitazioneOrdiniAcqBulk abilitazioneOrdiniAcqBulk)
				throws ComponentException, PersistencyException, RemoteException, ApplicationException {
			try {
				return (AbilitazioneOrdiniAcqBulk) invoke("initializeAbilitazioneOrdiniAcq", new Object[]{
						userContext, abilitazioneOrdiniAcqBulk});
			} catch (RemoteException e) {
				throw e;
			} catch (java.lang.reflect.InvocationTargetException e) {
				try {
					throw e.getTargetException();
				} catch (ComponentException ex) {
					throw ex;
				} catch (Throwable ex) {
					throw new RemoteException("Uncaugth exception", ex);
				}
			}
		}

		public RemoteIterator ricercaOrdiniAcqCons(UserContext userContext, ParametriSelezioneOrdiniAcqBulk parametri,String tipoSelezione) throws ComponentException, RemoteException{
			try {
				return (RemoteIterator)invoke("ricercaOrdiniAcqCons",new Object[] {
						userContext,
						parametri,
						tipoSelezione});
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
		public ContoBulk recuperoContoDefault(UserContext userContext, Categoria_gruppo_inventBulk categoria_gruppo_inventBulk) throws ComponentException, PersistencyException, RemoteException {
			try {
				return (ContoBulk) invoke("recuperoContoDefault", new Object[]{
						userContext, categoria_gruppo_inventBulk});
			} catch (RemoteException e) {
				throw e;
			} catch (java.lang.reflect.InvocationTargetException e) {
				try {
					throw e.getTargetException();
				} catch (ComponentException ex) {
					throw ex;
				} catch (Throwable ex) {
					throw new RemoteException("Uncaugth exception", ex);
				}
			}
		}

		@Override
		public void chiusuraForzataOrdini(UserContext userContext, OrdineAcqConsegnaBulk ordineAcqConsegnaBulk) throws ComponentException, PersistencyException, RemoteException, ApplicationException {
			try {
				invoke("chiusuraForzataOrdini",new Object[] {
						userContext, ordineAcqConsegnaBulk});
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
		public Dettaglio_contrattoBulk recuperoDettaglioContratto(UserContext userContext, OrdineAcqRigaBulk riga) throws ComponentException, PersistencyException, RemoteException {
			try {
				return (Dettaglio_contrattoBulk) invoke("recuperoDettaglioContratto", new Object[]{
						userContext, riga});
			} catch (RemoteException e) {
				throw e;
			} catch (java.lang.reflect.InvocationTargetException e) {
				try {
					throw e.getTargetException();
				} catch (ComponentException ex) {
					throw ex;
				} catch (Throwable ex) {
					throw new RemoteException("Uncaugth exception", ex);
				}
			}
		}
	}
