
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
    "identificativoDominio",
    "identificativoStazioneRichiedente"
})
@Generated("jsonschema2pojo")
public class Dominio__1  implements Serializable {

    @JsonProperty("identificativoDominio")
    private String identificativoDominio;
    @JsonProperty("identificativoStazioneRichiedente")
    private String identificativoStazioneRichiedente;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("identificativoDominio")
    public String getIdentificativoDominio() {
        return identificativoDominio;
    }

    @JsonProperty("identificativoDominio")
    public void setIdentificativoDominio(String identificativoDominio) {
        this.identificativoDominio = identificativoDominio;
    }

    @JsonProperty("identificativoStazioneRichiedente")
    public String getIdentificativoStazioneRichiedente() {
        return identificativoStazioneRichiedente;
    }

    @JsonProperty("identificativoStazioneRichiedente")
    public void setIdentificativoStazioneRichiedente(String identificativoStazioneRichiedente) {
        this.identificativoStazioneRichiedente = identificativoStazioneRichiedente;
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
