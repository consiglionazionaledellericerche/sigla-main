package it.cnr.contab.model;

import java.util.Date;
import java.util.List;

public class Lista {
    private Integer numRisultati;
    private Integer numPagine;
    private Integer risultatiPerPagina;
    private Integer pagina;
    private Date dataProduzioneDa;
    private Date dataProduzioneA;
    private Date dataUploadDa;
    private Date dataUploadA;

    private List<Risultato> risultati;

    public Lista() {
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

    public Date getDataUploadDa() {
        return dataUploadDa;
    }

    public void setDataUploadDa(Date dataUploadDa) {
        this.dataUploadDa = dataUploadDa;
    }

    public Date getDataUploadA() {
        return dataUploadA;
    }

    public void setDataUploadA(Date dataUploadA) {
        this.dataUploadA = dataUploadA;
    }

    public List<Risultato> getRisultati() {
        return risultati;
    }

    public void setRisultati(List<Risultato> risultati) {
        this.risultati = risultati;
    }

    @Override
    public String toString() {
        return "Lista{" +
                "numRisultati=" + numRisultati +
                ", numPagine=" + numPagine +
                ", risultatiPerPagina=" + risultatiPerPagina +
                ", pagina=" + pagina +
                ", dataProduzioneDa=" + dataProduzioneDa +
                ", dataProduzioneA=" + dataProduzioneA +
                ", dataUploadDa=" + dataUploadDa +
                ", dataUploadA=" + dataUploadA +
                ", risultati=" + risultati +
                '}';
    }
}
