/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General License for more details.
 *
 *     You should have received a copy of the GNU Affero General License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.docamm00.ejb;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTestataBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTrasmissioneBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.TipoIntegrazioneSDI;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.Remote;
@Remote
public interface FatturaElettronicaPassivaComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
    
	OggettoBulk creaDocumento(UserContext usercontext, OggettoBulk oggettobulk)
            throws ComponentException, RemoteException;	
    void completaDocumento(UserContext usercontext, DocumentoEleTrasmissioneBulk documentoEleTrasmissioneBulk)
            throws ComponentException, RemoteException;
	Fattura_passivaBulk cercaFatturaPassiva(UserContext userContext, DocumentoEleTestataBulk bulk)
			throws ComponentException, RemoteException;
	Fattura_passivaBulk cercaFatturaPassivaForNota(UserContext userContext, DocumentoEleTestataBulk bulk)
			throws ComponentException, RemoteException;
	void notificaEsito(UserContext usercontext, TipoIntegrazioneSDI tipoIntegrazioneSDI, DocumentoEleTestataBulk documentoEleTestataBulk)
			throws ComponentException, RemoteException;
	void allineaEsitoCommitente(UserContext usercontext, TipoIntegrazioneSDI tipoIntegrazioneSDI)
			throws ComponentException, RemoteException;
	void allineaEsitoCommitente(UserContext usercontext, Long identificativoSdI, String statoSDI, TipoIntegrazioneSDI tipoIntegrazioneSDI)
			throws ComponentException, RemoteException;
	void allineaEsitoCommitente(UserContext usercontext, List<Long> identificativi, TipoIntegrazioneSDI tipoIntegrazioneSDI)
			throws ComponentException, RemoteException;
	boolean existsIdentificativo(UserContext usercontext, java.lang.Long identificativoSdI)
			throws ComponentException, RemoteException;
	Configurazione_cnrBulk getEmailPecSdi(UserContext userContext, boolean lock) throws it.cnr.jada.comp.ComponentException;
	List<DocumentoEleTestataBulk> recuperoDocumento(UserContext usercontext, Long identificativoSdI) 
			throws ComponentException, RemoteException;
	List<DocumentoEleTrasmissioneBulk> recuperoTrasmissione(UserContext usercontext, Long identificativoSdI) 
			throws ComponentException, EJBException;
	void aggiornaDecorrenzaTerminiSDI(UserContext userContext, List<DocumentoEleTestataBulk> listaDoc) 
			throws PersistencyException, ComponentException,java.rmi.RemoteException;
	void aggiornaConsegnaEsitoPec(UserContext userContext, List<DocumentoEleTestataBulk> listaDoc, Calendar dataRicevimentoMail) 
			throws PersistencyException, ComponentException,java.rmi.RemoteException;
	void aggiornaScartoEsitoPec(UserContext userContext, List<DocumentoEleTestataBulk> listaDoc, Calendar dataRicevimentoMail) 
			throws PersistencyException, ComponentException,java.rmi.RemoteException;
	void unlockEmailPEC(UserContext userContext) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	Boolean isPartitaIvaGruppoIva(UserContext usercontext, AnagraficoBulk anagrafico, String partitaIva, Timestamp dataDocumento) throws ComponentException,java.rmi.RemoteException;
	void aggiornaEsitoPCC(UserContext userContext, String identificativoSDI, String esitoPCC) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	void aggiornaEsitoPCC(UserContext userContext, Map<String,String> esiti) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}