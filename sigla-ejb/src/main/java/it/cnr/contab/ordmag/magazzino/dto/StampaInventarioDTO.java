package it.cnr.contab.ordmag.magazzino.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class StampaInventarioDTO implements Serializable {

    String cd_magazzino;

    String cod_articolo;

    BigDecimal giacenza;

    Integer annoLotto;

    String tipoLotto;

    Integer numeroLotto;

    String categoriaGruppo;

    String descArticolo;

    String um;

    String descCatGrp;

    public String getCd_magazzino() {
        return cd_magazzino;
    }

    public void setCd_magazzino(String cd_magazzino) {
        this.cd_magazzino = cd_magazzino;
    }

    public String getCod_articolo() {
        return cod_articolo;
    }

    public void setCod_articolo(String cod_articolo) {
        this.cod_articolo = cod_articolo;
    }

    public BigDecimal getGiacenza() {
        return giacenza;
    }

    public void setGiacenza(BigDecimal giacenza) {
        this.giacenza = giacenza;
    }

    public Integer getAnnoLotto() {
        return annoLotto;
    }

    public void setAnnoLotto(Integer annoLotto) {
        this.annoLotto = annoLotto;
    }

    public String getTipoLotto() {
        return tipoLotto;
    }

    public void setTipoLotto(String tipoLotto) {
        this.tipoLotto = tipoLotto;
    }

    public Integer getNumeroLotto() {
        return numeroLotto;
    }

    public void setNumeroLotto(Integer numeroLotto) {
        this.numeroLotto = numeroLotto;
    }

    public String getCategoriaGruppo() {
        return categoriaGruppo;
    }

    public void setCategoriaGruppo(String categoriaGruppo) {
        this.categoriaGruppo = categoriaGruppo;
    }

    public String getDescArticolo() {
        return descArticolo;
    }

    public void setDescArticolo(String descArticolo) {
        this.descArticolo = descArticolo;
    }

    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }

    public String getDescCatGrp() {
        return descCatGrp;
    }

    public void setDescCatGrp(String descCatGrp) {
        this.descCatGrp = descCatGrp;
    }
}
