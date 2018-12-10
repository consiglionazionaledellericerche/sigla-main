package it.cnr.contab.model;

import java.time.LocalDate;
import java.util.Date;

public class Risultato {
    private Integer progFlusso;
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

    @Override
    public String toString() {
        return "Risultato{" +
                "progFlusso=" + progFlusso +
                ", dataProduzione=" + dataProduzione +
                ", dataUpload=" + dataUpload +
                ", download=" + download +
                ", location='" + location + '\'' +
                '}';
    }
}
