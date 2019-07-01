package it.cnr.contab.doccont00.core.bulk;

public class Mandato_terzoIBulk extends Mandato_terzoBulk {
    MandatoIBulk mandatoI;

    public Mandato_terzoIBulk() {
        super();
    }

    public Mandato_terzoIBulk(String cd_cds, Integer cd_terzo, Integer esercizio, Long pg_mandato) {
        super(cd_cds, cd_terzo, esercizio, pg_mandato);
    }

    /**
     * @return it.cnr.contab.doccont00.core.bulk.MandatoIBulk
     */
    public MandatoBulk getMandato() {
        return mandatoI;
    }

    /**
     * @param newMandato it.cnr.contab.doccont00.core.bulk.MandatoIBulk
     */
    public void setMandato(MandatoBulk newMandato) {
        setMandatoI((MandatoIBulk) newMandato);
    }

    /**
     * @return it.cnr.contab.doccont00.core.bulk.MandatoIBulk
     */
    public MandatoIBulk getMandatoI() {
        return mandatoI;
    }

    /**
     * @param newMandatoI it.cnr.contab.doccont00.core.bulk.MandatoIBulk
     */
    public void setMandatoI(MandatoIBulk newMandatoI) {
        mandatoI = newMandatoI;
    }
}
