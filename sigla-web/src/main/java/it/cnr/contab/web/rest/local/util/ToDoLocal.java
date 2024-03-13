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

package it.cnr.contab.web.rest.local.util;

import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Local
@Path("/todo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ToDoLocal {
    public enum ToDoBP {
        FirmaDigitalePdgVariazioniBP("PRVFIRMAVARIAZIONE"),
        FirmaDigitaleMandatiBP("DOCINTCASFIRMAMANRE"),
        FirmaDigitaleDOC1210BP("DOCINTCASFIRMAD1210"),
        CRUDFatturaPassivaElettronicaBP("AMMFATTURDOCELEPASS"),
        CRUDMissioneBP("AMMMISSIOCOREMISSIOM"),
        SelezionatoreDocAmmFatturazioneElettronica("AMMDOCFATTELETTVM"),
        SelezionatoreFattureLiquidazioneSospesaBP("AMMFATTURDOCLIQUIMAS"),
        ConsMandatiNonAcquisitiBP("CONSDOCAMMMANNONAC"),
        ConsReversaliNonAcquisitiBP("CONSDOCAMMREVNONAC");

        private final String cdAccesso;

        private ToDoBP(String cdAccesso) {
            this.cdAccesso = cdAccesso;
        }

        public String getCdAccesso() {
            return cdAccesso;
        }

        public static ToDoBP fromValue(String v) {
            for (ToDoBP c : ToDoBP.values()) {
                if (c.cdAccesso.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }
    }

    @GET
    Response all(@Context HttpServletRequest request) throws Exception;

    @GET
    @Path("/{toDoBP}")
    Response single(@Context HttpServletRequest request, @PathParam("toDoBP") ToDoBP toDoBP) throws Exception;

}
