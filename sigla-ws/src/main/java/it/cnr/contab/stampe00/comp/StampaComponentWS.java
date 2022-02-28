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

import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.utente00.nav.ejb.GestioneLoginComponentSession;
import it.cnr.contab.utenze00.bp.WSUserContext;
import it.cnr.contab.util.enumeration.AccessoEnum;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.FatturaNonProtocollataException;
import it.cnr.jada.comp.FatturaNonTrovataException;
import it.cnr.jada.comp.GenerazioneReportException;
import it.cnr.jada.persistency.PersistencyException;
import org.keycloak.KeycloakPrincipal;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.soap.*;
import javax.xml.ws.soap.SOAPFaultException;
import java.rmi.RemoteException;
import java.util.Optional;

@Stateless
@WebService(endpointInterface = "it.cnr.contab.stampe00.comp.StampaComponentSessionWS")
public class StampaComponentWS {
    @EJB
    FatturaAttivaSingolaComponentSession fatturaAttivaSingolaComponentSession;
    @EJB
    GestioneLoginComponentSession gestioneLoginComponentSession;
    @Resource
    private EJBContext ejbContext;

    public byte[] DownloadFattura(String user, Long pg_stampa)
            throws
			Exception {
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

    public Long inserisciDatiPerStampa(String user, String esercizio,
                                       String cds, String uo, String pg) throws
			Exception {
        UserContext userContext = new WSUserContext(user, null, new Integer(
                java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),
                null, null, null);
        if (cds == null || uo == null || pg == null || esercizio == null)
            throw new SOAPFaultException(faultChiaveFatturaNonCompleta());
        try {
            if (!gestioneLoginComponentSession.isUserAccessoAllowed(
                    Optional.ofNullable(ejbContext.getCallerPrincipal())
                            .filter(KeycloakPrincipal.class::isInstance)
                            .map(KeycloakPrincipal.class::cast)
                            .orElse(null),
                    Integer.valueOf(esercizio),
                    cds,
                    uo,
                    AccessoEnum.AMMFATTURDOCSFATATTV.name()
            )) {
                throw new SOAPFaultException(faultAccesononConsentito());
            }
            return fatturaAttivaSingolaComponentSession
                    .inserisciDatiPerStampaIva(userContext,
                            new Long(esercizio), cds, uo, new Long(pg));
        } catch (FatturaNonTrovataException e) {
            throw new SOAPFaultException(faultFatturaNonTrovata());
        } catch (FatturaNonProtocollataException e) {
            throw new SOAPFaultException(faultFatturaNonProtocollata());
        }
    }

    private SOAPFault faultAccesononConsentito() throws SOAPException {
        return generaFault("000", "Accesso non consentito!");
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
