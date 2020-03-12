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

package it.cnr.contab.utenze00.comp;

import it.cnr.contab.utenze00.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.siopeplus.StMotivoEsclusioneCigSiope;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe che ridefinisce alcune operazioni di CRUD su CdsBulk e Unita_organizzativaBulk
 */

public class AssBpAccessoComponent extends it.cnr.jada.comp.CRUDComponent {
    public AssBpAccessoComponent() {

    }

    public java.util.List findAccessoByBP(UserContext userContext, String bp) throws ComponentException {
        try {
            AssBpAccessoHome home = (AssBpAccessoHome) getHome(userContext, AssBpAccessoBulk.class);
            SQLBuilder sql = (SQLBuilder) super.select(userContext, null, new AssBpAccessoBulk());
            sql.addSQLClause("AND", "business_process", sql.EQUALS, bp);
            List lista = home.fetchAll(sql);
            if (lista != null && !lista.isEmpty()) {
                for (Iterator<Object> i = lista.iterator(); i.hasNext(); ) {
                    AssBpAccessoBulk accesso = (AssBpAccessoBulk) i.next();
                    AccessoHome accessoHome = (AccessoHome) getHome(userContext, AccessoBulk.class);
                    AccessoBulk accessoBulk = (AccessoBulk) accessoHome.findByPrimaryKey(new AccessoBulk(accesso.getCdAccesso()));
                    accesso.setAccesso(accessoBulk);
                }
            }
            return lista;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw new ComponentException(e);
        }
    }

    public AssBpAccessoBulk finAssBpAccesso(UserContext userContext, String businessProcess, String tiFunzione) throws ComponentException {
        try {
            AssBpAccessoHome home = (AssBpAccessoHome) getHome(userContext, AssBpAccessoBulk.class, "default");
            SQLBuilder sql = home.createSQLBuilder();
            sql.addClause(FindClause.AND, "businessProcess", SQLBuilder.EQUALS, businessProcess);
            sql.addClause(FindClause.AND, "tiFunzione", Optional.ofNullable(tiFunzione)
                    .filter(s -> s.equals("C"))
                    .map(s -> SQLBuilder.ISNULL)
                    .orElse(SQLBuilder.EQUALS), tiFunzione);
            List<AssBpAccessoBulk> result = home.fetchAll(sql);
            getHomeCache(userContext).fetchAll(userContext);
            return Optional.ofNullable(result)
                    .map(s -> s.stream())
                    .map(assBpAccessoBulkStream -> assBpAccessoBulkStream.findFirst())
                    .orElse(Optional.empty()).orElse(null);
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw new ComponentException(e);
        }
    }

    public Map<String, String> findDescrizioneBP(UserContext userContext) throws ComponentException {
        try {
            AssBpAccessoHome home = (AssBpAccessoHome) getHome(userContext, AssBpAccessoBulk.class, "default");
            SQLBuilder sql = home.createSQLBuilder();
            sql.openParenthesis(FindClause.AND);
                sql.addClause(FindClause.AND, "tiFunzione", SQLBuilder.NOT_EQUALS, "V");
                sql.addClause(FindClause.OR, "tiFunzione", SQLBuilder.ISNULL, null);
            sql.closeParenthesis();
            List<AssBpAccessoBulk> result = home.fetchAll(sql);
            getHomeCache(userContext).fetchAll(userContext);
            return result.stream()
                    .collect(Collectors.toMap(
                            assBpAccessoBulk -> assBpAccessoBulk.getBusinessProcess(),
                            assBpAccessoBulk -> Optional.ofNullable(assBpAccessoBulk.getAccesso()).map(AccessoBase::getDs_accesso).orElse(assBpAccessoBulk.getBusinessProcess()),
                            (oldValue, newValue) -> oldValue,
                            Hashtable::new
                    ));
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw new ComponentException(e);
        }
    }
}
