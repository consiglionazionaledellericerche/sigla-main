/*
 * Copyright (C) 2022  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.web.rest.model;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ProvinciaBulk;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class AnagraficaInfoDTO {
    private final AnagraficoBulk anagraficoBulk;

    public AnagraficaInfoDTO(AnagraficoBulk anagraficoBulk) {
        this.anagraficoBulk = anagraficoBulk;
    }

    public String getSesso() {
        return Optional.ofNullable(anagraficoBulk)
                .map(AnagraficoBulk::getTi_sesso)
                .orElse(null);
    }

    public String getData_nascita() {
        return Optional.ofNullable(anagraficoBulk)
                .flatMap(anagraficoBulk1 -> Optional.ofNullable(anagraficoBulk1.getDt_nascita()))
                .map(timestamp -> DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()))
                .orElse(null);
    }

    public String getComune_nascita() {
        return Optional.ofNullable(anagraficoBulk)
                .flatMap(anagraficoBulk1 -> Optional.ofNullable(anagraficoBulk1.getComune_nascita()))
                .map(ComuneBulk::getDs_comune)
                .orElse(null);
    }

    public String getProvincia_nascita() {
        return Optional.ofNullable(anagraficoBulk)
                .flatMap(anagraficoBulk1 -> Optional.ofNullable(anagraficoBulk1.getComune_nascita()))
                .flatMap(comuneBulk -> Optional.ofNullable(comuneBulk.getProvincia()))
                .map(ProvinciaBulk::getCd_provincia)
                .orElse(null);
    }

    public String getNazione_nascita() {
        return Optional.ofNullable(anagraficoBulk)
                .flatMap(anagraficoBulk1 -> Optional.ofNullable(anagraficoBulk1.getComune_nascita()))
                .flatMap(comuneBulk -> Optional.ofNullable(comuneBulk.getNazione()))
                .map(NazioneBulk::getDs_nazione)
                .orElse(null);
    }

    public Boolean getFl_cittadino_italiano() {
        return Optional.ofNullable(anagraficoBulk)
                .flatMap(anagraficoBulk1 -> Optional.ofNullable(anagraficoBulk1.getComune_nascita()))
                .flatMap(comuneBulk -> Optional.ofNullable(comuneBulk.getNazione()))
                .map(NazioneBulk::getCd_iso)
                .map(s -> s.equalsIgnoreCase("IT"))
                .orElse(Boolean.FALSE);
    }

    public String getIndirizzo_residenza() {
        return Optional.ofNullable(anagraficoBulk)
                .map(AnagraficoBulk::getVia_fiscale)
                .orElse(null);
    }

    public String getCap_residenza(){
        return Optional.ofNullable(anagraficoBulk)
                .map(AnagraficoBulk::getCap_comune_fiscale)
                .orElse(null);
    }

    public String getComune_residenza() {
        return Optional.ofNullable(anagraficoBulk)
                .flatMap(anagraficoBulk1 -> Optional.ofNullable(anagraficoBulk1.getComune_fiscale()))
                .map(ComuneBulk::getDs_comune)
                .orElse(null);
    }

    public String getProvincia_residenza() {
        return Optional.ofNullable(anagraficoBulk)
                .flatMap(anagraficoBulk1 -> Optional.ofNullable(anagraficoBulk1.getComune_fiscale()))
                .flatMap(comuneBulk -> Optional.ofNullable(comuneBulk.getProvincia()))
                .map(ProvinciaBulk::getCd_provincia)
                .orElse(null);
    }

    public String getTelefono_comunicazioni(){
        return null;
    }
}
