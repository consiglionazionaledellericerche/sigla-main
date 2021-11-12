
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
    "versioneOggetto",
    "dominio",
    "identificativoMessaggioRicevuta",
    "dataOraMessaggioRicevuta",
    "riferimentoMessaggioRichiesta",
    "riferimentoDataRichiesta",
    "istitutoAttestante",
    "enteBeneficiario",
    "soggettoVersante",
    "soggettoPagatore",
    "datiPagamento"
})
@Generated("jsonschema2pojo")
public class Rt  implements Serializable {

    @JsonProperty("versioneOggetto")
    private String versioneOggetto;
    @JsonProperty("dominio")
    private Dominio__1 dominio;
    @JsonProperty("identificativoMessaggioRicevuta")
    private String identificativoMessaggioRicevuta;
    @JsonProperty("dataOraMessaggioRicevuta")
    private String dataOraMessaggioRicevuta;
    @JsonProperty("riferimentoMessaggioRichiesta")
    private String riferimentoMessaggioRichiesta;
    @JsonProperty("riferimentoDataRichiesta")
    private String riferimentoDataRichiesta;
    @JsonProperty("istitutoAttestante")
    private IstitutoAttestante istitutoAttestante;
    @JsonProperty("enteBeneficiario")
    private EnteBeneficiario__1 enteBeneficiario;
    @JsonProperty("soggettoVersante")
    private Object soggettoVersante;
    @JsonProperty("soggettoPagatore")
    private SoggettoPagatore__1 soggettoPagatore;
    @JsonProperty("datiPagamento")
    private DatiPagamento datiPagamento;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("versioneOggetto")
    public String getVersioneOggetto() {
        return versioneOggetto;
    }

    @JsonProperty("versioneOggetto")
    public void setVersioneOggetto(String versioneOggetto) {
        this.versioneOggetto = versioneOggetto;
    }

    @JsonProperty("dominio")
    public Dominio__1 getDominio() {
        return dominio;
    }

    @JsonProperty("dominio")
    public void setDominio(Dominio__1 dominio) {
        this.dominio = dominio;
    }

    @JsonProperty("identificativoMessaggioRicevuta")
    public String getIdentificativoMessaggioRicevuta() {
        return identificativoMessaggioRicevuta;
    }

    @JsonProperty("identificativoMessaggioRicevuta")
    public void setIdentificativoMessaggioRicevuta(String identificativoMessaggioRicevuta) {
        this.identificativoMessaggioRicevuta = identificativoMessaggioRicevuta;
    }

    @JsonProperty("dataOraMessaggioRicevuta")
    public String getDataOraMessaggioRicevuta() {
        return dataOraMessaggioRicevuta;
    }

    @JsonProperty("dataOraMessaggioRicevuta")
    public void setDataOraMessaggioRicevuta(String dataOraMessaggioRicevuta) {
        this.dataOraMessaggioRicevuta = dataOraMessaggioRicevuta;
    }

    @JsonProperty("riferimentoMessaggioRichiesta")
    public String getRiferimentoMessaggioRichiesta() {
        return riferimentoMessaggioRichiesta;
    }

    @JsonProperty("riferimentoMessaggioRichiesta")
    public void setRiferimentoMessaggioRichiesta(String riferimentoMessaggioRichiesta) {
        this.riferimentoMessaggioRichiesta = riferimentoMessaggioRichiesta;
    }

    @JsonProperty("riferimentoDataRichiesta")
    public String getRiferimentoDataRichiesta() {
        return riferimentoDataRichiesta;
    }

    @JsonProperty("riferimentoDataRichiesta")
    public void setRiferimentoDataRichiesta(String riferimentoDataRichiesta) {
        this.riferimentoDataRichiesta = riferimentoDataRichiesta;
    }

    @JsonProperty("istitutoAttestante")
    public IstitutoAttestante getIstitutoAttestante() {
        return istitutoAttestante;
    }

    @JsonProperty("istitutoAttestante")
    public void setIstitutoAttestante(IstitutoAttestante istitutoAttestante) {
        this.istitutoAttestante = istitutoAttestante;
    }

    @JsonProperty("enteBeneficiario")
    public EnteBeneficiario__1 getEnteBeneficiario() {
        return enteBeneficiario;
    }

    @JsonProperty("enteBeneficiario")
    public void setEnteBeneficiario(EnteBeneficiario__1 enteBeneficiario) {
        this.enteBeneficiario = enteBeneficiario;
    }

    @JsonProperty("soggettoVersante")
    public Object getSoggettoVersante() {
        return soggettoVersante;
    }

    @JsonProperty("soggettoVersante")
    public void setSoggettoVersante(Object soggettoVersante) {
        this.soggettoVersante = soggettoVersante;
    }

    @JsonProperty("soggettoPagatore")
    public SoggettoPagatore__1 getSoggettoPagatore() {
        return soggettoPagatore;
    }

    @JsonProperty("soggettoPagatore")
    public void setSoggettoPagatore(SoggettoPagatore__1 soggettoPagatore) {
        this.soggettoPagatore = soggettoPagatore;
    }

    @JsonProperty("datiPagamento")
    public DatiPagamento getDatiPagamento() {
        return datiPagamento;
    }

    @JsonProperty("datiPagamento")
    public void setDatiPagamento(DatiPagamento datiPagamento) {
        this.datiPagamento = datiPagamento;
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
