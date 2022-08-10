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

package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

import java.util.Objects;

public class Fattura_passiva_rigaKey extends OggettoBulk implements KeyedPersistent {
    // CD_CDS VARCHAR(30) NOT NULL (PK)
    private java.lang.String cd_cds;

    // CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL (PK)
    private java.lang.String cd_unita_organizzativa;

    // ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
    private java.lang.Integer esercizio;

    // PG_FATTURA_PASSIVA DECIMAL(10,0) NOT NULL (PK)
    private java.lang.Long pg_fattura_passiva;

    // PROGRESSIVO_RIGA DECIMAL(10,0) NOT NULL (PK)
    private java.lang.Long progressivo_riga;

    public Fattura_passiva_rigaKey() {
        super();
    }

    public Fattura_passiva_rigaKey(java.lang.String cd_cds, java.lang.String cd_unita_organizzativa, java.lang.Integer esercizio, java.lang.Long pg_fattura_passiva, java.lang.Long progressivo_riga) {
        this.cd_cds = cd_cds;
        this.cd_unita_organizzativa = cd_unita_organizzativa;
        this.esercizio = esercizio;
        this.pg_fattura_passiva = pg_fattura_passiva;
        this.progressivo_riga = progressivo_riga;
    }

    public boolean equalsByPrimaryKey(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fattura_passiva_rigaKey)) return false;
        Fattura_passiva_rigaKey k = (Fattura_passiva_rigaKey) o;
        if (!compareKey(getCd_cds(), k.getCd_cds())) return false;
        if (!compareKey(getCd_unita_organizzativa(), k.getCd_unita_organizzativa())) return false;
        if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
        if (!compareKey(getPg_fattura_passiva(), k.getPg_fattura_passiva())) return false;
		return compareKey(getProgressivo_riga(), k.getProgressivo_riga());
	}

    /*
     * Getter dell'attributo cd_cds
     */
    public java.lang.String getCd_cds() {
        return cd_cds;
    }

    /*
     * Setter dell'attributo cd_cds
     */
    public void setCd_cds(java.lang.String cd_cds) {
        this.cd_cds = cd_cds;
    }

    /*
     * Getter dell'attributo cd_unita_organizzativa
     */
    public java.lang.String getCd_unita_organizzativa() {
        return cd_unita_organizzativa;
    }

    /*
     * Setter dell'attributo cd_unita_organizzativa
     */
    public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
        this.cd_unita_organizzativa = cd_unita_organizzativa;
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
     * Getter dell'attributo pg_fattura_passiva
     */
    public java.lang.Long getPg_fattura_passiva() {
        return pg_fattura_passiva;
    }

    /*
     * Setter dell'attributo pg_fattura_passiva
     */
    public void setPg_fattura_passiva(java.lang.Long pg_fattura_passiva) {
        this.pg_fattura_passiva = pg_fattura_passiva;
    }

    /*
     * Getter dell'attributo progressivo_riga
     */
    public java.lang.Long getProgressivo_riga() {
        return progressivo_riga;
    }

    /*
     * Setter dell'attributo progressivo_riga
     */
    public void setProgressivo_riga(java.lang.Long progressivo_riga) {
        this.progressivo_riga = progressivo_riga;
    }

    public int primaryKeyHashCode() {
        return
                calculateKeyHashCode(getCd_cds()) +
                        calculateKeyHashCode(getCd_unita_organizzativa()) +
                        calculateKeyHashCode(getEsercizio()) +
                        calculateKeyHashCode(getPg_fattura_passiva()) +
                        calculateKeyHashCode(getProgressivo_riga());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fattura_passiva_rigaKey that = (Fattura_passiva_rigaKey) o;
        return Objects.equals(cd_cds, that.cd_cds) && Objects.equals(cd_unita_organizzativa, that.cd_unita_organizzativa) && Objects.equals(esercizio, that.esercizio) && Objects.equals(pg_fattura_passiva, that.pg_fattura_passiva) && Objects.equals(progressivo_riga, that.progressivo_riga);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cd_cds, cd_unita_organizzativa, esercizio, pg_fattura_passiva, progressivo_riga);
    }
}
