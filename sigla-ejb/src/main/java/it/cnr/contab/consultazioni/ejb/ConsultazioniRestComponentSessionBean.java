package it.cnr.contab.consultazioni.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

/**
 * Bean implementation class for Enterprise Bean: CNRDOCAMM00_EJB_MonitoCococoComponentSession
 */
@Stateless(name="CNRDOCAMM00_EJB_ConsultazioniRestComponentSession")
public class ConsultazioniRestComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements ConsultazioniRestComponentSession{
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.consultazioni.comp.ConsultazioniRestComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws EJBException {
		return new ConsultazioniRestComponentSessionBean();
	}
}
