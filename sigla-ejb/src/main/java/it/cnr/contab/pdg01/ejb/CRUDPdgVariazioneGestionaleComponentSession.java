package it.cnr.contab.pdg01.ejb;

import javax.ejb.Remote;

@Remote
public interface CRUDPdgVariazioneGestionaleComponentSession extends it.cnr.contab.pdg00.ejb.PdGVariazioniComponentSession {
	void aggiungiDettaglioVariazione(it.cnr.jada.UserContext param0, it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk param1, it.cnr.contab.prevent00.bulk.V_assestatoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.jada.bulk.OggettoBulk generaVariazioneBilancio(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.jada.bulk.OggettoBulk esitaVariazioneBilancio(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;	
	it.cnr.contab.prevent00.bulk.V_assestatoBulk trovaAssestato(it.cnr.jada.UserContext param0, it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
