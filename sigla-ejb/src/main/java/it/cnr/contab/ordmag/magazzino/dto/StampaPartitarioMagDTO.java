package it.cnr.contab.ordmag.magazzino.dto;

import java.util.List;

public class StampaPartitarioMagDTO {

    List<StampaPartitarioBeneServizioDTO> beniServizio;

    public List<StampaPartitarioBeneServizioDTO> getBeniServizio() {
        return beniServizio;
    }

    public void setBeniServizio(List<StampaPartitarioBeneServizioDTO> beniServizio) {
        this.beniServizio = beniServizio;
    }
}
