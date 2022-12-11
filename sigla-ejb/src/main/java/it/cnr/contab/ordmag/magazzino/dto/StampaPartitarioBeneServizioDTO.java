package it.cnr.contab.ordmag.magazzino.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StampaPartitarioBeneServizioDTO {

    String codiceBeneServizio;

    String descrBeneServizio;

    String codiceUnitaMisura;

    String descrUnitaMisura;

    BigDecimal giacenza;

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


    public String getCodiceUnitaMisura() {
        return codiceUnitaMisura;
    }

    public void setCodiceUnitaMisura(String codiceUnitaMisura) {
        this.codiceUnitaMisura = codiceUnitaMisura;
    }

    public String getDescrUnitaMisura() {
        return descrUnitaMisura;
    }

    public void setDescrUnitaMisura(String descrUnitaMisura) {
        this.descrUnitaMisura = descrUnitaMisura;
    }

    public BigDecimal getGiacenza() {
        return giacenza;
    }

    public void setGiacenza(BigDecimal giacenza) {
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

    @Override
    public boolean equals(Object o) {
        StampaPartitarioBeneServizioDTO c = (StampaPartitarioBeneServizioDTO)o;
        return Optional.ofNullable(c.codiceBeneServizio).equals(Optional.ofNullable(codiceBeneServizio)) &&
               Optional.ofNullable(c.codiceUnitaMisura).equals(Optional.ofNullable(codiceUnitaMisura)) &&
               Optional.ofNullable(c.codiceDivisa).equals(Optional.ofNullable(codiceDivisa));
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + ((codiceBeneServizio == null) ? 0 : codiceBeneServizio.hashCode());
        result = 37 * result + ((codiceUnitaMisura == null) ? 0 : codiceUnitaMisura.hashCode());
        result = 37 * result + ((codiceDivisa == null) ? 0 : codiceDivisa.hashCode());
        return result;
    }
}
