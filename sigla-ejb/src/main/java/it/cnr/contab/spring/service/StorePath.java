/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.spring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by mspasiano on 6/20/17.
 */
@Service
public class StorePath {
    @Value("${cnr.storage.path.comunicazioni.dal}")
    private String pathComunicazioniDal;
    @Value("${cnr.storage.path.comunicazioni.al}")
    private String pathComunicazioniAl;
    @Value("${cnr.storage.path.fatture.passive}")
    private String pathFatturePassive;
    @Value("${cnr.storage.path.variazioni.piano.gestione}")
    private String pathVariazioniPianoDiGestione;
    @Value("${cnr.storage.path.concorrenti.formazione.reddito}")
    private String pathConcorrentiFormazioneReddito;
    @Value("${cnr.storage.path.non.concorrenti.formazione.reddito}")
    private String pathNonConcorrentiFormazioneReddito;
    @Value("${cnr.storage.path.missioni}")
    private String pathMissioni;
    @Value("${cnr.storage.path.richieste.ordini}")
    private String pathRichiesteOrdini;
    @Value("${cnr.storage.path.ordini}")
    private String pathOrdini;

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

    public String getPathRichiesteOrdini() {
        return pathRichiesteOrdini;
    }

    public void setPathRichiesteOrdini(String pathRichiesteOrdini) {
        this.pathRichiesteOrdini = pathRichiesteOrdini;
    }

    public String getPathOrdini() {
        return pathOrdini;
    }

    public void setPathOrdini(String pathOrdini) {
        this.pathOrdini = pathOrdini;
    }
}
