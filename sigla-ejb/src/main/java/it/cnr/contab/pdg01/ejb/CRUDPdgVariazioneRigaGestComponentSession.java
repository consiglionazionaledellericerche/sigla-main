package it.cnr.contab.pdg01.ejb;

import javax.ejb.Remote;

@Remote
public interface CRUDPdgVariazioneRigaGestComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	it.cnr.contab.progettiric00.core.bulk.ProgettoBulk getProgettoLineaAttivita(it.cnr.jada.UserContext usercontext, it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestBulk dett) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
