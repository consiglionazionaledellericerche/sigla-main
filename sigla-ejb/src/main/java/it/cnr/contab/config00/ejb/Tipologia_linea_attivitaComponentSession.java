package it.cnr.contab.config00.ejb;

import javax.ejb.Remote;

@Remote
public interface Tipologia_linea_attivitaComponentSession extends it.cnr.jada.ejb.CRUDDetailComponentSession {
void annullaModificaCdrAssociati(it.cnr.jada.UserContext param0,it.cnr.contab.config00.latt.bulk.Tipo_linea_attivitaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void associaTuttiCdr(it.cnr.jada.UserContext param0,it.cnr.contab.config00.latt.bulk.Tipo_linea_attivitaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaCdrAssociabili(it.cnr.jada.UserContext param0,it.cnr.contab.config00.latt.bulk.Tipo_linea_attivitaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void inizializzaCdrAssociatiPerModifica(it.cnr.jada.UserContext param0,it.cnr.contab.config00.latt.bulk.Tipo_linea_attivitaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void modificaCdrAssociati(it.cnr.jada.UserContext param0,it.cnr.contab.config00.latt.bulk.Tipo_linea_attivitaBulk param1,it.cnr.jada.bulk.OggettoBulk[] param2,java.util.BitSet param3,java.util.BitSet param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
