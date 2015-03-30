package it.cnr.contab.docamm00.ejb;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTestataBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTrasmissioneBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.TipoIntegrazioneSDI;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;

import java.rmi.RemoteException;

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
	public abstract void scanPECProtocollo(UserContext userContext)
			throws ComponentException, RemoteException;
	public abstract void notificaEsito(UserContext usercontext, TipoIntegrazioneSDI tipoIntegrazioneSDI, DocumentoEleTestataBulk documentoEleTestataBulk)
			throws ComponentException, RemoteException;

}