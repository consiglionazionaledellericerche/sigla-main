package it.cnr.contab.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class ACK {
    private Integer numRisultati;
    private Integer numPagine;
    private Integer risultatiPerPagina;
    private Integer pagina;
    private Date dataProduzioneDa;
    private Date dataProduzioneA;
    private List<Risultato> risultati;

    public ACK() {
    }

    public Integer getNumRisultati() {
        return numRisultati;
    }

    public void setNumRisultati(Integer numRisultati) {
        this.numRisultati = numRisultati;
    }

    public Integer getNumPagine() {
        return numPagine;
    }

    public void setNumPagine(Integer numPagine) {
        this.numPagine = numPagine;
    }

    public Integer getRisultatiPerPagina() {
        return risultatiPerPagina;
    }

    public void setRisultatiPerPagina(Integer risultatiPerPagina) {
        this.risultatiPerPagina = risultatiPerPagina;
    }

    public Integer getPagina() {
        return pagina;
    }

    public void setPagina(Integer pagina) {
        this.pagina = pagina;
    }

    public Date getDataProduzioneDa() {
        return dataProduzioneDa;
    }

    public void setDataProduzioneDa(Date dataProduzioneDa) {
        this.dataProduzioneDa = dataProduzioneDa;
    }

    public Date getDataProduzioneA() {
        return dataProduzioneA;
    }

    public void setDataProduzioneA(Date dataProduzioneA) {
        this.dataProduzioneA = dataProduzioneA;
    }

    public List<Risultato> getRisultati() {
        return risultati;
    }

    public void setRisultati(List<Risultato> risultati) {
        this.risultati = risultati;
    }

    @Override
    public String toString() {
        return "ACK{" +
                "numRisultati=" + numRisultati +
                ", numPagine=" + numPagine +
                ", risultatiPerPagina=" + risultatiPerPagina +
                ", pagina=" + pagina +
                ", dataProduzioneDa=" + dataProduzioneDa +
                ", dataProduzioneA=" + dataProduzioneA +
                ", risultati=" + risultati +
                '}';
    }
}
