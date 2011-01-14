package it.cnr.contab.docamm00.ejb;

import javax.ejb.Remote;

@Remote
public interface VoceIvaComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	public abstract it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk caricaVoceIvaDefault(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	public abstract boolean validaVoceIva(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk param1) throws java.rmi.RemoteException;
	java.util.List findListaVoceIVAWS(it.cnr.jada.UserContext userContext,String query,String dominio,String tipoRicerca)throws  it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
