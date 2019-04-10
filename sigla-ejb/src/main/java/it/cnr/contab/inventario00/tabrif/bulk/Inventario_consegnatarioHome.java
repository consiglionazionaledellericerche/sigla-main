package it.cnr.contab.inventario00.tabrif.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.Broker;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Inventario_consegnatarioHome extends BulkHome {
    public Inventario_consegnatarioHome(java.sql.Connection conn) {
        super(Inventario_consegnatarioBulk.class, conn);
    }

    public Inventario_consegnatarioHome(java.sql.Connection conn, PersistentCache persistentCache) {
        super(Inventario_consegnatarioBulk.class, conn, persistentCache);
    }

    public void checkInserimentoSuccessivo(Inventario_consegnatarioBulk consegnatario) throws PersistencyException {
        LoggableStatement ps = null;
        try {
            ps = new LoggableStatement(getConnection(),
                    "UPDATE " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "INVENTARIO_CONSEGNATARIO SET DT_FINE_VALIDITA=? -1 WHERE PG_INVENTARIO=? AND DT_FINE_VALIDITA IS NULL",
                    true, this.getClass());
            ps.setTimestamp(1, consegnatario.getDt_inizio_validita());
            ps.setLong(2, consegnatario.getPg_inventario().longValue());

            ps.executeQuery();

        } catch (java.sql.SQLException e) {
            throw new PersistencyException(e);
        } finally {
            if (ps != null)
                try {
                    ps.close();
                } catch (java.sql.SQLException e) {
                }
        }

    }
//^^@@

    /**
     * Carico il Primo Consegnatario/Delegato registrato per l'Inventario selezionato.
     * (dalla tabella INVENTARIO_CONSEGNATARIO)
     */
//^^@@
    public Inventario_consegnatarioBulk findFirstInventarioConsegnatarioFor(Id_inventarioBulk inv)
            throws PersistencyException, IntrospectionException {

        SQLBuilder sql = createSQLBuilder();
        Inventario_consegnatarioBulk invC = null;
        String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();

        String subQuery = "(SELECT MIN(DT_INIZIO_VALIDITA) " +
                "FROM " + schema + "INVENTARIO_CONSEGNATARIO " +
                "WHERE PG_INVENTARIO = " + inv.getPg_inventario() + ")";

        sql.addSQLClause("AND", "PG_INVENTARIO", SQLBuilder.EQUALS, inv.getPg_inventario());
        sql.addOrderBy("DT_INIZIO_VALIDITA");
        sql.addSQLClause("AND", "DT_INIZIO_VALIDITA = " + subQuery);
        Broker broker = createBroker(sql);
        if (broker.next()) {
            invC = (Inventario_consegnatarioBulk) fetch(broker);
        }
        broker.close();

        return invC;
    }
//^^@@

    /**
     * Carico il Consegnatario/Delegato dell'Inventario selezionato
     * (dalla tabella INVENTARIO_CONSEGNATARIO)
     */
//^^@@
    public Inventario_consegnatarioBulk findInventarioConsegnatarioFor(Id_inventarioBulk inv) throws PersistencyException, IntrospectionException {

        SQLBuilder sql = createSQLBuilder();
        Inventario_consegnatarioBulk invC = null;

        sql.addSQLClause("AND", "PG_INVENTARIO", SQLBuilder.EQUALS, inv.getPg_inventario());
        sql.addOrderBy("DT_INIZIO_VALIDITA DESC");

        Broker broker = createBroker(sql);
        if (broker.next()) {
            invC = (Inventario_consegnatarioBulk) fetch(broker);
        }
        broker.close();

        return invC;
    }
//^^@@

    /**
     * Carico il Consegnatario/Delegato dell'Inventario selezionato
     * (dalla tabella INVENTARIO_CONSEGNATARIO)
     */
//^^@@
    public Inventario_consegnatarioBulk findInventarioConsegnatarioFor(Id_inventarioBulk inv, java.util.Date dtIni) throws PersistencyException, IntrospectionException {

        SQLBuilder sql = createSQLBuilder();
        Inventario_consegnatarioBulk invC = null;

        sql.addSQLClause("AND", "PG_INVENTARIO", SQLBuilder.EQUALS, inv.getPg_inventario());
        sql.addSQLClause("AND", "DT_INIZIO_VALIDITA", SQLBuilder.LESS_EQUALS, dtIni);
        sql.addSQLClause("AND", "DT_FINE_VALIDITA", SQLBuilder.GREATER_EQUALS, dtIni);

        Broker broker = createBroker(sql);
        if (broker.next()) {
            invC = (Inventario_consegnatarioBulk) fetch(broker);
        }
        broker.close();

        return invC;
    }
//^^@@

    /**
     * Carico il Consegnatario/Delegato dell'Inventario selezionato
     * (dalla tabella INVENTARIO_CONSEGNATARIO)
     * Se non è presente, inizializzo un nuovo oggetto settando
     * come Consegnatario il responsabile della UO di scrivania
     */
//^^@@
    public Inventario_consegnatarioBulk findInventarioConsegnatarioFor(it.cnr.jada.UserContext aUC, Id_inventarioBulk inv) throws PersistencyException, IntrospectionException {

        Inventario_consegnatarioBulk invC = findInventarioConsegnatarioFor(inv);
        if (invC == null) {
            invC = new Inventario_consegnatarioBulk();
            invC.setInventario(inv);
            invC.setConsegnatario(inv.getUoResp().getResponsabile());
            invC.setUser(aUC.getUser());
        }

        return invC;
    }
}
