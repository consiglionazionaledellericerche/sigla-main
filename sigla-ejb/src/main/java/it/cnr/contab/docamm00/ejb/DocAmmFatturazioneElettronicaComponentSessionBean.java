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
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoElettronicoBulk;
import it.cnr.contab.docamm00.docs.bulk.VDocammElettroniciAttiviBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v1.FatturaElettronicaType;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBElement;
import java.rmi.RemoteException;
import java.util.Date;

/**
 * Bean implementation class for Enterprise Bean: CNRDOCAMM00_EJB_DocAmmFatturazioneElettronicaComponentSession
 */
@Stateless(name="CNRDOCAMM00_EJB_DocAmmFatturazioneElettronicaComponentSession")
public class DocAmmFatturazioneElettronicaComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements DocAmmFatturazioneElettronicaComponentSession  {
	@PostConstruct
	public void ejbCreate() {
		componentObj = new DocAmmFatturazioneElettronicaComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws EJBException {
		return new DocAmmFatturazioneElettronicaComponentSessionBean();
	}
	
	public Configurazione_cnrBulk getAuthenticatorPecSdi(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
		pre_component_invocation(userContext,componentObj);
		try {
			Configurazione_cnrBulk result = ((DocAmmFatturazioneElettronicaComponent)componentObj).getAuthenticatorPecSdi(userContext);
			component_invocation_succes(userContext,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(userContext,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(userContext,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(userContext,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(userContext,componentObj,e);
		}
	}
	public JAXBElement<FatturaElettronicaType> creaFatturaElettronicaType(UserContext userContext, IDocumentoAmministrativoElettronicoBulk docammElettronico)  throws it.cnr.jada.comp.ComponentException{
		pre_component_invocation(userContext,componentObj);
		try {
			JAXBElement<FatturaElettronicaType> result = ((DocAmmFatturazioneElettronicaComponent)componentObj).creaFatturaElettronicaType(userContext, docammElettronico);
			component_invocation_succes(userContext,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(userContext,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(userContext,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(userContext,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(userContext,componentObj,e);
		}
	}

	public String recuperoNomeFileXml(UserContext userContext, IDocumentoAmministrativoElettronicoBulk docammElettronico)  throws RemoteException, it.cnr.jada.comp.ComponentException{
		pre_component_invocation(userContext,componentObj);
		try {
			String result = ((DocAmmFatturazioneElettronicaComponent)componentObj).recuperoNomeFileXml(userContext, docammElettronico);
			component_invocation_succes(userContext,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(userContext,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(userContext,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(userContext,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(userContext,componentObj,e);
		}
	}

	public String recuperoInizioNomeFile(UserContext userContext) throws RemoteException,ComponentException {
		pre_component_invocation(userContext,componentObj);
		try {
			String result = ((DocAmmFatturazioneElettronicaComponent)componentObj).recuperoInizioNomeFile(userContext);
			component_invocation_succes(userContext,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(userContext,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(userContext,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(userContext,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(userContext,componentObj,e);
		}
	}
	
	public FatturaElettronicaType preparaFattura(UserContext userContext, IDocumentoAmministrativoElettronicoBulk docammElettronico)throws ComponentException{
		pre_component_invocation(userContext,componentObj);
		try {
			FatturaElettronicaType result = ((DocAmmFatturazioneElettronicaComponent)componentObj).preparaFattura(userContext, docammElettronico);
			component_invocation_succes(userContext,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(userContext,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(userContext,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(userContext,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(userContext,componentObj,e);
		}
	}
	public void aggiornaMetadati(UserContext userContext, Integer esercizio, String cdCds, Long pgFatturaAttiva)throws ComponentException{
		pre_component_invocation(userContext,componentObj);
		try {
			((DocAmmFatturazioneElettronicaComponent)componentObj).aggiornaMetadati(userContext, esercizio, cdCds,  pgFatturaAttiva);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(userContext,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(userContext,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(userContext,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(userContext,componentObj,e);
		}
	}

	public IDocumentoAmministrativoElettronicoBulk castDocumentoElettronico(UserContext userContext, VDocammElettroniciAttiviBulk vDocamm) throws ComponentException{
		pre_component_invocation(userContext,componentObj);
		try {
			IDocumentoAmministrativoElettronicoBulk result = ((DocAmmFatturazioneElettronicaComponent)componentObj).castDocumentoElettronico(userContext, vDocamm);
			component_invocation_succes(userContext,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(userContext,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(userContext,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(userContext,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(userContext,componentObj,e);
		}
	}

	public IDocumentoAmministrativoElettronicoBulk aggiornaDocammElettronicoPredispostoAllaFirma(UserContext userContext, IDocumentoAmministrativoElettronicoBulk docammElettronico) throws RemoteException, ComponentException{
		pre_component_invocation(userContext,componentObj);
		try {
			IDocumentoAmministrativoElettronicoBulk result = ((DocAmmFatturazioneElettronicaComponent)componentObj).aggiornaDocammElettronicoPredispostoAllaFirma(userContext, docammElettronico);
			component_invocation_succes(userContext,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(userContext,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(userContext,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(userContext,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(userContext,componentObj,e);
		}
	}
	public IDocumentoAmministrativoElettronicoBulk aggiornaFatturaConsegnaSDI(UserContext userContext, IDocumentoAmministrativoElettronicoBulk docammElettronico, Date dataConsegnaSdi) throws RemoteException, ComponentException{
		pre_component_invocation(userContext,componentObj);
		try {
			IDocumentoAmministrativoElettronicoBulk result = ((DocAmmFatturazioneElettronicaComponent)componentObj).aggiornaFatturaConsegnaSDI(userContext, docammElettronico, dataConsegnaSdi);
			component_invocation_succes(userContext,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(userContext,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(userContext,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(userContext,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(userContext,componentObj,e);
		}
	}

	public IDocumentoAmministrativoElettronicoBulk aggiornaFatturaMancataConsegnaInvioSDI(UserContext userContext, IDocumentoAmministrativoElettronicoBulk docammElettronico, String codiceSdi, String noteInvioSdi) throws RemoteException, ComponentException{
		pre_component_invocation(userContext,componentObj);
		try {
			IDocumentoAmministrativoElettronicoBulk result = ((DocAmmFatturazioneElettronicaComponent)componentObj).aggiornaFatturaMancataConsegnaInvioSDI(userContext, docammElettronico, codiceSdi, noteInvioSdi);
			component_invocation_succes(userContext,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(userContext,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(userContext,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(userContext,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(userContext,componentObj,e);
		}
	}
}
