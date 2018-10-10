package it.cnr.contab.config00.esercizio.bulk;

import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class EsercizioKey extends OggettoBulk implements KeyedPersistent {
    // CD_CDS VARCHAR(30) NOT NULL (PK)
    private java.lang.String cd_cds;

    // ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
    private java.lang.Integer esercizio;

    public EsercizioKey() {
        super();
    }

    public EsercizioKey(java.lang.String cd_cds, java.lang.Integer esercizio) {
        super();
        this.cd_cds = cd_cds;
        this.esercizio = esercizio;
    }

    public boolean equalsByPrimaryKey(Object o) {
        if (this == o) return true;
        if (!(o instanceof EsercizioKey)) return false;
        EsercizioKey k = (EsercizioKey) o;
        if (!compareKey(getCd_cds(), k.getCd_cds())) return false;
        if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
        return true;
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

    public int primaryKeyHashCode() {
        return
                calculateKeyHashCode(getCd_cds()) +
                        calculateKeyHashCode(getEsercizio());
    }

}
