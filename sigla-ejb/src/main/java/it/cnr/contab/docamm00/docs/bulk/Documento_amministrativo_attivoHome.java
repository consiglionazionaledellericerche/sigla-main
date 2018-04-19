package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

/**
 * Insert the type's description here.
 * Creation date: (3/22/2002 1:10:24 PM)
 *
 * @author: Roberto Peli
 */
public class Documento_amministrativo_attivoHome extends Fattura_attivaHome {
    /**
     * Documento_amministrativo_passivoHome constructor comment.
     *
     * @param clazz      java.lang.Class
     * @param connection java.sql.Connection
     */
    protected Documento_amministrativo_attivoHome(Class clazz, java.sql.Connection connection) {
        super(clazz, connection);
    }

    /**
     * Documento_amministrativo_passivoHome constructor comment.
     *
     * @param clazz           java.lang.Class
     * @param connection      java.sql.Connection
     * @param persistentCache it.cnr.jada.persistency.PersistentCache
     */
    protected Documento_amministrativo_attivoHome(Class clazz, java.sql.Connection connection, it.cnr.jada.persistency.PersistentCache persistentCache) {
        super(clazz, connection, persistentCache);
    }

    /**
     * Documento_amministrativo_passivoHome constructor comment.
     *
     * @param conn java.sql.Connection
     */
    public Documento_amministrativo_attivoHome(java.sql.Connection conn) {
        super(conn);
    }

    /**
     * Documento_amministrativo_passivoHome constructor comment.
     *
     * @param conn            java.sql.Connection
     * @param persistentCache it.cnr.jada.persistency.PersistentCache
     */
    public Documento_amministrativo_attivoHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
        super(conn, persistentCache);
    }

    public SQLBuilder selectByClauseForFattureAttiveDaFirmare(UserContext context, Documento_amministrativo_attivoBulk oggettobulk, CompoundFindClause compoundfindclause) {
        SQLBuilder sqlBuilder = super.createSQLBuilder();
        sqlBuilder.addClause("AND", "esercizio", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(context));
        sqlBuilder.addClause("AND", "cd_cds_origine", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(context));
        sqlBuilder.addClause("AND", "cd_uo_origine", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(context));
        sqlBuilder.addClause("AND", "codiceUnivocoUfficioIpa", SQLBuilder.ISNOTNULL, null);
        sqlBuilder.addClause("AND", "statoInvioSdi", SQLBuilder.EQUALS, Fattura_attivaBulk.FATT_ELETT_ALLA_FIRMA);
        sqlBuilder.addClause("AND", "stato_cofi", SQLBuilder.NOT_EQUALS, it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk.STATO_ANNULLATO);
        return sqlBuilder;
    }
}
