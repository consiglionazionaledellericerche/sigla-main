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
    @RequestLine("GET /ace/v1/persona/{id}")
    PersonaWebDto personaById(@Param("id") int var1);


    //    http://govpay.test.si.cnr.it/govpay
    @RequestLine("POST /ace/v1/pendenza/create")
    Pendenza creaPendenza(Pendenza pendenza);

    @RequestLine("PUT /ace/v1/persona/update")
    PersonaWebDto updatePersona(PersonaDto var1);

    @RequestLine("DELETE /ace/v1/persona/delete/{id}")
    Response deletePersona(@Param("id") int var1);

}