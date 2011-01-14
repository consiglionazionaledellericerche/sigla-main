package it.cnr.contab.prevent01.ejb;

import javax.ejb.Remote;

@Remote
public interface PdgModuloCostiComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
boolean esisteBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk calcolaPrevisioneAssestataRowByRow(it.cnr.jada.UserContext param0,it.cnr.contab.prevent01.bulk.Pdg_modulo_costiBulk param1,it.cnr.contab.prevent01.bulk.Pdg_modulo_speseBulk param2,Integer param3)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk cercaPdgEsercizio(it.cnr.jada.UserContext param0,it.cnr.contab.prevent01.bulk.Pdg_moduloBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.prevent01.bulk.Pdg_modulo_speseBulk getPdgModuloSpeseBulk(it.cnr.jada.UserContext param0, it.cnr.contab.pdg01.consultazioni.bulk.V_cons_pdgp_pdgg_speBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}