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

package it.cnr.contab.anagraf00.comp;

import it.cnr.contab.WSAttributes;
import it.cnr.contab.client.docamm.BeneServizio;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.utenze00.bp.Costanti;
import it.cnr.contab.utenze00.bp.WSUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
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

/**
 * Questa classe svolge le operazioni fondamentali di lettura, scrittura e
 * filtro dei dati immessi o richiesti dall'utente. In oltre sovrintende alla
 * gestione e creazione dati a cui l'utente stesso non ha libero accesso e/o non
 * gli sono trasparenti.
 */
@Stateless
@WebService(endpointInterface = "it.cnr.contab.anagraf00.ejb.BeneServizioComponentSessionWS")
@XmlSeeAlso({ java.util.ArrayList.class })


public class BeneServizioComponentWS {
	@EJB FatturaAttivaSingolaComponentSession fatturaAttivaSingolaComponentSession;
	@RolesAllowed({ WSAttributes.WSUSERROLE, WSAttributes.IITROLE })
	public java.util.ArrayList<BeneServizio> cercaBeneServizio(String query,
			String tipo, String dominio, Integer numMax, String user,
			String ricerca) throws Exception {
		try {
			java.util.ArrayList<BeneServizio> listaBeneServ = new ArrayList<BeneServizio>();
			List beneserv = null;
			if (user == null)
				user = "IIT";
			if (ricerca == null)
				ricerca = "selettiva";
			if (numMax == null)
				numMax = 20;
			if (tipo == null)
				tipo = "*";
			UserContext userContext = new WSUserContext(user, null,
					new Integer(java.util.Calendar.getInstance().get(
							java.util.Calendar.YEAR)), null, null, null);
			if (tipo.compareToIgnoreCase("B") != 0
					&& tipo.compareToIgnoreCase("S") != 0
					&& tipo.compareTo("*") != 0)
				throw new SOAPFaultException(faultTipoBeneServizio());

			if (query == null) {
				throw new SOAPFaultException(faultQueryNonDefinita());
			} else if (dominio == null
					|| (!dominio.equalsIgnoreCase("codice") && !dominio
							.equalsIgnoreCase("descrizione"))) {
				throw new SOAPFaultException(faultDominioNonDefinito());
			} else {
				try {
					beneserv = fatturaAttivaSingolaComponentSession
							.findListaBeneServizioWS(userContext, query, tipo,
									dominio, ricerca);
				} catch (ComponentException e) {
					throw new SOAPFaultException(faultGenerico());
				} catch (RemoteException e) {
					throw new SOAPFaultException(faultGenerico());
				}
			}

			int num = 0;
			if (beneserv != null && !beneserv.isEmpty()) {
				for (Iterator i = beneserv.iterator(); i.hasNext()
						&& num < new Integer(numMax).intValue();) {
					Bene_servizioBulk beneser = (Bene_servizioBulk) i.next();
					BeneServizio bs = new BeneServizio();
					bs.setCd_bene_servizio(beneser.getCd_bene_servizio());
					bs.setDs_bene_servizio(beneser.getDs_bene_servizio());
					listaBeneServ.add(bs);
					num++;
				}
			}
			return listaBeneServ;
		} catch (SOAPFaultException e) {
			throw e;
		} catch (Exception e) {
			throw new SOAPFaultException(faultGenerico());
		}
	}

	private SOAPFault faultGenerico() throws SOAPException {
		return generaFault(new String(Costanti.ERRORE_WS_100.toString()),
				Costanti.erroriWS.get(Costanti.ERRORE_WS_100));
	}

	private SOAPFault faultQueryNonDefinita() throws SOAPException {
		return generaFault(new String(Costanti.ERRORE_WS_101.toString()),
				Costanti.erroriWS.get(Costanti.ERRORE_WS_101));
	}

	private SOAPFault faultTipoBeneServizio() throws SOAPException {
		return generaFault(new String(Costanti.ERRORE_WS_114.toString()),
				Costanti.erroriWS.get(Costanti.ERRORE_WS_114));
	}

	private SOAPFault faultDominioNonDefinito() throws SOAPException {
		return generaFault(new String(Costanti.ERRORE_WS_102.toString()),
				Costanti.erroriWS.get(Costanti.ERRORE_WS_102));
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
