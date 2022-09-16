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

package it.cnr.contab.coepcoan00.filter.bulk;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.jada.bulk.OggettoBulk;

import java.util.Optional;

public class FiltroRicercaTerzoBulk extends OggettoBulk {
    private TerzoBulk terzo;

    private Boolean dettaglioTributi;

    public Boolean getDettaglioTributi() {
        return dettaglioTributi;
    }

    public void setDettaglioTributi(Boolean dettaglioTributi) {
        this.dettaglioTributi = dettaglioTributi;
    }

    public TerzoBulk getTerzo() {
        return terzo;
    }

    public void setTerzo(TerzoBulk terzo) {
        this.terzo = terzo;
    }


    public boolean isROTerzo() {
        return Optional.ofNullable(terzo)
                .filter(terzoBulk -> terzoBulk.getCrudStatus() == OggettoBulk.NORMAL)
                .isPresent();
    }
}
