package it.cnr.contab.web.rest.model;

import java.math.BigDecimal;
import java.util.Date;

public class MassimaleSpesaBulk {
    private static final long serialVersionUID = 1L;
    private Date data;
    private Long nazione;
    private Long inquadramento;
    private String cdTipoSpesa;
    private String cdTipoPasto;
    private String divisa;
    private BigDecimal km;
    private BigDecimal importoSpesa;

    public MassimaleSpesaBulk() {
        super();
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Long getNazione() {
        return nazione;
    }

    public void setNazione(Long nazione) {
        this.nazione = nazione;
    }

    public Long getInquadramento() {
        return inquadramento;
    }

    public void setInquadramento(Long inquadramento) {
        this.inquadramento = inquadramento;
    }

    public String getCdTipoSpesa() {
        return cdTipoSpesa;
    }

    public void setCdTipoSpesa(String cdTipoSpesa) {
        this.cdTipoSpesa = cdTipoSpesa;
    }

    public String getCdTipoPasto() {
        return cdTipoPasto;
    }

    public void setCdTipoPasto(String cdTipoPasto) {
        this.cdTipoPasto = cdTipoPasto;
    }

    public String getDivisa() {
        return divisa;
    }

    public void setDivisa(String divisa) {
        this.divisa = divisa;
    }

    public BigDecimal getKm() {
        return km;
    }

    public void setKm(BigDecimal km) {
        this.km = km;
    }

    public BigDecimal getImportoSpesa() {
        return importoSpesa;
    }

    public void setImportoSpesa(BigDecimal importoSpesa) {
        this.importoSpesa = importoSpesa;
    }
}
