package it.cnr.contab.pdg00.ejb;

import javax.ejb.Remote;

import it.cnr.contab.pdg00.cdip.bulk.V_cnr_estrazione_coriBulk;
import it.cnr.contab.pdg00.cdip.bulk.V_stipendi_cofi_dettBulk;
@Remote
public interface ElaboraFileStipendiComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	V_stipendi_cofi_dettBulk cercaFileStipendi(it.cnr.jada.UserContext param0,V_stipendi_cofi_dettBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	V_stipendi_cofi_dettBulk elaboraFile(it.cnr.jada.UserContext param0,V_stipendi_cofi_dettBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	V_stipendi_cofi_dettBulk annullaElaborazione(it.cnr.jada.UserContext param0,V_stipendi_cofi_dettBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;	
	it.cnr.jada.bulk.OggettoBulk cercaBatch(it.cnr.jada.UserContext param0,V_stipendi_cofi_dettBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	V_cnr_estrazione_coriBulk cercaCnrEstrazioneCori(it.cnr.jada.UserContext param0,V_cnr_estrazione_coriBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	V_cnr_estrazione_coriBulk elaboraStralcioMensile(it.cnr.jada.UserContext param0,V_cnr_estrazione_coriBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	boolean esisteStralcioNegativo(it.cnr.jada.UserContext param0,V_cnr_estrazione_coriBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
