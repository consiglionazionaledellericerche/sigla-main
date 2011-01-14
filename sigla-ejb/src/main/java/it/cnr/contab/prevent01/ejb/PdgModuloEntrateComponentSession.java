package it.cnr.contab.prevent01.ejb;

import javax.ejb.Remote;

@Remote
public interface PdgModuloEntrateComponentSession extends it.cnr.jada.ejb.CRUDComponentSession  {
	it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk findParametriLivelli(it.cnr.jada.UserContext param0, java.lang.Integer param1)  throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	public it.cnr.contab.config00.bulk.Parametri_cnrBulk parametriCnr(it.cnr.jada.UserContext aUC) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	java.util.Collection findNatura(it.cnr.jada.UserContext param0,it.cnr.contab.prevent01.bulk.Pdg_Modulo_EntrateBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
	boolean isUtenteEnte(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.contab.prevent01.bulk.Pdg_Modulo_EntrateBulk getPdgModuloEntrateBulk(it.cnr.jada.UserContext param0, it.cnr.contab.pdg01.consultazioni.bulk.V_cons_pdgp_pdgg_etrBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
