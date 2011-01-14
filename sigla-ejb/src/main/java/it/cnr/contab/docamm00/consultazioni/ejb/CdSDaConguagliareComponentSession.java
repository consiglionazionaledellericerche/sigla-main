package it.cnr.contab.docamm00.consultazioni.ejb;

import it.cnr.contab.docamm00.consultazioni.bulk.V_terzi_da_conguagliareBulk;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface CdSDaConguagliareComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	public List<V_terzi_da_conguagliareBulk> findTerzi(it.cnr.jada.UserContext param0, it.cnr.contab.config00.sto.bulk.CdsBulk param1) throws it.cnr.jada.comp.ComponentException;
}
