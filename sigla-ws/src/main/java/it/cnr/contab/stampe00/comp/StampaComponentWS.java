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

package it.cnr.contab.stampe00.comp;

import it.cnr.contab.WSAttributes;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.utenze00.bp.WSUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.FatturaNonProtocollataException;
import it.cnr.jada.comp.FatturaNonTrovataException;
import it.cnr.jada.comp.GenerazioneReportException;
import it.cnr.jada.persistency.PersistencyException;

import java.rmi.RemoteException;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.soap.SOAPFaultException;

import org.jboss.ws.api.annotation.WebContext;

@Stateless
@WebService(endpointInterface = "it.cnr.contab.stampe00.comp.StampaComponentSessionWS")


public class StampaComponentWS {
	@EJB FatturaAttivaSingolaComponentSession fatturaAttivaSingolaComponentSession;
	
	@RolesAllowed({ WSAttributes.WSUSERROLE, WSAttributes.IITROLE })
	public byte[] DownloadFattura(String user, Long pg_stampa)
			throws NumberFormatException, PersistencyException,
			ComponentException, RemoteException, EJBException, Exception {
		if (user == null)
			user = "IIT";
		UserContext userContext = new WSUserContext(user, null, new Integer(
				java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),
				null, null, null);
		try {
			return fatturaAttivaSingolaComponentSession
					.lanciaStampa(userContext, pg_stampa);
		} catch (GenerazioneReportException e) {
			throw new SOAPFaultException(faultGenerazioneStampa());
		}
	}

	private SOAPFault faultChiaveFatturaNonCompleta() throws SOAPException {
		return generaFault("001",
				"Identificativo Fattura non valido e/o incompleto");
	}

	private SOAPFault faultFatturaNonTrovata() throws SOAPException {
		return generaFault("002", "Fattura non trovata");
	}

	private SOAPFault faultFatturaNonProtocollata() throws SOAPException {
		return generaFault("003", "Fattura non Protocollata");
	}

	private SOAPFault faultGenerazioneStampa() throws SOAPException {
		return generaFault("004", "Generazione stampa non riuscita");
	}
	
	@RolesAllowed({ WSAttributes.WSUSERROLE, WSAttributes.IITROLE })
	public Long inserisciDatiPerStampa(String user, String esercizio,
			String cds, String uo, String pg) throws NumberFormatException,
			PersistencyException, ComponentException, RemoteException,
			EJBException, Exception {
		if (user == null)
			user = "IIT";
		UserContext userContext = new WSUserContext(user, null, new Integer(
				java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),
				null, null, null);
		if (cds == null || uo == null || pg == null || esercizio == null)
			throw new SOAPFaultException(faultChiaveFatturaNonCompleta());
		try {
			return fatturaAttivaSingolaComponentSession
					.inserisciDatiPerStampaIva(userContext,
							new Long(esercizio), cds, uo, new Long(pg));
		} catch (FatturaNonTrovataException e) {
			throw new SOAPFaultException(faultFatturaNonTrovata());
		} catch (FatturaNonProtocollataException e) {
			throw new SOAPFaultException(faultFatturaNonProtocollata());
		}
	}

	private SOAPFault generaFault(String localName, String stringFault)
			throws SOAPException {
		MessageFactory factory = MessageFactory.newInstance();
		SOAPMessage message = factory.createMessage();
		SOAPFactory soapFactory = SOAPFactory.newInstance();
		SOAPBody body = message.getSOAPBody();
		SOAPFault fault = body.addFault();
		Name faultName = soapFactory.createName(localName, "",
				SOAPConstants.URI_NS_SOAP_ENVELOPE);
		fault.setFaultCode(faultName);
		fault.setFaultString(stringFault);
		return fault;
	}
}
