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

package it.cnr.contab.doccont00.core.bulk;

public abstract class Mandato_terzoBulk extends Mandato_terzoBase {
    protected it.cnr.contab.doccont00.tabrif.bulk.Tipo_bolloBulk tipoBollo;
    protected it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzo;


    public Mandato_terzoBulk() {
        super();
    }

    /**
     * <!-- @TODO: da completare -->
     *
     * @param mandato
     * @param terzo
     */
    public Mandato_terzoBulk(MandatoBulk mandato, Mandato_terzoBulk terzo) {
        super();
        setToBeCreated();
        setUser(terzo.getUser());
        setMandato(mandato);
        setTerzo(terzo.getTerzo());
        setTipoBollo(terzo.getTipoBollo());
    }

    public Mandato_terzoBulk(java.lang.String cd_cds, java.lang.Integer cd_terzo, java.lang.Integer esercizio, java.lang.Long pg_mandato) {
        super(cd_cds, cd_terzo, esercizio, pg_mandato);
        setTerzo(new it.cnr.contab.anagraf00.core.bulk.TerzoBulk(cd_terzo));
        setMandato(new it.cnr.contab.doccont00.core.bulk.MandatoBulk(cd_cds, esercizio, pg_mandato));
    }

    public java.lang.String getCd_cds() {
        it.cnr.contab.doccont00.core.bulk.MandatoBulk mandato = this.getMandato();
        if (mandato == null)
            return null;
        it.cnr.contab.config00.sto.bulk.CdsBulk cds = mandato.getCds();
        if (cds == null)
            return null;
        return cds.getCd_unita_organizzativa();
    }

    public void setCd_cds(java.lang.String cd_cds) {
        this.getMandato().getCds().setCd_unita_organizzativa(cd_cds);
    }

    public java.lang.Integer getCd_terzo() {
        it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzo = this.getTerzo();
        if (terzo == null)
            return null;
        return terzo.getCd_terzo();
    }

    public void setCd_terzo(java.lang.Integer cd_terzo) {
        this.getTerzo().setCd_terzo(cd_terzo);
    }

    public java.lang.String getCd_tipo_bollo() {
        it.cnr.contab.doccont00.tabrif.bulk.Tipo_bolloBulk tipoBollo = this.getTipoBollo();
        if (tipoBollo == null)
            return null;
        return tipoBollo.getCd_tipo_bollo();
    }

    public void setCd_tipo_bollo(java.lang.String cd_tipo_bollo) {
        this.getTipoBollo().setCd_tipo_bollo(cd_tipo_bollo);
    }

    public java.lang.Integer getEsercizio() {
        it.cnr.contab.doccont00.core.bulk.MandatoBulk mandato = this.getMandato();
        if (mandato == null)
            return null;
        return mandato.getEsercizio();
    }

    public void setEsercizio(java.lang.Integer esercizio) {
        this.getMandato().setEsercizio(esercizio);
    }

    /**
     * @return it.cnr.contab.doccont00.core.bulk.MandatoBulk
     */
    public abstract MandatoBulk getMandato();

    /**
     * @param newMandato it.cnr.contab.doccont00.core.bulk.MandatoBulk
     */
    public abstract void setMandato(MandatoBulk newMandato);

    public java.lang.Long getPg_mandato() {
        it.cnr.contab.doccont00.core.bulk.MandatoBulk mandato = this.getMandato();
        if (mandato == null)
            return null;
        return mandato.getPg_mandato();
    }

    public void setPg_mandato(java.lang.Long pg_mandato) {
        this.getMandato().setPg_mandato(pg_mandato);
    }

    /**
     * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
     */
    public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getTerzo() {
        return terzo;
    }

    /**
     * @param newTerzo it.cnr.contab.anagraf00.core.bulk.TerzoBulk
     */
    public void setTerzo(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newTerzo) {
        terzo = newTerzo;
    }

    /**
     * @return it.cnr.contab.doccont00.tabrif.bulk.Tipo_bolloBulk
     */
    public it.cnr.contab.doccont00.tabrif.bulk.Tipo_bolloBulk getTipoBollo() {
        return tipoBollo;
    }

    /**
     * @param newTipoBollo it.cnr.contab.doccont00.tabrif.bulk.Tipo_bolloBulk
     */
    public void setTipoBollo(it.cnr.contab.doccont00.tabrif.bulk.Tipo_bolloBulk newTipoBollo) {
        tipoBollo = newTipoBollo;
    }
}
