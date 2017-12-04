package it.cnr.contab.doccont00.ejb;

import javax.ejb.Remote;

@Remote
public interface MandatoAutomaticoComponentSession extends MandatoComponentSession {
java.util.List findBancaOptions(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoAutomaticoWizardBulk param1) throws it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.MandatoAutomaticoWizardBulk listaImpegniTerzo (it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.MandatoAutomaticoWizardBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk inizializzaMappaAutomatismo(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk creaMandatoAutomatico(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
