package it.cnr.contab.prevent01.ejb;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface PdgMissioneComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	List<it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaBulk> findTipiUoAssociabili(it.cnr.jada.UserContext userContext, it.cnr.contab.prevent01.bulk.Pdg_missioneBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;
}
