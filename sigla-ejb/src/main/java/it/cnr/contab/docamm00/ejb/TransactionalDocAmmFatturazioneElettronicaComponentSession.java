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

package it.cnr.contab.docamm00.ejb;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.docamm00.comp.DocAmmFatturazioneElettronicaComponent;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoElettronicoBulk;
import it.cnr.contab.docamm00.docs.bulk.VDocammElettroniciAttiviBulk;
import it.cnr.contab.docamm00.docs.bulk.VDocammElettroniciAttiviHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v1.FatturaElettronicaType;

import java.rmi.RemoteException;
import java.util.Date;

import javax.xml.bind.JAXBElement;

public class TransactionalDocAmmFatturazioneElettronicaComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements DocAmmFatturazioneElettronicaComponentSession {

	public String recuperoNomeFileXml(it.cnr.jada.UserContext param0,Fattura_attivaBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (String)invoke("recuperoNomeFileXml",new Object[] {
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

	public Configurazione_cnrBulk getAuthenticatorPecSdi(it.cnr.jada.UserContext param0) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (Configurazione_cnrBulk)invoke("getAuthenticatorPecSdi",new Object[] {
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

@SuppressWarnings("unchecked")
	public JAXBElement<FatturaElettronicaType> creaFatturaElettronicaType(it.cnr.jada.UserContext param0,IDocumentoAmministrativoElettronicoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (JAXBElement<FatturaElettronicaType>)invoke("creaFatturaElettronicaType",new Object[] {
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

	@Override
	public String recuperoNomeFileXml(UserContext userContext, IDocumentoAmministrativoElettronicoBulk docammElettronico) throws RemoteException, ComponentException {
		try {
			return (String)invoke("recuperoNomeFileXml",new Object[] {
					userContext,
					docammElettronico});
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

	public FatturaElettronicaType preparaFattura(UserContext userContext, VDocammElettroniciAttiviBulk vDocamm) throws RemoteException,ComponentException{
		try {
			return (FatturaElettronicaType)invoke("preparaFattura",new Object[] {
					userContext,
					vDocamm});
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

	public void aggiornaMetadati(UserContext userContext, Integer esercizio, String cdCds, Long pgFatturaAttiva)throws RemoteException,ComponentException{
		try {
			invoke("aggiornaMetadati",new Object[] {
					userContext,
					esercizio, cdCds, pgFatturaAttiva});
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
	public String recuperoInizioNomeFile(UserContext param0) throws RemoteException,ComponentException {
		try {
			return (String)invoke("recuperoInizioNomeFile",new Object[] {
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

	@Override
	public FatturaElettronicaType preparaFattura(UserContext userContext, IDocumentoAmministrativoElettronicoBulk docamm) throws RemoteException, ComponentException {
		return null;
	}

	public IDocumentoAmministrativoElettronicoBulk castDocumentoElettronico(UserContext userContext, VDocammElettroniciAttiviBulk vDocamm) throws RemoteException,ComponentException{
		try {
			return (IDocumentoAmministrativoElettronicoBulk)invoke("castDocumentoElettronico",new Object[] {
					userContext,
					vDocamm});
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

	public IDocumentoAmministrativoElettronicoBulk aggiornaDocammElettronicoPredispostoAllaFirma(UserContext userContext, IDocumentoAmministrativoElettronicoBulk docammElettronico) throws ComponentException, RemoteException {
		try {
			return (IDocumentoAmministrativoElettronicoBulk)invoke("aggiornaDocammElettronicoPredispostoAllaFirma",new Object[] {
					userContext,
					docammElettronico});
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
	public IDocumentoAmministrativoElettronicoBulk aggiornaFatturaConsegnaSDI(UserContext userContext, IDocumentoAmministrativoElettronicoBulk docammElettronico, Date dataConsegnaSdi) throws RemoteException, ComponentException{
		try {
			return (IDocumentoAmministrativoElettronicoBulk)invoke("aggiornaFatturaConsegnaSDI",new Object[] {
					userContext,
					docammElettronico,
					dataConsegnaSdi});
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

	public IDocumentoAmministrativoElettronicoBulk aggiornaFatturaMancataConsegnaInvioSDI(UserContext userContext, IDocumentoAmministrativoElettronicoBulk docammElettronico, String codiceSdi, String noteInvioSdi) throws RemoteException, ComponentException{
		try {
			return (IDocumentoAmministrativoElettronicoBulk)invoke("aggiornaFatturaConsegnaSDI",new Object[] {
					userContext,
					docammElettronico,
					codiceSdi,
					noteInvioSdi});
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
