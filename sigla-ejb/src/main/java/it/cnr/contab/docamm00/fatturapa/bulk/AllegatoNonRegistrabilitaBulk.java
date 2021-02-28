/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.docamm00.fatturapa.bulk;

import it.cnr.contab.docamm00.storage.StorageDocAmmAspect;
import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;

import java.util.Date;

public class AllegatoNonRegistrabilitaBulk extends AllegatoFatturaBulk {
    @StoragePolicy(
            name = "P:sigla_commons_aspect:utente_applicativo_sigla",
            property = @StorageProperty(name = "sigla_commons_aspect:utente_applicativo")
    )
    private String utenteSIGLA;
    @StoragePolicy(
            name = "P:sigla_commons_aspect:protocollo",
            property = @StorageProperty(name = "sigla_commons_aspect:anno_protocollo")
    )
    private Integer annoProtocollo;
    @StoragePolicy(
            name = "P:sigla_commons_aspect:protocollo",
            property = @StorageProperty(name = "sigla_commons_aspect:numero_protocollo")
    )
    private String numeroProtocollo;
    @StoragePolicy(
            name = "P:sigla_commons_aspect:protocollo",
            property = @StorageProperty(name = "sigla_commons_aspect:data_protocollo")
    )
    private Date dataProtocollo;

    public AllegatoNonRegistrabilitaBulk() {
        super();
    }

    public AllegatoNonRegistrabilitaBulk(String storageKey) {
        super(storageKey);
    }

    @Override
    public String getAspectName() {
        return StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_COMUNICAZIONE_NON_REGISTRABILITA.value();
    }

    public String getUtenteSIGLA() {
        return utenteSIGLA;
    }

    public void setUtenteSIGLA(String utenteSIGLA) {
        this.utenteSIGLA = utenteSIGLA;
    }

    public Integer getAnnoProtocollo() {
        return annoProtocollo;
    }

    public void setAnnoProtocollo(Integer annoProtocollo) {
        this.annoProtocollo = annoProtocollo;
    }

    public String getNumeroProtocollo() {
        return numeroProtocollo;
    }

    public void setNumeroProtocollo(String numeroProtocollo) {
        this.numeroProtocollo = numeroProtocollo;
    }

    public Date getDataProtocollo() {
        return dataProtocollo;
    }

    public void setDataProtocollo(Date dataProtocollo) {
        this.dataProtocollo = dataProtocollo;
    }
}
