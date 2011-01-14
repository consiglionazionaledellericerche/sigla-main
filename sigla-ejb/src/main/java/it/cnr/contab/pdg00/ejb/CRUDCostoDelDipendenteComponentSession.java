package it.cnr.contab.pdg00.ejb;

import javax.ejb.Remote;

@Remote
public interface CRUDCostoDelDipendenteComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	it.cnr.contab.pdg00.cdip.bulk.Stipendi_cofiVirtualBulk caricaDettagliFiltrati(it.cnr.jada.UserContext param0,it.cnr.contab.pdg00.cdip.bulk.Stipendi_cofiVirtualBulk param1, it.cnr.jada.persistency.sql.CompoundFindClause param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;

}
