package it.cnr.contab.web.rest.local.docamm;

import it.cnr.contab.missioni00.docs.bulk.MissioneBulk;
import it.cnr.contab.web.rest.config.SIGLARoles;
import it.cnr.contab.web.rest.model.MassimaleSpesaBulk;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Local
@Path("/missioni")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed(SIGLARoles.MISSIONI)
public interface MissioneLocal {
    @POST
    @Path(value = "/validaMassimaleSpesa")
    Response validaMassimaleSpesa(@Context HttpServletRequest request, MassimaleSpesaBulk massimaleSpesaBulk) throws Exception;

    @PUT
    public Response insert(@Context HttpServletRequest request, MissioneBulk missioneBulk) throws Exception;

}
