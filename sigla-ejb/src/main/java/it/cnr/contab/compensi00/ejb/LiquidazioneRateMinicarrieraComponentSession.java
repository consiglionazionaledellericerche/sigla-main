package it.cnr.contab.compensi00.ejb;

import javax.ejb.Remote;

@Remote
public interface LiquidazioneRateMinicarrieraComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk findVoceF(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.Liquidazione_rate_minicarrieraBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void liquidaRate(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.Liquidazione_rate_minicarrieraBulk param1,java.util.List param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
