/*
 * Created on Jan 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.consultazioni.bp;

import it.cnr.contab.consultazioni.ejb.ConsultazioniRestComponentSession;
import it.cnr.jada.util.action.ConsultazioniBP;

/**
 * @author Matilde D'Urso
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsultazioniRestBP extends ConsultazioniBP {

	public ConsultazioniRestBP(String s) {
		super(s);
	}

	public ConsultazioniRestBP() {
		super();
	}

	public ConsultazioniRestComponentSession createComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		return (ConsultazioniRestComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_ConsultazioniRestComponentSession", ConsultazioniRestComponentSession.class);
	}
}
