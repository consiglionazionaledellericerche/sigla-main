package it.cnr.contab.coepcoan00.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

@Stateless(name="CNRCOEPCOAN00_EJB_ScritturaAnaliticaComponentSession")
public class ScritturaAnaliticaComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements ScritturaAnaliticaComponentSession {
@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
	componentObj = new it.cnr.contab.coepcoan00.comp.ScritturaAnaliticaComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new ScritturaAnaliticaComponentSessionBean();
}
}
