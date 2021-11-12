
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
    "tipo",
    "identificativo",
    "anagrafica",
    "indirizzo",
    "civico",
    "cap",
    "localita",
    "provincia",
    "nazione",
    "email",
    "cellulare"
})
public class SoggettoPagatore {

    @JsonProperty("tipo")
    private String tipo;
    @JsonProperty("identificativo")
    private String identificativo;
    @JsonProperty("anagrafica")
    private String anagrafica;
    @JsonProperty("indirizzo")
    private String indirizzo;
    @JsonProperty("civico")
    private String civico;
    @JsonProperty("cap")
    private Integer cap;
    @JsonProperty("localita")
    private String localita;
    @JsonProperty("provincia")
    private String provincia;
    @JsonProperty("nazione")
    private String nazione;
    @JsonProperty("email")
    private String email;
    @JsonProperty("cellulare")
    private String cellulare;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("tipo")
    public String getTipo() {
        return tipo;
    }

    @JsonProperty("tipo")
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @JsonProperty("identificativo")
    public String getIdentificativo() {
        return identificativo;
    }

    @JsonProperty("identificativo")
    public void setIdentificativo(String identificativo) {
        this.identificativo = identificativo;
    }

    @JsonProperty("anagrafica")
    public String getAnagrafica() {
        return anagrafica;
    }

    @JsonProperty("anagrafica")
    public void setAnagrafica(String anagrafica) {
        this.anagrafica = anagrafica;
    }

    @JsonProperty("indirizzo")
    public String getIndirizzo() {
        return indirizzo;
    }

    @JsonProperty("indirizzo")
    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    @JsonProperty("civico")
    public String getCivico() {
        return civico;
    }

    @JsonProperty("civico")
    public void setCivico(String civico) {
        this.civico = civico;
    }

    @JsonProperty("cap")
    public Integer getCap() {
        return cap;
    }

    @JsonProperty("cap")
    public void setCap(Integer cap) {
        this.cap = cap;
    }

    @JsonProperty("localita")
    public String getLocalita() {
        return localita;
    }

    @JsonProperty("localita")
    public void setLocalita(String localita) {
        this.localita = localita;
    }

    @JsonProperty("provincia")
    public String getProvincia() {
        return provincia;
    }

    @JsonProperty("provincia")
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    @JsonProperty("nazione")
    public String getNazione() {
        return nazione;
    }

    @JsonProperty("nazione")
    public void setNazione(String nazione) {
        this.nazione = nazione;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("cellulare")
    public String getCellulare() {
        return cellulare;
    }

    @JsonProperty("cellulare")
    public void setCellulare(String cellulare) {
        this.cellulare = cellulare;
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
