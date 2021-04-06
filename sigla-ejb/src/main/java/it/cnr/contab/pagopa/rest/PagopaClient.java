package it.cnr.contab.pagopa.rest;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;
import it.cnr.contab.pagopa.model.Pendenza;
import it.cnr.si.service.dto.anagrafica.letture.PersonaWebDto;
import it.cnr.si.service.dto.anagrafica.scritture.PersonaDto;

@Headers({"Content-Type: application/json"})
public interface PagopaClient {

    //    http://govpay.test.si.cnr.it/govpay
    @RequestLine("PUT /backend/api/pendenze/rs/basic/v2/pendenze/{idA2A}/{idPendenza}")
    Pendenza creaPendenza(@Param("idA2A") String application, @Param("idPendenza") Long idPendenza, Pendenza pendenza);
}