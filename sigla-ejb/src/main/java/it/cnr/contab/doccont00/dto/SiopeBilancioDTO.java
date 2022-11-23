package it.cnr.contab.doccont00.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class SiopeBilancioDTO extends SiopeBilancioKeyDto implements Serializable  {
    private String descrzioneVoceBilancio;

    private BigDecimal importo;

    public SiopeBilancioDTO(SiopeBilancioKeyDto key) {
        super(key.getVoceBilancio(), key.getGestione(), key.getAnnoResiduo());
    }

    public SiopeBilancioDTO(String voceBilancio, EnumSiopeBilancioGestione gestione, Integer annoResiduo) {
        super(voceBilancio, gestione, annoResiduo);
    }

    public String getDescrzioneVoceBilancio() {
        return descrzioneVoceBilancio;
    }

    public void setDescrzioneVoceBilancio(String descrzioneVoceBilancio) {
        this.descrzioneVoceBilancio = descrzioneVoceBilancio;
    }



    public BigDecimal getImporto() {
        return importo;
    }

    public void setImporto(BigDecimal importo) {
        this.importo = importo;
    }
}
