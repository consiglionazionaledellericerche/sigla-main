
package it.cnr.contab.pagopa.model.pagamento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import it.cnr.contab.pagopa.model.Riscossione;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "idDominio",
    "iuv",
    "iur",
    "indice",
    "pendenza",
    "idVocePendenza",
    "rpp",
    "stato",
    "tipo",
    "importo",
    "data",
    "commissioni",
    "allegato"
})
@Generated("jsonschema2pojo")
public class Riscossioni  implements Serializable {

    @JsonProperty("idDominio")
    private String idDominio;
    @JsonProperty("iuv")
    private String iuv;
    @JsonProperty("iur")
    private String iur;
    @JsonProperty("indice")
    private Integer indice;
    @JsonProperty("pendenza")
    private String pendenza;
    @JsonProperty("idVocePendenza")
    private String idVocePendenza;
    @JsonProperty("rpp")
    private String rpp;
    @JsonProperty("stato")
    private Riscossione.StatoEnum stato;
    @JsonProperty("tipo")
    private String tipo;
    @JsonProperty("importo")
    private BigDecimal importo;
    @JsonProperty("data")
    private Date data;
    @JsonProperty("commissioni")
    private Object commissioni;
    @JsonProperty("allegato")
    private Object allegato;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("idDominio")
    public String getIdDominio() {
        return idDominio;
    }

    @JsonProperty("idDominio")
    public void setIdDominio(String idDominio) {
        this.idDominio = idDominio;
    }

    @JsonProperty("iuv")
    public String getIuv() {
        return iuv;
    }

    @JsonProperty("iuv")
    public void setIuv(String iuv) {
        this.iuv = iuv;
    }

    @JsonProperty("iur")
    public String getIur() {
        return iur;
    }

    @JsonProperty("iur")
    public void setIur(String iur) {
        this.iur = iur;
    }

    @JsonProperty("indice")
    public Integer getIndice() {
        return indice;
    }

    @JsonProperty("indice")
    public void setIndice(Integer indice) {
        this.indice = indice;
    }

    @JsonProperty("pendenza")
    public String getPendenza() {
        return pendenza;
    }

    @JsonProperty("pendenza")
    public void setPendenza(String pendenza) {
        this.pendenza = pendenza;
    }

    @JsonProperty("idVocePendenza")
    public String getIdVocePendenza() {
        return idVocePendenza;
    }

    @JsonProperty("idVocePendenza")
    public void setIdVocePendenza(String idVocePendenza) {
        this.idVocePendenza = idVocePendenza;
    }

    @JsonProperty("rpp")
    public String getRpp() {
        return rpp;
    }

    @JsonProperty("rpp")
    public void setRpp(String rpp) {
        this.rpp = rpp;
    }

    @JsonProperty("stato")
    public Riscossione.StatoEnum getStato() {
        return stato;
    }

    @JsonProperty("stato")
    public void setStato(Riscossione.StatoEnum stato) {
        this.stato = stato;
    }

    @JsonProperty("tipo")
    public String getTipo() {
        return tipo;
    }

    @JsonProperty("tipo")
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @JsonProperty("importo")
    public BigDecimal getImporto() {
        return importo;
    }

    @JsonProperty("importo")
    public void setImporto(BigDecimal importo) {
        this.importo = importo;
    }

    @JsonProperty("data")
    public Date getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(Date data) {
        this.data = data;
    }

    @JsonProperty("commissioni")
    public Object getCommissioni() {
        return commissioni;
    }

    @JsonProperty("commissioni")
    public void setCommissioni(Object commissioni) {
        this.commissioni = commissioni;
    }

    @JsonProperty("allegato")
    public Object getAllegato() {
        return allegato;
    }

    @JsonProperty("allegato")
    public void setAllegato(Object allegato) {
        this.allegato = allegato;
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
