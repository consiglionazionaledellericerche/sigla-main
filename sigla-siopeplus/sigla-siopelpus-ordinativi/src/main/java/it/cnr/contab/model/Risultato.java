package it.cnr.contab.model;

import java.util.Date;

public class Risultato {
    private Integer progFlusso;
    private Integer progEsitoApplicativo;
    private Date dataProduzione;
    private Date dataUpload;
    private Boolean download;
    private String location;

    public Risultato() {
    }

    public Integer getProgFlusso() {
        return progFlusso;
    }

    public void setProgFlusso(Integer progFlusso) {
        this.progFlusso = progFlusso;
    }

    public Date getDataProduzione() {
        return dataProduzione;
    }

    public void setDataProduzione(Date dataProduzione) {
        this.dataProduzione = dataProduzione;
    }

    public Boolean getDownload() {
        return download;
    }

    public void setDownload(Boolean download) {
        this.download = download;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDataUpload() {
        return dataUpload;
    }

    public void setDataUpload(Date dataUpload) {
        this.dataUpload = dataUpload;
    }

    public Integer getProgEsitoApplicativo() {
        return progEsitoApplicativo;
    }

    public void setProgEsitoApplicativo(Integer progEsitoApplicativo) {
        this.progEsitoApplicativo = progEsitoApplicativo;
    }

    @Override
    public String toString() {
        return "Risultato{" +
                "progFlusso=" + progFlusso +
                ", progEsitoApplicativo=" + progEsitoApplicativo +
                ", dataProduzione=" + dataProduzione +
                ", dataUpload=" + dataUpload +
                ", download=" + download +
                ", location='" + location + '\'' +
                '}';
    }
}
