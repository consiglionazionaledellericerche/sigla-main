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
import it.cnr.contab.util.enumeration.TipoIVA;
import it.cnr.jada.util.OrderedHashtable;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Dictionary;
import java.util.Optional;

public class PartitarioBulk extends Movimento_cogeBulk {

    public final static Dictionary tipoDocAmmKeys = TipoDocumentoEnum.TIPO_DOCAMM_KEYS;
    public final static Dictionary
            tipoRigaKeys = new OrderedHashtable(),
            tipoKeys = TipoIVA.TipoIVAKeys;

    static {
        for (TipoRiga tipoRiga : TipoRiga.values()) {
            tipoRigaKeys.put(tipoRiga.value(), tipoRiga.label());
            tipoRigaKeys.put("SALDO", "SALDO");
        }
    }

    private String cd_riga;
    private java.math.BigDecimal im_movimento_dare;
    private java.math.BigDecimal im_movimento_avere;

    private java.math.BigDecimal differenza;

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

    public BigDecimal getDifferenza() {
        return differenza;
    }

    public void setDifferenza(BigDecimal differenza) {
        this.differenza = differenza;
    }

    public String getCd_riga() {
        return cd_riga;
    }

    public void setCd_riga(String cd_riga) {
        this.cd_riga = cd_riga;
    }

    public boolean equalsByPrimaryKey(Object o) {
        if (this == o) return true;
        if (!(o instanceof PartitarioBulk)) return false;
        PartitarioBulk k = (PartitarioBulk)o;
        if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
        if(!compareKey(getCd_unita_organizzativa(),k.getCd_unita_organizzativa())) return false;
        if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
        if(!compareKey(getPg_movimento(),k.getPg_movimento())) return false;
        if(!compareKey(getPg_scrittura(),k.getPg_scrittura())) return false;
        if(!compareKey(getCd_riga(),k.getCd_riga())) return false;
        return true;
    }

    public int primaryKeyHashCode() {
        return
                calculateKeyHashCode(getCd_cds())+
                        calculateKeyHashCode(getCd_unita_organizzativa())+
                        calculateKeyHashCode(getEsercizio())+
                        calculateKeyHashCode(getPg_movimento())+
                        calculateKeyHashCode(getPg_scrittura())+
                        calculateKeyHashCode(getCd_riga());
    }

    public enum TipologiaRiga {
        D,T;
        public static boolean isDettaglio(String s) {
            return TipologiaRiga.valueOf(s).equals(D);
        }
    }

    public Long getPgScritturaColumnValue() {
        return Optional.ofNullable(getCd_riga())
                .filter(s -> TipologiaRiga.isDettaglio(s))
                .map(s -> super.getPg_scrittura())
                .orElse(null);
    }
    public Timestamp getDtContabilizzazioneColumnValue() {
        return Optional.ofNullable(getCd_riga())
                .filter(s -> TipologiaRiga.isDettaglio(s))
                .map(s -> super.getScrittura().getDt_contabilizzazione())
                .orElse(null);
    }
    public String getDsScritturaColumnValue() {
        return Optional.ofNullable(getCd_riga())
                .filter(s -> TipologiaRiga.isDettaglio(s))
                .map(s -> super.getScrittura().getDs_scrittura())
                .orElse(null);
    }
    public String getCdVoceEpColumnValue() {
        return Optional.ofNullable(getCd_riga())
                .filter(s -> TipologiaRiga.isDettaglio(s))
                .map(s -> super.getCd_voce_ep())
                .orElse(null);
    }

    public String getDsVoceEpColumnValue() {
        return Optional.ofNullable(getCd_riga())
                .filter(s -> TipologiaRiga.isDettaglio(s))
                .map(s -> super.getConto().getDs_voce_ep())
                .orElse(null);
    }
}
