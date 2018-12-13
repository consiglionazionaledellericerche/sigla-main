package it.cnr.contab.web.rest.local.anagraf00;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.web.rest.config.SIGLARoles;

@Local
@Path("/terzo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed(SIGLARoles.TERZO)
public interface TerzoLocal {

	@POST
    public Response update(@Context HttpServletRequest request, TerzoBulk terzoBulk) throws Exception;

}
