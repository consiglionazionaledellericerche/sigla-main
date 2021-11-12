
package it.cnr.contab.pagopa.model;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "codEntrata",
    "ibanAccredito",
    "ibanAppoggio",
    "tipoContabilita",
    "codiceContabilita",
    "tipoBollo",
    "hashDocumento",
    "provinciaResidenza"
})
public class Voci {

    @JsonProperty("Entrata")
    private Entrata entrata;
    @JsonProperty("codEntrata")
    private String codEntrata;

    public Entrata getEntrata() {
        return entrata;
    }

    public void setEntrata(Entrata entrata) {
        this.entrata = entrata;
    }

    @JsonProperty("tipoBollo")
    private String tipoBollo;
    @JsonProperty("hashDocumento")
    private String hashDocumento;
    @JsonProperty("provinciaResidenza")
    private String provinciaResidenza;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("codEntrata")
    public String getCodEntrata() {
        return codEntrata;
    }

    @JsonProperty("codEntrata")
    public void setCodEntrata(String codEntrata) {
        this.codEntrata = codEntrata;
    }

    @JsonProperty("tipoBollo")
    public String getTipoBollo() {
        return tipoBollo;
    }

    @JsonProperty("tipoBollo")
    public void setTipoBollo(String tipoBollo) {
        this.tipoBollo = tipoBollo;
    }

    @JsonProperty("hashDocumento")
    public String getHashDocumento() {
        return hashDocumento;
    }

    @JsonProperty("hashDocumento")
    public void setHashDocumento(String hashDocumento) {
        this.hashDocumento = hashDocumento;
    }

    @JsonProperty("provinciaResidenza")
    public String getProvinciaResidenza() {
        return provinciaResidenza;
    }

    @JsonProperty("provinciaResidenza")
    public void setProvinciaResidenza(String provinciaResidenza) {
        this.provinciaResidenza = provinciaResidenza;
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
