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

package it.cnr.contab.coepcoan00.core.bulk;

import it.cnr.contab.docamm00.docs.bulk.TipoDocumentoEnum;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.ObjectNotFoundException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Scrittura_partita_doppiaHome extends BulkHome {
    public Scrittura_partita_doppiaHome(java.sql.Connection conn) {
        super(Scrittura_partita_doppiaBulk.class, conn);
    }

    public Scrittura_partita_doppiaHome(java.sql.Connection conn, PersistentCache persistentCache) {
        super(Scrittura_partita_doppiaBulk.class, conn, persistentCache);
    }

    public Collection<Movimento_cogeBulk> findMovimentiAvereColl(UserContext userContext, Scrittura_partita_doppiaBulk scrittura) throws PersistencyException {
        return this.findMovimentiAvereColl(userContext, scrittura, true);
    }

    public Collection<Movimento_cogeBulk> findMovimentiAvereColl(UserContext userContext, Scrittura_partita_doppiaBulk scrittura, boolean fetchAll) throws PersistencyException {
        final PersistentHome home = getHomeCache().getHome(Movimento_cogeBulk.class, "default", "documentoAmministrativo");
        SQLBuilder sql = home.createSQLBuilder();
        sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, scrittura.getEsercizio());
        sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS, scrittura.getCd_cds());
        sql.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, scrittura.getCd_unita_organizzativa());
        sql.addClause("AND", "pg_scrittura", SQLBuilder.EQUALS, scrittura.getPg_scrittura());
        sql.addClause("AND", "sezione", SQLBuilder.EQUALS, Movimento_cogeBulk.SEZIONE_AVERE);
        List<Movimento_cogeBulk> result = home.fetchAll(sql);
        if (fetchAll) getHomeCache().fetchAll(userContext);
        return result;
    }

    public Collection<Movimento_cogeBulk> findMovimentiDareColl(UserContext userContext, Scrittura_partita_doppiaBulk scrittura) throws PersistencyException {
        return this.findMovimentiDareColl(userContext, scrittura, true);
    }

    public Collection<Movimento_cogeBulk> findMovimentiDareColl(UserContext userContext, Scrittura_partita_doppiaBulk scrittura, boolean fetchAll) throws PersistencyException {
        final PersistentHome home = getHomeCache().getHome(Movimento_cogeBulk.class, "default", "documentoAmministrativo");
        SQLBuilder sql = home.createSQLBuilder();
        sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, scrittura.getEsercizio());
        sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS, scrittura.getCd_cds());
        sql.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, scrittura.getCd_unita_organizzativa());
        sql.addClause("AND", "pg_scrittura", SQLBuilder.EQUALS, scrittura.getPg_scrittura());
        sql.addClause("AND", "sezione", SQLBuilder.EQUALS, Movimento_cogeBulk.SEZIONE_DARE);
        List<Movimento_cogeBulk> result = home.fetchAll(sql);
        if (fetchAll) getHomeCache().fetchAll(userContext);
        return result;
    }

    /**
     *
     * @param documentoCogeBulk documentoCogeBulk
     * @return la prima scrittura generata dal documento
     * @throws PersistencyException PersistencyException
     */
    public Optional<Scrittura_partita_doppiaBulk> findByDocumentoAmministrativo(IDocumentoCogeBulk documentoCogeBulk) throws PersistencyException {
        return findByDocumentoCoge(documentoCogeBulk)
                .stream().min(Comparator.comparing(Scrittura_partita_doppiaKey::getPg_scrittura));
    }

	public List<Scrittura_partita_doppiaBulk> findByDocumentoCoge(IDocumentoCogeBulk documentoCogeBulk) throws PersistencyException {
        SQLBuilder sql = this.createSQLBuilder();
        sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, documentoCogeBulk.getEsercizio());
        sql.addClause("AND", "cd_cds_documento", SQLBuilder.EQUALS, documentoCogeBulk.getCd_cds());
        sql.addClause("AND", "cd_uo_documento", SQLBuilder.EQUALS, documentoCogeBulk.getCd_uo());
        sql.addClause("AND", "pg_numero_documento", SQLBuilder.EQUALS, documentoCogeBulk.getPg_doc());
        sql.addClause("AND", "cd_tipo_documento", SQLBuilder.EQUALS, documentoCogeBulk.getCd_tipo_doc());
        return fetchAll(sql);
    }

    /**
     * Imposta il pg_scrittura di un oggetto <code>Scrittura_partita_doppiaBulk</code>.
     *
     * @param bulk <code>OggettoBulk</code>
     * @throws ComponentException ComponentException
     */

    public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext, OggettoBulk bulk) throws ComponentException {
        try {
            Scrittura_partita_doppiaBulk scrittura = (Scrittura_partita_doppiaBulk) bulk;

            if (scrittura.getPg_scrittura()==null) {
                LoggableStatement cs = new LoggableStatement(getConnection(),
                        "{ ? = call " +
                                it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                                "CNRCTB200.getNextProgressivo(?, ?, ?, ?, ?)}", false, this.getClass());
                try {
                    cs.registerOutParameter(1, java.sql.Types.NUMERIC);
                    cs.setObject(2, scrittura.getEsercizio());
                    cs.setString(3, scrittura.getCd_cds());
                    cs.setString(4, scrittura.getCd_unita_organizzativa());
                    cs.setString(5, Scrittura_partita_doppiaBulk.TIPO_COGE);
                    cs.setString(6, scrittura.getUser());
                    cs.executeQuery();

                    Long result = cs.getLong(1);
                    scrittura.setPg_scrittura(result);
                } catch (java.lang.Exception e) {
                    throw new ComponentException(e);
                } finally {
                    cs.close();
                }
            }
        } catch (java.lang.Exception e) {
            throw new ComponentException(e);
        }
    }

    public Optional<Scrittura_partita_doppiaBulk> getScrittura(UserContext userContext, IDocumentoCogeBulk documentoCogeBulk, boolean fetchAll) throws ComponentException {
        try {
            Optional<Scrittura_partita_doppiaBulk> scritturaOpt = Optional.empty();
            if (Utility.createConfigurazioneCnrComponentSession().isAttivaEconomica(userContext)) {
                scritturaOpt = this.findByDocumentoAmministrativo(documentoCogeBulk);
                if (scritturaOpt.isPresent()) {
                    Scrittura_partita_doppiaBulk scrittura = scritturaOpt.get();
                    scrittura.setMovimentiDareColl(new BulkList(this.findMovimentiDareColl(userContext, scrittura, fetchAll)));
                    scrittura.setMovimentiAvereColl(new BulkList(this.findMovimentiAvereColl(userContext, scrittura, fetchAll)));
                }
            }
            return scritturaOpt;
        } catch (PersistencyException | RemoteException e) {
            throw new ComponentException(e);
        }
    }

    public Optional<Scrittura_partita_doppiaBulk> getScrittura(UserContext userContext, IDocumentoCogeBulk documentoCogeBulk) throws ComponentException {
        return this.getScrittura(userContext, documentoCogeBulk, Boolean.TRUE);
    }

    @Override
    public void handleObjectNotFoundException(ObjectNotFoundException objectnotfoundexception) throws ObjectNotFoundException {
    }

}
