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

package it.cnr.contab.anagraf00.tabrif.bulk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import it.cnr.jada.bulk.OggettoBulk;

/**
 * Gestione dei dati relativi alla tabella Tipo_rapporto
 */
@JsonInclude(value = Include.NON_NULL)
public class Tipo_rapportoBulk extends Tipo_rapportoBase {

    public final static java.util.Dictionary DIPENDENTE_ALTRO;
    public final static String DIPENDENTE = "D";
    public final static String ALTRO = "A";
    public final static java.util.Dictionary TIPI_RAPPORTO;
    public final static String PROFESSIONISTA = "P";
    public final static String STAGIONALE = "S";
    public final static String COLLABORATORE_COORD_E_CONT = "C";
    public final static String ASSIMILATO_A_LAVORO_DIP = "A";
    public final static String BORSISTI = "B";

    static {
        DIPENDENTE_ALTRO = new it.cnr.jada.util.OrderedHashtable();
        DIPENDENTE_ALTRO.put(DIPENDENTE, "Dipendenti");
        DIPENDENTE_ALTRO.put(ALTRO, "Altri soggetti");
    }

    static {
        TIPI_RAPPORTO = new it.cnr.jada.util.OrderedHashtable();
        TIPI_RAPPORTO.put(PROFESSIONISTA, "Professionista");
        TIPI_RAPPORTO.put(STAGIONALE, "Stagionale");
        TIPI_RAPPORTO.put(COLLABORATORE_COORD_E_CONT, "Collaboratore coordinato e continuativo");
        TIPI_RAPPORTO.put(ASSIMILATO_A_LAVORO_DIP, "Assimilato a lavoro dipendente");
        TIPI_RAPPORTO.put(BORSISTI, "Borsista");
    }

    /**
     *
     */
    public Tipo_rapportoBulk() {
    }

    public Tipo_rapportoBulk(java.lang.String cd_tipo_rapporto) {
        super(cd_tipo_rapporto);
    }

    @JsonIgnore
    public java.util.Dictionary getTi_dipendente_altroKeys() {
        return DIPENDENTE_ALTRO;
    }

    /**
     * Insert the method's description here.
     * Creation date: (13/03/2002 12.43.15)
     *
     * @return it.cnr.jada.bulk.BulkList
     */
	@JsonIgnore
    public java.util.Dictionary getTipiRapportoKeys() {
        return TIPI_RAPPORTO;
    }

    public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) {

        super.initializeForInsert(bp, context);
        this.setTi_dipendente_altro(DIPENDENTE);
        return this;
    }

    /**
     * Insert the method's description here.
     * Creation date: (13/03/2002 13.20.03)
     *
     * @return boolean
     */
    @JsonIgnore
    public boolean isDipendente() {
        return
                getTi_dipendente_altro() != null &&
                        getTi_dipendente_altro().equals(DIPENDENTE);
    }

    /**
     * Insert the method's description here.
     * Creation date: (13/03/2002 13.20.03)
     *
     * @return boolean
     */
	@JsonIgnore
    public boolean isFindTipoRapportoRO() {

        return false;
    }

    /**
     * Insert the method's description here.
     * Creation date: (13/03/2002 13.20.03)
     *
     * @return boolean
     */
	@JsonIgnore
    public boolean isTipoRapportoRO() {

        return ((getTi_dipendente_altro() == null) || (getTi_dipendente_altro().equals(DIPENDENTE)));
    }
}
