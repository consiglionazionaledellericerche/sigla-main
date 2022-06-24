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
 * Date 27/09/2018
 */
package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;

import java.util.Dictionary;

public class TipoFinanziamentoBulk extends TipoFinanziamentoBase {

    public static final String CODICE_FOE = "FOE";
    public static final String CODICE_FOE_PRO = "FOE_PRO";
    public static final String CODICE_AUT = "AUT";
    public static final String CODICE_AUT_AREE = "AUT_AREE";
    public static final String CODICE_RIM = "RIM";
    public static final String CODICE_COF = "COF";
    public static final String CODICE_FIN = "FIN";
    public static final String CODICE_ATT_COM = "ATT_COM";
    public static final String CODICE_ATT_COMM_TAR = "ATT_COMM_TAR";
    public static final String CODICE_DON = "DON";
    public static final String CODICE_ATT_COMM_SUB = "ATT_COMM_SUB";

    public final static Dictionary codiceKeys;

    static {
        codiceKeys = new it.cnr.jada.util.OrderedHashtable();
        codiceKeys.put(CODICE_FOE, "FOE");
        codiceKeys.put(CODICE_FOE_PRO, "FOE Progetti");
        codiceKeys.put(CODICE_AUT, "Autofinanziamento");
        codiceKeys.put(CODICE_AUT_AREE, "Autofinanziamento AREE");
        codiceKeys.put(CODICE_RIM, "Rimborsi da soggetti terzi");
        codiceKeys.put(CODICE_COF, "Cofinanziamento");
        codiceKeys.put(CODICE_FIN, "Finanziamento");
        codiceKeys.put(CODICE_ATT_COM, "Attività Commerciale pura");
        codiceKeys.put(CODICE_ATT_COMM_TAR, "Attività commerciale a tariffario");
        codiceKeys.put(CODICE_ATT_COMM_SUB, "Attività commerciale (CNR sub contraente)");
        codiceKeys.put(CODICE_DON, "Donazioni");
    };

    /**
     * Created by BulkGenerator 2.0 [07/12/2009]
     * Table name: TIPO_FINANZIAMENTO
     **/
    public TipoFinanziamentoBulk() {
        super();
    }

    /**
     * Created by BulkGenerator 2.0 [07/12/2009]
     * Table name: TIPO_FINANZIAMENTO
     **/
    public TipoFinanziamentoBulk(Long id) {
        super(id);
    }

    @Override
    public OggettoBulk initializeForInsert(CRUDBP crudbp, ActionContext actioncontext) {
        setFlPianoEcoFin(Boolean.FALSE);
        setFlAssCatVociInd(Boolean.FALSE);
        setFlAssCatVociDet(Boolean.FALSE);
        setFlAssCatVociAltro(Boolean.FALSE);
        setFlPrevEntSpesa(Boolean.FALSE);
        setFlRipCostiPers(Boolean.FALSE);
        setFlQuadPdgpEconom(Boolean.FALSE);
        setFlContrValProg(Boolean.FALSE);
        setFlPianoRend(Boolean.FALSE);
        setFlVarCons(Boolean.FALSE);
        setFlIncCons(Boolean.FALSE);
        setFlAllPrevFin(Boolean.FALSE);
        setFlQuadraContratto(Boolean.FALSE);
        setFlAssociaContratto(Boolean.FALSE);
        setFlValidazioneAutomatica(Boolean.FALSE);
        return super.initializeForInsert(crudbp, actioncontext);
    }

    @Override
    public OggettoBulk initializeForSearch(CRUDBP crudbp, ActionContext actioncontext) {
        return super.initializeForSearch(crudbp, actioncontext);
    }

    public Dictionary getCodiceKeys() {
        return codiceKeys;
    }
}