package it.cnr.contab.doccont00.dto;

import java.io.Serializable;
import java.util.Objects;

public class SiopeBilancioKeyDto implements Serializable {
    private String voceBilancio;

    private EnumSiopeBilancioGestione gestione;

    private Integer annoResiduo;

    public SiopeBilancioKeyDto(String voceBilancio, EnumSiopeBilancioGestione gestione, Integer annoResiduo) {
        this.voceBilancio = voceBilancio;
        this.gestione = gestione;
        this.annoResiduo = annoResiduo;
    }

    public String getVoceBilancio() {
        return voceBilancio;
    }

    public void setVoceBilancio(String voceBilancio) {
        this.voceBilancio = voceBilancio;
    }

    public EnumSiopeBilancioGestione getGestione() {
        return gestione;
    }

    public void setGestione(EnumSiopeBilancioGestione gestione) {
        this.gestione = gestione;
    }

    public Integer getAnnoResiduo() {
        return annoResiduo;
    }

    public void setAnnoResiduo(Integer annoResiduo) {
        this.annoResiduo = annoResiduo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof SiopeBilancioKeyDto)) return false;
        SiopeBilancioKeyDto that = (SiopeBilancioKeyDto) o;
        return Objects.equals(voceBilancio, that.voceBilancio) && gestione == that.gestione && Objects.equals(annoResiduo, that.annoResiduo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(voceBilancio, gestione, annoResiduo);
    }
}
