package it.cnr.contab.ordmag.magazzino.dto;

import java.math.BigDecimal;
import java.util.Date;

public class StampaPartitarioMovMagDTO {

    Long pgMovimento;

    String dataMovimento;

    String causaleMovimento;

    String origine;

    String descrizione;

    String dataCompetenza;

    String bolla;

    BigDecimal importo;

    BigDecimal entrata;

    BigDecimal uscita;

    Long quantita;

    BigDecimal giacenza;

    public Long getPgMovimento() {
        return pgMovimento;
    }

    public void setPgMovimento(Long pgMovimento) {
        this.pgMovimento = pgMovimento;
    }

    public String getDataMovimento() {
        return dataMovimento;
    }

    public void setDataMovimento(String dataMovimento) {
        this.dataMovimento = dataMovimento;
    }

    public String getCausaleMovimento() {
        return causaleMovimento;
    }

    public void setCausaleMovimento(String causaleMovimento) {
        this.causaleMovimento = causaleMovimento;
    }

    public String getOrigine() {
        return origine;
    }

    public void setOrigine(String origine) {
        this.origine = origine;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDataCompetenza() {
        return dataCompetenza;
    }

    public void setDataCompetenza(String dataCompetenza) {
        this.dataCompetenza = dataCompetenza;
    }

    public String getBolla() {
        return bolla;
    }

    public void setBolla(String bolla) {
        this.bolla = bolla;
    }

    public BigDecimal getImporto() {
        return importo;
    }

    public void setImporto(BigDecimal importo) {
        this.importo = importo;
    }

    public BigDecimal getEntrata() {
        return entrata;
    }

    public void setEntrata(BigDecimal entrata) {
        this.entrata = entrata;
    }

    public BigDecimal getUscita() {
        return uscita;
    }

    public void setUscita(BigDecimal uscita) {
        this.uscita = uscita;
    }
    public Long getQuantita() {
        return quantita;
    }

    public void setQuantita(Long quantita) {
        this.quantita = quantita;
    }

    public BigDecimal getGiacenza() {
        return giacenza;
    }

    public void setGiacenza(BigDecimal giacenza) {
        this.giacenza = giacenza;
    }
}
