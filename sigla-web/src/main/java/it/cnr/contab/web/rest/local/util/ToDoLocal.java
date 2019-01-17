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
        DocumentiAmministrativiFatturazioneElettronicaBP("AMMDOCFATTELETTVM"),
        CRUDDistintaCassiereBP("DOCINTCASCOREDISCASF");

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
