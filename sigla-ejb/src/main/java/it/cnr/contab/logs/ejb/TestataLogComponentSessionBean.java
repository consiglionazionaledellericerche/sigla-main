package it.cnr.contab.logs.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.*;

@Stateless(name="BLOGS_EJB_TestataLogComponentSession")
public class TestataLogComponentSessionBean extends it.cnr.jada.ejb.CRUDDetailComponentSessionBean implements TestataLogComponentSession{
	@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
        componentObj = new it.cnr.contab.logs.comp.TestataLogComponent();
    }
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws EJBException {
        return new TestataLogComponentSessionBean();
    }
}