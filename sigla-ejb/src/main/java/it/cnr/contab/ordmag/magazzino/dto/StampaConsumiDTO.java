package it.cnr.contab.ordmag.magazzino.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class StampaConsumiDTO implements Serializable {

    private String sigla_uo;
    private String desc_uo;
    private String cod_magazzino;
    private String desc_magazzino;
    private String cod_articolo;
    private String desc_articolo;
    private String cod_cat_gruppo;
    private String cod_categoria;
    private String cod_gruppo;
    private String desc_cat_gruppo;
    private String unita_misura;
    private BigDecimal importo_unitario;
    private BigDecimal giacenza;


    public String getSigla_uo() {
        return sigla_uo;
    }

    public void setSigla_uo(String sigla_uo) {
        this.sigla_uo = sigla_uo;
    }

    public String getDesc_uo() {
        return desc_uo;
    }

    public void setDesc_uo(String desc_uo) {
        this.desc_uo = desc_uo;
    }

    public String getCod_magazzino() {
        return cod_magazzino;
    }

    public void setCod_magazzino(String cod_magazzino) {
        this.cod_magazzino = cod_magazzino;
    }

    public String getDesc_magazzino() {
        return desc_magazzino;
    }

    public void setDesc_magazzino(String desc_magazzino) {
        this.desc_magazzino = desc_magazzino;
    }

    public String getCod_articolo() {
        return cod_articolo;
    }

    public void setCod_articolo(String cod_articolo) {
        this.cod_articolo = cod_articolo;
    }

    public String getDesc_articolo() {
        return desc_articolo;
    }

    public void setDesc_articolo(String desc_articolo) {
        this.desc_articolo = desc_articolo;
    }

    public String getCod_cat_gruppo() {
        return cod_cat_gruppo;
    }

    public void setCod_cat_gruppo(String cod_cat_gruppo) {
        this.cod_cat_gruppo = cod_cat_gruppo;
    }

    public String getDesc_cat_gruppo() {
        return desc_cat_gruppo;
    }

    public void setDesc_cat_gruppo(String desc_cat_gruppo) {
        this.desc_cat_gruppo = desc_cat_gruppo;
    }

    public String getUnita_misura() {
        return unita_misura;
    }

    public void setUnita_misura(String unita_misura) {
        this.unita_misura = unita_misura;
    }

    public BigDecimal getImporto_unitario() {
        return importo_unitario;
    }

    public void setImporto_unitario(BigDecimal importo_unitario) {
        this.importo_unitario = importo_unitario;
    }

    public BigDecimal getGiacenza() {
        return giacenza;
    }

    public void setGiacenza(BigDecimal giacenza) {
        this.giacenza = giacenza;
    }

    public String getCod_gruppo() {
        return cod_gruppo;
    }

    public void setCod_gruppo(String cod_gruppo) {
        this.cod_gruppo = cod_gruppo;
    }

    public String getCod_categoria() {
        return cod_categoria;
    }

    public void setCod_categoria(String cod_categoria) {
        this.cod_categoria = cod_categoria;
    }
}
