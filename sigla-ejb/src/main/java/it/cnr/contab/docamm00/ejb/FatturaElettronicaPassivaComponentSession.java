package it.cnr.contab.docamm00.ejb;
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
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Remote;
@Remote
public interface FatturaElettronicaPassivaComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
    
	public abstract OggettoBulk creaDocumento(UserContext usercontext, OggettoBulk oggettobulk)
            throws ComponentException, RemoteException;	
    public abstract void completaDocumento(UserContext usercontext, DocumentoEleTrasmissioneBulk documentoEleTrasmissioneBulk)
            throws ComponentException, RemoteException;
	public abstract Fattura_passivaBulk cercaFatturaPassiva(UserContext userContext, DocumentoEleTestataBulk bulk)
			throws ComponentException, RemoteException;
	public abstract Fattura_passivaBulk cercaFatturaPassivaForNota(UserContext userContext, DocumentoEleTestataBulk bulk)
			throws ComponentException, RemoteException;
	public abstract void notificaEsito(UserContext usercontext, TipoIntegrazioneSDI tipoIntegrazioneSDI, DocumentoEleTestataBulk documentoEleTestataBulk)
			throws ComponentException, RemoteException;
	public abstract void allineaEsitoCommitente(UserContext usercontext, TipoIntegrazioneSDI tipoIntegrazioneSDI)
			throws ComponentException, RemoteException;
	public abstract void allineaEsitoCommitente(UserContext usercontext, Long identificativoSdI, String statoSDI, TipoIntegrazioneSDI tipoIntegrazioneSDI)
			throws ComponentException, RemoteException;
	public abstract void allineaEsitoCommitente(UserContext usercontext, List<Long> identificativi, TipoIntegrazioneSDI tipoIntegrazioneSDI)
			throws ComponentException, RemoteException;
	public abstract boolean existsIdentificativo(UserContext usercontext, java.lang.Long identificativoSdI)
			throws ComponentException, RemoteException;
	public abstract Configurazione_cnrBulk getEmailPecSdi(UserContext userContext) throws it.cnr.jada.comp.ComponentException;
	public List<DocumentoEleTestataBulk> recuperoDocumento(UserContext usercontext, Long identificativoSdI) 
			throws ComponentException, RemoteException;
	public List<DocumentoEleTrasmissioneBulk> recuperoTrasmissione(UserContext usercontext, Long identificativoSdI) 
			throws ComponentException, EJBException;
	public void aggiornaDecorrenzaTerminiSDI(UserContext userContext, List<DocumentoEleTestataBulk> listaDoc) 
			throws PersistencyException, ComponentException,java.rmi.RemoteException;
	public void aggiornaConsegnaEsitoPec(UserContext userContext, List<DocumentoEleTestataBulk> listaDoc, Calendar dataRicevimentoMail) 
			throws PersistencyException, ComponentException,java.rmi.RemoteException;
	public void aggiornaScartoEsitoPec(UserContext userContext, List<DocumentoEleTestataBulk> listaDoc, Calendar dataRicevimentoMail) 
			throws PersistencyException, ComponentException,java.rmi.RemoteException;

}