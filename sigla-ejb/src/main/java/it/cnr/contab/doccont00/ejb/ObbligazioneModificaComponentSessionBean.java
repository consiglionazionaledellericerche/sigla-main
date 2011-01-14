/*
 * Created on Jun 23, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.doccont00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless(name="CNRDOCCONT00_EJB_ObbligazioneModificaComponentSession")
public class ObbligazioneModificaComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements ObbligazioneModificaComponentSession {
	@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
		componentObj = new it.cnr.contab.doccont00.comp.ObbligazioneModificaComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new ObbligazioneModificaComponentSessionBean();
	}
}
