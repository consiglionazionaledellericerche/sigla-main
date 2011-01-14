package it.cnr.contab.incarichi00.ejb;


import it.cnr.contab.incarichi00.bulk.V_terzi_da_completareBulk;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface CdSDaCompletareComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	public List<V_terzi_da_completareBulk> findTerzi(it.cnr.jada.UserContext param0, it.cnr.contab.config00.sto.bulk.CdsBulk param1) throws it.cnr.jada.comp.ComponentException;
}
