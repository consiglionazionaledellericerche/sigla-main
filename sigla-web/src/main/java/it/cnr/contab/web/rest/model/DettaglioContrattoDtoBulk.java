package it.cnr.contab.web.rest.model;

import java.math.BigDecimal;

public class DettaglioContrattoDtoBulk {

    private String cdBeneServizio;

    private java.math.BigDecimal quantitaMin;

    private java.math.BigDecimal quantitaMax;

    private java.math.BigDecimal prezzoUnitario;

    private String cdCategoriaGruppo;

    public String getCdBeneServizio() {
        return cdBeneServizio;
    }

    public void setCdBeneServizio(String cdBeneServizio) {
        this.cdBeneServizio = cdBeneServizio;
    }

    public BigDecimal getQuantitaMin() {
        return quantitaMin;
    }

    public void setQuantitaMin(BigDecimal quantitaMin) {
        this.quantitaMin = quantitaMin;
    }

    public BigDecimal getQuantitaMax() {
        return quantitaMax;
    }

    public void setQuantitaMax(BigDecimal quantitaMax) {
        this.quantitaMax = quantitaMax;
    }

    public BigDecimal getPrezzoUnitario() {
        return prezzoUnitario;
    }

    public void setPrezzoUnitario(BigDecimal prezzoUnitario) {
        this.prezzoUnitario = prezzoUnitario;
    }

    public String getCdCategoriaGruppo() {
        return cdCategoriaGruppo;
    }

    public void setCdCategoriaGruppo(String cdCategoriaGruppo) {
        this.cdCategoriaGruppo = cdCategoriaGruppo;
    }
}
