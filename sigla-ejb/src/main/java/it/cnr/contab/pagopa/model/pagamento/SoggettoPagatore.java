
package it.cnr.contab.pagopa.model.pagamento;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "identificativoUnivocoPagatore",
    "anagraficaPagatore",
    "indirizzoPagatore",
    "civicoPagatore",
    "capPagatore",
    "localitaPagatore",
    "provinciaPagatore",
    "nazionePagatore",
    "e-mailPagatore"
})
@Generated("jsonschema2pojo")
public class SoggettoPagatore  implements Serializable {

    @JsonProperty("identificativoUnivocoPagatore")
    private IdentificativoUnivocoPagatore identificativoUnivocoPagatore;
    @JsonProperty("anagraficaPagatore")
    private String anagraficaPagatore;
    @JsonProperty("indirizzoPagatore")
    private String indirizzoPagatore;
    @JsonProperty("civicoPagatore")
    private Object civicoPagatore;
    @JsonProperty("capPagatore")
    private String capPagatore;
    @JsonProperty("localitaPagatore")
    private String localitaPagatore;
    @JsonProperty("provinciaPagatore")
    private String provinciaPagatore;
    @JsonProperty("nazionePagatore")
    private String nazionePagatore;
    @JsonProperty("e-mailPagatore")
    private Object eMailPagatore;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("identificativoUnivocoPagatore")
    public IdentificativoUnivocoPagatore getIdentificativoUnivocoPagatore() {
        return identificativoUnivocoPagatore;
    }

    @JsonProperty("identificativoUnivocoPagatore")
    public void setIdentificativoUnivocoPagatore(IdentificativoUnivocoPagatore identificativoUnivocoPagatore) {
        this.identificativoUnivocoPagatore = identificativoUnivocoPagatore;
    }

    @JsonProperty("anagraficaPagatore")
    public String getAnagraficaPagatore() {
        return anagraficaPagatore;
    }

    @JsonProperty("anagraficaPagatore")
    public void setAnagraficaPagatore(String anagraficaPagatore) {
        this.anagraficaPagatore = anagraficaPagatore;
    }

    @JsonProperty("indirizzoPagatore")
    public String getIndirizzoPagatore() {
        return indirizzoPagatore;
    }

    @JsonProperty("indirizzoPagatore")
    public void setIndirizzoPagatore(String indirizzoPagatore) {
        this.indirizzoPagatore = indirizzoPagatore;
    }

    @JsonProperty("civicoPagatore")
    public Object getCivicoPagatore() {
        return civicoPagatore;
    }

    @JsonProperty("civicoPagatore")
    public void setCivicoPagatore(Object civicoPagatore) {
        this.civicoPagatore = civicoPagatore;
    }

    @JsonProperty("capPagatore")
    public String getCapPagatore() {
        return capPagatore;
    }

    @JsonProperty("capPagatore")
    public void setCapPagatore(String capPagatore) {
        this.capPagatore = capPagatore;
    }

    @JsonProperty("localitaPagatore")
    public String getLocalitaPagatore() {
        return localitaPagatore;
    }

    @JsonProperty("localitaPagatore")
    public void setLocalitaPagatore(String localitaPagatore) {
        this.localitaPagatore = localitaPagatore;
    }

    @JsonProperty("provinciaPagatore")
    public String getProvinciaPagatore() {
        return provinciaPagatore;
    }

    @JsonProperty("provinciaPagatore")
    public void setProvinciaPagatore(String provinciaPagatore) {
        this.provinciaPagatore = provinciaPagatore;
    }

    @JsonProperty("nazionePagatore")
    public String getNazionePagatore() {
        return nazionePagatore;
    }

    @JsonProperty("nazionePagatore")
    public void setNazionePagatore(String nazionePagatore) {
        this.nazionePagatore = nazionePagatore;
    }

    @JsonProperty("e-mailPagatore")
    public Object geteMailPagatore() {
        return eMailPagatore;
    }

    @JsonProperty("e-mailPagatore")
    public void seteMailPagatore(Object eMailPagatore) {
        this.eMailPagatore = eMailPagatore;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
