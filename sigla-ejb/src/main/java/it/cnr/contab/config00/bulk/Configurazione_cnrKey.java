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

package it.cnr.contab.config00.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Configurazione_cnrKey extends OggettoBulk implements KeyedPersistent {
    // ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
    private java.lang.Integer esercizio;

    // CD_CHIAVE_SECONDARIA VARCHAR(100) NOT NULL (PK)
    private java.lang.String cd_chiave_secondaria;

    // CD_UNITA_FUNZIONALE VARCHAR(30) NOT NULL (PK)
    private java.lang.String cd_unita_funzionale;

    // CD_CHIAVE_PRIMARIA VARCHAR(50) NOT NULL (PK)
    private java.lang.String cd_chiave_primaria;

    public Configurazione_cnrKey() {
        super();
    }

    public Configurazione_cnrKey(java.lang.String cd_chiave_primaria, java.lang.String cd_chiave_secondaria, java.lang.String cd_unita_funzionale, java.lang.Integer esercizio) {
        super();
        this.cd_chiave_primaria = cd_chiave_primaria;
        this.cd_chiave_secondaria = cd_chiave_secondaria;
        this.cd_unita_funzionale = cd_unita_funzionale;
        this.esercizio = esercizio;
    }

    public boolean equalsByPrimaryKey(Object o) {
        if (this == o) return true;
        if (!(o instanceof Configurazione_cnrKey)) return false;
        Configurazione_cnrKey k = (Configurazione_cnrKey) o;
        if (!compareKey(getCd_chiave_primaria(), k.getCd_chiave_primaria())) return false;
        if (!compareKey(getCd_chiave_secondaria(), k.getCd_chiave_secondaria())) return false;
        if (!compareKey(getCd_unita_funzionale(), k.getCd_unita_funzionale())) return false;
        if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
        return true;
    }

    /*
     * Getter dell'attributo cd_chiave_primaria
     */
    public java.lang.String getCd_chiave_primaria() {
        return cd_chiave_primaria;
    }

    /*
     * Setter dell'attributo cd_chiave_primaria
     */
    public void setCd_chiave_primaria(java.lang.String cd_chiave_primaria) {
        this.cd_chiave_primaria = cd_chiave_primaria;
    }

    /*
     * Getter dell'attributo cd_chiave_secondaria
     */
    public java.lang.String getCd_chiave_secondaria() {
        return cd_chiave_secondaria;
    }

    /*
     * Setter dell'attributo cd_chiave_secondaria
     */
    public void setCd_chiave_secondaria(java.lang.String cd_chiave_secondaria) {
        this.cd_chiave_secondaria = cd_chiave_secondaria;
    }

    /*
     * Getter dell'attributo cd_unita_funzionale
     */
    public java.lang.String getCd_unita_funzionale() {
        return cd_unita_funzionale;
    }

    /*
     * Setter dell'attributo cd_unita_funzionale
     */
    public void setCd_unita_funzionale(java.lang.String cd_unita_funzionale) {
        this.cd_unita_funzionale = cd_unita_funzionale;
    }

    /*
     * Getter dell'attributo esercizio
     */
    public java.lang.Integer getEsercizio() {
        return esercizio;
    }

    /*
     * Setter dell'attributo esercizio
     */
    public void setEsercizio(java.lang.Integer esercizio) {
        this.esercizio = esercizio;
    }

    public Configurazione_cnrKey esercizio(java.lang.Integer esercizio) {
        this.esercizio = esercizio;
        return this;
    }
    public int primaryKeyHashCode() {
        return
                calculateKeyHashCode(getCd_chiave_primaria()) +
                        calculateKeyHashCode(getCd_chiave_secondaria()) +
                        calculateKeyHashCode(getCd_unita_funzionale()) +
                        calculateKeyHashCode(getEsercizio());
    }
}
