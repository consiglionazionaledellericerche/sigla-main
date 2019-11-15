package it.cnr.contab.ordmag.magazzino.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.ordmag.magazzino.comp.TipoMovimentoMagComponent;
import it.cnr.jada.ejb.CRUDComponentSessionBean;
@Stateless(name="CNRORDMAG00_EJB_TipoMovimentoMagComponentSession")
public class TipoMovimentoMagComponentSessionBean extends CRUDComponentSessionBean implements TipoMovimentoMagComponentSession {

	@PostConstruct
	public void ejbCreate() {
		componentObj = new TipoMovimentoMagComponent();
	}
}
