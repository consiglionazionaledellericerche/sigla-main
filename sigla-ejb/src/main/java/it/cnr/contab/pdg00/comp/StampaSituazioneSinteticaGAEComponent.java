package it.cnr.contab.pdg00.comp;

import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageHome;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdrHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.pdg00.bulk.V_stm_paramin_sit_sint_gaeBulk;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;


public class StampaSituazioneSinteticaGAEComponent extends it.cnr.jada.comp.CRUDComponent implements it.cnr.jada.comp.ICRUDMgr, Cloneable, Serializable {

    public static final int LV_AC = 0; // Amministazione centrale
    public static final int LV_CDRI = 1; // CDR I 00
    public static final int LV_RUO = 2; // CDR II responsabile
    public static final int LV_NRUO = 3; // CDR II non responsabile
    private static final java.math.BigDecimal ZERO = new java.math.BigDecimal(0);


    public StampaSituazioneSinteticaGAEComponent() {
        super();
    }


    public void inserisciRecord(UserContext userContext,
                                java.math.BigDecimal pg_stampa,
                                java.util.List filtro)
            throws ComponentException, java.rmi.RemoteException {
        try {
            BigDecimal currentSequence = Utility.ZERO;
            WorkpackageHome home = (WorkpackageHome) getHome(userContext, WorkpackageBulk.class);
//				SQLBuilder query = home.createSQLBuilder();
//				query.addClause(filtro.getSqlClauses());
            for (Iterator i = filtro.iterator(); i.hasNext(); ) {
                WorkpackageBulk gae = (WorkpackageBulk) i.next();
                V_stm_paramin_sit_sint_gaeBulk paramStampa = new V_stm_paramin_sit_sint_gaeBulk(gae);
                paramStampa.setUser(CNRUserContext.getUser(userContext));
                paramStampa.setEsercizio(new BigDecimal(CNRUserContext.getEsercizio(userContext)));
                paramStampa.setId_report(pg_stampa);
                paramStampa.setChiave(pg_stampa.toString());
                currentSequence = currentSequence.add(new java.math.BigDecimal(1));
                paramStampa.setSequenza(currentSequence);
                paramStampa.setTipo("A");
                paramStampa.setToBeCreated();
                insertBulk(userContext, paramStampa);
            }
        } catch (Throwable t) {
            throw handleException(t);
        }
    }


    public java.math.BigDecimal getPgStampa(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException {

        //ricavo il progressivo unico pg_stampa
        return getSequence(userContext);
    }
//	^^@@

    /**
     * Identificativo univoco progressivo per la gestione dell' IVA
     * PreCondition:
     * Viene richiesta un progressivo
     * PostCondition:
     * ritorna un valore
     * PreCondition:
     * Si è verificato un errore.
     * PostCondition:
     * Viene inviato un messaggio con il relativo errore ritornato dal DB
     */
//	^^@@
    private java.math.BigDecimal getSequence(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException {

        //ricavo il progressivo unico pg_stampa
        java.math.BigDecimal pg_Stampa = new java.math.BigDecimal(0);
        try {
            LoggableStatement ps = new LoggableStatement(getConnection(userContext),
                    "select IBMSEQ00_STAMPA.nextval from dual", true, this.getClass());
            try {
                java.sql.ResultSet rs = ps.executeQuery();
                try {
                    if (rs.next())
                        pg_Stampa = rs.getBigDecimal(1);
                } finally {
                    try {
                        rs.close();
                    } catch (java.sql.SQLException e) {
                    }
                }
            } catch (java.sql.SQLException e) {
                throw handleException(e);
            } finally {
                try {
                    ps.close();
                } catch (java.sql.SQLException e) {
                }
            }
        } catch (java.sql.SQLException e) {
            throw handleException(e);
        }
        return pg_Stampa;

    }


    protected CdrBulk cdrFromUserContext(UserContext userContext) throws ComponentException {
        try {
            it.cnr.contab.utenze00.bulk.UtenteBulk user = new it.cnr.contab.utenze00.bulk.UtenteBulk(userContext.getUser());
            user = (it.cnr.contab.utenze00.bulk.UtenteBulk) getHome(userContext, user).findByPrimaryKey(user);

            CdrBulk cdr = new CdrBulk(it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cdr(userContext));
            return (CdrBulk) getHome(userContext, cdr).findByPrimaryKey(cdr);

        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw new ComponentException(e);
        }
    }

    protected Unita_organizzativaBulk uoFromUserContext(UserContext userContext) throws ComponentException {
        try {
            it.cnr.contab.utenze00.bulk.UtenteBulk user = new it.cnr.contab.utenze00.bulk.UtenteBulk(userContext.getUser());
            user = (it.cnr.contab.utenze00.bulk.UtenteBulk) getHome(userContext, user).findByPrimaryKey(user);

            Unita_organizzativaBulk uo = new Unita_organizzativaBulk(it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext));
            return (Unita_organizzativaBulk) getHome(userContext, uo).findByPrimaryKey(uo);

        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw new ComponentException(e);
        }
    }

    public RemoteIterator selezionaGae(UserContext userContext, CompoundFindClause clause) throws ComponentException, PersistencyException, RemoteException {
        WorkpackageHome gaeHome = (WorkpackageHome) getHome(userContext, WorkpackageBulk.class, "V_LINEA_ATTIVITA_VALIDA");
        SQLBuilder sql = gaeHome.createSQLBuilder();

        if (!isUtenteEnte(userContext)) {
            CdrBulk cdrUtente = cdrFromUserContext(userContext);
            String uoScrivania = CNRUserContext.getCd_unita_organizzativa(userContext);

            sql.addSQLClause(FindClause.AND, "V_LINEA_ATTIVITA_VALIDA.ESERCIZIO", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));

            sql.addTableToHeader("V_CDR_VALIDO");
            sql.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.ESERCIZIO", "V_CDR_VALIDO.ESERCIZIO");
            sql.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA", "V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA");
            sql.openParenthesis(FindClause.AND);
            sql.addSQLClause(FindClause.AND, "V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA", SQLBuilder.EQUALS, cdrUtente.getCd_centro_responsabilita());

            if (cdrUtente.getLivello().compareTo(CdrHome.CDR_PRIMO_LIVELLO) == 0)
                sql.addSQLClause(FindClause.OR, "V_CDR_VALIDO.CD_CDR_AFFERENZA", SQLBuilder.EQUALS, cdrUtente.getCd_centro_responsabilita());
            else
                sql.addSQLClause(FindClause.OR, "V_CDR_VALIDO.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, uoScrivania);

            sql.closeParenthesis();
        }

        Voce_f_saldi_cdr_lineaHome saldiHome = (Voce_f_saldi_cdr_lineaHome) getHome(userContext, Voce_f_saldi_cdr_lineaBulk.class);
        SQLBuilder sqlSaldi = saldiHome.createSQLBuilder();
        sqlSaldi.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.ESERCIZIO", "VOCE_F_SALDI_CDR_LINEA.ESERCIZIO");
        sqlSaldi.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA", "VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA");
        sqlSaldi.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA", "VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA");
        sql.addSQLExistsClause(FindClause.AND, sqlSaldi);
        sql.addClause(clause);
        return iterator(userContext, sql, WorkpackageBulk.class, getFetchPolicyName("find"));
    }

    private boolean isCdrEnte(UserContext userContext, CdrBulk cdr) throws ComponentException {
        try {
            getHome(userContext, cdr.getUnita_padre()).findByPrimaryKey(cdr.getUnita_padre());
            return cdr.isCdrAC();
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    private boolean isUtenteEnte(UserContext userContext) throws ComponentException {
        return isCdrEnte(userContext, cdrFromUserContext(userContext));
    }
}	
	

		


