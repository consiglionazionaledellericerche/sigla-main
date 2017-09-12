package it.cnr.contab.spring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by mspasiano on 6/20/17.
 */
@Service
public class StorePath {
    @Value("${store.path.comunicazioni.dal}")
    private String pathComunicazioniDal;
    @Value("${store.path.comunicazioni.al}")
    private String pathComunicazioniAl;
    @Value("${store.path.fatture.passive}")
    private String pathFatturePassive;
    @Value("${store.path.variazioni.piano.gestione}")
    private String pathVariazioniPianoDiGestione;
    @Value("${store.path.concorrenti.formazione.reddito}")
    private String pathConcorrentiFormazioneReddito;
    @Value("${store.path.non.concorrenti.formazione.reddito}")
    private String pathNonConcorrentiFormazioneReddito;
    @Value("${store.path.missioni}")
    private String pathMissioni;

    public String getPathComunicazioniDal() {
        return pathComunicazioniDal;
    }

    public void setPathComunicazioniDal(String pathComunicazioniDal) {
        this.pathComunicazioniDal = pathComunicazioniDal;
    }

    public String getPathComunicazioniAl() {
        return pathComunicazioniAl;
    }

    public void setPathComunicazioniAl(String pathComunicazioniAl) {
        this.pathComunicazioniAl = pathComunicazioniAl;
    }

    public String getPathFatturePassive() {
        return pathFatturePassive;
    }

    public void setPathFatturePassive(String pathFatturePassive) {
        this.pathFatturePassive = pathFatturePassive;
    }

    public String getPathVariazioniPianoDiGestione() {
        return pathVariazioniPianoDiGestione;
    }

    public void setPathVariazioniPianoDiGestione(String pathVariazioniPianoDiGestione) {
        this.pathVariazioniPianoDiGestione = pathVariazioniPianoDiGestione;
    }

    public String getPathConcorrentiFormazioneReddito() {
        return pathConcorrentiFormazioneReddito;
    }

    public void setPathConcorrentiFormazioneReddito(String pathConcorrentiFormazioneReddito) {
        this.pathConcorrentiFormazioneReddito = pathConcorrentiFormazioneReddito;
    }

    public String getPathNonConcorrentiFormazioneReddito() {
        return pathNonConcorrentiFormazioneReddito;
    }

    public void setPathNonConcorrentiFormazioneReddito(String pathNonConcorrentiFormazioneReddito) {
        this.pathNonConcorrentiFormazioneReddito = pathNonConcorrentiFormazioneReddito;
    }

    public String getPathMissioni() {
        return pathMissioni;
    }

    public void setPathMissioni(String pathMissioni) {
        this.pathMissioni = pathMissioni;
    }
}
