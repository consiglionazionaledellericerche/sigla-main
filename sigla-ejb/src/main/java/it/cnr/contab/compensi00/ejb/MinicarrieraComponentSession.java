package it.cnr.contab.compensi00.ejb;

import javax.ejb.Remote;

@Remote
public interface MinicarrieraComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk associaCompenso(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk param1,java.util.List param2,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk calcolaAliquotaMedia(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk cessa(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk completaPercipiente(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk param1,it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List findListaBanche(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Collection findModalita(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Collection findTermini(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Collection findTipiRapporto(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Collection findTipiTrattamento(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk generaRate(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk param1,boolean param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk rinnova(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk ripristina(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk sospendi(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
int validaPercipiente(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isTerzoCervellone(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isGestitiIncarichi(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk completaIncarico(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk param1,it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
