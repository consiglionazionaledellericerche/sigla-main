package it.cnr.contab.utente00.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.*;
@Stateless(name="CNRUTENZE00_EJB_TipoRuoloComponentSession")
public class TipoRuoloComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements TipoRuoloComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new it.cnr.contab.utente00.comp.TipoRuoloComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws EJBException {
	return new TipoRuoloComponentSessionBean();
}
}
