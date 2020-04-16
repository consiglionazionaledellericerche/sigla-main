package it.cnr.contab.web.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.cnr.contab.config00.contratto.bulk.ContrattoBase;

import java.util.List;

@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class ContrattoDtoBulk extends ContrattoBase {
    private Integer esercizio;
    private String cd_unita_organizzativa;
    private List<DittaInvitataExt> listaDitteInvitateExt;
    private List<UoAbilitataExt> listaUoAbilitateExt;
    private String cdCupExt;

    public String getCdCupExt() {
        return cdCupExt;
    }

    public void setCdCupExt(String cdCupExt) {
        this.cdCupExt = cdCupExt;
    }

    @Override
    public Integer getEsercizio() {
        return esercizio;
    }

    @Override
    public void setEsercizio(Integer esercizio) {
        this.esercizio = esercizio;
    }

    @Override
    public String getCd_unita_organizzativa() {
        return cd_unita_organizzativa;
    }

    @Override
    public void setCd_unita_organizzativa(String cd_unita_organizzativa) {
        this.cd_unita_organizzativa = cd_unita_organizzativa;
    }

    public List<DittaInvitataExt> getListaDitteInvitateExt() {
        return listaDitteInvitateExt;
    }

    public void setListaDitteInvitateExt(List<DittaInvitataExt> listaDitteInvitateExt) {
        this.listaDitteInvitateExt = listaDitteInvitateExt;
    }

    public List<UoAbilitataExt> getListaUoAbilitateExt() {
        return listaUoAbilitateExt;
    }

    public void setListaUoAbilitateExt(List<UoAbilitataExt> listaUoAbilitateExt) {
        this.listaUoAbilitateExt = listaUoAbilitateExt;
    }
}
