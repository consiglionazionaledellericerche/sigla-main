/*
 * Created by Generator 1.0
 * Date 27/04/2006
 */
package it.cnr.contab.doccont00.consultazioni.bulk;

import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.core.bulk.ReversaleBulk;
import it.cnr.contab.util.enumeration.EsitoOperazione;
import it.cnr.jada.util.OrderedHashtable;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Collectors;

public class V_cons_stato_invio_reversaliBulk extends V_cons_stato_invio_reversaliBase implements V_cons_stato_invio {
    public final static Map<String,String> esito_OperazioneKeys = Arrays.asList(EsitoOperazione.values())
            .stream()
            .collect(Collectors.toMap(
                    EsitoOperazione::value,
                    EsitoOperazione::label,
                    (oldValue, newValue) -> oldValue,
                    Hashtable::new
            ));

    private String contabile;
    // ERRORE_SIOPE_PLUS VARCHAR2(2000)
    private java.lang.String erroreSiopePlus;

    public V_cons_stato_invio_reversaliBulk() {
        super();
    }

    public Long getProgressivo() {
        return getPg_reversale();
    }

    public String getTipo() {
        return Numerazione_doc_contBulk.TIPO_REV;
    }

    public String getContabile() {
        return contabile;
    }

    public void setContabile(String contabile) {
        this.contabile = contabile;
    }
}