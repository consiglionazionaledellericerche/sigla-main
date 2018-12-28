/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 20/12/2018
 */
package it.cnr.contab.siope.plus.bulk;

import it.cnr.contab.model.Esito;
import it.cnr.contab.model.Risultato;
import it.cnr.contab.service.OrdinativiSiopePlusService;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

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

    public SIOPEPlusRisultatoBulk(String esito, Risultato risultato) {
        super();
        setEsito(esito);
        setProg_flusso(Optional.ofNullable(risultato.getProgFlusso()).orElse(null));
        setProg_esito_applicativo(Optional.ofNullable(risultato.getProgEsitoApplicativo()).orElse(null));
        setData_produzione(
                Optional.ofNullable(risultato.getDataProduzione())
                    .map(Date::getTime)
                    .map(aLong -> new Timestamp(aLong))
                    .orElse(null)
        );
        setData_upload(
                Optional.ofNullable(risultato.getDataUpload())
                .map(Date::getTime)
                .map(aLong -> new Timestamp(aLong))
                .orElse(null));
        setLocation(risultato.getLocation());
        setToBeCreated();
    }

    public Esito getEsitoEnum() {
        return Esito.valueOf(getEsito());
    }
}