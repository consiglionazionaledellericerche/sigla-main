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

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.docamm00.comp.FatturaElettronicaPassivaComponent;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTestataBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTrasmissioneBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.TipoIntegrazioneSDI;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.NoRollbackException;
import it.cnr.jada.persistency.PersistencyException;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
@Stateless(name="CNRDOCAMM00_EJB_FatturaElettronicaPassivaComponentSession")
public class FatturaElettronicaPassivaComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements FatturaElettronicaPassivaComponentSession {
@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.docamm00.comp.FatturaElettronicaPassivaComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new FatturaElettronicaPassivaComponentSessionBean();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public OggettoBulk creaDocumento(UserContext usercontext,
			OggettoBulk oggettobulk) throws ComponentException, EJBException {
		return super.creaConBulk(usercontext, oggettobulk);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void completaDocumento(UserContext usercontext, DocumentoEleTrasmissioneBulk documentoEleTrasmissioneBulk) throws ComponentException, EJBException{
        pre_component_invocation(usercontext, componentObj);
        try{
            ((FatturaElettronicaPassivaComponent)componentObj).completaDocumento(usercontext, documentoEleTrasmissioneBulk);
            component_invocation_succes(usercontext, componentObj);
        }catch(NoRollbackException norollbackexception){
            component_invocation_succes(usercontext, componentObj);
            throw norollbackexception;
        }catch(ComponentException componentexception){
            component_invocation_failure(usercontext, componentObj);
            throw componentexception;
        }catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(usercontext, componentObj, runtimeexception);
        }catch(Error error){
            throw uncaughtError(usercontext, componentObj, error);
        }
    }

	public Fattura_passivaBulk cercaFatturaPassiva(UserContext usercontext, DocumentoEleTestataBulk oggettobulk) throws ComponentException, EJBException{
        pre_component_invocation(usercontext, componentObj);
        try{
        	Fattura_passivaBulk oggettobulk1 = ((FatturaElettronicaPassivaComponent)componentObj).cercaFatturaPassiva(usercontext, oggettobulk);
            component_invocation_succes(usercontext, componentObj);
            return oggettobulk1;
        }catch(NoRollbackException norollbackexception){
            component_invocation_succes(usercontext, componentObj);
            throw norollbackexception;
        }catch(ComponentException componentexception){
            component_invocation_failure(usercontext, componentObj);
            throw componentexception;
        }catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(usercontext, componentObj, runtimeexception);
        }catch(Error error){
            throw uncaughtError(usercontext, componentObj, error);
        }
    }	
	public Fattura_passivaBulk cercaFatturaPassivaForNota(UserContext usercontext, DocumentoEleTestataBulk oggettobulk) throws ComponentException, EJBException{
        pre_component_invocation(usercontext, componentObj);
        try{
        	Fattura_passivaBulk oggettobulk1 = ((FatturaElettronicaPassivaComponent)componentObj).cercaFatturaPassivaForNota(usercontext, oggettobulk);
            component_invocation_succes(usercontext, componentObj);
            return oggettobulk1;
        }catch(NoRollbackException norollbackexception){
            component_invocation_succes(usercontext, componentObj);
            throw norollbackexception;
        }catch(ComponentException componentexception){
            component_invocation_failure(usercontext, componentObj);
            throw componentexception;
        }catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(usercontext, componentObj, runtimeexception);
        }catch(Error error){
            throw uncaughtError(usercontext, componentObj, error);
        }
    }
	public Configurazione_cnrBulk getEmailPecSdi(UserContext userContext, boolean lock) throws it.cnr.jada.comp.ComponentException{
        pre_component_invocation(userContext, componentObj);
        try{
        	Configurazione_cnrBulk result = ((FatturaElettronicaPassivaComponent)componentObj).getEmailPecSdi(userContext, lock);
            component_invocation_succes(userContext, componentObj);
            return result;
        }catch(NoRollbackException norollbackexception){
            component_invocation_succes(userContext, componentObj);
            throw norollbackexception;
        }catch(ComponentException componentexception){
            component_invocation_failure(userContext, componentObj);
            throw componentexception;
        }catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(userContext, componentObj, runtimeexception);
        }catch(Error error){
            throw uncaughtError(userContext, componentObj, error);
        }
	}
	public void unlockEmailPEC(UserContext userContext) throws it.cnr.jada.comp.ComponentException{
        pre_component_invocation(userContext, componentObj);
        try{
        	((FatturaElettronicaPassivaComponent)componentObj).unlockEmailPEC(userContext);
            component_invocation_succes(userContext, componentObj);
        }catch(NoRollbackException norollbackexception){
            component_invocation_succes(userContext, componentObj);
            throw norollbackexception;
        }catch(ComponentException componentexception){
            component_invocation_failure(userContext, componentObj);
            throw componentexception;
        }catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(userContext, componentObj, runtimeexception);
        }catch(Error error){
            throw uncaughtError(userContext, componentObj, error);
        }
	}
	public void notificaEsito(UserContext usercontext, TipoIntegrazioneSDI tipoIntegrazioneSDI, DocumentoEleTestataBulk documentoEleTestataBulk) throws ComponentException, EJBException{
        pre_component_invocation(usercontext, componentObj);
        try{
        	((FatturaElettronicaPassivaComponent)componentObj).notificaEsito(usercontext, tipoIntegrazioneSDI, documentoEleTestataBulk);
            component_invocation_succes(usercontext, componentObj);
        }catch(NoRollbackException norollbackexception){
            component_invocation_succes(usercontext, componentObj);
            throw norollbackexception;
        }catch(ComponentException componentexception){
            component_invocation_failure(usercontext, componentObj);
            throw componentexception;
        }catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(usercontext, componentObj, runtimeexception);
        }catch(Error error){
            throw uncaughtError(usercontext, componentObj, error);
        }
    }		
	public void allineaEsitoCommitente(UserContext usercontext, TipoIntegrazioneSDI tipoIntegrazioneSDI) throws ComponentException, EJBException{
        pre_component_invocation(usercontext, componentObj);
        try{
        	((FatturaElettronicaPassivaComponent)componentObj).allineaEsitoCommitente(usercontext, tipoIntegrazioneSDI);
            component_invocation_succes(usercontext, componentObj);
        }catch(NoRollbackException norollbackexception){
            component_invocation_succes(usercontext, componentObj);
            throw norollbackexception;
        }catch(ComponentException componentexception){
            component_invocation_failure(usercontext, componentObj);
            throw componentexception;
        }catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(usercontext, componentObj, runtimeexception);
        }catch(Error error){
            throw uncaughtError(usercontext, componentObj, error);
        }
    }		
	
	public void allineaEsitoCommitente(UserContext usercontext, Long identificativoSdI, String statoSDI, TipoIntegrazioneSDI tipoIntegrazioneSDI) throws ComponentException, EJBException{
        pre_component_invocation(usercontext, componentObj);
        try{
        	((FatturaElettronicaPassivaComponent)componentObj).allineaEsitoCommitente(usercontext, identificativoSdI, statoSDI, tipoIntegrazioneSDI);
            component_invocation_succes(usercontext, componentObj);
        }catch(NoRollbackException norollbackexception){
            component_invocation_succes(usercontext, componentObj);
            throw norollbackexception;
        }catch(ComponentException componentexception){
            component_invocation_failure(usercontext, componentObj);
            throw componentexception;
        }catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(usercontext, componentObj, runtimeexception);
        }catch(Error error){
            throw uncaughtError(usercontext, componentObj, error);
        }
    }		

	public void allineaEsitoCommitente(UserContext usercontext, List<Long> identificativi, TipoIntegrazioneSDI tipoIntegrazioneSDI) throws ComponentException, EJBException{
        pre_component_invocation(usercontext, componentObj);
        try{
        	((FatturaElettronicaPassivaComponent)componentObj).allineaEsitoCommitente(usercontext, identificativi, tipoIntegrazioneSDI);
            component_invocation_succes(usercontext, componentObj);
        }catch(NoRollbackException norollbackexception){
            component_invocation_succes(usercontext, componentObj);
            throw norollbackexception;
        }catch(ComponentException componentexception){
            component_invocation_failure(usercontext, componentObj);
            throw componentexception;
        }catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(usercontext, componentObj, runtimeexception);
        }catch(Error error){
            throw uncaughtError(usercontext, componentObj, error);
        }
    }		

	public boolean existsIdentificativo(UserContext usercontext, java.lang.Long identificativoSdI) throws ComponentException, EJBException{
        pre_component_invocation(usercontext, componentObj);
        try{
        	boolean exist = ((FatturaElettronicaPassivaComponent)componentObj).existsIdentificativo(usercontext, identificativoSdI);
            component_invocation_succes(usercontext, componentObj);
            return exist;
        }catch(NoRollbackException norollbackexception){
            component_invocation_succes(usercontext, componentObj);
            throw norollbackexception;
        }catch(ComponentException componentexception){
            component_invocation_failure(usercontext, componentObj);
            throw componentexception;
        }catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(usercontext, componentObj, runtimeexception);
        }catch(Error error){
            throw uncaughtError(usercontext, componentObj, error);
        }
    }
	
	public List<DocumentoEleTrasmissioneBulk> recuperoTrasmissione(UserContext usercontext, Long identificativoSdI) throws ComponentException, EJBException{
        pre_component_invocation(usercontext, componentObj);
        try{
        	List<DocumentoEleTrasmissioneBulk> docs = ((FatturaElettronicaPassivaComponent)componentObj).recuperoTrasmissione(usercontext, identificativoSdI);
            component_invocation_succes(usercontext, componentObj);
            return docs;
        }catch(NoRollbackException norollbackexception){
            component_invocation_succes(usercontext, componentObj);
            throw norollbackexception;
        }catch(ComponentException componentexception){
            component_invocation_failure(usercontext, componentObj);
            throw componentexception;
        }catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(usercontext, componentObj, runtimeexception);
        }catch(Error error){
            throw uncaughtError(usercontext, componentObj, error);
        }
	}

	public List<DocumentoEleTestataBulk> recuperoDocumento(UserContext usercontext, Long identificativoSdI) 
			throws ComponentException, EJBException{
        pre_component_invocation(usercontext, componentObj);
        try{
        	List<DocumentoEleTestataBulk> docs = ((FatturaElettronicaPassivaComponent)componentObj).recuperoDocumento(usercontext, identificativoSdI);
            component_invocation_succes(usercontext, componentObj);
            return docs;
        }catch(NoRollbackException norollbackexception){
            component_invocation_succes(usercontext, componentObj);
            throw norollbackexception;
        }catch(ComponentException componentexception){
            component_invocation_failure(usercontext, componentObj);
            throw componentexception;
        }catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(usercontext, componentObj, runtimeexception);
        }catch(Error error){
            throw uncaughtError(usercontext, componentObj, error);
        }
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void aggiornaDecorrenzaTerminiSDI(UserContext userContext, List<DocumentoEleTestataBulk> listaDoc) throws PersistencyException, ComponentException,java.rmi.RemoteException {
        pre_component_invocation(userContext, componentObj);
        try{
        	((FatturaElettronicaPassivaComponent)componentObj).aggiornaDecorrenzaTerminiSDI(userContext, listaDoc);
            component_invocation_succes(userContext, componentObj);
        }catch(NoRollbackException norollbackexception){
            component_invocation_succes(userContext, componentObj);
            throw norollbackexception;
        }catch(ComponentException componentexception){
            component_invocation_failure(userContext, componentObj);
            throw componentexception;
        }catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(userContext, componentObj, runtimeexception);
        }catch(Error error){
            throw uncaughtError(userContext, componentObj, error);
        }
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void aggiornaConsegnaEsitoPec(UserContext userContext, List<DocumentoEleTestataBulk> listaDoc, Calendar dataRicevimentoMail) throws PersistencyException, ComponentException,java.rmi.RemoteException {
        pre_component_invocation(userContext, componentObj);
        try{
        	((FatturaElettronicaPassivaComponent)componentObj).aggiornaConsegnaEsitoPec(userContext, listaDoc, dataRicevimentoMail);
            component_invocation_succes(userContext, componentObj);
        }catch(NoRollbackException norollbackexception){
            component_invocation_succes(userContext, componentObj);
            throw norollbackexception;
        }catch(ComponentException componentexception){
            component_invocation_failure(userContext, componentObj);
            throw componentexception;
        }catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(userContext, componentObj, runtimeexception);
        }catch(Error error){
            throw uncaughtError(userContext, componentObj, error);
        }
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void aggiornaScartoEsitoPec(UserContext userContext, List<DocumentoEleTestataBulk> listaDoc, Calendar dataRicevimentoMail) throws PersistencyException, ComponentException,java.rmi.RemoteException {
        pre_component_invocation(userContext, componentObj);
        try{
        	((FatturaElettronicaPassivaComponent)componentObj).aggiornaScartoEsitoPec(userContext, listaDoc, dataRicevimentoMail);
            component_invocation_succes(userContext, componentObj);
        }catch(NoRollbackException norollbackexception){
            component_invocation_succes(userContext, componentObj);
            throw norollbackexception;
        }catch(ComponentException componentexception){
            component_invocation_failure(userContext, componentObj);
            throw componentexception;
        }catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(userContext, componentObj, runtimeexception);
        }catch(Error error){
            throw uncaughtError(userContext, componentObj, error);
        }
	}
    public Boolean isPartitaIvaGruppoIva(UserContext usercontext, AnagraficoBulk anagrafico, String partitaIva, Timestamp dataDocumento) throws ComponentException, EJBException{
        pre_component_invocation(usercontext, componentObj);
        try{
            Boolean aBoolean = ((FatturaElettronicaPassivaComponent)componentObj).isPartitaIvaGruppoIva(usercontext, anagrafico, partitaIva, dataDocumento);
            component_invocation_succes(usercontext, componentObj);
            return aBoolean;
        }catch(NoRollbackException norollbackexception){
            component_invocation_succes(usercontext, componentObj);
            throw norollbackexception;
        }catch(ComponentException componentexception){
            component_invocation_failure(usercontext, componentObj);
            throw componentexception;
        }catch(RuntimeException runtimeexception){
            throw uncaughtRuntimeException(usercontext, componentObj, runtimeexception);
        }catch(Error error){
            throw uncaughtError(usercontext, componentObj, error);
        }
    }

}