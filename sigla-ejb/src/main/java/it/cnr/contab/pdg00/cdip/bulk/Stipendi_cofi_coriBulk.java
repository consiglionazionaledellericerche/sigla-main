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

package it.cnr.contab.pdg00.cdip.bulk;

import it.cnr.contab.compensi00.tabrif.bulk.Tipo_contributo_ritenutaBulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;

import java.util.Optional;

public class Stipendi_cofi_coriBulk extends Stipendi_cofi_coriBase {

    public static final String PERCIPIENTE = "P";
    public static final String ENTE = "E";
    public static final int GENNAIO = 1;
    public static final int FEBBRAIO = 2;
    public static final int MARZO = 3;
    public static final int APRILE = 4;
    public static final int MAGGIO = 5;
    public static final int GIUGNO = 6;
    public static final int LUGLIO = 7;
    public static final int AGOSTO = 8;
    public static final int SETTEMBRE = 9;
    public static final int OTTOBRE = 10;
    public static final int NOVEMBRE = 11;
    public static final int TREDICESIMA = 12;
    public static final int DICEMBRE = 13;
    private static final java.util.Dictionary tipoKeys = new it.cnr.jada.util.OrderedHashtable();
    private static final java.util.Dictionary meseKeys = new it.cnr.jada.util.OrderedHashtable();

    static {
        tipoKeys.put(PERCIPIENTE, "Percipiente");
        tipoKeys.put(ENTE, "Ente");
    }

    static {
        meseKeys.put(new Integer(GENNAIO), "Gennaio");
        meseKeys.put(new Integer(FEBBRAIO), "Febbraio");
        meseKeys.put(new Integer(MARZO), "Marzo");
        meseKeys.put(new Integer(APRILE), "Aprile");
        meseKeys.put(new Integer(MAGGIO), "Maggio");
        meseKeys.put(new Integer(GIUGNO), "Giugno");
        meseKeys.put(new Integer(LUGLIO), "Luglio");
        meseKeys.put(new Integer(AGOSTO), "Agosto");
        meseKeys.put(new Integer(SETTEMBRE), "Settembre");
        meseKeys.put(new Integer(OTTOBRE), "Ottobre");
        meseKeys.put(new Integer(NOVEMBRE), "Novembre");
        meseKeys.put(new Integer(TREDICESIMA), "Tredicesima");
        meseKeys.put(new Integer(DICEMBRE), "Dicembre");
    }

    private Stipendi_cofiBulk stipendi_cofi = new Stipendi_cofiBulk();
    private java.util.Collection tipoStipendi_cofi;
    private Tipo_contributo_ritenutaBulk tipo_contributo_ritenuta = new Tipo_contributo_ritenutaBulk();

    public Stipendi_cofi_coriBulk() {
        super();
    }

    public Stipendi_cofi_coriBulk(java.lang.Integer esercizio,
                                  java.lang.Integer mese,
                                  String cd_contributo_ritenuta,
                                  String ti_ente_percipiente) {
        super(esercizio, mese, cd_contributo_ritenuta, ti_ente_percipiente);
    }

    public final java.util.Dictionary getTipoKeys() {
        return tipoKeys;
    }

    public java.lang.Integer getEsercizio() {
        if (getStipendi_cofi() != null)
            return getStipendi_cofi().getEsercizio();
        return null;
    }

    public void setEsercizio(java.lang.Integer esercizio) {
        this.getStipendi_cofi().setEsercizio(esercizio);
    }

    public java.lang.Integer getMese() {
        if (getStipendi_cofi() != null)
            return getStipendi_cofi().getMese();
        return null;
    }

    public void setMese(java.lang.Integer mese) {
        this.getStipendi_cofi().setMese(mese);
    }

    /**
     * @return
     */
    public Stipendi_cofiBulk getStipendi_cofi() {
        return stipendi_cofi;
    }

    /**
     * @param bulk
     */
    public void setStipendi_cofi(Stipendi_cofiBulk bulk) {
        stipendi_cofi = bulk;
    }

    /**
     * @return
     */
    public Tipo_contributo_ritenutaBulk getTipo_contributo_ritenuta() {
        return tipo_contributo_ritenuta;
    }

    /**
     * @param bulk
     */
    public void setTipo_contributo_ritenuta(Tipo_contributo_ritenutaBulk bulk) {
        tipo_contributo_ritenuta = bulk;
    }

    public String getCd_contributo_ritenuta() {
        return getTipo_contributo_ritenuta().getCd_contributo_ritenuta();
    }

    public void setCd_contributo_ritenuta(String contributo_ritenuta) {
        this.getTipo_contributo_ritenuta().setCd_contributo_ritenuta(contributo_ritenuta);
    }

    public boolean isROTipo_contributo_ritenuta() {
        return getTipo_contributo_ritenuta() == null ||
                getTipo_contributo_ritenuta().getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL;
    }

    /**
     * @return
     */
    public java.util.Collection getTipoStipendi_cofi() {
        return tipoStipendi_cofi;
    }

    /**
     * @param collection
     */
    public void setTipoStipendi_cofi(java.util.Collection collection) {
        tipoStipendi_cofi = collection;
    }

    /**
     * @return
     */
    public final java.util.Dictionary getMeseKeys() {
        return meseKeys;
    }

    public void validate() throws ValidationException {

        if (getDt_da_competenza_coge() != null && getDt_a_competenza_coge() != null && getDt_da_competenza_coge().after(getDt_a_competenza_coge()))
            throw new ValidationException("La data di competenza 'Dal' deve essere precedente a quella di competenza 'Al'!");

        super.validate();
    }

    public OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) {

        super.initialize(bp, context);
        setEsercizio(((it.cnr.contab.utenze00.bp.CNRUserContext) context.getUserContext()).getEsercizio());
        return this;
    }

    public String getDs_contributo_ritenuta() {
        return Optional.ofNullable(getTipo_contributo_ritenuta())
                    .map(Tipo_contributo_ritenutaBulk::getDs_contributo_ritenuta)
                    .orElse(null);
    }

    public void setDs_contributo_ritenuta(String ds_contributo_ritenuta) {
        Optional.ofNullable(tipo_contributo_ritenuta)
                .orElse(new Tipo_contributo_ritenutaBulk())
                .setDs_contributo_ritenuta(ds_contributo_ritenuta);
    }

    public String getCd_classificazione_cori() {
        return Optional.ofNullable(getTipo_contributo_ritenuta())
                .map(Tipo_contributo_ritenutaBulk::getCd_classificazione_cori)
                .orElse(null);
    }

    public void setCd_classificazione_cori(String cd_classificazione_cori) {
        Optional.ofNullable(tipo_contributo_ritenuta)
                .orElse(new Tipo_contributo_ritenutaBulk())
                .setCd_classificazione_cori(cd_classificazione_cori);
    }
}