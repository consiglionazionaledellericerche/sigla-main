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

package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Elemento_voceKey extends OggettoBulk implements KeyedPersistent {
    // TI_APPARTENENZA CHAR(1) NOT NULL (PK)
    private java.lang.String ti_appartenenza;

    // CD_ELEMENTO_VOCE VARCHAR(20) NOT NULL (PK)
    private java.lang.String cd_elemento_voce;

    // ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
    private java.lang.Integer esercizio;

    // TI_GESTIONE CHAR(1) NOT NULL (PK)
    private java.lang.String ti_gestione;

    public Elemento_voceKey() {
        super();
    }

    public Elemento_voceKey(java.lang.String cd_elemento_voce, java.lang.Integer esercizio, java.lang.String ti_appartenenza, java.lang.String ti_gestione) {
        super();
        this.cd_elemento_voce = cd_elemento_voce;
        this.esercizio = esercizio;
        this.ti_appartenenza = ti_appartenenza;
        this.ti_gestione = ti_gestione;
    }

    public boolean equalsByPrimaryKey(Object o) {
        if (this == o) return true;
        if (!(o instanceof Elemento_voceKey)) return false;
        Elemento_voceKey k = (Elemento_voceKey) o;
        if (!compareKey(getCd_elemento_voce(), k.getCd_elemento_voce())) return false;
        if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
        if (!compareKey(getTi_appartenenza(), k.getTi_appartenenza())) return false;
        if (!compareKey(getTi_gestione(), k.getTi_gestione())) return false;
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        return this.equalsByPrimaryKey(obj);
    }

    /*
     * Getter dell'attributo cd_elemento_voce
     */
    public java.lang.String getCd_elemento_voce() {
        return cd_elemento_voce;
    }

    /*
     * Setter dell'attributo cd_elemento_voce
     */
    public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
        this.cd_elemento_voce = cd_elemento_voce;
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

    /*
     * Getter dell'attributo ti_appartenenza
     */
    public java.lang.String getTi_appartenenza() {
        return ti_appartenenza;
    }

    /*
     * Setter dell'attributo ti_appartenenza
     */
    public void setTi_appartenenza(java.lang.String ti_appartenenza) {
        this.ti_appartenenza = ti_appartenenza;
    }

    /*
     * Getter dell'attributo ti_gestione
     */
    public java.lang.String getTi_gestione() {
        return ti_gestione;
    }

    /*
     * Setter dell'attributo ti_gestione
     */
    public void setTi_gestione(java.lang.String ti_gestione) {
        this.ti_gestione = ti_gestione;
    }

    @Override
    public int hashCode() {
        return primaryKeyHashCode();
    }

    public int primaryKeyHashCode() {
        return
                calculateKeyHashCode(getCd_elemento_voce()) +
                        calculateKeyHashCode(getEsercizio()) +
                        calculateKeyHashCode(getTi_appartenenza()) +
                        calculateKeyHashCode(getTi_gestione());
    }
}
