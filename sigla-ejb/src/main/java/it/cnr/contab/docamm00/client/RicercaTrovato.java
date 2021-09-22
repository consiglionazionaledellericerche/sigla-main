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

package it.cnr.contab.docamm00.client;

import it.cnr.contab.docamm00.docs.bulk.TrovatoBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.jada.comp.ApplicationException;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RicercaTrovato {

    private static String targetEndpoint; //="http://siglaas4.cedrc.cnr.it:8480/";
    private static String siglaRestClientUser;
    private static String siglaRestClientPassword;

    public RicercaTrovato() throws IOException {
        super();
        loadProperties();
    }

    public static String getTargetEndpoint() {
        return targetEndpoint;
    }

    public static void setTargetEndpoint(String targetEndpoint) {
        RicercaTrovato.targetEndpoint = targetEndpoint;
    }

    public static String getSiglaRestClientUser() {
        return siglaRestClientUser;
    }

    public static void setSiglaRestClientUser(String siglaRestClientUser) {
        RicercaTrovato.siglaRestClientUser = siglaRestClientUser;
    }

    public static String getSiglaRestClientPassword() {
        return siglaRestClientPassword;
    }

    public static void setSiglaRestClientPassword(String siglaRestClientPassword) {
        RicercaTrovato.siglaRestClientPassword = siglaRestClientPassword;
    }

    private TrovatoProperties recuperoTrovatoProperties() {
        TrovatoProperties trovatoProperties = SpringUtil.getBean("trovatoProperties", TrovatoProperties.class);
        return trovatoProperties;
    }

    public TrovatoBulk ricercaDatiTrovato(it.cnr.jada.UserContext userContext, Long trovato, Boolean soloValidi) throws Exception {
        TrovatoBulk trovatoBulk = new TrovatoBulk();
        if (trovato == null) {
            throw new ApplicationException("Identificativo del trovato non indicato.");
        } else {
            trovatoBulk = cerca(trovato, soloValidi);
        }
        return trovatoBulk;
    }

    public TrovatoBulk ricercaDatiTrovato(it.cnr.jada.UserContext userContext, Long trovato) throws Exception {
        return ricercaDatiTrovato(userContext, trovato, false);
    }

    private TrovatoBulk cerca(Long pgTrovato, Boolean soloValidi) throws Exception {

        TrovatoBulk trovato = new TrovatoBulk();
        String url = "";
        url = getTargetEndpoint() + "/rest/trovato/";
        if (soloValidi) {
            url += "valido/";
        }
        url += pgTrovato;
        String username = getSiglaRestClientUser(),
                password = getSiglaRestClientPassword();

        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.register(new BasicAuthentication(username, password)).build();
        WebTarget target = client.target(url);
        Invocation.Builder request = target.request();
        Response response = request.get();
        TrovatoRest trovatoRest = response.readEntity(TrovatoRest.class);

        if (trovatoRest == null) {
            throw new ApplicationException("Identificativo del trovato indicato non esiste.");
        } else {
            valorizzaTrovato(trovato, trovatoRest);
        }

        return trovato;
    }

    private void valorizzaTrovato(TrovatoBulk trovatoBulk,
                                  TrovatoRest trovatoBean) {
        trovatoBulk.setPg_trovato(new Long(trovatoBean.getNsrif()));
        trovatoBulk.setInventore(trovatoBean.getInventore());
        trovatoBulk.setTitolo(trovatoBean.getTitolo());
    }

    public synchronized void loadProperties() throws IOException {
        TrovatoProperties trovatoProperties = recuperoTrovatoProperties();
        setTargetEndpoint(trovatoProperties.getTrovatoTargetEndpoint());
        setSiglaRestClientUser(trovatoProperties.getTrovatoSiglaRestClientUser());
        setSiglaRestClientPassword(trovatoProperties.getTrovatoSiglaRestClientPassword());
    }

    public class BasicAuthentication implements ClientRequestFilter {
        private final String authHeader;

        public BasicAuthentication(String username, String password) {
            StringBuffer buf = new StringBuffer(username);
            buf.append(':').append(password);
            this.authHeader = "Basic " + Base64.getEncoder().encodeToString(buf.toString().getBytes(StandardCharsets.UTF_8));
        }

        public void filter(ClientRequestContext requestContext) throws IOException {
            requestContext.getHeaders().putSingle("Authorization", this.authHeader);
        }
    }

}
