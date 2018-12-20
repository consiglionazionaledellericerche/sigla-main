/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 20/12/2018
 */
package it.cnr.contab.siope.plus.bulk;

import it.cnr.contab.model.Esito;
import it.cnr.contab.service.OrdinativiSiopePlusService;

public class SIOPEPlusRisultatoBulk extends SIOPEPlusRisultatoBase {
    /**
     * Created by BulkGenerator 2.0 [07/12/2009]
     * Table name: SIOPE_PLUS_RISULTATO
     **/
    public SIOPEPlusRisultatoBulk() {
        super();
    }

    /**
     * Created by BulkGenerator 2.0 [07/12/2009]
     * Table name: SIOPE_PLUS_RISULTATO
     **/
    public SIOPEPlusRisultatoBulk(Long id) {
        super(id);
    }

    public SIOPEPlusRisultatoBulk(String esito, String location) {
        super();
        setEsito(esito);
        setLocation(location);
        setToBeCreated();
    }

    public Esito getEsitoEnum() {
        return Esito.valueOf(getEsito());
    }
}