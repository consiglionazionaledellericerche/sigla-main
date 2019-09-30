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
 * Date 20/12/2018
 */
package it.cnr.contab.siope.plus.bulk;

import it.cnr.si.siopeplus.model.Esito;
import it.cnr.si.siopeplus.model.Risultato;

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