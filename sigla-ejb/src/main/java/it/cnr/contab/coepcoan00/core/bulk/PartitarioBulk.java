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

package it.cnr.contab.coepcoan00.core.bulk;

import it.cnr.contab.docamm00.docs.bulk.TipoDocumentoEnum;

import java.math.BigDecimal;
import java.util.Dictionary;

public class PartitarioBulk extends Movimento_cogeBulk {

    public final static Dictionary tipoDocAmmKeys = TipoDocumentoEnum.TIPO_DOCAMM_KEYS;

    private java.math.BigDecimal im_movimento_dare;
    private java.math.BigDecimal im_movimento_avere;

    public static Dictionary getTipoDocAmmKeys() {
        return tipoDocAmmKeys;
    }

    public BigDecimal getIm_movimento_dare() {
        return im_movimento_dare;
    }

    public void setIm_movimento_dare(BigDecimal im_movimento_dare) {
        this.im_movimento_dare = im_movimento_dare;
    }

    public BigDecimal getIm_movimento_avere() {
        return im_movimento_avere;
    }

    public void setIm_movimento_avere(BigDecimal im_movimento_avere) {
        this.im_movimento_avere = im_movimento_avere;
    }
}
