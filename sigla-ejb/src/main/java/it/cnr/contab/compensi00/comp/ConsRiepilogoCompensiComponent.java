package it.cnr.contab.compensi00.comp;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.V_struttura_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.V_struttura_organizzativaHome;
import it.cnr.contab.docamm00.consultazioni.bulk.VConsRiepCompensiBulk;
import it.cnr.contab.docamm00.consultazioni.bulk.VConsRiepCompensiHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.rmi.RemoteException;



/**
 * Insert the type's description here.
 * Creation date: (13/09/2006)
 * @author: Flavia Giardina
 */
public class ConsRiepilogoCompensiComponent extends CRUDComponent{
	/**
	 * VarBilancioComponent constructor comment.
	 */

	//private String tabAlias;

	public ConsRiepilogoCompensiComponent() {
		super();
	}

	public it.cnr.jada.persistency.sql.SQLBuilder selectFiltroSoggettoByClause(
			UserContext aUC,
			VConsRiepCompensiBulk incarichi, 
			TerzoBulk cliente,
			CompoundFindClause clauses) 
					throws ComponentException {
		TerzoHome home = (TerzoHome) getHome(aUC, cliente);
		SQLBuilder sql = home.createSQLBuilder();

		sql.setAutoJoins(true);

		sql.addClause(clauses);
		return sql;
	}

	public SQLBuilder selectUoForPrintByClause (CNRUserContext userContext, 
			VConsRiepCompensiBulk incarichi, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo, CompoundFindClause clause) throws ComponentException, PersistencyException
	{	
		Unita_organizzativaBulk uoScrivania = ((Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext))));
		boolean isUoEnte = uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0;
		boolean isUoSac  = uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC)==0;

		SQLBuilder sqlStruttura = getHome(userContext, V_struttura_organizzativaBulk.class).createSQLBuilder();
		sqlStruttura.addClause("AND", "esercizio", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
		if (!isUoEnte){
			sqlStruttura.addClause( "AND", "cd_cds", SQLBuilder.EQUALS, userContext.getCd_cds());
		}
		if (isUoSac){
			sqlStruttura.addClause(FindClause.AND, "cdUnitaOrganizzativa", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
		}

		sqlStruttura.addClause( "AND", "cd_tipo_livello", SQLBuilder.EQUALS, V_struttura_organizzativaHome.LIVELLO_UO);
		sqlStruttura.addSQLJoin( "V_STRUTTURA_ORGANIZZATIVA.CD_ROOT", "UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA");

		SQLBuilder sql = getHome(userContext, uo.getClass()).createSQLBuilder();
		sql.addSQLExistsClause("AND", sqlStruttura);
		sql.addClause( clause );
		return sql;
	}

	public VConsRiepCompensiBulk impostaDatiIniziali(
			it.cnr.jada.UserContext userContext,
			VConsRiepCompensiBulk incarichi)
					throws it.cnr.jada.comp.ComponentException {

		incarichi.setUoForPrint(new Unita_organizzativaBulk());
		incarichi.setUOForPrintEnabled(true);
		incarichi.setFiltroSoggetto(new TerzoBulk());
		return incarichi;
	}

	private void addColumn(SQLBuilder sql, String valore, boolean addColumn){
		if (addColumn)
			sql.addColumn(valore);
	}

	private void addSQLGroupBy(SQLBuilder sql, String valore, boolean addGroupBy){
		if (addGroupBy)
			sql.addSQLGroupBy(valore);
	}

	public it.cnr.jada.util.RemoteIterator findRiepilogoCompensi(UserContext userContext, VConsRiepCompensiBulk riepilogCompensi) throws PersistencyException, IntrospectionException, ComponentException, RemoteException
	{
		VConsRiepCompensiHome home = (VConsRiepCompensiHome)getHome(userContext, VConsRiepCompensiBulk.class, "CONSULTAZIONE");
		SQLBuilder sql = home.createSQLBuilder();
		sql.resetColumns();

		addColumn(sql,"CD_UNITA_ORGANIZZATIVA",true);
		addColumn(sql,"DS_UNITA_ORGANIZZATIVA",true);
		addColumn(sql,"CD_TERZO",true);
		addColumn(sql,"COGNOME",true);
		addColumn(sql,"NOME",true);
		addColumn(sql,"CODICE_FISCALE",true);

		addSQLGroupBy(sql,"CD_UNITA_ORGANIZZATIVA",true);
		addSQLGroupBy(sql,"DS_UNITA_ORGANIZZATIVA",true);
		addSQLGroupBy(sql,"COGNOME",true);
		addSQLGroupBy(sql,"NOME",true);
		addSQLGroupBy(sql,"CODICE_FISCALE",true);
		addSQLGroupBy(sql,"CD_TERZO",true);

		sql.addColumn("NVL(SUM(IM_LORDO),0)", "IM_LORDO");
		sql.addColumn("NVL(SUM(IRAP_ENTE),0)", "IRAP_ENTE");
		sql.addColumn("NVL(SUM(INPS_ENTE),0)", "INPS_ENTE");
		sql.addColumn("NVL(SUM(INAIL_ENTE),0)", "INAIL_ENTE");
		sql.addColumn("NVL(SUM(IM_FISCALE),0)", "IM_FISCALE");
		sql.addColumn("NVL(SUM(IRPEF),0)", "IRPEF");
		sql.addColumn("NVL(SUM(BONUSDL66),0)", "BONUSDL66");
		sql.addColumn("NVL(SUM(INPS_PERCIPIENTE),0)", "INPS_PERCIPIENTE");
		sql.addColumn("NVL(SUM(INAIL_PERCIPIENTE),0)", "INAIL_PERCIPIENTE");
		sql.addColumn("NVL(SUM(ADD_REG),0)", "ADD_REG");
		sql.addColumn("NVL(SUM(ADD_COM),0)", "ADD_COM");
		sql.addColumn("NVL(SUM(IM_LORDO),0) + NVL(SUM(IRAP_ENTE),0) + NVL(SUM(INPS_ENTE),0) + NVL(SUM(INAIL_ENTE),0)", "TOT_COSTO");

		sql.addOrderBy("CD_UNITA_ORGANIZZATIVA");
		sql.addOrderBy("COGNOME");
		sql.addOrderBy("NOME");

		if (riepilogCompensi.getFiltroSoggetto() != null && riepilogCompensi.getFiltroSoggetto().getCd_terzo() != null){
			sql.addSQLClause("AND","CD_TERZO",SQLBuilder.EQUALS,riepilogCompensi.getFiltroSoggetto().getCd_terzo());
		}

		if (riepilogCompensi.getUoForPrint() != null){
			sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,riepilogCompensi.getUoForPrint().getCd_unita_organizzativa());
		} else {
			Unita_organizzativaBulk uoScrivania = ((Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext))));
			boolean isUoEnte = uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0;
			boolean isUoSac  = uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC)==0;

			if (!isUoEnte){
				sql.addClause( "AND", "cd_cds", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
			}
			if (isUoSac){
				sql.addClause(FindClause.AND, "cdUnitaOrganizzativa", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
			}
		}

		if (riepilogCompensi.getDa_dt_pagamento() != null){
			sql.addSQLClause("AND","DT_TRASMISSIONE",SQLBuilder.GREATER_EQUALS,riepilogCompensi.getDa_dt_pagamento());
		}
		if (riepilogCompensi.getA_dt_pagamento() != null){
			sql.addSQLClause("AND","DT_TRASMISSIONE",SQLBuilder.LESS_EQUALS,riepilogCompensi.getA_dt_pagamento());
		}
		if (riepilogCompensi.getDa_dt_competenza() != null && riepilogCompensi.getA_dt_competenza() != null){
			sql.openParenthesis(FindClause.AND);

			sql.openParenthesis(FindClause.OR);
			sql.addSQLClause("AND","DT_DA_COMPETENZA_COGE",SQLBuilder.GREATER_EQUALS,riepilogCompensi.getDa_dt_competenza());
			sql.addSQLClause("AND","DT_DA_COMPETENZA_COGE",SQLBuilder.LESS_EQUALS,riepilogCompensi.getA_dt_competenza());
			sql.closeParenthesis();

			sql.openParenthesis(FindClause.OR);
			sql.addSQLClause("AND","DT_A_COMPETENZA_COGE",SQLBuilder.GREATER_EQUALS,riepilogCompensi.getDa_dt_competenza());
			sql.addSQLClause("AND","DT_A_COMPETENZA_COGE",SQLBuilder.LESS_EQUALS,riepilogCompensi.getA_dt_competenza());
			sql.closeParenthesis();

			sql.closeParenthesis();
		}

		return  iterator(userContext,sql,VConsRiepCompensiBulk.class,null);

	}
}
