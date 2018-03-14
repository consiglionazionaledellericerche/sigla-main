package it.cnr.contab.utenze00.comp;

import it.cnr.contab.utenze00.bulk.AccessoBulk;
import it.cnr.contab.utenze00.bulk.AccessoHome;
import it.cnr.contab.utenze00.bulk.AssBpAccessoBulk;
import it.cnr.contab.utenze00.bulk.AssBpAccessoHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

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
}
