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

package it.cnr.contab.config00.sto.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Dictionary;

public class CdsHome extends BulkHome {
    /**
     * <!-- @TODO: da completare -->
     * Costruisce un CdsHome
     *
     * @param conn La java.sql.Connection su cui vengono effettuate le operazione di persistenza
     */
    public CdsHome(java.sql.Connection conn) {
        super(CdsBulk.class, conn);
    }

    /**
     * <!-- @TODO: da completare -->
     * Costruisce un CdsHome
     *
     * @param conn            La java.sql.Connection su cui vengono effettuate le operazione di persistenza
     * @param persistentCache La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
     */
    public CdsHome(java.sql.Connection conn, PersistentCache persistentCache) {
        super(CdsBulk.class, conn, persistentCache);
    }

    /**
     * Aggiorna l'esercizio di fine per tutte le Uo, Cdr e Linee attivita sotto al Cds passato come paramentro
     *
     * @param esercizio esercizio in cui viene creata l'UO
     * @param codiceCDS codice del CDS
     */

    public void aggiornaEsercizioFinePerUoFigli(Integer esercizio, String codiceCds) throws ApplicationException, PersistencyException {
        try {

            LoggableStatement ps = new LoggableStatement(getConnection(),
                    "UPDATE " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "LINEA_ATTIVITA " +
                            "SET ESERCIZIO_FINE = ? " +
                            "WHERE " +
                            "ESERCIZIO_FINE > ?  AND " +
                            "CD_CENTRO_RESPONSABILITA IN " +
                            "(SELECT CD_CENTRO_RESPONSABILITA FROM " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "CDR " +
                            "WHERE  " +
                            "CD_UNITA_ORGANIZZATIVA IN " +
                            "(SELECT CD_UNITA_ORGANIZZATIVA FROM " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "UNITA_ORGANIZZATIVA " +
                            "WHERE FL_CDS = 'N' AND " +
                            "CD_UNITA_PADRE = ? ))", true, this.getClass());
            try {
                ps.setObject(1, esercizio);
                ps.setObject(2, esercizio);
                ps.setString(3, codiceCds);

                ps.executeUpdate();
            } catch (SQLException e) {
                throw new PersistencyException(e);
            } finally {
                try {
                    ps.close();
                } catch (java.sql.SQLException e) {
                }
            }

            ps = new LoggableStatement(getConnection(),
                    "UPDATE " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "CDR " +
                            "SET ESERCIZIO_FINE = ? " +
                            "WHERE " +
                            "ESERCIZIO_FINE > ?  AND " +
                            "CD_UNITA_ORGANIZZATIVA IN " +
                            "(SELECT CD_UNITA_ORGANIZZATIVA FROM " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "UNITA_ORGANIZZATIVA " +
                            "WHERE FL_CDS = 'N' AND " +
                            "CD_UNITA_PADRE = ? )", true, this.getClass());
            try {
                ps.setObject(1, esercizio);
                ps.setObject(2, esercizio);
                ps.setString(3, codiceCds);

                ps.executeUpdate();
            } catch (SQLException e) {
                throw new PersistencyException(e);
            } finally {
                try {
                    ps.close();
                } catch (java.sql.SQLException e) {
                }
            }

            ps = new LoggableStatement(getConnection(),
                    "UPDATE " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "UNITA_ORGANIZZATIVA " +
                            "SET ESERCIZIO_FINE = ? " +
                            "WHERE " +
                            "ESERCIZIO_FINE > ? AND " +
                            "FL_CDS = 'N' AND " +
                            "CD_UNITA_PADRE = ? ", true, this.getClass());
            try {
                ps.setObject(1, esercizio);
                ps.setObject(2, esercizio);
                ps.setString(3, codiceCds);

                ps.executeUpdate();
            } catch (SQLException e) {
                throw new PersistencyException(e);
            } finally {
                try {
                    ps.close();
                } catch (java.sql.SQLException e) {
                }
            }

            // aggiorno CDR Responsabile dell'UO_CDS con ESERCIZIO_FINE = ESERCIZIO_FINE del CDS
            ps = new LoggableStatement(getConnection(),
                    "UPDATE " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "CDR " +
                            "SET ESERCIZIO_FINE = ? " +
                            "WHERE " +
                            "CD_UNITA_ORGANIZZATIVA IN " +
                            "(SELECT CD_UNITA_ORGANIZZATIVA FROM " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "UNITA_ORGANIZZATIVA " +
                            "WHERE FL_CDS = 'N' AND " +
                            "FL_UO_CDS = 'Y' AND " +
                            "CD_UNITA_PADRE = ? )", true, this.getClass());
            try {
                ps.setObject(1, esercizio);
                ps.setString(2, codiceCds);

                ps.executeUpdate();
            } catch (SQLException e) {
                throw new PersistencyException(e);
            } finally {
                try {
                    ps.close();
                } catch (java.sql.SQLException e) {
                }
            }

            // aggiorno UO_CDS con ESERCIZIO_FINE = ESERCIZIO_FINE del CDS
            ps = new LoggableStatement(getConnection(),
                    "UPDATE " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "UNITA_ORGANIZZATIVA " +
                            "SET ESERCIZIO_FINE = ? " +
                            "WHERE " +
                            "FL_UO_CDS = 'Y' AND " +
                            "FL_CDS = 'N' AND " +
                            "CD_UNITA_PADRE = ? ", true, this.getClass());
            try {
                ps.setObject(1, esercizio);
                ps.setString(2, codiceCds);

                ps.executeUpdate();
            } catch (SQLException e) {
                throw new PersistencyException(e);
            } finally {
                try {
                    ps.close();
                } catch (java.sql.SQLException e) {
                }
            }

        } catch (SQLException e) {
            throw new PersistencyException(e);
        }

    }

    /**
     * Genera un nuovo codice per CdsBulk calcolando il progressivo successivo al massimo esistente
     * per l'esercizio corrente escludendo dalla ricerca il CDS di tipo ENTE
     *
     * @param esercizio esercizio per cui e' necessario creare il codice
     * @return String codice creato
     */
    public String creaNuovoCodice() throws ApplicationException, PersistencyException {
        String codice;

        try {
            LoggableStatement ps = new LoggableStatement(getConnection(),
                    "SELECT CD_PROPRIO_UNITA FROM " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "UNITA_ORGANIZZATIVA " +
                            "WHERE FL_CDS = 'Y' AND " +
                            "CD_PROPRIO_UNITA = ( SELECT MAX(CD_PROPRIO_UNITA) " +
                            "FROM " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "UNITA_ORGANIZZATIVA " +
                            "WHERE FL_CDS = 'Y' AND CD_TIPO_UNITA != '" + Tipo_unita_organizzativaHome.TIPO_UO_ENTE + "')" +
                            "FOR UPDATE NOWAIT", true, this.getClass());
            try {
                ResultSet rs = ps.executeQuery();
                try {
                    if (rs.next()) {
                        codice = rs.getString(1);
                        if (codice != null) {
                            long cdLong = Long.parseLong(codice) + 1;
                            codice = String.valueOf(cdLong);
                        } else
                            codice = String.valueOf(0);
                    } else
                        codice = String.valueOf(0);
                } catch (SQLException e) {
                    throw new PersistencyException(e);
                } finally {
                    try {
                        rs.close();
                    } catch (java.sql.SQLException e) {
                    }
                }
            } catch (SQLException e) {
                throw new PersistencyException(e);
            } finally {
                try {
                    ps.close();
                } catch (java.sql.SQLException e) {
                }
            }

            return codice;
//		return getLunghezza_chiaviHome().create().formatCdsKey( codice, esercizio );
        } catch (java.lang.NumberFormatException e) {
            throw new ApplicationException("Esistono codice non numerici nel database. ");
        } catch (SQLException e) {
            throw new PersistencyException(e);
        }

    }

    /**
     * Restituisce il SQLBuilder per selezionare fra tutte le Unita Organizzative quelle che sono CDS non ENTE
     *
     * @return SQLBuilder
     */

    public SQLBuilder createSQLBuilder() {
        SQLBuilder sql = createSQLBuilderIncludeEnte();
        sql.addClause("AND", "cd_tipo_unita", SQLBuilder.NOT_EQUALS, Tipo_unita_organizzativaHome.TIPO_UO_ENTE);
        return sql;
    }

    /**
     * Restituisce il SQLBuilder per selezionare fra tutte le Unita Organizzative quelle che sono CDS ed in più il CDS ENTE
     *
     * @return SQLBuilder
     */

    public SQLBuilder createSQLBuilderIncludeEnte() {
        SQLBuilder sql = super.createSQLBuilder();
        sql.addClause("AND", "fl_cds", SQLBuilder.EQUALS, Boolean.TRUE);
        return sql;
    }

    /**
     * Carica la lista delle percentuali di copertura obligazione
     *
     * @param cds cds in processo
     * @return Lista delle percentuali
     * @throws IntrospectionException
     * @throws PersistencyException
     */
    public java.util.List findPercentuali(CdsBulk cds) throws IntrospectionException, PersistencyException {
        PersistentHome prcHome = getHomeCache().getHome(Prc_copertura_obbligBulk.class);
        SQLBuilder sql = prcHome.createSQLBuilder();
        sql.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, cds.getCd_unita_organizzativa());
        sql.addOrderBy("esercizio");
        return prcHome.fetchAll(sql);
    }

    /**
     * Carica in una hashtable l'elenco di Tipologie di CDS  presenti nel database
     *
     * @return it.cnr.jada.util.OrderedHashtable
     */

    public Dictionary loadTipologiaKeys(CdsBulk bulk) throws ApplicationException {
        return new Tipo_unita_organizzativaHome(getConnection()).loadTipologiaCdsKeys();

    }

    /**
     * Ritorna false se l'esercizio di fine impostato sul cds è minore del massimo esercizio
     * esistente per pdg del cds in processo
     *
     * @param cds cds in processo
     * @return boolean
     * @throws PersistencyException
     */
    public boolean verificaEsercizioPreventivo(CdsBulk cds) throws PersistencyException {
        try {
            int esercizioPdG;

            LoggableStatement ps = new LoggableStatement(getConnection(),
                    "SELECT MAX(ESERCIZIO) FROM " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "PDG_PREVENTIVO " +
                            "WHERE " +
                            "CD_CENTRO_RESPONSABILITA LIKE ? ", true, this.getClass());
            try {
                ps.setString(1, cds.getCd_unita_organizzativa() + "%");

                ResultSet rs = ps.executeQuery();
                try {
                    if (rs.next()) {
                        esercizioPdG = rs.getInt(1);
                        if (esercizioPdG > 0 && esercizioPdG > cds.getEsercizio_fine().intValue())
                            return false;
                    }
                } catch (SQLException e) {
                    throw new PersistencyException(e);
                } finally {
                    try {
                        rs.close();
                    } catch (java.sql.SQLException e) {
                    }
                }
            } catch (SQLException e) {
                throw new PersistencyException(e);
            } finally {
                try {
                    ps.close();
                } catch (java.sql.SQLException e) {
                }
            }
            return true;
        } catch (SQLException e) {
            throw new PersistencyException(e);
        }
    }

    /**
     * Ritorna una lista contenente tutte le UO del CDS passato
     **/
    public java.util.List findUoCds(it.cnr.jada.UserContext context, String cds) throws PersistencyException, IntrospectionException {
        PersistentHome uoHome = getHomeCache().getHome(Unita_organizzativaBulk.class, "V_UNITA_ORGANIZZATIVA_VALIDA");
        SQLBuilder sql = uoHome.createSQLBuilder();
        sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) context).getEsercizio());
        sql.addClause("AND", "cd_unita_padre", SQLBuilder.EQUALS, cds);

        return uoHome.fetchAll(sql);
    }

	/**
	 * Ritorna una lista contenente tutti i CDS validi
	 **/
	public java.util.List findCdS(it.cnr.jada.UserContext context) throws PersistencyException, IntrospectionException {
		PersistentHome cdsHome = getHomeCache().getHome(CdsBulk.class, "V_CDS_VALIDO");
		SQLBuilder sql = cdsHome.createSQLBuilder();
		sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) context).getEsercizio());
		return cdsHome.fetchAll(sql);
	}
}
