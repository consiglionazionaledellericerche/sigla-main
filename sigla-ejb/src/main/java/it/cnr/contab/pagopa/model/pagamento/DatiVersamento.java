
package it.cnr.contab.pagopa.model.pagamento;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    "dataEsecuzionePagamento",
    "importoTotaleDaVersare",
    "tipoVersamento",
    "identificativoUnivocoVersamento",
    "codiceContestoPagamento",
    "ibanAddebito",
    "bicAddebito",
    "firmaRicevuta",
    "datiSingoloVersamento"
})
@Generated("jsonschema2pojo")
public class DatiVersamento  implements Serializable {

    @JsonProperty("dataEsecuzionePagamento")
    private Date dataEsecuzionePagamento;
    @JsonProperty("importoTotaleDaVersare")
    private String importoTotaleDaVersare;
    @JsonProperty("tipoVersamento")
    private String tipoVersamento;
    @JsonProperty("identificativoUnivocoVersamento")
    private String identificativoUnivocoVersamento;
    @JsonProperty("codiceContestoPagamento")
    private String codiceContestoPagamento;
    @JsonProperty("ibanAddebito")
    private Object ibanAddebito;
    @JsonProperty("bicAddebito")
    private Object bicAddebito;
    @JsonProperty("firmaRicevuta")
    private String firmaRicevuta;
    @JsonProperty("datiSingoloVersamento")
    private List<DatiSingoloVersamento> datiSingoloVersamento = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("dataEsecuzionePagamento")
    public Date getDataEsecuzionePagamento() {
        return dataEsecuzionePagamento;
    }

    @JsonProperty("dataEsecuzionePagamento")
    public void setDataEsecuzionePagamento(Date dataEsecuzionePagamento) {
        this.dataEsecuzionePagamento = dataEsecuzionePagamento;
    }

    @JsonProperty("importoTotaleDaVersare")
    public String getImportoTotaleDaVersare() {
        return importoTotaleDaVersare;
    }

    @JsonProperty("importoTotaleDaVersare")
    public void setImportoTotaleDaVersare(String importoTotaleDaVersare) {
        this.importoTotaleDaVersare = importoTotaleDaVersare;
    }

    @JsonProperty("tipoVersamento")
    public String getTipoVersamento() {
        return tipoVersamento;
    }

    @JsonProperty("tipoVersamento")
    public void setTipoVersamento(String tipoVersamento) {
        this.tipoVersamento = tipoVersamento;
    }

    @JsonProperty("identificativoUnivocoVersamento")
    public String getIdentificativoUnivocoVersamento() {
        return identificativoUnivocoVersamento;
    }

    @JsonProperty("identificativoUnivocoVersamento")
    public void setIdentificativoUnivocoVersamento(String identificativoUnivocoVersamento) {
        this.identificativoUnivocoVersamento = identificativoUnivocoVersamento;
    }

    @JsonProperty("codiceContestoPagamento")
    public String getCodiceContestoPagamento() {
        return codiceContestoPagamento;
    }

    @JsonProperty("codiceContestoPagamento")
    public void setCodiceContestoPagamento(String codiceContestoPagamento) {
        this.codiceContestoPagamento = codiceContestoPagamento;
    }

    @JsonProperty("ibanAddebito")
    public Object getIbanAddebito() {
        return ibanAddebito;
    }

    @JsonProperty("ibanAddebito")
    public void setIbanAddebito(Object ibanAddebito) {
        this.ibanAddebito = ibanAddebito;
    }

    @JsonProperty("bicAddebito")
    public Object getBicAddebito() {
        return bicAddebito;
    }

    @JsonProperty("bicAddebito")
    public void setBicAddebito(Object bicAddebito) {
        this.bicAddebito = bicAddebito;
    }

    @JsonProperty("firmaRicevuta")
    public String getFirmaRicevuta() {
        return firmaRicevuta;
    }

    @JsonProperty("firmaRicevuta")
    public void setFirmaRicevuta(String firmaRicevuta) {
        this.firmaRicevuta = firmaRicevuta;
    }

    @JsonProperty("datiSingoloVersamento")
    public List<DatiSingoloVersamento> getDatiSingoloVersamento() {
        return datiSingoloVersamento;
    }

    @JsonProperty("datiSingoloVersamento")
    public void setDatiSingoloVersamento(List<DatiSingoloVersamento> datiSingoloVersamento) {
        this.datiSingoloVersamento = datiSingoloVersamento;
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
