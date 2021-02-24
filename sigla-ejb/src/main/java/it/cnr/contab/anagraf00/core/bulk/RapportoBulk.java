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

package it.cnr.contab.anagraf00.core.bulk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.persistency.Broker;
import it.cnr.si.service.dto.anagrafica.enums.TipoContratto;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestione dei dati relativi alla tabella Rapporto
 */

public class RapportoBulk extends RapportoBase {

    private it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk tipo_rapporto;
    @JsonIgnore
    private AnagraficoBulk anagrafico;
    @JsonIgnore
    private it.cnr.jada.bulk.BulkList inquadramenti = new BulkList();
    private java.sql.Timestamp dt_fin_validita_originale;

    public static Map<String, TipoContratto> TIPOCONTRATTO_ACE = new HashMap<String, TipoContratto>(){
        {
            put("BORS", TipoContratto.BORSISTA);
            put("ASS", TipoContratto.ASSOCIATO_CON_INCARICO_DI_COLLABORAZIONE);
            put("COLL", TipoContratto.COLLABORATORE_PROFESSIONALE);
            put("PROF", TipoContratto.COLLABORATORE_PROFESSIONALE);
            put("OCCA", TipoContratto.COLLABORATORE_PROFESSIONALE);
        }
    };

    public RapportoBulk() {
        super();
    }

    public RapportoBulk(java.lang.Integer cd_anag, java.lang.String cd_tipo_rapporto, java.sql.Timestamp dt_ini_validita) {
        super(cd_anag, cd_tipo_rapporto, dt_ini_validita);
        setAnagrafico(new it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk(cd_anag));
        setTipo_rapporto(new it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk(cd_tipo_rapporto));
    }

    /**
     * Metodo per l'aggiunta di un elemento <code>InquadramentoBulk</code> alla lista
     * degli inquadramenti.
     *
     * @param inquadramento elemento da aggiungere.
     * @return la posizione nella lista
     * @see removeFromInquadramenti
     */

    public int addToInquadramenti(InquadramentoBulk inquadramento) throws ValidationException {
        if (getDt_ini_validita() == null || getDt_fin_validita() == null)
            throw new ValidationException("E' prima necessario impostare il periodo di validità del rapporto");
        java.util.Calendar cal = java.util.Calendar.getInstance();
        if (inquadramenti.isEmpty()) {
            inquadramento.setDt_ini_validita(getDt_ini_validita());
        } else for (java.util.Iterator i = inquadramenti.iterator(); i.hasNext(); ) {
            InquadramentoBulk inquadramento2 = (InquadramentoBulk) i.next();
            if (inquadramento.getDt_ini_validita() == null || inquadramento.getDt_ini_validita().before(inquadramento2.getDt_fin_validita())) {
                cal.setTime(inquadramento2.getDt_fin_validita());
                cal.add(Calendar.DAY_OF_YEAR, 1);
                inquadramento.setDt_ini_validita(new java.sql.Timestamp(cal.getTime().getTime()));
            }
        }
        if (inquadramento.getDt_ini_validita().after(getDt_fin_validita()))
            inquadramento.setDt_ini_validita(null);
        else
            inquadramento.setDt_fin_validita(getDt_fin_validita());
        inquadramenti.add(inquadramento);
        inquadramento.setRapporto(this);
        return inquadramenti.size() - 1;
    }

    public void fetchedFrom(Broker broker) throws it.cnr.jada.persistency.IntrospectionException, it.cnr.jada.persistency.FetchException {
        super.fetchedFrom(broker);
        setDt_fin_validita_originale(getDt_fin_validita());
    }

    /**
     * Restituisce l'<code>AnagraficoBulk</code> a cui l'oggetto è correlato.
     *
     * @return it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk
     * @see setAnagrafico
     */

    public AnagraficoBulk getAnagrafico() {
        return anagrafico;
    }

    /**
     * Imposta l'<code>AnagraficoBulk</code> a cui l'oggetto è correlato.
     *
     * @param newAnagrafico Anagrafica di riferimento.
     * @see getAnagrafico
     */

    public void setAnagrafico(AnagraficoBulk newAnagrafico) {
        anagrafico = newAnagrafico;
    }

    /**
     * Restituisce le liste di oggetti correlati al rapporto.
     *
     * @return it.cnr.jada.bulk.BulkCollection[]
     */

    public it.cnr.jada.bulk.BulkCollection[] getBulkLists() {
        return new it.cnr.jada.bulk.BulkCollection[]{
                inquadramenti
        };
    }

    public java.lang.Integer getCd_anag() {
        it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk anagrafico = this.getAnagrafico();
        if (anagrafico == null)
            return null;
        return anagrafico.getCd_anag();
    }

    public void setCd_anag(java.lang.Integer cd_anag) {
        this.getAnagrafico().setCd_anag(cd_anag);
    }

    public java.lang.String getCd_tipo_rapporto() {
        it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk tipo_rapporto = this.getTipo_rapporto();
        if (tipo_rapporto == null)
            return null;
        return tipo_rapporto.getCd_tipo_rapporto();
    }

    public void setCd_tipo_rapporto(java.lang.String cd_tipo_rapporto) {
        this.getTipo_rapporto().setCd_tipo_rapporto(cd_tipo_rapporto);
    }

    /**
     * Insert the method's description here.
     * Creation date: (03/07/2002 10:27:33)
     *
     * @return java.sql.Timestamp
     */
    public java.sql.Timestamp getDt_fin_validita_originale() {
        return dt_fin_validita_originale;
    }

    /**
     * Insert the method's description here.
     * Creation date: (03/07/2002 10:27:33)
     *
     * @param newDt_fin_validita_originale java.sql.Timestamp
     */
    public void setDt_fin_validita_originale(java.sql.Timestamp newDt_fin_validita_originale) {
        dt_fin_validita_originale = newDt_fin_validita_originale;
    }

    /**
     * Restituisce la <code>BulkList</code> contenente l'elenco di <code>InquadramentoBulk</code> in
     * relazione con l'oggetto.
     *
     * @return it.cnr.jada.bulk.BulkList
     * @see setInquadramenti
     */

    public it.cnr.jada.bulk.BulkList getInquadramenti() {
        return inquadramenti;
    }

    /**
     * Imposta la <code>BulkList</code> contenente l'elenco di <code>InquadramentoBulk</code> in
     * relazione con l'oggetto.
     *
     * @param newInquadramenti la lista
     * @see getInquadramenti
     */

    public void setInquadramenti(it.cnr.jada.bulk.BulkList newInquadramenti) {
        inquadramenti = newInquadramenti;
    }

    /**
     * Restituisce l'<code>Tipo_rapportoBulk</code> a cui l'oggetto è correlato.
     *
     * @return it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk
     * @see setTipo_rapporto
     */

    public it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk getTipo_rapporto() {
        return tipo_rapporto;
    }

    /**
     * Imposta l'<code>Tipo_rapportoBulk</code> a cui l'oggetto è correlato.
     *
     * @param newTipo_rapporto il tipo di rapporto.
     * @see getTipo_rapporto
     */

    public void setTipo_rapporto(it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk newTipo_rapporto) {
        tipo_rapporto = newTipo_rapporto;
    }

    /**
     * Indica quando tipo_rapporto deve essere read only.
     *
     * @return boolean
     */
	@JsonIgnore
    public boolean isROTipo_rapporto() {
        if (getAnagrafico() != null)
            return anagrafico.isDipendente();

        return tipo_rapporto == null || tipo_rapporto.getCrudStatus() == NORMAL;
    }

    /**
     * Elimina l'<code>InquadramentoBulk</code> alla posizione index dalla lista
     * inquadramenti.
     *
     * @param index Indice dell'elemento da cancellare.
     * @return InquadramentoBulk
     * @see addToInquadramenti
     */

    public InquadramentoBulk removeFromInquadramenti(int index) {
        return (InquadramentoBulk) inquadramenti.remove(index);
    }
	@JsonIgnore
    public boolean isAbilitato_inquadramento() {
        if (getTipo_rapporto() != null && getTipo_rapporto().getFl_inquadramento() != null)
            return getTipo_rapporto().getFl_inquadramento();
        else
            return false;
    }
}
