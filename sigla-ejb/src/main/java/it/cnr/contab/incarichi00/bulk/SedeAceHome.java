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

package it.cnr.contab.incarichi00.bulk;


import it.cnr.contab.anagraf00.tabter.bulk.CapBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoComunicaDatiBulk;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk;
import it.cnr.contab.missioni00.docs.bulk.MissioneBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.si.service.AceService;
import it.cnr.si.service.dto.anagrafica.simpleweb.SimpleEntitaOrganizzativaWebDto;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class SedeAceHome extends BulkHome {

    public SedeAceHome(Class clazz, java.sql.Connection conn)
    {
        super(clazz, conn);
    }

    public SedeAceHome(Class clazz, java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
        super(clazz, conn, persistentCache);
    }

    public SedeAceHome(java.sql.Connection conn) {
        super(SedeAceBulk.class, conn);
    }

    public SedeAceHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
        super(SedeAceBulk.class, conn, persistentCache);
    }
    public java.util.Collection cercaSedi(String uo) throws IntrospectionException, PersistencyException {
        if(uo != null){
            List lista = null;
            try {
                AceService aceService = SpringUtil.getBean("aceService", AceService.class);
                List<SimpleEntitaOrganizzativaWebDto> sedi = aceService.entitaOrganizzativaFind(100, uo, LocalDate.now(), null);
                for (SimpleEntitaOrganizzativaWebDto sede : sedi){
                    SedeAceBulk sedeAceBulk = new SedeAceBulk();
                    sedeAceBulk.setCdsuo(sede.getCdsuo());
                    sedeAceBulk.setId(sede.getId());
                    sedeAceBulk.setSigla(sede.getSigla());
                    sedeAceBulk.setIdnsip(sede.getIdnsip());
                    lista.add(sedeAceBulk);
                }
            } catch (NoSuchBeanDefinitionException _ex) {
                int a =0;
            }
            return lista;
        }
        return null;
    }

}
