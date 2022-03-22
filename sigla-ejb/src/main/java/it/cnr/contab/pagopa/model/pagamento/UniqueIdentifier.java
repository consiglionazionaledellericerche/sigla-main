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
        "entityUniqueIdentifierType",
        "entityUniqueIdentifierValue"
})
@Generated("jsonschema2pojo")
public class UniqueIdentifier implements Serializable {

    @JsonProperty("entityUniqueIdentifierType")
    private String entityUniqueIdentifierType;
    @JsonProperty("entityUniqueIdentifierValue")
    private String entityUniqueIdentifierValue;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("entityUniqueIdentifierType")
    public String getEntityUniqueIdentifierType() {
        return entityUniqueIdentifierType;
    }

    @JsonProperty("entityUniqueIdentifierType")
    public void setEntityUniqueIdentifierType(String entityUniqueIdentifierType) {
        this.entityUniqueIdentifierType = entityUniqueIdentifierType;
    }

    @JsonProperty("entityUniqueIdentifierValue")
    public String getEntityUniqueIdentifierValue() {
        return entityUniqueIdentifierValue;
    }

    @JsonProperty("entityUniqueIdentifierValue")
    public void setEntityUniqueIdentifierValue(String entityUniqueIdentifierValue) {
        this.entityUniqueIdentifierValue = entityUniqueIdentifierValue;
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