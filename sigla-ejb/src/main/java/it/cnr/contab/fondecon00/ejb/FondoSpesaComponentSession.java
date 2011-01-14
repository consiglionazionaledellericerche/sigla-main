package it.cnr.contab.fondecon00.ejb;

import javax.ejb.Remote;

@Remote
public interface FondoSpesaComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.contab.fondecon00.core.bulk.Fondo_spesaBulk setCitta(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Fondo_spesaBulk param1,it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
