package it.cnr.contab.incarichi00.comp;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoHome;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.CdsHome;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.V_struttura_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.V_struttura_organizzativaHome;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.bulk.VIncarichiAssRicBorseStBulk;
import it.cnr.contab.incarichi00.bulk.VIncarichiAssRicBorseStHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.DateUtils;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;



/**
 * Insert the type's description here.
 * Creation date: (13/09/2006)
 * @author: Flavia Giardina
 */
public class ConsIncarAssRicBorseStComponent extends CRUDComponent{
/**
 * VarBilancioComponent constructor comment.
 */
	
//private String tabAlias;

public ConsIncarAssRicBorseStComponent() {
	super();
}

public it.cnr.jada.persistency.sql.SQLBuilder selectFiltroSoggettoByClause(
		UserContext aUC,
		VIncarichiAssRicBorseStBulk incarichi, 
		TerzoBulk cliente,
		CompoundFindClause clauses) 
				throws ComponentException {
	try
	{
		TerzoHome home = (TerzoHome)getHome(aUC,TerzoBulk.class);
		return home.selectTerzoPerCompensi(incarichi.getFiltroSoggetto() != null ? incarichi.getFiltroSoggetto().getCd_terzo() : null, clauses);
	}
	catch(it.cnr.jada.persistency.PersistencyException ex){throw handleException(ex);}
}

public SQLBuilder selectUoForPrintByClause (CNRUserContext userContext, 
		VIncarichiAssRicBorseStBulk incarichi, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo, CompoundFindClause clause) throws ComponentException, PersistencyException
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

public VIncarichiAssRicBorseStBulk impostaDatiIniziali(
		it.cnr.jada.UserContext userContext,
		VIncarichiAssRicBorseStBulk incarichi)
		throws it.cnr.jada.comp.ComponentException {
	try {
		CdsHome cds_home = (CdsHome) getHome(userContext, CdsBulk.class);
		CdsBulk cds_scrivania = (CdsBulk) cds_home
				.findByPrimaryKey(new CdsBulk(CNRUserContext
						.getCd_cds(userContext)));

		if (cds_scrivania.getCd_tipo_unita().equals(
				Tipo_unita_organizzativaHome.TIPO_UO_ENTE)) {
			incarichi.setUoForPrint(new Unita_organizzativaBulk());
			incarichi.setUOForPrintEnabled(true);
		} else {
			Unita_organizzativaHome uoHome = (Unita_organizzativaHome) getHome(
					userContext, Unita_organizzativaBulk.class);
			Unita_organizzativaBulk uo = (Unita_organizzativaBulk) uoHome
					.findByPrimaryKey(new Unita_organizzativaBulk(
							CNRUserContext
									.getCd_unita_organizzativa(userContext)));

			if (!uo.isUoCds()) {
				incarichi.setUoForPrint(uo);
				incarichi.setUOForPrintEnabled(false);
			} else {
				incarichi
						.setUoForPrint(new Unita_organizzativaBulk());
				incarichi.setUOForPrintEnabled(true);
			}
		}

	} catch (it.cnr.jada.persistency.PersistencyException pe) {
		throw new ComponentException(pe);
	}
	return incarichi;
}

public it.cnr.jada.util.RemoteIterator findIncarichi(UserContext userContext, VIncarichiAssRicBorseStBulk incarichi) throws PersistencyException, IntrospectionException, ComponentException, RemoteException
{
	VIncarichiAssRicBorseStHome incarichiHome = (VIncarichiAssRicBorseStHome)getHome(userContext, VIncarichiAssRicBorseStBulk.class);
	SQLBuilder sql = incarichiHome.createSQLBuilder();
	
  	Unita_organizzativaBulk uoScrivania = ((Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext))));
  		
	SQLBuilder sqlCdsExists = getHome(userContext, Incarichi_repertorioBulk.class).createSQLBuilder();
	sqlCdsExists.resetColumns();
	sqlCdsExists.addColumn("1");
	sqlCdsExists.addSQLJoin("INCARICHI_REPERTORIO.ESERCIZIO_PROCEDURA", "V_INCARICHI_ASS_RIC_BORSE_ST.ESERCIZIO_PROCEDURA");
	sqlCdsExists.addSQLJoin("INCARICHI_REPERTORIO.PG_PROCEDURA", "V_INCARICHI_ASS_RIC_BORSE_ST.PG_PROCEDURA");

	sqlCdsExists.addTableToHeader("ASS_INCARICO_UO");
	sqlCdsExists.addSQLJoin("ASS_INCARICO_UO.ESERCIZIO","INCARICHI_REPERTORIO.ESERCIZIO");
	sqlCdsExists.addSQLJoin("ASS_INCARICO_UO.PG_REPERTORIO","INCARICHI_REPERTORIO.PG_REPERTORIO");

	if (incarichi.getUoForPrint() != null && incarichi.getUoForPrint().getCd_unita_organizzativa() != null){
  		Unita_organizzativaBulk uoFiltro = incarichi.getUoForPrint();
  		sqlCdsExists.addSQLClause(FindClause.AND, "ASS_INCARICO_UO.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, uoFiltro.getCd_unita_organizzativa());
			sql.openParenthesis(FindClause.AND);
				sql.addClause(FindClause.OR, "cdUnitaOrganizzativa", SQLBuilder.EQUALS, uoFiltro.getCd_unita_organizzativa());
				sql.addSQLExistsClause(FindClause.OR,sqlCdsExists);
			sql.closeParenthesis();
	} else {
		boolean isUoEnte = uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0;
		boolean isUoSac  = uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC)==0;
		

		if (isUoSac)
			sqlCdsExists.addSQLClause(FindClause.AND, "ASS_INCARICO_UO.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
		else {
			sqlCdsExists.addTableToHeader("V_STRUTTURA_ORGANIZZATIVA");
			sqlCdsExists.addSQLClause(FindClause.AND, "V_STRUTTURA_ORGANIZZATIVA.ESERCIZIO", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
			sqlCdsExists.addSQLJoin("V_STRUTTURA_ORGANIZZATIVA.CD_ROOT","ASS_INCARICO_UO.CD_UNITA_ORGANIZZATIVA");
			sqlCdsExists.addSQLClause(FindClause.AND, "V_STRUTTURA_ORGANIZZATIVA.CD_CDS", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
		}

		if (!isUoEnte) {
			sql.openParenthesis(FindClause.AND);
			sql.openParenthesis(FindClause.OR);
			sql.addClause(FindClause.AND, "cdCds", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
			if (isUoSac)
				sql.addClause(FindClause.AND, "cdUnitaOrganizzativa", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
			sql.closeParenthesis();
			sql.addSQLExistsClause(FindClause.OR,sqlCdsExists);
			sql.closeParenthesis();
		} else if (isUoSac) {
			sql.openParenthesis(FindClause.AND);
				sql.addClause(FindClause.OR, "cdUnitaOrganizzativa", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
				sql.addSQLExistsClause(FindClause.OR,sqlCdsExists);
			sql.closeParenthesis();
		}
		
	}
	
	sql.addOrderBy("ESERCIZIO");
	sql.addOrderBy("PG_REPERTORIO");
	
//	sql.setDistinctClause(true);
	if (!incarichi.isSceltaFontiTutte()){
		sql.addSQLClause("AND","TIPO_NATURA",SQLBuilder.EQUALS,incarichi.getFonti());
	}  

	filtroStati(incarichi, sql);
	filtroTipologia(incarichi, sql);

	if (incarichi.getFiltroSoggetto() != null && incarichi.getFiltroSoggetto().getCd_terzo() != null){
		sql.addSQLClause("AND","CD_TERZO",SQLBuilder.EQUALS,incarichi.getFiltroSoggetto().getCd_terzo());
	} 

	if (incarichi.getEsercizioValidita() != null){

    	GregorianCalendar gc = (GregorianCalendar) GregorianCalendar.getInstance();
    	gc.setTime(new GregorianCalendar(incarichi.getEsercizioValidita(),Calendar.DECEMBER,31).getTime());
		Date ultimoGiornoAnno = new Timestamp(gc.getTimeInMillis());
		
		sql.openParenthesis(FindClause.AND);
		
		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "dtProroga", SQLBuilder.ISNULL, null);
		sql.addClause(FindClause.AND, "dtFineValidita", SQLBuilder.ISNULL, null);
		sql.addClause(FindClause.AND, "dtInizioValidita", SQLBuilder.LESS_EQUALS, ultimoGiornoAnno);
		sql.closeParenthesis();
		
		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "dtProroga", SQLBuilder.ISNULL, null);
		sql.addClause(FindClause.AND, "dtFineValidita", SQLBuilder.ISNOTNULL, null);
		sql.addClause(FindClause.AND, "dtFineValidita", SQLBuilder.GREATER_EQUALS, DateUtils.firstDateOfTheYear(incarichi.getEsercizioValidita()));
		sql.addClause(FindClause.AND, "dtInizioValidita", SQLBuilder.LESS_EQUALS, ultimoGiornoAnno);
		sql.closeParenthesis();

		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "dtProroga", SQLBuilder.ISNOTNULL, null);
		sql.addClause(FindClause.AND, "dtProroga", SQLBuilder.GREATER_EQUALS, DateUtils.firstDateOfTheYear(incarichi.getEsercizioValidita()));
		sql.addClause(FindClause.AND, "dtInizioValidita", SQLBuilder.LESS_EQUALS, ultimoGiornoAnno);
		sql.closeParenthesis();
		sql.closeParenthesis();
	}
	
	if (incarichi.getDt_validita() != null){
		sql.addSQLClause("AND","DT_INIZIO_VALIDITA",SQLBuilder.LESS_EQUALS,incarichi.getDt_validita());
		sql.openParenthesis(FindClause.AND);

		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "dtProroga", SQLBuilder.ISNULL, null);
		sql.addClause(FindClause.AND, "dtFineValidita", SQLBuilder.ISNULL, null);
		sql.closeParenthesis();
		
		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "dtProroga", SQLBuilder.ISNULL, null);
		sql.addClause(FindClause.AND, "dtFineValidita", SQLBuilder.ISNOTNULL, null);
		sql.addClause(FindClause.AND, "dtFineValidita", SQLBuilder.GREATER_EQUALS, incarichi.getDt_validita());
		sql.closeParenthesis();

		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "dtProroga", SQLBuilder.ISNOTNULL, null);
		sql.addClause(FindClause.AND, "dtProroga", SQLBuilder.GREATER_EQUALS, incarichi.getDt_validita());
		sql.closeParenthesis();

		sql.closeParenthesis();
	}
	return  iterator(userContext,sql,VIncarichiAssRicBorseStBulk.class,null);
}

private void filtroStati(VIncarichiAssRicBorseStBulk incarichi, SQLBuilder sql) {
	sql.openParenthesis(FindClause.AND);
	if (incarichi.getStatoAnnullatoEliminato()){
		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "stato", SQLBuilder.EQUALS, Incarichi_repertorioBulk.STATO_ANNULLATO);
		sql.closeParenthesis();
		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "stato", SQLBuilder.EQUALS, Incarichi_repertorioBulk.STATO_ELIMINATO);
		sql.closeParenthesis();
	}  

	if (incarichi.getStatoChiuso()){
		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "stato", SQLBuilder.EQUALS, Incarichi_repertorioBulk.STATO_CHIUSO);
		sql.closeParenthesis();
	} 

	if (incarichi.getStatoDefinitivo()){
		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "stato", SQLBuilder.EQUALS, Incarichi_repertorioBulk.STATO_DEFINITIVO);
		sql.closeParenthesis();
	} 

	if (incarichi.getStatoInviato()){
		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "stato", SQLBuilder.EQUALS, Incarichi_repertorioBulk.STATO_INVIATO);
		sql.closeParenthesis();
	} 
	if (incarichi.getStatoProvvisorio()){
		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "stato", SQLBuilder.EQUALS, Incarichi_repertorioBulk.STATO_PROVVISORIO);
		sql.closeParenthesis();
	} 
	sql.closeParenthesis();
}
private void filtroTipologia(VIncarichiAssRicBorseStBulk incarichi, SQLBuilder sql) {
	sql.openParenthesis(FindClause.AND);
	if (incarichi.getTipologiaAssegni()){
		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "cdTipoRapporto", SQLBuilder.EQUALS, VIncarichiAssRicBorseStBulk.ASSEGNI_DI_RICERCA);
		sql.closeParenthesis();
	}  
	if (incarichi.getTipologiaBorsaDiStudio()){
		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "cdTipoRapporto", SQLBuilder.EQUALS, VIncarichiAssRicBorseStBulk.BORSA_DI_STUDIO);
		sql.closeParenthesis();
	}  
	if (incarichi.getTipologiaCococo()){
		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "cdTipoRapporto", SQLBuilder.EQUALS, VIncarichiAssRicBorseStBulk.COLLABORAZIONE_COORDINATA_CONTINUATIVA);
		sql.closeParenthesis();
	}  
	if (incarichi.getTipologiaCollOcc()){
		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "cdTipoRapporto", SQLBuilder.EQUALS, VIncarichiAssRicBorseStBulk.COLLABORAZIONE_OCCASIONALE);
		sql.closeParenthesis();
	}  
	if (incarichi.getTipologiaCollProf()){
		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "cdTipoRapporto", SQLBuilder.EQUALS, VIncarichiAssRicBorseStBulk.COLLABORAZIONE_PROFESSIONALE);
		sql.closeParenthesis();
	}  

	sql.closeParenthesis();
}
}
