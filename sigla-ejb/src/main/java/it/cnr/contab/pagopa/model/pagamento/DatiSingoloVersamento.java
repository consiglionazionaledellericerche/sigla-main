
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
    "importoSingoloVersamento",
    "commissioneCaricoPA",
    "ibanAccredito",
    "bicAccredito",
    "ibanAppoggio",
    "bicAppoggio",
    "credenzialiPagatore",
    "causaleVersamento",
    "datiSpecificiRiscossione",
    "datiMarcaBolloDigitale"
})
@Generated("jsonschema2pojo")
public class DatiSingoloVersamento  implements Serializable {

    @JsonProperty("importoSingoloVersamento")
    private String importoSingoloVersamento;
    @JsonProperty("commissioneCaricoPA")
    private Object commissioneCaricoPA;
    @JsonProperty("ibanAccredito")
    private String ibanAccredito;
    @JsonProperty("bicAccredito")
    private Object bicAccredito;
    @JsonProperty("ibanAppoggio")
    private Object ibanAppoggio;
    @JsonProperty("bicAppoggio")
    private Object bicAppoggio;
    @JsonProperty("credenzialiPagatore")
    private Object credenzialiPagatore;
    @JsonProperty("causaleVersamento")
    private String causaleVersamento;
    @JsonProperty("datiSpecificiRiscossione")
    private String datiSpecificiRiscossione;
    @JsonProperty("datiMarcaBolloDigitale")
    private Object datiMarcaBolloDigitale;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("importoSingoloVersamento")
    public String getImportoSingoloVersamento() {
        return importoSingoloVersamento;
    }

    @JsonProperty("importoSingoloVersamento")
    public void setImportoSingoloVersamento(String importoSingoloVersamento) {
        this.importoSingoloVersamento = importoSingoloVersamento;
    }

    @JsonProperty("commissioneCaricoPA")
    public Object getCommissioneCaricoPA() {
        return commissioneCaricoPA;
    }

    @JsonProperty("commissioneCaricoPA")
    public void setCommissioneCaricoPA(Object commissioneCaricoPA) {
        this.commissioneCaricoPA = commissioneCaricoPA;
    }

    @JsonProperty("ibanAccredito")
    public String getIbanAccredito() {
        return ibanAccredito;
    }

    @JsonProperty("ibanAccredito")
    public void setIbanAccredito(String ibanAccredito) {
        this.ibanAccredito = ibanAccredito;
    }

    @JsonProperty("bicAccredito")
    public Object getBicAccredito() {
        return bicAccredito;
    }

    @JsonProperty("bicAccredito")
    public void setBicAccredito(Object bicAccredito) {
        this.bicAccredito = bicAccredito;
    }

    @JsonProperty("ibanAppoggio")
    public Object getIbanAppoggio() {
        return ibanAppoggio;
    }

    @JsonProperty("ibanAppoggio")
    public void setIbanAppoggio(Object ibanAppoggio) {
        this.ibanAppoggio = ibanAppoggio;
    }

    @JsonProperty("bicAppoggio")
    public Object getBicAppoggio() {
        return bicAppoggio;
    }

    @JsonProperty("bicAppoggio")
    public void setBicAppoggio(Object bicAppoggio) {
        this.bicAppoggio = bicAppoggio;
    }

    @JsonProperty("credenzialiPagatore")
    public Object getCredenzialiPagatore() {
        return credenzialiPagatore;
    }

    @JsonProperty("credenzialiPagatore")
    public void setCredenzialiPagatore(Object credenzialiPagatore) {
        this.credenzialiPagatore = credenzialiPagatore;
    }

    @JsonProperty("causaleVersamento")
    public String getCausaleVersamento() {
        return causaleVersamento;
    }

    @JsonProperty("causaleVersamento")
    public void setCausaleVersamento(String causaleVersamento) {
        this.causaleVersamento = causaleVersamento;
    }

    @JsonProperty("datiSpecificiRiscossione")
    public String getDatiSpecificiRiscossione() {
        return datiSpecificiRiscossione;
    }

    @JsonProperty("datiSpecificiRiscossione")
    public void setDatiSpecificiRiscossione(String datiSpecificiRiscossione) {
        this.datiSpecificiRiscossione = datiSpecificiRiscossione;
    }

    @JsonProperty("datiMarcaBolloDigitale")
    public Object getDatiMarcaBolloDigitale() {
        return datiMarcaBolloDigitale;
    }

    @JsonProperty("datiMarcaBolloDigitale")
    public void setDatiMarcaBolloDigitale(Object datiMarcaBolloDigitale) {
        this.datiMarcaBolloDigitale = datiMarcaBolloDigitale;
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
