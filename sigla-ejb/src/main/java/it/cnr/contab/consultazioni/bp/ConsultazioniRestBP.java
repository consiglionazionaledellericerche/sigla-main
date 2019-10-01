/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
