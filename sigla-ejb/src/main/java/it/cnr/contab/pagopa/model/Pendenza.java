
package it.cnr.contab.pagopa.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "idTipoPendenza",
    "idDominio",
    "idUnitaOperativa",
    "causale",
    "soggettoPagatore",
    "importo",
    "numeroAvviso",
    "tassonomia",
    "tassonomiaAvviso",
    "direzione",
    "divisione",
    "dataValidita",
    "dataScadenza",
    "annoRiferimento",
    "cartellaPagamento",
    "datiAllegati",
    "documento",
    "dataNotificaAvviso",
    "dataPromemoriaScadenza",
    "voci"
})
public class Pendenza {

    @JsonProperty("idTipoPendenza")
    private String idTipoPendenza;
    @JsonProperty("idDominio")
    private Integer idDominio;
    @JsonProperty("idUnitaOperativa")
    private String idUnitaOperativa;
    @JsonProperty("causale")
    private String causale;
    @JsonProperty("soggettoPagatore")
    private SoggettoPagatore soggettoPagatore;
    @JsonProperty("importo")
    private BigDecimal importo;
    @JsonProperty("numeroAvviso")
    private Long numeroAvviso;
    @JsonProperty("tassonomia")
    private String tassonomia;
    @JsonProperty("tassonomiaAvviso")
    private String tassonomiaAvviso;
    @JsonProperty("direzione")
    private String direzione;
    @JsonProperty("divisione")
    private String divisione;
    @JsonProperty("dataValidita")
    private String dataValidita;
    @JsonProperty("dataScadenza")
    private String dataScadenza;
    @JsonProperty("annoRiferimento")
    private Integer annoRiferimento;
    @JsonProperty("cartellaPagamento")
    private String cartellaPagamento;
    @JsonProperty("datiAllegati")
    private DatiAllegati datiAllegati;
    @JsonProperty("documento")
    private Documento documento;
    @JsonProperty("dataNotificaAvviso")
    private String dataNotificaAvviso;
    @JsonProperty("dataPromemoriaScadenza")
    private String dataPromemoriaScadenza;
    @JsonProperty("voci")
    private List<Voci> voci = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("idTipoPendenza")
    public String getIdTipoPendenza() {
        return idTipoPendenza;
    }

    @JsonProperty("idTipoPendenza")
    public void setIdTipoPendenza(String idTipoPendenza) {
        this.idTipoPendenza = idTipoPendenza;
    }

    @JsonProperty("idDominio")
    public Integer getIdDominio() {
        return idDominio;
    }

    @JsonProperty("idDominio")
    public void setIdDominio(Integer idDominio) {
        this.idDominio = idDominio;
    }

    @JsonProperty("idUnitaOperativa")
    public String getIdUnitaOperativa() {
        return idUnitaOperativa;
    }

    @JsonProperty("idUnitaOperativa")
    public void setIdUnitaOperativa(String idUnitaOperativa) {
        this.idUnitaOperativa = idUnitaOperativa;
    }

    @JsonProperty("causale")
    public String getCausale() {
        return causale;
    }

    @JsonProperty("causale")
    public void setCausale(String causale) {
        this.causale = causale;
    }

    @JsonProperty("soggettoPagatore")
    public SoggettoPagatore getSoggettoPagatore() {
        return soggettoPagatore;
    }

    @JsonProperty("soggettoPagatore")
    public void setSoggettoPagatore(SoggettoPagatore soggettoPagatore) {
        this.soggettoPagatore = soggettoPagatore;
    }

    @JsonProperty("importo")
    public BigDecimal getImporto() {
        return importo;
    }

    @JsonProperty("importo")
    public void setImporto(BigDecimal importo) {
        this.importo = importo;
    }

    @JsonProperty("numeroAvviso")
    public Long getNumeroAvviso() {
        return numeroAvviso;
    }

    @JsonProperty("numeroAvviso")
    public void setNumeroAvviso(Long numeroAvviso) {
        this.numeroAvviso = numeroAvviso;
    }

    @JsonProperty("tassonomia")
    public String getTassonomia() {
        return tassonomia;
    }

    @JsonProperty("tassonomia")
    public void setTassonomia(String tassonomia) {
        this.tassonomia = tassonomia;
    }

    @JsonProperty("tassonomiaAvviso")
    public String getTassonomiaAvviso() {
        return tassonomiaAvviso;
    }

    @JsonProperty("tassonomiaAvviso")
    public void setTassonomiaAvviso(String tassonomiaAvviso) {
        this.tassonomiaAvviso = tassonomiaAvviso;
    }

    @JsonProperty("direzione")
    public String getDirezione() {
        return direzione;
    }

    @JsonProperty("direzione")
    public void setDirezione(String direzione) {
        this.direzione = direzione;
    }

    @JsonProperty("divisione")
    public String getDivisione() {
        return divisione;
    }

    @JsonProperty("divisione")
    public void setDivisione(String divisione) {
        this.divisione = divisione;
    }

    @JsonProperty("dataValidita")
    public String getDataValidita() {
        return dataValidita;
    }

    @JsonProperty("dataValidita")
    public void setDataValidita(String dataValidita) {
        this.dataValidita = dataValidita;
    }

    @JsonProperty("dataScadenza")
    public String getDataScadenza() {
        return dataScadenza;
    }

    @JsonProperty("dataScadenza")
    public void setDataScadenza(String dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    @JsonProperty("annoRiferimento")
    public Integer getAnnoRiferimento() {
        return annoRiferimento;
    }

    @JsonProperty("annoRiferimento")
    public void setAnnoRiferimento(Integer annoRiferimento) {
        this.annoRiferimento = annoRiferimento;
    }

    @JsonProperty("cartellaPagamento")
    public String getCartellaPagamento() {
        return cartellaPagamento;
    }

    @JsonProperty("cartellaPagamento")
    public void setCartellaPagamento(String cartellaPagamento) {
        this.cartellaPagamento = cartellaPagamento;
    }

    @JsonProperty("datiAllegati")
    public DatiAllegati getDatiAllegati() {
        return datiAllegati;
    }

    @JsonProperty("datiAllegati")
    public void setDatiAllegati(DatiAllegati datiAllegati) {
        this.datiAllegati = datiAllegati;
    }

    @JsonProperty("documento")
    public Documento getDocumento() {
        return documento;
    }

    @JsonProperty("documento")
    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    @JsonProperty("dataNotificaAvviso")
    public String getDataNotificaAvviso() {
        return dataNotificaAvviso;
    }

    @JsonProperty("dataNotificaAvviso")
    public void setDataNotificaAvviso(String dataNotificaAvviso) {
        this.dataNotificaAvviso = dataNotificaAvviso;
    }

    @JsonProperty("dataPromemoriaScadenza")
    public String getDataPromemoriaScadenza() {
        return dataPromemoriaScadenza;
    }

    @JsonProperty("dataPromemoriaScadenza")
    public void setDataPromemoriaScadenza(String dataPromemoriaScadenza) {
        this.dataPromemoriaScadenza = dataPromemoriaScadenza;
    }

    @JsonProperty("voci")
    public List<Voci> getVoci() {
        return voci;
    }

    @JsonProperty("voci")
    public void setVoci(List<Voci> voci) {
        this.voci = voci;
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
