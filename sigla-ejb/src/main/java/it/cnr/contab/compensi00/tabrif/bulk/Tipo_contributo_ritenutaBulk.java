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

package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.OrderedHashtable;

import java.util.Dictionary;
import java.util.Optional;

public class Tipo_contributo_ritenutaBulk extends Tipo_contributo_ritenutaBase {

    public final static String CASSA = "CA";
    public final static String COMPETENZA = "CO";
    public final static java.util.Dictionary ente_prev_stiKeys;
    public final static String ENTE_INPS = "05";
    public final static String ENTE_CPDEL = "06";
    public final static String ENTE_CPS = "08";
    public final static String ENTE_INPGI = "13";
    public final static String ENTE_FS = "FS";
    private final static Dictionary TI_CASSA_COMPETENZA;

    static {
        TI_CASSA_COMPETENZA = new OrderedHashtable();
        TI_CASSA_COMPETENZA.put(CASSA, "Cassa");
        TI_CASSA_COMPETENZA.put(COMPETENZA, "Competenza");
    }

    static {
        ente_prev_stiKeys = new it.cnr.jada.util.OrderedHashtable();
        ente_prev_stiKeys.put(ENTE_INPS, "Inps");
        ente_prev_stiKeys.put(ENTE_CPDEL, "Cpdel");
        ente_prev_stiKeys.put(ENTE_CPS, "Cps");
        ente_prev_stiKeys.put(ENTE_INPGI, "Inpgi");
        ente_prev_stiKeys.put(ENTE_FS, "Fondo Ferrovie");
    }

    private java.util.List intervalli;
    private Classificazione_montantiBulk classificazioneMontanti;
    private java.util.Collection classificazioneMontantiColl;
    private java.util.Collection classificazioneCoriColl;
    private Classificazione_coriBulk classificazioneCori;

    public Tipo_contributo_ritenutaBulk() {
        super();
    }

    public Tipo_contributo_ritenutaBulk(java.lang.String cd_contributo_ritenuta, java.sql.Timestamp dt_ini_validita) {
        super(cd_contributo_ritenuta, dt_ini_validita);
    }

    public final static java.util.Dictionary getEnte_prev_stiKeys() {
        return ente_prev_stiKeys;
    }

    public java.lang.String getCd_classificazione_cori() {
        it.cnr.contab.compensi00.tabrif.bulk.Classificazione_coriBulk classificazioneCori = this.getClassificazioneCori();
        if (classificazioneCori == null)
            return null;
        return classificazioneCori.getCd_classificazione_cori();
    }

    public void setCd_classificazione_cori(java.lang.String cd_classificazione_cori) {
    	if (!Optional.ofNullable(classificazioneCori).isPresent())
    		this.setClassificazioneCori(new Classificazione_coriBulk());
        this.getClassificazioneCori().setCd_classificazione_cori(cd_classificazione_cori);
    }

    /**
     * Insert the method's description here.
     * Creation date: (17/06/2002 16.24.10)
     *
     * @return it.cnr.contab.compensi00.tabrif.bulk.Classificazione_coriBulk
     */
    public Classificazione_coriBulk getClassificazioneCori() {
        return classificazioneCori;
    }

    /**
     * Insert the method's description here.
     * Creation date: (17/06/2002 16.24.10)
     *
     * @param newClassificazioneCori it.cnr.contab.compensi00.tabrif.bulk.Classificazione_coriBulk
     */
    public void setClassificazioneCori(Classificazione_coriBulk newClassificazioneCori) {
        classificazioneCori = newClassificazioneCori;
    }

    /**
     * Insert the method's description here.
     * Creation date: (17/06/2002 16.23.50)
     *
     * @return java.util.Collection
     */
    public java.util.Collection getClassificazioneCoriColl() {
        return classificazioneCoriColl;
    }

    /**
     * Insert the method's description here.
     * Creation date: (17/06/2002 16.23.50)
     *
     * @param newClassificazioneCoriColl java.util.Collection
     */
    public void setClassificazioneCoriColl(java.util.Collection newClassificazioneCoriColl) {
        classificazioneCoriColl = newClassificazioneCoriColl;
    }

    /**
     * Insert the method's description here.
     * Creation date: (17/06/2002 16.26.30)
     *
     * @return it.cnr.contab.compensi00.tabrif.bulk.Classificazione_montantiBulk
     */
    public Classificazione_montantiBulk getClassificazioneMontanti() {
        return classificazioneMontanti;
    }

    /**
     * Insert the method's description here.
     * Creation date: (17/06/2002 16.26.30)
     *
     * @param newClassificazioneMontanti it.cnr.contab.compensi00.tabrif.bulk.Classificazione_montantiBulk
     */
    public void setClassificazioneMontanti(Classificazione_montantiBulk newClassificazioneMontanti) {
        classificazioneMontanti = newClassificazioneMontanti;
    }

    /**
     * Insert the method's description here.
     * Creation date: (17/06/2002 16.24.22)
     *
     * @return java.util.Collection
     */
    public java.util.Collection getClassificazioneMontantiColl() {
        return classificazioneMontantiColl;
    }

    /**
     * Insert the method's description here.
     * Creation date: (17/06/2002 16.24.22)
     *
     * @param newClassificazioneMontantiColl java.util.Collection
     */
    public void setClassificazioneMontantiColl(java.util.Collection newClassificazioneMontantiColl) {
        classificazioneMontantiColl = newClassificazioneMontantiColl;
    }

    public java.sql.Timestamp getDataFineValidita() {

        if ((getDt_fin_validita() != null) && (getDt_fin_validita().equals(it.cnr.contab.config00.esercizio.bulk.EsercizioHome.DATA_INFINITO)))
            return null;
        return getDt_fin_validita();
    }

    public void setDataFineValidita(java.sql.Timestamp dt_fin_validita) {

        this.setDt_fin_validita(dt_fin_validita);

    }

    /**
     * Insert the method's description here.
     * Creation date: (06/06/2002 16.40.30)
     *
     * @return java.util.List
     */
    public java.util.List getIntervalli() {
        return intervalli;
    }

    /**
     * Insert the method's description here.
     * Creation date: (06/06/2002 16.40.30)
     *
     * @param newIntervalli java.util.List
     */
    public void setIntervalli(java.util.List newIntervalli) {
        intervalli = newIntervalli;
    }

    public java.lang.Long getPg_classificazione_montanti() {
        it.cnr.contab.compensi00.tabrif.bulk.Classificazione_montantiBulk classificazioneMontanti = this.getClassificazioneMontanti();
        if (classificazioneMontanti == null)
            return null;
        return classificazioneMontanti.getPg_classificazione_montanti();
    }

    public void setPg_classificazione_montanti(java.lang.Long pg_classificazione_montanti) {
        this.getClassificazioneMontanti().setPg_classificazione_montanti(pg_classificazione_montanti);
    }

    /**
     * Insert the method's description here.
     * Creation date: (27/11/2001 14.10.54)
     *
     * @return java.util.Dictionary
     */
    public java.util.Dictionary getTi_cassa_competenzaKeys() {
        return TI_CASSA_COMPETENZA;
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/01/2002 14.52.26)
     */
    public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) {

        super.initializeForInsert(bp, context);
        setTi_cassa_competenza(CASSA);
        resetFlags();

        return this;
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/01/2002 14.52.26)
     */
    public void resetFlags() {

        this.setFl_assistenza_fiscale(new Boolean(false));
        this.setFl_scrivi_montanti(new Boolean(false));
        this.setFl_uso_in_lordizza(new Boolean(false));
        this.setFl_gla(new Boolean(false));
        this.setFl_sospensione_irpef(new Boolean(false));
    }

    public void validate() throws ValidationException {

        // controllo su campo CONTRIBUTO_RITENUTA
        if (getCd_contributo_ritenuta() == null)
            throw new ValidationException("Il campo Codice deve essere valorizzato !");

        // controllo su campo CONTRIBUTO_RITENUTA
        if (getDs_contributo_ritenuta() == null)
            throw new ValidationException("Il campo Descrizione deve essere valorizzato !");

        // controllo su campo DATA INIZIO VALIDITA
        if (getDt_ini_validita() == null)
            throw new ValidationException("Il campo Data Inizio Validita deve essere valorizzato");
    }
}
