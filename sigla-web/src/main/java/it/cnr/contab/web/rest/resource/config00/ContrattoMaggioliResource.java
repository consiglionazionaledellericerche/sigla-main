package it.cnr.contab.web.rest.resource.config00;

import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.web.rest.local.config00.ContrattoMaggioliLocal;
import it.cnr.contab.web.rest.model.ContrattoMaggioliDTOBulk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

import javax.validation.Valid;
import javax.ws.rs.core.Response;

public class ContrattoMaggioliResource  implements ContrattoMaggioliLocal {
    private final Logger _log = LoggerFactory.getLogger(ContrattoMaggioliResource.class);
    @Override
    public Response insertContratto(HttpServletRequest request, @Valid ContrattoMaggioliDTOBulk contrattoMaggioliBulk) throws Exception {
        _log.info("insertContratto->Maggioli" );
        return Response.status(Response.Status.CREATED).entity(contrattoMaggioliBulk).build();
    }

    @Override
    public Response recuperoDatiContratto(HttpServletRequest request, String uo, Integer cdTerzo) throws Exception {
        _log.info("recuperoDatiContratto->Maggioli" );
        return Response.ok().entity(new ContrattoBulk()).build();
    }
}
