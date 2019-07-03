package it.cnr.contab.web.rest.local.config00;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.web.rest.config.SIGLARoles;

@Local
@Path("/contratto")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed(SIGLARoles.CONTRATTO)
public interface ContrattoLocal {
    @PUT
    public Response insert(@Context HttpServletRequest request, ContrattoBulk contrattoBulk) throws Exception;

}
