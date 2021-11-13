package it.cnr.contab.ordmag.magazzino.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class StampaInventarioDTO implements Serializable {

    String cd_magazzino;

    String desc_magazzino;

    String cod_articolo;

    String descArticolo;

    String categoriaGruppo;

    String descCatGrp;

    String cod_categoria;

    String cod_gruppo;

    BigDecimal giacenza;

    Integer annoLotto;

    String tipoLotto;

    Integer numeroLotto;





    String um;



    BigDecimal importoUnitario;

    String cdCds;

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

    public BigDecimal getImportoUnitario() {
        return importoUnitario;
    }

    public void setImportoUnitario(BigDecimal importo) {
        this.importoUnitario = importo;
    }

    public String getCdCds() {
        return cdCds;
    }

    public void setCdCds(String cdCds) {
        this.cdCds = cdCds;
    }

    public String getDesc_magazzino() {
        return desc_magazzino;
    }

    public void setDesc_magazzino(String desc_magazzino) {
        this.desc_magazzino = desc_magazzino;
    }

    public String getCod_categoria() {
        return cod_categoria;
    }

    public void setCod_categoria(String cod_categoria) {
        this.cod_categoria = cod_categoria;
    }

    public String getCod_gruppo() {
        return cod_gruppo;
    }

    public void setCod_gruppo(String cod_gruppo) {
        this.cod_gruppo = cod_gruppo;
    }
}
