package it.cnr.contab.compensi00.ejb;

import javax.ejb.Remote;

@Remote
public interface TipoContributoRitenutaComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
java.util.List caricaIntervalli(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.tabrif.bulk.Tipo_contributo_ritenutaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isUltimoIntervallo(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.tabrif.bulk.Tipo_contributo_ritenutaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Collection loadClassificazioneCori(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Collection loadClassificazioneMontanti(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
