
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
    "tipoIdentificativoUnivoco",
    "codiceIdentificativoUnivoco"
})
@Generated("jsonschema2pojo")
public class IdentificativoUnivocoPagatore  implements Serializable {

    @JsonProperty("tipoIdentificativoUnivoco")
    private String tipoIdentificativoUnivoco;
    @JsonProperty("codiceIdentificativoUnivoco")
    private String codiceIdentificativoUnivoco;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("tipoIdentificativoUnivoco")
    public String getTipoIdentificativoUnivoco() {
        return tipoIdentificativoUnivoco;
    }

    @JsonProperty("tipoIdentificativoUnivoco")
    public void setTipoIdentificativoUnivoco(String tipoIdentificativoUnivoco) {
        this.tipoIdentificativoUnivoco = tipoIdentificativoUnivoco;
    }

    @JsonProperty("codiceIdentificativoUnivoco")
    public String getCodiceIdentificativoUnivoco() {
        return codiceIdentificativoUnivoco;
    }

    @JsonProperty("codiceIdentificativoUnivoco")
    public void setCodiceIdentificativoUnivoco(String codiceIdentificativoUnivoco) {
        this.codiceIdentificativoUnivoco = codiceIdentificativoUnivoco;
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
