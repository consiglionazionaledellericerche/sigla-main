package it.cnr.contab.progettiric00.ejb;

import java.util.List;

import javax.ejb.Remote;

import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazioneBulk;
import it.cnr.jada.bulk.OggettoBulk;

@Remote
public interface RimodulaProgettoRicercaComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	Progetto_rimodulazioneBulk salvaDefinitivo(it.cnr.jada.UserContext param0,Progetto_rimodulazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	Progetto_rimodulazioneBulk approva(it.cnr.jada.UserContext param0,Progetto_rimodulazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	Progetto_rimodulazioneBulk respingi(it.cnr.jada.UserContext param0,Progetto_rimodulazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	Progetto_rimodulazioneBulk valida(it.cnr.jada.UserContext param0,Progetto_rimodulazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	List<OggettoBulk> constructVariazioniBilancio(it.cnr.jada.UserContext param0,Progetto_rimodulazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}