package it.cnr.contab.pdg00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.config00.comp.Classificazione_vociComponent;
import it.cnr.contab.pdg00.comp.CRUDCostoDelDipendenteComponent;
@Stateless(name="CNRPDG00_EJB_CRUDCostoDelDipendenteComponentSession")
public class CRUDCostoDelDipendenteComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements CRUDCostoDelDipendenteComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new it.cnr.contab.pdg00.comp.CRUDCostoDelDipendenteComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new CRUDCostoDelDipendenteComponentSessionBean();
}
public it.cnr.contab.pdg00.cdip.bulk.Stipendi_cofiVirtualBulk caricaDettagliFiltrati(it.cnr.jada.UserContext param0,it.cnr.contab.pdg00.cdip.bulk.Stipendi_cofiVirtualBulk param1, it.cnr.jada.persistency.sql.CompoundFindClause param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.contab.pdg00.cdip.bulk.Stipendi_cofiVirtualBulk result = ((CRUDCostoDelDipendenteComponent)componentObj).caricaDettagliFiltrati(param0, param1, param2);
		component_invocation_succes(param0,componentObj);
		return result;
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(param0,componentObj);
		throw e;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
}
