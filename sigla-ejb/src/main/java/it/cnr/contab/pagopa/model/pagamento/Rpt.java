
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
    "identificativoMessaggioRichiesta",
    "dataOraMessaggioRichiesta",
    "autenticazioneSoggetto",
    "soggettoVersante",
    "soggettoPagatore",
    "enteBeneficiario",
    "datiVersamento"
})
@Generated("jsonschema2pojo")
public class Rpt  implements Serializable {

    @JsonProperty("versioneOggetto")
    private String versioneOggetto;
    @JsonProperty("dominio")
    private Dominio dominio;
    @JsonProperty("identificativoMessaggioRichiesta")
    private String identificativoMessaggioRichiesta;
    @JsonProperty("dataOraMessaggioRichiesta")
    private String dataOraMessaggioRichiesta;
    @JsonProperty("autenticazioneSoggetto")
    private String autenticazioneSoggetto;
    @JsonProperty("soggettoVersante")
    private Object soggettoVersante;
    @JsonProperty("soggettoPagatore")
    private SoggettoPagatore soggettoPagatore;
    @JsonProperty("enteBeneficiario")
    private EnteBeneficiario enteBeneficiario;
    @JsonProperty("datiVersamento")
    private DatiVersamento datiVersamento;
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
    public Dominio getDominio() {
        return dominio;
    }

    @JsonProperty("dominio")
    public void setDominio(Dominio dominio) {
        this.dominio = dominio;
    }

    @JsonProperty("identificativoMessaggioRichiesta")
    public String getIdentificativoMessaggioRichiesta() {
        return identificativoMessaggioRichiesta;
    }

    @JsonProperty("identificativoMessaggioRichiesta")
    public void setIdentificativoMessaggioRichiesta(String identificativoMessaggioRichiesta) {
        this.identificativoMessaggioRichiesta = identificativoMessaggioRichiesta;
    }

    @JsonProperty("dataOraMessaggioRichiesta")
    public String getDataOraMessaggioRichiesta() {
        return dataOraMessaggioRichiesta;
    }

    @JsonProperty("dataOraMessaggioRichiesta")
    public void setDataOraMessaggioRichiesta(String dataOraMessaggioRichiesta) {
        this.dataOraMessaggioRichiesta = dataOraMessaggioRichiesta;
    }

    @JsonProperty("autenticazioneSoggetto")
    public String getAutenticazioneSoggetto() {
        return autenticazioneSoggetto;
    }

    @JsonProperty("autenticazioneSoggetto")
    public void setAutenticazioneSoggetto(String autenticazioneSoggetto) {
        this.autenticazioneSoggetto = autenticazioneSoggetto;
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
    public SoggettoPagatore getSoggettoPagatore() {
        return soggettoPagatore;
    }

    @JsonProperty("soggettoPagatore")
    public void setSoggettoPagatore(SoggettoPagatore soggettoPagatore) {
        this.soggettoPagatore = soggettoPagatore;
    }

    @JsonProperty("enteBeneficiario")
    public EnteBeneficiario getEnteBeneficiario() {
        return enteBeneficiario;
    }

    @JsonProperty("enteBeneficiario")
    public void setEnteBeneficiario(EnteBeneficiario enteBeneficiario) {
        this.enteBeneficiario = enteBeneficiario;
    }

    @JsonProperty("datiVersamento")
    public DatiVersamento getDatiVersamento() {
        return datiVersamento;
    }

    @JsonProperty("datiVersamento")
    public void setDatiVersamento(DatiVersamento datiVersamento) {
        this.datiVersamento = datiVersamento;
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
