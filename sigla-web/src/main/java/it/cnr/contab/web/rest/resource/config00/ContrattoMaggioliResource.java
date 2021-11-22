package it.cnr.contab.web.rest.resource.config00;

import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.web.rest.exception.RestException;
import it.cnr.contab.web.rest.local.config00.ContrattoMaggioliLocal;
import it.cnr.contab.web.rest.model.AttachmentContratto;
import it.cnr.contab.web.rest.model.ContrattoDtoBulk;
import it.cnr.contab.web.rest.model.ContrattoMaggioliDTOBulk;
import it.cnr.contab.web.rest.model.EnumTypeAttachmentContratti;
import it.cnr.jada.DetailedRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;

import javax.validation.Valid;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

public class ContrattoMaggioliResource  extends AbstractContrattoResource implements ContrattoMaggioliLocal {
    private final Logger _log = LoggerFactory.getLogger(ContrattoMaggioliResource.class);

    @Override
    public void validateContratto(ContrattoDtoBulk contrattoBulk, CNRUserContext userContext) {
        //Check valore tipoDettaglioContratto
        if (Optional.ofNullable(contrattoBulk.getTipo_dettaglio_contratto()).isPresent()){
            if ( !contrattoBulk.getTipo_dettaglio_contratto().equals(ContrattoBulk.DETTAGLIO_CONTRATTO_ARTICOLI) &&
                    contrattoBulk.getTipo_dettaglio_contratto().equals(ContrattoBulk.DETTAGLIO_CONTRATTO_CATGRP))
                throw new RestException(Response.Status.BAD_REQUEST, String.format("Per Il Tipo Dettaglio Contratto sono previsti i seguenti valori:{ vuoto,"
                        + ContrattoBulk.DETTAGLIO_CONTRATTO_ARTICOLI)+"}");
        }
        if (CollectionUtils.isEmpty(contrattoBulk.getAttachments()))
            throw new RestException(Response.Status.BAD_REQUEST,String.format("Deve essere presente tra gli allegati il documento del contratt0"));

        contrattoBulk.getAttachments().stream().filter(a->a.getTypeAttachment().equals(EnumTypeAttachmentContratti.CONTRATTO_FLUSSO)).findFirst().
                orElseThrow(
                        () -> new DetailedRuntimeException("Il file del contratto è obbligatorio"));
    }

    @Override
    public Response insertContratto(HttpServletRequest request, @Valid ContrattoDtoBulk contrattoDtoBulk) throws Exception {
        _log.info("insertContratto->Maggioli" );
        CNRUserContext userContext = (CNRUserContext) securityContext.getUserPrincipal();
        List<AttachmentContratto> l =contrattoDtoBulk.getAttachments();
        AttachmentContratto a = Optional.ofNullable(contrattoDtoBulk.getAttachments()).filter(s->s.size()>0).get().stream().findFirst().orElse(null);
        byte[] decoded= Base64.getDecoder().decode(a.getBytes());

        try (FileOutputStream outputStream = new FileOutputStream("D://temp//decodedFileContratto.pdf")) {
            outputStream.write(decoded);
        }
        //ContrattoBulk contratto = ( ContrattoBulk) super.insertContratto(request,contrattoDtoBulk).getEntity();
        //contrattoComponentSession.salvaDefinitivo(userContext,contratto);
        return Response.status(Response.Status.CREATED).entity(contrattoDtoBulk).build();
    }

    @Override
    public Response recuperoDatiContratto(HttpServletRequest request, String uo, Integer cdTerzo) throws Exception {
        _log.info("recuperoDatiContratto->Maggioli" );
       return super.recuperoDatiContratto(request,uo,cdTerzo);
    }
}
