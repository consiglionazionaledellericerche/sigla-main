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

package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.contab.config00.bulk.*;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.consultazioni.bulk.ConsultazioniRestHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.*;

import java.util.Enumeration;
import java.util.Optional;

public class ProgettoContrattoHome extends BulkHome implements ConsultazioniRestHome {
    public ProgettoContrattoHome(java.sql.Connection conn) {
        super(ProgettoContrattoBulk.class, conn);
    }

    public ProgettoContrattoHome(java.sql.Connection conn, PersistentCache persistentCache) {
        super(ProgettoContrattoBulk.class, conn, persistentCache);
    }

    protected ProgettoContrattoHome(Class class1, java.sql.Connection connection, PersistentCache persistentcache) {
        super(class1, connection, persistentcache);
    }

    @Override
    public SQLBuilder restSelect(UserContext userContext, SQLBuilder sql, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
        if (isUoEnte(userContext)) {
            if (compoundfindclause != null && compoundfindclause.getClauses() != null) {
                Boolean trovataCondizioneUo = false;
                CompoundFindClause newClauses = new CompoundFindClause();
                Enumeration e = compoundfindclause.getClauses();
                SQLBuilder sqlExists = null;
                while (e.hasMoreElements()) {
                    FindClause findClause = (FindClause) e.nextElement();
                    if (findClause instanceof SimpleFindClause) {
                        SimpleFindClause clause = (SimpleFindClause) findClause;
                        int operator = clause.getOperator();
                        if (clause.getPropertyName() != null && clause.getPropertyName().equals("cd_unita_organizzativa") &&
                                operator == SQLBuilder.EQUALS) {
                            trovataCondizioneUo = true;

                            Unita_organizzativaBulk uo = (Unita_organizzativaBulk) getHomeCache().getHome(Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk((String) clause.getValue()));

                            ProgettoHome progettoHome = (ProgettoHome) getHomeCache().getHome(ProgettoBulk.class);
                            sqlExists = progettoHome.createSQLBuilder();
                            sqlExists.addTableToHeader("V_ABIL_PROGETTI");

                            sqlExists.addTableToHeader("UNITA_ORGANIZZATIVA");
                            sqlExists.addSQLJoin("UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA", "V_ABIL_PROGETTI.CD_UNITA_ORGANIZZATIVA");
                            sqlExists.openParenthesis("AND");

                            Parametri_enteBulk parEnte = ((Parametri_enteHome) getHomeCache().getHome(Parametri_enteBulk.class)).getParametriEnteAttiva();
                            if (parEnte.isAbilProgettoUO())
                                sqlExists.addSQLClause("AND", "UNITA_ORGANIZZATIVA.CD_UNITA_PADRE", SQLBuilder.EQUALS, uo.getCd_unita_organizzativa());
                            else
                                sqlExists.addSQLClause("AND", "UNITA_ORGANIZZATIVA.CD_UNITA_PADRE", SQLBuilder.EQUALS, uo.getUnita_padre().getCd_unita_organizzativa());

                            if (uo.getCd_tipo_unita().compareTo(Tipo_unita_organizzativaHome.TIPO_UO_AREA) == 0) {
                                PersistentHome parCNRHome = getHomeCache().getHome(Parametri_cnrBulk.class);
                                Parametri_cnrBulk parCNR = (Parametri_cnrBulk) parCNRHome.findByPrimaryKey(new Parametri_cnrBulk(CNRUserContext.getEsercizio(userContext)));
                                if (!parCNR.getFl_nuovo_pdg()) {
                                    SQLBuilder sqlArea = getHomeCache().getHome(Ass_uo_areaBulk.class).createSQLBuilder();
                                    sqlArea.addTableToHeader("UNITA_ORGANIZZATIVA UO");
                                    sqlArea.addSQLJoin("UNITA_ORGANIZZATIVA.CD_UNITA_PADRE", "UO.CD_UNITA_PADRE");
                                    sqlArea.addSQLJoin("ASS_UO_AREA.CD_UNITA_ORGANIZZATIVA", "UO.CD_UNITA_ORGANIZZATIVA");
                                    sqlArea.addSQLClause("AND", "ASS_UO_AREA.CD_AREA_RICERCA", SQLBuilder.EQUALS, uo.getUnita_padre().getCd_unita_organizzativa());
                                    sqlArea.addSQLClause("AND", "ASS_UO_AREA.ESERCIZIO", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
                                    sqlExists.addSQLExistsClause("OR", sqlArea);
                                }
                            }
                            sqlExists.closeParenthesis();

                            sqlExists.addSQLJoin("PROGETTO.ESERCIZIO", "PROGETTO_GEST.ESERCIZIO");
                            sqlExists.addSQLJoin("PROGETTO.TIPO_FASE", "PROGETTO_GEST.TIPO_FASE");
                            sqlExists.addSQLJoin("PROGETTO.PG_PROGETTO", "PROGETTO_GEST.PG_PROGETTO");
                            sqlExists.addSQLJoin("PROGETTO_GEST.ESERCIZIO", "V_ABIL_PROGETTI.ESERCIZIO_COMMESSA");
                            sqlExists.addSQLJoin("PROGETTO_GEST.TIPO_FASE", "V_ABIL_PROGETTI.TIPO_FASE_COMMESSA");
                            sqlExists.addSQLJoin("PROGETTO_GEST.PG_PROGETTO", "V_ABIL_PROGETTI.PG_COMMESSA");
                        } else {
                            newClauses.addClause(clause.getLogicalOperator(), clause.getPropertyName(), clause.getOperator(), clause.getValue());
                        }
                    }
                }
                if (trovataCondizioneUo) {
                    sql = selectByClause(userContext, newClauses);
                    sql.addSQLExistsClause("AND", sqlExists);
                }
            }
        } else {
            ProgettoContrattoHome progettohome = (ProgettoContrattoHome) getHomeCache().getHome(ProgettoContrattoBulk.class, "CUP", "restService");
            sql = progettohome.createSQLBuilder();
            sql.setDistinctClause(true);
            sql.addTableToHeader("CONTRATTO");
            sql.addSQLJoin("V_PROGETTO_PADRE.PG_PROGETTO", "CONTRATTO.PG_PROGETTO(+)");
            sql.addSQLClause(FindClause.AND, "CONTRATTO.CD_CUP(+)", SQLBuilder.ISNOTNULL, null);
            sql.addClause(compoundfindclause);
            sql.addSQLClause(FindClause.AND, "V_PROGETTO_PADRE.ESERCIZIO", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
            sql.addClause(FindClause.AND, "tipo_fase", SQLBuilder.EQUALS, ProgettoBulk.TIPO_FASE_NON_DEFINITA);

            // Se uo 999.000 in scrivania: visualizza tutti i progetti
            if (!isUoEnte(userContext)) {
                try {
                    sql.addSQLExistsClause("AND", progettohome.abilitazioniCommesse(userContext));
                } catch (Exception e) {
                    throw new ComponentException(e);
                }
            }
        }
        return sql;
    }

    public SQLBuilder abilitazioniCommesse(it.cnr.jada.UserContext aUC) throws PersistencyException {
        SQLBuilder sql = abilitazioni(aUC);
        sql.addSQLJoin("V_ABIL_PROGETTI.ESERCIZIO_COMMESSA", "V_PROGETTO_PADRE.ESERCIZIO");
        sql.addSQLJoin("V_ABIL_PROGETTI.PG_COMMESSA", "V_PROGETTO_PADRE.PG_PROGETTO");
        sql.addSQLJoin("V_ABIL_PROGETTI.TIPO_FASE_COMMESSA", "V_PROGETTO_PADRE.TIPO_FASE");
        return sql;
    }

    public SQLBuilder abilitazioniModuli(it.cnr.jada.UserContext aUC) throws PersistencyException {
        SQLBuilder sql = abilitazioni(aUC);
        sql.addSQLJoin("V_ABIL_PROGETTI.ESERCIZIO_MODULO", "V_PROGETTO_PADRE.ESERCIZIO");
        sql.addSQLJoin("V_ABIL_PROGETTI.PG_MODULO", "V_PROGETTO_PADRE.PG_PROGETTO");
        sql.addSQLJoin("V_ABIL_PROGETTI.TIPO_FASE_MODULO", "V_PROGETTO_PADRE.TIPO_FASE");
        return sql;
    }

    public SQLBuilder abilitazioni(it.cnr.jada.UserContext aUC, String campo) throws PersistencyException {
        SQLBuilder sql = abilitazioni(aUC);
        sql.addSQLJoin("V_ABIL_PROGETTI.PG_MODULO", campo);
        return sql;
    }

    private SQLBuilder abilitazioni(it.cnr.jada.UserContext aUC) throws PersistencyException {
        Unita_organizzativaBulk uo = (Unita_organizzativaBulk) getHomeCache().getHome(Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(aUC)));
        ProgettoGestHome progettohome = (ProgettoGestHome) getHomeCache().getHome(ProgettoGestBulk.class, "V_ABIL_PROGETTI");
        SQLBuilder sql = progettohome.createSQLBuilder();
        sql.addTableToHeader("UNITA_ORGANIZZATIVA");
        sql.addSQLJoin("UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA", "V_ABIL_PROGETTI.CD_UNITA_ORGANIZZATIVA");
        sql.openParenthesis("AND");

        Parametri_enteBulk parEnte = ((Parametri_enteHome) getHomeCache().getHome(Parametri_enteBulk.class)).getParametriEnteAttiva();
        boolean abilProgettoUO = parEnte.isAbilProgettoUO();
        Optional<String> abilProgetti = ((Parametri_cdsHome) getHomeCache().getHome(Parametri_cdsBulk.class)).getAbilProgetti(aUC, CNRUserContext.getCd_cds(aUC));
        if (abilProgetti.isPresent()) {
            abilProgettoUO = abilProgetti.get().equalsIgnoreCase(V_struttura_organizzativaHome.LIVELLO_UO);
        }
        if (abilProgettoUO)
            sql.addSQLClause(FindClause.AND, "V_ABIL_PROGETTI.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(aUC));
        else
            sql.addSQLClause("AND", "UNITA_ORGANIZZATIVA.CD_UNITA_PADRE", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(aUC));

        if (uo.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_AREA) == 0) {
            PersistentHome parCNRHome = getHomeCache().getHome(Parametri_cnrBulk.class);
            Parametri_cnrBulk parCNR = (Parametri_cnrBulk) parCNRHome.findByPrimaryKey(new Parametri_cnrBulk(CNRUserContext.getEsercizio(aUC)));
            if (!parCNR.getFl_nuovo_pdg()) {
                SQLBuilder sqlArea = getHomeCache().getHome(Ass_uo_areaBulk.class).createSQLBuilder();
                sqlArea.addTableToHeader("UNITA_ORGANIZZATIVA UO");
                sqlArea.addSQLJoin("UNITA_ORGANIZZATIVA.CD_UNITA_PADRE", "UO.CD_UNITA_PADRE");
                sqlArea.addSQLJoin("ASS_UO_AREA.CD_UNITA_ORGANIZZATIVA", "UO.CD_UNITA_ORGANIZZATIVA");
                sqlArea.addSQLClause("AND", "ASS_UO_AREA.CD_AREA_RICERCA", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(aUC));
                sqlArea.addSQLClause("AND", "ASS_UO_AREA.ESERCIZIO", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(aUC));
                sql.addSQLExistsClause("OR", sqlArea);
            }
        }
        sql.closeParenthesis();
        return sql;
    }

    private Boolean isUoEnte(UserContext userContext) throws PersistencyException, ComponentException {
        Unita_organizzativa_enteBulk uoEnte = getUoEnte(userContext);
		return ((CNRUserContext) userContext).getCd_unita_organizzativa().equals(uoEnte.getCd_unita_organizzativa());
	}

    private Unita_organizzativa_enteBulk getUoEnte(UserContext userContext)
            throws PersistencyException, ComponentException {
        Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
        return uoEnte;
    }
}