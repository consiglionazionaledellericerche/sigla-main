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
        setFlPianoEcoFin(null);
        setFlAssCatVociInd(null);
        setFlAssCatVociDet(null);
        setFlAssCatVociAltro(null);
        setFlPrevEntSpesa(null);
        setFlRipCostiPers(null);
        setFlQuadPdgpEconom(null);
        setFlContrValProg(null);
        setFlPianoRend(null);
        setFlVarCons(null);
        setFlIncCons(null);
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