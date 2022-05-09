package it.cnr.contab.pagopa.rest;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;
import it.cnr.contab.pagopa.model.MovimentoCassaPagopa;
import it.cnr.contab.pagopa.model.Pendenza;
import it.cnr.contab.pagopa.model.PendenzaResponse;
import it.cnr.si.service.dto.anagrafica.letture.PersonaWebDto;
import it.cnr.si.service.dto.anagrafica.scritture.PersonaDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

@Headers({"Content-Type: application/json"})
public interface PagopaClient {

    @RequestLine("PUT /backend/api/pendenze/rs/basic/v2/pendenze/{idA2A}/{idPendenza}?stampaAvviso={stampaAvviso}")
    PendenzaResponse creaPendenza(@Param("idA2A") String application, @Param("idPendenza") Long idPendenza, @Param("stampaAvviso") Boolean stampaAvviso, Pendenza pendenza);


    @Headers({"Accept: application/pdf"})
    @RequestLine("GET /backend/api/pendenze/rs/basic/v2/avvisi/{idDominio}/{numeroAvviso}")
    byte[] stampaAvviso(@Param("idDominio") String idDominio, @Param("numeroAvviso") String numeroAvviso);

    @Headers({"Accept: application/pdf"})
    @RequestLine("GET /backend/api/pendenze/rs/basic/v2/rpp/{idDominio}/{iuv}/{ccp}/rt?visualizzaSoggettoDebitore={visualizzaDebitore}")
    byte[] stampaRt(@Param("idDominio") String idDominio, @Param("iuv") String iuv, @Param("ccp") String ccp, @Param("visualizzaDebitore") boolean visualizzaDebitore);

    @RequestLine("POST /backend/api/ragioneria/rs/basic/v2/riconciliazioni/{idDominio}")
    MovimentoCassaPagopa riconciliaIncasso(@Param("idDominio") String dominio, MovimentoCassaPagopa movimentoCassaPagopa);
}