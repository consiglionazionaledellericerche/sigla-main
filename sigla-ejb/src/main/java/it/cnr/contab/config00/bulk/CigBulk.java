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

/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 06/12/2012
 */
package it.cnr.contab.config00.bulk;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.util.ICancellatoLogicamente;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.CRUDBP;

import java.util.Optional;
import java.util.regex.Pattern;

public class CigBulk extends CigBase implements ICancellatoLogicamente {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * [TERZO Tabella contenente le entità anagrafiche di secondo livello (terzi). Ogni entità anagrafica di secondo livello afferisce ad una di primo (ANAGRAFICO).
     * <p>
     * Rappresenta le sedi, reali o per gestione, in cui si articola un soggetto anagrafico]
     **/
    private TerzoBulk terzo = new TerzoBulk();
    /**
     * [UNITA_ORGANIZZATIVA Rappresentazione dei Centri di Spesa e delle Unità Organizzative in una struttura ad albero organizzata su più livelli]
     **/
    private Unita_organizzativaBulk unitaOrganizzativa = new Unita_organizzativaBulk();

    /**
     * Created by BulkGenerator 2.0 [07/12/2009]
     * Table name: CIG
     **/
    public CigBulk() {
        super();
    }

    /**
     * Created by BulkGenerator 2.0 [07/12/2009]
     * Table name: CIG
     **/
    public CigBulk(java.lang.String cdCig) {
        super(cdCig);
    }

    /**
     * Created by BulkGenerator 2.0 [07/12/2009]
     * Restituisce il valore di: [Tabella contenente le entità anagrafiche di secondo livello (terzi). Ogni entità anagrafica di secondo livello afferisce ad una di primo (ANAGRAFICO).
     * <p>
     * Rappresenta le sedi, reali o per gestione, in cui si articola un soggetto anagrafico]
     **/
    public TerzoBulk getTerzo() {
        return terzo;
    }

    /**
     * Created by BulkGenerator 2.0 [07/12/2009]
     * Setta il valore di: [Tabella contenente le entità anagrafiche di secondo livello (terzi). Ogni entità anagrafica di secondo livello afferisce ad una di primo (ANAGRAFICO).
     * <p>
     * Rappresenta le sedi, reali o per gestione, in cui si articola un soggetto anagrafico]
     **/
    public void setTerzo(TerzoBulk terzo) {
        this.terzo = terzo;
    }

    /**
     * Created by BulkGenerator 2.0 [07/12/2009]
     * Restituisce il valore di: [Rappresentazione dei Centri di Spesa e delle Unità Organizzative in una struttura ad albero organizzata su più livelli]
     **/
    public Unita_organizzativaBulk getUnitaOrganizzativa() {
        return unitaOrganizzativa;
    }

    /**
     * Created by BulkGenerator 2.0 [07/12/2009]
     * Setta il valore di: [Rappresentazione dei Centri di Spesa e delle Unità Organizzative in una struttura ad albero organizzata su più livelli]
     **/
    public void setUnitaOrganizzativa(Unita_organizzativaBulk unitaOrganizzativa) {
        this.unitaOrganizzativa = unitaOrganizzativa;
    }

    /**
     * Created by BulkGenerator 2.0 [07/12/2009]
     * Restituisce il valore di: [cdTerzoRup]
     **/
    public java.lang.Integer getCdTerzoRup() {
        TerzoBulk terzo = this.getTerzo();
        if (terzo == null)
            return null;
        return getTerzo().getCd_terzo();
    }

    /**
     * Created by BulkGenerator 2.0 [07/12/2009]
     * Setta il valore di: [cdTerzoRup]
     **/
    public void setCdTerzoRup(java.lang.Integer cdTerzoRup) {
        this.getTerzo().setCd_terzo(cdTerzoRup);
    }

    /**
     * Created by BulkGenerator 2.0 [07/12/2009]
     * Restituisce il valore di: [cdUnitaOrganizzativa]
     **/
    public java.lang.String getCdUnitaOrganizzativa() {
        Unita_organizzativaBulk unitaOrganizzativa = this.getUnitaOrganizzativa();
        if (unitaOrganizzativa == null)
            return null;
        return getUnitaOrganizzativa().getCd_unita_organizzativa();
    }

    /**
     * Created by BulkGenerator 2.0 [07/12/2009]
     * Setta il valore di: [cdUnitaOrganizzativa]
     **/
    public void setCdUnitaOrganizzativa(java.lang.String cdUnitaOrganizzativa) {
        this.getUnitaOrganizzativa().setCd_unita_organizzativa(cdUnitaOrganizzativa);
    }

    public OggettoBulk initializeForInsert(CRUDBP crudbp, ActionContext actioncontext) {
        setFlValido(Boolean.TRUE);
        return super.initializeForInsert(crudbp, actioncontext);
    }

	@Override
    public OggettoBulk initializeForFreeSearch(CRUDBP crudbp,
                                               ActionContext actioncontext) {
        terzo = new TerzoBulk();
        unitaOrganizzativa = new Unita_organizzativaBulk();
        return super.initializeForFreeSearch(crudbp, actioncontext);
    }

    public boolean isCancellatoLogicamente() {
        return Optional.ofNullable(!getFlValido()).orElse(Boolean.FALSE);
    }

    public void cancellaLogicamente() {
        setFlValido(Boolean.FALSE);
    }

    @Override
    public void validate() throws ValidationException {
        super.validate();
        final String regex = "[0-9]{7}[0-9A-F]{3}|[V-Z]{1}[0-9A-F]{9}|[A-U]{1}[0-9A-F]{9}";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        if (Optional.ofNullable(getCdCig())
                .map(s -> !pattern.matcher(s).find())
                .orElse(Boolean.FALSE)) {
            throw new ValidationException("Il codice CIG inserito non è valido!");
        }
    }

}