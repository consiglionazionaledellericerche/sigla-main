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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoHome;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.ejb.NumTempDocContComponentSession;
import it.cnr.contab.pdg00.bulk.Pdg_preventivo_detBulk;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_entrate_gestBulk;
import it.cnr.contab.prevent00.bulk.Pdg_vincoloBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.OrderConstants;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class AccertamentoHome extends BulkHome {
    public AccertamentoHome(Class clazz, java.sql.Connection conn) {
        super(clazz, conn);
    }

    public AccertamentoHome(Class clazz, java.sql.Connection conn, PersistentCache persistentCache) {
        super(clazz, conn, persistentCache);
    }

    /**
     * <!-- @TODO: da completare -->
     * Costruisce un AccertamentoHome
     *
     * @param conn La java.sql.Connection su cui vengono effettuate le operazione di persistenza
     */
    public AccertamentoHome(java.sql.Connection conn) {
        super(AccertamentoBulk.class, conn);
    }

    /**
     * <!-- @TODO: da completare -->
     * Costruisce un AccertamentoHome
     *
     * @param conn            La java.sql.Connection su cui vengono effettuate le operazione di persistenza
     * @param persistentCache La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
     */
    public AccertamentoHome(java.sql.Connection conn, PersistentCache persistentCache) {
        super(AccertamentoBulk.class, conn, persistentCache);
    }

    /**
     * <!-- @TODO: da completare -->
     *
     * @param accertamentoTemporaneo
     * @param pg
     * @throws IntrospectionException
     * @throws PersistencyException
     */
    public void confirmAccertamentoTemporaneo(
            UserContext userContext,
            AccertamentoBulk accertamentoTemporaneo,
            Long pg)
            throws IntrospectionException, PersistencyException {

        if (pg == null)
            throw new PersistencyException("Impossibile ottenere un progressivo definitivo per l'impegno inserito!");

        LoggableStatement ps = null;
        java.io.StringWriter sql = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sql);
        String condition = " WHERE CD_CDS = ? AND ESERCIZIO = ? AND ESERCIZIO_ORIGINALE = ? AND PG_ACCERTAMENTO = ?";

        try {
            pw.write("INSERT INTO " + EJBCommonServices.getDefaultSchema() + getColumnMap().getTableName() + " (");
            pw.write(getPersistenColumnNamesReplacingWith(this, null).toString());
            pw.write(") SELECT ");
            pw.write(getPersistenColumnNamesReplacingWith(
                    this,
                    new String[][]{{"PG_ACCERTAMENTO", "?"}}).toString());
            pw.write(" FROM " + EJBCommonServices.getDefaultSchema() + getColumnMap().getTableName());
            pw.write(condition);
            pw.flush();

            ps = new LoggableStatement(getConnection(), sql.toString(), true, this.getClass());
            pw.close();
            ps.setLong(1, pg.longValue());
            ps.setString(2, accertamentoTemporaneo.getCd_cds());
            ps.setInt(3, accertamentoTemporaneo.getEsercizio().intValue());
            ps.setInt(4, accertamentoTemporaneo.getEsercizio_originale().intValue());
            ps.setLong(5, accertamentoTemporaneo.getPg_accertamento().longValue());

            ps.execute();
        } catch (java.sql.SQLException e) {
            throw new PersistencyException(e);
        } finally {
            try {
                ps.close();
            } catch (java.sql.SQLException e) {
            }
            ;
        }

        try {
            sql = new java.io.StringWriter();
            pw = new java.io.PrintWriter(sql);
            Accertamento_scadenzarioHome scadHome = (Accertamento_scadenzarioHome) getHomeCache().getHome(Accertamento_scadenzarioBulk.class);
            pw.write("INSERT INTO " + EJBCommonServices.getDefaultSchema() + scadHome.getColumnMap().getTableName() + " (");
            pw.write(getPersistenColumnNamesReplacingWith(scadHome, null).toString());
            pw.write(") SELECT ");
            pw.write(getPersistenColumnNamesReplacingWith(
                    scadHome,
                    new String[][]{{"PG_ACCERTAMENTO", "?"}}).toString());
            pw.write(" FROM " + EJBCommonServices.getDefaultSchema() + scadHome.getColumnMap().getTableName());
            pw.write(condition);
            pw.flush();
            ps = new LoggableStatement(getConnection(), sql.toString(), true, this.getClass());
            pw.close();
            ps.setLong(1, pg.longValue());
            ps.setString(2, accertamentoTemporaneo.getCd_cds());
            ps.setInt(3, accertamentoTemporaneo.getEsercizio().intValue());
            ps.setInt(4, accertamentoTemporaneo.getEsercizio_originale().intValue());
            ps.setLong(5, accertamentoTemporaneo.getPg_accertamento().longValue());

            ps.execute();
        } catch (java.sql.SQLException e) {
            throw new PersistencyException(e);
        } finally {
            try {
                ps.close();
            } catch (java.sql.SQLException e) {
            }
            ;
        }

        try {
            sql = new java.io.StringWriter();
            pw = new java.io.PrintWriter(sql);
            Accertamento_scad_voceHome scadVoceHome = (Accertamento_scad_voceHome) getHomeCache().getHome(Accertamento_scad_voceBulk.class);
            pw.write("INSERT INTO " + EJBCommonServices.getDefaultSchema() + scadVoceHome.getColumnMap().getTableName() + " (");
            pw.write(getPersistenColumnNamesReplacingWith(scadVoceHome, null).toString());
            pw.write(") SELECT ");
            pw.write(getPersistenColumnNamesReplacingWith(
                    scadVoceHome,
                    new String[][]{{"PG_ACCERTAMENTO", "?"}}).toString());
            pw.write(" FROM " + EJBCommonServices.getDefaultSchema() + scadVoceHome.getColumnMap().getTableName());
            pw.write(condition);
            pw.flush();
            ps = new LoggableStatement(getConnection(), sql.toString(), true, this.getClass());
            pw.close();
            ps.setLong(1, pg.longValue());
            ps.setString(2, accertamentoTemporaneo.getCd_cds());
            ps.setInt(3, accertamentoTemporaneo.getEsercizio().intValue());
            ps.setInt(4, accertamentoTemporaneo.getEsercizio_originale().intValue());
            ps.setLong(5, accertamentoTemporaneo.getPg_accertamento().longValue());

            ps.execute();
        } catch (java.sql.SQLException e) {
            throw new PersistencyException(e);
        } finally {
            try {
                ps.close();
            } catch (java.sql.SQLException e) {
            }
            ;
        }

        delete(accertamentoTemporaneo, userContext);

        accertamentoTemporaneo.setPg_accertamento(pg);
    }

    /**
     * <!-- @TODO: da completare -->
     *
     * @param accertamento
     * @return
     * @throws IntrospectionException
     * @throws PersistencyException
     */
    public java.util.List findAccertamento_scadenzarioList(AccertamentoBulk accertamento) throws IntrospectionException, PersistencyException {
        PersistentHome asHome = getHomeCache().getHome(Accertamento_scadenzarioBulk.class);
        SQLBuilder sql = asHome.createSQLBuilder();
        sql.addClause("AND", "cd_cds", sql.EQUALS, accertamento.getCd_cds());
        sql.addClause("AND", "esercizio", sql.EQUALS, accertamento.getEsercizio());
        sql.addClause("AND", "esercizio_originale", sql.EQUALS, accertamento.getEsercizio_originale());
        sql.addClause("AND", "pg_accertamento", sql.EQUALS, accertamento.getPg_accertamento());
        sql.addOrderBy("DT_SCADENZA_INCASSO");
        sql.addOrderBy("pg_accertamento_scadenzario");

        return asHome.fetchAll(sql);
    }

    /**
     * Dato l'oggetto AccertamentoBulk verifica il tipo
     * e ritorna l'oggetto AccertamentoResiduoBulk o AccertamentoOrdBulk
     *
     * @param obbligazione
     * @return
     * @throws PersistencyException
     * @throws IntrospectionException
     */
    public AccertamentoBulk findAccertamento(AccertamentoBulk accertamento) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {
        if (accertamento.isAccertamentoResiduo())
            return findAccertamentoRes(accertamento);
        else
            return findAccertamentoOrd(accertamento);
    }

    /**
     * Dato l'oggetto AccertamentoBulk ritorna l'oggetto AccertamentoCdsBulk
     *
     * @param accertamento
     * @return
     * @throws PersistencyException
     * @throws IntrospectionException
     */
    public AccertamentoCdsBulk findAccertamentoCds(AccertamentoBulk accertamento) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {
        return (AccertamentoCdsBulk) findByPrimaryKey(new AccertamentoCdsBulk(accertamento.getCd_cds(), accertamento.getEsercizio(), accertamento.getEsercizio_originale(), accertamento.getPg_accertamento()));
    }

    /**
     * Dato l'oggetto AccertamentoBulk ritorna l'oggetto AccertamentoResiduoBulk
     *
     * @param accertamento
     * @return AccertamentoResiduoBulk
     * @throws PersistencyException
     * @throws IntrospectionException
     */
    public AccertamentoResiduoBulk findAccertamentoRes(AccertamentoBulk accertamento) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {
        return (AccertamentoResiduoBulk) findByPrimaryKey(new AccertamentoResiduoBulk(accertamento.getCd_cds(), accertamento.getEsercizio(), accertamento.getEsercizio_originale(), accertamento.getPg_accertamento()));
    }

    /**
     * Dato l'oggetto AccertamentoBulk ritorna l'oggetto AccertamentoOrdBulk
     *
     * @param accertamento
     * @return
     * @throws PersistencyException
     * @throws IntrospectionException
     */
    public AccertamentoOrdBulk findAccertamentoOrd(AccertamentoBulk accertamento) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {
        return (AccertamentoOrdBulk) findByPrimaryKey(new AccertamentoOrdBulk(accertamento.getCd_cds(), accertamento.getEsercizio(), accertamento.getEsercizio_originale(), accertamento.getPg_accertamento()));
    }

    /**
     * <!-- @TODO: da completare -->
     *
     * @param accertamento
     * @return
     * @throws PersistencyException
     * @throws IntrospectionException
     */
    public AccertamentoPGiroBulk findAccertamentoPGiro(AccertamentoBulk accertamento) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {
        return (AccertamentoPGiroBulk) findByPrimaryKey(new AccertamentoPGiroBulk(accertamento.getCd_cds(), accertamento.getEsercizio(), accertamento.getEsercizio_originale(), accertamento.getPg_accertamento()));
    }

    /**
     * Lettura dalla tabella CDR di tutti i Centri di Responsabilita con
     * Unita Organizzativa uguale alla unita organizzativa origine dell'accertamento.
     * (Servira' per la gestione delle Linee di Attivita')
     *
     * @param accertamento
     * @return
     * @throws IntrospectionException
     * @throws PersistencyException
     * @throws ApplicationException
     */
    public java.util.List findCdr(AccertamentoBulk accertamento) throws IntrospectionException, PersistencyException, ApplicationException {
        if (accertamento.getCd_uo_origine() == null) //se in scrivania ho 999.000
            return new LinkedList();
        PersistentHome cdrHome = getHomeCache().getHome(CdrBulk.class, "V_CDR_VALIDO");
        SQLBuilder sql = cdrHome.createSQLBuilder();

        sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, accertamento.getEsercizio());
        sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", sql.EQUALS, accertamento.getCd_uo_origine());

        return cdrHome.fetchAll(sql);
    }

    /**
     * Cerco i codici natura del capitolo selezionato
     *
     * @param accertamento
     * @return
     * @throws IntrospectionException
     * @throws PersistencyException
     * @throws ApplicationException
     */
    public java.util.List findCodiciNatura(AccertamentoBulk accertamento) throws IntrospectionException, PersistencyException, ApplicationException {
        List coll = new Vector();

        PersistentHome assHome = getHomeCache().getHome(Ass_ev_evBulk.class);
        SQLBuilder sql = assHome.createSQLBuilder();

        sql.addClause("AND", "esercizio", sql.EQUALS, accertamento.getEsercizio());
        sql.addClause("AND", "cd_elemento_voce", sql.EQUALS, accertamento.getCapitolo().getCd_titolo_capitolo());
        sql.addClause("AND", "ti_appartenenza", sql.EQUALS, accertamento.getCapitolo().getTi_appartenenza());
        sql.addClause("AND", "ti_gestione", sql.EQUALS, accertamento.getCapitolo().getTi_gestione());

        coll = assHome.fetchAll(sql);

        if (coll.size() == 0) {
            return (new Vector());
        } else {
            return coll;
        }
    }

    /**
     * <!-- @TODO: da completare -->
     *
     * @param acc
     * @return
     * @throws PersistencyException
     */
    public Timestamp findDataUltimoAccertamentoPerCds(AccertamentoBulk acc) throws PersistencyException {
        try {
            LoggableStatement ps = new LoggableStatement(getConnection(),
                    "SELECT MAX(DT_REGISTRAZIONE) " +
                            "FROM " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "ACCERTAMENTO WHERE " +
                            "ESERCIZIO = ? AND CD_CDS = ? AND CD_TIPO_DOCUMENTO_CONT = ? AND FL_PGIRO = ?", true, this.getClass());
            try {
                ps.setObject(1, acc.getEsercizio());
                ps.setString(2, acc.getCd_cds());
                ps.setString(3, acc.getCd_tipo_documento_cont());
                if (acc.getFl_pgiro().booleanValue())
                    ps.setString(4, "Y");
                else
                    ps.setString(4, "N");


                ResultSet rs = ps.executeQuery();
                try {
                    if (rs.next())
                        return rs.getTimestamp(1);
                    else
                        return null;
                } catch (SQLException e) {
                    throw new PersistencyException(e);
                } finally {
                    try {
                        rs.close();
                    } catch (java.sql.SQLException e) {
                    }
                    ;
                }
            } catch (SQLException e) {
                throw new PersistencyException(e);
            } finally {
                try {
                    ps.close();
                } catch (java.sql.SQLException e) {
                }
                ;
            }
        } catch (SQLException e) {
            throw new PersistencyException(e);
        }
    }

    /**
     * Dato il codice di una Linea di Attivita ne ritorna l'oggetto
     *
     * @param accertamento
     * @param cdLineaAttivita
     * @return
     * @throws IntrospectionException
     * @throws PersistencyException
     * @throws ApplicationException
     */
    public it.cnr.contab.config00.latt.bulk.WorkpackageBulk findLineaAttivita(AccertamentoBulk accertamento, String cdLineaAttivita) throws IntrospectionException, PersistencyException, ApplicationException {
        PersistentHome laHome = getHomeCache().getHome(it.cnr.contab.config00.latt.bulk.WorkpackageBulk.class);
        SQLBuilder sql = laHome.createSQLBuilder();

        sql.setStatement("select A.* from " +
                it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "V_linea_attivita_VALIDA A, " +
                it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "cdr B " +
                " where A.esercizio = ?" +
                " and A.cd_linea_attivita = ?" +
                " and A.ti_gestione = ?" +
                " and B.cd_unita_organizzativa = ?" +
                " and A.cd_centro_responsabilita = B.cd_centro_responsabilita");

        sql.addParameter(accertamento.getEsercizio(), Types.NUMERIC, 1);
        sql.addParameter(cdLineaAttivita, Types.VARCHAR, 2);
        sql.addParameter(it.cnr.contab.config00.latt.bulk.WorkpackageBulk.TI_GESTIONE_ENTRATE, Types.VARCHAR, 3);
        sql.addParameter(accertamento.getCd_uo_origine(), Types.VARCHAR, 4);

        List listaLA = laHome.fetchAll(sql);
        if (listaLA.size() > 0)
            return ((it.cnr.contab.config00.latt.bulk.WorkpackageBulk) listaLA.get(0));

        return null;
    }

    /**
     * Recupero le Linee di attivita' con natura uguale a quelle del capitolo selezionato
     *
     * @param accertamento
     * @return List
     * @throws IntrospectionException
     * @throws PersistencyException
     * @throws ApplicationException
     */
/*
public java.util.List findLineeAttivita(AccertamentoBulk accertamento) throws IntrospectionException,PersistencyException, ApplicationException
{
	PersistentHome laHome = getHomeCache().getHome(it.cnr.contab.config00.latt.bulk.WorkpackageBulk.class,"V_LINEA_ATTIVITA_VALIDA","it.cnr.contab.doccont00.comp.AccertamentoComponent.find.linea_att");
	SQLBuilder sql = laHome.createSQLBuilder();

	sql.setStatement(	"select A.* from " + 
						it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "V_linea_attivita_VALIDA A, " + 
						it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "cdr B " + 
						" where A.esercizio = ?" +
						" and A.ti_gestione = ?" +						
						" and B.cd_unita_organizzativa = ?" +
						" and A.cd_natura IN (	select cd_natura from " +
						it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "ass_ev_ev C " + 						
												"where 	C.esercizio = ? " +
												" and C.cd_elemento_voce = ? " +
												" and C.ti_appartenenza = ? " +
												" and C.ti_gestione = ?)"	+					 
						" and A.cd_centro_responsabilita = B.cd_centro_responsabilita");

	sql.addParameter(accertamento.getEsercizio(), Types.NUMERIC, 1);
	sql.addParameter(it.cnr.contab.config00.latt.bulk.WorkpackageBulk.TI_GESTIONE_ENTRATE, Types.VARCHAR, 2);	
	sql.addParameter(accertamento.getCd_uo_origine(), Types.VARCHAR, 3);
	sql.addParameter(accertamento.getCapitolo().getEsercizio(), Types.NUMERIC, 4);
	sql.addParameter(accertamento.getCapitolo().getCd_titolo_capitolo(), Types.VARCHAR, 5);
	sql.addParameter(accertamento.getCapitolo().getTi_appartenenza(), Types.CHAR, 6);
	sql.addParameter(accertamento.getCapitolo().getTi_gestione(), Types.CHAR, 7);
	List l =  laHome.fetchAll(sql);
	getHomeCache().fetchAll();
	return l;	
}
*/
    public java.util.List findLineeAttivita(List cdrList, List capitoliList, AccertamentoBulk accertamento) throws IntrospectionException, PersistencyException {
        try {
            String statement =
                    "SELECT DISTINCT A.* FROM " +
                            EJBCommonServices.getDefaultSchema() +
                            "V_PDG_ACCERTAMENTO_ETR A " +
                            "WHERE " +
                            "(A.CATEGORIA_DETTAGLIO = ? OR " +
                            " A.CATEGORIA_DETTAGLIO = ?) AND " +
                            "A.ESERCIZIO = ? AND " +
                            "A.ESERCIZIO_RES = ? AND " +
                            "A.TI_GESTIONE = ? AND " +
                            "A.CD_ELEMENTO_VOCE = ? AND ";

            int size = cdrList.size();

            if (size == 0)
                return Collections.EMPTY_LIST;

            statement = statement.concat("( A.CD_CENTRO_RESPONSABILITA = ? ");
            for (int t = 1; t < size; t++)
                statement = statement.concat("OR A.CD_CENTRO_RESPONSABILITA = ? ");
            statement = statement.concat(" ) ");// AND ");

            size = capitoliList.size();
            if (size == 0)
                return Collections.EMPTY_LIST;

			/*
            statement = statement.concat( "( A.CD_FUNZIONE = ? ");
			for ( int t = 1 ; t < size; t++ )
				statement = statement.concat("OR A.CD_FUNZIONE = ? ");

			statement = statement.concat( " ) ");		
			*/

            //java.sql.PreparedStatement ps = getConnection().prepareStatement( statement );
            LoggableStatement ps = null;
            Connection conn = getConnection();
            ps = new LoggableStatement(conn, statement, true, this.getClass());

            try {
                PersistentHome parCNRHome = getHomeCache().getHome(Parametri_cnrBulk.class);
                Parametri_cnrBulk parCNR = (Parametri_cnrBulk) parCNRHome.findByPrimaryKey(new Parametri_cnrBulk(accertamento.getEsercizio()));
                if (!parCNR.getFl_nuovo_pdg()) {
                    Voce_fBulk capitolo = (Voce_fBulk) capitoliList.iterator().next();

                    ps.setString(1, Pdg_preventivo_detBulk.CAT_SINGOLO);
                    ps.setString(2, Pdg_modulo_entrate_gestBulk.CAT_DIRETTA);
                    ps.setObject(3, capitolo.getEsercizio());
                    ps.setObject(4, accertamento.getEsercizio_originale() == null ? capitolo.getEsercizio() : accertamento.getEsercizio_originale());
                    ps.setString(5, Elemento_voceHome.GESTIONE_ENTRATE);
                    ps.setString(6, capitolo.getCd_titolo_capitolo());
                } else {
                    V_voce_f_partita_giroBulk capitolo = (V_voce_f_partita_giroBulk) capitoliList.iterator().next();

                    ps.setString(1, Pdg_preventivo_detBulk.CAT_SINGOLO);
                    ps.setString(2, Pdg_modulo_entrate_gestBulk.CAT_DIRETTA);
                    ps.setObject(3, capitolo.getEsercizio());
                    ps.setObject(4, accertamento.getEsercizio_originale() == null ? capitolo.getEsercizio() : accertamento.getEsercizio_originale());
                    ps.setString(5, Elemento_voceHome.GESTIONE_ENTRATE);
                    ps.setString(6, capitolo.getCd_elemento_voce());
                }

                Iterator i = cdrList.iterator();
                ps.setString(7, ((CdrBulk) i.next()).getCd_centro_responsabilita());
                int j = 7;
                while (i.hasNext())
                    ps.setString(++j, ((CdrBulk) i.next()).getCd_centro_responsabilita());
						
				/*
				i = capitoliList.iterator();
				String voce = ((Voce_fBulk)i.next()).getCd_funzione();
				ps.setString( ++j, voce );
				while (  i.hasNext() )
					ps.setString( ++j, ((Voce_fBulk)i.next()).getCd_funzione() );
				*/

                ResultSet rs = ps.executeQuery();
                try {
                    PersistentHome pdgHome = getHomeCache().getHome(V_pdg_accertamento_etrBulk.class);
                    return pdgHome.fetchAll(pdgHome.createBroker(ps, rs));
                } catch (Exception e) {
                    throw new PersistencyException(e);
                } finally {
                    try {
                        rs.close();
                    } catch (java.sql.SQLException e) {
                    }
                    ;
                }
            } catch (SQLException e) {
                throw new PersistencyException(e);
            } finally {
                try {
                    ps.close();
                } catch (java.sql.SQLException e) {
                }
                ;
            }
        } catch (SQLException e) {
            throw new PersistencyException(e);
        }
    }

    /**
     * Recupero le Linee di Attivita' comuni a quella ricevuta come parametro
     * (Esse si riferiranno a Cdr diversi)
     *
     * @param latt
     * @param esercizio
     * @return
     * @throws IntrospectionException
     * @throws PersistencyException
     * @throws ApplicationException
     */
    public java.util.List findLineeAttivitaComuni(it.cnr.contab.config00.latt.bulk.WorkpackageBulk latt, Integer esercizio) throws IntrospectionException, PersistencyException, ApplicationException {
        PersistentHome laHome = getHomeCache().getHome(it.cnr.contab.config00.latt.bulk.WorkpackageBulk.class, "V_LINEA_ATTIVITA_VALIDA");
        SQLBuilder sql = laHome.createSQLBuilder();

        sql.addClause("AND", "cd_linea_attivita", sql.EQUALS, latt.getCd_linea_attivita());
        sql.addClause("AND", "ti_gestione", sql.EQUALS, it.cnr.contab.config00.latt.bulk.WorkpackageBulk.TI_GESTIONE_ENTRATE);
        sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, esercizio);

        return laHome.fetchAll(sql);
    }

    /**
     * <!-- @TODO: da completare -->
     *
     * @param accertamento
     * @return
     * @throws PersistencyException
     * @throws IntrospectionException
     */
    public java.util.List findUoCds(AccertamentoBulk accertamento) throws PersistencyException, IntrospectionException {
        PersistentHome uoHome = getHomeCache().getHome(Unita_organizzativaBulk.class, "V_UNITA_ORGANIZZATIVA_VALIDA");
        SQLBuilder sql = uoHome.createSQLBuilder();

        sql.addClause("AND", "fl_uo_cds", sql.EQUALS, new Boolean(true));
        sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, accertamento.getEsercizio());
        sql.addClause("AND", "cd_unita_padre", sql.EQUALS, accertamento.getCd_cds_origine());

        return uoHome.fetchAll(sql);
    }

    private StringBuffer getPersistenColumnNamesReplacingWith(
            BulkHome home,
            String[][] fields)
            throws PersistencyException {

        java.io.StringWriter columns = new java.io.StringWriter();

        if (home == null)
            throw new PersistencyException("Impossibile ottenere la home per l'aggiornamento dei progressivi temporanei dell'accertamento!");

        java.io.PrintWriter pw = new java.io.PrintWriter(columns);
        String[] persistenColumns = home.getColumnMap().getColumnNames();
        for (int i = 0; i < persistenColumns.length; i++) {
            String columnName = persistenColumns[i];
            if (fields != null) {
                for (int j = 0; j < fields.length; j++) {
                    String[] field = fields[j];
                    if (columnName.equalsIgnoreCase(field[0])) {
                        columnName = field[1];
                        break;
                    }
                }
            }
            pw.print(columnName);
            pw.print((i == persistenColumns.length - 1) ? "" : ", ");
        }
        pw.close();
        return columns.getBuffer();
    }

    /**
     * Imposta il pg_accertamento di un oggetto AccertamentoBulk.
     *
     * @param accertamento OggettoBulk
     * @throws PersistencyException
     */

    public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext, OggettoBulk bulk) throws PersistencyException, ComponentException {
        try {
            AccertamentoBulk accertamento = (AccertamentoBulk) bulk;
            Numerazione_doc_contHome numHome = (Numerazione_doc_contHome) getHomeCache().getHome(Numerazione_doc_contBulk.class);
            Long pg = (!userContext.isTransactional()) ?
                    numHome.getNextPg(userContext,
                            accertamento.getEsercizio(),
                            accertamento.getCd_cds(),
                            accertamento.getCd_tipo_documento_cont(),
                            accertamento.getUser()) :
                    ((NumTempDocContComponentSession) EJBCommonServices.createEJB(
                            "CNRDOCCONT00_EJB_NumTempDocContComponentSession",
                            NumTempDocContComponentSession.class)).getNextTempPg(userContext, accertamento);

            accertamento.setPg_accertamento(pg);
        } catch (ApplicationException e) {
            throw new ComponentException(e);
        } catch (Throwable e) {
            throw new PersistencyException(e);
        }
    }

    /**
     * Metodo per selezionare i capitoli di Entrata Cnr.
     *
     * @param bulk   <code>AccertamentoBulk</code> l'accertamento
     * @param home   istanza di <code>Voce_fHome</code>
     * @param voce_f <code>Voce_fBulk</code> i capitoli di entrata o di spesa
     * @param clause <code>CompoundFindClause</code> le clausole della selezione
     * @return sql i capitoli di Entrata Cnr definiti per l'accertamento
     */
    public SQLBuilder selectCapitoloByClause(AccertamentoBulk bulk, V_voce_f_partita_giroHome home, V_voce_f_partita_giroBulk voce_f, CompoundFindClause clause) throws IntrospectionException, PersistencyException, ApplicationException, SQLException {
        PersistentHome parCNRHome = getHomeCache().getHome(Parametri_cnrBulk.class);
        Parametri_cnrBulk parCNR = (Parametri_cnrBulk) parCNRHome.findByPrimaryKey(new Parametri_cnrBulk(bulk.getEsercizio()));
        final boolean isUOSac = Optional.ofNullable(bulk.getCd_cds_origine())
                .map(s -> new Unita_organizzativaBulk(s))
                .map(unita_organizzativaBulk -> {
                    try {
                        return (Unita_organizzativaBulk) getHomeCache().getHome(unita_organizzativaBulk).findByPrimaryKey(unita_organizzativaBulk);
                    } catch (PersistencyException e) {
                        return null;
                    }
                }).map(Unita_organizzativaBulk::getCd_tipo_unita).filter(s -> s.equals(Tipo_unita_organizzativaHome.TIPO_UO_SAC)).isPresent();

        PersistentHome voceHome = getHomeCache().getHome(V_voce_f_partita_giroBulk.class);
        SQLBuilder sql = voceHome.createSQLBuilder();

        // Ricerco tutti i capitolo di Entrata Cnr non in partita di giro
        // la cui natura non e' nulla nella tabella ASS_EV_EV
        if (bulk instanceof AccertamentoOrdBulk)
            sql.addSQLClause("AND", "fl_solo_residuo", sql.EQUALS, "N");
        if (bulk instanceof AccertamentoResiduoBulk)
            sql.addSQLClause("AND", "fl_solo_competenza", sql.EQUALS, "N");
        sql.addClause(clause);
        sql.openParenthesis("AND");
        sql.openParenthesis("AND");
        sql.addSQLClause("AND", "cd_unita_organizzativa", sql.EQUALS, bulk.getCd_uo_origine());
        sql.addSQLClause("OR", "cd_unita_organizzativa", sql.ISNULL, null);
        sql.closeParenthesis();
        sql.closeParenthesis();

        sql.addSQLClause("AND", "esercizio", sql.EQUALS, bulk.getEsercizio());
        sql.addSQLClause("AND", "ti_appartenenza", sql.EQUALS, Elemento_voceHome.APPARTENENZA_CNR);
        sql.addSQLClause("AND", "ti_gestione", sql.EQUALS, Elemento_voceHome.GESTIONE_ENTRATE);
        if (!parCNR.getFl_nuovo_pdg())
            sql.addSQLClause("AND", "ti_voce", sql.EQUALS, Elemento_voceHome.TIPO_ARTICOLO);
        sql.addSQLClause("AND", "fl_partita_giro", sql.EQUALS, "N");
        if (isUOSac)
            sql.addSQLClause("AND", "fl_voce_sac", sql.NOT_EQUALS, "X");
        else
		/* se in scrivania non ho il cds SAC devo escludere le voce_f riseravte SAC - clausola FL_VOCE_SAC != "Y" */
            sql.addSQLClause("AND", "fl_voce_sac", sql.NOT_EQUALS, "Y");


        SQLBuilder sqlExist = getHomeCache().getHome(Ass_ev_evBulk.class).createSQLBuilder();
        sqlExist.setStatement(" SELECT DISTINCT * FROM " +
                it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "ASS_EV_EV " +
                "WHERE ASS_EV_EV.CD_NATURA IS NOT NULL AND " +
                "V_VOCE_F_PARTITA_GIRO.TI_GESTIONE = ASS_EV_EV.TI_GESTIONE AND " +
                "V_VOCE_F_PARTITA_GIRO.TI_APPARTENENZA = ASS_EV_EV.TI_APPARTENENZA AND " +
                "V_VOCE_F_PARTITA_GIRO.CD_TITOLO_CAPITOLO = ASS_EV_EV.CD_ELEMENTO_VOCE AND " +
                "V_VOCE_F_PARTITA_GIRO.ESERCIZIO = ASS_EV_EV.ESERCIZIO ");
        sql.addSQLExistsClause("AND", sqlExist);
        return sql;
    }

    /*
    - accertamento non associato a documenti amministrativi: viene selezionato un qualsiasi terzo di tipo DEBITORE
      o ENTRAMBI
    - se l'accertamento è associato a documenti amministrativi e era gia' stato impostato un terzo: la selezione prevede
      tutti i terzi con tipo entità = DIVERSI di tipo DEBITORE/ENTRAMBI più il terzo già selezionato
    - se l'accertamento è associato a documenti amministrativi e non era gia' stato impostato un terzo: la selezione prevede
      tutti i terzi con tipo entità = DIVERSI di tipo DEBITORE/ENTRAMBI
    */
    public SQLBuilder selectDebitoreByClause(AccertamentoBulk bulk, TerzoHome home, TerzoBulk terzo, CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException, IllegalAccessException, it.cnr.jada.persistency.PersistencyException {

        SQLBuilder sql = getHomeCache().getHome(TerzoBulk.class, "V_TERZO_CF_PI").createSQLBuilder();

        if (terzo.getCd_precedente() != null)
            sql.addSQLClause("AND", "CD_PRECEDENTE", sql.EQUALS, terzo.getCd_precedente());

        if (!bulk.isAssociataADocAmm()) //accertamento non associato a documenti amministrativi
        {
            sql.openParenthesis("AND");
            sql.addSQLClause("AND", "TI_TERZO", sql.EQUALS, terzo.DEBITORE);
            sql.addSQLClause("OR", "TI_TERZO", sql.EQUALS, terzo.ENTRAMBI);
            sql.closeParenthesis();
            sql.addSQLClause("AND", "(V_TERZO_CF_PI.DT_FINE_RAPPORTO >= SYSDATE OR V_TERZO_CF_PI.DT_FINE_RAPPORTO IS NULL)");
            sql.addClause(clause);
            if (terzo.getAnagrafico() != null) {
                if (terzo.getAnagrafico().getCodice_fiscale() != null || terzo.getAnagrafico().getPartita_iva() != null) {   //aggiungo join su anagrafico
                    sql.addTableToHeader("ANAGRAFICO");
                    sql.addSQLJoin("V_TERZO_CF_PI.CD_ANAG", "ANAGRAFICO.CD_ANAG");
                }
                if (terzo.getAnagrafico().getCodice_fiscale() != null)
                    sql.addSQLClause("AND", "ANAGRAFICO.CODICE_FISCALE", sql.EQUALS, terzo.getAnagrafico().getCodice_fiscale());
                if (terzo.getAnagrafico().getPartita_iva() != null)
                    sql.addSQLClause("AND", "ANAGRAFICO.PARTITA_IVA", sql.EQUALS, terzo.getAnagrafico().getPartita_iva());
            }
        } else //accertamento associato a documenti amministrativi
        {
            sql.setHeader("select distinct V_TERZO_CF_PI.*");
            sql.addTableToHeader("ANAGRAFICO");
            sql.openParenthesis("AND");
            sql.addSQLClause("AND", "V_TERZO_CF_PI.TI_TERZO", sql.EQUALS, terzo.DEBITORE);
            sql.addSQLClause("OR", "V_TERZO_CF_PI.TI_TERZO", sql.EQUALS, terzo.ENTRAMBI);
            sql.closeParenthesis();
            sql.addSQLClause("AND", "(V_TERZO_CF_PI.DT_FINE_RAPPORTO >= SYSDATE OR V_TERZO_CF_PI.DT_FINE_RAPPORTO IS NULL)");
            sql.addClause(clause);
            sql.openParenthesis("AND");
            sql.openParenthesis("AND");
            sql.addSQLClause("AND", "V_TERZO_CF_PI.CD_ANAG = ANAGRAFICO.CD_ANAG");
            sql.addSQLClause("AND", "ANAGRAFICO.TI_ENTITA", sql.EQUALS, AnagraficoBulk.DIVERSI);
            sql.closeParenthesis();
            sql.addSQLClause("OR", "V_TERZO_CF_PI.CD_TERZO", sql.EQUALS, bulk.getCd_terzo_iniziale());
            sql.closeParenthesis();
            if (terzo.getAnagrafico() != null) {
                if (terzo.getAnagrafico().getCodice_fiscale() != null)
                    sql.addSQLClause("AND", "ANAGRAFICO.CODICE_FISCALE", sql.EQUALS, terzo.getAnagrafico().getCodice_fiscale());
                if (terzo.getAnagrafico().getPartita_iva() != null)
                    sql.addSQLClause("AND", "ANAGRAFICO.PARTITA_IVA", sql.EQUALS, terzo.getAnagrafico().getPartita_iva());
            }
        }
        return sql;
/*
	SQLBuilder sql = home.createSQLBuilder();
	if ( !bulk.isAssociataADocAmm() && 
		bulk.getCrudStatus() != bulk.TO_BE_CREATED && 
		bulk.getCrudStatus() != bulk.UNDEFINED ) //obbligazione non associata a documenti amministrativi e in fase di modifica
	{
		if ( bulk.getCd_terzo() != null ) //terzo già selezionato
		{
			sql.setStatement(
						"select distinct A.* from " + 
						it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "TERZO A " + 
						" where  " +
						"(A.TI_TERZO =  '" + terzo.DEBITORE + "' OR " +
						"A.TI_TERZO = '" + terzo.ENTRAMBI + "') AND " +
						"A.CD_TERZO = " + bulk.getCd_terzo() + " AND " +
						"(A.DT_FINE_RAPPORTO IS NULL " + " OR " +
						"A.DT_FINE_RAPPORTO >= SYSDATE )" );
		}
		else
		{
			sql.setStatement(
						"select A.* from " + 
						it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "TERZO A" + 
						" where  " +
						"(A.TI_TERZO =  '" + terzo.DEBITORE + "' OR " +
						"A.TI_TERZO = '" + terzo.ENTRAMBI + "' ) AND " +
						"(A.DT_FINE_RAPPORTO IS NULL " + " OR " +
						"A.DT_FINE_RAPPORTO >= SYSDATE )" );
		}	
	}
	else if ( bulk.isAssociataADocAmm() && 
			bulk.getCrudStatus() != bulk.TO_BE_CREATED && 
			bulk.getCrudStatus() != bulk.UNDEFINED ) //obbligazione associata a documenti amministrativi e in fase di modifica
	{	 			
		if ( bulk.getCd_terzo() != null ) //utente ha specificato il codice terzo
		{
			sql.setStatement(
						"select distinct A.* from " + 
						it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "TERZO A, " + 
						it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "ANAGRAFICO B " +
						" where  " +
						"(A.TI_TERZO =  '" + terzo.DEBITORE + "' OR " +
						"A.TI_TERZO = '" + terzo.ENTRAMBI + "') AND " +
						"((A.CD_ANAG = B.CD_ANAG " +						
						"AND B.TI_ENTITA = '" + AnagraficoBulk.DIVERSI + "' ) "  +
						"OR A.CD_TERZO = " + bulk.getCd_terzo_iniziale() + " ) "  +
						"AND A.CD_TERZO = " + bulk.getCd_terzo() + "  AND " +
						"(A.DT_FINE_RAPPORTO IS NULL OR " +
						"A.DT_FINE_RAPPORTO >= SYSDATE )" );

		}	
		else //utente non ha specificato il codice terzo
		{
			sql.setStatement(
						"select distinct A.* from " + 
						it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "TERZO A, " + 
						it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "ANAGRAFICO B " +
						" where  " +
						"(A.TI_TERZO =  '" + terzo.DEBITORE + "' OR " +
						"A.TI_TERZO = '" + terzo.ENTRAMBI + "') AND " +
						"((A.CD_ANAG = B.CD_ANAG " +						
						"AND B.TI_ENTITA = '" + AnagraficoBulk.DIVERSI + "' ) "  +
						"OR A.CD_TERZO = " + bulk.getCd_terzo_iniziale() + " ) " +
						"AND (A.DT_FINE_RAPPORTO IS NULL OR " +
						"A.DT_FINE_RAPPORTO >= SYSDATE )" );
		}			
	}
	else
	{
		sql.openParenthesis( "AND");
		sql.addSQLClause("AND","TI_TERZO",sql.EQUALS, terzo.DEBITORE );						
		sql.addSQLClause("OR","TI_TERZO",sql.EQUALS, terzo.ENTRAMBI );
		sql.closeParenthesis();
  	    sql.addSQLClause( "AND", "(DT_FINE_RAPPORTO >= SYSDATE OR DT_FINE_RAPPORTO IS NULL)");		
		if ( bulk.getCd_terzo() != null ) //terzo già selezionato
		{
			sql.addSQLClause("AND","CD_TERZO",sql.EQUALS, bulk.getCd_terzo());						
		}
		sql.addClause( clause );
	}
	return sql;
*/
    }

    /**
     * Metodo per verificare lo stato dell'esercizio dell'accertamento.
     *
     * @param bulk <code>AccertamentoBulk</code> l'accertamento
     * @return FALSE se non è stato inserito nessun esercizio o se l'esercizio non è in stato di "aperto"
     * TRUE in tutti gli altri casi
     */
    public boolean verificaStatoEsercizio(AccertamentoBulk bulk) throws PersistencyException, IntrospectionException {
//	EnteBulk ente = (EnteBulk) getHomeCache().getHome(EnteBulk.class).findAll().get(0);
        EsercizioBulk esercizio = (EsercizioBulk) getHomeCache().getHome(EsercizioBulk.class).findByPrimaryKey(
                new EsercizioBulk(bulk.getCd_cds_origine(), bulk.getEsercizio()));
        if (esercizio == null)
            return false;
        if (!esercizio.STATO_APERTO.equals(esercizio.getSt_apertura_chiusura()))
            return false;
        return true;
    }
/**
 * <!-- @TODO: da completare -->
 *
 *
 * @param capitoliList
 * @return
 * @throws IntrospectionException
 * @throws PersistencyException
 */
/*
 * Metodo sostituito integralmente da quello immediatamente successivo che consente di visualizzare la 
 * lista completa dei CDR anche se non è stato caricato alcun dettaglio di entrata nel piano di gestione
 */
/*
public java.util.List findCdr( List capitoliList, AccertamentoBulk accertamento ) throws IntrospectionException,PersistencyException 
{
	try
	{
		int size = capitoliList.size() ;
		
		if ( size == 0 )
			return Collections.EMPTY_LIST;
			
		String statement = 
			"SELECT DISTINCT B.* FROM " + 		
			EJBCommonServices.getDefaultSchema() +
			"V_PDG_ACCERTAMENTO_ETR A, " +
			EJBCommonServices.getDefaultSchema() +
			"CDR B " +
			"WHERE " +
			"(A.CATEGORIA_DETTAGLIO = ? OR " +
			" A.CATEGORIA_DETTAGLIO = ?) AND " +
			"A.ESERCIZIO = ? AND " +
			"A.ESERCIZIO_RES = ? AND " +
			"A.TI_GESTIONE = ? AND " +
			"A.CD_ELEMENTO_VOCE = ? AND " +
			"B.CD_CENTRO_RESPONSABILITA = A.CD_CENTRO_RESPONSABILITA AND " +									
			"B.CD_UNITA_ORGANIZZATIVA = ? ";//AND " ;

		-- vedi sotto
		--statement = statement.concat( "( A.CD_FUNZIONE = ? ");
		--for ( int t = 1 ; t < size; t++ )
		--	statement = statement.concat("OR A.CD_FUNZIONE = ? ");
		--statement = statement.concat( " ) ");		
			
		//java.sql.PreparedStatement ps = getConnection().prepareStatement( statement );
		java.sql.PreparedStatement ps = null;
		Connection conn = getConnection();
			
		boolean logEnabled = LoggableStatement.IS_ENABLED;
		if(logEnabled) // use a switch to toggle logging.
			ps = new LoggableStatement(conn,statement);
		else
			ps = conn.prepareStatement(statement);

		try
		{	
			Voce_fBulk capitolo = (Voce_fBulk) capitoliList.iterator().next();

			ps.setString( 1, Pdg_preventivo_detBulk.CAT_SINGOLO );
			ps.setString( 2, Pdg_modulo_entrate_gestBulk.CAT_DIRETTA );
			ps.setObject( 3, capitolo.getEsercizio() );
			ps.setObject( 4, accertamento.getEsercizio_originale()==null?capitolo.getEsercizio():accertamento.getEsercizio_originale());
			ps.setString( 5, Elemento_voceHome.GESTIONE_ENTRATE );
			ps.setString( 6, capitolo.getCd_titolo_capitolo() );
			ps.setString( 7, capitolo.getCd_unita_organizzativa() );

			-- per ora escludo questa parte
			-- anche perchè non funziona dato che
			-- getCd_funzione() è sempre null perchè
			-- capitoliList non contiene le righe di voce_f
			-- completamente valorizzate

			--Iterator i = capitoliList.iterator();
			--ps.setString( 6, ((Voce_fBulk)i.next()).getCd_funzione() );
			--for ( int j = 7; i.hasNext(); j++ )
			--	ps.setString( j, ((Voce_fBulk)i.next()).getCd_funzione() );
			
			if(logEnabled)
			   System.out.println("Executing query findCdr( List capitoliList ): "+
				 ((LoggableStatement)ps).getQueryString());

			ResultSet rs = LoggableStatement.executeQuery(ps);
			try
			{
				PersistentHome cdrHome = getHomeCache().getHome(CdrBulk.class);
				return cdrHome.fetchAll( cdrHome.createBroker(ps, rs ));
			}
			catch( Exception e )
			{
				throw new PersistencyException( e );
			}
			finally
			{
				try{rs.close();}catch( java.sql.SQLException e ){};
			}
		}
		catch( SQLException e )
		{
			throw new PersistencyException( e );
		}
		finally
		{
			try{ps.close();}catch( java.sql.SQLException e ){};
		}
	}
	catch ( SQLException e )
	{
		throw new PersistencyException( e );
	}
}
*/

    /**
     * <!-- @TODO: da completare -->
     *
     * @param capitoliList
     * @return
     * @throws IntrospectionException
     * @throws PersistencyException
     */
    public java.util.List findCdr(List capitoliList, AccertamentoBulk accertamento) throws IntrospectionException, PersistencyException {
        int size = capitoliList.size();

        if (size == 0)
            return Collections.EMPTY_LIST;
        PersistentHome cdrHome = getHomeCache().getHome(CdrBulk.class);
        SQLBuilder sql = cdrHome.createSQLBuilder();
        PersistentHome parCNRHome = getHomeCache().getHome(Parametri_cnrBulk.class);
        Parametri_cnrBulk parCNR = (Parametri_cnrBulk) parCNRHome.findByPrimaryKey(new Parametri_cnrBulk(accertamento.getEsercizio()));
        if (!parCNR.getFl_nuovo_pdg()) {
            Voce_fBulk capitolo = (Voce_fBulk) capitoliList.iterator().next();
            sql.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, capitolo.getCd_unita_organizzativa());
        } else
            sql.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, accertamento.getCd_uo_origine());
        return cdrHome.fetchAll(sql);
    }

    /**
     * <!-- @TODO: da completare -->
     *
     * @param capitoliList
     * @return
     * @throws IntrospectionException
     * @throws PersistencyException
     */
    public java.util.List findCdrPerSAC(List capitoliList, AccertamentoBulk accertamento) throws IntrospectionException, PersistencyException {

        try {
            int sizeCapitoli = capitoliList.size();

            if (sizeCapitoli == 0)
                return Collections.EMPTY_LIST;

            String statement =
                    "SELECT DISTINCT B.* FROM " +
                            EJBCommonServices.getDefaultSchema() +
                            "V_PDG_ACCERTAMENTO_ETR A, " +
                            EJBCommonServices.getDefaultSchema() +
                            "CDR B " +
                            "WHERE " +
                            "(A.CATEGORIA_DETTAGLIO = ? OR " +
                            " A.CATEGORIA_DETTAGLIO = ?) AND " +
                            "A.ESERCIZIO = ? AND " +
                            "A.ESERCIZIO_RES = ? AND " +
                            "A.TI_GESTIONE = ? AND " +
                            "A.CD_ELEMENTO_VOCE = ? AND " +
                            "B.CD_CENTRO_RESPONSABILITA = A.CD_CENTRO_RESPONSABILITA AND ";

            statement = statement.concat("( (A.CD_FUNZIONE = ? AND A.CD_CENTRO_RESPONSABILITA = ? ) ");
            for (int t = 1; t < sizeCapitoli; t++)
                statement = statement.concat("OR (A.CD_FUNZIONE = ? AND A.CD_CENTRO_RESPONSABILITA = ? ) ");
            statement = statement.concat(" ) ");

            //java.sql.PreparedStatement ps = getConnection().prepareStatement( statement );
            LoggableStatement ps = null;
            Connection conn = getConnection();
            ps = new LoggableStatement(conn, statement, true, this.getClass());

            try {
                Voce_fBulk capitolo = (Voce_fBulk) capitoliList.iterator().next();

                ps.setString(1, Pdg_preventivo_detBulk.CAT_SINGOLO);
                ps.setString(2, Pdg_modulo_entrate_gestBulk.CAT_DIRETTA);
                ps.setObject(3, capitolo.getEsercizio());
                ps.setObject(4, accertamento.getEsercizio_originale() == null ? capitolo.getEsercizio() : accertamento.getEsercizio_originale());
                ps.setString(5, Elemento_voceHome.GESTIONE_ENTRATE);
                ps.setString(6, capitolo.getCd_titolo_capitolo());

                int j = 7;
                Iterator i = capitoliList.iterator();
                capitolo = (Voce_fBulk) i.next();
                ps.setString(j++, capitolo.getCd_funzione());
                ps.setString(j++, capitolo.getCd_centro_responsabilita());

                for (; i.hasNext(); ) {
                    capitolo = (Voce_fBulk) i.next();
                    ps.setString(j++, capitolo.getCd_funzione());
                    ps.setString(j++, capitolo.getCd_centro_responsabilita());
                }

                ResultSet rs = ps.executeQuery();
                try {
                    PersistentHome cdrHome = getHomeCache().getHome(CdrBulk.class);
                    return cdrHome.fetchAll(cdrHome.createBroker(ps, rs));
                } catch (Exception e) {
                    throw new PersistencyException(e);
                } finally {
                    try {
                        rs.close();
                    } catch (java.sql.SQLException e) {
                    }
                    ;
                }
            } catch (SQLException e) {
                throw new PersistencyException(e);
            } finally {
                try {
                    ps.close();
                } catch (java.sql.SQLException e) {
                }
                ;
            }
        } catch (SQLException e) {
            throw new PersistencyException(e);
        }
    }

    public java.util.List findLineeAttivitaSAC(List capitoliList, AccertamentoBulk accertamento) throws IntrospectionException, PersistencyException {
        try {
            String statement =
                    "SELECT DISTINCT A.* FROM " +
                            EJBCommonServices.getDefaultSchema() +
                            "V_PDG_ACCERTAMENTO_ETR A " +
                            "WHERE " +
                            "(A.CATEGORIA_DETTAGLIO = ? OR " +
                            " A.CATEGORIA_DETTAGLIO = ?) AND " +
                            "A.ESERCIZIO = ? AND " +
                            "A.ESERCIZIO_RES = ? AND " +
                            "A.TI_GESTIONE = ? AND " +
                            "A.CD_ELEMENTO_VOCE = ? ";// AND ";

            int sizeCapitoli = capitoliList.size();
            if (sizeCapitoli == 0)
                return Collections.EMPTY_LIST;
				
			/*
			statement = statement.concat( "( (A.CD_FUNZIONE = ? AND A.CD_CENTRO_RESPONSABILITA = ? ) " );
			for ( int t = 1 ; t < sizeCapitoli; t++ )
				statement = statement.concat("OR (A.CD_FUNZIONE = ? AND A.CD_CENTRO_RESPONSABILITA = ? ) ");
			statement = statement.concat( " ) ");					
			*/
            //java.sql.PreparedStatement ps = getConnection().prepareStatement( statement );
            LoggableStatement ps = null;
            Connection conn = getConnection();
            ps = new LoggableStatement(conn, statement, true, this.getClass());
            try {

                PersistentHome parCNRHome = getHomeCache().getHome(Parametri_cnrBulk.class);
                Parametri_cnrBulk parCNR = (Parametri_cnrBulk) parCNRHome.findByPrimaryKey(new Parametri_cnrBulk(accertamento.getEsercizio()));
                if (!parCNR.getFl_nuovo_pdg()) {
                    Voce_fBulk capitolo = (Voce_fBulk) capitoliList.iterator().next();

                    ps.setString(1, Pdg_preventivo_detBulk.CAT_SINGOLO);
                    ps.setString(2, Pdg_modulo_entrate_gestBulk.CAT_DIRETTA);
                    ps.setObject(3, capitolo.getEsercizio());
                    ps.setObject(4, accertamento.getEsercizio_originale() == null ? capitolo.getEsercizio() : accertamento.getEsercizio_originale());
                    ps.setString(5, Elemento_voceHome.GESTIONE_ENTRATE);
                    ps.setString(6, capitolo.getCd_titolo_capitolo());
                } else {
                    V_voce_f_partita_giroBulk capitolo = (V_voce_f_partita_giroBulk) capitoliList.iterator().next();
                    ps.setString(1, Pdg_preventivo_detBulk.CAT_SINGOLO);
                    ps.setString(2, Pdg_modulo_entrate_gestBulk.CAT_DIRETTA);
                    ps.setObject(3, capitolo.getEsercizio());
                    ps.setObject(4, accertamento.getEsercizio_originale() == null ? capitolo.getEsercizio() : accertamento.getEsercizio_originale());
                    ps.setString(5, Elemento_voceHome.GESTIONE_ENTRATE);
                    ps.setString(6, capitolo.getCd_elemento_voce());
                }
			
				/*
				int j = 5;
				Iterator i = capitoliList.iterator();
				capitolo = (Voce_fBulk) i.next();
				ps.setString( j++, capitolo.getCd_funzione() );
				ps.setString( j++, capitolo.getCd_centro_responsabilita() );
				for ( ; i.hasNext(); )
				{
					capitolo = (Voce_fBulk) i.next();			
					ps.setString( j++, capitolo.getCd_funzione() );
					ps.setString( j++, capitolo.getCd_centro_responsabilita() );		
				}	
				*/
                ResultSet rs = ps.executeQuery();
                try {
                    PersistentHome pdgHome = getHomeCache().getHome(V_pdg_accertamento_etrBulk.class);
                    return pdgHome.fetchAll(pdgHome.createBroker(ps, rs));
                } catch (Exception e) {
                    throw new PersistencyException(e);
                } finally {
                    try {
                        rs.close();
                    } catch (java.sql.SQLException e) {
                    }
                    ;
                }
            } catch (SQLException e) {
                throw new PersistencyException(e);
            } finally {
                try {
                    ps.close();
                } catch (java.sql.SQLException e) {
                }
                ;
            }
        } catch (SQLException e) {
            throw new PersistencyException(e);
        }
    }

    public AccertamentoBulk refreshNuoveLineeAttivitaColl(UserContext usercontext, AccertamentoBulk accertamento) {

        V_pdg_accertamento_etrBulk latt;
        boolean found;
        it.cnr.contab.doccont00.core.bulk.Linea_attivitaBulk nuovaLatt;
        BulkList nuoveLineeAttivitaColl = new BulkList();


        //l'imputazione finanziaria e' sempre di testata: seleziono i dettagli di una qualsiasi scadenza con importo != 0 per
        // individuare l'elenco delle linee di attivita selezionate per l'intera accertamento

        Accertamento_scadenzarioBulk scadenza = null;
        for (Iterator i = accertamento.getAccertamento_scadenzarioColl().iterator(); i.hasNext(); ) {
            scadenza = (Accertamento_scadenzarioBulk) i.next();
            if (scadenza.getIm_scadenza().compareTo(new BigDecimal(0)) > 0)
                break;
        }


        for (Iterator s = scadenza.getAccertamento_scad_voceColl().iterator(); s.hasNext(); ) {
            Accertamento_scad_voceBulk osv = (Accertamento_scad_voceBulk) s.next();
            found = false;

            for (Iterator l = accertamento.getLineeAttivitaSelezionateColl().iterator(); l.hasNext(); ) {
                latt = (V_pdg_accertamento_etrBulk) l.next();
                if (osv.getCd_centro_responsabilita().equals(latt.getCd_centro_responsabilita()) &&
                        osv.getCd_linea_attivita().equals(latt.getCd_linea_attivita()))
                    found = true;
            }
            if (!found) {
                // escludiamo le linee di attività comuni su altri cdr
                // questi vengono visualizzati solo nelle scad per voce
                boolean found2 = true;
                if (osv.getCd_linea_attivita().startsWith("C")) {
                    if (accertamento.getCdrSelezionatiColl().size() > 0) {
                        found2 = false;
                        Iterator k = accertamento.getCdrSelezionatiColl().iterator();
                        while (k.hasNext()) {
                            CdrBulk cdr = (CdrBulk) k.next();
                            if (cdr.getCd_centro_responsabilita().equals(osv.getLinea_attivita().getCd_centro_responsabilita())) {
                                found2 = true;
                                break;
                            }
                        }
                    }
                }

                if (found2) {
                    nuovaLatt = new it.cnr.contab.doccont00.core.bulk.Linea_attivitaBulk();

                    try{
                    	PersistentHome laHome = getHomeCache().getHome(WorkpackageBulk.class, "V_LINEA_ATTIVITA_VALIDA", "it.cnr.contab.doccont00.comp.AccertamentoComponent.find.linea_att");
	                    nuovaLatt.setLinea_att((WorkpackageBulk)laHome.findByPrimaryKey(osv.getLinea_attivita()));
	                    getHomeCache().fetchAll(usercontext);
                    } catch(Exception e) {
                    	nuovaLatt.setLinea_att(osv.getLinea_attivita());
                    }

                    if (osv.getAccertamento_scadenzario().getIm_scadenza().compareTo(new BigDecimal(0)) == 0) {
                        double nrDettagli = scadenza.getAccertamento_scad_voceColl().size();
                        nuovaLatt.setPrcImputazioneFin(new BigDecimal(100).divide(new BigDecimal(nrDettagli), 2, BigDecimal.ROUND_HALF_UP));
                    } else
                        nuovaLatt.setPrcImputazioneFin(osv.getIm_voce().multiply(new BigDecimal(100)).divide(osv.getAccertamento_scadenzario().getIm_scadenza(), 2, BigDecimal.ROUND_HALF_UP));
                    nuovaLatt.setAccertamento(accertamento);
                    nuoveLineeAttivitaColl.add(nuovaLatt);
                }
            }
        }

        if (accertamento.getLineeAttivitaSelezionateColl().size() == 0) {
            //non esistono latt da pdg --> e'necessario quadrare
            BigDecimal totPrc = new BigDecimal(0);
            for (Iterator i = nuoveLineeAttivitaColl.iterator(); i.hasNext(); ) {
                nuovaLatt = (it.cnr.contab.doccont00.core.bulk.Linea_attivitaBulk) i.next();
                totPrc = totPrc.add(nuovaLatt.getPrcImputazioneFin());
            }
            if (totPrc.compareTo(new BigDecimal(100)) != 0) {
                if (nuoveLineeAttivitaColl.size() > 0) {
                    nuovaLatt = (it.cnr.contab.doccont00.core.bulk.Linea_attivitaBulk) nuoveLineeAttivitaColl.get(0);
                    nuovaLatt.setPrcImputazioneFin(nuovaLatt.getPrcImputazioneFin().add(new BigDecimal(100).subtract(totPrc)));
                }
            }
        }
        accertamento.setNuoveLineeAttivitaColl(nuoveLineeAttivitaColl);
        return accertamento;
    }

    public java.util.List findCapitoliDiEntrataCds(AccertamentoBulk accertamento) throws IntrospectionException, PersistencyException {
        PersistentHome parCNRHome = getHomeCache().getHome(Parametri_cnrBulk.class);
        Parametri_cnrBulk parCNR = (Parametri_cnrBulk) parCNRHome.findByPrimaryKey(new Parametri_cnrBulk(accertamento.getEsercizio()));

        if (parCNR.getFl_nuovo_pdg())
            return Arrays.asList(accertamento.getCapitolo());

        PersistentHome evHome = getHomeCache().getHome(Voce_fBulk.class);
        SQLBuilder sql = evHome.createSQLBuilder();
        sql.addClause("AND", "esercizio", sql.EQUALS, accertamento.getEsercizio());
        sql.addClause("AND", "ti_appartenenza", sql.EQUALS, accertamento.getTi_appartenenza());
        sql.addClause("AND", "ti_gestione", sql.EQUALS, accertamento.getTi_gestione());
        sql.addClause("AND", "cd_voce", sql.EQUALS, accertamento.getCd_voce());
        return evHome.fetchAll(sql);
    }

    public java.util.List findPdgVincoloList(AccertamentoBulk accertamento) throws IntrospectionException, PersistencyException {
        PersistentHome asHome = getHomeCache().getHome(Pdg_vincoloBulk.class);
        SQLBuilder sql = asHome.createSQLBuilder();
        sql.addClause(FindClause.AND, "cd_cds_accertamento", SQLBuilder.EQUALS, accertamento.getCd_cds());
        sql.addClause(FindClause.AND, "esercizio_accertamento", SQLBuilder.EQUALS, accertamento.getEsercizio());
        sql.addClause(FindClause.AND, "esercizio_ori_accertamento", SQLBuilder.EQUALS, accertamento.getEsercizio_originale());
        sql.addClause(FindClause.AND, "pg_accertamento", SQLBuilder.EQUALS, accertamento.getPg_accertamento());
        sql.addOrderBy("pg_vincolo");

        return asHome.fetchAll(sql);
    }

    public java.util.List findAccertamentoVincoloPerenteList(AccertamentoBulk accertamento) throws IntrospectionException, PersistencyException {
        PersistentHome asHome = getHomeCache().getHome(Accertamento_vincolo_perenteBulk.class);
        SQLBuilder sql = asHome.createSQLBuilder();
        sql.addClause(FindClause.AND, "cd_cds_accertamento", SQLBuilder.EQUALS, accertamento.getCd_cds());
        sql.addClause(FindClause.AND, "esercizio_accertamento", SQLBuilder.EQUALS, accertamento.getEsercizio());
        sql.addClause(FindClause.AND, "esercizio_ori_accertamento", SQLBuilder.EQUALS, accertamento.getEsercizio_originale());
        sql.addClause(FindClause.AND, "pg_accertamento", SQLBuilder.EQUALS, accertamento.getPg_accertamento());

        return asHome.fetchAll(sql);
    }

    /**
     * Ritorna tutti gli accertamenti uguali al bulk indipendentemente dall'esercizio
     * comprensivo di quello indicato nel bulk
     *
     * @param accertamento
     * @return AccertamentoBulk
     * @throws PersistencyException
     * @throws IntrospectionException
     */
    public SQLBuilder selectAllEqualsAccertamentiByClause(AccertamentoBulk bulk, AccertamentoHome home, OggettoBulk bulkClause, CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException, IllegalAccessException, it.cnr.jada.persistency.PersistencyException {
        SQLBuilder sql = this.createSQLBuilder();
        sql.addClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS, bulk.getCd_cds());
        sql.addClause(FindClause.AND, "esercizio_originale", SQLBuilder.EQUALS, bulk.getEsercizio_originale());
        sql.addClause(FindClause.AND, "pg_accertamento", SQLBuilder.EQUALS, bulk.getPg_accertamento());
        sql.addClause(clause);
        return sql;
    }

    public java.util.Collection findAccertamentiPluriennali(it.cnr.jada.UserContext userContext, AccertamentoBulk bulk) throws IntrospectionException, PersistencyException {
        PersistentHome dettHome = getHomeCache().getHome(Accertamento_pluriennaleBulk.class);
        SQLBuilder sql = dettHome.createSQLBuilder();
        sql.addSQLClause("AND", "CD_CDS", sql.EQUALS, bulk.getCd_cds());
        sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, bulk.getEsercizio());
        sql.addSQLClause("AND", "ESERCIZIO_ORIGINALE", sql.EQUALS, bulk.getEsercizio_originale());
        sql.addSQLClause("AND", "PG_ACCERTAMENTO", sql.EQUALS, bulk.getPg_accertamento());

        sql.setOrderBy("ANNO", OrderConstants.ORDER_DESC);
        return dettHome.fetchAll(sql);
    }
}
