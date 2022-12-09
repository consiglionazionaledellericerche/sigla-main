package it.cnr.contab.ordmag.magazzino.dto;

import java.util.ArrayList;
import java.util.List;

public class StampaPartitarioBeneServizioDTO {

    String codiceBeneServizio;

    String descrBeneServizio;

    String unitaMisura;

    Long giacenza;

    String codiceDivisa;

    List<StampaPartitarioMovMagDTO> movimenti= new ArrayList<StampaPartitarioMovMagDTO>();

    public String getCodiceBeneServizio() {
        return codiceBeneServizio;
    }

    public void setCodiceBeneServizio(String codiceBeneServizio) {
        this.codiceBeneServizio = codiceBeneServizio;
    }

    public String getDescrBeneServizio() {
        return descrBeneServizio;
    }

    public void setDescrBeneServizio(String descrBeneServizio) {
        this.descrBeneServizio = descrBeneServizio;
    }

    public String getUnitaMisura() {
        return unitaMisura;
    }

    public void setUnitaMisura(String unitaMisura) {
        this.unitaMisura = unitaMisura;
    }

    public Long getGiacenza() {
        return giacenza;
    }

    public void setGiacenza(Long giacenza) {
        this.giacenza = giacenza;
    }

    public String getCodiceDivisa() { return codiceDivisa; }

    public void setCodiceDivisa(String codiceDivisa) { this.codiceDivisa = codiceDivisa; }

    public List<StampaPartitarioMovMagDTO> getMovimenti() {
        return movimenti;
    }

    public void setMovimenti(List<StampaPartitarioMovMagDTO> movimenti) {
        this.movimenti = movimenti;
    }
}
